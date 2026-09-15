package org.example.dto;

import java.time.LocalDate;
import java.util.ArrayList;

public class KMTourDTO extends CTrinhKMDTO {
    private ArrayList<String> dsMaTour;

    public KMTourDTO() {
        this.dsMaTour = new ArrayList<>();
    }

    public KMTourDTO(String maKM, String tenKM, LocalDate ngayBD, LocalDate ngayKT, boolean hinhThucKM, float chietKhau, String ghiChu, ArrayList<String> dsMaTour) {
        super(maKM, tenKM, ngayBD, ngayKT, hinhThucKM, chietKhau, ghiChu);
        this.dsMaTour = dsMaTour;
    }

    public ArrayList<String> getDsMaTour() {
        return dsMaTour;
    }

    public void setDsMaTour(ArrayList<String> dsMaTour) {
        this.dsMaTour = dsMaTour;
    }
}