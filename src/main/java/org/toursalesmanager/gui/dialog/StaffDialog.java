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
import java.sql.SQLException;
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
        AddComponentToForm.addFormRow(panelForm, "Mã nhân viên: ", txtIdStaff);

        //row firstName
        txtFirstName = new JTextField();
        AddComponentToForm.addFormRow(panelForm, "Họ: ", txtFirstName);

        //row lastName
        txtLastName = new JTextField();
        AddComponentToForm.addFormRow(panelForm, "Tên: ", txtLastName);

        //row role
        loadRolesCombobox();
        panelForm.add(cbRoles);
        AddComponentToForm.addFormRow(panelForm, "Chức vụ: ", cbRoles);

        //row birthDay
        jDob = new JDateChooser();
        jDob.setDateFormatString("dd/MM/yyyy");
        jDob.setDate(valueOf(LocalDate.now()));
        AddComponentToForm.addFormRow(panelForm, "Ngày sinh: ", jDob);

        //row role
        txtPhoneNumber = new JTextField();
        AddComponentToForm.addFormRow(panelForm, "Số điện thoại: ", txtPhoneNumber);

        //row address
        txtAddress = new JTextField();
        AddComponentToForm.addFormRow(panelForm, "Địa chỉ: ", txtAddress);

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



            if (mode == Mode.ADD) {
                StaffDTO newStaff = new StaffDTO(idStaff, firstName, lastName, address, phoneNumber, dob);
                try {
                    if (!bus.addStaff(newStaff, role)) {
                        JOptionPane.showMessageDialog(this,
                                "Thêm nhân viên thất bại. Vui lòng kiểm tra dữ liệu hoặc kết nối cơ sở dữ liệu.",
                                "Lỗi",
                                JOptionPane.ERROR_MESSAGE);
                        return;
                    }
                } catch (IllegalArgumentException e) {
                    JOptionPane.showMessageDialog(this, e.getMessage(), "Dữ liệu không hợp lệ", JOptionPane.WARNING_MESSAGE);
                    return;
                } catch (SQLException e) {
                    e.printStackTrace();
                    JOptionPane.showMessageDialog(this,
                            "Không thể thêm nhân viên do lỗi cơ sở dữ liệu.",
                            "Lỗi", JOptionPane.ERROR_MESSAGE);
                    return;
                }
            } else if (mode == Mode.EDIT && currentNhanVien != null) {
                StaffDTO updatedStaff = new StaffDTO(
                        currentNhanVien.getIdStaff(),
                        firstName,
                        lastName,
                        address,
                        phoneNumber,
                        dob
                );

                try {
                    if (!bus.updateStaff(updatedStaff, role)) {
                        JOptionPane.showMessageDialog(this,
                                "Cập nhật nhân viên thất bại. Vui lòng kiểm tra dữ liệu hoặc kết nối cơ sở dữ liệu.",
                                "Lỗi",
                                JOptionPane.ERROR_MESSAGE);
                        return;
                    }
                } catch (IllegalArgumentException e) {
                    JOptionPane.showMessageDialog(this, e.getMessage(), "Dữ liệu không hợp lệ", JOptionPane.WARNING_MESSAGE);
                    return;
                } catch (SQLException e) {
                    e.printStackTrace();
                    JOptionPane.showMessageDialog(this,
                            "Không thể cập nhật nhân viên do lỗi cơ sở dữ liệu.",
                            "Lỗi", JOptionPane.ERROR_MESSAGE);
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