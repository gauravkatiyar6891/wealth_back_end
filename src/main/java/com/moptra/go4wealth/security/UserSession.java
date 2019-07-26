package com.moptra.go4wealth.security;

import java.io.Serializable;

public class UserSession implements Serializable {

	private static final long serialVersionUID = 1L;
	
	private Integer profileId;

	public UserSession() {
		super();
		
	}

	public UserSession(Integer talentId) {
		this.profileId = talentId;
	}
	
	public Integer getProfileId() {
		return profileId;
	}

}
