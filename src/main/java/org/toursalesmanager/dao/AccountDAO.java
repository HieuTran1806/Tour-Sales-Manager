package org.toursalesmanager.dao;

import org.toursalesmanager.dto.AccountDTO;
import org.toursalesmanager.enums.Role;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;

public class AccountDAO {

    public AccountDTO loginAccount(String username, String password) {
        String sql = "SELECT idAccount, userName, password, role, status, idstaff, idCustomer FROM taikhoan WHERE username = ? AND password = ?";

        try (Connection conn = MyConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setString(1, username);
            pstmt.setString(2, password);

            try (ResultSet rs = pstmt.executeQuery()) {

                if (rs.next()) {
                    return new AccountDTO(
                            rs.getInt("idaccount"),
                            rs.getString("username"),
                            rs.getString("password"),
                            Role.valueOf(
                                    rs.getString("role").toUpperCase().trim()
                            ),
                            rs.getString("status"),
                            rs.getString("idstaff"),
                            rs.getString("idcustomer")
                    );
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return null;
    }

    public boolean createStaffAccount(String idStaff, Role role) {

        String defaultPass = "123456";
        String sql = "INSERT INTO taikhoan (username, password, role, status, idstaff) VALUES (?, ?, ?, ?, ?)";

        try (Connection conn = MyConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setString(1, idStaff);
            pstmt.setString(2, defaultPass);
            pstmt.setString(3, role.name());
            pstmt.setString(4, "ACTIVE");
            pstmt.setString(5, idStaff);

            return pstmt.executeUpdate() > 0;

        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    public boolean updateStaffRole(String idStaff, Role role) {
        String sql = """
            UPDATE account
            SET role = ?
            WHERE idStaff = ?
            """;

        try (
                Connection conn = MyConnection.getConnection();
                PreparedStatement pstmt =
                        conn.prepareStatement(sql)
        ) {
            pstmt.setString(1, role.name());
            pstmt.setString(2, idStaff);

            return pstmt.executeUpdate() > 0;

        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    public boolean existsByStaffId(String idStaff) {
        String sql = """
            SELECT 1
            FROM account
            WHERE idStaff = ?
            LIMIT 1
            """;

        try (
                Connection conn = MyConnection.getConnection();
                PreparedStatement pstmt =
                        conn.prepareStatement(sql)
        ) {
            pstmt.setString(1, idStaff);

            try (ResultSet rs = pstmt.executeQuery()) {
                return rs.next();
            }

        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    public void registerCustomer(String username, String password, String firstName, String lastName, String address, String phoneNumber, LocalDate dob, Role role) {
    }

    public boolean existsByUsername(String username) {
        return false;
    }
}