package org.example.dao;

import org.example.dto.KeHoachTourDTO;

import java.sql.*;
import java.util.ArrayList;

public class KeHoachTourDAO {
    Connection c = MyConnection.getConnection();
    Statement st = null;

    public KeHoachTourDAO(){
        ArrayList<KeHoachTourDTO> lsKeHoachTour;
    }

    //get all ke hoach tours
    public ArrayList<KeHoachTourDTO> getAllKeHoachTours(){
        ArrayList<KeHoachTourDTO> lsKeHoachTour = new ArrayList<>();
        try {
            String sql = "select * from kehoachtour";
            PreparedStatement ps = c.prepareStatement(sql);
            ResultSet rs = ps.executeQuery();

            while(rs.next()){
                KeHoachTourDTO t = new KeHoachTourDTO(
                        rs.getString("maKHTour"),
                        rs.getDate("ngayKhoiHanh").toLocalDate(),
                        rs.getDate("ngayKetThuc").toLocalDate(),
                        rs.getInt("tongSoVe"),
                        rs.getLong("tongChiDuKien"),
                        rs.getLong("tongThuDuKien"),
                        rs.getInt("soVeConLai"),
                        rs.getString("trangThai"),
                        rs.getString("maTour"),
                        rs.getString("maNVHD")
                );
                lsKeHoachTour.add(t);
            }
        }catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return lsKeHoachTour;
    }

    //add
    public boolean addKeHoachTour(KeHoachTourDTO t){
        try{
            String sql = "Insert into kehoachtour values(";
            sql += "'" +  t.getMaKHTour() + "'";
            sql += ","  + "'" +  t.getNgayKhoiHanh() + "'";
            sql += ","  + "'" +  t.getNgayKetThuc() + "'";
            sql += ","  + "'" +  t.getTongSoVe() + "'";
            sql += ","  + "'" +  t.getTongChiDuKien() + "'";
            sql += ","  + "'" +  t.getTongThuDuKien() + "'";
            sql += ","  + "'" +  t.getSoVeConLai() + "'";
            sql += ","  + "'" +  t.getTrangThai() + "'";
            sql += ","  + "'" +  t.getMaTour() + "'";
            sql += ","  + "'" +  t.getMaNVHD() + "'";
            sql += ")";

            st = c.createStatement();
            st.executeUpdate(sql);
            return true;
        } catch (SQLException e) {
            return false;
        }
    }

    public boolean removeKeHoachTour(String maKeHoachTour){
        try{
            String sql = "delete from kehoachtour where makhtour = ?";
            PreparedStatement ps = c.prepareStatement(sql);
            ps.setString(1, maKeHoachTour);

            int rows = ps.executeUpdate();
            return rows > 0;
        } catch (SQLException e) {
            return false;
        }
    }

    //edit
    public boolean editKeHoachTour(KeHoachTourDTO t){
        String id = t.getMaKHTour();

        try{
            String qry = "update kehoachtour set ";
            qry += "ngaykhoihanh = " + "'" + t.getNgayKhoiHanh() + "'";
            qry += ",ngayketthuc = " + "'" + t.getNgayKetThuc() + "'";
            qry += ",tongsove = " + "'" + t.getTongSoVe() + "'";
            qry += ",tongchidukien = " + "'" + t.getTongChiDuKien() + "'";
            qry += ",tongthudukien = " + "'" + t.getTongThuDuKien() + "'";
            qry += ",soveconlai = " + "'" + t.getSoVeConLai() + "'";
            qry += ",trangthai = " + "'" + t.getTrangThai() + "'";
            qry += ",matour = " + "'" + t.getMaTour() + "'";
            qry += ",manvhd = " + "'" + t.getMaNVHD() + "'";
            qry += "where makhtour = '" + id + "';";

            st = c.createStatement();
            st.executeUpdate(qry);
            return true;
        }catch (SQLException e){
            e.printStackTrace();
            return false;
        }
    }

    public boolean capNhatSoluong(int sl, String makhtour){
        String sql="Update kehoachtour set tongsove=tongsove-? where makhtour=?";

        try(Connection conn= MyConnection.getConnection();
            PreparedStatement ps=conn.prepareStatement(sql)){
            ps.setInt(1,sl);
            ps.setString(2,makhtour);
            ps.executeUpdate();
        }catch (SQLException e){
            e.printStackTrace();
            return false;
        }
        return true;
    }
}
