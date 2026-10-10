package org.toursalesmanager.bus;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.FieldDefaults;
import org.toursalesmanager.dao.PromotionDAO;
import org.toursalesmanager.dao.PromotionalInvoiceDAO;
import org.toursalesmanager.dao.PromotionalTourDAO;
import org.toursalesmanager.dto.PromotionDTO;
import org.toursalesmanager.dto.PromotionalInvoiceDTO;
import org.toursalesmanager.dto.KMTourDTO;

import java.util.*;

@Getter
@Setter
@FieldDefaults(level = AccessLevel.PRIVATE)
public class PromotionBUS {
    ArrayList<PromotionDTO> dsCTrinhKM;
    PromotionDAO dao;

    public PromotionBUS() {
        dao = new PromotionDAO();
        this.dsCTrinhKM = dao.getDsCTrinhKM();
    }

    public ArrayList<PromotionDTO> getAllPromotions(){
        return dao.getDsCTrinhKM();
    }

    public boolean timCTrinhKM(PromotionDTO ct) {
        for (PromotionDTO c : dsCTrinhKM) {
            if (c.getIdPromotion().equals(ct.getIdPromotion())) {
                return true;
            }
        }
        return false;
    }

    public boolean themCTrinhKM(PromotionDTO ct) {
        // Kiểm tra trùng mã
        if (dao.timCTrinhKM(ct.getIdPromotion()) != null) return false;

        // Bước 1: Thêm vào bảng CTrinhKM (bảng cha)
        boolean resultCha = dao.themCTrinhKM(ct);
        if (!resultCha) return false;

        boolean resultCon = false;
        try {
            if (ct instanceof KMTourDTO) {
                PromotionalTourDAO daoTour = new PromotionalTourDAO();
                resultCon = daoTour.themKMTour((KMTourDTO) ct);
            } else if (ct instanceof PromotionalInvoiceDTO) {
                PromotionalInvoiceDAO daoKmhd = new PromotionalInvoiceDAO();
                resultCon = daoKmhd.themKMHD((PromotionalInvoiceDTO) ct);
            } else {
                // Trường hợp chỉ có CTrinhKM (không có chi tiết)
                resultCon = true;
            }

            // Nếu thêm chi tiết thất bại (false), rollback thủ công
            if (!resultCon) {
                dao.xoaCTrinhKM(ct.getIdPromotion());
                return false;
            }
        } catch (Exception e) {
            // Có lỗi phát sinh (ví dụ SQLException), rollback
            dao.xoaCTrinhKM(ct.getIdPromotion());
            e.printStackTrace();
            return false;
        }

        // Cập nhật danh sách nội bộ
        dsCTrinhKM.add(ct);
        return true;
    }

    public PromotionDTO getFullCTrinhKM(String maKM) {
        PromotionDTO basic = dao.timCTrinhKM(maKM);
        if (basic == null) return null;
        if (basic.isHinhThucKM()) {
            PromotionalInvoiceDAO daoHD = new PromotionalInvoiceDAO();
            return daoHD.timKMHD(maKM);   // Trả về đối tượng KMHD đầy đủ
        } else {
            PromotionalTourDAO daoTour = new PromotionalTourDAO();
            return daoTour.timKMTour(maKM);
        }
    }

    public boolean xoaCTrinhKM(String maKM) {
        PromotionDTO ct = timCTrinhKM(maKM);
        if (ct == null) return false;

        boolean result = false;
        if (ct instanceof KMTourDTO) {
            PromotionalTourDAO daoTour = new PromotionalTourDAO();
            result = daoTour.xoaKMTour(maKM);
        } else if (ct instanceof PromotionalInvoiceDTO) {
            PromotionalInvoiceDAO daoKmhd = new PromotionalInvoiceDAO();
            result = daoKmhd.xoaKMHD(maKM);
        } else {
            result = dao.xoaCTrinhKM(maKM);
        }

        if (result) {
            dsCTrinhKM.remove(ct);
        }
        return result;
    }

    public boolean suaCTrinhKM(PromotionDTO ct) {
        boolean result = false;
        if (ct instanceof KMTourDTO) {
            PromotionalTourDAO daoTour = new PromotionalTourDAO();
            result = daoTour.suaKMTour((KMTourDTO) ct);
        } else if (ct instanceof PromotionalInvoiceDTO) {
            PromotionalInvoiceDAO daoKmhd = new PromotionalInvoiceDAO();
            result = daoKmhd.suaKMHD((PromotionalInvoiceDTO) ct);
        } else {
            result = dao.suaCTrinhKM(ct);
        }

        if (result) {
            // Cập nhật trong danh sách nội bộ
            for (int i = 0; i < dsCTrinhKM.size(); i++) {
                if (dsCTrinhKM.get(i).getIdPromotion().equals(ct.getIdPromotion())) {
                    dsCTrinhKM.set(i, ct);
                    break;
                }
            }
        }
        return result;
    }

    public PromotionDTO timCTrinhKM(String maKM) {
        for (PromotionDTO ct : dsCTrinhKM) {
            if (ct.getIdPromotion().equals(maKM)) {
                return ct;
            }
        }
        return null; // Không tìm thấy
    }

    public ArrayList<PromotionDTO> searchCTrinhKM(String loai, String keyword) {
        ArrayList<PromotionDTO> result = new ArrayList<>();
        for (PromotionDTO ct : dsCTrinhKM) {
            switch (loai) {
                case "Tất cả":
                    if (ct.getIdPromotion().toLowerCase().contains(keyword.toLowerCase())
                            || ct.getPromotionName().toLowerCase().contains(keyword.toLowerCase())) {
                        result.add(ct);
                    }
                    break;
                case "KMHD":
                    if (ct.isHinhThucKM() && (ct.getIdPromotion().toLowerCase().contains(keyword.toLowerCase())||ct.getPromotionName().toLowerCase().contains(keyword.toLowerCase()))) {
                        result.add(ct);
                    }
                    break;
                case "KMTour":
                    if (!ct.isHinhThucKM() && (ct.getIdPromotion().toLowerCase().contains(keyword.toLowerCase())||ct.getPromotionName().toLowerCase().contains(keyword.toLowerCase()))) {
                        result.add(ct);
                    }
                    break;
            }
        }
        return result;
    }

    public void ghiDsCTrinhKM() {
        // Ghi dsCTrinhKM vào database thông qua DAO
        for (PromotionDTO ct : dsCTrinhKM) {
            if (dao.timCTrinhKM(ct.getIdPromotion()) != null) {
                dao.suaCTrinhKM(ct); // Cập nhật nếu đã tồn tại
            } else {
                dao.themCTrinhKM(ct); // Thêm mới nếu chưa tồn tại
            }
        }
    }

    public PromotionDTO maptoCTrinhKM(String maKM) {
        PromotionDTO ct = dao.timCTrinhKM(maKM);

        if (ct != null) {
            return ct;
        }
        return null;
    }
}