package com.tibil.assessment.gateway.config;

import java.security.Key;
import java.util.Date;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Component;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;
import jakarta.annotation.PostConstruct;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Component
public class JwtTokenProvider {
	
	@Value("${jwt.secret}")
    private String jwtSecret;

    @Value("${jwt.expiration-in-ms}")
    private int jwtExpirationInMs;

    private Key key;

    @PostConstruct
    public void init() {
        key = Keys.hmacShaKeyFor(jwtSecret.getBytes());
    }
	
	public String generateToken(Authentication authentication) {
		String username = authentication.getName();
		Date now = new Date();
		Date expiryDate = new Date(now.getTime() + jwtExpirationInMs);
		
		return Jwts.builder()
				.setSubject(username)
				.setIssuedAt(now)
				.setExpiration(expiryDate)
				.signWith(key, SignatureAlgorithm.HS256).compact();
	}
	
	public String getUsernameFromJWT(String token){
		Claims claims = Jwts.parserBuilder()
				.setSigningKey(key).build()
				.parseClaimsJws(token)
				.getBody();
		return claims.getSubject();
		
	}
	
	public boolean validateToken(String authToken) {
		try {
			Jwts.parserBuilder().setSigningKey(key).build().parseClaimsJws(authToken);
			return true;
		}catch (JwtException | IllegalArgumentException ex) {
			log.error("Invalid JWL token", ex);
		}
		return false;
	}
}
