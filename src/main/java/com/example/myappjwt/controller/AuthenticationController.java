package com.example.myappjwt.controller;

import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/user")
@CrossOrigin
public class AuthenticationController {

	// Method chính
//	@Autowired
//    private AuthenticationService authenticationService;
//
//    @PostMapping("/login")
//    public ResponseEntity<String> loginRequest(@RequestBody LoginRequest loginRequest) {
//    	try {
//            String token = authenticationService.AuthenticateAndGenerateToken(loginRequest.getUsername(), loginRequest.getPassword());
//            return ResponseEntity.ok(token);
//            
//        } catch (Exception e) {
//            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Invalid username or password.");
//        }
//    }
	
	//Method test
	@GetMapping("/test/get")
	public String testGET() {
		return "OK";
	}
}