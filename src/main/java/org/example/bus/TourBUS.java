package org.example.bus;

import org.example.dao.TourDAO;
import org.example.dto.TourDTO;

import java.security.PublicKey;
import java.util.ArrayList;

public class TourBUS {
    private ArrayList<TourDTO> lsTour;
    private TourDAO tourDAO;
    private KeHoachTourBUS keHoachTourBUS;

    //constructor
    public TourBUS(){
        tourDAO = new TourDAO();
        lsTour = new ArrayList<>();
        keHoachTourBUS = new KeHoachTourBUS();
    }

    public ArrayList<TourDTO> getAllTours(){
        lsTour = tourDAO.getAllTours();
        return lsTour;
    }

    public boolean addTour(TourDTO t){
        if(t == null) return false;

        if(t.getSoNgay() <= 0 || t.getSoCho() < 0){
            return false;
        }

        boolean success = tourDAO.addTour(t);
        if(success) lsTour.add(t);

        return success;
    }

    public boolean editTour(TourDTO t){
        if(t.getSoNgay() <= 0 || t.getSoCho() < 0)
            return false;

        return tourDAO.editTour(t);
    }

    public boolean removeTour(String maTour){
        return tourDAO.removeTour(maTour);
    }

    public ArrayList<TourDTO> search(String keyWord){
        ArrayList<TourDTO> list = new ArrayList<>();
        for (TourDTO lt : lsTour){
            if(lt.getTen().trim().toLowerCase().contains(keyWord)){
                list.add(lt);
            }
        }
        return list;
    }

    public TourDTO getByID(String maTour){
        TourDTO tour = new TourDTO();
        for (TourDTO t : lsTour){
            if(t.getMaTour().trim().equalsIgnoreCase(maTour)){
                tour = t;
                break;
            }
        }
        return tour;
    }

    public boolean existedTourWithID(String maTour){
        for (TourDTO t : lsTour){
            if(t.getMaTour().trim().equalsIgnoreCase(maTour))
                return true;
        }
        return false;
    }

    public ArrayList<TourDTO> getListsByName(String name){
        if(name.isEmpty()){
            return null;
        }
        lsTour = tourDAO.getListByName(name);
        return lsTour;
    }

    public int getVacantSpot(String maTour){
        TourDTO t = getByID(maTour);
        if(t != null){
            return t.getSoCho();
        }

        return 0;
    }


}
