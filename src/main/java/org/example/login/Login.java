package org.example.login;

import lombok.AccessLevel;
import lombok.experimental.FieldDefaults;
import org.example.dao.TaiKhoanDAO;
import org.example.dto.TaiKhoanDTO;
import org.example.gui.MainFrame;
import org.example.gui.panel.UIColors;

import java.awt.*;
import java.util.Arrays;
import java.util.Objects;
import javax.swing.*;

@FieldDefaults(level = AccessLevel.PRIVATE)
public class Login extends JFrame {
    JButton loginBtn, logoutBtn;
    JLabel jlbAccount, jlbPassword;
    JTextField txtUsername;
    JPasswordField txtPassword;

    final TaiKhoanDAO taiKhoanDAO = new TaiKhoanDAO();

    public Login() {
        initComponents();
        setLocationRelativeTo(null);
    }

    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {
        // Set favicon
        try {
            ImageIcon icon = new ImageIcon(Objects.requireNonNull(getClass().getClassLoader().getResource("logosgu.png")));
            setIconImage(icon.getImage());
        } catch (Exception e) {
            e.printStackTrace();
        }
        //set title
        setTitle("Đăng Nhập - Tour Management System");

        jlbAccount = new JLabel();
        jlbPassword = new JLabel();
        loginBtn = new JButton();
        logoutBtn = new JButton();
        txtUsername = new JTextField();
        txtPassword = new JPasswordField();
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

        // define login and logout function
        loginAccount();
        logoutAccount();

        // main Panel
        JPanel mainPanel = new JPanel();
        GroupLayout layout = new GroupLayout(mainPanel);
        mainPanel.setLayout(layout);
        layout.setHorizontalGroup(
                layout.createParallelGroup(GroupLayout.Alignment.LEADING)
                        .addGroup(layout.createSequentialGroup()
                                .addGroup(layout.createParallelGroup(GroupLayout.Alignment.LEADING)
                                        .addGroup(layout.createSequentialGroup()
                                                .addGap(10, 10, 10))
                                        .addGroup(GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                                                .addGap(106, 106, 106)
                                                .addGroup(layout.createParallelGroup(GroupLayout.Alignment.TRAILING)
                                                        .addComponent(jlbAccount)
                                                        .addComponent(jlbPassword))
                                                .addPreferredGap(LayoutStyle.ComponentPlacement.RELATED)
                                                .addGroup(layout.createParallelGroup(GroupLayout.Alignment.LEADING, false)
                                                        .addComponent(txtUsername)
                                                        .addComponent(txtPassword, GroupLayout.DEFAULT_SIZE, 134, Short.MAX_VALUE))))
                                .addContainerGap(GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                        .addGroup(layout.createSequentialGroup()
                                .addGap(82, 82, 82)
                                .addComponent(loginBtn)
                                .addPreferredGap(LayoutStyle.ComponentPlacement.RELATED, 77, Short.MAX_VALUE)
                                .addComponent(logoutBtn)
                                .addGap(80, 80, 80))
        );
        layout.setVerticalGroup(
                layout.createParallelGroup(GroupLayout.Alignment.LEADING)
                        .addGroup(layout.createSequentialGroup()
                                .addContainerGap()
                                .addGap(50, 50, 50)
                                .addGroup(layout.createParallelGroup(GroupLayout.Alignment.BASELINE)
                                        .addComponent(jlbAccount)
                                        .addComponent(txtUsername, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE))
                                .addGap(18, 18, 18)
                                .addGroup(layout.createParallelGroup(GroupLayout.Alignment.BASELINE)
                                        .addComponent(jlbPassword)
                                        .addComponent(txtPassword, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE))
                                .addPreferredGap(LayoutStyle.ComponentPlacement.RELATED, 66, Short.MAX_VALUE)
                                .addGroup(layout.createParallelGroup(GroupLayout.Alignment.BASELINE)
                                        .addComponent(loginBtn)
                                        .addComponent(logoutBtn))
                                .addGap(20, 20, 20))
        );

        add(headerPanel, BorderLayout.NORTH);
        add(mainPanel, BorderLayout.CENTER);
        pack();
    }

    private void loginAccount(){
        loginBtn = createBtn("Đăng nhập", UIColors.SAVE);
        loginBtn.addActionListener(v -> {
            String username = txtUsername.getText().trim();
            char[] passwordChars = txtPassword.getPassword();
            String password = new String(passwordChars).trim();
            Arrays.fill(passwordChars, '\0');

            if (username.isEmpty() || password.isEmpty()) {
                JOptionPane.showMessageDialog(this, "Vui lòng nhập đầy đủ tài khoản và mật khẩu.");
                return;
            }

            TaiKhoanDTO account = taiKhoanDAO.dangNhap(username, password);
            if (account == null) {
                JOptionPane.showMessageDialog(this, "Sai tài khoản hoặc mật khẩu.");
                return;
            }

            SessionManager.loginAccount(account);
            MainFrame mainFrame = new MainFrame(account);
            mainFrame.setVisible(true);
            this.dispose();
        });
    }

    private void logoutAccount(){
        logoutBtn = createBtn("Thoát", UIColors.CANCEL);
        logoutBtn.addActionListener(v -> {
            dispose();
        });
    }

    private JButton createBtn(String text, Color color){
        JButton btn = new JButton(text);
        btn.setBackground(color);
        btn.setForeground(Color.WHITE);
        btn.setFocusPainted(false);
        btn.setFont(new Font("SansSerif", Font.BOLD, 13));
        btn.setCursor(new Cursor(Cursor.HAND_CURSOR)); // in south panel

        btn.setContentAreaFilled(true);
        btn.setOpaque(true);
        btn.setBorderPainted(false);
        return btn;
    }
}