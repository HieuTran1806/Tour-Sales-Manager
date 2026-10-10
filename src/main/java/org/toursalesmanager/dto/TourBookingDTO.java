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

    // constructor non idTourBooking
    public TourBookingDTO(
            String idCustomer,
            String idTourPlan,
            LocalDateTime bookingDate,
            int tickets,
            BigDecimal price,
            BigDecimal costTotal,
            String bookingStatus,
            String note
    ) {
        this.idCustomer = idCustomer;
        this.idTourPlan = idTourPlan;
        this.bookingDate = bookingDate;
        this.tickets = tickets;
        this.price = price;
        this.costTotal = costTotal;
        this.bookingStatus = bookingStatus;
        this.note = note;
    }

    public String getBookingCode(){
        return String.format("PDT%03", idTourBooking);
    }
}
