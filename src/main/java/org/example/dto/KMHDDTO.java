package org.example.dto;

import java.time.LocalDate;

public class KMHDDTO extends CTrinhKMDTO {
    private float tongTienApDung;

    public KMHDDTO() {
    }

    public KMHDDTO(String maKM, String tenKM, LocalDate ngayBD, LocalDate ngayKT, boolean hinhThucKM, float chietKhau, String ghiChu, float tongTienApDung) {
        super(maKM, tenKM, ngayBD, ngayKT, hinhThucKM, chietKhau, ghiChu);
        this.tongTienApDung = tongTienApDung;
    }

    public float getTongTienApDung() {
        return tongTienApDung;
    }

    public void setTongTienApDung(float tongTienApDung) {
        this.tongTienApDung = tongTienApDung;
    }

}