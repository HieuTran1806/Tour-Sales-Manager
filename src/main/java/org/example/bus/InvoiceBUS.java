package org.example.bus;

import lombok.AccessLevel;
import lombok.experimental.FieldDefaults;
import org.example.dao.InvoiceDAO;
import org.example.dto.InvoiceDTO;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Date;

@FieldDefaults(level = AccessLevel.PRIVATE)
public class InvoiceBUS {
    ArrayList<InvoiceDTO> ds;
    InvoiceDAO dao = new InvoiceDAO();

    public InvoiceBUS(){
        if(ds==null)
            ds=dao.getDsHoaDon();
    }

    public void docDs(){
        ds=dao.getDsHoaDon();
    }

    public ArrayList<InvoiceDTO> getDs(){
        if (ds == null)
            ds = dao.getDsHoaDon();
        return ds;
    }

    public boolean timHd(InvoiceDTO h){
        if(dao.timHoaDon(h.getIdInvoice())!=null){
            for(InvoiceDTO hd: ds){
                if(hd.getIdInvoice().equals(h.getIdInvoice())){
                    return true;
                }
            }
        }
        return false;
    }

    public ArrayList docDS(){
        if(ds==null) {
            ds=new ArrayList<InvoiceDTO>();
            ds=dao.getDsHoaDon();
        }
        return ds;
    }

    public InvoiceDTO timHd(String mahd){
        if(dao.timHoaDon(mahd)!=null){
            for(InvoiceDTO hd: ds){
                if(hd.getIdInvoice().equals(mahd)){
                    return dao.timHoaDon(mahd);
                }
            }
        }
        return null;
    }

    public boolean themHoaDon(InvoiceDTO hd){
        if(timHd(hd)){
            return false;
        }

        boolean kq=dao.themHoaDon(hd);
        if(kq){
            ds.add(hd);
            return true;
        }
        return false;
    }

    public boolean xoaHoaDon(String mahd){
        InvoiceDTO hd=dao.timHoaDon(mahd);
        boolean b=timHd(hd);
        if(hd==null){
            return false;
        }

        if(b!=true){
            return false;
        }
        dao.deleteInvoice(hd);
        ds.remove(hd);
        return true;
    }

    public boolean suaHoaDon(InvoiceDTO hd){
        if(!timHd(hd)){
            return false;
        }
        if(dao.timHoaDon(hd.getIdInvoice())!=null){
            boolean kt=dao.editInvoice(hd);

            if(kt==true){
                for(int i=0;i<ds.size();i++){
                    if(hd.getIdInvoice().equals(ds.get(i).getIdInvoice())){
                        ds.set(i, hd);
                    }
                }
            }else return false;
            return true;
        }
        return false;
    }

    public ArrayList<InvoiceDTO> timNangcao(String loai, String key){
        if(key.trim().isEmpty()){
            return getDs();
        }
        String tencot="";
        if(loai.equals("Mã hóa đơn")){
            tencot="mahd";
        }
        else if(loai.equals("Mã kế hoạch tour")){
            tencot="makhtour";
        }
        else if(loai.equals("Mã khách hàng đặt")){
            tencot="makhangdat";
        }else if(loai.equals("Mã nhân viên")){
            tencot="manv";
        }
        ArrayList<InvoiceDTO> ds =dao.advanceFinding(tencot, key);
        return ds;
    }

    public ArrayList<InvoiceDTO> getHDtheongay(Date ngay){
        if(ngay==null){
            return ds;
        }
        else{
            return dao.getHdtheoNgay(ngay);
        }
    }

    public int getTongchiDuKien(LocalDate tungay, LocalDate denngay){
        if(tungay==null || denngay==null){
            return 0;
        }
        if(tungay.isAfter(denngay) || denngay.isBefore(tungay)){
            return 0;
        }
        return dao.getTongChiDuKien(tungay, denngay);
    }

    public int getTongThuDuKien(LocalDate tungay, LocalDate denngay){
        if(tungay==null || denngay==null){
            return 0;
        }
        if(tungay.isAfter(denngay) || denngay.isBefore(tungay)){
            return 0;
        }
        return dao.getTongThu(tungay, denngay);
    }

    public int[] getTongThuTungThang(int nam){
        if(nam<2000){
            return new int[12];
        }
        return dao.getTongThuTungThang(nam);
    }

    public int[] getTongChiTungThang(int nam){
        if(nam<2000){
            return new int[12];
        }
        return dao.getTongChiTungThang(nam);
    }
}