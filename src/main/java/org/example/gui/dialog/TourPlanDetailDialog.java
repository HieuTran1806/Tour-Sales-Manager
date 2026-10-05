package org.example.gui.dialog;

import com.toedter.calendar.JDateChooser;
import lombok.AccessLevel;
import lombok.experimental.FieldDefaults;
import org.example.bus.TourPlanDetailBUS;
import org.example.dto.TourPlanDetailDTO;
import org.example.gui.helper.UIColors;

import javax.swing.*;
import java.awt.*;
import java.math.BigDecimal;
import java.sql.Date;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;

import static java.sql.Date.valueOf;

@FieldDefaults(level = AccessLevel.PRIVATE)
public class TourPlanDetailDialog extends JDialog {
    JLabel jlbMaCTietKHTour, jlbTongChi, jlbTienO, jlbTienAn, jlbTienDiLai, jlbDiemDi, jlbDiemDen, jlbMaKHtour;

    JTextField txtMaCTietKHTour, txtNgayThucHien, txtTongChi, txtTienO, txtTienAn, txtTienDiLai, txtDiemDi, txtDiemDen, txtMaKHtour;

    JDateChooser date;

    JButton saveBtn, cancelBtn;

    TourPlanDetailDTO tourPlanDetailDTO;

    LocalDate today;
    DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");

    TourPlanDetailBUS bus;

    public TourPlanDetailDialog(String idTourPlan){
        bus = new TourPlanDetailBUS();
        today = LocalDate.now();

        setTitle(tourPlanDetailDTO == null ? "Thêm chi tiết kế hoạch tour" : "Sửa chi tiết kế hoạch Tour");
        setSize(300, 440);
        setLocationRelativeTo(null);
        setModal(true);

        init();
        if (tourPlanDetailDTO != null) {
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

        // row booking date
        date = new JDateChooser();
        date.setDateFormatString(String.valueOf(formatter));
        date.setDate(valueOf(LocalDate.now()));
        addFormRow(formPanel, "Ngày đặt: ",date);

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
        txtMaKHtour = new JTextField();
        txtMaKHtour.setEnabled(false);
        formPanel.add(txtMaKHtour);

        add(formPanel, BorderLayout.CENTER);
        add(southPanel, BorderLayout.SOUTH);
    }

    private void loadData(){
        txtMaCTietKHTour.setText(tourPlanDetailDTO.getIdTourPlanDetail());
        txtNgayThucHien.setText(String.valueOf(tourPlanDetailDTO.getDate()));
        txtTongChi.setText(tourPlanDetailDTO.getTotalExpenditure() + "");
        txtTienO.setText(tourPlanDetailDTO.getHousingCost() + "");
        txtTienAn.setText(tourPlanDetailDTO.getEatingCost() + "");
        txtTienDiLai.setText(tourPlanDetailDTO.getTravelingCost() + "");
        txtDiemDi.setText(tourPlanDetailDTO.getDepartureLocation());
        txtDiemDen.setText(tourPlanDetailDTO.getEndLocation());
        txtMaKHtour.setText(tourPlanDetailDTO.getIdTourPlan());
    }

    private void addFormRow(JPanel formPanel, String labelText, JComponent component) {
        JLabel label = new JLabel(labelText);

        label.setBorder(
                BorderFactory.createEmptyBorder(
                        5, 5, 5, 0
                )
        );

        formPanel.add(label);
        formPanel.add(component);
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
            BigDecimal tongChi, tienO, tienAn, tienDiLai;
            try {
                tongChi = BigDecimal.valueOf(Long.parseLong(txtTongChi.getText().trim()));
                tienO = BigDecimal.valueOf(Long.parseLong(txtTongChi.getText().trim()));
                tienAn = BigDecimal.valueOf(Long.parseLong(txtTongChi.getText().trim()));
                tienDiLai = BigDecimal.valueOf(Long.parseLong(txtTongChi.getText().trim()));
            } catch (NumberFormatException ex) {
                JOptionPane.showMessageDialog(this, "Vui lòng nhập đúng định dạng số");
                return;
            }
            Date selectedDate =
                    (Date) date.getDate();
            if (selectedDate == null) {
                JOptionPane.showMessageDialog(
                        this,
                        "Vui lòng chọn ngày đặt"
                );
                return;
            }

            LocalDateTime dateTime =
                    selectedDate.toInstant()
                            .atZone(
                                    ZoneId.systemDefault()
                            )
                            .toLocalDateTime();

            if(tourPlanDetailDTO == null){
                if(bus.existedCTietKHTourWithID(txtMaCTietKHTour.getText()))
                    JOptionPane.showMessageDialog(null, "Mã chi tiết kế hoạch tour đã tồn tại, vui lòng nhập mã khác!");

                else{
                    TourPlanDetailDTO newTourPlan = new TourPlanDetailDTO(
                            txtMaCTietKHTour.getText(), dateTime,
                            tongChi, tienO, tienAn,
                            tienDiLai, txtDiemDi.getText(), txtDiemDen.getText(), txtMaKHtour.getText()
                    );

                    boolean result = bus.addCTietKHTour(newTourPlan);
                    if(result) {
                        JOptionPane.showMessageDialog(this, "Đã thêm");
                        dispose();
                    }else{
                        JOptionPane.showMessageDialog(this, "Thêm thất bại");
                    }
                }
            }else{
                tourPlanDetailDTO.setIdTourPlanDetail(txtMaCTietKHTour.getText());
                tourPlanDetailDTO.setDate(LocalDateTime.parse(txtNgayThucHien.getText()));
                tourPlanDetailDTO.setTotalExpenditure(tongChi);
                tourPlanDetailDTO.setHousingCost(tienO);
                tourPlanDetailDTO.setEatingCost(tienAn);
                tourPlanDetailDTO.setTravelingCost(tienDiLai);
                tourPlanDetailDTO.setDepartureLocation(txtDiemDi.getText());
                tourPlanDetailDTO.setEndLocation(txtDiemDen.getText());

                tourPlanDetailDTO.setIdTourPlan(txtMaKHtour.getText());

                bus.editCTietKHTour(tourPlanDetailDTO); // edit by cTietKeHoachTourBus
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