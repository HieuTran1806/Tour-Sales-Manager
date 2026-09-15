package org.example.dto;

import lombok.*;
import lombok.experimental.FieldDefaults;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@FieldDefaults(level = AccessLevel.PRIVATE)
public class DiaDiemDTO {
    String MaDiaDiem;
    String TenDiaDiem;
    String DiaChi;
    String QuocGia;

    @Override
    public String toString(){
        return MaDiaDiem + " - " + TenDiaDiem;
    }
}