package com.example.myappjwt.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.example.myappjwt.db_hungha2.model.LoginRequest;
import com.example.myappjwt.db_hungha2.model.LoginResponse;
import com.example.myappjwt.service.AuthenticationService;

@RestController
@RequestMapping("/user")
@CrossOrigin
public class AuthenticationController {

	@Autowired
    private AuthenticationService authenticationService;

	// Method login/access token
	@PostMapping("/login")
	public ResponseEntity<LoginResponse> Login(@RequestBody LoginRequest loginRequest) {
	    try {
	        LoginResponse loginResponse = authenticationService.GenerateAccessToken(loginRequest.getUsername(), loginRequest.getPassword());
	        return new ResponseEntity<>(loginResponse, HttpStatus.OK);
	    } catch (Exception e) {
	        return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);
	    }
	}
	
	// Method refresh token
	@PostMapping("/refresh")
    public ResponseEntity<LoginResponse> RefreshLogin(@RequestBody LoginResponse loginResponse) {
        try {
            String refreshToken = loginResponse.getRefreshToken();
            loginResponse = authenticationService.RefreshAccessToken(refreshToken);
            return new ResponseEntity<>(loginResponse, HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);
        }
    }
	
	// Method test
	@PostMapping("/query/test")
	public ResponseEntity<String> queryTest(@RequestParam String username) {
	    try {
	        String result = authenticationService.queryTest(username);
	        return ResponseEntity.ok(result);
	    } catch (Exception e) {
	        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("An error occurred: " + e.getMessage());
	    }
	}
}