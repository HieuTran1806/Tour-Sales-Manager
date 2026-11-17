package org.toursalesmanager.enums;

import lombok.Getter;

import java.util.Arrays;

/**
 * Trạng thái phiếu đặt tour. CSDL (tourbooking.bookingStatus) lưu nhãn tiếng Việt,
 * nên dùng {@link #getLabel()} khi ghi và {@link #fromLabel(String)} khi đọc.
 */
@Getter
public enum BookingStatus {
    PENDING("Chờ xác nhận"),
    CONFIRMED("Đã xác nhận"),
    CANCELLED("Đã hủy"),
    COMPLETED("Hoàn thành"),
    // Khách không thanh toán / xác nhận đúng hạn
    EXPIRED("Hết hạn");

    private final String label;

    BookingStatus(String label) {
        this.label = label;
    }

    // null nếu nhãn rỗng hoặc không thuộc danh sách
    public static BookingStatus fromLabel(String label) {
        if (label == null) {
            return null;
        }
        String trimmed = label.trim();
        return Arrays.stream(values()).filter(s -> s.label.equals(trimmed)).findFirst().orElse(null);
    }

    public boolean matches(String label) {
        return this == fromLabel(label);
    }

    public static String[] labels() {
        return Arrays.stream(values()).map(BookingStatus::getLabel).toArray(String[]::new);
    }

    @Override
    public String toString() {
        return label;
    }
}
