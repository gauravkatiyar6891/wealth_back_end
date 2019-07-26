package com.moptra.go4wealth.admin.model;

import java.math.BigDecimal;

public class RedumptionResponse {

	private String minimumRedumptionAmount;
	private BigDecimal totalAmount;
	private String isRedumptionAllowed;
	private String schemeName;
	private String follioNo;
	private BigDecimal redumptionAmount;
	private String redumptionDate;
	private String redumptionStatus;
	private String type;
	
	public String getType() {
		return type;
	}
	
	public void setType(String type) {
		this.type = type;
	}

	public String getFollioNo() {
		return follioNo;
	}
	public void setFollioNo(String follioNo) {
		this.follioNo = follioNo;
	}
	public BigDecimal getRedumptionAmount() {
		return redumptionAmount;
	}
	public void setRedumptionAmount(BigDecimal redumptionAmount) {
		this.redumptionAmount = redumptionAmount;
	}
	public String getRedumptionDate() {
		return redumptionDate;
	}
	public void setRedumptionDate(String redumptionDate) {
		this.redumptionDate = redumptionDate;
	}
	public String getRedumptionStatus() {
		return redumptionStatus;
	}
	public void setRedumptionStatus(String redumptionStatus) {
		this.redumptionStatus = redumptionStatus;
	}
	public String getSchemeName() {
		return schemeName;
	}
	public void setSchemeName(String schemeName) {
		this.schemeName = schemeName;
	}
	public String getMinimumRedumptionAmount() {
		return minimumRedumptionAmount;
	}
	public void setMinimumRedumptionAmount(String minimumRedumptionAmount) {
		this.minimumRedumptionAmount = minimumRedumptionAmount;
	}
	public BigDecimal getTotalAmount() {
		return totalAmount;
	}
	public void setTotalAmount(BigDecimal totalAmount) {
		this.totalAmount = totalAmount;
	}
	public String getIsRedumptionAllowed() {
		return isRedumptionAllowed;
	}
	public void setIsRedumptionAllowed(String isRedumptionAllowed) {
		this.isRedumptionAllowed = isRedumptionAllowed;
	}
	
}
