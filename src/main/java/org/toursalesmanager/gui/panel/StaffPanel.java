package org.toursalesmanager.gui.panel;

import lombok.AccessLevel;
import lombok.experimental.FieldDefaults;
import org.toursalesmanager.bus.StaffBUS;
import org.toursalesmanager.dto.StaffDTO;
import org.toursalesmanager.enums.Permission;
import org.toursalesmanager.gui.component.ButtonFactory;
import org.toursalesmanager.gui.dialog.StaffDialog;
import org.toursalesmanager.gui.helper.UIColors;
import org.toursalesmanager.login.SessionManager;

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
import javax.swing.table.DefaultTableModel;

@FieldDefaults(level = AccessLevel.PRIVATE)
public class StaffPanel extends JPanel {
    JButton btnLamMoi, btnSua, btnThem, btnXoa;

    JComboBox<String> jComboBox1;

    JLabel jLabel1, jLabel2;

    JPanel jPanel1 , jPanel2, jPanel3;

    JScrollPane jScrollPane1;

    JTable jTable1;

    JTextField txtSearch;

    //formatter
    DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");

    StaffBUS staffBus = new StaffBUS();

    public StaffPanel() {
        staffBus = new StaffBUS();
        initComponents();
        applyPermission();
        reloadStaffTable();
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
        txtSearch.addActionListener(e -> search());

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

    private void them(){
        btnThem = ButtonFactory.create("Thêm", UIColors.ADD);
        btnThem.addActionListener(v -> {
            if (!SessionManager.isAdmin()) {
                JOptionPane.showMessageDialog(this, "Bạn không có quyền thao tác với nhân viên.");
                return;
            }

            StaffDialog dialog = new StaffDialog(
                    null,
                    true,
                    StaffDialog.Mode.ADD,
                    null
            );

            dialog.setVisible(true);
            reloadStaffTable();
        });
    }

    private void sua(){
        btnSua = ButtonFactory.create("Sửa", UIColors.EDIT);
        btnSua.setEnabled(false);
        btnSua.addActionListener(v -> {
            if (!SessionManager.isAdmin()) {
                JOptionPane.showMessageDialog(this, "Bạn không có quyền thao tác với nhân viên.");
                return;
            }
            int i = jTable1.getSelectedRow();

            if (i >= 0) {
                String maNV = jTable1.getValueAt(i, 0).toString();
                StaffDTO nv = staffBus.timNhanVienTheoMa(maNV);
                System.err.println(nv.getIdStaff());
                StaffDialog dialog = new StaffDialog(
                        null, true,
                        StaffDialog.Mode.EDIT,
                        nv);

                dialog.setVisible(true);
                reloadStaffTable();
            }
        });
    }

    private void xoa(){
        btnXoa = ButtonFactory.create("Xóa", UIColors.DELETE);
        btnXoa.setEnabled(false);
        btnXoa.addActionListener(v -> {
            if (!SessionManager.isAdmin()) {
                JOptionPane.showMessageDialog(this, "Bạn không có quyền xóa dữ liệu.");
                return;
            }
            int i = jTable1.getSelectedRow();
            int result = JOptionPane.showConfirmDialog(this, "Bạn có chắc muốn xóa nhân viên này?", "Xác nhận xóa", JOptionPane.YES_NO_OPTION);
            if (result == JOptionPane.YES_OPTION && i >= 0) {
                String maNV = jTable1.getValueAt(i, 0).toString();
                StaffBUS staffBus = new StaffBUS();
                staffBus.deleteStaff(maNV);
                DefaultTableModel model = (DefaultTableModel) jTable1.getModel();
                model.removeRow(i);
            }
        });
    }

    private void lamMoi(){
        btnLamMoi = ButtonFactory.create("Làm mới", UIColors.REFRESH);
        btnLamMoi.addActionListener(v -> {
            reloadStaffTable();
        });
    }

    private void jComboBox1ActionPerformed(ActionEvent evt) {//GEN-FIRST:event_jComboBox1ActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_jComboBox1ActionPerformed

    private void txtSearchActionPerformed(ActionEvent evt) {//GEN-FIRST:event_txtSearchActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_txtSearchActionPerformed

    private void jTable1MouseClicked(MouseEvent evt){
        if (SessionManager.isAdmin()) {
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

    private void loadNhanVienToTable(List<StaffDTO> ls) {
        DefaultTableModel model = (DefaultTableModel) jTable1.getModel();
        model.setRowCount(0); // Xóa dữ liệu cũ trong bảng

        for(StaffDTO item : ls){

            Date date = java.sql.Date.valueOf(item.getDob());

            String ngaySinhStr = "";
            if(date!=null){
                LocalDate localDate = ((java.sql.Date) date).toLocalDate();
                ngaySinhStr = localDate.format(formatter);
            }

            String roleText =
                    item.getRole()== null
                            ? "Chưa có tài khoản"
                            : item.getRole().toString();

            model.addRow(new Object[]{
                    item.getIdStaff(),
                    item.getFirstName(),
                    item.getLastName(),
                    roleText,
                    ngaySinhStr,
                    item.getPhoneNumber(),
                    item.getAddress()
            });
        }
    }

    private void applyPermission(){
        boolean canManage = SessionManager.hasPermission(Permission.MANAGE_STAFF);

        btnThem.setVisible(canManage);
        btnSua.setVisible(canManage);
        btnXoa.setVisible(canManage);
    }

    private void reloadStaffTable(){
        ArrayList<StaffDTO> ls =
                staffBus.getAllStaffWithRole();

        loadNhanVienToTable(ls);
    }

    private void search() {
        String keyword = txtSearch.getText().trim();
        String searchType =
                String.valueOf(jComboBox1.getSelectedItem());

        List<StaffDTO> ls;

        if (keyword.isBlank()) {
            ls = staffBus.getAllStaffWithRole();
        } else {
            ls = staffBus.timNhanVien(searchType, keyword);
        }

        loadNhanVienToTable(ls);
    }
}