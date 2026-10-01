package org.example.bus;

import org.example.dao.TourTypeDAO;
import org.example.dto.TourTypeDTO;

import java.util.ArrayList;

public class TourTypeBUS {
    private ArrayList<TourTypeDTO> lsCate;
    private TourTypeDAO tourTypeDAO;

    public TourTypeBUS(){
        tourTypeDAO = new TourTypeDAO();
        lsCate = new ArrayList<>();
    }

    public ArrayList<TourTypeDTO> getAllLoaiTour(){
        lsCate = tourTypeDAO.getAllLoaiTour();
        return lsCate;
    }

    public boolean addLoaiTour(TourTypeDTO cate){
        if(cate == null) return false;

        boolean success = tourTypeDAO.addLoaiTour(cate);
        if(success) lsCate.add(cate);

        return success;
    }

    public boolean editLoaiTour(TourTypeDTO cate){
        return tourTypeDAO.editLoaiTour(cate);
    }

    public boolean removeLoaiTour(String maLoaiTour){
        return tourTypeDAO.removeLoaiTour(maLoaiTour);
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
        lsCate = tourTypeDAO.getAllLoaiTour();
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
}
