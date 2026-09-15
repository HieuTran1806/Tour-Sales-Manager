package org.example.dto;

public class CTietKHTourDTO {
    private String maCTietKHTour;
    private String ngayThucHien;
    private long tongChi;
    private long tienO;
    private long tienAn;
    private long tienDiLai;
    private String diemDi;
    private String diemDen;
    private String maKHTour;

    public CTietKHTourDTO() {
        this.maCTietKHTour = "";
        this.ngayThucHien = "";
        this.tongChi = 0;
        this.tienO = 0;
        this.tienAn = 0;
        this.tienDiLai = 0;
        this.diemDi =  "";
        this.diemDen =  "";
        this.maKHTour =  "";
    }

    public CTietKHTourDTO(String maCTietKHTour, String ngayThucHien, long tongChi, long tienO, long tienAn, long tienDiLai, String diemDi, String diemDen, String maKHTour) {
        this.maCTietKHTour = maCTietKHTour;
        this.ngayThucHien = ngayThucHien;
        this.tongChi = tongChi;
        this.tienO = tienO;
        this.tienAn = tienAn;
        this.tienDiLai = tienDiLai;
        this.diemDi = diemDi;
        this.diemDen = diemDen;
        this.maKHTour = maKHTour;
    }

    public void setMaCTietKHTour(String maCTietKHTour) {
        this.maCTietKHTour = maCTietKHTour;
    }

    public void setNgayThucHien(String ngayThucHien) {
        this.ngayThucHien = ngayThucHien;
    }

    public void setTongChi(long tongChi) {
        this.tongChi = tongChi;
    }

    public void setTienO(long tienO) {
        this.tienO = tienO;
    }

    public void setTienAn(long tienAn) {
        this.tienAn = tienAn;
    }

    public void setTienDiLai(long tienDiLai) {
        this.tienDiLai = tienDiLai;
    }

    public void setDiemDi(String diemDi) {
        this.diemDi = diemDi;
    }

    public void setDiemDen(String diemDen) {
        this.diemDen = diemDen;
    }

    public void setMaKHTour(String maKHTour) {
        this.maKHTour = maKHTour;
    }

    public String getMaCTietKHTour() {
        return maCTietKHTour;
    }

    public String getNgayThucHien() {
        return ngayThucHien;
    }

    public long getTongChi() {
        return tongChi;
    }

    public long getTienO() {
        return tienO;
    }

    public long getTienAn() {
        return tienAn;
    }

    public long getTienDiLai() {
        return tienDiLai;
    }

    public String getDiemDi() {
        return diemDi;
    }

    public String getDiemDen() {
        return diemDen;
    }

    public String getMaKHTour() {
        return maKHTour;
    }
}
