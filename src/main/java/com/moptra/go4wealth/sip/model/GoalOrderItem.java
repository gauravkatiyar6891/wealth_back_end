package com.moptra.go4wealth.sip.model;

import java.util.Date;

public class GoalOrderItem {

	private Integer goalOrderItemId;
	private Integer userId;
	private Integer orderId;
	private Integer goalBucket;
	private String goalName;
	private Integer costOfGoal;
	private Double futureValue;
	private Integer duration;
	private Date goalDate;
	private Double monthlySaving;
	private Double lumpsumSaving;
	private Double sipValue;
	private Double lumPsumValue;
	
	public Integer getGoalOrderItemId() {
		return goalOrderItemId;
	}
	public void setGoalOrderItemId(Integer goalOrderItemId) {
		this.goalOrderItemId = goalOrderItemId;
	}
	public Integer getUserId() {
		return userId;
	}
	public void setUserId(Integer userId) {
		this.userId = userId;
	}
	public Integer getOrderId() {
		return orderId;
	}
	public void setOrderId(Integer orderId) {
		this.orderId = orderId;
	}
	public Integer getGoalBucket() {
		return goalBucket;
	}
	public void setGoalBucket(Integer goalBucket) {
		this.goalBucket = goalBucket;
	}
	public String getGoalName() {
		return goalName;
	}
	public void setGoalName(String goalName) {
		this.goalName = goalName;
	}
	public Integer getCostOfGoal() {
		return costOfGoal;
	}
	public void setCostOfGoal(Integer costOfGoal) {
		this.costOfGoal = costOfGoal;
	}
	public Double getFutureValue() {
		return futureValue;
	}
	public void setFutureValue(Double futureValue) {
		this.futureValue = futureValue;
	}
	public Integer getDuration() {
		return duration;
	}
	public void setDuration(Integer duration) {
		this.duration = duration;
	}
	public Date getGoalDate() {
		return goalDate;
	}
	public void setGoalDate(Date goalDate) {
		this.goalDate = goalDate;
	}
	public Double getMonthlySaving() {
		return monthlySaving;
	}
	public void setMonthlySaving(Double monthlySaving) {
		this.monthlySaving = monthlySaving;
	}
	public Double getLumpsumSaving() {
		return lumpsumSaving;
	}
	public void setLumpsumSaving(Double lumpsumSaving) {
		this.lumpsumSaving = lumpsumSaving;
	}
	public Double getSipValue() {
		return sipValue;
	}
	public void setSipValue(Double sipValue) {
		this.sipValue = sipValue;
	}
	public Double getLumPsumValue() {
		return lumPsumValue;
	}
	public void setLumPsumValue(Double lumPsumValue) {
		this.lumPsumValue = lumPsumValue;
	}
	
}
