package org.example.dto;

import java.time.LocalDate;

public class KeHoachTourDTO {
    private String maKHTour;
    private LocalDate ngayKhoiHanh;
    private LocalDate ngayKetThuc;
    private int tongSoVe;
    private long tongChiDuKien;
    private long tongThuDuKien;
    private int soVeConLai;
    private String trangThai;
    private String maTour;
    private String maNVHD;

    public KeHoachTourDTO() {
        this.maKHTour = "";
        this.ngayKhoiHanh = null;
        this.ngayKetThuc = null;
        this.tongSoVe = 0;
        this.tongChiDuKien = 0;
        this.tongThuDuKien = 0;
        this.soVeConLai = 0;
        this.trangThai = "";
        this.maTour = "";
        this.maNVHD = "";
    }

    public KeHoachTourDTO(String maKHTour, LocalDate ngayKhoiHanh, LocalDate ngayKetThuc, int tongSoVe, long tongChiDuKien, long tongThuDuKien,int soVeConLai, String trangThai, String maTour, String maNVHD) {
        this.maKHTour = maKHTour;
        this.ngayKhoiHanh = ngayKhoiHanh;
        this.ngayKetThuc = ngayKetThuc;
        this.tongSoVe = tongSoVe;
        this.tongChiDuKien = tongChiDuKien;
        this.tongThuDuKien = tongThuDuKien;
        this.soVeConLai = 0;
        this.trangThai = "";
        this.maTour = maTour;
        this.maNVHD = maNVHD;
    }

    public void setMaKHTour(String maKHTour) {
        this.maKHTour = maKHTour;
    }

    public void setNgayKhoiHanh(LocalDate ngayKhoiHanh) {
        this.ngayKhoiHanh = ngayKhoiHanh;
    }

    public void setNgayKetThuc(LocalDate ngayKetThuc) {
        this.ngayKetThuc = ngayKetThuc;
    }

    public void setTongSoVe(int tongSoVe) {
        this.tongSoVe = tongSoVe;
    }

    public void setTongChiDuKien(long tongChiDuKien) {
        this.tongChiDuKien = tongChiDuKien;
    }

    public void setTongThuDuKien(long tongThuDuKien) {
        this.tongThuDuKien = tongThuDuKien;
    }

    public void setMaTour(String maTour){
        this.maTour = maTour;
    }

    public void setMaNVHD(String maNVHD){
        this.maNVHD = maNVHD;
    }


    public String getMaKHTour() {
        return maKHTour;
    }

    public LocalDate getNgayKhoiHanh() {
        return ngayKhoiHanh;
    }

    public LocalDate getNgayKetThuc() {
        return ngayKetThuc;
    }

    public int getTongSoVe() {
        return tongSoVe;
    }

    public long getTongChiDuKien() {
        return tongChiDuKien;
    }

    public long getTongThuDuKien() {
        return tongThuDuKien;
    }

    public int getSoVeConLai() {
        return soVeConLai;
    }

    public void setSoVeConLai(int soVeConLai) {
        this.soVeConLai = soVeConLai;
    }

    public String getTrangThai() {
        return trangThai;
    }

    public void setTrangThai(String trangThai) {
        this.trangThai = trangThai;
    }

    public String getMaTour() {
        return maTour;
    }

    public String getMaNVHD() {
        return maNVHD;
    }
}
