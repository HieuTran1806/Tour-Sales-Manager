package org.example.login;

import org.example.dto.AccountDTO;

public class SessionManager {
    private static AccountDTO currentTaiKhoan;

    public static void loginAccount(AccountDTO account) {
        currentTaiKhoan = account;
    }
    
    public static void logoutAccount() {
        currentTaiKhoan = null;
    }

    public static AccountDTO getCurrentAccount() {
        return currentTaiKhoan;
    }

    public static boolean isAdmin() {
        if (currentTaiKhoan == null || currentTaiKhoan.getPosition() == null) {
            return false;
        }
        String role = currentTaiKhoan.getPosition().trim().toLowerCase();
        return role.equals("quản lí") || role.equals("quan li") || role.equals("quản lý") || role.equals("quan ly");
    }
}