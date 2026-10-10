package org.toursalesmanager.gui.dialog;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.experimental.FieldDefaults;
import org.toursalesmanager.bus.AccountBUS;
import org.toursalesmanager.gui.component.ButtonFactory;
import org.toursalesmanager.gui.helper.UIColors;


import javax.swing.*;
import java.awt.*;
import java.time.LocalDate;

@FieldDefaults(level = AccessLevel.PRIVATE)
public class RegisterDialog extends JDialog {
    final AccountBUS accountBUS;

    JPasswordField txtPassword;
    JPasswordField txtConfirmPassword;

    JTextField txtUsername, txtFirstName, txtLastName, txtAddress, txtPhoneNumber, txtPhone;
    LocalDate dob;

    JButton btnRegister, btnCancel;

    @Getter
    boolean registered;
    @Getter
    String registeredUsername;

    public RegisterDialog(Frame owner, AccountBUS accountBUS) {
        super(owner, "Đăng ký tài khoản", true);

        this.accountBUS = accountBUS;

        initButtons();
        initComponents();
        initEvents();

        setDefaultCloseOperation(DISPOSE_ON_CLOSE);
        pack();
        setMinimumSize(new Dimension(450, 400));
        setLocationRelativeTo(owner);
        setResizable(false);
    }

    private void initComponents() {

    }

    private void initButtons(){
        btnRegister = ButtonFactory.create("Đăng ký", UIColors.VIEW);
        btnCancel = ButtonFactory.create("Hủy", UIColors.CANCEL);
    }

    private void initEvents() {
        btnRegister.addActionListener(e -> register());
        btnCancel.addActionListener(e -> dispose());
    }

    private void register() {

        String username = txtUsername.getText().trim();
        String password = new String(txtPassword.getPassword());

        String confirmPassword = new String(txtConfirmPassword.getPassword());

        String firstName = txtFirstName.getText().trim();
        String lastName = txtLastName.getText().trim();
        String address = txtAddress.getText().trim();
        String phone = txtPhone.getText().trim();
        // LocalDate date = dob

        if (username.isBlank()
                || password.isBlank()
                || firstName.isBlank()
                || lastName.isBlank()
                || address.isBlank()
                || phone.isBlank()
        ) {

            JOptionPane.showMessageDialog(
                    this,
                    "Vui lòng nhập đầy đủ thông tin bắt buộc"
            );
            return;
        }

        if (!password.equals(confirmPassword)) {
            JOptionPane.showMessageDialog(
                    this,
                    "Mật khẩu xác nhận không khớp"
            );
            return;
        }

        try {
            accountBUS.registerCustomer(
                    username,
                    password,
                    firstName,
                    lastName,
                    address,
                    phone,
                    dob
            );

            registered = true;
            registeredUsername = username;

            JOptionPane.showMessageDialog(
                    this,
                    "Đăng ký tài khoản thành công"
            );

            dispose();

        } catch (IllegalArgumentException ex) {
            JOptionPane.showMessageDialog(
                    this,
                    ex.getMessage(),
                    "Dữ liệu không hợp lệ",
                    JOptionPane.WARNING_MESSAGE
            );
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(
                    this,
                    "Không thể đăng ký tài khoản",
                    "Lỗi",
                    JOptionPane.ERROR_MESSAGE
            );
        }
    }
}