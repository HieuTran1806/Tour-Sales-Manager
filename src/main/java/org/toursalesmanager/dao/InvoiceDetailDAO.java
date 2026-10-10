/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package org.toursalesmanager.dao;
import java.math.BigDecimal;
import java.sql.*;
import org.toursalesmanager.dto.InvoiceDetailDTO;
import java.util.*;

public class InvoiceDetailDAO {
    public InvoiceDetailDAO() {
    }

    public ArrayList<InvoiceDetailDTO> getAllInvoiceDetails() {
        ArrayList<InvoiceDetailDTO> ds=new ArrayList<>();
        String sql ="Select * from CThoadon";

        try(Connection conn= MyConnection.getConnection();
            PreparedStatement ps=conn.prepareStatement(sql)){
            ResultSet rs=ps.executeQuery();
            while(rs.next()){
                InvoiceDetailDTO ct=mapToInvoiceDetail(rs);
                ds.add(ct);
            }

        }catch (SQLException e) {
            e.printStackTrace();
        }
        return ds;
    }

    public ArrayList<InvoiceDetailDTO> getListWithIdInvoice(String mahd){
        ArrayList<InvoiceDetailDTO> ds=new ArrayList<>();
        String sql ="Select * from CThoadon where mahd=?";
        try(Connection conn = MyConnection.getConnection();
            PreparedStatement ps=conn.prepareStatement(sql)){
            ps.setString(1, mahd);
            ResultSet rs=ps.executeQuery();
            while(rs.next()){
                InvoiceDetailDTO cthd=mapToInvoiceDetail(rs);
                ds.add(cthd);
            }
        }
        catch(SQLException ex){
            ex.printStackTrace();
        }
        return ds;
    }

    public InvoiceDetailDTO mapToInvoiceDetail(ResultSet rs) throws SQLException{
        String MaHD =rs.getString("MaHD");
        String MaKHDi =rs.getString("MaKHang");
        BigDecimal GiaVe =rs.getBigDecimal("GiaVe");
        return new InvoiceDetailDTO(MaHD,MaKHDi,GiaVe);
    }

    public InvoiceDetailDTO findInvoice(String mahd){
        String sql = "Select * from CThoadon where mahd=?";
        try(Connection conn= MyConnection.getConnection();
            PreparedStatement ps=conn.prepareStatement(sql)){
            ps.setString(1, mahd);
            ResultSet rs=ps.executeQuery();
            if(rs.next()){
                return mapToInvoiceDetail(rs);
            }
        }catch(SQLException e){
            e.printStackTrace();
        }
        return null;
    }

    public boolean addInvoiceDetail(InvoiceDetailDTO ct){
        String sqlcheck = "Select * from cthoadon where mahd=? and makhang=?";
        String sqlUpdateCT = "Update cthoadon set giave = giave + ? where mahd=? and makhang=?";
        String sqlinsert = "Insert into cthoadon(mahd,makhang,giave) Values(?,?,?)";
        String sqlUpdateHD = "UPDATE hoadon SET soluong = soluong + 1, tongtien = tongtien + ? WHERE mahd = ?";

        Connection conn = null;
        try {
            conn = MyConnection.getConnection();
            conn.setAutoCommit(false);

            boolean daTonTai = false;

            try (PreparedStatement ps = conn.prepareStatement(sqlcheck)) {
                ps.setString(1, ct.getIdInvoice());
                ps.setString(2, ct.getIdCustomer());
                ResultSet rs = ps.executeQuery();
                if (rs.next()) {
                    daTonTai = true;
                }
            }

            if (daTonTai) {
                try (PreparedStatement ps = conn.prepareStatement(sqlUpdateCT)) {
                    ps.setBigDecimal(1, ct.getPrice());
                    ps.setString(2, ct.getIdInvoice());
                    ps.setString(3, ct.getIdCustomer());
                    if (ps.executeUpdate() <= 0) {
                        conn.rollback();
                        return false;
                    }
                }
            } else {
                try (PreparedStatement ps = conn.prepareStatement(sqlinsert)) {
                    ps.setString(1, ct.getIdInvoice());
                    ps.setString(2, ct.getIdCustomer());
                    ps.setBigDecimal(3, ct.getPrice());
                    if (ps.executeUpdate() <= 0) {
                        conn.rollback();
                        return false;
                    }
                }
            }

            try (PreparedStatement ps = conn.prepareStatement(sqlUpdateHD)) {
                ps.setBigDecimal(1, ct.getPrice());
                ps.setString(2, ct.getIdInvoice());

                if (ps.executeUpdate() > 0) {
                    conn.commit();
                    return true;
                } else {
                    conn.rollback();
                }
            }

        } catch (SQLException e) {
            if (conn != null) try { conn.rollback(); } catch (SQLException ex) {}
            e.printStackTrace();
        } finally {
            if (conn != null) {
                try {
                    conn.setAutoCommit(true);
                    conn.close();
                } catch (SQLException ex) {}
            }
        }
        return false;
    }
    public boolean deleteInvoiceDetail(String mahd, String makh) {
        BigDecimal giaVeGoc = getPrice(mahd);

        BigDecimal giaHienTai = BigDecimal.valueOf(0);
        String sqlCheck = "SELECT giave FROM cthoadon WHERE mahd=? AND makhang=?";

        String sqlUpdateHD = "UPDATE hoadon SET soluong = soluong - 1, tongtien = tongtien - ? WHERE mahd = ?";

        String sqlUpdateKHT = "UPDATE kehoachtour SET tongthu = tongthu - ? " +
                "WHERE makhtour = (SELECT makhtour FROM hoadon WHERE mahd = ?)";

        String sqlHoanVe = "UPDATE kehoachtour SET tongsove = tongsove + 1 " +
                "WHERE makhtour = (SELECT makhtour FROM hoadon WHERE mahd = ?)";

        Connection conn = null;
        try {
            conn = MyConnection.getConnection();
            conn.setAutoCommit(false);

            try (PreparedStatement ps = conn.prepareStatement(sqlCheck)) {
                ps.setString(1, mahd);
                ps.setString(2, makh);
                ResultSet rs = ps.executeQuery();
                if (rs.next()) {
                    giaHienTai = rs.getBigDecimal("giave");
                } else {
                    return false;
                }
            }

            if (giaHienTai.compareTo(giaVeGoc) > 0) { // giaHienTai > giaVeGoc
                String sqlTruTienCT = "UPDATE cthoadon SET giave = giave - ? WHERE mahd=? AND makhang=?";
                try (PreparedStatement ps = conn.prepareStatement(sqlTruTienCT)) {
                    ps.setBigDecimal(1, giaVeGoc);
                    ps.setString(2, mahd);
                    ps.setString(3, makh);
                    ps.executeUpdate();
                }
            } else {
                String sqlDelete = "DELETE FROM cthoadon WHERE mahd=? AND makhang=?";
                try (PreparedStatement ps = conn.prepareStatement(sqlDelete)) {
                    ps.setString(1, mahd);
                    ps.setString(2, makh);
                    ps.executeUpdate();
                }
            }

            try (PreparedStatement ps = conn.prepareStatement(sqlUpdateHD)) {
                ps.setBigDecimal(1, giaVeGoc);
                ps.setString(2, mahd);
                ps.executeUpdate();
            }

            try (PreparedStatement ps = conn.prepareStatement(sqlUpdateKHT)) {
                ps.setBigDecimal(1, giaVeGoc);
                ps.setString(2, mahd);
                ps.executeUpdate();
            }

            try (PreparedStatement ps = conn.prepareStatement(sqlHoanVe)) {
                ps.setString(1, mahd);
                ps.executeUpdate();
            }

            conn.commit();
            return true;

        } catch (SQLException e) {
            if (conn != null) try { conn.rollback(); } catch (SQLException ex) {}
            e.printStackTrace();
            return false;
        } finally {
            if (conn != null) try { conn.setAutoCommit(true); conn.close(); } catch (SQLException e) {}
        }
    }
    public BigDecimal getPrice(String mahd){
        BigDecimal gia= BigDecimal.valueOf(0);

        String makht="";
        String sql1="Select makhtour from hoadon where mahd=?";
        try(Connection conn= MyConnection.getConnection();
            PreparedStatement ps=conn.prepareStatement(sql1)){
            ps.setString(1, mahd);
            ResultSet rs=ps.executeQuery();
            if(rs.next()){
                makht=rs.getString("makhtour");
            }
        }catch (SQLException ex) {
            ex.printStackTrace();
        }
        String sql ="Select t.dongia from kehoachtour k join tour t on k.matour = t.matour where k.makhtour=?";
        try(Connection conn= MyConnection.getConnection();
            PreparedStatement ps=conn.prepareStatement(sql)){
            ps.setString(1, makht);
            ResultSet rs=ps.executeQuery();
            if(rs.next()){
                gia=rs.getBigDecimal("Dongia");
            }}
        catch(SQLException e){
            e.printStackTrace();
        }
        return gia;
    }

    public ArrayList<InvoiceDetailDTO> advanceFinding(String tencot, String key){
        ArrayList<InvoiceDetailDTO> ds =new ArrayList<>();

        String sql="Select * from cthoadon where "+tencot+" like ?";

        try(Connection conn = MyConnection.getConnection();
            PreparedStatement ps=conn.prepareStatement(sql)){
            ps.setString(1, "%" + key + "%");
            ResultSet rs=ps.executeQuery();
            while(rs.next()){
                InvoiceDetailDTO ct =mapToInvoiceDetail(rs);
                ds.add(ct);
            }

        }catch(SQLException e){
            e.printStackTrace();
        }
        return ds;
    }

    public boolean editInvoiceDetail(InvoiceDetailDTO ct){
        String sql = "Update cthoadon set giave=? where mahd=? and makhang=?";
        try(Connection conn= MyConnection.getConnection();
            PreparedStatement ps=conn.prepareStatement(sql)){
            ps.setBigDecimal(1,ct.getPrice() );
            ps.setString(2, ct.getIdInvoice());
            ps.setString(3, ct.getIdCustomer());

            return ps.executeUpdate()>0;
        }catch(SQLException e){
            e.printStackTrace();
        }
        return false;
    }
}