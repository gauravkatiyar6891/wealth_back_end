package com.moptra.go4wealth.security;

import java.io.IOException;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Date;
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
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.moptra.go4wealth.bean.User;
import com.moptra.go4wealth.configuration.JwtConfiguration;
import com.moptra.go4wealth.repository.UserRepository;

import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;

public class JwtUserProfileAuthenticationFilter extends UsernamePasswordAuthenticationFilter {

	public static class LoginProfileRequest {

		private String username;
		private String password;
		
		public String getUsername() {
			return username;
		}

		public void setUsername(String username) {
			this.username = username;
		}

		public String getPassword() {
			return password;
		}

		public void setPassword(String password) {
			this.password = password;
		}
		
	}

	AuthenticationManager authenticationManager;
	JwtConfiguration jwtConfiguration;
	UserRepository userRepository;	
	UserDetailService userDetailService;

	public JwtUserProfileAuthenticationFilter(AuthenticationManager authenticationManager,
			JwtConfiguration jwtConfiguration,UserRepository userRepository,UserDetailService userDetailService) {
		super();
		this.authenticationManager = authenticationManager;
		this.jwtConfiguration = jwtConfiguration;
		this.userRepository = userRepository;
		this.userDetailService = userDetailService;
	}

	@Override
	public Authentication attemptAuthentication(HttpServletRequest req,
			HttpServletResponse res) throws AuthenticationException {
		try {
			LoginProfileRequest loginRequest = new ObjectMapper()
					.readValue(req.getInputStream(), LoginProfileRequest.class);
			List<SimpleGrantedAuthority> authoritties = userDetailService.findByUsername(loginRequest.getUsername());

			return authenticationManager.authenticate(
					new UsernamePasswordAuthenticationToken(
							loginRequest.getUsername(),
							loginRequest.getPassword(),
							authoritties)
					);
		} catch (IOException e) {
			throw new RuntimeException(e);
		}
	}

	@Override
	protected void successfulAuthentication(HttpServletRequest req,
			HttpServletResponse res,
			FilterChain chain,
			Authentication auth) throws IOException, ServletException {
		User user = ((UserPrincipal) auth.getPrincipal()).getUser();
		UserSession userSession = new UserSession(user.getUserId());
		addTokenToResponse(jwtConfiguration, res, userSession,req);
		//res.addHeader("name", user.getFirstName());
		res.addHeader("name", user.getFirstName()+" "+user.getLastName());
		res.addHeader("id", user.getUserId()+"");
		res.addHeader("role", "USER");
		/*
		List<UserRole> userRoleList = userRoleRepository.findAll();
		for (UserRole userRole : userRoleList) {
			if(userRole.getUser().getUserId() == user.getUserId()){
				List<Role> roleList = roleRepository.findAll();
				for (Role role : roleList) {
					if(userRole.getId().getRoleId() == role.getRoleId())
						res.addHeader("role", role.getRoleName());
					}
				}
		}*/
	}

	
	public static void addTokenToResponse(JwtConfiguration jwtConfiguration, HttpServletResponse res, UserSession userSession, HttpServletRequest req) throws JsonProcessingException {
		String plateform = req.getHeader(jwtConfiguration.Platform);
		if(plateform ==null){
			LocalDateTime expirationTime = LocalDateTime.now().plusMinutes(jwtConfiguration.expirationTimeMin);
			String token = Jwts.builder()
					.setSubject(new ObjectMapper().writeValueAsString(userSession))
					.setExpiration(Date.from(expirationTime.atZone(ZoneId.systemDefault()).toInstant()))
					.signWith(SignatureAlgorithm.HS512, jwtConfiguration.secret.getBytes())
					.compact();
			res.addHeader(jwtConfiguration.headerName, token);
		}else if(plateform.equals("Android")){
			LocalDateTime expirationTime = LocalDateTime.now().plusMinutes(jwtConfiguration.androidExpirationTimeMin);
			String token = Jwts.builder()
					.setSubject(new ObjectMapper().writeValueAsString(userSession))
					.setExpiration(Date.from(expirationTime.atZone(ZoneId.systemDefault()).toInstant()))
					.signWith(SignatureAlgorithm.HS512, jwtConfiguration.secret.getBytes())
					.compact();
			res.addHeader(jwtConfiguration.headerName, token);
		}else{
			LocalDateTime expirationTime = LocalDateTime.now().plusMinutes(jwtConfiguration.expirationTimeMin);
			String token = Jwts.builder()
					.setSubject(new ObjectMapper().writeValueAsString(userSession))
					.setExpiration(Date.from(expirationTime.atZone(ZoneId.systemDefault()).toInstant()))
					.signWith(SignatureAlgorithm.HS512, jwtConfiguration.secret.getBytes())
					.compact();
			res.addHeader(jwtConfiguration.headerName, token);
		}
	}



}