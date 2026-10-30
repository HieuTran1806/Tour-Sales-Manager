-- Quyền riêng của từng tài khoản.
-- Tài khoản chưa có dòng nào trong bảng này sẽ dùng quyền mặc định theo vai trò (AuthorizationService).
CREATE TABLE IF NOT EXISTS taikhoan_quyen (
    idaccount  INT         NOT NULL,
    permission VARCHAR(50) NOT NULL,
    PRIMARY KEY (idaccount, permission)
);
