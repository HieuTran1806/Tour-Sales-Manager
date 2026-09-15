
package org.example.gui.dialog;


import com.toedter.calendar.JDateChooser;
import org.example.bus.KhachHangBUS;
import org.example.dao.KhachHangDAO;
import org.example.dto.KhachHangDTO;
import org.example.gui.panel.KhachHangPanel;
import org.example.gui.panel.UIColors;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.time.LocalDate;

public class KhachHangDialog extends JDialog {

    private static final java.util.logging.Logger logger = java.util.logging.Logger.getLogger(KhachHangDialog.class.getName());
    private KhachHangDAO dsKhachHang = new KhachHangDAO();
    private KhachHangPanel parentPanel;
    public enum Mode {
        ADD, EDIT
    }
    private Mode mode;
    private KhachHangDTO currentKhachHang;
    private KhachHangDAO ds;

    public KhachHangDialog(java.awt.Frame parent, boolean modal, KhachHangDAO ds, Mode mode, KhachHangDTO kh) {
        super(parent, modal);
        this.ds = ds;
        this.mode = mode;
        this.currentKhachHang = kh;

        initComponents();
        this.setLocationRelativeTo(null); // set location after init

        if (mode == Mode.EDIT && kh != null) {
            setKhachHangData(kh);
            txtMaKH.setEditable(false);
            txtHoKH.requestFocus();
            setTitle("Sửa khách hàng");
        } else {
            setTitle("Thêm khách hàng");
        }
    }


    @SuppressWarnings("unchecked")

    private void initComponents() {

        jPanel22 = new JPanel();
        jLabel22 = new JLabel();
        txtMaKH = new JTextField();
        jPanel23 = new JPanel();
        jLabel23 = new JLabel();
        txtTenKH = new JTextField();
        jPanel24 = new JPanel();
        jLabel24 = new JLabel();
        txtDiaChiKH = new JTextField();
        btnLuu = new JButton();
        btnHuy = new JButton();
        jPanel26 = new JPanel();
        jLabel26 = new JLabel();
        jDateChooser1 = new com.toedter.calendar.JDateChooser();
        jPanel27 = new JPanel();
        jLabel27 = new JLabel();
        txtHoKH = new JTextField();
        jPanel28 = new JPanel();
        jLabel28 = new JLabel();
        txtSoDienThoaiKH = new JTextField();

        setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);

        jLabel22.setText("Mã khách hàng:");

        txtMaKH.addActionListener(this::txtMaKHActionPerformed);

        txtMaKH.setInputVerifier(new InputVerifier() {
            @Override
            public boolean verify(JComponent input) {
                String ma = txtMaKH.getText().trim();

                if (!ma.matches("^KH\\d{3}$")) {
                    JOptionPane.showMessageDialog(null,
                            "Mã khách hàng phải có dạng KHxxx!");
                    return false; // Không cho rời field
                }
                if (ma.isEmpty()) {
                    JOptionPane.showMessageDialog(null,
                            "Mã khách hàng không được để trống!");
                    return false; // Không cho rời field
                }
                for (KhachHangDTO kh : ds.layDanhSachKHang()) {
                    if (kh.getMaKH().equals(ma)) {
                        JOptionPane.showMessageDialog(null,
                                "Mã khách hàng đã tồn tại!");
                        return false; // Không cho rời field
                    }
                }
                return true;
            }
        });

        GroupLayout jPanel22Layout = new GroupLayout(jPanel22);
        jPanel22.setLayout(jPanel22Layout);
        jPanel22Layout.setHorizontalGroup(
                jPanel22Layout.createParallelGroup(GroupLayout.Alignment.LEADING)
                        .addGroup(jPanel22Layout.createSequentialGroup()
                                .addGap(59, 59, 59)
                                .addComponent(jLabel22)
                                .addPreferredGap(LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(txtMaKH, GroupLayout.PREFERRED_SIZE, 203, GroupLayout.PREFERRED_SIZE)
                                .addContainerGap(GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        jPanel22Layout.setVerticalGroup(
                jPanel22Layout.createParallelGroup(GroupLayout.Alignment.LEADING)
                        .addGroup(jPanel22Layout.createSequentialGroup()
                                .addGap(12, 12, 12)
                                .addGroup(jPanel22Layout.createParallelGroup(GroupLayout.Alignment.BASELINE)
                                        .addComponent(jLabel22)
                                        .addComponent(txtMaKH, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE))
                                .addContainerGap(15, Short.MAX_VALUE))
        );

        jLabel23.setText("Tên:");

        txtTenKH.addActionListener(this::txtTenKHActionPerformed);

        txtTenKH.setInputVerifier(new InputVerifier() {
            @Override
            public boolean verify(JComponent input) {
                String ten = txtTenKH.getText().trim();
                if (!ten.matches("^[\\p{L}]+(\\s[\\p{L}]+)*$")) {
                    JOptionPane.showMessageDialog(null,
                            "Tên chỉ được chứa chữ cái và khoảng trắng!");
                    return false; // Không cho rời field
                }
                if (ten.isEmpty()) {
                    JOptionPane.showMessageDialog(null,
                            "Tên không được để trống!");
                    return false; // Không cho rời field
                }
                return true;
            }
        });

        GroupLayout jPanel23Layout = new GroupLayout(jPanel23);
        jPanel23.setLayout(jPanel23Layout);
        jPanel23Layout.setHorizontalGroup(
                jPanel23Layout.createParallelGroup(GroupLayout.Alignment.LEADING)
                        .addGroup(jPanel23Layout.createSequentialGroup()
                                .addContainerGap(GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                .addComponent(jLabel23)
                                .addPreferredGap(LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(txtTenKH, GroupLayout.PREFERRED_SIZE, 203, GroupLayout.PREFERRED_SIZE)
                                .addGap(46, 46, 46))
        );
        jPanel23Layout.setVerticalGroup(
                jPanel23Layout.createParallelGroup(GroupLayout.Alignment.LEADING)
                        .addGroup(jPanel23Layout.createSequentialGroup()
                                .addGap(12, 12, 12)
                                .addGroup(jPanel23Layout.createParallelGroup(GroupLayout.Alignment.BASELINE)
                                        .addComponent(jLabel23)
                                        .addComponent(txtTenKH, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE))
                                .addContainerGap(15, Short.MAX_VALUE))
        );

        jLabel24.setText("Địa chỉ:");

        txtDiaChiKH.addActionListener(this::txtDiaChiKHActionPerformed);

        txtDiaChiKH.setInputVerifier(new InputVerifier() {
            @Override
            public boolean verify(JComponent input) {
                String diaChi = txtDiaChiKH.getText().trim();
                if (!diaChi.matches("^[\\p{L}0-9\\s,.-]+$")) {
                    JOptionPane.showMessageDialog(null,
                            "Địa chỉ chỉ được chứa chữ cái, số, khoảng trắng và các ký tự ,.-!");
                    return false; // Không cho rời field
                }
                if (diaChi.isEmpty()) {
                    JOptionPane.showMessageDialog(null,
                            "Địa chỉ không được để trống!");
                    return false; // Không cho rời field
                }
                return true;
            }
        });

        GroupLayout jPanel24Layout = new GroupLayout(jPanel24);
        jPanel24.setLayout(jPanel24Layout);
        jPanel24Layout.setHorizontalGroup(
                jPanel24Layout.createParallelGroup(GroupLayout.Alignment.LEADING)
                        .addGroup(jPanel24Layout.createSequentialGroup()
                                .addGap(101, 101, 101)
                                .addComponent(jLabel24)
                                .addPreferredGap(LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(txtDiaChiKH, GroupLayout.PREFERRED_SIZE, 203, GroupLayout.PREFERRED_SIZE)
                                .addContainerGap(GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        jPanel24Layout.setVerticalGroup(
                jPanel24Layout.createParallelGroup(GroupLayout.Alignment.LEADING)
                        .addGroup(jPanel24Layout.createSequentialGroup()
                                .addGap(12, 12, 12)
                                .addGroup(jPanel24Layout.createParallelGroup(GroupLayout.Alignment.BASELINE)
                                        .addComponent(jLabel24)
                                        .addComponent(txtDiaChiKH, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE))
                                .addContainerGap(15, Short.MAX_VALUE))
        );

        // define handle function
        luu();
        huy();

        jLabel26.setText("Ngày sinh:");

        jDateChooser1.setDateFormatString("dd/MM/yyyy");

        jDateChooser1.setInputVerifier(new InputVerifier() {
            @Override
            public boolean verify(JComponent input) {
                if (jDateChooser1.getDate() == null) {
                    JOptionPane.showMessageDialog(null,
                            "Ngày sinh không được để trống!");
                    return false; // Không cho rời field
                }
                else if (jDateChooser1.getDate().after(new java.util.Date())) {
                    JOptionPane.showMessageDialog(null,
                            "Ngày sinh không được lớn hơn ngày hiện tại!");
                    return false; // Không cho rời field
                }
                else{
                    return true;
                }
            }
        });

        GroupLayout jPanel26Layout = new GroupLayout(jPanel26);
        jPanel26.setLayout(jPanel26Layout);
        jPanel26Layout.setHorizontalGroup(
                jPanel26Layout.createParallelGroup(GroupLayout.Alignment.LEADING)
                        .addGroup(jPanel26Layout.createSequentialGroup()
                                .addGap(86, 86, 86)
                                .addComponent(jLabel26)
                                .addPreferredGap(LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(jDateChooser1, GroupLayout.PREFERRED_SIZE, 201, GroupLayout.PREFERRED_SIZE)
                                .addContainerGap(GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        jPanel26Layout.setVerticalGroup(
                jPanel26Layout.createParallelGroup(GroupLayout.Alignment.LEADING)
                        .addGroup(jPanel26Layout.createSequentialGroup()
                                .addContainerGap()
                                .addGroup(jPanel26Layout.createParallelGroup(GroupLayout.Alignment.TRAILING)
                                        .addComponent(jDateChooser1, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)
                                        .addComponent(jLabel26))
                                .addContainerGap(21, Short.MAX_VALUE))
        );

        jLabel27.setText("Họ:");

        txtHoKH.addActionListener(this::txtHoKHActionPerformed);

        txtHoKH.setInputVerifier(new InputVerifier() {
            @Override
            public boolean verify(JComponent input) {
                String ho = txtHoKH.getText().trim();
                if (!ho.matches("^[\\p{L}]+(\\s[\\p{L}]+)*$")) {
                    JOptionPane.showMessageDialog(null,
                            "Họ chỉ được chứa chữ cái và khoảng trắng!");
                    return false; // Không cho rời field
                }
                if (ho.isEmpty()) {
                    JOptionPane.showMessageDialog(null,
                            "Họ không được để trống!");
                    return false; // Không cho rời field
                }
                return true;
            }
        });

        GroupLayout jPanel27Layout = new GroupLayout(jPanel27);
        jPanel27.setLayout(jPanel27Layout);
        jPanel27Layout.setHorizontalGroup(
                jPanel27Layout.createParallelGroup(GroupLayout.Alignment.LEADING)
                        .addGroup(jPanel27Layout.createSequentialGroup()
                                .addContainerGap(GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                .addComponent(jLabel27)
                                .addPreferredGap(LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(txtHoKH, GroupLayout.PREFERRED_SIZE, 203, GroupLayout.PREFERRED_SIZE)
                                .addGap(41, 41, 41))
        );
        jPanel27Layout.setVerticalGroup(
                jPanel27Layout.createParallelGroup(GroupLayout.Alignment.LEADING)
                        .addGroup(jPanel27Layout.createSequentialGroup()
                                .addGap(12, 12, 12)
                                .addGroup(jPanel27Layout.createParallelGroup(GroupLayout.Alignment.BASELINE)
                                        .addComponent(jLabel27)
                                        .addComponent(txtHoKH, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE))
                                .addContainerGap(15, Short.MAX_VALUE))
        );

        jLabel28.setText("Số điện thoại:");

        txtSoDienThoaiKH.addActionListener(this::txtSoDienThoaiKHActionPerformed);

        txtSoDienThoaiKH.setInputVerifier(new InputVerifier() {
            @Override
            public boolean verify(JComponent input) {
                String sdt = txtSoDienThoaiKH.getText().trim();
                if (!sdt.matches("^0\\d{9}$")) {
                    JOptionPane.showMessageDialog(null,
                            "Số điện thoại phải có 10 chữ số và bắt đầu bằng 0!");
                    return false; // Không cho rời field
                }
                if (sdt.isEmpty()) {
                    JOptionPane.showMessageDialog(null,
                            "Số điện thoại không được để trống!");
                    return false; // Không cho rời field
                }
                return true;
            }
        });

        GroupLayout jPanel28Layout = new GroupLayout(jPanel28);
        jPanel28.setLayout(jPanel28Layout);
        jPanel28Layout.setHorizontalGroup(
                jPanel28Layout.createParallelGroup(GroupLayout.Alignment.LEADING)
                        .addGroup(jPanel28Layout.createSequentialGroup()
                                .addGap(67, 67, 67)
                                .addComponent(jLabel28)
                                .addPreferredGap(LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(txtSoDienThoaiKH, GroupLayout.PREFERRED_SIZE, 203, GroupLayout.PREFERRED_SIZE)
                                .addContainerGap(39, Short.MAX_VALUE))
        );
        jPanel28Layout.setVerticalGroup(
                jPanel28Layout.createParallelGroup(GroupLayout.Alignment.LEADING)
                        .addGroup(jPanel28Layout.createSequentialGroup()
                                .addGap(12, 12, 12)
                                .addGroup(jPanel28Layout.createParallelGroup(GroupLayout.Alignment.BASELINE)
                                        .addComponent(jLabel28)
                                        .addComponent(txtSoDienThoaiKH, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE))
                                .addContainerGap(15, Short.MAX_VALUE))
        );

        GroupLayout layout = new GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
                layout.createParallelGroup(GroupLayout.Alignment.LEADING)
                        .addComponent(jPanel22, GroupLayout.DEFAULT_SIZE, GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(jPanel23, GroupLayout.DEFAULT_SIZE, GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addGroup(layout.createSequentialGroup()
                                .addContainerGap()
                                .addComponent(jPanel27, GroupLayout.DEFAULT_SIZE, GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                .addContainerGap())
                        .addGroup(layout.createSequentialGroup()
                                .addContainerGap()
                                .addComponent(jPanel26, GroupLayout.DEFAULT_SIZE, GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                .addContainerGap())
                        .addGroup(layout.createSequentialGroup()
                                .addContainerGap()
                                .addComponent(jPanel28, GroupLayout.DEFAULT_SIZE, GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                .addContainerGap())
                        .addGroup(layout.createSequentialGroup()
                                .addContainerGap()
                                .addComponent(jPanel24, GroupLayout.DEFAULT_SIZE, GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                .addContainerGap())
                        .addGroup(layout.createSequentialGroup()
                                .addGap(57, 57, 57)
                                .addComponent(btnLuu)
                                .addPreferredGap(LayoutStyle.ComponentPlacement.RELATED, GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                .addComponent(btnHuy)
                                .addGap(68, 68, 68))
        );
        layout.setVerticalGroup(
                layout.createParallelGroup(GroupLayout.Alignment.LEADING)
                        .addGroup(layout.createSequentialGroup()
                                .addComponent(jPanel22, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(jPanel27, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(jPanel23, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(jPanel26, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(jPanel28, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(jPanel24, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)
                                .addGap(18, 18, 18)
                                .addGroup(layout.createParallelGroup(GroupLayout.Alignment.BASELINE)
                                        .addComponent(btnLuu)
                                        .addComponent(btnHuy))
                                .addGap(0, 33, Short.MAX_VALUE))
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void txtMaKHActionPerformed(ActionEvent evt) {//GEN-FIRST:event_txtMaKHActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_txtMaKHActionPerformed

    private void txtTenKHActionPerformed(ActionEvent evt) {//GEN-FIRST:event_txtTenKHActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_txtTenKHActionPerformed

    private void txtDiaChiKHActionPerformed(ActionEvent evt) {//GEN-FIRST:event_txtDiaChiKHActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_txtDiaChiKHActionPerformed

    private JButton createBtn(String text, Color color){
        JButton btn = new JButton(text);
        btn.setBackground(color);
        btn.setForeground(Color.WHITE);
        btn.setFocusPainted(false);
        btn.setCursor(new Cursor(Cursor.HAND_CURSOR));// Trong jpBtn panel

        return btn;
    }

    private void luu(){
        btnLuu = createBtn("Lưu", UIColors.SAVE);
        btnLuu.addActionListener(v -> {
            String maKH = txtMaKH.getText().trim();
            String ten = txtTenKH.getText().trim();
            String diaChi = txtDiaChiKH.getText().trim();
            LocalDate ngaySinh = jDateChooser1.getDate() != null ? jDateChooser1.getDate().toInstant().atZone(java.time.ZoneId.systemDefault()).toLocalDate() : null;
            String ho = txtHoKH.getText().trim();
            String sdt = txtSoDienThoaiKH.getText().trim();

            KhachHangBUS khachHangBUS = new KhachHangBUS();
            if (mode == Mode.ADD) {
                KhachHangDTO newKhachHang = new KhachHangDTO(maKH, ho, ten, diaChi, sdt, ngaySinh);
                khachHangBUS.them(newKhachHang);
            } else if (mode == Mode.EDIT && currentKhachHang != null) {
                currentKhachHang.setHo(ho);
                currentKhachHang.setTen(ten);
                currentKhachHang.setDiaChi(diaChi);
                currentKhachHang.setNgaySinh (ngaySinh);
                currentKhachHang.setSdt(sdt);
                khachHangBUS.suaKhachHang(currentKhachHang);
            }
            dispose(); // Đóng dialog sau khi lưu
        });
    }

    private void huy(){
        btnHuy = createBtn("Hủy", UIColors.CANCEL);
        btnHuy.addActionListener(v -> {
            dispose();
        });
    }
    private void txtHoKHActionPerformed(ActionEvent evt) {//GEN-FIRST:event_txtHoKHActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_txtHoKHActionPerformed

    private void txtSoDienThoaiKHActionPerformed(ActionEvent evt) {//GEN-FIRST:event_txtSoDienThoaiKHActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_txtSoDienThoaiKHActionPerformed

    public void setKhachHangData(KhachHangDTO kh) {
        txtMaKH.setText(kh.getMaKH());
        txtTenKH.setText(kh.getTen());
        txtDiaChiKH.setText(kh.getDiaChi());
        if (kh.getNgaySinh() != null) {
            jDateChooser1.setDate(java.util.Date.from(kh.getNgaySinh().atStartOfDay(java.time.ZoneId.systemDefault()).toInstant()));
        } else {
            jDateChooser1.setDate(null);
        }
        txtHoKH.setText(kh.getHo());
        txtSoDienThoaiKH.setText(kh.getSdt());
    }

    // variables
    private JButton btnHuy, btnLuu;

    private JDateChooser jDateChooser1;

    private JLabel jLabel22, jLabel23, jLabel24, jLabel26, jLabel27, jLabel28;

    private JPanel jPanel22, jPanel23, jPanel24, jPanel26, jPanel27, jPanel28;

    private JTextField txtDiaChiKH, txtHoKH, txtMaKH, txtSoDienThoaiKH, txtTenKH;
}
