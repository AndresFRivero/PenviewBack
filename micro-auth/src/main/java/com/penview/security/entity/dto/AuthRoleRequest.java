package com.penview.security.entity.dto;

import java.util.List;

import org.springframework.validation.annotation.Validated;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Builder
@Data
@NoArgsConstructor
@AllArgsConstructor
@Validated
public class AuthRoleRequest {
	
	private List<String> roles;
}
