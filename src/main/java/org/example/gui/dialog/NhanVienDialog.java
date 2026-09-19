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
import java.util.Objects;

import static java.sql.Date.valueOf;

@FieldDefaults(level = AccessLevel.PRIVATE)
public class NhanVienDialog extends JDialog {
    Mode mode;
    NhanVienDTO currentNhanVien;
    NhanVienDAO ds;
    NhanVienBUS bus;

    JButton btnHuy, btnLuu;

    JDateChooser jDob;

    JLabel lbIDStaff, lbFirstName, lbLastName, lbRole, lbBirth, lbPhoneNumber, lbAddress;

    JTextField txtAddress, txtFirstName, txtIdStaff, txtPhoneNumber, txtLastName;

    JPanel panelForm, jpBtn;

    JComboBox<String> cbRoles;

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
        cbRoles = new JComboBox<>();

        initComponents();
        this.setLocationRelativeTo(null); // set location after init

        if (mode == Mode.EDIT && nv != null) {
            setStaffData(nv);
            txtIdStaff.setEditable(false); // Không cho sửa mã NV
            txtFirstName.requestFocus(); // Chuyển focus đến trường Họ
            setTitle("Sửa nhân viên");
        } else {
            setTitle("Thêm nhân viên");
        }
    }


    @SuppressWarnings("unchecked")

    private void initComponents() {
        setLayout(new BorderLayout());
        panelForm = new JPanel(new GridLayout(7, 2, 10, 10));
        jpBtn = new JPanel(new FlowLayout());


        //row idStaff
        lbIDStaff = new JLabel("Mã nhân viên: ");
        lbIDStaff.setBorder(BorderFactory.createEmptyBorder(5, 5, 5, 0));
        panelForm.add(lbIDStaff);
        txtIdStaff = new JTextField();
        panelForm.add(txtIdStaff);

        txtIdStaff.addActionListener(this::txtIdStaffActionPerformed);

        //row firstName
        lbFirstName = new JLabel("Họ: ");
        lbFirstName.setBorder(BorderFactory.createEmptyBorder(5, 5, 5, 0));
        panelForm.add(lbFirstName);
        txtFirstName = new JTextField();
        panelForm.add(txtFirstName);

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

        //row lastName
        lbLastName = new JLabel("Tên: ");
        lbLastName.setBorder(BorderFactory.createEmptyBorder(5, 5, 5, 0));
        panelForm.add(lbLastName);
        txtLastName = new JTextField();
        panelForm.add(txtLastName);

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

        //row role
        lbRole = new JLabel("Chức vụ: ");
        lbRole.setBorder(BorderFactory.createEmptyBorder(5, 5, 5, 0));
        panelForm.add(lbRole);
        loadRolesCombobox();
        panelForm.add(cbRoles);
        panelForm.add(cbRoles);

        //row birthDay
        lbBirth = new JLabel("Ngày sinh ");
        lbBirth.setBorder(BorderFactory.createEmptyBorder(5, 5, 5, 0));
        panelForm.add(lbBirth);

        jDob = new JDateChooser();
        jDob.setDateFormatString("dd/MM/yyyy");
        jDob.setDate(valueOf(LocalDate.now()));
        panelForm.add(jDob);

        jDob.setInputVerifier(new InputVerifier() {
            @Override
            public boolean verify(JComponent input) {
                if (jDob.getDate() == null) {
                    JOptionPane.showMessageDialog(null,
                            "Ngày sinh không được để trống!");
                    return false; // Không cho rời field
                }
                if (jDob.getDate().after(new java.util.Date())) {
                    JOptionPane.showMessageDialog(null,
                            "Ngày sinh không được lớn hơn ngày hiện tại!");
                    return false; // Không cho rời field
                }
                return true;
            }
        });

        //row role
        lbPhoneNumber = new JLabel("Số điện thoại: ");
        lbPhoneNumber.setBorder(BorderFactory.createEmptyBorder(5, 5, 5, 0));
        panelForm.add(lbPhoneNumber);
        txtPhoneNumber = new JTextField();
        panelForm.add(txtPhoneNumber);

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

        //row address
        lbAddress = new JLabel("Chức vụ: ");
        lbAddress.setBorder(BorderFactory.createEmptyBorder(5, 5, 5, 0));
        panelForm.add(lbAddress);
        txtAddress = new JTextField();
        panelForm.add(txtAddress);

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

        // define handle funtion
        luu();
        jpBtn.add(btnLuu);
        huy();
        jpBtn.add(btnHuy);


        add(panelForm, BorderLayout.CENTER);
        jpBtn.setBorder(BorderFactory.createEmptyBorder(5, 0, 10, 0));
        add(jpBtn, BorderLayout.SOUTH);

        setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);
        setLocationRelativeTo(null);
        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void loadRolesCombobox() {
        String[] roles = {"Tư vấn", "", "Điều hành tour", "Quản trị nội dung"};

        // Đưa mảng vào DefaultComboBoxModel
        DefaultComboBoxModel<String> model = new DefaultComboBoxModel<>(roles);

        // Gán model cho ComboBox
        cbRoles = new JComboBox<>(model);
    }

    private void txtPhoneNumberActionPerformed(ActionEvent evt) {//GEN-FIRST:event_txtPhoneNumberActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_txtPhoneNumberActionPerformed

    private void txtFirstNameActionPerformed(ActionEvent evt) {//GEN-FIRST:event_txtFirstNameActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_txtFirstNameActionPerformed

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
            String idStaff = txtFirstName.getText().trim();
            String firstName = txtFirstName.getText().trim();
            String lastName = txtLastName.getText().trim();
            String role = Objects.requireNonNull(cbRoles.getSelectedItem()).toString();
            LocalDate dob = jDob.getDate() != null ? jDob.getDate().toInstant().atZone(java.time.ZoneId.systemDefault()).toLocalDate() : null;
            String phoneNumber = txtPhoneNumber.getText().trim();
            String address = txtAddress.getText().trim();

            if (idStaff.isEmpty()) {
                JOptionPane.showMessageDialog(null,
                        "Mã nhân viên không được để trống!");
                return;
            }
            if (!idStaff.matches("^NV\\d{3}$")) {
                JOptionPane.showMessageDialog(null,
                        "Mã nhân viên phải có dạng NVxxx!");
                return;
            }
            for (NhanVienDTO nv : ds.layDanhSachNV()) {
                if (nv.getMaNV().equals(idStaff)) {
                    JOptionPane.showMessageDialog(null,
                            "Mã nhân viên đã tồn tại!");
                    return;
                }
            }

            bus = new NhanVienBUS();
            if (mode == Mode.ADD) {
                NhanVienDTO newStaff = new NhanVienDTO(idStaff, role, firstName, lastName, address, phoneNumber, dob);
                bus.them(newStaff);
            } else if (mode == Mode.EDIT && currentNhanVien != null) {
                currentNhanVien.setHo(firstName);
                currentNhanVien.setTen(lastName);
                currentNhanVien.setChucVu(role);
                currentNhanVien.setNgaySinh(dob);
                currentNhanVien.setSdt(phoneNumber);
                currentNhanVien.setDiaChi(address);

                bus.suaNhanVien(currentNhanVien);
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

    private void txtAddressActionPerformed(ActionEvent evt) {//GEN-FIRST:event_txtAddressActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_txtAddressActionPerformed

    private void txtLastNameActionPerformed(ActionEvent evt) {//GEN-FIRST:event_txtLastNameActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_txtLastNameActionPerformed

    private void txtIdStaffActionPerformed(ActionEvent evt) {//GEN-FIRST:event_txtFirstNameActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_txtFirstNameActionPerformed

    public void setStaffData(NhanVienDTO nv) {
        txtFirstName.setText(nv.getMaNV());
        txtFirstName.setText(nv.getHo());
        txtLastName.setText(nv.getTen());
        // cbRoles.setSelectedItem();

        String roleSelected = Objects.requireNonNull(cbRoles.getSelectedItem()).toString();

        if (nv.getNgaySinh() != null) {
            jDob.setDate(java.util.Date.from(nv.getNgaySinh().atStartOfDay(java.time.ZoneId.systemDefault()).toInstant()));
        } else {
            jDob.setDate(null);
        }
        txtPhoneNumber.setText(nv.getSdt());
        txtAddress.setText(nv.getDiaChi());
    }
}