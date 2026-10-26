package org.toursalesmanager.gui.panel;

import lombok.AccessLevel;
import lombok.experimental.FieldDefaults;
import org.toursalesmanager.dto.CustomerDTO;
import org.toursalesmanager.dto.StaffDTO;
import org.toursalesmanager.enums.Role;
import org.toursalesmanager.gui.helper.UIFontHelper;
import org.toursalesmanager.login.SessionManager;

import javax.swing.*;
import java.awt.*;
import java.awt.geom.Ellipse2D;
import java.awt.image.BufferedImage;
import java.net.URL;

@FieldDefaults(level = AccessLevel.PRIVATE)
public class ProfilePanel extends JPanel {
    JLabel lbMaNV, lbMaKH, lbHoTen, lbNgaySinh, lbGioiTinh, lbSDT, lbEmail, lbDiaChi, lbChucVu;
    JLabel lbTitle, lbLinkStatus;

    StaffDTO staffDTO;
    CustomerDTO customerDTO;

    public ProfilePanel(StaffDTO staffDTO, CustomerDTO customerDTO){
        this.staffDTO = staffDTO;
        this.customerDTO = customerDTO;

        init();
    }

    private void init(){
        setLayout(new BorderLayout());
        this.setBackground(new Color(238, 238, 238));

        this.add(createHeader(), BorderLayout.NORTH);
        this.add(createContent(), BorderLayout.CENTER);

        loadData();
    }

    private JPanel createHeader(){
        JPanel pnHeader = new JPanel(new BorderLayout());
        pnHeader.setOpaque(false);
        pnHeader.setBorder(BorderFactory.createEmptyBorder(15, 40, 10, 40));

        lbTitle = new JLabel("THÔNG TIN CÁ NHÂN");
        lbTitle.setFont(UIFontHelper.PAGE_TITLE);
        lbTitle.setHorizontalAlignment(SwingConstants.CENTER);
        lbTitle.setHorizontalTextPosition(SwingConstants.CENTER);
        pnHeader.add(lbTitle, BorderLayout.NORTH);

        lbLinkStatus = new JLabel(" ", SwingConstants.CENTER);
        lbLinkStatus.setForeground(new Color(180, 80, 0));
        pnHeader.add(lbLinkStatus, BorderLayout.SOUTH);

        return pnHeader;
    }

    private JPanel createContent(){
        JPanel content = new JPanel(new BorderLayout(40, 0));
        content.setOpaque(false);
        content.setBorder(BorderFactory.createEmptyBorder(10, 40, 20, 40));

        content.add(createInfo(), BorderLayout.CENTER);

        JPanel avatarPanel = new JPanel(new BorderLayout());
        avatarPanel.setOpaque(false);
        avatarPanel.add(setAvt(), BorderLayout.NORTH);
        content.add(avatarPanel, BorderLayout.EAST);
        return content;
    }

    private JPanel createInfo() {
        JPanel form = new JPanel(new GridBagLayout());
        form.setOpaque(false);

        lbMaNV     = new JLabel("-");
        lbMaKH     = new JLabel("-");
        lbHoTen    = new JLabel("-");
        lbNgaySinh = new JLabel("-");
        lbGioiTinh = new JLabel("-");
        lbSDT      = new JLabel("-");
        lbEmail    = new JLabel("-");
        lbDiaChi   = new JLabel("-");
        lbChucVu   = new JLabel("-");

        int row = 0;
        if(isStaff()){
            row = addRow(form, row, "Mã nhân viên", lbMaNV);
        } else {
            row = addRow(form, row, "Mã khách hàng", lbMaKH);
        }
        row = addRow(form, row, "Họ và tên", lbHoTen);
        row = addRow(form, row, "Ngày sinh", lbNgaySinh);
        row = addRow(form, row, "Giới tính", lbGioiTinh);
        row = addRow(form, row, "Số điện thoại", lbSDT);
        row = addRow(form, row, "Email", lbEmail);
        row = addRow(form, row, "Địa chỉ", lbDiaChi);
        if(isStaff()){
            row = addRow(form, row, "Chức vụ", lbChucVu);
        }

        // Ô trống cuối để đẩy các dòng lên trên
        GridBagConstraints gc = new GridBagConstraints();
        gc.gridy = row;
        gc.weighty = 1;
        form.add(Box.createGlue(), gc);

        return form;
    }

    // Thêm 1 dòng "nhãn — giá trị" + đường kẻ mờ, trả về số dòng tiếp theo
    private int addRow(JPanel form, int row, String label, JLabel value) {
        GridBagConstraints gc = new GridBagConstraints();
        gc.anchor = GridBagConstraints.WEST;

        // Nhãn (xám)
        gc.gridx = 0;
        gc.gridy = row;
        gc.insets = new Insets(10, 0, 10, 40);
        JLabel lb = new JLabel(label);
        lb.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        lb.setForeground(Color.GRAY);
        form.add(lb, gc);

        // Giá trị (đậm)
        gc.gridx = 1;
        gc.weightx = 1;
        gc.fill = GridBagConstraints.HORIZONTAL;
        gc.insets = new Insets(10, 0, 10, 0);
        value.setFont(new Font("Segoe UI", Font.BOLD, 14));
        value.setForeground(new Color(40, 40, 40));
        form.add(value, gc);

        // Đường kẻ ngăn cách
        GridBagConstraints gs = new GridBagConstraints();
        gs.gridx = 0;
        gs.gridy = row + 1;
        gs.gridwidth = 2;
        gs.fill = GridBagConstraints.HORIZONTAL;
        JSeparator sep = new JSeparator();
        sep.setForeground(new Color(220, 220, 220));
        form.add(sep, gs);

        return row + 2;
    }


    private JLabel setAvt() {
        JLabel lbAvt = new JLabel();
        URL url = getClass().getResource("/avt.png");
        if (url != null) {
            lbAvt.setIcon(makeCircle(new ImageIcon(url).getImage(), 160));
        } else {
            System.err.println("Không tìm thấy avt.png trong resources");
        }

        lbAvt.setHorizontalAlignment(SwingConstants.CENTER);
        lbAvt.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        lbAvt.setToolTipText("Ảnh đại diện");

        return lbAvt;
    }

    private ImageIcon makeCircle(Image image, int diameter) {
        int sourceWidth = image.getWidth(null);
        int sourceHeight = image.getHeight(null);
        if (sourceWidth <= 0 || sourceHeight <= 0 || diameter <= 0) {
            throw new IllegalArgumentException("Ảnh và kích thước bo tròn phải hợp lệ");
        }

        BufferedImage circle = new BufferedImage(diameter, diameter, BufferedImage.TYPE_INT_ARGB);
        Graphics2D g2 = circle.createGraphics();
        try {
            g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
            g2.setRenderingHint(RenderingHints.KEY_INTERPOLATION, RenderingHints.VALUE_INTERPOLATION_BICUBIC);
            g2.setClip(new Ellipse2D.Double(0, 0, diameter, diameter));

            double scale = Math.max((double) diameter / sourceWidth, (double) diameter / sourceHeight);
            int scaledWidth = (int) Math.ceil(sourceWidth * scale);
            int scaledHeight = (int) Math.ceil(sourceHeight * scale);
            int x = (diameter - scaledWidth) / 2;
            int y = (diameter - scaledHeight) / 2;
            g2.drawImage(image, x, y, scaledWidth, scaledHeight, null);
        } finally {
            g2.dispose();
        }
        return new ImageIcon(circle);
    }


    private void loadData(){
        if(isStaff()){
            if (staffDTO == null) {
                lbLinkStatus.setText(
                        "Chưa tìm thấy hồ sơ nhân viên. Tài khoản cũ cần có idstaff hoặc username trùng mã nhân viên."
                );
                return;
            }
            lbLinkStatus.setText(" ");
            lbMaNV.setText(staffDTO.getIdStaff());
            lbHoTen.setText(STR."\{staffDTO.getFirstName()} \{staffDTO.getLastName()}");
            lbNgaySinh.setText(String.valueOf(staffDTO.getDob()));
            lbGioiTinh.setText("null");
            lbSDT.setText(staffDTO.getPhoneNumber());
            lbEmail.setText("-");
            lbDiaChi.setText(staffDTO.getAddress());
            lbChucVu.setText(staffDTO.getRole() == null ? "-" : staffDTO.getRole().name());
        } else if (customerDTO != null) {
            lbLinkStatus.setText(" ");
            lbMaKH.setText(customerDTO.getIdCustomer());
            lbHoTen.setText(STR."\{customerDTO.getFirstName()} \{customerDTO.getLastName()}");
            lbNgaySinh.setText(String.valueOf(customerDTO.getDob()));
            lbGioiTinh.setText("-");
            lbSDT.setText(customerDTO.getPhoneNumber());
            lbEmail.setText("-");
            lbDiaChi.setText(customerDTO.getAddress());
        } else {
            lbLinkStatus.setText(
                    "Chưa tìm thấy hồ sơ khách hàng. Tài khoản cần được liên kết với mã khách hàng."
            );
        }
    }

    private boolean isStaff(){
        return !SessionManager.hasRole(Role.CUSTOMER);
    }

}
