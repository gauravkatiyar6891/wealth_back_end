package com.moptra.go4wealth.security;

import java.util.List;

import org.springframework.security.core.authority.SimpleGrantedAuthority;

import com.moptra.go4wealth.bean.User;

@SuppressWarnings("serial")
public class UserPrincipal extends org.springframework.security.core.userdetails.User {

	private User user;

	public UserPrincipal(User user,List<SimpleGrantedAuthority> authoritties) {
		super(user.getUsername(), user.getPassword(),authoritties);//load authorities
		this.user = user;
	}

	public User getUser() {
		return user;
	}

	public void setUser(User user) {
		this.user = user;
	}
	
	
}
