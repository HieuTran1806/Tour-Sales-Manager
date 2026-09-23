package org.example.dto;

import lombok.*;
import lombok.experimental.FieldDefaults;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@FieldDefaults(level = AccessLevel.PRIVATE)
public class TourBookingDTO {
    int idTourBooking;
    String idCustomer;
    String idTourPlan;
    LocalDateTime bookingDate;
    int tickets;
    BigDecimal price;
    BigDecimal costTotal;
    String bookingStatus;
    String note;
}
