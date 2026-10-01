package org.example.dao;

import org.example.dto.TourPlanDetailDTO;

import java.sql.*;
import java.time.LocalDateTime;
import java.util.ArrayList;

public class TourPlanDetailDAO {
    Connection c = MyConnection.getConnection();
    Statement st = null;

    public TourPlanDetailDAO(){
        ArrayList<TourPlanDetailDAO> lsCTietKHTours = new ArrayList<>();
    }

    //get all tours
    public ArrayList<TourPlanDetailDTO> getAllCTietKHTours(){
        ArrayList<TourPlanDetailDTO> lsCTietKHTours = new ArrayList<>();
        try {
            String sql = "select * from ctietkhtour";
            PreparedStatement ps = c.prepareStatement(sql);
            ResultSet rs = ps.executeQuery();

            LocalDateTime dateTime = rs.getObject(2, LocalDateTime.class);

            while(rs.next()){
                TourPlanDetailDTO t = new TourPlanDetailDTO(
                        rs.getString(1),
                        dateTime,
                        rs.getBigDecimal(3),
                        rs.getBigDecimal(4),
                        rs.getBigDecimal(5),
                        rs.getBigDecimal(6),
                        rs.getString(7),
                        rs.getString(8),
                        rs.getString(9)
                );
                lsCTietKHTours.add(t);
            }

        }catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return lsCTietKHTours;
    }

    //add
    public boolean addCTietKHTour(TourPlanDetailDTO t){
        try{
            String sql = "Insert into ctietkhtour values(";
            sql += "'" +  t.getIdTourPlanDetail() + "'";
            sql += ","  + "'" +  t.getDate() + "'";
            sql += ","  + "'" +  t.getTotalExpenditure() + "'";
            sql += ","  + "'" +  t.getEatingCost() + "'";
            sql += ","  + "'" +  t.getEatingCost() + "'";
            sql += ","  + "'" +  t.getTravelingCost() + "'";
            sql += ","  + "'" +  t.getDepartureLocation() + "'";
            sql += ","  + "'" +  t.getEndLocation() + "'";
            sql += ","  + "'" +  t.getIdTourPlan() + "'";
            sql += ")";
            st = c.createStatement();
            st.executeUpdate(sql);
            return true;
        } catch (SQLException e) {
            return false;
        }
    }

    public boolean removeCTietKHTour(String maCTietKHTour){
        try{
            String sql = "delete from ctietkhtour where mactietkhtour = ?";
            PreparedStatement ps = c.prepareStatement(sql);
            ps.setString(1, maCTietKHTour);

            int rows = ps.executeUpdate();
            return rows > 0;
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    //edit
    public boolean editCTietKHTour(TourPlanDetailDTO t){
        String id = t.getIdTourPlanDetail();

        try{
            String qry = "update ctietkhtour set ";
            qry += "ngaythuchien = " + "'" + t.getDate() + "'";
            qry += ",tongchi = " + "'" + t.getTotalExpenditure() + "'";
            qry += ",tieno = " + "'" + t.getHousingCost() + "'";
            qry += ",tienan = " + "'" + t.getEatingCost() + "'";
            qry += ",tiendilai = " + "'" + t.getTravelingCost() + "'";
            qry += ",diemdi = " + "'" + t.getDepartureLocation() + "'";
            qry += ",diemden = " + "'" + t.getEndLocation() + "'";
            qry += ",makhtour = " + "'" + t.getIdTourPlan() + "'";
            qry += "where mactietkhtour = '" + id + "';";
            st = c.createStatement();
            st.executeUpdate(qry);
            return true;
        }catch (SQLException e){
            e.printStackTrace();
            return false;
        }
    }
}
