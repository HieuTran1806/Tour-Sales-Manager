package org.toursalesmanager.enums;

import lombok.Getter;

/**
 * Vòng đời một giao dịch thanh toán:
 * AWAITING_PAYMENT (đã tạo QR / chọn trả trực tiếp, khách chưa trả)
 *   → PENDING_VERIFICATION (khách báo đã chuyển khoản, chờ nhân viên đối soát)
 *   → CONFIRMED (nhân viên xác nhận đã nhận đúng số tiền) hoặc REJECTED.
 * Giao dịch chưa trả có thể bị CANCELLED (khách hủy / đổi phương thức) hoặc EXPIRED (QR hết hạn).
 */
@Getter
public enum PaymentStatus {
    AWAITING_PAYMENT("Chờ khách thanh toán"),
    PENDING_VERIFICATION("Chờ nhân viên xác nhận"),
    CONFIRMED("Đã xác nhận"),
    REJECTED("Bị từ chối"),
    CANCELLED("Đã hủy"),
    EXPIRED("Hết hạn");

    private final String label;

    PaymentStatus(String label) {
        this.label = label;
    }

    // Giao dịch còn hiệu lực, chặn việc tạo giao dịch mới cho cùng phiếu
    public boolean isOpen() {
        return this == AWAITING_PAYMENT || this == PENDING_VERIFICATION;
    }

    @Override
    public String toString() {
        return label;
    }
}
