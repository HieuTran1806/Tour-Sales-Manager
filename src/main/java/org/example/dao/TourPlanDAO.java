package org.example.dao;

import org.example.dto.TourPlanDTO;

import java.sql.*;
import java.util.ArrayList;

public class TourPlanDAO {
    Connection c = MyConnection.getConnection();
    Statement st = null;

    public TourPlanDAO(){
        ArrayList<TourPlanDTO> lsKeHoachTour;
    }

    //get all ke hoach tours
    public ArrayList<TourPlanDTO> getAllKeHoachTours(){
        ArrayList<TourPlanDTO> lsKeHoachTour = new ArrayList<>();
        try {
            String sql = "select * from kehoachtour";
            PreparedStatement ps = c.prepareStatement(sql);
            ResultSet rs = ps.executeQuery();

            while(rs.next()){
                TourPlanDTO t = new TourPlanDTO(
                        rs.getString("maKHTour"),
                        rs.getDate("ngayKhoiHanh").toLocalDate(),
                        rs.getDate("ngayKetThuc").toLocalDate(),
                        rs.getInt("tongSoVe"),
                        rs.getBigDecimal("tongChiDuKien"),
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
    public boolean addKeHoachTour(TourPlanDTO t){
        try{
            String sql = "Insert into kehoachtour values(";
            sql += "'" +  t.getIdTour() + "'";
            sql += ","  + "'" +  t.getDepartureDate() + "'";
            sql += ","  + "'" +  t.getEndDate() + "'";
            sql += ","  + "'" +  t.getTickets() + "'";
            sql += ","  + "'" +  t.getEstimatedCost() + "'";
            sql += ","  + "'" +  t.getRemainingTickets() + "'";
            sql += ","  + "'" +  t.getStatus() + "'";
            sql += ","  + "'" +  t.getIdTour() + "'";
            sql += ","  + "'" +  t.getIdTourGuide() + "'";
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
    public boolean editKeHoachTour(TourPlanDTO t){
        String id = t.getIdTour();

        try{
            String qry = "update kehoachtour set ";
            qry += "ngaykhoihanh = " + "'" + t.getDepartureDate() + "'";
            qry += ",ngayketthuc = " + "'" + t.getEndDate() + "'";
            qry += ",tongsove = " + "'" + t.getTickets() + "'";
            qry += ",tongchidukien = " + "'" + t.getEstimatedCost() + "'";
            qry += ",soveconlai = " + "'" + t.getRemainingTickets() + "'";
            qry += ",trangthai = " + "'" + t.getStatus() + "'";
            qry += ",matour = " + "'" + t.getIdTour() + "'";
            qry += ",manvhd = " + "'" + t.getIdTourGuide() + "'";
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
