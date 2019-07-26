package com.moptra.go4wealth.sip.model;

import com.moptra.go4wealth.bean.AgeSlabs;

public class AgeSlabDTO {

	private String ageSlabName;
	private String ageSlabCode;
	
	public AgeSlabDTO() {
		super();
	}
	
	public AgeSlabDTO(AgeSlabs ageSlabs) {
		this.ageSlabName = ageSlabs.getAgeSlabName();
		this.ageSlabCode = ageSlabs.getAgeSlabCode();
	}
	
	public String getAgeSlabName() {
		return ageSlabName;
	}
	
	public void setAgeSlabName(String ageSlabName) {
		this.ageSlabName = ageSlabName;
	}
	public String getAgeSlabCode() {
		return ageSlabCode;
	}
	public void setAgeSlabCode(String ageSlabCode) {
		this.ageSlabCode = ageSlabCode;
	}	
}
