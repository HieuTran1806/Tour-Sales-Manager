package org.example.dto;

import lombok.*;
import lombok.experimental.FieldDefaults;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@FieldDefaults(level = AccessLevel.PRIVATE)
public class TaiKhoanDTO {
    String username;
    String password;
    String position;

    public enum Role{
        ADMIN,
        STAFF,
    }
}