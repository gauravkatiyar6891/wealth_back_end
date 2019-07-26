package com.moptra.go4wealth.ticob.common.rest;

import javax.xml.bind.annotation.XmlType;

@XmlType(propOrder = { "status", "message", "data"})
public class GoForWealthTICOBResponseInfo {

	private String status;
	private String message;
	private Long totalRecords;
	private Object data;	

	public Long getTotalRecords() {
		return totalRecords;
	}
	public void setTotalRecords(long totalRecords) {
		this.totalRecords = totalRecords;
	}
	public Object getData() {
		return data;
	}
	public void setData(Object data) {
		this.data = data;
	}
	public String getStatus() {
		return status;
	}
	public void setStatus(String status) {
		this.status = status;
	}
	public String getMessage() {
		return message;
	}
	public void setMessage(String message) {
		this.message = message;
	}
}
