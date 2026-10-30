package org.toursalesmanager.login;

import jakarta.mail.MessagingException;
import lombok.AccessLevel;
import lombok.experimental.FieldDefaults;
import org.toursalesmanager.bus.AccountBUS;
import org.toursalesmanager.dto.AccountDTO;
import org.toursalesmanager.gui.MainFrame;
import org.toursalesmanager.gui.component.ButtonFactory;
import org.toursalesmanager.gui.dialog.RegisterDialog;
import org.toursalesmanager.gui.helper.PasswordFieldWithReveal;
import org.toursalesmanager.gui.helper.UIColors;

import java.awt.*;
import java.util.Arrays;
import java.util.Objects;
import javax.swing.*;

@FieldDefaults(level = AccessLevel.PRIVATE)
public class Login extends JDialog {
    JButton loginBtn, logoutBtn, registerBtn;
    JButton forgotPasswordBtn;
    JLabel jlbAccount, jlbPassword;
    JTextField txtUsername;
    JPasswordField txtPassword;
    AccountBUS accountBUS;
    private Runnable successAction;

    public Login() {
        this(null, null);
    }

    public Login(Window parent, Runnable successAction) {
        super(parent, "Đăng Nhập - Tour Management System", ModalityType.APPLICATION_MODAL);
        this.successAction = successAction;
        accountBUS = new AccountBUS();
        initComponents();
        setLocationRelativeTo(parent);

        // Set the login button as the default button
        this.getRootPane().setDefaultButton(loginBtn);
    }

    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {
        initLogoWithLink("logosgu.png");

        //set title
        setTitle("Đăng Nhập - Tour Management System");

        jlbAccount = new JLabel();
        jlbPassword = new JLabel();

        loginBtn = new JButton();
        logoutBtn = new JButton();
        registerBtn = new JButton();

        txtUsername = new JTextField(20);
        txtUsername.setPreferredSize(new Dimension(280, 30));

        txtPassword = new JPasswordField(20);
        txtPassword.setPreferredSize(new Dimension(280, 30));
        txtPassword.setEchoChar('*');

        // Header Panel
        JPanel headerPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 15, 15));
        headerPanel.setBackground(Color.CYAN);
        headerPanel.setPreferredSize(new Dimension(450, 100));

        // Logo
        try {
            ImageIcon originalIcon = new ImageIcon(Objects.requireNonNull(getClass().getClassLoader().getResource("logo.png")));
            Image scaledImage = originalIcon.getImage().getScaledInstance(60, 60, Image.SCALE_SMOOTH);
            JLabel logoLabel = new JLabel(new ImageIcon(scaledImage));
            headerPanel.add(logoLabel);
        } catch (Exception e) {
            e.printStackTrace();
        }

        // title
        JLabel jlbTitle = new JLabel("TOUR MANAGEMENT SYSTEM");
        jlbTitle.setFont(new Font("Segoe UI", Font.BOLD, 24));
        jlbTitle.setForeground(Color.WHITE);
        headerPanel.add(jlbTitle);

        //set close operation
        setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);

        jlbAccount.setText("Tài khoản:");

        jlbPassword.setText("Mật khẩu:");

        // main Panel
        JPanel mainPanel = new JPanel(new BorderLayout(10, 15));
        mainPanel.setBorder(
                BorderFactory.createEmptyBorder(25, 0, 15, 0)
        );
        JPanel formPanel = new JPanel(new GridBagLayout());

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(8, 8, 8, 8);
        gbc.anchor = GridBagConstraints.WEST;

        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.weightx = 0;
        gbc.fill = GridBagConstraints.NONE;
        formPanel.add(jlbAccount, gbc);

        gbc.gridx = 1;
        gbc.weightx = 1.0;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        formPanel.add(txtUsername, gbc);

        gbc.gridx = 0;
        gbc.gridy = 1;
        gbc.weightx = 0;
        gbc.fill = GridBagConstraints.NONE;
        formPanel.add(jlbPassword, gbc);

        gbc.gridx = 1;
        gbc.weightx = 1.0;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        formPanel.add(PasswordFieldWithReveal.create(txtPassword), gbc);


        JPanel southPanel = new JPanel();
        southPanel.setLayout(
                new BoxLayout(southPanel, BoxLayout.Y_AXIS)
        );

        JPanel actionPanel = new JPanel(
                new FlowLayout(FlowLayout.CENTER, 25, 5)
        );

        // define login and logout function
        loginAccount();
        logoutAccount();
        registerAccount();

        actionPanel.add(loginBtn);
        actionPanel.add(logoutBtn);

        JPanel registerPanel = new JPanel(
                new FlowLayout(FlowLayout.CENTER, 8, 5)
        );
        JPanel forgotPasswordPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 8, 5));

        JLabel lblRegisterQuestion =
                new JLabel("Bạn chưa có tài khoản?");


        registerPanel.add(lblRegisterQuestion);
        registerPanel.add(registerBtn);
        forgotPasswordBtn = new JButton("<html><u>Quên mật khẩu?</u></html>");
        forgotPasswordBtn.setFont(new Font("SansSerif", Font.PLAIN, 12));
        forgotPasswordBtn.setForeground(UIColors.BUTTON);
        forgotPasswordBtn.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        forgotPasswordBtn.setFocusPainted(false);
        forgotPasswordBtn.setContentAreaFilled(false);
        forgotPasswordBtn.setBorderPainted(false);
        forgotPasswordBtn.setOpaque(false);
        forgotPasswordBtn.setMargin(new Insets(0, 0, 0, 0));
        forgotPasswordBtn.addActionListener(e -> forgotPassword());
        forgotPasswordPanel.add(forgotPasswordBtn);

        southPanel.add(actionPanel);
        southPanel.add(Box.createVerticalStrut(8));
        southPanel.add(registerPanel);
        southPanel.add(forgotPasswordPanel);

        mainPanel.add(headerPanel, BorderLayout.NORTH);
        mainPanel.add(formPanel, BorderLayout.CENTER);
        mainPanel.add(southPanel, BorderLayout.SOUTH);

        setContentPane(mainPanel);
        mainPanel.setBackground(UIColors.BACKGROUND);
        formPanel.setBackground(UIColors.BACKGROUND);

        pack();
        setLocationRelativeTo(null);
    }

    private void forgotPassword() {
        String email = JOptionPane.showInputDialog(this, "Nhập email đã đăng ký:", "Quên mật khẩu",
                JOptionPane.QUESTION_MESSAGE);
        if (email == null || email.isBlank()) {
            return;
        }

        try {
            boolean codeSent = accountBUS.sendPasswordResetCode(email);
            JOptionPane.showMessageDialog(this,
                    "Nếu email đã đăng ký, mã xác nhận sẽ được gửi đến hộp thư đó.");
            if (!codeSent) {
                return;
            }

            String code = showOtpVerificationDialog(email);
            if (code == null) {
                return;
            }

            JPasswordField passwordField = new JPasswordField(16);
            JPasswordField confirmField = new JPasswordField(16);
            JPanel passwordPanel = new JPanel(new GridLayout(0, 1, 4, 4));
            passwordPanel.add(new JLabel("Mật khẩu mới:"));
            passwordPanel.add(passwordField);
            passwordPanel.add(new JLabel("Xác nhận mật khẩu:"));
            passwordPanel.add(confirmField);
            if (JOptionPane.showConfirmDialog(this, passwordPanel, "Đặt lại mật khẩu",
                    JOptionPane.OK_CANCEL_OPTION) != JOptionPane.OK_OPTION) {
                return;
            }

            boolean updated = accountBUS.resetCustomerPassword(email, code,
                    new String(passwordField.getPassword()), new String(confirmField.getPassword()));
            if (updated) {
                JOptionPane.showMessageDialog(this, "Đặt lại mật khẩu thành công.");
            } else {
                JOptionPane.showMessageDialog(this, "Không thể cập nhật mật khẩu. Vui lòng thử lại.",
                        "Lỗi", JOptionPane.ERROR_MESSAGE);
            }
        } catch (IllegalArgumentException ex) {
            JOptionPane.showMessageDialog(this, ex.getMessage(), "Dữ liệu không hợp lệ",
                    JOptionPane.WARNING_MESSAGE);
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, "Không thể gửi mã hoặc đặt lại mật khẩu: " + ex.getMessage(),
                    "Lỗi", JOptionPane.ERROR_MESSAGE);
        }
    }

    private String showOtpVerificationDialog(String email) {
        JDialog dialog = new JDialog(this, "Nhập mã OTP", true);
        dialog.setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);

        JTextField codeField = new JTextField(10);
        JLabel messageLabel = new JLabel("Mã xác nhận đã được gửi đến email của bạn.");
        messageLabel.setForeground(Color.GRAY);
        JButton resendButton = new JButton("Gửi lại OTP");
        resendButton.setFont(resendButton.getFont().deriveFont(Font.PLAIN, 11f));
        resendButton.setBorderPainted(false);
        resendButton.setContentAreaFilled(false);
        resendButton.setForeground(UIColors.BUTTON);
        resendButton.setEnabled(false);

        JPanel content = new JPanel(new GridBagLayout());
        content.setBorder(BorderFactory.createEmptyBorder(12, 16, 12, 16));
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.gridwidth = 2;
        gbc.anchor = GridBagConstraints.WEST;
        gbc.insets = new Insets(4, 4, 8, 4);
        content.add(messageLabel, gbc);

        gbc.gridy++;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        content.add(codeField, gbc);

        JLabel resendHint = new JLabel("Bạn chưa nhận được mã?");
        resendHint.setFont(resendHint.getFont().deriveFont(Font.PLAIN, 11f));
        gbc.gridy++;
        gbc.gridwidth = 1;
        gbc.fill = GridBagConstraints.NONE;
        content.add(resendHint, gbc);
        gbc.gridx = 1;
        gbc.insets = new Insets(4, 0, 8, 4);
        content.add(resendButton, gbc);

        JButton verifyButton = ButtonFactory.create("Xác nhận", UIColors.BUTTON);
        JButton cancelButton = ButtonFactory.create("Hủy", UIColors.BUTTON);
        JPanel buttons = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        buttons.add(cancelButton);
        buttons.add(verifyButton);
        dialog.add(content, BorderLayout.CENTER);
        dialog.add(buttons, BorderLayout.SOUTH);

        int[] secondsRemaining = {60};
        Timer resendTimer = new Timer(1000, event -> {
            secondsRemaining[0]--;
            if (secondsRemaining[0] <= 0) {
                resendButton.setEnabled(true);
                resendButton.setText("Gửi lại OTP");
                ((Timer) event.getSource()).stop();
            } else {
                resendButton.setText("Gửi lại OTP (" + secondsRemaining[0] + "s)");
            }
        });
        resendButton.setText("Gửi lại OTP (60s)");
        resendTimer.start();

        String[] verifiedCode = {null};
        verifyButton.addActionListener(event -> {
            try {
                String code = codeField.getText().trim();
                accountBUS.verifyPasswordResetCode(email, code);
                verifiedCode[0] = code;
                dialog.dispose();
            } catch (IllegalArgumentException ex) {
                messageLabel.setText(ex.getMessage());
                messageLabel.setForeground(Color.RED);
            }
        });
        resendButton.addActionListener(event -> {
            try {
                if (!accountBUS.sendPasswordResetCode(email)) {
                    messageLabel.setText("Không thể gửi mã. Vui lòng kiểm tra email.");
                    messageLabel.setForeground(Color.RED);
                    return;
                }
                messageLabel.setText("Đã gửi mã OTP mới đến email của bạn.");
                messageLabel.setForeground(Color.GRAY);
                codeField.setText("");
                secondsRemaining[0] = 60;
                resendButton.setEnabled(false);
                resendButton.setText("Gửi lại OTP (60s)");
                resendTimer.restart();
            } catch (MessagingException | IllegalArgumentException | IllegalStateException ex) {
                messageLabel.setText(ex.getMessage());
                messageLabel.setForeground(Color.RED);
            }
        });
        cancelButton.addActionListener(event -> dialog.dispose());
        dialog.getRootPane().setDefaultButton(verifyButton);
        dialog.pack();
        dialog.setLocationRelativeTo(this);
        dialog.setVisible(true);
        resendTimer.stop();
        return verifiedCode[0];
    }

    private void loginAccount(){
        loginBtn = ButtonFactory.create("Đăng nhập", UIColors.SAVE);
        loginBtn.addActionListener(v -> {
            String username = txtUsername.getText().trim();
            char[] passwordChars = txtPassword.getPassword();
            String password = new String(passwordChars).trim();
            Arrays.fill(passwordChars, '\0');

            if (username.isEmpty() || password.isEmpty()) {
                JOptionPane.showMessageDialog(this, "Vui lòng nhập đầy đủ tài khoản và mật khẩu.");
                return;
            }

            AccountDTO account = accountBUS.login(username, password);
            if (account == null) {
                JOptionPane.showMessageDialog(this, "Sai tài khoản hoặc mật khẩu.");
                return;
            }

            SessionManager.loginAccount(account);
            if (successAction != null) {
                SwingUtilities.invokeLater(successAction);
                dispose();
                return;
            }
            MainFrame mainFrame = new MainFrame();
            mainFrame.setVisible(true);
            this.dispose();
        });
    }

    private void logoutAccount(){
        logoutBtn = ButtonFactory.create("Thoát", UIColors.CANCEL);
        logoutBtn.addActionListener(v -> {
            dispose();
        });
    }

    private void initLogoWithLink(String link){
        // Set favicon
        try {
            ImageIcon icon = new ImageIcon(Objects.requireNonNull(getClass().getClassLoader().getResource(link)));
            setIconImage(icon.getImage());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void registerAccount(){
        registerBtn = ButtonFactory.create("Đăng ký", UIColors.VIEW);
        registerBtn.addActionListener(e -> {
            RegisterDialog dialog = new RegisterDialog(
                    null,
                    accountBUS
            );

            dialog.setVisible(true);

            if (dialog.isRegistered()) {
                txtUsername.setText(
                        dialog.getRegisteredUsername()
                );

                txtPassword.setText("");
                txtPassword.requestFocus();
            }
        });

    }
}