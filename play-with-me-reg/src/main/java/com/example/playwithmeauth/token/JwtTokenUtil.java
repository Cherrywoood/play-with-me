package com.example.playwithmeauth.token;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;

import java.util.Date;
import java.util.UUID;

public class JwtTokenUtil {
    private static final String JWT_SECRET = System.getenv("jwt_secret");

    private static final long VALIDITY_IN_MILLISECONDS = 3600000;

    public static String createToken(UUID id, String username) {
        Claims claims = Jwts.claims().setSubject(String.valueOf(id));
        claims.put("username", username);
        return Jwts.builder()
                .setClaims(claims)
                .setIssuedAt(new Date())
                .setExpiration(new Date(System.currentTimeMillis() + VALIDITY_IN_MILLISECONDS))
                .signWith(SignatureAlgorithm.HS256, JWT_SECRET)
                .compact();
    }
}
