package com.penview.gateway.security.filter;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.cloud.gateway.filter.GatewayFilter;
import org.springframework.cloud.gateway.filter.factory.AbstractGatewayFilterFactory;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.server.ServerWebExchange;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.penview.gateway.dto.ErrorResponse;
import com.penview.gateway.dto.TokenRequest;

import reactor.core.publisher.Mono;

@Component
public class GatewayAuthenticationFilter extends AbstractGatewayFilterFactory<GatewayAuthenticationFilter.Config> {
	
	Logger logger = LoggerFactory.getLogger(GatewayAuthenticationFilter.class);
	
	private WebClient webClient;
	
	public GatewayAuthenticationFilter(WebClient.Builder webClient) {
		super(Config.class);
		this.webClient = webClient.build();
	}

	@Override
	public GatewayFilter apply(Config config) {
		return (((exchange, chain) -> {
			
			if (!exchange.getRequest().getHeaders().containsKey(HttpHeaders.AUTHORIZATION)) {
				
				return this.onError(exchange, "Not content authorization header", HttpStatus.BAD_REQUEST);
			}
			
			String token = exchange.getRequest().getHeaders().getFirst(HttpHeaders.AUTHORIZATION).substring(7);
			
			if (token.isEmpty()) {
				
				exchange.getResponse().setStatusCode(HttpStatus.UNAUTHORIZED);
				
				return exchange.getResponse().setComplete();
				
			} else {
				
				TokenRequest tokenDTO = new TokenRequest();
				
				tokenDTO.setToken(token);
				
				return webClient
		                .post()
		                .uri("http://localhost:8090/auth/v1/validate")
		                .contentType(MediaType.APPLICATION_JSON)
		                .bodyValue(tokenDTO)
		                .retrieve()
		                .onStatus(status -> status.isError(), response -> {
		                	
		                	exchange.getResponse().setStatusCode(HttpStatus.UNAUTHORIZED);
		                    exchange.getResponse().getHeaders().setContentType(MediaType.APPLICATION_JSON);
		                    
		                	ErrorResponse errorResponse = new ErrorResponse();
		    		                	
		    		        errorResponse.setCode(HttpStatus.UNAUTHORIZED.value());
		    		        errorResponse.setMessage("UNAUTHORIZED");
		    		                	
		    		        byte[] responseBytes;
		    		            	    
		    		        try {
		    		            responseBytes = new ObjectMapper().writeValueAsBytes(errorResponse);
		    		        
		    		            return exchange.getResponse().writeWith(Mono.just(exchange.getResponse()
		    		                .bufferFactory().wrap(responseBytes)))
		    		                .then(Mono.error(new RuntimeException("Error de servidor")));
		    		            
		    		        } catch (Exception e) {
		    		            
		    		        	return Mono.error(e);
		    		        }
		                	
		                })
		                .bodyToMono(Boolean.class)
		                .flatMap(isValid -> {
		                    if (isValid) {
		                    	
		                        return chain.filter(exchange);
		                        
		                    } else {
		                    	
		                    	exchange.getResponse().setStatusCode(HttpStatus.UNAUTHORIZED);
		        				
		        				return exchange.getResponse().setComplete();
		        				
		                   }
		                });
			}
			
			//return chain.filter(exchange);
					
		}));
	}
	
	private Mono<Void> onError(ServerWebExchange exchange, String errorMessage, HttpStatus httpStatus) {
	    exchange.getResponse().setStatusCode(httpStatus);
	    exchange.getResponse().getHeaders().setContentType(MediaType.APPLICATION_JSON);

	    ErrorResponse errorResponse = new ErrorResponse(httpStatus.value(), errorMessage);
	    byte[] responseBytes;
		try {
			responseBytes = new ObjectMapper().writeValueAsBytes(errorResponse);
			
			return exchange.getResponse().writeWith(Mono.just(exchange.getResponse()
			        .bufferFactory().wrap(responseBytes)));
			
		} catch (JsonProcessingException e) {
			logger.error(e.getMessage(), e);
			return exchange.getResponse().setComplete();
		}
	}
	
	public static class Config {

	}

}
