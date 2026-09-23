package org.example.gui.panel;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.util.List;
import javax.swing.*;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import javax.swing.table.DefaultTableModel;

import lombok.AccessLevel;
import lombok.experimental.FieldDefaults;
import org.example.bus.TourBookingBUS;
import org.example.dao.TourBookingDAO;
import org.example.dao.KhachHangDAO;
import org.example.dto.TourBookingDTO;
import org.example.dto.KhachHangDTO;
import org.example.gui.dialog.TourBookingDialog;
import org.example.login.SessionManager;

@FieldDefaults(level = AccessLevel.PRIVATE)
public class TourBookingPanel extends JPanel {
    JButton refreshBtn, editBtn, addBtn, deleteBtn;

    JComboBox<String> jComboBox2;

    JLabel jLabel1, jLabel2;

    JPanel jPanel1, jPanel2, jPanel3;

    JScrollPane jScrollPane2;

    JTable table;

    JTextField txtSearch;
    TourBookingDAO ds = new TourBookingDAO();
    KhachHangDAO dsKH = new KhachHangDAO();
    TourBookingBUS tourBookingBus;
    TourBookingDialog tourBookingDialog;

    public TourBookingPanel() {
        tourBookingBus = new TourBookingBUS();
        initComponents();
        if (!SessionManager.isAdmin()) {
            deleteBtn.setEnabled(false);
        }
        txtSearch.getDocument().addDocumentListener(new DocumentListener() {

            private void search() {
                String keyword = txtSearch.getText().trim();
                List<TourBookingDTO> list = tourBookingBus.timKHang_KHTours(getColumnName(jComboBox2.getSelectedItem().toString()), keyword);
                loadKHang_KHTourToTable(list);
            }

            @Override
            public void insertUpdate(DocumentEvent e) {
                search();
            }

            @Override
            public void removeUpdate(DocumentEvent e) {
                search();
            }

            @Override
            public void changedUpdate(DocumentEvent e) {
                search();
            }
        });
        loadKHang_KHTourToTable(ds.getAllTourBooking());
        hasSelectedRow();
    }

    private void initComponents() {
        jPanel1 = new JPanel();
        jLabel1 = new JLabel();
        jPanel2 = new JPanel();
        jLabel2 = new JLabel();
        jComboBox2 = new JComboBox<>();
        txtSearch = new JTextField();
        jScrollPane2 = new JScrollPane();
        table = new JTable();
        jPanel3 = new JPanel();
        addBtn = new JButton();
        deleteBtn = new JButton();
        editBtn = new JButton();
        refreshBtn = new JButton();

        setLayout(new BorderLayout());

        jPanel1.setLayout(new BorderLayout());

        jLabel1.setFont(new Font("Segoe UI", 1, 24)); // NOI18N
        jLabel1.setHorizontalAlignment(SwingConstants.CENTER);
        jLabel1.setText("QUẢN LÝ PHIẾU ĐẶT TOUR");
        jLabel1.setHorizontalTextPosition(SwingConstants.CENTER);
        jPanel1.add(jLabel1, BorderLayout.CENTER);

        jLabel2.setText("Tìm kiếm");
        jPanel2.add(jLabel2);

        jComboBox2.setModel(new DefaultComboBoxModel<>(new String[] { "Mã khách hàng", "Họ", "Tên", "Mã kế hoạch tour", "Giá vé" }));
        jComboBox2.addActionListener(this::jComboBox2ActionPerformed);
        jPanel2.add(jComboBox2);

        txtSearch.setPreferredSize(new Dimension(360, 22));
        jPanel2.add(txtSearch);

        jPanel1.add(jPanel2, BorderLayout.PAGE_END);

        add(jPanel1, BorderLayout.PAGE_START);

        table.setModel(new DefaultTableModel(
                new Object [][] {
                        {null, null, null, null, null},
                        {null, null, null, null, null},
                        {null, null, null, null, null},
                        {null, null, null, null, null},
                        {null, null, null, null, null}
                },
                new String [] {
                        "Mã khách hàng", "Họ", "Tên khách hàng", "Mã kế hoạch tour", "Giá vé"
                }
        ) {
            Class[] types = new Class [] {
                    String.class, String.class, String.class, String.class, Long.class
            };

            public Class getColumnClass(int columnIndex) {
                return types [columnIndex];
            }
        });
        jScrollPane2.setViewportView(table);
        jScrollPane2.setBorder(BorderFactory.createEmptyBorder(0, 10, 0, 10));
        add(jScrollPane2, BorderLayout.CENTER);

        them();
        jPanel3.add(addBtn);

        xoa();
        jPanel3.add(deleteBtn);

        sua();
        jPanel3.add(editBtn);

        lamMoi();
        jPanel3.add(refreshBtn);

        add(jPanel3, BorderLayout.PAGE_END);
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

    private void them(){
        addBtn = createBtn("Thêm", UIColors.ADD);
        addBtn.addActionListener(v -> {
            tourBookingDialog = new TourBookingDialog(null, true, ds, TourBookingDialog.Mode.ADD, null);
            tourBookingDialog.setVisible(true);

            loadKHang_KHTourToTable(ds.getAllTourBooking());
        });
    }

    private void xoa(){
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

    private void sua(){
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
                    tourBookingDialog = new TourBookingDialog(null, true, ds, TourBookingDialog.Mode.EDIT, khangkht);
                    tourBookingDialog.setVisible(true);

                    loadKHang_KHTourToTable(ds.getAllTourBooking());
                }
                System.out.println(khangkht);
                System.out.println(khangkht.getIdCustomer());
                System.out.println(maKHang);
            }
        });
    }

    private void lamMoi(){
        refreshBtn = createBtn("Làm mới", UIColors.REFRESH);
        refreshBtn.addActionListener(v -> {
            loadKHang_KHTourToTable(ds.getAllTourBooking());
        });
    }

    private void searchKHang_KHTour() {
        String keyword = txtSearch.getText().trim();
        String selected = jComboBox2.getSelectedItem().toString();
        List<TourBookingDTO> list;
        if (keyword.isEmpty()) {
            loadKHang_KHTourToTable(ds.getAllTourBooking());
            return;
        } else {
            String column = getColumnName(selected);
            list = tourBookingBus.timKHang_KHTours(column, keyword);
        }
        loadKHang_KHTourToTable(list);
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

    private void loadKHang_KHTourToTable(List<TourBookingDTO> khangkhtList) {
        DefaultTableModel model = (DefaultTableModel) table.getModel();
        model.setRowCount(0);
        for (TourBookingDTO kht : khangkhtList) {
            for (KhachHangDTO kh : dsKH.layDanhSachKHang()) {
                if (kht.getIdCustomer().equals(kh.getMaKH())) {
                    Object[] row = {kht.getIdCustomer(), kh.getHo(), kh.getTen(), kht.getMaKHTour(), kht.getGiaVe()};
                    model.addRow(row);
                    break;
                }
            }
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
}