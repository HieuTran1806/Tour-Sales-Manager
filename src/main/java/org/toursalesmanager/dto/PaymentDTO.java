package org.toursalesmanager.dto;

import lombok.*;
import lombok.experimental.FieldDefaults;
import org.toursalesmanager.enums.PaymentMethod;
import org.toursalesmanager.enums.PaymentStatus;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@FieldDefaults(level = AccessLevel.PRIVATE)
public class PaymentDTO {
    int idPayment;
    int idTourBooking;
    // Lấy kèm từ phiếu đặt để hiển thị/lọc, không lưu trong bảng payment
    String idCustomer;
    PaymentMethod method;
    BigDecimal amount;
    BigDecimal receivedAmount;
    String transferContent;
    String transactionRef;
    PaymentStatus status;
    LocalDateTime createdAt;
    LocalDateTime expiresAt;
    LocalDateTime paidAt;
    LocalDateTime confirmedAt;
    String confirmedBy;
    String note;

    public boolean isExpired(LocalDateTime now) {
        return expiresAt != null && now.isAfter(expiresAt);
    }
}
