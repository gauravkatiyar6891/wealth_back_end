package com.moptra.go4wealth.sip.model;

import com.moptra.go4wealth.bean.MaritalStatus;

public class MaritalDTO {

	private Integer maritalStatusId;
	private String maritalStatusName;
	private String maritalStatusCode;
	private String maritalStatusValue;
	
	public MaritalDTO() {
		super();
	}
	
	public MaritalDTO(Integer maritalStatusId, String maritalStatusName) {
		this.maritalStatusId = maritalStatusId;
		this.maritalStatusName = maritalStatusName;
	}
	
	public MaritalDTO(MaritalStatus maritalStatus) {
		this.maritalStatusName = maritalStatus.getMaritalStatusName();
		this.maritalStatusCode = maritalStatus.getMaritalStatusCode();
	}
	
	public MaritalDTO(Integer maritalStatusId, String maritalStatusName,String maritalStatusValue) {
		this.maritalStatusId = maritalStatusId;
		this.maritalStatusName = maritalStatusName;
		this.maritalStatusValue = maritalStatusValue;
	}

	public Integer getMaritalStatusId() {
		return maritalStatusId;
	}
	public void setMaritalStatusId(Integer maritalStatusId) {
		this.maritalStatusId = maritalStatusId;
	}
	public String getMaritalStatusName() {
		return maritalStatusName;
	}
	public void setMaritalStatusName(String maritalStatusName) {
		this.maritalStatusName = maritalStatusName;
	}

	public String getMaritalStatusCode() {
		return maritalStatusCode;
	}

	public void setMaritalStatusCode(String maritalStatusCode) {
		this.maritalStatusCode = maritalStatusCode;
	}

	public String getMaritalStatusValue() {
		return maritalStatusValue;
	}

	public void setMaritalStatusValue(String maritalStatusValue) {
		this.maritalStatusValue = maritalStatusValue;
	}
}
