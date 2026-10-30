package org.toursalesmanager.gui;

import lombok.AccessLevel;
import lombok.experimental.FieldDefaults;
import org.toursalesmanager.bus.CustomerBUS;
import org.toursalesmanager.bus.StaffBUS;
import org.toursalesmanager.dto.AccountDTO;
import org.toursalesmanager.dto.CustomerDTO;
import org.toursalesmanager.dto.StaffDTO;
import org.toursalesmanager.enums.Permission;
import org.toursalesmanager.enums.Role;
import org.toursalesmanager.gui.helper.UIColors;
import org.toursalesmanager.gui.panel.*;
import org.toursalesmanager.login.SessionManager;

import java.awt.*;
import java.util.List;
import java.util.Objects;

import javax.swing.*;

import org.toursalesmanager.login.Login;

@FieldDefaults(level = AccessLevel.PRIVATE)
public class MainFrame extends JFrame {
    //layout
    CardLayout cardLayout;
    JPanel contentArea;
    JButton activeButton;

    AccountDTO account;

    private record MenuItem(
            String label,
            String cardName,
            Permission permission
    ) {
    }

    public MainFrame() {
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
        buildLayout();

        setVisible(true);
    }

    // Dựng lại sidebar và nội dung theo trạng thái đăng nhập hiện tại
    private void buildLayout() {
        account = SessionManager.isLoggedIn() ? SessionManager.getCurrentAccount() : null;
        activeButton = null;

        getContentPane().removeAll();
        add(buildSideBar(), BorderLayout.WEST);
        add(buildContentArea(), BorderLayout.CENTER);
        cardLayout.show(contentArea, "Tour");

        revalidate();
        repaint();
    }

    /**
     * Chạy action ngay nếu đã đăng nhập; nếu chưa thì mở hộp thoại đăng nhập,
     * đăng nhập thành công thì dựng lại giao diện theo quyền mới rồi mới chạy action.
     */
    public static void requireLogin(Component source, Runnable action) {
        if (SessionManager.isLoggedIn()) {
            action.run();
            return;
        }

        // source có thể chính là MainFrame (nút ở sidebar), khi đó getWindowAncestor trả về null
        Window owner = source instanceof Window window
                ? window
                : SwingUtilities.getWindowAncestor(source);
        Login login = new Login(owner, () -> {
            if (owner instanceof MainFrame mainFrame) {
                mainFrame.buildLayout();
            }
            action.run();
        });
        login.setVisible(true);
    }

    private JPanel buildSideBar(){
        JPanel sidebar = new JPanel();

        sidebar.setBackground(UIColors.SIDEBAR_BACKGROUND);
        sidebar.setPreferredSize(new Dimension(240, 0));
        sidebar.setLayout(new BoxLayout(sidebar, BoxLayout.Y_AXIS));

        //menu items: label, cardName
        List<MenuItem> menus = List.of(
                new MenuItem("Tour", "Tour", Permission.VIEW_TOUR),
                new MenuItem("Loại Tour", "LoaiTour", Permission.VIEW_TOUR_TYPE),
                new MenuItem("Kế Hoạch Tour", "KeHoachTour", Permission.VIEW_TOUR_PLAN),
                new MenuItem("Phiếu Đặt Tour", "PhieuDatTour", Permission.VIEW_BOOKING),
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

        // Trang phân quyền chỉ dành cho quản trị viên
        if (SessionManager.isAdmin()) {
            sidebar.add(createMenuButton("Phân quyền", "PhanQuyen", null));
            sidebar.add(Box.createRigidArea(new Dimension(0, 2)));
        }

        if (SessionManager.isLoggedIn()) {
            sidebar.add(createMenuButton("Thông tin cá nhân", "ThongTinCaNhan", null));
            sidebar.add(Box.createRigidArea(new Dimension(0, 2)));
        }

        sidebar.add(Box.createVerticalGlue());

        JButton authBtn;
        if (SessionManager.isLoggedIn()) {
            authBtn = createMenuButton("Đăng xuất", null, UIColors.LOGOUT_BACKGROUND);
            authBtn.addActionListener(e -> handleLogout());
        } else {
            authBtn = createMenuButton("Đăng nhập", null, UIColors.LOGOUT_BACKGROUND);
            authBtn.addActionListener(e -> requireLogin(this, () -> {}));
        }

        sidebar.add(authBtn);

        return sidebar;
    }

    private JPanel buildContentArea(){
        cardLayout = new CardLayout();
        contentArea = new JPanel(cardLayout);
        contentArea.setBackground(UIColors.MAIN_CONTENT_BACKGROUND);

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

        if(SessionManager.hasPermission(Permission.VIEW_STATISTICS)){
            contentArea.add(new StatisticsPanel(), "ThongKe");
        }

        if (SessionManager.isAdmin())
            contentArea.add(new PermissionPanel(), "PhanQuyen");


        // Khách chưa đăng nhập không có trang thông tin cá nhân
        if (account == null) {
            return contentArea;
        }

        CustomerDTO customerDTO = null;
        StaffDTO staffDTO = null;
        if(account.getIdCustomer() != null){
            CustomerBUS bus = new CustomerBUS();
            customerDTO = bus.findCustomerById(account.getIdCustomer());
        }else if(account.getRole() == Role.CUSTOMER){
            CustomerBUS bus = new CustomerBUS();
            customerDTO = bus.findCustomerById(account.getUsername());
        }else if(account.getIdStaff() != null || account.getRole() != Role.CUSTOMER){
            StaffBUS bus = new StaffBUS(false);
            String staffId = account.getIdStaff() != null
                    ? account.getIdStaff()
                    : account.getUsername();
            staffDTO = bus.findStaffById(staffId);
            if (staffDTO != null && staffDTO.getRole() == null) {
                staffDTO.setRole(account.getRole());
            }
        }else{
            System.err.println("Tài khoản khách hàng chưa được liên kết với mã khách hàng.");
        }

        contentArea.add(new ProfilePanel(staffDTO, customerDTO), "ThongTinCaNhan");

        return contentArea;
    }

    private JButton createMenuButton(String text, String card, Color color){
        JButton btn = new JButton(text) {
            @Override protected void paintComponent(Graphics g) {
                if(color != null){
                    g.setColor(getModel().isRollover() ? UIColors.LOGOUT_HOVER : color);
                    g.fillRoundRect(8, 4, getWidth() - 16, getHeight() - 8, 12, 12);
                }else if (this == activeButton) {
                    g.setColor(UIColors.SIDEBAR_ACTIVE);
                    g.fillRoundRect(8, 4, getWidth() - 16, getHeight() - 8, 12, 12);
                } else if (getModel().isRollover()) {
                    g.setColor(UIColors.SIDEBAR_HOVER);
                    g.fillRoundRect(8, 4, getWidth() - 16, getHeight() - 8, 12, 12);
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
        btn.setForeground(UIColors.SIDEBAR_TEXT);
        btn.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        btn.setMaximumSize(new Dimension(240, 48));
        btn.setPreferredSize(new Dimension(240, 48));
        btn.setBorder(BorderFactory.createEmptyBorder(0, 24, 0, 0));

        btn.addActionListener(e -> {
            if(activeButton != null) // fix color painted
                activeButton.repaint();

            activeButton = btn;
            btn.repaint();
            if(card != null) {
                cardLayout.show(contentArea, card);
            }
        });
        return btn;
    }

    private void handleLogout() {
        SessionManager.logoutAccount();
        buildLayout();
    }
}