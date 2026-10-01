package org.example.bus;
import org.example.dao.InvoiceDetailDAO;
import org.example.dao.TourPlanDAO;
import org.example.dto.InvoiceDetailDTO;

import java.math.BigDecimal;
import java.util.ArrayList;

public class InvoiceDetailBUS {
    public static ArrayList<InvoiceDetailDTO> ds;
    public static InvoiceDetailDAO dao;
    public static TourPlanDAO khtdao;

    public InvoiceDetailBUS(){
        if(ds==null){
            dao=new InvoiceDetailDAO();
            ds=dao.getAllInvoiceDetails();
        }
        khtdao = new TourPlanDAO();
    }

    public void docDs(){
        ds=dao.getAllInvoiceDetails();
    }

    public static ArrayList<InvoiceDetailDTO> getDs(){
        return ds;
    }

    public boolean timCtiethd(InvoiceDetailDTO ct){
        for(InvoiceDetailDTO cthd:ds){
            if(ct.getIdInvoice().equals(cthd.getIdInvoice()) && ct.getIdCustomer().equals(cthd.getIdCustomer()))
                return true;
        }
        return false;
    }

    public InvoiceDetailDTO timCt(String mact, String makh){
        for(InvoiceDetailDTO cthd:ds)
            if(cthd.getIdInvoice().equals(mact) && cthd.getIdCustomer().equals(makh))
                return cthd;

        return null;
    }

    public boolean addInvoiceDetail(InvoiceDetailDTO ct){
        ds.add(ct);
        dao.addInvoiceDetail(ct);
        return true;
    }

    public boolean deleteInvoiceDetail(String mact,String makh){
        InvoiceDetailDTO ct = timCt(mact, makh);
        if (ct == null)
            return false;

        if(dao.deleteInvoiceDetail(mact, makh)) {
            ds.remove(ct);
            return true;
        }

        return false;
    }

    public boolean editInvoiceDetail(InvoiceDetailDTO ct){
        boolean flag = false;
        if(timCtiethd(ct) != true)
            flag = false;
        else {
            flag = true;
            for(int i=0;i<ds.size();i++){
                if(ds.get(i).getIdInvoice().equals(ct.getIdInvoice()) && ds.get(i).getIdCustomer().equals(ct.getIdCustomer())){
                    ds.set(i,ct);
                    flag=true;
                }
            }
        }
        if(dao.findInvoice(ct.getIdInvoice())==null)
            flag=false;
        else
            dao.editInvoiceDetail(ct);

        return flag;
    }

    public ArrayList<InvoiceDetailDTO> getListWithIdInvoice(String mahd){
        InvoiceDetailDAO dao = new InvoiceDetailDAO();
        return dao.getListWithIdInvoice(mahd);
    }

    public BigDecimal getPrice(String mahd){
        InvoiceDetailDAO daoCTietHD=new InvoiceDetailDAO();
        return daoCTietHD.getPrice(mahd);
    }

    public ArrayList<InvoiceDetailDTO> advanceFinding(String loai, String key){
        ArrayList<InvoiceDetailDTO> ds =new ArrayList<>();
        String tencot="";
        if(loai.equals("Mã hóa đơn")){
            tencot="mahd";
        }
        else if(loai.equals("Mã khách hàng")){
            tencot="makhang";
        }else{
            return null;
        }
        return dao.advanceFinding(tencot, key);
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
