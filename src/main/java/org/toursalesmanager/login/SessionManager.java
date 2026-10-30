package org.toursalesmanager.login;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.toursalesmanager.dto.AccountDTO;
import org.toursalesmanager.enums.Permission;
import org.toursalesmanager.enums.Role;
import org.toursalesmanager.security.AuthorizationService;

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

    // Khách chưa đăng nhập vẫn thấy nút đặt tour, khi bấm sẽ được yêu cầu đăng nhập
    public static boolean canRequestBooking() {
        return !isLoggedIn() || hasPermission(Permission.CREATE_BOOKING);
    }

    public static boolean hasPermission(
            Permission permission
    ) {
        if (permission == null) {
            return false;
        }

        if (!isLoggedIn()) {
            return permission == Permission.VIEW_TOUR
                    || permission == Permission.VIEW_TOUR_TYPE
                    || permission == Permission.VIEW_TOUR_PLAN
                    || permission == Permission.VIEW_LOCATION
                    || permission == Permission.VIEW_PROMOTION;
        }

        return AuthorizationService.hasPermission(
                currentAccount.getRole(),
                permission
        );
    }
}