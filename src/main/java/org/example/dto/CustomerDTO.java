package org.example.dto;

import lombok.*;
import lombok.experimental.FieldDefaults;

import java.time.LocalDate;

@NoArgsConstructor
@Getter
@Setter
@FieldDefaults(level = AccessLevel.PRIVATE)
public class CustomerDTO extends Person {
    String idCustomer;

    public CustomerDTO(String idStaff, String firstName, String lastName, String address, String phoneNumber, LocalDate dob) {
        super(firstName, lastName, address, phoneNumber, dob);
        this.idCustomer = idStaff;
    }
}