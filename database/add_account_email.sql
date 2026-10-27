ALTER TABLE taikhoan
    ADD COLUMN email VARCHAR(254) NULL,
    ADD UNIQUE KEY uq_taikhoan_email (email);
