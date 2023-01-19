package com.example.playwithme.security.jwt;

import com.example.playwithme.security.UserDetailsService;
import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.Jwts;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

import java.util.UUID;

@RequiredArgsConstructor
@Slf4j
@Component
public class JwtTokenService {
    private final UserDetailsService userDetailsService;
    @Value("${jwt.secret}")
    private String jwtSecret;

    public Authentication getAuthentication(String token) {
        UUID userId = getId(token);
        UserDetails userDetails = userDetailsService.loadUserById(userId);
        return new UsernamePasswordAuthenticationToken(userId, userDetails, userDetails.getAuthorities());
    }

    public UUID getId(String token) {
        return UUID.fromString(Jwts.parser().setSigningKey(jwtSecret).parseClaimsJws(token).getBody().getSubject());
    }

    public Boolean validateToken(String token) {
        try {
            Jwts.parser().setSigningKey(jwtSecret).parseClaimsJws(token);
            return true;
        } catch (JwtException ex) {
            log.error(ex.getMessage());
        }
        return false;
    }

}
