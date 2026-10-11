package org.toursalesmanager.gui.panel;

import java.awt.*;
import java.awt.event.*;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.util.ArrayList;

import com.toedter.calendar.JDateChooser;
import lombok.AccessLevel;
import lombok.experimental.FieldDefaults;
import org.toursalesmanager.enums.Permission;
import org.toursalesmanager.gui.component.ButtonFactory;
import org.toursalesmanager.gui.dialog.LocationDialog;
import org.toursalesmanager.bus.*;
import org.toursalesmanager.dto.*;
import org.toursalesmanager.gui.helper.UIColors;
import org.toursalesmanager.login.SessionManager;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;

@FieldDefaults(level = AccessLevel.PRIVATE)
public class LocationPanel extends JPanel {
    // define variables
    JButton resetBtn, editBtn, addBtn, deleteBtn;

    JComboBox<String> cbtim;

    JScrollPane jScrollPane2;

    JLabel lbname, lbtim;

    JPanel pnlfooter, pnlheader, pnltable, pnlsearch;

    JTable tbldd;

    JDateChooser txtdate;

    JTextField txttendd;

    DefaultTableModel model;
    LocationBUS bus;

    public LocationPanel() {
        initComponents();
        bus=new LocationBUS();
        txtdate.setVisible(false);
        model = (DefaultTableModel) tbldd.getModel();
        loadData();
    }

    private void loadData(java.util.Date ngay){
        model.setRowCount(0);

        ArrayList<LocationDTO> ds =bus.getDstheongay(ngay);
        if(ds==null) return;
        for(LocationDTO dd: ds){
            model.addRow(new Object[]{
                    dd.getIdLocation(), dd.getLocationName(), dd.getAddress(),dd.getNation()});
        }
    }
    private void loadData(){
        model.setRowCount(0);

        ArrayList<LocationDTO> ds = bus.getAllLocations();
        if(ds==null) return;
        for(LocationDTO dd: ds){
            model.addRow(new Object[]{
                    dd.getIdLocation() ,dd.getLocationName(),dd.getAddress(),dd.getNation()});
        }
    }

    private void loadData(String ma){
        model.setRowCount(0);

        ArrayList<LocationDTO> ds = bus.getAllLocations();
        if(ds==null) return;
        for(LocationDTO dd: ds){
            model.addRow(new Object[]{
                    dd.getIdLocation(), dd.getLocationName(),dd.getAddress(),dd.getNation()});
        }
    }

    private void initComponents() {
        boolean canManageLocation = requireManagePermission();

        pnlheader = new JPanel();
        lbname = new JLabel();
        pnlsearch = new JPanel();
        lbtim = new JLabel();
        cbtim = new JComboBox<>();
        txttendd = new JTextField();
        txtdate = new com.toedter.calendar.JDateChooser();
        pnlfooter = new JPanel();
        addBtn = new JButton();
        deleteBtn = new JButton();
        editBtn = new JButton();
        resetBtn = new JButton();
        pnltable = new JPanel();
        jScrollPane2 = new JScrollPane();
        tbldd = new JTable();

        setLayout(new BorderLayout());

        pnlheader.setLayout(new BorderLayout());

        lbname.setFont(new Font("Arial", 0, 18)); // NOI18N
        lbname.setHorizontalAlignment(SwingConstants.CENTER);
        lbname.setText("Quản lý địa điểm");
        pnlheader.add(lbname, BorderLayout.CENTER);

        lbtim.setText("Tìm theo:");
        pnlsearch.add(lbtim);

        cbtim.setModel(new DefaultComboBoxModel<>(new String[] { "Tên địa điểm", "Địa chỉ", "Quốc gia" }));
        cbtim.addItemListener(new ItemListener() {
            public void itemStateChanged(ItemEvent evt) {
                cbTimItemStateChanged(evt);
            }
        });
        pnlsearch.add(cbtim);

        txttendd.setColumns(20);
        txttendd.addKeyListener(new KeyAdapter() {
            public void keyReleased(KeyEvent evt) {
                txtTenDdKeyReleased(evt);
            }
        });
        pnlsearch.add(txttendd);

        txtdate.addPropertyChangeListener(new PropertyChangeListener() {
            public void propertyChange(PropertyChangeEvent evt) {
                txtDatePropertyChange(evt);
            }
        });
        pnlsearch.add(txtdate);

        pnlheader.add(pnlsearch, BorderLayout.PAGE_END);

        add(pnlheader, BorderLayout.PAGE_START);

        addLocation();
        addBtn.setVisible(canManageLocation);
        pnlfooter.add(addBtn);

        deleteLocation();
        deleteBtn.setVisible(canManageLocation);
        pnlfooter.add(deleteBtn);

        updateLocation();
        editBtn.setVisible(canManageLocation);
        pnlfooter.add(editBtn);

        refresh();
        pnlfooter.add(resetBtn);

        add(pnlfooter, BorderLayout.PAGE_END);

        pnltable.setBorder(BorderFactory.createEmptyBorder(1, 10, 1, 10));
        pnltable.setLayout(new BorderLayout());

        tbldd.setModel(new DefaultTableModel(
                new Object [][] {
                        {null, null, null},
                        {null, null, null},
                        {null, null, null},
                        {null, null, null}
                },
                new String [] {
                        "Mã địa điểm" ,"Tên địa điểm", "Địa chỉ", "Quốc gia"
                }
        ));
        tbldd.setPreferredSize(null);
        tbldd.addMouseListener(new MouseAdapter() { // when click row of table
            @Override
            public void mouseClicked(MouseEvent e) {
                if (tbldd.getSelectedRow() != -1) {
                    deleteBtn.setEnabled(true);
                    editBtn.setEnabled(true);
                }
            }
        });
        jScrollPane2.setViewportView(tbldd);

        pnltable.add(jScrollPane2, BorderLayout.CENTER);

        add(pnltable, BorderLayout.CENTER);
    }// </editor-fold>//GEN-END:initComponents

    private void addLocation(){
        addBtn = ButtonFactory.create("Thêm", UIColors.ADD);
        addBtn.addActionListener(v -> {
            if (!requireManagePermission()) {
                JOptionPane.showMessageDialog(this, "Bạn không có quyền thêm địa điểm.");
                return;
            }

            LocationDialog ddd=new LocationDialog();
            ddd.setModal(true);
            ddd.setVisible(true);
            loadData();
        });
    }

    private void updateLocation(){
        editBtn = ButtonFactory.create("Sửa", UIColors.EDIT);
        editBtn.setEnabled(false);
        editBtn.addActionListener(v -> {
            if(!requireManagePermission()) {
                JOptionPane.showMessageDialog(this, "Bạn không có quyền sửa địa điểm.");
                return;
            }

            int row=tbldd.getSelectedRow();
            if(row!=-1){
                String maDiaDiem = tbldd.getValueAt(row, 0).toString().trim();
                LocationDTO dd =bus.timDiaDiemTheoMa(maDiaDiem);

                LocationDialog ddd=new LocationDialog(dd);
                ddd.setModal(true);
                ddd.setVisible(true);

                loadData();
            }
        });
    }

    private void deleteLocation(){
        deleteBtn = ButtonFactory.create("Xóa", UIColors.DELETE);
        deleteBtn.setEnabled(false);
        deleteBtn.addActionListener(v -> {
            if (!requireManagePermission()) {
                JOptionPane.showMessageDialog(this, "Bạn không có quyền xóa địa điểm.");
                return;
            }

            int row=tbldd.getSelectedRow();
            if(row != -1){
                String maDiaDiem =model.getValueAt(row, 0).toString();
                LocationDTO dd=bus.timDiaDiemTheoMa(maDiaDiem); if(dd==null){
                    JOptionPane.showMessageDialog(this, "Lỗi không tìm thấy");
                    return;
                }
                if(JOptionPane.showConfirmDialog(this, "Xóa địa điểm?","Xác nhận",JOptionPane.YES_NO_OPTION)==JOptionPane.YES_OPTION){
                    if(bus.xoaDiaDiem(dd)){
                        JOptionPane.showMessageDialog(this, "Xóa thành công");
                        loadData();
                    }else{
                        JOptionPane.showMessageDialog(this, "Xóa thất bại");
                    }
                }
                loadData();
            }else{ // row == -1
                JOptionPane.showMessageDialog(this, "Vui lòng chọn địa điểm cần xóa");
            }
        });
    }

    private void refresh(){
        resetBtn = ButtonFactory.create("Làm mới", UIColors.REFRESH);
        resetBtn.addActionListener(v -> {
            loadData();
        });
    }

    private void txtTenDdKeyReleased(KeyEvent evt) {
        // TODO add your handling code here:
        String ten = txttendd.getText().trim();
        loadData( ten);
    }

    private void cbTimItemStateChanged(ItemEvent evt) {
        // TODO add your handling code here:
        String chose=cbtim.getSelectedItem().toString().trim();
        if(chose.equals("Ngày thực hiện")){
            txttendd.setVisible(false);
            txtdate.setVisible(true);
        }
        else{
            txttendd.setVisible(true);
            txtdate.setVisible(false);
        }
    }

    private void txtDatePropertyChange(PropertyChangeEvent evt) {
        // TODO add your handling code here:
        if ("date".equals(evt.getPropertyName())) {
            java.util.Date ngay = txtdate.getDate();
            if (ngay != null) {
                loadData(ngay);
            } else {
                loadData();
            }
        }
    }

    private boolean requireManagePermission() {
        return SessionManager.hasPermission(Permission.MANAGE_LOCATION);
    }
}