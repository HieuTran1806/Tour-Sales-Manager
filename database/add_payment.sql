-- Bảng lưu các giao dịch thanh toán của phiếu đặt tour.
-- Số tiền đã thanh toán của một phiếu = tổng `amount` của các giao dịch có status = 'CONFIRMED'.
-- Còn phải thanh toán = tourbooking.costTotal - số tiền đã thanh toán.
CREATE TABLE IF NOT EXISTS `payment` (
  `idPayment` int NOT NULL AUTO_INCREMENT,
  `idTourBooking` int NOT NULL,
  `method` varchar(20) NOT NULL,              -- CASH | BANK_QR
  `amount` decimal(12,0) NOT NULL,            -- số tiền khách phải trả trong giao dịch
  `receivedAmount` decimal(12,0) DEFAULT NULL,-- số tiền nhân viên thực nhận khi đối soát
  `transferContent` varchar(50) DEFAULT NULL, -- nội dung chuyển khoản trong mã QR
  `transactionRef` varchar(50) DEFAULT NULL,  -- mã giao dịch ngân hàng khách nhập
  `status` varchar(30) NOT NULL,              -- AWAITING_PAYMENT | PENDING_VERIFICATION | CONFIRMED | REJECTED | CANCELLED | EXPIRED
  `createdAt` datetime NOT NULL,
  `expiresAt` datetime DEFAULT NULL,          -- hạn của mã QR / hạn thanh toán trực tiếp
  `paidAt` datetime DEFAULT NULL,             -- thời điểm khách báo đã chuyển khoản
  `confirmedAt` datetime DEFAULT NULL,        -- thời điểm nhân viên xác nhận / từ chối
  `confirmedBy` varchar(50) DEFAULT NULL,     -- tên đăng nhập nhân viên xử lý
  `note` varchar(255) DEFAULT NULL,
  PRIMARY KEY (`idPayment`),
  KEY `fk_payment_tourbooking_idx` (`idTourBooking`),
  KEY `idx_payment_status` (`status`),
  CONSTRAINT `fk_payment_tourbooking` FOREIGN KEY (`idTourBooking`) REFERENCES `tourbooking` (`idTourBooking`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
