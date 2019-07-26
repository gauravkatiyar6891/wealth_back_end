package com.moptra.go4wealth.prs.model;

public class SimilarSchemeDTO {
	
	private Integer schemeId;
	private String schemeCode;
	private String schemeName;
	private String schemeType;
	private String schemePlan;
	private Integer minInvestment;
	private String amcCode;
	
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
}