package com.moptra.go4wealth.prs.orderapi.response;

public class SwitchOrderEntryResponse {

	private String transCode;
	private String uniqeRefNo;
	private String orderId;
	private String userID;
	private String memberId;
	private String clientCode;
	private String successFlag;
	private String BseRemarks;
	
	
	public String getTransCode() {
		return transCode;
	}
	public void setTransCode(String transCode) {
		this.transCode = transCode;
	}
	public String getUniqeRefNo() {
		return uniqeRefNo;
	}
	public void setUniqeRefNo(String uniqeRefNo) {
		this.uniqeRefNo = uniqeRefNo;
	}
	public String getOrderId() {
		return orderId;
	}
	public void setOrderId(String orderId) {
		this.orderId = orderId;
	}
	public String getUserID() {
		return userID;
	}
	public void setUserID(String userID) {
		this.userID = userID;
	}
	public String getMemberId() {
		return memberId;
	}
	public void setMemberId(String memberId) {
		this.memberId = memberId;
	}
	public String getClientCode() {
		return clientCode;
	}
	public void setClientCode(String clientCode) {
		this.clientCode = clientCode;
	}
	public String getSuccessFlag() {
		return successFlag;
	}
	public void setSuccessFlag(String successFlag) {
		this.successFlag = successFlag;
	}
	public String getBseRemarks() {
		return BseRemarks;
	}
	public void setBseRemarks(String bseRemarks) {
		BseRemarks = bseRemarks;
	}
	
}
