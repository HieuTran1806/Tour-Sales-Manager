package org.example.dto;

/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Other/File.java to edit this template
 */

/**
 *
 * @author Nhat
 */
public class CTietHDDTO {
    private String MaHD;
    private String MaKHDi;
    private float GiaVe;

    public CTietHDDTO(String MaHD, String MaKHDi, float GiaVe) {
        this.MaHD = MaHD;
        this.MaKHDi = MaKHDi;
        this.GiaVe = GiaVe;
    }

    public String getMaHD() {
        return MaHD;
    }

    public String getMaKHDi() {
        return MaKHDi;
    }

    public float getGiaVe() {
        return GiaVe;
    }

    public void setMaHD(String MaHD) {
        this.MaHD = MaHD;
    }

    public void setMaKHDi(String MaKHDi) {
        this.MaKHDi = MaKHDi;
    }

    public void setGiaVe(float GiaVe) {
        this.GiaVe = GiaVe;
    }
}
