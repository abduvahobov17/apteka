package com.asgardiateam.aptekaproject.service.interfaces;

import com.asgardiateam.aptekaproject.entity.User;
import org.telegram.telegrambots.meta.api.methods.BotApiMethod;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Update;

import java.io.Serializable;

public interface BotService {

    BotApiMethod<? extends Serializable> executeMethod(Update update, User user);

    BotApiMethod<? extends Serializable> greet(Update update, User user);

}
