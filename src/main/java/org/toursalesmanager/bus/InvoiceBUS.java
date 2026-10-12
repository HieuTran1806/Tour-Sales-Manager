package org.toursalesmanager.bus;

import lombok.AccessLevel;
import lombok.experimental.FieldDefaults;
import org.toursalesmanager.dao.InvoiceDAO;
import org.toursalesmanager.dto.InvoiceDTO;
import org.toursalesmanager.enums.Permission;
import org.toursalesmanager.login.SessionManager;

import javax.swing.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Date;

@FieldDefaults(level = AccessLevel.PRIVATE)
public class InvoiceBUS {
    ArrayList<InvoiceDTO> ds;
    InvoiceDAO dao = new InvoiceDAO();

    public InvoiceBUS(){
        ds=dao.getDsHoaDon();
    }

    public ArrayList<InvoiceDTO> getAllInvoices(){
        if (ds == null)
            ds = dao.getDsHoaDon();
        return ds;
    }

    public boolean invoiceExisted(InvoiceDTO h){
        if(dao.getInvoiceById(h.getIdInvoice())!=null){
            for(InvoiceDTO hd: ds){
                if(hd.getIdInvoice().equals(h.getIdInvoice())){
                    return true;
                }
            }
        }
        return false;
    }

    public InvoiceDTO getInvoiceById(String mahd){
        if(dao.getInvoiceById(mahd)!=null){
            for(InvoiceDTO hd: ds){
                if(hd.getIdInvoice().equals(mahd)){
                    return dao.getInvoiceById(mahd);
                }
            }
        }
        return null;
    }

    public boolean addInvoice(InvoiceDTO hd){
        requireManagePermission();

        validateInvoiceData(hd);

        if(invoiceExisted(hd)){
            return false;
        }

        boolean kq=dao.addInvoice(hd);
        if(kq){
            ds.add(hd);
            return true;
        }
        return false;
    }

    private void validateInvoiceData(InvoiceDTO invoice) {
        // validate id invoice
        if (invoice.getIdInvoice() == null || invoice.getIdInvoice().trim().isEmpty()) {
            throw new IllegalArgumentException("Mã hóa đơn không được để trống.");
        }
        if(!invoice.getIdInvoice().matches("^HD\\d{3}$")){
            throw new IllegalArgumentException("Mã hóa đơn phải có dạng HDxx!");
        }

        // validate id tour plan
        if (invoice.getIdTourPlan() == null || invoice.getIdTourPlan().trim().isEmpty()) {
            throw new IllegalArgumentException("Mã kế hoạch tour không được để trống.");
        }

        // validate id customer
        if (invoice.getIdCustomer() == null || invoice.getIdCustomer().trim().isEmpty()) {
            throw new IllegalArgumentException("Mã khách hàng đặt không được để trống.");
        }

        // validate id employee
        if (invoice.getIdStaff() == null || invoice.getIdStaff().trim().isEmpty()) {
            throw new IllegalArgumentException("Mã nhân viên không được để trống.");
        }

        // validate date
        if (invoice.getDate() == null) {
            throw new IllegalArgumentException("Ngày lập hóa đơn không được để trống.");
        }

        // validate tickets
        if (invoice.getTickets() <= 0) {
            throw new IllegalArgumentException("Số lượng vé phải lớn hơn 0.");
        }

        // validate total cost
        if (invoice.getCostTotal() == null || invoice.getCostTotal().compareTo(java.math.BigDecimal.ZERO) <= 0) {
            throw new IllegalArgumentException("Tổng chi phí phải lớn hơn 0.");
        }
    }

    public boolean deleteInvoice(String mahd){
        requireManagePermission();

        InvoiceDTO hd=dao.getInvoiceById(mahd);
        boolean b=invoiceExisted(hd);
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

    public boolean updateInvoice(InvoiceDTO hd){
        requireManagePermission();

        validateInvoiceData(hd);

        if(!invoiceExisted(hd)){
            return false;
        }
        if(dao.getInvoiceById(hd.getIdInvoice())!=null){


            boolean kt=dao.updateInvoice(hd);

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
            return getAllInvoices();
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

    private void requireManagePermission() {
        if (!SessionManager.hasPermission(Permission.MANAGE_INVOICE)) {
            throw new SecurityException(
                    "Bạn không có quyền quản lý hóa đơn."
            );
        }
    }
}