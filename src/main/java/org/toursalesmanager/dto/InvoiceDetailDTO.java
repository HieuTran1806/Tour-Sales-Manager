package org.toursalesmanager.dto;

import lombok.*;
import lombok.experimental.FieldDefaults;

import java.math.BigDecimal;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@FieldDefaults(level = AccessLevel.PRIVATE)
public class InvoiceDetailDTO {
    String idInvoice;
    String idCustomer;
    BigDecimal price;
}
