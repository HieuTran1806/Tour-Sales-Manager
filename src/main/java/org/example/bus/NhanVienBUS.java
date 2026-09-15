package org.example.bus;
import java.util.ArrayList;
import org.example.dao.NhanVienDAO;
import org.example.dto.NhanVienDTO;
import java.util.List;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

public class NhanVienBUS {
    public static ArrayList<NhanVienDTO> dsNV;
    public static NhanVienDAO dataNV = new NhanVienDAO();
    public NhanVienBUS() {
        if (dsNV == null) {
            dsNV = dataNV.layDanhSachNV();
        }
    }

    public void docDSNV() {
        if (dsNV == null) {
            dsNV = new ArrayList<NhanVienDTO>();
        }
        dsNV = dataNV.layDanhSachNV();
    }
    public void them(NhanVienDTO nv) {
        try{
            if (dsNV == null) {
                dsNV = new ArrayList<NhanVienDTO>();
            }
            if (nv == null) {
                return;
            }
            if (nv.getMaNV() == null || nv.getMaNV().isEmpty()) {
                return;
            }
            for (NhanVienDTO existingNV : dsNV) {
                if (existingNV.getMaNV().equals(nv.getMaNV())) {
                    return;
                }
            }
            dataNV.themNhanVien(nv);
            if (dsNV != null) {
                dsNV.add(nv);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    public void xoaNhanVien(String maNV) {
        try {
            if (dsNV == null) {
                return;
            }
            dataNV.xoaNhanVien(maNV);
            dsNV.removeIf(nv -> nv.getMaNV().equals(maNV));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public NhanVienDTO timNhanVienTheoMa(String maNV) {
        if (dsNV == null) {
            return null;
        }
        for (NhanVienDTO nv : dsNV) {
            if (nv.getMaNV().equals(maNV)) {
                dataNV.timNhanVienTheoMa(maNV);
                return nv;
            }
        }
        return null;
    }


    public List<NhanVienDTO> timNhanVien(String type, String keyword) {
        return dataNV.timNhanVien(type, keyword);
    }

    public boolean suaNhanVien(NhanVienDTO nv) {
        try {
            if (dsNV == null) {
                return false;
            }
            boolean success = dataNV.suaNhanVien(nv);
            if (success) {
                for (int i = 0; i < dsNV.size(); i++) {
                    if (dsNV.get(i).getMaNV().equals(nv.getMaNV())) {
                        dsNV.set(i, nv);
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