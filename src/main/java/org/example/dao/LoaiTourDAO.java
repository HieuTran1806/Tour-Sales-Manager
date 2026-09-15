package org.example.dao;

import org.example.dto.LoaiTourDTO;
import org.example.dto.TourDTO;

import java.sql.*;
import java.util.ArrayList;

public class LoaiTourDAO {
    Connection c = MyConnection.getConnection();
    Statement st = null;

    public LoaiTourDAO(){
        ArrayList<TourDTO> lsTour = new ArrayList<>();
    }

    //get all tours
    public ArrayList<LoaiTourDTO> getAllLoaiTour(){
        ArrayList<LoaiTourDTO> lsCate = new ArrayList<>();
        try {
            String sql = "select * from loaitour";
            PreparedStatement ps = c.prepareStatement(sql);
            ResultSet rs = ps.executeQuery();

            while(rs.next()){
                LoaiTourDTO t = new LoaiTourDTO(
                        rs.getString(1),
                        rs.getString(2),
                        rs.getString(3),
                        rs.getInt(4)
                );
                lsCate.add(t);
            }

        }catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return lsCate;
    }

    //add
    public boolean addLoaiTour(LoaiTourDTO t){
        try{
            String sql = "Insert into loaitour values(";
            sql += "'" +  t.getMaLoaiTour() + "'";
            sql += ","  + "'" +  t.getTheLoai() + "'";
            sql += ","  + "'" +  t.getMoTa() + "'";
            sql += ","  + "'" +  t.getTrangThai() + "'";
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
    public boolean editLoaiTour(LoaiTourDTO t){
        String id = t.getMaLoaiTour();
        try{
            String qry = "update loaitour set ";
            qry += "theloai = '" + t.getTheLoai() + "'";
            qry += ",mota = '" + t.getMoTa() + "'";
            qry += ",trangthai = '" + t.getTrangThai() + "'";
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
