package com.penview.security.entity.dto;

import com.fasterxml.jackson.annotation.JsonPropertyOrder;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@JsonPropertyOrder({"username", "message", "token", "status"})
public class AuthResponse {

	private String username;
	
	private String message;
	
	private String token;
	
	private boolean status;
}
