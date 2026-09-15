package org.example.gui.panel;

import org.example.bus.NhanVienBUS;
import org.example.dao.NhanVienDAO;
import org.example.dto.NhanVienDTO;
import org.example.gui.dialog.NhanVienDialog;
import org.example.login.PhanQuyen;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.swing.*;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import javax.swing.table.DefaultTableModel;

public class NhanVienPanel extends JPanel {

    NhanVienDAO ds = new NhanVienDAO();
    NhanVienBUS nvBUS = new NhanVienBUS();
    public NhanVienPanel() {
        nvBUS = new NhanVienBUS();
        initComponents();
        if (!PhanQuyen.laQuanLy()) {
            btnThem.setEnabled(false);
            btnXoa.setEnabled(false);
            btnSua.setEnabled(false);
        }
        txtSearch.getDocument().addDocumentListener(new DocumentListener() {

            private void search() {
                String keyword = txtSearch.getText().trim();
                List<NhanVienDTO> list = nvBUS.timNhanVien(getColumnName(jComboBox1.getSelectedItem().toString()), keyword);
                loadNhanVienToTable(list);
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
        loadNhanVienToTable(ds.layDanhSachNV());
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
        jLabel1.setText("Quản lí nhân viên");
        jLabel1.setHorizontalTextPosition(SwingConstants.CENTER);
        jPanel1.add(jLabel1,BorderLayout.CENTER);

        jLabel2.setText("Tìm kiếm");
        jPanel2.add(jLabel2);

        jComboBox1.setModel(new DefaultComboBoxModel<>(new String[] { "Mã nhân viên", "Họ", "Tên", "Chức vụ", "Số điện thoại", "Địa chỉ" }));
        jComboBox1.addActionListener(this::jComboBox1ActionPerformed);
        jPanel2.add(jComboBox1);

        txtSearch.setPreferredSize(new Dimension(360, 22));
        txtSearch.addActionListener(this::txtSearchActionPerformed);
        jPanel2.add(txtSearch);

        jPanel1.add(jPanel2,BorderLayout.PAGE_END);

        add(jPanel1,BorderLayout.PAGE_START);

        jTable1.setModel(new DefaultTableModel(
                new Object [][] {
                        {null, null, null, null, null, null, null},
                        {null, null, null, null, null, null, null},
                        {null, null, null, null, null, null, null},
                        {null, null, null, null, null, null, null},
                        {null, null, null, null, null, null, null}
                },
                new String [] {
                        "Mã nhân viên", "Họ", "Tên", "Chức vụ", "Ngày sinh", "Số điện thoại", "Địa chỉ"
                }
        ) {
            Class[] types = new Class [] {
                    java.lang.String.class, java.lang.String.class, java.lang.String.class, java.lang.String.class, java.lang.String.class, java.lang.String.class, java.lang.String.class
            };

            public Class getColumnClass(int columnIndex) {
                return types [columnIndex];
            }
        });
        jTable1.addMouseListener(new MouseAdapter() {
            public void mouseClicked(MouseEvent evt) {
                jTable1MouseClicked(evt);
            }
        });
        jScrollPane1.setViewportView(jTable1);
        jScrollPane1.setBorder(BorderFactory.createEmptyBorder(0, 10, 0, 10));

        add(jScrollPane1,BorderLayout.CENTER);

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
            if (!PhanQuyen.laQuanLy()) {
                JOptionPane.showMessageDialog(this, "Bạn không có quyền thao tác với nhân viên.");
                return;
            }

            NhanVienDialog dialog = new NhanVienDialog(
                    null,
                    true,
                    ds,
                    NhanVienDialog.Mode.ADD,
                    null
            );

            dialog.setVisible(true);

            loadNhanVienToTable(ds.layDanhSachNV());
        });
    }

    private void sua(){
        btnSua = createBtn("Sửa", UIColors.EDIT);
        btnSua.setEnabled(false);
        btnSua.addActionListener(v -> {
            if (!PhanQuyen.laQuanLy()) {
                JOptionPane.showMessageDialog(this, "Bạn không có quyền thao tác với nhân viên.");
                return;
            }
            int i = jTable1.getSelectedRow();

            if (i >= 0) {
                String maNV = jTable1.getValueAt(i, 0).toString();
                NhanVienDTO nv = nvBUS.timNhanVienTheoMa(maNV);
                System.err.println(nv.getMaNV());
                NhanVienDialog dialog = new NhanVienDialog(
                        null, true, ds,
                        NhanVienDialog.Mode.EDIT,
                        nv);

                dialog.setVisible(true);
                loadNhanVienToTable(ds.layDanhSachNV());
            }
        });
    }

    private void xoa(){
        btnXoa = createBtn("Xóa", UIColors.DELETE);
        btnXoa.setEnabled(false);
        btnXoa.addActionListener(v -> {
            if (!PhanQuyen.laQuanLy()) {
                JOptionPane.showMessageDialog(this, "Bạn không có quyền xóa dữ liệu.");
                return;
            }
            int i = jTable1.getSelectedRow();
            int result = JOptionPane.showConfirmDialog(this, "Bạn có chắc muốn xóa nhân viên này?", "Xác nhận xóa", JOptionPane.YES_NO_OPTION);
            if (result == JOptionPane.YES_OPTION && i >= 0) {
                String maNV = jTable1.getValueAt(i, 0).toString();
                NhanVienBUS nvBUS = new NhanVienBUS();
                nvBUS.xoaNhanVien(maNV);
                DefaultTableModel model = (DefaultTableModel) jTable1.getModel();
                model.removeRow(i);
            }
        });
    }

    private void lamMoi(){
        btnLamMoi = createBtn("Làm mới", UIColors.REFRESH);
        btnLamMoi.addActionListener(v -> {
            loadNhanVienToTable(ds.layDanhSachNV());
        });
    }

    private void jComboBox1ActionPerformed(ActionEvent evt) {//GEN-FIRST:event_jComboBox1ActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_jComboBox1ActionPerformed

    private void txtSearchActionPerformed(ActionEvent evt) {//GEN-FIRST:event_txtSearchActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_txtSearchActionPerformed

    private void jTable1MouseClicked(MouseEvent evt){
        if (PhanQuyen.laQuanLy()) {
            btnXoa.setEnabled(true);
            btnSua.setEnabled(true);
        }
    }

    private String getColumnName(String selected) {
        switch (selected) {
            case "Mã nhân viên":
                return "maNV";
            case "Họ":
                return "ho";
            case "Tên":
                return "ten";
            case "Chức vụ":
                return "chucVu";
            case "Số điện thoại":
                return "sdt";
            case "Địa chỉ":
                return "diaChi";
            default:
                return "maNV";
        }
    }

    private void loadNhanVienToTable(List<NhanVienDTO> list) {
        DefaultTableModel model = (DefaultTableModel) jTable1.getModel();
        model.setRowCount(0); // Xóa dữ liệu cũ trong bảng

        for(NhanVienDTO nv : list){
            Date date = java.sql.Date.valueOf(nv.getNgaySinh());
            String ngaySinhStr = "";
            if(date!=null){
                LocalDate localDate = ((java.sql.Date) date).toLocalDate();
                ngaySinhStr = localDate.format(formatter);
            }

            model.addRow(new Object[]{
                    nv.getMaNV(),
                    nv.getHo(),
                    nv.getTen(),
                    nv.getChucVu(),
                    ngaySinhStr,
                    nv.getSdt(),
                    nv.getDiaChi()
            });
        }
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private JButton btnLamMoi, btnSua, btnThem, btnXoa;

    private JComboBox<String> jComboBox1;

    private JLabel jLabel1, jLabel2;

    private JPanel jPanel1 , jPanel2, jPanel3;

    private JScrollPane jScrollPane1;

    private JTable jTable1;

    private JTextField txtSearch;

    //formatter
    private DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");
}