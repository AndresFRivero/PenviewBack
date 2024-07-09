package com.penview.security.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.penview.security.entity.dto.AuthCreateUser;
import com.penview.security.entity.dto.AuthLoginRequest;
import com.penview.security.entity.dto.AuthResponse;
import com.penview.security.service.UserDetailServiceImpl;

import jakarta.validation.Valid;

@RestController
@RequestMapping("/auth/v1")
public class AuthAuthenticationController {
	
	@Autowired
	private UserDetailServiceImpl userDetailService;
	
	@PostMapping("/login")
	public ResponseEntity<AuthResponse> login(@RequestBody @Valid AuthLoginRequest userRequest){
		return new ResponseEntity<>(this.userDetailService.login(userRequest), HttpStatus.OK);
	}
	
	@PostMapping("/signup")
	public ResponseEntity<AuthResponse> register(@RequestBody @Valid AuthCreateUser authCreateUser){
		return new ResponseEntity<>(this.userDetailService.create(authCreateUser), HttpStatus.OK);
	}

}
