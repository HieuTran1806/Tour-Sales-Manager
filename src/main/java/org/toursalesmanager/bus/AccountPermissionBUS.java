package org.toursalesmanager.bus;

import org.toursalesmanager.dao.AccountDAO;
import org.toursalesmanager.dao.AccountPermissionDAO;
import org.toursalesmanager.dto.AccountDTO;
import org.toursalesmanager.enums.Permission;
import org.toursalesmanager.enums.Role;
import org.toursalesmanager.login.SessionManager;
import org.toursalesmanager.security.AuthorizationService;

import java.util.EnumSet;
import java.util.List;
import java.util.Set;

public class AccountPermissionBUS {

    private final AccountDAO accountDAO;
    private final AccountPermissionDAO accountPermissionDAO;

    public AccountPermissionBUS() {
        this.accountDAO = new AccountDAO();
        this.accountPermissionDAO = new AccountPermissionDAO();
    }

    public List<AccountDTO> getAllAccounts() {
        requireAdmin();
        return accountDAO.getAllAccounts();
    }

    // Quyền đang có hiệu lực: quyền riêng nếu đã phân quyền, ngược lại là quyền mặc định theo vai trò
    public Set<Permission> getEffectivePermissions(AccountDTO account) {
        return AuthorizationService.resolvePermissions(account);
    }

    public boolean hasCustomPermissions(AccountDTO account) {
        if (account == null || account.getRole() == Role.ADMIN) {
            return false;
        }
        return accountPermissionDAO.getPermissions(account.getIdAccount()) != null;
    }

    public boolean savePermissions(AccountDTO account, Set<Permission> permissions) {
        requireAdmin();
        validateEditableAccount(account);

        if (permissions == null || permissions.isEmpty()) {
            throw new IllegalArgumentException(
                    "Tài khoản phải có ít nhất một quyền"
            );
        }

        return accountPermissionDAO.replacePermissions(
                account.getIdAccount(),
                EnumSet.copyOf(permissions)
        );
    }

    public boolean resetToRoleDefault(AccountDTO account) {
        requireAdmin();
        validateEditableAccount(account);
        return accountPermissionDAO.deletePermissions(account.getIdAccount());
    }

    private void validateEditableAccount(AccountDTO account) {
        if (account == null) {
            throw new IllegalArgumentException("Vui lòng chọn tài khoản");
        }
        if (account.getRole() == Role.ADMIN) {
            throw new IllegalArgumentException(
                    "Không thể thay đổi quyền của tài khoản quản trị viên"
            );
        }
    }

    private void requireAdmin() {
        if (!SessionManager.isAdmin()) {
            throw new SecurityException(
                    "Chỉ quản trị viên mới được phân quyền"
            );
        }
    }
}
