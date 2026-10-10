package org.toursalesmanager.dto;
import java.time.LocalDate;

import lombok.*;
import lombok.experimental.FieldDefaults;
import org.toursalesmanager.enums.Role;

@Getter
@Setter
@NoArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class StaffDTO extends Person {
    String idStaff;
    Role role;

    public StaffDTO(String idStaff, String ho, String ten, String diaChi, String sdt, LocalDate ngaySinh) {
        super(ho, ten, diaChi, sdt, ngaySinh);
        this.idStaff = idStaff;
    }

    @Override
    public String toString() {
        return idStaff + " - " + super.getFirstName() + " " + super.getLastName();
    }
}