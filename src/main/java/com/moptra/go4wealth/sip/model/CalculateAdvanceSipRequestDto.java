package com.moptra.go4wealth.sip.model;

import java.util.Date;
import java.util.List;

public class CalculateAdvanceSipRequestDto {
	
	private Date startDate;
	private Integer currResidenceLocationId;
	private Integer currentAge;
	private Integer incomeSlabId;
	private Integer maritalStatusId;
	private Integer kids;
	
	private Integer ageToRetire;
	private Integer riskTypeId;
	private Integer assetClassId;
	private Integer returnsTypeId;
	
	private Double duration;
	
	private List<Integer> selectedGoalIdList;
	
	private Double investCorpus;
	
	private List<GoalDTO> goalList;
	
	private Double lumpSumAmount;
	
	private Double inflation;
	
	private Double rate;
	
	private boolean inflationFlag;
	
	private Double monthlySipAmount;
	
	
	public Date getStartDate() {
		return startDate;
	}
	public void setStartDate(Date startDate) {
		this.startDate = startDate;
	}
	public Integer getCurrResidenceLocationId() {
		return currResidenceLocationId;
	}
	public void setCurrResidenceLocationId(Integer currResidenceLocationId) {
		this.currResidenceLocationId = currResidenceLocationId;
	}
	public Integer getCurrentAge() {
		return currentAge;
	}
	public void setCurrentAge(Integer currentAge) {
		this.currentAge = currentAge;
	}
	public Integer getIncomeSlabId() {
		return incomeSlabId;
	}
	public void setIncomeSlabId(Integer incomeSlabId) {
		this.incomeSlabId = incomeSlabId;
	}
	public Integer getMaritalStatusId() {
		return maritalStatusId;
	}
	public void setMaritalStatusId(Integer maritalStatusId) {
		this.maritalStatusId = maritalStatusId;
	}
	public Integer getKids() {
		return kids;
	}
	public void setKids(Integer kids) {
		this.kids = kids;
	}
	public Integer getAgeToRetire() {
		return ageToRetire;
	}
	public void setAgeToRetire(Integer ageToRetire) {
		this.ageToRetire = ageToRetire;
	}
	public Integer getRiskTypeId() {
		return riskTypeId;
	}
	public void setRiskTypeId(Integer riskTypeId) {
		this.riskTypeId = riskTypeId;
	}
	public Integer getAssetClassId() {
		return assetClassId;
	}
	public void setAssetClassId(Integer assetClassId) {
		this.assetClassId = assetClassId;
	}
	public Integer getReturnsTypeId() {
		return returnsTypeId;
	}
	public void setReturnsTypeId(Integer returnsTypeId) {
		this.returnsTypeId = returnsTypeId;
	}
	public Double getDuration() {
		return duration;
	}
	public void setDuration(Double duration) {
		this.duration = duration;
	}
	public List<Integer> getSelectedGoalIdList() {
		return selectedGoalIdList;
	}
	public void setSelectedGoalIdList(List<Integer> selectedGoalIdList) {
		this.selectedGoalIdList = selectedGoalIdList;
	}
	public Double getInvestCorpus() {
		return investCorpus;
	}
	public void setInvestCorpus(Double investCorpus) {
		this.investCorpus = investCorpus;
	}
	public List<GoalDTO> getGoalList() {
		return goalList;
	}
	public void setGoalList(List<GoalDTO> goalList) {
		this.goalList = goalList;
	}
	public Double getLumpSumAmount() {
		return lumpSumAmount;
	}
	public void setLumpSumAmount(Double lumpSumAmount) {
		this.lumpSumAmount = lumpSumAmount;
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
	public Double getMonthlySipAmount() {
		return monthlySipAmount;
	}
	public void setMonthlySipAmount(Double monthlySipAmount) {
		this.monthlySipAmount = monthlySipAmount;
	}	
}
