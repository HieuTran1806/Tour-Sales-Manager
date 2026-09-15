package org.example.gui.dialog;

import com.toedter.calendar.JDateChooser;
import lombok.AccessLevel;
import lombok.experimental.FieldDefaults;
import org.example.bus.NhanVienBUS;
import org.example.dao.NhanVienDAO;
import org.example.dto.NhanVienDTO;
import org.example.gui.panel.UIColors;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.time.LocalDate;

@FieldDefaults(level = AccessLevel.PRIVATE)
public class NhanVienDialog extends JDialog {
    Mode mode;
    NhanVienDTO currentNhanVien;
    NhanVienDAO ds;

    JButton btnHuy, btnLuu;

    JDateChooser jDateChooser1;

    JLabel jLabel1, jLabel2, jLabel3, jLabel4, jLabel5, jLabel6, jLabel7;

    JPanel jPanel1, jPanel2, jPanel3, jPanel4, jPanel5, jPanel6, jPanel7;

    JTextField txtChucVu, txtDiaChi, txtHoNV, txtMaNV, txtSoDienThoai, txtTenNV;

    public enum Mode {
        ADD,
        EDIT
    }

    public NhanVienDialog(java.awt.Frame parent, boolean modal,
                          NhanVienDAO ds, Mode mode,
                          NhanVienDTO nv) {
        super(parent, modal);
        this.ds = ds;
        this.mode = mode;
        this.currentNhanVien = nv;

        initComponents();
        this.setLocationRelativeTo(null); // set location after init

        if (mode == Mode.EDIT && nv != null) {
            setNhanVienData(nv);
            txtMaNV.setEditable(false); // Không cho sửa mã NV
            txtHoNV.requestFocus(); // Chuyển focus đến trường Họ
            setTitle("Sửa nhân viên");
        } else {
            setTitle("Thêm nhân viên");
        }
    }


    @SuppressWarnings("unchecked")

    private void initComponents() {

        jPanel1 = new JPanel();
        jLabel1 = new JLabel();
        txtMaNV = new JTextField();
        jPanel2 = new JPanel();
        jLabel2 = new JLabel();
        txtHoNV = new JTextField();
        jPanel3 = new JPanel();
        jLabel3 = new JLabel();
        txtTenNV = new JTextField();
        jPanel4 = new JPanel();
        jLabel4 = new JLabel();
        txtChucVu = new JTextField();
        jPanel5 = new JPanel();
        jLabel5 = new JLabel();
        jDateChooser1 = new JDateChooser();
        jPanel6 = new JPanel();
        jLabel6 = new JLabel();
        txtSoDienThoai = new JTextField();
        jPanel7 = new JPanel();
        jLabel7 = new JLabel();
        txtDiaChi = new JTextField();
        btnLuu = new JButton();
        btnHuy = new JButton();

        setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);

        jLabel1.setText("Mã nhân viên:");

        txtMaNV.addActionListener(this::txtMaNVActionPerformed);

        txtMaNV.setInputVerifier(new InputVerifier() {
            @Override
            public boolean verify(JComponent input) {
                String ma = txtMaNV.getText().trim();

                if (!ma.matches("^NV\\d{3}$")) {
                    JOptionPane.showMessageDialog(null,
                            "Mã nhân viên phải có dạng NVxxx!");
                    return false; // Không cho rời field
                }
                if (ma.isEmpty()) {
                    JOptionPane.showMessageDialog(null,
                            "Mã nhân viên không được để trống!");
                    return false; // Không cho rời field
                }
                for (NhanVienDTO nv : ds.layDanhSachNV()) {
                    if (nv.getMaNV().equals(ma)) {
                        JOptionPane.showMessageDialog(null,
                                "Mã nhân viên đã tồn tại!");
                        return false; // Không cho rời field
                    }
                }
                return true;
            }
        });

        GroupLayout jPanel1Layout = new GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
                jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addGroup(jPanel1Layout.createSequentialGroup()
                                .addGap(71, 71, 71)
                                .addComponent(jLabel1)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(txtMaNV, GroupLayout.PREFERRED_SIZE, 203, GroupLayout.PREFERRED_SIZE)
                                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        jPanel1Layout.setVerticalGroup(
                jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addGroup(jPanel1Layout.createSequentialGroup()
                                .addGap(12, 12, 12)
                                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                        .addComponent(jLabel1)
                                        .addComponent(txtMaNV, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE))
                                .addContainerGap(15, Short.MAX_VALUE))
        );

        jLabel2.setText("Họ:");

        txtHoNV.addActionListener(this::txtHoNVActionPerformed);

        txtHoNV.setInputVerifier(new InputVerifier() {
            @Override
            public boolean verify(JComponent input) {
                String ho = txtHoNV.getText().trim();
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

        GroupLayout jPanel2Layout = new GroupLayout(jPanel2);
        jPanel2.setLayout(jPanel2Layout);
        jPanel2Layout.setHorizontalGroup(
                jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addGroup(jPanel2Layout.createSequentialGroup()
                                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                .addComponent(jLabel2)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(txtHoNV, GroupLayout.PREFERRED_SIZE, 203, GroupLayout.PREFERRED_SIZE)
                                .addGap(82, 82, 82))
        );
        jPanel2Layout.setVerticalGroup(
                jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addGroup(jPanel2Layout.createSequentialGroup()
                                .addGap(12, 12, 12)
                                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                        .addComponent(jLabel2)
                                        .addComponent(txtHoNV, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE))
                                .addContainerGap(15, Short.MAX_VALUE))
        );

        jLabel3.setText("Tên:");

        txtTenNV.addActionListener(this::txtTenNVActionPerformed);

        txtTenNV.setInputVerifier(new InputVerifier() {
            @Override
            public boolean verify(JComponent input) {
                String ten = txtTenNV.getText().trim();
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

        GroupLayout jPanel3Layout = new GroupLayout(jPanel3);
        jPanel3.setLayout(jPanel3Layout);
        jPanel3Layout.setHorizontalGroup(
                jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addGroup(jPanel3Layout.createSequentialGroup()
                                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                .addComponent(jLabel3)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(txtTenNV, GroupLayout.PREFERRED_SIZE, 203, GroupLayout.PREFERRED_SIZE)
                                .addGap(87, 87, 87))
        );
        jPanel3Layout.setVerticalGroup(
                jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addGroup(jPanel3Layout.createSequentialGroup()
                                .addGap(12, 12, 12)
                                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                        .addComponent(jLabel3)
                                        .addComponent(txtTenNV, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE))
                                .addContainerGap(15, Short.MAX_VALUE))
        );

        jLabel4.setText("Chức vụ:");

        txtChucVu.addActionListener(this::txtChucVuActionPerformed);

        txtChucVu.setInputVerifier(new InputVerifier() {
            @Override
            public boolean verify(JComponent input) {
                String chucVu = txtChucVu.getText().trim();
                if (!chucVu.matches("^[\\p{L}]+(\\s[\\p{L}]+)*$")) {
                    JOptionPane.showMessageDialog(null,
                            "Chức vụ chỉ được chứa chữ cái và khoảng trắng!");
                    return false; // Không cho rời field
                }
                if (chucVu.isEmpty()) {
                    JOptionPane.showMessageDialog(null,
                            "Chức vụ không được để trống!");
                    return false; // Không cho rời field
                }
                return true;
            }
        });

        GroupLayout jPanel4Layout = new GroupLayout(jPanel4);
        jPanel4.setLayout(jPanel4Layout);
        jPanel4Layout.setHorizontalGroup(
                jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addGroup(jPanel4Layout.createSequentialGroup()
                                .addGap(99, 99, 99)
                                .addComponent(jLabel4)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(txtChucVu, GroupLayout.PREFERRED_SIZE, 203, GroupLayout.PREFERRED_SIZE)
                                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        jPanel4Layout.setVerticalGroup(
                jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addGroup(jPanel4Layout.createSequentialGroup()
                                .addGap(12, 12, 12)
                                .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                        .addComponent(jLabel4)
                                        .addComponent(txtChucVu, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE))
                                .addContainerGap(15, Short.MAX_VALUE))
        );

        jLabel5.setText("Ngày sinh");

        jDateChooser1.setDateFormatString("dd/MM/yyyy");

        jDateChooser1.setInputVerifier(new InputVerifier() {
            @Override
            public boolean verify(JComponent input) {
                if (jDateChooser1.getDate() == null) {
                    JOptionPane.showMessageDialog(null,
                            "Ngày sinh không được để trống!");
                    return false; // Không cho rời field
                }
                if (jDateChooser1.getDate().after(new java.util.Date())) {
                    JOptionPane.showMessageDialog(null,
                            "Ngày sinh không được lớn hơn ngày hiện tại!");
                    return false; // Không cho rời field
                }
                return true;
            }
        });

        GroupLayout jPanel5Layout = new GroupLayout(jPanel5);
        jPanel5.setLayout(jPanel5Layout);
        jPanel5Layout.setHorizontalGroup(
                jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addGroup(jPanel5Layout.createSequentialGroup()
                                .addGap(93, 93, 93)
                                .addComponent(jLabel5)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(jDateChooser1, GroupLayout.PREFERRED_SIZE, 199, GroupLayout.PREFERRED_SIZE)
                                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        jPanel5Layout.setVerticalGroup(
                jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addGroup(jPanel5Layout.createSequentialGroup()
                                .addContainerGap()
                                .addGroup(jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                        .addComponent(jDateChooser1, GroupLayout.Alignment.TRAILING, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)
                                        .addComponent(jLabel5, GroupLayout.Alignment.TRAILING))
                                .addContainerGap(18, Short.MAX_VALUE))
        );

        jLabel6.setText("Số điện thoại:");

        txtSoDienThoai.addActionListener(this::txtSoDienThoaiActionPerformed);

        txtSoDienThoai.setInputVerifier(new InputVerifier() {
            @Override
            public boolean verify(JComponent input) {
                String sdt = txtSoDienThoai.getText().trim();
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


        GroupLayout jPanel6Layout = new GroupLayout(jPanel6);
        jPanel6.setLayout(jPanel6Layout);
        jPanel6Layout.setHorizontalGroup(
                jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addGroup(jPanel6Layout.createSequentialGroup()
                                .addGap(73, 73, 73)
                                .addComponent(jLabel6)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(txtSoDienThoai, GroupLayout.PREFERRED_SIZE, 203, GroupLayout.PREFERRED_SIZE)
                                .addContainerGap(86, Short.MAX_VALUE))
        );
        jPanel6Layout.setVerticalGroup(
                jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addGroup(jPanel6Layout.createSequentialGroup()
                                .addGap(12, 12, 12)
                                .addGroup(jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                        .addComponent(jLabel6)
                                        .addComponent(txtSoDienThoai, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE))
                                .addContainerGap(15, Short.MAX_VALUE))
        );

        jLabel7.setText("Địa chỉ:");

        txtDiaChi.addActionListener(this::txtDiaChiActionPerformed);

        txtDiaChi.setInputVerifier(new InputVerifier() {
            @Override
            public boolean verify(JComponent input) {
                String diaChi = txtDiaChi.getText().trim();
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

        GroupLayout jPanel7Layout = new GroupLayout(jPanel7);
        jPanel7.setLayout(jPanel7Layout);
        jPanel7Layout.setHorizontalGroup(
                jPanel7Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addGroup(jPanel7Layout.createSequentialGroup()
                                .addGap(107, 107, 107)
                                .addComponent(jLabel7)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(txtDiaChi, GroupLayout.PREFERRED_SIZE, 203, GroupLayout.PREFERRED_SIZE)
                                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        jPanel7Layout.setVerticalGroup(
                jPanel7Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addGroup(jPanel7Layout.createSequentialGroup()
                                .addGap(12, 12, 12)
                                .addGroup(jPanel7Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                        .addComponent(jLabel7)
                                        .addComponent(txtDiaChi, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE))
                                .addContainerGap(15, Short.MAX_VALUE))
        );

        // define handle funtion
        luu();
        huy();

        GroupLayout layout = new GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
                layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addComponent(jPanel1, GroupLayout.DEFAULT_SIZE, GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(jPanel3, GroupLayout.DEFAULT_SIZE, GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(jPanel4, GroupLayout.DEFAULT_SIZE, GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(jPanel5, GroupLayout.DEFAULT_SIZE, GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(jPanel6, GroupLayout.DEFAULT_SIZE, GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(jPanel7, GroupLayout.DEFAULT_SIZE, GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addGroup(layout.createSequentialGroup()
                                .addGap(80, 80, 80)
                                .addComponent(btnLuu)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                .addComponent(btnHuy)
                                .addGap(88, 88, 88))
                        .addGroup(layout.createSequentialGroup()
                                .addContainerGap()
                                .addComponent(jPanel2, GroupLayout.DEFAULT_SIZE, GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                .addContainerGap())
        );
        layout.setVerticalGroup(
                layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addGroup(layout.createSequentialGroup()
                                .addComponent(jPanel1, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(jPanel2, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(jPanel3, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(jPanel4, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(jPanel5, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(jPanel6, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(jPanel7, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)
                                .addGap(18, 18, 18)
                                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                        .addComponent(btnLuu)
                                        .addComponent(btnHuy))
                                .addGap(0, 27, Short.MAX_VALUE))
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void txtSoDienThoaiActionPerformed(ActionEvent evt) {//GEN-FIRST:event_txtSoDienThoaiActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_txtSoDienThoaiActionPerformed

    private void txtHoNVActionPerformed(ActionEvent evt) {//GEN-FIRST:event_txtHoNVActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_txtHoNVActionPerformed

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
            String maNV = txtMaNV.getText().trim();
            String ho = txtHoNV.getText().trim();
            String ten = txtTenNV.getText().trim();
            String chucVu = txtChucVu.getText().trim();
            LocalDate ngaySinh = jDateChooser1.getDate() != null ? jDateChooser1.getDate().toInstant().atZone(java.time.ZoneId.systemDefault()).toLocalDate() : null;
            String sdt = txtSoDienThoai.getText().trim();
            String diaChi = txtDiaChi.getText().trim();

            NhanVienBUS nvBUS = new NhanVienBUS();
            if (mode == Mode.ADD) {
                NhanVienDTO newNV = new NhanVienDTO(maNV, chucVu, ho, ten, diaChi, sdt, ngaySinh);
                nvBUS.them(newNV);
            } else if (mode == Mode.EDIT && currentNhanVien != null) {
                currentNhanVien.setHo(ho);
                currentNhanVien.setTen(ten);
                currentNhanVien.setChucVu(chucVu);
                currentNhanVien.setNgaySinh(ngaySinh);
                currentNhanVien.setSdt(sdt);
                currentNhanVien.setDiaChi(diaChi);
                nvBUS.suaNhanVien(currentNhanVien);
            }

            JOptionPane.showMessageDialog(this, "Đã lưu TK là mà nhân viên, MK: 123"   );
            dispose(); // đóng dialog sau khi lưu
        });
    }

    private void huy(){
        btnHuy = createBtn("Hủy", UIColors.CANCEL);
        btnHuy.addActionListener(v -> {
            dispose();
        });
    }

    private void txtChucVuActionPerformed(ActionEvent evt) {//GEN-FIRST:event_txtChucVuActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_txtChucVuActionPerformed

    private void txtDiaChiActionPerformed(ActionEvent evt) {//GEN-FIRST:event_txtDiaChiActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_txtDiaChiActionPerformed

    private void txtTenNVActionPerformed(ActionEvent evt) {//GEN-FIRST:event_txtTenNVActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_txtTenNVActionPerformed

    private void txtMaNVActionPerformed(ActionEvent evt) {//GEN-FIRST:event_txtMaNVActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_txtMaNVActionPerformed

    public void setNhanVienData(NhanVienDTO nv) {
        txtMaNV.setText(nv.getMaNV());
        txtHoNV.setText(nv.getHo());
        txtTenNV.setText(nv.getTen());
        txtChucVu.setText(nv.getChucVu());
        if (nv.getNgaySinh() != null) {
            jDateChooser1.setDate(java.util.Date.from(nv.getNgaySinh().atStartOfDay(java.time.ZoneId.systemDefault()).toInstant()));
        } else {
            jDateChooser1.setDate(null);
        }
        txtSoDienThoai.setText(nv.getSdt());
        txtDiaChi.setText(nv.getDiaChi());
    }
}