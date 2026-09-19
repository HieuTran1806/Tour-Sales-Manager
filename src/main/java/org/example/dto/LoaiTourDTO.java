package org.example.dto;
import lombok.*;
import lombok.experimental.FieldDefaults;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@FieldDefaults(level = AccessLevel.PRIVATE)
public class LoaiTourDTO {
    String maLoaiTour;
    String theLoai;
    String moTa;
    String trangThai;

    @Override
    public String toString(){
        return maLoaiTour + " - " + theLoai;
    }
}
