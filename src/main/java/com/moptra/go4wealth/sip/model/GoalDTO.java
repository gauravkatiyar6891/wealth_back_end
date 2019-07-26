package com.moptra.go4wealth.sip.model;

import java.math.BigDecimal;
import java.util.List;

import com.moptra.go4wealth.bean.Goals;

public class GoalDTO implements Comparable{

	private Integer goalId;
	private GoalBucketDto goalBucket;
	private String goalName;
	private Integer showToProfileType;
	private BigDecimal costOfGoal;
	private BigDecimal futureValue;
	private Double duration;
	private BigDecimal pmtFutureValue;
	private Double inflation;
	private Double rate;
	private boolean inflationFlag;
	private String goalIcon;
	private BigDecimal currentCost;
	private BigDecimal sipValue;
	private BigDecimal lumPsumValue;
	private Integer goalsOrderId;
	private Integer userId;
	private List<AssetClassDTO> assetClassDto;
	private boolean investmentFlag;

	public GoalDTO() {
		super();
	}

	public GoalDTO(Goals goals) {
		this.goalId = goals.getGoalId();
		this.goalBucket = new GoalBucketDto(goals.getGoalBucket().getGoalBucketId(),
				goals.getGoalBucket().getGoalBucketName());
		this.goalName = goals.getGoalName();
		this.showToProfileType = goals.getShowToProfileType();
		this.costOfGoal = goals.getCostOfGoal();
		this.goalIcon = goals.getGoalIcon();
	}

	public Integer getGoalId() {
		return goalId;
	}

	public void setGoalId(Integer goalId) {
		this.goalId = goalId;
	}

	public GoalBucketDto getGoalBucket() {
		return goalBucket;
	}

	public void setGoalBucket(GoalBucketDto goalBucket) {
		this.goalBucket = goalBucket;
	}

	public String getGoalName() {
		return goalName;
	}

	public void setGoalName(String goalName) {
		this.goalName = goalName;
	}

	public Integer getShowToProfileType() {
		return showToProfileType;
	}

	public void setShowToProfileType(Integer showToProfileType) {
		this.showToProfileType = showToProfileType;
	}

	public BigDecimal getCostOfGoal() {
		return costOfGoal;
	}

	public void setCostOfGoal(BigDecimal costOfGoal) {
		this.costOfGoal = costOfGoal;
	}

	public BigDecimal getFutureValue() {
		return futureValue;
	}

	public void setFutureValue(BigDecimal futureValue) {
		this.futureValue = futureValue;
	}

	public Double getDuration() {
		return duration;
	}

	public void setDuration(Double duration) {
		this.duration = duration;
	}

	public BigDecimal getPmtFutureValue() {
		return pmtFutureValue;
	}

	public void setPmtFutureValue(BigDecimal pmtFutureValue) {
		this.pmtFutureValue = pmtFutureValue;
	}

	public Double getInflation() {
		return inflation;
	}

	public void setInflation(Double inflation) {
		this.inflation = inflation;
	}

	public Double getRate() {
		return rate;
	}

	public void setRate(Double rate) {
		this.rate = rate;
	}

	public boolean isInflationFlag() {
		return inflationFlag;
	}

	public void setInflationFlag(boolean inflationFlag) {
		this.inflationFlag = inflationFlag;
	}

	public String getGoalIcon() {
		return goalIcon;
	}

	public void setGoalIcon(String goalIcon) {
		this.goalIcon = goalIcon;
	}

	public BigDecimal getCurrentCost() {
		return currentCost;
	}

	public void setCurrentCost(BigDecimal currentCost) {
		this.currentCost = currentCost;
	}

	public BigDecimal getSipValue() {
		return sipValue;
	}

	public void setSipValue(BigDecimal sipValue) {
		this.sipValue = sipValue;
	}

	public BigDecimal getLumPsumValue() {
		return lumPsumValue;
	}

	public void setLumPsumValue(BigDecimal lumPsumValue) {
		this.lumPsumValue = lumPsumValue;
	}

	public Integer getGoalsOrderId() {
		return goalsOrderId;
	}

	public void setGoalsOrderId(Integer goalsOrderId) {
		this.goalsOrderId = goalsOrderId;
	}

	public Integer getUserId() {
		return userId;
	}

	public void setUserId(Integer userId) {
		this.userId = userId;
	}
	
	public boolean isInvestmentFlag() {
		return investmentFlag;
	}

	public void setInvestmentFlag(boolean investmentFlag) {
		this.investmentFlag = investmentFlag;
	}

	public List<AssetClassDTO> getAssetClassDto() {
		return assetClassDto;
	}
	
	public void setAssetClassDto(List<AssetClassDTO> assetClassDto) {
		this.assetClassDto = assetClassDto;
	}

	@Override
	public int compareTo(Object o) {
		int compareProfile=((GoalDTO)o).getShowToProfileType();
	    /* For Ascending order*/
	    return this.showToProfileType-compareProfile;
	    /* For Descending order do like this */
	   //return compareProfile-this.showToProfileType;
	}

}
