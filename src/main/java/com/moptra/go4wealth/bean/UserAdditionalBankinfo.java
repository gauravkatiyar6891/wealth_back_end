package com.moptra.go4wealth.bean;

import static javax.persistence.GenerationType.IDENTITY;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "user_additional_bankinfo", catalog = "go4wealthdb")
public class UserAdditionalBankinfo {
	
	private Integer userAdditionalBankinfoId;
	private int userId;
	private String accountNumber;
	private String ifscCode;
	private String bankName;
	private String bankAddress;
	private String bankBranch;
	private String accountType;
	private String micrCode;
	private String status;
	
	public UserAdditionalBankinfo() {
	}
	
	public UserAdditionalBankinfo(Integer userId, String accountNumber, String ifscCode,String bankName, String bankAddress,String bankBranch,String accountType, String micrCode,String status) {
		this.userId = userId;
		this.accountNumber = accountNumber;
		this.ifscCode = ifscCode;
		this.bankName = bankName;
		this.bankAddress = bankAddress;
		this.bankBranch = bankBranch;
		this.accountType = accountType;
		this.status = status;
	}
	
	
	@Id
	@GeneratedValue(strategy = IDENTITY)
	@Column(name = "USER_ADDITIONAL_BANKINFO_ID", unique = true, nullable = false)
	public Integer getUserAdditionalBankinfoId() {
		return userAdditionalBankinfoId;
	}

	public void setUserAdditionalBankinfoId(Integer userAdditionalBankinfoId) {
		this.userAdditionalBankinfoId = userAdditionalBankinfoId;
	}
	
	@Column(name = "USERID", nullable = false, length = 50)
	public int getUserId() {
		return userId;
	}
	public void setUserId(int userId) {
		this.userId = userId;
	}
	
	@Column(name = "IFSC_CODE", length = 512)
	public String getIfscCode() {
		return ifscCode;
	}
	public void setIfscCode(String ifscCode) {
		this.ifscCode = ifscCode;
	}
	
	@Column(name = "BANK_NAME", length = 512)
	public String getBankName() {
		return bankName;
	}
	public void setBankName(String bankName) {
		this.bankName = bankName;
	}
	
	@Column(name = "BANK_ADDRESS", length = 512)
	public String getBankAddress() {
		return bankAddress;
	}
	public void setBankAddress(String bankAddress) {
		this.bankAddress = bankAddress;
	}
	
	@Column(name = "BANK_BRANCH", length = 512)
	public String getBankBranch() {
		return bankBranch;
	}
	public void setBankBranch(String bankBranch) {
		this.bankBranch = bankBranch;
	}
	
	@Column(name = "ACCOUNT_TYPE", length = 512)
	public String getAccountType() {
		return accountType;
	}
	public void setAccountType(String accountType) {
		this.accountType = accountType;
	}
	
	@Column(name = "MICR_CODE", length = 512)
	public String getMicrCode() {
		return micrCode;
	}
	public void setMicrCode(String micrCode) {
		this.micrCode = micrCode;
	}

	@Column(name = "ACCOUNT_NUMBER", length = 512)
	public String getAccountNumber() {
		return accountNumber;
	}

	public void setAccountNumber(String accountNumber) {
		this.accountNumber = accountNumber;
	}

	@Column(name = "STATUS", length = 512)
	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	
}