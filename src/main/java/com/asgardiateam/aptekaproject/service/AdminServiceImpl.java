package com.asgardiateam.aptekaproject.service;

import com.asgardiateam.aptekaproject.common.ThreadLocalSingleton;
import com.asgardiateam.aptekaproject.entity.Admin;
import com.asgardiateam.aptekaproject.exception.AptekaException;
import com.asgardiateam.aptekaproject.mapper.AdminMapper;
import com.asgardiateam.aptekaproject.payload.AdminDTO;
import com.asgardiateam.aptekaproject.payload.request.AdminRequest;
import com.asgardiateam.aptekaproject.repository.AdminRepository;
import com.asgardiateam.aptekaproject.service.interfaces.AdminService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class AdminServiceImpl implements AdminService {

    private final AdminMapper adminMapper;
    private final AdminRepository adminRepository;

    @Override
    public Optional<Admin> findByLogin(String login) {
        return adminRepository.findByLogin(login);
    }

    @Override
    public Optional<Admin> findById(Long id) {
        return adminRepository.findById(id);
    }

    @Override
    public AdminDTO getMe() {
        return adminMapper.toDTO(ThreadLocalSingleton.getUser());
    }

    @Override
    public AdminDTO updateMe(AdminRequest request) {
        Admin admin = ThreadLocalSingleton.getUser();
        admin = adminMapper.toUpdate(request, admin);
        return adminMapper.toDTO(save(admin));
    }

    private Admin save(Admin admin) {
        try {
            return adminRepository.save(admin);
        } catch (Exception e) {
            throw AptekaException.saveError();
        }
    }
}
