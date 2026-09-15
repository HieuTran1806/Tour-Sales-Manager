package org.example.dto;


import lombok.*;
import lombok.experimental.FieldDefaults;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@FieldDefaults(level = AccessLevel.PRIVATE)
public class CTietKHTourDTO {
    private String maCTietKHTour;
    private String ngayThucHien;
    private long tongChi;
    private long tienO;
    private long tienAn;
    private long tienDiLai;
    private String diemDi;
    private String diemDen;
    private String maKHTour;
}
