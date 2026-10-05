package org.example.gui.dialog;

import com.toedter.calendar.JDateChooser;
import lombok.AccessLevel;
import lombok.experimental.FieldDefaults;
import org.example.validate.ValidationException;
import org.example.bus.CustomerBUS;
import org.example.dao.CustomerDAO;
import org.example.dto.CustomerDTO;
import org.example.gui.helper.UIColors;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.time.LocalDate;

import static java.sql.Date.valueOf;

@FieldDefaults(level = AccessLevel.PRIVATE)
public class CustomerDialog extends JDialog {
    JButton cancelBtn, saveBtn;

    JDateChooser jDob;

    JLabel lbIDCustomer, lbLastName, lbAddress, lbDob, lbFirstName, lbPhoneNumber;

    JPanel panelForm, jpBtn;

    JTextField txtAddress, txtFirstName, txtIDCustomer, txtPhoneNumber, txtLastName;

    public enum Mode {
        ADD, EDIT
    }

    Mode mode;
    CustomerDTO currentKhachHang;
    CustomerDAO ds;

    public CustomerDialog(java.awt.Frame parent, boolean modal, CustomerDAO ds, Mode mode, CustomerDTO kh) {
        super(parent, modal);
        this.ds = ds;
        this.mode = mode;
        this.currentKhachHang = kh;

        initComponents();
        setLocationRelativeTo(null);

        if (mode == Mode.EDIT && kh != null) {
            setDataCustomer(kh);
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

        // Save button
        save();
        jpBtn.add(saveBtn);

        //Cancel button
        cancel();
        jpBtn.add(cancelBtn);

        //row idCustomer
        lbIDCustomer = new JLabel("Mã khách hàng:");
        lbIDCustomer.setBorder(BorderFactory.createEmptyBorder(5, 5, 5, 0));
        panelForm.add(lbIDCustomer);
        txtIDCustomer = new JTextField();
        panelForm.add(txtIDCustomer);
        txtIDCustomer.addActionListener(this::txtIDCustomerActionPerformed);

        //row FirstName
        lbFirstName = new JLabel("Họ: ");
        lbFirstName.setBorder(BorderFactory.createEmptyBorder(5, 5, 5, 0));
        panelForm.add(lbFirstName);
        txtFirstName = new JTextField();
        panelForm.add(txtFirstName);
        txtFirstName.addActionListener(this::txtFirstNameActionPerformed);

        //row LastName
        lbLastName = new JLabel("Tên: ");
        lbLastName.setBorder(BorderFactory.createEmptyBorder(5, 5, 5, 0));
        panelForm.add(lbLastName);
        txtLastName = new JTextField();
        panelForm.add(txtLastName);

        txtLastName.addActionListener(this::txtLastNameActionPerformed);

        //row Dob
        lbDob = new JLabel("Ngày sinh");
        lbDob.setBorder(BorderFactory.createEmptyBorder(5, 5, 5, 0));
        panelForm.add(lbDob);
        jDob = new JDateChooser();
        jDob.setDateFormatString("dd/MM/yyyy");
        jDob.setDate(valueOf(LocalDate.now()));
        panelForm.add(jDob);

        //row PhoneNumber
        lbPhoneNumber = new JLabel("Số điện thoại");
        lbPhoneNumber.setBorder(BorderFactory.createEmptyBorder(5, 5, 5, 0));
        panelForm.add(lbPhoneNumber);
        txtPhoneNumber = new JTextField();
        panelForm.add(txtPhoneNumber);
        txtPhoneNumber.addActionListener(this::txtPhoneNumberActionPerformed);

        //row Address
        lbAddress = new JLabel("Địa chỉ");
        lbAddress.setBorder(BorderFactory.createEmptyBorder(5, 5, 5, 0));
        panelForm.add(lbAddress);
        txtAddress = new JTextField();
        panelForm.add(txtAddress);
        txtAddress.addActionListener(this::txtAddressActionPerformed);

        add(panelForm, BorderLayout.NORTH);
        jpBtn.setBorder(BorderFactory.createEmptyBorder(0, 0, 10, 0));
        add(jpBtn, BorderLayout.SOUTH);

        setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);
        pack();
    }

    private void txtIDCustomerActionPerformed(ActionEvent evt) {//GEN-FIRST:event_txtIDCustomerActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_txtIDCustomerActionPerformed

    private void txtLastNameActionPerformed(ActionEvent evt) {//GEN-FIRST:event_txtLastNameActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_txtLastNameActionPerformed

    private void txtAddressActionPerformed(ActionEvent evt) {//GEN-FIRST:event_txtAddressActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_txtAddressActionPerformed

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
                    customerBUS.them(newCustomer);
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
        for (CustomerDTO kh : ds.getAllCustomers()) {
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
        cancelBtn = createBtn("Hủy", UIColors.CANCEL);
        cancelBtn.addActionListener(v -> {
            dispose();
        });
    }
    private void txtFirstNameActionPerformed(ActionEvent evt) {//GEN-FIRST:event_txtFirstNameActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_txtFirstNameActionPerformed

    private void txtPhoneNumberActionPerformed(ActionEvent evt) {//GEN-FIRST:event_txtPhoneNumberActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_txtPhoneNumberActionPerformed

    private void setDataCustomer(CustomerDTO kh) {
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
