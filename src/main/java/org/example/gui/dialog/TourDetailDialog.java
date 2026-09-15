package org.example.gui.dialog;

import lombok.AccessLevel;
import lombok.experimental.FieldDefaults;
import org.example.bus.LoaiTourBUS;
import org.example.dto.LoaiTourDTO;
import org.example.dto.TourDTO;
import org.example.gui.panel.UIColors;

import javax.swing.*;
import java.awt.*;

@FieldDefaults(level = AccessLevel.PRIVATE)
public class TourDetailDialog extends JDialog {
    // label and txt
    JLabel jlbImage;
    JTextField txtMaTour, txtTen, txtSoNgay, txtDonGia, txtSoCho, txtDiaDiemKhoiHanh, txtLoaiTour;

    // define btn
    JButton exitBtn;

    // define relate panel
    JPanel formPanel;

    // defined tourDTO
    TourDTO tourDTO;
    LoaiTourBUS loaiTourBUS = new LoaiTourBUS();

    public TourDetailDialog(TourDTO tourDTO){
        this.tourDTO = tourDTO;

        init();
        fillData(tourDTO);
    }

    private void init(){
        setTitle("TOUR " + tourDTO.getMaTour());
        setPreferredSize(new Dimension(360,520));
        pack();
        setLocationRelativeTo(null);
        setModal(true);
        setLayout(new BorderLayout());

        // use GridBagLayout
        formPanel = new JPanel(new GridBagLayout());

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5,10,5,10);
        gbc.fill = GridBagConstraints.HORIZONTAL;

        //row maTour
        int row = 0;
        gbc.gridx = 0;
        gbc.gridy = row;
        formPanel.add(new JLabel("Mã tour:"), gbc);

        gbc.gridx = 1;
        txtMaTour = new JTextField(15);
        txtMaTour.setEditable(false);
        formPanel.add(txtMaTour, gbc);

        // row ten
        row++;
        gbc.gridx = 0;
        gbc.gridy = row;
        formPanel.add(new JLabel("Tên tour:"), gbc);

        gbc.gridx = 1;
        txtTen = new JTextField(15);
        txtTen.setEditable(false);
        formPanel.add(txtTen, gbc);

        // row soNgay
        row++;
        gbc.gridx = 0;
        gbc.gridy = row;
        formPanel.add(new JLabel("Số ngày:"), gbc);

        gbc.gridx = 1;
        txtSoNgay = new JTextField();
        txtSoNgay.setEditable(false);
        formPanel.add(txtSoNgay, gbc);

        // row donGia
        row++;
        gbc.gridx = 0;
        gbc.gridy = row;
        formPanel.add(new JLabel("Đơn giá:"), gbc);

        gbc.gridx = 1;
        txtDonGia = new JTextField();
        txtDonGia.setEditable(false);
        formPanel.add(txtDonGia, gbc);

        // .............
        // row soCho
        row++;
        gbc.gridx = 0;
        gbc.gridy = row;
        formPanel.add(new JLabel("Số chỗ:"), gbc);

        gbc.gridx = 1;
        txtSoCho = new JTextField();
        txtSoCho.setEditable(false);
        formPanel.add(txtSoCho, gbc);

        // row ddKhoiHanh
        row++;
        gbc.gridx = 0;
        gbc.gridy = row;
        formPanel.add(new JLabel("Địa điểm khời hành:"), gbc);

        gbc.gridx = 1;
        txtDiaDiemKhoiHanh = new JTextField();
        txtDiaDiemKhoiHanh.setEditable(false);
        formPanel.add(txtDiaDiemKhoiHanh, gbc);

        // row loaiTour
        row++;
        gbc.gridx = 0;
        gbc.gridy = row;
        formPanel.add(new JLabel("Loại tour:"), gbc);

        gbc.gridx = 1;
        txtLoaiTour = new JTextField();
        txtLoaiTour.setEditable(false);
        formPanel.add(txtLoaiTour, gbc);

        // row image
        row++;
        gbc.gridx = 0;
        gbc.gridy = row;
        gbc.gridwidth = 2;

        formPanel.add(new JLabel("Hình ảnh:"), gbc);

        row++;
        jlbImage = new JLabel();
        jlbImage.setPreferredSize(new Dimension(200,150));
        jlbImage.setBorder(BorderFactory.createLineBorder(Color.GRAY));

        gbc.gridx = 0;
        gbc.gridy = row;
        gbc.gridwidth = 2;

        formPanel.add(jlbImage, gbc);
        gbc.gridwidth = 1;

        // Exit button
        exit();
        add(formPanel, BorderLayout.CENTER);
        add(exitBtn, BorderLayout.SOUTH);
    }

    private JButton createBtn(String text, Color color){
        JButton btn = new JButton(text);
        btn.setBackground(color);
        btn.setForeground(Color.WHITE);
        btn.setFocusPainted(false);
        btn.setCursor(new Cursor(Cursor.HAND_CURSOR));// Trong jpBtn panel

        return btn;
    }

    private void exit(){
        exitBtn = createBtn("Thoát", UIColors.CANCEL);
        exitBtn.addActionListener(e -> {
            dispose();
        });
    }

    private void loadImage(String imgLink){
        if(imgLink == null || imgLink.isEmpty()) return;

        ImageIcon icon = new ImageIcon(imgLink);

        Image img = icon.getImage().getScaledInstance(
                280,
                150,
                Image.SCALE_SMOOTH
        );

        jlbImage.setIcon(new ImageIcon(img));
    }

    private void fillData(TourDTO tour){
        txtMaTour.setText(tour.getMaTour());
        txtTen.setText(tour.getTen());
        txtSoNgay.setText(tour.getSoNgay() + "");
        txtDonGia.setText(tour.getDonGia() + "");
        txtSoCho.setText(tour.getSoCho() + "");
        txtDiaDiemKhoiHanh.setText(tour.getDiaDiemKhoiHanh());

        String maLoaiTour = tour.getMaLoaiTour();
        LoaiTourDTO lt = loaiTourBUS.getById(maLoaiTour);
        txtLoaiTour.setText(lt.getTheLoai());

        System.out.println(tour.getImgLink());
        loadImage(tour.getImgLink());
    }
}
