package org.toursalesmanager.gui.panel;

import org.toursalesmanager.gui.helper.TableLayoutHelper;

import org.toursalesmanager.gui.helper.TableSelectionHelper;

import org.toursalesmanager.gui.helper.WindowHelper;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.swing.*;
import javax.swing.border.TitledBorder;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableRowSorter;

import lombok.AccessLevel;
import lombok.experimental.FieldDefaults;
import org.toursalesmanager.bus.PaymentBUS;
import org.toursalesmanager.bus.TourBookingBUS;
import org.toursalesmanager.bus.TourPlanBUS;
import org.toursalesmanager.dto.TourBookingDTO;
import org.toursalesmanager.dto.TourPlanDTO;
import org.toursalesmanager.enums.BookingStatus;
import org.toursalesmanager.enums.Permission;
import org.toursalesmanager.enums.Role;
import org.toursalesmanager.gui.dialog.PaymentDialog;
import org.toursalesmanager.gui.dialog.TourBookingDetailDialog;
import org.toursalesmanager.gui.dialog.TourBookingDialog;
import org.toursalesmanager.gui.helper.BookingLookup;
import org.toursalesmanager.gui.component.ButtonFactory;
import org.toursalesmanager.gui.helper.UIColors;
import org.toursalesmanager.login.SessionManager;

@FieldDefaults(level = AccessLevel.PRIVATE)
public class TourBookingPanel extends JPanel {
    // Chỉ số cột trong model (không đổi khi ẩn cột khách hàng)
    static final int COL_ID = 0, COL_CUSTOMER = 1, COL_TOUR = 2, COL_DEPARTURE = 3,
            COL_TICKETS = 4, COL_TOTAL = 5, COL_REMAINING = 6, COL_STATUS = 7;

    JButton refreshBtn, editBtn, addBtn, deleteBtn, payBtn, detailBtn;

    // Phiếu đang hiển thị theo mã, dùng cho dialog chi tiết và tìm kiếm
    final Map<Integer, TourBookingDTO> bookings = new HashMap<>();
    final Map<Integer, String> searchText = new HashMap<>();
    BookingLookup lookup;

    JTextField txtSearch;

    // comboBox
    JComboBox<String> cmbSearchType;

    JPanel northPanel, southPanel, searchPanel;

    JScrollPane scrollPane;
    DefaultTableModel tableModel;
    TableRowSorter<DefaultTableModel> rowSorter;

    JTable table;

    TourBookingBUS tourBookingBus;
    TourPlanBUS tourPlanBUS;
    TourBookingDialog tourBookingDialog;

    public TourBookingPanel()  {
        tourBookingBus = new TourBookingBUS();
        tourPlanBUS = new TourPlanBUS();

        initComponents();
        boolean customer = SessionManager.hasRole(Role.CUSTOMER);
        addBtn.setVisible(SessionManager.hasPermission(Permission.CREATE_BOOKING));
        addBtn.setText(customer ? "Đặt tour" : "Thêm");
        editBtn.setVisible(!customer && SessionManager.hasPermission(Permission.UPDATE_BOOKING));
        deleteBtn.setVisible(!customer && SessionManager.isAdmin());
        // Khách hàng tự thanh toán phiếu của mình (README - mục Thanh toán)
        payBtn.setVisible(customer && SessionManager.hasPermission(Permission.VIEW_PAYMENT));
        loadTourBookingData(tourBookingBus.getVisibleBookings());
        hasSelectedRow();

        // Phiếu có thể được tạo từ trang khác (kế hoạch tour) nên tải lại mỗi khi mở trang
        addComponentListener(new ComponentAdapter() {
            @Override
            public void componentShown(ComponentEvent e) {
                loadTourBookingData(tourBookingBus.getVisibleBookings());
            }
        });
    }

    private void initComponents() {
        setLayout(new BorderLayout());

        // northPanel
        northPanel = new JPanel(new BorderLayout());
        JLabel jlbTitle = new JLabel(
                SessionManager.hasRole(Role.CUSTOMER) ? "ĐẶT TOUR CỦA TÔI" : "QUẢN LÝ PHIẾU ĐẶT TOUR",
                JLabel.CENTER
        );
        jlbTitle.setFont(new Font("Arial", Font.BOLD, 18));
        northPanel.add(jlbTitle, BorderLayout.NORTH); // northPanel add components

        // Search panel (define)
        searchPanel = new JPanel(new GridBagLayout());
        searchPanel.setBackground(Color.WHITE);

        // titleBorder
        TitledBorder titleSearch = BorderFactory.createTitledBorder(
                BorderFactory.createLineBorder(Color.CYAN, 2), " TÌM KIẾM PHIẾU ĐẶT TOUR "
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
        cmbSearchType = new JComboBox<>(SessionManager.hasRole(Role.CUSTOMER)
                ? new String[]{" Tất cả", " Mã phiếu", " Tour", " Trạng thái"}
                : new String[]{" Tất cả", " Mã phiếu", " Khách hàng", " Tour", " Trạng thái"});
        cmbSearchType.addActionListener(e -> searchByType());
        cmbSearchType.setFont(new Font("Arial", Font.PLAIN, 14));
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


        // southPanel contains btns
        southPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 15, 10));
        setAddBtn();
        setEditBtn();
        setDeleteBtn();
        setRefreshBtn();
        setPayBtn();
        setDetailBtn();
        southPanel.add(addBtn);
        southPanel.add(payBtn);
        southPanel.add(detailBtn);
        southPanel.add(editBtn);
        southPanel.add(deleteBtn);
        southPanel.add(refreshBtn);

        northPanel.add(searchPanel, BorderLayout.CENTER);

        // Phải tạo bảng trước khi add scrollPane, nếu không giao diện chỉ có scrollPane rỗng
        initTable();

        add(northPanel, BorderLayout.NORTH);
        add(scrollPane, BorderLayout.CENTER);
        add(southPanel, BorderLayout.SOUTH);

    }// </editor-fold>//GEN-END:initComponents
    private void jComboBox2ActionPerformed(ActionEvent evt) {//GEN-FIRST:event_jComboBox2ActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_jComboBox2ActionPerformed

    public void initTable(){
        // Bảng chỉ hiện thông tin chính; ngày đặt, đơn giá, ghi chú, khách hàng, kế hoạch tour, lịch sử thanh toán... xem ở "Xem chi tiết"
        String[] columns = {"Mã phiếu", "Khách hàng", "Tour", "Khởi hành", "Số vé", "Tổng tiền", "Còn phải trả", "Trạng thái"};

        tableModel = new DefaultTableModel(columns, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }

            // Kiểu thật của cột để sắp xếp đúng theo số / ngày thay vì theo chữ
            @Override
            public Class<?> getColumnClass(int column) {
                return switch (column) {
                    case COL_ID, COL_TICKETS -> Integer.class;
                    case COL_DEPARTURE -> LocalDate.class;
                    case COL_TOTAL, COL_REMAINING -> BigDecimal.class;
                    default -> String.class;
                };
            }
        };
        table = new JTable(tableModel);
        TableLayoutHelper.moneyColumns(table, "Tổng tiền", "Còn phải trả");
        table.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        table.setDefaultRenderer(LocalDate.class, new DefaultTableCellRenderer() {
            @Override
            protected void setValue(Object value) {
                setText(value instanceof LocalDate date ? date.format(PaymentBUS.DATE) : "");
            }
        });
        // Mã phiếu lưu dạng số (để sắp xếp), hiển thị dạng PDT001 giống trang thanh toán
        table.getColumnModel().getColumn(COL_ID).setCellRenderer(new DefaultTableCellRenderer() {
            @Override
            protected void setValue(Object value) {
                setText(value instanceof Integer id ? PaymentBUS.bookingCode(id) : "");
            }
        });

        rowSorter = new TableRowSorter<>(tableModel);
        table.setRowSorter(rowSorter);

        // Khách hàng chỉ xem phiếu của mình nên không cần cột khách hàng
        if (SessionManager.hasRole(Role.CUSTOMER)) {
            table.removeColumn(table.getColumnModel().getColumn(COL_CUSTOMER));
        }

        // Nhấp đúp: khách hàng mở màn hình thanh toán, nhân viên xem chi tiết phiếu
        table.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                if (e.getClickCount() != 2 || table.getSelectedRow() == -1) {
                    return;
                }
                if (payBtn.isVisible()) {
                    openPayment();
                } else {
                    openDetail();
                }
            }
        });

        scrollPane = new JScrollPane(table);
        scrollPane.setBorder(BorderFactory.createEmptyBorder(0, 10, 0, 10));
    }


    private void setAddBtn(){
        addBtn = ButtonFactory.create("Thêm", UIColors.ADD);
        addBtn.addActionListener(v -> {
            tourBookingDialog = new TourBookingDialog(
                    WindowHelper.getParentFrame(this), true, TourBookingDialog.Mode.ADD, null, tourPlanBUS);
            tourBookingDialog.setVisible(true);
            loadTourBookingData(tourBookingBus.getVisibleBookings());
        });
    }

    private void setDeleteBtn(){
        deleteBtn = ButtonFactory.create("Xóa", UIColors.DELETE);
        deleteBtn.setEnabled(false);
        deleteBtn.addActionListener(v -> {
            if (!SessionManager.isAdmin()) {
                JOptionPane.showMessageDialog(this, "Bạn không có quyền xóa dữ liệu.");
                return;
            }
            int i = table.getSelectedRow();
            int result = JOptionPane.showConfirmDialog(this, "Bạn có chắc chắn muốn xóa?", "Xác nhận", JOptionPane.YES_NO_OPTION);
            if (result == JOptionPane.YES_OPTION && i >= 0) {
                int modelRow = table.convertRowIndexToModel(i);
                int idTourBooking = (Integer) tableModel.getValueAt(modelRow, 0);
                if (!tourBookingBus.deleteTourBooking(idTourBooking)) {
                    JOptionPane.showMessageDialog(this, "Không thể xóa phiếu đặt tour.",
                            "Lỗi", JOptionPane.ERROR_MESSAGE);
                }
                loadTourBookingData(tourBookingBus.getVisibleBookings());
            }
        });
    }

    private void setEditBtn(){
        editBtn = ButtonFactory.create("Sửa", UIColors.EDIT);
        editBtn.setEnabled(false);
        editBtn.addActionListener(v -> {
            int row = table.getSelectedRow();
            if (row == -1) {
                JOptionPane.showMessageDialog(this, "Vui lòng chọn phiếu đặt tour cần sửa");
                return;
            }

            if (row >= 0) {
                int modelRow = table.convertRowIndexToModel(row);
                int idTourBooking = (Integer) tableModel.getValueAt(modelRow, 0);
                TourBookingDTO khangkht = tourBookingBus.findTourBookingById(idTourBooking);
                if (khangkht != null) {
                    tourBookingDialog = new TourBookingDialog(
                            WindowHelper.getParentFrame(this), true, TourBookingDialog.Mode.EDIT, khangkht, tourPlanBUS);
                    tourBookingDialog.setVisible(true);

                    loadTourBookingData(tourBookingBus.getVisibleBookings());
                }
            }
        });
    }

    private void setDetailBtn(){
        detailBtn = ButtonFactory.create("Xem chi tiết", UIColors.VIEW);
        detailBtn.setEnabled(false);
        detailBtn.addActionListener(v -> openDetail());
    }

    private void openDetail() {
        TourBookingDTO booking = selectedBooking();
        if (booking == null) {
            JOptionPane.showMessageDialog(this, "Vui lòng chọn phiếu đặt tour cần xem");
            return;
        }
        TourBookingDetailDialog.open(this, booking, lookup);
    }

    private TourBookingDTO selectedBooking() {
        int modelRow = TableSelectionHelper.getSelectedModelRow(table);
        return modelRow == -1 ? null : bookings.get((Integer) tableModel.getValueAt(modelRow, COL_ID));
    }

    private void setPayBtn(){
        payBtn = ButtonFactory.create("Thanh toán", UIColors.SAVE);
        payBtn.setEnabled(false);
        payBtn.addActionListener(v -> openPayment());
    }

    private void openPayment() {
        int modelRow = TableSelectionHelper.getSelectedModelRow(table);
        if (modelRow == -1) {
            JOptionPane.showMessageDialog(this, "Vui lòng chọn phiếu đặt tour cần thanh toán");
            return;
        }
        PaymentDialog.open(this, (Integer) tableModel.getValueAt(modelRow, 0));
        loadTourBookingData(tourBookingBus.getVisibleBookings());
    }

    private void setRefreshBtn(){
        refreshBtn = ButtonFactory.create("Làm mới", UIColors.REFRESH);
        refreshBtn.addActionListener(v -> {
            loadTourBookingData(tourBookingBus.getVisibleBookings());
        });
    }

    private void loadTourBookingData(List<TourBookingDTO> ls) {
        tableModel.setRowCount(0);
        bookings.clear();
        searchText.clear();
        lookup = new BookingLookup();

        // Số tiền đã thanh toán lấy từ bảng payment; CSDL cũ chưa có bảng thì để trống
        Map<Integer, BigDecimal> paidTotals = null;
        if (SessionManager.hasPermission(Permission.VIEW_PAYMENT)) {
            try {
                paidTotals = new PaymentBUS().getConfirmedTotals();
            } catch (RuntimeException e) {
                System.err.println(e.getMessage());
            }
        }

        for (TourBookingDTO dto : ls) {
            BigDecimal paid = paidTotals == null ? null : paidTotals.getOrDefault(dto.getIdTourBooking(), BigDecimal.ZERO);
            // Phiếu đã hủy / hết hạn không còn nghĩa vụ thanh toán nên để trống
            BigDecimal remaining = paid == null || dto.getCostTotal() == null || !isPayable(dto)
                    ? null : dto.getCostTotal().subtract(paid).max(BigDecimal.ZERO);
            TourPlanDTO plan = lookup.plan(dto.getIdTourPlan());
            String customerName = lookup.customerName(dto.getIdCustomer());
            String tourName = lookup.tourName(dto.getIdTourPlan());

            tableModel.addRow(new Object[]{
                    dto.getIdTourBooking(),
                    customerName,
                    tourName,
                    plan == null ? null : plan.getDepartureDate(),
                    dto.getTickets(),
                    dto.getCostTotal(),
                    remaining,
                    dto.getBookingStatus()
            });
            bookings.put(dto.getIdTourBooking(), dto);
            // "Tất cả" tìm cả các trường chỉ có trong dialog chi tiết (mã khách hàng, mã kế hoạch, ghi chú...)
            searchText.put(dto.getIdTourBooking(), String.join(" ",
                    dto.getBookingCode(), customerName, String.valueOf(dto.getIdCustomer()), tourName,
                    String.valueOf(dto.getIdTourPlan()), String.valueOf(dto.getBookingStatus()),
                    String.valueOf(dto.getNote())).toLowerCase());
        }
        searchByType();
    }

    private static boolean isPayable(TourBookingDTO dto) {
        BookingStatus status = BookingStatus.fromLabel(dto.getBookingStatus());
        return status != BookingStatus.CANCELLED && status != BookingStatus.EXPIRED;
    }

    private void hasSelectedRow(){
        TableSelectionHelper.bindButtons(table, editBtn, deleteBtn, payBtn, detailBtn);
    }

    private void searchByType(){
        String keyWord = txtSearch.getText().trim().toLowerCase();
        String searchType = (String) cmbSearchType.getSelectedItem();

        RowFilter<DefaultTableModel, Object> rf = new RowFilter<DefaultTableModel, Object>() {
            @Override
            public boolean include(Entry<? extends DefaultTableModel, ? extends Object> entry) {
                if (keyWord.isEmpty()) {
                    return true;
                }
                Integer id = (Integer) entry.getValue(COL_ID);
                return switch (searchType) {
                    case " Mã phiếu" -> PaymentBUS.bookingCode(id).toLowerCase().contains(keyWord);
                    case " Khách hàng" -> entry.getStringValue(COL_CUSTOMER).toLowerCase().contains(keyWord)
                            || String.valueOf(bookings.get(id).getIdCustomer()).toLowerCase().contains(keyWord);
                    case " Tour" -> entry.getStringValue(COL_TOUR).toLowerCase().contains(keyWord);
                    case " Trạng thái" -> entry.getStringValue(COL_STATUS).toLowerCase().contains(keyWord);
                    default -> searchText.getOrDefault(id, "").contains(keyWord); // Tất cả
                };
            }
        };
        rowSorter.setRowFilter(rf);
    }
}