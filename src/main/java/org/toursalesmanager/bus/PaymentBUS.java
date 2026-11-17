package org.toursalesmanager.bus;

import org.toursalesmanager.enums.TourPlanStatus;
import org.toursalesmanager.enums.BookingStatus;
import org.toursalesmanager.config.AppConfig;
import org.toursalesmanager.dao.PaymentDAO;
import org.toursalesmanager.dao.TourBookingDAO;
import org.toursalesmanager.dto.AccountDTO;
import org.toursalesmanager.dto.PaymentDTO;
import org.toursalesmanager.dto.TourBookingDTO;
import org.toursalesmanager.dto.TourPlanDTO;
import org.toursalesmanager.enums.Permission;
import org.toursalesmanager.enums.PaymentMethod;
import org.toursalesmanager.enums.PaymentStatus;
import org.toursalesmanager.enums.Role;
import org.toursalesmanager.login.SessionManager;
import org.toursalesmanager.service.MailService;
import org.toursalesmanager.service.VietQrService;

import java.awt.image.BufferedImage;
import java.math.BigDecimal;
import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Locale;
import java.util.Map;

public class PaymentBUS {
    public static final DateTimeFormatter DATE_TIME = DateTimeFormatter.ofPattern("HH:mm dd/MM/yyyy");
    public static final DateTimeFormatter DATE = DateTimeFormatter.ofPattern("dd/MM/yyyy");

    public record PaymentContext(
            TourBookingDTO booking,
            TourPlanDTO plan,
            String tourName,
            BigDecimal paid,
            BigDecimal remaining,
            LocalDateTime deadline
    ) {
    }

    public record QrPayment(
            PaymentDTO payment,
            BufferedImage image,
            String bankName,
            String accountNo,
            String accountName
    ) {
    }

    private final PaymentDAO paymentDAO = new PaymentDAO();
    private final TourBookingDAO tourBookingDAO = new TourBookingDAO();

    /**
     * Bước 2-3: kiểm tra phiếu có được thanh toán không. Không đủ điều kiện thì ném
     * IllegalStateException với lý do rõ ràng để giao diện hiển thị cho khách.
     */
    public PaymentContext checkEligibility(int idTourBooking) {
        AccountDTO account = requireCustomer();
        paymentDAO.expireOverdue(LocalDateTime.now());

        TourBookingDTO booking = tourBookingDAO.findById(idTourBooking);
        if (booking == null) {
            throw new IllegalStateException("Không tìm thấy phiếu đặt tour.");
        }
        if (!account.getIdCustomer().equals(booking.getIdCustomer())) {
            throw new IllegalStateException("Phiếu đặt tour này không thuộc tài khoản của bạn.");
        }

        BookingStatus status = BookingStatus.fromLabel(booking.getBookingStatus());
        if (status == BookingStatus.CANCELLED) {
            throw new IllegalStateException("Phiếu đặt tour đã bị hủy, không thể thanh toán.");
        }
        if (status == BookingStatus.EXPIRED) {
            throw new IllegalStateException("Phiếu đặt tour đã hết hạn. " + contactStaff() + " để đặt lại.");
        }
        if (status == BookingStatus.COMPLETED) {
            throw new IllegalStateException("Phiếu đặt tour đã hoàn thành.");
        }

        if (booking.getTickets() <= 0 || booking.getCostTotal() == null || booking.getCostTotal().signum() <= 0
                || booking.getIdTourPlan() == null || booking.getBookingDate() == null) {
            throw new IllegalStateException(
                    "Thông tin phiếu đặt chưa đầy đủ (số vé, tổng tiền hoặc ngày đặt). " + contactStaff() + ".");
        }

        TourPlanDTO plan = findPlan(booking.getIdTourPlan());
        if (plan == null) {
            throw new IllegalStateException("Không tìm thấy kế hoạch tour của phiếu. " + contactStaff() + ".");
        }
        TourPlanStatus planStatus = TourPlanStatus.fromLabel(plan.getStatus());
        if (planStatus == TourPlanStatus.CANCELLED) {
            throw new IllegalStateException("Kế hoạch tour đã bị hủy, không thể thanh toán.");
        }
        if (plan.getDepartureDate() == null || !plan.getDepartureDate().isAfter(LocalDate.now())
                || planStatus == TourPlanStatus.IN_PROGRESS || planStatus == TourPlanStatus.FINISHED) {
            throw new IllegalStateException("Tour đã khởi hành, không thể thanh toán.");
        }
        // Phiếu đã giữ chỗ khi tạo; số vé còn lại âm hoặc phiếu vượt tổng số vé nghĩa là kế hoạch bị bán quá chỗ
        if (plan.getRemainingTickets() < 0 || booking.getTickets() > plan.getTickets()) {
            throw new IllegalStateException(
                    "Kế hoạch tour không còn đủ chỗ cho phiếu này. " + contactStaff() + " để đổi lịch.");
        }

        BigDecimal paid = paymentDAO.getConfirmedTotal(idTourBooking);
        BigDecimal remaining = booking.getCostTotal().subtract(paid);
        if (remaining.signum() <= 0) {
            throw new IllegalStateException("Phiếu đặt tour đã được thanh toán đủ.");
        }

        LocalDateTime deadline = paymentDeadline(booking, plan);
        if (LocalDateTime.now().isAfter(deadline)) {
            throw new IllegalStateException("Đã quá hạn thanh toán (hạn: " + deadline.format(DATE_TIME)
                    + "). " + contactStaff() + ".");
        }

        for (PaymentDTO payment : paymentDAO.findByBooking(idTourBooking)) {
            if (payment.getStatus() == PaymentStatus.PENDING_VERIFICATION) {
                throw new IllegalStateException("Giao dịch #" + payment.getIdPayment()
                        + " của phiếu đang chờ nhân viên xác nhận. Vui lòng chờ kết quả hoặc liên hệ nhân viên.");
            }
        }

        String tourName = paymentDAO.findTourName(booking.getIdTourPlan());
        return new PaymentContext(booking, plan, tourName, paid, remaining, deadline);
    }

    // Giao dịch chờ thanh toán còn hiệu lực (nếu khách đã tạo trước đó), để mở lại màn hình
    public PaymentDTO findAwaitingPayment(int idTourBooking) {
        requireCustomer();
        LocalDateTime now = LocalDateTime.now();
        return paymentDAO.findByBooking(idTourBooking).stream()
                .filter(p -> p.getStatus() == PaymentStatus.AWAITING_PAYMENT && !p.isExpired(now))
                .findFirst()
                .orElse(null);
    }

    // thanh toán trực tiếp, hạn trả tiền là hạn thanh toán của phiếu
    public PaymentDTO createCashPayment(PaymentContext context) {
        requireCustomer();
        PaymentDTO dto = newPayment(context, PaymentMethod.CASH, context.deadline());
        dto.setNote("Khách chọn thanh toán trực tiếp");
        return paymentDAO.createPayment(dto, transferPrefix(context.booking()));
    }

    // kiểm tra phiếu + số tiền, tạo giao dịch và mã QR chứa nội dung chuyển khoản riêng của giao dịch
    public QrPayment createQrPayment(PaymentContext context) {
        requireCustomer();
        String bin = AppConfig.bankBin();
        String accountNo = AppConfig.bankAccountNo();
        if (bin.isEmpty() || accountNo.isEmpty()) {
            throw new IllegalStateException("Hệ thống chưa cấu hình tài khoản nhận chuyển khoản, không tạo được mã QR. "
                    + "Vui lòng chọn thanh toán trực tiếp hoặc liên hệ nhân viên.");
        }

        LocalDateTime qrExpiry = LocalDateTime.now().plusMinutes(AppConfig.paymentQrExpireMinutes());
        // Mã QR không được dài hơn hạn thanh toán của phiếu
        if (qrExpiry.isAfter(context.deadline())) {
            qrExpiry = context.deadline();
        }

        PaymentDTO dto = newPayment(context, PaymentMethod.BANK_QR, qrExpiry);
        dto.setNote("Khách tạo mã QR chuyển khoản");
        PaymentDTO payment = paymentDAO.createPayment(dto, transferPrefix(context.booking()));
        return buildQr(payment);
    }

    // Dựng lại ảnh QR cho giao dịch chờ thanh toán đã có (khi khách mở lại màn hình)
    public QrPayment rebuildQr(PaymentDTO payment) {
        requireOwnPayment(payment.getIdPayment());
        return buildQr(payment);
    }

    private QrPayment buildQr(PaymentDTO payment) {
        try {
            String payload = VietQrService.buildPayload(AppConfig.bankBin(), AppConfig.bankAccountNo(),
                    payment.getAmount(), payment.getTransferContent());
            return new QrPayment(payment, VietQrService.toImage(payload, 280),
                    AppConfig.bankName(), AppConfig.bankAccountNo(), AppConfig.bankAccountName());
        } catch (RuntimeException e) {
            paymentDAO.closeAwaiting(payment.getIdPayment(), PaymentStatus.CANCELLED, "Không tạo được mã QR");
            throw new IllegalStateException("Không tạo được mã QR: " + e.getMessage()
                    + ". Vui lòng tạo lại hoặc chọn thanh toán trực tiếp.", e);
        }
    }

    // khách báo đã chuyển khoản; giao dịch chuyển sang chờ nhân viên đối soát
    public void reportTransferred(int idPayment, String transactionRef) {
        PaymentDTO payment = requireOwnPayment(idPayment);
        LocalDateTime now = LocalDateTime.now();
        if (payment.getStatus() == PaymentStatus.AWAITING_PAYMENT && payment.isExpired(now)) {
            paymentDAO.closeAwaiting(idPayment, PaymentStatus.EXPIRED, "Mã QR hết hạn");
            throw new IllegalStateException(
                    "Mã QR đã hết hạn. Vui lòng tạo lại mã QR hoặc đổi phương thức thanh toán.");
        }
        String ref = transactionRef == null ? null : transactionRef.trim();
        if (ref != null && ref.length() > 50) {
            throw new IllegalArgumentException("Mã giao dịch ngân hàng tối đa 50 ký tự.");
        }
        if (!paymentDAO.markPendingVerification(idPayment, ref == null || ref.isEmpty() ? null : ref, now)) {
            throw new IllegalStateException(
                    "Không ghi nhận được giao dịch (mã QR đã hết hạn hoặc giao dịch đã được xử lý). Vui lòng thực hiện lại.");
        }
    }

    // khách hủy giao dịch, không cập nhật trạng thái đã thanh toán
    public void cancelPayment(int idPayment) {
        requireOwnPayment(idPayment);
        paymentDAO.closeAwaiting(idPayment, PaymentStatus.CANCELLED, "Khách hủy giao dịch");
    }

    public void expirePayment(int idPayment) {
        requireOwnPayment(idPayment);
        paymentDAO.closeAwaiting(idPayment, PaymentStatus.EXPIRED, "Mã QR hết hạn");
    }

    // payments
    public List<PaymentDTO> getVisiblePayments() {
        requirePermission(Permission.VIEW_PAYMENT, "Bạn không có quyền xem thanh toán");
        paymentDAO.expireOverdue(LocalDateTime.now());
        AccountDTO account = SessionManager.getCurrentAccount();
        if (account.getRole() == Role.CUSTOMER) {
            return paymentDAO.findByCustomer(account.getIdCustomer());
        }
        return paymentDAO.getAll();
    }

    // detailed dialog
    public List<PaymentDTO> getPaymentsOfBooking(int idTourBooking) {
        requirePermission(Permission.VIEW_PAYMENT, "Bạn không có quyền xem thanh toán");
        AccountDTO account = SessionManager.getCurrentAccount();
        List<PaymentDTO> result = paymentDAO.findByBooking(idTourBooking);
        if (account.getRole() == Role.CUSTOMER) {
            result.removeIf(p -> account.getIdCustomer() == null || !account.getIdCustomer().equals(p.getIdCustomer()));
        }
        return result;
    }

    // Số tiền đã thanh toán của từng phiếu, dùng cho bảng phiếu đặt tour
    public Map<Integer, BigDecimal> getConfirmedTotals() {
        return paymentDAO.getConfirmedTotals();
    }

    // staffs
    /**
     * nhân viên kiểm tra giao dịch và nhập số tiền thực nhận.
     * Đúng số tiền → ghi nhận, phiếu "Chờ xác nhận" được chuyển "Đã xác nhận" khi đã trả đủ.
     * Sai số tiền → giao dịch bị từ chối, không cập nhật trạng thái đã thanh toán.
     */
    public PaymentDAO.ConfirmOutcome confirmPayment(int idPayment, BigDecimal receivedAmount, String note) {
        requirePermission(Permission.MANAGE_PAYMENT, "Bạn không có quyền xác nhận thanh toán");
        if (receivedAmount == null || receivedAmount.signum() <= 0) {
            throw new IllegalArgumentException("Số tiền thực nhận phải lớn hơn 0");
        }
        PaymentDTO payment = paymentDAO.findById(idPayment);
        if (payment == null) {
            throw new IllegalStateException("Không tìm thấy giao dịch #" + idPayment);
        }

        PaymentDAO.ConfirmOutcome outcome = paymentDAO.confirm(idPayment, receivedAmount, staffName(),
                outcomeNote(note, receivedAmount, payment.getAmount()), LocalDateTime.now());

        String code = bookingCode(payment.getIdTourBooking());
        switch (outcome) {
            case FULLY_PAID -> notifyCustomer(payment.getIdCustomer(), "Xác nhận thanh toán " + code,
                    "Chúng tôi đã nhận " + money(receivedAmount) + " cho phiếu đặt tour " + code
                            + ". Phiếu đã được thanh toán đủ và được xác nhận đặt tour.\n"
                            + "Vui lòng có mặt đúng giờ theo lịch khởi hành. Cảm ơn quý khách!");
            case CONFIRMED -> notifyCustomer(payment.getIdCustomer(), "Xác nhận thanh toán " + code,
                    "Chúng tôi đã nhận " + money(receivedAmount) + " cho phiếu đặt tour " + code
                            + ". Vui lòng xem số tiền còn lại trong ứng dụng.");
            case AMOUNT_MISMATCH -> notifyCustomer(payment.getIdCustomer(), "Giao dịch thanh toán " + code + " chưa hợp lệ",
                    "Giao dịch #" + idPayment + " cho phiếu " + code + " có số tiền không khớp (cần "
                            + money(payment.getAmount()) + ", nhận " + money(receivedAmount)
                            + "). " + contactStaff() + " để được hỗ trợ.");
        }
        return outcome;
    }

    public void rejectPayment(int idPayment, String reason) {
        requirePermission(Permission.MANAGE_PAYMENT, "Bạn không có quyền từ chối thanh toán");
        if (reason == null || reason.isBlank()) {
            throw new IllegalArgumentException("Vui lòng nhập lý do từ chối");
        }
        PaymentDTO payment = paymentDAO.findById(idPayment);
        if (payment == null) {
            throw new IllegalStateException("Không tìm thấy giao dịch #" + idPayment);
        }
        if (!paymentDAO.reject(idPayment, staffName(), limit(reason.trim()), LocalDateTime.now())) {
            throw new IllegalStateException("Giao dịch đã được xử lý trước đó, vui lòng làm mới danh sách.");
        }
        String code = bookingCode(payment.getIdTourBooking());
        notifyCustomer(payment.getIdCustomer(), "Giao dịch thanh toán " + code + " bị từ chối",
                "Giao dịch #" + idPayment + " cho phiếu " + code + " không được xác nhận.\nLý do: " + reason.trim()
                        + "\nVui lòng thực hiện lại hoặc liên hệ nhân viên.");
    }

    // Hạn thanh toán: N giờ kể từ lúc đặt (payment.deadline.hours), nhưng không muộn hơn ngày khởi hành
    public static LocalDateTime paymentDeadline(TourBookingDTO booking, TourPlanDTO plan) {
        LocalDateTime deadline = booking.getBookingDate().plusHours(AppConfig.paymentDeadlineHours());
        LocalDateTime departure = plan.getDepartureDate().atStartOfDay();
        return deadline.isAfter(departure) ? departure : deadline;
    }

    public static String money(BigDecimal amount) {
        if (amount == null) {
            return "";
        }
        DecimalFormat format = new DecimalFormat("#,##0", DecimalFormatSymbols.getInstance(Locale.forLanguageTag("vi-VN")));
        return format.format(amount) + " đ";
    }

    public static String bookingCode(int idTourBooking) {
        return String.format("PDT%03d", idTourBooking);
    }

    private static String transferPrefix(TourBookingDTO booking) {
        return AppConfig.paymentTransferPrefix() + " " + booking.getBookingCode() + " GD";
    }

    private PaymentDTO newPayment(PaymentContext context, PaymentMethod method, LocalDateTime expiresAt) {
        PaymentDTO dto = new PaymentDTO();
        dto.setIdTourBooking(context.booking().getIdTourBooking());
        dto.setIdCustomer(context.booking().getIdCustomer());
        dto.setMethod(method);
        dto.setAmount(context.remaining());
        dto.setCreatedAt(LocalDateTime.now());
        dto.setExpiresAt(expiresAt);
        return dto;
    }

    private TourPlanDTO findPlan(String idTourPlan) {
        return paymentDAO.findTourPlan(idTourPlan);
    }

    private AccountDTO requireCustomer() {
        requirePermission(Permission.VIEW_PAYMENT, "Bạn không có quyền thanh toán");
        AccountDTO account = SessionManager.getCurrentAccount();
        if (account.getRole() != Role.CUSTOMER) {
            throw new SecurityException("Chỉ khách hàng mới thanh toán phiếu đặt tour của mình");
        }
        if (account.getIdCustomer() == null || account.getIdCustomer().isBlank()) {
            throw new IllegalStateException("Tài khoản chưa được liên kết với hồ sơ khách hàng");
        }
        return account;
    }

    private PaymentDTO requireOwnPayment(int idPayment) {
        AccountDTO account = requireCustomer();
        PaymentDTO payment = paymentDAO.findById(idPayment);
        if (payment == null || !account.getIdCustomer().equals(payment.getIdCustomer())) {
            throw new IllegalStateException("Không tìm thấy giao dịch của bạn");
        }
        return payment;
    }

    private static void requirePermission(Permission permission, String message) {
        if (!SessionManager.hasPermission(permission)) {
            throw new SecurityException(message);
        }
    }

    private static String staffName() {
        AccountDTO account = SessionManager.getCurrentAccount();
        return account.getIdStaff() != null && !account.getIdStaff().isBlank()
                ? account.getIdStaff()
                : account.getUsername();
    }

    private static String outcomeNote(String note, BigDecimal received, BigDecimal expected) {
        String base = note == null ? "" : note.trim();
        if (received.compareTo(expected) != 0) {
            String mismatch = "Sai số tiền: cần " + money(expected) + ", nhận " + money(received);
            base = base.isEmpty() ? mismatch : mismatch + ". " + base;
        }
        return limit(base.isEmpty() ? null : base);
    }

    private static String limit(String text) {
        return text != null && text.length() > 255 ? text.substring(0, 255) : text;
    }

    // "Vui lòng liên hệ nhân viên" kèm hotline của đơn vị nếu có cấu hình company.hotline
    private static String contactStaff() {
        String hotline = AppConfig.companyHotline();
        return hotline.isEmpty() ? "Vui lòng liên hệ nhân viên" : "Vui lòng liên hệ nhân viên (hotline " + hotline + ")";
    }

    // Gửi email thông báo cho khách ở luồng riêng; lỗi gửi mail không ảnh hưởng kết quả đối soát
    private void notifyCustomer(String idCustomer, String subject, String body) {
        Thread thread = new Thread(() -> {
            try {
                String email = paymentDAO.findCustomerEmail(idCustomer);
                if (email != null) {
                    MailService.send(email, subject, body);
                }
            } catch (Exception e) {
                System.err.println("Không gửi được email thông báo thanh toán: " + e.getMessage());
            }
        }, "payment-mail");
        thread.setDaemon(true);
        thread.start();
    }
}
