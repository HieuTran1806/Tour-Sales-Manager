package org.example.dto;

import java.time.LocalDate;

import lombok.*;
import lombok.experimental.FieldDefaults;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@FieldDefaults(level = AccessLevel.PRIVATE)
public class Person {
    String firstName;
    String lastName;
    String address;
    String phoneNumber;
    LocalDate ngaySinh;
}