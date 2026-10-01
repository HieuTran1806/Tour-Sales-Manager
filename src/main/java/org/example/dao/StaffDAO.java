package org.example.dao;

import org.example.dto.StaffDTO;

import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
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

    public boolean themNhanVien(StaffDTO nv) {

        String sql = "INSERT INTO NhanVien (MaNV, ChucVu, Ho, Ten, DiaChi, SDT, NgaySinh) VALUES (?, ?, ?, ?, ?, ?, ?)";

        try (Connection conn = MyConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setString(1, nv.getIdStaff());
            pstmt.setString(2, nv.getRole());
            pstmt.setString(3, nv.getFirstName());
            pstmt.setString(4, nv.getLastName());
            pstmt.setString(5, nv.getAddress());
            pstmt.setString(6, nv.getPhoneNumber());
            pstmt.setDate(7, Date.valueOf(nv.getDob()));

            boolean themNhanVien = pstmt.executeUpdate() > 0;
            if (!themNhanVien) {
                return false;
            }
            return accountDAO.createStaffAccount(nv.getIdStaff(), nv.getRole());

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

    public List<StaffDTO> timNhanVien(String column, String keyword) {
        List<StaffDTO> list = new ArrayList<>();

        String sql = "SELECT * FROM NhanVien WHERE " + column + " LIKE ?";

        try (Connection con = MyConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setString(1, "%" + keyword + "%");

            ResultSet rs = ps.executeQuery();

            while (rs.next()) {
                list.add(mapToNhanVien(rs));
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

        return list;
    }

    public boolean capNhatNhanVien(StaffDTO nv) {
        String sql = "UPDATE NhanVien SET ChucVu = ?, Ho = ?, Ten = ?, DiaChi = ?, SDT = ?, NgaySinh = ? WHERE MaNV = ?";

        try (Connection conn = MyConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setString(1, nv.getRole());
            pstmt.setString(2, nv.getFirstName());
            pstmt.setString(3, nv.getLastName());
            pstmt.setString(4, nv.getAddress());
            pstmt.setString(5, nv.getPhoneNumber());
            pstmt.setDate(6, Date.valueOf(nv.getDob()));
            pstmt.setString(7, nv.getIdStaff());

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

    public boolean suaNhanVien(StaffDTO nv) {
        String sql = "UPDATE NhanVien SET ChucVu = ?, Ho = ?, Ten = ?, DiaChi = ?, SDT = ?, NgaySinh = ? WHERE MaNV = ?";

        try (Connection conn = MyConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setString(1, nv.getRole());
            pstmt.setString(2, nv.getFirstName());
            pstmt.setString(3, nv.getLastName());
            pstmt.setString(4, nv.getAddress());
            pstmt.setString(5, nv.getPhoneNumber());
            pstmt.setDate(6, Date.valueOf(nv.getDob()));
            pstmt.setString(7, nv.getIdStaff());

            return pstmt.executeUpdate() > 0;

        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    private StaffDTO mapToNhanVien(ResultSet rs) throws SQLException {
        String maNV = rs.getString("MaNV");
        String chucVu = rs.getString("ChucVu");
        String ho = rs.getString("Ho");
        String ten = rs.getString("Ten");
        String diaChi = rs.getString("DiaChi");
        String sdt = rs.getString("SDT");
        LocalDate ngaySinh = rs.getDate("NgaySinh").toLocalDate();

        return new StaffDTO(maNV, chucVu, ho, ten, diaChi, sdt, ngaySinh);
    }
}