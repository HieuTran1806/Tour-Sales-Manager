package org.example.bus;

import org.example.dao.KhachHangDAO;
import org.example.dto.KhachHangDTO;

import java.util.ArrayList;
import java.util.List;

public class KhachHangBUS {
    public static ArrayList<KhachHangDTO> dsKH;
    public static KhachHangDAO dataKH = new KhachHangDAO();

    public KhachHangBUS() {
        docDSKH();
    }
    public void docDSKH() {
        dsKH = dataKH.layDanhSachKHang();
    }
    public void them(KhachHangDTO khang) {
        try{
            if (dsKH == null) {
                dsKH = new ArrayList<KhachHangDTO>();
            }
            if (khang == null) {
                return;
            }
            if (khang.getMaKH() == null || khang.getMaKH().isEmpty()) {
                return;
            }
            for (KhachHangDTO existingKH : dsKH) {
                if (existingKH.getMaKH().equals(khang.getMaKH())) {
                    return;
                }
            }
            dataKH.themKhachHang(khang);
            if (dsKH != null) {
                dsKH.add(khang);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    public void xoaKhachHang(String maKH) {
        try {
            if (dsKH == null) {
                return;
            }
            dataKH.xoaKhachHang(maKH);
            dsKH.removeIf(kh -> kh.getMaKH().equals(maKH));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    public KhachHangDTO timKiemKH(String maKH){
        for (KhachHangDTO kh : dsKH) {
            if (kh.getMaKH().equals(maKH)) {
                return dataKH.timKhachHangTheoMa(maKH);
            }
        }
        return null;
    }

    public List<KhachHangDTO> timKhachHang(String column, String keyword) {
        return dataKH.timKhachHang(column, keyword);
    }

    public boolean suaKhachHang(KhachHangDTO khang) {
        try {
            if (dsKH == null) {
                return false;
            }

            boolean success = dataKH.suaKhachHang(khang);
            if (success) {
                for (int i = 0; i < dsKH.size(); i++) {
                    if (dsKH.get(i).getMaKH().equals(khang.getMaKH())) {
                        dsKH.set(i, khang);
                        break;
                    }
                }
            }
            return success;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }
}