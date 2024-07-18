package com.penview.security.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import com.penview.security.entity.RoleEntity;
import com.penview.security.entity.UserEntity;
import com.penview.security.entity.dto.AuthUser;
import com.penview.security.entity.dto.AuthLoginRequest;
import com.penview.security.entity.dto.AuthResponse;
import com.penview.security.repository.IRoleRepository;
import com.penview.security.repository.IUserRepository;
import com.penview.security.util.JwtUtils;

@Service
public class UserDetailServiceImpl implements UserDetailsService {
	
	@Autowired
	private IUserRepository userRepository;
	
	@Autowired
	private JwtUtils jwtUtils;
	
	@Autowired
	private PasswordEncoder passwordEncoder;
	
	@Autowired
	private IRoleRepository roleRepository;

	@Override
	public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
		
		UserEntity userEntity = userRepository.findByUsername(username);
		
		if (userEntity == null) {
			return (UserDetails) new UsernameNotFoundException(username);
		}
		
		List<SimpleGrantedAuthority> authorityList = new ArrayList<>();
		
		userEntity.getRoles()
			.forEach(role -> authorityList.add(new SimpleGrantedAuthority("ROLE_".concat(role.getRoleEnum().name()))));
		
		userEntity.getRoles()
			.stream()
			.flatMap(role -> role.getPermissions().stream())
					.forEach(permission -> authorityList.add(new SimpleGrantedAuthority(permission.getName())));
		
		return new User(userEntity.getUsername(), userEntity.getPassword(), authorityList);
	}
	
	public AuthResponse login(AuthLoginRequest authLoginRequest) {
		
		String username = authLoginRequest.getUsername();
		
		String password = authLoginRequest.getPassword();
		
		Authentication authentication = this.authenticated(username, password);
		
		SecurityContextHolder.getContext().setAuthentication(authentication);
		
		String token = jwtUtils.createToken(authentication);
		
		AuthResponse authResponse = new AuthResponse(username, "User Logued succesfully", token, true);
		
		return authResponse;
	}

	public Authentication authenticated(String username, String password) {
		
		UserDetails userDetails = this.loadUserByUsername(username);
		
		if (userDetails == null) {
			throw new BadCredentialsException("Invalid username or password.");
		}
		
		if (!passwordEncoder.matches(password, userDetails.getPassword())) {
			throw new BadCredentialsException("Incorrect password");
		}
		
		return new UsernamePasswordAuthenticationToken(username, password, userDetails.getAuthorities());
	}
	
	public AuthResponse create(AuthUser authCreateUser) {
		
		String username = authCreateUser.getUsername();
		
		String password = authCreateUser.getPassword();
		
		List<String> roles = authCreateUser.getRole().getRoles();
		
		Set<RoleEntity> rolesSet = roleRepository.findRoleEntitiesByRoleEnumIn(roles).stream().collect(Collectors.toSet());
		
		if (rolesSet.isEmpty()) {
			throw new IllegalArgumentException("Roles specified does not exist");
		}
		
		UserEntity userEntity = UserEntity.builder()
				.username(username)
				.password(passwordEncoder.encode(password))
				.roles(rolesSet)
				.build();
		
		UserEntity userCreated = userRepository.save(userEntity);
		
		List<SimpleGrantedAuthority> authorities = new ArrayList<>();
		
		userCreated.getRoles()
		.forEach(role -> 
			authorities.add(new SimpleGrantedAuthority("ROLE_".concat(role.getRoleEnum().name()))));
		
		userCreated.getRoles().stream()
		.flatMap(role -> role.getPermissions().stream())
			.forEach(permission -> authorities.add(new SimpleGrantedAuthority(permission.getName())));
		
		Authentication authentication = new UsernamePasswordAuthenticationToken(userCreated.getUsername(), userCreated.getPassword(), authorities);
		
		String token = jwtUtils.createToken(authentication);
		
		AuthResponse authResponse = new AuthResponse(userCreated.getUsername(), "User created succesfully", token, true);
				
		return authResponse;
	}

}
