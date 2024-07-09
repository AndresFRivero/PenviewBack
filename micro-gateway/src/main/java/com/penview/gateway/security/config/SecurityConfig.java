package com.penview.gateway.security.config;

import org.springframework.context.annotation.Configuration;

@Configuration
//@EnableWebFluxSecurity
public class SecurityConfig {

	/*@Bean
	public SecurityWebFilterChain securityWebFilterChain (ServerHttpSecurity http) {
        http.csrf(csrf -> csrf.disable()
                .authorizeExchange(exchange -> exchange
                        .pathMatchers("/login", "register").permitAll()
                        .anyExchange().authenticated())
                .oauth2Client());
		return http.build();
	}*/
}
