package com.tibil.assessment.gateway.service.impl;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.client.RestTemplate;

import com.tibil.assessment.gateway.dto.ApiResponseDto;
import com.tibil.assessment.gateway.dto.ApiUrlRequest;
import com.tibil.assessment.gateway.entity.ApiResponse;
import com.tibil.assessment.gateway.entity.ApiUrl;
import com.tibil.assessment.gateway.entity.User;
import com.tibil.assessment.gateway.exception.ResourceNotFoundException;
import com.tibil.assessment.gateway.factory.JsonParserFactory;
import com.tibil.assessment.gateway.factory.JsonParserStrategy;
import com.tibil.assessment.gateway.repository.ApiResponseRepository;
import com.tibil.assessment.gateway.repository.ApiUrlRepository;
import com.tibil.assessment.gateway.repository.UserRepository;
import com.tibil.assessment.gateway.service.ApiService;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class ApiServiceImpl implements ApiService {

	private final UserRepository userRepository;
	private final ApiUrlRepository apiUrlRepository;
	private final ApiResponseRepository	apiResponseRepository;
	
	private final JsonParserFactory jsonParserFactory;
	private final RestTemplate restTemplate = new RestTemplate();
	
	@Transactional
	@Override
	public ApiResponseDto addAndFetchApi(ApiUrlRequest request, String username) {
		User user = userRepository.findByUsername(username)
				.orElseThrow(() -> new ResourceNotFoundException("User Not Found: "+ username));
		// Checking if URL is already saved for user
		ApiUrl apiUrl = apiUrlRepository.findByUser(user).stream()
				.filter(u -> u.getUrl().equals(request.getUrl()))
				.findFirst()
				.orElse(null);
		if(apiUrl == null) {
			apiUrl = new ApiUrl();
			apiUrl.setUser(user);
			apiUrl.setUrl(request.getUrl());
			apiUrl = apiUrlRepository.save(apiUrl);
		}
		
		//fetch data from external API
		String jsonResponse = restTemplate.getForObject(request.getUrl(), String.class);
		
		// select parser strategy based on URL
		JsonParserStrategy parser = jsonParserFactory.getParser(request.getUrl());
		String parsedContent = parser.parse(jsonResponse);
		
		ApiResponse response = apiUrl.getApiResponse();
		if(response == null) {
			response = new ApiResponse();
			response.setApiUrl(apiUrl);
		}
		
		response.setResponseJson(parsedContent);
		apiResponseRepository.save(response);
		
		apiUrl.setApiResponse(response);
		
		return new ApiResponseDto(apiUrl.getId(), apiUrl.getUrl(), response.getResponseJson());
	}

	@Override
	@Transactional(readOnly = true)
	public List<ApiResponseDto> getAllApiResponses(String username) {
		
		User user = userRepository.findByUsername(username)
			.orElseThrow(() -> new ResourceNotFoundException("User Not Found: "+ username));
		
		List<ApiUrl> apiUrls = apiUrlRepository.findByUser(user); //if it is inside transaction
		
		//eagerly access LOB field 
		List<ApiResponseDto> dtos = apiUrls.stream()
				.map(apiUrl -> {
					String responseJson = null;
							if(apiUrl.getApiResponse() != null) {
								responseJson = apiUrl.getApiResponse().getResponseJson();
							}
					return new ApiResponseDto(apiUrl.getId(), apiUrl.getUrl(), responseJson);
				}).collect(Collectors.toList());
		return dtos;
	}

	@Override
	@Transactional
	public void deleteUrlForUser(String id, String username) {
		User user = userRepository.findByUsername(username)
		        .orElseThrow(() -> new ResourceNotFoundException("User not found: " + username));
			
		    ApiUrl apiUrl = apiUrlRepository.findByIdAndUser(id, user)
		    		.orElseThrow(() -> new ResourceNotFoundException("URL not found for this user"));
		    apiUrlRepository.delete(apiUrl);
	}

}
