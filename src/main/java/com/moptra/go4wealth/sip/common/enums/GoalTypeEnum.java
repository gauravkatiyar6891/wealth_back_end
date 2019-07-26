package com.moptra.go4wealth.sip.common.enums;

public enum GoalTypeEnum {

	PRE_DEFINED("PD"), USER_DEFINED("UD");

	private String value;

	GoalTypeEnum(String value){
		this.value=value;	
	}

	public String getValue() {
		return value;
	}
}
