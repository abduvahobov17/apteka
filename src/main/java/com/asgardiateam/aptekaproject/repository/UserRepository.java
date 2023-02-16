package com.asgardiateam.aptekaproject.repository;

import com.asgardiateam.aptekaproject.entity.User;
import org.springframework.data.jpa.repository.support.JpaRepositoryImplementation;

import java.util.Optional;

public interface UserRepository extends JpaRepositoryImplementation<User, Long> {

    Optional<User> findByTelegramId(String telegramId);
}
