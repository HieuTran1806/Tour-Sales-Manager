package org.example.dto;

import lombok.*;
import lombok.experimental.FieldDefaults;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@FieldDefaults(level = AccessLevel.PRIVATE)
public class CTietHDDTO {
    String MaHD;
    String MaKHDi;
    float GiaVe;
}
