
package org.example.gui.dialog;

import java.awt.*;
import java.awt.event.*;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import lombok.AccessLevel;
import lombok.experimental.FieldDefaults;
import org.example.bus.*;
import org.example.dto.*;
import org.example.gui.panel.UIColors;

import javax.swing.*;

@FieldDefaults(level = AccessLevel.PRIVATE)
public class InvoiceDetailPanel extends JDialog {
    InvoiceDetailBUS bus;
    InvoiceBUS invoiceBUS;
    CustomerBUS customerBUS;

    JButton saveBtn, refreshBtn;

    JComboBox<String> cbmahd;
    JComboBox<String> cbmakh;

    JLabel lbPrice, lbIdInvoice, lbIdStaff;

    JTextField txtPrice;

    public InvoiceDetailPanel(InvoiceBUS invoiceBUS , CustomerBUS customerBUS) {
        this.invoiceBUS = invoiceBUS;
        this.customerBUS = customerBUS;

        initComponents();
        loadCbox();
        this.setTitle("Chi tiết hóa đơn");
        this.bus=new InvoiceDetailBUS();
        this.setLocationRelativeTo(null);
    }

    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {
        lbIdInvoice = new JLabel();
        lbIdStaff = new JLabel();
        lbPrice = new JLabel();
        txtPrice = new JTextField();
        saveBtn = new JButton();
        refreshBtn = new JButton();
        cbmahd = new JComboBox<>();
        cbmakh = new JComboBox<>();

        setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);

        lbIdInvoice.setText("Mã hóa đơn");

        lbIdStaff.setText("Mã khách hàng đi");

        lbPrice.setText("Giá vé");

        txtPrice.setToolTipText("");
        txtPrice.setEnabled(false);
        txtPrice.addFocusListener(new FocusAdapter() {
            public void focusLost(FocusEvent evt) {
                txtPriceFocusLost(evt);
            }
        });
        txtPrice.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent evt) {
                txtPriceActionPerformed(evt);
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
                                .addComponent(saveBtn, GroupLayout.PREFERRED_SIZE, 75, GroupLayout.PREFERRED_SIZE)
                                .addGroup(layout.createParallelGroup(GroupLayout.Alignment.LEADING)
                                        .addGroup(layout.createSequentialGroup()
                                                .addGap(59, 59, 59)
                                                .addComponent(refreshBtn))
                                        .addGroup(layout.createSequentialGroup()
                                                .addGap(41, 41, 41)
                                                .addGroup(layout.createParallelGroup(GroupLayout.Alignment.LEADING, false)
                                                        .addComponent(cbmakh, 0, GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                                        .addComponent(cbmahd, 0, GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                                        .addComponent(txtPrice, GroupLayout.DEFAULT_SIZE, 112, Short.MAX_VALUE))))
                                .addContainerGap(11, Short.MAX_VALUE))
                        .addGroup(layout.createParallelGroup(GroupLayout.Alignment.LEADING)
                                .addGroup(layout.createSequentialGroup()
                                        .addGap(21, 21, 21)
                                        .addGroup(layout.createParallelGroup(GroupLayout.Alignment.LEADING)
                                                .addComponent(lbIdInvoice, GroupLayout.PREFERRED_SIZE, 101, GroupLayout.PREFERRED_SIZE)
                                                .addComponent(lbIdStaff, GroupLayout.PREFERRED_SIZE, 117, GroupLayout.PREFERRED_SIZE)
                                                .addComponent(lbPrice, GroupLayout.PREFERRED_SIZE, 64, GroupLayout.PREFERRED_SIZE))
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
                                .addComponent(txtPrice, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)
                                .addGap(18, 18, 18)
                                .addGroup(layout.createParallelGroup(GroupLayout.Alignment.BASELINE)
                                        .addComponent(saveBtn)
                                        .addComponent(refreshBtn))
                                .addGap(47, 47, 47))
                        .addGroup(layout.createParallelGroup(GroupLayout.Alignment.LEADING)
                                .addGroup(layout.createSequentialGroup()
                                        .addGap(94, 94, 94)
                                        .addComponent(lbIdInvoice)
                                        .addPreferredGap(LayoutStyle.ComponentPlacement.UNRELATED)
                                        .addComponent(lbIdStaff)
                                        .addPreferredGap(LayoutStyle.ComponentPlacement.RELATED)
                                        .addComponent(lbPrice)
                                        .addContainerGap(101, Short.MAX_VALUE)))
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents



    public void loadCbox(){
        this.invoiceBUS =new InvoiceBUS();
        this.bus =new InvoiceDetailBUS();
        ArrayList<CustomerDTO> dskh = customerBUS.getAllCustomers();
        ArrayList<InvoiceDTO> dshd = invoiceBUS.getDs();
        List<String> dsMa = new ArrayList<>();

        for(InvoiceDTO hd: dshd){
            dsMa.add(hd.getIdInvoice());
        }
        setupAutoComplete(cbmahd, dsMa);
        dsMa=new ArrayList<>();
        for(CustomerDTO kh:dskh){
            dsMa.add(kh.getIdCustomer());
        }
        setupAutoComplete(cbmakh, dsMa);
    }

    public void loadCbox(InvoiceDetailDTO ct){
        this.invoiceBUS =new InvoiceBUS();
        this.bus =new InvoiceDetailBUS();
        ArrayList<CustomerDTO> dskh = customerBUS.getAllCustomers();
        ArrayList<InvoiceDTO> dshd = invoiceBUS.getDs();
        List<String> dsMa = new ArrayList<>();

        for(InvoiceDTO hd: dshd){
            dsMa.add(hd.getIdInvoice());
        }
        setupAutoComplete(cbmahd, dsMa);
        dsMa=new ArrayList<>();
        for(CustomerDTO kh:dskh){
            dsMa.add(kh.getIdCustomer());
        }
        setupAutoComplete(cbmakh, dsMa);
        cbmahd.setSelectedItem(ct.getIdInvoice());
        cbmakh.setSelectedItem(ct.getIdCustomer());
        txtPrice.setText(String.format("%.0f", bus.getPrice(ct.getIdInvoice())));

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
        txtPrice.setText("");
    }

    private void txtPriceActionPerformed(ActionEvent evt) {

    }

    private void saveBtnActionPerformed(ActionEvent evt) {


    }

    private void txtPriceFocusLost(FocusEvent evt) {
        // TODO add your handling code here:

    }

    private void refreshBtnActionPerformed(ActionEvent evt) {
        // TODO add your handling code here:
    }

    private void cbmakhActionPerformed(ActionEvent evt) {
        // TODO add your handling code here:
    }

    private void cbmahdFocusLost(FocusEvent evt) {
        // TODO add your handling code here:
        String mahd =cbmahd.getSelectedItem().toString().trim();
        System.out.println(mahd);
        txtPrice.setText(String.format("%.0f", bus.getPrice(mahd)));
    }

    private void cbmahdItemStateChanged(ItemEvent evt) {
        // TODO add your handling code here:
        String mahd = cbmahd.getSelectedItem().toString().trim();
        txtPrice.setText(String.format("%.0f", bus.getPrice(mahd)));
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
        saveBtn = createBtn("Lưu", UIColors.SAVE);
        saveBtn.addActionListener(v -> {
            try{
                String ma=cbmahd.getSelectedItem().toString().trim();
                String makh=cbmakh.getSelectedItem().toString().trim();
                if(ma.isEmpty()){
                    JOptionPane.showMessageDialog(this, "Lỗi chưa nhập mã hóa đơn");
                    return;
                }
                InvoiceDetailDTO cthd=new InvoiceDetailDTO(ma, makh, BigDecimal.valueOf(Long.parseLong(txtPrice.getText())));
                InvoiceDetailDTO kt=bus.timCt(ma,cbmahd.getSelectedItem().toString().trim());

                if(kt!=null){
                    if(bus.editInvoiceDetail(cthd)){
                        resetField();
                        JOptionPane.showMessageDialog(this, "Cập nhật chi tiết hóa đơn thành công");
                        this.dispose();
                    }
                    else{
                        JOptionPane.showMessageDialog(this, "Cập nhật thất bại");
                    }
                }else{
                    ArrayList<InvoiceDetailDTO> ds=new ArrayList<>();
                    if(bus.addInvoiceDetail(cthd)){
                        resetField();
                        JOptionPane.showMessageDialog(this, "Thêm thành công");
                        this.dispose();
                    }else{
                        JOptionPane.showMessageDialog(this, "Thêm thất bại");
                    }
                }
            }catch(Exception e){
                e.printStackTrace();
                JOptionPane.showMessageDialog(this, "Lỗi");
            }
        });
    }

    private void handleRefresh(){
        refreshBtn = createBtn("Làm mới", UIColors.REFRESH);
        refreshBtn.addActionListener(v -> {
            resetField();
        });
    }
}