package com.moptra.go4wealth.prs.model;

public class MandatePdfFormRequestDTO {

	private String date;
	private String sponserBankCode;
	private String utilityCode;
	private String formType;//create , modify , cancel
	private String bankAccNo;
	private String accType;
	private String bankName;
	private String ifscCode;
	private String micrNumber;
	private String amountInWorld;
	private String amountInNumber;
	private String mandateNumber;
	private String mobileNo;
	private String clientCode;
	private String email;
	private String fromDate;
	private String tickImageValue;
	
	public String getTickImageValue() {
		return tickImageValue;
	}
	
	public void setTickImageValue(String tickImageValue) {
		this.tickImageValue = tickImageValue;
	}

	public String getDate() {
		return date;
	}
	public void setDate(String date) {
		this.date = date;
	}
	public String getSponserBankCode() {
		return sponserBankCode;
	}
	public void setSponserBankCode(String sponserBankCode) {
		this.sponserBankCode = sponserBankCode;
	}
	public String getUtilityCode() {
		return utilityCode;
	}
	public void setUtilityCode(String utilityCode) {
		this.utilityCode = utilityCode;
	}
	public String getFormType() {
		return formType;
	}
	public void setFormType(String formType) {
		this.formType = formType;
	}
	public String getBankAccNo() {
		return bankAccNo;
	}
	public void setBankAccNo(String bankAccNo) {
		this.bankAccNo = bankAccNo;
	}
	public String getAccType() {
		return accType;
	}
	public void setAccType(String accType) {
		this.accType = accType;
	}
	public String getBankName() {
		return bankName;
	}
	public void setBankName(String bankName) {
		this.bankName = bankName;
	}
	public String getIfscCode() {
		return ifscCode;
	}
	public void setIfscCode(String ifscCode) {
		this.ifscCode = ifscCode;
	}
	public String getMicrNumber() {
		return micrNumber;
	}
	public void setMicrNumber(String micrNumber) {
		this.micrNumber = micrNumber;
	}
	public String getAmountInWorld() {
		return amountInWorld;
	}
	public void setAmountInWorld(String amountInWorld) {
		this.amountInWorld = amountInWorld;
	}
	public String getAmountInNumber() {
		return amountInNumber;
	}
	public void setAmountInNumber(String amountInNumber) {
		this.amountInNumber = amountInNumber;
	}
	public String getMandateNumber() {
		return mandateNumber;
	}
	public void setMandateNumber(String mandateNumber) {
		this.mandateNumber = mandateNumber;
	}
	public String getMobileNo() {
		return mobileNo;
	}
	public void setMobileNo(String mobileNo) {
		this.mobileNo = mobileNo;
	}
	public String getClientCode() {
		return clientCode;
	}
	public void setClientCode(String clientCode) {
		this.clientCode = clientCode;
	}
	public String getEmail() {
		return email;
	}
	public void setEmail(String email) {
		this.email = email;
	}
	public String getFromDate() {
		return fromDate;
	}
	public void setFromDate(String fromDate) {
		this.fromDate = fromDate;
	}

}