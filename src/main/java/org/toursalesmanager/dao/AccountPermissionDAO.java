package org.toursalesmanager.dao;

import org.toursalesmanager.enums.Permission;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.EnumSet;
import java.util.Set;

public class AccountPermissionDAO {

    /**
     * Trả về danh sách quyền riêng của tài khoản,
     * hoặc null nếu tài khoản chưa được phân quyền riêng (dùng quyền mặc định theo vai trò).
     */
    public Set<Permission> getPermissions(int idAccount) {
        String sql = "SELECT permission FROM taikhoan_quyen WHERE idaccount = ?";
        try (Connection conn = MyConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setInt(1, idAccount);

            try (ResultSet rs = pstmt.executeQuery()) {
                boolean hasRow = false;
                Set<Permission> permissions = EnumSet.noneOf(Permission.class);
                while (rs.next()) {
                    hasRow = true;
                    try {
                        permissions.add(Permission.valueOf(rs.getString("permission").trim()));
                    } catch (IllegalArgumentException e) {
                        // Bỏ qua quyền không còn tồn tại trong enum
                    }
                }
                return hasRow ? permissions : null;
            }
        } catch (SQLException e) {
            throw new RuntimeException("Không thể tải quyền của tài khoản.", e);
        }
    }

    public boolean replacePermissions(int idAccount, Set<Permission> permissions) {
        String deleteSql = "DELETE FROM taikhoan_quyen WHERE idaccount = ?";
        String insertSql = "INSERT INTO taikhoan_quyen (idaccount, permission) VALUES (?, ?)";

        try (Connection conn = MyConnection.getConnection()) {
            conn.setAutoCommit(false);
            try {
                try (PreparedStatement pstmt = conn.prepareStatement(deleteSql)) {
                    pstmt.setInt(1, idAccount);
                    pstmt.executeUpdate();
                }

                try (PreparedStatement pstmt = conn.prepareStatement(insertSql)) {
                    for (Permission permission : permissions) {
                        pstmt.setInt(1, idAccount);
                        pstmt.setString(2, permission.name());
                        pstmt.addBatch();
                    }
                    pstmt.executeBatch();
                }

                conn.commit();
                return true;
            } catch (SQLException | RuntimeException e) {
                conn.rollback();
                throw e;
            } finally {
                conn.setAutoCommit(true);
            }
        } catch (SQLException e) {
            throw new RuntimeException("Không thể lưu quyền của tài khoản.", e);
        }
    }

    public boolean deletePermissions(int idAccount) {
        String sql = "DELETE FROM taikhoan_quyen WHERE idaccount = ?";
        try (Connection conn = MyConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setInt(1, idAccount);
            pstmt.executeUpdate();
            return true;
        } catch (SQLException e) {
            throw new RuntimeException("Không thể khôi phục quyền mặc định của tài khoản.", e);
        }
    }
}
