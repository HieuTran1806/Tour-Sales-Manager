package org.example.dto;
import java.time.LocalDate;

import lombok.*;
import lombok.experimental.FieldDefaults;

@Getter
@Setter
@NoArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class StaffDTO extends Person {
    String idStaff;
    String role;

    public StaffDTO(String maNV, String role, String ho, String ten, String diaChi, String sdt, LocalDate ngaySinh) {
        super(ho, ten, diaChi, sdt, ngaySinh);
        this.idStaff = maNV;
        this.role = role;
    }

    @Override
    public String toString() {
        return idStaff + " - " + super.getFirstName() + " " + super.getLastName();
    }
}