package com.moptra.go4wealth.uma.model;

import com.moptra.go4wealth.prs.model.AddressProofDTO;

public class UserLoginResponseDTO {
	
	private Integer userId;
	private String userName;
	private String email;
	private String mobile;
	private String message;
	private String panNumber;
	private String fullName;
	private String pancardName;
	private String dob;
	private String gender;
	private String bankName;
	private String ifscCode;
	private String bankAddress;
	private String bankBranch;
	private String nomineeName;
	private String nomineeRelation;
	private String accountType;
	private String accountNumber;
	private String micrCode;
	private String startDate;
	private String endDate;
	private String motherName;
	private String fatherName;
	private String maritalStatus;
	private String registerType;
	private String occupation;
	private boolean isEmailVerified;
	private boolean isMobileVerified;
	private boolean isPanVerified;
	private boolean isPanExist;
	private boolean isUserImageExist;
	private boolean isSignatureImageExist;
	private boolean isPancardImageExist;
	private boolean isAdharcardImageExist;
	private boolean isIfscCodeVerified;
	private AddressProofDTO addressProof;
	private Integer status;
	private Integer userOverallStatus;
	private String orderStatus;
	
	private String xsipId;
	private String xsipStatus;
	private String isipId;
	private String isipStatus;
	private String uccClientCode;
	private String mandateStatus;
	private String uploadMandateStatus;
	private boolean isUserGoalExist;
	private int goalSize;
	
	public String getUploadMandateStatus() {
		return uploadMandateStatus;
	}
	public void setUploadMandateStatus(String uploadMandateStatus) {
		this.uploadMandateStatus = uploadMandateStatus;
	}
	
	public String getOrderStatus() {
		return orderStatus;
	}
	public void setOrderStatus(String orderStatus) {
		this.orderStatus = orderStatus;
	}
	
	
	public Integer getUserId() {
		return userId;
	}
	public void setUserId(Integer userId) {
		this.userId = userId;
	}
	public String getUserName() {
		return userName;
	}
	public void setUserName(String userName) {
		this.userName = userName;
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
	public String getMessage() {
		return message;
	}
	public void setMessage(String message) {
		this.message = message;
	}
	public String getPanNumber() {
		return panNumber;
	}
	public void setPanNumber(String panNumber) {
		this.panNumber = panNumber;
	}
	public String getFullName() {
		return fullName;
	}
	public void setFullName(String fullName) {
		this.fullName = fullName;
	}
	public String getDob() {
		return dob;
	}
	public void setDob(String dob) {
		this.dob = dob;
	}
	public String getGender() {
		return gender;
	}
	public void setGender(String gender) {
		this.gender = gender;
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
	public String getAccountType() {
		return accountType;
	}
	public void setAccountType(String accountType) {
		this.accountType = accountType;
	}
	public String getAccountNumber() {
		return accountNumber;
	}
	public void setAccountNumber(String accountNumber) {
		this.accountNumber = accountNumber;
	}
	public String getMicrCode() {
		return micrCode;
	}
	public void setMicrCode(String micrCode) {
		this.micrCode = micrCode;
	}
	public String getStartDate() {
		return startDate;
	}
	public void setStartDate(String startDate) {
		this.startDate = startDate;
	}
	public String getEndDate() {
		return endDate;
	}
	public void setEndDate(String endDate) {
		this.endDate = endDate;
	}
	public String getMotherName() {
		return motherName;
	}
	public void setMotherName(String motherName) {
		this.motherName = motherName;
	}
	public String getFatherName() {
		return fatherName;
	}
	public void setFatherName(String fatherName) {
		this.fatherName = fatherName;
	}
	public String getMaritalStatus() {
		return maritalStatus;
	}
	public void setMaritalStatus(String maritalStatus) {
		this.maritalStatus = maritalStatus;
	}
	public String getRegisterType() {
		return registerType;
	}
	public void setRegisterType(String registerType) {
		this.registerType = registerType;
	}
	public boolean isEmailVerified() {
		return isEmailVerified;
	}
	public void setEmailVerified(boolean isEmailVerified) {
		this.isEmailVerified = isEmailVerified;
	}
	public boolean isMobileVerified() {
		return isMobileVerified;
	}
	public void setMobileVerified(boolean isMobileVerified) {
		this.isMobileVerified = isMobileVerified;
	}
	public boolean isPanVerified() {
		return isPanVerified;
	}
	public void setPanVerified(boolean isPanVerified) {
		this.isPanVerified = isPanVerified;
	}
	public boolean isPanExist() {
		return isPanExist;
	}
	public void setPanExist(boolean isPanExist) {
		this.isPanExist = isPanExist;
	}
	public boolean isUserImageExist() {
		return isUserImageExist;
	}
	public void setUserImageExist(boolean isUserImageExist) {
		this.isUserImageExist = isUserImageExist;
	}
	public boolean isSignatureImageExist() {
		return isSignatureImageExist;
	}
	public void setSignatureImageExist(boolean isSignatureImageExist) {
		this.isSignatureImageExist = isSignatureImageExist;
	}
	public boolean isPancardImageExist() {
		return isPancardImageExist;
	}
	public void setPancardImageExist(boolean isPancardImageExist) {
		this.isPancardImageExist = isPancardImageExist;
	}
	public boolean isAdharcardImageExist() {
		return isAdharcardImageExist;
	}
	public void setAdharcardImageExist(boolean isAdharcardImageExist) {
		this.isAdharcardImageExist = isAdharcardImageExist;
	}
	public AddressProofDTO getAddressProof() {
		return addressProof;
	}
	public void setAddressProof(AddressProofDTO addressProof) {
		this.addressProof = addressProof;
	}
	public String getOccupation() {
		return occupation;
	}
	public void setOccupation(String occupation) {
		this.occupation = occupation;
	}
	public boolean isIfscCodeVerified() {
		return isIfscCodeVerified;
	}
	public void setIfscCodeVerified(boolean isIfscCodeVerified) {
		this.isIfscCodeVerified = isIfscCodeVerified;
	}
	public Integer getStatus() {
		return status;
	}
	public void setStatus(Integer status) {
		this.status = status;
	}
	public String getPancardName() {
		return pancardName;
	}
	public void setPancardName(String pancardName) {
		this.pancardName = pancardName;
	}
	public String getNomineeName() {
		return nomineeName;
	}
	public void setNomineeName(String nomineeName) {
		this.nomineeName = nomineeName;
	}
	public String getNomineeRelation() {
		return nomineeRelation;
	}
	public void setNomineeRelation(String nomineeRelation) {
		this.nomineeRelation = nomineeRelation;
	}
	public Integer getUserOverallStatus() {
		return userOverallStatus;
	}
	public void setUserOverallStatus(Integer userOverallStatus) {
		this.userOverallStatus = userOverallStatus;
	}
	public String getMandateStatus() {
		return mandateStatus;
	}
	public void setMandateStatus(String mandateStatus) {
		this.mandateStatus = mandateStatus;
	}
	public String getXsipId() {
		return xsipId;
	}
	public void setXsipId(String xsipId) {
		this.xsipId = xsipId;
	}
	public String getXsipStatus() {
		return xsipStatus;
	}
	public void setXsipStatus(String xsipStatus) {
		this.xsipStatus = xsipStatus;
	}
	public String getIsipId() {
		return isipId;
	}
	public void setIsipId(String isipId) {
		this.isipId = isipId;
	}
	public String getIsipStatus() {
		return isipStatus;
	}
	public void setIsipStatus(String isipStatus) {
		this.isipStatus = isipStatus;
	}
	public String getUccClientCode() {
		return uccClientCode;
	}
	public void setUccClientCode(String uccClientCode) {
		this.uccClientCode = uccClientCode;
	}
	public boolean isUserGoalExist() {
		return isUserGoalExist;
	}
	public void setUserGoalExist(boolean isUserGoalExist) {
		this.isUserGoalExist = isUserGoalExist;
	}
	public int getGoalSize() {
		return goalSize;
	}
	public void setGoalSize(int goalSize) {
		this.goalSize = goalSize;
	}
	
	
}
