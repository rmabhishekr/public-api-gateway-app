package com.tibil.assessment.gateway.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.tibil.assessment.gateway.entity.User;

public interface UserRepository extends JpaRepository<User, String> {

	Optional<User> findByUsername(String username);
}
