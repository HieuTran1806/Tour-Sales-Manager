package org.example.gui.panel;

import com.toedter.calendar.JDateChooser;
import lombok.AccessLevel;
import lombok.experimental.FieldDefaults;
import org.example.bus.*;
import org.example.dao.InvoiceDAO;
import org.example.dto.*;
import org.example.gui.dialog.NhapCTHD;
import org.example.gui.dialog.TourPlanDetailDialog;
import org.example.gui.helper.DateHelper;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.*;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@FieldDefaults(level = AccessLevel.PRIVATE)
public class TourPlanDetailPanel extends JDialog {
    // relate to table
    DefaultTableModel tableModel;
    JTable table;
    JScrollPane scrollPane;

    // define btn
    JButton addBtn, deleteBtn, editBtn, refreshBtn;

    CustomerBUS customerBUS;

    TourPlanDetailBUS tourPlanDetailBUS;
    TourPlanDetailDTO tourPlanDetailDTO;

    public TourPlanDetailPanel(TourPlanDetailBUS bus, TourPlanDetailDTO dto){
        this.tourPlanDetailBUS = bus;
        this.tourPlanDetailDTO = dto;

        this.tourPlanDetailBUS = new TourPlanDetailBUS();
        this.customerBUS = new CustomerBUS();

        String idTourPlan = dto.getIdTourPlan();
        setTitle("Chi tiết kế hoạch: " + idTourPlan);
        setSize(1000, 500);
        setLocationRelativeTo(null);
        setModal(true);

        init();
        loadTableWithIdTourPlan(idTourPlan);
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

        hasSelectedRow();
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

    private void loadTableWithIdTourPlan(String maKHTour){
        tableModel.setRowCount(0);
        ArrayList<TourPlanDetailDTO> lsCTKeHoachTours = tourPlanDetailBUS.getLsCTietKHToursById(maKHTour);

        for (TourPlanDetailDTO ct : lsCTKeHoachTours){
            tableModel.addRow(new Object[]{
                    ct.getIdTourPlanDetail(),
                    ct.getDate(),
                    ct.getTotalExpenditure(),
                    ct.getHousingCost(),
                    ct.getEatingCost(),
                    ct.getTravelingCost(),
                    ct.getDepartureLocation(),
                    ct.getDepartureLocation(),
                    ct.getIdTourPlan()
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

    private void openDiaLog(TourPlanDetailDTO dto){
        TourPlanDetailDialog dialog = new TourPlanDetailDialog(tourPlanDetailDTO.getIdTourPlan());

        dialog.setVisible(true);
        loadTableWithIdTourPlan(dto.getIdTourPlan());
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
                boolean result = tourPlanDetailBUS.removeCTietKHTour(maCTKHTour);

                if(result)
                    JOptionPane.showMessageDialog(this, "Đã xóa chi tiết kế hoạch tour có mã: " + maCTKHTour);
                else
                    JOptionPane.showMessageDialog(this, "Mã chi tiết kế hoạch tour không tồn tại: " + maCTKHTour);
                loadTableWithIdTourPlan(tourPlanDetailDTO.getIdTourPlan());
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
            TourPlanDetailDTO ct = tourPlanDetailBUS.getCTietKHTourById(maCTKHTour);
            openDiaLog(ct);
        });
    }

    // refresh button
    private void refresh(){
        refreshBtn = createBtn("Làm mới", UIColors.SAVE);
        refreshBtn.addActionListener(e -> {
            loadTableWithIdTourPlan(tourPlanDetailDTO.getIdTourPlan());
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
