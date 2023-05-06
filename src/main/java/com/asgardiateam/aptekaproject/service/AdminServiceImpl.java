package com.asgardiateam.aptekaproject.service;

import com.asgardiateam.aptekaproject.common.ThreadLocalSingleton;
import com.asgardiateam.aptekaproject.entity.Admin;
import com.asgardiateam.aptekaproject.exception.AptekaException;
import com.asgardiateam.aptekaproject.mapper.AdminMapper;
import com.asgardiateam.aptekaproject.payload.AdminDTO;
import com.asgardiateam.aptekaproject.payload.ChangePasswordRequest;
import com.asgardiateam.aptekaproject.payload.PhotoUpdateRequest;
import com.asgardiateam.aptekaproject.payload.request.AdminRequest;
import com.asgardiateam.aptekaproject.repository.AdminRepository;
import com.asgardiateam.aptekaproject.service.interfaces.AdminService;
import com.asgardiateam.aptekaproject.service.interfaces.MinioFileUploader;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.apache.commons.lang3.StringUtils;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;

import java.util.Optional;
import java.util.UUID;

@Log4j2
@Service
@RequiredArgsConstructor
public class AdminServiceImpl implements AdminService {

    private final AdminMapper adminMapper;
    private final MinioFileUploader fileUploader;
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
        Admin admin = ThreadLocalSingleton.getUser();
        return adminMapper.toDTO(ThreadLocalSingleton.getUser(), fileUploader.getTempUrl(admin.getPhotoUrl(), "apteka-bot"));
    }

    @Override
    public AdminDTO updateMe(AdminRequest request) {
        Admin admin = ThreadLocalSingleton.getUser();
        admin = adminMapper.toUpdate(request, admin);
        return adminMapper.toDTO(save(admin), fileUploader.getTempUrl(admin.getPhotoUrl(), "apteka-bot"));
    }

    @Override
    public AdminDTO updatePhoto(PhotoUpdateRequest request) {
        Admin admin = ThreadLocalSingleton.getUser();
        String objectName = request.getTitle().replaceAll("\\s+", "_").toLowerCase() + "-" + UUID.randomUUID();
        objectName = fileUploader.upload("apteka-bot", request.getFile(), objectName);
        admin.setPhotoUrl(objectName);
        return adminMapper.toDTO(admin, fileUploader.getTempUrl(admin.getPhotoUrl(), "apteka-bot"));
    }

    public Admin save(Admin admin) {
        try {
            return adminRepository.save(admin);
        } catch (Exception e) {
            throw AptekaException.saveError();
        }
    }
}
