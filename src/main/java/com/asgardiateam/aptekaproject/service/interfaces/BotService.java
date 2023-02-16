package com.asgardiateam.aptekaproject.service.interfaces;

import com.asgardiateam.aptekaproject.entity.User;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Update;

public interface BotService {

    SendMessage executeMethod(Update update, User user);


}
