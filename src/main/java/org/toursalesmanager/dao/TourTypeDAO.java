package org.toursalesmanager.dao;

import org.toursalesmanager.dto.TourTypeDTO;
import org.toursalesmanager.dto.TourDTO;

import java.sql.*;
import java.util.ArrayList;

public class TourTypeDAO {
    Connection c = MyConnection.getConnection();
    Statement st = null;

    public TourTypeDAO(){
        ArrayList<TourDTO> lsTour = new ArrayList<>();
    }

    //get all tours
    public ArrayList<TourTypeDTO> getAllLoaiTour(){
        ArrayList<TourTypeDTO> lsCate = new ArrayList<>();
        try {
            String sql = "select * from loaitour";
            PreparedStatement ps = c.prepareStatement(sql);
            ResultSet rs = ps.executeQuery();

            while(rs.next()){
                TourTypeDTO t = new TourTypeDTO(
                        rs.getString(1),
                        rs.getString(2),
                        rs.getString(3),
                        rs.getString(4)
                );
                lsCate.add(t);
            }

        }catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return lsCate;
    }

    //add
    public boolean addLoaiTour(TourTypeDTO t){
        try{
            String sql = "Insert into loaitour values(";
            sql += "'" +  t.getIdTourType() + "'";
            sql += ","  + "'" +  t.getTypeOfTour() + "'";
            sql += ","  + "'" +  t.getDescription() + "'";
            sql += ","  + "'" +  t.getStatus() + "'";
            sql += ")";
            st = c.createStatement();
            st.executeUpdate(sql);
            return true;
        } catch (SQLException e) {
            return false;
        }
    }

    public boolean removeLoaiTour(String maLoaiTour){
        try{
            String sql = "delete from loaitour where maloaitour = ?";
            PreparedStatement ps = c.prepareStatement(sql);
            ps.setString(1, maLoaiTour);
            int rows = ps.executeUpdate();
            return rows > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    //edit
    public boolean editLoaiTour(TourTypeDTO t){
        String id = t.getIdTourType();
        try{
            String qry = "update loaitour set ";
            qry += "theloai = '" + t.getTypeOfTour() + "'";
            qry += ",mota = '" + t.getDescription() + "'";
            qry += ",trangthai = '" + t.getStatus() + "'";
            qry += "where maloaitour = '" + id + "';";
            st = c.createStatement();
            st.executeUpdate(qry);
            return true;
        }catch (SQLException e){
            e.printStackTrace();
            return false;
        }
    }
}
