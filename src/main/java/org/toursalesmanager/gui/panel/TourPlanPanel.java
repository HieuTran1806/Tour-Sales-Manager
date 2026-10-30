package org.toursalesmanager.gui.panel;

import lombok.AccessLevel;
import lombok.experimental.FieldDefaults;
import org.toursalesmanager.bus.TourPlanBUS;
import org.toursalesmanager.bus.TourBUS;
import org.toursalesmanager.dto.TourPlanDTO;
import org.toursalesmanager.dto.TourDTO;
import org.toursalesmanager.enums.Permission;
import org.toursalesmanager.enums.Role;
import org.toursalesmanager.gui.MainFrame;
import org.toursalesmanager.gui.component.ButtonFactory;
import org.toursalesmanager.gui.dialog.TourPlanDetailDialog;
import org.toursalesmanager.gui.dialog.TourPlanDialog;
import org.toursalesmanager.gui.dialog.TourBookingDialog;
import org.toursalesmanager.gui.dialog.CustomerTourBookingDialog;
import org.toursalesmanager.gui.helper.UIColors;
import org.toursalesmanager.login.SessionManager;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;


@FieldDefaults(level = AccessLevel.PRIVATE)
public class TourPlanPanel extends JPanel {
    final JComboBox<TourDTO> cbTour;

    DefaultComboBoxModel<TourDTO> toursModel;

    JButton addBtn, deleteBtn, editBtn, detailsBtn, refreshBtn, bookingBtn;

    DefaultTableModel tableModel;

    JTable table;

    JScrollPane scrollPane;

    TourPlanBUS tourPlanBUS;

    TourBUS tourBUS;

    JLabel jlbChonTour;

    ArrayList<TourPlanDTO> lsKeHoachTours;

    TourBookingDialog tourBookingDialog;
    CustomerTourBookingDialog customerTourBookingDialog;

    // formatter
    DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");

    public TourPlanPanel(){
        tourPlanBUS = new TourPlanBUS();
        tourBUS = new TourBUS();
        cbTour = new JComboBox<>();

        init();

        // first load table
        TourDTO selectedTour = (TourDTO) cbTour.getSelectedItem();
        if(selectedTour != null)
            loadTable(selectedTour.getIdTour());
        hasSelectedRow();
    }

    private void init(){
        boolean canManageTourPlan = SessionManager.hasPermission(Permission.MANAGE_TOUR_PLAN);

        setLayout(new BorderLayout());

        //North Panel
        JPanel northPanel = new JPanel(new BorderLayout());
        JLabel lblTitle = new JLabel(
                canManageTourPlan ? "QUẢN LÝ KẾ HOẠCH TOUR" : "XEM KẾ HOẠCH TOUR", JLabel.CENTER);
        lblTitle.setFont(new Font("Arial", Font.BOLD, 18));
        northPanel.add(lblTitle, BorderLayout.NORTH);

        //center panel add jlabel chose tour
        JPanel filterPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        jlbChonTour = new JLabel("Chọn Tour");
        filterPanel.add(jlbChonTour);

        // Load combo tours
        toursModel = new DefaultComboBoxModel<>();
        toursModel = CBTourPresent();
        cbTour.setModel(toursModel);
        filterPanel.add(cbTour); // center panel add combobox tours

        cbTour.addActionListener(e -> {
            TourDTO selectedTour = (TourDTO) cbTour.getSelectedItem();
            if(selectedTour != null){
                loadTable(selectedTour.getIdTour());
            }
        });

        //northPanel add filterPanel
        northPanel.add(filterPanel, BorderLayout.CENTER);

        // init table tourPlan
        initTable();

        //South Panel contain buttons
        JPanel southPanel = new JPanel(new FlowLayout());
        add();
        addBtn.setVisible(canManageTourPlan);
        southPanel.add(addBtn);

        delete();
        deleteBtn.setVisible(canManageTourPlan);
        southPanel.add(deleteBtn);

        edit();
        editBtn.setVisible(canManageTourPlan);
        southPanel.add(editBtn);

        viewDetail();
        southPanel.add(detailsBtn);

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
        String[] columns = {"Mã kế hoạch Tour", "Ngày khởi hành", "Ngày kết thúc", "Tổng số vé", "Tổng chi dự kiến", "Số vé còn lại", "Trạng thái", "Mã Tour", "Mã nhân viên hướng dẫn"};

        tableModel = new DefaultTableModel(columns, 0);
        table = new JTable(tableModel);
        table.setDefaultEditor(Object.class, null);

        scrollPane = new JScrollPane(table);
        scrollPane.setBorder(BorderFactory.createEmptyBorder(0, 10, 0, 10));
    }

    private void loadTable(String maTour){
        tableModel.setRowCount(0);
        lsKeHoachTours = tourPlanBUS.getAllKeHoachToursByID(maTour);

        for (TourPlanDTO kt : lsKeHoachTours){
            tableModel.addRow(new Object[]{
                    kt.getIdTourPlan(),
                    kt.getDepartureDate().format(formatter),
                    kt.getEndDate().format(formatter),
                    kt.getTickets(),
                    kt.getEstimatedCost(),
                    kt.getRemainingTickets(),
                    kt.getStatus(),
                    kt.getIdTour(),
                    kt.getIdTourGuide()
            });
        }
    }

    private DefaultComboBoxModel<TourDTO> CBTourPresent(){
        DefaultComboBoxModel<TourDTO> model;
        model = new DefaultComboBoxModel<>();
        ArrayList<TourDTO> lsTours = tourBUS.getAllTours();
        for (TourDTO t : lsTours){
            model.addElement(t);
        }
        return model;
    }

    private void add(){
        addBtn = ButtonFactory.create("Thêm kế hoạch Tour", UIColors.ADD);
        TourPlanDTO tourPlanDTO = null;
        addBtn.addActionListener(e -> {
            if (!requireManagePermission()) {
                return;
            }
            openDiaLog(tourPlanDTO);
        });
    }

    private void openDiaLog(TourPlanDTO tourPlanDTO){
        TourDTO selectedTour = (TourDTO) cbTour.getSelectedItem();

        if(selectedTour == null){
            JOptionPane.showMessageDialog(this, "Vui lòng chọn tour");
            return;
        }
        TourPlanDialog tourPlanDialog = new TourPlanDialog(tourPlanBUS, tourPlanDTO, tourBUS,( (TourDTO)cbTour.getSelectedItem()).getIdTour());
        tourPlanDialog.setVisible(true);

        loadTable(selectedTour.getIdTour());
    }

    private void booking(){
        bookingBtn = ButtonFactory.create(
                !SessionManager.isLoggedIn() || SessionManager.hasRole(Role.CUSTOMER)
                        ? "Đặt tour" : "Tạo phiếu đặt",
                UIColors.BOOKING
        );
        bookingBtn.addActionListener(e -> {
            int row = table.getSelectedRow();
            String planId = row >= 0
                    ? tableModel.getValueAt(table.convertRowIndexToModel(row), 0).toString()
                    : null;
            // Sau khi đăng nhập, MainFrame dựng lại panel mới nên giữ lại cửa sổ cha
            Window owner = SwingUtilities.getWindowAncestor(this);

            MainFrame.requireLogin(this, () -> openBookingDialog(owner, planId));
        });
    }

    private void openBookingDialog(Window owner, String planId) {
        if (!SessionManager.hasPermission(Permission.CREATE_BOOKING)) {
            JOptionPane.showMessageDialog(owner,
                    "Tài khoản hiện tại không có quyền đặt tour.",
                    "Không đủ quyền",
                    JOptionPane.WARNING_MESSAGE);
            return;
        }
        if (SessionManager.hasRole(Role.CUSTOMER)) {
            customerTourBookingDialog = new CustomerTourBookingDialog(
                    null, true, tourPlanBUS, planId, null);
            customerTourBookingDialog.setVisible(true);
            return;
        }
        if (planId == null) {
            JOptionPane.showMessageDialog(owner, "Vui lòng chọn kế hoạch tour cần đặt.");
            return;
        }
        tourBookingDialog = new TourBookingDialog(
                null, true, TourBookingDialog.Mode.ADD, null, tourPlanBUS);
        tourBookingDialog.setVisible(true);
    }

    private void delete(){
        deleteBtn = ButtonFactory.create("Xóa kế hoạch tour", UIColors.DELETE);
        deleteBtn.setEnabled(false);
        deleteBtn.addActionListener(e ->{
            if (!requireManagePermission()) {
                return;
            }

            int row = table.getSelectedRow();
            if (row == -1){
                JOptionPane.showMessageDialog(this, "Vui lòng chọn kế hoạch tour cần xóa");
                return;
            }

            String maKHTour = tableModel.getValueAt(row, 0).toString();
            int confirm = JOptionPane.showConfirmDialog(this, "Xác nhận xóa?");
            if(confirm == JOptionPane.YES_OPTION){
                boolean result = tourPlanBUS.removeKeHoachTour(maKHTour);
                if(result)
                    JOptionPane.showMessageDialog(null, "Đã xóa kế hoạch tour có mã: " + maKHTour);
                else
                    JOptionPane.showMessageDialog(null, "Không thể xóa kế hoạch tour này");
                TourDTO selectedTour = (TourDTO) cbTour.getSelectedItem();
                loadTable(selectedTour.getIdTour());
            }
        });
    }

    private void edit(){
        editBtn = ButtonFactory.create("Chỉnh sửa", UIColors.EDIT);
        editBtn.setEnabled(false);
        editBtn.addActionListener(e -> {
            if(!requireManagePermission()) {
                return;
            }

            int row = table.getSelectedRow();
            if (row == -1) {
                JOptionPane.showMessageDialog(this, "Vui lòng chọn kế hoạch tour cần sửa");
                return;
            }
            String maKHTour = tableModel.getValueAt(row, 0).toString();
            TourPlanDTO kt = tourPlanBUS.getById(maKHTour);
            openDiaLog(kt);
        });
    }

    private void viewDetail(){
        detailsBtn = ButtonFactory.create("Xem chi tiết", UIColors.VIEW);
        detailsBtn.setEnabled(false);
        detailsBtn.addActionListener(e -> {
            int row = table.getSelectedRow();
            if (row == -1) {
                JOptionPane.showMessageDialog(this, "Vui lòng chọn kế hoạch tour để xem");
                return;
            }


            TourDTO selectedTour = (TourDTO) cbTour.getSelectedItem();
            assert selectedTour != null;

            TourPlanDetailDialog dialog = new TourPlanDetailDialog(selectedTour.getIdTour());
            dialog.setVisible(true);
        });
    }

    private void refresh(){
        refreshBtn = ButtonFactory.create("Làm mới", UIColors.REFRESH);
        refreshBtn.addActionListener(e -> {
            //load tours when press refresh
            toursModel = new DefaultComboBoxModel<>();
            toursModel = CBTourPresent();
            cbTour.setModel(toursModel);
            CBTourPresent();

            TourDTO selectedTour = (TourDTO) cbTour.getSelectedItem();
            assert selectedTour != null;
            loadTable(selectedTour.getIdTour());
        });
    }

    private void hasSelectedRow(){
        table.getSelectionModel().addListSelectionListener(e ->{
            boolean hadSelection = table.getSelectedRow() != -1;
            deleteBtn.setEnabled(hadSelection);
            editBtn.setEnabled(hadSelection);
            detailsBtn.setEnabled(hadSelection);
            bookingBtn.setEnabled(SessionManager.canRequestBooking()
                    && (!SessionManager.isLoggedIn() || SessionManager.hasRole(Role.CUSTOMER) || hadSelection));
        });
    }

    private boolean requireManagePermission() {
        if (SessionManager.hasPermission(Permission.MANAGE_TOUR_PLAN)) {
            return true;
        }

        JOptionPane.showMessageDialog(
                this,
                "Bạn không có quyền quản lý kế hoạch tour.",
                "Không đủ quyền",
                JOptionPane.WARNING_MESSAGE
        );
        return false;
    }
}
