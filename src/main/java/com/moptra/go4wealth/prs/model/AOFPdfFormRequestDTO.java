package com.moptra.go4wealth.prs.model;

public class AOFPdfFormRequestDTO {
	
	private String arnCode;
	private String subBrokerCode;//it is blank in dounloades form
	private String euin;//it is blank in dounloades form
	private String firstApplicant;
	
	private String nameOfGuardian;//it is blank in dounloades form
	private String pan;//it is blank in dounloades form
	private String address;
	private String addressLine1;
	private String addressLine2;
	
	private String telephoneOff;//it is blank in dounloades form
	private String telephoneRes;//it is blank in dounloades form
	private String email;
	private String faxOff;//it is blank in dounloades form
	private String faxRes;//it is blank in dounloades form
	private String mobile;
	private String modeOfHolding;
	private String occupation;
	
	private String panNumber1;//it is prepopulated
	private String kyc1;//it is blank in dounloades form
	private String dob1;
	
	private String city1;
	private String pincode1;
	private String state1;
	private String country1;

	private String secondApplicant;
	private String panNumber2;//name of second applicant it is blank in form
	private String kyc2;//name of second applicant it is blank in form
	private String dob2;//name of second applicant it is blank in form
	
	private String city2;//Others details of first Applicant overseas address(in case of NRI Investor)
	private String pincode2;//it is blank in dounloades form Others details of first Applicant overseas address(in case of NRI Investor)
	private String country2;//it is blank in dounloades form Others details of first Applicant overseas address(in case of NRI Investor)

	private String thirdApplicant;
	private String panNumber3;//name of third applicant it is blank in form
	private String kyc3;//name of third applicant it is blank in form
	private String dob3;//name of third applicant it is blank in form
	
	private String city3;
	private String pincode3;//it is blank in dounloades form
	private String state3;

	private String nameOfBank;
	private String branch;
	private String accountNo;
	private String accountType;
	private String ifscCode;
	private String bankAddress;
	private String bankCountry;
	private String bankCity;
	private String bankPincode;
	private String bankState;
	
	private String nomineeName;//it is blank in form
	private String relationship;//it is blank in form
	private String gurdianName;//if nominee is minor,it is blank in form
	private String nomineecity;//nominee address, blank in form
	private String nomineePincode;//nominee address, blank in form
	private String nomineeState;//nominee address, blank in form
	private String date;//fill by user
	private String place;//fill by user
	private String nomineeAddress;
	
	private String firstApplicantSignature;//fill by user
	private String secondApplicantSignature;//fill by user
	private String thirdApplicantSignature;//fill by user
	
	private String otherDetails;
	private String overseasAddress;
	private String otherCity;//Others details of first Applicant overseas address(in case of NRI Investor)
	private String otherPincode;//it is blank in dounloades form Others details of first Applicant overseas address(in case of NRI Investor)
	private String otherCountry;//it is blank in dounloades form Others details of first Applicant overseas address(in case of NRI Investor)
	
	
	public String getArnCode() {
		return arnCode;
	}
	public void setArnCode(String arnCode) {
		this.arnCode = arnCode;
	}
	public String getSubBrokerCode() {
		return subBrokerCode;
	}
	public void setSubBrokerCode(String subBrokerCode) {
		this.subBrokerCode = subBrokerCode;
	}
	public String getEuin() {
		return euin;
	}
	public void setEuin(String euin) {
		this.euin = euin;
	}
	public String getFirstApplicant() {
		return firstApplicant;
	}
	public void setFirstApplicant(String firstApplicant) {
		this.firstApplicant = firstApplicant;
	}
	public String getNameOfGuardian() {
		return nameOfGuardian;
	}
	public void setNameOfGuardian(String nameOfGuardian) {
		this.nameOfGuardian = nameOfGuardian;
	}
	public String getPan() {
		return pan;
	}
	public void setPan(String pan) {
		this.pan = pan;
	}
	
	public String getAddress() {
		return address;
	}
	public void setAddress(String address) {
		this.address = address;
	}
	public String getAddressLine1() {
		return addressLine1;
	}
	public void setAddressLine1(String addressLine1) {
		this.addressLine1 = addressLine1;
	}
	public String getAddressLine2() {
		return addressLine2;
	}
	public void setAddressLine2(String addressLine2) {
		this.addressLine2 = addressLine2;
	}
	public String getTelephoneOff() {
		return telephoneOff;
	}
	public void setTelephoneOff(String telephoneOff) {
		this.telephoneOff = telephoneOff;
	}
	public String getTelephoneRes() {
		return telephoneRes;
	}
	public void setTelephoneRes(String telephoneRes) {
		this.telephoneRes = telephoneRes;
	}
	public String getEmail() {
		return email;
	}
	public void setEmail(String email) {
		this.email = email;
	}
	public String getFaxOff() {
		return faxOff;
	}
	public void setFaxOff(String faxOff) {
		this.faxOff = faxOff;
	}
	public String getFaxRes() {
		return faxRes;
	}
	public void setFaxRes(String faxRes) {
		this.faxRes = faxRes;
	}
	public String getMobile() {
		return mobile;
	}
	public void setMobile(String mobile) {
		this.mobile = mobile;
	}
	public String getModeOfHolding() {
		return modeOfHolding;
	}
	public void setModeOfHolding(String modeOfHolding) {
		this.modeOfHolding = modeOfHolding;
	}
	public String getOccupation() {
		return occupation;
	}
	public void setOccupation(String occupation) {
		this.occupation = occupation;
	}
	public String getPanNumber1() {
		return panNumber1;
	}
	public void setPanNumber1(String panNumber1) {
		this.panNumber1 = panNumber1;
	}
	public String getKyc1() {
		return kyc1;
	}
	public void setKyc1(String kyc1) {
		this.kyc1 = kyc1;
	}
	public String getDob1() {
		return dob1;
	}
	public void setDob1(String dob1) {
		this.dob1 = dob1;
	}
	public String getCity1() {
		return city1;
	}
	public void setCity1(String city1) {
		this.city1 = city1;
	}
	public String getPincode1() {
		return pincode1;
	}
	public void setPincode1(String pincode1) {
		this.pincode1 = pincode1;
	}
	public String getState1() {
		return state1;
	}
	public void setState1(String state1) {
		this.state1 = state1;
	}
	public String getCountry1() {
		return country1;
	}
	public void setCountry1(String country1) {
		this.country1 = country1;
	}
	public String getPanNumber2() {
		return panNumber2;
	}
	public void setPanNumber2(String panNumber2) {
		this.panNumber2 = panNumber2;
	}
	public String getKyc2() {
		return kyc2;
	}
	public void setKyc2(String kyc2) {
		this.kyc2 = kyc2;
	}
	public String getDob2() {
		return dob2;
	}
	public void setDob2(String dob2) {
		this.dob2 = dob2;
	}
	public String getCity2() {
		return city2;
	}
	public void setCity2(String city2) {
		this.city2 = city2;
	}
	public String getPincode2() {
		return pincode2;
	}
	public void setPincode2(String pincode2) {
		this.pincode2 = pincode2;
	}
	public String getCountry2() {
		return country2;
	}
	public void setCountry2(String country2) {
		this.country2 = country2;
	}
	public String getPanNumber3() {
		return panNumber3;
	}
	public void setPanNumber3(String panNumber3) {
		this.panNumber3 = panNumber3;
	}
	public String getKyc3() {
		return kyc3;
	}
	public void setKyc3(String kyc3) {
		this.kyc3 = kyc3;
	}
	public String getDob3() {
		return dob3;
	}
	public void setDob3(String dob3) {
		this.dob3 = dob3;
	}
	public String getCity3() {
		return city3;
	}
	public void setCity3(String city3) {
		this.city3 = city3;
	}
	public String getPincode3() {
		return pincode3;
	}
	public void setPincode3(String pincode3) {
		this.pincode3 = pincode3;
	}
	public String getState3() {
		return state3;
	}
	public void setState3(String state3) {
		this.state3 = state3;
	}
	public String getNameOfBank() {
		return nameOfBank;
	}
	public void setNameOfBank(String nameOfBank) {
		this.nameOfBank = nameOfBank;
	}
	public String getBranch() {
		return branch;
	}
	public void setBranch(String branch) {
		this.branch = branch;
	}
	public String getAccountNo() {
		return accountNo;
	}
	public void setAccountNo(String accountNo) {
		this.accountNo = accountNo;
	}
	public String getAccountType() {
		return accountType;
	}
	public void setAccountType(String accountType) {
		this.accountType = accountType;
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
	public String getBankCountry() {
		return bankCountry;
	}
	public void setBankCountry(String bankCountry) {
		this.bankCountry = bankCountry;
	}
	public String getBankCity() {
		return bankCity;
	}
	public void setBankCity(String bankCity) {
		this.bankCity = bankCity;
	}
	public String getBankPincode() {
		return bankPincode;
	}
	public void setBankPincode(String bankPincode) {
		this.bankPincode = bankPincode;
	}
	public String getBankState() {
		return bankState;
	}
	public void setBankState(String bankState) {
		this.bankState = bankState;
	}
	public String getNomineeName() {
		return nomineeName;
	}
	public void setNomineeName(String nomineeName) {
		this.nomineeName = nomineeName;
	}
	public String getRelationship() {
		return relationship;
	}
	public void setRelationship(String relationship) {
		this.relationship = relationship;
	}
	public String getGurdianName() {
		return gurdianName;
	}
	public void setGurdianName(String gurdianName) {
		this.gurdianName = gurdianName;
	}
	public String getNomineecity() {
		return nomineecity;
	}
	public void setNomineecity(String nomineecity) {
		this.nomineecity = nomineecity;
	}
	public String getNomineePincode() {
		return nomineePincode;
	}
	public void setNomineePincode(String nomineePincode) {
		this.nomineePincode = nomineePincode;
	}
	public String getNomineeState() {
		return nomineeState;
	}
	public void setNomineeState(String nomineeState) {
		this.nomineeState = nomineeState;
	}
	public String getDate() {
		return date;
	}
	public void setDate(String date) {
		this.date = date;
	}
	public String getPlace() {
		return place;
	}
	public void setPlace(String place) {
		this.place = place;
	}
	public String getFirstApplicantSignature() {
		return firstApplicantSignature;
	}
	public void setFirstApplicantSignature(String firstApplicantSignature) {
		this.firstApplicantSignature = firstApplicantSignature;
	}
	public String getSecondApplicantSignature() {
		return secondApplicantSignature;
	}
	public void setSecondApplicantSignature(String secondApplicantSignature) {
		this.secondApplicantSignature = secondApplicantSignature;
	}
	public String getThirdApplicantSignature() {
		return thirdApplicantSignature;
	}
	public void setThirdApplicantSignature(String thirdApplicantSignature) {
		this.thirdApplicantSignature = thirdApplicantSignature;
	}
	public String getSecondApplicant() {
		return secondApplicant;
	}
	public void setSecondApplicant(String secondApplicant) {
		this.secondApplicant = secondApplicant;
	}
	public String getThirdApplicant() {
		return thirdApplicant;
	}
	public void setThirdApplicant(String thirdApplicant) {
		this.thirdApplicant = thirdApplicant;
	}
	public String getOtherDetails() {
		return otherDetails;
	}
	public void setOtherDetails(String otherDetails) {
		this.otherDetails = otherDetails;
	}
	public String getOverseasAddress() {
		return overseasAddress;
	}
	public void setOverseasAddress(String overseasAddress) {
		this.overseasAddress = overseasAddress;
	}
	public String getOtherCity() {
		return otherCity;
	}
	public void setOtherCity(String otherCity) {
		this.otherCity = otherCity;
	}
	public String getOtherPincode() {
		return otherPincode;
	}
	public void setOtherPincode(String otherPincode) {
		this.otherPincode = otherPincode;
	}
	public String getOtherCountry() {
		return otherCountry;
	}
	public void setOtherCountry(String otherCountry) {
		this.otherCountry = otherCountry;
	}
	public String getNomineeAddress() {
		return nomineeAddress;
	}
	public void setNomineeAddress(String nomineeAddress) {
		this.nomineeAddress = nomineeAddress;
	}
	
	
	
	
}
