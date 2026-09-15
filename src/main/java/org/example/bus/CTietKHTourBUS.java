package org.example.bus;

import org.example.dao.CTietKHTourDAO;
import org.example.dto.CTietKHTourDTO;

import java.util.ArrayList;

public class CTietKHTourBUS {
    private ArrayList<CTietKHTourDTO> lsCTietKHTours;
    private CTietKHTourDAO cTietKHTourDAO;

    //constructor
    public CTietKHTourBUS(){
        cTietKHTourDAO = new CTietKHTourDAO();
        lsCTietKHTours = new ArrayList<>();
    }

    public ArrayList<CTietKHTourDTO> getAllCTietKHTours(){
        lsCTietKHTours = cTietKHTourDAO.getAllCTietKHTours();
        return lsCTietKHTours;
    }
    public boolean addCTietKHTour(CTietKHTourDTO t){
        if(t == null) return false;

        boolean success = cTietKHTourDAO.addCTietKHTour(t);
        if(success) lsCTietKHTours.add(t);

        return success;
    }

    public boolean editCTietKHTour(CTietKHTourDTO t){
        return cTietKHTourDAO.editCTietKHTour(t);
    }

    public boolean removeCTietKHTour(String maCTietKHTour){
        return cTietKHTourDAO.removeCTietKHTour(maCTietKHTour);
    }

    public CTietKHTourDTO getCTietKHTourById(String maCTietKHTour){
        CTietKHTourDTO result = null;
        for (CTietKHTourDTO ct : lsCTietKHTours){
            if(ct.getMaKHTour().trim().equalsIgnoreCase(maCTietKHTour)) {
                result = ct;
                break;
            }
        }
        return result;
    }

    // get CTietKeHTour equal with maKHTour
    public ArrayList<CTietKHTourDTO> getLsCTietKHToursById(String maKHTour){
        ArrayList<CTietKHTourDTO> result = new ArrayList<>(); // result CTietKHTours
        ArrayList<CTietKHTourDTO> list = getAllCTietKHTours(); // list CTietKHTours

        for (CTietKHTourDTO ct : list){
            if(ct.getMaKHTour().trim().equalsIgnoreCase(maKHTour)) {
                result.add(ct);
            }
        }
        return result;
    }

    public boolean existedCTietKHTourWithID(String maCTKHTour){
        ArrayList<CTietKHTourDTO> list = getAllCTietKHTours(); // list CTietKHTours

        for (CTietKHTourDTO ct : list){
            if(ct.getMaKHTour().trim().equalsIgnoreCase(maCTKHTour))
                return true;
        }
        return false;
    }
}
