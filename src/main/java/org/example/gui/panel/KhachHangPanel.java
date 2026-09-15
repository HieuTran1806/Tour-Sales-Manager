package org.example.gui.panel;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Date;
import java.util.List;
import org.example.bus.KhachHangBUS;
import org.example.dao.KhachHangDAO;
import org.example.dto.KhachHangDTO;
import org.example.gui.dialog.KhachHangDialog;
import org.example.login.PhanQuyen;

import javax.swing.*;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import javax.swing.table.DefaultTableModel;

public class KhachHangPanel extends JPanel {

    KhachHangDAO ds = new KhachHangDAO();
    KhachHangBUS khachHangBUS = new KhachHangBUS();
    KhachHangDialog KHdialog;
    public KhachHangPanel() {
        khachHangBUS = new KhachHangBUS();
        initComponents();
        if (!PhanQuyen.laQuanLy()) {
            btnXoa.setEnabled(false);
        }
        txtSearch.getDocument().addDocumentListener(new DocumentListener() {

            private void search() {
                String keyword = txtSearch.getText().trim();
                List<KhachHangDTO> list = khachHangBUS.timKhachHang(getColumnName(jComboBox1.getSelectedItem().toString()), keyword);
                loadKhachHangToTable(list);
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
        loadKhachHangToTable(ds.layDanhSachKHang());
    }

    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jPanel1 = new JPanel();
        jLabel1 = new JLabel();
        jPanel2 = new JPanel();
        jLabel2 = new JLabel();
        jComboBox1 = new JComboBox<>();
        txtSearch = new JTextField();
        jScrollPane1 = new JScrollPane();
        jTable1 = new JTable();
        jPanel3 = new JPanel();
        btnThem = new JButton();
        btnXoa = new JButton();
        btnSua = new JButton();
        btnLamMoi = new JButton();

        setLayout(new BorderLayout());

        jPanel1.setLayout(new BorderLayout());

        jLabel1.setFont(new Font("Segoe UI", 1, 24)); // NOI18N
        jLabel1.setHorizontalAlignment(SwingConstants.CENTER);
        jLabel1.setText("Quản lí khách hàng");
        jLabel1.setHorizontalTextPosition(SwingConstants.CENTER);
        jPanel1.add(jLabel1, BorderLayout.CENTER);

        jLabel2.setText("Tìm kiếm");
        jPanel2.add(jLabel2);

        jComboBox1.setModel(new DefaultComboBoxModel<>(new String[] { "Mã khách hàng", "Họ", "Tên", "Số điện thoại", "Địa chỉ" }));
        jComboBox1.addActionListener(this::jComboBox1ActionPerformed);
        jPanel2.add(jComboBox1);

        txtSearch.setPreferredSize(new Dimension(360, 22));
        jPanel2.add(txtSearch);

        jPanel1.add(jPanel2, BorderLayout.PAGE_END);

        add(jPanel1, BorderLayout.PAGE_START);

        jTable1.setModel(new DefaultTableModel(
                new Object [][] {
                        {null, null, null, null, null, null},
                        {null, null, null, null, null, null},
                        {null, null, null, null, null, null},
                        {null, null, null, null, null, null},
                        {null, null, null, null, null, null}
                },
                new String [] {
                        "Mã khách hàng", "Họ", "Tên", "Ngày sinh", "Số điện thoại", "Địa chỉ"
                }
        ) {
            Class[] types = new Class [] {
                    java.lang.String.class, java.lang.String.class, java.lang.String.class, java.lang.String.class, java.lang.String.class, java.lang.String.class
            };

            public Class getColumnClass(int columnIndex) {
                return types [columnIndex];
            }
        });
        jScrollPane1.setViewportView(jTable1);
        jScrollPane1.setBorder(BorderFactory.createEmptyBorder(0, 10, 0, 10));
        add(jScrollPane1, BorderLayout.CENTER);

        them();
        jPanel3.add(btnThem);

        xoa();
        btnXoa.setEnabled(false);
        jPanel3.add(btnXoa);

        sua();
        btnSua.setEnabled(false);
        jPanel3.add(btnSua);

        lamMoi();
        jPanel3.add(btnLamMoi);

        jTable1.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                jTable1MouseClicked(e);
            }
        });
        jPanel3.add(btnSua);

        btnLamMoi.setText("Làm mới");
        jPanel3.add(btnLamMoi);

        add(jPanel3, BorderLayout.PAGE_END);
    }// </editor-fold>//GEN-END:initComponents

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
            KhachHangDialog dialog = new KhachHangDialog(null, true, ds, KhachHangDialog.Mode.ADD, null);
            dialog.setVisible(true);

            loadKhachHangToTable(ds.layDanhSachKHang());
        });
    }
    private void sua(){
        btnSua = createBtn("Sửa", UIColors.EDIT);
        btnSua.setEnabled(false);
        btnSua.addActionListener(v -> {
            int i = jTable1.getSelectedRow();

            if (i>=0){
                String maKH = jTable1.getValueAt(i, 0).toString();
                KhachHangDTO kh = khachHangBUS.timKiemKH(maKH);
                KhachHangDialog dialog = new KhachHangDialog(null, true, ds, KhachHangDialog.Mode.EDIT, kh);
                dialog.setVisible(true);

                loadKhachHangToTable(ds.layDanhSachKHang());
            }
        });
    }
    private void xoa(){
        btnXoa = createBtn("Xóa", UIColors.DELETE);
        btnSua.setEnabled(false);
        btnXoa.addActionListener(v -> {
            if (!PhanQuyen.laQuanLy()) {
                JOptionPane.showMessageDialog(this, "Bạn không có quyền xóa dữ liệu.");
                return;
            }
            int i = jTable1.getSelectedRow();
            int result = JOptionPane.showConfirmDialog(this, "Bạn có chắc muốn xóa khách hàng này?", "Xác nhận", JOptionPane.YES_NO_OPTION);
            if (result == JOptionPane.YES_OPTION && i >= 0) {
                String maKH = jTable1.getValueAt(i, 0).toString();
                KhachHangBUS khbus = new KhachHangBUS();
                khbus.xoaKhachHang(maKH);
                DefaultTableModel model = (DefaultTableModel) jTable1.getModel();
                model.removeRow(i);
            }
        });
    }
    private void lamMoi(){
        btnLamMoi = createBtn("Làm mới", UIColors.REFRESH);
        btnLamMoi.addActionListener(v -> {
            loadKhachHangToTable(ds.layDanhSachKHang());
        });
    }

    private void jComboBox1ActionPerformed(ActionEvent evt) {//GEN-FIRST:event_jComboBox1ActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_jComboBox1ActionPerformed

    private void jTable1MouseClicked(MouseEvent evt) {
        if (PhanQuyen.laQuanLy()) {
            btnXoa.setEnabled(true);
        }
        btnSua.setEnabled(true);
    }

    private String getColumnName(String selected) {
        switch (selected) {
            case "Mã khách hàng":
                return "MaKH";
            case "Họ":
                return "Ho";
            case "Tên":
                return "Ten";
            case "Số điện thoại":
                return "Sdt";
            case "Địa chỉ":
                return "DiaChi";
            default:
                return "MaKH";
        }
    }

    private void loadKhachHangToTable(List<KhachHangDTO> list) {
        DefaultTableModel model = (DefaultTableModel) jTable1.getModel();
        model.setRowCount(0); // Xóa dữ liệu cũ trong bảng
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");

        for(KhachHangDTO kh : list){
            Date date = java.sql.Date.valueOf(kh.getNgaySinh());
            String ngaySinhStr = "";
            if(date!=null){
                LocalDate localDate = ((java.sql.Date) date).toLocalDate();
                ngaySinhStr = localDate.format(formatter);
            }

            model.addRow(new Object[]{
                    kh.getMaKH(),
                    kh.getHo(),
                    kh.getTen(),
                    ngaySinhStr,
                    kh.getSdt(),
                    kh.getDiaChi()
            });
        }
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private JButton btnLamMoi, btnSua, btnThem, btnXoa;

    private JComboBox<String> jComboBox1;

    private JLabel jLabel1, jLabel2;
    private JPanel jPanel1, jPanel2, jPanel3;

    private JScrollPane jScrollPane1;

    private JTable jTable1;

    private JTextField txtSearch;
}
