package org.toursalesmanager.bus;

import org.toursalesmanager.dao.AccountDAO;
import org.toursalesmanager.dto.AccountDTO;
import org.toursalesmanager.enums.Role;

import java.time.LocalDate;

public class AccountBUS {

    private final AccountDAO accountDAO;

    public AccountBUS() {
        this.accountDAO = new AccountDAO();
    }

    public AccountBUS(AccountDAO accountDAO) {
        this.accountDAO = accountDAO;
    }

    public AccountDTO login(
            String username,
            String password
    ) {
        if (username == null || username.isBlank()) {
            throw new IllegalArgumentException(
                    "Tên đăng nhập không được để trống"
            );
        }

        if (password == null || password.isBlank()) {
            throw new IllegalArgumentException(
                    "Mật khẩu không được để trống"
            );
        }

        return accountDAO.loginAccount(
                username.trim(),
                password
        );
    }

    public boolean createStaffAccount(
            String idStaff,
            Role role
    ) {
        if (idStaff == null || idStaff.isBlank()) {
            throw new IllegalArgumentException(
                    "Mã nhân viên không hợp lệ"
            );
        }

        validateStaffRole(role);

        if (accountDAO.existsByStaffId(idStaff.trim())) {
            throw new IllegalArgumentException(
                    "Nhân viên này đã có tài khoản"
            );
        }

        return accountDAO.createStaffAccount(
                idStaff.trim(),
                role
        );
    }

    public boolean updateStaffRole(
            String idStaff,
            Role role
    ) {
        if (idStaff == null || idStaff.isBlank()) {
            throw new IllegalArgumentException(
                    "Mã nhân viên không hợp lệ"
            );
        }

        validateStaffRole(role);

        return accountDAO.updateStaffRole(
                idStaff.trim(),
                role
        );
    }

    // public boolean lockAccount(int idAccount) {
    //     if (idAccount <= 0) {
    //         throw new IllegalArgumentException(
    //                 "Mã tài khoản không hợp lệ"
    //         );
    //     }
    //
    //     return accountDAO.updateStatus(
    //             idAccount,
    //             "LOCKED"
    //     );
    // }
    //
    // public boolean unlockAccount(int idAccount) {
    //     if (idAccount <= 0) {
    //         throw new IllegalArgumentException(
    //                 "Mã tài khoản không hợp lệ"
    //         );
    //     }
    //
    //     return accountDAO.updateStatus(
    //             idAccount,
    //             "ACTIVE"
    //     );
    // }

    private void validateStaffRole(Role role) {
        if (role == null) {
            throw new IllegalArgumentException(
                    "Vai trò không được để trống"
            );
        }

        if (role == Role.CUSTOMER) {
            throw new IllegalArgumentException(
                    "Không thể gán vai trò CUSTOMER cho nhân viên"
            );
        }
    }

    public boolean registerCustomer(String username, String password, String firstName,
            String lastName, String address, String phoneNumber,
            LocalDate dob, String confirmPassword) {

        validateCustomerInfo(username, password, firstName, lastName, address, phoneNumber, dob, confirmPassword);

        Role role = Role.CUSTOMER;

        return accountDAO.registerCustomer(
                username,
                password,
                firstName,
                lastName,
                address,
                phoneNumber,
                dob,
                role
        );
    }

    private void validateCustomerInfo(String username, String password, String firstName, String lastName, String address, String phoneNumber, LocalDate dob, String confirmPassword) {
        if (accountDAO.existsByUsername(username)) {
            throw new IllegalArgumentException(
                    "Tên tài khoản đã tồn tại"
            );
        }
        if (username == null || username.isBlank()) {
            throw new IllegalArgumentException("Tên tài khoản không được để trống");
        }

        if (!password.equals(confirmPassword)) {
            throw new IllegalArgumentException(
                    "Mật khẩu xác nhận không khớp"
            );
        }
        if(password.length() < 6) {
            throw new IllegalArgumentException(
                    "Mật khẩu phải có ít nhất 6 ký tự"
            );
        }
        if (password.isBlank()) {
            throw new IllegalArgumentException("Mật khẩu không được để trống");
        }


        if (firstName == null || firstName.isBlank()) {
            throw new IllegalArgumentException("Tên không được để trống");
        }

        if (lastName == null || lastName.isBlank()) {
            throw new IllegalArgumentException("Họ không được để trống");
        }

        if (address == null || address.isBlank()) {
            throw new IllegalArgumentException("Địa chỉ không được để trống");
        }

        if (phoneNumber == null || phoneNumber.isBlank()) {
            throw new IllegalArgumentException("Số điện thoại không được để trống");
        }
        if (phoneNumber.length() != 10) {
            throw new IllegalArgumentException("Số điện thoại phải có 10 chữ số");
        }
        if (phoneNumber.matches(".*[a-zA-Z].*")) {
            throw new IllegalArgumentException("Số điện thoại chỉ được chứa chữ số");
        }
        if (!phoneNumber.startsWith("0")) {
            throw new IllegalArgumentException("Số điện thoại phải bắt đầu bằng số 0");
        }


        if (dob == null) {
            throw new IllegalArgumentException("Ngày sinh không được để trống");
        }
        if(dob.isAfter(LocalDate.now())) {
            throw new IllegalArgumentException("Ngày sinh không được sau ngày hiện tại");
        }

    }
}