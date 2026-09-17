package org.example.gui.dialog;

import lombok.AccessLevel;
import lombok.experimental.FieldDefaults;
import org.example.bus.NhanVienBUS;
import org.example.bus.KeHoachTourBUS;
import org.example.bus.TourBUS;
import org.example.dto.NhanVienDTO;
import org.example.dto.KeHoachTourDTO;
import org.example.dto.TourDTO;
import org.example.gui.panel.UIColors;

import javax.swing.*;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.ArrayList;
import java.util.Objects;

@FieldDefaults(level = AccessLevel.PRIVATE)
public class KeHoachTourDialog extends JDialog {
    // define jlabel and txt

    JLabel jlbMaKHTour, jlbNgayKhoiHanh, jlbNgayKetThuc, jlbTongSoVe, jlbTongChi, jlbSoVeConLai, jlbTrangThai, jlbMaTour, jlbMaNVHD;
    JTextField txtMaKHTour, txtNgayKhoiHanh, txtNgayKetThuc, txtTongSoVe, txtTongChi, txtSoVeConLai, txtMaTour;

    // combobox
    JComboBox<NhanVienDTO> cbStaff;
    DefaultComboBoxModel<NhanVienDTO> staffModel;

    JComboBox<TourDTO> cbTour;
    DefaultComboBoxModel<TourDTO> tourModel;

    JComboBox<String> cbStatus;
    DefaultComboBoxModel<String> statusModel;

    // define btn
    JButton saveBtn, cancelBtn;

    NhanVienBUS nhanVienBUS;
    TourBUS tourBUS;
    KeHoachTourBUS keHoachTourBUS;
    KeHoachTourDTO keHoachTourDTO;

    String maTour;

    // formatter
    LocalDate today;
    DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");

    public KeHoachTourDialog(KeHoachTourBUS keHoachTourBUS, KeHoachTourDTO keHoachTourDTO,TourBUS tourBUS, String maTour) {
        this.keHoachTourBUS = keHoachTourBUS;
        this.keHoachTourDTO = keHoachTourDTO;
        this.maTour = maTour;
        this.tourBUS = new TourBUS();
        this.nhanVienBUS = new NhanVienBUS();
        nhanVienBUS.docDSNV();

        // load staff combobox
        cbStaff = new JComboBox<>();
        cbTour = new JComboBox<>();
        cbStatus = new JComboBox<>();

        today = LocalDate.now();

        setTitle(keHoachTourDTO == null ? "Thêm kế hoạch tour" : "Sửa kế hoạch Tour");
        setSize(300, 440);
        setLocationRelativeTo(null);
        setModal(true);

        init();
        if (keHoachTourDTO != null) {
            loadData();
        }else{
            int soChoMacDinh = tourBUS.getVacantSpot(maTour);
            txtSoVeConLai.setText(String.valueOf(soChoMacDinh));
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

        //row soVeConLai
        jlbSoVeConLai = new JLabel("Số vé còn lại");
        jlbSoVeConLai.setBorder(BorderFactory.createEmptyBorder(5, 5, 5, 0));
        formPanel.add(jlbSoVeConLai);
        txtSoVeConLai = new JTextField();
        txtSoVeConLai.setEditable(false); // can't change by manual
        txtTongSoVe.getDocument().addDocumentListener(new DocumentListener() {
            @Override
            public void insertUpdate(DocumentEvent e) {
                updateSoVe();
            }

            @Override
            public void removeUpdate(DocumentEvent e) {
                updateSoVe();
            }

            @Override
            public void changedUpdate(DocumentEvent e) {
                updateSoVe();
            }

            private void updateSoVe(){
                String input = txtTongSoVe.getText().trim();
                int soChoMacDinh = tourBUS.getVacantSpot(maTour);

                if (input.isEmpty()) {
                    txtSoVeConLai.setText(String.valueOf(soChoMacDinh));
                }else{
                    try {
                        int tongSo = Integer.parseInt(input);
                        if (tongSo >= 0) {
                            txtSoVeConLai.setText(String.valueOf(soChoMacDinh - tongSo));
                        }
                    }
                     catch (NumberFormatException ex) {
                        txtSoVeConLai.setText("");
                    }
                }
            }
        });
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
        // load combo tour ID
        tourModel = new DefaultComboBoxModel<>();
        tourModel = CBTourPresent();
        cbTour.setModel(tourModel);
        if (tourModel.getSize() > 0) {
            cbTour.setSelectedIndex(0);
        }
        formPanel.add(cbTour); // center panel add combobox tours
        cbTour.addActionListener(e -> {
            String newIdTour = cbTour.getSelectedItem().toString();

            int soChoConLai = tourBUS.getVacantSpot(newIdTour);

            txtSoVeConLai.setText(String.valueOf(soChoConLai));
        });

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
        String matour = keHoachTourDTO.getMaTour();

        txtMaKHTour.setText(keHoachTourDTO.getMaKHTour());
        txtMaKHTour.setEditable(false);

        txtNgayKhoiHanh.setText(formatter.format(keHoachTourDTO.getNgayKhoiHanh()));
        txtNgayKetThuc.setText(formatter.format(keHoachTourDTO.getNgayKetThuc()));

        txtTongSoVe.setText(String.valueOf(keHoachTourDTO.getTongSoVe()));
        txtSoVeConLai.setText(String.valueOf(keHoachTourDTO.getSoVeConLai()));

        txtTongChi.setText(String.valueOf(keHoachTourDTO.getTongChiDuKien()));
        txtMaTour.setText(matour);
    }

    private DefaultComboBoxModel<TourDTO> CBTourPresent(){
        DefaultComboBoxModel<TourDTO> model = new DefaultComboBoxModel<>();

        ArrayList<TourDTO> lsTour = tourBUS.getAllTours();
        if (lsTour == null || lsTour.isEmpty())
            return model;

        for (TourDTO t : lsTour)
            model.addElement(t);
        return model;
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
            if(isEmpty(txtMaKHTour, txtNgayKhoiHanh, txtNgayKetThuc, txtTongSoVe, txtTongChi, txtSoVeConLai)){
                JOptionPane.showMessageDialog(this, "Vui lòng nhập đầy đủ thông tin");
                return;
            }

            //validate numbers
            int tongSoVe, soVeConLai;
            long tongChi;
            LocalDate ngayKhoiHanh;
            LocalDate ngayKetThuc;
            String trangThai = "";
            try {
                tongSoVe = Integer.parseInt(txtTongSoVe.getText().trim());
                tongChi = Long.parseLong(txtTongChi.getText().trim());
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
                    NhanVienDTO selectedStaff = getStaffSelected();
                    String status = getStatusSelected();
                    TourDTO selectedTour = getTourSelected();

                    KeHoachTourDTO keHoachTourMoi = new KeHoachTourDTO(
                            txtMaKHTour.getText(), ngayKhoiHanh,
                            ngayKetThuc, tongSoVe,
                            tongChi,
                            soVeConLai, status,
                            selectedTour.getMaTour(), selectedStaff.getMaNV()
                    );

                    // validate before add
                    boolean validated =  validatedForm(keHoachTourMoi);
                    if(validated){
                        keHoachTourBUS.addKeHoachTour(keHoachTourMoi); // edit by keHoachTourBus

                        JOptionPane.showMessageDialog(this, "Thêm thành công!");
                        dispose();
                    }
                }
            }else{
                keHoachTourDTO.setMaKHTour(txtMaKHTour.getText());
                keHoachTourDTO.setNgayKhoiHanh(LocalDate.parse(txtNgayKhoiHanh.getText(), formatter));
                keHoachTourDTO.setNgayKetThuc(LocalDate.parse(txtNgayKetThuc.getText(), formatter));
                keHoachTourDTO.setTongSoVe(tongSoVe);
                keHoachTourDTO.setTongChiDuKien(tongChi);
                keHoachTourDTO.setSoVeConLai(soVeConLai);
                keHoachTourDTO.setTrangThai(trangThai);
                TourDTO selectedTour = getTourSelected();
                keHoachTourDTO.setMaTour(selectedTour.getMaTour());
                NhanVienDTO selectedStaff = getStaffSelected();
                keHoachTourDTO.setMaNVHD(selectedStaff.getMaNV());

                boolean validated = validatedForm(keHoachTourDTO);
                if(validated){
                    keHoachTourBUS.editKeHoachTour(keHoachTourDTO); // edit by keHoachTourBus
                    JOptionPane.showMessageDialog(this, "Đã sửa ");
                    dispose();
                }
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

    private TourDTO getTourSelected(){
        return (TourDTO) cbTour.getSelectedItem();
    }

    private String getStatusSelected(){
        return Objects.requireNonNull(cbStatus.getSelectedItem()).toString();
    }

    private boolean validatedForm(KeHoachTourDTO dto){
        String error = keHoachTourBUS.validateKeHoachTour(dto);
        if(error != null){
            JOptionPane.showMessageDialog(this, error);
            return false;
        }

        return true;
    }
}
