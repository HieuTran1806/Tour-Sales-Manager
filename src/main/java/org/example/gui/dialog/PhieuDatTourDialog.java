package org.example.gui.dialog;

import lombok.AccessLevel;
import lombok.experimental.FieldDefaults;
import org.example.bus.PhieuDatTourBUS;
import org.example.dao.PhieuDatTourDAO;
import org.example.dao.KhachHangDAO;
import org.example.dao.KeHoachTourDAO;
import org.example.dao.TourDAO;
import org.example.dto.PhieuDatTourDTO;
import org.example.dto.KhachHangDTO;
import org.example.dto.KeHoachTourDTO;
import org.example.gui.panel.PhieuDatTourPanel;
import org.example.gui.panel.UIColors;

import javax.swing.*;
import java.awt.*;
import java.awt.event.FocusAdapter;
import java.awt.event.FocusEvent;

@FieldDefaults(level = AccessLevel.PRIVATE)
public class PhieuDatTourDialog extends JDialog {
    JButton btnDong, btnLuu;
    JLabel txtHo, txtTen, jlbMaKH, jlbMaKeHoachTour, jlbGia;

    JTextField txtGiaVe, txtHoKH, txtMaKH, txtMaKHTour, txtTenKH;

    public enum Mode {
        ADD, EDIT
    }

    Mode mode;
    PhieuDatTourDTO currentKHangKHTour;
    KhachHangDAO dsKhachHang;
    KeHoachTourDAO dsKeHoachTour;

    public PhieuDatTourDialog(Frame parent, boolean modal,
                              PhieuDatTourDAO ds, Mode mode, PhieuDatTourDTO khangkhtour) {

        super(parent, modal);
        this.mode = mode;
        this.currentKHangKHTour = khangkhtour;

        dsKhachHang = new KhachHangDAO();
        dsKeHoachTour = new KeHoachTourDAO();

        initComponents();
        this.setLocationRelativeTo(null);

        if (mode == Mode.EDIT && khangkhtour != null) {
            setDataToFields();
            setTitle("Sửa thông tin khách hàng - kế hoạch tour");
        }else {
            setTitle("Thêm khách hàng - kế hoạch tour");
        }
    }

    private void initComponents() {
        setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);

        btnLuu = new JButton("Lưu");
        btnDong = new JButton("Đóng");

        jlbMaKH = new JLabel("Mã khách hàng:");
        txtMaKH = new JTextField();

        txtHo = new JLabel("Họ:");
        txtHoKH = new JTextField();
        txtHoKH.setEnabled(false);

        txtTen = new JLabel("Tên:");
        txtTenKH = new JTextField();
        txtTenKH.setEnabled(false);

        jlbMaKeHoachTour = new JLabel("Mã kế hoạch - Tour:");
        txtMaKHTour = new JTextField();

        jlbGia = new JLabel("Giá vé:");
        txtGiaVe = new JTextField();
        txtGiaVe.setEnabled(false);

        // Events & Verifiers
        save();
        cancel();

        txtMaKH.addFocusListener(new FocusAdapter() {
            public void focusLost(FocusEvent evt) { txtMaKHFocusLost(evt); }
        });
        txtMaKH.addActionListener(this::txtMaKHActionPerformed);
        txtMaKH.setInputVerifier(new InputVerifier() {
            @Override
            public boolean verify(JComponent input) {
                String text = txtMaKH.getText().trim();
                if (text.isEmpty()) {
                    JOptionPane.showMessageDialog(PhieuDatTourDialog.this, "Mã khách hàng không được để trống.", "Lỗi", JOptionPane.ERROR_MESSAGE);
                    return false;
                }
                for (KhachHangDTO kh : dsKhachHang.layDanhSachKHang()) {
                    if (kh.getMaKH().equals(text)) return true;
                }
                JOptionPane.showMessageDialog(PhieuDatTourDialog.this, "Mã khách hàng không tồn tại.", "Lỗi", JOptionPane.ERROR_MESSAGE);
                return false;
            }
        });

        txtMaKHTour.addFocusListener(new FocusAdapter() {
            public void focusLost(FocusEvent evt) { txtMaKHTourFocusLost(evt); }
        });
        txtMaKHTour.addActionListener(this::txtMaKHTourActionPerformed);
        txtMaKHTour.setInputVerifier(new InputVerifier() {
            @Override
            public boolean verify(JComponent input) {
                String text = txtMaKHTour.getText().trim();
                if (text.isEmpty()) {
                    JOptionPane.showMessageDialog(PhieuDatTourDialog.this, "Mã kế hoạch - Tour không được để trống.", "Lỗi", JOptionPane.ERROR_MESSAGE);
                    return false;
                }
                for (KeHoachTourDTO kt : dsKeHoachTour.getAllKeHoachTours()) {
                    if (kt.getMaKHTour().equals(text)) return true;
                }
                JOptionPane.showMessageDialog(PhieuDatTourDialog.this, "Mã kế hoạch - Tour không tồn tại.", "Lỗi", JOptionPane.ERROR_MESSAGE);
                return false;
            }
        });

        txtGiaVe.addFocusListener(new FocusAdapter() {
            public void focusLost(FocusEvent evt) { txtGiaVeFocusLost(evt); }
        });
        txtGiaVe.addActionListener(this::txtGiaVeActionPerformed);
        txtHoKH.addFocusListener(new FocusAdapter() {
            public void focusLost(FocusEvent evt) { txtHoKHFocusLost(evt); }
        });

        // main layout
        setLayout(new BorderLayout(10, 10));

        // 4 rows 2 cols
        JPanel formPanel = new JPanel(new GridLayout(4, 2, 10, 15));
        formPanel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20)); // Căn lề padding 4 góc

        formPanel.add(jlbMaKH);
        formPanel.add(txtMaKH);

        JPanel hoPanel = new JPanel(new BorderLayout(5, 0));
        hoPanel.add(txtHo, BorderLayout.WEST);
        hoPanel.add(txtHoKH, BorderLayout.CENTER);

        JPanel tenPanel = new JPanel(new BorderLayout(5, 0));
        tenPanel.add(txtTen, BorderLayout.WEST);
        tenPanel.add(txtTenKH, BorderLayout.CENTER);

        JPanel namePanel = new JPanel(new GridLayout(1, 2, 10, 0));
        namePanel.add(hoPanel);
        namePanel.add(tenPanel);

        formPanel.add(new JLabel("Họ và Tên khách:"));
        formPanel.add(namePanel);

        formPanel.add(jlbMaKeHoachTour);
        formPanel.add(txtMaKHTour);

        formPanel.add(jlbGia);
        formPanel.add(txtGiaVe);

        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 30, 10));
        buttonPanel.add(btnLuu);
        buttonPanel.add(btnDong);

        add(formPanel, BorderLayout.CENTER);
        add(buttonPanel, BorderLayout.SOUTH);

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
        btnLuu = createBtn("Lưu",  UIColors.SAVE);
        btnLuu.addActionListener(v -> {
            String maKH = txtMaKH.getText().trim();
            String ho = txtHoKH.getText().trim();
            String ten = txtTenKH.getText().trim();
            String maKHTour = txtMaKHTour.getText().trim();

            if(txtGiaVe.getText().trim().isEmpty()){
                PhieuDatTourDAO dao = new PhieuDatTourDAO();
                long gia = dao.layDonGiaTheoMaKHTour(maKHTour);
                txtGiaVe.setText(String.valueOf(gia));
            }
            long giaVe = Long.parseLong(txtGiaVe.getText().trim());
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

    private void cancel(){
        btnDong = createBtn("Đóng", UIColors.CANCEL);
        btnDong.addActionListener(v -> {
            this.dispose();
        });
    }

    private void txtMaKHActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txtMaKHActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_txtMaKHActionPerformed

    private void txtMaKHTourActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txtMaKHTourActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_txtMaKHTourActionPerformed

    private void txtGiaVeActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txtGiaVeActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_txtGiaVeActionPerformed

    private void txtMaKHFocusLost(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_txtMaKHFocusLost
        // TODO add your handling code here:
        String ma = txtMaKH.getText().trim();
        for(KhachHangDTO kh : dsKhachHang.layDanhSachKHang()) {
            if(kh.getMaKH().equals(ma)) {
                txtHoKH.setText(kh.getHo());
                txtTenKH.setText(kh.getTen());
                return;
            }
        }
        JOptionPane.showMessageDialog(PhieuDatTourDialog.this, "Mã khách hàng không tồn tại.", "Lỗi", JOptionPane.ERROR_MESSAGE);
    }//GEN-LAST:event_txtMaKHFocusLost

    private void txtMaKHTourFocusLost(FocusEvent evt) {
        String maKHTour = txtMaKHTour.getText().trim();

        if(!maKHTour.isEmpty()){
            PhieuDatTourDAO dao = new PhieuDatTourDAO();
            long gia = dao.layDonGiaTheoMaKHTour(maKHTour);

            txtGiaVe.setText(String.valueOf(gia));
        }
    }

    private void txtHoKHFocusLost(FocusEvent evt) {
        // TODO add your handling code here:
    }

    private void txtTenKHFocusLost(FocusEvent evt) {
        // TODO add your handling code here:
    }

    private void txtGiaVeFocusLost(FocusEvent evt) {
        // TODO add your handling code here:
    }

    private void setDataToFields() {
        if (currentKHangKHTour != null) {
            txtMaKH.setText(currentKHangKHTour.getMaKHang());
            txtMaKHTour.setText(currentKHangKHTour.getMaKHTour());
            txtGiaVe.setText(String.valueOf(currentKHangKHTour.getGiaVe()));
        }
    }
}