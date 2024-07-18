package com.penview.security.filter;

import java.io.IOException;
import java.util.Collection;

import org.springframework.http.HttpHeaders;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.filter.OncePerRequestFilter;

import com.auth0.jwt.interfaces.DecodedJWT;
import com.penview.security.util.JwtUtils;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.NoArgsConstructor;

@NoArgsConstructor
public class JwtAuthenticationFilter extends OncePerRequestFilter {

	private JwtUtils jwtUtils;
	
	public JwtAuthenticationFilter(JwtUtils jwtUtils) {
		this.jwtUtils = jwtUtils;
	}

	@Override
	protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
			throws ServletException, IOException {
		
		String token = request.getHeader(HttpHeaders.AUTHORIZATION);
		
		if (token != null) {
			token = token.substring(7);
			
			DecodedJWT decodedJWT = jwtUtils.validate(token);
			
			String username = jwtUtils.extractUsername(decodedJWT);
			
			String claimAuthorities = jwtUtils.extractSpecificClaim(decodedJWT, "authorities").asString();
			
			Collection<? extends GrantedAuthority> authorities = AuthorityUtils.commaSeparatedStringToAuthorityList(claimAuthorities);
			
			org.springframework.security.core.context.SecurityContext context = SecurityContextHolder.getContext();
			
			Authentication authentication = new UsernamePasswordAuthenticationToken(username, null, authorities);
			
			context.setAuthentication(authentication);
			
			SecurityContextHolder.setContext(context);
		}
		
		filterChain.doFilter(request, response);
		
	}

}
