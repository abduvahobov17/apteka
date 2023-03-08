package com.asgardiateam.aptekaproject.service;

import com.asgardiateam.aptekaproject.entity.Admin;
import com.asgardiateam.aptekaproject.repository.AdminRepository;
import com.asgardiateam.aptekaproject.service.interfaces.AdminService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class AdminServiceImpl implements AdminService {

    private final AdminRepository adminRepository;

    @Override
    public Optional<Admin> findByLogin(String login) {
        return adminRepository.findByLogin(login);
    }

    @Override
    public Optional<Admin> findById(Long id) {
        return adminRepository.findById(id);
    }
}
