package org.toursalesmanager.gui.panel;

import lombok.AccessLevel;
import lombok.experimental.FieldDefaults;
import org.toursalesmanager.bus.AccountPermissionBUS;
import org.toursalesmanager.dto.AccountDTO;
import org.toursalesmanager.enums.Permission;
import org.toursalesmanager.enums.Role;
import org.toursalesmanager.gui.component.ButtonFactory;
import org.toursalesmanager.gui.helper.UIColors;
import org.toursalesmanager.gui.helper.UIFontHelper;
import org.toursalesmanager.login.SessionManager;

import javax.swing.*;
import javax.swing.border.TitledBorder;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableRowSorter;
import java.awt.*;
import java.util.ArrayList;
import java.util.EnumSet;
import java.util.List;
import java.util.Set;

@FieldDefaults(level = AccessLevel.PRIVATE)
public class PermissionPanel extends JPanel {
    // cột của bảng quyền
    static final int COL_GRANTED = 0;
    static final int COL_GROUP = 1;
    static final int COL_LABEL = 2;
    static final int COL_CODE = 3;

    AccountPermissionBUS accountPermissionBUS;
    List<AccountDTO> accounts = new ArrayList<>();
    AccountDTO selectedAccount;
    boolean dirty;
    boolean loadingPermissions;

    // bảng tài khoản
    JTextField txtSearch;
    JTable accountTable;
    DefaultTableModel accountTableModel;
    TableRowSorter<DefaultTableModel> accountRowSorter;

    // bảng quyền
    JTable permissionTable;
    DefaultTableModel permissionTableModel;
    JLabel lblSelectedAccount, lblPermissionSource;

    JButton selectAllBtn, clearAllBtn, resetBtn, saveBtn, refreshBtn;

    public PermissionPanel() {
        setLayout(new BorderLayout());

        // Chỉ quản trị viên được vào trang phân quyền
        if (!SessionManager.isAdmin()) {
            JLabel lblDenied = new JLabel("Chỉ quản trị viên mới được truy cập trang phân quyền.", JLabel.CENTER);
            lblDenied.setFont(UIFontHelper.SECTION_TITLE);
            add(lblDenied, BorderLayout.CENTER);
            return;
        }

        accountPermissionBUS = new AccountPermissionBUS();
        init();
        loadAccounts();
        updatePermissionControls();
    }

    private void init() {
        JLabel lblTitle = new JLabel("PHÂN QUYỀN TÀI KHOẢN", JLabel.CENTER);
        lblTitle.setFont(UIFontHelper.PAGE_TITLE);
        lblTitle.setBorder(BorderFactory.createEmptyBorder(10, 0, 0, 0));
        add(lblTitle, BorderLayout.NORTH);

        JSplitPane splitPane = new JSplitPane(
                JSplitPane.HORIZONTAL_SPLIT,
                buildAccountPanel(),
                buildPermissionPanel()
        );
        splitPane.setResizeWeight(0.4);
        splitPane.setBorder(BorderFactory.createEmptyBorder(10, 10, 0, 10));
        add(splitPane, BorderLayout.CENTER);

        JPanel southPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 15, 10));
        selectAllBtn = ButtonFactory.create("Chọn tất cả", UIColors.CHOSE, e -> setAllPermissions(true));
        clearAllBtn = ButtonFactory.create("Bỏ chọn tất cả", UIColors.CHOSE, e -> setAllPermissions(false));
        resetBtn = ButtonFactory.create("Khôi phục mặc định", UIColors.REFRESH, e -> resetToRoleDefault());
        saveBtn = ButtonFactory.create("Lưu thay đổi", UIColors.SAVE, e -> savePermissions());
        refreshBtn = ButtonFactory.create("Làm mới", UIColors.REFRESH, e -> refresh());
        southPanel.add(selectAllBtn);
        southPanel.add(clearAllBtn);
        southPanel.add(resetBtn);
        southPanel.add(saveBtn);
        southPanel.add(refreshBtn);
        add(southPanel, BorderLayout.SOUTH);
    }

    private JPanel buildAccountPanel() {
        JPanel panel = new JPanel(new BorderLayout(0, 8));
        panel.setBorder(createTitledBorder(" DANH SÁCH TÀI KHOẢN "));

        JPanel searchPanel = new JPanel(new BorderLayout(8, 0));
        searchPanel.add(new JLabel("Tìm kiếm:"), BorderLayout.WEST);
        txtSearch = new JTextField(15);
        txtSearch.addCaretListener(e -> searchAccount());
        searchPanel.add(txtSearch, BorderLayout.CENTER);
        panel.add(searchPanel, BorderLayout.NORTH);

        String[] columns = {"Mã TK", "Tên đăng nhập", "Vai trò", "Trạng thái"};
        accountTableModel = new DefaultTableModel(columns, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };
        accountTable = new JTable(accountTableModel);
        accountTable.setFont(UIFontHelper.TABLE);
        accountTable.setRowHeight(24);
        accountTable.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        accountRowSorter = new TableRowSorter<>(accountTableModel);
        accountTable.setRowSorter(accountRowSorter);
        accountTable.getSelectionModel().addListSelectionListener(e -> {
            if (!e.getValueIsAdjusting()) {
                onAccountSelected();
            }
        });

        panel.add(new JScrollPane(accountTable), BorderLayout.CENTER);
        return panel;
    }

    private JPanel buildPermissionPanel() {
        JPanel panel = new JPanel(new BorderLayout(0, 8));
        panel.setBorder(createTitledBorder(" DANH SÁCH QUYỀN "));

        JPanel infoPanel = new JPanel(new GridLayout(2, 1));
        lblSelectedAccount = new JLabel("Chưa chọn tài khoản");
        lblSelectedAccount.setFont(UIFontHelper.SECTION_TITLE);
        lblPermissionSource = new JLabel(" ");
        lblPermissionSource.setFont(UIFontHelper.BODY);
        infoPanel.add(lblSelectedAccount);
        infoPanel.add(lblPermissionSource);
        panel.add(infoPanel, BorderLayout.NORTH);

        String[] columns = {"Cấp quyền", "Nhóm", "Quyền", "Mã quyền"};
        permissionTableModel = new DefaultTableModel(columns, 0) {
            @Override
            public Class<?> getColumnClass(int column) {
                return column == COL_GRANTED ? Boolean.class : String.class;
            }

            @Override
            public boolean isCellEditable(int row, int column) {
                return column == COL_GRANTED && isSelectedAccountEditable();
            }
        };
        permissionTableModel.addTableModelListener(e -> {
            if (!loadingPermissions && e.getColumn() == COL_GRANTED) {
                dirty = true;
                updatePermissionControls();
            }
        });

        permissionTable = new JTable(permissionTableModel);
        permissionTable.setFont(UIFontHelper.TABLE);
        permissionTable.setRowHeight(24);
        permissionTable.getColumnModel().getColumn(COL_GRANTED).setMaxWidth(90);
        permissionTable.getColumnModel().getColumn(COL_GROUP).setPreferredWidth(120);
        permissionTable.getColumnModel().getColumn(COL_LABEL).setPreferredWidth(220);
        permissionTable.getColumnModel().getColumn(COL_CODE).setPreferredWidth(170);

        // Danh sách quyền cố định theo enum Permission
        for (Permission permission : Permission.values()) {
            permissionTableModel.addRow(new Object[]{
                    false,
                    permission.getGroup(),
                    permission.getLabel(),
                    permission.name()
            });
        }

        panel.add(new JScrollPane(permissionTable), BorderLayout.CENTER);
        return panel;
    }

    private TitledBorder createTitledBorder(String title) {
        TitledBorder border = BorderFactory.createTitledBorder(
                BorderFactory.createLineBorder(UIColors.BUTTON, 2), title
        );
        border.setTitleFont(UIFontHelper.SECTION_TITLE);
        border.setTitleColor(UIColors.BUTTON);
        return border;
    }

    private void loadAccounts() {
        accountTable.clearSelection();
        try {
            accounts = accountPermissionBUS.getAllAccounts();
        } catch (RuntimeException e) {
            accounts = new ArrayList<>();
            showError(e.getMessage());
        }

        accountTableModel.setRowCount(0);
        for (AccountDTO account : accounts) {
            accountTableModel.addRow(new Object[]{
                    account.getIdAccount(),
                    account.getUsername(),
                    account.getRole(),
                    account.getStatus()
            });
        }
    }

    private void searchAccount() {
        String keyword = txtSearch.getText().trim().toLowerCase();
        if (keyword.isEmpty()) {
            accountRowSorter.setRowFilter(null);
            return;
        }

        accountRowSorter.setRowFilter(new RowFilter<>() {
            @Override
            public boolean include(Entry<? extends DefaultTableModel, ? extends Integer> entry) {
                return entry.getStringValue(1).toLowerCase().contains(keyword)
                        || entry.getStringValue(2).toLowerCase().contains(keyword);
            }
        });
    }

    private void onAccountSelected() {
        AccountDTO account = getAccountAtSelectedRow();
        if (account == selectedAccount) {
            return;
        }

        if (dirty && !confirmDiscardChanges()) {
            // Giữ lại lựa chọn cũ để admin lưu thay đổi trước
            selectAccountRow(selectedAccount);
            return;
        }

        selectedAccount = account;
        loadPermissionsOfSelectedAccount();
    }

    private AccountDTO getAccountAtSelectedRow() {
        int row = accountTable.getSelectedRow();
        if (row == -1) {
            return null;
        }
        return accounts.get(accountTable.convertRowIndexToModel(row));
    }

    private void selectAccountRow(AccountDTO account) {
        int index = accounts.indexOf(account);
        if (index == -1) {
            accountTable.clearSelection();
            return;
        }
        int viewRow = accountTable.convertRowIndexToView(index);
        if (viewRow != -1) {
            accountTable.setRowSelectionInterval(viewRow, viewRow);
        }
    }

    private void loadPermissionsOfSelectedAccount() {
        Set<Permission> granted = EnumSet.noneOf(Permission.class);

        if (selectedAccount == null) {
            lblSelectedAccount.setText("Chưa chọn tài khoản");
            lblPermissionSource.setText(" ");
        } else {
            lblSelectedAccount.setText("Tài khoản: " + selectedAccount.getUsername()
                    + " (" + selectedAccount.getRole() + ")");
            try {
                granted = accountPermissionBUS.getEffectivePermissions(selectedAccount);
                if (selectedAccount.getRole() == Role.ADMIN) {
                    lblPermissionSource.setText("Quản trị viên luôn có toàn quyền, không thể chỉnh sửa.");
                } else if (accountPermissionBUS.hasCustomPermissions(selectedAccount)) {
                    lblPermissionSource.setText("Đang dùng quyền riêng của tài khoản.");
                } else {
                    lblPermissionSource.setText("Đang dùng quyền mặc định theo vai trò.");
                }
            } catch (RuntimeException e) {
                lblPermissionSource.setText("Không tải được quyền của tài khoản.");
                showError(e.getMessage());
            }
        }

        loadingPermissions = true;
        try {
            Permission[] permissions = Permission.values();
            for (int row = 0; row < permissions.length; row++) {
                permissionTableModel.setValueAt(granted.contains(permissions[row]), row, COL_GRANTED);
            }
        } finally {
            loadingPermissions = false;
        }

        dirty = false;
        updatePermissionControls();
    }

    private void setAllPermissions(boolean granted) {
        if (!isSelectedAccountEditable()) {
            return;
        }
        for (int row = 0; row < permissionTableModel.getRowCount(); row++) {
            permissionTableModel.setValueAt(granted, row, COL_GRANTED);
        }
    }

    private Set<Permission> getCheckedPermissions() {
        Set<Permission> permissions = EnumSet.noneOf(Permission.class);
        Permission[] values = Permission.values();
        for (int row = 0; row < values.length; row++) {
            if (Boolean.TRUE.equals(permissionTableModel.getValueAt(row, COL_GRANTED))) {
                permissions.add(values[row]);
            }
        }
        return permissions;
    }

    private void savePermissions() {
        if (!isSelectedAccountEditable()) {
            return;
        }
        stopPermissionEditing();

        try {
            if (accountPermissionBUS.savePermissions(selectedAccount, getCheckedPermissions())) {
                JOptionPane.showMessageDialog(this,
                        "Đã lưu quyền cho tài khoản " + selectedAccount.getUsername()
                                + ".\nThay đổi có hiệu lực từ lần đăng nhập tiếp theo của tài khoản đó.");
                loadPermissionsOfSelectedAccount();
            } else {
                showError("Lưu quyền không thành công.");
            }
        } catch (RuntimeException e) {
            showError(e.getMessage());
        }
    }

    private void resetToRoleDefault() {
        if (!isSelectedAccountEditable()) {
            return;
        }

        int confirm = JOptionPane.showConfirmDialog(this,
                "Khôi phục quyền của tài khoản " + selectedAccount.getUsername()
                        + " về mặc định theo vai trò " + selectedAccount.getRole() + "?",
                "Xác nhận", JOptionPane.YES_NO_OPTION);
        if (confirm != JOptionPane.YES_OPTION) {
            return;
        }

        try {
            accountPermissionBUS.resetToRoleDefault(selectedAccount);
            loadPermissionsOfSelectedAccount();
        } catch (RuntimeException e) {
            showError(e.getMessage());
        }
    }

    private void refresh() {
        if (dirty && !confirmDiscardChanges()) {
            return;
        }
        int selectedId = selectedAccount == null ? -1 : selectedAccount.getIdAccount();

        dirty = false;
        selectedAccount = null;
        txtSearch.setText("");
        loadAccounts();

        for (AccountDTO account : accounts) {
            if (account.getIdAccount() == selectedId) {
                selectAccountRow(account);
                return;
            }
        }
        loadPermissionsOfSelectedAccount();
    }

    private boolean isSelectedAccountEditable() {
        return selectedAccount != null && selectedAccount.getRole() != Role.ADMIN;
    }

    private void updatePermissionControls() {
        boolean editable = isSelectedAccountEditable();
        selectAllBtn.setEnabled(editable);
        clearAllBtn.setEnabled(editable);
        resetBtn.setEnabled(editable);
        saveBtn.setEnabled(editable && dirty);
    }

    private void stopPermissionEditing() {
        if (permissionTable.isEditing()) {
            permissionTable.getCellEditor().stopCellEditing();
        }
    }

    private boolean confirmDiscardChanges() {
        int confirm = JOptionPane.showConfirmDialog(this,
                "Quyền của tài khoản hiện tại chưa được lưu. Bỏ các thay đổi?",
                "Chưa lưu thay đổi", JOptionPane.YES_NO_OPTION);
        return confirm == JOptionPane.YES_OPTION;
    }

    private void showError(String message) {
        JOptionPane.showMessageDialog(this, message, "Lỗi", JOptionPane.ERROR_MESSAGE);
    }
}
