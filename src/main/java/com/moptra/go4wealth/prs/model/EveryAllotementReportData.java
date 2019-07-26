package com.moptra.go4wealth.prs.model;

import java.util.List;

public class EveryAllotementReportData {
	
	private String custName;
	private String email;
	private String mobile;
	private String pan;
	private String nominee;
	private String reportType;
	private List<EveryAllotementReportSchemeData> everyAllotementReportSchemeData;

	public String getCustName() {
		return custName;
	}

	public void setCustName(String custName) {
		this.custName = custName;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getMobile() {
		return mobile;
	}

	public void setMobile(String mobile) {
		this.mobile = mobile;
	}

	public String getPan() {
		return pan;
	}

	public void setPan(String pan) {
		this.pan = pan;
	}

	public String getNominee() {
		return nominee;
	}

	public void setNominee(String nominee) {
		this.nominee = nominee;
	}

	public String getReportType() {
		return reportType;
	}

	public void setReportType(String reportType) {
		this.reportType = reportType;
	}

	public List<EveryAllotementReportSchemeData> getEveryAllotementReportSchemeData() {
		return everyAllotementReportSchemeData;
	}
	
	public void setEveryAllotementReportSchemeData(List<EveryAllotementReportSchemeData> everyAllotementReportSchemeData) {
		this.everyAllotementReportSchemeData = everyAllotementReportSchemeData;
	}
	
	
}
