package com.tibil.assessment.gateway.utils;

import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

public class PasswordEncoderUtil {

	private static final BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
	
	public static String encode(String rawPassword) {
		return passwordEncoder.encode(rawPassword);
	}
	
}
