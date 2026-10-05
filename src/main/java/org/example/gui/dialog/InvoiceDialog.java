package org.example.gui.dialog;


import com.toedter.calendar.JDateChooser;
import lombok.AccessLevel;
import lombok.experimental.FieldDefaults;
import org.example.bus.*;
import org.example.dao.InvoiceDAO;
import org.example.dto.*;
import org.example.gui.helper.DateHelper;
import org.example.gui.panel.InvoiceDetailPanel;
import org.example.gui.helper.UIColors;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Date;

@FieldDefaults(level = AccessLevel.PRIVATE)
public class InvoiceDialog extends JDialog {
    JTextField txtmahd, txtsoluong, txttongtien;
    int soluong=0;

    JButton saveBtn, refreshBtn;

    JComboBox<String> cbmakh, cbmakht, cbmanv;

    JComboBox<PromotionDTO> cbKM;
    JLabel lbmahd, lbmakh, lbmakht, lbmanv, lbngay, lbsoluong, lbMaKM,lbtongtien;


    JDateChooser txtngay;

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

        if(invoiceDTO != null){
            loadCbox();
        }


    }

    public InvoiceDialog(InvoiceDTO hd) {
        initComponents();
        this.setTitle("Sửa hóa đơn");
        this.setLocationRelativeTo(null);
        this.soluong=hd.getTickets();

        loadCbox();
        if (hd.getIdPromotion() != null) {
            for (int i = 0; i < cbKM.getItemCount(); i++) {
                PromotionDTO km = cbKM.getItemAt(i);
                if (km.getIdPromotion().equals(hd.getIdPromotion())) {
                    cbKM.setSelectedItem(km);
                    break;
                }
            }
        }

        txtmahd.setInputVerifier(new InputVerifier(){
            @Override
            public boolean verify(JComponent input){
                String ma=txtmahd.getText().trim();

                if(!ma.matches("^HD\\d{3}$") && invoiceBUS.timHd(ma)==null){
                    JOptionPane.showMessageDialog(null, "Mã hóa đơn phải có dạng HDxx!");
                    return false;
                }
                return true;
            }

        });
        txtsoluong.setInputVerifier(new InputVerifier(){
            @Override
            public boolean verify(JComponent input){
                int sl=Integer.parseInt(txtsoluong.getText().trim());

                if(sl<=0){
                    JOptionPane.showMessageDialog(null, "Số lượng không được bé hơn 0");
                    return false;
                }
                return true;
            }
        });

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


    public void loadCbox(){
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

    @SuppressWarnings("unchecked")
    private void initComponents() {
        lbmakht = new JLabel();
        lbmanv = new JLabel();
        lbmakh = new JLabel();
        lbtongtien = new JLabel();
        txtmahd = new JTextField();
        txttongtien = new JTextField();
        lbmahd = new JLabel();
        saveBtn = new JButton();
        txtsoluong = new JTextField();
        lbsoluong = new JLabel();
        refreshBtn = new JButton();
        lbMaKM = new JLabel();
        lbngay = new JLabel();
        txtngay = new JDateChooser();

        cbmakht = new JComboBox<>();
        cbmakh = new JComboBox<>();
        cbmanv = new JComboBox<>();
        cbKM = new JComboBox<>();

        setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);

        lbmakht.setText("Mã kế hoạch tour");

        lbmanv.setText("Mã nhân viên");

        lbmakh.setText("Mã khách hàng đặt");

        lbMaKM.setText("Khuyến mãi");

        lbtongtien.setText("Tổng tiền");

        txtmahd.addInputMethodListener(new InputMethodListener() {
            public void caretPositionChanged(InputMethodEvent evt) {
            }
            public void inputMethodTextChanged(InputMethodEvent evt) {
                txtmahdInputMethodTextChanged(evt);
            }
        });
        txtmahd.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent evt) {
                txtmahdActionPerformed(evt);
            }
        });

        txttongtien.setEditable(false);
        txttongtien.addFocusListener(new FocusAdapter() {
            public void focusLost(FocusEvent evt) {
                txttongtienFocusLost(evt);
            }
        });
        txttongtien.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent evt) {
                txttongtienActionPerformed(evt);
            }
        });

        lbmahd.setText("Mã hóa đơn");

        txtsoluong.addFocusListener(new FocusAdapter() {
            public void focusLost(FocusEvent evt) {
                txtsoluongFocusLost(evt);
            }
        });

        lbsoluong.setText("Số lượng");

        // define handle function
        luu();
        lamMoi();

        lbngay.setText("Ngày");

        txtngay.setDateFormatString("dd/MM/yyyy");

        cbmakht.setEditable(true);
        cbmakht.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent evt) {
                cbmakhtActionPerformed(evt);
            }
        });

        GroupLayout layout = new GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
                layout.createParallelGroup(GroupLayout.Alignment.LEADING)
                        .addGroup(layout.createSequentialGroup()
                                .addGroup(layout.createParallelGroup(GroupLayout.Alignment.LEADING)
                                        .addGroup(layout.createSequentialGroup()
                                                .addGap(30, 30, 30)
                                                .addComponent(lbmahd, GroupLayout.PREFERRED_SIZE, 183, GroupLayout.PREFERRED_SIZE)
                                                .addGap(6, 6, 6)
                                                .addComponent(txtmahd, GroupLayout.PREFERRED_SIZE, 177, GroupLayout.PREFERRED_SIZE))
                                        .addGroup(layout.createSequentialGroup()
                                                .addGap(30, 30, 30)
                                                .addComponent(lbmakht, GroupLayout.PREFERRED_SIZE, 151, GroupLayout.PREFERRED_SIZE)
                                                .addGap(38, 38, 38)
                                                .addComponent(cbmakht, GroupLayout.PREFERRED_SIZE, 177, GroupLayout.PREFERRED_SIZE))
                                        .addGroup(layout.createSequentialGroup()
                                                .addGap(30, 30, 30)
                                                .addComponent(lbngay, GroupLayout.PREFERRED_SIZE, 49, GroupLayout.PREFERRED_SIZE)
                                                .addGap(140, 140, 140)
                                                .addComponent(txtngay, GroupLayout.PREFERRED_SIZE, 177, GroupLayout.PREFERRED_SIZE))
                                        .addGroup(layout.createSequentialGroup()
                                                .addGap(30, 30, 30)
                                                .addComponent(lbsoluong, GroupLayout.PREFERRED_SIZE, 70, GroupLayout.PREFERRED_SIZE)
                                                .addGap(119, 119, 119)
                                                .addComponent(txtsoluong, GroupLayout.PREFERRED_SIZE, 177, GroupLayout.PREFERRED_SIZE))
                                        .addGroup(layout.createSequentialGroup()
                                                .addGap(30, 30, 30)
                                                .addComponent(lbMaKM, GroupLayout.PREFERRED_SIZE, 136, GroupLayout.PREFERRED_SIZE)
                                                .addGap(53, 53, 53)
                                                .addComponent(cbKM, GroupLayout.PREFERRED_SIZE, 177, GroupLayout.PREFERRED_SIZE))
                                        .addGroup(layout.createSequentialGroup()
                                                .addGap(30, 30, 30)
                                                .addComponent(lbtongtien, GroupLayout.PREFERRED_SIZE, 136, GroupLayout.PREFERRED_SIZE)
                                                .addGap(53, 53, 53)
                                                .addComponent(txttongtien, GroupLayout.PREFERRED_SIZE, 177, GroupLayout.PREFERRED_SIZE))
                                        .addGroup(layout.createSequentialGroup()
                                                .addGap(69, 69, 69)
                                                .addComponent(saveBtn, GroupLayout.PREFERRED_SIZE, 75, GroupLayout.PREFERRED_SIZE)
                                                .addGap(112, 112, 112)
                                                .addComponent(refreshBtn, GroupLayout.PREFERRED_SIZE, 86, GroupLayout.PREFERRED_SIZE))
                                        .addGroup(layout.createSequentialGroup()
                                                .addGap(30, 30, 30)
                                                .addGroup(layout.createParallelGroup(GroupLayout.Alignment.LEADING, false)
                                                        .addGroup(layout.createSequentialGroup()
                                                                .addComponent(lbmakh, GroupLayout.DEFAULT_SIZE, 151, Short.MAX_VALUE)
                                                                .addGap(38, 38, 38))
                                                        .addGroup(layout.createSequentialGroup()
                                                                .addComponent(lbmanv, GroupLayout.DEFAULT_SIZE, GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                                                .addGap(53, 53, 53)))
                                                .addGroup(layout.createParallelGroup(GroupLayout.Alignment.LEADING, false)
                                                        .addComponent(cbmakh, 0, 177, Short.MAX_VALUE)
                                                        .addComponent(cbmanv, 0, GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))))
                                .addContainerGap(20, Short.MAX_VALUE))
        );
        layout.setVerticalGroup(
                layout.createParallelGroup(GroupLayout.Alignment.LEADING)
                        .addGroup(layout.createSequentialGroup()
                                .addGap(6, 6, 6)
                                .addGroup(layout.createParallelGroup(GroupLayout.Alignment.LEADING)
                                        .addGroup(layout.createSequentialGroup()
                                                .addGap(3, 3, 3)
                                                .addComponent(lbmahd))
                                        .addComponent(txtmahd, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE))
                                .addGap(18, 18, 18)
                                .addGroup(layout.createParallelGroup(GroupLayout.Alignment.LEADING)
                                        .addGroup(layout.createSequentialGroup()
                                                .addGap(3, 3, 3)
                                                .addComponent(lbmakht))
                                        .addComponent(cbmakht, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE))
                                .addGroup(layout.createParallelGroup(GroupLayout.Alignment.LEADING)
                                        .addGroup(layout.createSequentialGroup()
                                                .addGap(21, 21, 21)
                                                .addComponent(lbmakh))
                                        .addGroup(layout.createSequentialGroup()
                                                .addGap(18, 18, 18)
                                                .addComponent(cbmakh, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)))
                                .addGroup(layout.createParallelGroup(GroupLayout.Alignment.LEADING)
                                        .addGroup(layout.createSequentialGroup()
                                                .addGap(16, 16, 16)
                                                .addComponent(lbmanv))
                                        .addGroup(layout.createSequentialGroup()
                                                .addPreferredGap(LayoutStyle.ComponentPlacement.UNRELATED)
                                                .addComponent(cbmanv, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)))
                                .addGap(7, 7, 7)

                                .addGroup(layout.createParallelGroup(GroupLayout.Alignment.LEADING)
                                        .addGroup(layout.createSequentialGroup()
                                                .addGap(6, 6, 6)
                                                .addComponent(lbngay))
                                        .addComponent(txtngay, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE))
                                .addGap(6, 6, 6)

                                .addGroup(layout.createParallelGroup(GroupLayout.Alignment.LEADING)
                                        .addGroup(layout.createSequentialGroup()
                                                .addGap(3, 3, 3)
                                                .addComponent(lbsoluong))
                                        .addComponent(txtsoluong, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE))
                                .addGap(12, 12, 12)

                                .addGroup(layout.createParallelGroup(GroupLayout.Alignment.LEADING)
                                        .addGroup(layout.createSequentialGroup()
                                                .addGap(3, 3, 3)
                                                .addComponent(lbMaKM))
                                        .addComponent(cbKM, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE))
                                .addGap(12, 12, 12)

                                .addGroup(layout.createParallelGroup(GroupLayout.Alignment.LEADING)
                                        .addComponent(lbtongtien, GroupLayout.PREFERRED_SIZE, 22, GroupLayout.PREFERRED_SIZE)
                                        .addComponent(txttongtien, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE))
                                .addGap(12, 12, 12)
                                .addGroup(layout.createParallelGroup(GroupLayout.Alignment.LEADING)
                                        .addComponent(saveBtn)
                                        .addComponent(refreshBtn)))
        );

        this.setLocationRelativeTo(null);
        pack();
    }

    private JButton createBtn(String text, Color color){
        JButton btn = new JButton(text);
        btn.setBackground(color);
        btn.setForeground(Color.WHITE);
        btn.setFocusPainted(false);
        btn.setCursor(new Cursor(Cursor.HAND_CURSOR));// Trong jpBtn panel

        return btn;
    }

    private void luu() {
        saveBtn = createBtn("Lưu", UIColors.SAVE);
        saveBtn.addActionListener(v -> {
            try {
                String ma = txtmahd.getText().trim();

                if (ma.isEmpty()) {
                    JOptionPane.showMessageDialog(this, "Chưa nhập mã hóa đơn!");
                    return;
                }

                String makht = cbmakht.getSelectedItem().toString().trim();
                String makh = cbmakh.getSelectedItem().toString().trim();
                String manv = cbmanv.getSelectedItem().toString().trim();

                PromotionDTO km = (PromotionDTO) cbKM.getSelectedItem();
                String maKM = km != null ? km.getIdPromotion() : null;

                int newSl;
                try {
                    newSl = Integer.parseInt(txtsoluong.getText().trim());
                } catch (Exception e) {
                    JOptionPane.showMessageDialog(this, "Số lượng không hợp lệ!");
                    return;
                }

                BigDecimal tongTien;
                try {
                    tongTien = BigDecimal.valueOf(Float.parseFloat(txttongtien.getText().trim()));
                } catch (Exception e) {
                    tongTien = new BigDecimal(String.valueOf(BigDecimal.ZERO));
                }

                InvoiceDTO kt = invoiceBUS.timHd(ma);
                if (kt != null) {
                    InvoiceDTO hd = new InvoiceDTO(
                            ma, makht, makh, manv,
                            kt.getDate(),
                            newSl,maKM, tongTien
                    );
                    if (invoiceBUS.suaHoaDon(hd)) {
                        if (newSl > this.soluong) {
                            int canThem = newSl - this.soluong;
                            JOptionPane.showMessageDialog(this,
                                    "Số lượng tăng. Nhập thêm " + canThem + " vé.");

                            NhapCTHD nhap = new NhapCTHD(null, true, ma, canThem);
                            nhap.setVisible(true);

                        } else if (newSl < this.soluong) {
                            int veDu = this.soluong - newSl;

                            JOptionPane.showMessageDialog(this,
                                    "Số lượng giảm. Xóa " + veDu + " vé.");

                            org.example.gui.panel.InvoiceDetailPanel panelXoa = new InvoiceDetailPanel(ma, veDu);
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
                    Date ngaydl = txtngay.getDate();
                    if (ngaydl == null) {
                        JOptionPane.showMessageDialog(this, "Vui lòng chọn ngày!");
                        return;
                    }
                    LocalDate ngay = DateHelper.toLocalDateFromUtil(ngaydl);

                    makht = cbmakht.getSelectedItem().toString().trim();
                    makh = cbmakh.getSelectedItem().toString().trim();
                    manv = cbmanv.getSelectedItem().toString().trim();

                    int sl = Integer.parseInt(txtsoluong.getText().trim());

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

                    if (invoiceBUS.themHoaDon(hdmoi)) {
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
        txtngay.setDate(new Date());
    }

    private void lamMoi(){
        refreshBtn = createBtn("Làm mới", UIColors.REFRESH);
        refreshBtn.addActionListener(v -> {
            resetField();
        });
    }

    private void txtmahdActionPerformed(ActionEvent evt) {
        // TODO add your handling code here:
    }

    private void txttongtienFocusLost(FocusEvent evt) {
        // TODO add your handling code here:
    }

    private void txttongtienActionPerformed(ActionEvent evt) {
        // TODO add your handling code here:
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

    private void txtmahdInputMethodTextChanged(InputMethodEvent evt) {//GEN-FIRST:event_txtmahdInputMethodTextChanged
        // TODO add your handling code here:
    }

    private void cbmakhtActionPerformed(ActionEvent evt) {//GEN-FIRST:event_cbmakhtActionPerformed
        // TODO add your handling code here:
    }
}