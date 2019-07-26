package com.moptra.go4wealth.sip.model;

public class KidsSlabDTO {

	private String kidsSlabName;
	private String kidsSlabCode;
	public KidsSlabDTO() {
		super();
	}
	
	public KidsSlabDTO(String name,String code) {
		this.kidsSlabCode = code;
		this.kidsSlabName = name;
	}

	public String getKidsSlabName() {
		return kidsSlabName;
	}

	public void setKidsSlabName(String kidsSlabName) {
		this.kidsSlabName = kidsSlabName;
	}

	public String getKidsSlabCode() {
		return kidsSlabCode;
	}

	public void setKidsSlabCode(String kidsSlabCode) {
		this.kidsSlabCode = kidsSlabCode;
	}
	
}
