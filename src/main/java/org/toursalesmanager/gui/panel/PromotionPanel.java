package org.toursalesmanager.gui.panel;
import lombok.AccessLevel;
import lombok.experimental.FieldDefaults;
import org.toursalesmanager.bus.PromotionBUS;
import org.toursalesmanager.dto.PromotionDTO;
import org.toursalesmanager.enums.Permission;
import org.toursalesmanager.gui.component.ButtonFactory;
import org.toursalesmanager.gui.dialog.PromotionDialog;
import org.toursalesmanager.gui.helper.ExcelHelper;
import org.toursalesmanager.gui.helper.UIColors;
import org.toursalesmanager.login.SessionManager;


import javax.swing.*;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Comparator;

@FieldDefaults(level = AccessLevel.PRIVATE)
public class PromotionPanel extends JPanel {
    JButton btnAdd, btnDel, btnEdit, btnRefresh, btnExport;

    JComboBox<String> cbbKM;

    Box.Filler filler1;

    JLabel lbTitle, jLabel2, jLabel3;

    JPanel jPanel1, jPanel2, jPanel3;

    JScrollPane jScrollPane1;

    JTable tblKM;

    JTextField txtSearch;

    PromotionBUS bus;
    DefaultTableModel model;

    public PromotionPanel() {
        initComponents();
        txtSearch.getDocument().addDocumentListener(new DocumentListener() {
            public void insertUpdate(DocumentEvent e) {
                autoSearch();
            }

            public void removeUpdate(DocumentEvent e) {
                autoSearch();
            }

            public void changedUpdate(DocumentEvent e) {
                autoSearch();
            }
        });
        tblKM.addMouseListener(new MouseAdapter() {
            public void mousePressed(MouseEvent evt) {
                tblKMMouseClicked(evt);
            }
        });
        bus=new PromotionBUS();
        model = (DefaultTableModel) tblKM.getModel();
        tblKM.setDefaultEditor(Object.class, null);
        loadData();
    }


    @SuppressWarnings("unchecked")
    private void initComponents() {
        boolean canManagePromotion = SessionManager.hasPermission(Permission.MANAGE_PROMOTION);

        jPanel1 = new JPanel();
        lbTitle = new JLabel();
        jPanel2 = new JPanel();
        jLabel2 = new JLabel();
        cbbKM = new JComboBox<>();
        filler1 = new Box.Filler(new Dimension(20, 0), new Dimension(20, 0), new Dimension(20, 32767));
        jLabel3 = new JLabel();
        txtSearch = new JTextField();
        jScrollPane1 = new JScrollPane();
        tblKM = new JTable();
        jPanel3 = new JPanel();
        btnAdd = new JButton();
        btnDel = new JButton();
        btnEdit = new JButton();
        btnRefresh = new JButton();
        btnExport = new JButton();

        setPreferredSize(new Dimension(623, 300));
        setLayout(new BorderLayout());

        jPanel1.setAlignmentX(0.0F);
        jPanel1.setAlignmentY(0.0F);
        jPanel1.setPreferredSize(new Dimension(623, 50));
        jPanel1.setLayout(new BorderLayout());

        lbTitle.setFont(new Font("Segoe UI", 1, 18)); // NOI18N8N
        lbTitle.setHorizontalAlignment(SwingConstants.CENTER);
        lbTitle.setText(canManagePromotion ? "QUẢN LÝ KHUYẾN MÃI" : "DANH SÁCH KHUYẾN MÃI");
        jPanel1.add(lbTitle, BorderLayout.PAGE_START);

        jPanel2.setBorder(BorderFactory.createEmptyBorder(5, 100, 5, 100));
        jPanel2.setAlignmentX(0.0F);
        jPanel2.setAlignmentY(0.0F);
        jPanel2.setMinimumSize(new Dimension(230, 35));
        jPanel2.setPreferredSize(new Dimension(100, 45));

        jLabel2.setFont(new Font("Segoe UI", 3, 14)); // NOI18N
        jLabel2.setText("Loại");
        jPanel2.add(jLabel2);

        cbbKM.setFont(new Font("Segoe UI", 1, 14)); // NOI18N
        cbbKM.setModel(new DefaultComboBoxModel<>(new String[] { "Tất cả", "KMHD", "KMTour" }));
        cbbKM.setToolTipText("");
        cbbKM.setMinimumSize(new Dimension(91, 20));
        cbbKM.setName(""); // NOI18N
        cbbKM.setPreferredSize(new Dimension(81, 30));
        cbbKM.addActionListener(this::cbbKMActionPerformed);
        jPanel2.add(cbbKM);
        jPanel2.add(filler1);

        jLabel3.setFont(new Font("Segoe UI", 3, 14)); // NOI18N
        jLabel3.setText("Từ khóa");
        jPanel2.add(jLabel3);

        txtSearch.setHorizontalAlignment(JTextField.CENTER);
        txtSearch.setToolTipText("");
        txtSearch.setAlignmentX(2.0F);
        txtSearch.setAlignmentY(20.0F);
        txtSearch.setMargin(new Insets(5, 10, 5, 10));
        txtSearch.setMinimumSize(new Dimension(40, 22));
        txtSearch.setPreferredSize(new Dimension(200, 28));
        txtSearch.addActionListener(this::txtSearchActionPerformed);
        jPanel2.add(txtSearch);

        jPanel1.add(jPanel2, BorderLayout.PAGE_END);

        add(jPanel1, BorderLayout.PAGE_START);

        tblKM.setModel(new DefaultTableModel(
                new Object [][] {
                        {null, null, null, null, null, null, null},
                        {null, null, null, null, null, null, null},
                        {null, null, null, null, null, null, null},
                        {null, null, null, null, null, null, null}
                },
                new String [] {
                        "Mã KM", "Tên KM", "Ngày BD", "Ngày KT", "Hình thức", "Chiết Khấu(%)", "Ghi chú"
                }
        ) {
            Class[] types = new Class [] {
                    String.class, String.class, String.class, String.class, String.class, Float.class, String.class
            };

            public Class getColumnClass(int columnIndex) {
                return types [columnIndex];
            }
        });
        jScrollPane1.setViewportView(tblKM);
        jScrollPane1.setBorder(BorderFactory.createEmptyBorder(0, 10, 0, 10));

        add(jScrollPane1, BorderLayout.CENTER);

        jPanel3.setLayout(new FlowLayout(FlowLayout.CENTER, 20, 5));


        addPromotion();
        btnAdd.setVisible(canManagePromotion);
        jPanel3.add(btnAdd);

        deletePromotion();
        btnDel.setVisible(canManagePromotion);
        jPanel3.add(btnDel);

        editPromotion();
        btnEdit.setVisible(canManagePromotion);
        jPanel3.add(btnEdit);

        refresh();
        jPanel3.add(btnRefresh);

        exportExcel();
        jPanel3.add(btnExport);

        add(jPanel3, BorderLayout.PAGE_END);
    }

    private void loadData() {
        model.setRowCount(0);
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");
        ArrayList<PromotionDTO> list = new ArrayList<>(bus.getDsCTrinhKM());
        list.sort(Comparator.comparing(PromotionDTO::getIdPromotion));
        for (PromotionDTO km : list) {
            model.addRow(new Object[]{
                    km.getIdPromotion(),
                    km.getPromotionName(),
                    km.getStartDate().format(formatter),
                    km.getEndDate().format(formatter),
                    km.isHinhThucKM() ? "KMHD" : "KMTour",
                    km.getDiscount(),
                    km.getNote()
            });
        }
    }

    private void loadData(String loai, String keyword) {
        model.setRowCount(0);
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");
        bus.searchCTrinhKM(loai, keyword).forEach(km -> {
            model.addRow(new Object[]{
                    km.getIdPromotion(),
                    km.getPromotionName(),
                    km.getStartDate().format(formatter),
                    km.getEndDate().format(formatter),
                    km.isHinhThucKM() ? "KMHD" : "KMTour",
                    km.getDiscount(),
                    km.getNote()
            });
        });
    }


    private void addPromotion(){
        btnAdd = ButtonFactory.create("Thêm", UIColors.ADD);
        btnAdd.addActionListener(v -> {
            if (!requireManagePermission()) {
                return;
            }

            PromotionDialog dialog=new PromotionDialog(bus);
            dialog.setModal(true);
            dialog.setVisible(true);
            loadData();
        });
    }

    private void deletePromotion(){
        btnDel = ButtonFactory.create("Xóa", UIColors.DELETE);
        btnDel.addActionListener(v -> {
            if (!requireManagePermission()) {
                return;
            }

            try{
                int row = tblKM.getSelectedRow();

                if (row >= 0) {
                    int confirm = JOptionPane.showConfirmDialog(this, "Bạn có chắc muốn xóa chương trình khuyến mãi này?", "Xác nhận", JOptionPane.YES_NO_OPTION);
                    if (confirm == JOptionPane.YES_OPTION) {
                        String maKM = model.getValueAt(row, 0).toString();
                        if(bus.xoaCTrinhKM(maKM)) {
                            DefaultTableModel model = (DefaultTableModel) tblKM.getModel();
                            model.removeRow(row);
                            loadData();
                            JOptionPane.showMessageDialog(this, "Xóa chương trình khuyến mãi thành công.");
                        } else {
                            JOptionPane.showMessageDialog(this, "Xóa chương trình khuyến mãi thất bại.");
                        }
                    }
                } else {
                    JOptionPane.showMessageDialog(this, "Vui lòng chọn một chương trình khuyến mãi để xóa.");
                }
            } catch (Exception e) {
                JOptionPane.showMessageDialog(this, "Có lỗi xảy ra khi xóa chương trình khuyến mãi.");
            }
        });
    }

    private void editPromotion(){
        btnEdit = ButtonFactory.create("Chỉnh sửa", UIColors.EDIT);
        btnEdit.addActionListener(v -> {
            if (!requireManagePermission()) {
                return;
            }

            int row = tblKM.getSelectedRow();
            if (row >= 0) {
                String maKM = tblKM.getValueAt(row, 0).toString();
                PromotionDTO ct = bus.getFullCTrinhKM(maKM);  // Lấy chi tiết
                if (ct != null) {
                    PromotionDialog dialog = new PromotionDialog(bus, ct);
                    dialog.setModal(true);
                    dialog.setVisible(true);
                    loadData();
                } else {
                    JOptionPane.showMessageDialog(this, "Không tìm thấy chi tiết chương trình khuyến mãi.");
                }
            } else {
                JOptionPane.showMessageDialog(this, "Vui lòng chọn một chương trình khuyến mãi để chỉnh sửa.");
            }
        });
    }

    private void refresh(){
        btnRefresh = ButtonFactory.create("Làm mới", UIColors.REFRESH);
        btnRefresh.addActionListener(v -> {
            loadData();
        });
    }

    private void exportExcel(){
        btnExport = ButtonFactory.create("Xuất excel", UIColors.EXPORT_EXCEL);
        btnExport.addActionListener(v -> {
            ExcelHelper.xuatExcel(tblKM, this, "Danh sách khuyến mãi");
        });
    }

    private void tblKMMouseClicked(MouseEvent evt) {
        int row = tblKM.rowAtPoint(evt.getPoint());

        if (row >= 0) {

            btnEdit.setEnabled(true);
            btnDel.setEnabled(true);

            if (evt.getClickCount() == 2 && !evt.isConsumed()) {

                evt.consume();

                String[] option = {/*"Xem chi tiết",*/ "Chỉnh sửa", "Hủy"};

                int choice = JOptionPane.showOptionDialog(
                        this,
                        "Bạn muốn làm gì với chương trình khuyến mãi này?",
                        "Lựa chọn",
                        JOptionPane.DEFAULT_OPTION,
                        JOptionPane.INFORMATION_MESSAGE,
                        null,
                        option,
                        option[0]
                );

                if (choice == 0) {
                    String maKM = tblKM.getValueAt(row, 0).toString();
                    PromotionDTO ct = bus.getFullCTrinhKM(maKM);
                    if (ct != null) {
                        PromotionDialog dialog = new PromotionDialog(bus, ct);
                        dialog.setModal(true);
                        dialog.setVisible(true);
                        loadData();
                    } else {
                        JOptionPane.showMessageDialog(this, "Không tìm thấy chi tiết chương trình khuyến mãi.");
                    }
                }
            }
        }
    }

    private void cbbKMActionPerformed(ActionEvent evt) {//GEN-FIRST:event_cbbKMActionPerformed
        String loai =cbbKM.getSelectedItem().toString().trim();
        loadData(loai,"");
    }//GEN-LAST:event_cbbKMActionPerformed

    private void autoSearch() {
        // TODO add your handling code here:
        String tim = txtSearch.getText().toString().trim();
        String loai =cbbKM.getSelectedItem().toString().trim();

        if(tim.isEmpty()){
            loadData();
        }else {
            loadData(loai, tim);
        }
    }//GEN-LAST:event_btnSearchActionPerformed

    private void txtSearchActionPerformed(ActionEvent evt) {//GEN-FIRST:event_txtSearchActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_txtSearchActionPerformed

    private boolean requireManagePermission() {
        if (SessionManager.hasPermission(Permission.MANAGE_PROMOTION)) {
            return true;
        }

        JOptionPane.showMessageDialog(
                this,
                "Bạn không có quyền quản lý chương trình khuyến mãi.",
                "Không đủ quyền",
                JOptionPane.WARNING_MESSAGE
        );
        return false;
    }
}
