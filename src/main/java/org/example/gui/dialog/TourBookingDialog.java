package org.example.gui.dialog;

import com.toedter.calendar.JDateChooser;
import lombok.AccessLevel;
import lombok.experimental.FieldDefaults;
import org.example.bus.CustomerBUS;
import org.example.bus.TourBookingBUS;
import org.example.bus.TourPlanBUS;
import org.example.dto.TourBookingDTO;
import org.example.dto.CustomerDTO;
import org.example.dto.TourPlanDTO;
import org.example.gui.panel.UIColors;
import org.example.validate.ValidationException;

import javax.swing.*;
import java.awt.*;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Objects;

import static java.sql.Date.valueOf;

@FieldDefaults(level = AccessLevel.PRIVATE)
public class TourBookingDialog extends JDialog {
    JButton closeBtn, saveBtn, searchCustomerBtn;

    JTextField txtCustomerSearch, txtPrice, txtFirstName, txtIdCustomer, txtLastName, txtCostTotal, txtAddress, txtPhoneNumber, txtTickets;

    JTextArea txtNote;

    JRadioButton rbExistingCustomer, rbNewCustomer;

    JDateChooser jBookingDate, dateCustomerDob;;

    JPanel formPanel, southPanel, buttonPanel;

    public enum Mode {
        ADD, EDIT
    }

    enum CustomerMode {
        EXISTING,
        NEW
    }

    String formatter = "dd/MM/yyyy";

    CustomerMode customerMode;
    CustomerBUS customerBUS;

    Mode mode;
    TourBookingDTO tourBookingDTO;
    TourBookingBUS tourBookingBUS;
    TourPlanBUS tourPlanBUS;

    DefaultComboBoxModel<TourPlanDTO> tourPlanModel;
    JComboBox<TourPlanDTO> tourPlanCombo;
    JComboBox<String> tourBookingStatusCombo;
    JComboBox<String> cbCustomerSearchType;

    public TourBookingDialog(Frame parent, boolean modal, Mode mode, TourBookingDTO tourBookingDTO, TourPlanBUS tourPlanBUS) {
        super(parent, modal);
        this.mode = mode;
        this.tourBookingDTO = tourBookingDTO;
        this.tourBookingBUS = new TourBookingBUS();
        this.tourPlanBUS = tourPlanBUS;
        this.customerBUS = new CustomerBUS();

        this.tourPlanModel = new DefaultComboBoxModel<>();
        tourPlanCombo = new JComboBox<>();

        initComponents();

        setTitle(tourBookingDTO == null ? "Thêm phiếu đặt tour" : "Sửa phiếu đặt tour");
        if(tourBookingDTO != null){
            loadData();
        }
    }

    private void initComponents() {
        setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);
        setLayout(new BorderLayout(10, 10));

        // 4 rows 2 cols
        formPanel = new JPanel(new GridLayout(0, 2, 10, 15));
        formPanel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20)); // Căn lề padding 4 góc

        southPanel = new JPanel(new FlowLayout());


        JLabel lblCustomerMode = new JLabel("Khách hàng:");
        lblCustomerMode.setBorder(
                BorderFactory.createEmptyBorder(5, 5, 5, 0)
        );

        rbExistingCustomer = new JRadioButton("Khách hàng cũ");
        rbNewCustomer = new JRadioButton("Khách hàng mới");

        ButtonGroup customerModeGroup = new ButtonGroup();
        customerModeGroup.add(rbExistingCustomer);
        customerModeGroup.add(rbNewCustomer);

        JPanel customerModePanel = new JPanel(
                new FlowLayout(FlowLayout.LEFT, 10, 0)
        );

        customerModePanel.add(rbExistingCustomer);
        customerModePanel.add(rbNewCustomer);

        // Thêm đúng hai component vào một hàng của GridLayout
        formPanel.add(lblCustomerMode);
        formPanel.add(customerModePanel);
        rbExistingCustomer.setSelected(true);
        customerMode = CustomerMode.EXISTING;

        // row find customer
        cbCustomerSearchType = new JComboBox<>(
                new String[] {
                        "Mã khách hàng",
                        "Tên khách hàng",
                        "Số điện thoại"
                }
        );

        txtCustomerSearch = new JTextField();

        searchCustomerBtn = new JButton("Tìm");

        JPanel customerSearchPanel = new JPanel(new BorderLayout(5, 0));

        cbCustomerSearchType.setPreferredSize(new Dimension(140, 30));

        searchCustomerBtn.setPreferredSize(new Dimension(70, 30));

        customerSearchPanel.add(cbCustomerSearchType, BorderLayout.WEST);
        customerSearchPanel.add(txtCustomerSearch, BorderLayout.CENTER);
        customerSearchPanel.add(searchCustomerBtn, BorderLayout.EAST);

        addFormRow(formPanel, "Tìm khách hàng:", customerSearchPanel);

        // row id customer
        txtIdCustomer = new JTextField();
        addFormRow(formPanel, "Mã khách hàng", txtIdCustomer);

        txtFirstName = new JTextField();
        addFormRow(formPanel, "Họ: ", txtFirstName);

        txtLastName = new JTextField();
        addFormRow(formPanel, "Tên: ", txtLastName);

        txtAddress = new JTextField();
        addFormRow(formPanel, "Địa chỉ: ", txtAddress);

        txtPhoneNumber = new JTextField();
        addFormRow(formPanel, "Số điện thoại: ", txtPhoneNumber);

        dateCustomerDob = new JDateChooser();
        dateCustomerDob.setDateFormatString(formatter);
        addFormRow(formPanel, "Ngày sinh: ", dateCustomerDob);

        // row id tour plan
        loadTourPlans();
        tourPlanCombo.setModel(tourPlanModel);
        addFormRow(formPanel, "Mã kế hoạch tour:", tourPlanCombo);

        // row booking date
        jBookingDate = new JDateChooser();
        jBookingDate.setDateFormatString(formatter);
        jBookingDate.setDate(valueOf(LocalDate.now()));
        addFormRow(formPanel, "Ngày đặt: ",jBookingDate);

        // row tickets
        txtTickets = new JTextField();
        addFormRow(formPanel, "Số lượng vé: ", txtTickets);


        // row price
        txtPrice = new JTextField();
        txtPrice.setEditable(false);
        addFormRow(formPanel, "Giá vé: ",txtPrice);


        // row cost total
        txtCostTotal = new JTextField();
        txtCostTotal.setEditable(false);
        addFormRow(formPanel, "Tổng số tiền: ",txtCostTotal);

        // row booking status
        String[]  tourBookingStatus = {"Chờ xác nhận", "Đã xác nhận", "Đã hủy", "Hoàn thành", "Hết hạn"};
        // hết hạn khi khách hàng không thanh toán/ xác nhận đúng hạn
        tourBookingStatusCombo = new JComboBox<>(tourBookingStatus);
        addFormRow(formPanel, "Trạng thái phiếu đặt: ",tourBookingStatusCombo);


        // row mo ta
        txtNote = new JTextArea();
        addFormRow(formPanel, "Ghi chú: ",txtNote);


        southPanel.setBorder(BorderFactory.createEmptyBorder(0, 0, 10, 0));


        // Events & Verifiers
        handleSave();
        southPanel.add(saveBtn);
        cancel();
        southPanel.add(closeBtn);

        add(formPanel, BorderLayout.CENTER);
        add(southPanel, BorderLayout.SOUTH);

        initEvents();
        pack();
        setLocationRelativeTo(null);
    }

    private void loadTourPlans() {
        tourPlanModel.removeAllElements();

        ArrayList<TourPlanDTO> tourPlanList =
                tourPlanBUS.getAllKeHoachTours();

        for (TourPlanDTO tourPlan : tourPlanList) {
            if (tourPlan != null) {
                tourPlanModel.addElement(tourPlan);
            }
        }
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

    private void initEvents() {
        saveBtn.addActionListener(e ->
                handleSave()
        );

        closeBtn.addActionListener(e ->
                dispose()
        );

        rbExistingCustomer.addActionListener(e ->
                changeCustomerMode(
                        CustomerMode.EXISTING
                )
        );

        rbNewCustomer.addActionListener(e ->
                changeCustomerMode(
                        CustomerMode.NEW
                )
        );

        // searchCustomerBtn.addActionListener(e ->
        //         searchCustomer()
        // );

        tourPlanCombo.addActionListener(e ->
                handleTourPlanChanged()
        );

        // txtTickets.getDocument().addDocumentListener(
        //         new SimpleDocumentListener(
        //                 this::calculateTotal
        //         )
        // );
    }

    private JButton createBtn(String text, Color color){
        JButton btn = new JButton(text);
        btn.setBackground(color);
        btn.setForeground(Color.WHITE);
        btn.setFocusPainted(false);
        btn.setCursor(new Cursor(Cursor.HAND_CURSOR));// Trong jpBtn panel

        return btn;
    }

    private void handleSave(){
        saveBtn = createBtn("Lưu",  UIColors.SAVE);
        saveBtn.addActionListener(v -> {
            // get data
            String idCustomer = txtIdCustomer.getText().trim();
            String firstName = txtFirstName.getText().trim();
            String lastName = txtLastName.getText().trim();
            LocalDateTime bookingDate = LocalDateTime.now();
            int tickets = Integer.parseInt(txtTickets.getText().trim());
            BigDecimal price = BigDecimal.valueOf(Long.parseLong(txtPrice.getText().trim()));
            String note = txtNote.getText().trim();

            String idTourPlan;
            try {
                idTourPlan = getSelectedTourPlanId();
            } catch (ValidationException e) {
                throw new RuntimeException(e);
            }
            if(txtPrice.getText().trim().isEmpty()){
                BigDecimal gia = tourBookingBUS.getPriceByIdTourPlan(idTourPlan);
                txtPrice.setText(String.valueOf(gia));
            }
            BigDecimal giaVe = BigDecimal.valueOf(Long.parseLong(txtPrice.getText().trim()));

            try {
                validateIdTourPlan(idTourPlan);
                validateCustomerId(idCustomer);

                BigDecimal total = price.multiply(BigDecimal.valueOf(tickets));
                String status = getSelectedStatus();


                if(mode==Mode.ADD) {
                    TourBookingDTO newTourBooking = new TourBookingDTO(idCustomer, idTourPlan, bookingDate, tickets,
                            price, total, status, note);

                    tourBookingBUS.addTourBooking(newTourBooking);
                } else if(mode==Mode.EDIT&&tourBookingDTO!=null) {
                    tourBookingDTO.setIdCustomer(idCustomer);
                    tourBookingDTO.setIdTourPlan(idTourPlan);
                    tourBookingDTO.setBookingDate(bookingDate);
                    tourBookingDTO.setTickets(tickets);
                    tourBookingDTO.setPrice(price);
                    tourBookingDTO.setCostTotal(total);
                    tourBookingDTO.setBookingStatus(status);
                    tourBookingDTO.setNote(note);

                    tourBookingBUS.editTourBooking(tourBookingDTO);
                }
                dispose();

            }catch (ValidationException validationException) {
                JOptionPane.showMessageDialog(
                        this,
                        validationException.getMessage(),
                        "Lỗi nhập liệu",
                        JOptionPane.ERROR_MESSAGE
                );
                return;
            }
        });
    }



    private void validateIdTourPlan(String id) throws ValidationException {
        if (id.isEmpty()) throw new ValidationException("Mã phiếu đặt tour không được để trống!");

        // if (!id.matches("^KH\\d{3}$"))  throw new ValidationException("Mã phiếu đặt tour phải có dạng KHxxx!");
    }

    private void validateCustomerId(String id)
            throws ValidationException {

        if (id == null || id.isBlank()) {
            throw new ValidationException(
                    "Mã khách hàng không được để trống"
            );
        }

        boolean existed =
                isExistedIdCustomer(id);

        if (customerMode == CustomerMode.EXISTING
                && !existed) {

            throw new ValidationException(
                    "Mã khách hàng không tồn tại"
            );
        }

        if (customerMode == CustomerMode.NEW
                && existed) {

            throw new ValidationException(
                    "Mã khách hàng đã tồn tại"
            );
        }
    }


    private void changeCustomerMode(
            CustomerMode mode
    ) {
        customerMode = mode;

        boolean existing =
                mode == CustomerMode.EXISTING;

        cbCustomerSearchType.setEnabled(existing);
        txtCustomerSearch.setEnabled(existing);
        searchCustomerBtn.setEnabled(existing);

        txtIdCustomer.setEditable(!existing);
        txtFirstName.setEditable(!existing);
        txtLastName.setEditable(!existing);
        txtAddress.setEditable(!existing);
        txtPhoneNumber.setEditable(!existing);
        dateCustomerDob.setEnabled(!existing);

        clearCustomerFields();

        if (existing) {
            txtCustomerSearch.requestFocus();
        } else {
            txtIdCustomer.requestFocus();
        }
    }

    private boolean isExistedIdCustomer(String id){
        for (CustomerDTO kh : customerBUS.getAllCustomers()) {
            if (kh.getIdCustomer().equals(id))
                return true;
        }
        return false;
    }

    private void cancel(){
        closeBtn = createBtn("Đóng", UIColors.CANCEL);
        closeBtn.addActionListener(v -> {
            this.dispose();
        });
    }

    private void loadData() {
        if (tourBookingDTO == null) {
            return;
        }
        txtIdCustomer.setText(
                tourBookingDTO.getIdCustomer()
        );

        loadCustomerInformation(
                tourBookingDTO.getIdCustomer()
        );

        selectTourPlan(
                tourBookingDTO.getIdTourPlan()
        );

        txtTickets.setText(
                String.valueOf(
                        tourBookingDTO.getTickets()
                )
        );

        if(tourBookingDTO.getPrice() != null){
            txtPrice.setText(
                    tourBookingDTO.getPrice()
                            .toPlainString()
            );
        }

        BigDecimal costTotal = Objects.requireNonNullElse(tourBookingDTO.getCostTotal(), BigDecimal.ZERO);
        txtCostTotal.setText(costTotal.toPlainString());

        tourBookingStatusCombo.setSelectedItem(
                tourBookingDTO.getBookingStatus()
        );

        txtNote.setText(
                tourBookingDTO.getNote() == null
                        ? ""
                        : tourBookingDTO.getNote()
        );

        customerMode = CustomerMode.EXISTING;
        rbExistingCustomer.setSelected(true);

        setCustomerFieldsEditable(false);
    }

    private void setCustomerFieldsEditable(boolean editable) {
        txtIdCustomer.setEditable(editable);
        txtFirstName.setEditable(editable);
        txtLastName.setEditable(editable);
        txtAddress.setEditable(editable);
        txtPhoneNumber.setEditable(editable);

        // JDateChooser không có setEditable trực tiếp
        dateCustomerDob.setEnabled(editable);
    }

    private void selectTourPlan(String idTourPlan) {
        for (int i = 0;
             i < tourPlanModel.getSize();
             i++) {

            TourPlanDTO dto =
                    tourPlanModel.getElementAt(i);

            if (dto.getIdTourPlan()
                    .equals(idTourPlan)) {

                tourPlanCombo.setSelectedIndex(i);
                return;
            }
        }

        tourPlanCombo.setSelectedIndex(-1);
    }

    private String getSelectedTourPlanId() throws ValidationException{
        TourPlanDTO tourPlanSelected = (TourPlanDTO) tourPlanCombo.getSelectedItem();

        if(tourPlanSelected != null){
            return tourPlanSelected.getIdTourPlan();
        }

        throw new ValidationException("Vui lòng chọn kế hoạch tour");
    }

    private String getSelectedStatus(){
        return Objects.requireNonNull(tourPlanCombo.getSelectedItem()).toString();
    }

    private void loadCustomerInformation(String customerId) {
        ArrayList<CustomerDTO> ls = customerBUS.getAllCustomers();
        for (CustomerDTO customer : ls) {
            if (customer.getIdCustomer().equals(customerId)) {

                txtFirstName.setText(
                        customer.getFirstName()
                );

                txtLastName.setText(
                        customer.getLastName()
                );

                txtAddress.setText(
                        customer.getAddress()
                );

                txtPhoneNumber.setText(
                        customer.getPhoneNumber()
                );

                if (customer.getDob() != null) {
                    dateCustomerDob.setDate(
                            java.sql.Date.valueOf(
                                    customer.getDob()
                            )
                    );
                }

                return;
            }
        }
    }

    private void clearCustomerFields() {
        txtIdCustomer.setText("");
        txtFirstName.setText("");
        txtLastName.setText("");
        txtAddress.setText("");
        txtPhoneNumber.setText("");
        dateCustomerDob.setDate(null);
    }

    private void handleTourPlanChanged() {
        TourPlanDTO selectedPlan =
                (TourPlanDTO)
                        tourPlanCombo.getSelectedItem();

        if (selectedPlan == null) {
            txtPrice.setText("");
            txtCostTotal.setText("");
            return;
        }

        BigDecimal price =
                tourBookingBUS.getPriceByIdTourPlan(
                        selectedPlan.getIdTourPlan()
                );

        if (price == null) {
            txtPrice.setText("");
            txtCostTotal.setText("");
            return;
        }

        txtPrice.setText(
                price.toPlainString()
        );

        calculateTotal();
    }

    private void calculateTotal() {
        try {
            String ticketText = txtTickets.getText().trim();

            String priceText = txtPrice.getText().trim();

            if (ticketText.isEmpty() || priceText.isEmpty()) {
                txtCostTotal.setText("");
                return;
            }

            int tickets = Integer.parseInt(ticketText);

            if (tickets <= 0) {
                txtCostTotal.setText("");
                return;
            }

            BigDecimal price = new BigDecimal(priceText);

            BigDecimal total = price.multiply(BigDecimal.valueOf(tickets));

            txtCostTotal.setText(total.toPlainString());

        } catch (NumberFormatException e) {
            txtCostTotal.setText("");
        }
    }
}