package com.asgardiateam.aptekaproject.controller;

import com.asgardiateam.aptekaproject.service.interfaces.BotService;
import com.asgardiateam.aptekaproject.service.interfaces.UserService;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.methods.BotApiMethod;
import org.telegram.telegrambots.meta.api.objects.Update;

@Component
public record TelegramFacade(BotService botService,
                             UserService userService) {

    public TelegramFacade {
        assert botService != null;
    }

    public BotApiMethod<?> handleUpdate(Update update) {

        String chatId = String.valueOf(update.hasCallbackQuery() ? update.getCallbackQuery().getMessage().getChatId() :
                update.getMessage().getChatId());

        if (update.hasMessage() && update.getMessage().hasText() && update.getMessage().getText().equals("/start")) {
            return botService.greet(update, userService.userByTelegramId(String.valueOf(update.getMessage().getChatId())));
        }

        return botService
                .executeMethod(update, userService
                        .userByTelegramId(chatId));
    }

}