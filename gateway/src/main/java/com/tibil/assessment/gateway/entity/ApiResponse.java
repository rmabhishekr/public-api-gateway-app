package com.tibil.assessment.gateway.entity;

import java.time.Instant;

import jakarta.persistence.Basic;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.Lob;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "api_responses")
@Data
@NoArgsConstructor 
@AllArgsConstructor
public class ApiResponse {

	@Id
	@GeneratedValue(strategy = GenerationType.UUID)
	private String id;
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "api_url_id")
	private ApiUrl apiUrl;
	@Lob
    @Column(nullable = false, columnDefinition = "TEXT")
	private String responseJson;
	private Integer statusCode;
	private Instant fetchedAt = Instant.now();
	@Column(columnDefinition = "text")
	private String summary;
}
