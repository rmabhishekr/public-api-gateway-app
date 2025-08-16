package com.tibil.assessment.gateway.entity;

import java.time.Instant;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "api_urls")
@Setter
@Getter
@NoArgsConstructor 
@AllArgsConstructor
public class ApiUrl {

	@Id
	@GeneratedValue(strategy = GenerationType.UUID)
	private String id;
	private String name;
	@Column(nullable = false)
	private String url;
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "user_id")
	private User user;
	private Instant createdAt = Instant.now();
	@OneToOne(mappedBy = "apiUrl", cascade = CascadeType.ALL, orphanRemoval = true)
    private ApiResponse apiResponse;
}
