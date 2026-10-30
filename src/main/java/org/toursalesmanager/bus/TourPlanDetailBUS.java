package org.toursalesmanager.bus;

import org.toursalesmanager.dao.TourPlanDetailDAO;
import org.toursalesmanager.dto.TourPlanDetailDTO;
import org.toursalesmanager.enums.Permission;
import org.toursalesmanager.login.SessionManager;

import java.util.ArrayList;

public class TourPlanDetailBUS {
    private final TourPlanDetailDAO tourPlanDetailDAO;

    //constructor
    public TourPlanDetailBUS(){
        tourPlanDetailDAO = new TourPlanDetailDAO();
    }

    public ArrayList<TourPlanDetailDTO> getAllCTietKHTours(){
        return tourPlanDetailDAO.getAllCTietKHTours();
    }

    public String getNextTourPlanDetailId(){
        requireManagePermission();
        return tourPlanDetailDAO.getNextTourPlanDetailId();
    }

    public boolean addCTietKHTour(TourPlanDetailDTO t){
        requireManagePermission();
        if(t == null) return false;

        return tourPlanDetailDAO.addCTietKHTour(t);
    }

    public boolean editCTietKHTour(TourPlanDetailDTO t){
        requireManagePermission();
        if(t == null) return false;

        return tourPlanDetailDAO.editCTietKHTour(t);
    }

    public boolean removeCTietKHTour(String maCTietKHTour){
        requireManagePermission();
        return tourPlanDetailDAO.removeCTietKHTour(maCTietKHTour);
    }

    public TourPlanDetailDTO getCTietKHTourById(String maCTietKHTour){
        if (maCTietKHTour == null || maCTietKHTour.isBlank()) return null;
        return tourPlanDetailDAO.getById(maCTietKHTour.trim());
    }

    // get CTietKeHTour equal with maKHTour
    public ArrayList<TourPlanDetailDTO> getLsCTietKHToursById(String maKHTour){
        if (maKHTour == null || maKHTour.isBlank()) return new ArrayList<>();
        return tourPlanDetailDAO.getByTourPlanId(maKHTour.trim());
    }

    public boolean existedCTietKHTourWithID(String maCTKHTour){
        return getCTietKHTourById(maCTKHTour) != null;
    }

    private void requireManagePermission() {
        if (!SessionManager.hasPermission(Permission.MANAGE_TOUR_PLAN)) {
            throw new SecurityException(
                    "Bạn không có quyền quản lý chi tiết kế hoạch tour."
            );
        }
    }
}
