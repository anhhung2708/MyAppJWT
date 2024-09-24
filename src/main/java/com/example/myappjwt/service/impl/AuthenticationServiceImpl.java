package com.example.myappjwt.service.impl;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import com.example.myappjwt.mapper.UserMapper;
import com.example.myappjwt.model.User;
import com.example.myappjwt.security.JwtProvider;
import com.example.myappjwt.service.AuthenticationService;

@Service
public class AuthenticationServiceImpl implements AuthenticationService {

    @Autowired
    private JwtProvider jwtProvider;
    
    @Autowired
    private UserMapper userMapper;

    @Autowired
    private AuthenticationManager authenticationManager;

    private static final Logger logger = LoggerFactory.getLogger(AuthenticationServiceImpl.class);
    public String AuthenticateAndGenerateToken(String username, String password) {
        User user = userMapper.selectByUsername(username);
        logger.info("Authenticating user: {}", username);
        if (user == null) {
        	logger.warn("User not found: {}", username);
            throw new RuntimeException("User not found");
        }
        
        else {
        	logger.info("Attempting to authenticate user: {}", username);
        	try {
        		Authentication authentication = authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(username, password));
                SecurityContextHolder.getContext().setAuthentication(authentication);
        		logger.info("Authentication successful for user: {}", username);
        		
        	} catch (Exception e) {
        		logger.error("Authentication failed: {}", e.getMessage(), e);
        		throw new RuntimeException("Invalid username or password in serviceimpl");
        	}
        }
        
        return jwtProvider.generateToken(user.getUsername());
    }
}