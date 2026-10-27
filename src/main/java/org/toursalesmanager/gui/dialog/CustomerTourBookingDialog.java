package org.toursalesmanager.gui.dialog;

import org.toursalesmanager.bus.TourBookingBUS;
import org.toursalesmanager.bus.TourPlanBUS;
import org.toursalesmanager.dto.TourPlanDTO;
import org.toursalesmanager.enums.Permission;
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
        setLayout(new BorderLayout(10, 10));

        JPanel form = new JPanel(new GridLayout(0, 2, 10, 12));
        form.setBorder(BorderFactory.createEmptyBorder(20, 20, 10, 20));

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

        form.add(new JLabel("Kế hoạch tour:"));
        form.add(tourPlanCombo);
        form.add(new JLabel("Số vé:"));
        form.add(ticketsField);
        form.add(new JLabel("Đơn giá:"));
        form.add(priceLabel);
        form.add(new JLabel("Tổng tiền dự kiến:"));
        form.add(totalLabel);
        form.add(new JLabel("Ghi chú:"));
        form.add(new JScrollPane(noteArea));

        JButton submitButton = new JButton("Gửi yêu cầu đặt tour");
        submitButton.setBackground(UIColors.SAVE);
        submitButton.setForeground(Color.WHITE);
        JButton closeButton = new JButton("Đóng");
        closeButton.addActionListener(event -> dispose());
        submitButton.addActionListener(event -> submitBooking());

        JPanel actions = new JPanel(new FlowLayout(FlowLayout.CENTER, 12, 8));
        actions.add(submitButton);
        actions.add(closeButton);

        add(form, BorderLayout.CENTER);
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
