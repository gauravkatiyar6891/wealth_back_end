package com.moptra.go4wealth.sip.common.enums;

public enum InflationTypeEnum {

	EDUCATION("Education"),GENERAL("General");

	private String value;

	InflationTypeEnum(String value){
		this.value=value;	
	}

	public String getValue() {
		return value;
	}
}
