package org.toursalesmanager.gui.panel;

import org.toursalesmanager.gui.helper.TableSelectionHelper;

import lombok.AccessLevel;
import lombok.experimental.FieldDefaults;
import org.toursalesmanager.bus.TourPlanDetailBUS;
import org.toursalesmanager.dto.TourPlanDetailDTO;
import org.toursalesmanager.enums.Permission;
import org.toursalesmanager.gui.component.ButtonFactory;
import org.toursalesmanager.gui.dialog.TourPlanDetailDialog;
import org.toursalesmanager.gui.helper.UIColors;
import org.toursalesmanager.login.SessionManager;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;

@FieldDefaults(level = AccessLevel.PRIVATE)
public class TourPlanDetailPanel extends JDialog {
    // relate to table
    DefaultTableModel tableModel;
    JTable table;
    JScrollPane scrollPane;

    // define btn
    JButton addBtn, deleteBtn, editBtn, refreshBtn;

    TourPlanDetailBUS tourPlanDetailBUS;
    final String idTourPlan;
    final boolean canManageTourPlan;

    DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");

    public TourPlanDetailPanel(Window owner, String idTourPlan){
        super(owner, "Chi tiết kế hoạch: " + idTourPlan, ModalityType.APPLICATION_MODAL);
        this.idTourPlan = idTourPlan;
        this.tourPlanDetailBUS = new TourPlanDetailBUS();
        this.canManageTourPlan = SessionManager.hasPermission(Permission.MANAGE_TOUR_PLAN);

        setSize(1000, 500);
        setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);

        init();
        loadTableWithIdTourPlan();
        setLocationRelativeTo(owner); // hiển thị giữa màn hình cha
    }

    private void init(){
        setLayout(new BorderLayout());
        initTable(); // init table here

        //South Panel
        JPanel southPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 15, 10));
        add(); // add button
        addBtn.setVisible(canManageTourPlan);
        southPanel.add(addBtn);
        delete(); // delete button
        deleteBtn.setVisible(canManageTourPlan);
        southPanel.add(deleteBtn);
        edit(); // edit button
        editBtn.setVisible(canManageTourPlan);
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
        table.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        scrollPane = new JScrollPane(table);
        scrollPane.setBorder(BorderFactory.createEmptyBorder(10, 10, 0, 10));
    }

    private void loadTableWithIdTourPlan(){
        tableModel.setRowCount(0);
        ArrayList<TourPlanDetailDTO> lsCTKeHoachTours;
        try {
            lsCTKeHoachTours = tourPlanDetailBUS.getLsCTietKHToursById(idTourPlan);
        } catch (RuntimeException e) {
            JOptionPane.showMessageDialog(this, e.getMessage(), "Lỗi", JOptionPane.ERROR_MESSAGE);
            return;
        }

        for (TourPlanDetailDTO ct : lsCTKeHoachTours){
            tableModel.addRow(new Object[]{
                    ct.getIdTourPlanDetail(),
                    ct.getDate() == null ? "" : ct.getDate().format(formatter),
                    ct.getTotalExpenditure(),
                    ct.getHousingCost(),
                    ct.getEatingCost(),
                    ct.getTravelingCost(),
                    ct.getDepartureLocation(),
                    ct.getEndLocation(),
                    ct.getIdTourPlan()
            });
        }
    }

    private void add(){
        addBtn = ButtonFactory.create("Thêm chi tiết kế hoạch Tour", UIColors.ADD);
        addBtn.addActionListener(e -> openDiaLog(null)); // null là ở chế độ thêm, có đối tượng DTO là ở dạng sửa
    }

    private void openDiaLog(TourPlanDetailDTO dto){
        try {
            TourPlanDetailDialog dialog = new TourPlanDetailDialog(this, idTourPlan, dto);
            dialog.setVisible(true);
        } catch (SecurityException ex) {
            JOptionPane.showMessageDialog(this, ex.getMessage(), "Không đủ quyền", JOptionPane.WARNING_MESSAGE);
        }
        loadTableWithIdTourPlan();
    }

    private String getSelectedDetailId(){
        int row = table.getSelectedRow();
        if (row == -1) return null;
        return tableModel.getValueAt(table.convertRowIndexToModel(row), 0).toString();
    }

    // delete button
    private void delete(){
        deleteBtn = ButtonFactory.create("Xóa chi tiết kế hoạch tour", UIColors.DELETE);
        deleteBtn.setEnabled(false);
        deleteBtn.addActionListener(e ->{
            String maCTKHTour = getSelectedDetailId();
            if (maCTKHTour == null){
                JOptionPane.showMessageDialog(this, "Vui lòng chọn chi tiết kế hoạch tour muốn xóa");
                return;
            }

            int confirm = JOptionPane.showConfirmDialog(this, "Xác nhận xóa?");
            if(confirm == JOptionPane.YES_OPTION){
                try {
                    boolean result = tourPlanDetailBUS.removeCTietKHTour(maCTKHTour);
                    if(result)
                        JOptionPane.showMessageDialog(this, "Đã xóa chi tiết kế hoạch tour có mã: " + maCTKHTour);
                    else
                        JOptionPane.showMessageDialog(this, "Mã chi tiết kế hoạch tour không tồn tại: " + maCTKHTour);
                } catch (RuntimeException ex) {
                    JOptionPane.showMessageDialog(this, ex.getMessage(), "Lỗi", JOptionPane.ERROR_MESSAGE);
                }
                loadTableWithIdTourPlan();
            }
        });
    }

    // edit button
    private void edit(){
        editBtn = ButtonFactory.create("Chỉnh sửa", UIColors.EDIT);
        editBtn.setEnabled(false);
        editBtn.addActionListener(e -> {
            String maCTKHTour = getSelectedDetailId();
            if (maCTKHTour == null) {
                JOptionPane.showMessageDialog(this, "Vui lòng chọn chi tiết kế hoạch tour cần sửa");
                return;
            }
            TourPlanDetailDTO ct = tourPlanDetailBUS.getCTietKHTourById(maCTKHTour);
            if (ct == null) {
                JOptionPane.showMessageDialog(this, "Không tìm thấy chi tiết kế hoạch tour: " + maCTKHTour);
                loadTableWithIdTourPlan();
                return;
            }
            openDiaLog(ct);
        });
    }

    // refresh button
    private void refresh(){
        refreshBtn = ButtonFactory.create("Làm mới", UIColors.REFRESH);
        refreshBtn.addActionListener(e -> loadTableWithIdTourPlan());
    }

    private void hasSelectedRow(){
        TableSelectionHelper.bindButtons(table, () -> canManageTourPlan, deleteBtn, editBtn);
    }

}
