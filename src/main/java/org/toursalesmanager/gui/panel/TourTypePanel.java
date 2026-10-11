package org.toursalesmanager.gui.panel;

import lombok.AccessLevel;
import lombok.experimental.FieldDefaults;
import org.toursalesmanager.bus.TourTypeBUS;
import org.toursalesmanager.dto.TourTypeDTO;
import org.toursalesmanager.enums.Permission;
import org.toursalesmanager.gui.component.ButtonFactory;
import org.toursalesmanager.gui.dialog.TourTypeDialog;
import org.toursalesmanager.gui.helper.UIColors;
import org.toursalesmanager.login.SessionManager;

import javax.swing.*;
import javax.swing.border.TitledBorder;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableRowSorter;
import java.awt.*;
import java.util.ArrayList;

@FieldDefaults(level = AccessLevel.PRIVATE)
public class TourTypePanel extends JPanel {
    // txt field
    JTextField txtSearch;

    // define panel
    JPanel northPanel, southPanel, searchPanel;

    // relate to table
    JTable table;
    JScrollPane scrollPane;
    DefaultTableModel tableModel;
    TableRowSorter<DefaultTableModel> rowSorter;

    // define btn
    JButton addBtn, deleteBtn, editBtn, refreshBtn;

    ArrayList<TourTypeDTO> lsLoaiTour;
    TourTypeBUS tourTypeBUS;

    public TourTypePanel(){
        tourTypeBUS = new TourTypeBUS();
        init();
        loadTable();
        hasSelectedRow();
    }

    public void init(){
        boolean canManageTourType = SessionManager.hasPermission(Permission.MANAGE_TOUR_TYPE);

        setLayout(new BorderLayout());

        //North Panel
        northPanel = new JPanel(new BorderLayout());
        JLabel lblTitle = new JLabel(canManageTourType ? "QUẢN LÝ LOẠI TOUR" : "DANH SÁCH LOẠI TOUR", JLabel.CENTER);
        lblTitle.setFont(new Font("Arial", Font.BOLD, 18));
        northPanel.add(lblTitle, BorderLayout.NORTH);

        // Search panel
        searchPanel = new JPanel();

        // titleBorder
        TitledBorder titleSearch = BorderFactory.createTitledBorder(
                BorderFactory.createLineBorder(Color.CYAN, 2), " TÌM KIẾM LOẠI TOUR "
        );
        titleSearch.setTitleFont(new Font("Arial", Font.BOLD, 14));
        titleSearch.setTitleColor(new Color(0, 102, 204));
        searchPanel.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createEmptyBorder(20, 10, 20, 10),
                titleSearch)
        );

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(10, 10, 10, 10); // Khoảng cách giữa các ô
        gbc.fill = GridBagConstraints.HORIZONTAL;

        // column 1 : type label
        gbc.gridx = 0; gbc.gridy = 0;
        searchPanel.add(new JLabel("Tìm kiếm theo thể loại:"), gbc);

        txtSearch = new JTextField(15);
        txtSearch.addCaretListener(e -> searchByType());
        gbc.gridx = 1; gbc.weightx = 1.0;
        searchPanel.add(txtSearch, gbc);

        northPanel.add(searchPanel, BorderLayout.CENTER);

        // init table
        initTable();

        //South Panel
        southPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 15, 10));
        add(); // add button
        addBtn.setVisible(canManageTourType);
        southPanel.add(addBtn);
        delete(); // delete button
        deleteBtn.setVisible(canManageTourType);
        southPanel.add(deleteBtn);
        edit(); // edit button
        editBtn.setVisible(canManageTourType);
        southPanel.add(editBtn);
        refresh();
        southPanel.add(refreshBtn);

        add(northPanel, BorderLayout.NORTH);
        add(scrollPane, BorderLayout.CENTER);
        add(southPanel, BorderLayout.SOUTH);
    }

    public void initTable(){
        // columns of table
        String[] columns = {"Mã loại tour", "Thể loại", "Mô tả", "Trạng thái"};

        tableModel = new DefaultTableModel(columns, 0);
        table = new JTable(tableModel);
        table.setDefaultEditor(Object.class, null);

        rowSorter = new TableRowSorter<>(tableModel);
        table.setRowSorter(rowSorter);

        scrollPane = new JScrollPane(table);
        scrollPane.setBorder(BorderFactory.createEmptyBorder(0, 10, 0, 10));
    }

    private void loadTable(){
        tableModel.setRowCount(0);
        lsLoaiTour = tourTypeBUS.getAllLoaiTour();

        for (TourTypeDTO lt : lsLoaiTour){
            tableModel.addRow(new Object[]{
                    lt.getIdTourType(),
                    lt.getTypeOfTour(),
                    lt.getDescription(),
                    lt.getStatus()
            });
        }
    }

    private void searchByType(){
        String keyWord = txtSearch.getText().trim().toLowerCase();

        RowFilter<DefaultTableModel, Object> rf = new RowFilter<DefaultTableModel, Object>() {
            @Override
            public boolean include(Entry<? extends DefaultTableModel, ? extends Object> entry) {
                boolean found = false;
                found = entry.getStringValue(1).toLowerCase().contains(keyWord);

                return found;
            }
        };
        rowSorter.setRowFilter(rf);
    }

    private void add(){
        addBtn = ButtonFactory.create("Thêm loại Tour", UIColors.ADD);
        addBtn.addActionListener(
                e -> {
                    if (!requireManagePermission()) {
                        return;
                    }
                    openDiaLog(null);
                }
        );
    }

    private void openDiaLog(TourTypeDTO tourTypeDTO){
        TourTypeDialog tourTypeDialog = new TourTypeDialog(tourTypeBUS, tourTypeDTO);
        tourTypeDialog.setVisible(true);
        loadTable();
    }

    private void delete(){
        deleteBtn = ButtonFactory.create("Xóa loại Tour", UIColors.DELETE);
        deleteBtn.setEnabled(false);
        deleteBtn.addActionListener(e ->{
            if (!requireManagePermission()) {
                return;
            }
            int row = table.getSelectedRow();
            if (row == -1){
                JOptionPane.showMessageDialog(this, "Vui lòng chọn dòng cần xóa");
                return;
            }

            int modelRow = table.convertRowIndexToModel(row);
            String maLoaiTour = tableModel.getValueAt(modelRow, 0).toString();
            int confirm = JOptionPane.showConfirmDialog(this, "Xác nhận xóa?");
            if(confirm == JOptionPane.YES_OPTION){
                boolean result = tourTypeBUS.removeLoaiTour(maLoaiTour);
                if(result)
                    JOptionPane.showMessageDialog(null, "Đã xóa loại tour có mã: " + maLoaiTour);
                else
                    JOptionPane.showMessageDialog(null, "Lỗi! Xóa không thành công loại tour: " + maLoaiTour);
                loadTable();
            }
        });
    }

    private void edit(){
        editBtn = ButtonFactory.create("Chỉnh sửa", UIColors.EDIT);
        editBtn.setEnabled(false);

        editBtn.addActionListener(e -> {
            if(!requireManagePermission()) {
                return;
            }

            int row = table.getSelectedRow();
            if (row == -1) {
                JOptionPane.showMessageDialog(this, "Vui lòng chọn loại tour cần sửa");
                return;
            }
            int modelRow = table.convertRowIndexToModel(row);
            String maLoaiTour = tableModel.getValueAt(modelRow, 0).toString();
            TourTypeDTO lt = tourTypeBUS.getById(maLoaiTour);
            openDiaLog(lt);
        });
    }

    private void refresh(){
        refreshBtn = ButtonFactory.create("Làm mới", UIColors.REFRESH);
        refreshBtn.addActionListener(e -> {
            String keyWord = txtSearch.getText().trim().toLowerCase();
            ArrayList<TourTypeDTO> lsCate = tourTypeBUS.search(keyWord);
            loadTableByName(lsCate);
        });
    }

    private void loadTableByName(ArrayList<TourTypeDTO> list){
        tableModel.setRowCount(0);

        for(TourTypeDTO lt : list) {
            tableModel.addRow(new Object[]{
                    lt.getIdTourType(),
                    lt.getTypeOfTour(),
                    lt.getDescription(),
                    lt.getStatus()
            });
        }
    }

    private void hasSelectedRow(){
        table.getSelectionModel().addListSelectionListener(e ->{
            boolean hadSelection = table.getSelectedRow() != -1;
            boolean canManageTourType =
                    SessionManager.hasPermission(Permission.MANAGE_TOUR_TYPE);
            deleteBtn.setEnabled(hadSelection && canManageTourType);
            editBtn.setEnabled(hadSelection && canManageTourType);
        });
    }

    private boolean requireManagePermission() {
        if (SessionManager.hasPermission(Permission.MANAGE_TOUR_TYPE)) {
            return true;
        }

        JOptionPane.showMessageDialog(
                this,
                "Bạn không có quyền quản lý loại tour.",
                "Không đủ quyền",
                JOptionPane.WARNING_MESSAGE
        );
        return false;
    }
}
