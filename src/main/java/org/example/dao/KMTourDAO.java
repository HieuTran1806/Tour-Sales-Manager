package org.example.dao;

import org.example.dto.KMTourDTO;

import java.sql.*;
import java.time.LocalDate;
import java.util.*;
public class KMTourDAO {
    public ArrayList<KMTourDTO> getDsKMTour() {

        ArrayList<KMTourDTO> list = new ArrayList<>();

        String sql = "SELECT * FROM CTrinhKM km " +
                "JOIN KMTour_CHITIET ct ON km.maKM = ct.maKM " +
                "WHERE km.hinhThucKM = 0";

        try (Connection conn = MyConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {

            Map<String, KMTourDTO> map = new HashMap<>();

            while (rs.next()) {

                String maKM = rs.getString("maKM");

                KMTourDTO km;

                if (!map.containsKey(maKM)) {

                    km = new KMTourDTO(
                            rs.getString("maKM"),
                            rs.getString("tenKM"),
                            LocalDate.parse(rs.getDate("ngayBD").toString()),
                            LocalDate.parse(rs.getDate("ngayKT").toString()),
                            rs.getBoolean("hinhThucKM"),
                            rs.getFloat("chietKhau"),
                            rs.getString("ghiChu"),
                            new ArrayList<>()
                    );

                    map.put(maKM, km);

                } else {
                    km = map.get(maKM);
                }

                String maTour = rs.getString("maTour");

                if (maTour != null) {
                    km.getDsMaTour().add(maTour);
                }
            }

            list.addAll(map.values());

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return list;
    }

    public KMTourDTO timKMTour(String maKM) {

        KMTourDTO km = null;

        String sql = "SELECT * FROM CTrinhKM km " +
                "JOIN KMTour_CHITIET ct ON km.maKM = ct.maKM " +
                "WHERE km.maKM=?";

        try (Connection conn = MyConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setString(1, maKM);

            ResultSet rs = ps.executeQuery();

            while (rs.next()) {

                if (km == null) {

                    km = new KMTourDTO(
                            rs.getString("maKM"),
                            rs.getString("tenKM"),
                            LocalDate.parse(rs.getDate("ngayBD").toString()),
                            LocalDate.parse(rs.getDate("ngayKT").toString()),
                            rs.getBoolean("hinhThucKM"),
                            rs.getFloat("chietKhau"),
                            rs.getString("ghiChu"),
                            new ArrayList<>()
                    );
                }

                String maTour = rs.getString("maTour");

                if (maTour != null) {
                    km.getDsMaTour().add(maTour);
                }
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return km;
    }

    public KMTourDTO maptoKMTour(ResultSet rs) throws SQLException {
        return new KMTourDTO(
                rs.getString("maKM"),
                rs.getString("tenKM"),
                LocalDate.parse(rs.getDate("ngayBD").toString()),
                LocalDate.parse(rs.getDate("ngayKT").toString()),
                rs.getBoolean("hinhThucKM"),
                rs.getFloat("chietKhau"),
                rs.getString("ghiChu"),
                new ArrayList<>()
        );
    }

    public boolean themKMTour(KMTourDTO kmTour) {

        try (Connection conn = MyConnection.getConnection()) {

            conn.setAutoCommit(false);

            String sql2 = "INSERT INTO kmtour_chitiet VALUES (?,?)";

            for (String maTour : kmTour.getDsMaTour()) {

                PreparedStatement ps2 = conn.prepareStatement(sql2);

                ps2.setString(1, kmTour.getMaKM());
                ps2.setString(2, maTour);

                ps2.executeUpdate();
            }

            conn.commit();
            return true;

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return false;
    }

    public boolean xoaKMTour(String maKM) {

        try (Connection conn = MyConnection.getConnection()) {

            conn.setAutoCommit(false);

            PreparedStatement ps1 =
                    conn.prepareStatement("DELETE FROM kmtour_chitiet WHERE maKM=?");

            ps1.setString(1, maKM);
            ps1.executeUpdate();

            PreparedStatement ps2 =
                    conn.prepareStatement("DELETE FROM ctrinhkm WHERE maKM=?");

            ps2.setString(1, maKM);
            ps2.executeUpdate();

            conn.commit();

            return true;

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return false;
    }

    public boolean suaKMTour(KMTourDTO kmTour) {

        try (Connection conn = MyConnection.getConnection()) {

            conn.setAutoCommit(false);

            String sql = """
                    UPDATE ctrinhkm
                    SET tenKM=?, ngayBD=?, ngayKT=?, chietKhau=?, ghiChu=?
                    WHERE maKM=?
                    """;

            PreparedStatement ps = conn.prepareStatement(sql);

            ps.setString(1, kmTour.getTenKM());
            ps.setDate(2, java.sql.Date.valueOf(kmTour.getNgayBD()));
            ps.setDate(3, java.sql.Date.valueOf(kmTour.getNgayKT()));
            ps.setFloat(4, kmTour.getChietKhau());
            ps.setString(5, kmTour.getGhiChu());
            ps.setString(6, kmTour.getMaKM());

            ps.executeUpdate();

            PreparedStatement ps2 =
                    conn.prepareStatement("DELETE FROM kmtour_chitiet WHERE maKM=?");

            ps2.setString(1, kmTour.getMaKM());
            ps2.executeUpdate();

            String sql3 = "INSERT INTO kmtour_chitiet VALUES (?,?)";

            for (String maTour : kmTour.getDsMaTour()) {

                PreparedStatement ps3 = conn.prepareStatement(sql3);

                ps3.setString(1, kmTour.getMaKM());
                ps3.setString(2, maTour);

                ps3.executeUpdate();
            }

            conn.commit();
            return true;

        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }
}