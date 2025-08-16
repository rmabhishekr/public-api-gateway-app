package com.tibil.assessment.gateway.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.tibil.assessment.gateway.entity.ApiUrl;
import com.tibil.assessment.gateway.entity.User;

public interface ApiUrlRepository extends JpaRepository<ApiUrl, String> {

	List<ApiUrl> findByUser(User user);
	Optional<ApiUrl> findByIdAndUser(String id, User user);
}
