package org.example.bus;

import org.example.dao.KMTourDAO;
import org.example.dto.KMTourDTO;

import java.util.*;

public class KMTourBUS {
    private ArrayList<KMTourDTO> dsKMTour;
    public static KMTourDAO dao;
    public KMTourBUS() {
        if (dsKMTour == null) {
            // dao = new DsKMTour();
            //khoit tao dsKMTour tu database
            dsKMTour = dao.getDsKMTour();
        }
    }

    public ArrayList<KMTourDTO> getDsKMTour() {
        return dsKMTour;
    }

    public void setDsKMTour(ArrayList<KMTourDTO> dsKMTour) {
        this.dsKMTour = dsKMTour;
    }

    public KMTourDTO timKMTour(String maKM) {
        for (KMTourDTO kmTour : dsKMTour) {
            if (kmTour.getMaKM().equals(maKM)) {
                return kmTour;
            }
        }
        return null;
    }

    public boolean timKMTour(KMTourDTO kmTour) {
        for (KMTourDTO km : dsKMTour) {
            if (km.getMaKM().equals(kmTour.getMaKM())) {
                return true;
            }
        }
        if(dao.timKMTour(kmTour.getMaKM()) != null) {
            return true; // Đã tồn tại trong cơ sở dữ liệu
        }
        return false;
    }

    public boolean themKMTour(KMTourDTO kmTour) {
        if (timKMTour(kmTour)) {
            return false; // Đã tồn tại, không thêm
        }
        dsKMTour.add(kmTour);
        return true;
    }

    public boolean xoaKMTour(String maKM) {
        KMTourDTO kmTour = timKMTour(maKM);
        if (kmTour != null) {
            dsKMTour.remove(kmTour);
            return true; // Xóa thành công
        }
        if(dao.timKMTour(maKM) != null) {
            // Xóa từ cơ sở dữ liệu nếu tồn tại
            dao.xoaKMTour(maKM);
            return true; // Giả sử xóa thành công từ database
        }
        return false; // Không tìm thấy, không xóa
    }

    public boolean suaKMTour(KMTourDTO kmTour) {
        for (int i = 0; i < dsKMTour.size(); i++) {
            if (dsKMTour.get(i).getMaKM().equals(kmTour.getMaKM())) {
                dsKMTour.set(i, kmTour);
                return true; // Sửa thành công
            }
        }
        if(dao.timKMTour(kmTour.getMaKM()) != null) {
            // Cập nhật trong cơ sở dữ liệu nếu tồn tại
            dao.suaKMTour(kmTour);
            return true; // Giả sử cập nhật thành công từ database
        }
        return false; // Không tìm thấy, không sửa
    }
}