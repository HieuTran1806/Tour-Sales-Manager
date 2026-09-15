package org.example.dto;

import java.time.LocalDate;
import java.util.*;
public class HoaDonDTO {

    private String MaHD;
    private String MaKHTour;
    private String MaKHDat;
    private String MaNV;
    private LocalDate ngay;
    private int soLuong;
    private String maKM;
    private float TongTien;

    public HoaDonDTO(String MaHD, String MaKHTour, String MaKHDat, String MaNV, LocalDate ngay, int soLuong, String maKM, float TongTien) {
        this.MaHD = MaHD;
        this.MaKHTour = MaKHTour;
        this.MaKHDat = MaKHDat;
        this.MaNV = MaNV;
        this.ngay=ngay;
        this.soLuong=soLuong;
        this.maKM = maKM;
        this.TongTien = TongTien;
    }

    public void setNgay(LocalDate ngay) {
        this.ngay = ngay;
    }

    public LocalDate getNgay() {
        return ngay;
    }

    public void setSoLuong(int soLuong) {
        this.soLuong = soLuong;
    }

    public int getSoLuong() {
        return soLuong;
    }

    public String getMaHD() {
        return MaHD;
    }

    public String getMaKHTour() {
        return MaKHTour;
    }

    public String getMaKHDat() {
        return MaKHDat;
    }

    public String getMaNV() {
        return MaNV;
    }

    public float getTongTien() {
        return TongTien;
    }

    public void setMaHD(String MaHD) {
        this.MaHD = MaHD;
    }

    public void setMaKHTour(String MaKHTour) {
        this.MaKHTour = MaKHTour;
    }

    public void setMaKHDat(String MaKHDat) {
        this.MaKHDat = MaKHDat;
    }

    public void setMaNV(String MaNV) {
        this.MaNV = MaNV;
    }

    public void setTongTien(float TongTien) {
        this.TongTien = TongTien;
    }

    public void setMaKM(String maKM) {
        this.maKM = maKM;
    }

    public String getMaKM() {
        return maKM;
    }
}
