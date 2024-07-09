package com.penview.security.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import com.penview.security.filter.JwtAuthenticationFilter;
import com.penview.security.service.UserDetailServiceImpl;
import com.penview.security.util.JwtUtils;

@Configuration
@EnableWebSecurity
@EnableMethodSecurity
public class SecurityConfig {
	
	@Autowired
	private JwtUtils jwtUtils;
	
	@Bean
	public SecurityFilterChain securityFilterChain (HttpSecurity httpSecurity) throws Exception {
		
		return httpSecurity
				.csrf(csrf -> csrf.disable())
				.httpBasic(Customizer.withDefaults())
				.sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
				.authorizeHttpRequests(http -> {
					http.requestMatchers(HttpMethod.POST, "/auth/v1/**").permitAll();
					http.requestMatchers(HttpMethod.GET, "/auth/hello-secured").hasAuthority("CREATE");
					http.requestMatchers(HttpMethod.GET, "/auth/post").hasAuthority("CREATE");
					http.requestMatchers(HttpMethod.GET, "/auth/findAll").hasAnyRole("ADMIN", "DEVELOPER", "INVITED");
					http.anyRequest().denyAll();
				})
				.addFilterBefore(new JwtAuthenticationFilter(jwtUtils), UsernamePasswordAuthenticationFilter.class)
				.build();
	}
	
	/*@Bean
	public SecurityFilterChain securityFilterChain (HttpSecurity httpSecurity) throws Exception {
		return httpSecurity
				.csrf(csrf -> csrf.disable())
				.httpBasic(Customizer.withDefaults())
				.sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
				.build();
	}
	
	@Bean
	public AuthenticationManager authenticationManager () throws Exception {
		return authenticationConfiguration.getAuthenticationManager();
	}*/
	
	@Bean	
	public AuthenticationProvider authenticationProvider (UserDetailServiceImpl userDetailService) {
		DaoAuthenticationProvider provider = new DaoAuthenticationProvider();
		provider.setPasswordEncoder(passwordEncoder());
		provider.setUserDetailsService(userDetailService);
		return provider;
	}
	
	@Bean
	public PasswordEncoder passwordEncoder () {
		return new BCryptPasswordEncoder();
	}

}
