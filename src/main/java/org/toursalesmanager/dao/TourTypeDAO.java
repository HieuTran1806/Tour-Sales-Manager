package org.toursalesmanager.dao;

import org.toursalesmanager.dto.TourTypeDTO;
import org.toursalesmanager.dto.TourDTO;

import java.sql.*;
import java.util.ArrayList;
import java.util.Locale;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class TourTypeDAO {
    Connection c = MyConnection.getConnection();
    Statement st = null;

    public TourTypeDAO(){
        ArrayList<TourDTO> lsTour = new ArrayList<>();
    }

    //get all tours
    public ArrayList<TourTypeDTO> getAllTourTypes(){
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
    public boolean addTourType(TourTypeDTO t){
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

    public String getNextTourTypeId(){
        String sql = "SELECT maloaitour from loaitour";
        Pattern tourTypeIdPattern = Pattern.compile("^LT([0-9]+)$", Pattern.CASE_INSENSITIVE);
        long maxSequence = 0;
        int sequenceWidth = 3;

        try(PreparedStatement ps = c.prepareStatement(sql);
            ResultSet rs = ps.executeQuery()){
            while(rs.next()){
                Matcher matcher = tourTypeIdPattern.matcher(rs.getString("maloaitour"));
                if (matcher.matches()) {
                    long sequence = Long.parseLong(matcher.group(1));
                    maxSequence = Math.max(maxSequence, sequence);
                    sequenceWidth = Math.max(sequenceWidth, matcher.group(1).length());
                }
            }

        } catch (SQLException e) {
            throw new RuntimeException("Không thể tạo mã loại tour tiếp theo", e);
        }
        long nextSequence = Math.addExact(maxSequence, 1);
        return String.format(
                Locale.ROOT,
                "LT%0" + sequenceWidth + "d",
                nextSequence
        );
    }

    public boolean removeTourType(String maLoaiTour){
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
    public boolean editTourType(TourTypeDTO t){
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
