package org.toursalesmanager.bus;
import org.toursalesmanager.dao.TourBookingDAO;
import org.toursalesmanager.dto.TourPlanDTO;
import org.toursalesmanager.dto.AccountDTO;
import org.toursalesmanager.dto.TourBookingDTO;
import org.toursalesmanager.bus.TourPlanBUS;
import org.toursalesmanager.enums.Permission;
import org.toursalesmanager.enums.Role;
import org.toursalesmanager.login.SessionManager;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class TourBookingBUS {
    ArrayList<TourBookingDTO> dsKHKHTour;
    TourBookingDAO tourBookingDao;

    public TourBookingBUS() {
        tourBookingDao = new TourBookingDAO();
        dsKHKHTour = new ArrayList<>(getVisibleBookings());
    }

    public ArrayList<TourBookingDTO> getAllTourBooking(){
        AccountDTO account = SessionManager.getCurrentAccount();
        if (!SessionManager.hasPermission(Permission.VIEW_BOOKING)) {
            throw new SecurityException("Bạn không có quyền xem phiếu đặt tour");
        }
        if (account.getRole() == Role.CUSTOMER) {
            return tourBookingDao.findByCustomer(getCurrentCustomerId(account));
        }
        return tourBookingDao.getAllTourBooking();
    }

    public boolean addTourBooking(TourBookingDTO dto) {
        if (!SessionManager.hasPermission(Permission.CREATE_BOOKING)) {
            throw new SecurityException("Bạn không có quyền tạo phiếu đặt tour");
        }
        if (dto == null || dto.getIdTourPlan() == null || dto.getIdTourPlan().isBlank()) {
            throw new IllegalArgumentException("Vui lòng chọn kế hoạch tour");
        }
        if (dto.getTickets() <= 0) {
            throw new IllegalArgumentException("Số vé phải lớn hơn 0");
        }

        AccountDTO account = SessionManager.getCurrentAccount();
        String customerId = account.getRole() == Role.CUSTOMER
                ? getCurrentCustomerId(account)
                : dto.getIdCustomer();
        if (customerId == null || customerId.isBlank()) {
            throw new IllegalArgumentException("Mã khách hàng không hợp lệ");
        }

        BigDecimal price = getPriceByIdTourPlan(dto.getIdTourPlan());
        if (price == null || price.signum() < 0) {
            throw new IllegalStateException("Không tìm thấy giá của kế hoạch tour đã chọn");
        }

        dto.setIdCustomer(customerId);
        dto.setBookingDate(dto.getBookingDate() == null ? LocalDateTime.now() : dto.getBookingDate());
        dto.setPrice(price);
        dto.setCostTotal(price.multiply(BigDecimal.valueOf(dto.getTickets())));
        dto.setBookingStatus(account.getRole() == Role.CUSTOMER
                ? "Chờ xác nhận"
                : (dto.getBookingStatus() == null || dto.getBookingStatus().isBlank()
                    ? "Chờ xác nhận"
                    : dto.getBookingStatus()));
        dto.setNote(dto.getNote() == null ? "" : dto.getNote().trim());

        boolean saved = tourBookingDao.addTourBooking(dto);
        if (saved) {
            if (dsKHKHTour == null) {
                dsKHKHTour = new ArrayList<>();
            }
            dsKHKHTour.add(dto);
        }
        return saved;
    }

    public boolean createCustomerBooking(String idTourPlan, int tickets, String note) {
        AccountDTO account = SessionManager.getCurrentAccount();
        if (account.getRole() != Role.CUSTOMER
                || !SessionManager.hasPermission(Permission.CREATE_BOOKING)) {
            throw new SecurityException("Chỉ khách hàng có quyền mới được tự đặt tour");
        }
        if (idTourPlan == null || idTourPlan.isBlank() || tickets <= 0) {
            throw new IllegalArgumentException("Kế hoạch tour và số vé không hợp lệ");
        }

        TourPlanDTO plan = new TourPlanBUS().getAllKeHoachTours().stream()
                .filter(item -> idTourPlan.equals(item.getIdTourPlan()))
                .findFirst()
                .orElseThrow(() -> new IllegalArgumentException("Không tìm thấy kế hoạch tour"));
        if (plan.getDepartureDate() == null || plan.getDepartureDate().isBefore(LocalDate.now())) {
            throw new IllegalArgumentException("Không thể đặt kế hoạch tour đã khởi hành");
        }
        if (tickets > plan.getRemainingTickets()) {
            throw new IllegalArgumentException("Số vé yêu cầu vượt quá số vé còn lại");
        }

        return addTourBooking(new TourBookingDTO(
                getCurrentCustomerId(account),
                idTourPlan,
                LocalDateTime.now(),
                tickets,
                null,
                null,
                "Chờ xác nhận",
                note
        ));
    }

    private String getCurrentCustomerId(AccountDTO account) {
        String customerId = account.getIdCustomer();
        if (customerId == null || customerId.isBlank()) {
            throw new IllegalStateException("Tài khoản chưa được liên kết với hồ sơ khách hàng");
        }
        return customerId;
    }

    public void deleteTourBooking(String maKHTour, String maKHang) {
        if (!SessionManager.isAdmin()) {
            throw new SecurityException("Chỉ quản trị viên mới được xóa phiếu đặt tour");
        }
        try {
            if (dsKHKHTour == null) {
                return;
            }
            tourBookingDao.deleteTourBooking(maKHTour, maKHang);
            dsKHKHTour.removeIf(kht -> kht.getIdTourPlan().equals(maKHTour) && kht.getIdCustomer().equals(maKHang));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    // have to update again
    public boolean editTourBooking(TourBookingDTO kht) {
        if (!SessionManager.hasPermission(Permission.UPDATE_BOOKING)) {
            throw new SecurityException("Bạn không có quyền sửa phiếu đặt tour");
        }
        try {
            if (dsKHKHTour == null || kht == null || kht.getIdTourPlan() == null || kht.getIdTourPlan().isEmpty()) {
                return false;
            }
            for (int i = 0; i < dsKHKHTour.size(); i++) {
                TourBookingDTO existingKHT = dsKHKHTour.get(i);
                if (existingKHT.getIdTourPlan().equals(kht.getIdTourPlan()) && existingKHT.getIdCustomer().equals(kht.getIdCustomer())) {
                    tourBookingDao.updateTourBooking(kht);
                    dsKHKHTour.set(i, kht);
                    return true;
                }
            }
            return false;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    public TourBookingDTO findTourBookingByIdTourPlan(String maKHTour) {
        try {
            if (dsKHKHTour == null || maKHTour == null) {
                System.out.println("abcd");
                return null;
            }
            for (TourBookingDTO kht : dsKHKHTour) {
                if (kht.getIdTourPlan().equals(maKHTour)) {
                    System.out.println("abc");
                    return kht;
                }
            }
            return null;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    public List<TourBookingDTO> timKHang_KHToursTheoHo(String ho) {
        try {
            if (dsKHKHTour == null || ho == null) {
                return new ArrayList<>();
            }
            List<TourBookingDTO> result = new ArrayList<>();
            for (TourBookingDTO kht : dsKHKHTour) {
                String maKHang = kht.getIdCustomer();
                if (maKHang != null && maKHang.startsWith(ho)) {
                    result.add(kht);
                }
            }
            return result;
        } catch (Exception e) {
            e.printStackTrace();
            return new ArrayList<>();
        }
    }

    public TourBookingDTO timKHang_KHTourTheoTen(String ten) {
        try {
            if (dsKHKHTour == null || ten == null) {
                return null;
            }
            for (TourBookingDTO kht : dsKHKHTour) {
                String maKHang = kht.getIdCustomer();
                if (maKHang != null && maKHang.equalsIgnoreCase(ten)) {
                    return kht;
                }
            }
            return null;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    public BigDecimal getPriceByIdTourPlan(
            String idTourPlan
    ) {
        return tourBookingDao.getPriceByIdTourPlan(
                idTourPlan
        );
    }

    public List<TourBookingDTO> getVisibleBookings(){
        return getAllTourBooking();
    }
}
