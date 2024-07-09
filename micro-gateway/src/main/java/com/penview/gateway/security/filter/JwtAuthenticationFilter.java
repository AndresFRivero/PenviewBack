package com.penview.gateway.security.filter;

import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.web.server.ServerWebExchange;
import org.springframework.web.server.WebFilter;
import org.springframework.web.server.WebFilterChain;

import reactor.core.publisher.Mono;

public class JwtAuthenticationFilter implements WebFilter {
	
	private String secretKey = "clave_secreta";

	@Override
	public Mono<Void> filter(ServerWebExchange exchange, WebFilterChain chain) {
		ServerHttpRequest request = exchange.getRequest();
		
		if (!request.getHeaders().containsKey(HttpHeaders.AUTHORIZATION)) {
			exchange.getResponse().setStatusCode(HttpStatus.UNAUTHORIZED);
			return exchange.getResponse().setComplete();
		}
		
		String authorizationHeader = request.getHeaders().getOrEmpty(HttpHeaders.AUTHORIZATION).get(0);
		
		if (!authorizationHeader.startsWith("Bearer ")) {
			exchange.getResponse().setStatusCode(HttpStatus.UNAUTHORIZED);
			return exchange.getResponse().setComplete();
		}
		
		//String token = authorizationHeader.substring(7);
		
		return chain.filter(exchange);
	}

}
