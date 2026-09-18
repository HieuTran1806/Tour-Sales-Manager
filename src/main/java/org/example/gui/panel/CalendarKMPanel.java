package org.example.gui.panel;

import lombok.AccessLevel;
import lombok.experimental.FieldDefaults;
import org.example.bus.CTrinhKMBUS;
import org.example.dto.CTrinhKMDTO;

import javax.swing.*;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import java.awt.*;
import java.awt.event.*;
import java.text.Normalizer;
import java.time.*;
import java.util.ArrayList;

@FieldDefaults(level = AccessLevel.PRIVATE)
public class CalendarKMPanel extends JPanel {

    JPanel calendarPanel;
    JLabel lblMonth;
    JTextField txtSearch;

    int month;
    int year;

    ArrayList<CTrinhKMDTO> listKM = new ArrayList<>();
    ArrayList<CTrinhKMDTO> filteredList = new ArrayList<>();

    CTrinhKMBUS bus;

    int currentIndex = -1;

    public CalendarKMPanel() {
        setLayout(new BorderLayout(10,10));

        bus = new CTrinhKMBUS();

        JPanel header = new JPanel(new BorderLayout());

        JPanel top = new JPanel(new BorderLayout());
        JButton btnPrev = new JButton("<");
        JButton btnNext = new JButton(">");

        lblMonth = new JLabel("", JLabel.CENTER);
        lblMonth.setFont(new Font("Arial", Font.BOLD, 18));
        //control
        JPanel controlPanel = new JPanel();

        JButton btnToday = new JButton("Today");
        JButton btnPrevKM = new JButton("Prev KM");
        JButton btnNextKM = new JButton("Next KM");

        controlPanel.add(btnToday);
        controlPanel.add(btnPrevKM);
        controlPanel.add(btnNextKM);

        top.add(btnPrev, BorderLayout.WEST);
        top.add(lblMonth, BorderLayout.CENTER);
        top.add(btnNext, BorderLayout.EAST);

        txtSearch = new JTextField();
        txtSearch.setBorder(BorderFactory.createTitledBorder("🔍 Tìm theo mã, tên, trạng thái"));

        header.add(top, BorderLayout.NORTH);
        header.add(txtSearch, BorderLayout.CENTER);
        header.add(controlPanel, BorderLayout.SOUTH);

        add(header, BorderLayout.NORTH);

        calendarPanel = new JPanel(new GridLayout(7,7,5,5));
        calendarPanel.setBackground(Color.WHITE);
        add(calendarPanel, BorderLayout.CENTER);

        LocalDate now = LocalDate.now();
        month = now.getMonthValue();
        year = now.getYear();

        loadCalendar();

        btnPrev.addActionListener(e -> changeMonth(-1));
        btnNext.addActionListener(e -> changeMonth(1));

        btnToday.addActionListener(e -> {
            LocalDate n = LocalDate.now();
            month = n.getMonthValue();
            year = n.getYear();
            currentIndex = -1;
            loadCalendar();
        });

        btnNextKM.addActionListener(e -> {
            if(filteredList.isEmpty()) return;

            currentIndex++;
            if(currentIndex >= filteredList.size()){
                currentIndex = 0;
            }

            jumpToKM(filteredList.get(currentIndex));
        });

        btnPrevKM.addActionListener(e -> {
            if(filteredList.isEmpty()) return;

            currentIndex--;
            if(currentIndex < 0){
                currentIndex = filteredList.size() - 1;
            }

            jumpToKM(filteredList.get(currentIndex));
        });

        // SEARCH REALTIME
        txtSearch.getDocument().addDocumentListener(new DocumentListener() {
            public void insertUpdate(DocumentEvent e) { loadCalendar(); }
            public void removeUpdate(DocumentEvent e) { loadCalendar(); }
            public void changedUpdate(DocumentEvent e) { loadCalendar(); }
        });

        addComponentListener(new ComponentAdapter() {
            public void componentShown(ComponentEvent e) {
                loadCalendar();
            }
        });
    }

    private void changeMonth(int delta){
        month += delta;

        if(month < 1){ month = 12; year--; }
        if(month > 12){ month = 1; year++; }

        loadCalendar();
    }

    private void loadCalendar(){

        bus.docDsCTrinhKM();
        listKM = bus.getDsCTrinhKM();

        String keyword = normalize(txtSearch.getText().trim());

        // FILTER LIST
        filteredList.clear();

        for(CTrinhKMDTO km : listKM){

            String maKM = normalize(km.getMaKM());
            String tenKM = normalize(km.getTenKM());

            LocalDate now = LocalDate.now();
            String trangThai;

            if(now.isBefore(km.getNgayBD())){
                trangThai = "sap";
            } else if(now.isAfter(km.getNgayKT())){
                trangThai = "het";
            } else {
                trangThai = "dang";
            }

            boolean match =
                    keyword.isEmpty()
                            || maKM.contains(keyword)
                            || tenKM.contains(keyword)
                            || trangThai.contains(keyword);

            if(match){
                filteredList.add(km);
            }
        }

        // AUTO JUMP KHI SEARCH
        if(!filteredList.isEmpty() && !keyword.isEmpty()){
            month = filteredList.get(0).getNgayBD().getMonthValue();
            year = filteredList.get(0).getNgayBD().getYear();
            currentIndex = 0;
        }

        calendarPanel.removeAll();

        lblMonth.setText("Tháng " + month + " / " + year);

        // HEADER THỨ
        String[] days = {"CN","T2","T3","T4","T5","T6","T7"};
        for(String d : days){
            JLabel lbl = new JLabel(d, JLabel.CENTER);
            lbl.setFont(new Font("Arial", Font.BOLD, 14));
            calendarPanel.add(lbl);
        }

        YearMonth ym = YearMonth.of(year, month);
        LocalDate firstDay = ym.atDay(1);

        int start = firstDay.getDayOfWeek().getValue() % 7;
        int totalDays = ym.lengthOfMonth();

        for(int i=0;i<start;i++){
            calendarPanel.add(new JPanel());
        }

        for(int day = 1; day <= totalDays; day++){
            calendarPanel.add(createDayPanel(day));
        }

        int remain = 42 - (start + totalDays);
        for(int i=0;i<remain;i++){
            calendarPanel.add(new JPanel());
        }

        calendarPanel.revalidate();
        calendarPanel.repaint();
    }

    private JPanel createDayPanel(int day){

        JPanel panel = new JPanel(new BorderLayout());
        panel.setBorder(BorderFactory.createLineBorder(Color.LIGHT_GRAY));

        LocalDate date = LocalDate.of(year, month, day);
        LocalDate today = LocalDate.now();

        String keyword = normalize(txtSearch.getText().trim());

        JLabel lblDay = new JLabel(String.valueOf(day));
        lblDay.setBorder(BorderFactory.createEmptyBorder(2,5,2,2));

        if(date.equals(today)){
            lblDay.setForeground(Color.BLUE);
        }

        panel.add(lblDay, BorderLayout.NORTH);

        JPanel kmPanel = new JPanel();
        kmPanel.setLayout(new BoxLayout(kmPanel, BoxLayout.Y_AXIS));

        for(CTrinhKMDTO km : listKM){

            if(isInRange(date, km.getNgayBD(), km.getNgayKT())){

                String maKM = normalize(km.getMaKM());
                String tenKM = normalize(km.getTenKM());

                LocalDate now = LocalDate.now();

                String trangThai;
                if(now.isBefore(km.getNgayBD())){
                    trangThai = "sap";
                } else if(now.isAfter(km.getNgayKT())){
                    trangThai = "het";
                } else {
                    trangThai = "dang";
                }

                boolean match =
                        keyword.isEmpty()
                                || maKM.contains(keyword)
                                || tenKM.contains(keyword)
                                || trangThai.contains(keyword);

                if(match){

                    JLabel lbl = new JLabel("• " + km.getTenKM());
                    lbl.setFont(new Font("Arial", Font.PLAIN, 11));

                    if(trangThai.equals("dang")) lbl.setForeground(new Color(0,150,0));
                    else if(trangThai.equals("sap")) lbl.setForeground(Color.ORANGE);
                    else lbl.setForeground(Color.GRAY);

                    kmPanel.add(lbl);
                }
            }
        }

        panel.add(kmPanel, BorderLayout.CENTER);

        // HIGHLIGHT KM ĐANG CHỌN
        if(currentIndex >= 0 && currentIndex < filteredList.size()){
            CTrinhKMDTO currentKM = filteredList.get(currentIndex);

            if(isInRange(date, currentKM.getNgayBD(), currentKM.getNgayKT())){
                panel.setBorder(BorderFactory.createLineBorder(Color.RED, 2));
            }
        }

        panel.addMouseListener(new MouseAdapter() {
            public void mouseClicked(MouseEvent e) {
                showDetail(date);
            }
        });

        return panel;
    }

    private void jumpToKM(CTrinhKMDTO km){
        month = km.getNgayBD().getMonthValue();
        year = km.getNgayBD().getYear();
        loadCalendar();
    }

    private String normalize(String s){
        if(s == null) return "";
        return Normalizer.normalize(s, Normalizer.Form.NFD)
                .replaceAll("\\p{InCombiningDiacriticalMarks}+", "")
                .toLowerCase();
    }

    private boolean isInRange(LocalDate d, LocalDate s, LocalDate e){
        return (!d.isBefore(s) && !d.isAfter(e));
    }

    private void showDetail(LocalDate date){
        StringBuilder detail = new StringBuilder("📅 Khuyến mãi ngày " + date + ":\n\n");

        for(CTrinhKMDTO km : listKM){
            if(isInRange(date, km.getNgayBD(), km.getNgayKT())){
                detail.append("- ").append(km.getTenKM()).append("\n");
            }
        }
        JOptionPane.showMessageDialog(this, detail.toString());
    }
}