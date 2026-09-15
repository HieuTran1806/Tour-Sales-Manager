package org.example.gui.dialog;

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

public class PhieuDatTourDialog extends JDialog {

    private PhieuDatTourDAO dskhangkhtour = new PhieuDatTourDAO();
    private PhieuDatTourPanel parentPanel;
    public enum Mode {
        ADD, EDIT
    }
    private Mode mode;
    private PhieuDatTourDTO currentKHangKHTour;
    private PhieuDatTourDAO ds;
    private KhachHangDAO dsKhachHang;
    private KeHoachTourDAO dsKeHoachTour;
    private TourDAO dsTour;

    public PhieuDatTourDialog(Frame parent, boolean modal,
                              PhieuDatTourDAO ds, Mode mode, PhieuDatTourDTO khangkhtour) {

        super(parent, modal);
        this.ds = ds;
        this.mode = mode;
        this.currentKHangKHTour = khangkhtour;

        dsKhachHang = new KhachHangDAO();
        dsKeHoachTour = new KeHoachTourDAO();
        dsTour = new TourDAO();

        initComponents();
        this.setLocationRelativeTo(null);

        if (mode == Mode.EDIT && khangkhtour != null) {
            setDataToFields();
            setTitle("Sửa thông tin khách hàng - kế hoạch tour");
        }else {
            setTitle("Thêm khách hàng - kế hoạch tour");
        }
    }

    @SuppressWarnings("unchecked")
    private void initComponents() {

        btnLuu = new JButton();
        btnDong = new JButton();
        jPanel33 = new JPanel();
        jlbMaKH = new JLabel();
        txtMaKH = new JTextField();
        jPanel34 = new JPanel();
        jlbMaKeHoachTour = new JLabel();
        txtMaKHTour = new JTextField();
        jPanel35 = new JPanel();
        jlbGia = new JLabel();
        txtGiaVe = new JTextField();
        txtHo = new JLabel();
        txtHoKH = new JTextField();
        txtTen = new JLabel();
        txtTenKH = new JTextField();

        setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);

        save();
        cancel();

        jlbMaKH.setText("Mã khách hàng:");

        txtMaKH.addFocusListener(new FocusAdapter() {
            public void focusLost(FocusEvent evt) {
                txtMaKHFocusLost(evt);
            }
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
                    if (kh.getMaKH().equals(text)) {
                        return true;
                    }
                }
                JOptionPane.showMessageDialog(PhieuDatTourDialog.this, "Mã khách hàng không tồn tại.", "Lỗi", JOptionPane.ERROR_MESSAGE);
                return false;
            }
        });

        javax.swing.GroupLayout jPanel33Layout = new javax.swing.GroupLayout(jPanel33);
        jPanel33.setLayout(jPanel33Layout);
        jPanel33Layout.setHorizontalGroup(
                jPanel33Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addGroup(jPanel33Layout.createSequentialGroup()
                                .addGap(59, 59, 59)
                                .addComponent(jlbMaKH)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(txtMaKH, javax.swing.GroupLayout.PREFERRED_SIZE, 203, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addContainerGap(49, Short.MAX_VALUE))
        );
        jPanel33Layout.setVerticalGroup(
                jPanel33Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addGroup(jPanel33Layout.createSequentialGroup()
                                .addGap(12, 12, 12)
                                .addGroup(jPanel33Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                        .addComponent(jlbMaKH)
                                        .addComponent(txtMaKH, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                                .addContainerGap(15, Short.MAX_VALUE))
        );

        jlbMaKeHoachTour.setText("Mã kế hoạch - Tour:");

        txtMaKHTour.addFocusListener(new FocusAdapter() {
            public void focusLost(FocusEvent evt) {
                txtMaKHTourFocusLost(evt);
            }
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
                    if (kt.getMaKHTour().equals(text)) {
                        return true;
                    }
                }
                JOptionPane.showMessageDialog(PhieuDatTourDialog.this, "Mã kế hoạch - Tour không tồn tại.", "Lỗi", JOptionPane.ERROR_MESSAGE);
                return false;
            }
        });

        javax.swing.GroupLayout jPanel34Layout = new javax.swing.GroupLayout(jPanel34);
        jPanel34.setLayout(jPanel34Layout);
        jPanel34Layout.setHorizontalGroup(
                jPanel34Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addGroup(jPanel34Layout.createSequentialGroup()
                                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                .addComponent(jlbMaKeHoachTour)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(txtMaKHTour, javax.swing.GroupLayout.PREFERRED_SIZE, 203, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGap(41, 41, 41))
        );
        jPanel34Layout.setVerticalGroup(
                jPanel34Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addGroup(jPanel34Layout.createSequentialGroup()
                                .addGap(12, 12, 12)
                                .addGroup(jPanel34Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                        .addComponent(jlbMaKeHoachTour)
                                        .addComponent(txtMaKHTour, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                                .addContainerGap(15, Short.MAX_VALUE))
        );

        jlbGia.setText("Giá vé:");

        txtGiaVe.setEnabled(false);
        txtGiaVe.addFocusListener(new FocusAdapter() {
            public void focusLost(FocusEvent evt) {
                txtGiaVeFocusLost(evt);
            }
        });
        txtGiaVe.addActionListener(this::txtGiaVeActionPerformed);

        javax.swing.GroupLayout jPanel35Layout = new javax.swing.GroupLayout(jPanel35);
        jPanel35.setLayout(jPanel35Layout);
        jPanel35Layout.setHorizontalGroup(
                jPanel35Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addGroup(jPanel35Layout.createSequentialGroup()
                                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                .addComponent(jlbGia)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(txtGiaVe, javax.swing.GroupLayout.PREFERRED_SIZE, 201, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGap(41, 41, 41))
        );
        jPanel35Layout.setVerticalGroup(
                jPanel35Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addGroup(jPanel35Layout.createSequentialGroup()
                                .addGap(12, 12, 12)
                                .addGroup(jPanel35Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                        .addComponent(jlbGia)
                                        .addComponent(txtGiaVe, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                                .addContainerGap(15, Short.MAX_VALUE))
        );

        txtHo.setText("Họ:");

        txtHoKH.setEnabled(false);
        txtHoKH.addFocusListener(new FocusAdapter() {
            public void focusLost(FocusEvent evt) {
                txtHoKHFocusLost(evt);
            }
        });

        txtTen.setText("Tên:");

        txtTenKH.setEnabled(false);

        javax.swing.GroupLayout layout = new GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
                layout.createParallelGroup(GroupLayout.Alignment.LEADING)
                        .addComponent(jPanel33, GroupLayout.DEFAULT_SIZE, GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addGroup(layout.createSequentialGroup()
                                .addContainerGap()
                                .addComponent(jPanel34, GroupLayout.DEFAULT_SIZE, GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                .addContainerGap())
                        .addGroup(layout.createSequentialGroup()
                                .addGap(58, 58, 58)
                                .addComponent(btnLuu)
                                .addPreferredGap(LayoutStyle.ComponentPlacement.RELATED, GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                .addComponent(btnDong)
                                .addGap(61, 61, 61))
                        .addGroup(layout.createSequentialGroup()
                                .addContainerGap()
                                .addComponent(jPanel35, GroupLayout.DEFAULT_SIZE, GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                .addContainerGap())
                        .addGroup(layout.createSequentialGroup()
                                .addGap(43, 43, 43)
                                .addComponent(txtHo)
                                .addPreferredGap(LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(txtHoKH, GroupLayout.PREFERRED_SIZE, 134, GroupLayout.PREFERRED_SIZE)
                                .addGap(18, 18, 18)
                                .addComponent(txtTen)
                                .addPreferredGap(LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(txtTenKH, GroupLayout.PREFERRED_SIZE, 103, GroupLayout.PREFERRED_SIZE)
                                .addContainerGap(GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        layout.setVerticalGroup(
                layout.createParallelGroup(GroupLayout.Alignment.LEADING)
                        .addGroup(layout.createSequentialGroup()
                                .addComponent(jPanel33, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)
                                .addGap(18, 18, 18)
                                .addGroup(layout.createParallelGroup(GroupLayout.Alignment.BASELINE)
                                        .addComponent(txtHo)
                                        .addComponent(txtHoKH, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)
                                        .addComponent(txtTen)
                                        .addComponent(txtTenKH, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE))
                                .addPreferredGap(LayoutStyle.ComponentPlacement.RELATED, 13, Short.MAX_VALUE)
                                .addComponent(jPanel34, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(jPanel35, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)
                                .addGap(14, 14, 14)
                                .addGroup(layout.createParallelGroup(GroupLayout.Alignment.BASELINE)
                                        .addComponent(btnLuu)
                                        .addComponent(btnDong))
                                .addGap(33, 33, 33))
        );
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

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private JButton btnDong;
    private JButton btnLuu;
    private JLabel txtHo;
    private JLabel txtTen;
    private JLabel jlbMaKH;
    private JLabel jlbMaKeHoachTour;
    private JLabel jlbGia;
    private JPanel jPanel33;
    private JPanel jPanel34;
    private JPanel jPanel35;
    private JTextField txtGiaVe;
    private JTextField txtHoKH;
    private JTextField txtMaKH;
    private JTextField txtMaKHTour;
    private JTextField txtTenKH;
}