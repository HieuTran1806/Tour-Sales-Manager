package org.toursalesmanager.gui.dialog;

import com.toedter.calendar.JDateChooser;
import lombok.AccessLevel;
import lombok.experimental.FieldDefaults;
import org.toursalesmanager.bus.StaffBUS;
import org.toursalesmanager.dao.StaffDAO;
import org.toursalesmanager.dto.StaffDTO;
import org.toursalesmanager.enums.Role;
import org.toursalesmanager.gui.component.ButtonFactory;
import org.toursalesmanager.gui.helper.AddComponentToForm;
import org.toursalesmanager.gui.helper.UIColors;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.time.LocalDate;
import java.util.Arrays;
import java.util.Objects;

import static java.sql.Date.valueOf;

@FieldDefaults(level = AccessLevel.PRIVATE)
public class StaffDialog extends JDialog {
    Mode mode;
    StaffDTO currentNhanVien;
    StaffBUS bus;

    JButton cancelBtn, saveBtn;

    JDateChooser jDob;

    JLabel lbIDStaff, lbFirstName, lbLastName, lbRole, lbBirth, lbPhoneNumber, lbAddress;

    JTextField txtAddress, txtFirstName, txtIdStaff, txtPhoneNumber, txtLastName;

    JPanel panelForm, jpBtn;

    JComboBox<Role> cbRoles;

    public enum Mode {
        ADD,
        EDIT
    }

    public StaffDialog(java.awt.Frame parent, boolean modal, Mode mode,
                       StaffDTO nv) {
        super(parent, modal);
        this.mode = mode;
        this.currentNhanVien = nv;
        this.bus = new StaffBUS();
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
        panelForm = new JPanel(new GridLayout(0, 2, 10, 10));
        jpBtn = new JPanel(new FlowLayout());


        //row idStaff
        txtIdStaff = new JTextField();
        txtIdStaff.addActionListener(this::txtIdStaffActionPerformed);
        AddComponentToForm.addFormRow(panelForm, "Mã nhân viên: ", txtIdStaff);


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
        save();
        jpBtn.add(saveBtn);
        cancel();
        jpBtn.add(cancelBtn);


        add(panelForm, BorderLayout.CENTER);
        jpBtn.setBorder(BorderFactory.createEmptyBorder(5, 0, 10, 0));
        add(jpBtn, BorderLayout.SOUTH);

        setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);
        setLocationRelativeTo(null);
        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void loadRolesCombobox() {
        Role[] roles = Arrays.stream(Role.values())
                .filter(role -> role != Role.CUSTOMER && role != Role.ADMIN)
                .toArray(Role[]::new);

        cbRoles.setModel(new DefaultComboBoxModel<>(roles));
    }

    private void txtPhoneNumberActionPerformed(ActionEvent evt) {//GEN-FIRST:event_txtPhoneNumberActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_txtPhoneNumberActionPerformed

    private void txtFirstNameActionPerformed(ActionEvent evt) {//GEN-FIRST:event_txtFirstNameActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_txtFirstNameActionPerformed

    private void save(){
        saveBtn = ButtonFactory.create("Lưu", UIColors.SAVE);
        saveBtn.addActionListener(v -> {
            // get data
            String idStaff = txtIdStaff.getText().trim();
            String firstName = txtFirstName.getText().trim();
            String lastName = txtLastName.getText().trim();
            Role role = Role.valueOf(Objects.requireNonNull(cbRoles.getSelectedItem()).toString());
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
            for (StaffDTO nv : bus.getAllStaffs()) {
                if (nv.getIdStaff().equals(idStaff)) {
                    JOptionPane.showMessageDialog(null,
                            "Mã nhân viên đã tồn tại!");
                    return;
                }
            }

            if (mode == Mode.ADD) {
                StaffDTO newStaff = new StaffDTO(idStaff, firstName, lastName, address, phoneNumber, dob);
                if (!bus.addStaff(newStaff, role)) {
                    JOptionPane.showMessageDialog(this,
                            "Thêm nhân viên thất bại. Vui lòng kiểm tra dữ liệu hoặc kết nối cơ sở dữ liệu.",
                            "Lỗi",
                            JOptionPane.ERROR_MESSAGE);
                    return;
                }
            } else if (mode == Mode.EDIT && currentNhanVien != null) {
                currentNhanVien.setFirstName(firstName);
                currentNhanVien.setLastName(lastName);
                currentNhanVien.setAddress(address);
                currentNhanVien.setPhoneNumber(phoneNumber);
                currentNhanVien.setDob(dob);

                if (!bus.updateStaff(currentNhanVien, role)) {
                    JOptionPane.showMessageDialog(this,
                            "Cập nhật nhân viên thất bại. Vui lòng kiểm tra dữ liệu hoặc kết nối cơ sở dữ liệu.",
                            "Lỗi",
                            JOptionPane.ERROR_MESSAGE);
                    return;
                }
            }

            JOptionPane.showMessageDialog(this, "Đã lưu nhân viên. Mật khẩu tài khoản mặc định: 123456.");
            dispose(); // đóng dialog sau khi lưu
        });
    }

    private void cancel(){
        cancelBtn = ButtonFactory.create("Hủy", UIColors.CANCEL);
        cancelBtn.addActionListener(v -> {
            dispose();
        });
    }

    private void txtAddressActionPerformed(ActionEvent evt) {//GEN-FIRST:event_txtAddressActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_txtAddressActionPerformed

    private void txtLastNameActionPerformed(ActionEvent evt) {//GEN-FIRST:event_txtLastNameActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_txtLastNameActionPerformed

    private void txtIdStaffActionPerformed(ActionEvent evt) {//GEN-FIRST:event_txtIdStaffActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_txtIdStaffActionPerformed

    public void setStaffData(StaffDTO nv) {
        txtIdStaff.setText(nv.getIdStaff());
        txtFirstName.setText(nv.getFirstName());
        txtLastName.setText(nv.getLastName());
        cbRoles.setSelectedItem(nv.getRole());

        if (nv.getDob() != null) {
            jDob.setDate(java.util.Date.from(nv.getDob().atStartOfDay(java.time.ZoneId.systemDefault()).toInstant()));
        } else {
            jDob.setDate(null);
        }
        txtPhoneNumber.setText(nv.getPhoneNumber());
        txtAddress.setText(nv.getAddress());
    }
}