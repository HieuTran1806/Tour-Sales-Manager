package org.toursalesmanager.gui.dialog;

import com.toedter.calendar.JDateChooser;
import lombok.AccessLevel;
import lombok.experimental.FieldDefaults;
import org.toursalesmanager.bus.TourPlanDetailBUS;
import org.toursalesmanager.dto.TourPlanDetailDTO;
import org.toursalesmanager.gui.component.ButtonFactory;
import org.toursalesmanager.gui.helper.AddComponentToForm;
import org.toursalesmanager.gui.helper.UIColors;

import javax.swing.*;
import java.awt.*;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Date;

@FieldDefaults(level = AccessLevel.PRIVATE)
public class TourPlanDetailDialog extends JDialog {

    JTextField txtMaCTietKHTour, txtTongChi, txtTienO, txtTienAn, txtTienDiLai, txtDiemDi, txtDiemDen, txtMaKHtour;

    JDateChooser date;

    JButton saveBtn, cancelBtn;

    final String idTourPlan;
    final TourPlanDetailDTO tourPlanDetailDTO; // null: thêm mới, khác null: sửa

    final TourPlanDetailBUS bus;

    public TourPlanDetailDialog(Window owner, String idTourPlan, TourPlanDetailDTO tourPlanDetailDTO){
        super(owner, ModalityType.APPLICATION_MODAL);
        this.bus = new TourPlanDetailBUS();
        this.idTourPlan = idTourPlan;
        this.tourPlanDetailDTO = tourPlanDetailDTO;

        setTitle(tourPlanDetailDTO == null ? "Thêm chi tiết kế hoạch tour" : "Sửa chi tiết kế hoạch Tour");
        setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);

        init();
        if (tourPlanDetailDTO != null) {
            loadData();
        } else {
            txtMaCTietKHTour.setText(bus.getNextTourPlanDetailId());
            txtMaKHtour.setText(idTourPlan);
        }

        setSize(420, 440);
        setLocationRelativeTo(owner); // hiển thị giữa màn hình cha
    }

    private void init(){
        setLayout(new BorderLayout());

        JPanel formPanel = new JPanel(new GridLayout(0, 2, 10, 10)); // at center
        formPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        JPanel southPanel = new JPanel(new FlowLayout());

        // Save button
        save();
        southPanel.add(saveBtn);

        //Cancel button
        cancel();
        southPanel.add(cancelBtn);

        //row MaCTietKHTour
        txtMaCTietKHTour = new JTextField();
        txtMaCTietKHTour.setEditable(false);
        AddComponentToForm.addFormRow(formPanel, "Mã chi tiết kế hoạch tour", txtMaCTietKHTour);

        // row ngay thuc hien
        date = new JDateChooser();
        date.setDateFormatString("dd/MM/yyyy");
        date.setDate(toDate(LocalDate.now()));
        AddComponentToForm.addFormRow(formPanel, "Ngày thực hiện", date);

        //row TongChi
        txtTongChi = new JTextField();
        AddComponentToForm.addFormRow(formPanel, "Tổng chi", txtTongChi);

        //row TienO
        txtTienO = new JTextField();
        AddComponentToForm.addFormRow(formPanel, "Tiền ở", txtTienO);

        //row TienAn
        txtTienAn = new JTextField();
        AddComponentToForm.addFormRow(formPanel, "Tiền ăn", txtTienAn);

        //row TienDiLai
        txtTienDiLai = new JTextField();
        AddComponentToForm.addFormRow(formPanel, "Tiền đi lại", txtTienDiLai);

        //row DiemDi
        txtDiemDi = new JTextField();
        AddComponentToForm.addFormRow(formPanel, "Điểm đi", txtDiemDi);

        //row DiemDen
        txtDiemDen = new JTextField();
        AddComponentToForm.addFormRow(formPanel, "Điểm đến", txtDiemDen);

        //row MaKHtour
        txtMaKHtour = new JTextField();
        txtMaKHtour.setEditable(false);
        AddComponentToForm.addFormRow(formPanel, "Mã kế hoạch tour", txtMaKHtour);

        add(formPanel, BorderLayout.CENTER);
        add(southPanel, BorderLayout.SOUTH);
    }

    private void loadData(){
        txtMaCTietKHTour.setText(tourPlanDetailDTO.getIdTourPlanDetail());
        date.setDate(tourPlanDetailDTO.getDate() == null
                ? null
                : toDate(tourPlanDetailDTO.getDate().toLocalDate()));
        txtTongChi.setText(toText(tourPlanDetailDTO.getTotalExpenditure()));
        txtTienO.setText(toText(tourPlanDetailDTO.getHousingCost()));
        txtTienAn.setText(toText(tourPlanDetailDTO.getEatingCost()));
        txtTienDiLai.setText(toText(tourPlanDetailDTO.getTravelingCost()));
        txtDiemDi.setText(tourPlanDetailDTO.getDepartureLocation());
        txtDiemDen.setText(tourPlanDetailDTO.getEndLocation());
        txtMaKHtour.setText(tourPlanDetailDTO.getIdTourPlan());
    }

    private void save(){
        saveBtn = ButtonFactory.create("Lưu", UIColors.SAVE);
        saveBtn.addActionListener(e -> {
            if(isEmpty(txtMaCTietKHTour, txtTongChi, txtTienO, txtTienAn, txtTienDiLai, txtDiemDi, txtDiemDen)){
                JOptionPane.showMessageDialog(this, "Vui lòng nhập đầy đủ thông tin");
                return;
            }

            //validate numbers
            BigDecimal tongChi, tienO, tienAn, tienDiLai;
            try {
                tongChi = parseMoney(txtTongChi);
                tienO = parseMoney(txtTienO);
                tienAn = parseMoney(txtTienAn);
                tienDiLai = parseMoney(txtTienDiLai);
            } catch (NumberFormatException ex) {
                JOptionPane.showMessageDialog(this, "Các khoản tiền phải là số nguyên không âm");
                return;
            }

            Date selectedDate = date.getDate();
            if (selectedDate == null) {
                JOptionPane.showMessageDialog(this, "Vui lòng chọn ngày thực hiện");
                return;
            }
            LocalDateTime dateTime = selectedDate.toInstant()
                    .atZone(ZoneId.systemDefault())
                    .toLocalDate()
                    .atStartOfDay();

            try {
                if(tourPlanDetailDTO == null){
                    String id = txtMaCTietKHTour.getText().trim();
                    if(bus.existedCTietKHTourWithID(id)) {
                        JOptionPane.showMessageDialog(this, "Mã chi tiết kế hoạch tour đã tồn tại, vui lòng thử lại!");
                        txtMaCTietKHTour.setText(bus.getNextTourPlanDetailId());
                        return;
                    }

                    TourPlanDetailDTO newDetail = new TourPlanDetailDTO(
                            id, dateTime,
                            tongChi, tienO, tienAn, tienDiLai,
                            txtDiemDi.getText().trim(), txtDiemDen.getText().trim(), idTourPlan
                    );

                    if(bus.addCTietKHTour(newDetail)) {
                        JOptionPane.showMessageDialog(this, "Đã thêm chi tiết kế hoạch tour");
                        dispose();
                    }else{
                        JOptionPane.showMessageDialog(this, "Thêm thất bại");
                    }
                }else{
                    tourPlanDetailDTO.setDate(dateTime);
                    tourPlanDetailDTO.setTotalExpenditure(tongChi);
                    tourPlanDetailDTO.setHousingCost(tienO);
                    tourPlanDetailDTO.setEatingCost(tienAn);
                    tourPlanDetailDTO.setTravelingCost(tienDiLai);
                    tourPlanDetailDTO.setDepartureLocation(txtDiemDi.getText().trim());
                    tourPlanDetailDTO.setEndLocation(txtDiemDen.getText().trim());

                    if(bus.editCTietKHTour(tourPlanDetailDTO)) {
                        JOptionPane.showMessageDialog(this, "Đã cập nhật chi tiết kế hoạch tour");
                        dispose();
                    }else{
                        JOptionPane.showMessageDialog(this, "Cập nhật thất bại");
                    }
                }
            } catch (RuntimeException ex) {
                JOptionPane.showMessageDialog(this, ex.getMessage(), "Lỗi", JOptionPane.ERROR_MESSAGE);
            }
        });
    }

    private void cancel(){
        cancelBtn = ButtonFactory.create("Hủy", UIColors.CANCEL);
        cancelBtn.addActionListener(e -> dispose());
    }

    private BigDecimal parseMoney(JTextField field){
        BigDecimal value = new BigDecimal(field.getText().trim());
        if (value.signum() < 0 || value.stripTrailingZeros().scale() > 0) {
            throw new NumberFormatException();
        }
        return value;
    }

    private String toText(BigDecimal value){
        return value == null ? "" : value.toPlainString();
    }

    private Date toDate(LocalDate localDate){
        return Date.from(localDate.atStartOfDay(ZoneId.systemDefault()).toInstant());
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
