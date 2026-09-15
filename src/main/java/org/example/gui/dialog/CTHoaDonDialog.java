
package org.example.gui.dialog;

import java.awt.*;
import java.awt.event.*;
import java.util.ArrayList;
import java.util.List;
import org.example.bus.*;
import org.example.dto.*;
import org.example.gui.panel.UIColors;

import javax.swing.*;

public class CTHoaDonDialog extends JDialog {
    private CTHoaDonBUS bus;
    private HoaDonBUS hdbus;
    private KhachHangBUS khbus;

    public CTHoaDonDialog() {
        initComponents();
        loadCbox();
        this.setTitle("Chi tiết hóa đơn");
        this.bus=new CTHoaDonBUS();
        this.setLocationRelativeTo(null);
    }

    public CTHoaDonDialog(CTietHDDTO ct) {
        initComponents();
        loadCbox(ct);
        this.setTitle("Chi tiết hóa đơn");
        this.bus=new CTHoaDonBUS();
        this.setLocationRelativeTo(null);
    }

    public void loadCbox(){
        this.hdbus=new HoaDonBUS();
        this.khbus =new KhachHangBUS();
        this.bus =new CTHoaDonBUS();
        ArrayList<KhachHangDTO> dskh =KhachHangBUS.dsKH;
        ArrayList<HoaDonDTO> dshd =HoaDonBUS.ds;
        List<String> dsMa = new ArrayList<>();

        for(HoaDonDTO hd: dshd){
            dsMa.add(hd.getMaHD());
        }
        setupAutoComplete(cbmahd, dsMa);
        dsMa=new ArrayList<>();
        for(KhachHangDTO kh:dskh){
            dsMa.add(kh.getMaKH());
        }
        setupAutoComplete(cbmakh, dsMa);
    }

    public void loadCbox(CTietHDDTO ct){
        this.hdbus=new HoaDonBUS();
        this.khbus =new KhachHangBUS();
        this.bus =new CTHoaDonBUS();
        ArrayList<KhachHangDTO> dskh =KhachHangBUS.dsKH;
        ArrayList<HoaDonDTO> dshd =HoaDonBUS.ds;
        List<String> dsMa = new ArrayList<>();

        for(HoaDonDTO hd: dshd){
            dsMa.add(hd.getMaHD());
        }
        setupAutoComplete(cbmahd, dsMa);
        dsMa=new ArrayList<>();
        for(KhachHangDTO kh:dskh){
            dsMa.add(kh.getMaKH());
        }
        setupAutoComplete(cbmakh, dsMa);
        cbmahd.setSelectedItem(ct.getMaHD());
        cbmakh.setSelectedItem(ct.getMaKHDi());
        txtgiave.setText(String.format("%.0f", bus.layGia(ct.getMaHD())));

    }

    public void setupAutoComplete(JComboBox<String> cbx, List<String> data) {
        cbx.setEditable(true);
        cbx.setModel(new DefaultComboBoxModel<>(data.toArray(new String[0])));
        JTextField txt = (JTextField) cbx.getEditor().getEditorComponent();

        txt.addKeyListener(new KeyAdapter() {
            @Override
            public void keyReleased(KeyEvent e) {
                int k = e.getKeyCode();
                if (k == 38 || k == 40 || k == 10) return;

                SwingUtilities.invokeLater(() -> {
                    String input = txt.getText();

                    String[] filtered = data.stream()
                            .filter(s -> s.toLowerCase().contains(input.toLowerCase()))
                            .toArray(String[]::new);

                    cbx.setModel(new DefaultComboBoxModel<>(filtered));
                    txt.setText(input);
                    cbx.setPopupVisible(filtered.length > 0);
                });
            }
        });
    }

    public void resetField(){
        cbmahd.setSelectedItem("");
        cbmakh.setSelectedItem("");
        txtgiave.setText("");
    }

    private void txtgiaveActionPerformed(ActionEvent evt) {

    }

    private void btnluuActionPerformed(ActionEvent evt) {


    }

    private void txtgiaveFocusLost(FocusEvent evt) {
        // TODO add your handling code here:

    }

    private void btnresetActionPerformed(ActionEvent evt) {
        // TODO add your handling code here:
    }

    private void cbmakhActionPerformed(ActionEvent evt) {
        // TODO add your handling code here:
    }

    private void cbmahdFocusLost(FocusEvent evt) {
        // TODO add your handling code here:
        String mahd =cbmahd.getSelectedItem().toString().trim();
        System.out.println(mahd);
        txtgiave.setText(String.format("%.0f", bus.layGia(mahd)));
    }

    private void cbmahdItemStateChanged(ItemEvent evt) {
        // TODO add your handling code here:
        String mahd = cbmahd.getSelectedItem().toString().trim();
        txtgiave.setText(String.format("%.0f", bus.layGia(mahd)));
    }

    private JButton btnluu;
    private JButton btnreset;
    private JComboBox<String> cbmahd;
    private JComboBox<String> cbmakh;
    private JLabel lbgiave;
    private JLabel lbmahd;
    private JLabel lbmakh;
    private JTextField txtgiave;

    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {
        lbmahd = new JLabel();
        lbmakh = new JLabel();
        lbgiave = new JLabel();
        txtgiave = new JTextField();
        btnluu = new JButton();
        btnreset = new JButton();
        cbmahd = new JComboBox<>();
        cbmakh = new JComboBox<>();

        setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);

        lbmahd.setText("Mã hóa đơn");

        lbmakh.setText("Mã khách hàng đi");

        lbgiave.setText("Giá vé");

        txtgiave.setToolTipText("");
        txtgiave.setEnabled(false);
        txtgiave.addFocusListener(new FocusAdapter() {
            public void focusLost(FocusEvent evt) {
                txtgiaveFocusLost(evt);
            }
        });
        txtgiave.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent evt) {
                txtgiaveActionPerformed(evt);
            }
        });

        // define handle function
        handleSave();
        handleRefresh();

        cbmahd.addItemListener(new ItemListener() {
            public void itemStateChanged(ItemEvent evt) {
                cbmahdItemStateChanged(evt);
            }
        });
        cbmahd.addFocusListener(new FocusAdapter() {
            public void focusLost(FocusEvent evt) {
                cbmahdFocusLost(evt);
            }
        });

        cbmakh.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent evt) {
                cbmakhActionPerformed(evt);
            }
        });

        GroupLayout layout = new GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
                layout.createParallelGroup(GroupLayout.Alignment.LEADING)
                        .addGroup(layout.createSequentialGroup()
                                .addGap(31, 31, 31)
                                .addComponent(btnluu, GroupLayout.PREFERRED_SIZE, 75, GroupLayout.PREFERRED_SIZE)
                                .addGroup(layout.createParallelGroup(GroupLayout.Alignment.LEADING)
                                        .addGroup(layout.createSequentialGroup()
                                                .addGap(59, 59, 59)
                                                .addComponent(btnreset))
                                        .addGroup(layout.createSequentialGroup()
                                                .addGap(41, 41, 41)
                                                .addGroup(layout.createParallelGroup(GroupLayout.Alignment.LEADING, false)
                                                        .addComponent(cbmakh, 0, GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                                        .addComponent(cbmahd, 0, GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                                        .addComponent(txtgiave, GroupLayout.DEFAULT_SIZE, 112, Short.MAX_VALUE))))
                                .addContainerGap(11, Short.MAX_VALUE))
                        .addGroup(layout.createParallelGroup(GroupLayout.Alignment.LEADING)
                                .addGroup(layout.createSequentialGroup()
                                        .addGap(21, 21, 21)
                                        .addGroup(layout.createParallelGroup(GroupLayout.Alignment.LEADING)
                                                .addComponent(lbmahd, GroupLayout.PREFERRED_SIZE, 101, GroupLayout.PREFERRED_SIZE)
                                                .addComponent(lbmakh, GroupLayout.PREFERRED_SIZE, 117, GroupLayout.PREFERRED_SIZE)
                                                .addComponent(lbgiave, GroupLayout.PREFERRED_SIZE, 64, GroupLayout.PREFERRED_SIZE))
                                        .addContainerGap(132, Short.MAX_VALUE)))
        );
        layout.setVerticalGroup(
                layout.createParallelGroup(GroupLayout.Alignment.LEADING)
                        .addGroup(GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                                .addContainerGap(94, Short.MAX_VALUE)
                                .addComponent(cbmahd, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(cbmakh, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)
                                .addGap(7, 7, 7)
                                .addComponent(txtgiave, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)
                                .addGap(18, 18, 18)
                                .addGroup(layout.createParallelGroup(GroupLayout.Alignment.BASELINE)
                                        .addComponent(btnluu)
                                        .addComponent(btnreset))
                                .addGap(47, 47, 47))
                        .addGroup(layout.createParallelGroup(GroupLayout.Alignment.LEADING)
                                .addGroup(layout.createSequentialGroup()
                                        .addGap(94, 94, 94)
                                        .addComponent(lbmahd)
                                        .addPreferredGap(LayoutStyle.ComponentPlacement.UNRELATED)
                                        .addComponent(lbmakh)
                                        .addPreferredGap(LayoutStyle.ComponentPlacement.RELATED)
                                        .addComponent(lbgiave)
                                        .addContainerGap(101, Short.MAX_VALUE)))
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private JButton createBtn(String text, Color color){
        JButton btn = new JButton(text);
        btn.setBackground(color);
        btn.setForeground(Color.WHITE);
        btn.setFocusPainted(false);
        btn.setCursor(new Cursor(Cursor.HAND_CURSOR));// Trong jpBtn panel
        return btn;
    }

    private void handleSave(){
        btnluu = createBtn("Lưu", UIColors.SAVE);
        btnluu.addActionListener(v -> {
            try{
                String ma=cbmahd.getSelectedItem().toString().trim();
                String makh=cbmakh.getSelectedItem().toString().trim();
                if(ma.isEmpty()){
                    JOptionPane.showMessageDialog(this, "Lỗi chưa nhập mã hóa đơn");
                    return;
                }
                CTietHDDTO cthd=new CTietHDDTO(ma, makh,Float.parseFloat(txtgiave.getText()));
                CTietHDDTO kt=bus.timCt(ma,cbmahd.getSelectedItem().toString().trim());

                if(kt!=null){
                    if(bus.suaCtiethd(cthd)){
                        resetField();
                        JOptionPane.showMessageDialog(this, "Cập nhật chi tiết hóa đơn thành công");
                        this.dispose();
                    }
                    else{
                        JOptionPane.showMessageDialog(this, "Cập nhật thất bại");
                        return;
                    }
                }else{
                    ArrayList<CTietHDDTO> ds=new ArrayList<>();
                    if(bus.themCTietHd(cthd)){
                        resetField();
                        JOptionPane.showMessageDialog(this, "Thêm thành công");
                        this.dispose();
                    }else{
                        JOptionPane.showMessageDialog(this, "Thêm thất bại");
                        return;
                    }
                }
            }catch(Exception e){
                e.printStackTrace();
                JOptionPane.showMessageDialog(this, "Lỗi");
            }
        });
    }

    private void handleRefresh(){
        btnreset = createBtn("Làm mới", UIColors.REFRESH);
        btnreset.addActionListener(v -> {
            resetField();
        });
    }
}