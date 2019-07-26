package com.moptra.go4wealth.sip.model;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonIgnore;

public class CalculateSipResponseDto {

	private List<GoalDTO> goalList = new ArrayList<GoalDTO>(0);
	private BigDecimal generalInflation;
	private BigDecimal avgNominalReturn;
	private BigDecimal avgInflationAdjustedReturn;
	private BigDecimal currentCostTotal;
	private BigDecimal futureValueTotal;
	private BigDecimal sipValueTotal;
	private BigDecimal lumpsumValueTotal;
	private String generalSipMsg;

	@JsonIgnore
	private String cityName;
	@JsonIgnore
	private String maritalStatusName;
	private Integer goalsOrderId;
	private String userType;

	public List<GoalDTO> getGoalList() {
		return goalList;
	}

	public void setGoalList(List<GoalDTO> goalList) {
		this.goalList = goalList;
	}

	public BigDecimal getGeneralInflation() {
		return generalInflation;
	}

	public void setGeneralInflation(BigDecimal generalInflation) {
		this.generalInflation = generalInflation;
	}

	public BigDecimal getAvgNominalReturn() {
		return avgNominalReturn;
	}

	public void setAvgNominalReturn(BigDecimal avgNominalReturn) {
		this.avgNominalReturn = avgNominalReturn;
	}

	public BigDecimal getAvgInflationAdjustedReturn() {
		return avgInflationAdjustedReturn;
	}

	public void setAvgInflationAdjustedReturn(BigDecimal avgInflationAdjustedReturn) {
		this.avgInflationAdjustedReturn = avgInflationAdjustedReturn;
	}

	public String getCityName() {
		return cityName;
	}

	public void setCityName(String cityName) {
		this.cityName = cityName;
	}

	public String getMaritalStatusName() {
		return maritalStatusName;
	}

	public void setMaritalStatusName(String maritalStatusName) {
		this.maritalStatusName = maritalStatusName;
	}

	public Integer getGoalsOrderId() {
		return goalsOrderId;
	}

	public void setGoalsOrderId(Integer goalsOrderId) {
		this.goalsOrderId = goalsOrderId;
	}

	public BigDecimal getCurrentCostTotal() {
		return currentCostTotal;
	}

	public void setCurrentCostTotal(BigDecimal currentCostTotal) {
		this.currentCostTotal = currentCostTotal;
	}

	public BigDecimal getFutureValueTotal() {
		return futureValueTotal;
	}

	public void setFutureValueTotal(BigDecimal futureValueTotal) {
		this.futureValueTotal = futureValueTotal;
	}

	public BigDecimal getSipValueTotal() {
		return sipValueTotal;
	}

	public void setSipValueTotal(BigDecimal sipValueTotal) {
		this.sipValueTotal = sipValueTotal;
	}

	public BigDecimal getLumpsumValueTotal() {
		return lumpsumValueTotal;
	}

	public void setLumpsumValueTotal(BigDecimal lumpsumValueTotal) {
		this.lumpsumValueTotal = lumpsumValueTotal;
	}

	public String getGeneralSipMsg() {
		return generalSipMsg;
	}

	public void setGeneralSipMsg(String generalSipMsg) {
		this.generalSipMsg = generalSipMsg;
	}

	public String getUserType() {
		return userType;
	}

	public void setUserType(String userType) {
		this.userType = userType;
	}	
}