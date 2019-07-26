package com.moptra.go4wealth.prs.model;



public class PrsDTO {
	
	private String mandateStatus;
	private String remarks;
	private String orderStatus;
	private String response;
	private String billerId;
	private String clientCode;
	private String mandateApprovedDate;
	
	public String getBillerId() {
		return billerId;
	}
	public void setBillerId(String billerId) {
		this.billerId = billerId;
	}
	
	public String getClientCode() {
		return clientCode;
	}
	public void setClientCode(String clientCode) {
		this.clientCode = clientCode;
	}
	public String getMandateApprovedDate() {
		return mandateApprovedDate;
	}
	public void setMandateApprovedDate(String mandateApprovedDate) {
		this.mandateApprovedDate = mandateApprovedDate;
	}
	public String getOrderStatus() {
		return orderStatus;
	}
	public void setOrderStatus(String orderStatus) {
		this.orderStatus = orderStatus;
	}
	public String getResponse() {
		return response;
	}
	public void setResponse(String response) {
		this.response = response;
	}
	public String getMandateStatus() {
		return mandateStatus;
	}
	public void setMandateStatus(String mandateStatus) {
		this.mandateStatus = mandateStatus;
	}
	
	public String getRemarks() {
		return remarks;
	}
	public void setRemarks(String remarks) {
		this.remarks = remarks;
	}

}
