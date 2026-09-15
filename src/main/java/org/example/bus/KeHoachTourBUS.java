package org.example.bus;

import org.example.dao.KeHoachTourDAO;
import org.example.dto.KeHoachTourDTO;

import javax.swing.*;
import java.util.ArrayList;

import static javax.swing.JOptionPane.showMessageDialog;

public class KeHoachTourBUS {
    private ArrayList<KeHoachTourDTO> lsKeHoachTour;
    KeHoachTourDAO keHoachTourDAO;

    public KeHoachTourBUS(){
        lsKeHoachTour = new ArrayList<>();
        keHoachTourDAO = new KeHoachTourDAO();
    }

    public ArrayList<KeHoachTourDTO> getAllKeHoachTours(){
        lsKeHoachTour = keHoachTourDAO.getAllKeHoachTours();
        return lsKeHoachTour;
    }

    public ArrayList<KeHoachTourDTO> getAllKeHoachToursByID(String maTour){
        lsKeHoachTour = keHoachTourDAO.getAllKeHoachTours();
        ArrayList<KeHoachTourDTO> lsKeHoachToursID = new ArrayList<>();

        for(KeHoachTourDTO kt : lsKeHoachTour){
            if(kt.getMaTour().trim().equalsIgnoreCase(maTour)){
                lsKeHoachToursID.add(kt);
            }
        }
        return lsKeHoachToursID;
    }

    public boolean addKeHoachTour(KeHoachTourDTO t){
        if(t == null) return false;

        // use "Guard clause" technique to aggregate all error conditions to handle -> code will more compact
        boolean isNullDay = t.getNgayKhoiHanh() == null || t.getNgayKetThuc() == null;
        boolean isWrongDay = !isNullDay && t.getNgayKetThuc().isBefore(t.getNgayKhoiHanh());
        boolean isWrongTicket = t.getTongSoVe() <= 0;


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

        boolean success = keHoachTourDAO.addKeHoachTour(t);

        if(success)
            lsKeHoachTour.add(t);
        return success;
    }

    public String validateKeHoachTour(KeHoachTourDTO t){
        if(t == null) return "Dữ liệu không hợp lệ";

        if(t.getNgayKhoiHanh() == null || t.getNgayKetThuc() == null)
            return "Ngày không được để trống";

        if(t.getNgayKetThuc().isBefore(t.getNgayKhoiHanh()))
            return "Ngày kết thúc phải sau ngày khởi hành";

        if(t.getTongChiDuKien() < 0)
            return "Tổng chi không hợp lệ";

        if(t.getTongThuDuKien() < 0)
            return "Tổng thu không hợp lệ";

        if(t.getTongSoVe() < 0)
            return "Tổng số vé không hợp lệ";

        return null;
    }

    public boolean editKeHoachTour(KeHoachTourDTO t){
        return keHoachTourDAO.editKeHoachTour(t);
    }

    public boolean removeKeHoachTour(String matour){
        return keHoachTourDAO.removeKeHoachTour(matour);
    }

    public KeHoachTourDTO getById(String maKHTour){
        KeHoachTourDTO result = new KeHoachTourDTO();
        for (KeHoachTourDTO kt : lsKeHoachTour){
            if(kt.getMaKHTour().trim().equalsIgnoreCase(maKHTour)) {
                result = kt;
                break;
            }
        }
        return result;
    }

    public boolean existedKeHoachTourWithID(String maKHTour){
        for (KeHoachTourDTO kt : lsKeHoachTour){
            if(kt.getMaTour().trim().equalsIgnoreCase(maKHTour))
                return true;
        }
        return false;
    }
}
