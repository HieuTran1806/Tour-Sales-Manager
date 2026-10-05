package org.example.enums;

public enum Permission {

    // Tour
    VIEW_TOUR,
    MANAGE_TOUR,

    // Loại tour
    VIEW_TOUR_TYPE,
    MANAGE_TOUR_TYPE,

    // Kế hoạch tour
    VIEW_TOUR_PLAN,
    MANAGE_TOUR_PLAN,

    // Khách hàng
    VIEW_CUSTOMER,
    MANAGE_CUSTOMER,

    // Phiếu đặt tour
    VIEW_BOOKING,
    CREATE_BOOKING,
    UPDATE_BOOKING,
    CANCEL_BOOKING,

    // Thanh toán
    VIEW_PAYMENT,
    MANAGE_PAYMENT,

    // Báo cáo
    VIEW_REVENUE_REPORT,

    VIEW_PROMOTION_CALENDAR,

    // Địa điểm
    VIEW_LOCATION,
    MANAGE_LOCATION,

    // Hóa đơn
    VIEW_INVOICE,
    CREATE_INVOICE,
    UPDATE_INVOICE,
    CANCEL_INVOICE,

    // Khuyến mãi
    VIEW_PROMOTION,
    MANAGE_PROMOTION,

    // Thống kê
    VIEW_STATISTICS,

    // Nhân viên
    VIEW_STAFF,
    MANAGE_STAFF,

    // Tài khoản và phân quyền
    MANAGE_ACCOUNT,
    MANAGE_ROLE
}