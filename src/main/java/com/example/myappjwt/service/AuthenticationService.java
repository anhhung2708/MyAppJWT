package com.example.myappjwt.service;

import com.example.myappjwt.db_hungha2.model.LoginResponse;

public interface AuthenticationService {

	LoginResponse GenerateAccessToken(String username, String password);
	LoginResponse RefreshAccessToken(String refreshToken);
	
	String queryTest (String username);
}