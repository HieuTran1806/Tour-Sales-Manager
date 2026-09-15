package org.example.dto;
import lombok.*;
import lombok.experimental.FieldDefaults;

import java.time.LocalDate;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@FieldDefaults(level = AccessLevel.PRIVATE)
public class HoaDonDTO {
    String MaHD;
    String MaKHTour;
    String MaKHDat;
    String MaNV;
    LocalDate ngay;
    int soLuong;
    String maKM;
    float TongTien;
}
