package org.toursalesmanager.gui.dialog;

import org.toursalesmanager.gui.helper.WindowHelper;

import com.toedter.calendar.JDateChooser;
import lombok.AccessLevel;
import lombok.experimental.FieldDefaults;
import org.toursalesmanager.bus.StaffBUS;
import org.toursalesmanager.bus.TourPlanBUS;
import org.toursalesmanager.bus.TourBUS;
import org.toursalesmanager.dto.StaffDTO;
import org.toursalesmanager.dto.TourPlanDTO;
import org.toursalesmanager.dto.TourDTO;
import org.toursalesmanager.gui.component.ButtonFactory;
import org.toursalesmanager.gui.helper.AddComponentToForm;
import org.toursalesmanager.gui.helper.UIColors;
import org.toursalesmanager.validate.ValidationException;

import javax.swing.*;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import java.awt.*;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.Objects;

import static java.sql.Date.valueOf;

@FieldDefaults(level = AccessLevel.PRIVATE)
public class TourPlanDialog extends JDialog {
    // define jlabel and txt
    JLabel jlbMaKHTour, jlbNgayKhoiHanh, jlbNgayKetThuc, jlbTongSoVe, jlbTongChi, jlbSoVeConLai, jlbTrangThai, jlbMaTour, jlbMaNVHD;
    JTextField txtMaKHTour, txtTongSoVe, txtTongChi, txtSoVeConLai;
    JDateChooser jDepartureDate, jEndDate;


    // combobox
    JComboBox<StaffDTO> cbStaff;
    DefaultComboBoxModel<StaffDTO> staffModel;

    JComboBox<TourDTO> cbTour;
    DefaultComboBoxModel<TourDTO> tourModel;

    JComboBox<String> cbStatus;
    DefaultComboBoxModel<String> statusModel;

    // define btn
    JButton saveBtn, cancelBtn;

    StaffBUS staffBUS;
    TourBUS tourBUS;
    TourPlanBUS tourPlanBUS;
    TourPlanDTO tourPlanDTO;

    String maTour;

    // formatter
    String formatter = ("dd/MM/yyyy");

    public TourPlanDialog(TourPlanBUS tourPlanBUS, TourPlanDTO tourPlanDTO, TourBUS tourBUS, String maTour) {
        this.tourPlanBUS = tourPlanBUS;
        this.tourPlanDTO = tourPlanDTO;
        this.maTour = maTour;
        this.tourBUS = new TourBUS();
        this.staffBUS = new StaffBUS();

        staffBUS.getAllStaffs();

        // load staff combobox
        cbStaff = new JComboBox<>();
        cbTour = new JComboBox<>();
        cbStatus = new JComboBox<>();

        setTitle(tourPlanDTO == null ? "Thêm kế hoạch tour" : "Sửa kế hoạch Tour");
        setSize(300, 440);
        WindowHelper.centerOnParent(this);
        setModal(true);

        init();
        requestFocusTourPlanDepartureDate();

        if (tourPlanDTO != null) {
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
        txtMaKHTour = new JTextField();
        txtMaKHTour.setEditable(false);
        if (tourPlanDTO == null) {
            txtMaKHTour.setText(tourPlanBUS.getNextTourPlanId());
        }
        AddComponentToForm.addFormRow(formPanel,"Mã kế hoạch tour", txtMaKHTour);

        // row ngayKhoiHanh
        jDepartureDate = new JDateChooser();
        jDepartureDate.setDateFormatString(formatter);
        jDepartureDate.setDate(valueOf(LocalDate.now()));
        AddComponentToForm.addFormRow(formPanel, "Ngày khởi hành", jDepartureDate);

        //row ngayKetThuc
        jEndDate = new JDateChooser();
        jEndDate.setDateFormatString(formatter);
        jEndDate.setDate(valueOf(LocalDate.now()));
        AddComponentToForm.addFormRow(formPanel, "Ngày kết thúc", jEndDate);

        initTicketFields();
        AddComponentToForm.addFormRow(formPanel, "Tổng số vé", txtTongSoVe);

        //row tongChi
        txtTongChi = new JTextField();
        AddComponentToForm.addFormRow(formPanel, "Tổng chi dự kiến", txtTongChi);

        //row soVeConLai
        AddComponentToForm.addFormRow(formPanel, "Số vé còn lại", txtSoVeConLai);

        //row trangThai
        //load status
        loadStatusCombobox();
        AddComponentToForm.addFormRow(formPanel, "Trạng thái", cbStatus);


        //row maTour
        // load combo tour ID
        tourModel = CBTourPresent();
        cbTour.setModel(tourModel);
        selectTour(tourPlanDTO == null ? maTour : tourPlanDTO.getIdTour());
        AddComponentToForm.addFormRow(formPanel, "Mã tour", cbTour);
        addTourSelectionListener();

        //row maNVHD
        // Load combo staff
        staffModel = new DefaultComboBoxModel<>();
        staffModel = CBStaffPresent();
        cbStaff.setModel(staffModel);
        if (staffModel.getSize() > 0) {
            cbStaff.setSelectedIndex(0);
        }
        AddComponentToForm.addFormRow(formPanel, "Mã nhân viên hướng dẫn", cbStaff);

        add(formPanel, BorderLayout.CENTER);
        add(southPanel, BorderLayout.SOUTH);
    }

    private void loadData () {
        txtMaKHTour.setText(tourPlanDTO.getIdTourPlan());
        txtMaKHTour.setEditable(false);

        jDepartureDate.setDate(
                java.sql.Date.valueOf(tourPlanDTO.getDepartureDate())
        );
        jEndDate.setDate(
                java.sql.Date.valueOf(tourPlanDTO.getEndDate())
        );

        txtTongSoVe.setText(String.valueOf(tourPlanDTO.getTickets()));
        txtSoVeConLai.setText(String.valueOf(tourPlanDTO.getRemainingTickets()));

        txtTongChi.setText(String.valueOf(tourPlanDTO.getEstimatedCost()));

        // load status
        String oldStatus = tourPlanDTO.getStatus();
        cbStatus.setSelectedItem(oldStatus);

        // load idStaff
        String idStaff = tourPlanDTO.getIdTourGuide();
        cbStaff.setSelectedItem(idStaff);
    }

    private void addTicketCountListener() {
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
        });
    }

    private void initTicketFields() {
        txtTongSoVe = new JTextField();
        txtSoVeConLai = new JTextField();
        txtSoVeConLai.setEditable(false);
        addTicketCountListener();
    }

    private void addTourSelectionListener() {
        cbTour.addActionListener(e -> updateSoVe());
    }

    private void selectTour(String idTour) {
        for (int i = 0; i < cbTour.getItemCount(); i++) {
            if (Objects.equals(cbTour.getItemAt(i).getIdTour(), idTour)) {
                cbTour.setSelectedIndex(i);
                return;
            }
        }
        if (cbTour.getItemCount() > 0) {
            cbTour.setSelectedIndex(0);
        }
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

    private DefaultComboBoxModel<StaffDTO> CBStaffPresent(){
        DefaultComboBoxModel<StaffDTO> model = new DefaultComboBoxModel<>();

        staffBUS.getAllStaffs();

        ArrayList<StaffDTO> lsStaff = staffBUS.dsNV;
        if (lsStaff == null || lsStaff.isEmpty())
            return model;

        for (StaffDTO t : lsStaff)
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

    public void save(){
        saveBtn = ButtonFactory.create("Lưu", UIColors.SAVE);
        saveBtn.addActionListener(e -> {
            //get all data
            LocalDate departureDate = jDepartureDate.getDate() != null ? jDepartureDate.getDate().toInstant().atZone(ZoneId.systemDefault()).toLocalDate() : null;
            LocalDate endDate = jEndDate.getDate() != null ? jEndDate.getDate().toInstant().atZone(ZoneId.systemDefault()).toLocalDate() : null;
            String tongSoVe = txtTongSoVe.getText().trim();
            String tongChi = txtTongChi.getText().trim();

            // validate
            try {
                // validate data form
                validateDateOfKHT(departureDate, endDate);
                validateEstimateCost(tongChi);

                StaffDTO selectedStaff = getStaffSelected();
                String status = getStatusSelected();
                TourDTO selectedTour = getTourSelected();
                int remainingTickets = validateTickets(tongSoVe, selectedTour);

                // add kehoachtour
                if (tourPlanDTO == null) {
                    TourPlanDTO newKHT = new TourPlanDTO(
                            txtMaKHTour.getText(), departureDate,
                            endDate, Integer.parseInt(tongSoVe.trim()),
                            BigDecimal.valueOf(Long.parseLong(tongChi.trim())),
                            remainingTickets, status,
                            selectedTour.getIdTour(), selectedStaff.getIdStaff()
                    );

                    boolean validated = validatedForm(newKHT);
                    if (validated) {
                        if (tourPlanBUS.addKeHoachTour(newKHT)) {
                            JOptionPane.showMessageDialog(this, "Thêm thành công!");
                            dispose();
                        } else {
                            JOptionPane.showMessageDialog(
                                    this,
                                    "Không thể thêm kế hoạch tour.",
                                    "Lỗi",
                                    JOptionPane.ERROR_MESSAGE
                            );
                        }
                    }


                } else {
                    tourPlanDTO.setDepartureDate(departureDate);
                    tourPlanDTO.setEndDate(endDate);
                    tourPlanDTO.setTickets(Integer.parseInt(tongSoVe.trim()));
                    tourPlanDTO.setEstimatedCost(BigDecimal.valueOf(Long.parseLong(tongChi)));
                    tourPlanDTO.setRemainingTickets(remainingTickets);
                    tourPlanDTO.setStatus(status);
                    tourPlanDTO.setIdTour(selectedTour.getIdTour());
                    tourPlanDTO.setIdTourGuide(selectedStaff.getIdStaff());

                    boolean validated = validatedForm(tourPlanDTO);
                    if (validated) {
                        tourPlanBUS.editKeHoachTour(tourPlanDTO); // edit by keHoachTourBus
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

    private int validateTickets(String totalTicketsInput, TourDTO selectedTour) throws ValidationException {
        if (totalTicketsInput.isEmpty()) {
            throw new ValidationException("Tổng số vé không được để trống!");
        }
        if (selectedTour == null) {
            throw new ValidationException("Vui lòng chọn tour!");
        }

        int totalTickets;
        try{
            totalTickets = Integer.parseInt(totalTicketsInput);
        }catch (NumberFormatException e) {
            throw new ValidationException("Tổng số vé phải là số nguyên!");
        }
        if (totalTickets <= 0) {
            throw new ValidationException("Tổng số vé phải lớn hơn 0!");
        }
        if (totalTickets > selectedTour.getSeats()) {
            throw new ValidationException(
                    "Tổng số vé không được vượt quá sức chứa của tour (" + selectedTour.getSeats() + ")."
            );
        }

        int bookedTickets = getBookedTickets();
        int remainingTickets = totalTickets - bookedTickets;
        if (remainingTickets < 0) {
            throw new ValidationException(
                    "Tổng số vé không thể ít hơn số vé đã đặt (" + bookedTickets + ")."
            );
        }
        return remainingTickets;
    }

    private int getBookedTickets() {
        if (tourPlanDTO == null) {
            return 0;
        }
        return tourPlanDTO.getTickets() - tourPlanDTO.getRemainingTickets();
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
        cancelBtn = ButtonFactory.create("Hủy", UIColors.CANCEL);
        cancelBtn.addActionListener(e -> {
            dispose();
        });
    }

    private StaffDTO getStaffSelected(){
        return (StaffDTO) cbStaff.getSelectedItem();
    }

    private TourDTO getTourSelected(){
        return (TourDTO) cbTour.getSelectedItem();
    }

    private String getStatusSelected(){
        return Objects.requireNonNull(cbStatus.getSelectedItem()).toString();
    }

    private boolean validatedForm(TourPlanDTO dto){
        String error = tourPlanBUS.validateKeHoachTour(dto);
        if(error != null){
            JOptionPane.showMessageDialog(this, error);
            return false;
        }

        return true;
    }

    private void updateSoVe(){
        String input = txtTongSoVe.getText().trim();
        TourDTO selectedTour = getTourSelected();
        if (input.isEmpty() || selectedTour == null) {
            txtSoVeConLai.setText("");
            return;
        }

        try {
            int totalTickets = Integer.parseInt(input);
            int remainingTickets = totalTickets - getBookedTickets();
            if (totalTickets <= 0
                    || totalTickets > selectedTour.getSeats()
                    || remainingTickets < 0) {
                txtSoVeConLai.setText("");
                return;
            }
            txtSoVeConLai.setText(String.valueOf(remainingTickets));
        } catch (NumberFormatException ex) {
            txtSoVeConLai.setText("");
        }
    }

    private void requestFocusTourPlanDepartureDate(){
        addWindowListener(new WindowAdapter() {
            @Override
            public void windowOpened(WindowEvent e) {
                SwingUtilities.invokeLater(jDepartureDate::requestFocusInWindow);
            }
        });
    }
}
