package com.kleim.eventmanager.security.token;


import com.kleim.eventmanager.auth.User;
import com.kleim.eventmanager.auth.UserRole;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import javax.crypto.spec.SecretKeySpec;
import java.nio.charset.StandardCharsets;
import java.security.Key;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

@Service
public class JwtTokenManager {

    private final Long tokenLifetime;
    private final Key signKey;


    public JwtTokenManager(
            @Value("${jwt.lifetime}") Long tokenLifetime,
            @Value("${jwt.singature}") String signKey
    ) {
        this.tokenLifetime = tokenLifetime;
        this.signKey = Keys.hmacShaKeyFor(signKey.getBytes());
    }

    public String generateToken(
            User user
    ) {
        Map<String, Object> claims = new HashMap<>();
        claims.put("role", user.role().name());
        Date issuedTime = new Date();
        Date expiredTime = new Date(issuedTime.getTime() + tokenLifetime);
        return Jwts.builder()
                .claims(claims)
                .subject(user.login())
                .issuedAt(issuedTime)
                .expiration(expiredTime)
                .signWith(signKey)
                .compact();

    }


    public boolean isTokenValid(String jwtToken) {
        try {
            Jwts.parser()
                    .setSigningKey(signKey)
                    .build()
                    .parse(jwtToken);
        } catch (Exception e) {
            return false;
        }
        return true;
     }

    public String getLoginFromToken(String jwtToken) {
           return Jwts.parser()
                    .setSigningKey(signKey)
                    .build()
                    .parseClaimsJws(jwtToken)
                    .getPayload()
                    .getSubject();

    }

    public String getRoleFromToken(String jwtToken) {
        return Jwts.parser()
                .setSigningKey(signKey)
                .build()
                .parseClaimsJws(jwtToken)
                .getBody().get("role", String.class);

    }
}










































