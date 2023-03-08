package com.asgardiateam.aptekaproject.config.security.jwt;

import com.asgardiateam.aptekaproject.common.ThreadLocalSingleton;
import com.asgardiateam.aptekaproject.config.security.CustomUserDetailsService;
import com.asgardiateam.aptekaproject.constants.HeaderConstants;
import com.asgardiateam.aptekaproject.entity.Admin;
import com.asgardiateam.aptekaproject.exception.AptekaException;
import io.vavr.control.Try;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Objects;

@Log4j2
@Component
@RequiredArgsConstructor
public class JWTAuthenticationFilter extends OncePerRequestFilter {

    private final JWTTokenProvider jwtTokenProvider;

    private final CustomUserDetailsService customUserDetailsService;

    @Override
    public void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {

        try {
            String jwt = getJwtFromRequest(request);

            if (StringUtils.hasText(jwt) && jwtTokenProvider.validateToken(jwt)) {

                Long userId = jwtTokenProvider.getUserIdFromToken(jwt);
                Admin userDetails = customUserDetailsService.loadByUserId(userId);

                ThreadLocalSingleton.setUser(userDetails);

                UsernamePasswordAuthenticationToken authenticationToken =
                        new UsernamePasswordAuthenticationToken(userDetails, null, userDetails.getAuthorities());

                authenticationToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
                SecurityContextHolder.getContext().setAuthentication(authenticationToken);
            }
        } catch (Exception ex) {
            log.error("Could not set user authentication");
        }


        filterChain.doFilter(request, response);
    }

    private String getJwtFromRequest(HttpServletRequest request) {

        return Try.of(() -> {
                    String token = request.getHeader(HeaderConstants.AUTHORIZATION);
                    if (Objects.nonNull(token) && token.startsWith("Bearer") && token.length() > 8)
                        return token.substring(7);
                    return null;
                })
                .onFailure(log::error)
                .getOrNull();
    }
}
