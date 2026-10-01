/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package org.example.dao;
import org.example.dto.LocationDTO;
import java.util.*;
import java.sql.*;
import java.util.Date;

public class LocationDAO {

    public ArrayList<LocationDTO> getAllLocations() {
        ArrayList<LocationDTO> ds =new ArrayList<>();

        String sql ="Select * from Diadiem";
        try(Connection conn= MyConnection.getConnection();
            PreparedStatement ps=conn.prepareStatement(sql)){
            ResultSet rs=ps.executeQuery();
            while(rs.next()){
                LocationDTO dd=maptoDiaDiem(rs);
                ds.add(dd);
            }
        }catch(SQLException e){
            e.printStackTrace();
        }
        return ds;
    }

    public LocationDTO maptoDiaDiem(ResultSet rs) throws SQLException{
        String madiadiem = rs.getString("MaDiaDiem");
        String tendd = rs.getString("TenDiaDiem");
        String diachi=rs.getString("DiaChi");
        String quocgia =rs.getString("QuocGia");

        return new LocationDTO(madiadiem,tendd,diachi,quocgia);
    }

    public boolean themDiaDiem(LocationDTO dd){
        String sql = "Insert into DiaDiem(MaDiaDiem,TenDiaDiem,DiaChi,QuocGia) Values (?,?,?,?) ";

        try(Connection conn= MyConnection.getConnection();
            PreparedStatement ps=conn.prepareStatement(sql)){
            ps.setNString(1, dd.getIdLocation());
            ps.setNString(2,dd.getLocationName());
            ps.setNString(3,dd.getAddress());
            ps.setNString(4,dd.getNation());

            return ps.executeUpdate()>0;
        }catch(SQLException e){
            e.printStackTrace();
            return false;
        }
    }

    public boolean xoaDiaDiem(LocationDTO dd){
        String sql = "Delete from DiaDiem where TenDiaDiem=?";

        try(Connection conn= MyConnection.getConnection();
            PreparedStatement ps=conn.prepareStatement(sql)){
            ps.setString(1, dd.getLocationName());
            return ps.executeUpdate()>0;
        }catch(SQLException e){
            e.printStackTrace();
        }
        return false;
    }

    public boolean suaDiaDiem(LocationDTO dd){
        String sql ="Update DiaDiem set tendiadiem=?, DiaChi=?,QuocGia=? where madiadiem=?";

        try(Connection conn= MyConnection.getConnection();
            PreparedStatement ps=conn.prepareStatement(sql)){
            ps.setNString(1, dd.getLocationName());
            ps.setNString(2,dd.getAddress());
            ps.setNString(3,dd.getNation());
            ps.setNString(4,dd.getIdLocation());
            return ps.executeUpdate()>0;
        }catch(SQLException e){
            e.printStackTrace();
        }
        return false;
    }

    public ArrayList<LocationDTO> getDstheongay(Date ngay){
        ArrayList<LocationDTO> dd=new ArrayList<>();
        String sql ="Select * from DiaDiem where ngaythuchien=?";
        try(Connection conn= MyConnection.getConnection();
            PreparedStatement ps=conn.prepareStatement(sql)){
            ps.setDate(1, new java.sql.Date(ngay.getTime()));
            ResultSet rs=ps.executeQuery();
            while(rs.next()){
                LocationDTO dddto=maptoDiaDiem(rs);
                dd.add(dddto);
            }
        }catch(SQLException e){
            return null;
        }
        return dd;
    }
    public ArrayList<LocationDTO> getDstheoDiaChi(String diachi){
        ArrayList<LocationDTO> ds=new ArrayList<>();
        String sql ="Select * from DiaDiem where diachi like";
        try(Connection conn = MyConnection.getConnection();
            PreparedStatement ps =conn.prepareStatement(sql)){
            ps.setString(1,"%"+diachi+"%");
            ResultSet rs=ps.executeQuery();
            while(rs.next()){
                LocationDTO dd=maptoDiaDiem(rs);
                ds.add(dd);
            }
        }catch (SQLException e){
            e.printStackTrace();
        }
        return ds;
    }

    public ArrayList<LocationDTO> getDstheoQuocGia(String diachi){
        ArrayList<LocationDTO> ds=new ArrayList<>();

        String sql="Select * from DiaDiem where quocgia like ?";

        try(Connection conn= MyConnection.getConnection();
            PreparedStatement ps= conn.prepareStatement(sql)){
            ps.setString(1,"%"+diachi+"%");
            ResultSet rs=ps.executeQuery();
            while(rs.next()){
                LocationDTO dd=maptoDiaDiem(rs);
                ds.add(dd);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return ds;
    }
}