package com.asgardiateam.aptekaproject.service.interfaces;

import com.asgardiateam.aptekaproject.entity.Admin;
import com.asgardiateam.aptekaproject.payload.AdminDTO;
import com.asgardiateam.aptekaproject.payload.request.AdminRequest;

import java.util.Optional;

public interface AdminService {

    Optional<Admin> findByLogin(String login);

    Optional<Admin> findById(Long id);

    AdminDTO getMe();

    AdminDTO updateMe(AdminRequest request);
}
