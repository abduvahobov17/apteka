package com.asgardiateam.aptekaproject.payload;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.SuperBuilder;

import javax.validation.constraints.NotBlank;

import static com.asgardiateam.aptekaproject.constants.MessageKey.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@SuperBuilder
public class ChangePasswordRequest {

    @NotBlank(message = OLD_PASSWORD_NOT_VALID)
    private String oldPassword;

    @NotBlank(message = NEW_PASSWORD_NOT_VALID)
    private String newPassword;

    @NotBlank(message = REPEATED_PASSWORD_NOT_VALID)
    private String repeatedPassword;

}
