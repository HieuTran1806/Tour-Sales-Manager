package org.toursalesmanager.bus;

import lombok.AccessLevel;
import lombok.experimental.FieldDefaults;
import org.toursalesmanager.dao.LocationDAO;
import org.toursalesmanager.dto.LocationDTO;
import java.util.ArrayList;

@FieldDefaults(level = AccessLevel.PRIVATE)
public class LocationBUS {
    ArrayList<LocationDTO> ds;
    LocationDAO dao=new LocationDAO();

    public LocationBUS(){
        if(ds==null){
            ds=dao.getAllLocations();
        }
    }
    public ArrayList<LocationDTO> getAllLocations(){
        return dao.getAllLocations();
    }

    public boolean timDiaDiem(LocationDTO dd){
        for(LocationDTO d:ds){
            if(d.getIdLocation().equals(dd.getIdLocation())){
                return true;
            }
        }
        return false;
    }

    public LocationDTO timDiaDiemTheoMa(String maDiaDiem){
        for(LocationDTO dd : ds){
            if(dd.getIdLocation().trim().equalsIgnoreCase(maDiaDiem))
                return dd;
        }
        return null;
    }

    public boolean themDiaDiem(LocationDTO dd){
        if(timDiaDiem(dd)){
            return false;
        }

        ds.add(dd);
        dao.themDiaDiem(dd);
        return true;
    }

    public boolean xoaDiaDiem(LocationDTO dd){
        if(timDiaDiem(dd)!=true){
            return false;
        }

        ds.remove(dd);
        dao.xoaDiaDiem(dd);
        return true;
    }

    public boolean suaDiaDiem(LocationDTO dd, String maDiaDiem){
        for(int i=0;i<ds.size();i++){
            if(ds.get(i).getIdLocation().equals(maDiaDiem)){
                ds.set(i, dd);
            }
        }

        return dao.suaDiaDiem(dd);
    }

    public ArrayList<LocationDTO> getDstheongay(java.util.Date ngay){
        if(ngay==null){
            return null;
        }
        return dao.getDstheongay(ngay);
    }

    public ArrayList<LocationDTO> getDsTheoDiachi(String DiaChi){
        if(DiaChi.isEmpty()){
            return null;
        }
        return dao.getDstheoDiaChi(DiaChi);
    }

    public ArrayList<LocationDTO> getDsTheoQuocGia(String quocgia){
        if(quocgia.isEmpty()){
            return null;
        }
        return dao.getDstheoQuocGia(quocgia);
    }
}