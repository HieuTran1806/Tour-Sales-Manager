package org.example.gui.dialog;

import lombok.AccessLevel;
import lombok.experimental.FieldDefaults;
import org.example.bus.*;
import org.example.dto.*;
import org.example.gui.panel.UIColors;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

@FieldDefaults(level = AccessLevel.PRIVATE)
public class DiaDiemDialog extends JDialog {
    DiaDiemBUS bus;
    DiaDiemDTO dd;
    boolean sua=false;
    String maDiaDiemCu = "";

    JButton btnluu, btnHuy;

    JLabel lbMaDiaDiem, lbDiaChi, lbTenDiaDiem, lbTenQuocGia;

    JTextField txtmadiadiem, txtdiachi, txttendd,txtquocgia;

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
        txtdiachi.setText(dd.getDiaChi());
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
        setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);
        setTitle("Địa điểm");

        lbMaDiaDiem = new JLabel("Mã địa điểm:");
        txtmadiadiem = new JTextField();

        lbTenDiaDiem = new JLabel("Tên địa điểm:");
        txttendd = new JTextField();

        lbDiaChi = new JLabel("Địa chỉ:");
        txtdiachi = new JTextField();

        lbTenQuocGia = new JLabel("Quốc gia:");
        txtquocgia = new JTextField();

        btnluu = createBtn("Lưu", UIColors.SAVE);
        btnHuy = createBtn("Huỷ", UIColors.CANCEL);

        setLayout(new BorderLayout(10, 10));

        JPanel formPanel = new JPanel(new GridLayout(4, 2, 10, 15));
        formPanel.setBorder(BorderFactory.createEmptyBorder(20, 30, 10, 30));

        formPanel.add(lbMaDiaDiem);
        formPanel.add(txtmadiadiem);

        formPanel.add(lbTenDiaDiem);
        formPanel.add(txttendd);

        formPanel.add(lbDiaChi);
        formPanel.add(txtdiachi);

        formPanel.add(lbTenQuocGia);
        formPanel.add(txtquocgia);

        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 30, 10));
        buttonPanel.setBorder(BorderFactory.createEmptyBorder(0, 0, 15, 0)); // Tạo lề dưới

        luu();
        btnluu.setPreferredSize(new Dimension(100, 35));
        huy();
        btnHuy.setPreferredSize(new Dimension(100, 35));

        buttonPanel.add(btnluu);
        buttonPanel.add(btnHuy);

        add(formPanel, BorderLayout.CENTER);
        add(buttonPanel, BorderLayout.SOUTH);

        setPreferredSize(new Dimension(350, 260));
        pack();
        setLocationRelativeTo(null);
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

}