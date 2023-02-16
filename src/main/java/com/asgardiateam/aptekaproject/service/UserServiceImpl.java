package com.asgardiateam.aptekaproject.service;

import com.asgardiateam.aptekaproject.entity.User;
import com.asgardiateam.aptekaproject.exception.AptekaException;
import com.asgardiateam.aptekaproject.repository.UserRepository;
import com.asgardiateam.aptekaproject.service.interfaces.UserService;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.stereotype.Service;

import java.util.List;

@Log4j2
@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;

    @Override
    public List<User> users() {
        return userRepository.findAll();
    }

    @Override
    public User userByTelegramId(String telegramId) {
        return userRepository.findByTelegramId(telegramId)
                        .orElse(new User(telegramId));
    }

    @Override
    public User userById(Long id) {
        return userRepository.findById(id).orElseThrow(AptekaException::userNotFoundById);
    }

    @Override
    public User saveUser(User user) {
        try {
            return userRepository.save(user);
        } catch (Exception e) {
            log.error(e);
            throw new AptekaException("USER SAVE ERROR");
        }
    }
}
