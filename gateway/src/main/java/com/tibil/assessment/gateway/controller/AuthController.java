package com.tibil.assessment.gateway.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.tibil.assessment.gateway.dto.SignupRequest;
import com.tibil.assessment.gateway.dto.UserLoginRequest;
import com.tibil.assessment.gateway.dto.UserLoginResponse;
import com.tibil.assessment.gateway.service.UserService;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/auth")
@RequiredArgsConstructor
public class AuthController {

	private final UserService userService;
	
	@PostMapping("/login")
	public ResponseEntity<UserLoginResponse> login(@Valid @RequestBody UserLoginRequest request){
		UserLoginResponse response = userService.authenticate(request);
		return ResponseEntity.ok(response);
	}
	
	@PostMapping("/signup")
	public ResponseEntity<?> signup(@Valid @RequestBody SignupRequest request){
		userService.signup(request);
		return ResponseEntity.ok().body("User registered successfully!");
	}
}
