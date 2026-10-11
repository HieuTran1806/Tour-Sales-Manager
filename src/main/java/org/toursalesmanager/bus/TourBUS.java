package org.toursalesmanager.bus;

import org.toursalesmanager.dao.TourDAO;
import org.toursalesmanager.dto.TourDTO;
import org.toursalesmanager.enums.Permission;
import org.toursalesmanager.login.SessionManager;

import java.util.ArrayList;

public class TourBUS {
    private ArrayList<TourDTO> lsTour;
    private TourDAO tourDAO;
    private TourPlanBUS tourPlanBUS;

    //constructor
    public TourBUS(){
        tourDAO = new TourDAO();
        lsTour = new ArrayList<>();
        tourPlanBUS = new TourPlanBUS();
    }

    public ArrayList<TourDTO> getAllTours(){
        requirePermission(Permission.VIEW_TOUR);
        lsTour = tourDAO.getAllTours();
        return lsTour;
    }

    public boolean addTour(TourDTO t){
        requirePermission(Permission.MANAGE_TOUR);
        if(t == null) return false;

        if(t.getNumberOfDate() <= 0 || t.getSeats() < 0){
            return false;
        }

        boolean success = tourDAO.addTour(t);
        if(success) lsTour.add(t);

        return success;
    }

    public boolean editTour(TourDTO t){
        requirePermission(Permission.MANAGE_TOUR);
        if (t == null) return false;
        if(t.getNumberOfDate() <= 0 || t.getSeats() < 0)
            return false;

        return tourDAO.editTour(t);
    }

    public boolean removeTour(String maTour){
        requirePermission(Permission.MANAGE_TOUR);
        return tourDAO.removeTour(maTour);
    }

    public ArrayList<TourDTO> search(String keyWord){
        requirePermission(Permission.VIEW_TOUR);
        ArrayList<TourDTO> list = new ArrayList<>();
        for (TourDTO lt : lsTour){
            if(lt.getTourName().trim().toLowerCase().contains(keyWord)){
                list.add(lt);
            }
        }
        return list;
    }

    public TourDTO getByID(String maTour){
        requirePermission(Permission.VIEW_TOUR);
        TourDTO tour = new TourDTO();
        for (TourDTO t : lsTour){
            if(t.getIdTour().trim().equalsIgnoreCase(maTour)){
                tour = t;
                break;
            }
        }
        return tour;
    }

    public boolean existedTourWithID(String maTour){
        requirePermission(Permission.VIEW_TOUR);
        for (TourDTO t : lsTour){
            if(t.getIdTour().trim().equalsIgnoreCase(maTour))
                return true;
        }
        return false;
    }

    public ArrayList<TourDTO> getListsByName(String name){
        requirePermission(Permission.VIEW_TOUR);
        if(name.isEmpty()){
            return null;
        }
        lsTour = tourDAO.getListByName(name);
        return lsTour;
    }

    public int getVacantSpot(String maTour){
        requirePermission(Permission.VIEW_TOUR);
        TourDTO t = getByID(maTour);
        if(t != null){
            return t.getSeats();
        }

        return 0;
    }

    private void requirePermission(Permission permission) {
        if (!SessionManager.hasPermission(permission)) {
            throw new SecurityException(
                    "Bạn không có quyền thực hiện thao tác này"
            );
        }
    }

}
