package org.example.login;

import lombok.*;
import lombok.experimental.FieldDefaults;
import org.example.dto.TaiKhoanDTO;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@FieldDefaults(level = AccessLevel.PRIVATE)
public class SessionManager {
    // Shouldn't use static variable for account of staff, if not -> data leak
    TaiKhoanDTO currentTaiKhoan;

    public void loginAccount(TaiKhoanDTO account) {
        currentTaiKhoan = account;
    }
    
    public void logout() {
        currentTaiKhoan = null;
    }

    public boolean isAdmin() {
        if (currentTaiKhoan == null || currentTaiKhoan.getPosition() == null) {
            return false;
        }
        String chucVu = currentTaiKhoan.getPosition().trim().toLowerCase();
        return chucVu.equals("quản lí") || chucVu.equals("quan li") || chucVu.equals("quản lý") || chucVu.equals("quan ly");
    }
}