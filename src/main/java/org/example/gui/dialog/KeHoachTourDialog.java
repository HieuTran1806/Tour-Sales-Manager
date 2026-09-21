package org.example.gui.dialog;

import com.toedter.calendar.JDateChooser;
import lombok.AccessLevel;
import lombok.experimental.FieldDefaults;
import org.example.bus.NhanVienBUS;
import org.example.bus.KeHoachTourBUS;
import org.example.bus.TourBUS;
import org.example.dto.NhanVienDTO;
import org.example.dto.KeHoachTourDTO;
import org.example.dto.TourDTO;
import org.example.gui.panel.UIColors;
import org.example.validate.ValidationException;

import javax.swing.*;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import java.awt.*;
import java.time.LocalDate;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Objects;

import static java.sql.Date.valueOf;

@FieldDefaults(level = AccessLevel.PRIVATE)
public class KeHoachTourDialog extends JDialog {
    // define jlabel and txt
    JLabel jlbMaKHTour, jlbNgayKhoiHanh, jlbNgayKetThuc, jlbTongSoVe, jlbTongChi, jlbSoVeConLai, jlbTrangThai, jlbMaTour, jlbMaNVHD;
    JTextField txtMaKHTour, txtTongSoVe, txtTongChi, txtSoVeConLai;
    JDateChooser jDepartureDate, jEndDate;


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
        jlbNgayKhoiHanh = new JLabel("Ngày khởi hành");
        jlbNgayKhoiHanh.setBorder(BorderFactory.createEmptyBorder(5, 5, 5, 0));
        formPanel.add(jlbNgayKhoiHanh);
        jDepartureDate = new JDateChooser();
        jDepartureDate.setDateFormatString("dd/MM/yyyy");
        jDepartureDate.setDate(valueOf(LocalDate.now()));
        formPanel.add(jDepartureDate);

        //row ngayKetThuc
        jlbNgayKetThuc = new JLabel("Ngày kết thúc");
        jlbNgayKetThuc.setBorder(BorderFactory.createEmptyBorder(5, 5, 5, 0));
        formPanel.add(jlbNgayKetThuc);
        jEndDate = new JDateChooser();
        jEndDate.setDateFormatString("dd/MM/yyyy");
        jEndDate.setDate(valueOf(LocalDate.now()));
        formPanel.add(jEndDate);

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
        txtMaKHTour.setText(keHoachTourDTO.getMaKHTour());
        txtMaKHTour.setEditable(false);

        jDepartureDate.setDate(
                java.sql.Date.valueOf(keHoachTourDTO.getNgayKhoiHanh())
        );
        jEndDate.setDate(
                java.sql.Date.valueOf(keHoachTourDTO.getNgayKhoiHanh())
        );

        txtTongSoVe.setText(String.valueOf(keHoachTourDTO.getTongSoVe()));
        txtSoVeConLai.setText(String.valueOf(keHoachTourDTO.getSoVeConLai()));

        txtTongChi.setText(String.valueOf(keHoachTourDTO.getTongChiDuKien()));

        // load status
        String oldStatus = keHoachTourDTO.getTrangThai();
        cbStatus.setSelectedItem(oldStatus);

        // load idTour
        for (int i = 0; i < cbTour.getItemCount(); i++) {
            TourDTO t = cbTour.getItemAt(i);
            if (t.getMaTour().equals(maTour)) {
                cbTour.setSelectedIndex(i);
                break;
            }
        }

        // load idStaff
        String idStaff = keHoachTourDTO.getMaNVHD();
        cbStaff.setSelectedItem(idStaff);
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
        statusModel = new DefaultComboBoxModel<>(cacTrangThai);

        // Gán model cho ComboBox
        cbStatus = new JComboBox<>(statusModel);
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
            //get all data
            String idKHT = txtMaKHTour.getText().trim();
            LocalDate departureDate = jDepartureDate.getDate() != null ? jDepartureDate.getDate().toInstant().atZone(ZoneId.systemDefault()).toLocalDate() : null;
            LocalDate endDate = jEndDate.getDate() != null ? jEndDate.getDate().toInstant().atZone(ZoneId.systemDefault()).toLocalDate() : null;
            String tongSoVe = txtTongSoVe.getText().trim();
            String tongChi = txtTongChi.getText().trim();
            String soVeConLai = txtSoVeConLai.getText().trim();

            // validate
            try {
                // validate data form
                validateDateOfKHT(departureDate, endDate);
                validateTickets(tongSoVe, soVeConLai);
                validateEstimateCost(tongChi);

                NhanVienDTO selectedStaff = getStaffSelected();
                String status = getStatusSelected();
                TourDTO selectedTour = getTourSelected();

                // add kehoachtour
                if (keHoachTourDTO == null) {
                    validateIdKHT(idKHT);

                    KeHoachTourDTO newKHT = new KeHoachTourDTO(
                            txtMaKHTour.getText(), departureDate,
                            endDate, Integer.parseInt(tongSoVe.trim()),
                            Long.parseLong(tongChi.trim()),
                            Integer.parseInt(soVeConLai.trim()), status,
                            selectedTour.getMaTour(), selectedStaff.getMaNV()
                    );

                    boolean validated = validatedForm(newKHT);
                    if (validated) {
                        keHoachTourBUS.addKeHoachTour(newKHT); // edit by keHoachTourBus

                        JOptionPane.showMessageDialog(this, "Thêm thành công!");
                        dispose();
                    }


                } else {
                    keHoachTourDTO.setNgayKhoiHanh(departureDate);
                    keHoachTourDTO.setNgayKetThuc(endDate);
                    keHoachTourDTO.setTongSoVe(Integer.parseInt(tongSoVe.trim()));
                    keHoachTourDTO.setTongChiDuKien(Long.parseLong(tongChi.trim()));
                    keHoachTourDTO.setSoVeConLai(Integer.parseInt(soVeConLai.trim()));
                    keHoachTourDTO.setTrangThai(status);
                    keHoachTourDTO.setMaTour(selectedTour.getMaTour());
                    keHoachTourDTO.setMaNVHD(selectedStaff.getMaNV());

                    boolean validated = validatedForm(keHoachTourDTO);
                    if (validated) {
                        keHoachTourBUS.editKeHoachTour(keHoachTourDTO); // edit by keHoachTourBus
                        JOptionPane.showMessageDialog(this, "Đã sửa ");
                        dispose();
                    }
                }
            } catch (ValidationException validationException) {
                JOptionPane.showMessageDialog(
                        this,
                        validationException.getMessage(),
                        "Lỗi nhập liệu",
                        JOptionPane.ERROR_MESSAGE
                );
            }
        });
    }

    private void validateIdKHT(String id) throws ValidationException{
        if (id.isEmpty()) throw new ValidationException("Mã kế hoạch tour không được để trống!");

        if (!id.matches("^KH\\d{3}$"))  throw new ValidationException("Mã kế hoạch tour phải có dạng KHxxx!");

        if(isIdKHTDuplicated(id)) throw new ValidationException("Mã kế hoạch tour đã tồn tại!");
    }

    private boolean isIdKHTDuplicated(String id){
        for (KeHoachTourDTO ls : keHoachTourBUS.getAllKeHoachTours()) {
            if (ls.getMaKHTour().equals(id))
                return true;
        }
        return false;
    }

    private void validateTickets(String prices, String tickets) throws ValidationException{
        if (prices.isEmpty()) throw new ValidationException("Giá vé không được để trống!");
        if (tickets.isEmpty()) throw new ValidationException("Số vé còn lại không được để trống!");


        long price;
        try{
            price = Long.parseLong(prices.trim());
        }catch (NumberFormatException e) {
            throw new ValidationException("Giá vé phải là số!");
        }
        //
        if (price < 0) {
            throw new ValidationException("Giá vé không được âm!");
        }

        long remainingTickets;
        try{
            remainingTickets = Long.parseLong(tickets.trim());
        }catch (NumberFormatException e) {
            throw new ValidationException("Số vé còn lại phải là số!");
        }
        //
        if (remainingTickets < 0) {
            throw new ValidationException("Số vé còn lại không được âm!");
        }
    }


    private void validateEstimateCost(String cash) throws ValidationException{
        if (cash.isEmpty()) throw new ValidationException("Tổng chi dự kiến không được để trống!");

        long money;
        try{
            money = Long.parseLong(cash.trim());
        }catch (NumberFormatException e) {
            throw new ValidationException("Trường tổng chi phải là số!");
        }
        //
        if (money < 0) {
            throw new ValidationException("Tổng chi không được âm!");
        }
    }



    private void validateDateOfKHT(LocalDate startDate, LocalDate endDate) throws ValidationException{
        if (startDate == null) throw new ValidationException("Ngày khởi hành không được để trống!");

        if (endDate == null) throw new ValidationException("Ngày kết thúc không được để trống!");

        else if (startDate.isAfter(endDate)) throw new ValidationException("Ngày khởi hành phải sau hơn ngày kết thúc!");
    }

    public void cancel(){
        cancelBtn = createBtn("Hủy", UIColors.CANCEL);
        cancelBtn.addActionListener(e -> {
            dispose();
        });
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
