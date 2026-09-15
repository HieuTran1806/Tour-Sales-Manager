
package org.example.gui.dialog;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.util.ArrayList;
import org.example.bus.CTHoaDonBUS;
import org.example.bus.HoaDonBUS;
import org.example.bus.KhachHangBUS;
import org.example.dto.*;
import org.example.dto.CTietHDDTO;
import org.example.gui.panel.UIColors;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableColumn;

public class NhapCTHD extends JDialog {
    private String mahd;
    private int soluong;
    private CTHoaDonBUS bus;
    public HoaDonBUS hdbus;

    public NhapCTHD(Frame parent, boolean modal, String mahd, int soluong) {
        super(parent, modal);
        this.soluong=soluong;
        this.mahd=mahd;
        bus=new CTHoaDonBUS();
        hdbus=new HoaDonBUS();
        this.setTitle("Nhập chi tiết hóa đơn");
        this.setLocationRelativeTo(null);
        initComponents();
        loaddata();
        setupComboBoxKhachHang();
    }

    public NhapCTHD(String ma) {
        this.soluong=soluong;
        this.mahd=mahd;
        bus=new org.example.bus.CTHoaDonBUS();
        this.setTitle("Chi tiết hóa đơn của hóa đơn: "+ma);
        this.setLocationRelativeTo(null);
        initComponents();
        btnluu.setVisible(false);
        tblnhapct.setDefaultEditor(Object.class, null);
        loaddata(ma);
    }

    private void setupComboBoxKhachHang() {
        JComboBox<String> cbKhachHang = new JComboBox<>();

        KhachHangBUS khBus = new KhachHangBUS();
        if (KhachHangBUS.dsKH == null) {
            khBus.docDSKH();
        }

        for (KhachHangDTO kh : KhachHangBUS.dsKH) {
            cbKhachHang.addItem(kh.getMaKH());
        }

        TableColumn khColumn = tblnhapct.getColumnModel().getColumn(1);
        khColumn.setCellEditor(new DefaultCellEditor(cbKhachHang));
    }

    private void loaddata(){
        DefaultTableModel model=(DefaultTableModel) tblnhapct.getModel();

        model.setRowCount(0);

        for(int i=0;i<soluong;i++){
            model.addRow(new Object[]{mahd,"",bus.layGia(mahd)});
        }
    }
    private void loaddata(String mahd){
        DefaultTableModel model=(DefaultTableModel) tblnhapct.getModel();
        model.setRowCount(0);

        ArrayList<CTietHDDTO> ds=bus.getDstheoma(mahd);
        for(CTietHDDTO cthddto: ds){
            model.addRow(new Object[]{
                    cthddto.getMaHD(),cthddto.getMaKHDi(),String.valueOf(cthddto.getGiaVe())
            });
        }
    }

    @SuppressWarnings("unchecked")
    private void initComponents() {

        jScrollPane1 = new JScrollPane();
        tblnhapct = new JTable();
        btnluu = new JButton();

        setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);

        tblnhapct.setModel(new DefaultTableModel(
                new Object [][] {
                        {null, null, null},
                        {null, null, null},
                        {null, null, null},
                        {null, null, null}
                },
                new String [] {
                        "Mã hóa đơn", "Mã khách hàng", "Giá vé"
                }
        ));
        tblnhapct.addKeyListener(new KeyAdapter() {
            public void keyPressed(KeyEvent evt) {
                tblnhapctKeyPressed(evt);
            }
        });
        jScrollPane1.setViewportView(tblnhapct);

        // define handle funtion
        luu();

        GroupLayout layout = new GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
                layout.createParallelGroup(GroupLayout.Alignment.LEADING)
                        .addGroup(layout.createSequentialGroup()
                                .addGroup(layout.createParallelGroup(GroupLayout.Alignment.LEADING)
                                        .addGroup(layout.createSequentialGroup()
                                                .addContainerGap()
                                                .addComponent(jScrollPane1, GroupLayout.PREFERRED_SIZE, 375, GroupLayout.PREFERRED_SIZE))
                                        .addGroup(layout.createSequentialGroup()
                                                .addGap(155, 155, 155)
                                                .addComponent(btnluu, GroupLayout.PREFERRED_SIZE, 75, GroupLayout.PREFERRED_SIZE)))
                                .addContainerGap(19, Short.MAX_VALUE))
        );
        layout.setVerticalGroup(
                layout.createParallelGroup(GroupLayout.Alignment.LEADING)
                        .addGroup(layout.createSequentialGroup()
                                .addContainerGap()
                                .addComponent(jScrollPane1, GroupLayout.PREFERRED_SIZE, 275, GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(LayoutStyle.ComponentPlacement.UNRELATED)
                                .addComponent(btnluu)
                                .addContainerGap(GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        pack();
    }

    private JButton createBtn(String text, Color color){
        JButton btn = new JButton(text);
        btn.setBackground(color);
        btn.setForeground(Color.WHITE);
        btn.setFocusPainted(false);
        btn.setCursor(new Cursor(Cursor.HAND_CURSOR));// Trong jpBtn panel
        return btn;
    }

    private void luu(){
        btnluu = createBtn("Lưu", UIColors.SAVE);
        btnluu.addActionListener(v -> {
            DefaultTableModel model=(DefaultTableModel) tblnhapct.getModel();

            if(tblnhapct.isEditing()){
                tblnhapct.getCellEditor().stopCellEditing();
            }

            boolean loi=false;
            String mahd =model.getValueAt(0, 0).toString().trim();

            bus.capNhatSoluong(soluong,hdbus.timHd(mahd).getMaKHTour());
            for(int i=0;i<soluong;i++){
                mahd =model.getValueAt(i, 0).toString().trim();
                String makh =model.getValueAt(i, 1).toString().trim();
                float giave= Float.parseFloat(model.getValueAt(i, 2).toString().trim());
                CTietHDDTO cthd=new CTietHDDTO(mahd,makh,giave);
                if(!bus.themCTietHd(cthd)){
                    loi=true;
                    break;
                }
            }
            if(!loi){
                JOptionPane.showMessageDialog(this, "Lưu thành công");
                this.dispose();
            }else{
                JOptionPane.showMessageDialog(this, "Lỗi");
            }
        });
    }

    private void tblnhapctKeyPressed(KeyEvent evt) {
        // TODO add your handling code here:
    }

    // variables
    private JButton btnluu;
    private JScrollPane jScrollPane1;
    private JTable tblnhapct;
}