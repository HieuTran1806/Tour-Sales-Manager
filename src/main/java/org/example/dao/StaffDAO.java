package org.example.dao;

import org.example.dto.StaffDTO;
import org.example.enums.Role;

import java.sql.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class StaffDAO {
    private final AccountDAO accountDAO = new AccountDAO();

    public ArrayList<StaffDTO> getAllStaffs() {

        ArrayList<StaffDTO> dsNV = new ArrayList<>();
        String sql = "SELECT * FROM NhanVien";

        try (Connection conn = MyConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql);
             ResultSet rs = pstmt.executeQuery()) {

            while (rs.next()) {
                dsNV.add(mapToNhanVien(rs));
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return dsNV;
    }

    public boolean themNhanVien(StaffDTO nv, Role role) {

        String sql = "INSERT INTO NhanVien (MaNV, Ho, Ten, DiaChi, SDT, NgaySinh) VALUES (?, ?, ?, ?, ?, ?, ?)";

        try (Connection conn = MyConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setString(1, nv.getIdStaff());
            pstmt.setString(2, nv.getFirstName());
            pstmt.setString(3, nv.getLastName());
            pstmt.setString(4, nv.getAddress());
            pstmt.setString(5, nv.getPhoneNumber());
            pstmt.setDate(6, Date.valueOf(nv.getDob()));

            boolean themNhanVien = pstmt.executeUpdate() > 0;
            if (!themNhanVien) {
                return false;
            }

            return accountDAO.createStaffAccount(nv.getIdStaff(), role);

        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    public StaffDTO timNhanVienTheoMa(String maNV) {

        String sql = "SELECT * FROM NhanVien WHERE MaNV = ?";

        try (Connection conn = MyConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setString(1, maNV);
            ResultSet rs = pstmt.executeQuery();

            if (rs.next()) {
                return mapToNhanVien(rs);
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    public List<StaffDTO> timNhanVien(String searchType, String keyword) {
        List<StaffDTO> list = new ArrayList<>();

        String column = switch (searchType) {
            case "Mã nhân viên" -> "MaNV";
            case "Họ tên"       -> "HoTen";
            case "Số điện thoại" -> "SDT";
            default -> throw new IllegalArgumentException(
                    "Loại tìm kiếm không hợp lệ: " + searchType
            );
        };

        String sql = "SELECT * FROM NhanVien WHERE " + column + " LIKE ?";

        try (Connection con = MyConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setString(1, "%" + keyword.trim() + "%");

            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    list.add(mapToNhanVien(rs));
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException("Không thể tìm kiếm nhân viên", e);
        }

        return list;
    }

    public boolean updateStaff(StaffDTO nv, Role role) {
        String sql = "UPDATE NhanVien SET Ho = ?, Ten = ?, DiaChi = ?, SDT = ?, NgaySinh = ? WHERE MaNV = ?";

        try (Connection conn = MyConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

                        pstmt.setString(1, nv.getFirstName());
                        pstmt.setString(2, nv.getLastName());
                        pstmt.setString(3, nv.getAddress());
                        pstmt.setString(4, nv.getPhoneNumber());

                        if (nv.getDob() != null) {
                            pstmt.setDate(5, Date.valueOf(nv.getDob()));
                        } else {
                            pstmt.setNull(5, Types.DATE);
                        }

                        pstmt.setString(6, nv.getIdStaff());
            return pstmt.executeUpdate() > 0;

        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    public boolean xoaNhanVien(String maNV) {

        String sql = "DELETE FROM NhanVien WHERE MaNV = ?";

        try (Connection conn = MyConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setString(1, maNV);
            return pstmt.executeUpdate() > 0;

        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    private StaffDTO mapToNhanVien(ResultSet rs) throws SQLException {
        String maNV = rs.getString("MaNV");
        String ho = rs.getString("Ho");
        String ten = rs.getString("Ten");
        String diaChi = rs.getString("DiaChi");
        String sdt = rs.getString("SDT");
        LocalDate ngaySinh = rs.getDate("NgaySinh").toLocalDate();

        return new StaffDTO(maNV, ho, ten, diaChi, sdt, ngaySinh);
    }

    public ArrayList<StaffDTO> getAllStaffWithRole() {
        ArrayList<StaffDTO> result =
                new ArrayList<>();

        String sql = """
            SELECT
                nv.MaNV,
                nv.Ho,
                nv.Ten,
                nv.DiaChi,
                nv.SDT,
                nv.NgaySinh,
                tk.role
            FROM NhanVien nv
            LEFT JOIN TaiKhoan tk
                ON tk.idstaff = nv.MaNV
            """;

        try (
                Connection conn =
                        MyConnection.getConnection();

                PreparedStatement pstmt =
                        conn.prepareStatement(sql);

                ResultSet rs =
                        pstmt.executeQuery()
        ) {
            while (rs.next()) {
                Date dobValue =
                        rs.getDate("NgaySinh");

                LocalDate dob = dobValue == null
                        ? null
                        : dobValue.toLocalDate();

                StaffDTO staff = new StaffDTO(
                        rs.getString("MaNV"),
                        rs.getString("Ho"),
                        rs.getString("Ten"),
                        rs.getString("SDT"),
                        rs.getString("DiaChi"),
                        dob
                );

                Role role = parseNullableRole(
                        rs.getString("role")
                );
                staff.setRole(role);

                result.add(staff);
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return result;
    }

    private Role parseNullableRole(String value)
            throws SQLException {

        if (value == null || value.isBlank()) {
            return null;
        }

        try {
            return Role.valueOf(
                    value.trim().toUpperCase()
            );
        } catch (IllegalArgumentException e) {
            throw new SQLException(
                    "Role không hợp lệ: " + value,
                    e
            );
        }
    }
}