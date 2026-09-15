package org.example.dto;

import java.time.LocalDate;
public class KhachHangDTO extends Person {
    private String maKH;

    public KhachHangDTO() {
    }

    public KhachHangDTO(String maKH, String ho, String ten, String diaChi, String sdt, LocalDate ngaySinh) {
        super(ho, ten, diaChi, sdt, ngaySinh);
        this.maKH = maKH;
    }

    public String getMaKH() {
        return maKH;
    }

    public void setMaKH(String maKH) {
        this.maKH = maKH;
    }

}