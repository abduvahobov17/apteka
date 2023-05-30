package com.asgardiateam.aptekaproject.controller;

import com.asgardiateam.aptekaproject.service.interfaces.BotService;
import com.asgardiateam.aptekaproject.service.interfaces.UserService;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.api.objects.Update;

//@Component
//@RequiredArgsConstructor
//public class TelegramController extends TelegramLongPollingBot {
//
//    private final BotService botService;
//    private final UserService userService;
//
//    @Value("${asgardiateam.telegram.bot.username}")
//    private String botUsername;
//
//    @Value("${asgardiateam.telegram.bot.token}")
//    private String botToken;
//
//    @Override
//    public String getBotUsername() {
//        return botUsername;
//    }
//
//    @Override
//    public String getBotToken() {
//        return botToken;
//    }
//
//    @SneakyThrows
//    @Override
//    public void onUpdateReceived(Update update) {
//
//        String chatId = String.valueOf(update.hasCallbackQuery() ? update.getCallbackQuery().getMessage().getChatId() :
//                update.getMessage().getChatId());
//
//        if (update.hasMessage() && update.getMessage().hasText() && update.getMessage().getText().equals("/start")) {
//            execute(botService.greet(update, userService.userByTelegramId(String.valueOf(update.getMessage().getChatId()))));
//        }
//
//        execute(botService
//                .executeMethod(update, userService
//                        .userByTelegramId(chatId)));
//    }
//
//}
