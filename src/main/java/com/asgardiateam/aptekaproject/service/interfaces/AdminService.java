package com.asgardiateam.aptekaproject.service.interfaces;

import com.asgardiateam.aptekaproject.entity.Admin;
import com.asgardiateam.aptekaproject.payload.AdminDTO;
import com.asgardiateam.aptekaproject.payload.ChangePasswordRequest;
import com.asgardiateam.aptekaproject.payload.PhotoUpdateRequest;
import com.asgardiateam.aptekaproject.payload.request.AdminRequest;

import java.util.Optional;

public interface AdminService {

    Optional<Admin> findByLogin(String login);

    Optional<Admin> findById(Long id);

    Admin save(Admin admin);

    AdminDTO getMe();

    AdminDTO updateMe(AdminRequest request);

    AdminDTO updatePhoto(PhotoUpdateRequest request);

}
