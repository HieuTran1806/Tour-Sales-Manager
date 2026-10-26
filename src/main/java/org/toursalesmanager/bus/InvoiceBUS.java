package org.toursalesmanager.bus;

import lombok.AccessLevel;
import lombok.experimental.FieldDefaults;
import org.toursalesmanager.dao.InvoiceDAO;
import org.toursalesmanager.dto.InvoiceDTO;
import org.toursalesmanager.enums.Permission;
import org.toursalesmanager.login.SessionManager;

import javax.swing.*;
import java.math.BigDecimal;
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
        if (h == null || h.getIdInvoice() == null) {
            return false;
        }
        return dao.getInvoiceById(h.getIdInvoice()) != null;
    }

    public InvoiceDTO getInvoiceById(String mahd){
        return mahd == null ? null : dao.getInvoiceById(mahd);
    }

    public String getNextInvoiceId() {
        requireManagePermission();
        return dao.getNextInvoiceId();
    }

    public boolean addInvoice(InvoiceDTO hd){
        requireManagePermission();

        validateInvoiceData(hd);

        if (invoiceExisted(hd)) {
            throw new IllegalArgumentException("Mã hóa đơn " + hd.getIdInvoice() + " đã tồn tại.");
        }

        boolean kq=dao.addInvoice(hd);
        if(kq){
            ds.add(hd);
            return true;
        }
        return false;
    }

    private void validateInvoiceData(InvoiceDTO invoice) {
        if (invoice == null) {
            throw new IllegalArgumentException("Thông tin hóa đơn không được để trống.");
        }

        String invoiceId = invoice.getIdInvoice();
        if (invoiceId == null || invoiceId.isBlank()) {
            throw new IllegalArgumentException("Mã hóa đơn không được để trống.");
        }
        if (!invoiceId.trim().matches("^HD\\d{3,}$")) {
            throw new IllegalArgumentException("Mã hóa đơn phải có dạng HDxxx (ví dụ: HD001).");
        }
        if (invoiceId.trim().length() > 10) {
            throw new IllegalArgumentException("Mã hóa đơn không được dài quá 10 ký tự.");
        }

        validateRequiredId(invoice.getIdTourPlan(), "Mã kế hoạch tour");
        validateRequiredId(invoice.getIdCustomer(), "Mã khách hàng đặt");
        validateRequiredId(invoice.getIdStaff(), "Mã nhân viên");
        validateOptionalId(invoice.getIdPromotion(), "Mã khuyến mãi");

        if (invoice.getDate() == null) {
            throw new IllegalArgumentException("Ngày lập hóa đơn không được để trống.");
        }

        if (invoice.getTickets() <= 0) {
            throw new IllegalArgumentException("Số lượng vé phải lớn hơn 0.");
        }

        BigDecimal total = invoice.getCostTotal();
        if (total == null || total.signum() < 0) {
            throw new IllegalArgumentException("Tổng tiền không được để trống hoặc âm.");
        }
        if (total.compareTo(new BigDecimal("9999999999")) > 0) {
            throw new IllegalArgumentException("Tổng tiền vượt quá giới hạn lưu trữ của hóa đơn.");
        }
    }

    private void validateRequiredId(String id, String fieldName) {
        if (id == null || id.isBlank()) {
            throw new IllegalArgumentException(fieldName + " không được để trống.");
        }
        if (id.trim().length() > 10) {
            throw new IllegalArgumentException(fieldName + " không được dài quá 10 ký tự.");
        }
    }

    private void validateOptionalId(String id, String fieldName) {
        if (id != null && !id.isBlank() && id.trim().length() > 10) {
            throw new IllegalArgumentException(fieldName + " không được dài quá 10 ký tự.");
        }
    }

    public boolean deleteInvoice(String mahd){
        requireManagePermission();

        InvoiceDTO hd=dao.getInvoiceById(mahd);
        if(hd==null){
            return false;
        }

        if (!dao.deleteInvoice(hd)) {
            return false;
        }
        if (ds != null) {
            ds.removeIf(invoice -> invoice.getIdInvoice().equals(mahd));
        }
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