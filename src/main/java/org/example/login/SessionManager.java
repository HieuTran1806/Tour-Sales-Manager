package org.example.login;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.example.dto.AccountDTO;
import org.example.enums.Permission;
import org.example.enums.Role;
import org.example.security.AuthorizationService;

@FieldDefaults(level = AccessLevel.PRIVATE)
@NoArgsConstructor
public class SessionManager {
    static AccountDTO currentAccount;

    public static void loginAccount(AccountDTO account) {
        if (account == null) {
            throw new IllegalArgumentException(
                    "Tài khoản không được null"
            );
        }

        currentAccount = account;
    }
    
    public static void logoutAccount() {
        currentAccount = null;
    }

    public static boolean isLoggedIn() {
        return currentAccount != null;
    }

    public static AccountDTO getCurrentAccount() {
        if (currentAccount == null) {
            throw new IllegalStateException(
                    "Chưa có tài khoản đăng nhập"
            );
        }
        return currentAccount;
    }

    public static Role getCurrentRole(){
        return getCurrentAccount().getRole();
    }

    public static boolean hasRole(Role role){
        return isLoggedIn() && role != null && currentAccount.getRole() == role;
    }

    public static boolean isAdmin(){
        return hasRole(Role.ADMIN);
    }

    public static boolean hasPermission(
            Permission permission
    ) {
        return isLoggedIn()
                && AuthorizationService.hasPermission(
                currentAccount.getRole(),
                permission
        );
    }
}