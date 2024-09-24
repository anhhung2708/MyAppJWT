package com.example.myappjwt.service;

public interface AuthenticationService {

	String AuthenticateAndGenerateToken(String username, String password);
	
}