package com.asgardiateam.aptekaproject.payload;

import com.asgardiateam.aptekaproject.enums.BotState;
import com.asgardiateam.aptekaproject.enums.ClientType;
import com.asgardiateam.aptekaproject.enums.Lang;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.SuperBuilder;

import javax.persistence.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@SuperBuilder
public class UserDTO {

    private Long id;

    private Long registeredDate;

    private String firstName;

    private String lastName;

    private String firstNameTeleg;

    private String lastNameTeleg;

    private String userNameTeleg;

    private String telegramId;

    private String phoneNumber;

    private String clientType;

    private Lang lang;

    private BotState botState;
}
