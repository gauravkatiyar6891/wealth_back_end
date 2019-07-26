package com.moptra.go4wealth.prs.model;

import java.math.BigDecimal;

public class EveryAllotementReportSchemeData {

	private String schemeName;
	private String schemeType;
	private String navDate;
	private BigDecimal nav;
	private BigDecimal unit;
	private BigDecimal investement;
	private BigDecimal investedAmount;
	private String sipDate;
	private String startDate;
	private BigDecimal sipAmount;
	private BigDecimal marketValue;
	private int orderNumber;
	private Long sipRegnNo;
	
	
	public int getOrderNumber() {
		return orderNumber;
	}
	public void setOrderNumber(int orderNumber) {
		this.orderNumber = orderNumber;
	}
	public Long getSipRegnNo() {
		return sipRegnNo;
	}
	public void setSipRegnNo(Long sipRegnNo) {
		this.sipRegnNo = sipRegnNo;
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
	public String getNavDate() {
		return navDate;
	}
	public void setNavDate(String navDate) {
		this.navDate = navDate;
	}
	public BigDecimal getNav() {
		return nav;
	}
	public void setNav(BigDecimal nav) {
		this.nav = nav;
	}
	public BigDecimal getUnit() {
		return unit;
	}
	public void setUnit(BigDecimal unit) {
		this.unit = unit;
	}
	public BigDecimal getInvestement() {
		return investement;
	}
	public void setInvestement(BigDecimal investement) {
		this.investement = investement;
	}
	public BigDecimal getInvestedAmount() {
		return investedAmount;
	}
	public void setInvestedAmount(BigDecimal investedAmount) {
		this.investedAmount = investedAmount;
	}
	public String getSipDate() {
		return sipDate;
	}
	public void setSipDate(String sipDate) {
		this.sipDate = sipDate;
	}
	public String getStartDate() {
		return startDate;
	}
	public void setStartDate(String startDate) {
		this.startDate = startDate;
	}
	public BigDecimal getSipAmount() {
		return sipAmount;
	}
	public void setSipAmount(BigDecimal sipAmount) {
		this.sipAmount = sipAmount;
	}
	public BigDecimal getMarketValue() {
		return marketValue;
	}
	public void setMarketValue(BigDecimal marketValue) {
		this.marketValue = marketValue;
	}
	
	
}
