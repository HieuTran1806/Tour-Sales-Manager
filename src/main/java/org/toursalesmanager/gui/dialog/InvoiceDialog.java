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
import java.awt.event.*;
import java.math.BigDecimal;
import java.sql.Timestamp;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Date;
import java.util.Objects;

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

    InvoiceDTO invoiceDTO;

    public InvoiceDialog(InvoiceBUS invoiceBUS, TourPlanBUS tourPlanBUS, CustomerBUS customerBUS, PromotionBUS promotionBUS, InvoiceDTO invoiceDTO) {
        this.invoiceBUS= invoiceBUS;
        this.tourPlanBUS= tourPlanBUS;
        this.promotionBUS = promotionBUS;
        this.customerBUS = customerBUS;

        initComponents();

        this.setTitle(invoiceBUS == null ? "Thêm hóa đơn" : "Sửa hóa đơn");

        // load invoice data
        if(invoiceDTO != null){
            loadData();
        }
    }

    public InvoiceDialog(InvoiceDTO hd) {
        initComponents();
        this.setTitle("Sửa hóa đơn");
        this.setLocationRelativeTo(null);

        loadData();
        if (hd.getIdPromotion() != null) {
            for (int i = 0; i < cbKM.getItemCount(); i++) {
                PromotionDTO km = cbKM.getItemAt(i);
                if (km.getIdPromotion().equals(hd.getIdPromotion())) {
                    cbKM.setSelectedItem(km);
                    break;
                }
            }
        }
    }

    @SuppressWarnings("unchecked")
    private void initComponents() {
        this.setLayout(new BorderLayout());
        setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);

        panelForm = new JPanel(new GridLayout(0, 2, 10, 10));
        panelBtn = new JPanel(new FlowLayout());

        txtmahd = new JTextField();
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
        // set txt DiaDiemKhoiHanh when init
        cbKM.addActionListener(e -> {
            PromotionDTO selected = (PromotionDTO) cbKM.getSelectedItem();
            if(selected != null){
                String makht = cbmakht.getSelectedItem().toString().trim();

                int sl = Integer.parseInt(txtsoluong.getText().trim());

                InvoiceDAO dao = new InvoiceDAO();
                float gia = dao.getPrice(makht);
                float tong = gia * sl;

                if (selected.getIdPromotion() != null) {
                    PromotionDTO km = promotionBUS.getFullCTrinhKM(selected.getIdPromotion());
                    if (km != null) {
                        float chietKhau = km.getDiscount();
                        tong = tong - tong * chietKhau / 100;
                    }
                }
                txttongtien.setText(String.format("%.0f", tong));
            }
        });
    }



    private void save() {
        saveBtn = ButtonFactory.create("Lưu", UIColors.SAVE);
        saveBtn.addActionListener(v -> {
            try {
                // get Data
                String ma = txtmahd.getText().trim();
                String makht = Objects.requireNonNull(cbmakht.getSelectedItem()).toString().trim();
                String makh = Objects.requireNonNull(cbmakh.getSelectedItem()).toString().trim();
                String manv = Objects.requireNonNull(cbmanv.getSelectedItem()).toString().trim();
                PromotionDTO km = (PromotionDTO) cbKM.getSelectedItem();
                String maKM = km != null ? km.getIdPromotion() : null;
                int sl = Integer.parseInt(txtsoluong.getText().trim());
                BigDecimal tongTien = BigDecimal.valueOf(Float.parseFloat(txttongtien.getText().trim()));

                InvoiceDTO kt = invoiceBUS.getInvoiceById(ma);
                if (kt != null) {
                    invoiceBUS.updateInvoice(new InvoiceDTO(
                            ma, makht, makh, manv,
                            kt.getDate(),
                            sl,maKM, tongTien
                    ));
                    if (invoiceBUS.updateInvoice(hd)) {
                        if (sl > invoiceDTO.getTickets()) {
                            int canThem = sl - this.soluong;
                            JOptionPane.showMessageDialog(this,
                                    "Số lượng tăng. Nhập thêm " + canThem + " vé.");

                            NhapCTHD nhap = new NhapCTHD(null, true, ma, canThem);
                            nhap.setVisible(true);

                        } else if (sl < this.soluong) {
                            int veDu = this.soluong - sl;

                            JOptionPane.showMessageDialog(this,
                                    "Số lượng giảm. Xóa " + veDu + " vé.");

                            org.toursalesmanager.gui.panel.InvoiceDetailPanel panelXoa = new InvoiceDetailPanel(ma, veDu);
                            JOptionPane.showConfirmDialog(null, panelXoa,
                                    "Xóa chi tiết",
                                    JOptionPane.DEFAULT_OPTION,
                                    JOptionPane.PLAIN_MESSAGE);
                        } else {
                            JOptionPane.showMessageDialog(this, "Cập nhật thành công!");
                        }
                        this.dispose();
                    } else {
                        JOptionPane.showMessageDialog(this, "Cập nhật thất bại!");
                    }
                }
                else {
                    Date ngaydl = jDate.getDate();
                    if (ngaydl == null) {
                        JOptionPane.showMessageDialog(this, "Vui lòng chọn ngày!");
                        return;
                    }
                    LocalDate ngay = DateHelper.toLocalDateFromUtil(ngaydl);

                    makht = cbmakht.getSelectedItem().toString().trim();
                    makh = cbmakh.getSelectedItem().toString().trim();
                    manv = cbmanv.getSelectedItem().toString().trim();

                    // int sl = Integer.parseInt(txtsoluong.getText().trim());

                    InvoiceDAO dao = new InvoiceDAO();
                    BigDecimal gia = BigDecimal.valueOf(dao.getPrice(makht));
                    BigDecimal tong = gia.multiply(BigDecimal.valueOf(soluong));

                    if (maKM != null && !maKM.isBlank()) {
                        km = promotionBUS.getFullCTrinhKM(maKM);

                        if (km != null) {
                            BigDecimal chietKhau = new BigDecimal(String.valueOf(km.getDiscount()));

                            BigDecimal tyLeGiam = chietKhau.movePointLeft(2);

                            BigDecimal tienGiam = tong.multiply(tyLeGiam);

                            tong = tong.subtract(tienGiam);
                        }
                    }

                    txttongtien.setText(String.format("%.0f", tong));

                    InvoiceDTO hdmoi = new InvoiceDTO(ma, makht, makh, manv, ngay, sl,maKM, tong);

                    if (invoiceBUS.addInvoice(hdmoi)) {
                        JOptionPane.showMessageDialog(this, "Thêm thành công!");

                        NhapCTHD nhap = new NhapCTHD(null, true, ma, sl);
                        nhap.setVisible(true);

                        this.dispose();
                    } else {
                        JOptionPane.showMessageDialog(this, "Thêm thất bại!");
                    }
                }
            } catch (Exception e) {
                e.printStackTrace();
                JOptionPane.showMessageDialog(this, "Lỗi dữ liệu!");
            }
        });
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
        try{
            String makht = cbmakht.getSelectedItem() != null ? cbmakht.getSelectedItem().toString().trim() : "";
            int sl=Integer.parseInt(txtsoluong.getText().trim());

            if(!makht.isEmpty()){
                InvoiceDAO dao =new InvoiceDAO();
                float gia=dao.getPrice(makht);

                float tong = gia * sl;
                // áp dụng khuyến mãi
                String maKm = cbKM.getSelectedItem() != null ? cbKM.getSelectedItem().toString().trim() : "";
                if(!maKm.isEmpty()){
                    PromotionDTO km = promotionBUS.getFullCTrinhKM(maKm);
                    if(km != null){
                        float chietKhau = km.getDiscount();
                        tong = tong - tong * chietKhau / 100;
                    }
                }
                txttongtien.setText(String.format("%.0f", gia*sl));
            }
        }catch (Exception e){
            txttongtien.setText("0");
        }
    }
}