package org.toursalesmanager.gui.panel;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Date;
import java.util.List;

import lombok.AccessLevel;
import lombok.experimental.FieldDefaults;
import org.toursalesmanager.bus.CustomerBUS;
import org.toursalesmanager.dao.CustomerDAO;
import org.toursalesmanager.dto.CustomerDTO;
import org.toursalesmanager.enums.Permission;
import org.toursalesmanager.gui.component.ButtonFactory;
import org.toursalesmanager.gui.dialog.CustomerDialog;
import org.toursalesmanager.gui.helper.UIColors;
import org.toursalesmanager.gui.helper.UIFontHelper;
import org.toursalesmanager.login.SessionManager;

import javax.swing.*;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import javax.swing.table.DefaultTableModel;

@FieldDefaults(level = AccessLevel.PRIVATE)
public class CustomerPanel extends JPanel {
    JButton refreshBtn, editBtn, addBtn, deleteBtn;

    JComboBox<String> jComboBox1;

    JLabel jLabel1, jLabel2;
    JPanel jPanel1, jPanel2, jPanel3;

    JScrollPane jScrollPane1;

    JTable jTable1;

    JTextField txtSearch;

    CustomerDAO ds = new CustomerDAO();
    CustomerBUS customerBUS = new CustomerBUS();

    public CustomerPanel() {
        customerBUS = new CustomerBUS();

        initComponents();

        txtSearch.getDocument().addDocumentListener(new DocumentListener() {

            private void search() {
                String keyword = txtSearch.getText().trim();
                List<CustomerDTO> list = customerBUS.findCustomer(getColumnName(jComboBox1.getSelectedItem().toString()), keyword);
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
        loadKhachHangToTable(ds.getAllCustomers());
        hasSelectedRow();
    }

    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {
        boolean canManageCustomer = SessionManager.hasPermission(Permission.MANAGE_CUSTOMER);

        jPanel1 = new JPanel();
        jLabel1 = new JLabel();
        jPanel2 = new JPanel();
        jLabel2 = new JLabel();
        jComboBox1 = new JComboBox<>();
        txtSearch = new JTextField();
        jScrollPane1 = new JScrollPane();
        jTable1 = new JTable();
        jPanel3 = new JPanel();
        addBtn = new JButton();
        deleteBtn = new JButton();
        editBtn = new JButton();
        refreshBtn = new JButton();

        setLayout(new BorderLayout());

        jPanel1.setLayout(new BorderLayout());

        jLabel1.setFont(UIFontHelper.PAGE_TITLE); // NOI18N
        jLabel1.setHorizontalAlignment(SwingConstants.CENTER);
        jLabel1.setText(canManageCustomer ? "QUẢN LÝ KHÁCH HÀNG" : "THÔNG TIN KHÁCH HÀNG");
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

        addCustomer();
        addBtn.setVisible(canManageCustomer);
        jPanel3.add(addBtn);

        deleteCustomer();
        deleteBtn.setVisible(canManageCustomer);
        jPanel3.add(deleteBtn);

        updateCustomer();
        editBtn.setVisible(canManageCustomer);
        jPanel3.add(editBtn);

        lamMoi();
        jPanel3.add(refreshBtn);

        jTable1.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                jTable1MouseClicked(e);
            }
        });
        jPanel3.add(editBtn);

        jPanel3.add(refreshBtn);

        add(jPanel3, BorderLayout.PAGE_END);
    }// </editor-fold>//GEN-END:initComponents

    private void addCustomer(){
        addBtn = ButtonFactory.create("Thêm khách hàng", UIColors.ADD);
        addBtn.addActionListener(v -> {
            if (!requireManagePermission()) {
                return;
            }

            CustomerDialog dialog = new CustomerDialog(null, true, ds, CustomerDialog.Mode.ADD, null);
            dialog.setVisible(true);

            loadKhachHangToTable(ds.getAllCustomers());
        });
    }
    private void updateCustomer(){
        editBtn = ButtonFactory.create("Sửa", UIColors.EDIT);
        editBtn.setEnabled(false);
        editBtn.addActionListener(v -> {
            if (!requireManagePermission()) {
                return;
            }

            int i = jTable1.getSelectedRow();

            if (i>=0){
                String idStaff = jTable1.getValueAt(i, 0).toString();
                CustomerDTO kh = customerBUS.findCustomerById(idStaff);
                CustomerDialog dialog = new CustomerDialog(null, true, ds, CustomerDialog.Mode.EDIT, kh);
                dialog.setVisible(true);

                loadKhachHangToTable(ds.getAllCustomers());
            }
        });
    }
    private void deleteCustomer(){
        deleteBtn = ButtonFactory.create("Xóa", UIColors.DELETE);
        deleteBtn.setEnabled(false);
        deleteBtn.addActionListener(v -> {
            if (!requireManagePermission()) {
                return;
            }

            int i = jTable1.getSelectedRow();
            int result = JOptionPane.showConfirmDialog(this, "Bạn có chắc muốn xóa khách hàng này?", "Xác nhận", JOptionPane.YES_NO_OPTION);
            if (result == JOptionPane.YES_OPTION && i >= 0) {
                String idStaff = jTable1.getValueAt(i, 0).toString();
                CustomerBUS bus = new CustomerBUS();

                bus.deleteCustomer(idStaff);
                DefaultTableModel model = (DefaultTableModel) jTable1.getModel();
                model.removeRow(i);
            }
        });
    }
    private void lamMoi(){
        refreshBtn = ButtonFactory.create("Làm mới", UIColors.REFRESH);
        refreshBtn.addActionListener(v -> {
            loadKhachHangToTable(ds.getAllCustomers());
        });
    }

    private void jComboBox1ActionPerformed(ActionEvent evt) {//GEN-FIRST:event_jComboBox1ActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_jComboBox1ActionPerformed

    private void jTable1MouseClicked(MouseEvent evt) {
        if (SessionManager.isAdmin()) {
            deleteBtn.setEnabled(true);
        }
        editBtn.setEnabled(true);
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

    private void loadKhachHangToTable(List<CustomerDTO> list) {
        DefaultTableModel model = (DefaultTableModel) jTable1.getModel();
        model.setRowCount(0); // Xóa dữ liệu cũ trong bảng
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");

        for(CustomerDTO kh : list){
            Date date = java.sql.Date.valueOf(kh.getDob());
            String ngaySinhStr = "";
            if(date!=null){
                LocalDate localDate = ((java.sql.Date) date).toLocalDate();
                ngaySinhStr = localDate.format(formatter);
            }

            model.addRow(new Object[]{
                    kh.getIdCustomer(),
                    kh.getFirstName(),
                    kh.getLastName(),
                    ngaySinhStr,
                    kh.getPhoneNumber(),
                    kh.getAddress()
            });
        }
    }

    private void hasSelectedRow(){
        jTable1.getSelectionModel().addListSelectionListener(e ->{
            boolean hadSelection = jTable1.getSelectedRow() != -1;
            deleteBtn.setEnabled(hadSelection);
            editBtn.setEnabled(hadSelection);
        });
    }

    private boolean requireManagePermission() {
        if (!SessionManager.hasPermission(Permission.MANAGE_CUSTOMER)) {
            JOptionPane.showMessageDialog(this, "Bạn không có quyền quản lý khách hàng.");
            return false;
        }
        return true;
    }
}
