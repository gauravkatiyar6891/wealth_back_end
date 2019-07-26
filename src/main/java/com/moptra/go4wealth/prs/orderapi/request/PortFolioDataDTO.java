package com.moptra.go4wealth.prs.orderapi.request;

import java.math.BigDecimal;

public class PortFolioDataDTO {

	public BigDecimal investedAmount;
	public BigDecimal currentAmount;
	public BigDecimal nav;
	public String follioNo;
	public String type;
	public String statedOn;
	public String schemeName;
	public String amcCode;
	public int goalId;
	public String goalName;
	public BigDecimal allotedUnit;
	public BigDecimal currentNav;
	public String currentDate;
	public String status;
	public int orderId;
	public String sipDate;
	private String schemeType;
	private BigDecimal minimumRedumptionAmount;
	private BigDecimal totalAmount;
	private String isRedumptionAllowed;
	private String schemeCode;
	private BigDecimal minSipAmount;
	private BigDecimal minLumpsumAmount;
	private String[] availableSipDate;
	private boolean isEnachEnable;
	private boolean isBillerEnable;
	private boolean isIsipAllowed;
	private boolean isSipAllowed;
	private boolean isLumpsumAllowed;
	private BigDecimal minAdditionalAmount;
	private String maximumPurchaseAmount;
	private String sipMaxInstallmentAmount;
	
	
	
	public String getMaximumPurchaseAmount() {
		return maximumPurchaseAmount;
	}
	public void setMaximumPurchaseAmount(String maximumPurchaseAmount) {
		this.maximumPurchaseAmount = maximumPurchaseAmount;
	}
	public String getSipMaxInstallmentAmount() {
		return sipMaxInstallmentAmount;
	}
	public void setSipMaxInstallmentAmount(String sipMaxInstallmentAmount) {
		this.sipMaxInstallmentAmount = sipMaxInstallmentAmount;
	}
	public BigDecimal getMinAdditionalAmount() {
		return minAdditionalAmount;
	}
	public void setMinAdditionalAmount(BigDecimal minAdditionalAmount) {
		this.minAdditionalAmount = minAdditionalAmount;
	}
	
	/** New Code Start **/
	private String sipRegNo;
	
	public String getSipRegNo() {
		return sipRegNo;
	}
	
	public void setSipRegNo(String sipRegNo) {
		this.sipRegNo = sipRegNo;
	}
	/** New Code End **/
	
	public String getSchemeType() {
		return schemeType;
	}
	public void setSchemeType(String schemeType) {
		this.schemeType = schemeType;
	}
	public String getSipDate() {
		return sipDate;
	}
	public void setSipDate(String sipDate) {
		this.sipDate = sipDate;
	}
	public String getCurrentDate() {
		return currentDate;
	}
	public void setCurrentDate(String currentDate) {
		this.currentDate = currentDate;
	}
	
	public void setOrderId(int orderId) {
		this.orderId = orderId;
	}
	public int getOrderId() {
		return orderId;
	}
	public void setStatus(String status) {
		this.status = status;
	}
	
     public String getStatus() {
		return status;
	}
	
	public void setAllotedUnit(BigDecimal allotedUnit) {
		this.allotedUnit = allotedUnit;
	}
	public BigDecimal getAllotedUnit() {
		return allotedUnit;
	}
	public void setGoalId(int goalId) {
		this.goalId = goalId;
	}
	public int getGoalId() {
		return goalId;
	}
	public void setGoalName(String goalName) {
		this.goalName = goalName;
	}
	public String getGoalName() {
		return goalName;
	}
	public void setAmcCode(String amcCode) {
		this.amcCode = amcCode;
	}
	public String getAmcCode() {
		return amcCode;
	}
	public void setSchemeName(String schemeName) {
		this.schemeName = schemeName;
	}
	public String getSchemeName() {
		return schemeName;
	}
	public BigDecimal getInvestedAmount() {
		return investedAmount;
	}
	public void setInvestedAmount(BigDecimal investedAmount) {
		this.investedAmount = investedAmount;
	}
	public BigDecimal getCurrentAmount() {
		return currentAmount;
	}
	public void setCurrentAmount(BigDecimal currentAmount) {
		this.currentAmount = currentAmount;
	}
	public BigDecimal getNav() {
		return nav;
	}
	public void setNav(BigDecimal nav) {
		this.nav = nav;
	}
	public String getFollioNo() {
		return follioNo;
	}
	public void setFollioNo(String follioNo) {
		this.follioNo = follioNo;
	}
	public String getType() {
		return type;
	}
	public void setType(String type) {
		this.type = type;
	}
	public String getStatedOn() {
		return statedOn;
	}
	public void setStatedOn(String statedOn) {
		this.statedOn = statedOn;
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
	public BigDecimal getMinimumRedumptionAmount() {
		return minimumRedumptionAmount;
	}
	public void setMinimumRedumptionAmount(BigDecimal minimumRedumptionAmount) {
		this.minimumRedumptionAmount = minimumRedumptionAmount;
	}
	public String getSchemeCode() {
		return schemeCode;
	}
	public void setSchemeCode(String schemeCode) {
		this.schemeCode = schemeCode;
	}
	public BigDecimal getMinSipAmount() {
		return minSipAmount;
	}
	public void setMinSipAmount(BigDecimal minSipAmount) {
		this.minSipAmount = minSipAmount;
	}
	public BigDecimal getMinLumpsumAmount() {
		return minLumpsumAmount;
	}
	public void setMinLumpsumAmount(BigDecimal minLumpsumAmount) {
		this.minLumpsumAmount = minLumpsumAmount;
	}
	public boolean isIsipAllowed() {
		return isIsipAllowed;
	}
	public void setIsipAllowed(boolean isIsipAllowed) {
		this.isIsipAllowed = isIsipAllowed;
	}
	public boolean isEnachEnable() {
		return isEnachEnable;
	}
	public void setEnachEnable(boolean isEnachEnable) {
		this.isEnachEnable = isEnachEnable;
	}
	public boolean isBillerEnable() {
		return isBillerEnable;
	}
	public void setBillerEnable(boolean isBillerEnable) {
		this.isBillerEnable = isBillerEnable;
	}
	public BigDecimal getCurrentNav() {
		return currentNav;
	}
	public void setCurrentNav(BigDecimal currentNav) {
		this.currentNav = currentNav;
	}
	public String[] getAvailableSipDate() {
		return availableSipDate;
	}
	public void setAvailableSipDate(String[] availableSipDate) {
		this.availableSipDate = availableSipDate;
	}
	public boolean isSipAllowed() {
		return isSipAllowed;
	}
	public void setSipAllowed(boolean isSipAllowed) {
		this.isSipAllowed = isSipAllowed;
	}
	public boolean isLumpsumAllowed() {
		return isLumpsumAllowed;
	}
	public void setLumpsumAllowed(boolean isLumpsumAllowed) {
		this.isLumpsumAllowed = isLumpsumAllowed;
	}
}