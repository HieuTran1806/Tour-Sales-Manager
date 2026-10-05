package org.example.bus;
import org.example.dao.TourBookingDAO;
import org.example.dto.AccountDTO;
import org.example.dto.TourBookingDTO;
import org.example.enums.Permission;
import org.example.enums.Role;
import org.example.login.SessionManager;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class TourBookingBUS {
    ArrayList<TourBookingDTO> dsKHKHTour;
    TourBookingDAO tourBookingDao;

    public TourBookingBUS() {
        tourBookingDao = new TourBookingDAO();
        dsKHKHTour = tourBookingDao.getAllTourBooking();
    }

    public ArrayList<TourBookingDTO> getAllTourBooking(){
        return tourBookingDao.getAllTourBooking();
    }

    public void addTourBooking(TourBookingDTO dto) {
        try{
            if (dsKHKHTour == null) {
                dsKHKHTour = new ArrayList<TourBookingDTO>();
            }
            if (dto == null) {
                return;
            }
            if (dto.getIdTourPlan() == null || dto.getIdTourPlan().isEmpty()) {
                return;
            }
            for (TourBookingDTO existingKHT : dsKHKHTour) {
                if (existingKHT.getIdTourPlan().equals(dto.getIdTourPlan())) {
                    return;
                }
            }
            tourBookingDao.addTourBooking(dto);
            if (dsKHKHTour != null) {
                dsKHKHTour.add(dto);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void deleteTourBooking(String maKHTour, String maKHang) {
        try {
            if (dsKHKHTour == null) {
                return;
            }
            tourBookingDao.deleteTourBooking(maKHTour, maKHang);
            dsKHKHTour.removeIf(kht -> kht.getIdTourPlan().equals(maKHTour) && kht.getIdCustomer().equals(maKHang));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    // have to update again
    public boolean editTourBooking(TourBookingDTO kht) {
        try {
            if (dsKHKHTour == null || kht == null || kht.getIdTourPlan() == null || kht.getIdTourPlan().isEmpty()) {
                return false;
            }
            for (int i = 0; i < dsKHKHTour.size(); i++) {
                TourBookingDTO existingKHT = dsKHKHTour.get(i);
                if (existingKHT.getIdTourPlan().equals(kht.getIdTourPlan()) && existingKHT.getIdCustomer().equals(kht.getIdCustomer())) {
                    tourBookingDao.updateTourBooking(kht);
                    dsKHKHTour.set(i, kht);
                    return true;
                }
            }
            return false;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    public TourBookingDTO findTourBookingByIdTourPlan(String maKHTour) {
        try {
            if (dsKHKHTour == null || maKHTour == null) {
                System.out.println("abcd");
                return null;
            }
            for (TourBookingDTO kht : dsKHKHTour) {
                if (kht.getIdTourPlan().equals(maKHTour)) {
                    System.out.println("abc");
                    return kht;
                }
            }
            return null;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    public List<TourBookingDTO> timKHang_KHToursTheoHo(String ho) {
        try {
            if (dsKHKHTour == null || ho == null) {
                return new ArrayList<>();
            }
            List<TourBookingDTO> result = new ArrayList<>();
            for (TourBookingDTO kht : dsKHKHTour) {
                String maKHang = kht.getIdCustomer();
                if (maKHang != null && maKHang.startsWith(ho)) {
                    result.add(kht);
                }
            }
            return result;
        } catch (Exception e) {
            e.printStackTrace();
            return new ArrayList<>();
        }
    }

    public TourBookingDTO timKHang_KHTourTheoTen(String ten) {
        try {
            if (dsKHKHTour == null || ten == null) {
                return null;
            }
            for (TourBookingDTO kht : dsKHKHTour) {
                String maKHang = kht.getIdCustomer();
                if (maKHang != null && maKHang.equalsIgnoreCase(ten)) {
                    return kht;
                }
            }
            return null;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    public BigDecimal getPriceByIdTourPlan(
            String idTourPlan
    ) {
        return tourBookingDao.getPriceByIdTourPlan(
                idTourPlan
        );
    }

    public List<TourBookingDTO> getVisibleBookings(){
        AccountDTO accountDTO = SessionManager.getCurrentAccount();

        if(accountDTO.getRole() == Role.CUSTOMER){
            return tourBookingDao.findByCustomer(accountDTO.getIdCustomer());
        }

        if(SessionManager.hasPermission(Permission.VIEW_BOOKING)){
            return tourBookingDao.getAllTourBooking();
        }
        throw new SecurityException(
                "Bạn không có quyền xem phiếu đặt tour"
        );
    }
}
