package org.example.login;

import org.example.dto.TaiKhoanDTO;

public class PhanQuyen {
    private static TaiKhoanDTO currentTaiKhoan;

    private PhanQuyen() {
    }

    public static void dangNhap(TaiKhoanDTO taiKhoan) {
        currentTaiKhoan = taiKhoan;
    }

    public static void dangXuat() {
        currentTaiKhoan = null;
    }

    public static TaiKhoanDTO getCurrentTaiKhoan() {
        return currentTaiKhoan;
    }

    public static boolean laQuanLy() {
        if (currentTaiKhoan == null || currentTaiKhoan.getPosition() == null) {
            return false;
        }
        String chucVu = currentTaiKhoan.getPosition().trim().toLowerCase();
        return chucVu.equals("quản lí") || chucVu.equals("quan li") || chucVu.equals("quản lý") || chucVu.equals("quan ly");
    }
}