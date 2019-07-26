package com.moptra.go4wealth.sip.model;

import java.util.List;

public class UserGoalDto {
	
	private String description;
	private Integer futureValue;
	private Integer duration;
	private Integer monthlySaving;
	private Integer lumpsumSaving;
	private Integer currestCost;
	private String goalName;
	private Integer goalId;
	private Integer assetValue;
	private Integer assetFutureValue;
	private String riskProfile;
	private String fundType;
	private Integer inflation;
	private Integer roi;
	private Integer totalMonthlySaving;
	private Integer totalLumpsumSaving;
	private Integer totalFutureValue;
	private Integer totalAssetValue;
	private String status;
	private Integer netMonthlySaving;
	private Integer netLumpsumSaving;
	private List<GoalDTO> goalDto;
	
	public String getDescription() {
		return description;
	}
	public void setDescription(String description) {
		this.description = description;
	}
	public Integer getFutureValue() {
		return futureValue;
	}
	public void setFutureValue(Integer futureValue) {
		this.futureValue = futureValue;
	}
	public Integer getDuration() {
		return duration;
	}
	public void setDuration(Integer duration) {
		this.duration = duration;
	}
	public Integer getMonthlySaving() {
		return monthlySaving;
	}
	public void setMonthlySaving(Integer monthlySaving) {
		this.monthlySaving = monthlySaving;
	}
	public Integer getLumpsumSaving() {
		return lumpsumSaving;
	}
	public void setLumpsumSaving(Integer lumpsumSaving) {
		this.lumpsumSaving = lumpsumSaving;
	}
	public Integer getCurrestCost() {
		return currestCost;
	}
	public void setCurrestCost(Integer currestCost) {
		this.currestCost = currestCost;
	}
	public String getGoalName() {
		return goalName;
	}
	public void setGoalName(String goalName) {
		this.goalName = goalName;
	}
	public Integer getAssetValue() {
		return assetValue;
	}
	public void setAssetValue(Integer assetValue) {
		this.assetValue = assetValue;
	}
	public Integer getAssetFutureValue() {
		return assetFutureValue;
	}
	public void setAssetFutureValue(Integer assetFutureValue) {
		this.assetFutureValue = assetFutureValue;
	}
	public String getRiskProfile() {
		return riskProfile;
	}
	public void setRiskProfile(String riskProfile) {
		this.riskProfile = riskProfile;
	}
	public String getFundType() {
		return fundType;
	}
	public void setFundType(String fundType) {
		this.fundType = fundType;
	}
	public Integer getInflation() {
		return inflation;
	}
	public void setInflation(Integer inflation) {
		this.inflation = inflation;
	}
	public Integer getRoi() {
		return roi;
	}
	public void setRoi(Integer roi) {
		this.roi = roi;
	}
	public Integer getTotalMonthlySaving() {
		return totalMonthlySaving;
	}
	public void setTotalMonthlySaving(Integer totalMonthlySaving) {
		this.totalMonthlySaving = totalMonthlySaving;
	}
	public Integer getTotalLumpsumSaving() {
		return totalLumpsumSaving;
	}
	public void setTotalLumpsumSaving(Integer totalLumpsumSaving) {
		this.totalLumpsumSaving = totalLumpsumSaving;
	}
	public Integer getTotalFutureValue() {
		return totalFutureValue;
	}
	public void setTotalFutureValue(Integer totalFutureValue) {
		this.totalFutureValue = totalFutureValue;
	}
	public Integer getTotalAssetValue() {
		return totalAssetValue;
	}
	public void setTotalAssetValue(Integer totalAssetValue) {
		this.totalAssetValue = totalAssetValue;
	}
	public String getStatus() {
		return status;
	}
	public void setStatus(String status) {
		this.status = status;
	}
	public Integer getGoalId() {
		return goalId;
	}
	public void setGoalId(Integer goalId) {
		this.goalId = goalId;
	}
	public List<GoalDTO> getGoalDto() {
		return goalDto;
	}
	public void setGoalDto(List<GoalDTO> goalDto) {
		this.goalDto = goalDto;
	}
	public Integer getNetMonthlySaving() {
		return netMonthlySaving;
	}
	public void setNetMonthlySaving(Integer netMonthlySaving) {
		this.netMonthlySaving = netMonthlySaving;
	}
	public Integer getNetLumpsumSaving() {
		return netLumpsumSaving;
	}
	public void setNetLumpsumSaving(Integer netLumpsumSaving) {
		this.netLumpsumSaving = netLumpsumSaving;
	}

}