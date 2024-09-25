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

	private SecretKey secretKey;
	
	public JwtProvider(@Value("${jwts.secret}") String jwtSecret) {
        this.secretKey = Keys.hmacShaKeyFor(Decoders.BASE64.decode(jwtSecret));
	}
	
    int sec = 1000;
	int min = 60 * sec;
	int hour = 60* min;
	int tokenExpiredTime = min/2;
	int tokenRefreshExpiredTime = min;
	
    public String generateAccessToken(String username) {
        Date now = new Date();
        Date expiredDate = new Date(now.getTime() + tokenExpiredTime);
        try {
            String accessToken = Jwts.builder()
            		.subject(username)
                    .expiration(expiredDate)
                    .signWith(secretKey)
                    .compact();
            
            System.out.println("Generated Token: " + accessToken);
            return accessToken;
            
        } catch (Exception e) {
        	e.printStackTrace();
            return null;
        }
    }

    public String validateAccessToken(String token) {
        try {
            return Jwts.parser()
                    .verifyWith(secretKey)
                    .build()
                    .parseSignedClaims(token)
	                .getPayload()
	                .getSubject();
            
        } catch (Exception e) {
        	e.printStackTrace();
            return null;
        }
    }
    
    public String generateRefreshToken(String username) {
        Date now = new Date();
        Date expiredDate = new Date(now.getTime() + tokenRefreshExpiredTime);
        try {
            String refreshToken = Jwts.builder()
            		.subject(username)
                    .expiration(expiredDate)
                    .signWith(secretKey)
                    .compact();
            
            System.out.println("Generated Token: " + refreshToken);
            return refreshToken;
            
        } catch (Exception e) {
        	e.printStackTrace();
            return null;
        }
    }
    
    public String validateRefreshToken(String token) {
        try {
            return Jwts.parser()
                    .verifyWith(secretKey)
                    .build()
                    .parseSignedClaims(token)
	                .getPayload()
	                .getSubject();
            
        } catch (Exception e) {
        	e.printStackTrace();
            return null;
        }
    }
}