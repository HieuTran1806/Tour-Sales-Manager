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
public class InvoiceDTO {
    String idInvoice;
    String idTourPlan;
    String idCustomer;
    String idStaff;
    LocalDate date;
    int tickets;
    String idPromotion;
    BigDecimal costTotal;
}
