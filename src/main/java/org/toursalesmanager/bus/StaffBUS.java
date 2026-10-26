package org.toursalesmanager.bus;
import java.sql.SQLException;
import java.time.LocalDate;
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
        this(true);
    }

    public StaffBUS(boolean preloadStaff) {
        if (dsNV == null) {
            dsNV = preloadStaff ? dao.getAllStaffs() : null;
        }
    }

    public ArrayList<StaffDTO> getAllStaffs(){
        return dao.getAllStaffs();
    }

    public boolean addStaff(StaffDTO nv, Role role) throws SQLException {
        if(!requirePermission()){
            throw new SecurityException(
                    "Bạn không có quyền thêm nhân viên"
            );
        }

        if (nv == null) {
            throw new IllegalArgumentException("Thông tin nhân viên không được để trống");
        }
        validateRole(role);
        validateStaffData(
                nv.getIdStaff(), nv.getFirstName(), nv.getLastName(),
                nv.getDob(), nv.getPhoneNumber(), nv.getAddress(), true
        );

        try{
            if (dsNV == null) {
                dsNV = new ArrayList<>();
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
        validateStaffData(idStaff, firstName, lastName, dob, phoneNumber, address, true);
    }

    private void validateStaffData(
            String idStaff,
            String firstName,
            String lastName,
            LocalDate dob,
            String phoneNumber,
            String address,
            boolean isNewStaff
    ) throws SQLException {
        if (idStaff == null || idStaff.isBlank()) {
            throw new IllegalArgumentException("Mã nhân viên không được để trống");
        }

        String normalizedId = idStaff.trim();
        if (isNewStaff && !normalizedId.matches("^NV\\d{3}$")) {
            throw new IllegalArgumentException("Mã nhân viên phải có dạng NVxxx");
        }
        if (isNewStaff && dao.recordExists(normalizedId)) {
            throw new IllegalArgumentException("Mã nhân viên đã tồn tại");
        }

        if (firstName == null || firstName.isBlank()) {
            throw new IllegalArgumentException("Tên nhân viên không được để trống");
        }
        if (firstName.trim().length() > 45) {
            throw new IllegalArgumentException("Tên nhân viên không được dài quá 45 ký tự");
        }

        if (lastName == null || lastName.isBlank()) {
            throw new IllegalArgumentException("Họ nhân viên không được để trống");
        }
        if (lastName.trim().length() > 45) {
            throw new IllegalArgumentException("Họ nhân viên không được dài quá 45 ký tự");
        }

        if (dob == null) {
            throw new IllegalArgumentException("Ngày sinh không được để trống");
        }
        if (dob.isAfter(LocalDate.now())) {
            throw new IllegalArgumentException("Ngày sinh không được sau ngày hiện tại");
        }

        if (phoneNumber == null || phoneNumber.isBlank()) {
            throw new IllegalArgumentException("Số điện thoại không được để trống");
        }
        if (!phoneNumber.trim().matches("^0\\d{9}$")) {
            throw new IllegalArgumentException("Số điện thoại phải có 10 chữ số và bắt đầu bằng 0");
        }

        if (address == null || address.isBlank()) {
            throw new IllegalArgumentException("Địa chỉ không được để trống");
        }
        if (address.trim().length() > 255) {
            throw new IllegalArgumentException("Địa chỉ không được dài quá 255 ký tự");
        }
    }

    public StaffDTO findStaffById(String maNV) {
        StaffDTO staff = dao.findStaffById(maNV);
        if (staff != null && dsNV != null) {
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

    public boolean updateStaff(StaffDTO nv, Role role) throws SQLException {
        if(!requirePermission()){
            throw new SecurityException(
                    "Bạn không có quyền cập nhật nhân viên"
            );
        }

        if (nv == null) {
            throw new IllegalArgumentException("Thông tin nhân viên không được để trống");
        }
        validateRole(role);
        validateStaffData(
                nv.getIdStaff(), nv.getFirstName(), nv.getLastName(),
                nv.getDob(), nv.getPhoneNumber(), nv.getAddress(), false
        );

        try {
            boolean success = dao.updateStaff(nv, role);
            if (success && dsNV != null) {
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

    private void validateRole(Role role) {
        if (role == null || role == Role.CUSTOMER) {
            throw new IllegalArgumentException("Vai trò nhân viên không hợp lệ");
        }
    }

    private boolean requirePermission(){
        return SessionManager.hasPermission(
                Permission.MANAGE_STAFF
        );
    }
}