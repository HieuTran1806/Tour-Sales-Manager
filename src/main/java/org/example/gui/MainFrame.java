package org.example.gui;

import lombok.AccessLevel;
import lombok.experimental.FieldDefaults;
import org.example.dto.AccountDTO;
import org.example.enums.Permission;
import org.example.gui.panel.*;
import org.example.login.SessionManager;

import java.awt.*;
import java.util.List;
import java.util.Objects;

import javax.swing.*;

import org.example.login.Login;

@FieldDefaults(level = AccessLevel.PRIVATE)
public class MainFrame extends JFrame {
    //layout
    CardLayout cardLayout;
    JPanel contentArea;
    JButton activeButton;

    private record MenuItem(
            String label,
            String cardName,
            Permission permission
    ) {
    }

    public MainFrame() {
        AccountDTO account = SessionManager.getCurrentAccount();

        System.out.println(account.getIdAccount());
        System.out.println(account.getRole());


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
        List<MenuItem> menus = List.of(
                new MenuItem("Tour", "Tour", Permission.VIEW_TOUR),
                new MenuItem("Loại Tour", "LoaiTour", Permission.VIEW_TOUR_TYPE),
                new MenuItem("Kế Hoạch Tour", "KeHoachTour", Permission.VIEW_TOUR_PLAN),
                new MenuItem("PhieuDatTour", "PhieuDatTour", Permission.VIEW_BOOKING),
                new MenuItem("Hóa đơn", "HoaDon", Permission.VIEW_INVOICE),
                new MenuItem("Địa điểm", "DiaDiem", Permission.VIEW_LOCATION),
                new MenuItem("Nhân viên", "NhanVien", Permission.VIEW_STAFF),
                new MenuItem("Khách hàng", "KhachHang", Permission.VIEW_CUSTOMER),
                new MenuItem("Chương trình khuyến mãi", "CTrinhKM", Permission.VIEW_PROMOTION),
                new MenuItem("Lịch khuyến mãi", "CalendarKM", Permission.VIEW_PROMOTION_CALENDAR),
                new MenuItem("Thống kê", "ThongKe", Permission.VIEW_STATISTICS)

        );

        for(MenuItem menu : menus){
            if(!SessionManager.hasPermission(menu.permission)){
                continue;
            }
            System.out.println(menu.permission);

            JButton button = createMenuButton(menu.label, menu.cardName, null);
            sidebar.add(button);

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

        if(SessionManager.hasPermission(Permission.VIEW_TOUR))
            contentArea.add(new TourPanel(), "Tour");

        if(SessionManager.hasPermission(Permission.VIEW_TOUR_TYPE))
            contentArea.add(new TourTypePanel(), "LoaiTour");

        if(SessionManager.hasPermission(Permission.VIEW_TOUR_PLAN))
            contentArea.add(new TourPlanPanel(), "KeHoachTour");

        if(SessionManager.hasPermission(Permission.VIEW_BOOKING))
            contentArea.add(new TourBookingPanel(), "PhieuDatTour");

        if(SessionManager.hasPermission(Permission.VIEW_INVOICE))
            contentArea.add(new InvoicePanel(), "HoaDon");

        if(SessionManager.hasPermission(Permission.VIEW_LOCATION))
            contentArea.add(new LocationPanel(), "DiaDiem");

        if(SessionManager.hasPermission(Permission.VIEW_STAFF))
            contentArea.add(new StaffPanel(), "NhanVien");

        if(SessionManager.hasPermission(Permission.VIEW_CUSTOMER))
            contentArea.add(new CustomerPanel(), "KhachHang");

        if(SessionManager.hasPermission(Permission.VIEW_PROMOTION))
            contentArea.add(new PromotionPanel(), "CTrinhKM");

        if(SessionManager.hasPermission(Permission.VIEW_PROMOTION_CALENDAR))
            contentArea.add(new CalendarKMPanel(), "CalendarKM");

        if(SessionManager.hasPermission(Permission.VIEW_STATISTICS))
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
            SessionManager.class.getMethod("dangXuat").invoke(null);
        } catch (Exception ignored) {}

        SwingUtilities.invokeLater(() -> {
            Login dangNhap = new Login();
            dangNhap.setVisible(true);
            this.dispose();
        });
    }
}