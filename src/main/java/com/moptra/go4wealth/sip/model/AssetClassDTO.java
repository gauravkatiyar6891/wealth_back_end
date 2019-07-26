package com.moptra.go4wealth.sip.model;

public class AssetClassDTO {

	private Integer assetClassId;
	private Integer userAssetItemId;
	private String assetClass;
	private double assetValue;
	private double futureValue;
	private double duration;
	private Integer goalsOrderId;
	private Integer associateGoalItemId;
	private String associateGoalItemDuration;
	private String associatedGoalName;
	private Integer assetClassRoi;

	public AssetClassDTO(Integer assetClassId, double assetValue) {
		this.assetClassId = assetClassId;
		this.assetValue = assetValue;
	}

	public AssetClassDTO(Integer assetClassId, String assetClass) {
		this.assetClassId = assetClassId;
		this.assetClass = assetClass;
	}

	public AssetClassDTO() {
		super();
	}

	public Integer getAssetClassId() {
		return assetClassId;
	}

	public void setAssetClassId(Integer assetClassId) {
		this.assetClassId = assetClassId;
	}

	public String getAssetClass() {
		return assetClass;
	}

	public void setAssetClass(String assetClass) {
		this.assetClass = assetClass;
	}

	public double getAssetValue() {
		return assetValue;
	}

	public void setAssetValue(double assetValue) {
		this.assetValue = assetValue;
	}

	public double getFutureValue() {
		return futureValue;
	}

	public void setFutureValue(double futureValue) {
		this.futureValue = futureValue;
	}

	public Integer getUserAssetItemId() {
		return userAssetItemId;
	}

	public void setUserAssetItemId(Integer userAssetItemId) {
		this.userAssetItemId = userAssetItemId;
	}

	public double getDuration() {
		return duration;
	}

	public void setDuration(double duration) {
		this.duration = duration;
	}

	public Integer getGoalsOrderId() {
		return goalsOrderId;
	}

	public void setGoalsOrderId(Integer goalsOrderId) {
		this.goalsOrderId = goalsOrderId;
	}

	public Integer getAssociateGoalItemId() {
		return associateGoalItemId;
	}

	public void setAssociateGoalItemId(Integer associateGoalItemId) {
		this.associateGoalItemId = associateGoalItemId;
	}

	public String getAssociateGoalItemDuration() {
		return associateGoalItemDuration;
	}

	public void setAssociateGoalItemDuration(String associateGoalItemDuration) {
		this.associateGoalItemDuration = associateGoalItemDuration;
	}

	public String getAssociatedGoalName() {
		return associatedGoalName;
	}

	public void setAssociatedGoalName(String associatedGoalName) {
		this.associatedGoalName = associatedGoalName;
	}

	public Integer getAssetClassRoi() {
		return assetClassRoi;
	}

	public void setAssetClassRoi(Integer assetClassRoi) {
		this.assetClassRoi = assetClassRoi;
	}

}
