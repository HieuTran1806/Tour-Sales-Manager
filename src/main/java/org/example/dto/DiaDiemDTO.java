package org.example.dto;

public class DiaDiemDTO {
    public String MaDiaDiem;
    public String TenDiaDiem;
    public String DiaChi;
    public String QuocGia;

    public DiaDiemDTO(String MaDiaDiem ,String TenDiaDiem, String DiaChi, String QuocGia) {
        this.MaDiaDiem = MaDiaDiem;
        this.TenDiaDiem = TenDiaDiem;
        this.DiaChi = DiaChi;
        this.QuocGia = QuocGia;
    }

    public DiaDiemDTO() {
        this.MaDiaDiem = MaDiaDiem;
        this.TenDiaDiem = TenDiaDiem;
        this.DiaChi = DiaChi;
        this.QuocGia = QuocGia;
    }

    public String getMaDiaDiem() {
        return MaDiaDiem;
    }

    public String getTenDiaDiem() {
        return TenDiaDiem;
    }

    public String getdiachi() {
        return DiaChi;
    }

    public String getQuocGia() {
        return QuocGia;
    }

    public void setMaDiaDiem(String maDiaDiem) {
        MaDiaDiem = maDiaDiem;
    }

    public void setTenDiaDiem(String TenDiaDiem) {
        this.TenDiaDiem = TenDiaDiem;
    }

    public void setdiachi(String diachi) {
        this.DiaChi = diachi;
    }

    public void setQuocGia(String QuocGia) {
        this.QuocGia = QuocGia;
    }

    @Override
    public String toString(){
        return MaDiaDiem + " - " + TenDiaDiem;
    }
}