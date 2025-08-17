package com.tibil.assessment.gateway.controller;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.tibil.assessment.gateway.dto.SignupRequest;
import com.tibil.assessment.gateway.entity.User;
import com.tibil.assessment.gateway.repository.UserRepository;


@SpringBootTest
@AutoConfigureMockMvc
public class AuthControllerTest {

	@Autowired
    private MockMvc mockMvc;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private ObjectMapper objectMapper;
    
    @BeforeEach
    void setUp() {
        userRepository.deleteAll();
    }
    
    @Test
    void testSignupSuccess() throws Exception {
        SignupRequest request = new SignupRequest();
        request.setUsername("testuser");
        request.setPassword("Password@123"); // must match your password regex

        mockMvc.perform(post("/auth/signup")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request)))
            .andExpect(status().isOk())
            .andExpect(content().string("User registered successfully!"));

        // Verify user in DB
        Optional<User> userOpt = userRepository.findAll().stream()
                                 .filter(u -> "testuser".equals(u.getUsername())).findFirst();
        assert(userOpt.isPresent());
    }
    
    @Test
    void testSignupDuplicateUser() throws Exception {

        User user = new User();
        user.setUsername("testuser");
        user.setPassword("Password@123");
        userRepository.save(user);

        // Trying to signup again with same username
        SignupRequest request = new SignupRequest();
        request.setUsername("testuser");
        request.setPassword("Password@123");

        mockMvc.perform(post("/auth/signup")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request)))
            .andExpect(status().is4xxClientError());
    }
    
    @Test
    void testSignupWeakPassword() throws Exception {
        SignupRequest request = new SignupRequest();
        request.setUsername("testuser2");
        request.setPassword("abc"); // invalid

        mockMvc.perform(post("/auth/signup")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request)))
            .andExpect(status().isBadRequest());
    }
    
    @Test
    void testLoginAndGetDashboard() throws Exception {
        // First, register user
        SignupRequest signupRequest = new SignupRequest();
        signupRequest.setUsername("apiuser");
        signupRequest.setPassword("Password@123");
        mockMvc.perform(post("/auth/signup")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(signupRequest)));

        // Now, login to get JWT
        String loginPayload = "{ \"username\": \"apiuser\", \"password\": \"Password@123\" }";

        String loginResponse = mockMvc.perform(post("/auth/login")
                .contentType(MediaType.APPLICATION_JSON)
                .content(loginPayload))
            .andExpect(status().isOk())
            .andReturn().getResponse().getContentAsString();

        String jwtToken = extractToken(loginResponse); // Write utility method to parse token!

        // Now, request /api/dashboard with JWT
        mockMvc.perform(get("/api/dashboard")
                .header("Authorization", "Bearer " + jwtToken))
            .andExpect(status().isOk());
    }
    
    private String extractToken(String response) {
        // assumes response is: { "token": "xyz.jwt.token" }
        try {
            return objectMapper.readTree(response).get("token").asText();
        } catch (Exception e) {
            throw new RuntimeException("Could not extract JWT token from login response");
        }
    }

}
