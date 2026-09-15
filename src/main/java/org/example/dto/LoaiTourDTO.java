package org.example.dto;

public class LoaiTourDTO {
    private String maLoaiTour;
    private String theLoai;
    private String moTa;
    private int trangThai; // 1: Đang hoạt động, 0: Ngưng

    public LoaiTourDTO(){
        this.maLoaiTour = "";
        this.theLoai = "";
        this.moTa = "";
        this.trangThai = 1; // mặc định là đang hoạt đông
    }

    public LoaiTourDTO(String maLoaiTour, String theLoai, String moTa, int trangThai) {
        this.maLoaiTour = maLoaiTour;
        this.theLoai = theLoai;
        this.moTa = moTa;
        this.trangThai = trangThai;
    }

    public void setMaLoaiTour(String maLoaiTour) {
        this.maLoaiTour = maLoaiTour;
    }

    public void setTheLoai(String theLoai) {
        this.theLoai = theLoai;
    }

    public void setMoTa(String moTa) {
        this.moTa = moTa;
    }

    public void setTrangThai(int trangThai) {
        this.trangThai = trangThai;
    }

    public String getMaLoaiTour() {
        return maLoaiTour;
    }

    public String getTheLoai() {
        return theLoai;
    }

    public String getMoTa() {
        return moTa;
    }

    public int getTrangThai() {
        return trangThai;
    }

    @Override
    public String toString(){
        return maLoaiTour + " - " + theLoai;
    }
}
