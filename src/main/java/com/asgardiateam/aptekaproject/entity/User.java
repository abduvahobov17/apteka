package com.asgardiateam.aptekaproject.entity;

import com.asgardiateam.aptekaproject.audit.AuditingEntity;
import com.asgardiateam.aptekaproject.enums.BotState;
import com.asgardiateam.aptekaproject.enums.ClientType;
import com.asgardiateam.aptekaproject.enums.Lang;
import com.asgardiateam.aptekaproject.enums.UserType;
import lombok.*;
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

    @Column(name = "order_amount")
    @Builder.Default
    private Long orderAmount = 0L;

    @Column(name = "client_type")
    @Enumerated(EnumType.STRING)
    private ClientType clientType;

    @Column(name = "language")
    @Enumerated(EnumType.STRING)
    private Lang lang;

    @Column(name = "bot_state")
    @Enumerated(EnumType.STRING)
    private BotState botState;

    public User(String telegramId) {
        this.telegramId = telegramId;
        this.botState = BotState.START;
        this.orderAmount = 0L;
    }

    public void addAmountToOrderAmount(Long amount) {
        orderAmount = orderAmount + amount;
    }

}
