package com.asgardiateam.aptekaproject.controller;

import com.asgardiateam.aptekaproject.service.interfaces.BotService;
import com.asgardiateam.aptekaproject.service.interfaces.UserService;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.api.objects.Update;

@Component
@RequiredArgsConstructor
public class TelegramController extends TelegramLongPollingBot {

    private final BotService botService;
    private final UserService userService;

    @Override
    public String getBotUsername() {
        return "@aptekabeta_bot";
    }

    @Override
    public String getBotToken() {
        return "6132565436:AAFIo4jysRetnG0GihnmCVmHGU1RV9lOabE";
    }

    @SneakyThrows
    @Override
    public void onUpdateReceived(Update update) {
        execute(botService.executeMethod(update, userService.userByTelegramId(String.valueOf(update.getMessage().getChatId()))));
    }

}
