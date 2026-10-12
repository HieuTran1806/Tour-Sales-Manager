package org.toursalesmanager.gui.dialog;

import com.toedter.calendar.JDateChooser;
import lombok.AccessLevel;
import lombok.experimental.FieldDefaults;
import org.toursalesmanager.gui.component.ButtonFactory;
import org.toursalesmanager.gui.helper.AddComponentToForm;
import org.toursalesmanager.validate.ValidationException;
import org.toursalesmanager.bus.CustomerBUS;
import org.toursalesmanager.dto.CustomerDTO;
import org.toursalesmanager.gui.helper.UIColors;

import javax.swing.*;
import java.awt.*;
import java.time.LocalDate;

import static java.sql.Date.valueOf;

@FieldDefaults(level = AccessLevel.PRIVATE)
public class CustomerDialog extends JDialog {
    JButton cancelBtn, saveBtn;

    JDateChooser jDob;

    JPanel panelForm, jpBtn;

    JTextField txtAddress, txtFirstName, txtIDCustomer, txtPhoneNumber, txtLastName;

    public enum Mode {
        ADD, EDIT
    }

    CustomerBUS customerBUS;

    Mode mode;
    CustomerDTO currentKhachHang;

    public CustomerDialog(java.awt.Frame parent, boolean modal, CustomerBUS customerBUS, Mode mode, CustomerDTO kh) {
        super(parent, modal);
        this.customerBUS = customerBUS;
        this.mode = mode;
        this.currentKhachHang = kh;

        initComponents();

        if (mode == Mode.EDIT && kh != null) {
            loadData(kh);
            txtIDCustomer.setEditable(false);
            txtFirstName.requestFocus();
            setTitle("Sửa thông tin khách hàng");
        } else {
            setTitle("Thêm khách hàng");
        }
    }

    @SuppressWarnings("unchecked")
    private void initComponents() {
        setLayout(new BorderLayout());

        panelForm = new JPanel(new GridLayout(6, 2, 10, 10));
        jpBtn = new JPanel(new FlowLayout());

        //row idCustomer
        txtIDCustomer = new JTextField();
        AddComponentToForm.addFormRow(panelForm, "Mã khách hàng:", txtIDCustomer);

        //row FirstName
        txtFirstName = new JTextField();
        AddComponentToForm.addFormRow(panelForm, "Họ:", txtFirstName);

        //row LastName
        txtLastName = new JTextField();
        AddComponentToForm.addFormRow(panelForm, "Tên:", txtLastName);

        //row Dob
        jDob = new JDateChooser();
        jDob.setDateFormatString("dd/MM/yyyy");
        jDob.setDate(valueOf(LocalDate.now()));
        AddComponentToForm.addFormRow(panelForm, "Ngày sinh:", jDob);

        //row PhoneNumber
        txtPhoneNumber = new JTextField();
        AddComponentToForm.addFormRow(panelForm, "Số điện thoại:", txtPhoneNumber);

        //row Address
        txtAddress = new JTextField();
        AddComponentToForm.addFormRow(panelForm, "Địa chỉ:", txtAddress);

        // Save button
        save();
        jpBtn.add(saveBtn);

        //Cancel button
        cancel();
        jpBtn.add(cancelBtn);

        add(panelForm, BorderLayout.NORTH);
        add(jpBtn, BorderLayout.SOUTH);

        setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);
        setLocationRelativeTo(null);
        pack();
    }

    private void save(){
        saveBtn = ButtonFactory.create("Lưu", UIColors.SAVE);
        saveBtn.addActionListener(v -> {
            // get data from form
            String idCustomer = txtIDCustomer.getText().trim();
            String firstName = txtFirstName.getText().trim();
            String lastName = txtLastName.getText().trim();
            String address = txtAddress.getText().trim();
            LocalDate dob = jDob.getDate() != null ? jDob.getDate().toInstant().atZone(java.time.ZoneId.systemDefault()).toLocalDate() : null;
            String phoneNumber = txtPhoneNumber.getText().trim();

            try {
                validateFirstName(firstName);
                validateLastName(firstName);
                validateDob(dob);
                validateAddress(address);
                validatePhoneNumber(phoneNumber);

                CustomerBUS customerBUS = new CustomerBUS();
                if (mode == Mode.ADD) {
                    validateIdCustomer(idCustomer); // validate id customer

                    CustomerDTO newCustomer = new CustomerDTO(idCustomer, firstName, lastName, address, phoneNumber, dob);
                    customerBUS.addCustomer(newCustomer);
                    JOptionPane.showMessageDialog(this, "Thêm khách hàng thành công!");
                } else if (mode == Mode.EDIT && currentKhachHang != null) {
                    currentKhachHang.setFirstName(firstName);
                    currentKhachHang.setLastName(lastName);
                    currentKhachHang.setAddress(address);
                    currentKhachHang.setDob (dob);
                    currentKhachHang.setPhoneNumber(phoneNumber);

                    customerBUS.editCustomer(currentKhachHang);
                    JOptionPane.showMessageDialog(this, "Đã chỉnh sửa thông tin khách!");
                }
                dispose(); // Đóng dialog sau khi lưu
            }  catch (ValidationException e) {
                JOptionPane.showMessageDialog(
                        this,
                        e.getMessage(),
                        "Lỗi nhập liệu",
                        JOptionPane.ERROR_MESSAGE
                );
            }
        });
    }

    private void validateIdCustomer(String id) throws ValidationException{
        if (id.isEmpty()) throw new ValidationException("Mã KH không được để trống!");

        if (!id.matches("^KH\\d{3}$"))  throw new ValidationException("Mã khách hàng phải có dạng KHxxx!");

        if(isIdDuplicated(id)) throw new ValidationException("Mã khách hàng đã tồn tại!");
    }

    private boolean isIdDuplicated(String id){
        for (CustomerDTO kh : customerBUS.getAllCustomers()) {
            if (kh.getIdCustomer().equals(id))
                return true;
        }
        return false;
    }

    private void validateFirstName(String firstName) throws ValidationException{
        if (firstName.isEmpty()) throw new ValidationException("Họ không được để trống!");

        if (!firstName.matches("^[\\p{L}]+(\\s[\\p{L}]+)*$"))  throw new ValidationException("Họ chỉ được chứa chữ cái và khoảng trắng!");
    }

    private void validateLastName(String lastName) throws ValidationException{
        if (lastName.isEmpty()) throw new ValidationException("Tên không được để trống!");

        if (!lastName.matches("^[\\p{L}]+(\\s[\\p{L}]+)*$"))  throw new ValidationException("Tên chỉ được chứa chữ cái và khoảng trắng!");
    }

    private void validateDob(LocalDate dob) throws ValidationException{
        if (dob == null) throw new ValidationException("Ngày sinh không được để trống!");

        else if (dob.isAfter(LocalDate.now())) throw new ValidationException("Ngày sinh không được lớn hơn ngày hiện tại!");
    }

    private void validateAddress(String address) throws ValidationException{
        if (address.isEmpty()) throw new ValidationException("Địa chỉ không được để trống!");

        if (!address.matches("^[\\p{L}0-9\\s,.-]+$"))  throw new ValidationException("Địa chỉ chỉ được chứa chữ cái, số, khoảng trắng và các ký tự ,.-!");
    }

    private void validatePhoneNumber(String phoneNumber) throws ValidationException{
        if (phoneNumber.isEmpty()) throw new ValidationException("Số điện thoại không được để trống!");

        if (!phoneNumber.matches("^0\\d{9}$"))  throw new ValidationException("Số điện thoại phải có 10 chữ số và bắt đầu bằng 0!");
    }

    private void cancel(){
        cancelBtn = ButtonFactory.create("Hủy", UIColors.CANCEL);
        cancelBtn.addActionListener(v -> {
            dispose();
        });
    }

    private void loadData(CustomerDTO kh) {
        txtIDCustomer.setText(kh.getIdCustomer());
        txtFirstName.setText(kh.getFirstName());
        txtLastName.setText(kh.getLastName());
        txtAddress.setText(kh.getAddress());
        if (kh.getDob() != null) {
            jDob.setDate(java.util.Date.from(kh.getDob().atStartOfDay(java.time.ZoneId.systemDefault()).toInstant()));
        } else {
            jDob.setDate(null);
        }
        txtPhoneNumber.setText(kh.getPhoneNumber());
    }
}
