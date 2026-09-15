package org.example.dao;

import org.example.dto.CTrinhKMDTO;

import java.sql.*;
import java.time.LocalDate;
import java.util.*;
public class CTrinhKMDAO {
    public ArrayList<CTrinhKMDTO> getDsCTrinhKM() {
        ArrayList<CTrinhKMDTO> list = new ArrayList<>();
        try (Connection conn = MyConnection.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery("SELECT * FROM CTrinhKM")) {
            while (rs.next()) {
                CTrinhKMDTO ct = new CTrinhKMDTO(
                        rs.getString("maKM"),
                        rs.getString("tenKM"),
                        LocalDate.parse(rs.getDate("ngayBD").toString()),
                        LocalDate.parse(rs.getDate("ngayKT").toString()),
                        rs.getBoolean("hinhThucKM"),
                        rs.getFloat("chietKhau"),
                        rs.getString("ghiChu")
                );
                list.add(ct);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return list;
    }

    public CTrinhKMDAO() {
    }

    public CTrinhKMDTO timCTrinhKM(String maKM) {
        try (Connection conn = MyConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement("SELECT * FROM CTrinhKM WHERE maKM = ?")) {
            pstmt.setString(1, maKM);
            try (ResultSet rs = pstmt.executeQuery()) {
                if (rs.next()) {
                    return new CTrinhKMDTO(
                            rs.getString("maKM"),
                            rs.getString("tenKM"),
                            LocalDate.parse(rs.getDate("ngayBD").toString()),
                            LocalDate.parse(rs.getDate("ngayKT").toString()),
                            rs.getBoolean("hinhThucKM"),
                            rs.getFloat("chietKhau"),
                            rs.getString("ghiChu")
                    );
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    public CTrinhKMDTO maptoCTrinhKM(ResultSet rs) throws SQLException {
        return new CTrinhKMDTO(
                rs.getString("maKM"),
                rs.getString("tenKM"),
                LocalDate.parse(rs.getDate("ngayBD").toString()),
                LocalDate.parse(rs.getDate("ngayKT").toString()),
                rs.getBoolean ("hinhThucKM"),
                rs.getFloat("chietKhau"),
                rs.getString("ghiChu")
        );
    }
    public boolean themCTrinhKM(CTrinhKMDTO ct) {
        String sql = "INSERT INTO CTrinhKM (maKM, tenKM, ngayBD, ngayKT, hinhThucKM,chietKhau, ghiChu) VALUES (?, ?, ?, ?, ?, ?, ?)";
        try (Connection conn = MyConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, ct.getMaKM());
            pstmt.setString(2, ct.getTenKM());
            pstmt.setDate(3, java.sql.Date.valueOf(ct.getNgayBD()));
            pstmt.setDate(4, java.sql.Date.valueOf(ct.getNgayKT()));
            pstmt.setBoolean(5,ct.isHinhThucKM() );
            pstmt.setFloat(6, ct.getChietKhau());
            pstmt.setString(7, ct.getGhiChu());
            int rowsAffected = pstmt.executeUpdate();
            return rowsAffected > 0;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }
    public boolean xoaCTrinhKM(String maKM) {
        String sql = "DELETE FROM CTrinhKM WHERE maKM = ?";
        try (Connection conn = MyConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, maKM);
            int rowsAffected = pstmt.executeUpdate();
            return rowsAffected > 0;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }
    public boolean suaCTrinhKM(CTrinhKMDTO ct) {
        String sql = "UPDATE CTrinhKM SET tenKM = ?, ngayBD = ?, ngayKT = ?, hinhThucKM = ?,chietKhau = ?, ghiChu = ? WHERE maKM = ?";
        try (Connection conn = MyConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, ct.getTenKM());
            pstmt.setDate(2, java.sql.Date.valueOf(ct.getNgayBD()));
            pstmt.setDate(3, java.sql.Date.valueOf(ct.getNgayKT()));
            pstmt.setBoolean(4, ct.isHinhThucKM());
            pstmt.setFloat(5, ct.getChietKhau());
            pstmt.setString(6, ct.getGhiChu());
            pstmt.setString(7, ct.getMaKM());
            int rowsAffected = pstmt.executeUpdate();
            return rowsAffected > 0;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    public ArrayList<CTrinhKMDTO> searchCTrinhKM(String loai, String keyword) {
        ArrayList<CTrinhKMDTO> result = new ArrayList<>();
        String sql = "SELECT * FROM CTrinhKM WHERE 1=1";
        switch (loai) {
            case "Tất cả":
                sql += " AND (maKM LIKE ? OR tenKM LIKE ?)";
                break;
            case "Mã KM":
                sql += " AND maKM LIKE ?";
                break;
            case "Tên KM":
                sql += " AND tenKM LIKE ?";
                break;
            // Thêm các trường khác nếu cần
        }
        try (Connection conn = MyConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            if (loai.equals("Tất cả")) {
                pstmt.setString(1, "%" + keyword + "%");
                pstmt.setString(2, "%" + keyword + "%");
            } else {
                pstmt.setString(1, "%" + keyword + "%");
            }
            try (ResultSet rs = pstmt.executeQuery()) {
                while (rs.next()) {
                    result.add(maptoCTrinhKM(rs));
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return result;
    }
}