package org.example.bus;
import org.example.dao.CTietHDDAO;
import org.example.dao.KeHoachTourDAO;
import org.example.dto.CTietHDDTO;
import java.util.ArrayList;

public class CTHoaDonBUS {
    public static ArrayList<CTietHDDTO> ds;
    public static CTietHDDAO dao;
    public static KeHoachTourDAO khtdao;

    public CTHoaDonBUS(){
        if(ds==null){
            dao=new CTietHDDAO();
            ds=dao.getDs();
        }
        khtdao = new KeHoachTourDAO();
    }

    public void docDs(){
        ds=dao.getDs();
    }

    public static ArrayList<CTietHDDTO> getDs(){
        return ds;
    }

    public boolean timCtiethd(CTietHDDTO ct){
        for(CTietHDDTO cthd:ds){
            if(ct.getMaHD().equals(cthd.getMaHD()) && ct.getMaKHDi().equals(cthd.getMaKHDi()))
                return true;
        }
        return false;
    }

    public CTietHDDTO timCt(String mact, String makh){
        for(CTietHDDTO cthd:ds)
            if(cthd.getMaHD().equals(mact) && cthd.getMaKHDi().equals(makh))
                return cthd;

        return null;
    }

    public boolean themCTietHd(CTietHDDTO ct){
        ds.add(ct);
        dao.themCtietHD(ct);
        return true;
    }

    public boolean xoaCtietHd(String mact,String makh){
        CTietHDDTO ct = timCt(mact, makh);
        if (ct == null)
            return false;

        if(dao.xoaCtietHd(mact, makh)) {
            ds.remove(ct);
            return true;
        }

        return false;
    }

    public boolean suaCtiethd(CTietHDDTO ct){
        boolean flag = false;
        if(timCtiethd(ct) != true)
            flag = false;
        else {
            flag = true;
            for(int i=0;i<ds.size();i++){
                if(ds.get(i).getMaHD().equals(ct.getMaHD()) && ds.get(i).getMaKHDi().equals(ct.getMaKHDi())){
                    ds.set(i,ct);
                    flag=true;
                }
            }
        }
        if(dao.TimHD(ct.getMaHD())==null)
            flag=false;
        else
            dao.suaCthd(ct);

        return flag;
    }

    public ArrayList<CTietHDDTO> getDstheoma(String mahd){
        CTietHDDAO dao = new CTietHDDAO();
        return dao.getDstheoma(mahd);
    }

    public float layGia(String mahd){
        CTietHDDAO daoCTietHD=new CTietHDDAO();
        return daoCTietHD.laygia(mahd);
    }

    public ArrayList<CTietHDDTO> timNangcao(String loai, String key){
        ArrayList<CTietHDDTO> ds =new ArrayList<>();
        String tencot="";
        if(loai.equals("Mã hóa đơn")){
            tencot="mahd";
        }
        else if(loai.equals("Mã khách hàng")){
            tencot="makhang";
        }else{
            return null;
        }
        return dao.timNangcao(tencot, key);
    }

    public boolean capNhatSoluong(int sl, String makhtour){
        if(makhtour.isEmpty()) return false;
        if(sl<=0) {
            return false;
        }else{
            return khtdao.capNhatSoluong(sl, makhtour);
        }
    }
}
