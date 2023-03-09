package com.asgardiateam.aptekaproject.service;

import com.asgardiateam.aptekaproject.entity.User;
import com.asgardiateam.aptekaproject.entity.dynamicquery.criteria.UserCriteria;
import com.asgardiateam.aptekaproject.exception.AptekaException;
import com.asgardiateam.aptekaproject.payload.UserDTO;
import com.asgardiateam.aptekaproject.payload.request.UserRequest;
import com.asgardiateam.aptekaproject.repository.UserRepository;
import com.asgardiateam.aptekaproject.service.interfaces.UserService;
import com.asgardiateam.aptekaproject.utils.PageDto;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;

@Log4j2
@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;

    @Override
    public UserDTO create(UserRequest userRequest) {
        return null;
    }

    @Override
    public UserDTO update(UserRequest userRequest, Long aLong) {
        return null;
    }

    @Override
    public UserDTO getById(Long aLong) {
        return null;
    }

    @Override
    public PageDto<UserDTO> getAll(Pageable pageable, UserCriteria userCriteria) {
        return null;
    }

    @Override
    public User findById(Long id) {
        return userRepository.findById(id).orElseThrow(AptekaException::userNotFoundById);
    }

    @Override
    public Page<User> findAll(Pageable pageable, UserCriteria userCriteria) {
        return null;
    }

    @Override
    public void delete(User user) {

    }

    @Override
    public User userByTelegramId(String telegramId) {
        return userRepository.findByTelegramId(telegramId)
                        .orElse(new User(telegramId));
    }

    @Override
    public User save(User user) {
        try {
            return userRepository.save(user);
        } catch (Exception e) {
            log.error(e);
            throw new AptekaException("USER SAVE ERROR");
        }
    }
}
