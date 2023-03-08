package com.asgardiateam.aptekaproject.service.interfaces;

import com.asgardiateam.aptekaproject.entity.Admin;

import java.util.Optional;

public interface AdminService {

    Optional<Admin> findByLogin(String login);

    Optional<Admin> findById(Long id);

}
