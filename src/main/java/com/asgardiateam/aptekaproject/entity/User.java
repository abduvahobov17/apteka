package com.asgardiateam.aptekaproject.entity;

import com.asgardiateam.aptekaproject.audit.AuditingEntity;
import com.asgardiateam.aptekaproject.enums.BotState;
import com.asgardiateam.aptekaproject.enums.ClientType;
import com.asgardiateam.aptekaproject.enums.Lang;
import com.asgardiateam.aptekaproject.enums.UserType;
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
@Entity
@Table(name = "users")
public class User extends AuditingEntity {

    @Id
    @GeneratedValue
    private Long id;

    @Column(name = "first_name")
    private String firstName;

    @Column(name = "last_name")
    private String lastName;

    @Column(name = "first_name_teleg")
    private String firstNameTeleg;

    @Column(name = "last_name_teleg")
    private String lastNameTeleg;

    @Column(name = "user_name_teleg")
    private String userNameTeleg;

    @Column(name = "telegram_id", unique = true)
    private String telegramId;

    @Column(name = "phone_number")
    private String phoneNumber;

    @Column(name = "lon")
    private Long lon;

    @Column(name = "lat")
    private Long lat;

    @Column(name = "client_type")
    @Enumerated(EnumType.STRING)
    private ClientType clientType;

//    @Column(name = "user_type")
//    @Enumerated(EnumType.STRING)
//    private UserType userType;

    @Column(name = "language")
    @Enumerated(EnumType.STRING)
    private Lang lang;

    @Column(name = "bot_state")
    @Enumerated(EnumType.STRING)
    private BotState botState;

    public User(String telegramId) {
        this.telegramId = telegramId;
        this.botState = BotState.START;
    }

}
