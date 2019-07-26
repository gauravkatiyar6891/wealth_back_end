package com.moptra.go4wealth.sip.common.enums;

public enum KidsSlabCodeEnum {

	ONE("A"),TWO("B"),GREATER_THAN_TWO("C"),NO_CHILD("D");

	private String value;

	KidsSlabCodeEnum(String value){
		this.value=value;	
	}

	public String getValue() {
		return value;
	}

}
