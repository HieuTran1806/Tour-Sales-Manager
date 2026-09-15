package org.example.dao;
import org.example.dto.PhieuDatTourDTO;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class PhieuDatTourDAO {
    public ArrayList<PhieuDatTourDTO> layDanhSachKHang_KHTour() {
        ArrayList<PhieuDatTourDTO> dsKHang_KHTour = new ArrayList<>();
        String sql = "SELECT * FROM KHang_KHTour";
        try (Connection conn = MyConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql);
             ResultSet rs = pstmt.executeQuery()) {
            while (rs.next()) {
                String MaKHTour = rs.getString("MaKHTour");
                String MaKHang = rs.getString("MaKHang");
                long GiaVe = rs.getLong("GiaVe");
                PhieuDatTourDTO kht = new PhieuDatTourDTO(MaKHTour, MaKHang, GiaVe);
                dsKHang_KHTour.add(kht);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return dsKHang_KHTour;
    }

    public boolean themKHang_KHTour(PhieuDatTourDTO kht) {

        String sql = "INSERT INTO khang_khtour (MaKHang, MaKHTour, GiaVe) VALUES (?, ?, ?)";

        try (Connection conn = MyConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setString(1, kht.getMaKHang().trim());
            pstmt.setString(2, kht.getMaKHTour().trim());
            pstmt.setLong(3, kht.getGiaVe());

            int rows = pstmt.executeUpdate();
            return rows > 0;

        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    public PhieuDatTourDTO timKHang_KHTourTheoMaTour(String MaKHTour) {
        String sql = "SELECT * FROM KHang_KHTour WHERE MaKHTour = ?";
        try (Connection conn = MyConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, MaKHTour);
            try (ResultSet rs = pstmt.executeQuery()) {
                if (rs.next()) {
                    String MaKHang = rs.getString("MaKHang");
                    long GiaVe = rs.getLong("GiaVe");
                    return new PhieuDatTourDTO(MaKHTour, MaKHang, GiaVe);
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    public List<PhieuDatTourDTO> timKHang_KHTours(String column, String value) {
        List<PhieuDatTourDTO> results = new ArrayList<>();
        String sql = "SELECT * FROM KHang_KHTour WHERE " + column + " LIKE ?";
        try (Connection conn = MyConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, "%" + value + "%");
            try (ResultSet rs = pstmt.executeQuery()) {
                while (rs.next()) {
                    String MaKHTour = rs.getString("MaKHTour");
                    String MaKHang = rs.getString("MaKHang");
                    long GiaVe = rs.getLong("GiaVe");
                    results.add(new PhieuDatTourDTO(MaKHTour, MaKHang, GiaVe));
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return results;
    }

    public List<PhieuDatTourDTO> timKHang_KHToursTheoHo(String ho) {
        List<PhieuDatTourDTO> results = new ArrayList<>();
        String sql = """
            SELECT kk.MaKHTour, kk.MaKHang, kk.GiaVe
            FROM KHang_KHTour kk
            JOIN khachhang kh ON kk.MaKHang = kh.MaKHang
            WHERE kh.Ho LIKE ?
            """;
        try (Connection conn = MyConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, "%" + ho + "%");
            try (ResultSet rs = pstmt.executeQuery()) {
                while (rs.next()) {
                    String MaKHTour = rs.getString("MaKHTour");
                    String MaKHang = rs.getString("MaKHang");
                    long GiaVe = rs.getLong("GiaVe");
                    results.add(new PhieuDatTourDTO(MaKHTour, MaKHang, GiaVe));
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return results;
    }

    public List<PhieuDatTourDTO> timKHang_KHToursTheoTen(String ten) {
        List<PhieuDatTourDTO> results = new ArrayList<>();
        String sql = """
            SELECT kk.MaKHTour, kk.MaKHang, kk.GiaVe
            FROM KHang_KHTour kk
            JOIN khachhang kh ON kk.MaKHang = kh.MaKHang
            WHERE kh.Ten LIKE ?
            """;
        try (Connection conn = MyConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, "%" + ten + "%");
            try (ResultSet rs = pstmt.executeQuery()) {
                while (rs.next()) {
                    String MaKHTour = rs.getString("MaKHTour");
                    String MaKHang = rs.getString("MaKHang");
                    long GiaVe = rs.getLong("GiaVe");
                    results.add(new PhieuDatTourDTO(MaKHTour, MaKHang, GiaVe));
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return results;
    }

    public boolean xoaKHang_KHTour(String MaKHTour, String MaKHang) {
        String sql = "DELETE FROM KHang_KHTour WHERE MaKHTour = ? AND MaKHang = ?";
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

    private PhieuDatTourDTO mapToKHang_KHTour(ResultSet rs) throws SQLException {
        String MaKHTour = rs.getString("MaKHTour");
        String MaKHang = rs.getString("MaKHang");
        long GiaVe = rs.getLong("GiaVe");
        return new PhieuDatTourDTO(MaKHTour, MaKHang, GiaVe);
    }

    public boolean capNhatKHang_KHTour(PhieuDatTourDTO kht) {
        String sql = "UPDATE KHang_KHTour SET MaKHang = ?, GiaVe = ? WHERE MaKHTour = ?";
        try (Connection conn = MyConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, kht.getMaKHang());
            pstmt.setLong(2, kht.getGiaVe());
            pstmt.setString(3, kht.getMaKHTour());
            int rowsAffected = pstmt.executeUpdate();
            return rowsAffected > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    public long layDonGiaTheoMaKHTour(String maKHTour){
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
}
