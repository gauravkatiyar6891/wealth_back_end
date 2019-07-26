package com.moptra.go4wealth.sip.common.enums;

public enum MaritalStatusEnum {
	
	Married("MA","A"),Not_Married("NM","B"),Other("OT","C");

	private String value;
	private String slabCode;
	
	MaritalStatusEnum(String value,String slabCode){
	this.value=value;
	this.slabCode=slabCode;
	}

	public String getValue() {
		return value;
	}

	public String getSlabCode() {
		return slabCode;
	}
	
}
