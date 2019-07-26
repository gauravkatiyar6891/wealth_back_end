package com.moptra.go4wealth.security;

import org.springframework.security.core.GrantedAuthority;

@SuppressWarnings("serial")
public class UserAuthority implements GrantedAuthority {

	private String authority;

	public UserAuthority(String authorityType) {
		this.authority=authorityType;
	}

	@Override
	public String getAuthority() {
		return this.authority;
	}
}
