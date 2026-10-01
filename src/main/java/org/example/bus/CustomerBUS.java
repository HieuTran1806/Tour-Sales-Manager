package org.example.bus;

import org.example.dao.CustomerDAO;
import org.example.dto.CustomerDTO;

import java.util.ArrayList;
import java.util.List;

public class CustomerBUS {
    public ArrayList<CustomerDTO> dsKH;
    public CustomerDAO dao = new CustomerDAO();

    public CustomerBUS() {
        dsKH = new ArrayList<>();
    }
    public ArrayList<CustomerDTO> getAllCustomers() {
        return dao.getAllCustomers();
    }
    public void them(CustomerDTO khang) {
        try{
            if (dsKH == null) {
                dsKH = new ArrayList<CustomerDTO>();
            }
            if (khang == null) {
                return;
            }
            if (khang.getIdCustomer() == null || khang.getIdCustomer().isEmpty()) {
                return;
            }
            for (CustomerDTO existingKH : dsKH) {
                if (existingKH.getIdCustomer().equals(khang.getIdCustomer())) {
                    return;
                }
            }
            dao.addCustomer(khang);
            if (dsKH != null) {
                dsKH.add(khang);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    public void deleteCustomer(String maKH) {
        try {
            if (dsKH == null) {
                return;
            }
            dao.deleteCustomer(maKH);
            dsKH.removeIf(kh -> kh.getIdCustomer().equals(maKH));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    public CustomerDTO timKiemKH(String maKH){
        for (CustomerDTO kh : dsKH) {
            if (kh.getIdCustomer().equals(maKH)) {
                return dao.findCustomerWithId(maKH);
            }
        }
        return null;
    }

    public List<CustomerDTO> findCustomer(String column, String keyword) {
        return dao.findCustomer(column, keyword);
    }

    public boolean editCustomer(CustomerDTO khang) {
        try {
            if (dsKH == null) {
                return false;
            }

            boolean success = dao.editCustomer(khang);
            if (success) {
                for (int i = 0; i < dsKH.size(); i++) {
                    if (dsKH.get(i).getIdCustomer().equals(khang.getIdCustomer())) {
                        dsKH.set(i, khang);
                        break;
                    }
                }
            }
            return success;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }
}