package org.example.gui.panel;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.MouseEvent;
import java.util.List;
import javax.swing.*;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import javax.swing.table.DefaultTableModel;

import lombok.AccessLevel;
import lombok.experimental.FieldDefaults;
import org.example.bus.PhieuDatTourBUS;
import org.example.dao.PhieuDatTourDAO;
import org.example.dao.KhachHangDAO;
import org.example.dto.PhieuDatTourDTO;
import org.example.dto.KhachHangDTO;
import org.example.gui.dialog.PhieuDatTourDialog;
import org.example.login.SessionManager;

@FieldDefaults(level = AccessLevel.PRIVATE)
public class PhieuDatTourPanel extends JPanel {
    JButton btnLamMoi, btnSua, btnThem, btnXoa;

    JComboBox<String> jComboBox2;

    JLabel jLabel1, jLabel2;

    JPanel jPanel1, jPanel2, jPanel3;

    JScrollPane jScrollPane2;

    JTable jTable2;

    JTextField txtSearch;
    PhieuDatTourDAO ds = new PhieuDatTourDAO();
    KhachHangDAO dsKH = new KhachHangDAO();
    PhieuDatTourBUS khangkhtBUS = new PhieuDatTourBUS();
    PhieuDatTourDialog phieuDatTourDialog;
    SessionManager sessionManager;

    public PhieuDatTourPanel() {
        khangkhtBUS = new PhieuDatTourBUS();
        initComponents();
        if (!sessionManager.isAdmin()) {
            btnXoa.setEnabled(false);
        }
        txtSearch.getDocument().addDocumentListener(new DocumentListener() {

            private void search() {
                String keyword = txtSearch.getText().trim();
                List<PhieuDatTourDTO> list = khangkhtBUS.timKHang_KHTours(getColumnName(jComboBox2.getSelectedItem().toString()), keyword);
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
        loadKHang_KHTourToTable(ds.layDanhSachKHang_KHTour());
    }

    private void initComponents() {
        jPanel1 = new JPanel();
        jLabel1 = new JLabel();
        jPanel2 = new JPanel();
        jLabel2 = new JLabel();
        jComboBox2 = new JComboBox<>();
        txtSearch = new JTextField();
        jScrollPane2 = new JScrollPane();
        jTable2 = new JTable();
        jPanel3 = new JPanel();
        btnThem = new JButton();
        btnXoa = new JButton();
        btnSua = new JButton();
        btnLamMoi = new JButton();

        setLayout(new BorderLayout());

        jPanel1.setLayout(new BorderLayout());

        jLabel1.setFont(new Font("Segoe UI", 1, 24)); // NOI18N
        jLabel1.setHorizontalAlignment(SwingConstants.CENTER);
        jLabel1.setText("Quản lí khách hàng - kế hoạch tour");
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

        jTable2.setModel(new DefaultTableModel(
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
        jScrollPane2.setViewportView(jTable2);
        jScrollPane2.setBorder(BorderFactory.createEmptyBorder(0, 10, 0, 10));
        add(jScrollPane2, BorderLayout.CENTER);

        them();
        jPanel3.add(btnThem);

        xoa();
        jPanel3.add(btnXoa);

        sua();
        jPanel3.add(btnSua);

        lamMoi();
        jPanel3.add(btnLamMoi);

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
        btnThem = createBtn("Thêm", UIColors.ADD);
        btnThem.addActionListener(v -> {
            phieuDatTourDialog = new PhieuDatTourDialog(null, true, ds, PhieuDatTourDialog.Mode.ADD, null);
            phieuDatTourDialog.setVisible(true);

            loadKHang_KHTourToTable(ds.layDanhSachKHang_KHTour());
        });
    }

    private void xoa(){
        btnXoa = createBtn("Xóa", UIColors.DELETE);
        btnSua.setEnabled(false);
        btnXoa.addActionListener(v -> {
            if (!sessionManager.isAdmin()) {
                JOptionPane.showMessageDialog(this, "Bạn không có quyền xóa dữ liệu.");
                return;
            }
            int i = jTable2.getSelectedRow();
            int result = JOptionPane.showConfirmDialog(this, "Bạn có chắc chắn muốn xóa?", "Xác nhận", JOptionPane.YES_NO_OPTION);
            if (result == JOptionPane.YES_OPTION && i >= 0) {
                String maKHang = (String) jTable2.getValueAt(i, 0);
                String maKHTour = (String) jTable2.getValueAt(i, 3);
                PhieuDatTourBUS khangkhtBUS = new PhieuDatTourBUS();
                khangkhtBUS.xoaKHang_KHTour(maKHTour, maKHang);
                DefaultTableModel model = (DefaultTableModel) jTable2.getModel();
                model.removeRow(i);
            }
        });
    }

    private void sua(){
        btnSua = createBtn("Sửa", UIColors.EDIT);
        btnSua.setEnabled(false);
        btnSua.addActionListener(v -> {
            int i = jTable2.getSelectedRow();

            if (i >= 0) {
                String maKHang = (String) jTable2.getValueAt(i, 0);
                String maKHTour = (String) jTable2.getValueAt(i, 3);
                PhieuDatTourDTO khangkht = khangkhtBUS.timKiemKHang_KHTourTheoMaKHTour(maKHTour);
                if (khangkht != null && khangkht.getMaKHang().equals(maKHang)) {
                    phieuDatTourDialog = new PhieuDatTourDialog(null, true, ds, PhieuDatTourDialog.Mode.EDIT, khangkht);
                    phieuDatTourDialog.setVisible(true);

                    loadKHang_KHTourToTable(ds.layDanhSachKHang_KHTour());
                }
            }
        });
    }

    private void lamMoi(){
        btnLamMoi = createBtn("Làm mới", UIColors.REFRESH);
        btnLamMoi.addActionListener(v -> {
            loadKHang_KHTourToTable(ds.layDanhSachKHang_KHTour());
        });
    }

    private void jTable2MouseClicked(MouseEvent evt) {//GEN-FIRST:event_jTable2MouseClicked
        if (sessionManager.isAdmin()) {
            btnXoa.setEnabled(true);
        }
        btnSua.setEnabled(true);
    }//GEN-LAST:event_jTable2MouseClicked

    private void searchKHang_KHTour() {
        String keyword = txtSearch.getText().trim();
        String selected = jComboBox2.getSelectedItem().toString();
        List<PhieuDatTourDTO> list;
        if (keyword.isEmpty()) {
            loadKHang_KHTourToTable(ds.layDanhSachKHang_KHTour());
            return;
        } else {
            String column = getColumnName(selected);
            list = khangkhtBUS.timKHang_KHTours(column, keyword);
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

    private void loadKHang_KHTourToTable(List<PhieuDatTourDTO> khangkhtList) {
        DefaultTableModel model = (DefaultTableModel) jTable2.getModel();
        model.setRowCount(0);
        for (PhieuDatTourDTO kht : khangkhtList) {
            for (KhachHangDTO kh : dsKH.layDanhSachKHang()) {
                if (kht.getMaKHang().equals(kh.getMaKH())) {
                    Object[] row = {kht.getMaKHang(), kh.getHo(), kh.getTen(), kht.getMaKHTour(), kht.getGiaVe()};
                    model.addRow(row);
                    break;
                }
            }
        }
    }
}