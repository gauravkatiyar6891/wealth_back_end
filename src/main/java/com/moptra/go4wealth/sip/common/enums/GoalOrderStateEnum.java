package com.moptra.go4wealth.sip.common.enums;

public enum GoalOrderStateEnum {
  
	NEW("N"),WORK_IN_PROGRESS("W"),SAVED("S");

	private String value;

	GoalOrderStateEnum(String value){
		this.value=value;	
	}

	public String getValue() {
		return value;
	}
}
