package org.toursalesmanager.dao;

import org.toursalesmanager.dto.AccountDTO;
import org.toursalesmanager.enums.Role;
import org.toursalesmanager.gui.helper.PasswordUtil;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class AccountDAO {

    public AccountDTO loginAccount(String username) {
        String sql = "SELECT * FROM taikhoan WHERE username = ?";
        try (Connection conn = MyConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setString(1, username);

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
                            rs.getString("idcustomer"),
                            rs.getString("email")
                    );
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return null;
    }

    public boolean updatePassword(String username, String hashedPassword) {
        String sql = "UPDATE taikhoan SET password = ? WHERE username = ?";
        try (Connection conn = MyConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, hashedPassword);
            pstmt.setString(2, username);
            return pstmt.executeUpdate() == 1;
        } catch (SQLException e) {
            throw new RuntimeException("Không thể cập nhật mật khẩu tài khoản.", e);
        }

    }

    public boolean customerEmailExists(String email) {
        String sql = """
                SELECT 1
                FROM taikhoan
                WHERE email = ? AND role = ?
                LIMIT 1
                """;
        try (Connection conn = MyConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, email);
            pstmt.setString(2, Role.CUSTOMER.name());
            try (ResultSet rs = pstmt.executeQuery()) {
                return rs.next();
            }
        } catch (SQLException e) {
            throw new RuntimeException("Không thể kiểm tra email tài khoản.", e);
        }
    }

    public boolean updateCustomerPasswordByEmail(String email, String hashedPassword) {
        String sql = """
                UPDATE taikhoan
                SET password = ?
                WHERE email = ? AND role = ?
                """;
        try (Connection conn = MyConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, hashedPassword);
            pstmt.setString(2, email);
            pstmt.setString(3, Role.CUSTOMER.name());
            return pstmt.executeUpdate() == 1;
        } catch (SQLException e) {
            throw new RuntimeException("Không thể cập nhật mật khẩu tài khoản.", e);
        }
    }

    public boolean emailExists(String email) {
        String sql = "SELECT 1 FROM taikhoan WHERE email = ? LIMIT 1";
        try (Connection conn = MyConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, email);
            try (ResultSet rs = pstmt.executeQuery()) {
                return rs.next();
            }
        } catch (SQLException e) {
            throw new RuntimeException("Không thể kiểm tra email đã đăng ký.", e);
        }
    }

    public boolean createStaffAccount(String idStaff, Role role) {

        String defaultPass = "123456";
        String sql = "INSERT INTO taikhoan (username, password, role, status, idstaff, idcustomer) VALUES (?, ?, ?, ?, ?, ?)";

        try (Connection conn = MyConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setString(1, idStaff);
            pstmt.setString(2, PasswordUtil.hash(defaultPass));
            pstmt.setString(3, role.name());
            pstmt.setString(4, "ACTIVE");
            pstmt.setString(5, idStaff);
            pstmt.setString(6, null); // idCustomer
            return pstmt.executeUpdate() > 0;

        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    public boolean updateStaffRole(String idStaff, Role role) {
        String sql = """
            UPDATE taikhoan
            SET role = ?
            WHERE idstaff = ?
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
                PreparedStatement pstmt = conn.prepareStatement(sql)
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

    public boolean isPhoneExisted(String phoneNumber) {
        String sql = """
            SELECT 1
            FROM KhachHang
            WHERE SDT = ?
            LIMIT 1
            """;

        try (
                Connection conn = MyConnection.getConnection();
                PreparedStatement pstmt = conn.prepareStatement(sql)
        ) {
            pstmt.setString(1, phoneNumber);

            try (ResultSet rs = pstmt.executeQuery()) {
                return rs.next();
            }

        } catch (SQLException e) {
            throw new RuntimeException("Không thể kiểm tra số điện thoại đã đăng ký.", e);
        }
    }

    public boolean registerCustomer(String username, String password, String firstName, String lastName,
                                    String address, String phoneNumber, LocalDate dob, Role role, String email) {
        String customerSql = """
                INSERT INTO KhachHang (MaKH, Ho, Ten, DiaChi, SDT, NgaySinh)
                VALUES (?, ?, ?, ?, ?, ?)
                """;
        String accountSql = """
                INSERT INTO taikhoan (username, password, role, status, idcustomer, email)
                VALUES (?, ?, ?, ?, ?, ?)
                """;

        try (Connection conn = MyConnection.getConnection()) {
            conn.setAutoCommit(false);
            try {
                String customerId = generateCustomerId(conn);

                try (PreparedStatement pstmt = conn.prepareStatement(customerSql)) {
                    pstmt.setString(1, customerId);
                    pstmt.setString(2, firstName);
                    pstmt.setString(3, lastName);
                    pstmt.setString(4, address);
                    pstmt.setString(5, phoneNumber);
                    pstmt.setDate(6, java.sql.Date.valueOf(dob));
                    if (pstmt.executeUpdate() != 1) {
                        conn.rollback();
                        return false;
                    }
                }

                try (PreparedStatement pstmt = conn.prepareStatement(accountSql)) {
                    pstmt.setString(1, username);
                    pstmt.setString(2, PasswordUtil.hash(password));
                    pstmt.setString(3, role.name());
                    pstmt.setString(4, "ACTIVE");
                    pstmt.setString(5, customerId);
                    pstmt.setString(6, email);
                    if (pstmt.executeUpdate() != 1) {
                        conn.rollback();
                        return false;
                    }
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
            e.printStackTrace();
            return false;
        }
    }

    private String generateCustomerId(Connection conn) throws SQLException {
        String sql = """
                SELECT COALESCE(MAX(CAST(SUBSTRING(MaKH, 3) AS UNSIGNED)), 0) + 1 AS nextId
                FROM KhachHang
                WHERE MaKH REGEXP '^KH[0-9]+$'
                """;
        try (Statement statement = conn.createStatement();
             ResultSet rs = statement.executeQuery(sql)) {
            if (!rs.next()) {
                throw new SQLException("Không thể tạo mã khách hàng.");
            }
            int nextId = rs.getInt("nextId");
            if (nextId > 999) {
                throw new SQLException("Đã vượt quá số lượng mã khách hàng KHxxx có thể tạo.");
            }
            return String.format("KH%03d", nextId);
        }
    }

    public List<AccountDTO> getAllAccounts() {
        String sql = "SELECT * FROM taikhoan ORDER BY idaccount";
        List<AccountDTO> accounts = new ArrayList<>();
        try (Connection conn = MyConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql);
             ResultSet rs = pstmt.executeQuery()) {

            while (rs.next()) {
                accounts.add(new AccountDTO(
                        rs.getInt("idaccount"),
                        rs.getString("username"),
                        rs.getString("password"),
                        Role.valueOf(
                                rs.getString("role").toUpperCase().trim()
                        ),
                        rs.getString("status"),
                        rs.getString("idstaff"),
                        rs.getString("idcustomer"),
                        rs.getString("email")
                ));
            }
        } catch (SQLException e) {
            throw new RuntimeException("Không thể tải danh sách tài khoản.", e);
        }
        return accounts;
    }

    public boolean existsByUsername(String username) {
        String sql = "SELECT 1 FROM taikhoan WHERE username = ? LIMIT 1";
        try (
                Connection conn = MyConnection.getConnection();
                PreparedStatement pstmt = conn.prepareStatement(sql)
        ) {
            pstmt.setString(1, username);

            try (ResultSet rs = pstmt.executeQuery()) {
                return rs.next();
            }
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }
}