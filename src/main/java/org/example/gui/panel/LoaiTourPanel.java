package org.example.gui.panel;

import org.example.bus.LoaiTourBUS;
import org.example.dto.LoaiTourDTO;
import org.example.gui.dialog.LoaiTourDialog;

import javax.swing.*;
import javax.swing.border.TitledBorder;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableRowSorter;
import java.awt.*;
import java.util.ArrayList;

public class LoaiTourPanel extends JPanel {
    // txt field
    private JTextField txtSearch;

    // define panel
    private JPanel northPanel, southPanel, searchPanel;

    // relate to table
    private JTable table;
    private JScrollPane scrollPane;
    private DefaultTableModel tableModel;
    private TableRowSorter<DefaultTableModel> rowSorter;

    // define btn
    private JButton addBtn, deleteBtn, editBtn, refreshBtn;

    private ArrayList<LoaiTourDTO> lsLoaiTour;
    private LoaiTourBUS loaiTourBUS;

    public LoaiTourPanel(){
        loaiTourBUS = new LoaiTourBUS();
        init();
        loadTable();
        hasSelectedRow();
    }

    public void init(){
        setLayout(new BorderLayout());

        //North Panel
        northPanel = new JPanel(new BorderLayout());
        JLabel lblTitle = new JLabel("QUẢN LÝ LOẠI TOUR", JLabel.CENTER);
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
        southPanel.add(addBtn);
        delete(); // delete button
        southPanel.add(deleteBtn);
        edit(); // edit button
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
        lsLoaiTour = loaiTourBUS.getAllLoaiTour();

        for (LoaiTourDTO lt : lsLoaiTour){
            String trangThaiString;
            if(lt.getTrangThai() == 1){
                trangThaiString = "Đang hoạt đông";
            }else{
                trangThaiString = "Ngưng";
            }
            tableModel.addRow(new Object[]{
                    lt.getMaLoaiTour(),
                    lt.getTheLoai(),
                    lt.getMoTa(),
                    trangThaiString
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
        addBtn = createBtn("Thêm loại Tour", UIColors.ADD);
        addBtn.addActionListener(e -> openDiaLog(null));
    }

    private void openDiaLog(LoaiTourDTO loaiTourDTO){
        LoaiTourDialog loaiTourDialog = new LoaiTourDialog(loaiTourBUS, loaiTourDTO);
        loaiTourDialog.setVisible(true);
        loadTable();
    }

    private void delete(){
        deleteBtn = createBtn("Xóa loại Tour", UIColors.DELETE);
        deleteBtn.setEnabled(false);
        deleteBtn.addActionListener(e ->{
            int row = table.getSelectedRow();
            if (row == -1){
                JOptionPane.showMessageDialog(this, "Vui lòng chọn dòng cần xóa");
                return;
            }

            String maLoaiTour = tableModel.getValueAt(row, 0).toString();
            int confirm = JOptionPane.showConfirmDialog(this, "Xác nhận xóa?");
            if(confirm == JOptionPane.YES_OPTION){
                boolean result = loaiTourBUS.removeLoaiTour(maLoaiTour);
                if(result)
                    JOptionPane.showMessageDialog(null, "Đã xóa loại tour có mã: " + maLoaiTour);
                else
                    JOptionPane.showMessageDialog(null, "Lỗi! Xóa không thành công loại tour: " + maLoaiTour);
                loadTable();
            }
        });
    }

    private void edit(){
        editBtn = createBtn("Chỉnh sửa", UIColors.EDIT);
        editBtn.setEnabled(false);
        editBtn.addActionListener(e -> {
            int row = table.getSelectedRow();
            if (row == -1) {
                JOptionPane.showMessageDialog(this, "Vui lòng chọn loại tour cần sửa");
                return;
            }
            String maLoaiTour = tableModel.getValueAt(row, 0).toString();
            LoaiTourDTO lt = loaiTourBUS.getById(maLoaiTour);
            openDiaLog(lt);
        });
    }

    private void refresh(){
        refreshBtn = createBtn("Làm mới", UIColors.REFRESH);
        refreshBtn.addActionListener(e -> {
            String keyWord = txtSearch.getText().trim().toLowerCase();
            ArrayList<LoaiTourDTO> lsCate = loaiTourBUS.search(keyWord);
            loadTableByName(lsCate);
        });
    }

    private void loadTableByName(ArrayList<LoaiTourDTO> list){
        tableModel.setRowCount(0);

        for(LoaiTourDTO lt : list) {
            String trangThaiString;
            if(lt.getTrangThai() == 1){
                trangThaiString = "Đang hoạt đông";
            }else{
                trangThaiString = "Ngưng";
            }
            tableModel.addRow(new Object[]{
                    lt.getMaLoaiTour(),
                    lt.getTheLoai(),
                    lt.getMoTa(),
                    trangThaiString
            });
        }
    }

    private void hasSelectedRow(){
        table.getSelectionModel().addListSelectionListener(e ->{
            boolean hadSelection = table.getSelectedRow() != -1;
            deleteBtn.setEnabled(hadSelection);
            editBtn.setEnabled(hadSelection);
        });
    }
}
