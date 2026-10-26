package org.toursalesmanager.bus;

import org.toursalesmanager.dao.TourTypeDAO;
import org.toursalesmanager.dto.TourTypeDTO;
import org.toursalesmanager.enums.Permission;
import org.toursalesmanager.login.SessionManager;

import java.util.ArrayList;

public class TourTypeBUS {
    private ArrayList<TourTypeDTO> lsCate;
    private TourTypeDAO tourTypeDAO;

    public TourTypeBUS(){
        tourTypeDAO = new TourTypeDAO();
        lsCate = new ArrayList<>();
    }

    public ArrayList<TourTypeDTO> getAllLoaiTour(){
        lsCate = tourTypeDAO.getAllTourTypes();
        return lsCate;
    }

    public boolean addLoaiTour(TourTypeDTO cate){
        requireManagePermission();
        if(cate == null) return false;

        boolean success = tourTypeDAO.addTourType(cate);
        if(success) lsCate.add(cate);

        return success;
    }

    public String getNextTourTypeId(){
        requireManagePermission();
        return tourTypeDAO.getNextTourTypeId();
    }

    public boolean editLoaiTour(TourTypeDTO cate){
        requireManagePermission();
        return tourTypeDAO.editTourType(cate);
    }

    public boolean removeLoaiTour(String maLoaiTour){
        requireManagePermission();
        return tourTypeDAO.removeTourType(maLoaiTour);
    }

    public ArrayList<TourTypeDTO> search(String keyWord){
        ArrayList<TourTypeDTO> list = new ArrayList<>();

        for (TourTypeDTO lt : lsCate){
            if(lt.getTypeOfTour().trim().toLowerCase().contains(keyWord)){
                list.add(lt);
            }
        }
        return list;
    }

    public TourTypeDTO getById(String maLoaiTour){
        TourTypeDTO result = null;
        lsCate = tourTypeDAO.getAllTourTypes();
        for (TourTypeDTO lt : lsCate){
            if(lt.getIdTourType().trim().equalsIgnoreCase(maLoaiTour)) {
                result = lt;
                break;
            }
        }
        return result;
    }

    public boolean existedLoaiTourWithID(String maLoaiTour){
        for (TourTypeDTO lt : lsCate){
            if(lt.getIdTourType().trim().equalsIgnoreCase(maLoaiTour))
                return true;
        }
        return false;
    }

    private void requireManagePermission() {
        if (!SessionManager.hasPermission(Permission.MANAGE_TOUR_TYPE)) {
            throw new SecurityException(
                    "Bạn không có quyền quản lý loại tour."
            );
        }
    }
}
