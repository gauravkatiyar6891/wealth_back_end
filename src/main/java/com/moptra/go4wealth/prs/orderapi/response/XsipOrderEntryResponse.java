package com.moptra.go4wealth.prs.orderapi.response;

public class XsipOrderEntryResponse {
	
	private String transCode;
	private String uniqeRefNo;
	private String memberId;
	private String clientCode;
	private String userID;
	private String xsipRegId;
	private String ipAdd;
	private String BseRemarks;
	private String successFlag;
	
	
	
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
	public String getUserID() {
		return userID;
	}
	public void setUserID(String userID) {
		this.userID = userID;
	}
	public String getXsipRegId() {
		return xsipRegId;
	}
	public void setXsipRegId(String xsipRegId) {
		this.xsipRegId = xsipRegId;
	}
	public String getIpAdd() {
		return ipAdd;
	}
	public void setIpAdd(String ipAdd) {
		this.ipAdd = ipAdd;
	}
	public String getBseRemarks() {
		return BseRemarks;
	}
	public void setBseRemarks(String bseRemarks) {
		BseRemarks = bseRemarks;
	}
	public String getSuccessFlag() {
		return successFlag;
	}
	public void setSuccessFlag(String successFlag) {
		this.successFlag = successFlag;
	}
	

}
