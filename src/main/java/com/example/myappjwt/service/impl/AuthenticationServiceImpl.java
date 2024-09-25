package com.example.myappjwt.service.impl;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import com.example.myappjwt.db_hungha2.mapper.UserMapper;
import com.example.myappjwt.db_hungha2.model.LoginResponse;
import com.example.myappjwt.db_hungha2.model.User;
import com.example.myappjwt.db_hungha2.model.UserExample;
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
    
    public LoginResponse GenerateAccessToken(String username, String password) {
        UserExample userExample = new UserExample();
        userExample.createCriteria().andUsernameEqualTo(username);
        List<User> users = userMapper.selectByExample(userExample);

        logger.info("Authenticating user: {}", username);
        if (users.isEmpty()) {
            logger.warn("User not found: {}", username);
            throw new RuntimeException("User not found");
        }

        User user = users.get(0);
        logger.info("Attempting to authenticate user: {}", username);
        try {
            Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(username, password)
            );
            SecurityContextHolder.getContext().setAuthentication(authentication);
            logger.info("Authentication successful for user: {}", username);     
        } catch (Exception e) {
            logger.error("Authentication failed: {}", e.getMessage(), e);
            throw new RuntimeException("Invalid username or password in serviceimpl");
        }
        
        String accessToken = jwtProvider.generateAccessToken(user.getUsername());
        String refreshToken = jwtProvider.generateRefreshToken(user.getUsername());
        
        LoginResponse loginResponse = new LoginResponse();
        loginResponse.setUsername(username);
        loginResponse.setAccessToken(accessToken);
        loginResponse.setRefreshToken(refreshToken);
        
        return loginResponse;
    }
    
    public LoginResponse RefreshAccessToken(String refreshToken) {
    	String username = jwtProvider.validateRefreshToken(refreshToken);
    	
    	UserExample userExample = new UserExample();
        userExample.createCriteria().andUsernameEqualTo(username);
        List<User> users = userMapper.selectByExample(userExample);
        
        logger.info("Authenticating user: {}", username);
        if (users.isEmpty()) {
            logger.warn("User not found: {}", username);
            throw new RuntimeException("User not found");
        }
        
        User user = users.get(0);
        String newAccessToken = jwtProvider.generateAccessToken(user.getUsername());
        String newrefreshToken = jwtProvider.generateRefreshToken(user.getUsername());
        
        LoginResponse loginResponse = new LoginResponse();
        loginResponse.setUsername(username);
        loginResponse.setAccessToken(newAccessToken);
        loginResponse.setRefreshToken(newrefreshToken);
        
        return loginResponse;
    }
    
    public String queryTest(String username) {
    	UserExample example = new UserExample();
    	example.createCriteria().andUsernameEqualTo(username);
    	List<User> users = userMapper.selectByExample(example);
    	
    	return users.isEmpty() ? "No user found" : users.get(0).getUsername();
    }
}