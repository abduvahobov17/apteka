package com.asgardiateam.aptekaproject.service.interfaces;

import com.asgardiateam.aptekaproject.entity.User;

import java.util.List;

public interface UserService {

    List<User> users();

    User userByTelegramId(String telegramId);

    User userById(Long id);

    User saveUser(User user);

}
