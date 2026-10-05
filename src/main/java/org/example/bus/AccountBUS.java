package org.example.bus;

import org.example.dao.AccountDAO;
import org.example.dto.AccountDTO;
import org.example.enums.Role;

public class AccountBUS {

    private final AccountDAO accountDAO;

    public AccountBUS() {
        this.accountDAO = new AccountDAO();
    }

    // Dùng khi muốn truyền DAO từ bên ngoài, thuận tiện cho kiểm thử
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


}