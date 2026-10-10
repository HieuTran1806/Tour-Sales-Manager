package org.toursalesmanager.dto;

import lombok.*;
import lombok.experimental.FieldDefaults;

import java.math.BigDecimal;
import java.time.LocalDate;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@FieldDefaults(level = AccessLevel.PRIVATE)
public class TourPlanDTO {
    String idTourPlan;
    LocalDate departureDate;
    LocalDate endDate;
    int tickets;
    BigDecimal estimatedCost;
    int remainingTickets;
    String status;
    String idTour;
    String idTourGuide;
}
