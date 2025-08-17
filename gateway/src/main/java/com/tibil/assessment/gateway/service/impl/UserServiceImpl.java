package com.tibil.assessment.gateway.service.impl;

import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import com.tibil.assessment.gateway.config.JwtTokenProvider;
import com.tibil.assessment.gateway.dto.SignupRequest;
import com.tibil.assessment.gateway.dto.UserLoginRequest;
import com.tibil.assessment.gateway.dto.UserLoginResponse;
import com.tibil.assessment.gateway.entity.User;
import com.tibil.assessment.gateway.repository.UserRepository;
import com.tibil.assessment.gateway.service.UserService;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService{

	private final AuthenticationManager authenticationManager;
	private final JwtTokenProvider jwtTokenProvider;
    private final UserRepository userRepository;
    private final BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
	
	@Override
	public UserLoginResponse authenticate(UserLoginRequest request) {
		Authentication authentication = authenticationManager.authenticate(
				new UsernamePasswordAuthenticationToken(request.getUsername(), request.getPassword())
				);
		String token = jwtTokenProvider.generateToken(authentication);
		return new UserLoginResponse(token);
	}

	@Override
	public void signup(SignupRequest request) {
		if(userRepository.existsByUsername(request.getUsername())) {
			throw new RuntimeException("Username already exists.");
		}
		
		User user = new User();
		user.setUsername(request.getUsername());
		user.setPassword(passwordEncoder.encode(request.getPassword()));
		userRepository.save(user);
		
	}

}
