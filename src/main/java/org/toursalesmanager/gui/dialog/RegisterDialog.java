package org.toursalesmanager.gui.dialog;

import org.toursalesmanager.gui.helper.WindowHelper;

import com.toedter.calendar.JDateChooser;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.experimental.FieldDefaults;
import org.toursalesmanager.bus.AccountBUS;

import javax.swing.*;
import java.awt.*;
import java.time.LocalDate;
import java.util.Date;

import org.toursalesmanager.gui.component.ButtonFactory;
import org.toursalesmanager.gui.helper.AddComponentToForm;
import org.toursalesmanager.gui.helper.DateHelper;
import org.toursalesmanager.gui.helper.PasswordFieldWithReveal;
import org.toursalesmanager.gui.helper.UIColors;

@FieldDefaults(level = AccessLevel.PRIVATE)
public class RegisterDialog extends JDialog {
    final AccountBUS accountBUS;

    JPasswordField txtPassword;
    JPasswordField txtConfirmPassword;

    JTextField txtUsername, txtFirstName, txtLastName, txtAddress, txtPhoneNumber, txtEmail;
    JDateChooser dob;

    String formatter = ("dd/MM/yyyy");

    JPanel panelForm, panelButtons;

    JButton btnRegister, btnCancel;

    @Getter
    String registeredUsername;

    public RegisterDialog(Frame owner, AccountBUS accountBUS) {
        super(owner, "Đăng ký tài khoản", true);

        this.accountBUS = accountBUS;

        initComponents();
        initEvents();

        pack();
        WindowHelper.centerOnParent(this);
    }

    private void initComponents() {
        setLayout(new BorderLayout());

        setDefaultCloseOperation(DISPOSE_ON_CLOSE);
        setMinimumSize(new Dimension(450, 400));
        setResizable(false);

        panelForm = new JPanel(new GridLayout(0, 2, 10, 10));
        panelButtons = new JPanel(new FlowLayout(FlowLayout.RIGHT, 10, 10));

        // add buttons to south panel
        initButtons();

        // add form fields to center panel
        AddComponentToForm.addFormRow(panelForm, "Tên đăng nhập:", txtUsername = new JTextField());
        txtPassword = new JPasswordField();
        AddComponentToForm.addFormRow(panelForm, "Mật khẩu:", PasswordFieldWithReveal.create(txtPassword));
        txtConfirmPassword = new JPasswordField();
        AddComponentToForm.addFormRow(panelForm, "Xác nhận mật khẩu:", PasswordFieldWithReveal.create(txtConfirmPassword));
        AddComponentToForm.addFormRow(panelForm, "Họ:", txtFirstName = new JTextField());
        AddComponentToForm.addFormRow(panelForm, "Tên:", txtLastName = new JTextField());
        AddComponentToForm.addFormRow(panelForm, "Địa chỉ:", txtAddress = new JTextField());
        AddComponentToForm.addFormRow(panelForm, "Số điện thoại:", txtPhoneNumber = new JTextField());
        AddComponentToForm.addFormRow(panelForm, "Email:", txtEmail = new JTextField());

        dob = new JDateChooser();
        dob.setDateFormatString(formatter);
        dob.setDate(null);
        AddComponentToForm.addFormRow(panelForm, "Ngày sinh:", dob);

        panelButtons.add(btnRegister);
        panelButtons.add(btnCancel);

        add(panelForm, BorderLayout.CENTER);
        add(panelButtons, BorderLayout.SOUTH);
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
        String phone = txtPhoneNumber.getText().trim();
        String email = txtEmail.getText().trim();
        Date selectedDob = dob.getDate();
        LocalDate dateOfBirth = DateHelper.toLocalDateFromUtil(selectedDob);

        try {
            boolean success = accountBUS.registerCustomer(
                    username,
                    password,
                    firstName,
                    lastName,
                    address,
                    phone,
                    dateOfBirth,
                    confirmPassword,
                    email
            );

            if (success) {
                registeredUsername = username;
                JOptionPane.showMessageDialog(
                        this,
                        "Đăng ký tài khoản thành công"
                );
                dispose();
            } else {
                JOptionPane.showMessageDialog(
                        this,
                        "Đăng ký tài khoản thất bại",
                        "Lỗi",
                        JOptionPane.ERROR_MESSAGE
                );
            }
        } catch (IllegalArgumentException ex) {
            JOptionPane.showMessageDialog(
                    this,
                    ex.getMessage(),
                    "Dữ liệu không hợp lệ",
                    JOptionPane.WARNING_MESSAGE
            );
        } catch (Exception e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(
                    this,
                    "Đã xảy ra lỗi khi đăng ký tài khoản: " + e.getMessage(),
                    "Lỗi",
                    JOptionPane.ERROR_MESSAGE
            );
        }
    }

    public boolean isRegistered() {
        return registeredUsername != null;
    }
}