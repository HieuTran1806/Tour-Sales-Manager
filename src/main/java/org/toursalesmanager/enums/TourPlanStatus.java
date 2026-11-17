package org.toursalesmanager.enums;

import lombok.Getter;

import java.util.Arrays;

@Getter
public enum TourPlanStatus {
    UPCOMING("Sắp khởi hành"),
    IN_PROGRESS("Đang diễn ra"),
    FINISHED("Đã kết thúc"),
    CANCELLED("Đã hủy");

    private final String label;

    TourPlanStatus(String label) {
        this.label = label;
    }

    // null nếu nhãn rỗng hoặc không thuộc danh sách
    public static TourPlanStatus fromLabel(String label) {
        if (label == null) {
            return null;
        }
        String trimmed = label.trim();
        return Arrays.stream(values()).filter(s -> s.label.equals(trimmed)).findFirst().orElse(null);
    }

    public static String[] labels() {
        return Arrays.stream(values()).map(TourPlanStatus::getLabel).toArray(String[]::new);
    }

    @Override
    public String toString() {
        return label;
    }
}
