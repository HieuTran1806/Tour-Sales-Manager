package org.example.bus;

import lombok.AccessLevel;
import lombok.experimental.FieldDefaults;
import org.example.dao.TourPlanDAO;
import org.example.dto.TourPlanDTO;

import java.math.BigDecimal;
import java.util.ArrayList;

import static javax.swing.JOptionPane.showMessageDialog;

@FieldDefaults(level = AccessLevel.PRIVATE)
public class TourPlanBUS {
    ArrayList<TourPlanDTO> lsKeHoachTour;
    TourPlanDAO tourPlanDAO;

    public TourPlanBUS(){
        lsKeHoachTour = new ArrayList<>();
        tourPlanDAO = new TourPlanDAO();
    }

    public ArrayList<TourPlanDTO> getAllKeHoachTours(){
        lsKeHoachTour = tourPlanDAO.getAllKeHoachTours();
        return lsKeHoachTour;
    }

    public ArrayList<TourPlanDTO> getAllKeHoachToursByID(String maTour){
        lsKeHoachTour = tourPlanDAO.getAllKeHoachTours();
        ArrayList<TourPlanDTO> lsKeHoachToursID = new ArrayList<>();

        for(TourPlanDTO kt : lsKeHoachTour){
            if(kt.getIdTour().trim().equalsIgnoreCase(maTour)){
                lsKeHoachToursID.add(kt);
            }
        }
        return lsKeHoachToursID;
    }

    public boolean addKeHoachTour(TourPlanDTO t){
        if(t == null) return false;

        // use "Guard clause" technique to aggregate all error conditions to handle -> code will more compact
        boolean isNullDay = t.getDepartureDate() == null || t.getEndDate() == null;
        boolean isWrongDay = !isNullDay && t.getEndDate().isBefore(t.getDepartureDate());
        boolean isWrongTicket = t.getTickets() <= 0;


        if(isNullDay){
            showMessageDialog(null, "Ngày không được để trống");
            return false;
        }

        if(isWrongDay){
            showMessageDialog(null, "Ngày kết thúc phải sau ngày khởi hành");
            return false;
        }

        if(isWrongTicket){
            showMessageDialog(null, "Số vé đặt tối thiểu phải là 1");
            return false;
        }

        boolean success = tourPlanDAO.addKeHoachTour(t);

        if(success)
            lsKeHoachTour.add(t);
        return success;
    }

    public String validateKeHoachTour(TourPlanDTO t){
        if(t == null) return "Dữ liệu không hợp lệ";

        if(t.getDepartureDate() == null || t.getEndDate() == null)
            return "Ngày không được để trống";

        if(t.getEndDate().isBefore(t.getDepartureDate()))
            return "Ngày kết thúc phải sau ngày khởi hành";

        if(t.getEstimatedCost().compareTo(BigDecimal.ZERO) < 0)
            return "Tổng chi không hợp lệ";

        if(t.getTickets() < 0)
            return "Tổng số vé không hợp lệ";

        if(t.getRemainingTickets() < 0)
            return "Số vé còn lại không hợp lệ";
        return null;
    }

    public boolean editKeHoachTour(TourPlanDTO t){
        return tourPlanDAO.editKeHoachTour(t);
    }

    public boolean removeKeHoachTour(String matour){
        return tourPlanDAO.removeKeHoachTour(matour);
    }

    public TourPlanDTO getById(String maKHTour){
        TourPlanDTO result = new TourPlanDTO();
        for (TourPlanDTO kt : lsKeHoachTour){
            if(kt.getIdTourPlan().trim().equalsIgnoreCase(maKHTour)) {
                result = kt;
                break;
            }
        }
        return result;
    }

    public boolean existedKeHoachTourWithID(String maKHTour){
        for (TourPlanDTO kt : lsKeHoachTour){
            if(kt.getIdTour().trim().equalsIgnoreCase(maKHTour))
                return true;
        }
        return false;
    }
}
