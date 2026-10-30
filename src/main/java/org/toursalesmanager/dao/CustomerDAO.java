package org.toursalesmanager.dao;

import org.toursalesmanager.dto.CustomerDTO;
import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class CustomerDAO {
    public ArrayList<CustomerDTO> getAllCustomers() {
        ArrayList<CustomerDTO> dsKH = new ArrayList<>();
        String sql = "SELECT * FROM KhachHang";
        try (Connection conn = MyConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql);
             ResultSet rs = pstmt.executeQuery()) {
            while (rs.next()) {
                CustomerDTO dto = mapToKhachHang(rs);
                dsKH.add(dto);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return dsKH;
    }

    public String getNextCustomerId() {
        String sql = "SELECT MaKH FROM KhachHang";
        Pattern customerIdPattern = Pattern.compile("^KH([0-9]+)$", Pattern.CASE_INSENSITIVE);
        long maxSequence = 0;
        int sequenceWidth = 3;

        try (Connection conn = MyConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql);
             ResultSet rs = pstmt.executeQuery()) {
            while (rs.next()) {
                Matcher matcher = customerIdPattern.matcher(rs.getString("MaKH"));
                if (matcher.matches()) {
                    long sequence = Long.parseLong(matcher.group(1));
                    maxSequence = Math.max(maxSequence, sequence);
                    sequenceWidth = Math.max(sequenceWidth, matcher.group(1).length());
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException("Không thể tạo mã khách hàng tiếp theo", e);
        }

        long nextSequence = Math.addExact(maxSequence, 1);
        return String.format(
                Locale.ROOT,
                "KH%0" + sequenceWidth + "d",
                nextSequence
        );
    }

    public boolean addCustomer(CustomerDTO dto) {
        String sql = "INSERT INTO KhachHang (MaKH, Ho, Ten, DiaChi, SDT, NgaySinh) VALUES (?, ?, ?, ?, ?, ?)";
        try (Connection conn = MyConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, dto.getIdCustomer());
            pstmt.setString(2, dto.getFirstName());
            pstmt.setString(3, dto.getLastName());
            pstmt.setString(4, dto.getAddress());
            pstmt.setString(5, dto.getPhoneNumber());
            pstmt.setDate(6, Date.valueOf(dto.getDob()));
            return pstmt.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    public CustomerDTO findCustomerWithId(String maKH) {
        String sql = "SELECT * FROM KhachHang WHERE MaKH = ?";
        try (Connection conn = MyConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, maKH);
            try (ResultSet rs = pstmt.executeQuery()) {
                if (rs.next()) {
//                    System.out.println("chay toi dao");
                    return mapToKhachHang(rs);
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    public List<CustomerDTO> findCustomer(String column, String keyword) {
        List<CustomerDTO> list = new ArrayList<>();
        String sql = "SELECT * FROM KhachHang WHERE " + column + " LIKE ?";
        try (Connection conn = MyConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, "%" + keyword + "%");
            try (ResultSet rs = pstmt.executeQuery()) {
                while (rs.next()) {
                    list.add(mapToKhachHang(rs));
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return list;
    }

    public boolean deleteCustomer(String maKH) {
        String sql = "DELETE FROM KhachHang WHERE MaKH = ?";
        try (Connection conn = MyConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, maKH);
            return pstmt.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    public boolean editCustomer(CustomerDTO dto) {
        String sql = "UPDATE KhachHang SET Ho = ?, Ten = ?, DiaChi = ?, SDT = ?, NgaySinh = ? WHERE MaKH = ?";
        try (Connection conn = MyConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, dto.getFirstName());
            pstmt.setString(2, dto.getLastName());
            pstmt.setString(3, dto.getAddress());
            pstmt.setString(4, dto.getPhoneNumber());
            pstmt.setDate(5, Date.valueOf(dto.getDob()));
            pstmt.setString(6, dto.getIdCustomer());
            return pstmt.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    private CustomerDTO mapToKhachHang(ResultSet rs) throws SQLException {
        String maKH = rs.getString("MaKH");
        String ho = rs.getString("Ho");
        String ten = rs.getString("Ten");
        String diaChi = rs.getString("DiaChi");
        String sdt = rs.getString("SDT");
        Date dob = rs.getDate("NgaySinh");
        LocalDate ngaySinh = dob == null ? null : dob.toLocalDate();

        return new CustomerDTO(maKH, ho, ten, diaChi, sdt, ngaySinh);
    }
}
