package org.example.addressbook;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ResetPasswordRequestDTO {
    private String token;
    private String newPassword;
}
