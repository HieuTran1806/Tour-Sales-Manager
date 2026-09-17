package org.example.dto;

import lombok.*;
import lombok.experimental.FieldDefaults;

import java.time.LocalDate;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@FieldDefaults(level = AccessLevel.PRIVATE)
public class KeHoachTourDTO {
    String maKHTour;
    LocalDate ngayKhoiHanh;
    LocalDate ngayKetThuc;
    int tongSoVe;
    long tongChiDuKien;
    int soVeConLai;
    String trangThai;
    String maTour;
    String maNVHD;
}
