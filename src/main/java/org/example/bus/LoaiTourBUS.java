package org.example.bus;

import org.example.dao.LoaiTourDAO;
import org.example.dto.LoaiTourDTO;

import java.util.ArrayList;

public class LoaiTourBUS {
    private ArrayList<LoaiTourDTO> lsCate;
    private LoaiTourDAO loaiTourDAO;

    public LoaiTourBUS(){
        loaiTourDAO = new LoaiTourDAO();
        lsCate = new ArrayList<>();
    }

    public ArrayList<LoaiTourDTO> getAllLoaiTour(){
        lsCate = loaiTourDAO.getAllLoaiTour();
        return lsCate;
    }

    public boolean addLoaiTour(LoaiTourDTO cate){
        if(cate == null) return false;

        boolean success = loaiTourDAO.addLoaiTour(cate);
        if(success) lsCate.add(cate);

        return success;
    }

    public boolean editLoaiTour(LoaiTourDTO cate){
        return loaiTourDAO.editLoaiTour(cate);
    }

    public boolean removeLoaiTour(String maLoaiTour){
        return loaiTourDAO.removeLoaiTour(maLoaiTour);
    }

    public ArrayList<LoaiTourDTO> search(String keyWord){
        ArrayList<LoaiTourDTO> list = new ArrayList<>();

        for (LoaiTourDTO lt : lsCate){
            if(lt.getTheLoai().trim().toLowerCase().contains(keyWord)){
                list.add(lt);
            }
        }
        return list;
    }

    public LoaiTourDTO getById(String maLoaiTour){
        LoaiTourDTO result = null;
        lsCate = loaiTourDAO.getAllLoaiTour();
        for (LoaiTourDTO lt : lsCate){
            if(lt.getMaLoaiTour().trim().equalsIgnoreCase(maLoaiTour)) {
                result = lt;
                break;
            }
        }
        return result;
    }

    public boolean existedLoaiTourWithID(String maLoaiTour){
        for (LoaiTourDTO lt : lsCate){
            if(lt.getMaLoaiTour().trim().equalsIgnoreCase(maLoaiTour))
                return true;
        }
        return false;
    }
}
