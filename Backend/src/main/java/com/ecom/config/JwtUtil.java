package com.ecom.config;


import java.nio.charset.StandardCharsets;
import java.util.Date;

import javax.crypto.SecretKey;

import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;

@Component
public class JwtUtil {

    // private final String SECRET_KEY = "ghjklasdfb123";
    private static final String SECRET = "myverysecureandlongenoughsecretkey123!@#"; // >= 32 characters

    SecretKey secretKey = Keys.hmacShaKeyFor(SECRET.getBytes(StandardCharsets.UTF_8));
    private final long ACCESS_TOKEN_VALIDITY = 10 * 60 * 1000; // 10 minutes
    private final long REFRESH_TOKEN_VALIDITY = 24 * 60 * 60 * 1000; // 1 day

    public String generateAccessToken(UserDetails user) {
        return createToken(user.getUsername(), ACCESS_TOKEN_VALIDITY);
    }

    public String generateRefreshToken(UserDetails user) {
        return createToken(user.getUsername(), REFRESH_TOKEN_VALIDITY);
    }

    private String createToken(String subject, long validity) {
        return Jwts.builder()
                .setSubject(subject)

                .setIssuedAt(new Date())
                .setExpiration(new Date(System.currentTimeMillis() + validity))
                .signWith(SignatureAlgorithm.HS256, secretKey)
                .compact();
    }

    public String extractUsername(String token) {
        return Jwts.parser().setSigningKey(secretKey).parseClaimsJws(token)
                .getBody().getSubject();
    }

    public boolean isTokenValid(String token, UserDetails userDetails) {
        final String username = extractUsername(token);
        return username.equals(userDetails.getUsername()) && !isTokenExpired(token);
    }

    private boolean isTokenExpired(String token) {
        return Jwts.parser().setSigningKey(secretKey).parseClaimsJws(token)
                .getBody().getExpiration().before(new Date());
    }
}
