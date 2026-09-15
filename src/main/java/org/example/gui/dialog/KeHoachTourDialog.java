package org.example.gui.dialog;

import org.example.bus.NhanVienBUS;
import org.example.bus.KeHoachTourBUS;
import org.example.dto.NhanVienDTO;
import org.example.dto.KeHoachTourDTO;
import org.example.gui.panel.UIColors;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.ArrayList;

public class KeHoachTourDialog extends JDialog {
    // define jlabel and txt
    private JLabel jlbMaKHTour, jlbNgayKhoiHanh, jlbNgayKetThuc, jlbTongSoVe, jlbTongChi, jlbTongThu, jlbSoVeConLai, jlbTrangThai, jlbMaTour, jlbMaNVHD;
    private JTextField txtMaKHTour, txtNgayKhoiHanh, txtNgayKetThuc, txtTongSoVe, txtTongChi, txtTongThu, txtSoVeConLai, txtMaTour;

    // combobox
    private JComboBox<NhanVienDTO> cbStaff;
    private DefaultComboBoxModel<NhanVienDTO> staffModel;
    private JComboBox<String> cbStatus;
    private DefaultComboBoxModel<String> statusModel;

    // define btn
    private JButton saveBtn, cancelBtn;

    private NhanVienBUS nhanVienBUS;
    private KeHoachTourBUS keHoachTourBUS;
    private KeHoachTourDTO keHoachTourDTO;

    private String maTour;

    // formatter
    private LocalDate today;
    private DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");

    public KeHoachTourDialog(KeHoachTourBUS keHoachTourBUS, KeHoachTourDTO keHoachTourDTO, String maTour) {
        this.keHoachTourBUS = keHoachTourBUS;
        this.keHoachTourDTO = keHoachTourDTO;
        this.maTour = maTour;
        this.nhanVienBUS = new NhanVienBUS();
        nhanVienBUS.docDSNV();

        // load staff combobox
        cbStaff = new JComboBox<>();
        cbStatus = new JComboBox<>();

        today = LocalDate.now();

        setTitle(keHoachTourDTO == null ? "Thêm kế hoạch tour" : "Sửa kế hoạch Tour");
        setSize(300, 440);
        setLocationRelativeTo(null);
        setModal(true);

        init();
        if (keHoachTourDTO != null) {
            loadData();
        }
    }

    private void init(){
        setLayout(new BorderLayout());

        JPanel formPanel = new JPanel(new GridLayout(11, 2)); // at center

        JPanel southPanel = new JPanel(new FlowLayout());

        // Save button
        save();
        southPanel.add(saveBtn);

        //Cancel button
        cancel();
        southPanel.add(cancelBtn);

        //row maKHTour
        jlbMaKHTour = new JLabel("Mã kế hoạch tour");
        jlbMaKHTour.setBorder(BorderFactory.createEmptyBorder(5, 5, 5, 0));
        formPanel.add(jlbMaKHTour);
        txtMaKHTour = new JTextField();
        formPanel.add(txtMaKHTour);

        // row ngayKhoiHanh
        jlbNgayKhoiHanh = new JLabel("Ngày khơi hành");
        jlbNgayKhoiHanh.setBorder(BorderFactory.createEmptyBorder(5, 5, 5, 0));
        formPanel.add(jlbNgayKhoiHanh);
        // txt
        txtNgayKhoiHanh = new JTextField();
        txtNgayKhoiHanh.setText(today.format(formatter));
        formPanel.add(txtNgayKhoiHanh);

        //row ngayKetThuc
        jlbNgayKetThuc = new JLabel("Ngày kết thúc");
        jlbNgayKetThuc.setBorder(BorderFactory.createEmptyBorder(5, 5, 5, 0));
        formPanel.add(jlbNgayKetThuc);
        //txt
        txtNgayKetThuc = new JTextField();
        LocalDate endDate = today.plusDays(1);
        txtNgayKetThuc.setText(endDate.format(formatter));
        formPanel.add(txtNgayKetThuc);

        //row tongSoVe
        jlbTongSoVe = new JLabel("Tổng số vé");
        jlbTongSoVe.setBorder(BorderFactory.createEmptyBorder(5, 5, 5, 0));
        formPanel.add(jlbTongSoVe);
        txtTongSoVe = new JTextField();
        formPanel.add(txtTongSoVe);

        //row tongChi
        jlbTongChi = new JLabel("Tổng chi dự kiến");
        jlbTongChi.setBorder(BorderFactory.createEmptyBorder(5, 5, 5, 0));
        formPanel.add(jlbTongChi);
        txtTongChi = new JTextField();
        formPanel.add(txtTongChi);

        //row tongThu
        jlbTongThu = new JLabel("Tổng thu");
        jlbTongThu.setBorder(BorderFactory.createEmptyBorder(5, 5, 5, 0));
        formPanel.add(jlbTongThu);
        txtTongThu = new JTextField();
        formPanel.add(txtTongThu);

        //row soVeConLai
        jlbSoVeConLai = new JLabel("Số vé còn lại");
        jlbSoVeConLai.setBorder(BorderFactory.createEmptyBorder(5, 5, 5, 0));
        formPanel.add(jlbSoVeConLai);
        txtSoVeConLai = new JTextField();
        formPanel.add(txtSoVeConLai);

        //row trangThai
        jlbTrangThai = new JLabel("Trạng thái");
        jlbTrangThai.setBorder(BorderFactory.createEmptyBorder(5, 5, 5, 0));
        formPanel.add(jlbTrangThai);
        //load status
        loadStatusCombobox();
        formPanel.add(cbStatus);

        //row maTour
        jlbMaTour = new JLabel("Mã tour");
        jlbMaTour.setBorder(BorderFactory.createEmptyBorder(5, 5, 5, 0));
        formPanel.add(jlbMaTour);
        txtMaTour = new JTextField(maTour);
        txtMaTour.setEnabled(false);
        formPanel.add(txtMaTour);

        //row maNVHD
        jlbMaNVHD = new JLabel("Mã nhân viên hướng dẫn");
        jlbMaNVHD.setBorder(BorderFactory.createEmptyBorder(5, 5, 5, 0));
        formPanel.add(jlbMaNVHD);

        // Load combo staff
        staffModel = new DefaultComboBoxModel<>();
        staffModel = CBStaffPresent();
        cbStaff.setModel(staffModel);
        if (staffModel.getSize() > 0) {
            cbStaff.setSelectedIndex(0);
        }
        formPanel.add(cbStaff); // center panel add combobox tours

        add(formPanel, BorderLayout.CENTER);
        add(southPanel, BorderLayout.SOUTH);
    }

    private void loadData () {
        txtMaKHTour.setText(keHoachTourDTO.getMaKHTour());

        txtNgayKhoiHanh.setText(formatter.format(keHoachTourDTO.getNgayKhoiHanh()));
        txtNgayKetThuc.setText(formatter.format(keHoachTourDTO.getNgayKetThuc()));

        txtTongSoVe.setText(keHoachTourDTO.getTongSoVe() + "");
        txtTongChi.setText(keHoachTourDTO.getTongChiDuKien() + "");
        txtTongThu.setText(keHoachTourDTO.getTongThuDuKien() + "");
        txtMaTour.setText(keHoachTourDTO.getMaTour());
        // combobox manvhd
    }

    private DefaultComboBoxModel<NhanVienDTO> CBStaffPresent(){
        DefaultComboBoxModel<NhanVienDTO> model = new DefaultComboBoxModel<>();

        nhanVienBUS.docDSNV();

        ArrayList<NhanVienDTO> lsStaff = nhanVienBUS.dsNV;
        if (lsStaff == null || lsStaff.isEmpty())
            return model;

        for (NhanVienDTO t : lsStaff)
            model.addElement(t);
        return model;
    }

    private void loadStatusCombobox(){
        String[] cacTrangThai = {"Sắp khởi hành", "Đang diễn ra", "Đã kết thúc", "Đã hủy"};

        // Đưa mảng vào DefaultComboBoxModel
        DefaultComboBoxModel<String> model = new DefaultComboBoxModel<>(cacTrangThai);

        // Gán model cho ComboBox
        cbStatus = new JComboBox<>(model);
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
            if(isEmpty(txtMaKHTour, txtNgayKhoiHanh, txtNgayKetThuc, txtTongSoVe, txtTongChi, txtTongThu, txtSoVeConLai)){
                JOptionPane.showMessageDialog(this, "Vui lòng nhập đầy đủ thông tin");
                return;
            }

            //validate numbers
            int tongSoVe, soVeConLai;
            long tongChi, tongThu;
            LocalDate ngayKhoiHanh;
            LocalDate ngayKetThuc;
            String trangThai = "";
            try {
                tongSoVe = Integer.parseInt(txtTongSoVe.getText().trim());
                tongChi = Long.parseLong(txtTongChi.getText().trim());
                tongThu = Long.parseLong(txtTongThu.getText().trim());
                soVeConLai = Integer.parseInt(txtSoVeConLai.getText().trim());
                trangThai = cbStatus.getSelectedItem().toString();
            } catch (NumberFormatException ex) {
                JOptionPane.showMessageDialog(this, "Vui lòng nhập đúng định dạng số");
                return;
            }

            try {
                ngayKhoiHanh = LocalDate.parse(txtNgayKhoiHanh.getText(), formatter);
                ngayKetThuc = LocalDate.parse(txtNgayKetThuc.getText(), formatter);
            } catch (DateTimeParseException ex) {
                JOptionPane.showMessageDialog(this, "Ngày phải đúng định dạng dd/mm/yyyy");
                return;
            }

            if(keHoachTourDTO == null){
                if(keHoachTourBUS.existedKeHoachTourWithID(txtMaKHTour.getText()))
                    JOptionPane.showMessageDialog(null, "Mã kế hoạch tour đã tồn tại, vui lòng nhập mã khác!");
                else{
                    KeHoachTourDTO keHoachTourMoi = null;
                    NhanVienDTO selectedStaff = getStaffSelected();
                    String status = getStatusSelected();

                    keHoachTourMoi = new KeHoachTourDTO(
                            txtMaKHTour.getText(), ngayKhoiHanh,
                            ngayKetThuc, tongSoVe,
                            tongChi, tongThu,
                            soVeConLai, status,
                            txtMaTour.getText(), selectedStaff.getMaNV()
                    );

                    // validate before add
                    String error = keHoachTourBUS.validateKeHoachTour(keHoachTourMoi);
                    if(error == null){
                        boolean result = keHoachTourBUS.addKeHoachTour(keHoachTourMoi);
                        if(result) {
                            JOptionPane.showMessageDialog(this, "Đã thêm");
                            dispose();
                        }else{
                            JOptionPane.showMessageDialog(this, "Thêm thất bại");
                        }
                    }else{
                        JOptionPane.showMessageDialog(this, error);
                        this.requestFocus();
                    }
                }
                dispose();
            }else{
                keHoachTourDTO.setMaKHTour(txtMaKHTour.getText());
                keHoachTourDTO.setNgayKhoiHanh(LocalDate.parse(txtNgayKhoiHanh.getText(), formatter));
                keHoachTourDTO.setNgayKetThuc(LocalDate.parse(txtNgayKetThuc.getText(), formatter));
                keHoachTourDTO.setTongSoVe(tongSoVe);
                keHoachTourDTO.setTongChiDuKien(tongChi);
                keHoachTourDTO.setTongThuDuKien(tongThu);
                keHoachTourDTO.setSoVeConLai(soVeConLai);
                keHoachTourDTO.setTrangThai(trangThai);
                keHoachTourDTO.setMaTour(txtMaTour.getText());
                NhanVienDTO selectedStaff = getStaffSelected();
                keHoachTourDTO.setMaNVHD(selectedStaff.getMaNV());

                keHoachTourBUS.editKeHoachTour(keHoachTourDTO); // edit by keHoachTourBus
                JOptionPane.showMessageDialog(this, "Đã chỉnh sửa ");
                dispose();
            }
        });
    }

    public void cancel(){
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

    private NhanVienDTO getStaffSelected(){
        return (NhanVienDTO) cbStaff.getSelectedItem();
    }

    private String getStatusSelected(){
        return cbStatus.getSelectedItem().toString();
    }
}
