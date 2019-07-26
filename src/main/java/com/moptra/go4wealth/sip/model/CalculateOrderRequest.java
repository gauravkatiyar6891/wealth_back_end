package com.moptra.go4wealth.sip.model;

import java.util.List;

public class CalculateOrderRequest {

	private Integer userId;
	private Integer orderId;
	List<AssetClassDTO> assetClassList;
	private double inflation;
	private double roi;
	private String riskProfile;
	private double duration;
	private AssetClassDTO assetClassDto;

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

	public List<AssetClassDTO> getAssetClassList() {
		return assetClassList;
	}

	public void setAssetClassList(List<AssetClassDTO> assetClassList) {
		this.assetClassList = assetClassList;
	}

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

	public double getDuration() {
		return duration;
	}

	public void setDuration(double duration) {
		this.duration = duration;
	}

	public AssetClassDTO getAssetClassDto() {
		return assetClassDto;
	}

	public void setAssetClassDto(AssetClassDTO assetClassDto) {
		this.assetClassDto = assetClassDto;
	}

}
