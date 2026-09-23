package org.example.gui.panel;

import lombok.AccessLevel;
import lombok.experimental.FieldDefaults;
import org.example.bus.KeHoachTourBUS;
import org.example.bus.TourBUS;
import org.example.dao.TourBookingDAO;
import org.example.dto.KeHoachTourDTO;
import org.example.dto.TourDTO;
import org.example.gui.dialog.KeHoachTourDetailDialog;
import org.example.gui.dialog.KeHoachTourDialog;
import org.example.gui.dialog.TourBookingDialog;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;


@FieldDefaults(level = AccessLevel.PRIVATE)
public class KeHoachTourPanel extends JPanel {
    final JComboBox<TourDTO> cbTour;

    DefaultComboBoxModel<TourDTO> toursModel;

    JButton addBtn, deleteBtn, editBtn, detailsBtn, refreshBtn, bookingBtn;

    DefaultTableModel tableModel;

    JTable table;

    JScrollPane scrollPane;

    KeHoachTourBUS keHoachTourBUS;

    TourBUS tourBUS;

    JLabel jlbChonTour;

    ArrayList<KeHoachTourDTO> lsKeHoachTours;

    TourBookingDialog tourBookingDialog;
    TourBookingDAO dao = new TourBookingDAO();

    // formatter
    DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");

    public KeHoachTourPanel(){
        keHoachTourBUS = new KeHoachTourBUS();
        tourBUS = new TourBUS();
        cbTour = new JComboBox<>();

        init();

        // first load table
        TourDTO selectedTour = (TourDTO) cbTour.getSelectedItem();
        if(selectedTour != null)
            loadTable(selectedTour.getMaTour());
        hasSelectedRow();
    }

    private void init(){
        setLayout(new BorderLayout());

        //North Panel
        JPanel northPanel = new JPanel(new BorderLayout());
        JLabel lblTitle = new JLabel("QUẢN LÝ KẾ HOẠCH TOUR", JLabel.CENTER);
        lblTitle.setFont(new Font("Arial", Font.BOLD, 18));
        northPanel.add(lblTitle, BorderLayout.NORTH);

        //center panel add jlabel chose tour
        JPanel filterPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        jlbChonTour = new JLabel("Chọn Tour");
        filterPanel.add(jlbChonTour);

        // Load combo tours
        toursModel = new DefaultComboBoxModel<>();
        toursModel = CBTourPresent();
        cbTour.setModel(toursModel);
        filterPanel.add(cbTour); // center panel add combobox tours

        cbTour.addActionListener(e -> {
            TourDTO selectedTour = (TourDTO) cbTour.getSelectedItem();
            if(selectedTour != null){
                loadTable(selectedTour.getMaTour());
            }
        });

        //northPanel add filterPanel
        northPanel.add(filterPanel, BorderLayout.CENTER);

        // init table kehoachtour
        initTable();

        //South Panel contain buttons
        JPanel southPanel = new JPanel(new FlowLayout());
        add();
        southPanel.add(addBtn);
        delete();
        southPanel.add(deleteBtn);
        edit();
        southPanel.add(editBtn);
        viewDetail();
        southPanel.add(detailsBtn);
        booking();
        southPanel.add(bookingBtn);
        refresh();
        southPanel.add(refreshBtn);

        add(northPanel, BorderLayout.NORTH);
        add(scrollPane, BorderLayout.CENTER);
        add(southPanel, BorderLayout.SOUTH);
    }

    private void initTable(){
        // columns of table
        String[] columns = {"Mã kế hoạch Tour", "Ngày khởi hành", "Ngày kết thúc", "Tổng số vé", "Tổng chi dự kiến", "Số vé còn lại", "Trạng thái", "Mã Tour", "Mã nhân viên hướng dẫn"};

        tableModel = new DefaultTableModel(columns, 0);
        table = new JTable(tableModel);
        table.setDefaultEditor(Object.class, null);

        scrollPane = new JScrollPane(table);
        scrollPane.setBorder(BorderFactory.createEmptyBorder(0, 10, 0, 10));
    }

    private void loadTable(String maTour){
        tableModel.setRowCount(0);
        lsKeHoachTours = keHoachTourBUS.getAllKeHoachToursByID(maTour);

        for (KeHoachTourDTO kt : lsKeHoachTours){
            tableModel.addRow(new Object[]{
                    kt.getMaKHTour(),
                    kt.getNgayKhoiHanh().format(formatter),
                    kt.getNgayKetThuc().format(formatter),
                    kt.getTongSoVe(),
                    kt.getTongChiDuKien(),
                    kt.getSoVeConLai(),
                    kt.getTrangThai(),
                    kt.getMaTour(),
                    kt.getMaNVHD()
            });
        }
    }

    private DefaultComboBoxModel<TourDTO> CBTourPresent(){
        DefaultComboBoxModel<TourDTO> model;
        model = new DefaultComboBoxModel<>();
        ArrayList<TourDTO> lsTours = tourBUS.getAllTours();
        for (TourDTO t : lsTours){
            model.addElement(t);
        }
        return model;
    }

    private JButton createBtn(String text, Color color){
        JButton btn = new JButton(text);
        btn.setBackground(color);
        btn.setForeground(Color.WHITE);
        btn.setFocusPainted(false);
        btn.setFont(new Font("SansSerif", Font.BOLD, 13));
        btn.setCursor(new Cursor(Cursor.HAND_CURSOR));

        btn.setContentAreaFilled(true);
        btn.setOpaque(true);
        btn.setBorderPainted(false);

        return btn;
    }

    private void add(){
        addBtn = createBtn("Thêm kế hoạch Tour", UIColors.ADD);
        KeHoachTourDTO keHoachTourDTO = null;
        addBtn.addActionListener(e -> openDiaLog(keHoachTourDTO)); // null là ở chế độ thêm, có đối tượng DTO là ở dạng sửa
    }

    private void openDiaLog(KeHoachTourDTO keHoachTourDTO){
        TourDTO selectedTour = (TourDTO) cbTour.getSelectedItem();

        if(selectedTour == null){
            JOptionPane.showMessageDialog(this, "Vui lòng chọn tour");
            return;
        }
        KeHoachTourDialog keHoachTourDialog = new KeHoachTourDialog(keHoachTourBUS, keHoachTourDTO, tourBUS,( (TourDTO)cbTour.getSelectedItem()).getMaTour());
        keHoachTourDialog.setVisible(true);

        loadTable(selectedTour.getMaTour());
    }

    private void booking(){
        bookingBtn = createBtn("Booking", UIColors.BOOKING);
        bookingBtn.addActionListener(e -> {
            tourBookingDialog = new TourBookingDialog(null, true, dao, TourBookingDialog.Mode.ADD, null);
            tourBookingDialog.setVisible(true);
        });
    }

    private void delete(){
        deleteBtn = createBtn("Xóa kế hoạch tour", UIColors.DELETE);
        deleteBtn.setEnabled(false);
        deleteBtn.addActionListener(e ->{
            int row = table.getSelectedRow();
            if (row == -1){
                JOptionPane.showMessageDialog(this, "Vui lòng chọn kế hoạch tour cần xóa");
                return;
            }

            String maKHTour = tableModel.getValueAt(row, 0).toString();
            int confirm = JOptionPane.showConfirmDialog(this, "Xác nhận xóa?");
            if(confirm == JOptionPane.YES_OPTION){
                boolean result = keHoachTourBUS.removeKeHoachTour(maKHTour);
                if(result)
                    JOptionPane.showMessageDialog(null, "Đã xóa kế hoạch tour có mã: " + maKHTour);
                else
                    JOptionPane.showMessageDialog(null, "Không thể xóa kế hoạch tour này");
                TourDTO selectedTour = (TourDTO) cbTour.getSelectedItem();
                loadTable(selectedTour.getMaTour());
            }
        });
    }

    private void edit(){
        editBtn = createBtn("Chỉnh sửa", UIColors.EDIT);
        editBtn.setEnabled(false);
        editBtn.addActionListener(e -> {
            int row = table.getSelectedRow();
            if (row == -1) {
                JOptionPane.showMessageDialog(this, "Vui lòng chọn kế hoạch tour cần sửa");
                return;
            }
            String maKHTour = tableModel.getValueAt(row, 0).toString();
            KeHoachTourDTO kt = keHoachTourBUS.getById(maKHTour);
            openDiaLog(kt);
        });
    }

    private void viewDetail(){
        detailsBtn = createBtn("Xem chi tiết", UIColors.VIEW);
        detailsBtn.setEnabled(false);
        detailsBtn.addActionListener(e -> {
            int row = table.getSelectedRow();
            if (row == -1) {
                JOptionPane.showMessageDialog(this, "Vui lòng chọn kế hoạch tour để xem");
                return;
            }

            String maKHTour = tableModel.getValueAt(row, 0).toString();
            KeHoachTourDetailDialog dialog = new KeHoachTourDetailDialog(maKHTour);
            dialog.setVisible(true);
        });
    }

    private void refresh(){
        refreshBtn = createBtn("Làm mới", UIColors.REFRESH);
        refreshBtn.addActionListener(e -> {
            //load tours when press refresh
            toursModel = new DefaultComboBoxModel<>();
            toursModel = CBTourPresent();
            cbTour.setModel(toursModel);
            CBTourPresent();

            TourDTO selectedTour = (TourDTO) cbTour.getSelectedItem();
            assert selectedTour != null;
            loadTable(selectedTour.getMaTour());
        });
    }

    private void hasSelectedRow(){
        table.getSelectionModel().addListSelectionListener(e ->{
            boolean hadSelection = table.getSelectedRow() != -1;
            deleteBtn.setEnabled(hadSelection);
            editBtn.setEnabled(hadSelection);
            detailsBtn.setEnabled(hadSelection);
            bookingBtn.setEnabled(hadSelection);
        });
    }
}
