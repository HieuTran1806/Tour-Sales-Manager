package org.example.dto;
import java.time.LocalDate;

import lombok.*;
import lombok.experimental.FieldDefaults;

@Getter
@Setter
@FieldDefaults(level = AccessLevel.PRIVATE)
public class NhanVienDTO extends Person {
    String maNV;
    String chucVu;

    public NhanVienDTO() {
    }

    public NhanVienDTO(String maNV, String chucVu, String ho, String ten, String diaChi, String sdt, LocalDate ngaySinh) {
        super(ho, ten, diaChi, sdt, ngaySinh);
        this.maNV = maNV;
        this.chucVu = chucVu;
    }

    @Override
    public String toString() {
        return maNV + " - " + super.getHo() + " " + super.getTen();
    }
}