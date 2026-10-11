package org.toursalesmanager.bus;
import java.sql.SQLException;
import java.time.LocalDate;
import java.time.YearMonth;
import java.util.ArrayList;
import java.util.List;

import org.toursalesmanager.dao.StaffDAO;
import org.toursalesmanager.dto.StaffDTO;
import org.toursalesmanager.enums.Permission;
import org.toursalesmanager.enums.Role;
import org.toursalesmanager.login.SessionManager;

import javax.swing.*;

public class StaffBUS {
    public ArrayList<StaffDTO> dsNV;
    public StaffDAO dao = new StaffDAO();

    public StaffBUS() {
        if (dsNV == null) {
            dsNV = dao.getAllStaffs();
        }
    }

    public ArrayList<StaffDTO> getAllStaffs(){
        return dao.getAllStaffs();
    }

    public boolean addStaff(StaffDTO nv, Role role) {
        if(!requirePermission()){
            throw new SecurityException(
                    "Bạn không có quyền thêm nhân viên"
            );
        }

        try{
            if (dsNV == null) {
                dsNV = new ArrayList<>();
            }
            if (nv == null) {
                return false;
            }
            if (nv.getIdStaff() == null || nv.getIdStaff().isEmpty()) {
                return false;
            }
            for (StaffDTO existingNV : dsNV) {
                if (existingNV.getIdStaff().equals(nv.getIdStaff())) {
                    return false;
                }
            }
            boolean success = dao.addStaff(nv, role);
            if (success) {
                nv.setRole(role);
                dsNV.add(nv);
            }
            return success;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    public void deleteStaff(String maNV) {
        if(!requirePermission()){
            throw new SecurityException(
                    "Bạn không có quyền xóa nhân viên"
            );
        }

        try {
            if (dsNV == null) {
                return;
            }
            dao.deleteStaff(maNV);
            dsNV.removeIf(nv -> nv.getIdStaff().equals(maNV));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void validateStaffData(String idStaff, String firstName, String lastName, LocalDate dob, String phoneNumber, String address) throws SQLException {
        // validate idStaff
        if (idStaff == null || idStaff.isEmpty()) {
            throw new IllegalArgumentException("Mã nhân viên không được để trống");
        }
        if (!idStaff.matches("^NV\\d{3}$")) {
            throw new IllegalArgumentException("Mã nhân viên phải có dạng NVxxx!");
        }
        if(dao.recordExists(idStaff)){
            throw new IllegalArgumentException("Mã nhân viên đã tồn tại!");
        }

        if(firstName == null || firstName.isEmpty()) {
            throw new IllegalArgumentException("Tên nhân viên không được để trống");
        }

        //validate lastName
        if(lastName == null || lastName.isEmpty()) {
            throw new IllegalArgumentException("Họ nhân viên không được để trống");
        }

        // validate dob
        if (dob == null) {
            throw new IllegalArgumentException("Ngày sinh không được để trống!");
        }
        if (dob.isAfter(YearMonth.now().atEndOfMonth())) {
            throw new IllegalArgumentException("Ngày sinh không được sau ngày hiện tại!");
        }

        // validate phoneNumber
        if (!phoneNumber.matches("^0\\d{9}$")) {
            throw new IllegalArgumentException("Số điện thoại phải có 10 chữ số và bắt đầu bằng 0!");
        }
        if (phoneNumber.isEmpty()) {
            throw new IllegalArgumentException("Số điện thoại không được để trống!");
        }

        // validate address
        if(address == null || address.isEmpty()) {
            throw new IllegalArgumentException("Địa chỉ không được để trống");
        }

        if (!address.matches("^[\\p{L}0-9\\s,.-]+$")) {
            throw new IllegalArgumentException("Địa chỉ chỉ được chứa chữ cái, số, khoảng trắng và các ký tự ,.-!");
        }
    }

    public StaffDTO findStaffById(String maNV) {
        if (dsNV == null) {
            return null;
        }
        StaffDTO staff = dao.findStaffById(maNV);
        if (staff != null) {
            for (int i = 0; i < dsNV.size(); i++) {
                if (dsNV.get(i).getIdStaff().equals(maNV)) {
                    dsNV.set(i, staff);
                    break;
                }
            }
        }
        return staff;
    }

    public ArrayList<StaffDTO> getAllStaffWithRole() {
        if (!SessionManager.hasPermission(
                Permission.VIEW_STAFF
        )) {
            throw new SecurityException(
                    "Bạn không có quyền xem nhân viên"
            );
        }

        return dao.getAllStaffWithRole();
    }


    public List<StaffDTO> findStaff(String type, String keyword) {
        if (keyword == null || keyword.isBlank()) {
            return getAllStaffWithRole();
        }

        return dao.findStaff(
                type,
                keyword.trim()
        );
    }

    public boolean updateStaff(StaffDTO nv, Role role) {
        if(!requirePermission()){
            throw new SecurityException(
                    "Bạn không có quyền cập nhật nhân viên"
            );
        }

        try {
            if (dsNV == null) {
                return false;
            }
            boolean success = dao.updateStaff(nv, role);
            if (success) {
                nv.setRole(role);
                for (int i = 0; i < dsNV.size(); i++) {
                    if (dsNV.get(i).getIdStaff().equals(nv.getIdStaff())) {
                        dsNV.set(i, nv);
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

    private boolean requirePermission(){
        return SessionManager.hasPermission(
                Permission.MANAGE_STAFF
        );
    }
}