package com.asgardiateam.aptekaproject.payload.request;

import com.asgardiateam.aptekaproject.enums.BotState;
import com.asgardiateam.aptekaproject.enums.ClientType;
import com.asgardiateam.aptekaproject.enums.Lang;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.SuperBuilder;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@SuperBuilder
public class UserRequest {

    private String firstName;

    private String lastName;

    private String firstNameTeleg;

    private String lastNameTeleg;

    private String userNameTeleg;

    private String telegramId;

    private String phoneNumber;

    private ClientType clientType;

    private Lang lang;

    private BotState botState;

}
