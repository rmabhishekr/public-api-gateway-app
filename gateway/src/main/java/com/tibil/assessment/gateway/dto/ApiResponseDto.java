package com.tibil.assessment.gateway.dto;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ApiResponseDto {

	private String url;
    private String jsonResponse;
}
