package org.example.dto;

import lombok.*;
import lombok.experimental.FieldDefaults;

import java.time.LocalDate;
import java.util.ArrayList;

@Getter
@Setter
@FieldDefaults(level = AccessLevel.PRIVATE)
public class KMTourDTO extends CTrinhKMDTO {
    ArrayList<String> dsMaTour;

    public KMTourDTO() {
        super();
    }

    public KMTourDTO(String maKM, String tenKM, LocalDate ngayBD, LocalDate ngayKT, boolean hinhThucKM, float chietKhau, String ghiChu, ArrayList<String> dsMaTour) {
        super(maKM, tenKM, ngayBD, ngayKT, hinhThucKM, chietKhau, ghiChu);
        this.dsMaTour = dsMaTour;
    }
}