package com.moptra.go4wealth.prs.model;

public class SchemeRecentViewDTO {

	private Integer schemeId;
	private String schemeCode;
	private String schemeName;
	private String schemeType;
	private String schemePlan;
	private Integer minInvestment;
	private String amcCode;
	private String schemeKeyword;
	private String minSipAmount;
	private String schemeLaunchDate;
	
	public String getMinSipAmount() {
		return minSipAmount;
	}
	public void setMinSipAmount(String minSipAmount) {
		this.minSipAmount = minSipAmount;
	}
	public String getSchemeLaunchDate() {
		return schemeLaunchDate;
	}
	public void setSchemeLaunchDate(String schemeLaunchDate) {
		this.schemeLaunchDate = schemeLaunchDate;
	}
	
	public Integer getSchemeId() {
		return schemeId;
	}
	public void setSchemeId(Integer schemeId) {
		this.schemeId = schemeId;
	}
	public String getSchemeCode() {
		return schemeCode;
	}
	public void setSchemeCode(String schemeCode) {
		this.schemeCode = schemeCode;
	}
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
	public String getSchemePlan() {
		return schemePlan;
	}
	public void setSchemePlan(String schemePlan) {
		this.schemePlan = schemePlan;
	}
	public Integer getMinInvestment() {
		return minInvestment;
	}
	public void setMinInvestment(Integer minInvestment) {
		this.minInvestment = minInvestment;
	}
	public String getAmcCode() {
		return amcCode;
	}
	public void setAmcCode(String amcCode) {
		this.amcCode = amcCode;
	}
	public String getSchemeKeyword() {
		return schemeKeyword;
	}
	public void setSchemeKeyword(String schemeKeyword) {
		this.schemeKeyword = schemeKeyword;
	}
}