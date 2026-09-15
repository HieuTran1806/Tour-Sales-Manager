package org.example.dao;

import org.example.dto.TaiKhoanDTO;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class TaiKhoanDAO {

    public TaiKhoanDTO dangNhap(String username, String password) {
        String sql = "SELECT username, password, position FROM taikhoan WHERE username = ? AND password = ?";

        try (Connection conn = MyConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setString(1, username);
            pstmt.setString(2, password);

            try (ResultSet rs = pstmt.executeQuery()) {
                if (rs.next()) {
                    return new TaiKhoanDTO(
                            rs.getString("username"),
                            rs.getString("password"),
                            rs.getString("position")
                    );
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return null;
    }

    public boolean taoTaiKhoanNhanVien(String maNV, String chucVu) {
        String position = chucVu == null ? "" : chucVu.trim();
        String matKhauMacDinh = laQuanLy(position) ? "456" : "123";
        String sql = "INSERT INTO taikhoan (username, password, position) VALUES (?, ?, ?)";

        try (Connection conn = MyConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setString(1, maNV);
            pstmt.setString(2, matKhauMacDinh);
            pstmt.setString(3, position);
            return pstmt.executeUpdate() > 0;

        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    private boolean laQuanLy(String chucVu) {
        String lower = chucVu.toLowerCase();
        return lower.equals("quản lí") || lower.equals("quan li") || lower.equals("quản lý") || lower.equals("quan ly");
    }
}