package org.toursalesmanager.gui.dialog;

import lombok.AccessLevel;
import lombok.experimental.FieldDefaults;
import org.toursalesmanager.bus.TourTypeBUS;
import org.toursalesmanager.dto.TourTypeDTO;
import org.toursalesmanager.gui.component.ButtonFactory;
import org.toursalesmanager.gui.helper.UIColors;

import javax.swing.*;
import java.awt.*;

@FieldDefaults(level = AccessLevel.PRIVATE)
public class TourTypeDialog extends JDialog{
    // define label and txt
    JLabel jlbMaLoaiTour, jlbTheLoai, jlbMoTa, jlbTrangThai;
    JTextField txtMaLoaiTour, txtTheLoai, txtMoTa;

    // define btn
    JButton saveBtn, cancelBtn;

    // cmb trang thai
    JComboBox<String> cbTrangThai;

    TourTypeDTO tourTypeDTO;
    TourTypeBUS tourTypeBUS;

    public TourTypeDialog(TourTypeBUS tourTypeBUS, TourTypeDTO tourTypeDTO){
        this.tourTypeBUS = tourTypeBUS;
        this.tourTypeDTO = tourTypeDTO;

        setTitle(tourTypeDTO == null ? "Thêm loại Tour" : "Sửa loại Tour");
        setSize(300, 240);
        setLocationRelativeTo(null);
        setModal(true);

        init();
        if(tourTypeDTO != null){
            loadData();
        }
    }

    private void loadData(){
        txtMaLoaiTour.setText(tourTypeDTO.getIdTourType());
        txtMaLoaiTour.setEnabled(false);

        txtTheLoai.setText(tourTypeDTO.getTypeOfTour());
        txtMoTa.setText(tourTypeDTO.getDescription());

        String oldStatus = tourTypeDTO.getStatus(); // load status
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

    public void save(){
        saveBtn = ButtonFactory.create("Lưu", UIColors.SAVE);
        saveBtn.addActionListener(e -> {
            if(txtMaLoaiTour.getText().trim().isEmpty() || txtTheLoai.getText().trim().isEmpty()){
                JOptionPane.showMessageDialog(this, "Vui lòng nhập đầy đủ thông tin");
                return;
            }

            if(tourTypeDTO == null){
                if(tourTypeBUS.existedLoaiTourWithID(txtMaLoaiTour.getText()))
                    JOptionPane.showMessageDialog(null, "Mã loại tour đã tồn tại, vui lòng nhập mã khác!");
                else {
                    // trang thai
                    String statusSelected = (String) cbTrangThai.getSelectedItem();
                    TourTypeDTO newType = new TourTypeDTO(txtMaLoaiTour.getText(), txtTheLoai.getText(), txtMoTa.getText(), statusSelected);
                    tourTypeBUS.addLoaiTour(newType);
                    dispose();
                    JOptionPane.showMessageDialog(null, "Đã thêm");
                }
            }else{
                tourTypeDTO.setTypeOfTour(txtTheLoai.getText());
                tourTypeDTO.setDescription(txtMoTa.getText());

                String selectedStatus = (cbTrangThai.getSelectedIndex() == 0) ? "Đang hoạt động" : "Ngưng";
                tourTypeDTO.setStatus(selectedStatus);

                tourTypeBUS.editLoaiTour(tourTypeDTO);
                dispose();
                JOptionPane.showMessageDialog(this, "Chỉnh sửa thành công");
            }
        });
    }

    public void cancel(){
        cancelBtn = ButtonFactory.create("Hủy", UIColors.CANCEL);
        cancelBtn.addActionListener(e -> {
            dispose();
        });
    }
}
