package org.example.bus;
import java.util.ArrayList;
import org.example.dao.StaffDAO;
import org.example.dto.StaffDTO;
import java.util.List;

public class StaffBUS {
    public ArrayList<StaffDTO> dsNV;
    public StaffDAO dao = new StaffDAO();

    public StaffBUS() {
        if (dsNV == null) {
            dsNV = dao.getAllStaffs();
        }
    }

    public ArrayList<StaffDTO> getAllStaffs(){
        return dao.getAllStaffs();
    }

    public void them(StaffDTO nv) {
        try{
            if (dsNV == null) {
                dsNV = new ArrayList<StaffDTO>();
            }
            if (nv == null) {
                return;
            }
            if (nv.getIdStaff() == null || nv.getIdStaff().isEmpty()) {
                return;
            }
            for (StaffDTO existingNV : dsNV) {
                if (existingNV.getIdStaff().equals(nv.getIdStaff())) {
                    return;
                }
            }
            dao.themNhanVien(nv);
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
            dao.xoaNhanVien(maNV);
            dsNV.removeIf(nv -> nv.getIdStaff().equals(maNV));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public StaffDTO timNhanVienTheoMa(String maNV) {
        if (dsNV == null) {
            return null;
        }
        for (StaffDTO nv : dsNV) {
            if (nv.getIdStaff().equals(maNV)) {
                dao.timNhanVienTheoMa(maNV);
                return nv;
            }
        }
        return null;
    }


    public List<StaffDTO> timNhanVien(String type, String keyword) {
        return dao.timNhanVien(type, keyword);
    }

    public boolean suaNhanVien(StaffDTO nv) {
        try {
            if (dsNV == null) {
                return false;
            }
            boolean success = dao.suaNhanVien(nv);
            if (success) {
                for (int i = 0; i < dsNV.size(); i++) {
                    if (dsNV.get(i).getIdStaff().equals(nv.getIdStaff())) {
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