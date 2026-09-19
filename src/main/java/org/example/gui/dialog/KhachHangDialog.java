package org.example.gui.dialog;

import com.toedter.calendar.JDateChooser;
import lombok.AccessLevel;
import lombok.experimental.FieldDefaults;
import org.example.bus.KhachHangBUS;
import org.example.dao.KhachHangDAO;
import org.example.dto.KhachHangDTO;
import org.example.gui.panel.UIColors;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.time.LocalDate;

@FieldDefaults(level = AccessLevel.PRIVATE)
public class KhachHangDialog extends JDialog {
    JButton btnHuy, btnLuu;

    JDateChooser jDateChooser1;

    JLabel lbIDCustomer, lbLastName, lbAddress, lbDob, lbFirstName, lbPhoneNumber;

    JPanel jPanel22, jPanel23, jPanel24, jPanel26, jPanel27, jPanel28;

    JTextField txtAddress, txtFirstName, txtIDCustomer, txtPhoneNumber, txtLastName;

    public enum Mode {
        ADD, EDIT
    }

    Mode mode;
    KhachHangDTO currentKhachHang;
    KhachHangDAO ds;

    public KhachHangDialog(java.awt.Frame parent, boolean modal, KhachHangDAO ds, Mode mode, KhachHangDTO kh) {
        super(parent, modal);
        this.ds = ds;
        this.mode = mode;
        this.currentKhachHang = kh;

        initComponents();

        if (mode == Mode.EDIT && kh != null) {
            setKhachHangData(kh);
            txtIDCustomer.setEditable(false);
            txtFirstName.requestFocus();
            setTitle("Sửa khách hàng");
        } else {
            setTitle("Thêm khách hàng");
        }
    }

    @SuppressWarnings("unchecked")
    private void initComponents() {

        jPanel22 = new JPanel();
        lbIDCustomer = new JLabel();
        txtIDCustomer = new JTextField();
        jPanel23 = new JPanel();
        lbLastName = new JLabel();
        txtLastName = new JTextField();
        jPanel24 = new JPanel();
        lbAddress = new JLabel();
        txtAddress = new JTextField();
        btnLuu = new JButton();
        btnHuy = new JButton();
        jPanel26 = new JPanel();
        lbDob = new JLabel();
        jDateChooser1 = new com.toedter.calendar.JDateChooser();
        jPanel27 = new JPanel();
        lbFirstName = new JLabel();
        txtFirstName = new JTextField();
        jPanel28 = new JPanel();
        lbPhoneNumber = new JLabel();
        txtPhoneNumber = new JTextField();

        setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);

        lbIDCustomer.setText("Mã khách hàng:");

        txtIDCustomer.addActionListener(this::txtIDCustomerActionPerformed);

        txtIDCustomer.setInputVerifier(new InputVerifier() {
            @Override
            public boolean verify(JComponent input) {
                String ma = txtIDCustomer.getText().trim();

                if (!ma.matches("^KH\\d{3}$")) {
                    JOptionPane.showMessageDialog(null,
                            "Mã khách hàng phải có dạng KHxxx!");
                    return false;
                }
                if (ma.isEmpty()) {
                    JOptionPane.showMessageDialog(null,
                            "Mã khách hàng không được để trống!");
                    return false;
                }
                for (KhachHangDTO kh : ds.layDanhSachKHang()) {
                    if (kh.getMaKH().equals(ma)) {
                        JOptionPane.showMessageDialog(null,
                                "Mã khách hàng đã tồn tại!");
                        return false;
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
                                .addComponent(lbIDCustomer)
                                .addPreferredGap(LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(txtIDCustomer, GroupLayout.PREFERRED_SIZE, 203, GroupLayout.PREFERRED_SIZE)
                                .addContainerGap(GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        jPanel22Layout.setVerticalGroup(
                jPanel22Layout.createParallelGroup(GroupLayout.Alignment.LEADING)
                        .addGroup(jPanel22Layout.createSequentialGroup()
                                .addGap(12, 12, 12)
                                .addGroup(jPanel22Layout.createParallelGroup(GroupLayout.Alignment.BASELINE)
                                        .addComponent(lbIDCustomer)
                                        .addComponent(txtIDCustomer, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE))
                                .addContainerGap(15, Short.MAX_VALUE))
        );

        lbLastName.setText("Tên:");

        txtLastName.addActionListener(this::txtLastNameActionPerformed);

        txtLastName.setInputVerifier(new InputVerifier() {
            @Override
            public boolean verify(JComponent input) {
                String ten = txtLastName.getText().trim();
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
                                .addComponent(lbLastName)
                                .addPreferredGap(LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(txtLastName, GroupLayout.PREFERRED_SIZE, 203, GroupLayout.PREFERRED_SIZE)
                                .addGap(46, 46, 46))
        );
        jPanel23Layout.setVerticalGroup(
                jPanel23Layout.createParallelGroup(GroupLayout.Alignment.LEADING)
                        .addGroup(jPanel23Layout.createSequentialGroup()
                                .addGap(12, 12, 12)
                                .addGroup(jPanel23Layout.createParallelGroup(GroupLayout.Alignment.BASELINE)
                                        .addComponent(lbLastName)
                                        .addComponent(txtLastName, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE))
                                .addContainerGap(15, Short.MAX_VALUE))
        );

        lbAddress.setText("Địa chỉ:");

        txtAddress.addActionListener(this::txtAddressActionPerformed);

        txtAddress.setInputVerifier(new InputVerifier() {
            @Override
            public boolean verify(JComponent input) {
                String diaChi = txtAddress.getText().trim();
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
                                .addComponent(lbAddress)
                                .addPreferredGap(LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(txtAddress, GroupLayout.PREFERRED_SIZE, 203, GroupLayout.PREFERRED_SIZE)
                                .addContainerGap(GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        jPanel24Layout.setVerticalGroup(
                jPanel24Layout.createParallelGroup(GroupLayout.Alignment.LEADING)
                        .addGroup(jPanel24Layout.createSequentialGroup()
                                .addGap(12, 12, 12)
                                .addGroup(jPanel24Layout.createParallelGroup(GroupLayout.Alignment.BASELINE)
                                        .addComponent(lbAddress)
                                        .addComponent(txtAddress, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE))
                                .addContainerGap(15, Short.MAX_VALUE))
        );

        // define handle function
        luu();
        huy();

        lbDob.setText("Ngày sinh:");

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
                                .addComponent(lbDob)
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
                                        .addComponent(lbDob))
                                .addContainerGap(21, Short.MAX_VALUE))
        );

        lbFirstName.setText("Họ:");

        txtFirstName.addActionListener(this::txtFirstNameActionPerformed);

        txtFirstName.setInputVerifier(new InputVerifier() {
            @Override
            public boolean verify(JComponent input) {
                String ho = txtFirstName.getText().trim();
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
                                .addComponent(lbFirstName)
                                .addPreferredGap(LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(txtFirstName, GroupLayout.PREFERRED_SIZE, 203, GroupLayout.PREFERRED_SIZE)
                                .addGap(41, 41, 41))
        );
        jPanel27Layout.setVerticalGroup(
                jPanel27Layout.createParallelGroup(GroupLayout.Alignment.LEADING)
                        .addGroup(jPanel27Layout.createSequentialGroup()
                                .addGap(12, 12, 12)
                                .addGroup(jPanel27Layout.createParallelGroup(GroupLayout.Alignment.BASELINE)
                                        .addComponent(lbFirstName)
                                        .addComponent(txtFirstName, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE))
                                .addContainerGap(15, Short.MAX_VALUE))
        );

        lbPhoneNumber.setText("Số điện thoại:");

        txtPhoneNumber.addActionListener(this::txtPhoneNumberActionPerformed);

        txtPhoneNumber.setInputVerifier(new InputVerifier() {
            @Override
            public boolean verify(JComponent input) {
                String sdt = txtPhoneNumber.getText().trim();
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
                                .addComponent(lbPhoneNumber)
                                .addPreferredGap(LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(txtPhoneNumber, GroupLayout.PREFERRED_SIZE, 203, GroupLayout.PREFERRED_SIZE)
                                .addContainerGap(39, Short.MAX_VALUE))
        );
        jPanel28Layout.setVerticalGroup(
                jPanel28Layout.createParallelGroup(GroupLayout.Alignment.LEADING)
                        .addGroup(jPanel28Layout.createSequentialGroup()
                                .addGap(12, 12, 12)
                                .addGroup(jPanel28Layout.createParallelGroup(GroupLayout.Alignment.BASELINE)
                                        .addComponent(lbPhoneNumber)
                                        .addComponent(txtPhoneNumber, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE))
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

        this.setLocationRelativeTo(null); // set location after init
        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void txtIDCustomerActionPerformed(ActionEvent evt) {//GEN-FIRST:event_txtIDCustomerActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_txtIDCustomerActionPerformed

    private void txtLastNameActionPerformed(ActionEvent evt) {//GEN-FIRST:event_txtLastNameActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_txtLastNameActionPerformed

    private void txtAddressActionPerformed(ActionEvent evt) {//GEN-FIRST:event_txtAddressActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_txtAddressActionPerformed

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
            String maKH = txtIDCustomer.getText().trim();
            String ten = txtLastName.getText().trim();
            String diaChi = txtAddress.getText().trim();
            LocalDate ngaySinh = jDateChooser1.getDate() != null ? jDateChooser1.getDate().toInstant().atZone(java.time.ZoneId.systemDefault()).toLocalDate() : null;
            String ho = txtFirstName.getText().trim();
            String sdt = txtPhoneNumber.getText().trim();

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
    private void txtFirstNameActionPerformed(ActionEvent evt) {//GEN-FIRST:event_txtFirstNameActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_txtFirstNameActionPerformed

    private void txtPhoneNumberActionPerformed(ActionEvent evt) {//GEN-FIRST:event_txtPhoneNumberActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_txtPhoneNumberActionPerformed

    public void setKhachHangData(KhachHangDTO kh) {
        txtIDCustomer.setText(kh.getMaKH());
        txtLastName.setText(kh.getTen());
        txtAddress.setText(kh.getDiaChi());
        if (kh.getNgaySinh() != null) {
            jDateChooser1.setDate(java.util.Date.from(kh.getNgaySinh().atStartOfDay(java.time.ZoneId.systemDefault()).toInstant()));
        } else {
            jDateChooser1.setDate(null);
        }
        txtFirstName.setText(kh.getHo());
        txtPhoneNumber.setText(kh.getSdt());
    }
}
