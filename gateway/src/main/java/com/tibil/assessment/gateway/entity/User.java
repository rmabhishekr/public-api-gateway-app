package com.tibil.assessment.gateway.entity;

import java.time.Instant;
import java.util.Set;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.Setter;

@Entity
@Table(name = "users")
@Setter
@Getter
public class User {

	@Id
	@GeneratedValue(strategy = GenerationType.UUID)
	private String id;
	@Column(nullable = false, unique = true)
	private String username;
	private String email;
	@Column(nullable = false)
	private String password;
	private Instant createdAt = Instant.now();
	@OneToMany(mappedBy = "user", cascade = CascadeType.ALL, orphanRemoval = true)
    private Set<ApiUrl> apiUrls;
	
}
