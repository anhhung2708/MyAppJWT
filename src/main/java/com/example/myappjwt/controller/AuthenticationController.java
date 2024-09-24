package com.example.myappjwt.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.myappjwt.model.LoginRequest;
import com.example.myappjwt.service.AuthenticationService;

@RestController
@RequestMapping("/user")
@CrossOrigin
public class AuthenticationController {

	// Method chính
	@Autowired
    private AuthenticationService authenticationService;

    @PostMapping("/login")
    public ResponseEntity<String> loginRequest(@RequestBody LoginRequest loginRequest) {
    	try {
            String token = authenticationService.AuthenticateAndGenerateToken(loginRequest.getUsername(), loginRequest.getPassword());
            return ResponseEntity.ok(token);
            
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Invalid username or password in controller");
        }
    }
	
	//Method test
	@GetMapping("/test/get")
	public String testGET() {
		return "OK";
	}
}