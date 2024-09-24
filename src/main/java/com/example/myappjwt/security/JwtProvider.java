package com.example.myappjwt.security;

import java.util.Date;

import javax.crypto.SecretKey;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;

@Component
public class JwtProvider {

    @Value("${jwts.secret}")
	private String jwtSecret;
    
    int sec = 1000;
	int min = 60 * sec;
	int hour = 60* min;
	int expiredTime = 24 * hour;

    public String generateToken(String username) {
        Date now = new Date();
        Date expiredDate = new Date(now.getTime() + expiredTime);
        try {
            SecretKey secretKey = Keys.hmacShaKeyFor(Decoders.BASE64URL.decode(jwtSecret));
            String token = Jwts.builder()
                    .signWith(secretKey)
                    .subject(username)
                    .expiration(expiredDate)
                    .compact();
            return token;
            
        } catch (Exception e) {
            return null;
        }
    }

    public String validateToken(String token) {
        try {
            SecretKey secretKey = Keys.hmacShaKeyFor(Decoders.BASE64URL.decode(jwtSecret));
            return Jwts.parser()
                    .verifyWith(secretKey)
                    .build()
                    .parseSignedClaims(token)
	                .getPayload()
	                .getSubject();
            
        } catch (Exception e) {
            return null;
        }
    }
}