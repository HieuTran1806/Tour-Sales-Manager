package org.example.gui.dialog;

import org.example.bus.CTietKHTourBUS;
import org.example.dto.CTietKHTourDTO;
import org.example.gui.panel.UIColors;

import javax.swing.*;
import java.awt.*;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

public class CTietKHTourDialog extends JDialog {
    private JLabel jlbMaCTietKHTour, jlbNgayThucHien, jlbTongChi, jlbTienO, jlbTienAn, jlbTienDiLai, jlbDiemDi, jlbDiemDen, jlbMaKHtour;
    private JTextField txtMaCTietKHTour, txtNgayThucHien, txtTongChi, txtTienO, txtTienAn, txtTienDiLai, txtDiemDi, txtDiemDen, txtMaKHtour;
    private JButton saveBtn, cancelBtn;
    private CTietKHTourBUS cTietKHTourBUS;
    private CTietKHTourDTO cTietKHTourDTO;
    private String maKHTour;
    private DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");
    private LocalDate today;

    public CTietKHTourDialog(CTietKHTourBUS cTietKHTourBUS, CTietKHTourDTO cTietKHTourDTO, String maKHTour){
        this.cTietKHTourBUS = cTietKHTourBUS;
        this.cTietKHTourDTO = cTietKHTourDTO;
        this.maKHTour = maKHTour;
        today = LocalDate.now();

        setTitle(cTietKHTourDTO == null ? "Thêm chi tiết kế hoạch tour" : "Sửa chi tiết kế hoạch Tour");
        setSize(300, 440);
        setLocationRelativeTo(null);
        setModal(true);

        init();
        if (cTietKHTourDTO != null) {
            loadData();
        }
    }

    private void init(){
        setLayout(new BorderLayout());

        JPanel formPanel = new JPanel(new GridLayout(9, 2)); // at center

        JPanel southPanel = new JPanel(new FlowLayout());

        // Save button
        save();
        southPanel.add(saveBtn);

        //Cancel button
        cancel();
        southPanel.add(cancelBtn);

        //row MaCTietKHTour
        jlbMaCTietKHTour = new JLabel("Mã kế chi tiết hoạch tour");
        jlbMaCTietKHTour.setBorder(BorderFactory.createEmptyBorder(5, 5, 5, 0));
        formPanel.add(jlbMaCTietKHTour);
        txtMaCTietKHTour = new JTextField();
        formPanel.add(txtMaCTietKHTour);

        // row NgayThucHien
        jlbNgayThucHien = new JLabel("Ngày thực hiện");
        jlbNgayThucHien.setBorder(BorderFactory.createEmptyBorder(5, 5, 5, 0));
        formPanel.add(jlbNgayThucHien);
        txtNgayThucHien = new JTextField();
        txtNgayThucHien.setText(today.format(formatter));
        formPanel.add(txtNgayThucHien);

        //row TongChi
        jlbTongChi = new JLabel("Tổng chi");
        jlbTongChi.setBorder(BorderFactory.createEmptyBorder(5, 5, 5, 0));
        formPanel.add(jlbTongChi);
        txtTongChi = new JTextField();
        formPanel.add(txtTongChi);

        //row TienO
        jlbTienO = new JLabel("Tiền ở");
        jlbTienO.setBorder(BorderFactory.createEmptyBorder(5, 5, 5, 0));
        formPanel.add(jlbTienO);
        txtTienO = new JTextField();
        formPanel.add(txtTienO);

        //row TienAn
        jlbTienAn = new JLabel("Tiền ăn");
        jlbTienAn.setBorder(BorderFactory.createEmptyBorder(5, 5, 5, 0));
        formPanel.add(jlbTienAn);
        txtTienAn = new JTextField();
        formPanel.add(txtTienAn);

        //row TienDiLai
        jlbTienDiLai = new JLabel("Tiền đi lại");
        jlbTienDiLai.setBorder(BorderFactory.createEmptyBorder(5, 5, 5, 0));
        formPanel.add(jlbTienDiLai);
        txtTienDiLai = new JTextField();
        formPanel.add(txtTienDiLai);

        //row DiemDi
        jlbDiemDi = new JLabel("Điểm đi");
        jlbDiemDi.setBorder(BorderFactory.createEmptyBorder(5, 5, 5, 0));
        formPanel.add(jlbDiemDi);
        txtDiemDi = new JTextField();
        formPanel.add(txtDiemDi);

        //row DiemDen
        jlbDiemDen = new JLabel("Điểm đến");
        jlbDiemDen.setBorder(BorderFactory.createEmptyBorder(5, 5, 5, 0));
        formPanel.add(jlbDiemDen);
        txtDiemDen = new JTextField();
        formPanel.add(txtDiemDen);

        //row MaKHtour
        jlbMaKHtour = new JLabel("Mã kế hoạch tour");
        jlbMaKHtour.setBorder(BorderFactory.createEmptyBorder(5, 5, 5, 0));
        formPanel.add(jlbMaKHtour);
        txtMaKHtour = new JTextField(maKHTour);
        txtMaKHtour.setEnabled(false);
        formPanel.add(txtMaKHtour);

        add(formPanel, BorderLayout.CENTER);
        add(southPanel, BorderLayout.SOUTH);
    }

    private void loadData(){
        txtMaCTietKHTour.setText(cTietKHTourDTO.getMaCTietKHTour());
        txtNgayThucHien.setText(cTietKHTourDTO.getNgayThucHien());
        txtTongChi.setText(cTietKHTourDTO.getTongChi() + "");
        txtTienO.setText(cTietKHTourDTO.getTienO() + "");
        txtTienAn.setText(cTietKHTourDTO.getTienAn() + "");
        txtTienDiLai.setText(cTietKHTourDTO.getTienDiLai() + "");
        txtDiemDi.setText(cTietKHTourDTO.getDiemDi());
        txtDiemDen.setText(cTietKHTourDTO.getDiemDen());
        txtMaKHtour.setText(maKHTour);
    }

    private JButton createBtn(String text, Color color){
        JButton btn = new JButton(text);
        btn.setBackground(color);
        btn.setForeground(Color.WHITE);
        btn.setFocusPainted(false);
        btn.setCursor(new Cursor(Cursor.HAND_CURSOR));// Trong jpBtn panel
        return btn;
    }

    private void save(){
        saveBtn = createBtn("Lưu", UIColors.SAVE);
        saveBtn.addActionListener(e -> {
            if(isEmpty(txtMaCTietKHTour, txtNgayThucHien, txtTongChi, txtTienO, txtTienAn, txtTongChi, txtTienDiLai, txtDiemDi, txtDiemDen)){
                JOptionPane.showMessageDialog(this, "Vui lòng nhập đầy đủ thông tin");
                return;
            }

            //validate numbers
            long tongChi, tienO, tienAn, tienDiLai;
            try {
                tongChi = Long.parseLong(txtTongChi.getText().trim());
                tienO = Long.parseLong(txtTongChi.getText().trim());
                tienAn = Long.parseLong(txtTongChi.getText().trim());
                tienDiLai = Long.parseLong(txtTongChi.getText().trim());
            } catch (NumberFormatException ex) {
                JOptionPane.showMessageDialog(this, "Vui lòng nhập đúng định dạng số");
                return;
            }

            if(cTietKHTourDTO == null){
                if(cTietKHTourBUS.existedCTietKHTourWithID(txtMaCTietKHTour.getText()))
                    JOptionPane.showMessageDialog(null, "Mã chi tiết kế hoạch tour đã tồn tại, vui lòng nhập mã khác!");

                else{
                    CTietKHTourDTO cTietkHTourMoi = new CTietKHTourDTO(
                            txtMaCTietKHTour.getText(), txtNgayThucHien.getText(),
                            tongChi, tienO, tienAn,
                            tienDiLai, txtDiemDi.getText(), txtDiemDen.getText(), txtMaKHtour.getText()
                    );

                    boolean result = cTietKHTourBUS.addCTietKHTour(cTietkHTourMoi);
                    if(result) {
                        JOptionPane.showMessageDialog(this, "Đã thêm");
                        dispose();
                    }else{
                        JOptionPane.showMessageDialog(this, "Thêm thất bại");
                    }
                }
            }else{
                cTietKHTourDTO.setMaCTietKHTour(txtMaCTietKHTour.getText());
                cTietKHTourDTO.setNgayThucHien(txtNgayThucHien.getText());
                cTietKHTourDTO.setTongChi(tongChi);
                cTietKHTourDTO.setTienO(tienO);
                cTietKHTourDTO.setTienAn(tienAn);
                cTietKHTourDTO.setTienDiLai(tienDiLai);
                cTietKHTourDTO.setDiemDi(txtDiemDi.getText());
                cTietKHTourDTO.setDiemDen(txtDiemDen.getText());
                cTietKHTourDTO.setMaKHTour(maKHTour);

                cTietKHTourBUS.editCTietKHTour(cTietKHTourDTO); // edit by cTietKeHoachTourBus
            }
        });
    }

    private void cancel(){
        cancelBtn = createBtn("Hủy", UIColors.CANCEL);
        cancelBtn.addActionListener(e -> {
            dispose();
        });
    }

    private boolean isEmpty(JTextField... fields){
        for(JTextField field : fields){
            if(field.getText().trim().isEmpty()){
                return true;
            }
        }
        return false;
    }
}
