package com.moptra.go4wealth.ticob.model;

import java.math.BigDecimal;

public class TransferInRecordDTO {
	
	private int transferInId;
	private String folioNo;
	private String schemeName;
	private String sipAmount;
	private String schemeCode;
	private boolean isEnachEnable;
	private boolean isBillerEnable;
	private String[] sipAllowedDate;
	private String sipDate;
	private String investmentType;
	private int minSipAmount;
	private int minLumpsumAmount;
	private boolean isIsipAllowed;
	private boolean isSipAllowed;
	private boolean isLumpsumAllowed;
	private BigDecimal investedAmount;
	private String amcCode;
	private BigDecimal currentNav;
	private String schemeType;
	private BigDecimal allotedUnit;
	private BigDecimal availableUnit;
	private String goalName;
	private int goalId;
	private String isRedemptionAllowed;
	private String startedOn;
	private BigDecimal minimumRedemptionAmount;
	private BigDecimal minAdditionalAmount;
	private String maxSipAmount;
	private String maxLumpsumAmount;
	
	
	
	public BigDecimal getMinAdditionalAmount() {
		return minAdditionalAmount;
	}
	public void setMinAdditionalAmount(BigDecimal minAdditionalAmount) {
		this.minAdditionalAmount = minAdditionalAmount;
	}
	public String getMaxSipAmount() {
		return maxSipAmount;
	}
	public void setMaxSipAmount(String maxSipAmount) {
		this.maxSipAmount = maxSipAmount;
	}
	public String getMaxLumpsumAmount() {
		return maxLumpsumAmount;
	}
	public void setMaxLumpsumAmount(String maxLumpsumAmount) {
		this.maxLumpsumAmount = maxLumpsumAmount;
	}
	public String getFolioNo() {
		return folioNo;
	}
	public void setFolioNo(String folioNo) {
		this.folioNo = folioNo;
	}
	public String getSchemeName() {
		return schemeName;
	}
	public void setSchemeName(String schemeName) {
		this.schemeName = schemeName;
	}
	public String getSchemeCode() {
		return schemeCode;
	}
	public void setSchemeCode(String schemeCode) {
		this.schemeCode = schemeCode;
	}
	public String getInvestmentType() {
		return investmentType;
	}
	public void setInvestmentType(String investmentType) {
		this.investmentType = investmentType;
	}
	public String[] getSipAllowedDate() {
		return sipAllowedDate;
	}
	public void setSipAllowedDate(String[] sipAllowedDate) {
		this.sipAllowedDate = sipAllowedDate;
	}
	public String getSipDate() {
		return sipDate;
	}
	public void setSipDate(String sipDate) {
		this.sipDate = sipDate;
	}
	public int getMinSipAmount() {
		return minSipAmount;
	}
	public void setMinSipAmount(int minSipAmount) {
		this.minSipAmount = minSipAmount;
	}
	public int getMinLumpsumAmount() {
		return minLumpsumAmount;
	}
	public void setMinLumpsumAmount(int minLumpsumAmount) {
		this.minLumpsumAmount = minLumpsumAmount;
	}
	public int getTransferInId() {
		return transferInId;
	}
	public void setTransferInId(int transferInId) {
		this.transferInId = transferInId;
	}
	public boolean isIsipAllowed() {
		return isIsipAllowed;
	}
	public void setIsipAllowed(boolean isIsipAllowed) {
		this.isIsipAllowed = isIsipAllowed;
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
	public String getSipAmount() {
		return sipAmount;
	}
	public void setSipAmount(String sipAmount) {
		this.sipAmount = sipAmount;
	}
	public String getAmcCode() {
		return amcCode;
	}
	public void setAmcCode(String amcCode) {
		this.amcCode = amcCode;
	}
	public BigDecimal getCurrentNav() {
		return currentNav;
	}
	public void setCurrentNav(BigDecimal currentNav) {
		this.currentNav = currentNav;
	}
	public String getSchemeType() {
		return schemeType;
	}
	public void setSchemeType(String schemeType) {
		this.schemeType = schemeType;
	}
	public BigDecimal getAllotedUnit() {
		return allotedUnit;
	}
	public void setAllotedUnit(BigDecimal allotedUnit) {
		this.allotedUnit = allotedUnit;
	}
	public String getGoalName() {
		return goalName;
	}
	public void setGoalName(String goalName) {
		this.goalName = goalName;
	}
	public int getGoalId() {
		return goalId;
	}
	public void setGoalId(int goalId) {
		this.goalId = goalId;
	}
	public String getStartedOn() {
		return startedOn;
	}
	public void setStartedOn(String startedOn) {
		this.startedOn = startedOn;
	}
	public BigDecimal getMinimumRedemptionAmount() {
		return minimumRedemptionAmount;
	}
	public void setMinimumRedemptionAmount(BigDecimal minimumRedemptionAmount) {
		this.minimumRedemptionAmount = minimumRedemptionAmount;
	}
	public String getIsRedemptionAllowed() {
		return isRedemptionAllowed;
	}
	public void setIsRedemptionAllowed(String isRedemptionAllowed) {
		this.isRedemptionAllowed = isRedemptionAllowed;
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
	public BigDecimal getInvestedAmount() {
		return investedAmount;
	}
	public void setInvestedAmount(BigDecimal investedAmount) {
		this.investedAmount = investedAmount;
	}
	public BigDecimal getAvailableUnit() {
		return availableUnit;
	}
	public void setAvailableUnit(BigDecimal availableUnit) {
		this.availableUnit = availableUnit;
	}
}