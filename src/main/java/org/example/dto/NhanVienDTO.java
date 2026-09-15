package org.example.dto;

import java.time.LocalDate;

public class NhanVienDTO extends Person {
    private String maNV;
    private String chucVu;

    public NhanVienDTO() {
    }

    public NhanVienDTO(String maNV, String chucVu, String ho, String ten, String diaChi, String sdt, LocalDate ngaySinh) {
        super(ho, ten, diaChi, sdt, ngaySinh);
        this.maNV = maNV;
        this.chucVu = chucVu;
    }

    public String getMaNV() {
        return maNV;
    }

    public void setMaNV(String maNV) {
        this.maNV = maNV;
    }

    public String getChucVu() {
        return chucVu;
    }

    public void setChucVu(String chucVu) {
        this.chucVu = chucVu;
    }

    @Override
    public String toString() {
        return maNV + " - " + super.getHo() + " " + super.getTen();
    }
}