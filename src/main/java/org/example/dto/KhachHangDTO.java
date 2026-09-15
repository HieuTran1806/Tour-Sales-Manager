package org.example.dto;

import lombok.*;
import lombok.experimental.FieldDefaults;

import java.time.LocalDate;

@Getter
@Setter
@FieldDefaults(level = AccessLevel.PRIVATE)
public class KhachHangDTO extends Person {
    String maKH;

    public KhachHangDTO() {
    }

    public KhachHangDTO(String maKH, String ho, String ten, String diaChi, String sdt, LocalDate ngaySinh) {
        super(ho, ten, diaChi, sdt, ngaySinh);
        this.maKH = maKH;
    }
}