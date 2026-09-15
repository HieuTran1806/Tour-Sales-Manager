package org.example.dao;

import org.example.dto.KMHDDTO;

import java.sql.*;
import java.time.LocalDate;
import java.util.*;

public class KMHDDAO {
    public ArrayList<KMHDDTO> getDsKMHD() {
        ArrayList<KMHDDTO> list = new ArrayList<>();
        String sql="SELECT * FROM CTrinhKM km " +
                "JOIN KMHD_CHITIET hd ON km.maKM = hd.maKM " +
                "WHERE km.hinhThucKM = 1";
        try (Connection conn = MyConnection.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            while (rs.next()) {
                KMHDDTO kmhd = new KMHDDTO(
                        rs.getString("maKM"),
                        rs.getString("tenKM"),
                        LocalDate.parse(rs.getDate("ngayBD").toString()),
                        LocalDate.parse(rs.getDate("ngayKT").toString()),
                        rs.getBoolean("hinhThucKM"),
                        rs.getFloat("chietKhau"),
                        rs.getString("ghiChu"),
                        rs.getFloat("tongTienApDung")

                );
                list.add(kmhd);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return list;
    }

    public KMHDDAO() {
    }

    public KMHDDTO timKMHD(String maKM) {
        String sql = "SELECT * FROM CTrinhKM km " +
                "JOIN KMHD_CHITIET hd ON km.maKM = hd.maKM " +
                "WHERE km.maKM = ?";
        try (Connection conn = MyConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, maKM);
            try (ResultSet rs = pstmt.executeQuery()) {
                if (rs.next()) {
                    return new KMHDDTO(
                            rs.getString("maKM"),
                            rs.getString("tenKM"),
                            LocalDate.parse(rs.getDate("ngayBD").toString()),
                            LocalDate.parse(rs.getDate("ngayKT").toString()),
                            rs.getBoolean("hinhThucKM"),
                            rs.getFloat("chietKhau"),
                            rs.getString("ghiChu"),
                            rs.getFloat("tongTienApDung")
                    );
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    public KMHDDTO maptoKMHD(ResultSet rs) throws SQLException {
        return new KMHDDTO(
                rs.getString("maKM"),
                rs.getString("tenKM"),
                LocalDate.parse(rs.getDate("ngayBD").toString()),
                LocalDate.parse(rs.getDate("ngayKT").toString()),
                rs.getBoolean("hinhThucKM"),
                rs.getFloat("chietKhau"),
                rs.getString("ghiChu"),
                rs.getFloat("tongTienApDung")
        );
    }

    public ArrayList<KMHDDTO> getDsKMHDTheoNgay(String ngay) {
        String sql = "SELECT * FROM CTrinhKM km " +
                "JOIN KMHD_CHITIET hd ON km.maKM = hd.maKM " +
                "WHERE km.ngayBD <= ? AND km.ngayKT >= ?";
        ArrayList<KMHDDTO> list = new ArrayList<>();
        try (Connection conn = MyConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, ngay);
            pstmt.setString(2, ngay);
            try (ResultSet rs = pstmt.executeQuery()) {
                while (rs.next()) {
                    KMHDDTO kmhd = new KMHDDTO(
                            rs.getString("maKM"),
                            rs.getString("tenKM"),
                            LocalDate.parse(rs.getDate("ngayBD").toString()),
                            LocalDate.parse(rs.getDate("ngayKT").toString()),
                            rs.getBoolean("hinhThucKM"),
                            rs.getFloat("chietKhau"),
                            rs.getString("ghiChu"),
                            rs.getFloat("tongTienApDung")
                    );
                    list.add(kmhd);
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
            return null;
        }
        return list;
    }


    public boolean themKMHD(KMHDDTO kmhd) {
        String sqlHD = "INSERT INTO KMHD_CHITIET VALUES (?, ?)";
        try (Connection conn = MyConnection.getConnection()) {

            conn.setAutoCommit(false);


            try (PreparedStatement p2 = conn.prepareStatement(sqlHD)) {
                p2.setString(1, kmhd.getMaKM());
                p2.setFloat(2, kmhd.getTongTienApDung());
                p2.executeUpdate();
            }

            conn.commit();
            return true;

        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }


    public boolean xoaKMHD(String maKM) {
        String sql = "DELETE FROM CTrinhKM WHERE maKM = ?";
        try (Connection conn = MyConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, maKM);
            int rowsAffected = pstmt.executeUpdate();
            return rowsAffected > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    public boolean suaKMHD(KMHDDTO kmhd) {
        String sqlKM = "UPDATE CTrinhKM SET tenKM=?, ngayBD=?, ngayKT=?, chietKhau=?, ghiChu=? WHERE maKM=?";
        String sqlHD = "UPDATE KMHD_CHITIET SET tongTienApDung=? WHERE maKM=?";

        try (Connection conn = MyConnection.getConnection()) {

            conn.setAutoCommit(false);

            try (PreparedStatement p1 = conn.prepareStatement(sqlKM)) {
                p1.setString(1, kmhd.getTenKM());
                p1.setDate(2, java.sql.Date.valueOf(kmhd.getNgayBD()));
                p1.setDate(3, java.sql.Date.valueOf(kmhd.getNgayKT()));
                p1.setFloat(4, kmhd.getChietKhau());
                p1.setString(5, kmhd.getGhiChu());
                p1.setString(6, kmhd.getMaKM());
                p1.executeUpdate();
            }

            try (PreparedStatement p2 = conn.prepareStatement(sqlHD)) {
                p2.setFloat(1, kmhd.getTongTienApDung());
                p2.setString(2, kmhd.getMaKM());
                p2.executeUpdate();
            }

            conn.commit();
            return true;

        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }
}