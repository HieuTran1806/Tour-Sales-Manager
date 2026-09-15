package org.example.dao;
import org.example.dto.HoaDonDTO;

import java.sql.*;
import java.util.*;
import java.time.LocalDate;

public class HoaDonDAO {
    public ArrayList<HoaDonDTO> getDsHoaDon(){
        ArrayList<HoaDonDTO> list= new ArrayList<>();

        String sql="SELECT * FROM HoaDon";

        try(Connection conn= MyConnection.getConnection();
            PreparedStatement ps=conn.prepareStatement(sql);
            ResultSet rs=ps.executeQuery()){
            while(rs.next()){
                HoaDonDTO hd=maptoHd(rs);
                list.add(hd);
            }
        }catch(SQLException e){
            e.printStackTrace();
        }
        return list;
    }

    public HoaDonDAO() {
    }

    public HoaDonDTO timHoaDon(String mahd){
        String sql="SELECT * FROM HOADON where mahd=?";
        try(Connection conn= MyConnection.getConnection();
            PreparedStatement ps=conn.prepareStatement(sql)){
            ps.setString(1, mahd);
            ResultSet rs=ps.executeQuery();
            if(rs.next()){
                return maptoHd(rs);
            }
        }catch(SQLException e){
            e.printStackTrace();
        }
        return null;
    }

    public HoaDonDTO maptoHd(ResultSet rs) throws SQLException{
        String mahd=rs.getString("MaHD");
        String makhtour = rs.getString("MaKHTour");
        String makhdi = rs.getString("MaKHangDat");
        int tongtien =rs.getInt("TongTien");
        String manv=rs.getString("MaNV");
        LocalDate ngay =LocalDate.parse(rs.getDate("ngay").toString());
        int soluong=rs.getInt("soluong");
        String makm=rs.getString("makm");

        return new HoaDonDTO(mahd, makhtour, makhdi, manv,ngay,soluong, makm, tongtien);
    }

    public boolean themHoaDon(HoaDonDTO hd) {
        if(timHoaDon(hd.getMaHD()) != null) {
            return false;
        }

        String sqlhd = "Insert into hoadon(mahd,makhtour,makhangdat,manv, ngay,soluong, makm, tongtien) values(?,?,?,?,?,?,?,?)";
        Connection conn = null;

        try {
            conn = MyConnection.getConnection();

            try (PreparedStatement ps = conn.prepareStatement(sqlhd)) {
                ps.setString(1, hd.getMaHD());
                ps.setString(2, hd.getMaKHTour());
                ps.setString(3, hd.getMaKHDat());
                ps.setString(4, hd.getMaNV());
                ps.setDate(5, java.sql.Date.valueOf(hd.getNgay()));
                ps.setInt(6, hd.getSoLuong());
                ps.setString(7, hd.getMaKM());
                ps.setFloat(8, hd.getTongTien());

                int rowAffected = ps.executeUpdate();
                return rowAffected > 0;
            }

        } catch (SQLException ex) {
            ex.printStackTrace();
        } finally {
            if (conn != null) {
                try {
                    conn.close();
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
        }
        return false;
    }

    public boolean xoaHd(HoaDonDTO hd){
        String sqlct = "Delete from cthoadon where mahd=?";
        String sql="Delete from hoadon where mahd=?";

        String sqlHoanVe = "UPDATE kehoachtour SET tongsove = tongsove + ? WHERE makhtour=?";

        Connection connection=null;
        try{
            connection= MyConnection.getConnection();
            connection.setAutoCommit(false);

            try(PreparedStatement psHoan = connection.prepareStatement(sqlHoanVe)){
                psHoan.setInt(1, hd.getSoLuong());
                psHoan.setString(2, hd.getMaKHTour());
                psHoan.executeUpdate();
            }

            try(PreparedStatement ps=connection.prepareStatement(sqlct)){
                ps.setString(1, hd.getMaHD());
                ps.executeUpdate();
            }

            try(PreparedStatement ps=connection.prepareStatement(sql)){
                ps.setString(1, hd.getMaHD());
                int result=ps.executeUpdate();
                if(result > 0){
                    connection.commit();
                    return true;
                } else {
                    connection.rollback();
                    return false;
                }
            }
        }catch (SQLException ex) {
            ex.printStackTrace();
            if(connection!=null){
                try{
                    connection.rollback();
                }catch(SQLException e){
                    e.printStackTrace();
                }
            }
        }finally{
            if(connection!=null){
                try{
                    connection.setAutoCommit(true);
                    connection.close();
                }catch(SQLException e){
                    e.printStackTrace();
                }
            }
        }
        return false;
    }

    public boolean suaHd(HoaDonDTO hd){
        String sqlhd ="Update hoadon set makhtour=?,makhangdat=?,manv=?,ngay=?,soluong=?, makm=?,tongtien=? where mahd=?";

        try(Connection conn= MyConnection.getConnection();
            PreparedStatement ps = conn.prepareStatement(sqlhd)){
            ps.setString(1, hd.getMaKHTour());
            ps.setString(2, hd.getMaKHDat());
            ps.setString(3, hd.getMaNV());
            ps.setDate(4, java.sql.Date.valueOf(hd.getNgay()));
            ps.setInt(5, hd.getSoLuong());
            ps.setString(6, hd.getMaKM());
            ps.setFloat(7, hd.getTongTien());
            ps.setString(8, hd.getMaHD());
            return ps.executeUpdate()>0;
        }catch(SQLException e){
            e.printStackTrace();
        }
        return false;
    }

    public float laygia(String makht){
        float gia=0;
        String sql ="Select t.dongia from kehoachtour k join tour t on k.matour = t.matour where k.makhtour=?";
        try(Connection conn= MyConnection.getConnection();
            PreparedStatement ps=conn.prepareStatement(sql)){
            ps.setString(1, makht);
            ResultSet rs=ps.executeQuery();
            if(rs.next()){
                gia=rs.getFloat("Dongia");
            }}
        catch(SQLException e){
            e.printStackTrace();
        }
        return gia;
    }

    public ArrayList<HoaDonDTO> timNangcao(String tencot,String key){
        ArrayList<HoaDonDTO> ds =new ArrayList<>();
        String sql ="Select * from Hoadon where "+ tencot +" like ?";

        try(Connection conn= MyConnection.getConnection();
            PreparedStatement ps=conn.prepareStatement(sql)){
            ps.setString(1, "%"+key+"%");
            ResultSet rs=ps.executeQuery();

            while(rs.next()){
                HoaDonDTO hd=maptoHd(rs);
                ds.add(hd);
            }
        }catch(SQLException e){
            e.printStackTrace();
        }
        return ds;
    }

    public ArrayList<HoaDonDTO> getHdtheoNgay(java.util.Date ngay){
        ArrayList<HoaDonDTO> ds =new ArrayList<>();
        String sql ="Select * from Hoadon where ngay=?";

        try(Connection conn= MyConnection.getConnection();
            PreparedStatement ps=conn.prepareStatement(sql)){

            ps.setDate(1, new java.sql.Date(ngay.getTime()));
            ResultSet rs=ps.executeQuery();

            while(rs.next()){
                HoaDonDTO hd=maptoHd(rs);
                ds.add(hd);
            }
        }catch(SQLException e){
            e.printStackTrace();
        }
        return ds;
    }

    public int getTongChiDuKien(LocalDate tungay, LocalDate denngay){
        int tongchi=0;
        String sql ="Select sum(tongchidukien)as tong from kehoachtour where makhtour in ("+"select distinct makhtour from hoadon where ngay between ? and ?)";
        try(Connection conn= MyConnection.getConnection();
            PreparedStatement ps=conn.prepareStatement(sql)){

            ps.setDate(1, java.sql.Date.valueOf(tungay));
            ps.setDate(2, java.sql.Date.valueOf(denngay));
            ResultSet rs=ps.executeQuery();
            if(rs.next()){
                tongchi=rs.getInt("tong");
            }
        }catch (SQLException ex) {
            ex.printStackTrace();
        }
        return tongchi;
    }

    public int getTongThu(LocalDate tungay, LocalDate denngay){
        int tong=0;
        String sql ="Select sum(tongtien) as tong from hoadon where ngay between ? and ?";
        try(Connection conn= MyConnection.getConnection();
            PreparedStatement ps=conn.prepareStatement(sql)){
            ps.setDate(1, java.sql.Date.valueOf(tungay));
            ps.setDate(2, java.sql.Date.valueOf(denngay));
            ResultSet rs =ps.executeQuery();
            if(rs.next()){
                tong=rs.getInt("tong");
            }
        }catch (SQLException ex) {
            ex.printStackTrace();
        }
        return tong;
    }

    public int[] getTongThuTungThang(int nam){
        int[] tongtien=new int[12];
        String sql ="Select month(ngay) as thang, sum(tongtien) as tong from hoadon where year(ngay)=? group by month(ngay)";
        try(Connection conn = MyConnection.getConnection();
            PreparedStatement ps=conn.prepareStatement(sql)){
            ps.setInt(1, nam);
            ResultSet rs =ps.executeQuery();
            while(rs.next()){
                int thang=rs.getInt("thang");
                int tong=rs.getInt("tong");
                if(thang>=1 && thang<=12){
                    tongtien[thang-1]=tong;
                }
            }
        }catch (SQLException ex) {
            ex.printStackTrace();
        }
        return tongtien;
    }

    public int [] getTongChiTungThang(int nam) {
        int[] tongtien = new int[12];
        String sql = "Select month(h.ngay) as thang, sum(distinct k.tongchi) as tong from hoadon h join kehoachtour k"
                + " on k.makhtour=h.makhtour where year(h.ngay)=? group by month(h.ngay)";
        try (Connection conn = MyConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, nam);
            ResultSet rs = ps.executeQuery();

            while (rs.next()) {
                int thang = rs.getInt("thang");
                int tong = rs.getInt("tong");
                if (thang >= 1 && thang <= 12) {
                    tongtien[thang - 1] = tong;
                }
            }
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
        return tongtien;
    }
}