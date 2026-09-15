package org.example.dto;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.FieldDefaults;

import java.time.LocalDate;

@Getter
@Setter
@FieldDefaults(level = AccessLevel.PRIVATE)
public class KMHDDTO extends CTrinhKMDTO {
    float tongTienApDung;

    public KMHDDTO() {
    }

    public KMHDDTO(String maKM, String tenKM, LocalDate ngayBD, LocalDate ngayKT, boolean hinhThucKM, float chietKhau, String ghiChu, float tongTienApDung) {
        super(maKM, tenKM, ngayBD, ngayKT, hinhThucKM, chietKhau, ghiChu);
        this.tongTienApDung = tongTienApDung;
    }
}