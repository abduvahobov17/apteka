package com.asgardiateam.aptekaproject.service;

import com.asgardiateam.aptekaproject.entity.User;
import com.asgardiateam.aptekaproject.entity.dynamicquery.criteria.UserCriteria;
import com.asgardiateam.aptekaproject.entity.dynamicquery.specifications.UserSpecifications;
import com.asgardiateam.aptekaproject.exception.AptekaException;
import com.asgardiateam.aptekaproject.mapper.UserMapper;
import com.asgardiateam.aptekaproject.payload.UserDTO;
import com.asgardiateam.aptekaproject.payload.request.UserRequest;
import com.asgardiateam.aptekaproject.repository.UserRepository;
import com.asgardiateam.aptekaproject.service.interfaces.UserService;
import com.asgardiateam.aptekaproject.utils.Page2Dto;
import com.asgardiateam.aptekaproject.utils.PageDto;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

@Log4j2
@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {

    private final UserMapper userMapper;
    private final UserRepository userRepository;

    @Override
    public UserDTO create(UserRequest userRequest) {
        return userMapper.toDTO(save(userMapper.toCreate(userRequest)));
    }

    @Override
    public UserDTO update(UserRequest userRequest, Long id) {
        return userMapper.toDTO(userMapper.toUpdate(userRequest, findById(id)));
    }

    @Override
    public UserDTO getById(Long id) {
        return userMapper.toDTO(findById(id));
    }

    @Override
    public PageDto<UserDTO> getAll(Pageable pageable, UserCriteria userCriteria) {
        return Page2Dto.toDTO(findAll(pageable, userCriteria).map(userMapper::toDTO));
    }

    @Override
    public User findById(Long id) {
        return userRepository.findById(id).orElseThrow(AptekaException::userNotFoundById);
    }

    @Override
    public Page<User> findAll(Pageable pageable, UserCriteria userCriteria) {
        return userRepository.findAll(UserSpecifications.createSpecification(userCriteria), pageable);
    }

    @Override
    public void deleteById(Long id) {
        delete(findById(id));
    }

    @Override
    public void delete(User user) {
        try {
            userRepository.delete(user);
        } catch (Exception e) {
            log.error(e);
            throw AptekaException.deleteUserException();
        }
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
