
package org.example.gui.panel;

import com.toedter.calendar.JDateChooser;
import org.example.dto.*;
import org.example.bus.*;
import org.example.gui.dialog.*;
import org.example.gui.helper.DateHelper;
import org.example.gui.helper.ExcelHelper;

import java.awt.*;
import java.awt.event.*;
import java.util.ArrayList;
import java.util.Date;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;

public class HoaDonPanel extends JPanel {
    private HoaDonBUS bus;
    private DefaultTableModel model;

    public HoaDonPanel() {
        initComponents();
        bus=new HoaDonBUS();
        model=(DefaultTableModel) tblhoadon.getModel();
        txtday.setVisible(false);
        loadData();;
    }

    private void loadData(){
        model.setRowCount(0);
        bus.docDs();

        ArrayList<HoaDonDTO> ds = HoaDonBUS.getDs();
        if(ds==null) return;
        for(HoaDonDTO hd: ds){
            model.addRow(new Object[]{
                    hd.getMaHD(),hd.getMaKHTour(),hd.getMaKHDat(),hd.getMaNV(), DateHelper.toString(hd.getNgay()),hd.getSoLuong(), hd.getMaKM(),String.format("%.0f",hd.getTongTien())
            });
        }
    }
    private void loadData(Date ngay){
        model.setRowCount(0);
        bus.docDs();

        ArrayList<HoaDonDTO> ds =bus.getHDtheongay(ngay);
        if(ds==null) return;
        for(HoaDonDTO hd: ds){
            model.addRow(new Object[]{
                    hd.getMaHD(),hd.getMaKHTour(),hd.getMaKHDat(),hd.getMaNV(),DateHelper.toString(hd.getNgay()),hd.getSoLuong(),hd.getMaKM(),String.format("%.0f",hd.getTongTien())
            });
        }
    }
    private void loadData(String loai,String key){
        model.setRowCount(0);
        bus.docDs();

        ArrayList<HoaDonDTO> ds =bus.timNangcao(loai, key);
        if(ds==null) return;
        for(HoaDonDTO hd: ds){
            model.addRow(new Object[]{
                    hd.getMaHD(),hd.getMaKHTour(),hd.getMaKHDat(),hd.getMaNV(),DateHelper.toString(hd.getNgay()),hd.getSoLuong(), hd.getMaKM(),hd.getTongTien()
            });
        }
    }

    private void initComponents() {
        pnlheader = new JPanel();
        lbname = new JLabel();
        pnlsearch = new JPanel();
        lbtim = new JLabel();
        cbtim = new JComboBox<>();
        txttim = new JTextField();
        txtday = new com.toedter.calendar.JDateChooser();
        pnlfooter = new JPanel();
        btnthem = new JButton();
        btnxoa = new JButton();
        btnsua = new JButton();
        btnchitiet = new JButton();
        btnreset = new JButton();
        btnxuat = new JButton();
        pnltable = new JPanel();
        jScrollPane1 = new JScrollPane();
        tblhoadon = new JTable();

        setLayout(new BorderLayout());

        pnlheader.setLayout(new BorderLayout());

        lbname.setFont(new Font("Arial", 0, 18)); // NOI18N
        lbname.setHorizontalAlignment(SwingConstants.CENTER);
        lbname.setText("Quản lý hóa đơn");
        pnlheader.add(lbname, BorderLayout.CENTER);

        lbtim.setText("Tìm theo:");
        pnlsearch.add(lbtim);

        cbtim.setModel(new DefaultComboBoxModel<>(new String[] { "Mã hóa đơn", "Mã kế hoạch tour", "Mã khách hàng đặt", "Mã nhân viên", "Ngày" }));
        cbtim.addItemListener(new ItemListener() {
            public void itemStateChanged(ItemEvent evt) {
                cbtimItemStateChanged(evt);
            }
        });
        pnlsearch.add(cbtim);

        txttim.setColumns(10);
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

        txtday.setPreferredSize(new Dimension(90, 22));
        txtday.addPropertyChangeListener(new java.beans.PropertyChangeListener() {
            public void propertyChange(java.beans.PropertyChangeEvent evt) {
                txtdayPropertyChange(evt);
            }
        });
        txtday.addKeyListener(new KeyAdapter() {
            public void keyReleased(KeyEvent evt) {
                txtdayKeyReleased(evt);
            }
        });
        pnlsearch.add(txtday);

        pnlheader.add(pnlsearch, BorderLayout.PAGE_END);

        add(pnlheader, BorderLayout.NORTH);

        them();
        pnlfooter.add(btnthem);

        xoa();
        pnlfooter.add(btnxoa);

        sua();
        pnlfooter.add(btnsua);

        xemChiTiet();
        pnlfooter.add(btnchitiet);

        lamMoi();
        pnlfooter.add(btnreset);

        xuatExcel();
        pnlfooter.add(btnxuat);

        add(pnlfooter, BorderLayout.PAGE_END);

        pnltable.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        pnltable.setLayout(new BorderLayout());

        tblhoadon.setBorder(BorderFactory.createEmptyBorder(15, 15, 15, 15));
        tblhoadon.setModel(new DefaultTableModel(
                new Object [][] {

                },
                new String [] {
                        "Mã hóa đơn", "Mã kế hoạch tour", "Mã khách hàng đặt", "Mã nhân viên", "Ngày", "Số lượng", "Khuyến mãi","Tổng tiền"
                }
        ) {
            boolean[] canEdit = new boolean [] {
                    false, false, false, false, false, false, false, false
            };

            public boolean isCellEditable(int rowIndex, int columnIndex) {
                return canEdit [columnIndex];
            }
        });
        tblhoadon.setRowSelectionAllowed(true);
        tblhoadon.setColumnSelectionAllowed(false);
        tblhoadon.addMouseListener(new MouseAdapter() {
            public void mouseClicked(MouseEvent evt) {
                tblhoadonMouseClicked(evt);
            }
        });
        jScrollPane1.setViewportView(tblhoadon);
        tblhoadon.getColumnModel().getSelectionModel().setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        if (tblhoadon.getColumnModel().getColumnCount() > 0) {
            tblhoadon.getColumnModel().getColumn(0).setPreferredWidth(120);
            tblhoadon.getColumnModel().getColumn(1).setPreferredWidth(120);
            tblhoadon.getColumnModel().getColumn(2).setPreferredWidth(120);
            tblhoadon.getColumnModel().getColumn(3).setPreferredWidth(120);
            tblhoadon.getColumnModel().getColumn(4).setPreferredWidth(120);
            tblhoadon.getColumnModel().getColumn(5).setPreferredWidth(120);
            tblhoadon.getColumnModel().getColumn(6).setPreferredWidth(120);
            tblhoadon.getColumnModel().getColumn(6).setPreferredWidth(120);
        }

        pnltable.add(jScrollPane1, BorderLayout.CENTER);

        add(pnltable, BorderLayout.CENTER);
    }

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
        btnthem = createBtn("Thêm", UIColors.ADD);
        btnthem.addActionListener(v -> {
            HoaDonDialog hdd=new HoaDonDialog();

            hdd.setModal(true);
            hdd.setVisible(true);
            loadData();
        });
    }

    private void xoa(){
        btnxoa = createBtn("Xóa", UIColors.DELETE);
        btnxoa.setEnabled(false);
        btnxoa.addActionListener(v -> {
            try{
                int row =tblhoadon.getSelectedRow();

                if(row==-1){
                    JOptionPane.showMessageDialog(this, "Vui lòng chọn hóa đơn cần xóa");
                    return;
                }
                int cf =JOptionPane.showConfirmDialog(this, "Bạn có muốn xóa không?","Xác nhận",JOptionPane.YES_NO_OPTION);
                if(cf==JOptionPane.YES_OPTION){
                    String ma = tblhoadon.getValueAt(row, 0).toString();
                    HoaDonDTO hd=bus.timHd(ma);

                    if(bus.xoaHoaDon(ma)){
                        DefaultTableModel model=(DefaultTableModel) tblhoadon.getModel();

                        model.removeRow(row);
                        loadData();
                        JOptionPane.showMessageDialog(this, "Xóa thành công");
                    }

                }
            }catch(Exception ex){
                ex.printStackTrace();
            }
        });
    }

    private void sua(){
        btnsua = createBtn("Chỉnh sửa", UIColors.EDIT);
        btnsua.setEnabled(false);
        btnsua.addActionListener(v -> {

            int row =tblhoadon.getSelectedRow();
            if(row==-1){
                JOptionPane.showMessageDialog(this, "Vui lòng chọn dòng cần sửa");
                return;
            }
            String ma =tblhoadon.getValueAt(row, 0).toString().trim();
            HoaDonDTO hd=bus.timHd(ma);
            if(hd!=null){
                org.example.gui.dialog.HoaDonDialog hdd=new org.example.gui.dialog.HoaDonDialog(hd);
                hdd.setModal(true);
                hdd.setVisible(true);
                loadData();
            }else{
                JOptionPane.showMessageDialog(this, "Không tìm thấy dữ liệu hóa đơn");
            }
        });
    }

    private void xemChiTiet(){
        btnchitiet = createBtn("Xem chi tiết", UIColors.VIEW);
        btnchitiet.addActionListener(v -> {
            int row=tblhoadon.getSelectedRow();
            String ma =tblhoadon.getValueAt(row, 0).toString().trim();
            if(row!=-1){
                btnchitiet.setEnabled(true);
                NhapCTHD nhap=new NhapCTHD(ma);
                nhap.setModal(true);
                nhap.setVisible(true);
            }
        });
    }

    private void lamMoi(){
        btnreset = createBtn("Làm mới", UIColors.REFRESH);
        btnreset.addActionListener(v -> {
            loadData();
        });
    }

    private void xuatExcel(){
        btnxuat = createBtn("Xuất excel", UIColors.EXPORT_EXCEL);
        btnxuat.addActionListener(v -> {
            ExcelHelper.xuatExcel(tblhoadon, this, "Danh sách hóa đơn");
        });
    }

    private void txttimActionPerformed(ActionEvent evt) {
        // TODO add your handling code here:
    }

    private void txttimKeyReleased(KeyEvent evt) {
        // TODO add your handling code here:
        String text=txttim.getText().trim();
        String loai =cbtim.getSelectedItem().toString().trim();
        loadData(loai, text);
    }

    private void cbtimItemStateChanged(ItemEvent evt) {
        // TODO add your handling code here:
        String item=cbtim.getSelectedItem().toString();
        if(item.equals("Ngày")){
            txttim.setVisible(false);
            txtday.setVisible(true);
        }else{
            txttim.setVisible(true);
            txtday.setVisible(false);
        }
    }

    private void txtdayKeyReleased(KeyEvent evt) {
        // TODO add your handling code here:
    }

    private void txtdayPropertyChange(java.beans.PropertyChangeEvent evt) {
        // TODO add your handling code here:
        java.util.Date ngay = txtday.getDate();
        loadData(ngay);
    }

    private void tblhoadonMouseClicked(MouseEvent evt) {
        // TODO add your handling code here:
        int row=tblhoadon.getSelectedRow();
        String ma =tblhoadon.getValueAt(row, 0).toString().trim();
        if(row!=-1){
            btnsua.setEnabled(true);
            btnxoa.setEnabled(true);
            btnchitiet.setEnabled(true);
            if(evt.getClickCount()==2){
                int cf=JOptionPane.showConfirmDialog(this, "Bạn có muốn xuất excel dòng này không?");
                if(cf==JOptionPane.YES_OPTION){
                    ExcelHelper.xuatExcel1Dong(tblhoadon, row, btnreset, ma);
                }
            }
        }
    }

    private JButton btnchitiet, btnreset, btnsua, btnthem, btnxoa, btnxuat;

    private JComboBox<String> cbtim;

    private JScrollPane jScrollPane1;

    private JLabel lbname, lbtim;
    private JPanel pnlfooter, pnlheader, pnlsearch, pnltable;

    private JTable tblhoadon;

    private JDateChooser txtday;
    private JTextField txttim;
}