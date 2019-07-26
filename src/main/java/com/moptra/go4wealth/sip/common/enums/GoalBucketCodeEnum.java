package com.moptra.go4wealth.sip.common.enums;

public enum GoalBucketCodeEnum {

	/**SHORT_TERM("ST",10d),MEDIUM_TERM("MT",20d),LONG_TERM("LT",30d);**/
	
	SHORT_TERM("ST",3d),MEDIUM_TERM("MT",7d),LONG_TERM("LT",10d);

	//SHORT_TERM("ST",4d),MEDIUM_TERM("MT",5d),LONG_TERM("LT",10d);
	
	private String code;
	private Double duration;

	GoalBucketCodeEnum(String code,Double duration){
		this.code=code;
		this.duration=duration;
	}

	public String getCode() {
		return code;
	}

	public Double getDuration() {
		return duration;
	}
	
}
