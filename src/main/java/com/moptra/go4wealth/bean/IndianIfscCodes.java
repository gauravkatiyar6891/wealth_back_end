package com.moptra.go4wealth.bean;

import static javax.persistence.GenerationType.IDENTITY;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "indian_ifsc_codes", catalog = "go4wealthdb")
public class IndianIfscCodes implements java.io.Serializable {
	private Integer ifscCodeId;
	private String ifscCode;
	private String bankName;
	private String mircCode;
	private String branchName;
	private String address;
	private String contact;
	private String city;
	private String district;
	private String state;
	private String field1;
	private String field2;
	private String field3;

	 public IndianIfscCodes() {
	}

	public IndianIfscCodes(String ifscCode,	String bankName, String mircCode, String branchName, String address,String contact, String city, String district, String state,	String field1, String field2,	 String field3) {
		 this.ifscCode=ifscCode ;	
		 this.bankName=bankName;	
		 this.mircCode=mircCode;
		 this.branchName=branchName ;
		 this.address=address;
		 this.contact=contact;
		 this.city=city;
		 this.district=district;
		 this.state=state ;
		 this.field1=field1;
		 this.field2=field2;
		 this.field3=field3;
	}
	@Id
	@GeneratedValue(strategy = IDENTITY)
	@Column(name = "IFSC_CODE_ID", unique = true, nullable = false)
	public Integer getIfscCodeId() {
		return ifscCodeId;
	}

	public void setIfscCodeId(Integer ifscCodeId) {
		this.ifscCodeId = ifscCodeId;
	}


	@Column(name = "IFSC_Code", length = 100)
	public String getIfscCode() {
		return ifscCode;
	}

	public void setIfscCode(String ifscCode) {
		this.ifscCode = ifscCode;
	}

	@Column(name = "BANK", length = 100)
	public String getBankName() {
		return bankName;
	}
	
	public void setBankName(String bankName) {
		this.bankName = bankName;
	}


	@Column(name = "MICR_CODE", length = 100)
	public String getMircCode() {
		return mircCode;
	}

	public void setMircCode(String mircCode) {
		this.mircCode = mircCode;
	}


	@Column(name = "BRANCH", length = 150)
	public String getBranchName() {
		return branchName;
	}

	public void setBranchName(String branchName) {
		this.branchName = branchName;
	}
	

	@Column(name = "ADDRESS", length = 250)
	public String getAddress() {
		return address;
	}

	public void setAddress(String address) {
		this.address = address;
	}


	@Column(name = "CONTACT", length = 150)
	public String getContact() {
		return contact;
	}

	public void setContact(String contact) {
		this.contact = contact;
	}


	@Column(name = "CITY", length = 150)
	public String getCity() {
		return city;
	}

	public void setCity(String city) {
		this.city = city;
	}


	@Column(name = "DISTRICT", length = 200)
	public String getDistrict() {
		return district;
	}

	public void setDistrict(String district) {
		this.district = district;
	}

	@Column(name = "STATE", length = 200)
	public String getState() {
		return state;
	}

	public void setState(String state) {
		this.state = state;
	}


	@Column(name = "FIELD_1", length = 100)
	public String getField1() {
		return field1;
	}

	public void setField1(String field1) {
		this.field1 = field1;
	}


	@Column(name = "FIELD_2", length = 100)
	public String getField2() {
		return field2;
	}

	public void setField2(String field2) {
		this.field2 = field2;
	}


	@Column(name = "FIELD_3", length = 100)
	public String getField3() {
		return field3;
	}

	public void setField3(String field3) {
		this.field3 = field3;
	}

}
