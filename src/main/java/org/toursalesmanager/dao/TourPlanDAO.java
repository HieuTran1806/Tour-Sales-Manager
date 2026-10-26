package org.toursalesmanager.dao;

import org.toursalesmanager.dto.TourPlanDTO;

import java.sql.*;
import java.util.ArrayList;
import java.util.Locale;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

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
            sql += "'" +  t.getIdTourPlan() + "'";
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

    public String getNextTourPlanId() {
        String sql = "SELECT makhtour FROM kehoachtour";
        Pattern idPattern = Pattern.compile("^KH([0-9]+)$", Pattern.CASE_INSENSITIVE);
        long maxSequence = 0;
        int sequenceWidth = 3;

        try (PreparedStatement ps = c.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {
            while (rs.next()) {
                Matcher matcher = idPattern.matcher(rs.getString("makhtour"));
                if (matcher.matches()) {
                    long sequence = Long.parseLong(matcher.group(1));
                    maxSequence = Math.max(maxSequence, sequence);
                    sequenceWidth = Math.max(sequenceWidth, matcher.group(1).length());
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException("Không thể tạo mã kế hoạch tour tiếp theo", e);
        }

        long nextSequence = Math.addExact(maxSequence, 1);
        return String.format(
                Locale.ROOT,
                "KH%0" + sequenceWidth + "d",
                nextSequence
        );
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
