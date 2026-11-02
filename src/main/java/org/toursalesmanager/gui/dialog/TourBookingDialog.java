package org.toursalesmanager.gui.dialog;

import org.toursalesmanager.gui.helper.WindowHelper;

import com.toedter.calendar.JDateChooser;
import lombok.AccessLevel;
import lombok.experimental.FieldDefaults;
import org.toursalesmanager.bus.CustomerBUS;
import org.toursalesmanager.bus.TourBookingBUS;
import org.toursalesmanager.bus.TourPlanBUS;
import org.toursalesmanager.dto.TourBookingDTO;
import org.toursalesmanager.dto.CustomerDTO;
import org.toursalesmanager.dto.TourPlanDTO;
import org.toursalesmanager.enums.Role;
import org.toursalesmanager.gui.component.ButtonFactory;
import org.toursalesmanager.gui.helper.AddComponentToForm;
import org.toursalesmanager.gui.helper.UIColors;
import org.toursalesmanager.login.SessionManager;
import org.toursalesmanager.validate.ValidationException;

import javax.swing.*;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import java.awt.*;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
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

    JPanel formPanel, southPanel;

    public enum Mode {
        ADD, EDIT
    }

    enum CustomerMode {
        EXISTING,
        NEW
    }

    String formatter = "dd/MM/yyyy";
    DateTimeFormatter dateFormat = DateTimeFormatter.ofPattern(formatter);

    CustomerMode customerMode;
    CustomerBUS customerBUS;

    Mode mode;
    TourBookingDTO tourBookingDTO;
    TourBookingBUS tourBookingBUS;
    TourPlanBUS tourPlanBUS;

    // Khách hàng tự đặt: không chọn khách hàng, không chọn trạng thái
    boolean selfBooking;
    String preselectedTourPlanId;

    DefaultComboBoxModel<TourPlanDTO> tourPlanModel;
    JComboBox<TourPlanDTO> tourPlanCombo;
    JComboBox<String> tourBookingStatusCombo;
    JComboBox<String> cbCustomerSearchType;

    public TourBookingDialog(Frame parent, boolean modal, Mode mode, TourBookingDTO tourBookingDTO, TourPlanBUS tourPlanBUS) {
        this(parent, modal, mode, tourBookingDTO, tourPlanBUS, null);
    }

    public TourBookingDialog(Frame parent, boolean modal, Mode mode, TourBookingDTO tourBookingDTO, TourPlanBUS tourPlanBUS,
                             String preselectedTourPlanId) {
        super(parent, modal);
        this.mode = mode;
        this.tourBookingDTO = tourBookingDTO;
        this.tourBookingBUS = new TourBookingBUS();
        this.tourPlanBUS = tourPlanBUS;
        this.customerBUS = new CustomerBUS();
        this.selfBooking = mode == Mode.ADD && SessionManager.hasRole(Role.CUSTOMER);
        this.preselectedTourPlanId = preselectedTourPlanId;

        this.tourPlanModel = new DefaultComboBoxModel<>();
        tourPlanCombo = new JComboBox<>();

        initComponents();

        if (selfBooking) {
            setTitle("Đặt tour");
            loadCurrentCustomer();
        } else {
            setTitle(tourBookingDTO == null ? "Thêm phiếu đặt tour" : "Sửa phiếu đặt tour");
        }

        if(tourBookingDTO != null){
            loadData();
        }
    }

    private void initComponents() {
        setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);
        setLayout(new BorderLayout(10, 10));

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
        if (!selfBooking) {
            formPanel.add(lblCustomerMode);
            formPanel.add(customerModePanel);
        }
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

        searchCustomerBtn = ButtonFactory.create("Tìm", UIColors.BUTTON);

        JPanel customerSearchPanel = new JPanel(new BorderLayout(5, 0));

        cbCustomerSearchType.setPreferredSize(new Dimension(140, 30));

        searchCustomerBtn.setPreferredSize(new Dimension(70, 30));

        customerSearchPanel.add(cbCustomerSearchType, BorderLayout.WEST);
        customerSearchPanel.add(txtCustomerSearch, BorderLayout.CENTER);
        customerSearchPanel.add(searchCustomerBtn, BorderLayout.EAST);

        if (!selfBooking) {
            AddComponentToForm.addFormRow(formPanel, "Tìm khách hàng:", customerSearchPanel);
        }

        // row id customer
        txtIdCustomer = new JTextField();
        AddComponentToForm.addFormRow(formPanel, "Mã khách hàng", txtIdCustomer);

        txtFirstName = new JTextField();
        AddComponentToForm.addFormRow(formPanel, "Họ: ", txtFirstName);

        txtLastName = new JTextField();
        AddComponentToForm.addFormRow(formPanel, "Tên: ", txtLastName);

        txtAddress = new JTextField();
        AddComponentToForm.addFormRow(formPanel, "Địa chỉ: ", txtAddress);

        txtPhoneNumber = new JTextField();
        AddComponentToForm.addFormRow(formPanel, "Số điện thoại: ", txtPhoneNumber);

        dateCustomerDob = new JDateChooser();
        dateCustomerDob.setDateFormatString(formatter);
        AddComponentToForm.addFormRow(formPanel, "Ngày sinh: ", dateCustomerDob);

        // row id tour plan
        loadTourPlans();
        tourPlanCombo.setModel(tourPlanModel);
        tourPlanCombo.setRenderer(new DefaultListCellRenderer() {
            @Override
            public Component getListCellRendererComponent(JList<?> list, Object value, int index,
                                                          boolean isSelected, boolean cellHasFocus) {
                super.getListCellRendererComponent(list, value, index, isSelected, cellHasFocus);
                if (value instanceof TourPlanDTO plan) {
                    setText(plan.getIdTourPlan() + " | "
                            + formatDate(plan.getDepartureDate()) + " - "
                            + formatDate(plan.getEndDate()) + " | Còn "
                            + plan.getRemainingTickets() + " vé");
                }
                return this;
            }
        });
        for (int i = 0; i < tourPlanModel.getSize(); i++) {
            if (tourPlanModel.getElementAt(i).getIdTourPlan().equals(preselectedTourPlanId)) {
                tourPlanCombo.setSelectedIndex(i);
                break;
            }
        }
        AddComponentToForm.addFormRow(formPanel, "Mã kế hoạch tour:", tourPlanCombo);

        // row booking date
        jBookingDate = new JDateChooser();
        jBookingDate.setDateFormatString(formatter);
        jBookingDate.setDate(valueOf(LocalDate.now()));
        // Ngày đặt luôn lấy thời điểm lưu phiếu
        jBookingDate.setEnabled(false);
        AddComponentToForm.addFormRow(formPanel, "Ngày đặt: ",jBookingDate);

        // row tickets
        txtTickets = new JTextField(selfBooking ? "1" : "");
        AddComponentToForm.addFormRow(formPanel, "Số lượng vé: ", txtTickets);

        // row price
        txtPrice = new JTextField();
        txtPrice.setEditable(false);
        AddComponentToForm.addFormRow(formPanel, "Giá vé: ",txtPrice);

        // row cost total
        txtCostTotal = new JTextField();
        txtCostTotal.setEditable(false);
        AddComponentToForm.addFormRow(formPanel, "Tổng số tiền: ",txtCostTotal);

        // row booking status
        String[]  tourBookingStatus = {"Chờ xác nhận", "Đã xác nhận", "Đã hủy", "Hoàn thành", "Hết hạn"};
        // hết hạn khi khách hàng không thanh toán/ xác nhận đúng hạn
        tourBookingStatusCombo = new JComboBox<>(tourBookingStatus);
        // Phiếu khách hàng tự đặt luôn ở trạng thái chờ nhân viên xác nhận
        if (!selfBooking) {
            AddComponentToForm.addFormRow(formPanel, "Trạng thái phiếu đặt: ",tourBookingStatusCombo);
        }


        // row mo ta
        txtNote = new JTextArea();
        AddComponentToForm.addFormRow(formPanel, "Ghi chú: ",txtNote);

        southPanel.setBorder(BorderFactory.createEmptyBorder(0, 0, 10, 0));

        // Events & Verifiers
        handleSave();
        southPanel.add(saveBtn);
        cancel();
        southPanel.add(closeBtn);

        add(formPanel, BorderLayout.CENTER);
        add(southPanel, BorderLayout.SOUTH);

        initEvents();
        // Combo tự chọn sẵn kế hoạch đầu tiên nhưng không bắn sự kiện, nên điền giá vé ngay từ đầu
        handleTourPlanChanged();
        pack();
        WindowHelper.centerOnParent(this);
    }

    private void loadTourPlans() {
        tourPlanModel.removeAllElements();

        ArrayList<TourPlanDTO> tourPlanList =
                tourPlanBUS.getAllKeHoachTours();

        // Ngày khởi hành và số vé còn lại do TourBookingBUS kiểm tra khi lưu
        for (TourPlanDTO tourPlan : tourPlanList) {
            if (tourPlan != null) {
                tourPlanModel.addElement(tourPlan);
            }
        }
    }

    private void initEvents() {
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

        txtTickets.getDocument().addDocumentListener(new DocumentListener() {
            @Override
            public void insertUpdate(DocumentEvent e) {
                calculateTotal();
            }

            @Override
            public void removeUpdate(DocumentEvent e) {
                calculateTotal();
            }

            @Override
            public void changedUpdate(DocumentEvent e) {
                calculateTotal();
            }
        });
    }

    private void handleSave(){
        saveBtn = ButtonFactory.create(selfBooking ? "Gửi yêu cầu đặt tour" : "Lưu",  UIColors.SAVE);
        saveBtn.addActionListener(v -> {
            // get data
            String idCustomer = txtIdCustomer.getText().trim();
            LocalDateTime bookingDate = LocalDateTime.now();
            String note = txtNote.getText().trim();

            try {
                String idTourPlan = getSelectedTourPlanId();
                validateIdTourPlan(idTourPlan);

                if (selfBooking) {
                    submitSelfBooking(idTourPlan, parseTickets(), note);
                    return;
                }

                validateCustomerId(idCustomer);

                int tickets = parseTickets();
                BigDecimal price = tourBookingBUS.getPriceByIdTourPlan(idTourPlan);
                if (price == null) {
                    throw new ValidationException("Kế hoạch tour này chưa có giá vé");
                }
                txtPrice.setText(price.toPlainString());

                BigDecimal total = price.multiply(BigDecimal.valueOf(tickets));
                String status = getSelectedStatus();


                if(mode==Mode.ADD) {
                    TourBookingDTO newTourBooking = new TourBookingDTO(idCustomer, idTourPlan, bookingDate, tickets,
                            price, total, status, note);

                    if (!tourBookingBUS.addTourBooking(newTourBooking)) {
                        JOptionPane.showMessageDialog(
                                this,
                                "Không thể lưu phiếu đặt. Có thể kế hoạch tour không còn đủ vé.",
                                "Không thể lưu phiếu đặt",
                                JOptionPane.ERROR_MESSAGE
                        );
                        return;
                    }
                } else if(mode==Mode.EDIT&&tourBookingDTO!=null) {
                    tourBookingDTO.setIdCustomer(idCustomer);
                    tourBookingDTO.setIdTourPlan(idTourPlan);
                    tourBookingDTO.setBookingDate(bookingDate);
                    tourBookingDTO.setTickets(tickets);
                    tourBookingDTO.setPrice(price);
                    tourBookingDTO.setCostTotal(total);
                    tourBookingDTO.setBookingStatus(status);
                    tourBookingDTO.setNote(note);

                    if (!tourBookingBUS.editTourBooking(tourBookingDTO)) {
                        JOptionPane.showMessageDialog(
                                this,
                                "Không thể cập nhật phiếu đặt tour. Có thể kế hoạch tour không còn đủ vé.",
                                "Không thể lưu phiếu đặt",
                                JOptionPane.ERROR_MESSAGE
                        );
                        return;
                    }
                }
                dispose();

            }catch (ValidationException validationException) {
                JOptionPane.showMessageDialog(
                        this,
                        validationException.getMessage(),
                        "Lỗi nhập liệu",
                        JOptionPane.ERROR_MESSAGE
                );
            } catch (IllegalArgumentException | IllegalStateException | SecurityException exception) {
                // Lỗi nghiệp vụ từ BUS (thiếu quyền, sai mã khách hàng, ...)
                JOptionPane.showMessageDialog(
                        this,
                        exception.getMessage(),
                        "Không thể lưu phiếu đặt",
                        JOptionPane.ERROR_MESSAGE
                );
            }
        });
    }

    private void submitSelfBooking(String idTourPlan, int tickets, String note) {
        if (!tourBookingBUS.createCustomerBooking(idTourPlan, tickets, note)) {
            JOptionPane.showMessageDialog(
                    this,
                    "Không thể lưu yêu cầu đặt tour. Có thể kế hoạch tour không còn đủ vé.",
                    "Không thể đặt tour",
                    JOptionPane.ERROR_MESSAGE
            );
            return;
        }
        JOptionPane.showMessageDialog(
                this,
                "Đã gửi yêu cầu đặt tour. Yêu cầu đang chờ nhân viên xác nhận.",
                "Đặt tour thành công",
                JOptionPane.INFORMATION_MESSAGE
        );
        dispose();
    }

    private int parseTickets() throws ValidationException {
        try {
            int tickets = Integer.parseInt(txtTickets.getText().trim());
            if (tickets <= 0) {
                throw new ValidationException("Số lượng vé phải lớn hơn 0");
            }
            return tickets;
        } catch (NumberFormatException e) {
            throw new ValidationException("Số lượng vé phải là số nguyên dương");
        }
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
        closeBtn = ButtonFactory.create("Đóng", UIColors.CANCEL);
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

    private void loadCurrentCustomer() {
        String customerId = SessionManager.getCurrentAccount().getIdCustomer();
        if (customerId != null) {
            txtIdCustomer.setText(customerId);
            loadCustomerInformation(customerId);
        }
        setCustomerFieldsEditable(false);
    }

    private String formatDate(LocalDate date) {
        return date == null ? "-" : date.format(dateFormat);
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
        return Objects.requireNonNull(tourBookingStatusCombo.getSelectedItem()).toString();
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