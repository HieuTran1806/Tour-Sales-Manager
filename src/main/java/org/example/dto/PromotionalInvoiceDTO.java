package org.example.dto;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.FieldDefaults;

import java.math.BigDecimal;
import java.time.LocalDate;

@NoArgsConstructor
@Getter
@Setter
@FieldDefaults(level = AccessLevel.PRIVATE)
public class PromotionalInvoiceDTO extends PromotionDTO {
    BigDecimal promotionalCost;

    public PromotionalInvoiceDTO(String maKM, String tenKM, LocalDate ngayBD, LocalDate ngayKT, boolean hinhThucKM, float chietKhau, String ghiChu, BigDecimal promotionalCost) {
        super(maKM, tenKM, ngayBD, ngayKT, hinhThucKM, chietKhau, ghiChu);
        this.promotionalCost = promotionalCost;
    }
}