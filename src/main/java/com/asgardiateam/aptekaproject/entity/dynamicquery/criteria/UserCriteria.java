package com.asgardiateam.aptekaproject.entity.dynamicquery.criteria;

import com.asgardiateam.aptekaproject.common.deserializer.BotStateDeserializer;
import com.asgardiateam.aptekaproject.common.deserializer.ClientDeserializer;
import com.asgardiateam.aptekaproject.common.deserializer.LangDeserializer;
import com.asgardiateam.aptekaproject.enums.BotState;
import com.asgardiateam.aptekaproject.enums.ClientType;
import com.asgardiateam.aptekaproject.enums.Lang;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.SuperBuilder;

import java.time.Instant;

@Getter
@Setter
@AllArgsConstructor
@SuperBuilder
public class UserCriteria {

    private Long id;

    private String firstName;

    private String lastName;

    private Instant startRegisteredDate;

    private Instant endRegisteredDate;

    private String firstNameTeleg;

    private String lastNameTeleg;

    private String userNameTeleg;

    private String telegramId;

    @JsonDeserialize(using = ClientDeserializer.class)
    private ClientType clientType;

    @JsonDeserialize(using = LangDeserializer.class)
    private Lang lang;

    @JsonDeserialize(using = BotStateDeserializer.class)
    private BotState botState;

    public UserCriteria() {
        startRegisteredDate = Instant.ofEpochMilli(0L);
    }

    public void setStartRegisteredDate(Long startRegisteredDate) {
        this.startRegisteredDate = Instant.ofEpochMilli(startRegisteredDate);
    }

    public void setEndRegisteredDate(Long endRegisteredDate) {
        this.endRegisteredDate = Instant.ofEpochMilli(endRegisteredDate);
    }
}
