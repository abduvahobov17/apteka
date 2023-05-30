package com.asgardiateam.aptekaproject.config;

import com.asgardiateam.aptekaproject.controller.TelegramBot;
import com.asgardiateam.aptekaproject.controller.TelegramFacade;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.telegram.telegrambots.meta.api.methods.updates.SetWebhook;

import java.util.List;

@Configuration
public class AppConfig {
    private final TelegramBotConfig botConfig;

    public AppConfig(TelegramBotConfig botConfig) {
        this.botConfig = botConfig;
    }

    @Bean
    public SetWebhook setWebhookInstance() {
        return SetWebhook.builder()
                .url(botConfig.getPath())
                .dropPendingUpdates(true)
                .allowedUpdates(List.of("callback_query","inline_query", "message"))
                .build();
    }

    @Bean
    public TelegramBot springWebhookBot(SetWebhook setWebhook, TelegramFacade telegramFacade) {
        TelegramBot bot = new TelegramBot(telegramFacade, setWebhook);
        bot.setBotToken(botConfig.getBotToken());
        bot.setBotUsername(botConfig.getBotUsername());
        bot.setBotPath(botConfig.getPath());

        return bot;
    }
}