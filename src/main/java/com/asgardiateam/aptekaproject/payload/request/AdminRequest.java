package com.asgardiateam.aptekaproject.payload.request;

import lombok.*;

import javax.validation.constraints.NotNull;

import static com.asgardiateam.aptekaproject.constants.MessageKey.*;

@Getter
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Setter
public class AdminRequest {

    @NotNull(message = FIRST_NAME_NOT_VALID)
    private String firstName;

    @NotNull(message = LAST_NAME_NOT_VALID)
    private String lastName;

    @NotNull(message = PHONE_NUMBER_NOT_VALID)
    private String phoneNumber;

    @NotNull(message = LOGIN_NOT_VALID)
    private String login;
}
