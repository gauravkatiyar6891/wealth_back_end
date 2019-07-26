package com.moptra.go4wealth.prs.mfuploadapi.model;

public class PaymentStatusRequest {

	private GetPasswordRequest getPasswordRequest;
	private String userId;
	private String password;
	private String clientCode;
	private String flag;
	private String orderNo;
	private String sagment;
	
	public GetPasswordRequest getGetPasswordRequest() {
		return getPasswordRequest;
	}
	
	public void setGetPasswordRequest(GetPasswordRequest getPasswordRequest) {
		this.getPasswordRequest = getPasswordRequest;
	}
	
	public String getUserId() {
		return userId;
	}
	public void setUserId(String userId) {
		this.userId = userId;
	}
	public String getPassword() {
		return password;
	}
	public void setPassword(String password) {
		this.password = password;
	}
	public String getClientCode() {
		return clientCode;
	}
	public void setClientCode(String clientCode) {
		this.clientCode = clientCode;
	}
	public String getFlag() {
		return flag;
	}
	public void setFlag(String flag) {
		this.flag = flag;
	}
	public String getOrderNo() {
		return orderNo;
	}
	public void setOrderNo(String orderNo) {
		this.orderNo = orderNo;
	}
	public String getSagment() {
		return sagment;
	}
	public void setSagment(String sagment) {
		this.sagment = sagment;
	}
	
	
}
