package org.example.dto;

import lombok.*;
import lombok.experimental.FieldDefaults;

import java.time.LocalDate;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@FieldDefaults(level = AccessLevel.PRIVATE)
public class CTrinhKMDTO {
    private String maKM;
    private String tenKM;
    private LocalDate ngayBD;
    private LocalDate ngayKT;
    private boolean hinhThucKM;
    private float ChietKhau;
    private String GhiChu;

    @Override
    public String toString(){
        return maKM + " - Giảm" + ChietKhau + "%";
    }
}
