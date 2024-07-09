package com.penview.security.util;

import java.util.Date;
import java.util.Map;
import java.util.UUID;
import java.util.stream.Collectors;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.stereotype.Component;

import com.auth0.jwt.JWT;
import com.auth0.jwt.JWTVerifier;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.exceptions.JWTVerificationException;
import com.auth0.jwt.interfaces.Claim;
import com.auth0.jwt.interfaces.DecodedJWT;

@Component
public class JwtUtils {
	
	private String keySecret = "b0092863c545a61acc55d201506931da5ab785acafa98295715d0dee5a7ffe4d";
	
	private String userGenerator = "USERAUTHPENVIEW";
	
	public String createToken(Authentication authentication) {
		Algorithm algorithm = Algorithm.HMAC256(keySecret);
		
		String username = authentication.getPrincipal().toString();
		
		String authorities = authentication.getAuthorities().stream()
				.map(GrantedAuthority::getAuthority)
				.collect(Collectors.joining(","));
		
		String token = JWT.create()
				.withIssuer(userGenerator)
				.withSubject(username)
				.withClaim("authorities", authorities)
				.withIssuedAt(new Date())
				.withExpiresAt(new Date(System.currentTimeMillis() + 1800000))
				.withJWTId(UUID.randomUUID().toString())
				.withNotBefore(new Date(System.currentTimeMillis()))
				.sign(algorithm);
		
		return token;
		
	}
	
	public DecodedJWT validate(String token) {
		try {
			Algorithm algorithm = Algorithm.HMAC256(keySecret);
			
			JWTVerifier verifier = JWT.require(algorithm)
					.withIssuer(userGenerator)
					.build();
			
			DecodedJWT decodedJWT = verifier.verify(token);
			
			return decodedJWT;
			
		} catch (JWTVerificationException e) {
			
			throw new JWTVerificationException("Token invalid, not Authorized");
		}
	}
	
	public String extractUsername(DecodedJWT decodedJWT) {
		return decodedJWT.getSubject().toString();
	}
	
	public Claim extractSpecificClaim(DecodedJWT decodedJWT, String claimName) {
		return decodedJWT.getClaim(claimName);
	}
	
	public Map<String, Claim> extractAllClaim(DecodedJWT decodedJWT){
		return decodedJWT.getClaims();
	}
	
}
