package org.example.dto;

import lombok.*;
import lombok.experimental.FieldDefaults;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@FieldDefaults(level = AccessLevel.PRIVATE)
public class TourDTO {
    String maTour;
    String ten;
    int soNgay;
    long donGia;
    int soCho;
    String diaDiemKhoiHanh;
    String imgLink;
    String maLoaiTour;
    String maDiaDiem;

    @Override
    public String toString(){
        return maTour + " - " + ten;
    }
}
