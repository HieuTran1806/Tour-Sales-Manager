package org.example.gui.dialog;

import lombok.AccessLevel;
import lombok.experimental.FieldDefaults;
import org.example.bus.LoaiTourBUS;
import org.example.dto.LoaiTourDTO;
import org.example.gui.panel.UIColors;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;

@FieldDefaults(level = AccessLevel.PRIVATE)
public class LoaiTourDialog extends JDialog{
    // define label and txt
    JLabel jlbMaLoaiTour, jlbTheLoai, jlbMoTa, jlbTrangThai;
    JTextField txtMaLoaiTour, txtTheLoai, txtMoTa;

    // define btn
    JButton saveBtn, cancelBtn;

    // cmb trang thai
    JComboBox<String> cbTrangThai;

    LoaiTourDTO loaiTourDTO;
    LoaiTourBUS loaiTourBUS;

    public LoaiTourDialog(LoaiTourBUS loaiTourBUS, LoaiTourDTO loaiTourDTO){
        this.loaiTourBUS = loaiTourBUS;
        this.loaiTourDTO = loaiTourDTO;

        setTitle(loaiTourDTO == null ? "Thêm loại Tour" : "Sửa loại Tour");
        setSize(300, 240);
        setLocationRelativeTo(null);
        setModal(true);

        init();
        if(loaiTourDTO != null){
            loadData();
        }
    }

    private void loadData(){
        txtMaLoaiTour.setText(loaiTourDTO.getMaLoaiTour());
        txtMaLoaiTour.setEnabled(false);

        txtTheLoai.setText(loaiTourDTO.getTheLoai());
        txtMoTa.setText(loaiTourDTO.getMoTa());

        // if(loaiTourDTO.getTrangThai() == 0){
        //
        // }

        String oldStatus = String.valueOf(loaiTourDTO.getTrangThai());
        cbTrangThai.setSelectedItem(oldStatus);
    }

    public void init(){
        setLayout(new BorderLayout());

        JPanel panelForm = new JPanel(new GridLayout(4, 2, 10, 10));

        // Panel Buttons
        JPanel jpBtn = new JPanel(new FlowLayout());

        // Save button
        save();
        jpBtn.add(saveBtn);

        //Cancel button
        cancel();
        jpBtn.add(cancelBtn);

        //row MaLoai
        jlbMaLoaiTour = new JLabel("Mã loại");
        jlbMaLoaiTour.setBorder(BorderFactory.createEmptyBorder(5, 5, 5, 0));
        panelForm.add(jlbMaLoaiTour);
        txtMaLoaiTour = new JTextField();
        panelForm.add(txtMaLoaiTour);

        // row the loai
        jlbTheLoai = new JLabel("Thể loại");
        jlbTheLoai.setBorder(BorderFactory.createEmptyBorder(5, 5, 5, 0));
        panelForm.add(jlbTheLoai);
        txtTheLoai = new JTextField();
        panelForm.add(txtTheLoai);

        // row mo ta
        jlbMoTa = new JLabel("Mô tả");
        jlbMoTa.setBorder(BorderFactory.createEmptyBorder(5, 5, 5, 0));
        panelForm.add(jlbMoTa);
        txtMoTa = new JTextField();
        panelForm.add(txtMoTa);

        // row trang thai
        jlbTrangThai = new JLabel("Trạng thái");
        jlbTrangThai.setBorder(BorderFactory.createEmptyBorder(5, 5, 10, 0));
        panelForm.add(jlbTrangThai);

        String[] status = {"Đang hoạt động", "Ngưng"};
        cbTrangThai = new JComboBox<>(status);
        panelForm.add(cbTrangThai);

        jpBtn.setBorder(BorderFactory.createEmptyBorder(0, 0, 10, 0));

        add(panelForm, BorderLayout.CENTER);
        add(jpBtn, BorderLayout.SOUTH);
    }

    private JButton createBtn(String text, Color color){
        JButton btn = new JButton(text);
        btn.setBackground(color);
        btn.setForeground(Color.WHITE);
        btn.setFocusPainted(false);
        btn.setCursor(new Cursor(Cursor.HAND_CURSOR));// Trong jpBtn panel

        return btn;
    }

    public void save(){
        saveBtn = createBtn("Lưu", UIColors.SAVE);
        saveBtn.addActionListener(e -> {
            if(txtMaLoaiTour.getText().trim().isEmpty() || txtTheLoai.getText().trim().isEmpty()){
                JOptionPane.showMessageDialog(this, "Vui lòng nhập đầy đủ thông tin");
                return;
            }

            if(loaiTourDTO == null){
                if(loaiTourBUS.existedLoaiTourWithID(txtMaLoaiTour.getText()))
                    JOptionPane.showMessageDialog(null, "Mã loại tour đã tồn tại, vui lòng nhập mã khác!");
                else {
                    // trang thai
                    String statusSelected = (String) cbTrangThai.getSelectedItem();
                    LoaiTourDTO newType = new LoaiTourDTO(txtMaLoaiTour.getText(), txtTheLoai.getText(), txtMoTa.getText(), statusSelected);
                    loaiTourBUS.addLoaiTour(newType);
                    dispose();
                    JOptionPane.showMessageDialog(null, "Đã thêm");
                }
            }else{
                loaiTourDTO.setTheLoai(txtTheLoai.getText());
                loaiTourDTO.setMoTa(txtMoTa.getText());
                loaiTourDTO.setTrangThai(String.valueOf(cbTrangThai.getSelectedIndex()));
                loaiTourBUS.editLoaiTour(loaiTourDTO);
                dispose();
                JOptionPane.showMessageDialog(this, "Chỉnh sửa thành công");
            }
        });
    }

    public void cancel(){
        cancelBtn = createBtn("Hủy", UIColors.CANCEL);
        cancelBtn.addActionListener(e -> {
            dispose();
        });
    }
}
