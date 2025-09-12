package com.tibil.assessment.gateway.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.tibil.assessment.gateway.dto.ApiResponseDto;
import com.tibil.assessment.gateway.entity.ApiResponse;
import com.tibil.assessment.gateway.entity.User;

public interface ApiResponseRepository extends JpaRepository<ApiResponse, String> {

	@Query("SELECT new com.tibil.assessment.gateway.dto.ApiResponseDto(au.id, au.url, ar.responseJson) " 
	+ "FROM ApiUrl au LEFT JOIN au.apiResponse ar WHERE au.user = :user")
	List<ApiResponseDto> findByUserWithJson(User user);
	
}
