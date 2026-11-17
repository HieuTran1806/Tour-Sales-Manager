package org.toursalesmanager.enums;

import lombok.Getter;

@Getter
public enum PaymentMethod {
    CASH("Thanh toán trực tiếp"),
    BANK_QR("Chuyển khoản / QR code");

    private final String label;

    PaymentMethod(String label) {
        this.label = label;
    }

    @Override
    public String toString() {
        return label;
    }
}
