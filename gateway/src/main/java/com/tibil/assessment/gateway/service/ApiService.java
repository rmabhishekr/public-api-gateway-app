package com.tibil.assessment.gateway.service;

import java.util.List;

import com.tibil.assessment.gateway.dto.ApiResponseDto;
import com.tibil.assessment.gateway.dto.ApiUrlRequest;

public interface ApiService {

	ApiResponseDto addAndFetchApi(ApiUrlRequest request, String username);
	List<ApiResponseDto> getAllApiResponses(String username);
	void deleteUrlForUser(String id, String username);
}
