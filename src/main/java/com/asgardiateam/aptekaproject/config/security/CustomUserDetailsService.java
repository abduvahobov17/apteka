package com.asgardiateam.aptekaproject.config.security;

import com.asgardiateam.aptekaproject.entity.Admin;
import com.asgardiateam.aptekaproject.service.interfaces.AdminService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class CustomUserDetailsService implements UserDetailsService {

    private final AdminService adminService;

    @Override
    public UserDetails loadUserByUsername(String login) throws UsernameNotFoundException {

        return adminService.findByLogin(login)
                .orElseThrow(() -> new UsernameNotFoundException("Login not found with login: " + login));
    }

    public Admin loadByUserId(Long id) {
        return adminService.findById(id)
                .orElse(null);
    }
}
