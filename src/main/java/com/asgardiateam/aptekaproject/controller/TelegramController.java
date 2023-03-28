package com.asgardiateam.aptekaproject.controller;

import com.asgardiateam.aptekaproject.service.interfaces.BotService;
import com.asgardiateam.aptekaproject.service.interfaces.UserService;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.api.objects.Update;

import java.util.Optional;

@Component
@RequiredArgsConstructor
public class TelegramController extends TelegramLongPollingBot {

    private final BotService botService;
    private final UserService userService;

    @Override
    public String getBotUsername() {
        return "@housetinchlik_bot";
    }

    @Override
    public String getBotToken() {
        return "5059171928:AAFpfNI9vYvm4dQG7opQ_FgrceK6MovIlf0";
    }

    @SneakyThrows
    @Override
    public void onUpdateReceived(Update update) {

        String chatId = String.valueOf(update.hasCallbackQuery() ? update.getCallbackQuery().getMessage().getChatId() :
                update.getMessage().getChatId());

        execute(botService
                .executeMethod(update, userService
                        .userByTelegramId(chatId)));
    }

}
