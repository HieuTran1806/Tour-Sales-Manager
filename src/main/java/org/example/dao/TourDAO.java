package org.example.dao;

import org.example.dto.TourDTO;

import java.sql.*;
import java.util.ArrayList;

public class TourDAO {
    Connection c = MyConnection.getConnection();

    public TourDAO(){
        ArrayList<TourDTO> lsTour = new ArrayList<>();
    }

    //get all tours
    public ArrayList<TourDTO> getAllTours(){
        ArrayList<TourDTO> lsTour = new ArrayList<>();
        try {
            String sql = "select * from tour";
            PreparedStatement ps = c.prepareStatement(sql);
            ResultSet rs = ps.executeQuery();

            while(rs.next()){
                TourDTO t = new TourDTO(
                        rs.getString(1),
                        rs.getString(2),
                        rs.getInt(3),
                        rs.getLong(4),
                        rs.getInt(5),
                        rs.getString(6),
                        rs.getString(7),
                        rs.getString(8),
                        rs.getString(9)
                );
                lsTour.add(t);
            }
        }catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return lsTour;
    }

    //add
    public boolean addTour(TourDTO t){
        String sql = "INSERT INTO tour (matour, ten, songay, dongia, socho, ddkhoihanh, imglink, maloaitour, madiadiem) "
                + "VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?)";

        try {
            PreparedStatement pst = c.prepareStatement(sql); // use PreparedStatement

            pst.setString(1, t.getMaTour());
            pst.setString(2, t.getTen());
            pst.setInt(3, t.getSoNgay());
            pst.setLong(4, t.getDonGia());
            pst.setInt(5, t.getSoCho());
            pst.setString(6, t.getDiaDiemKhoiHanh());
            pst.setString(7, t.getImgLink());
            pst.setString(8, t.getMaLoaiTour());
            pst.setString(9, t.getMaDiaDiem());

            int rowAffected = pst.executeUpdate();
            return rowAffected > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    public boolean removeTour(String matour){
        try{
            String sql = "delete from tour where matour = ?";
            PreparedStatement ps = c.prepareStatement(sql);
            ps.setString(1, matour);

            int rows = ps.executeUpdate();
            return rows > 0;
        } catch (SQLException e) {
            return false;
        }
    }

    // edit
    public boolean editTour(TourDTO t) {
        String sql = "UPDATE tour SET ten=?, songay=?, dongia=?, socho=?, ddkhoihanh=?, imglink=?, maloaitour=?, madiadiem=? WHERE matour=?";
        try {
            PreparedStatement pst = c.prepareStatement(sql);
            pst.setString(1, t.getTen());
            pst.setInt(2, t.getSoNgay());
            pst.setLong(3, t.getDonGia());
            pst.setInt(4, t.getSoCho());
            pst.setString(5, t.getDiaDiemKhoiHanh());
            pst.setString(6, t.getImgLink());
            pst.setString(7, t.getMaLoaiTour());
            pst.setString(8, t.getMaDiaDiem());
            pst.setString(9, t.getMaTour());

            return pst.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    public ArrayList<TourDTO> getListByName(String name){
        ArrayList<TourDTO> ds=new ArrayList<>();

        String sql="Select * from tour where ten like ?";

        try(Connection conn= MyConnection.getConnection();
            PreparedStatement ps= conn.prepareStatement(sql)){
            ps.setString(1,"%"+name+"%");
            ResultSet rs=ps.executeQuery();
            while(rs.next()){
                TourDTO dd=mapToTour(rs);
                ds.add(dd);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return ds;
    }

    public TourDTO mapToTour(ResultSet rs) throws SQLException{
        String maTour = rs.getString("matour");
        String ten = rs.getString("ten");
        int soNgay = rs.getInt("songay");
        long donGia =rs.getLong("dongia");
        int soCho =rs.getInt("soCho");
        String ddKhoiHanh =rs.getString("ddkhoihanh");
        String imgLink =rs.getString("imglink");
        String maLoaiTour =rs.getString("maloaitour");
        String maDiaDiem =rs.getString("MaDiaDiem");

        return new TourDTO(maTour, ten , soNgay, donGia,soCho, ddKhoiHanh, imgLink,maLoaiTour,maDiaDiem);
    }
}
