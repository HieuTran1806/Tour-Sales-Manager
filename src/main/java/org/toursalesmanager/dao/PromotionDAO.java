package org.toursalesmanager.dao;

import org.toursalesmanager.dto.PromotionDTO;

import java.sql.*;
import java.time.LocalDate;
import java.util.*;
public class PromotionDAO {
    public ArrayList<PromotionDTO> getDsCTrinhKM() {
        ArrayList<PromotionDTO> list = new ArrayList<>();
        try (Connection conn = MyConnection.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery("SELECT * FROM CTrinhKM")) {
            while (rs.next()) {
                PromotionDTO ct = new PromotionDTO(
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

    public PromotionDAO() {
    }

    public PromotionDTO timCTrinhKM(String maKM) {
        try (Connection conn = MyConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement("SELECT * FROM CTrinhKM WHERE maKM = ?")) {
            pstmt.setString(1, maKM);
            try (ResultSet rs = pstmt.executeQuery()) {
                if (rs.next()) {
                    return new PromotionDTO(
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

    public PromotionDTO maptoCTrinhKM(ResultSet rs) throws SQLException {
        return new PromotionDTO(
                rs.getString("maKM"),
                rs.getString("tenKM"),
                LocalDate.parse(rs.getDate("ngayBD").toString()),
                LocalDate.parse(rs.getDate("ngayKT").toString()),
                rs.getBoolean ("hinhThucKM"),
                rs.getFloat("chietKhau"),
                rs.getString("ghiChu")
        );
    }
    public boolean themCTrinhKM(PromotionDTO ct) {
        String sql = "INSERT INTO CTrinhKM (maKM, tenKM, ngayBD, ngayKT, hinhThucKM,chietKhau, ghiChu) VALUES (?, ?, ?, ?, ?, ?, ?)";
        try (Connection conn = MyConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, ct.getIdPromotion());
            pstmt.setString(2, ct.getPromotionName());
            pstmt.setDate(3, java.sql.Date.valueOf(ct.getStartDate()));
            pstmt.setDate(4, java.sql.Date.valueOf(ct.getEndDate()));
            pstmt.setBoolean(5,ct.isHinhThucKM() );
            pstmt.setFloat(6, ct.getDiscount());
            pstmt.setString(7, ct.getNote());
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
    public boolean suaCTrinhKM(PromotionDTO ct) {
        String sql = "UPDATE CTrinhKM SET tenKM = ?, ngayBD = ?, ngayKT = ?, hinhThucKM = ?,chietKhau = ?, ghiChu = ? WHERE maKM = ?";
        try (Connection conn = MyConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, ct.getPromotionName());
            pstmt.setDate(2, java.sql.Date.valueOf(ct.getStartDate()));
            pstmt.setDate(3, java.sql.Date.valueOf(ct.getEndDate()));
            pstmt.setBoolean(4, ct.isHinhThucKM());
            pstmt.setFloat(5, ct.getDiscount());
            pstmt.setString(6, ct.getNote());
            pstmt.setString(7, ct.getIdPromotion());
            int rowsAffected = pstmt.executeUpdate();
            return rowsAffected > 0;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    public ArrayList<PromotionDTO> searchCTrinhKM(String loai, String keyword) {
        ArrayList<PromotionDTO> result = new ArrayList<>();
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