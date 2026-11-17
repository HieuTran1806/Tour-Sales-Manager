package org.toursalesmanager.dao;

import org.toursalesmanager.enums.BookingStatus;
import org.toursalesmanager.dto.PaymentDTO;
import org.toursalesmanager.dto.TourPlanDTO;
import org.toursalesmanager.enums.PaymentMethod;
import org.toursalesmanager.enums.PaymentStatus;

import java.math.BigDecimal;
import java.sql.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class PaymentDAO {
    private static final String SELECT_PAYMENT = """
            SELECT p.*, b.idCustomer
            FROM payment p
            JOIN tourbooking b ON b.idTourBooking = p.idTourBooking
            """;

    public enum ConfirmOutcome {
        CONFIRMED,          // đúng số tiền, đã ghi nhận
        FULLY_PAID,         // đúng số tiền và phiếu đã thanh toán đủ
        AMOUNT_MISMATCH     // sai số tiền, giao dịch bị từ chối
    }

    public List<PaymentDTO> getAll() {
        return query(SELECT_PAYMENT + " ORDER BY p.idPayment DESC", ps -> { });
    }

    public List<PaymentDTO> findByCustomer(String idCustomer) {
        return query(SELECT_PAYMENT + " WHERE b.idCustomer = ? ORDER BY p.idPayment DESC",
                ps -> ps.setString(1, idCustomer));
    }

    public List<PaymentDTO> findByBooking(int idTourBooking) {
        return query(SELECT_PAYMENT + " WHERE p.idTourBooking = ? ORDER BY p.idPayment DESC",
                ps -> ps.setInt(1, idTourBooking));
    }

    public PaymentDTO findById(int idPayment) {
        List<PaymentDTO> result = query(SELECT_PAYMENT + " WHERE p.idPayment = ?",
                ps -> ps.setInt(1, idPayment));
        return result.isEmpty() ? null : result.get(0);
    }

    // Tổng tiền đã được nhân viên xác nhận của từng phiếu
    public Map<Integer, BigDecimal> getConfirmedTotals() {
        String sql = "SELECT idTourBooking, SUM(amount) AS paid FROM payment WHERE status = ? GROUP BY idTourBooking";
        Map<Integer, BigDecimal> totals = new HashMap<>();
        try (Connection conn = MyConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, PaymentStatus.CONFIRMED.name());
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    totals.put(rs.getInt("idTourBooking"), rs.getBigDecimal("paid"));
                }
            }
        } catch (SQLException e) {
            throw dbError(e);
        }
        return totals;
    }

    public BigDecimal getConfirmedTotal(int idTourBooking) {
        try (Connection conn = MyConnection.getConnection()) {
            return confirmedTotal(conn, idTourBooking);
        } catch (SQLException e) {
            throw dbError(e);
        }
    }

    public TourPlanDTO findTourPlan(String idTourPlan) {
        String sql = "SELECT * FROM kehoachtour WHERE TRIM(makhtour) = TRIM(?)";
        try (Connection conn = MyConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, idTourPlan);
            try (ResultSet rs = ps.executeQuery()) {
                if (!rs.next()) {
                    return null;
                }
                Date departure = rs.getDate("ngaykhoihanh");
                Date end = rs.getDate("ngayketthuc");
                return new TourPlanDTO(
                        rs.getString("makhtour"),
                        departure == null ? null : departure.toLocalDate(),
                        end == null ? null : end.toLocalDate(),
                        rs.getInt("tongsove"),
                        rs.getBigDecimal("tongchidukien"),
                        rs.getInt("soveconlai"),
                        rs.getString("trangthai"),
                        rs.getString("matour"),
                        rs.getString("manvhd")
                );
            }
        } catch (SQLException e) {
            throw dbError(e);
        }
    }

    public String findTourName(String idTourPlan) {
        String sql = "SELECT t.ten FROM kehoachtour k JOIN tour t ON t.matour = k.matour WHERE k.makhtour = ?";
        try (Connection conn = MyConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, idTourPlan);
            try (ResultSet rs = ps.executeQuery()) {
                return rs.next() ? rs.getString(1) : null;
            }
        } catch (SQLException e) {
            throw dbError(e);
        }
    }

    public String findCustomerEmail(String idCustomer) {
        String sql = "SELECT email FROM taikhoan WHERE idcustomer = ? AND email IS NOT NULL AND email <> '' LIMIT 1";
        try (Connection conn = MyConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, idCustomer);
            try (ResultSet rs = ps.executeQuery()) {
                return rs.next() ? rs.getString(1) : null;
            }
        } catch (SQLException e) {
            throw dbError(e);
        }
    }

    // Chuyển các giao dịch chờ thanh toán đã quá hạn sang EXPIRED
    public void expireOverdue(LocalDateTime now) {
        String sql = "UPDATE payment SET status = ?, note = ? WHERE status = ? AND expiresAt IS NOT NULL AND expiresAt < ?";
        try (Connection conn = MyConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, PaymentStatus.EXPIRED.name());
            ps.setString(2, "Hết hạn thanh toán");
            ps.setString(3, PaymentStatus.AWAITING_PAYMENT.name());
            ps.setTimestamp(4, Timestamp.valueOf(now));
            ps.executeUpdate();
        } catch (SQLException e) {
            throw dbError(e);
        }
    }

    public PaymentDTO createPayment(PaymentDTO dto, String transferPrefix) {
        String lockBooking = "SELECT costTotal FROM tourbooking WHERE idTourBooking = ? FOR UPDATE";
        String pendingSql = "SELECT idPayment FROM payment WHERE idTourBooking = ? AND status = ? LIMIT 1";
        String cancelOld = "UPDATE payment SET status = ?, note = ? WHERE idTourBooking = ? AND status = ?";
        String insertSql = """
                INSERT INTO payment (idTourBooking, method, amount, status, createdAt, expiresAt, note)
                VALUES (?, ?, ?, ?, ?, ?, ?)
                """;
        String contentSql = "UPDATE payment SET transferContent = ? WHERE idPayment = ?";

        try (Connection conn = MyConnection.getConnection()) {
            conn.setAutoCommit(false);
            try {
                BigDecimal costTotal;
                try (PreparedStatement ps = conn.prepareStatement(lockBooking)) {
                    ps.setInt(1, dto.getIdTourBooking());
                    try (ResultSet rs = ps.executeQuery()) {
                        if (!rs.next()) {
                            throw new IllegalStateException("Không tìm thấy phiếu đặt tour");
                        }
                        costTotal = rs.getBigDecimal("costTotal");
                    }
                }

                try (PreparedStatement ps = conn.prepareStatement(pendingSql)) {
                    ps.setInt(1, dto.getIdTourBooking());
                    ps.setString(2, PaymentStatus.PENDING_VERIFICATION.name());
                    try (ResultSet rs = ps.executeQuery()) {
                        if (rs.next()) {
                            throw new IllegalStateException("Giao dịch #" + rs.getInt(1)
                                    + " của phiếu này đang chờ nhân viên xác nhận, vui lòng chờ kết quả.");
                        }
                    }
                }

                BigDecimal remaining = (costTotal == null ? BigDecimal.ZERO : costTotal)
                        .subtract(confirmedTotal(conn, dto.getIdTourBooking()));
                if (remaining.compareTo(dto.getAmount()) != 0) {
                    throw new IllegalStateException(
                            "Số tiền cần thanh toán của phiếu vừa thay đổi, vui lòng mở lại màn hình thanh toán.");
                }

                try (PreparedStatement ps = conn.prepareStatement(cancelOld)) {
                    ps.setString(1, PaymentStatus.CANCELLED.name());
                    ps.setString(2, "Khách tạo giao dịch mới");
                    ps.setInt(3, dto.getIdTourBooking());
                    ps.setString(4, PaymentStatus.AWAITING_PAYMENT.name());
                    ps.executeUpdate();
                }

                int id;
                try (PreparedStatement ps = conn.prepareStatement(insertSql, Statement.RETURN_GENERATED_KEYS)) {
                    ps.setInt(1, dto.getIdTourBooking());
                    ps.setString(2, dto.getMethod().name());
                    ps.setBigDecimal(3, dto.getAmount());
                    ps.setString(4, PaymentStatus.AWAITING_PAYMENT.name());
                    ps.setTimestamp(5, Timestamp.valueOf(dto.getCreatedAt()));
                    ps.setTimestamp(6, dto.getExpiresAt() == null ? null : Timestamp.valueOf(dto.getExpiresAt()));
                    ps.setString(7, dto.getNote());
                    ps.executeUpdate();
                    try (ResultSet keys = ps.getGeneratedKeys()) {
                        if (!keys.next()) {
                            throw new SQLException("Không lấy được mã giao dịch vừa tạo");
                        }
                        id = keys.getInt(1);
                    }
                }

                String content = transferPrefix + id;
                try (PreparedStatement ps = conn.prepareStatement(contentSql)) {
                    ps.setString(1, content);
                    ps.setInt(2, id);
                    ps.executeUpdate();
                }

                conn.commit();
                dto.setIdPayment(id);
                dto.setTransferContent(content);
                dto.setStatus(PaymentStatus.AWAITING_PAYMENT);
                return dto;
            } catch (SQLException | RuntimeException e) {
                conn.rollback();
                throw e;
            }
        } catch (SQLException e) {
            throw dbError(e);
        }
    }

    // Khách báo đã chuyển khoản; chỉ nhận khi giao dịch còn chờ thanh toán và chưa hết hạn
    public boolean markPendingVerification(int idPayment, String transactionRef, LocalDateTime now) {
        String sql = """
                UPDATE payment SET status = ?, paidAt = ?, transactionRef = ?
                WHERE idPayment = ? AND status = ? AND method = ? AND (expiresAt IS NULL OR expiresAt >= ?)
                """;
        try (Connection conn = MyConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, PaymentStatus.PENDING_VERIFICATION.name());
            ps.setTimestamp(2, Timestamp.valueOf(now));
            ps.setString(3, transactionRef);
            ps.setInt(4, idPayment);
            ps.setString(5, PaymentStatus.AWAITING_PAYMENT.name());
            ps.setString(6, PaymentMethod.BANK_QR.name());
            ps.setTimestamp(7, Timestamp.valueOf(now));
            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            throw dbError(e);
        }
    }

    // Hủy hoặc đánh dấu hết hạn một giao dịch khách chưa trả
    public boolean closeAwaiting(int idPayment, PaymentStatus newStatus, String note) {
        String sql = "UPDATE payment SET status = ?, note = ? WHERE idPayment = ? AND status = ?";
        try (Connection conn = MyConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, newStatus.name());
            ps.setString(2, note);
            ps.setInt(3, idPayment);
            ps.setString(4, PaymentStatus.AWAITING_PAYMENT.name());
            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            throw dbError(e);
        }
    }

    /**
     * Nhân viên đối soát: đúng số tiền thì ghi nhận CONFIRMED và cập nhật phiếu khi đã trả đủ;
     * sai số tiền thì giao dịch bị REJECTED, không cập nhật trạng thái đã thanh toán.
     */
    public ConfirmOutcome confirm(int idPayment, BigDecimal receivedAmount, String staff, String note, LocalDateTime now) {
        String lockPayment = "SELECT idTourBooking, amount, status FROM payment WHERE idPayment = ? FOR UPDATE";
        String lockBooking = "SELECT costTotal, bookingStatus FROM tourbooking WHERE idTourBooking = ? FOR UPDATE";
        String updatePayment = """
                UPDATE payment SET status = ?, receivedAmount = ?, confirmedAt = ?, confirmedBy = ?, note = ?
                WHERE idPayment = ?
                """;
        String updateBooking = "UPDATE tourbooking SET bookingStatus = ? WHERE idTourBooking = ?";

        try (Connection conn = MyConnection.getConnection()) {
            conn.setAutoCommit(false);
            try {
                int idTourBooking;
                BigDecimal amount;
                try (PreparedStatement ps = conn.prepareStatement(lockPayment)) {
                    ps.setInt(1, idPayment);
                    try (ResultSet rs = ps.executeQuery()) {
                        if (!rs.next()) {
                            throw new IllegalStateException("Không tìm thấy giao dịch #" + idPayment);
                        }
                        PaymentStatus status = PaymentStatus.valueOf(rs.getString("status"));
                        if (!status.isOpen()) {
                            throw new IllegalStateException("Giao dịch #" + idPayment
                                    + " đang ở trạng thái \"" + status.getLabel() + "\", không thể xác nhận.");
                        }
                        idTourBooking = rs.getInt("idTourBooking");
                        amount = rs.getBigDecimal("amount");
                    }
                }

                BigDecimal costTotal;
                String bookingStatus;
                try (PreparedStatement ps = conn.prepareStatement(lockBooking)) {
                    ps.setInt(1, idTourBooking);
                    try (ResultSet rs = ps.executeQuery()) {
                        if (!rs.next()) {
                            throw new IllegalStateException("Không tìm thấy phiếu đặt tour của giao dịch");
                        }
                        costTotal = rs.getBigDecimal("costTotal");
                        bookingStatus = rs.getString("bookingStatus");
                    }
                }

                boolean matched = receivedAmount.compareTo(amount) == 0;
                try (PreparedStatement ps = conn.prepareStatement(updatePayment)) {
                    ps.setString(1, matched ? PaymentStatus.CONFIRMED.name() : PaymentStatus.REJECTED.name());
                    ps.setBigDecimal(2, receivedAmount);
                    ps.setTimestamp(3, Timestamp.valueOf(now));
                    ps.setString(4, staff);
                    ps.setString(5, note);
                    ps.setInt(6, idPayment);
                    ps.executeUpdate();
                }

                if (!matched) {
                    conn.commit();
                    return ConfirmOutcome.AMOUNT_MISMATCH;
                }

                BigDecimal paid = confirmedTotal(conn, idTourBooking);
                boolean fullyPaid = costTotal != null && paid.compareTo(costTotal) >= 0;
                if (fullyPaid && BookingStatus.PENDING.matches(bookingStatus)) {
                    try (PreparedStatement ps = conn.prepareStatement(updateBooking)) {
                        ps.setString(1, BookingStatus.CONFIRMED.getLabel());
                        ps.setInt(2, idTourBooking);
                        ps.executeUpdate();
                    }
                }

                conn.commit();
                return fullyPaid ? ConfirmOutcome.FULLY_PAID : ConfirmOutcome.CONFIRMED;
            } catch (SQLException | RuntimeException e) {
                conn.rollback();
                throw e;
            }
        } catch (SQLException e) {
            throw dbError(e);
        }
    }

    public boolean reject(int idPayment, String staff, String note, LocalDateTime now) {
        String sql = """
                UPDATE payment SET status = ?, confirmedAt = ?, confirmedBy = ?, note = ?
                WHERE idPayment = ? AND status IN (?, ?)
                """;
        try (Connection conn = MyConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, PaymentStatus.REJECTED.name());
            ps.setTimestamp(2, Timestamp.valueOf(now));
            ps.setString(3, staff);
            ps.setString(4, note);
            ps.setInt(5, idPayment);
            ps.setString(6, PaymentStatus.AWAITING_PAYMENT.name());
            ps.setString(7, PaymentStatus.PENDING_VERIFICATION.name());
            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            throw dbError(e);
        }
    }

    private static BigDecimal confirmedTotal(Connection conn, int idTourBooking) throws SQLException {
        String sql = "SELECT COALESCE(SUM(amount), 0) FROM payment WHERE idTourBooking = ? AND status = ?";
        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, idTourBooking);
            ps.setString(2, PaymentStatus.CONFIRMED.name());
            try (ResultSet rs = ps.executeQuery()) {
                rs.next();
                return rs.getBigDecimal(1);
            }
        }
    }

    @FunctionalInterface
    private interface Binder {
        void bind(PreparedStatement ps) throws SQLException;
    }

    private List<PaymentDTO> query(String sql, Binder binder) {
        List<PaymentDTO> result = new ArrayList<>();
        try (Connection conn = MyConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            binder.bind(ps);
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    result.add(map(rs));
                }
            }
        } catch (SQLException e) {
            throw dbError(e);
        }
        return result;
    }

    private static PaymentDTO map(ResultSet rs) throws SQLException {
        return new PaymentDTO(
                rs.getInt("idPayment"),
                rs.getInt("idTourBooking"),
                rs.getString("idCustomer"),
                PaymentMethod.valueOf(rs.getString("method")),
                rs.getBigDecimal("amount"),
                rs.getBigDecimal("receivedAmount"),
                rs.getString("transferContent"),
                rs.getString("transactionRef"),
                PaymentStatus.valueOf(rs.getString("status")),
                toDateTime(rs.getTimestamp("createdAt")),
                toDateTime(rs.getTimestamp("expiresAt")),
                toDateTime(rs.getTimestamp("paidAt")),
                toDateTime(rs.getTimestamp("confirmedAt")),
                rs.getString("confirmedBy"),
                rs.getString("note")
        );
    }

    private static LocalDateTime toDateTime(Timestamp timestamp) {
        return timestamp == null ? null : timestamp.toLocalDateTime();
    }

    private static RuntimeException dbError(SQLException e) {
        if (e.getErrorCode() == 1146) {
            return new IllegalStateException(
                    "CSDL chưa có bảng payment. Hãy chạy script database/add_payment.sql.", e);
        }
        return new IllegalStateException("Lỗi cơ sở dữ liệu khi xử lý thanh toán: " + e.getMessage(), e);
    }
}
