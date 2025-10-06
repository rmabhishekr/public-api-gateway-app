package com.tibil.assessment.gateway.controller;

import java.util.List;
import java.util.Map;

import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

import com.tibil.assessment.gateway.dto.ApiResponseDto;
import com.tibil.assessment.gateway.dto.ApiUrlRequest;
import com.tibil.assessment.gateway.service.ApiService;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api")
@RequiredArgsConstructor
public class ApiController {

	private final ApiService apiService;
	private final RestTemplate restTemplate = new RestTemplate();
	
	@PostMapping("/add")
	public ResponseEntity<ApiResponseDto> addApiUrl(
			@Valid @RequestBody ApiUrlRequest request,
			@AuthenticationPrincipal UserDetails userDetails) {
		ApiResponseDto response = apiService.addAndFetchApi(request, userDetails.getUsername());
		ResponseEntity<String> forEntityRes = restTemplate.getForEntity(request.getUrl(), String.class);
		if(forEntityRes.getStatusCodeValue() == 429) {
			throw new RuntimeException("YouTube is blocking automated requests. Try again later or use official API.");
		}
		return ResponseEntity.ok(response);
	}
	
	@GetMapping("/dashboard")
	public ResponseEntity<List<ApiResponseDto>> getDashboard(
			@AuthenticationPrincipal UserDetails userDetails){
		List<ApiResponseDto> response = apiService.getAllApiResponses(userDetails.getUsername());
		return ResponseEntity.ok(response);
	}

	@GetMapping("/me")
	public ResponseEntity<?> getCurrentUser(@AuthenticationPrincipal UserDetails userDetails){
		return ResponseEntity.ok().body(Map.of("Username", userDetails.getUsername()));
	}

	@DeleteMapping("/{id}")
	public ResponseEntity<?> deleteApiUrl(
			@PathVariable("id") String id,
			@AuthenticationPrincipal UserDetails userDetails) {
		apiService.deleteUrlForUser(id, userDetails.getUsername());
		return ResponseEntity.ok().body("URL deleted successfully.");
	}

}
