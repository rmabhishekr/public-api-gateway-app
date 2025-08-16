package com.tibil.assessment.gateway.config;

import java.util.List;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.reactive.UrlBasedCorsConfigurationSource;

import lombok.RequiredArgsConstructor;

@Configuration
@RequiredArgsConstructor
@EnableMethodSecurity
public class SecurityConfig {

	private final JwtAuthenticationFilter jwtAuthenticationFilter;
    private final JwtAuthenticationEntryPoint jwtAuthenticationEntryPoint;
	
    @Bean
    public PasswordEncoder passwordEncoder() {
    	return new BCryptPasswordEncoder();
    }
    
    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration config) throws Exception{
    	return config.getAuthenticationManager();
    }
    
    @Bean	
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
    	http
    		.csrf(csrf -> csrf.disable())
    		.cors().and()
    		.exceptionHandling(ex -> ex.authenticationEntryPoint(jwtAuthenticationEntryPoint))
    		.authorizeHttpRequests(auth -> auth
    				.requestMatchers("/auth/**").permitAll()
    				.anyRequest().authenticated()
    		)
    		.sessionManagement(sess -> sess.sessionCreationPolicy(SessionCreationPolicy.STATELESS));
    	
    	http.addFilterBefore(jwtAuthenticationFilter, UsernamePasswordAuthenticationFilter.class);
    	
    	return http.build();
    }
   
}
