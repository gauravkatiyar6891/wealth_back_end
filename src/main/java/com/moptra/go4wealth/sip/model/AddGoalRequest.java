package com.moptra.go4wealth.sip.model;

import java.util.List;

public class AddGoalRequest {

	private Integer userId;
	private Integer orderId;
	private Integer goalBucket;
	private double duartion;
	private String goalName;
	private Double costOfGoal;
	private boolean inflationFlag;
	private double inflation;
	private double roi;
	private String riskProfile;
	List<AssetClassDTO> assetClassList;
	private Integer goalOrderItemId;
	private String riskSum;
	private Integer goalsOrderId;
	private Integer assetId;

	public double getInflation() {
		return inflation;
	}

	public void setInflation(double inflation) {
		this.inflation = inflation;
	}

	public double getRoi() {
		return roi;
	}

	public void setRoi(double roi) {
		this.roi = roi;
	}

	public String getRiskProfile() {
		return riskProfile;
	}

	public void setRiskProfile(String riskProfile) {
		this.riskProfile = riskProfile;
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

	public Double getCostOfGoal() {
		return costOfGoal;
	}

	public void setCostOfGoal(Double costOfGoal) {
		this.costOfGoal = costOfGoal;
	}

	public double getDuartion() {
		return duartion;
	}

	public void setDuartion(double duartion) {
		this.duartion = duartion;
	}

	public boolean isInflationFlag() {
		return inflationFlag;
	}

	public void setInflationFlag(boolean inflationFlag) {
		this.inflationFlag = inflationFlag;
	}

	public List<AssetClassDTO> getAssetClassList() {
		return assetClassList;
	}

	public void setAssetClassList(List<AssetClassDTO> assetClassList) {
		this.assetClassList = assetClassList;
	}

	public Integer getGoalOrderItemId() {
		return goalOrderItemId;
	}

	public void setGoalOrderItemId(Integer goalOrderItemId) {
		this.goalOrderItemId = goalOrderItemId;
	}

	public String getRiskSum() {
		return riskSum;
	}

	public void setRiskSum(String riskSum) {
		this.riskSum = riskSum;
	}

	public Integer getGoalsOrderId() {
		return goalsOrderId;
	}

	public void setGoalsOrderId(Integer goalsOrderId) {
		this.goalsOrderId = goalsOrderId;
	}

	public Integer getAssetId() {
		return assetId;
	}

	public void setAssetId(Integer assetId) {
		this.assetId = assetId;
	}
}
