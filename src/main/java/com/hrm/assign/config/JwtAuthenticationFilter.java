package com.hrm.assign.config;

import java.io.IOException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import com.hrm.assign.helper.JwtUtil;
import com.hrm.assign.serviceImpl.CustomUserDetailService;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@Component
public class JwtAuthenticationFilter extends OncePerRequestFilter{

	@Autowired
	JwtUtil jwtUtil;
	
	@Autowired
	CustomUserDetailService customUserDetailService;
	
	Logger log = LoggerFactory.getLogger(JwtAuthenticationFilter.class);
	
	/**
	 * Check whether the request have Authorization token if not then set the response with particular error.
	 */
	@Override
	protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
			throws ServletException, IOException {
		
		String requestTokenHeader = request.getHeader("Authorization");
		String username = null;
		String jwtToken = null;
		
		if(requestTokenHeader != null && requestTokenHeader.startsWith("Bearer ")) {
			jwtToken = requestTokenHeader.substring(7);
			
			try {
				username = jwtUtil.extractUsername(jwtToken);
			}catch (Exception e) {
				
				log.error("Validation error with the JWT Token");
				response.setContentType("application/json");
		        response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
		        response.getOutputStream().println("{ \"error\": \"" +  e.getMessage()+ "\" }");
		        
		        return;
			}
		}
		
		if(null != username && SecurityContextHolder.getContext().getAuthentication() == null) {
            UserDetails userDetails
                    = customUserDetailService.loadUserByUsername(username);

            boolean isValidToken = jwtUtil.validateToken(jwtToken,userDetails);
            if(isValidToken) {
            	
                UsernamePasswordAuthenticationToken usernamePasswordAuthenticationToken
                        = new UsernamePasswordAuthenticationToken(userDetails,
                        null, userDetails.getAuthorities());

                usernamePasswordAuthenticationToken.setDetails(
                        new WebAuthenticationDetailsSource().buildDetails(request)
                );

                SecurityContextHolder.getContext().setAuthentication(usernamePasswordAuthenticationToken);
            }
            
            
        }
		
		filterChain.doFilter(request, response);
	}

	
}
