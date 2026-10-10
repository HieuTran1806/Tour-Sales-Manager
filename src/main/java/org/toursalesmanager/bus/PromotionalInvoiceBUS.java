package org.toursalesmanager.bus;

import org.toursalesmanager.dao.PromotionalInvoiceDAO;
import org.toursalesmanager.dto.PromotionalInvoiceDTO;

import java.util.*;

public class PromotionalInvoiceBUS {
    public ArrayList<PromotionalInvoiceDTO> dsKMHD;
    public static PromotionalInvoiceDAO dao;

    public PromotionalInvoiceBUS() {
        if (dsKMHD == null) {
            // dao = new DsKMHD();
            //khoit tao dsKMHD tu database
            dsKMHD = dao.getDsKMHD();
        }
    }

    public ArrayList<PromotionalInvoiceDTO> getDsKMHD() {
        return dsKMHD;
    }

    public void setDsKMHD(ArrayList<PromotionalInvoiceDTO> dsKMHD) {
        this.dsKMHD = dsKMHD;
    }

    public boolean timKMHD(PromotionalInvoiceDTO kmhd) {
        for (PromotionalInvoiceDTO km : dsKMHD) {
            if (km.getIdPromotion().equals(kmhd.getIdPromotion())) {
                return true;
            }
        }
        return false;
    }

    public boolean themKMHD(PromotionalInvoiceDTO kmhd) {
        if (timKMHD(kmhd)) {
            return false; // Đã tồn tại, không thêm
        }
        if (dao.timKMHD(kmhd.getIdPromotion()) != null) {
            return false; // Đã tồn tại trong cơ sở dữ liệu, không thêm
        }
        dsKMHD.add(kmhd);
        return true;
    }

    public boolean xoaKMHD(String maKM) {
        PromotionalInvoiceDTO kmhd = null;
        for (PromotionalInvoiceDTO km : dsKMHD) {
            if (km.getIdPromotion().equals(maKM)) {
                kmhd = km;
                break;
            }
        }
        if (kmhd != null) {
            dsKMHD.remove(kmhd);
            return true;
        }
        if(dao.timKMHD(maKM) != null) {
            dao.xoaKMHD(maKM);
            return true; // Xóa thành công từ cơ sở dữ liệu
        }
        return false; // Không tìm thấy, không xóa
    }

    public boolean suaKMHD(PromotionalInvoiceDTO kmhd) {
        for (int i = 0; i < dsKMHD.size(); i++) {
            if (dsKMHD.get(i).getIdPromotion().equals(kmhd.getIdPromotion())) {
                dsKMHD.set(i, kmhd);
                return true; // Sửa thành công
            }
        }
        if(dao.timKMHD(kmhd.getIdPromotion()) != null) {
            dao.suaKMHD(kmhd);
            return true; // Sửa thành công từ cơ sở dữ liệu
        }
        return false; // Không tìm thấy, không sửa
    }
}