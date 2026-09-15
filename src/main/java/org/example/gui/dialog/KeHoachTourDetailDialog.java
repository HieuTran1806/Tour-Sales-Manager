package org.example.gui.dialog;

import org.example.bus.CTietKHTourBUS;
import org.example.dto.CTietKHTourDTO;
import org.example.gui.panel.UIColors;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.util.ArrayList;

public class KeHoachTourDetailDialog extends JDialog {
    private String maKHTour;

    // relate to table
    private DefaultTableModel tableModel;
    private JTable table;
    private JScrollPane scrollPane;

    // define btn
    private JButton addBtn, deleteBtn, editBtn, refreshBtn;

    private CTietKHTourBUS cTietKHTourBUS;

    public KeHoachTourDetailDialog(String maKHTour){
        this.maKHTour = maKHTour;
        this.cTietKHTourBUS = new CTietKHTourBUS();

        setTitle("Chi tiết kế hoạch: " + maKHTour);
        setSize(1000, 500);
        setLocationRelativeTo(null);
        setModal(true);

        init();
        loadTable(maKHTour);
        hasSelectedRow();
    }

    private void init(){
        setLayout(new BorderLayout());
        initTable(); // init table here

        //South Panel
        JPanel southPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 15, 10));
        add(); // add button
        southPanel.add(addBtn);
        delete(); // delete button
        southPanel.add(deleteBtn);
        edit(); // edit button
        southPanel.add(editBtn);
        refresh();
        southPanel.add(refreshBtn);

        add(scrollPane, BorderLayout.CENTER);
        add(southPanel, BorderLayout.SOUTH);
    }

    public void initTable(){
        String[] columns = {"Mã Chi tiết kế hoạch tour", "Ngày thực hiện", "Tổng chi",
            "Tiền ở", "Tiền ăn", "Tiền đi lại", "Điểm đi", "Điểm đến", "Mã kế hoạch tour"
        };

        tableModel = new DefaultTableModel(columns, 0);
        table = new JTable(tableModel);
        table.setDefaultEditor(Object.class, null);
        scrollPane = new JScrollPane(table);
    }

    private void loadTable(String maKHTour){
        tableModel.setRowCount(0);
        ArrayList<CTietKHTourDTO> lsCTKeHoachTours = cTietKHTourBUS.getLsCTietKHToursById(maKHTour);

        for (CTietKHTourDTO ct : lsCTKeHoachTours){
            tableModel.addRow(new Object[]{
                    ct.getMaCTietKHTour(),
                    ct.getNgayThucHien(),
                    ct.getTongChi(),
                    ct.getTienO(),
                    ct.getTienAn(),
                    ct.getTienDiLai(),
                    ct.getDiemDi(),
                    ct.getDiemDen(),
                    ct.getMaKHTour()
            });
        }
    }

    private JButton createBtn(String text, Color color){
        JButton btn = new JButton(text);
        btn.setBackground(color);
        btn.setForeground(Color.WHITE);
        btn.setFocusPainted(false);
        btn.setFont(new Font("SansSerif", Font.BOLD, 13));
        btn.setCursor(new Cursor(Cursor.HAND_CURSOR)); // in south panel

        btn.setContentAreaFilled(true);
        btn.setOpaque(true);
        btn.setBorderPainted(false);

        return btn;
    }

    private void add(){
        addBtn = createBtn("Thêm chi tiết kế hoạch Tour", Color.GREEN);
        addBtn.addActionListener(e -> openDiaLog(null)); // null là ở chế độ thêm, có đối tượng DTO là ở dạng sửa
    }

    private void openDiaLog(CTietKHTourDTO cTietKHTourDTO){
        CTietKHTourDialog dialog = new CTietKHTourDialog(cTietKHTourBUS, cTietKHTourDTO, maKHTour);
        dialog.setVisible(true);
        loadTable(maKHTour);
    }

    // delete button
    private void delete(){
        deleteBtn = createBtn("Xóa chi tiết kế hoạch tour", Color.RED);
        deleteBtn.setEnabled(false);
        deleteBtn.addActionListener(e ->{
            int row = table.getSelectedRow();
            if (row == -1){
                JOptionPane.showMessageDialog(this, "Vui lòng chọn chi tiết kế hoạch tour muốn xóa");
                return;
            }

            String maCTKHTour = tableModel.getValueAt(row, 0).toString();
            int confirm = JOptionPane.showConfirmDialog(this, "Xác nhận xóa?");
            if(confirm == JOptionPane.YES_OPTION){
                boolean result = cTietKHTourBUS.removeCTietKHTour(maCTKHTour);

                if(result)
                    JOptionPane.showMessageDialog(this, "Đã xóa chi tiết kế hoạch tour có mã: " + maCTKHTour);
                else
                    JOptionPane.showMessageDialog(this, "Mã chi tiết kế hoạch tour không tồn tại: " + maCTKHTour);
                loadTable(maKHTour);
            }
        });
    }

    // edit button
    private void edit(){
        editBtn = createBtn("Chỉnh sửa", UIColors.EDIT);
        editBtn.setEnabled(false);
        editBtn.addActionListener(e -> {
            int row = table.getSelectedRow();
            if (row == -1) {
                JOptionPane.showMessageDialog(this, "Vui lòng chọn chi tiết kế hoạch tour cần sửa");
                return;
            }
            String maCTKHTour = tableModel.getValueAt(row, 0).toString();
            CTietKHTourDTO ct = cTietKHTourBUS.getCTietKHTourById(maCTKHTour);
            openDiaLog(ct);
        });
    }

    // refresh button
    private void refresh(){
        refreshBtn = createBtn("Làm mới", UIColors.SAVE);
        refreshBtn.addActionListener(e -> {
            loadTable(maKHTour);
        });
    }

    private void hasSelectedRow(){
        table.getSelectionModel().addListSelectionListener(e ->{
            boolean hadSelection = table.getSelectedRow() != -1;
            deleteBtn.setEnabled(hadSelection);
            editBtn.setEnabled(hadSelection);
        });
    }
}
