package com.moptra.go4wealth.sip.model;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

public class CalculateAdvanceSipResponseDTO {

	private List<GoalDTO> goalList = new ArrayList<GoalDTO>(0);
	private List<String> suggestedFundTypeList;
	private BigDecimal generalInflation;
	private BigDecimal avgNominalReturn;
	private BigDecimal avgInflationAdjustedReturn;
	
	private BigDecimal lumpSumpsPmtFV;
	private BigDecimal lumpSumpsFutureValue;
	private String lumpSumpsMsg;
	
	private BigDecimal investCorpusPmt;
	private BigDecimal investCorpusInvestment;
	private String investCorpusMsg;
	
	private String generalSipMsg;
	
	private BigDecimal monthlySipAmountFutureValue;
	private BigDecimal monthlySipAmountTotalInvestment;
	private String monthlySipAmountMsg;
	
	private double avgDurationSelectedGoal;
	private BigDecimal monthlySipAmountSelectedGoal;
	private BigDecimal totalInvestmentSelectedGoal;

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
	public List<String> getSuggestedFundTypeList() {
		return suggestedFundTypeList;
	}
	public void setSuggestedFundTypeList(List<String> suggestedFundTypeList) {
		this.suggestedFundTypeList = suggestedFundTypeList;
	}
	public BigDecimal getLumpSumpsPmtFV() {
		return lumpSumpsPmtFV;
	}
	public void setLumpSumpsPmtFV(BigDecimal lumpSumpsPmtFV) {
		this.lumpSumpsPmtFV = lumpSumpsPmtFV;
	}
	public BigDecimal getLumpSumpsFutureValue() {
		return lumpSumpsFutureValue;
	}
	public void setLumpSumpsFutureValue(BigDecimal lumpSumpsFutureValue) {
		this.lumpSumpsFutureValue = lumpSumpsFutureValue;
	}
	public BigDecimal getInvestCorpusPmt() {
		return investCorpusPmt;
	}
	public void setInvestCorpusPmt(BigDecimal investCorpusPmt) {
		this.investCorpusPmt = investCorpusPmt;
	}
	public String getInvestCorpusMsg() {
		return investCorpusMsg;
	}
	public void setInvestCorpusMsg(String investCorpusMsg) {
		this.investCorpusMsg = investCorpusMsg;
	}
	public String getLumpSumpsMsg() {
		return lumpSumpsMsg;
	}
	public void setLumpSumpsMsg(String lumpSumpsMsg) {
		this.lumpSumpsMsg = lumpSumpsMsg;
	}
	public String getGeneralSipMsg() {
		return generalSipMsg;
	}
	public void setGeneralSipMsg(String generalSipMsg) {
		this.generalSipMsg = generalSipMsg;
	}
	public BigDecimal getInvestCorpusInvestment() {
		return investCorpusInvestment;
	}
	public void setInvestCorpusInvestment(BigDecimal investCorpusInvestment) {
		this.investCorpusInvestment = investCorpusInvestment;
	}
	public BigDecimal getMonthlySipAmountFutureValue() {
		return monthlySipAmountFutureValue;
	}
	public void setMonthlySipAmountFutureValue(BigDecimal monthlySipAmountFutureValue) {
		this.monthlySipAmountFutureValue = monthlySipAmountFutureValue;
	}
	public BigDecimal getMonthlySipAmountTotalInvestment() {
		return monthlySipAmountTotalInvestment;
	}
	public void setMonthlySipAmountTotalInvestment(BigDecimal monthlySipAmountTotalInvestment) {
		this.monthlySipAmountTotalInvestment = monthlySipAmountTotalInvestment;
	}
	public String getMonthlySipAmountMsg() {
		return monthlySipAmountMsg;
	}
	public void setMonthlySipAmountMsg(String monthlySipAmountMsg) {
		this.monthlySipAmountMsg = monthlySipAmountMsg;
	}
	public double getAvgDurationSelectedGoal() {
		return avgDurationSelectedGoal;
	}
	public void setAvgDurationSelectedGoal(double avgDurationSelectedGoal) {
		this.avgDurationSelectedGoal = avgDurationSelectedGoal;
	}
	public BigDecimal getMonthlySipAmountSelectedGoal() {
		return monthlySipAmountSelectedGoal;
	}
	public void setMonthlySipAmountSelectedGoal(BigDecimal monthlySipAmountSelectedGoal) {
		this.monthlySipAmountSelectedGoal = monthlySipAmountSelectedGoal;
	}
	public BigDecimal getTotalInvestmentSelectedGoal() {
		return totalInvestmentSelectedGoal;
	}
	public void setTotalInvestmentSelectedGoal(BigDecimal totalInvestmentSelectedGoal) {
		this.totalInvestmentSelectedGoal = totalInvestmentSelectedGoal;
	}
	
}
