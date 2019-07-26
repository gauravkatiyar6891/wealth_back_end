package com.moptra.go4wealth.ticob.model;

public class TransferInMasterDTO {

	private String investerName;
	private String investerEmail;
	private String investerMobile;
	private String pan;

	public String getInvesterName() {
		return investerName;
	}
	public void setInvesterName(String investerName) {
		this.investerName = investerName;
	}
	public String getInvesterEmail() {
		return investerEmail;
	}
	public void setInvesterEmail(String investerEmail) {
		this.investerEmail = investerEmail;
	}
	public String getInvesterMobile() {
		return investerMobile;
	}
	public void setInvesterMobile(String investerMobile) {
		this.investerMobile = investerMobile;
	}
	public String getPan() {
		return pan;
	}
	public void setPan(String pan) {
		this.pan = pan;
	}
}