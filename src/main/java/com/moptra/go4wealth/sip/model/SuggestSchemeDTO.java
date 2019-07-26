package com.moptra.go4wealth.sip.model;

public class SuggestSchemeDTO {

	private String schemeName;
	private String schemeType;
	private String schemeLaunchDate;
	private int minimumPurchaseAmount;
	private int schemeId;
	private String schemeKeyword;
	private String schemeCode;
	private int sequence;
	
	public String getSchemeName() {
		return schemeName;
	}
	public void setSchemeName(String schemeName) {
		this.schemeName = schemeName;
	}
	public String getSchemeType() {
		return schemeType;
	}
	public void setSchemeType(String schemeType) {
		this.schemeType = schemeType;
	}
	public String getSchemeLaunchDate() {
		return schemeLaunchDate;
	}
	public void setSchemeLaunchDate(String schemeLaunchDate) {
		this.schemeLaunchDate = schemeLaunchDate;
	}
	public int getMinimumPurchaseAmount() {
		return minimumPurchaseAmount;
	}
	public void setMinimumPurchaseAmount(int minimumPurchaseAmount) {
		this.minimumPurchaseAmount = minimumPurchaseAmount;
	}
	public int getSchemeId() {
		return schemeId;
	}
	public void setSchemeId(int schemeId) {
		this.schemeId = schemeId;
	}
	public String getSchemeKeyword() {
		return schemeKeyword;
	}
	public void setSchemeKeyword(String schemeKeyword) {
		this.schemeKeyword = schemeKeyword;
	}
	public String getSchemeCode() {
		return schemeCode;
	}
	public void setSchemeCode(String schemeCode) {
		this.schemeCode = schemeCode;
	}
	public int getSequence() {
		return sequence;
	}
	public void setSequence(int sequence) {
		this.sequence = sequence;
	}
}