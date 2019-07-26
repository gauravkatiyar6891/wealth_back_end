package com.moptra.go4wealth.sip.model;

import java.util.Date;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;

public class CalculateSipRequestDto {
	
	private Date startDate;
	
	@NotNull
	private Integer currResidenceLocationId;
	
	@NotNull
	@Min(20)
	private Integer currentAge;
	
	private Integer incomeSlabId;
	
	@NotNull
	private Integer maritalStatusId;
	
	private Integer kids;
	
	@NotNull
	private Integer incomeVal;
	
	private String incomeSlabCode;
	
	private int userId;
	
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
	public Integer getIncomeVal() {
		return incomeVal;
	}
	public void setIncomeVal(Integer incomeVal) {
		this.incomeVal = incomeVal;
	}
	
	@Override
	public String toString() {
		return "CalculateSipRequestDto [startDate=" + startDate + ", currResidenceLocationId=" + currResidenceLocationId
				+ ", currentAge=" + currentAge + ", incomeSlabId=" + incomeSlabId + ", maritalStatusId="
				+ maritalStatusId + ", kids=" + kids + ", incomeVal=" + incomeVal + ", incomeSlabCode=" + incomeSlabCode
				+ "]";
	}
	public String getIncomeSlabCode() {
		return incomeSlabCode;
	}
	public void setIncomeSlabCode(String incomeSlabCode) {
		this.incomeSlabCode = incomeSlabCode;
	}
	public int getUserId() {
		return userId;
	}
	public void setUserId(int userId) {
		this.userId = userId;
	}

}
