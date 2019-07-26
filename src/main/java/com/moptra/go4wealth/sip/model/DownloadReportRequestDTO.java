package com.moptra.go4wealth.sip.model;

public class DownloadReportRequestDTO {
	
	private String currResidenceLocationId;
	private String ageval;
	private String incomeval;
	private String maritalStatusId;
	private String incomeSlabCode;
	private String goalsOrderId;
	private String kidsval;
	private String userEmail;
	
	public String getCurrResidenceLocationId() {
		return currResidenceLocationId;
	}
	public void setCurrResidenceLocationId(String currResidenceLocationId) {
		this.currResidenceLocationId = currResidenceLocationId;
	}
	public String getAgeval() {
		return ageval;
	}
	public void setAgeval(String ageval) {
		this.ageval = ageval;
	}
	public String getIncomeval() {
		return incomeval;
	}
	public void setIncomeval(String incomeval) {
		this.incomeval = incomeval;
	}
	public String getMaritalStatusId() {
		return maritalStatusId;
	}
	public void setMaritalStatusId(String maritalStatusId) {
		this.maritalStatusId = maritalStatusId;
	}
	public String getIncomeSlabCode() {
		return incomeSlabCode;
	}
	public void setIncomeSlabCode(String incomeSlabCode) {
		this.incomeSlabCode = incomeSlabCode;
	}
	public String getGoalsOrderId() {
		return goalsOrderId;
	}
	public void setGoalsOrderId(String goalsOrderId) {
		this.goalsOrderId = goalsOrderId;
	}
	public String getKidsval() {
		return kidsval;
	}
	public void setKidsval(String kidsval) {
		this.kidsval = kidsval;
	}
	public String getUserEmail() {
		return userEmail;
	}
	public void setUserEmail(String userEmail) {
		this.userEmail = userEmail;
	}
}