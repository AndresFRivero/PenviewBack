package com.penview.security.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.penview.security.entity.dto.AuthUser;
import com.penview.security.service.UserService;

@RestController
@RequestMapping("/auth")
public class AuthController {
	
	@Autowired
	private UserService userService;
	
	@GetMapping("/hello")
	public String hello () {
		return "Hola Mundo";
	}
	
	@GetMapping("/hello-secured")
	public String helloSecured () {
		return "Hola Mundo Secured";
	}
	
	@PostMapping("/post")
	public String helloPost () {
		return "Hola Mundo - POST";
	}
	
	@GetMapping("/findAll")
	public ResponseEntity<?> findAll(){
		return ResponseEntity.ok(userService.findAll());
	}
	
	@PostMapping("/username")
	public ResponseEntity<?> findUserByUsername(@RequestBody AuthUser authUser) {
		return ResponseEntity.ok(this.userService.getUserbyUsername(authUser.getUsername()));
	}

}
