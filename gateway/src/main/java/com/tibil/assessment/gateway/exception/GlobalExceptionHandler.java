package com.tibil.assessment.gateway.exception;

import java.util.HashMap;
import java.util.Map;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.AuthenticationException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import io.jsonwebtoken.ExpiredJwtException;

@ControllerAdvice
public class GlobalExceptionHandler {
	
	@ExceptionHandler(ExpiredJwtException.class)
	public ResponseEntity<?> handleExpiredJwtException(ExpiredJwtException ex){
		Map<String, String> response = new HashMap<>();
		
		response.put("error", "Token Expired");
		response.put("message", ex.getMessage());
		
		return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(response);
	}
	
	@ExceptionHandler(AuthenticationException.class)
	public ResponseEntity<?> handleAuthenticationException(AuthenticationException ex) {
		Map<String, String> response = new HashMap<>();
		
		response.put("error", "Invalid Credentials");
		response.put("message", ex.getMessage());
		
		return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(response);
	}
	
	@ExceptionHandler(RuntimeException.class)
	public ResponseEntity<?> handleRuntimeException(RuntimeException ex) {
		Map<String, String> response = new HashMap<>();
		
		response.put("error", "Error");
		response.put("message", ex.getMessage());
		
		return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);
	}
	
	@ExceptionHandler(MethodArgumentNotValidException.class)
	public ResponseEntity<?> handleValidationException(MethodArgumentNotValidException ex) {
		Map<String, String> errors = new HashMap<>();
		ex.getBindingResult().getFieldErrors().forEach(
				error -> errors.put(error.getField(), error.getDefaultMessage()));
		return ResponseEntity.badRequest().body(errors);
	}
	
	@ExceptionHandler(ResourceNotFoundException.class)
	public ResponseEntity<?> handleNotFound(ResourceNotFoundException ex) {
	    Map<String, String> error = Map.of("error", "Not Found", "message", ex.getMessage());
	    return ResponseEntity.status(HttpStatus.NOT_FOUND).body(error);
	}
	
	@ExceptionHandler(Exception.class)
	public ResponseEntity<?> handleGenericException(Exception ex) {
	    Map<String, String> error = Map.of("error", "Internal Server Error", "message", ex.getMessage());
	    return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(error);
	}
}
