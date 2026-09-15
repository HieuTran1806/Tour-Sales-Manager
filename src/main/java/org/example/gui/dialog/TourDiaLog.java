package org.example.gui.dialog;

import lombok.AccessLevel;
import lombok.experimental.FieldDefaults;
import org.example.bus.DiaDiemBUS;
import org.example.bus.LoaiTourBUS;
import org.example.bus.TourBUS;
import org.example.dto.DiaDiemDTO;
import org.example.dto.LoaiTourDTO;
import org.example.dto.TourDTO;
import org.example.gui.panel.UIColors;

import javax.swing.*;
import java.awt.*;
import java.io.File;
import java.util.ArrayList;

@FieldDefaults(level = AccessLevel.PRIVATE)
public class TourDiaLog extends JDialog {
    // label and txt
    JLabel jlbMaTour, jlbTen, jlbSoNgay, jlbDonGia, jlbSoCho, jlbDiaDiemKhoiHanh, jlbMaLoaiTour, jlbImgLink, jlbPreview, jlbMaDiaDiem;
    JTextField txtMaTour, txtTen, txtSoNgay, txtDonGia, txtSoCho, txtDiaDiemKhoiHanh, txtImgLink;

    // define btn
    JButton saveBtn, cancelBtn, chooseImageBtn;

    String path;

    File fileSelected;

    // define relate panel
    JPanel formPanel, southPanel;

    TourBUS tourBUS;
    TourDTO tourDTO;
    LoaiTourBUS loaiTourBUS;
    DiaDiemBUS diaDiemBUS;
    JComboBox<LoaiTourDTO> cbLoaiTours;
    JComboBox<DiaDiemDTO> cbDiaDiem;

    public TourDiaLog(TourBUS tourBUS, TourDTO tourDTO){
        this.tourDTO = tourDTO;
        this.tourBUS = tourBUS;
        loaiTourBUS = new LoaiTourBUS();
        diaDiemBUS = new DiaDiemBUS();
        cbLoaiTours = new JComboBox<>();
        cbDiaDiem = new JComboBox<>();

        setTitle(tourDTO == null ? "Thêm Tour" : "Sửa Tour");
        setSize(300, 360);
        setLocationRelativeTo(null);
        setModal(true);

        init();
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
        jlbMaTour = new JLabel("Mã tour");
        jlbMaTour.setBorder(BorderFactory.createEmptyBorder(5, 5, 5, 0));
        formPanel.add(jlbMaTour);
        txtMaTour = new JTextField();
        formPanel.add(txtMaTour);

        // row ten
        jlbTen = new JLabel("Tên");
        jlbTen.setBorder(BorderFactory.createEmptyBorder(5, 5, 5, 0));
        formPanel.add(jlbTen);
        txtTen = new JTextField();
        formPanel.add(txtTen);

        //row soNgay
        jlbSoNgay = new JLabel("Số ngày");
        jlbSoNgay.setBorder(BorderFactory.createEmptyBorder(5, 5, 5, 0));
        formPanel.add(jlbSoNgay);
        txtSoNgay = new JTextField();
        formPanel.add(txtSoNgay);

        // row donGia
        jlbDonGia = new JLabel("Đơn giá");
        jlbDonGia.setBorder(BorderFactory.createEmptyBorder(5, 5, 5, 0));
        formPanel.add(jlbDonGia);
        txtDonGia = new JTextField();
        formPanel.add(txtDonGia);

        //row soNguoi
        jlbSoCho = new JLabel("Số chỗ");
        jlbSoCho.setBorder(BorderFactory.createEmptyBorder(5, 5, 5, 0));
        formPanel.add(jlbSoCho);
        txtSoCho = new JTextField();
        formPanel.add(txtSoCho);

        // row ddkhoihanh
        jlbDiaDiemKhoiHanh = new JLabel("Địa điểm khởi hành");
        jlbDiaDiemKhoiHanh.setBorder(BorderFactory.createEmptyBorder(5, 5, 5, 0));
        formPanel.add(jlbDiaDiemKhoiHanh);
        txtDiaDiemKhoiHanh = new JTextField();
        formPanel.add(txtDiaDiemKhoiHanh);

        // row maLoaiTour
        jlbMaLoaiTour = new JLabel("Mã loại tour");
        jlbMaLoaiTour.setBorder(BorderFactory.createEmptyBorder(5, 5, 5, 0));
        formPanel.add(jlbMaLoaiTour);

        ArrayList<LoaiTourDTO> lsCate = loaiTourBUS.getAllLoaiTour();
        DefaultComboBoxModel<LoaiTourDTO> loaiToursModel = new DefaultComboBoxModel<>();
        for(LoaiTourDTO lt : lsCate){
            loaiToursModel.addElement(lt);
        }
        cbLoaiTours.setModel(loaiToursModel);
        formPanel.add(cbLoaiTours);

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

        // row maDiaDiem
        jlbMaDiaDiem = new JLabel("Mã Địa điểm");
        jlbMaDiaDiem.setBorder(BorderFactory.createEmptyBorder(5, 5, 5, 0));
        formPanel.add(jlbMaDiaDiem);

        ArrayList<DiaDiemDTO> lsDiaDiem = DiaDiemBUS.ds;
        diaDiemBUS.DocDs();
        DefaultComboBoxModel<DiaDiemDTO> diaDiemModel = new DefaultComboBoxModel<>();
        for(DiaDiemDTO dd : lsDiaDiem){
            diaDiemModel.addElement(dd);
        }
        cbDiaDiem.setModel(diaDiemModel);
        // set txt DiaDiemKhoiHanh when init
        cbDiaDiem.addActionListener(e -> {
            DiaDiemDTO selected = (DiaDiemDTO) cbDiaDiem.getSelectedItem();
            if(selected != null){
                txtDiaDiemKhoiHanh.setText(selected.getDiaChi());
            }
        });
        formPanel.add(cbDiaDiem);

        add(formPanel, BorderLayout.CENTER);
        add(southPanel, BorderLayout.SOUTH);
    }

    private void loadData(){
        // load txt
        txtMaTour.setText(tourDTO.getMaTour());
        txtMaTour.setEnabled(false);

        txtTen.setText(tourDTO.getTen());
        txtSoNgay.setText(tourDTO.getSoNgay() + "");
        txtDonGia.setText(tourDTO.getDonGia() + "");
        txtSoCho.setText(tourDTO.getSoCho() + "");

        //field diaDiemKhoihanh
        for(int i = 0; i < cbDiaDiem.getItemCount(); i++){
            DiaDiemDTO dd = cbDiaDiem.getItemAt(i);
            if(dd.getMaDiaDiem().equalsIgnoreCase(tourDTO.getMaDiaDiem())){
                cbDiaDiem.setSelectedIndex(i);
                txtDiaDiemKhoiHanh.setText(dd.getDiaChi());
                break;
            }
        }

        //field maLoaiTour
        for(int i = 0; i < cbLoaiTours.getItemCount(); i++){
            LoaiTourDTO lt = cbLoaiTours.getItemAt(i);
            if(lt.getMaLoaiTour().equalsIgnoreCase(tourDTO.getMaLoaiTour())){
                cbLoaiTours.setSelectedIndex(i);
                break;
            }
        }
        LoaiTourDTO selectedLoaiTour = (LoaiTourDTO) cbLoaiTours.getSelectedItem();
        tourDTO.setMaLoaiTour(selectedLoaiTour.getMaLoaiTour());

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
            DiaDiemDTO dd = cbDiaDiem.getItemAt(i);
            if(dd.getMaDiaDiem().equalsIgnoreCase(tourDTO.getMaDiaDiem())){
                cbDiaDiem.setSelectedIndex(i);
                break;
            }
        }
    }

    private JButton createBtn(String text, Color color){
        JButton btn = new JButton(text);
        btn.setBackground(color);
        btn.setForeground(Color.WHITE);
        btn.setFocusPainted(false);
        btn.setCursor(new Cursor(Cursor.HAND_CURSOR));// Trong jpBtn panel

        return btn;
    }

    private void save(){
        saveBtn = createBtn("Lưu", UIColors.SAVE);
        saveBtn.addActionListener(e -> {
            if(isEmpty(txtMaTour, txtTen, txtSoNgay, txtDonGia, txtSoCho, txtDiaDiemKhoiHanh, txtImgLink)){
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
                if(tourBUS.existedTourWithID(txtMaTour.getText())){
                    JOptionPane.showMessageDialog(null, "Mã tour đã tồn tại, vui lòng nhập mã khác!");
                }else{
                    LoaiTourDTO selectedLoaiTour = (LoaiTourDTO) cbLoaiTours.getSelectedItem();
                    String maLoaiTour = selectedLoaiTour.getMaLoaiTour();
                    DiaDiemDTO selectedLocation = (DiaDiemDTO) cbDiaDiem.getSelectedItem();
                    String maDiaDiem = selectedLocation.getMaDiaDiem();

                    TourDTO tourMoi = new TourDTO(
                            txtMaTour.getText(), txtTen.getText(),
                            soNgay, donGia, soCho, txtDiaDiemKhoiHanh.getText(),
                            txtImgLink.getText(), maLoaiTour, maDiaDiem
                    );
                    boolean result = tourBUS.addTour(tourMoi);
                    if (result){
                        JOptionPane.showMessageDialog(this, "Đã thêm");
                        dispose();
                    }else{
                        JOptionPane.showMessageDialog(this, "Thêm thất bại");
                    }
                }
            }else{
                tourDTO.setTen(txtTen.getText());
                tourDTO.setSoNgay(soNgay);
                tourDTO.setDonGia(donGia);
                tourDTO.setSoCho(soCho);
                tourDTO.setDiaDiemKhoiHanh(txtDiaDiemKhoiHanh.getText());
                tourDTO.setImgLink(txtImgLink.getText());

                // field maLoaiTour
                LoaiTourDTO selectedLoaiTour = (LoaiTourDTO) cbLoaiTours.getSelectedItem();
                String maLoaiTour = selectedLoaiTour.getMaLoaiTour();
                tourDTO.setMaLoaiTour(maLoaiTour);

                // field maLoaiTour
                DiaDiemDTO selectedLocation = (DiaDiemDTO) cbDiaDiem.getSelectedItem();
                String maDiaDiem = selectedLocation.getMaDiaDiem();
                tourDTO.setMaDiaDiem(maDiaDiem);

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
        cancelBtn = createBtn("Hủy", UIColors.CANCEL);
        cancelBtn.addActionListener(e -> {
            dispose();
        });
    }

    private void chooseImage(){
        chooseImageBtn = createBtn("Chọn", UIColors.CHOSE);
        chooseImageBtn.addActionListener(e -> {
            JFileChooser chooser = new JFileChooser();
            int result = chooser.showOpenDialog(this);

            if(result == JFileChooser.APPROVE_OPTION){
                fileSelected = chooser.getSelectedFile();
                path = fileSelected.getAbsolutePath(); // get absolute path of file

                txtImgLink.setText(path);

                ImageIcon icon = new ImageIcon(path);

                Image img = icon.getImage().getScaledInstance(
                        120,
                        80,
                        Image.SCALE_SMOOTH
                );

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
}
