package com.asgardiateam.aptekaproject.service.interfaces;

import com.asgardiateam.aptekaproject.entity.User;
import com.asgardiateam.aptekaproject.entity.dynamicquery.criteria.UserCriteria;
import com.asgardiateam.aptekaproject.payload.UserDTO;
import com.asgardiateam.aptekaproject.payload.request.UserRequest;
import com.asgardiateam.aptekaproject.utils.CrudService;
import org.springframework.data.domain.Pageable;

public interface UserService extends CrudService<User, UserDTO, UserRequest, Long, UserCriteria> {

    User userByTelegramId(String telegramId);

    byte[] generateExcel(Pageable pageable, UserCriteria criteria);

}
