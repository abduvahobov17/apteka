package com.asgardiateam.aptekaproject.controller;

import com.asgardiateam.aptekaproject.config.security.jwt.JWTTokenProvider;
import com.asgardiateam.aptekaproject.payload.AuthDTO;
import com.asgardiateam.aptekaproject.payload.JwtTokenDTO;
import com.asgardiateam.aptekaproject.payload.MessageDTO;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

import static com.asgardiateam.aptekaproject.common.ResponseData.ok;
import static com.asgardiateam.aptekaproject.constants.MessageKey.SUCCESS_MESSAGE;
import static com.asgardiateam.aptekaproject.constants.SecurityConstants.EXPIRATION_TIME;
import static com.asgardiateam.aptekaproject.constants.SecurityConstants.TOKEN_PREFIX;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1")
public class AuthController {

    private final JWTTokenProvider jwtTokenProvider;
    private final AuthenticationManager authenticationManager;

    @PostMapping("/authentication/token")
    public Object authenticationUser(@Valid @RequestBody AuthDTO authDTO) {

        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        authDTO.getLogin(),
                        authDTO.getPassword())
                );

        SecurityContextHolder.getContext().setAuthentication(authentication);

        String jwt = TOKEN_PREFIX + jwtTokenProvider.generateToken(authentication);

        return  ok(new JwtTokenDTO(jwt, EXPIRATION_TIME, "Bearer"));
    }

    @GetMapping("/logout")
    public Object logOut() {
        return ok(new MessageDTO(SUCCESS_MESSAGE));
    }

}
