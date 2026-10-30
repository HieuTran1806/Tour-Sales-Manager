package org.toursalesmanager.dao;

import org.toursalesmanager.dto.TourPlanDetailDTO;

import java.sql.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Locale;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class TourPlanDetailDAO {

    //get all tour plan details
    public ArrayList<TourPlanDetailDTO> getAllCTietKHTours(){
        ArrayList<TourPlanDetailDTO> lsCTietKHTours = new ArrayList<>();
        String sql = "select * from ctietkhtour";
        try (Connection conn = MyConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {
            while(rs.next()){
                lsCTietKHTours.add(mapToDTO(rs));
            }
        } catch (SQLException e) {
            throw new RuntimeException("Không thể tải chi tiết kế hoạch tour", e);
        }
        return lsCTietKHTours;
    }

    // get details of one tour plan
    public ArrayList<TourPlanDetailDTO> getByTourPlanId(String maKHTour){
        ArrayList<TourPlanDetailDTO> lsCTietKHTours = new ArrayList<>();
        String sql = "select * from ctietkhtour where makhtour = ? order by ngaythuchien, mactietkhtour";
        try (Connection conn = MyConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, maKHTour);
            try (ResultSet rs = ps.executeQuery()) {
                while(rs.next()){
                    lsCTietKHTours.add(mapToDTO(rs));
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException("Không thể tải chi tiết kế hoạch tour", e);
        }
        return lsCTietKHTours;
    }

    public TourPlanDetailDTO getById(String maCTietKHTour){
        String sql = "select * from ctietkhtour where mactietkhtour = ?";
        try (Connection conn = MyConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, maCTietKHTour);
            try (ResultSet rs = ps.executeQuery()) {
                return rs.next() ? mapToDTO(rs) : null;
            }
        } catch (SQLException e) {
            throw new RuntimeException("Không thể tải chi tiết kế hoạch tour", e);
        }
    }

    public String getNextTourPlanDetailId(){
        String sql = "select mactietkhtour from ctietkhtour";
        Pattern idPattern = Pattern.compile("^CKHT([0-9]+)$", Pattern.CASE_INSENSITIVE);
        long maxSequence = 0;
        int sequenceWidth = 2;

        try (Connection conn = MyConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {
            while(rs.next()){
                Matcher matcher = idPattern.matcher(rs.getString("mactietkhtour"));
                if (matcher.matches()) {
                    long sequence = Long.parseLong(matcher.group(1));
                    maxSequence = Math.max(maxSequence, sequence);
                    sequenceWidth = Math.max(sequenceWidth, matcher.group(1).length());
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException("Không thể tạo mã chi tiết kế hoạch tour tiếp theo", e);
        }

        long nextSequence = Math.addExact(maxSequence, 1);
        return String.format(
                Locale.ROOT,
                "CKHT%0" + sequenceWidth + "d",
                nextSequence
        );
    }

    //add
    public boolean addCTietKHTour(TourPlanDetailDTO t){
        String sql = """
                insert into ctietkhtour
                    (mactietkhtour, ngaythuchien, tongchi, tieno, tienan, tiendilai, diemdi, diemden, makhtour)
                values (?, ?, ?, ?, ?, ?, ?, ?, ?)
                """;
        try (Connection conn = MyConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, t.getIdTourPlanDetail());
            setCommonParams(ps, 2, t);
            ps.setString(9, t.getIdTourPlan());
            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    public boolean removeCTietKHTour(String maCTietKHTour){
        String sql = "delete from ctietkhtour where mactietkhtour = ?";
        try (Connection conn = MyConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, maCTietKHTour);
            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    //edit
    public boolean editCTietKHTour(TourPlanDetailDTO t){
        String sql = """
                update ctietkhtour
                set ngaythuchien = ?, tongchi = ?, tieno = ?, tienan = ?, tiendilai = ?,
                    diemdi = ?, diemden = ?, makhtour = ?
                where mactietkhtour = ?
                """;
        try (Connection conn = MyConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            setCommonParams(ps, 1, t);
            ps.setString(8, t.getIdTourPlan());
            ps.setString(9, t.getIdTourPlanDetail());
            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    // ngaythuchien, tongchi, tieno, tienan, tiendilai, diemdi, diemden
    private void setCommonParams(PreparedStatement ps, int startIndex, TourPlanDetailDTO t) throws SQLException {
        if (t.getDate() != null) {
            ps.setDate(startIndex, Date.valueOf(t.getDate().toLocalDate()));
        } else {
            ps.setNull(startIndex, Types.DATE);
        }
        ps.setBigDecimal(startIndex + 1, t.getTotalExpenditure());
        ps.setBigDecimal(startIndex + 2, t.getHousingCost());
        ps.setBigDecimal(startIndex + 3, t.getEatingCost());
        ps.setBigDecimal(startIndex + 4, t.getTravelingCost());
        ps.setString(startIndex + 5, t.getDepartureLocation());
        ps.setString(startIndex + 6, t.getEndLocation());
    }

    private TourPlanDetailDTO mapToDTO(ResultSet rs) throws SQLException {
        Date date = rs.getDate("ngaythuchien");
        LocalDateTime dateTime = date == null ? null : date.toLocalDate().atStartOfDay();

        return new TourPlanDetailDTO(
                rs.getString("mactietkhtour"),
                dateTime,
                rs.getBigDecimal("tongchi"),
                rs.getBigDecimal("tieno"),
                rs.getBigDecimal("tienan"),
                rs.getBigDecimal("tiendilai"),
                rs.getString("diemdi"),
                rs.getString("diemden"),
                rs.getString("makhtour")
        );
    }
}
