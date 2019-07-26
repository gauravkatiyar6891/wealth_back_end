package com.moptra.go4wealth.uma.common.enums;

public enum UserStatus {

	GUEST("1"),ACTIVE("2"),DEACTIVE("3"),DELETED("4");

	private String status;

	private UserStatus(String status) {
		this.status = status;
	}

	public String getStatus() {
		return status;
	}

}
