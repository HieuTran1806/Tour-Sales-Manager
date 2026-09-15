package org.example.dto;

public class TourDTO {
    private String maTour;
    private String ten;
    private int soNgay;
    private long donGia;
    private int soCho;
    private String diaDiemKhoiHanh;
    private String imgLink;
    private String maLoaiTour;
    private String maDiaDiem;

    public TourDTO(){
        this.maTour = "";
        this.ten = "";
        this.soNgay = 0;
        this.donGia = 0;
        this.soCho = 0;
        this.diaDiemKhoiHanh = "";
        this.imgLink = "";
        this.maLoaiTour = "";
        this.maDiaDiem = "";
    }

    public TourDTO(String maTour, String ten, int soNgay, long donGia, int soCho, String diaDiemKhoiHanh, String imgLink, String maLoaiTour, String maDiaDiem) {
        this.maTour = maTour;
        this.ten = ten;
        this.soNgay = soNgay;
        this.donGia = donGia;
        this.soCho = soCho;
        this.diaDiemKhoiHanh = diaDiemKhoiHanh;
        this.imgLink = imgLink;
        this.maLoaiTour = maLoaiTour;
        this.maDiaDiem = maDiaDiem;
    }

    public String getMaTour() {
        return maTour;
    }

    public void setMaTour(String maTour) {
        this.maTour = maTour;
    }

    public String getTen() {
        return ten;
    }

    public void setTen(String ten) {
        this.ten = ten;
    }

    public int getSoNgay() {
        return soNgay;
    }

    public void setSoNgay(int soNgay) {
        this.soNgay = soNgay;
    }

    public long getDonGia() {
        return donGia;
    }

    public void setDonGia(long donGia) {
        this.donGia = donGia;
    }

    public int getSoCho() {
        return soCho;
    }

    public void setSoCho(int soCho) {
        this.soCho = soCho;
    }

    public String getDiaDiemKhoiHanh() {
        return diaDiemKhoiHanh;
    }

    public void setDiaDiemKhoiHanh(String diaDiemKhoiHanh) {
        this.diaDiemKhoiHanh = diaDiemKhoiHanh;
    }

    public void setImgLink(String imgLink){
        this.imgLink = imgLink;
    }

    public String getImgLink(){
        return imgLink;
    }

    public String getMaLoaiTour() {
        return maLoaiTour;
    }
    public void setMaLoaiTour(String maLoaiTour) {
        this.maLoaiTour = maLoaiTour;
    }

    public String getMaDiaDiem() {
        return maDiaDiem;
    }

    public void setMaDiaDiem(String maDiaDiem) {
        this.maDiaDiem = maDiaDiem;
    }

    @Override
    public String toString(){
        return maTour + " - " + ten;
    }
}
