package org.toursalesmanager.gui.dialog;


import com.toedter.calendar.JDateChooser;
import lombok.AccessLevel;
import lombok.experimental.FieldDefaults;
import org.toursalesmanager.bus.*;
import org.toursalesmanager.dao.InvoiceDAO;
import org.toursalesmanager.dto.*;
import org.toursalesmanager.gui.component.ButtonFactory;
import org.toursalesmanager.gui.helper.AddComponentToForm;
import org.toursalesmanager.gui.helper.DateHelper;
import org.toursalesmanager.gui.panel.InvoiceDetailPanel;
import org.toursalesmanager.gui.helper.UIColors;

import javax.swing.*;
import java.awt.*;
import java.awt.event.FocusAdapter;
import java.awt.event.FocusEvent;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Objects;
import java.util.Date;

@FieldDefaults(level = AccessLevel.PRIVATE)
public class InvoiceDialog extends JDialog {
    JTextField txtmahd, txtsoluong, txttongtien;

    JPanel panelForm, panelBtn;

    JButton saveBtn, refreshBtn;

    JComboBox<String> cbmakh, cbmakht, cbmanv;

    JComboBox<PromotionDTO> cbKM;

    JDateChooser jDate;

    CustomerBUS customerBUS;
    InvoiceBUS invoiceBUS;
    TourPlanBUS tourPlanBUS;
    PromotionBUS promotionBUS;
    StaffBUS staffBUS;
    InvoiceDAO invoiceDAO;

    InvoiceDTO invoiceDTO;

    public InvoiceDialog(InvoiceBUS invoiceBUS, TourPlanBUS tourPlanBUS, CustomerBUS customerBUS,
                         PromotionBUS promotionBUS, StaffBUS staffBUS, InvoiceDTO invoiceDTO) {
        this.invoiceBUS= invoiceBUS;
        this.tourPlanBUS= tourPlanBUS;
        this.promotionBUS = promotionBUS;
        this.customerBUS = customerBUS;
        this.staffBUS = staffBUS;
        this.invoiceDAO = new InvoiceDAO();
        this.invoiceDTO = invoiceDTO;

        initComponents();

        this.setTitle(invoiceDTO == null ? "Thêm hóa đơn" : "Sửa hóa đơn");
        loadData();
    }

    @SuppressWarnings("unchecked")
    private void initComponents() {
        this.setLayout(new BorderLayout());
        setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);

        panelForm = new JPanel(new GridLayout(0, 2, 10, 10));
        panelBtn = new JPanel(new FlowLayout());

        txtmahd = new JTextField();
        txtmahd.setEditable(false);
        if (invoiceDTO == null) {
            txtmahd.setText(invoiceBUS.getNextInvoiceId());
        }
        AddComponentToForm.addFormRow(panelForm,"Mã hóa đơn", txtmahd );

        cbmakht = new JComboBox<>();
        cbmakht.setEditable(true);
        AddComponentToForm.addFormRow(panelForm,"Mã kế hoạch tour", cbmakht );

        jDate = new JDateChooser();
        jDate.setDateFormatString("dd/MM/yyyy");
        AddComponentToForm.addFormRow(panelForm,"Ngày", jDate);

        txtsoluong = new JTextField();
        txtsoluong.addFocusListener(new FocusAdapter() {
            public void focusLost(FocusEvent evt) {
                txtsoluongFocusLost(evt);
            }
        });
        AddComponentToForm.addFormRow(panelForm,"Số lượng", txtsoluong );

        cbKM = new JComboBox<>();
        AddComponentToForm.addFormRow(panelForm,"Khuyến mãi", cbKM );

        txttongtien = new JTextField();
        txttongtien.setEditable(false);
        AddComponentToForm.addFormRow(panelForm,"Tổng tiền", txttongtien);

        cbmakh = new JComboBox<>();
        AddComponentToForm.addFormRow(panelForm,"Mã khách hàng đặt", cbmakh );

        cbmanv = new JComboBox<>();
        AddComponentToForm.addFormRow(panelForm,"Mã nhân viên", cbmanv );

        // define handle function
        save();
        refresh();
        panelBtn.add(saveBtn);
        panelBtn.add(refreshBtn);

        this.add(panelForm, BorderLayout.CENTER);
        this.add(panelBtn, BorderLayout.SOUTH);

        this.setLocationRelativeTo(null);
        pack();
        if (invoiceDTO == null) {
            SwingUtilities.invokeLater(() ->
                    cbmakht.getEditor().getEditorComponent().requestFocusInWindow()
            );
        }
    }

    public void setupAutoComplete(JComboBox<String> cbx, java.util.List<String> data) {
        cbx.setEditable(true);
        cbx.setModel(new DefaultComboBoxModel<>(data.toArray(new String[0])));
        JTextField txt = (JTextField) cbx.getEditor().getEditorComponent();

        txt.addKeyListener(new KeyAdapter() {
            @Override
            public void keyReleased(KeyEvent e) {
                int k = e.getKeyCode();
                if (k == 38 || k == 40 || k == 10) return;

                SwingUtilities.invokeLater(() -> {
                    String input = txt.getText();

                    String[] filtered = data.stream()
                            .filter(s -> s.toLowerCase().contains(input.toLowerCase()))
                            .toArray(String[]::new);

                    cbx.setModel(new DefaultComboBoxModel<>(filtered));
                    txt.setText(input);
                    cbx.setPopupVisible(filtered.length > 0);
                });
            }
        });
    }


    public void loadData(){
        ArrayList<TourPlanDTO> tourPlanList = tourPlanBUS.getAllKeHoachTours();
        ArrayList<CustomerDTO> customerList = customerBUS.getAllCustomers();
        ArrayList<StaffDTO> staffList = staffBUS.getAllStaffs();
        // Danh sách mã kế hoạch tour
        ArrayList<String> tourPlanIds =
                new ArrayList<>();

        for (TourPlanDTO tourPlan : tourPlanList) {
            tourPlanIds.add(
                    tourPlan.getIdTourPlan()
            );
        }

        setupAutoComplete(
                cbmakht,
                tourPlanIds
        );

        // Danh sách mã khách hàng
        ArrayList<String> customerIds =
                new ArrayList<>();

        for (CustomerDTO customer : customerList) {
            customerIds.add(
                    customer.getIdCustomer()
            );
        }

        setupAutoComplete(
                cbmakh,
                customerIds
        );

        // Danh sách mã nhân viên
        ArrayList<String> staffIds = new ArrayList<>();

        for (StaffDTO staff : staffList) {
            staffIds.add(
                    staff.getIdStaff()
            );
        }

        setupAutoComplete(
                cbmanv,
                staffIds
        );

        // cbKM
        ArrayList<PromotionDTO> lsKM = promotionBUS.getAllPromotions();

        DefaultComboBoxModel<PromotionDTO> promotionModel = new DefaultComboBoxModel<>();
        for(PromotionDTO km : lsKM){
            promotionModel.addElement(km);
        }
        cbKM.setModel(promotionModel);
        cbKM.setSelectedItem(null);

        if (invoiceDTO != null) {
            txtmahd.setText(invoiceDTO.getIdInvoice());
            txtmahd.setEditable(false);
            cbmakht.setSelectedItem(invoiceDTO.getIdTourPlan());
            cbmakh.setSelectedItem(invoiceDTO.getIdCustomer());
            cbmanv.setSelectedItem(invoiceDTO.getIdStaff());
            jDate.setDate(DateHelper.toUtilDate(invoiceDTO.getDate()));
            txtsoluong.setText(String.valueOf(invoiceDTO.getTickets()));
            txttongtien.setText(invoiceDTO.getCostTotal() == null ? "" : invoiceDTO.getCostTotal().toPlainString());
            for (int i = 0; i < cbKM.getItemCount(); i++) {
                PromotionDTO promotion = cbKM.getItemAt(i);
                if (Objects.equals(promotion.getIdPromotion(), invoiceDTO.getIdPromotion())) {
                    cbKM.setSelectedIndex(i);
                    break;
                }
            }
        }

        cbmakht.addActionListener(e -> updateTotal());
        cbKM.addActionListener(e -> updateTotal());
    }



    private void save() {
        saveBtn = ButtonFactory.create("Lưu", UIColors.SAVE);
        saveBtn.addActionListener(v -> {
            try {
                String ma = txtmahd.getText().trim();
                String makht = selectedCode(cbmakht, "Mã kế hoạch tour");
                String makh = selectedCode(cbmakh, "Mã khách hàng");
                String manv = selectedCode(cbmanv, "Mã nhân viên");
                Date selectedDate = jDate.getDate();
                int tickets = Integer.parseInt(txtsoluong.getText().trim());

                LocalDate date = DateHelper.toLocalDateFromUtil(selectedDate);
                PromotionDTO promotion = (PromotionDTO) cbKM.getSelectedItem();
                String promotionId = promotion == null ? null : promotion.getIdPromotion();
                BigDecimal total = calculateTotal(makht, tickets, promotionId);

                InvoiceDTO updatedInvoice = new InvoiceDTO(
                        ma, makht, makh, manv, date, tickets, promotionId, total
                );

                if(invoiceBUS.addInvoice(updatedInvoice)){
                    JOptionPane.showMessageDialog(this, "Thêm hóa đơn thành công.");
                    new NhapCTHD(null, true, ma, tickets).setVisible(true);
                } else {
                    JOptionPane.showMessageDialog(this, "Thêm hóa đơn thất bại.");
                }

                dispose();
            } catch (Exception e) {
                e.printStackTrace();
                JOptionPane.showMessageDialog(this,
                        e.getMessage() == null ? "Lỗi dữ liệu hóa đơn." : e.getMessage(),
                        "Lỗi", JOptionPane.ERROR_MESSAGE);
            }
        });
    }

    private String selectedCode(JComboBox<String> comboBox, String fieldName) {
        Object value = comboBox.getEditor().getItem();
        if (value == null || value.toString().trim().isEmpty()) {
            throw new IllegalArgumentException(fieldName + " không được để trống.");
        }
        return value.toString().trim();
    }

    private BigDecimal calculateTotal(String tourPlanId, int tickets, String promotionId) {
        BigDecimal total = BigDecimal.valueOf(invoiceDAO.getPrice(tourPlanId))
                .multiply(BigDecimal.valueOf(tickets));
        if (promotionId != null && !promotionId.isBlank()) {
            PromotionDTO promotion = promotionBUS.getFullCTrinhKM(promotionId);
            if (promotion != null) {
                BigDecimal discountRate = BigDecimal.valueOf(promotion.getDiscount())
                        .movePointLeft(2);
                total = total.subtract(total.multiply(discountRate));
            }
        }
        return total;
    }

    private void updateTotal() {
        try {
            Object selectedTourPlan = cbmakht.getSelectedItem();
            if (selectedTourPlan == null || txtsoluong.getText().isBlank()) {
                return;
            }
            int tickets = Integer.parseInt(txtsoluong.getText().trim());
            if (tickets <= 0) {
                return;
            }
            PromotionDTO promotion = (PromotionDTO) cbKM.getSelectedItem();
            String promotionId = promotion == null ? null : promotion.getIdPromotion();
            txttongtien.setText(calculateTotal(selectedTourPlan.toString().trim(),
                    tickets, promotionId).toPlainString());
        } catch (NumberFormatException ignored) {
            txttongtien.setText("");
        }
    }

    public void resetField(){
        txtmahd.setText("");
        cbmakh.setSelectedItem("");
        cbmakht.setSelectedItem("");
        cbmanv.setSelectedItem("");
        cbKM.setSelectedItem(null);
        txtsoluong.setText("");
        txttongtien.setText("");
        jDate.setDate(new Date());
    }

    private void refresh(){
        refreshBtn = ButtonFactory.create("Làm mới", UIColors.REFRESH);
        refreshBtn.addActionListener(v -> {
            resetField();
        });
    }

    private void txtsoluongFocusLost(FocusEvent evt) {
        updateTotal();
    }
}