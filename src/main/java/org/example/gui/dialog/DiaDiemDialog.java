package org.example.gui.dialog;

import org.example.bus.*;
import org.example.dto.*;
import org.example.gui.panel.UIColors;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class DiaDiemDialog extends JDialog {
    private DiaDiemBUS bus;
    private DiaDiemDTO dd;
    private boolean sua=false;
    private String maDiaDiemCu = "";

    public DiaDiemDialog() {
        this.bus=new DiaDiemBUS();
        initComponents();
        this.setTitle("Địa điểm");
        this.setLocationRelativeTo(null);
    }
    public DiaDiemDialog(DiaDiemDTO dd) {
        this.bus=new DiaDiemBUS();
        this.dd = dd;

        initComponents();

        this.setTitle("Sửa địa điểm");
        this.setLocationRelativeTo(null);

        this.sua= true;
        this.maDiaDiemCu=dd.getMaDiaDiem();
        //load data if sua
        txtmadiadiem.setText(dd.getMaDiaDiem());
        txtmadiadiem.setEnabled(false);
        txttendd.setText(dd.getTenDiaDiem());
        txtdiachi.setText(dd.getdiachi());
        txtquocgia.setText(dd.getQuocGia());
    }

    private void resetField(){
        txtmadiadiem.setText("");
        txttendd.setText("");
        txtdiachi.setText("");
        txtquocgia.setText("");
        txttendd.requestFocus();
    }

    private void initComponents() {
        txtmadiadiem = new JTextField();
        txttendd = new JTextField();
        txtquocgia = new JTextField();
        txtdiachi = new JTextField();
        lbMaDiaDiem = new JLabel();
        lbTenQuocGia = new JLabel();
        lbTenDiaDiem = new JLabel();
        lbDiaChi = new JLabel();
        btnluu = new JButton();
        btnHuy = new JButton();

        setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);

        lbMaDiaDiem.setText("Mã địa điểm");

        lbTenQuocGia.setText("Quốc gia");

        txtquocgia.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent evt) {
                txtquocgiaActionPerformed(evt);
            }
        });

        lbTenDiaDiem.setText("Tên địa điểm");

        lbDiaChi.setText("Địa chỉ");

        // define handle function
        luu();
        huy();

        GroupLayout layout = new GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
                layout.createParallelGroup(GroupLayout.Alignment.LEADING)
                        .addGroup(layout.createSequentialGroup()
                                .addGap(47, 47, 47)
                                .addGroup(layout.createParallelGroup(GroupLayout.Alignment.TRAILING)
                                        .addComponent(btnluu, GroupLayout.PREFERRED_SIZE, 75, GroupLayout.PREFERRED_SIZE)
                                        .addGroup(layout.createParallelGroup(GroupLayout.Alignment.LEADING, false)
                                                .addComponent(lbMaDiaDiem, GroupLayout.PREFERRED_SIZE, 85, GroupLayout.PREFERRED_SIZE)
                                                .addComponent(lbTenDiaDiem, GroupLayout.PREFERRED_SIZE, 85, GroupLayout.PREFERRED_SIZE)
                                                .addComponent(lbDiaChi, GroupLayout.PREFERRED_SIZE, 85, GroupLayout.PREFERRED_SIZE)
                                                .addComponent(lbTenQuocGia, GroupLayout.PREFERRED_SIZE, 85, GroupLayout.PREFERRED_SIZE)))
                                .addGap(48, 48, 48)
                                .addGroup(layout.createParallelGroup(GroupLayout.Alignment.TRAILING, false)
                                        .addComponent(txtmadiadiem)
                                        .addComponent(txttendd)
                                        .addComponent(txtquocgia)
                                        .addGroup(layout.createSequentialGroup()
                                                .addComponent(btnHuy)
                                                .addGap(9, 9, 9))
                                        .addComponent(txtdiachi, GroupLayout.DEFAULT_SIZE, GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                                .addContainerGap(56, Short.MAX_VALUE))
        );
        layout.setVerticalGroup(
                layout.createParallelGroup(GroupLayout.Alignment.LEADING)
                        .addGroup(GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                                .addContainerGap(68, Short.MAX_VALUE)
                                .addGroup(layout.createParallelGroup(GroupLayout.Alignment.BASELINE)
                                        .addComponent(lbMaDiaDiem)
                                        .addComponent(txtmadiadiem, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE))
                                .addGap(9, 9, 9)
                                .addGroup(layout.createParallelGroup(GroupLayout.Alignment.BASELINE)
                                        .addComponent(lbTenDiaDiem)
                                        .addComponent(txttendd, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE))
                                .addGap(9, 9, 9)
                                .addGroup(layout.createParallelGroup(GroupLayout.Alignment.TRAILING)
                                        .addComponent(lbDiaChi)
                                        .addComponent(txtdiachi, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE))
                                .addGap(9, 9, 9)
                                .addGroup(layout.createParallelGroup(GroupLayout.Alignment.BASELINE)
                                        .addComponent(lbTenQuocGia)
                                        .addComponent(txtquocgia, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE))
                                .addGap(9, 9, 9)
                                .addGroup(layout.createParallelGroup(GroupLayout.Alignment.BASELINE)
                                        .addComponent(btnluu)
                                        .addComponent(btnHuy))
                                .addGap(18, 18, 18))
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
            try{
                // them dia diem
                String maDiaDiem = txtmadiadiem.getText().trim();
                if(maDiaDiem.isEmpty()){
                    JOptionPane.showMessageDialog(this, "Lỗi");
                    return;
                }
                String ten = txttendd.getText().trim();
                if(ten.isEmpty()){
                    JOptionPane.showMessageDialog(this, "Lỗi");
                    return;
                }
                String diachi=txtdiachi.getText().trim();
                if(diachi.isEmpty()){
                    JOptionPane.showMessageDialog(this, "Lỗi");
                    return;
                }
                String quocgia=txtquocgia.getText().trim();

                DiaDiemDTO dd=new DiaDiemDTO(maDiaDiem, ten, diachi, quocgia);

                //sua dia diem
                if(sua){
                    if(bus.suaDiaDiem(dd,maDiaDiemCu)){
                        JOptionPane.showMessageDialog(this, "Cập nhật thành công");
                        this.dispose();
                    }else{
                        JOptionPane.showMessageDialog(this, "Cập nhật thất bại");
                    }
                }else{
                    if(bus.timDiaDiemTheoMa(maDiaDiem)!=null){
                        JOptionPane.showMessageDialog(this, "Lỗi mã địa điểm đã tồn tại");
                        return;
                    }

                    if(bus.themDiaDiem(dd)){
                        resetField();
                        JOptionPane.showMessageDialog(this, "Thêm thành công");
                    }else {
                        JOptionPane.showMessageDialog(this, "Thêm thất bại");
                    }
                }
            }catch(Exception e){
                e.printStackTrace();
                JOptionPane.showMessageDialog(this, "Lỗi");
            }
            dispose();
        });
    }

    private void huy(){
        btnHuy = createBtn("Huỷ", UIColors.CANCEL);
        btnHuy.addActionListener(v -> {
            dispose();
        });
    }

    private void txtquocgiaActionPerformed(ActionEvent evt) {//GEN-FIRST:event_txtquocgiaActionPerformed
        // TODO add your handling code here:
    }

    // define variables
    private JButton btnluu, btnHuy;

    private JLabel lbMaDiaDiem, lbDiaChi, lbTenDiaDiem, lbTenQuocGia;

    private JTextField txtmadiadiem, txtdiachi, txttendd,txtquocgia;
}