package org.example.login;

import lombok.AccessLevel;
import lombok.experimental.FieldDefaults;
import org.example.bus.AccountBUS;
import org.example.dao.AccountDAO;
import org.example.dto.AccountDTO;
import org.example.gui.MainFrame;
import org.example.gui.component.ButtonFactory;
import org.example.gui.dialog.RegisterDialog;
import org.example.gui.helper.UIColors;

import java.awt.*;
import java.util.Arrays;
import java.util.Objects;
import javax.swing.*;

@FieldDefaults(level = AccessLevel.PRIVATE)
public class Login extends JFrame {
    JButton loginBtn, logoutBtn, registerBtn;
    JLabel jlbAccount, jlbPassword;
    JTextField txtUsername;
    JPasswordField txtPassword;
    AccountBUS accountBUS;

    public Login() {
        initComponents();
        setLocationRelativeTo(null);
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
        txtPassword = new JPasswordField(20);
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
        setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);

        jlbAccount.setText("Tài khoản:");

        jlbPassword.setText("Mật khẩu:");



        // main Panel
        JPanel mainPanel = new JPanel(new BorderLayout(10, 15));
        mainPanel.setBorder(
                BorderFactory.createEmptyBorder(25, 30, 15, 30)
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
        formPanel.add(txtPassword, gbc);


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

        JLabel lblRegisterQuestion =
                new JLabel("Bạn chưa có tài khoản?");


        registerPanel.add(lblRegisterQuestion);
        registerPanel.add(registerBtn);

        southPanel.add(actionPanel);
        southPanel.add(Box.createVerticalStrut(8));
        southPanel.add(registerPanel);

        mainPanel.add(headerPanel, BorderLayout.NORTH);
        mainPanel.add(formPanel, BorderLayout.CENTER);
        mainPanel.add(southPanel, BorderLayout.SOUTH);

        setContentPane(mainPanel);



        pack();
        setLocationRelativeTo(null);
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

            AccountBUS accountBUS = new AccountBUS();
            AccountDTO account = accountBUS.login(username, password);
            if (account == null) {
                JOptionPane.showMessageDialog(this, "Sai tài khoản hoặc mật khẩu.");
                return;
            }

            SessionManager.loginAccount(account);
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
                    this,
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