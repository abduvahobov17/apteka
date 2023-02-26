package com.asgardiateam.aptekaproject.config.security.jwt;

import com.asgardiateam.aptekaproject.constants.SecurityConstants;
import com.asgardiateam.aptekaproject.entity.Admin;
import com.asgardiateam.aptekaproject.exception.AptekaException;
import io.jsonwebtoken.*;
import lombok.extern.log4j.Log4j2;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Component;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

@Log4j2
@Component
public class JWTTokenProvider {

    public String generateToken(Authentication authentication) {
        Admin admin = (Admin) authentication.getPrincipal();
        return generateTokenByUser(admin);
    }

    public String generateTokenByUser(Admin user) {
        Date now = new Date(System.currentTimeMillis());
        Date expireDate = new Date(now.getTime() + SecurityConstants.EXPIRATION_TIME);
        String userId = user.getId().toString();

        Map<String, Object> claimsMap = new HashMap<>();

        claimsMap.put("userName", user.getUsername());
        claimsMap.put("firstName", user.getFirstName());
        claimsMap.put("lastName", user.getLastName());
        claimsMap.put("email", user.getEmail());

        return Jwts.builder()
                .setSubject(userId)
                .addClaims(claimsMap)
                .setIssuedAt(now)
                .setExpiration(expireDate)
                .signWith(SignatureAlgorithm.HS512, SecurityConstants.SECRET)
                .compact();
    }

    public void validateToken(String token) {
        try {
            Jwts.parser()
                    .setSigningKey(SecurityConstants.SECRET)
                    .parseClaimsJws(token);
        } catch (SignatureException |
                 MalformedJwtException |
                 ExpiredJwtException |
                 UnsupportedJwtException |
                 IllegalArgumentException ex) {
            log.error(ex.getMessage());
            throw AptekaException.unauthorized();
        }
    }

    public Long getUserIdFromToken(String token) {
        Claims claims = Jwts.parser()
                .setSigningKey(SecurityConstants.SECRET)
                .parseClaimsJws(token)
                .getBody();

        return Long.parseLong(String.valueOf(claims.get("sub")));
    }

}
