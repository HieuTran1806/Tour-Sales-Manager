package org.toursalesmanager.gui.dialog;

import org.toursalesmanager.bus.TourBookingBUS;
import org.toursalesmanager.bus.TourPlanBUS;
import org.toursalesmanager.dto.TourPlanDTO;
import org.toursalesmanager.enums.Permission;
import org.toursalesmanager.gui.helper.AddComponentToForm;
import org.toursalesmanager.gui.helper.UIColors;
import org.toursalesmanager.login.SessionManager;

import javax.swing.*;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import java.awt.*;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

public class CustomerTourBookingDialog extends JDialog {
    private static final DateTimeFormatter DATE_FORMAT = DateTimeFormatter.ofPattern("dd/MM/yyyy");

    private final TourBookingBUS tourBookingBUS = new TourBookingBUS();
    private final TourPlanBUS tourPlanBUS;
    private final JComboBox<TourPlanDTO> tourPlanCombo = new JComboBox<>();
    private final JTextField ticketsField = new JTextField("1", 12);
    private final JTextArea noteArea = new JTextArea(3, 20);
    private final JLabel priceLabel = new JLabel("-");
    private final JLabel totalLabel = new JLabel("-");

    public CustomerTourBookingDialog(Frame parent, boolean modal, TourPlanBUS tourPlanBUS,
                                     String preselectedTourPlanId, String tourId) {
        super(parent, modal);
        this.tourPlanBUS = tourPlanBUS;

        if (!SessionManager.hasPermission(Permission.CREATE_BOOKING)) {
            throw new SecurityException("Bạn không có quyền đặt tour");
        }

        initialize(preselectedTourPlanId, tourId);
    }

    private void initialize(String preselectedTourPlanId, String tourId) {
        setTitle("Đặt tour");
        setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);
        setLayout(new BorderLayout(0, 12));
        setResizable(false);

        JPanel content = new JPanel(new BorderLayout(0, 16));
        content.setBorder(BorderFactory.createEmptyBorder(20, 24, 8, 24));
        content.setBackground(UIColors.BACKGROUND);

        JPanel heading = new JPanel();
        heading.setLayout(new BoxLayout(heading, BoxLayout.Y_AXIS));
        heading.setOpaque(false);
        JLabel titleLabel = new JLabel("Thông tin đặt tour");
        titleLabel.setFont(titleLabel.getFont().deriveFont(Font.BOLD, 20f));
        titleLabel.setForeground(UIColors.SIDEBAR_BACKGROUND);
        JLabel subtitleLabel = new JLabel("Chọn kế hoạch và số vé bạn muốn đăng ký.");
        subtitleLabel.setForeground(new Color(95, 112, 120));
        heading.add(titleLabel);
        heading.add(Box.createVerticalStrut(4));
        heading.add(subtitleLabel);
        content.add(heading, BorderLayout.NORTH);

        GridBagLayout formLayout = new GridBagLayout();
        JPanel form = new JPanel(formLayout);
        form.setOpaque(false);

        tourPlanCombo.setRenderer(new DefaultListCellRenderer() {
            @Override
            public Component getListCellRendererComponent(JList<?> list, Object value, int index,
                                                          boolean isSelected, boolean cellHasFocus) {
                super.getListCellRendererComponent(list, value, index, isSelected, cellHasFocus);
                if (value instanceof TourPlanDTO plan) {
                    String endDate = plan.getEndDate() == null
                            ? "-"
                            : plan.getEndDate().format(DATE_FORMAT);
                    setText(plan.getIdTourPlan() + " | "
                            + plan.getDepartureDate().format(DATE_FORMAT) + " - "
                            + endDate + " | Còn "
                            + plan.getRemainingTickets() + " vé");
                }
                return this;
            }
        });
        tourPlanCombo.setPreferredSize(new Dimension(360, 34));

        ticketsField.setPreferredSize(new Dimension(120, 34));
        noteArea.setLineWrap(true);
        noteArea.setWrapStyleWord(true);
        JScrollPane noteScrollPane = new JScrollPane(noteArea);
        noteScrollPane.setPreferredSize(new Dimension(360, 58));

        priceLabel.setFont(priceLabel.getFont().deriveFont(14f));
        priceLabel.setForeground(new Color(75, 91, 99));
        totalLabel.setFont(totalLabel.getFont().deriveFont(Font.BOLD, 16f));
        totalLabel.setForeground(UIColors.SIDEBAR_ACTIVE);

        addFormRow(form, formLayout, 0, "Kế hoạch tour:", tourPlanCombo);
        addFormRow(form, formLayout, 1, "Số vé:", ticketsField);
        addFormRow(form, formLayout, 2, "Đơn giá:", priceLabel);
        addFormRow(form, formLayout, 3, "Tổng tiền dự kiến:", totalLabel);
        addFormRow(form, formLayout, 4, "Ghi chú:", noteScrollPane);
        content.add(form, BorderLayout.CENTER);

        JButton submitButton = new JButton("Gửi yêu cầu đặt tour");
        submitButton.setBackground(UIColors.SAVE);
        submitButton.setForeground(Color.WHITE);
        submitButton.setFocusPainted(false);
        JButton closeButton = new JButton("Đóng");
        closeButton.setBackground(UIColors.CANCEL);
        closeButton.setForeground(Color.WHITE);
        closeButton.setFocusPainted(false);
        closeButton.addActionListener(event -> dispose());
        submitButton.addActionListener(event -> submitBooking());

        JPanel actions = new JPanel(new FlowLayout(FlowLayout.RIGHT, 10, 8));
        actions.setOpaque(false);
        actions.setBorder(BorderFactory.createEmptyBorder(0, 16, 8, 16));
        actions.add(submitButton);
        actions.add(closeButton);

        add(content, BorderLayout.CENTER);
        add(actions, BorderLayout.SOUTH);

        for (TourPlanDTO plan : tourPlanBUS.getAllKeHoachTours()) {
            if (plan != null && plan.getDepartureDate() != null
                    && plan.getRemainingTickets() > 0
                    && (tourId == null || tourId.equals(plan.getIdTour()))
                    && !plan.getDepartureDate().isBefore(LocalDate.now())) {
                tourPlanCombo.addItem(plan);
                if (plan.getIdTourPlan().equals(preselectedTourPlanId)) {
                    tourPlanCombo.setSelectedItem(plan);
                }
            }
        }

        tourPlanCombo.addActionListener(event -> updatePriceAndTotal());
        ticketsField.getDocument().addDocumentListener(new DocumentListener() {
            @Override
            public void insertUpdate(DocumentEvent event) {
                updatePriceAndTotal();
            }

            @Override
            public void removeUpdate(DocumentEvent event) {
                updatePriceAndTotal();
            }

            @Override
            public void changedUpdate(DocumentEvent event) {
                updatePriceAndTotal();
            }
        });
        updatePriceAndTotal();
        pack();
        setLocationRelativeTo(getParent());
    }

    private void addFormRow(JPanel form, GridBagLayout layout, int row,
                            String label, JComponent component) {
        int firstComponent = form.getComponentCount();
        AddComponentToForm.addFormRow(form, label, component);

        GridBagConstraints labelConstraints = new GridBagConstraints();
        labelConstraints.gridx = 0;
        labelConstraints.gridy = row;
        labelConstraints.anchor = GridBagConstraints.WEST;
        labelConstraints.insets = new Insets(3, 0, 3, 12);
        layout.setConstraints(form.getComponent(firstComponent), labelConstraints);

        GridBagConstraints fieldConstraints = new GridBagConstraints();
        fieldConstraints.gridx = 1;
        fieldConstraints.gridy = row;
        fieldConstraints.weightx = 1;
        fieldConstraints.fill = GridBagConstraints.HORIZONTAL;
        fieldConstraints.insets = new Insets(3, 0, 3, 0);
        layout.setConstraints(form.getComponent(firstComponent + 1), fieldConstraints);
    }

    private void updatePriceAndTotal() {
        TourPlanDTO plan = (TourPlanDTO) tourPlanCombo.getSelectedItem();
        if (plan == null) {
            priceLabel.setText("-");
            totalLabel.setText("-");
            return;
        }

        BigDecimal price = tourBookingBUS.getPriceByIdTourPlan(plan.getIdTourPlan());
        priceLabel.setText(price == null ? "Chưa có giá" : price.toPlainString());
        try {
            int tickets = Integer.parseInt(ticketsField.getText().trim());
            if (price == null || tickets <= 0) {
                totalLabel.setText("-");
                return;
            }
            totalLabel.setText(price.multiply(BigDecimal.valueOf(tickets)).toPlainString());
        } catch (NumberFormatException exception) {
            totalLabel.setText("-");
        }
    }

    private void submitBooking() {
        TourPlanDTO plan = (TourPlanDTO) tourPlanCombo.getSelectedItem();
        if (plan == null) {
            showError("Hiện không có kế hoạch tour phù hợp để đặt.");
            return;
        }

        final int tickets;
        try {
            tickets = Integer.parseInt(ticketsField.getText().trim());
        } catch (NumberFormatException exception) {
            showError("Số vé phải là số nguyên dương.");
            return;
        }
        if (tickets <= 0) {
            showError("Số vé phải lớn hơn 0.");
            return;
        }

        try {
            if (!tourBookingBUS.createCustomerBooking(
                    plan.getIdTourPlan(), tickets, noteArea.getText().trim())) {
                showError("Không thể lưu yêu cầu đặt tour. Vui lòng thử lại.");
                return;
            }
            JOptionPane.showMessageDialog(
                    this,
                    "Đã gửi yêu cầu đặt tour. Yêu cầu đang chờ nhân viên xác nhận.",
                    "Đặt tour thành công",
                    JOptionPane.INFORMATION_MESSAGE
            );
            dispose();
        } catch (IllegalArgumentException | IllegalStateException | SecurityException exception) {
            showError(exception.getMessage());
        }
    }

    private void showError(String message) {
        JOptionPane.showMessageDialog(this, message, "Không thể đặt tour", JOptionPane.ERROR_MESSAGE);
    }
}
