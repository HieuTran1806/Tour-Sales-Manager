package org.toursalesmanager.bus;

import org.toursalesmanager.dao.CustomerDAO;
import org.toursalesmanager.dto.CustomerDTO;
import org.toursalesmanager.enums.Permission;
import org.toursalesmanager.login.SessionManager;

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
    public String getNextCustomerId() {
        requireManagePermission();
        return dao.getNextCustomerId();
    }

    public void addCustomer(CustomerDTO khang) {
        requireManagePermission();

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
        requireManagePermission();

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
    public CustomerDTO findCustomerById(String maKH){
        return dao.findCustomerWithId(maKH);
    }

    public List<CustomerDTO> findCustomer(String column, String keyword) {
        return dao.findCustomer(column, keyword);
    }

    public boolean editCustomer(CustomerDTO khang) {
        requireManagePermission();

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

    private void requireManagePermission() {
        if (!SessionManager.hasPermission(Permission.MANAGE_CUSTOMER)) {
            throw new SecurityException(
                    "Bạn không có quyền quản lý khách hàng."
            );
        }
    }
}