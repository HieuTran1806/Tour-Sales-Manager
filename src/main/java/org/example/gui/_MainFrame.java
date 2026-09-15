package org.example.gui;

import org.example.dto.TaiKhoanDTO;
import org.example.gui.panel.*;
import org.example.login.PhanQuyen;

import java.awt.*;
import java.util.Objects;

import javax.swing.*;

import org.example.login.DangNhap;

public class _MainFrame extends JFrame {
    //layout
    private CardLayout cardLayout;
    private JPanel contentArea;
    private JButton activeButton;

    public _MainFrame(TaiKhoanDTO taiKhoanDangNhap) {
        // Set favicon
        try {
            ImageIcon icon = new ImageIcon(Objects.requireNonNull(getClass().getClassLoader().getResource("logosgu.png")));
            setIconImage(icon.getImage());
        } catch (Exception e) {
            e.printStackTrace();
        }
        setTitle("Quản lý Tour du lịch");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);


        setSize(1000, 600);
        setLocationRelativeTo(null);

        setExtendedState(JFrame.MAXIMIZED_BOTH);

        setLayout(new BorderLayout());
        add(buildSideBar(), BorderLayout.WEST);
        add(buildContentArea(), BorderLayout.CENTER);

        setVisible(true);
    }

    private JPanel buildSideBar(){
        JPanel sidebar = new JPanel();
        sidebar.setBackground(new Color(30, 90, 160));
        sidebar.setPreferredSize(new Dimension(240, 0));
        sidebar.setLayout(new BoxLayout(sidebar, BoxLayout.Y_AXIS));

        //menu items: label, cardName
        String[][] menus;
        if (PhanQuyen.laQuanLy()) {
            menus = new String[][]{
                    {"Tour", "Tour"},
                    {"Loại Tour", "LoaiTour"},
                    {"Kế Hoạch Tour", "KeHoachTour"},
                    {"Phiếu Đặt Tour", "PhieuDatTour"},
                    {"Hóa đơn", "HoaDon"},
                    {"Địa điểm", "DiaDiem"},
                    {"Nhân viên", "NhanVien"},
                    {"Khách hàng", "KhachHang"},
                    {"Chương trình khuyến mãi", "CTrinhKM"},
                    {"Lịch khuyến mãi", "CalendarKM"},
                    {"Thống kê", "ThongKe"},
            };
        } else {
            menus = new String[][]{
                    {"Tour", "Tour"},
                    {"Loại Tour", "LoaiTour"},
                    {"Kế Hoạch Tour", "KeHoachTour"},
                    {"Phiếu Đặt Tour", "PhieuDatTour"},
                    {"Hóa đơn", "HoaDon"},
                    {"Địa điểm", "DiaDiem"},
                    {"Khách hàng", "KhachHang"},
                    {"Chương trình khuyến mãi", "CTrinhKM"},
                    {"Lịch khuyến mãi", "CalendarKM"},
            };
        }

        for(String[] m : menus){
            JButton btn = createMenuButton(m[0], m[1], null);
            sidebar.add(btn);
            sidebar.add(Box.createRigidArea(new Dimension(0, 2)));
        }
        sidebar.add(Box.createVerticalGlue());

        JButton logoutBtn = createMenuButton("Đăng xuất", null, Color.RED);
        logoutBtn.addActionListener(e -> handleLogout());

        sidebar.add(logoutBtn);

        return sidebar;
    }

    private JPanel buildContentArea(){
        cardLayout = new CardLayout();
        contentArea = new JPanel(cardLayout);
        contentArea.setBackground(Color.cyan);

        contentArea.add(new TourPanel(), "Tour");
        contentArea.add(new LoaiTourPanel(), "LoaiTour");
        contentArea.add(new KeHoachTourPanel(), "KeHoachTour");
        contentArea.add(new PhieuDatTourPanel(), "PhieuDatTour");
        contentArea.add(new HoaDonPanel(), "HoaDon");
        contentArea.add(new DiaDiemPanel(), "DiaDiem");
        contentArea.add(new NhanVienPanel(), "NhanVien");
        contentArea.add(new KhachHangPanel(), "KhachHang");
        contentArea.add(new CTrinhKMPanel(), "CTrinhKM");
        contentArea.add(new CalendarKMPanel(), "CalendarKM");
        contentArea.add(new StatisticsPanel(), "ThongKe");

        return contentArea;
    }

    private JButton createMenuButton(String text, String card, Color color){
        JButton btn = new JButton(text) {
            @Override protected void paintComponent(Graphics g) {
                if(color != null){
                    g.setColor(color);
                    g.fillRect(0, 0, getWidth(), getHeight());
                }else if (this == activeButton) {
                    g.setColor(new Color(255, 255, 255, 30));
                    g.fillRoundRect(8, 4, getWidth() - 16, getHeight() - 8, 10, 10);
                } else if (getModel().isRollover()) {
                    g.setColor(Color.blue);
                    g.fillRect(0, 0, getWidth(), getHeight());
                }
                super.paintComponent(g);
            }
        };


        btn.setOpaque(false);
        btn.setContentAreaFilled(false);
        btn.setBorderPainted(false);
        btn.setFocusPainted(false);
        btn.setHorizontalAlignment(SwingConstants.LEFT);
        btn.setFont(new Font("SansSerif", Font.PLAIN, 14));
        btn.setForeground(Color.WHITE);
        btn.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        btn.setMaximumSize(new Dimension(240, 48));
        btn.setPreferredSize(new Dimension(240, 48));
        btn.setBorder(BorderFactory.createEmptyBorder(0, 24, 0, 0));

        btn.addActionListener(e -> {
            if(activeButton != null) // fix color painted
                activeButton.repaint();

            activeButton = btn;
            btn.repaint();
            cardLayout.show(contentArea, card);

            if(card != null) { // nếu card null (nút đăng xuất) sẽ là nút bình thường không show ra như những nút khác
                cardLayout.show(contentArea, card);
            }
        });
        return btn;
    }

    private void handleLogout() {
        try {
            PhanQuyen.class.getMethod("dangXuat").invoke(null);
        } catch (Exception ignored) {}

        SwingUtilities.invokeLater(() -> {
            DangNhap dangNhap = new DangNhap();
            dangNhap.setVisible(true);
            this.dispose();
        });
    }
}