package org.toursalesmanager.bus;

import org.toursalesmanager.dao.TourPlanDetailDAO;
import org.toursalesmanager.dto.TourPlanDetailDTO;

import java.util.ArrayList;

public class TourPlanDetailBUS {
    private ArrayList<TourPlanDetailDTO> lsCTietKHTours;
    private TourPlanDetailDAO tourPlanDetailDAO;

    //constructor
    public TourPlanDetailBUS(){
        tourPlanDetailDAO = new TourPlanDetailDAO();
        lsCTietKHTours = new ArrayList<>();
    }

    public ArrayList<TourPlanDetailDTO> getAllCTietKHTours(){
        lsCTietKHTours = tourPlanDetailDAO.getAllCTietKHTours();
        return lsCTietKHTours;
    }
    public boolean addCTietKHTour(TourPlanDetailDTO t){
        if(t == null) return false;

        boolean success = tourPlanDetailDAO.addCTietKHTour(t);
        if(success) lsCTietKHTours.add(t);

        return success;
    }

    public boolean editCTietKHTour(TourPlanDetailDTO t){
        return tourPlanDetailDAO.editCTietKHTour(t);
    }

    public boolean removeCTietKHTour(String maCTietKHTour){
        return tourPlanDetailDAO.removeCTietKHTour(maCTietKHTour);
    }

    public TourPlanDetailDTO getCTietKHTourById(String maCTietKHTour){
        TourPlanDetailDTO result = null;
        for (TourPlanDetailDTO ct : lsCTietKHTours){
            if(ct.getIdTourPlan().trim().equalsIgnoreCase(maCTietKHTour)) {
                result = ct;
                break;
            }
        }
        return result;
    }

    // get CTietKeHTour equal with maKHTour
    public ArrayList<TourPlanDetailDTO> getLsCTietKHToursById(String maKHTour){
        ArrayList<TourPlanDetailDTO> result = new ArrayList<>(); // result CTietKHTours
        ArrayList<TourPlanDetailDTO> list = getAllCTietKHTours(); // list CTietKHTours

        for (TourPlanDetailDTO ct : list){
            if(ct.getIdTourPlan().trim().equalsIgnoreCase(maKHTour)) {
                result.add(ct);
            }
        }
        return result;
    }

    public boolean existedCTietKHTourWithID(String maCTKHTour){
        ArrayList<TourPlanDetailDTO> list = getAllCTietKHTours(); // list CTietKHTours

        for (TourPlanDetailDTO ct : list){
            if(ct.getIdTourPlan().trim().equalsIgnoreCase(maCTKHTour))
                return true;
        }
        return false;
    }
}
