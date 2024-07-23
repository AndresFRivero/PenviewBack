package com.penview.security.entity.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Builder
@Data
@NoArgsConstructor
@AllArgsConstructor
public class AuthUser {
	
	@NotBlank
	private String username;
	
	@NotBlank
	private String password;
	
	private AuthRoleRequest role;

}
