package com.moptra.go4wealth.prs.model;

public class BankDetailsDTO {
	
	private String bankName;
	private String bankAddress;
	private String bankBranch;
	private String micrCode;
	private String message;
	private String ifscCode;
	private String accountNumber;
	private String isipAllowedStatus;
	
	public String getIsipAllowedStatus() {
		return isipAllowedStatus;
	}
	public void setIsipAllowedStatus(String isipAllowedStatus) {
		this.isipAllowedStatus = isipAllowedStatus;
	}
	
	public String getBankName() {
		return bankName;
	}
	public void setBankName(String bankName) {
		this.bankName = bankName;
	}
	public String getBankAddress() {
		return bankAddress;
	}
	public void setBankAddress(String bankAddress) {
		this.bankAddress = bankAddress;
	}
	public String getBankBranch() {
		return bankBranch;
	}
	public void setBankBranch(String bankBranch) {
		this.bankBranch = bankBranch;
	}
	
	public String getMicrCode() {
		return micrCode;
	}
	public void setMicrCode(String micrCode) {
		this.micrCode = micrCode;
	}
	
	public String getMessage() {
		return message;
	}
	
	public void setMessage(String message) {
		this.message = message;
	}
	public String getIfscCode() {
		return ifscCode;
	}
	public void setIfscCode(String ifscCode) {
		this.ifscCode = ifscCode;
	}
	public String getAccountNumber() {
		return accountNumber;
	}
	public void setAccountNumber(String accountNumber) {
		this.accountNumber = accountNumber;
	}	
}
