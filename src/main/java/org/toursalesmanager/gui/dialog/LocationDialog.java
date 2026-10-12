package org.toursalesmanager.gui.dialog;

import lombok.AccessLevel;
import lombok.experimental.FieldDefaults;
import org.toursalesmanager.bus.*;
import org.toursalesmanager.dto.*;
import org.toursalesmanager.gui.component.ButtonFactory;
import org.toursalesmanager.gui.helper.AddComponentToForm;
import org.toursalesmanager.gui.helper.UIColors;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;

@FieldDefaults(level = AccessLevel.PRIVATE)
public class LocationDialog extends JDialog {
    JPanel formPanel, buttonPanel;

    LocationBUS bus;
    LocationDTO dd;
    boolean sua=false;
    String maDiaDiemCu = "";

    JButton btnluu, btnHuy;

    JTextField txtmadiadiem, txtdiachi, txttendd,txtquocgia;

    public LocationDialog() {
        this.bus=new LocationBUS();
        initComponents();
        this.setTitle("Địa điểm");
        this.setLocationRelativeTo(null);
    }

    public LocationDialog(LocationDTO dd) {
        this.bus=new LocationBUS();
        this.dd = dd;

        initComponents();

        this.setTitle("Sửa địa điểm");
        this.setLocationRelativeTo(null);

        this.sua= true;
        this.maDiaDiemCu=dd.getIdLocation();
        //load data if sua
        txtmadiadiem.setText(dd.getIdLocation());
        txtmadiadiem.setEnabled(false);
        txttendd.setText(dd.getLocationName());
        txtdiachi.setText(dd.getAddress());
        txtquocgia.setText(dd.getNation());
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

        formPanel = new JPanel(new GridLayout(0, 2, 10, 10));
        formPanel.setBorder(BorderFactory.createEmptyBorder(20, 30, 10, 30));
        buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 10, 10));
        buttonPanel.setBorder(BorderFactory.createEmptyBorder(0, 0, 15, 0)); // Tạo lề dưới

        setLayout(new BorderLayout(10, 10));

        txtmadiadiem = new JTextField();
        AddComponentToForm.addFormRow(formPanel, "Mã địa điểm:", txtmadiadiem);

        txttendd = new JTextField();
        AddComponentToForm.addFormRow(formPanel, "Tên địa điểm:", txttendd);

        txtdiachi = new JTextField();
        AddComponentToForm.addFormRow(formPanel, "Địa chỉ:", txtdiachi);

        txtquocgia = new JTextField();
        AddComponentToForm.addFormRow(formPanel, "Quốc gia:", txtquocgia);

        luu();
        buttonPanel.add(btnluu);
        huy();
        buttonPanel.add(btnHuy);

        add(formPanel, BorderLayout.CENTER);
        add(buttonPanel, BorderLayout.SOUTH);

        setPreferredSize(new Dimension(350, 260));
        pack();
        setLocationRelativeTo(null);
    }

    private void luu(){
        btnluu = ButtonFactory.create("Lưu", UIColors.SAVE);
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

                LocationDTO dd=new LocationDTO(maDiaDiem, ten, diachi, quocgia);

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
        btnHuy = ButtonFactory.create("Huỷ", UIColors.CANCEL);
        btnHuy.addActionListener(v -> {
            dispose();
        });
    }
}