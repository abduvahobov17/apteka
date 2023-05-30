package com.asgardiateam.aptekaproject.config;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.experimental.FieldDefaults;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
@Getter
@FieldDefaults(level = AccessLevel.PRIVATE)
public class TelegramBotConfig {

    @Value("${asgardiateam.telegram.bot.username}")
    private String botUsername;

    @Value("${asgardiateam.telegram.bot.token}")
    private String botToken;

    @Value("${asgardiateam.telegram.bot.path}")
    private String  path;
}