package org.toursalesmanager.dto;


import lombok.*;
import lombok.experimental.FieldDefaults;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@FieldDefaults(level = AccessLevel.PRIVATE)
public class TourPlanDetailDTO {
    String idTourPlanDetail;
    LocalDateTime date;
    BigDecimal totalExpenditure;
    BigDecimal housingCost;
    BigDecimal eatingCost;
    BigDecimal travelingCost;
    String departureLocation;
    String endLocation;
    String idTourPlan;
}
