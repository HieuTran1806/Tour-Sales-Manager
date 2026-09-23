package org.example.dao;
import org.example.dto.TourBookingDTO;

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


    public boolean addTourBooking(TourBookingDTO dto) {

        String sql = "INSERT INTO tourbooking (idCustomer, idTourPlan, bookingDate, tickets, price, costTotal, bookingStatus, note) VALUES (?, ?, ?, ?, ?, ?, ?, ?)";

        try (Connection conn = MyConnection.getConnection();
             PreparedStatement pre = conn.prepareStatement(sql)) {

            // get data from form to insert to table
            pre.setString(1, dto.getIdCustomer().trim());
            pre.setString(2, dto.getIdTourPlan().trim());
            pre.setTimestamp(3, Timestamp.valueOf(dto.getBookingDate()));
            pre.setInt(4, dto.getTickets());
            pre.setBigDecimal(5, dto.getPrice());
            pre.setBigDecimal(6, dto.getCostTotal());
            pre.setString(7, dto.getBookingStatus().trim());
            pre.setString(8, dto.getNote().trim());

            int rows = pre.executeUpdate();
            return rows > 0;

        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    // public PhieuDatTourDTO timtourbookingTheoMaTour(String MaKHTour) {
    //     String sql = "SELECT * FROM tourbooking WHERE MaKHTour = ?";
    //     try (Connection conn = MyConnection.getConnection();
    //          PreparedStatement pstmt = conn.prepareStatement(sql)) {
    //         pstmt.setString(1, MaKHTour);
    //         try (ResultSet rs = pstmt.executeQuery()) {
    //             if (rs.next()) {
    //                 String MaKHang = rs.getString("MaKHang");
    //                 long GiaVe = rs.getLong("GiaVe");
    //                 return new PhieuDatTourDTO(MaKHTour, MaKHang, GiaVe);
    //             }
    //         }
    //     } catch (SQLException e) {
    //         e.printStackTrace();
    //     }
    //     return null;
    // }

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

    public boolean deleteTourBooking(String MaKHTour, String MaKHang) {
        String sql = "DELETE FROM tourbooking WHERE MaKHTour = ? AND MaKHang = ?";
        try (Connection conn = MyConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, MaKHTour);
            pstmt.setString(2, MaKHang);
            return pstmt.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    public boolean updateTourBooking(TourBookingDTO dto) {
        String sql = "UPDATE tourbooking SET idCustomer = ?, idTourPlan = ?, bookingDate = ?, tickets = ?" +
                "price = ?, costTotal = ?, bookingStatus = ?, note; = ? WHERE idTourBooking = ?";
        try (Connection conn = MyConnection.getConnection();
             PreparedStatement pre = conn.prepareStatement(sql)) {


            pre.setString(1, dto.getIdCustomer().trim());
            pre.setString(2, dto.getIdTourPlan().trim());
            pre.setTimestamp(3, Timestamp.valueOf(dto.getBookingDate()));
            pre.setInt(4, dto.getTickets());
            pre.setBigDecimal(5, dto.getPrice());
            pre.setBigDecimal(6, dto.getCostTotal());
            pre.setString(7, dto.getBookingStatus().trim());
            pre.setString(8, dto.getNote().trim());

            int rowsAffected = pre.executeUpdate();

            return rowsAffected > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    public long getPriceByIdTourPlan(String maKHTour){
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
                return rs.getLong("DonGia");
            }

        }catch(Exception e){
            e.printStackTrace();
        }
        return 0;
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
