package com.moptra.go4wealth.prs.model;

public class UserGoalResponse {

	private int goalId;
	private String goalName;
	private int orderId;
	private String action;
	private String follioNo;
	private String schemeCode;
	private int oldGoalId;
	
	public int getOldGoalId() {
		return oldGoalId;
	}
	public void setOldGoalId(int oldGoalId) {
		this.oldGoalId = oldGoalId;
	}
	
	
	public String getFollioNo() {
		return follioNo;
	}
	public void setFollioNo(String follioNo) {
		this.follioNo = follioNo;
	}
	public String getSchemeCode() {
		return schemeCode;
	}
	public void setSchemeCode(String schemeCode) {
		this.schemeCode = schemeCode;
	}
	public int getGoalId() {
		return goalId;
	}
	public void setGoalId(int goalId) {
		this.goalId = goalId;
	}
	public String getGoalName() {
		return goalName;
	}
	public void setGoalName(String goalName) {
		this.goalName = goalName;
	}
	public int getOrderId() {
		return orderId;
	}
	public void setOrderId(int orderId) {
		this.orderId = orderId;
	}
	public String getAction() {
		return action;
	}
	public void setAction(String action) {
		this.action = action;
	}
}