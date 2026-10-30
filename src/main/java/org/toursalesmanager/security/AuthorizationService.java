package org.toursalesmanager.security;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.toursalesmanager.dao.AccountPermissionDAO;
import org.toursalesmanager.dto.AccountDTO;
import org.toursalesmanager.enums.Permission;
import org.toursalesmanager.enums.Role;

import java.util.EnumMap;
import java.util.EnumSet;
import java.util.Map;
import java.util.Set;

@NoArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class AuthorizationService {
    static final Map<Role, Set<Permission>>
            ROLE_PERMISSION = new EnumMap<>(Role.class);

    static {
        configureOperatorPermissions();
        configureCustomerPermissions();
        configureAccountantPermissions();
        configureAdminPermissions();
    }

    private static void configureAdminPermissions(){
        ROLE_PERMISSION.put(Role.ADMIN, EnumSet.allOf(Permission.class));
    }

    private static void configureAccountantPermissions(){
        ROLE_PERMISSION.put(Role.ACCOUNTANT,
                EnumSet.of(
                        Permission.VIEW_TOUR,
                        Permission.VIEW_TOUR_PLAN,

                        Permission.VIEW_CUSTOMER,
                        Permission.MANAGE_CUSTOMER,

                        Permission.VIEW_LOCATION,

                        Permission.VIEW_BOOKING,

                        Permission.VIEW_PAYMENT,
                        Permission.MANAGE_PAYMENT,

                        Permission.VIEW_REVENUE_REPORT,

                        Permission.VIEW_INVOICE,
                        Permission.MANAGE_INVOICE,

                        Permission.VIEW_PROMOTION,
                        Permission.VIEW_STATISTICS
                )
        );
    }

    private static void configureCustomerPermissions(){
        ROLE_PERMISSION.put(
                Role.CUSTOMER,
                EnumSet.of(
                        Permission.VIEW_TOUR,
                        Permission.VIEW_TOUR_TYPE,
                        Permission.VIEW_TOUR_PLAN,

                        Permission.VIEW_LOCATION,

                        Permission.VIEW_BOOKING,
                        Permission.CREATE_BOOKING,
                        Permission.CANCEL_BOOKING,

                        Permission.VIEW_PAYMENT,

                        Permission.VIEW_PROMOTION
                )
        );
    }


    private static void configureOperatorPermissions(){
        ROLE_PERMISSION.put(
                Role.TOUR_OPERATE,
                EnumSet.of(
                        Permission.VIEW_TOUR,
                        Permission.MANAGE_TOUR,

                        Permission.VIEW_TOUR_TYPE,
                        Permission.MANAGE_TOUR_TYPE,

                        Permission.VIEW_TOUR_PLAN,
                        Permission.MANAGE_TOUR_PLAN,

                        Permission.VIEW_CUSTOMER,

                        Permission.VIEW_BOOKING,
                        Permission.CREATE_BOOKING,
                        Permission.UPDATE_BOOKING,
                        Permission.CANCEL_BOOKING,

                        Permission.VIEW_LOCATION,
                        Permission.MANAGE_LOCATION,

                        Permission.VIEW_PROMOTION,
                        Permission.MANAGE_PROMOTION,

                        Permission.VIEW_STATISTICS,

                        Permission.VIEW_PAYMENT
                )
        );
    }

    public static boolean hasPermission(Role role, Permission permission){
        if (role == null || permission == null) {
            return false;
        }

        Set<Permission> permissions = ROLE_PERMISSION.get(role);

        return permissions != null && permissions.contains(permission);
    }

    /**
     * Quyền thực tế của một tài khoản:
     * ADMIN luôn có toàn quyền; tài khoản đã được phân quyền riêng dùng danh sách riêng;
     * còn lại dùng quyền mặc định theo vai trò.
     */
    public static Set<Permission> resolvePermissions(AccountDTO account){
        if (account == null || account.getRole() == null) {
            return EnumSet.noneOf(Permission.class);
        }

        if (account.getRole() == Role.ADMIN) {
            return getPermissions(Role.ADMIN);
        }

        try {
            Set<Permission> customPermissions =
                    new AccountPermissionDAO().getPermissions(account.getIdAccount());
            if (customPermissions != null) {
                return customPermissions;
            }
        } catch (RuntimeException e) {
            System.err.println("Không tải được quyền riêng của tài khoản: " + e.getMessage());
        }

        return getPermissions(account.getRole());
    }

    public static Set<Permission> getPermissions(Role role){
        Set<Permission> permissions =
                ROLE_PERMISSION.get(role);

        if (permissions == null) {
            return EnumSet.noneOf(Permission.class);
        }

        // Trả về bản sao, tránh code bên ngoài sửa dữ liệu
        return EnumSet.copyOf(permissions);
    }
}
