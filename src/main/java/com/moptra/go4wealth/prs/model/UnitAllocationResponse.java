package com.moptra.go4wealth.prs.model;

public class UnitAllocationResponse {

	private String orderConfirmed;
	private String awaitingPayment;
	private String unitAllocation;
	private String unitsInYourAccount;
	private String SchemeName;
	private String amount;
	private String schemeAmcCode;
	
	public String getSchemeName() {
		return SchemeName;
	}
	public void setSchemeName(String schemeName) {
		SchemeName = schemeName;
	}
	public String getAmount() {
		return amount;
	}
	public void setAmount(String amount) {
		this.amount = amount;
	}
	public String getOrderConfirmed() {
		return orderConfirmed;
	}
	public void setOrderConfirmed(String orderConfirmed) {
		this.orderConfirmed = orderConfirmed;
	}
	public String getAwaitingPayment() {
		return awaitingPayment;
	}
	public void setAwaitingPayment(String awaitingPayment) {
		this.awaitingPayment = awaitingPayment;
	}
	public String getUnitAllocation() {
		return unitAllocation;
	}
	public void setUnitAllocation(String unitAllocation) {
		this.unitAllocation = unitAllocation;
	}
	public String getUnitsInYourAccount() {
		return unitsInYourAccount;
	}
	public void setUnitsInYourAccount(String unitsInYourAccount) {
		this.unitsInYourAccount = unitsInYourAccount;
	}
	public String getSchemeAmcCode() {
		return schemeAmcCode;
	}
	public void setSchemeAmcCode(String schemeAmcCode) {
		this.schemeAmcCode = schemeAmcCode;
	}
}
