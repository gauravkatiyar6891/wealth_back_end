package com.moptra.go4wealth.uma.common.enums;

public enum RegisterType {
	
	MOBILE("MO"),EMAIL("EM"),FACEBOOK("FB"),GMAIL("GM"),GUEST_USER("GU");
	
	String regType;

	private RegisterType(String regType) {
		this.regType = regType;
	}

	public String getRegType() {
		return regType;
	}

}
