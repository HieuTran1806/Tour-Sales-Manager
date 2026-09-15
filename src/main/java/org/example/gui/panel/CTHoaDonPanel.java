
package org.example.gui.panel;

import org.example.gui.dialog.CTHoaDonDialog;

import java.awt.*;
import java.awt.event.*;
import java.util.ArrayList;
import org.example.dto.*;
import org.example.bus.*;
import org.example.gui.helper.ExcelHelper;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;

public class CTHoaDonPanel extends JPanel {
    private DefaultTableModel model;
    private CTHoaDonBUS bus;
    private int soluongcanxoa=0;

    public CTHoaDonPanel() {
        initComponents();
        bus=new CTHoaDonBUS();
        bus.docDs();
        model=(DefaultTableModel) tblcthd.getModel();
        tblcthd.isCellEditable(ERROR, WIDTH);
        loaddata();
    }

    public CTHoaDonPanel(String mahd) {
        initComponents();
        bus=new CTHoaDonBUS();
        lbname.setText("Chi tiết hóa đơn của hóa đơn: "+mahd);
        model=(DefaultTableModel) tblcthd.getModel();
        txttim.setVisible(false);
        cbtim.setVisible(false);
        btnthem.setVisible(false);
        btnxoa.setVisible(false);
        btnxuat.setVisible(false);
        btnreset.setVisible(false);
        loadchitiet(mahd);
    }

    public CTHoaDonPanel(String mahd,int vedu) {
        initComponents();
        bus=new CTHoaDonBUS();
        lbname.setText("Chi tiết hóa đơn của hóa đơn: "+mahd);
        model=(DefaultTableModel) tblcthd.getModel();
        txttim.setVisible(false);
        cbtim.setVisible(false);
        btnthem.setVisible(false);
        btnxoa.setVisible(false);
        btnxuat.setVisible(false);
        btnsua.setVisible(false);
        btnreset.setVisible(false);

        if(vedu>0){
            this.soluongcanxoa=vedu;
            btnxoa.setVisible(true);
            lbname.setText("Cần xóa thêm "+vedu+" VÉ");
        }else{
            btnxoa.setVisible(true);
        }
        loadchitiet(mahd);
    }

    private void loaddata(){
        model.setRowCount(0);
        DefaultTableModel model=(DefaultTableModel) tblcthd.getModel();
        ArrayList<CTietHDDTO> ds= CTHoaDonBUS.getDs();
        for(CTietHDDTO cthd:ds){
            model.addRow(new Object[]{
                    cthd.getMaHD(),cthd.getMaKHDi(),String.format("%.0f", cthd.getGiaVe())
            });
        }

    }

    private void loaddata(ArrayList<CTietHDDTO> ds){
        model.setRowCount(0);
        String loai =cbtim.getSelectedItem().toString();
        String key=txttim.getText().toString().trim();
        ds= bus.timNangcao(loai, key);
        for(CTietHDDTO cthd:ds){
            model.addRow(new Object[]{
                    cthd.getMaHD(),cthd.getMaKHDi(),String.format("%.0f", cthd.getGiaVe())
            });
        }
    }

    private void loadchitiet(String mahd){
        DefaultTableModel model=(DefaultTableModel) tblcthd.getModel();
        this.model.setRowCount(0);

        try{
            CTHoaDonBUS bus=new CTHoaDonBUS();
            ArrayList<CTietHDDTO> ds=bus.getDstheoma(mahd);
            if(ds!=null){
                for(CTietHDDTO cthd:ds){
                    model.addRow(new Object[]{
                            cthd.getMaHD(),cthd.getMaKHDi(),String.format("%.0f", cthd.getGiaVe())
                    });
                }
            }
        }catch(Exception e){
            e.printStackTrace();
            JOptionPane.showMessageDialog(this, "Loi");
        }
    }

    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        pnlheader = new JPanel();
        lbname = new JLabel();
        pnlsearch = new JPanel();
        lbtim = new JLabel();
        cbtim = new JComboBox<>();
        txttim = new JTextField();
        pnltable = new JPanel();
        btnthem = new JButton();
        btnxoa = new JButton();
        btnsua = new JButton();
        btnreset = new JButton();
        btnxuat = new JButton();
        pnlfooter = new JPanel();
        jScrollPane1 = new JScrollPane();
        tblcthd = new JTable();

        setLayout(new java.awt.BorderLayout());

        pnlheader.setLayout(new java.awt.BorderLayout());

        lbname.setFont(new java.awt.Font("Arial", 0, 18)); // NOI18N
        lbname.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        lbname.setText("Quản lý Chi tiết hóa đơn");
        pnlheader.add(lbname, java.awt.BorderLayout.CENTER);

        lbtim.setText("Tìm theo:");
        pnlsearch.add(lbtim);

        cbtim.setModel(new DefaultComboBoxModel<>(new String[] { "Mã hóa đơn", "Mã khách hàng" }));
        pnlsearch.add(cbtim);

        txttim.setColumns(20);
        txttim.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent evt) {
                txttimActionPerformed(evt);
            }
        });
        txttim.addKeyListener(new KeyAdapter() {
            public void keyReleased(KeyEvent evt) {
                txttimKeyReleased(evt);
            }
        });
        pnlsearch.add(txttim);

        pnlheader.add(pnlsearch, java.awt.BorderLayout.PAGE_END);

        add(pnlheader, java.awt.BorderLayout.PAGE_START);

        them();
        pnltable.add(btnthem);

        xoa();
        pnltable.add(btnxoa);

        sua();
        pnltable.add(btnsua);

        lamMoi();
        pnltable.add(btnreset);

        xuatExcel();
        pnltable.add(btnxuat);

        add(pnltable, java.awt.BorderLayout.PAGE_END);

        pnlfooter.setBorder(javax.swing.BorderFactory.createEmptyBorder(1, 10, 1, 10));
        pnlfooter.setLayout(new java.awt.BorderLayout());

        tblcthd.setModel(new DefaultTableModel(
                new Object [][] {
                        {null, null, null},
                        {null, null, null},
                        {null, null, null},
                        {null, null, null}
                },
                new String [] {
                        "Mã hóa đơn", "Mã khách hàng", "Giá vé"
                }
        ) {
            boolean[] canEdit = new boolean [] {
                    false, false, false
            };

            public boolean isCellEditable(int rowIndex, int columnIndex) {
                return canEdit [columnIndex];
            }
        });
        tblcthd.addMouseListener(new MouseAdapter() {
            public void mouseClicked(MouseEvent evt) {
                tblcthdMouseClicked(evt);
            }
        });
        jScrollPane1.setViewportView(tblcthd);

        pnlfooter.add(jScrollPane1, java.awt.BorderLayout.CENTER);

        add(pnlfooter, java.awt.BorderLayout.CENTER);
    }// </editor-fold>//GEN-END:initComponents

    private JButton createBtn(String text, Color color){
        JButton btn = new JButton(text);
        btn.setBackground(color);
        btn.setForeground(Color.WHITE);
        btn.setFocusPainted(false);
        btn.setFont(new Font("SansSerif", Font.BOLD, 13));
        btn.setCursor(new Cursor(Cursor.HAND_CURSOR));

        btn.setContentAreaFilled(true);
        btn.setOpaque(true);
        btn.setBorderPainted(false);

        return btn;
    }

    private void them(){
        btnthem = createBtn("", UIColors.ADD);
        btnthem.addActionListener(v -> {
            CTHoaDonDialog cthd=new CTHoaDonDialog();
            cthd.setModal(true);
            cthd.setVisible(true);
            loaddata();
        });
    }

    private void xoa(){
        btnxoa = createBtn("", UIColors.DELETE);
        btnxoa.addActionListener(v -> {
            int row=tblcthd.getSelectedRow();

            if(row==-1){
                JOptionPane.showMessageDialog(this, "Vui lòng chọn chi tiết cần xóa");
                return;
            }

            String mahd =model.getValueAt(row, 0).toString();
            String makh =model.getValueAt(row, 1).toString();

            if(JOptionPane.showConfirmDialog(this, "Xóa vé này?","Xác nhận",JOptionPane.YES_NO_OPTION)==JOptionPane.YES_OPTION){
                if(bus.xoaCtietHd(mahd,makh)){
                    model.removeRow(row);
                    JOptionPane.showMessageDialog(this, "Xóa thành công");

                    if(soluongcanxoa>0){
                        soluongcanxoa--;
                        if(soluongcanxoa>0){
                            lbname.setText("Cần xóa thêm "+soluongcanxoa+" VÉ");

                        }else{
                            lbname.setText("Đã xóa đủ");
                            btnxoa.setVisible(false);

                        }
                    }
                }
            }
        });
    }

    private void sua(){
        btnsua = createBtn("", UIColors.EDIT);
        btnsua.addActionListener(v -> {
            int row = tblcthd.getSelectedRow();
            if (row < 0) {
                return;
            } else {
                String mact = tblcthd.getValueAt(row, 0).toString().trim();
                String makh = tblcthd.getValueAt(row, 1).toString().trim();
                CTietHDDTO ct = bus.timCt(mact, makh);

                CTHoaDonDialog ctpn = new CTHoaDonDialog(ct);
                ctpn.setModal(true);
                ctpn.setVisible(true);
            }
        });
    }

    private void lamMoi(){
        btnreset = createBtn("", UIColors.REFRESH);
        btnreset.addActionListener(v -> {
            loaddata();
        });
    }

    private void xuatExcel(){
        btnxuat = createBtn("", UIColors.EXPORT_EXCEL);
        btnxuat.addActionListener(v -> {
            ExcelHelper.xuatExcel(tblcthd, this, "Danh sách chi tiết hóa đơn");
        });
    }

    private void txttimKeyReleased(KeyEvent evt) {
        // TODO add your handling code here:
        String tim=lbname.getText().trim();
        String loai=cbtim.getSelectedItem().toString().trim();
        loaddata(bus.timNangcao(loai, tim));
    }

    private void txttimActionPerformed(ActionEvent evt) {
        // TODO add your handling code here:
    }

    private void tblcthdMouseClicked(MouseEvent evt) {
        // TODO add your handling code here:
        int row=tblcthd.getSelectedRow();
        if(row!=-1){
            btnsua.setEnabled(true);
            btnxoa.setEnabled(true);
            int cf=JOptionPane.showConfirmDialog(this, "Bạn có muốn xuất Excel dòng này không?");
            if(cf==JOptionPane.YES_OPTION){
                ExcelHelper.xuatExcel1Dong(tblcthd, row, btnreset, "Chi tiết hóa đơn");
            }
        }
    }


    private JButton btnreset;
    private JButton btnsua;
    private JButton btnthem;
    private JButton btnxoa;
    private JButton btnxuat;
    private JComboBox<String> cbtim;
    private JScrollPane jScrollPane1;
    private JLabel lbname;
    private JLabel lbtim;
    private JPanel pnlfooter;
    private JPanel pnlheader;
    private JPanel pnlsearch;
    private JPanel pnltable;
    private JTable tblcthd;
    private JTextField txttim;
}