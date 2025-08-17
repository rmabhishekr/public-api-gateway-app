package com.tibil.assessment.gateway.service;

import com.tibil.assessment.gateway.dto.SignupRequest;
import com.tibil.assessment.gateway.dto.UserLoginRequest;
import com.tibil.assessment.gateway.dto.UserLoginResponse;

public interface UserService {

	UserLoginResponse authenticate(UserLoginRequest request);
	
	void signup(SignupRequest request);
}
