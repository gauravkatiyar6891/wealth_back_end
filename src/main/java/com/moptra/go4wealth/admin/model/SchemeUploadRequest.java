package com.moptra.go4wealth.admin.model;

public class SchemeUploadRequest {
	
	private String fileName;
	private String fileType;
	private String base64;
	
	private String schemeCode;
	private String schemeName;
	private String priority;
	
	private String goalName;
	private String goalAmount;
	private String goalDuration;
	private String sequence;
	
	private String navDate;
	private String navValue;
	
	private String schemeType;

	public String getFileName() {
		return fileName;
	}
	public void setFileName(String fileName) {
		this.fileName = fileName;
	}
	public String getFileType() {
		return fileType;
	}
	public void setFileType(String fileType) {
		this.fileType = fileType;
	}
	public String getBase64() {
		return base64;
	}
	public void setBase64(String base64) {
		this.base64 = base64;
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
	public String getPriority() {
		return priority;
	}
	public void setPriority(String priority) {
		this.priority = priority;
	}
	public String getGoalName() {
		return goalName;
	}
	public void setGoalName(String goalName) {
		this.goalName = goalName;
	}
	public String getSequence() {
		return sequence;
	}
	public void setSequence(String sequence) {
		this.sequence = sequence;
	}
	public String getGoalAmount() {
		return goalAmount;
	}
	public void setGoalAmount(String goalAmount) {
		this.goalAmount = goalAmount;
	}
	public String getGoalDuration() {
		return goalDuration;
	}
	public void setGoalDuration(String goalDuration) {
		this.goalDuration = goalDuration;
	}
	public String getNavDate() {
		return navDate;
	}
	public void setNavDate(String navDate) {
		this.navDate = navDate;
	}
	public String getNavValue() {
		return navValue;
	}
	public void setNavValue(String navValue) {
		this.navValue = navValue;
	}
	public String getSchemeType() {
		return schemeType;
	}
	public void setSchemeType(String schemeType) {
		this.schemeType = schemeType;
	}
}