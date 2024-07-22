package com.penview.security.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.penview.security.entity.dto.AuthUser;
import com.penview.security.entity.dto.TokenRequest;
import com.auth0.jwt.interfaces.DecodedJWT;
import com.penview.security.entity.dto.AuthLoginRequest;
import com.penview.security.entity.dto.AuthResponse;
import com.penview.security.service.UserDetailServiceImpl;
import com.penview.security.util.JwtUtils;

import jakarta.validation.Valid;

@RestController
//@CrossOrigin
@RequestMapping("/auth/v1")
public class AuthenticationController {
	
	@Autowired
	private UserDetailServiceImpl userDetailService;
	
	@Autowired
	private JwtUtils jwtUtils;
	
	@PostMapping("/login")
	public ResponseEntity<AuthResponse> login(@RequestBody @Valid AuthLoginRequest userRequest){
		return new ResponseEntity<>(this.userDetailService.login(userRequest), HttpStatus.OK);
	}
	
	@PostMapping("/signup")
	public ResponseEntity<AuthResponse> register(@RequestBody @Valid AuthUser authCreateUser){
		return new ResponseEntity<>(this.userDetailService.create(authCreateUser), HttpStatus.OK);
	}
	
	@PostMapping("/validate")
	public ResponseEntity<?> validateToken(@RequestBody @Valid TokenRequest tokenRequest){
		DecodedJWT decodedJWT = this.jwtUtils.validate(tokenRequest.getToken());
		
		if (decodedJWT == null) {
			return ResponseEntity.badRequest().build();
		}
		
		return ResponseEntity.ok(true);
	}

}
