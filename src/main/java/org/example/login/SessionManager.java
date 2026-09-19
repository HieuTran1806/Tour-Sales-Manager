package org.example.login;

import lombok.*;
import lombok.experimental.FieldDefaults;
import org.example.dto.TaiKhoanDTO;

public class SessionManager {
    private static TaiKhoanDTO currentTaiKhoan;

    public static void loginAccount(TaiKhoanDTO account) {
        currentTaiKhoan = account;
    }
    
    public static void logoutAccount() {
        currentTaiKhoan = null;
    }

    public static TaiKhoanDTO getCurrentAccount() {
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