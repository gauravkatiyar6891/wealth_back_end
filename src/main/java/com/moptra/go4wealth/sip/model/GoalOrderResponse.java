package com.moptra.go4wealth.sip.model;

import java.math.BigDecimal;
import java.util.List;

import com.moptra.go4wealth.bean.User;

public class GoalOrderResponse {

	private Integer goalsOrderId;
	private User user;
	private String description;
	private String riskProfile;
	private double inflation;
	private double roi;
	private double totalMonthlySaving;
	private double totalLumpsumSaving;
	private double totalMonthlyAdjustment;
	private double totalLumpsumAdjusment;
	private double totalFutureValue;
	private double totalAssetValue;
	private String status;
	private String generalSipMsg;
	private List<GoalOrderItem> goals;
	private List<SuggestSchemeDTO> suggestSchemeDTOList;
	private List<AssetClassDTO> assetList;

	private BigDecimal netSip;
	private BigDecimal netLumpsum;
	private BigDecimal netRequiredFutureValue;
	private BigDecimal assetFutureValueTotal;
	
	private Integer goalsOrderItemId;
	private double futureValue;
	private double monthlySaving;
	private double lumpsumSaving;
	private String isRiskCalculate;
	private String duration;
	private String associateGoalItemDuration;
	
	public Integer getGoalsOrderId() {
		return goalsOrderId;
	}

	public void setGoalsOrderId(Integer goalsOrderId) {
		this.goalsOrderId = goalsOrderId;
	}

	public User getUser() {
		return user;
	}

	public void setUser(User user) {
		this.user = user;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public String getRiskProfile() {
		return riskProfile;
	}

	public void setRiskProfile(String riskProfile) {
		this.riskProfile = riskProfile;
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

	public double getTotalMonthlySaving() {
		return totalMonthlySaving;
	}

	public void setTotalMonthlySaving(double totalMonthlySaving) {
		this.totalMonthlySaving = totalMonthlySaving;
	}

	public double getTotalLumpsumSaving() {
		return totalLumpsumSaving;
	}

	public void setTotalLumpsumSaving(double totalLumpsumSaving) {
		this.totalLumpsumSaving = totalLumpsumSaving;
	}

	public double getTotalMonthlyAdjustment() {
		return totalMonthlyAdjustment;
	}

	public void setTotalMonthlyAdjustment(double totalMonthlyAdjustment) {
		this.totalMonthlyAdjustment = totalMonthlyAdjustment;
	}

	public double getTotalLumpsumAdjusment() {
		return totalLumpsumAdjusment;
	}

	public void setTotalLumpsumAdjusment(double totalLumpsumAdjusment) {
		this.totalLumpsumAdjusment = totalLumpsumAdjusment;
	}

	public double getTotalFutureValue() {
		return totalFutureValue;
	}

	public void setTotalFutureValue(double totalFutureValue) {
		this.totalFutureValue = totalFutureValue;
	}

	public double getTotalAssetValue() {
		return totalAssetValue;
	}

	public void setTotalAssetValue(double totalAssetValue) {
		this.totalAssetValue = totalAssetValue;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public String getGeneralSipMsg() {
		return generalSipMsg;
	}

	public void setGeneralSipMsg(String generalSipMsg) {
		this.generalSipMsg = generalSipMsg;
	}

	public List<GoalOrderItem> getGoals() {
		return goals;
	}

	public void setGoals(List<GoalOrderItem> goals) {
		this.goals = goals;
	}

	public List<AssetClassDTO> getAssetList() {
		return assetList;
	}

	public void setAssetList(List<AssetClassDTO> assetList) {
		this.assetList = assetList;
	}

	public List<SuggestSchemeDTO> getSuggestSchemeDTOList() {
		return suggestSchemeDTOList;
	}

	public void setSuggestSchemeDTOList(List<SuggestSchemeDTO> suggestSchemeDTOList) {
		this.suggestSchemeDTOList = suggestSchemeDTOList;
	}

	public BigDecimal getNetSip() {
		return netSip;
	}

	public void setNetSip(BigDecimal netSip) {
		this.netSip = netSip;
	}

	public BigDecimal getNetLumpsum() {
		return netLumpsum;
	}

	public void setNetLumpsum(BigDecimal netLumpsum) {
		this.netLumpsum = netLumpsum;
	}

	public BigDecimal getNetRequiredFutureValue() {
		return netRequiredFutureValue;
	}

	public void setNetRequiredFutureValue(BigDecimal netRequiredFutureValue) {
		this.netRequiredFutureValue = netRequiredFutureValue;
	}

	public BigDecimal getAssetFutureValueTotal() {
		return assetFutureValueTotal;
	}

	public void setAssetFutureValueTotal(BigDecimal assetFutureValueTotal) {
		this.assetFutureValueTotal = assetFutureValueTotal;
	}

	public Integer getGoalsOrderItemId() {
		return goalsOrderItemId;
	}

	public void setGoalsOrderItemId(Integer goalsOrderItemId) {
		this.goalsOrderItemId = goalsOrderItemId;
	}

	public double getFutureValue() {
		return futureValue;
	}

	public void setFutureValue(double futureValue) {
		this.futureValue = futureValue;
	}

	public double getMonthlySaving() {
		return monthlySaving;
	}

	public void setMonthlySaving(double monthlySaving) {
		this.monthlySaving = monthlySaving;
	}

	public double getLumpsumSaving() {
		return lumpsumSaving;
	}

	public void setLumpsumSaving(double lumpsumSaving) {
		this.lumpsumSaving = lumpsumSaving;
	}

	public String isRiskCalculate() {
		return isRiskCalculate;
	}

	public void setRiskCalculate(String isRiskCalculate) {
		this.isRiskCalculate = isRiskCalculate;
	}

	public String getDuration() {
		return duration;
	}

	public void setDuration(String duration) {
		this.duration = duration;
	}

	public String getAssociateGoalItemDuration() {
		return associateGoalItemDuration;
	}

	public void setAssociateGoalItemDuration(String associateGoalItemDuration) {
		this.associateGoalItemDuration = associateGoalItemDuration;
	}
}
