package org.toursalesmanager.gui.dialog;

import lombok.AccessLevel;
import lombok.experimental.FieldDefaults;
import org.toursalesmanager.bus.LocationBUS;
import org.toursalesmanager.bus.TourTypeBUS;
import org.toursalesmanager.bus.TourBUS;
import org.toursalesmanager.dto.LocationDTO;
import org.toursalesmanager.dto.TourTypeDTO;
import org.toursalesmanager.dto.TourDTO;
import org.toursalesmanager.gui.component.ButtonFactory;
import org.toursalesmanager.gui.helper.AddComponentToForm;
import org.toursalesmanager.gui.helper.UIColors;

import javax.swing.*;
import java.awt.*;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.File;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Locale;

@FieldDefaults(level = AccessLevel.PRIVATE)
public class TourDiaLog extends JDialog {
    // label and txt
    JLabel jlbImgLink, jlbPreview;
    JTextField txtMaTour, txtTen, txtSoNgay, txtDonGia, txtSoCho, txtDiaDiemKhoiHanh, txtImgLink;

    // define btn
    JButton saveBtn, cancelBtn, chooseImageBtn;

    String path;

    File fileSelected;

    // define relate panel
    JPanel formPanel, southPanel;

    TourBUS tourBUS;
    TourDTO tourDTO;
    TourTypeBUS tourTypeBUS;
    LocationBUS locationBUS;

    DefaultComboBoxModel<TourTypeDTO> loaiToursModel;
    JComboBox<TourTypeDTO> cbLoaiTours;

    DefaultComboBoxModel<LocationDTO> diaDiemModel;
    JComboBox<LocationDTO> cbDiaDiem;

    public TourDiaLog(TourBUS tourBUS, TourDTO tourDTO){
        this.tourDTO = tourDTO;
        this.tourBUS = tourBUS;
        tourTypeBUS = new TourTypeBUS();
        locationBUS = new LocationBUS();
        cbLoaiTours = new JComboBox<>();
        cbDiaDiem = new JComboBox<>();

        setTitle(tourDTO == null ? "Thêm Tour" : "Sửa Tour");
        setSize(300, 360);
        setLocationRelativeTo(null);
        setModal(true);

        init();
        requestFocusName();


        if(tourDTO != null){
            loadData();
        }
    }

    private void init(){
        setLayout(new BorderLayout());

        formPanel = new JPanel(new GridLayout(9, 2));

        // southPanel contain Buttons
        southPanel = new JPanel(new FlowLayout());

        // Save button
        save();
        southPanel.add(saveBtn);

        //Cancel button
        cancel();
        southPanel.add(cancelBtn);

        //row maTour
        txtMaTour = new JTextField();
        txtMaTour.setEditable(false);
        if (tourDTO == null) {
            txtMaTour.setText(tourBUS.getNextTourId());
        }
        AddComponentToForm.addFormRow(formPanel, "Mã tour", txtMaTour);

        // row ten
        txtTen = new JTextField();
        AddComponentToForm.addFormRow(formPanel, "Tên", txtTen);

        //row soNgay
        txtSoNgay = new JTextField();
        AddComponentToForm.addFormRow(formPanel, "Số ngày", txtSoNgay);

        // row donGia
        txtDonGia = new JTextField();
        AddComponentToForm.addFormRow(formPanel, "Đơn giá", txtDonGia);

        //row soNguoi
        txtSoCho = new JTextField();
        AddComponentToForm.addFormRow(formPanel, "Số chỗ", txtSoCho);

        // row ddkhoihanh
        txtDiaDiemKhoiHanh = new JTextField();
        AddComponentToForm.addFormRow(formPanel, "Địa điểm khởi hành", txtDiaDiemKhoiHanh);

        // row maLoaiTour
        ArrayList<TourTypeDTO> lsCate = tourTypeBUS.getAllLoaiTour();
        loaiToursModel = new DefaultComboBoxModel<>();
        for(TourTypeDTO lt : lsCate){
            loaiToursModel.addElement(lt);
        }
        cbLoaiTours.setModel(loaiToursModel);
        formPanel.add(cbLoaiTours);
        AddComponentToForm.addFormRow(formPanel, "Mã loại tour", cbLoaiTours);

        // row image
        jlbImgLink = new JLabel(" Đường dẫn ảnh");
        formPanel.add(jlbImgLink);

        // image panel
        JPanel imgPanel = new JPanel(new BorderLayout());
        txtImgLink = new JTextField();

        // define chooseImage function
        chooseImage();
        imgPanel.add(chooseImageBtn, BorderLayout.EAST);

        jlbPreview = new JLabel();
        jlbPreview.setPreferredSize(new Dimension(120,80));
        jlbPreview.setBorder(BorderFactory.createLineBorder(Color.GRAY));
        imgPanel.add(jlbPreview, BorderLayout.CENTER);
        formPanel.add(imgPanel);

        // row location
        ArrayList<LocationDTO> lsDiaDiem = locationBUS.getAllLocations();
        diaDiemModel = new DefaultComboBoxModel<>();
        for(LocationDTO dd : lsDiaDiem){
            diaDiemModel.addElement(dd);
        }
        cbDiaDiem.setModel(diaDiemModel);
        // set txt DiaDiemKhoiHanh when init
        cbDiaDiem.addActionListener(e -> {
            LocationDTO selected = (LocationDTO) cbDiaDiem.getSelectedItem();
            if(selected != null){
                txtDiaDiemKhoiHanh.setText(selected.getAddress());
            }
        });
        AddComponentToForm.addFormRow(formPanel, "Mã địa điểm", cbDiaDiem);

        add(formPanel, BorderLayout.CENTER);
        add(southPanel, BorderLayout.SOUTH);
    }

    private void loadData(){
        // load txt
        txtMaTour.setText(tourDTO.getIdTour());
        txtMaTour.setEnabled(false);

        txtTen.setText(tourDTO.getTourName());
        txtSoNgay.setText(tourDTO.getNumberOfDate() + "");
        txtDonGia.setText(tourDTO.getPrice() + "");
        txtSoCho.setText(tourDTO.getSeats() + "");

        //field diaDiemKhoihanh
        for(int i = 0; i < cbDiaDiem.getItemCount(); i++){
            LocationDTO dd = cbDiaDiem.getItemAt(i);
            if(dd.getIdLocation().equalsIgnoreCase(tourDTO.getIdLocation())){
                cbDiaDiem.setSelectedIndex(i);
                txtDiaDiemKhoiHanh.setText(dd.getAddress());
                break;
            }
        }

        //field maLoaiTour
        for(int i = 0; i < cbLoaiTours.getItemCount(); i++){
            TourTypeDTO lt = cbLoaiTours.getItemAt(i);
            if(lt.getIdTourType().equalsIgnoreCase(tourDTO.getIdTourType())){
                cbLoaiTours.setSelectedIndex(i);
                break;
            }
        }
        TourTypeDTO selectedLoaiTour = (TourTypeDTO) cbLoaiTours.getSelectedItem();
        tourDTO.setIdTourType(selectedLoaiTour.getIdTourType());

        // load image
        txtImgLink.setText(tourDTO.getImgLink());
        if(tourDTO.getImgLink() != null){
            ImageIcon icon = new ImageIcon(tourDTO.getImgLink());

            Image img = icon.getImage().getScaledInstance(
                    120,
                    80,
                    Image.SCALE_SMOOTH
            );
            jlbPreview.setIcon(new ImageIcon(img));
        }

        //field maDiaDiem
        for(int i = 0; i < cbDiaDiem.getItemCount(); i++){
            LocationDTO dd = cbDiaDiem.getItemAt(i);
            if(dd.getIdLocation().equalsIgnoreCase(tourDTO.getIdLocation())){
                cbDiaDiem.setSelectedIndex(i);
                break;
            }
        }
    }

    private void save(){
        saveBtn = ButtonFactory.create("Lưu", UIColors.SAVE);
        saveBtn.addActionListener(e -> {
            if(isEmpty(txtTen, txtSoNgay, txtDonGia, txtSoCho, txtDiaDiemKhoiHanh, txtImgLink)){
                JOptionPane.showMessageDialog(this, "Vui lòng nhập đầy đủ thông tin");
                return;
            }

            //validate numbers
            int soNgay;
            long donGia;
            int soCho;
            try {
                soNgay = Integer.parseInt(txtSoNgay.getText().trim());
                donGia = Long.parseLong(txtDonGia.getText().trim());
                soCho = Integer.parseInt(txtSoCho.getText().trim());
            } catch (NumberFormatException ex) {
                JOptionPane.showMessageDialog(this, "Vui lòng nhập đúng định dạng số");
                return;
            }

            if(tourDTO == null){
                TourTypeDTO selectedLoaiTour = (TourTypeDTO) cbLoaiTours.getSelectedItem();
                String maLoaiTour = selectedLoaiTour.getIdTourType();
                LocationDTO selectedLocation = (LocationDTO) cbDiaDiem.getSelectedItem();
                String maDiaDiem = selectedLocation.getIdLocation();

                TourDTO tourMoi = new TourDTO(
                        txtMaTour.getText(), txtTen.getText(),
                        soNgay,BigDecimal.valueOf(donGia), soCho, txtDiaDiemKhoiHanh.getText(),
                        txtImgLink.getText(), maLoaiTour, maDiaDiem
                );
                boolean result = tourBUS.addTour(tourMoi);
                if (result){
                    JOptionPane.showMessageDialog(this, "Đã thêm");
                    dispose();
                }else{
                    JOptionPane.showMessageDialog(this, "Thêm thất bại");
                }
            }else{
                tourDTO.setTourName(txtTen.getText());
                tourDTO.setNumberOfDate(soNgay);
                tourDTO.setPrice(BigDecimal.valueOf(donGia));
                tourDTO.setSeats(soCho);
                tourDTO.setDepartureLocation(txtDiaDiemKhoiHanh.getText());
                tourDTO.setImgLink(txtImgLink.getText());

                // field maLoaiTour
                TourTypeDTO selectedLoaiTour = (TourTypeDTO) cbLoaiTours.getSelectedItem();
                String maLoaiTour = selectedLoaiTour.getIdTourType();
                tourDTO.setIdTourType(maLoaiTour);

                // field maLoaiTour
                LocationDTO selectedLocation = (LocationDTO) cbDiaDiem.getSelectedItem();
                String maDiaDiem = selectedLocation.getIdLocation();
                tourDTO.setIdLocation(maDiaDiem);

                boolean result = tourBUS.editTour(tourDTO);
                if(result){
                    JOptionPane.showMessageDialog(this, "Chỉnh sửa thành công");
                    dispose();
                }else{
                    JOptionPane.showMessageDialog(this, "Chỉnh sửa thất bại");
                }
            }
        });
    }

    private void cancel(){
        cancelBtn = ButtonFactory.create("Hủy", UIColors.CANCEL);
        cancelBtn.addActionListener(e -> {
            dispose();
        });
    }

    private void chooseImage(){
        chooseImageBtn = ButtonFactory.create("Chọn ảnh...", UIColors.CHOSE);
        chooseImageBtn.addActionListener(e -> {
            FileDialog chooser = new FileDialog(this, "Chọn ảnh tour", FileDialog.LOAD);
            File currentDirectory = new File(System.getProperty("user.dir"));
            File rootDirectory = currentDirectory.toPath().getRoot().toFile();
            chooser.setDirectory(rootDirectory.getAbsolutePath());
            chooser.setFilenameFilter((directory, name) -> {
                String lowerCaseName = name.toLowerCase(Locale.ROOT);
                return lowerCaseName.endsWith(".png")
                        || lowerCaseName.endsWith(".jpg")
                        || lowerCaseName.endsWith(".jpeg")
                        || lowerCaseName.endsWith(".gif")
                        || lowerCaseName.endsWith(".bmp");
            });
            chooser.setVisible(true);

            String selectedFile = chooser.getFile();
            if(selectedFile != null){
                fileSelected = new File(chooser.getDirectory(), selectedFile);
                path = fileSelected.getAbsolutePath();
                txtImgLink.setText(path);

                ImageIcon icon = new ImageIcon(path);
                Image img = icon.getImage().getScaledInstance(120, 80, Image.SCALE_SMOOTH);
                jlbPreview.setIcon(new ImageIcon(img));
            }
        });
    }

    private boolean isEmpty(JTextField... fields){
        for(JTextField field : fields){
            if(field.getText().trim().isEmpty()){
                return true;
            }
        }
        return false;
    }

    private void requestFocusName(){
        addWindowListener(new WindowAdapter() {
            @Override
            public void windowOpened(WindowEvent e) {
                SwingUtilities.invokeLater(txtTen::requestFocusInWindow);
            }
        });
    }
}
