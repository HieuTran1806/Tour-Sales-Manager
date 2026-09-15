package org.example.bus;

import org.example.dao.DiaDiemDAO;
import org.example.dto.DiaDiemDTO;
import java.util.ArrayList;

public class DiaDiemBUS {
    public static ArrayList<DiaDiemDTO> ds;
    public static DiaDiemDAO dao=new DiaDiemDAO();

    public DiaDiemBUS(){
        if(ds==null){
            ds=dao.getDs();
        }
    }

    public void DocDs(){
        dao.getDs();
    }

    public static ArrayList<DiaDiemDTO> getDs(){
        return ds;
    }

    public boolean timDiaDiem(DiaDiemDTO dd){
        for(DiaDiemDTO d:ds){
            if(d.getMaDiaDiem().equals(dd.getMaDiaDiem())){
                return true;
            }
        }
        return false;
    }

    public DiaDiemDTO timDiaDiemTheoMa(String maDiaDiem){
        for(DiaDiemDTO dd : ds){
            if(dd.MaDiaDiem.trim().equalsIgnoreCase(maDiaDiem))
                return dd;
        }
        return null;
    }

    public boolean themDiaDiem(DiaDiemDTO dd){
        if(timDiaDiem(dd)){
            return false;
        }

        ds.add(dd);
        dao.themDiaDiem(dd);
        return true;
    }

    public boolean xoaDiaDiem(DiaDiemDTO dd){
        if(timDiaDiem(dd)!=true){
            return false;
        }

        ds.remove(dd);
        dao.xoaDiaDiem(dd);
        return true;
    }

    public boolean suaDiaDiem(DiaDiemDTO dd,String maDiaDiem){
        for(int i=0;i<ds.size();i++){
            if(ds.get(i).getMaDiaDiem().equals(maDiaDiem)){
                ds.set(i, dd);
            }
        }

        return dao.suaDiaDiem(dd);
    }

    public ArrayList<DiaDiemDTO> getDstheongay(java.util.Date ngay){
        if(ngay==null){
            return null;
        }
        return dao.getDstheongay(ngay);
    }

    public ArrayList<DiaDiemDTO> getDsTheoDiachi(String DiaChi){
        if(DiaChi.isEmpty()){
            return null;
        }
        return dao.getDstheoDiaChi(DiaChi);
    }

    public ArrayList<DiaDiemDTO> getDsTheoQuocGia(String quocgia){
        if(quocgia.isEmpty()){
            return null;
        }
        return dao.getDstheoQuocGia(quocgia);
    }
}