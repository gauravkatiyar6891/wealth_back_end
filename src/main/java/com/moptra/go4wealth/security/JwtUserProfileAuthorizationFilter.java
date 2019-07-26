package com.moptra.go4wealth.security;

import java.io.IOException;
import java.util.List;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.www.BasicAuthenticationFilter;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.moptra.go4wealth.bean.User;
import com.moptra.go4wealth.configuration.JwtConfiguration;
import com.moptra.go4wealth.repository.UserRepository;

import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.MalformedJwtException;

public class JwtUserProfileAuthorizationFilter extends BasicAuthenticationFilter {

	JwtConfiguration jwtConfiguration;
	
	UserRepository userRepository;
	
	UserDetailService userDetailService;
	
    public JwtUserProfileAuthorizationFilter(JwtConfiguration jwtConfiguration,UserRepository userRepository,UserDetailService userDetailService) {
    	super(new AuthenticationManager() {
			public Authentication authenticate(Authentication authentication) throws AuthenticationException {
				return null;
			}
		});
        this.jwtConfiguration = jwtConfiguration;
        this.userRepository = userRepository;
        this.userDetailService = userDetailService;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest req,
                                    HttpServletResponse res,
                                    FilterChain chain) throws IOException, ServletException {
        String header = req.getHeader(jwtConfiguration.headerName);
        if (header == null) {
            chain.doFilter(req, res);
            return;
        }
        UsernamePasswordAuthenticationToken authentication = getAuthentication(header);
        SecurityContextHolder.getContext().setAuthentication(authentication);
        UserPrincipal userPrincipal = (UserPrincipal) authentication.getPrincipal();
        // Refreshes the JWT token.
        JwtUserProfileAuthenticationFilter.addTokenToResponse(jwtConfiguration, res, new UserSession(userPrincipal.getUser().getUserId()),req);
        chain.doFilter(req, res);
    }

    private UsernamePasswordAuthenticationToken getAuthentication(String token) throws IOException {
    	try{
	    	String subjectJson = Jwts.parser()
	    			.setSigningKey(jwtConfiguration.secret.getBytes())
	    			.parseClaimsJws(token)
	    			.getBody()
	    			.getSubject();
	
	    	UserSession userSession = new ObjectMapper().readValue(subjectJson, UserSession.class);
	    	if (userSession != null) {
	    		User user = userRepository.findByUserId(userSession.getProfileId());
	    		List<SimpleGrantedAuthority> authoritties=userDetailService.findUserById(userSession.getProfileId());
	    	      return new UsernamePasswordAuthenticationToken(new UserPrincipal(user,authoritties), null,authoritties);
	    	}
    	}catch(ExpiredJwtException ex){
    		throw new RuntimeException("Token expired",ex);
    	}catch(MalformedJwtException ex){
    		throw new RuntimeException("No Token",ex);
    	}
    	return null;
    }
    
}