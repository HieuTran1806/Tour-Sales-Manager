package org.example.gui.dialog;

import com.toedter.calendar.JDateChooser;
import org.example.bus.CTrinhKMBUS;
import org.example.dao.TourDAO;
import org.example.dto.CTrinhKMDTO;
import org.example.dto.KMHDDTO;
import org.example.dto.KMTourDTO;
import org.example.dto.TourDTO;
import org.example.gui.helper.DateHelper;
import org.example.gui.panel.UIColors;

import javax.swing.*;
import javax.swing.event.ChangeEvent;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.InputMethodEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.util.ArrayList;
import java.util.Date;

import java.time.LocalDate;

public class CTrinhKMDialog extends JDialog {
    private CTrinhKMBUS bus;
    private boolean isEdit = false;
    private CardLayout card;

    public CTrinhKMDialog() {
        initComponents();
        card = (CardLayout) pnlSwitch.getLayout();
        this.bus = new CTrinhKMBUS();
        this.setTitle("Chương trình khuyến mãi");
        this.setLocationRelativeTo(null);
        txtMaCTKM.setInputVerifier(new InputVerifier() {
            @Override
            public boolean verify(JComponent input) {
                String ma = txtMaCTKM.getText().trim();
                if (isEdit) return true; // Không kiểm tra khi sửa
                if (!ma.matches("^KM\\d{2}$")) {
                    JOptionPane.showMessageDialog(CTrinhKMDialog.this, "Mã phải có định dạng KMxx (x là số)");
                    return false;
                }
                if (bus.timCTrinhKM(ma) != null) {
                    JOptionPane.showMessageDialog(CTrinhKMDialog.this, "Mã đã tồn tại");
                    return false;
                }
                return true;
            }
        });
        txtTenctkm.setInputVerifier(new InputVerifier() {
            @Override
            public boolean verify(JComponent input){
                String ten=txtTenctkm.getText().trim();
                if(ten.isEmpty()){
                    JOptionPane.showMessageDialog(CTrinhKMDialog.this, "Tên chương trình khuyến mãi không được để trống");
                    return false;
                }
                return true;
            }
        });

        txtChietkhau.setInputVerifier(new InputVerifier() {
            @Override
            public boolean verify(JComponent input){
                String chietkhau=txtChietkhau.getText().trim();
                try{
                    double ck=Double.parseDouble(chietkhau);
                    if(ck<0||ck>100){
                        JOptionPane.showMessageDialog(CTrinhKMDialog.this, "Chiết khấu phải là số từ 0 đến 100");
                        return false;
                    }
                }catch(NumberFormatException e){
                    JOptionPane.showMessageDialog(CTrinhKMDialog.this, "Chiết khấu phải là số hợp lệ");
                    return false;
                }
                return true;
            }
        });

    }

    public CTrinhKMDialog(CTrinhKMBUS bus) {
        this.bus = bus;
        initComponents();
        card = (CardLayout) pnlSwitch.getLayout();
        this.setTitle("Chương trình khuyến mãi");
        this.setLocationRelativeTo(null);
        txtMaCTKM.setInputVerifier(new InputVerifier() {
            @Override
            public boolean verify(JComponent input) {
                String ma = txtMaCTKM.getText().trim();
                if (!ma.matches("^KM\\d{2}$")) {
                    JOptionPane.showMessageDialog(CTrinhKMDialog.this, "Mã phải có định dạng KMxx (x là số)");
                    return false;
                }
                if (bus.timCTrinhKM(ma) != null) {
                    JOptionPane.showMessageDialog(CTrinhKMDialog.this, "Mã đã tồn tại");
                    return false;
                }
                return true;
            }
        });
        txtTenctkm.setInputVerifier(new InputVerifier() {
            @Override
            public boolean verify(JComponent input){
                String ten=txtTenctkm.getText().trim();
                if(ten.isEmpty()){
                    JOptionPane.showMessageDialog(CTrinhKMDialog.this, "Tên chương trình khuyến mãi không được để trống");
                    return false;
                }
                return true;
            }
        });

        txtChietkhau.setInputVerifier(new InputVerifier() {
            @Override
            public boolean verify(JComponent input){
                String chietkhau=txtChietkhau.getText().trim();
                try{
                    double ck=Double.parseDouble(chietkhau);
                    if(ck<0||ck>100){
                        JOptionPane.showMessageDialog(CTrinhKMDialog.this, "Chiết khấu phải là số từ 0 đến 100");
                        return false;
                    }
                }catch(NumberFormatException e){
                    JOptionPane.showMessageDialog(CTrinhKMDialog.this, "Chiết khấu phải là số hợp lệ");
                    return false;
                }
                return true;
            }
        });
    }

    public CTrinhKMDialog(CTrinhKMBUS bus, CTrinhKMDTO ct) {
        initComponents();
        card = (CardLayout) pnlSwitch.getLayout();
        this.setTitle("Chương trình khuyến mãi");
        this.setLocationRelativeTo(null);
        this.bus = bus;
        isEdit = true;
        txtMaCTKM.setText(ct.getMaKM());
        txtMaCTKM.setEnabled(false);   // khóa sửa mã
        txtTenctkm.setText(ct.getTenKM());
        txtNgayBD.setDate(ct.getNgayBD() != null ? java.sql.Date.valueOf(ct.getNgayBD()) : null);
        txtNgayKT.setDate(ct.getNgayKT() != null ? java.sql.Date.valueOf(ct.getNgayKT()) : null);
        if (ct.getHinhThucKM()) {          // KMHD
            rdoHd.setSelected(true);
            card.show(pnlSwitch, "card2");
            if (ct instanceof KMHDDTO) {
                txtDieukienapdung.setText(String.valueOf(((KMHDDTO) ct).getTongTienApDung()));
            }
        } else {                           // KMTour
            rdoTour.setSelected(true);
            loadTourTable();
            card.show(pnlSwitch, "card3");
            if(ct instanceof KMTourDTO){

                KMTourDTO km = (KMTourDTO) ct;
                setSelectedTour(km.getDsMaTour());
            }
        }
        txtChietkhau.setText(String.valueOf(ct.getChietKhau()));
        txtGhichu.setText(ct.getGhiChu());
        txtMaCTKM.setInputVerifier(new InputVerifier() {
            @Override
            public boolean verify(JComponent input){
                String ma=txtMaCTKM.getText().trim();
                if(!ma.matches("^KM\\d{2}$")|| bus.timCTrinhKM(ma)==null){
                    JOptionPane.showMessageDialog(CTrinhKMDialog.this, "Mã chương trình khuyến mãi phải có định dạng KMxx (x là số) và không được trùng");
                    return false;
                }
                return true;
            }
        });
        txtTenctkm.setInputVerifier(new InputVerifier() {
            @Override
            public boolean verify(JComponent input){
                String ten=txtTenctkm.getText().trim();
                if(ten.isEmpty()){
                    JOptionPane.showMessageDialog(CTrinhKMDialog.this, "Tên chương trình khuyến mãi không được để trống");
                    return false;
                }
                return true;
            }
        });

        txtChietkhau.setInputVerifier(new InputVerifier() {
            @Override
            public boolean verify(JComponent input){
                String chietkhau=txtChietkhau.getText().trim();
                try{
                    double ck=Double.parseDouble(chietkhau);
                    if(ck<0||ck>100){
                        JOptionPane.showMessageDialog(CTrinhKMDialog.this, "Chiết khấu phải là số từ 0 đến 100");
                        return false;
                    }
                }catch(NumberFormatException e){
                    JOptionPane.showMessageDialog(CTrinhKMDialog.this, "Chiết khấu phải là số hợp lệ");
                    return false;
                }
                return true;
            }
        });
    }

    private JButton createBtn(String text, Color color){
        JButton btn = new JButton(text);
        btn.setBackground(color);
        btn.setForeground(Color.WHITE);
        btn.setFocusPainted(false);
        btn.setCursor(new Cursor(Cursor.HAND_CURSOR));// Trong jpBtn panel

        return btn;
    }

    private void handleSave(){
        btnLuu = createBtn("Lưu", UIColors.SAVE);
        btnLuu.addActionListener(v -> {
            try {
                String ma = txtMaCTKM.getText().trim();
                String ten = txtTenctkm.getText().trim();
                Date ngaybd = txtNgayBD.getDate();
                Date ngaykt = txtNgayKT.getDate();
                LocalDate ngayBD = DateHelper.toLocalDateFromUtil(ngaybd);
                LocalDate ngayKT = DateHelper.toLocalDateFromUtil(ngaykt);
                LocalDate today = LocalDate.now();

                if (ngaybd == null || ngaykt == null) {
                    JOptionPane.showMessageDialog(this, "Vui lòng chọn đầy đủ ngày bắt đầu và ngày kết thúc");
                    return;
                }

                if (ngayBD.isBefore(today)) {
                    JOptionPane.showMessageDialog(this, "Ngày bắt đầu phải lớn hơn hoặc bằng hôm nay");
                    return;
                }

                if (!ngayKT.isAfter(ngayBD)) {
                    JOptionPane.showMessageDialog(this, "Ngày kết thúc phải sau ngày bắt đầu");
                    return;
                }
                float chietkhau = Float.parseFloat(txtChietkhau.getText().trim());
                String ghichu = txtGhichu.getText().trim();

                CTrinhKMDTO ct;

                if (rdoTour.isSelected()) {
                    ArrayList<String> dsTour = getSelectedTour();
                    ct = new KMTourDTO(
                            ma, ten, ngayBD, ngayKT, false, chietkhau, ghichu, dsTour
                    );
                } else { // KM Hóa đơn
                    float dieuKien = Float.parseFloat(txtDieukienapdung.getText().trim());
                    ct = new KMHDDTO(
                            ma, ten, ngayBD, ngayKT, true, chietkhau, ghichu,  dieuKien
                    );
                }

                if (isEdit) {
                    if (bus.suaCTrinhKM(ct)) {
                        JOptionPane.showMessageDialog(this, "Sửa thành công");
                    } else {
                        JOptionPane.showMessageDialog(this, "Sửa thất bại");
                        return;
                    }
                } else {
                    if (bus.themCTrinhKM(ct)) {
                        JOptionPane.showMessageDialog(this, "Thêm thành công");
                    } else {
                        JOptionPane.showMessageDialog(this, "Thêm thất bại (có thể trùng mã)");
                        return;
                    }
                }

                dispose();
            } catch (NumberFormatException e) {
                JOptionPane.showMessageDialog(this, "Vui lòng nhập số hợp lệ cho chiết khấu và điều kiện áp dụng");
            } catch (Exception e) {
                e.printStackTrace();
                JOptionPane.showMessageDialog(this, "Lỗi: " + e.getMessage());
            }
        });
    }

    private void handleReset(){
        btnReset = createBtn("Làm mới", UIColors.REFRESH);
        btnReset.addActionListener(v -> {
            resetForm();
        });
    }

    private void handleCancel(){
        btnHuy = createBtn("Hủy", UIColors.CANCEL);
        btnHuy.addActionListener(v -> {
            setVisible(false);
            dispose();
        });
    }

    public void resetForm(){
        txtMaCTKM.setText("");
        txtTenctkm.setText("");
        txtNgayBD.setDate(new Date());
        txtNgayKT.setDate(new Date());
        txtChietkhau.setText("");
        txtGhichu.setText("");
    }

    private void initComponents() {
        jLabel1 = new JLabel();
        txtMaCTKM = new JTextField();
        jLabel2 = new JLabel();
        txtTenctkm = new JTextField();
        txtNgayBD = new JDateChooser();
        jLabel3 = new JLabel();
        jLabel4 = new JLabel();
        txtNgayKT = new JDateChooser();
        jLabel5 = new JLabel();
        rdoHd = new JRadioButton();
        rdoTour = new JRadioButton();
        ButtonGroup group= new ButtonGroup();
        group.add(rdoHd);
        group.add(rdoTour);
        rdoHd.setSelected(true);
        jLabel6 = new JLabel();
        txtChietkhau = new JTextField();
        jLabel7 = new JLabel();
        txtGhichu = new JTextField();
        btnLuu = new JButton();
        btnReset = new JButton();
        btnHuy = new JButton();
        pnlSwitch = new JPanel();
        HOADON = new JPanel();
        jLabel8 = new JLabel();
        txtDieukienapdung = new JTextField();
        TOUR = new JPanel();
        jLabel9 = new JLabel();
        jScrollPane1 = new JScrollPane();
        tblTour = new JTable();

        addWindowListener(new WindowAdapter() {
            public void windowClosing(WindowEvent evt) {
                closeDialog(evt);
            }
        });

        jLabel1.setText("Mã chương trình khuyến mãi");

        txtMaCTKM.addActionListener(this::txtMaCTKMActionPerformed);

        jLabel2.setText("Tên chương trình khuyến mãi");

        txtTenctkm.addActionListener(this::txtTenctkmActionPerformed);

        jLabel3.setText("Ngày bắt đầu");

        jLabel4.setText("Ngày kết thúc");

        jLabel5.setText("Hình thức khuyến mãi");

        rdoHd.setText("Khuyến mãi hóa đơn");
        rdoHd.addActionListener(this::rdoHdActionPerformed);

        rdoTour.setText("Khuyến mãi tour");
        rdoTour.addActionListener(this::rdoTourActionPerformed);

        jLabel6.setText("Chiết khấu");

        txtChietkhau.addActionListener(this::txtChietkhauActionPerformed);

        jLabel7.setText("Ghi chú");

        txtGhichu.addActionListener(this::txtGhichuActionPerformed);


        // define function
        handleSave();
        handleReset();
        handleCancel();

        pnlSwitch.setLayout(new CardLayout());

        jLabel8.setFont(new Font("Segoe UI", 0, 14)); // NOI18N
        jLabel8.setText("Tổng tiền áp dụng tối thiểu:");

        txtDieukienapdung.addActionListener(this::txtDieukienapdungActionPerformed);

        GroupLayout HOADONLayout = new GroupLayout(HOADON);
        HOADON.setLayout(HOADONLayout);
        HOADONLayout.setHorizontalGroup(
                HOADONLayout.createParallelGroup(GroupLayout.Alignment.LEADING)
                        .addGroup(HOADONLayout.createSequentialGroup()
                                .addGroup(HOADONLayout.createParallelGroup(GroupLayout.Alignment.LEADING)
                                        .addGroup(HOADONLayout.createSequentialGroup()
                                                .addGap(18, 18, 18)
                                                .addComponent(jLabel8, GroupLayout.PREFERRED_SIZE, 197, GroupLayout.PREFERRED_SIZE))
                                        .addGroup(HOADONLayout.createSequentialGroup()
                                                .addGap(45, 45, 45)
                                                .addComponent(txtDieukienapdung, GroupLayout.PREFERRED_SIZE, 378, GroupLayout.PREFERRED_SIZE)))
                                .addContainerGap(113, Short.MAX_VALUE))
        );
        HOADONLayout.setVerticalGroup(
                HOADONLayout.createParallelGroup(GroupLayout.Alignment.LEADING)
                        .addGroup(HOADONLayout.createSequentialGroup()
                                .addContainerGap()
                                .addComponent(jLabel8, GroupLayout.PREFERRED_SIZE, 40, GroupLayout.PREFERRED_SIZE)
                                .addGap(18, 18, 18)
                                .addComponent(txtDieukienapdung, GroupLayout.PREFERRED_SIZE, 44, GroupLayout.PREFERRED_SIZE)
                                .addContainerGap(135, Short.MAX_VALUE))
        );

        pnlSwitch.add(HOADON, "card2");

        jLabel9.setFont(new Font("Segoe UI", 0, 14)); // NOI18N
        jLabel9.setText("Danh sách tour áp dụng");

        tblTour.setModel(new DefaultTableModel(
                new Object [][] {

                },
                new String [] {
                        "Chọn", "Mã Tour", "Tên Tour", "Số ngày", "Đơn giá"
                }
        ) {
            Class[] types = new Class [] {
                    java.lang.Boolean.class, java.lang.String.class, java.lang.Object.class, java.lang.Integer.class, java.lang.Long.class
            };

            public Class getColumnClass(int columnIndex) {
                return types [columnIndex];
            }
        });
        jScrollPane1.setViewportView(tblTour);

        GroupLayout TOURLayout = new GroupLayout(TOUR);
        TOUR.setLayout(TOURLayout);
        TOURLayout.setHorizontalGroup(
                TOURLayout.createParallelGroup(GroupLayout.Alignment.LEADING)
                        .addComponent(jScrollPane1, GroupLayout.DEFAULT_SIZE, 536, Short.MAX_VALUE)
                        .addGroup(TOURLayout.createSequentialGroup()
                                .addComponent(jLabel9, GroupLayout.PREFERRED_SIZE, 298, GroupLayout.PREFERRED_SIZE)
                                .addGap(0, 0, Short.MAX_VALUE))
        );
        TOURLayout.setVerticalGroup(
                TOURLayout.createParallelGroup(GroupLayout.Alignment.LEADING)
                        .addGroup(GroupLayout.Alignment.TRAILING, TOURLayout.createSequentialGroup()
                                .addContainerGap()
                                .addComponent(jLabel9, GroupLayout.DEFAULT_SIZE, 24, Short.MAX_VALUE)
                                .addPreferredGap(LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(jScrollPane1, GroupLayout.PREFERRED_SIZE, 207, GroupLayout.PREFERRED_SIZE))
        );

        pnlSwitch.add(TOUR, "card3");

        GroupLayout layout = new GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
                layout.createParallelGroup(GroupLayout.Alignment.LEADING)
                        .addGroup(layout.createSequentialGroup()
                                .addGroup(layout.createParallelGroup(GroupLayout.Alignment.LEADING)
                                        .addGroup(layout.createSequentialGroup()
                                                .addContainerGap()
                                                .addGroup(layout.createParallelGroup(GroupLayout.Alignment.LEADING)
                                                        .addGroup(layout.createParallelGroup(GroupLayout.Alignment.TRAILING, false)
                                                                .addComponent(jLabel4, GroupLayout.DEFAULT_SIZE, GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                                                .addComponent(jLabel3, GroupLayout.DEFAULT_SIZE, GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                                                .addComponent(jLabel1, GroupLayout.Alignment.LEADING, GroupLayout.PREFERRED_SIZE, 154, GroupLayout.PREFERRED_SIZE))
                                                        .addComponent(jLabel2))
                                                .addGap(38, 38, 38)
                                                .addGroup(layout.createParallelGroup(GroupLayout.Alignment.LEADING, false)
                                                        .addComponent(txtTenctkm, GroupLayout.PREFERRED_SIZE, 235, GroupLayout.PREFERRED_SIZE)
                                                        .addComponent(txtNgayKT, GroupLayout.PREFERRED_SIZE, 146, GroupLayout.PREFERRED_SIZE)
                                                        .addComponent(txtNgayBD, GroupLayout.PREFERRED_SIZE, 149, GroupLayout.PREFERRED_SIZE)
                                                        .addComponent(txtMaCTKM, GroupLayout.PREFERRED_SIZE, 235, GroupLayout.PREFERRED_SIZE)))
                                        .addGroup(layout.createSequentialGroup()
                                                .addGap(48, 48, 48)
                                                .addComponent(rdoHd, GroupLayout.PREFERRED_SIZE, 155, GroupLayout.PREFERRED_SIZE)
                                                .addGap(50, 50, 50)
                                                .addComponent(rdoTour, GroupLayout.PREFERRED_SIZE, 156, GroupLayout.PREFERRED_SIZE))
                                        .addGroup(layout.createSequentialGroup()
                                                .addContainerGap()
                                                .addComponent(jLabel5, GroupLayout.PREFERRED_SIZE, 151, GroupLayout.PREFERRED_SIZE))
                                        .addGroup(layout.createSequentialGroup()
                                                .addContainerGap()
                                                .addGroup(layout.createParallelGroup(GroupLayout.Alignment.LEADING)
                                                        .addComponent(jLabel6, GroupLayout.PREFERRED_SIZE, 163, GroupLayout.PREFERRED_SIZE)
                                                        .addComponent(jLabel7, GroupLayout.PREFERRED_SIZE, 163, GroupLayout.PREFERRED_SIZE))
                                                .addGap(29, 29, 29)
                                                .addGroup(layout.createParallelGroup(GroupLayout.Alignment.LEADING)
                                                        .addComponent(txtGhichu, GroupLayout.PREFERRED_SIZE, 235, GroupLayout.PREFERRED_SIZE)
                                                        .addComponent(txtChietkhau, GroupLayout.PREFERRED_SIZE, 235, GroupLayout.PREFERRED_SIZE))))
                                .addContainerGap(GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                        .addGroup(layout.createSequentialGroup()
                                .addGap(29, 29, 29)
                                .addComponent(btnLuu)
                                .addPreferredGap(LayoutStyle.ComponentPlacement.RELATED, GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                .addComponent(btnReset)
                                .addGap(126, 126, 126)
                                .addComponent(btnHuy)
                                .addGap(32, 32, 32))
                        .addComponent(pnlSwitch, GroupLayout.DEFAULT_SIZE, GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );
        layout.setVerticalGroup(
                layout.createParallelGroup(GroupLayout.Alignment.LEADING)
                        .addGroup(layout.createSequentialGroup()
                                .addGap(28, 28, 28)
                                .addGroup(layout.createParallelGroup(GroupLayout.Alignment.BASELINE)
                                        .addComponent(jLabel1, GroupLayout.PREFERRED_SIZE, 25, GroupLayout.PREFERRED_SIZE)
                                        .addComponent(txtMaCTKM, GroupLayout.PREFERRED_SIZE, 25, GroupLayout.PREFERRED_SIZE))
                                .addGap(18, 18, 18)
                                .addGroup(layout.createParallelGroup(GroupLayout.Alignment.BASELINE)
                                        .addComponent(jLabel2, GroupLayout.PREFERRED_SIZE, 25, GroupLayout.PREFERRED_SIZE)
                                        .addComponent(txtTenctkm, GroupLayout.PREFERRED_SIZE, 25, GroupLayout.PREFERRED_SIZE))
                                .addGap(18, 18, 18)
                                .addGroup(layout.createParallelGroup(GroupLayout.Alignment.BASELINE)
                                        .addComponent(jLabel3, GroupLayout.PREFERRED_SIZE, 25, GroupLayout.PREFERRED_SIZE)
                                        .addComponent(txtNgayBD, GroupLayout.PREFERRED_SIZE, 25, GroupLayout.PREFERRED_SIZE))
                                .addGap(18, 18, 18)
                                .addGroup(layout.createParallelGroup(GroupLayout.Alignment.BASELINE)
                                        .addComponent(jLabel4, GroupLayout.PREFERRED_SIZE, 25, GroupLayout.PREFERRED_SIZE)
                                        .addComponent(txtNgayKT, GroupLayout.PREFERRED_SIZE, 25, GroupLayout.PREFERRED_SIZE))
                                .addGroup(layout.createParallelGroup(GroupLayout.Alignment.LEADING)
                                        .addGroup(layout.createSequentialGroup()
                                                .addGap(18, 18, 18)
                                                .addComponent(jLabel5, GroupLayout.PREFERRED_SIZE, 25, GroupLayout.PREFERRED_SIZE)
                                                .addGap(18, 18, 18)
                                                .addComponent(rdoHd, GroupLayout.PREFERRED_SIZE, 32, GroupLayout.PREFERRED_SIZE))
                                        .addGroup(GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                                                .addGap(55, 55, 55)
                                                .addComponent(rdoTour, GroupLayout.PREFERRED_SIZE, 38, GroupLayout.PREFERRED_SIZE)))
                                .addGap(18, 18, 18)
                                .addGroup(layout.createParallelGroup(GroupLayout.Alignment.BASELINE)
                                        .addComponent(jLabel6, GroupLayout.PREFERRED_SIZE, 25, GroupLayout.PREFERRED_SIZE)
                                        .addComponent(txtChietkhau, GroupLayout.PREFERRED_SIZE, 25, GroupLayout.PREFERRED_SIZE))
                                .addGap(18, 18, 18)
                                .addGroup(layout.createParallelGroup(GroupLayout.Alignment.BASELINE)
                                        .addComponent(jLabel7, GroupLayout.PREFERRED_SIZE, 27, GroupLayout.PREFERRED_SIZE)
                                        .addComponent(txtGhichu, GroupLayout.PREFERRED_SIZE, 27, GroupLayout.PREFERRED_SIZE))
                                .addPreferredGap(LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(pnlSwitch, GroupLayout.DEFAULT_SIZE, GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                .addPreferredGap(LayoutStyle.ComponentPlacement.RELATED)
                                .addGroup(layout.createParallelGroup(GroupLayout.Alignment.BASELINE)
                                        .addComponent(btnLuu)
                                        .addComponent(btnReset)
                                        .addComponent(btnHuy))
                                .addContainerGap())
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    public void loadTourTable() {
        DefaultTableModel model = (DefaultTableModel) tblTour.getModel();
        model.setRowCount(0);

        TourDAO dao = new TourDAO();
        ArrayList<TourDTO> list = dao.getAllTours();

        for (TourDTO t : list) {
            model.addRow(new Object[]{
                    false,                 // checkbox chọn tour
                    t.getMaTour(),
                    t.getTen(),
                    t.getSoNgay(),
                    t.getDonGia()
            });
        }
    }
    public ArrayList<String> getSelectedTour(){
        ArrayList<String> list = new ArrayList<>();
        for(int i = 0; i < tblTour.getRowCount(); i++){

            Boolean checked = (Boolean) tblTour.getValueAt(i,0);

            if(checked != null && checked){
                list.add(tblTour.getValueAt(i,1).toString());
            }
        }

        return list;
    }

    public void setSelectedTour(ArrayList<String> dsTour){
        for(int i=0;i<tblTour.getRowCount();i++){

            String maTour = tblTour.getValueAt(i,1).toString();

            if(dsTour.contains(maTour)){
                tblTour.setValueAt(true,i,0);
            }
        }
    }

    private void closeDialog(WindowEvent evt) {//GEN-FIRST:event_closeDialog
        setVisible(false);
        dispose();
    }//GEN-LAST:event_closeDialog

    private void txtMaCTKMActionPerformed(ActionEvent evt) {//GEN-FIRST:event_txtMaCTKMActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_txtMaCTKMActionPerformed

    private void txtTenctkmActionPerformed(ActionEvent evt) {//GEN-FIRST:event_txtTenctkmActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_txtTenctkmActionPerformed

    private void txtNgayBDActionPerformed(ActionEvent evt) {//GEN-FIRST:event_txtNgayBDActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_txtNgayBDActionPerformed

    private void txtNgayKTActionPerformed(ActionEvent evt) {//GEN-FIRST:event_txtNgayKTActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_txtNgayKTActionPerformed

    private void txtChietkhauActionPerformed(ActionEvent evt) {//GEN-FIRST:event_txtChietkhauActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_txtChietkhauActionPerformed

    private void txtGhichuActionPerformed(ActionEvent evt) {//GEN-FIRST:event_txtGhichuActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_txtGhichuActionPerformed

    private void txtMaCTKMInputMethodTextChanged(InputMethodEvent evt) {//GEN-FIRST:event_txtMaCTKMInputMethodTextChanged
        // TODO add your handling code here:
    }//GEN-LAST:event_txtMaCTKMInputMethodTextChanged

    private void txtTenctkmInputMethodTextChanged(InputMethodEvent evt) {//GEN-FIRST:event_txtTenctkmInputMethodTextChanged
        // TODO add your handling code here:
    }//GEN-LAST:event_txtTenctkmInputMethodTextChanged

    private void txtNgayBDInputMethodTextChanged(InputMethodEvent evt) {//GEN-FIRST:event_txtNgayBDInputMethodTextChanged
        // TODO add your handling code here:
    }//GEN-LAST:event_txtNgayBDInputMethodTextChanged

    private void txtNgayKTInputMethodTextChanged(InputMethodEvent evt) {//GEN-FIRST:event_txtNgayKTInputMethodTextChanged
        // TODO add your handling code here:
    }//GEN-LAST:event_txtNgayKTInputMethodTextChanged

    private void txtChietkhauInputMethodTextChanged(InputMethodEvent evt) {//GEN-FIRST:event_txtChietkhauInputMethodTextChanged
        // TODO add your handling code here:
    }//GEN-LAST:event_txtChietkhauInputMethodTextChanged

    private void txtGhichuInputMethodTextChanged(InputMethodEvent evt) {//GEN-FIRST:event_txtGhichuInputMethodTextChanged
        // TODO add your handling code here:
    }//GEN-LAST:event_txtGhichuInputMethodTextChanged

    private void jRadioButton1ActionPerformed(ActionEvent evt) {
        // TODO add your handling code here:
    }

    private void rdoHdActionPerformed(ActionEvent evt) {//GEN-FIRST:event_rdoHdActionPerformed
        // TODO add your handling code here:
        rdoHd.setSelected(true);
        card.show(pnlSwitch, "card2");
        JOptionPane.showMessageDialog(this, "Bạn đã chọn hình thức khuyến mãi hóa đơn");
    }//GEN-LAST:event_rdoHdActionPerformed

    private void rdoTourActionPerformed(ActionEvent evt) {//GEN-FIRST:event_rdoTourActionPerformed
        // TODO add your handling code here:
        rdoTour.setSelected(true);
        card.show(pnlSwitch, "card3");
        loadTourTable();
        JOptionPane.showMessageDialog(this, "Bạn đã chọn hình thức khuyến mãi tour");

    }//GEN-LAST:event_rdoTourActionPerformed

    private void btnHinhthucStateChanged(ChangeEvent evt) {//GEN-FIRST:event_btnHinhthucStateChanged
        // TODO add your handling code here:
    }//GEN-LAST:event_btnHinhthucStateChanged

    private void txtDieukienapdungActionPerformed(ActionEvent evt) {//GEN-FIRST:event_txtDieukienapdungActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_txtDieukienapdungActionPerformed

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private JPanel HOADON, pnlSwitch, TOUR;

    private JButton btnHuy, btnLuu, btnReset;

    private JLabel jLabel1, jLabel2, jLabel3, jLabel4, jLabel5, jLabel6, jLabel7, jLabel8, jLabel9;

    private JScrollPane jScrollPane1;

    private JRadioButton rdoHd, rdoTour;

    private JTable tblTour;

    private JTextField txtChietkhau, txtDieukienapdung, txtGhichu,txtMaCTKM, txtTenctkm;

    private JDateChooser txtNgayBD, txtNgayKT;
}