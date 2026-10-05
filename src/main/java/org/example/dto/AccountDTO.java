package org.example.dto;

import lombok.*;
import lombok.experimental.FieldDefaults;
import org.example.enums.Role;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@FieldDefaults(level = AccessLevel.PRIVATE)
public class AccountDTO {
    int idAccount;
    String username;
    String password;
    Role role;
    String status;

    // Tài khoản nhân viên thì có idStaff
    String idStaff;

    // Tài khoản khách hàng thì có idCustomer
    String idCustomer;
}