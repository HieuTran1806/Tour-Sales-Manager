package org.toursalesmanager.enums;

import lombok.Getter;

@Getter
public enum Permission {

    // Tour
    VIEW_TOUR("Tour", "Xem tour"),
    MANAGE_TOUR("Tour", "Quản lý tour"),

    // Loại tour
    VIEW_TOUR_TYPE("Loại tour", "Xem loại tour"),
    MANAGE_TOUR_TYPE("Loại tour", "Quản lý loại tour"),

    // Kế hoạch tour
    VIEW_TOUR_PLAN("Kế hoạch tour", "Xem kế hoạch tour"),
    MANAGE_TOUR_PLAN("Kế hoạch tour", "Quản lý kế hoạch tour"),

    // Khách hàng
    VIEW_CUSTOMER("Khách hàng", "Xem khách hàng"),
    MANAGE_CUSTOMER("Khách hàng", "Quản lý khách hàng"),

    // Phiếu đặt tour
    VIEW_BOOKING("Phiếu đặt tour", "Xem phiếu đặt tour"),
    CREATE_BOOKING("Phiếu đặt tour", "Tạo phiếu đặt tour"),
    UPDATE_BOOKING("Phiếu đặt tour", "Cập nhật phiếu đặt tour"),
    CANCEL_BOOKING("Phiếu đặt tour", "Hủy phiếu đặt tour"),

    // Thanh toán
    VIEW_PAYMENT("Thanh toán", "Xem thanh toán"),
    MANAGE_PAYMENT("Thanh toán", "Quản lý thanh toán"),

    // Báo cáo
    VIEW_REVENUE_REPORT("Báo cáo", "Xem báo cáo doanh thu"),

    VIEW_PROMOTION_CALENDAR("Khuyến mãi", "Xem lịch khuyến mãi"),

    // Địa điểm
    VIEW_LOCATION("Địa điểm", "Xem địa điểm"),
    MANAGE_LOCATION("Địa điểm", "Quản lý địa điểm"),

    // Hóa đơn
    VIEW_INVOICE("Hóa đơn", "Xem hóa đơn"),
    MANAGE_INVOICE("Hóa đơn", "Quản lý hóa đơn"),

    // Khuyến mãi
    VIEW_PROMOTION("Khuyến mãi", "Xem chương trình khuyến mãi"),
    MANAGE_PROMOTION("Khuyến mãi", "Quản lý chương trình khuyến mãi"),

    // Thống kê
    VIEW_STATISTICS("Thống kê", "Xem thống kê"),

    // Nhân viên
    VIEW_STAFF("Nhân viên", "Xem nhân viên"),
    MANAGE_STAFF("Nhân viên", "Quản lý nhân viên"),

    // Tài khoản và phân quyền
    MANAGE_ACCOUNT("Tài khoản", "Quản lý tài khoản"),
    MANAGE_ROLE("Tài khoản", "Quản lý phân quyền");

    private final String group;
    private final String label;

    Permission(String group, String label) {
        this.group = group;
        this.label = label;
    }
}
