package org.example.bus;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.FieldDefaults;
import org.example.dao.CTrinhKMDAO;
import org.example.dao.KMHDDAO;
import org.example.dao.KMTourDAO;
import org.example.dto.CTrinhKMDTO;
import org.example.dto.KMHDDTO;
import org.example.dto.KMTourDTO;

import java.util.*;

@Getter
@Setter
@FieldDefaults(level = AccessLevel.PRIVATE)
public class CTrinhKMBUS {
    ArrayList<CTrinhKMDTO> dsCTrinhKM;
    CTrinhKMDAO dao;

    public CTrinhKMBUS() {
        dao = new CTrinhKMDAO();
        this.dsCTrinhKM = dao.getDsCTrinhKM();
    }

    public boolean timCTrinhKM(CTrinhKMDTO ct) {
        for (CTrinhKMDTO c : dsCTrinhKM) {
            if (c.getMaKM().equals(ct.getMaKM())) {
                return true;
            }
        }
        return false;
    }

    public boolean themCTrinhKM(CTrinhKMDTO ct) {
        // Kiểm tra trùng mã
        if (dao.timCTrinhKM(ct.getMaKM()) != null) return false;

        // Bước 1: Thêm vào bảng CTrinhKM (bảng cha)
        boolean resultCha = dao.themCTrinhKM(ct);
        if (!resultCha) return false;

        boolean resultCon = false;
        try {
            if (ct instanceof KMTourDTO) {
                KMTourDAO daoTour = new KMTourDAO();
                resultCon = daoTour.themKMTour((KMTourDTO) ct);
            } else if (ct instanceof KMHDDTO) {
                KMHDDAO daoKmhd = new KMHDDAO();
                resultCon = daoKmhd.themKMHD((KMHDDTO) ct);
            } else {
                // Trường hợp chỉ có CTrinhKM (không có chi tiết)
                resultCon = true;
            }

            // Nếu thêm chi tiết thất bại (false), rollback thủ công
            if (!resultCon) {
                dao.xoaCTrinhKM(ct.getMaKM());
                return false;
            }
        } catch (Exception e) {
            // Có lỗi phát sinh (ví dụ SQLException), rollback
            dao.xoaCTrinhKM(ct.getMaKM());
            e.printStackTrace();
            return false;
        }

        // Cập nhật danh sách nội bộ
        dsCTrinhKM.add(ct);
        return true;
    }

    public CTrinhKMDTO getFullCTrinhKM(String maKM) {
        CTrinhKMDTO basic = dao.timCTrinhKM(maKM);
        if (basic == null) return null;
        if (basic.isHinhThucKM()) {
            KMHDDAO daoHD = new KMHDDAO();
            return daoHD.timKMHD(maKM);   // Trả về đối tượng KMHD đầy đủ
        } else {
            KMTourDAO daoTour = new KMTourDAO();
            return daoTour.timKMTour(maKM);
        }
    }

    public boolean xoaCTrinhKM(String maKM) {
        CTrinhKMDTO ct = timCTrinhKM(maKM);
        if (ct == null) return false;

        boolean result = false;
        if (ct instanceof KMTourDTO) {
            KMTourDAO daoTour = new KMTourDAO();
            result = daoTour.xoaKMTour(maKM);
        } else if (ct instanceof KMHDDTO) {
            KMHDDAO daoKmhd = new KMHDDAO();
            result = daoKmhd.xoaKMHD(maKM);
        } else {
            result = dao.xoaCTrinhKM(maKM);
        }

        if (result) {
            dsCTrinhKM.remove(ct);
        }
        return result;
    }

    public boolean suaCTrinhKM(CTrinhKMDTO ct) {
        boolean result = false;
        if (ct instanceof KMTourDTO) {
            KMTourDAO daoTour = new KMTourDAO();
            result = daoTour.suaKMTour((KMTourDTO) ct);
        } else if (ct instanceof KMHDDTO) {
            KMHDDAO daoKmhd = new KMHDDAO();
            result = daoKmhd.suaKMHD((KMHDDTO) ct);
        } else {
            result = dao.suaCTrinhKM(ct);
        }

        if (result) {
            // Cập nhật trong danh sách nội bộ
            for (int i = 0; i < dsCTrinhKM.size(); i++) {
                if (dsCTrinhKM.get(i).getMaKM().equals(ct.getMaKM())) {
                    dsCTrinhKM.set(i, ct);
                    break;
                }
            }
        }
        return result;
    }

    public CTrinhKMDTO timCTrinhKM(String maKM) {
        for (CTrinhKMDTO ct : dsCTrinhKM) {
            if (ct.getMaKM().equals(maKM)) {
                return ct;
            }
        }
        return null; // Không tìm thấy
    }

    public ArrayList<CTrinhKMDTO> searchCTrinhKM(String loai, String keyword) {
        ArrayList<CTrinhKMDTO> result = new ArrayList<>();
        for (CTrinhKMDTO ct : dsCTrinhKM) {
            switch (loai) {
                case "Tất cả":
                    if (ct.getMaKM().toLowerCase().contains(keyword.toLowerCase())
                            || ct.getTenKM().toLowerCase().contains(keyword.toLowerCase())) {
                        result.add(ct);
                    }
                    break;
                case "KMHD":
                    if (ct.isHinhThucKM() && (ct.getMaKM().toLowerCase().contains(keyword.toLowerCase())||ct.getTenKM().toLowerCase().contains(keyword.toLowerCase()))) {
                        result.add(ct);
                    }
                    break;
                case "KMTour":
                    if (!ct.isHinhThucKM() && (ct.getMaKM().toLowerCase().contains(keyword.toLowerCase())||ct.getTenKM().toLowerCase().contains(keyword.toLowerCase()))) {
                        result.add(ct);
                    }
                    break;
            }
        }
        return result;
    }

    public void docDsCTrinhKM() {
        dsCTrinhKM = dao.getDsCTrinhKM();
    }

    public void ghiDsCTrinhKM() {
        // Ghi dsCTrinhKM vào database thông qua DAO
        for (CTrinhKMDTO ct : dsCTrinhKM) {
            if (dao.timCTrinhKM(ct.getMaKM()) != null) {
                dao.suaCTrinhKM(ct); // Cập nhật nếu đã tồn tại
            } else {
                dao.themCTrinhKM(ct); // Thêm mới nếu chưa tồn tại
            }
        }
    }

    public CTrinhKMDTO maptoCTrinhKM(String maKM) {
        CTrinhKMDTO ct = dao.timCTrinhKM(maKM);

        if (ct != null) {
            return ct;
        }
        return null;
    }
}