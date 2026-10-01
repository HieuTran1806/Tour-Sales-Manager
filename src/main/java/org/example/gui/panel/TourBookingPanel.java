package org.example.gui.panel;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.util.ArrayList;
import javax.swing.*;
import javax.swing.border.TitledBorder;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableRowSorter;

import lombok.AccessLevel;
import lombok.experimental.FieldDefaults;
import org.example.bus.TourBookingBUS;
import org.example.bus.TourPlanBUS;
import org.example.dto.TourBookingDTO;
import org.example.gui.dialog.TourBookingDialog;
import org.example.login.SessionManager;

@FieldDefaults(level = AccessLevel.PRIVATE)
public class TourBookingPanel extends JPanel {
    JButton refreshBtn, editBtn, addBtn, deleteBtn;

    JTextField txtSearch;

    // comboBox
    JComboBox<String> cmbSearchType;

    JPanel northPanel, southPanel, searchPanel;

    JScrollPane scrollPane;
    DefaultTableModel tableModel;
    TableRowSorter<DefaultTableModel> rowSorter;

    JTable table;

    TourBookingBUS tourBookingBus;
    TourBookingDTO tourBookingDTO;
    TourPlanBUS tourPlanBUS;
    TourBookingDialog tourBookingDialog;

    public TourBookingPanel()  {
        tourBookingBus = new TourBookingBUS();
        tourBookingDTO = new TourBookingDTO();
        tourPlanBUS = new TourPlanBUS();

        scrollPane = new JScrollPane();

        initComponents();
        if (!SessionManager.isAdmin()) {
            deleteBtn.setEnabled(false);
        }
        loadTourBookingData(tourBookingBus.getAllTourBooking());
        hasSelectedRow();
    }

    private void initComponents() {
        setLayout(new BorderLayout());

        // northPanel
        northPanel = new JPanel(new BorderLayout());
        JLabel jlbTitle = new JLabel("QUẢN LÝ PHIẾU ĐẶT TOUR", JLabel.CENTER);
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
        cmbSearchType = new JComboBox<>(new String[]{
                " Tất cả", " Tên Tour", " Địa điểm khởi hành"
        });
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
        southPanel.add(addBtn);
        southPanel.add(editBtn);
        southPanel.add(deleteBtn);
        southPanel.add(refreshBtn);

        northPanel.add(searchPanel, BorderLayout.CENTER);


        add(northPanel, BorderLayout.NORTH);
        add(scrollPane, BorderLayout.CENTER);
        add(southPanel, BorderLayout.SOUTH);

        initTable();

    }// </editor-fold>//GEN-END:initComponents
    private void jComboBox2ActionPerformed(ActionEvent evt) {//GEN-FIRST:event_jComboBox2ActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_jComboBox2ActionPerformed

    private JButton createBtn(String text, Color color){
        JButton btn = new JButton(text);
        btn.setBackground(color);
        btn.setForeground(Color.WHITE);
        btn.setFocusPainted(false);
        btn.setFont(new Font("SansSerif", Font.BOLD, 13));
        btn.setCursor(new Cursor(Cursor.HAND_CURSOR)); // in south panel

        btn.setContentAreaFilled(true);
        btn.setOpaque(true);
        btn.setBorderPainted(false);

        return btn;
    }

    public void initTable(){
        String[] columns = {"Mã phiếu đặt", "Mã khách hàng", "Mã kế hoạch tour", "Thời gian đặt", "Số vé", "Đơn giá", "Tổng tiền", "Trạng thái", "Ghi chú"};

        tableModel = new DefaultTableModel(columns, 0);
        table = new JTable(tableModel);
        table.setDefaultEditor(Object.class, null);

        rowSorter = new TableRowSorter<>(tableModel);
        table.setRowSorter(rowSorter);

        scrollPane = new JScrollPane(table);
        scrollPane.setBorder(BorderFactory.createEmptyBorder(0, 10, 0, 10));
    }


    private void setAddBtn(){
        addBtn = createBtn("Thêm", UIColors.ADD);
        addBtn.addActionListener(v -> {
            tourBookingDialog = new TourBookingDialog(null, true, TourBookingDialog.Mode.ADD, tourBookingDTO, tourPlanBUS);
            tourBookingDialog.setVisible(true);

            loadTourBookingData(tourBookingBus.getAllTourBooking());
        });
    }

    private void setDeleteBtn(){
        deleteBtn = createBtn("Xóa", UIColors.DELETE);
        editBtn.setEnabled(false);
        deleteBtn.addActionListener(v -> {
            if (!SessionManager.isAdmin()) {
                JOptionPane.showMessageDialog(this, "Bạn không có quyền xóa dữ liệu.");
                return;
            }
            int i = table.getSelectedRow();
            int result = JOptionPane.showConfirmDialog(this, "Bạn có chắc chắn muốn xóa?", "Xác nhận", JOptionPane.YES_NO_OPTION);
            if (result == JOptionPane.YES_OPTION && i >= 0) {
                String maKHang = (String) table.getValueAt(i, 0);
                String maKHTour = (String) table.getValueAt(i, 3);
                TourBookingBUS tourBookingBus = new TourBookingBUS();
                tourBookingBus.deleteTourBooking(maKHTour, maKHang);
                DefaultTableModel model = (DefaultTableModel) table.getModel();
                model.removeRow(i);
            }
        });
    }

    private void setEditBtn(){
        editBtn = createBtn("Sửa", UIColors.EDIT);
        editBtn.setEnabled(false);
        editBtn.addActionListener(v -> {
            int row = table.getSelectedRow();
            if (row == -1) {
                JOptionPane.showMessageDialog(this, "Vui lòng chọn kế hoạch tour cần sửa");
                return;
            }

            if (row >= 0) {
                String maKHang = (String) table.getValueAt(row, 0);
                String maKHTour = (String) table.getValueAt(row, 3);
                TourBookingDTO khangkht = tourBookingBus.findTourBookingByIdTourPlan(maKHTour);
                if (khangkht != null && khangkht.getIdCustomer().equals(maKHang)) {
                    tourBookingDialog = new TourBookingDialog(null, true, TourBookingDialog.Mode.EDIT, tourBookingDTO, tourPlanBUS);
                    tourBookingDialog.setVisible(true);

                    loadTourBookingData(tourBookingBus.getAllTourBooking());
                }
            }
        });
    }

    private void setRefreshBtn(){
        refreshBtn = createBtn("Làm mới", UIColors.REFRESH);
        refreshBtn.addActionListener(v -> {
            loadTourBookingData(tourBookingBus.getAllTourBooking());
        });
    }

    private String getColumnName(String selected) {
        switch (selected) {
            case "Mã khách hàng":
                return "MaKHang";
            case "Họ":
                return "Ho";
            case "Tên":
                return "Ten";
            case "Mã kế hoạch tour":
                return "MaKHTour";
            case "Giá vé":
                return "GiaVe";
            default:
                return "";
        }
    }

    private void loadTourBookingData(ArrayList<TourBookingDTO> ls) {
        tableModel.setRowCount(0);

        for (TourBookingDTO dto : ls) {
            tableModel.addRow(new Object[]{
                    dto.getIdTourPlan(),
                    dto.getIdCustomer(),
                    dto.getIdTourPlan(),
                    dto.getBookingDate(),
                    dto.getTickets(),
                    dto.getPrice(),
                    dto.getCostTotal(),
                    dto.getBookingStatus(),
                    dto.getNote()
            });
        }
    }

    private void hasSelectedRow(){
        table.getSelectionModel().addListSelectionListener(e ->{
            boolean hasSelected = table.getSelectedRow() != -1;
            refreshBtn.setEnabled(hasSelected);
            editBtn.setEnabled(hasSelected);
            addBtn.setEnabled(hasSelected);
            deleteBtn.setEnabled(hasSelected);
        });
    }

    private void searchByType(){
        String keyWord = txtSearch.getText().trim().toLowerCase();
        String searchType = (String) cmbSearchType.getSelectedItem();

        RowFilter<DefaultTableModel, Object> rf = new RowFilter<DefaultTableModel, Object>() {
            @Override
            public boolean include(Entry<? extends DefaultTableModel, ? extends Object> entry) {
                if(!keyWord.isEmpty()){
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
                    if(!found) return false;
                }

                return true;
            }
        };
        rowSorter.setRowFilter(rf);
    }
}