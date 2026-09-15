package org.example.dto;

import java.time.LocalDate;

public class CTrinhKMDTO {
    private String maKM;
    private String tenKM;
    private LocalDate ngayBD;
    private LocalDate ngayKT;
    private boolean hinhThucKM;
    private float ChietKhau;
    private String GhiChu;

    public CTrinhKMDTO() {
    }

    public CTrinhKMDTO(String maKM, String tenKM, LocalDate ngayBD, LocalDate ngayKT, boolean hinhThucKM, float chietKhau, String ghiChu) {
        this.maKM = maKM;
        this.tenKM = tenKM;
        this.ngayBD = ngayBD;
        this.ngayKT = ngayKT;
        this.hinhThucKM = hinhThucKM;
        this.ChietKhau = chietKhau;
        this.GhiChu = ghiChu;
    }

    public String getMaKM() {
        return maKM;
    }

    public void setMaKM(String maKM) {
        this.maKM = maKM;
    }

    public String getTenKM() {
        return tenKM;
    }

    public void setTenKM(String tenKM) {
        this.tenKM = tenKM;
    }

    public LocalDate getNgayBD() {
        return ngayBD;
    }

    public void setNgayBD(LocalDate ngayBD) {
        this.ngayBD = ngayBD;
    }

    public LocalDate getNgayKT() {
        return ngayKT;
    }

    public void setNgayKT(LocalDate ngayKT) {
        this.ngayKT = ngayKT;
    }

    public boolean getHinhThucKM() {
        return hinhThucKM;
    }

    public void setHinhThucKM(boolean hinhThucKM) {
        hinhThucKM = hinhThucKM;
    }

    public String getGhiChu() {
        return GhiChu;
    }

    public void setGhiChu(String ghiChu) {
        GhiChu = ghiChu;
    }

    public float getChietKhau() {
        return ChietKhau;
    }

    public void setChietKhau(float chietKhau) {
        ChietKhau = chietKhau;
    }

    @Override
    public String toString(){
        return maKM + " - Giảm" + ChietKhau + "%";
    }
}
