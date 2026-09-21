package org.example.gui.dialog;

import lombok.AccessLevel;
import lombok.experimental.FieldDefaults;
import org.example.bus.PhieuDatTourBUS;
import org.example.dao.PhieuDatTourDAO;
import org.example.dao.KhachHangDAO;
import org.example.dao.KeHoachTourDAO;
import org.example.dto.PhieuDatTourDTO;
import org.example.dto.KhachHangDTO;
import org.example.dto.KeHoachTourDTO;
import org.example.gui.panel.UIColors;
import org.example.validate.ValidationException;

import javax.swing.*;
import java.awt.*;
import java.awt.event.FocusAdapter;
import java.awt.event.FocusEvent;

@FieldDefaults(level = AccessLevel.PRIVATE)
public class PhieuDatTourDialog extends JDialog {
    JButton closeBtn, saveBtn;

    JLabel lbFirstName, lbLastName, lbIdCustomer, lbIdTourPlan, lbPrice;

    JTextField txtPrice, txtFirstName, txtIdCustomer, txtIdTourPlan, txtLastName;

    JPanel formPanel, southPanel;

    public enum Mode {
        ADD, EDIT
    }

    Mode mode;
    PhieuDatTourDTO currentKHangKHTour;
    PhieuDatTourBUS bus;
    KhachHangDAO dsKhachHang;

    public PhieuDatTourDialog(Frame parent, boolean modal,
                              PhieuDatTourDAO ds, Mode mode, PhieuDatTourDTO khangkhtour) {

        super(parent, modal);
        this.mode = mode;
        this.currentKHangKHTour = khangkhtour;
        bus = new PhieuDatTourBUS();

        dsKhachHang = new KhachHangDAO();

        initComponents();
        this.setLocationRelativeTo(null);

        setTitle(khangkhtour == null ? "Thêm phiếu đặt tour" : "Sửa phiếu đặt tour");
    }

    private void initComponents() {
        setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);
        setLayout(new BorderLayout(10, 10));

        // 4 rows 2 cols
        formPanel = new JPanel(new GridLayout(5, 2, 10, 15));
        formPanel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20)); // Căn lề padding 4 góc

        southPanel = new JPanel(new FlowLayout());

        // row id tour plan
        lbIdTourPlan = new JLabel("Mã phiếu đặt tour");
        lbIdTourPlan.setBorder(BorderFactory.createEmptyBorder(5, 5, 5, 0));
        formPanel.add(lbIdTourPlan);
        txtIdTourPlan = new JTextField();
        formPanel.add(txtIdTourPlan);

        // row firstName
        lbFirstName = new JLabel("Họ: ");
        lbFirstName.setBorder(BorderFactory.createEmptyBorder(5, 5, 5, 0));
        formPanel.add(lbFirstName);
        txtFirstName = new JTextField();
        formPanel.add(txtFirstName);

        // row lastName
        lbLastName = new JLabel("Tên: ");
        lbLastName.setBorder(BorderFactory.createEmptyBorder(5, 5, 5, 0));
        formPanel.add(lbLastName);
        txtLastName = new JTextField();
        formPanel.add(txtLastName);

        // row id customer
        lbIdCustomer = new JLabel("Mã khách hàng: ");
        lbIdCustomer.setBorder(BorderFactory.createEmptyBorder(5, 5, 5, 0));
        formPanel.add(lbIdCustomer);
        txtIdCustomer = new JTextField();
        formPanel.add(txtIdCustomer);

        // row price
        lbPrice = new JLabel("Giá vé: ");
        lbPrice.setBorder(BorderFactory.createEmptyBorder(5, 5, 5, 0));
        formPanel.add(lbPrice);
        txtPrice = new JTextField();
        txtPrice.setEnabled(false);
        formPanel.add(txtPrice);

        // Events & Verifiers
        save();
        cancel();

        southPanel.add(saveBtn);
        southPanel.add(closeBtn);
        southPanel.setBorder(BorderFactory.createEmptyBorder(0, 0, 10, 0));

        add(formPanel, BorderLayout.CENTER);
        add(southPanel, BorderLayout.SOUTH);

        pack();
        setLocationRelativeTo(null);
    }

    private JButton createBtn(String text, Color color){
        JButton btn = new JButton(text);
        btn.setBackground(color);
        btn.setForeground(Color.WHITE);
        btn.setFocusPainted(false);
        btn.setCursor(new Cursor(Cursor.HAND_CURSOR));// Trong jpBtn panel
        return btn;
    }

    private void save(){
        saveBtn = createBtn("Lưu",  UIColors.SAVE);
        saveBtn.addActionListener(v -> {
            // get data
            String maKH = txtIdCustomer.getText().trim();
            String ho = txtFirstName.getText().trim();
            String ten = txtLastName.getText().trim();
            String maKHTour = txtIdTourPlan.getText().trim();

            try {
                // validate
                validateIdTourPlan(maKHTour);
                validateIdCustomer(maKH);


            }catch (ValidationException validationException) {
                JOptionPane.showMessageDialog(
                        this,
                        validationException.getMessage(),
                        "Lỗi nhập liệu",
                        JOptionPane.ERROR_MESSAGE
                );
            }
            if(txtPrice.getText().trim().isEmpty()){
                PhieuDatTourDAO dao = new PhieuDatTourDAO();
                long gia = dao.layDonGiaTheoMaKHTour(maKHTour);
                txtPrice.setText(String.valueOf(gia));
            }
            long giaVe = Long.parseLong(txtPrice.getText().trim());
            PhieuDatTourBUS khangkhtBUS = new PhieuDatTourBUS();
            if(mode==Mode.ADD) {
                PhieuDatTourDTO newKHangKHTour = new PhieuDatTourDTO(maKHTour, maKH, giaVe);
                khangkhtBUS.them(newKHangKHTour);
            } else if(mode==Mode.EDIT&&currentKHangKHTour!=null) {
                currentKHangKHTour.setMaKHang(maKH);
                currentKHangKHTour.setMaKHTour(maKHTour);
                currentKHangKHTour.setGiaVe(giaVe);
                khangkhtBUS.suaKHang_KHTour(currentKHangKHTour);
            }
            dispose();
        });
    }

    private void validateIdTourPlan(String id) throws ValidationException {
        if (id.isEmpty()) throw new ValidationException("Mã phiếu đặt tour không được để trống!");

        // if (!id.matches("^KH\\d{3}$"))  throw new ValidationException("Mã phiếu đặt tour phải có dạng KHxxx!");

        if(isIdKHTDuplicated(id)) throw new ValidationException("Mã phiếu đặt tour đã tồn tại!");
    }

    private boolean isIdKHTDuplicated(String id){
        for (PhieuDatTourDTO ls : bus.getAllPhieuDatTour()){
            if (ls.getMaKHTour().equals(id))
                return true;
        }
        return false;
    }

    private void validateIdCustomer(String id) throws ValidationException{
        if (id.isEmpty()) throw new ValidationException("Mãk khách hàng không được để trống!");

        if(isExistedIdCustomer(id)) throw new ValidationException("Mã khách hàng đã tồn tại.");
    }

    private boolean isExistedIdCustomer(String id){
        for (KhachHangDTO kh : dsKhachHang.layDanhSachKHang()) {
            if (kh.getMaKH().equals(id))
                return true;
        }
        return false;
    }

    private void cancel(){
        closeBtn = createBtn("Đóng", UIColors.CANCEL);
        closeBtn.addActionListener(v -> {
            this.dispose();
        });
    }

    private void txtIdCustomerActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txtIdCustomerActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_txtIdCustomerActionPerformed

    private void txtIdTourPlanActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txtIdTourPlanActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_txtIdTourPlanActionPerformed

    private void txtPriceActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txtPriceActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_txtPriceActionPerformed

    private void txtIdCustomerFocusLost(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_txtIdCustomerFocusLost
        // TODO add your handling code here:
        String ma = txtIdCustomer.getText().trim();
        for(KhachHangDTO kh : dsKhachHang.layDanhSachKHang()) {
            if(kh.getMaKH().equals(ma)) {
                txtFirstName.setText(kh.getHo());
                txtLastName.setText(kh.getTen());
                return;
            }
        }
        JOptionPane.showMessageDialog(PhieuDatTourDialog.this, "Mã khách hàng không tồn tại.", "Lỗi", JOptionPane.ERROR_MESSAGE);
    }//GEN-LAST:event_txtIdCustomerFocusLost

    private void txtIdTourPlanFocusLost(FocusEvent evt) {
        String maKHTour = txtIdTourPlan.getText().trim();

        if(!maKHTour.isEmpty()){
            PhieuDatTourDAO dao = new PhieuDatTourDAO();
            long gia = dao.layDonGiaTheoMaKHTour(maKHTour);

            txtPrice.setText(String.valueOf(gia));
        }
    }

    private void txtFirstNameFocusLost(FocusEvent evt) {
        // TODO add your handling code here:
    }

    private void txtLastNameFocusLost(FocusEvent evt) {
        // TODO add your handling code here:
    }

    private void txtPriceFocusLost(FocusEvent evt) {
        // TODO add your handling code here:
    }

    private void loadData() {
        if (currentKHangKHTour != null) {
            txtIdCustomer.setText(currentKHangKHTour.getMaKHang());
            txtIdTourPlan.setText(currentKHangKHTour.getMaKHTour());
            txtPrice.setText(String.valueOf(currentKHangKHTour.getGiaVe()));
        }
    }
}