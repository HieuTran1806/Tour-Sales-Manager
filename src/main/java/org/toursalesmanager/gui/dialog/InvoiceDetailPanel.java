
package org.toursalesmanager.gui.dialog;

import java.awt.*;
import java.awt.event.*;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import lombok.AccessLevel;
import lombok.experimental.FieldDefaults;
import org.toursalesmanager.bus.*;
import org.toursalesmanager.dto.*;
import org.toursalesmanager.gui.component.ButtonFactory;
import org.toursalesmanager.gui.helper.AddComponentToForm;
import org.toursalesmanager.gui.helper.UIColors;

import javax.swing.*;

@FieldDefaults(level = AccessLevel.PRIVATE)
public class InvoiceDetailPanel extends JDialog {
    InvoiceDetailBUS bus;
    InvoiceBUS invoiceBUS;
    CustomerBUS customerBUS;

    JPanel formPanel, buttonPanel;

    JButton saveBtn, refreshBtn;

    JComboBox<String> cbmahd;
    JComboBox<String> cbmakh;

    JTextField txtPrice;

    public InvoiceDetailPanel(InvoiceBUS invoiceBUS , CustomerBUS customerBUS) {
        this.invoiceBUS = invoiceBUS;
        this.customerBUS = customerBUS;

        initComponents();

        loadData();
        this.setTitle("Chi tiết hóa đơn");
        this.bus=new InvoiceDetailBUS();
        this.setLocationRelativeTo(null);
    }

    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {
        this.setLayout(new BorderLayout());

        formPanel = new JPanel(new GridLayout(0, 2, 10, 10));
        buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 10, 10));

        cbmahd = new JComboBox<>();
        AddComponentToForm.addFormRow(formPanel, "Mã hóa đơn", cbmahd = new JComboBox<>());

        cbmakh = new JComboBox<>();
        AddComponentToForm.addFormRow(formPanel, "Mã khách hàng", cbmakh = new JComboBox<>());

        txtPrice = new JTextField();
        txtPrice.setEnabled(false);
        txtPrice.setToolTipText("");
        AddComponentToForm.addFormRow(formPanel, "Giá vé", txtPrice = new JTextField());

        // define handle function
        handleSave();
        handleRefresh();

        buttonPanel.add(saveBtn);
        buttonPanel.add(refreshBtn);

        this.add(formPanel, BorderLayout.CENTER);
        this.add(buttonPanel, BorderLayout.SOUTH);

        setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);
        pack();
    }// </editor-fold>//GEN-END:initComponents

    public void loadData(){
        this.invoiceBUS =new InvoiceBUS();
        this.bus =new InvoiceDetailBUS();
        ArrayList<CustomerDTO> dskh = customerBUS.getAllCustomers();
        ArrayList<InvoiceDTO> dshd = invoiceBUS.getAllInvoices();
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

    public void loadCombobox(InvoiceDetailDTO ct){
        ArrayList<CustomerDTO> dskh = customerBUS.getAllCustomers();
        ArrayList<InvoiceDTO> dshd = invoiceBUS.getAllInvoices();
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

    private void handleSave(){
        saveBtn = ButtonFactory.create("Lưu", UIColors.SAVE);
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
        refreshBtn = ButtonFactory.create("Làm mới", UIColors.REFRESH);
        refreshBtn.addActionListener(v -> {
            resetField();
        });
    }
}