package org.toursalesmanager.bus;

import org.toursalesmanager.dao.AccountDAO;
import org.toursalesmanager.dto.AccountDTO;
import org.toursalesmanager.enums.Role;
import org.toursalesmanager.gui.helper.PasswordUtil;

import java.nio.charset.StandardCharsets;
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

        AccountDTO account = accountDAO.loginAccount(username.trim());
        if (account == null) {
            return null;
        }

        String storedPassword = account.getPassword();
        if (PasswordUtil.matches(password, storedPassword)) {
            return account;
        }

        if (!PasswordUtil.isBCryptHash(storedPassword) && password.equals(storedPassword)) {
            String hashedPassword = PasswordUtil.hash(password);
            if (accountDAO.updatePassword(account.getUsername(), hashedPassword)) {
                account.setPassword(hashedPassword);
            }
            return account;
        }
        return null;
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

        String normalizedUsername = username == null ? null : username.trim();
        String normalizedFirstName = firstName == null ? null : firstName.trim();
        String normalizedLastName = lastName == null ? null : lastName.trim();
        String normalizedAddress = address == null ? null : address.trim();
        String normalizedPhone = phoneNumber == null ? null : phoneNumber.trim();

        validateCustomerInfo(normalizedUsername, password, normalizedFirstName, normalizedLastName,
                normalizedAddress, normalizedPhone, dob, confirmPassword);

        Role role = Role.CUSTOMER;

        return accountDAO.registerCustomer(
                normalizedUsername,
                password,
                normalizedFirstName,
                normalizedLastName,
                normalizedAddress,
                normalizedPhone,
                dob,
                role
        );
    }

    private void validateCustomerInfo(String username, String password, String firstName, String lastName, String address, String phoneNumber, LocalDate dob, String confirmPassword) {
        if (username == null || username.isBlank()) {
            throw new IllegalArgumentException("Tên tài khoản không được để trống");
        }
        if (username.length() > 30) {
            throw new IllegalArgumentException("Tên tài khoản không được dài quá 30 ký tự");
        }

        if (accountDAO.existsByUsername(username)) {
            throw new IllegalArgumentException(
                    "Tên tài khoản đã tồn tại"
            );
        }

        if (password == null || password.isBlank()) {
            throw new IllegalArgumentException("Mật khẩu không được để trống");
        }
        if (confirmPassword == null || !password.equals(confirmPassword)) {
            throw new IllegalArgumentException(
                    "Mật khẩu xác nhận không khớp"
            );
        }
        if (password.length() < 6) {
            throw new IllegalArgumentException(
                    "Mật khẩu phải có ít nhất 6 ký tự"
            );
        }
        if (password.getBytes(StandardCharsets.UTF_8).length > 72) {
            throw new IllegalArgumentException("Mật khẩu không được vượt quá 72 byte UTF-8");
        }

        if (firstName == null || firstName.isBlank()) {
            throw new IllegalArgumentException("Tên không được để trống");
        }
        if (firstName.length() > 45) {
            throw new IllegalArgumentException("Tên không được dài quá 45 ký tự");
        }

        if (lastName == null || lastName.isBlank()) {
            throw new IllegalArgumentException("Họ không được để trống");
        }
        if (lastName.length() > 45) {
            throw new IllegalArgumentException("Họ không được dài quá 45 ký tự");
        }

        if (address == null || address.isBlank()) {
            throw new IllegalArgumentException("Địa chỉ không được để trống");
        }
        if (address.length() > 255) {
            throw new IllegalArgumentException("Địa chỉ không được dài quá 255 ký tự");
        }

        if (phoneNumber == null || !phoneNumber.matches("^0\\d{9}$")) {
            throw new IllegalArgumentException("Số điện thoại phải gồm 10 chữ số và bắt đầu bằng số 0");
        }
        if (isPhoneExisted(phoneNumber)) {
            throw new IllegalArgumentException("Số điện thoại này đã được sử dụng");
        }

        if (dob == null) {
            throw new IllegalArgumentException("Ngày sinh không được để trống");
        }
        if (dob.isAfter(LocalDate.now())) {
            throw new IllegalArgumentException("Ngày sinh không được sau ngày hiện tại");
        }

    }

    private boolean isPhoneExisted(String phoneNumber) {
        return accountDAO.isPhoneExisted(phoneNumber);
    }

}