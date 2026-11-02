package org.toursalesmanager.dao;
import org.toursalesmanager.dto.TourBookingDTO;

import java.math.BigDecimal;
import java.sql.*;
import java.time.LocalDateTime;
import java.util.ArrayList;

public class TourBookingDAO {
    public ArrayList<TourBookingDTO> getAllTourBooking() {
        ArrayList<TourBookingDTO> tourBookingList = new ArrayList<>();

        String sql = """
        SELECT idTourBooking,
               idCustomer,
               idTourPlan,
               bookingDate,
               tickets,
               price,
               costTotal,
               bookingStatus,
               note
        FROM tourbooking
        """;

        try (Connection con = MyConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()
        ) {while (rs.next()){
                tourBookingList.add(mapResultSetToDTO(rs));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return tourBookingList;
    }


    // Lưu phiếu đặt và trừ số vé còn lại của kế hoạch tour trong cùng một transaction
    public boolean addTourBooking(TourBookingDTO dto) {

        String reserveSql = "UPDATE kehoachtour SET soveconlai = soveconlai - ? WHERE makhtour = ? AND soveconlai >= ?";
        String insertSql = "INSERT INTO tourbooking (idCustomer, idTourPlan, bookingDate, tickets, price, costTotal, bookingStatus, note) VALUES (?, ?, ?, ?, ?, ?, ?, ?)";

        try (Connection conn = MyConnection.getConnection()) {
            conn.setAutoCommit(false);
            try (PreparedStatement reserve = conn.prepareStatement(reserveSql);
                 PreparedStatement pre = conn.prepareStatement(insertSql)) {

                reserve.setInt(1, dto.getTickets());
                reserve.setString(2, dto.getIdTourPlan().trim());
                reserve.setInt(3, dto.getTickets());
                // Không đủ vé (hoặc người khác vừa đặt hết) thì không lưu phiếu
                if (reserve.executeUpdate() == 0) {
                    conn.rollback();
                    return false;
                }

                // get data from form to insert to table
                pre.setString(1, dto.getIdCustomer().trim());
                pre.setString(2, dto.getIdTourPlan().trim());
                pre.setTimestamp(3, Timestamp.valueOf(dto.getBookingDate()));
                pre.setInt(4, dto.getTickets());
                pre.setBigDecimal(5, dto.getPrice());
                pre.setBigDecimal(6, dto.getCostTotal());
                pre.setString(7, dto.getBookingStatus().trim());
                pre.setString(8, dto.getNote().trim());

                if (pre.executeUpdate() == 0) {
                    conn.rollback();
                    return false;
                }
                conn.commit();
                return true;
            } catch (SQLException e) {
                conn.rollback();
                throw e;
            }
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    public ArrayList<TourBookingDTO> findByCustomer(String idCustomer) {
        String sql = "SELECT * FROM tourbooking WHERE idCustomer = ?";

        ArrayList<TourBookingDTO> ls = new ArrayList<>();
        if (idCustomer == null || idCustomer.isBlank()) {
            return ls;
        }

        try (Connection conn = MyConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, idCustomer);

            try (ResultSet rs = pstmt.executeQuery()) {
                while (rs.next()) {
                    ls.add(mapResultSetToDTO(rs));
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return ls;
    }

    // public List<PhieuDatTourDTO> timtourbookings(String column, String value) {
    //     List<PhieuDatTourDTO> results = new ArrayList<>();
    //     String sql = "SELECT * FROM tourbooking WHERE " + column + " LIKE ?";
    //     try (Connection conn = MyConnection.getConnection();
    //          PreparedStatement pstmt = conn.prepareStatement(sql)) {
    //         pstmt.setString(1, "%" + value + "%");
    //         try (ResultSet rs = pstmt.executeQuery()) {
    //             while (rs.next()) {
    //                 String MaKHTour = rs.getString("MaKHTour");
    //                 String MaKHang = rs.getString("MaKHang");
    //                 long GiaVe = rs.getLong("GiaVe");
    //                 results.add(new PhieuDatTourDTO(MaKHTour, MaKHang, GiaVe));
    //             }
    //         }
    //     } catch (SQLException e) {
    //         e.printStackTrace();
    //     }
    //     return results;
    // }

    // public List<PhieuDatTourDTO> timtourbookingsTheoHo(String ho) {
    //     List<PhieuDatTourDTO> results = new ArrayList<>();
    //     String sql = """
    //         SELECT kk.MaKHTour, kk.MaKHang, kk.GiaVe
    //         FROM tourbooking kk
    //         JOIN khachhang kh ON kk.MaKHang = kh.MaKHang
    //         WHERE kh.Ho LIKE ?
    //         """;
    //     try (Connection conn = MyConnection.getConnection();
    //          PreparedStatement pstmt = conn.prepareStatement(sql)) {
    //         pstmt.setString(1, "%" + ho + "%");
    //         try (ResultSet rs = pstmt.executeQuery()) {
    //             while (rs.next()) {
    //                 String MaKHTour = rs.getString("MaKHTour");
    //                 String MaKHang = rs.getString("MaKHang");
    //                 long GiaVe = rs.getLong("GiaVe");
    //                 results.add(new PhieuDatTourDTO(MaKHTour, MaKHang, GiaVe));
    //             }
    //         }
    //     } catch (SQLException e) {
    //         e.printStackTrace();
    //     }
    //     return results;
    // }

    // public List<PhieuDatTourDTO> timtourbookingsTheoTen(String ten) {
    //     List<PhieuDatTourDTO> results = new ArrayList<>();
    //     String sql = """
    //         SELECT kk.MaKHTour, kk.MaKHang, kk.GiaVe
    //         FROM tourbooking kk
    //         JOIN khachhang kh ON kk.MaKHang = kh.MaKHang
    //         WHERE kh.Ten LIKE ?
    //         """;
    //     try (Connection conn = MyConnection.getConnection();
    //          PreparedStatement pstmt = conn.prepareStatement(sql)) {
    //         pstmt.setString(1, "%" + ten + "%");
    //         try (ResultSet rs = pstmt.executeQuery()) {
    //             while (rs.next()) {
    //                 String MaKHTour = rs.getString("MaKHTour");
    //                 String MaKHang = rs.getString("MaKHang");
    //                 long GiaVe = rs.getLong("GiaVe");
    //                 results.add(new PhieuDatTourDTO(MaKHTour, MaKHang, GiaVe));
    //             }
    //         }
    //     } catch (SQLException e) {
    //         e.printStackTrace();
    //     }
    //     return results;
    // }

    // Xóa phiếu đặt và hoàn lại số vé phiếu đang giữ cho kế hoạch tour trong cùng một transaction
    public boolean deleteTourBooking(int idTourBooking) {
        String selectSql = "SELECT idTourPlan, tickets, bookingStatus FROM tourbooking WHERE idTourBooking = ? FOR UPDATE";
        String releaseSql = "UPDATE kehoachtour SET soveconlai = soveconlai + ? WHERE makhtour = ?";
        String deleteSql = "DELETE FROM tourbooking WHERE idTourBooking = ?";

        try (Connection conn = MyConnection.getConnection()) {
            conn.setAutoCommit(false);
            try (PreparedStatement select = conn.prepareStatement(selectSql);
                 PreparedStatement release = conn.prepareStatement(releaseSql);
                 PreparedStatement delete = conn.prepareStatement(deleteSql)) {

                String plan;
                int held;
                select.setInt(1, idTourBooking);
                try (ResultSet rs = select.executeQuery()) {
                    if (!rs.next()) {
                        conn.rollback();
                        return false;
                    }
                    plan = rs.getString("idTourPlan");
                    held = heldTickets(rs.getString("bookingStatus"), rs.getInt("tickets"));
                }

                if (held > 0) {
                    release.setInt(1, held);
                    release.setString(2, plan);
                    release.executeUpdate();
                }

                delete.setInt(1, idTourBooking);
                if (delete.executeUpdate() == 0) {
                    conn.rollback();
                    return false;
                }
                conn.commit();
                return true;
            } catch (SQLException e) {
                conn.rollback();
                throw e;
            }
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    // Phiếu đã hủy không còn giữ chỗ trên kế hoạch tour
    private static final String CANCELLED_STATUS = "Đã hủy";

    private static int heldTickets(String status, int tickets) {
        return status != null && CANCELLED_STATUS.equals(status.trim()) ? 0 : tickets;
    }

    // Cập nhật phiếu đặt và điều chỉnh số vé còn lại của kế hoạch tour trong cùng một transaction:
    // hoàn số vé phiếu cũ đang giữ cho kế hoạch cũ, rồi giữ số vé mới trên kế hoạch mới.
    public boolean updateTourBooking(TourBookingDTO dto) {
        String selectOldSql = "SELECT idTourPlan, tickets, bookingStatus FROM tourbooking WHERE idTourBooking = ? FOR UPDATE";
        String releaseSql = "UPDATE kehoachtour SET soveconlai = soveconlai + ? WHERE makhtour = ?";
        String reserveSql = "UPDATE kehoachtour SET soveconlai = soveconlai - ? WHERE makhtour = ? AND soveconlai >= ?";
        String updateSql = "UPDATE tourbooking SET idCustomer = ?, idTourPlan = ?, bookingDate = ?, tickets = ?, " +
                "price = ?, costTotal = ?, bookingStatus = ?, note = ? WHERE idTourBooking = ?";

        try (Connection conn = MyConnection.getConnection()) {
            conn.setAutoCommit(false);
            try (PreparedStatement selectOld = conn.prepareStatement(selectOldSql);
                 PreparedStatement release = conn.prepareStatement(releaseSql);
                 PreparedStatement reserve = conn.prepareStatement(reserveSql);
                 PreparedStatement pre = conn.prepareStatement(updateSql)) {

                // Đọc và khóa phiếu cũ để tránh hai người sửa cùng lúc
                String oldPlan;
                int oldHeld;
                selectOld.setInt(1, dto.getIdTourBooking());
                try (ResultSet rs = selectOld.executeQuery()) {
                    if (!rs.next()) {
                        conn.rollback();
                        return false;
                    }
                    oldPlan = rs.getString("idTourPlan");
                    oldHeld = heldTickets(rs.getString("bookingStatus"), rs.getInt("tickets"));
                }

                String newPlan = dto.getIdTourPlan().trim();
                int newHeld = heldTickets(dto.getBookingStatus(), dto.getTickets());

                // Hoàn vé cho kế hoạch cũ
                if (oldHeld > 0) {
                    release.setInt(1, oldHeld);
                    release.setString(2, oldPlan);
                    release.executeUpdate();
                }

                // Giữ vé trên kế hoạch mới; không đủ vé thì hủy toàn bộ thay đổi
                if (newHeld > 0) {
                    reserve.setInt(1, newHeld);
                    reserve.setString(2, newPlan);
                    reserve.setInt(3, newHeld);
                    if (reserve.executeUpdate() == 0) {
                        conn.rollback();
                        return false;
                    }
                }

                pre.setString(1, dto.getIdCustomer().trim());
                pre.setString(2, newPlan);
                pre.setTimestamp(3, Timestamp.valueOf(dto.getBookingDate()));
                pre.setInt(4, dto.getTickets());
                pre.setBigDecimal(5, dto.getPrice());
                pre.setBigDecimal(6, dto.getCostTotal());
                pre.setString(7, dto.getBookingStatus().trim());
                pre.setString(8, dto.getNote() == null ? "" : dto.getNote().trim());
                pre.setInt(9, dto.getIdTourBooking());

                if (pre.executeUpdate() == 0) {
                    conn.rollback();
                    return false;
                }
                conn.commit();
                return true;
            } catch (SQLException e) {
                conn.rollback();
                throw e;
            }
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    public BigDecimal getPriceByIdTourPlan(String maKHTour){
        String sql = """
        SELECT DonGia
        FROM tour t
        JOIN kehoachtour k ON t.MaTour = k.MaTour
        WHERE k.MaKHTour = ?
        """;

        try(Connection conn = MyConnection.getConnection();
            PreparedStatement ps = conn.prepareStatement(sql)){

            ps.setString(1, maKHTour);
            ResultSet rs = ps.executeQuery();

            if(rs.next()){
                return rs.getBigDecimal("DonGia");
            }

        }catch(Exception e){
            e.printStackTrace();
        }
        return null;
    }

    private TourBookingDTO mapResultSetToDTO(ResultSet rs)
            throws SQLException {

        Timestamp timestamp = rs.getTimestamp("bookingDate");

        LocalDateTime bookingDate =
                timestamp != null ? timestamp.toLocalDateTime() : null;

        return new TourBookingDTO(
                rs.getInt("idTourBooking"),
                rs.getString("idCustomer"),
                rs.getString("idTourPlan"),
                bookingDate,
                rs.getInt("tickets"),
                rs.getBigDecimal("price"),
                rs.getBigDecimal("costTotal"),
                rs.getString("bookingStatus"),
                rs.getString("note")
        );
    }
}
