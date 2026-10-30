package org.toursalesmanager.gui.panel;
import lombok.AccessLevel;
import lombok.experimental.FieldDefaults;
import org.toursalesmanager.bus.TourBUS;
import org.toursalesmanager.bus.TourPlanBUS;
import org.toursalesmanager.dto.TourDTO;
import org.toursalesmanager.enums.Permission;
import org.toursalesmanager.enums.Role;
import org.toursalesmanager.gui.component.ButtonFactory;
import org.toursalesmanager.gui.dialog.TourDetailDialog;
import org.toursalesmanager.gui.dialog.TourDiaLog;
import org.toursalesmanager.gui.dialog.CustomerTourBookingDialog;
import org.toursalesmanager.gui.dialog.TourBookingDialog;
import org.toursalesmanager.gui.helper.UIColors;
import org.toursalesmanager.login.SessionManager;
import org.toursalesmanager.gui.MainFrame;

import javax.swing.*;
import javax.swing.border.TitledBorder;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableRowSorter;
import java.awt.*;

@FieldDefaults(level = AccessLevel.PRIVATE)
public class TourPanel extends JPanel {
    // text field
    JTextField txtSearch;

    // define panel
    JPanel northPanel, southPanel, searchPanel;

    // define button

    JButton addBtn, deleteBtn, editBtn, refreshBtn, detailBtn, bookingBtn;

    // relate to table
    JTable table;
    JScrollPane scrollPane;
    DefaultTableModel tableModel;
    TableRowSorter<DefaultTableModel> rowSorter;

    // comboBox
    JComboBox<String> cmbSearchType;

    final TourBUS tourBUS;

    public TourPanel(){
        if (!SessionManager.hasPermission(Permission.VIEW_TOUR)) {
            throw new SecurityException("Bạn không có quyền xem tour");
        }
        tourBUS = new TourBUS();
        init();
        loadTable();
        hasSelectedRow();
    }

    private void init(){
        boolean canManageTours =
                SessionManager.hasPermission(Permission.MANAGE_TOUR);

        setLayout(new BorderLayout());

        // northPanel
        northPanel = new JPanel(new BorderLayout());
        JLabel jlbTitle = new JLabel(
                canManageTours ? "QUẢN LÝ TOUR DU LỊCH" : "DANH SÁCH TOUR DU LỊCH",
                JLabel.CENTER
        );
        jlbTitle.setFont(new Font("Arial", Font.BOLD, 18));
        northPanel.add(jlbTitle, BorderLayout.NORTH); // northPanel add components

        // Search panel (define)
        searchPanel = new JPanel(new GridBagLayout());
        searchPanel.setBackground(Color.WHITE);

        // titleBorder
        TitledBorder titleSearch = BorderFactory.createTitledBorder(
                BorderFactory.createLineBorder(Color.CYAN, 2), " TÌM KIẾM TOUR "
        );
        titleSearch.setTitleFont(new Font("Arial", Font.BOLD, 14));
        titleSearch.setTitleColor(new Color(0, 102, 204));
        searchPanel.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createEmptyBorder(20, 10, 20, 10),
                titleSearch)
        );

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(10, 10, 10, 10); // Khoảng cách giữa các ô
        gbc.fill = GridBagConstraints.HORIZONTAL;

        // column 1 : type label
        gbc.gridx = 0; gbc.gridy = 0;
        searchPanel.add(new JLabel("Loại:"), gbc);

        // column 2 : cmbSearch By Type
        cmbSearchType = new JComboBox<>(new String[]{
                " Tất cả", " Tên Tour", " Địa điểm khởi hành"
        });
        cmbSearchType.setFont(new Font("Arial", Font.PLAIN, 14));
        cmbSearchType.addActionListener(e -> searchByType());
        gbc.gridx = 1;
        searchPanel.add(cmbSearchType, gbc);

        // column 3 : keyWord label
        gbc.gridx = 2;
        searchPanel.add(new JLabel("Từ khóa:"), gbc);

        // column 4 : keyWord txtField
        txtSearch = new JTextField(15);
        txtSearch.addCaretListener(e -> searchByType());
        gbc.gridx = 3; gbc.weightx = 1.0;
        searchPanel.add(txtSearch, gbc);

        northPanel.add(searchPanel, BorderLayout.CENTER);

        initTable(); // init table

        // southPanel contain buttons
        southPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 15, 10));

        add();
        addBtn.setVisible(canManageTours);
        southPanel.add(addBtn);

        delete();
        deleteBtn.setVisible(canManageTours);
        southPanel.add(deleteBtn);

        edit();
        editBtn.setVisible(canManageTours);
        southPanel.add(editBtn);

        viewDetail();
        southPanel.add(detailBtn);

        booking();
        bookingBtn.setVisible(SessionManager.canRequestBooking());
        southPanel.add(bookingBtn);

        refresh();
        southPanel.add(refreshBtn);

        add(northPanel, BorderLayout.NORTH);
        add(scrollPane, BorderLayout.CENTER);
        add(southPanel, BorderLayout.SOUTH);
    }

    private void initTable(){
        // columns of table
        String[] columns = {"Mã tour", "Tên", "Số ngày", "Đơn giá", "Số chỗ", "Địa điểm khởi hành", "Mã loại tour", "Mã địa điểm"};

        tableModel = new DefaultTableModel(columns, 0);
        table = new JTable(tableModel);

        table.setDefaultEditor(Object.class, null);

        rowSorter = new TableRowSorter<>(tableModel);
        table.setRowSorter(rowSorter);

        scrollPane = new JScrollPane(table);
        scrollPane.setBorder(BorderFactory.createEmptyBorder(0, 10, 0, 10));
    }

    private void searchByType(){
        String keyWord = txtSearch.getText().trim().toLowerCase();
        String searchType = (String) cmbSearchType.getSelectedItem();

        RowFilter<DefaultTableModel, Object> rf = new RowFilter<DefaultTableModel, Object>() {
            @Override
            public boolean include(Entry<? extends DefaultTableModel, ? extends Object> entry) {
                if (!keyWord.isEmpty()) {
                    boolean found = false;
                    switch (searchType) {
                        case " Tên Tour":
                            found = entry.getStringValue(1).toLowerCase().contains(keyWord);
                            break;
                        case " Địa điểm khởi hành":
                            found = entry.getStringValue(5).toLowerCase().contains(keyWord);
                            break;
                        default: // Tất cả
                            for (int i = 0; i < entry.getValueCount(); i++) {
                                if (entry.getStringValue(i).toLowerCase().contains(keyWord)) {
                                    found = true;
                                    break;
                                }
                            }
                    }
                    if (!found) return false;
                }

                return true;
            }
        };
        rowSorter.setRowFilter(rf);
    }

    private void add(){
        addBtn = ButtonFactory.create("Thêm Tour", UIColors.ADD);
        addBtn.addActionListener(e -> {
            if (!requireManagePermission()) {
                return;
            }
            openDiaLog(null);
        });
    }

    private void openDiaLog(TourDTO tourDTO){
        TourDiaLog tourDiaLog = new TourDiaLog(tourBUS, tourDTO);
        tourDiaLog.setVisible(true);
        loadTable();
    }

    private void delete(){
        deleteBtn = ButtonFactory.create("Xóa tour", UIColors.DELETE);
        deleteBtn.setEnabled(false);
        deleteBtn.addActionListener(e ->{
            if (!requireManagePermission()) {
                return;
            }
            int row = table.getSelectedRow();
            if (row == -1){
                JOptionPane.showMessageDialog(this, "Vui lòng chọn tour cần xóa");
                return;
            }

            String maTour = getSelectedTourId();
            if (maTour == null) {
                return;
            }
            int confirm = JOptionPane.showConfirmDialog(this, "Xác nhận xóa?");
            if(confirm == JOptionPane.YES_OPTION){
                boolean result = tourBUS.removeTour(maTour);
                if(result)
                    JOptionPane.showMessageDialog(null, "Đã xóa tour có mã: " + maTour);
                else
                    JOptionPane.showMessageDialog(null, "Mã tour không tồn tại: " + maTour);
                loadTable();
            }
        });
    }

    private void edit(){
        editBtn = ButtonFactory.create("Chỉnh sửa", UIColors.EDIT);
        editBtn.setEnabled(false);
        editBtn.addActionListener(e -> {
            if (!requireManagePermission()) {
                return;
            }

            int row = table.getSelectedRow();
            if (row == -1) {
                JOptionPane.showMessageDialog(this, "Vui lòng chọn tour cần sửa");
                return;
            }
            String maTour = getSelectedTourId();
            if (maTour == null) {
                return;
            }
            TourDTO t = tourBUS.getByID(maTour);
            openDiaLog(t);
        });
    }

    private void viewDetail(){
        detailBtn = ButtonFactory.create("Xem chi tiết", UIColors.VIEW);
        detailBtn.setEnabled(false);
        detailBtn.addActionListener(e -> {
            String maTour = getSelectedTourId();
            if (maTour == null) {
                JOptionPane.showMessageDialog(this, "Vui lòng chọn kế tour để xem");
                return;
            }
            TourDTO t = tourBUS.getByID(maTour);
            TourDetailDialog dialog = new TourDetailDialog(t);
            dialog.setVisible(true);
        });
    }

    private void booking() {
        bookingBtn = ButtonFactory.create("Chọn lịch & đặt tour", UIColors.BOOKING);
        bookingBtn.addActionListener(e -> {
            int row = table.getSelectedRow();
            String selectedTourId = null;
            if (row >= 0) {
                int modelRow = table.convertRowIndexToModel(row);
                selectedTourId = tableModel.getValueAt(modelRow, 0).toString();
            }

            final String selectedTourIdForDialog = selectedTourId;
            final Frame parentFrame = (this.getTopLevelAncestor() instanceof Frame frame)
                    ? frame
                    : null;

            // Sau khi đăng nhập, MainFrame dựng lại panel mới nên dùng parentFrame thay vì this
            MainFrame.requireLogin(this, () -> {
                if (!SessionManager.hasPermission(Permission.CREATE_BOOKING)) {
                    JOptionPane.showMessageDialog(parentFrame,
                            "Tài khoản hiện tại không có quyền đặt tour.",
                            "Không đủ quyền",
                            JOptionPane.WARNING_MESSAGE);
                    return;
                }

                if (SessionManager.hasRole(Role.CUSTOMER)) {
                    CustomerTourBookingDialog dialog = new CustomerTourBookingDialog(
                            parentFrame, true, new TourPlanBUS(), null, selectedTourIdForDialog);
                    dialog.setVisible(true);
                } else {
                    // Admin/nhân viên tạo phiếu đặt hộ khách hàng
                    TourBookingDialog dialog = new TourBookingDialog(
                            parentFrame, true, TourBookingDialog.Mode.ADD, null, new TourPlanBUS());
                    dialog.setVisible(true);
                }
            });
        });
    }

    private void refresh(){
        refreshBtn = ButtonFactory.create("Làm mới", UIColors.REFRESH);
        refreshBtn.addActionListener(e -> {
            loadTable();
            searchByType();
        });
    }

    public void loadTable(){
        tableModel.setRowCount(0);

        for(TourDTO t : tourBUS.getAllTours()){
            tableModel.addRow(new Object[]{
                    t.getIdTour(),
                    t.getTourName(),
                    t.getNumberOfDate(),
                    t.getPrice(),
                    t.getSeats(),
                    t.getDepartureLocation(),
                    t.getIdTourType(),
                    t.getIdLocation()
            });
        }
    }

    private void hasSelectedRow(){
        table.getSelectionModel().addListSelectionListener(e ->{
            boolean hadSelection = table.getSelectedRow() != -1;

            boolean canManageTours =
                    SessionManager.hasPermission(Permission.MANAGE_TOUR);
            deleteBtn.setEnabled(hadSelection && canManageTours);
            editBtn.setEnabled(hadSelection && canManageTours);
            detailBtn.setEnabled(hadSelection);
        });
    }

    private String getSelectedTourId() {
        int selectedRow = table.getSelectedRow();
        if (selectedRow < 0) {
            return null;
        }

        int modelRow = table.convertRowIndexToModel(selectedRow);
        return tableModel.getValueAt(modelRow, 0).toString();
    }

    private boolean requireManagePermission() {
        if (SessionManager.hasPermission(Permission.MANAGE_TOUR)) {
            return true;
        }

        JOptionPane.showMessageDialog(
                this,
                "Bạn không có quyền quản lý tour.",
                "Không đủ quyền",
                JOptionPane.WARNING_MESSAGE
        );
        return false;
    }
}
