package com.asgardiateam.aptekaproject.controller;

import com.asgardiateam.aptekaproject.common.ThreadLocalSingleton;
import com.asgardiateam.aptekaproject.entity.Admin;
import com.asgardiateam.aptekaproject.exception.AptekaException;
import com.asgardiateam.aptekaproject.mapper.AdminMapper;
import com.asgardiateam.aptekaproject.payload.ChangePasswordRequest;
import com.asgardiateam.aptekaproject.payload.request.AdminRequest;
import com.asgardiateam.aptekaproject.service.interfaces.AdminService;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.apache.commons.lang3.StringUtils;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

import static com.asgardiateam.aptekaproject.common.ResponseData.ok;
import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;

@Log4j2
@RestController
@RequiredArgsConstructor
@RequestMapping(value = "/api/v1/admin", produces = APPLICATION_JSON_VALUE)
public class AdminController {

    private final AdminMapper adminMapper;
    private final AdminService adminService;
    private final PasswordEncoder passwordEncoder;
    private final AuthenticationManager authenticationManager;

    @GetMapping
    public Object getMe() {
        return ok(adminService.getMe());
    }

    @PutMapping
    public Object updateMe(@RequestBody @Valid AdminRequest request) {
        return ok(adminService.updateMe(request));
    }

    @PutMapping("/password")
    public Object changePassword(@RequestBody @Valid ChangePasswordRequest request) {
        Admin admin = ThreadLocalSingleton.getUser();

        if (!StringUtils.equals(request.getNewPassword(), request.getRepeatedPassword()))
            throw AptekaException.newAndRepeatedPasswordDoesNotMatch();

        try {
            authenticationManager
                    .authenticate(new UsernamePasswordAuthenticationToken(admin.getLogin(), request.getOldPassword()));
        } catch (Exception e) {
            log.error(e);
            throw AptekaException.oldPasswordNotCorrect();
        }

        admin.setPassword(passwordEncoder.encode(request.getNewPassword()));
        adminService.save(admin);

        return adminMapper.toDTO(admin);
    }
}
