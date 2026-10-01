package org.example.dto;

import lombok.*;
import lombok.experimental.FieldDefaults;

import java.time.LocalDate;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@FieldDefaults(level = AccessLevel.PRIVATE)
public class PromotionDTO {
    String idPromotion;
    String promotionName;
    LocalDate startDate;
    LocalDate endDate;
    boolean hinhThucKM;
    float discount;
    String note;

    @Override
    public String toString(){
        return idPromotion + " - Giảm" + discount + "%";
    }
}
