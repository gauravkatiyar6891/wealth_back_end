package com.moptra.go4wealth.prs.model;

import java.util.Date;
import java.util.List;

public class AddToCartDTO {
	
	private Integer schemeId;
	private String schemeCode;
	private Integer userId;
	private double amount;
	private String schemeName;
	private String dayOfSip;
	private String investmentType;
	private Integer orderId;
	private String status;
	private Date creationDate;
	private String type;
	private Integer minimumSipAmount;
	private String amcCode;
	private String goalName;
	private Integer goalId;
	private String orderDate;
	private String modelPortfolioCategory;
	private String orderCategory;
	private int mPBundleId;
	private String paymentAmount;
	private String finalResponse;
	private String[] sipDateList;
	private String sipDate;
	private String paymentOptions;
	private String bseOrderId;
	private String mandateId;
	private String lumpsumOrderId;
	private String sipRegnId;
	private String folioNo;
	private Integer transferInId;
	private String orderAmount;
	
	
	public String getOrderAmount() {
		return orderAmount;
	}
	public void setOrderAmount(String orderAmount) {
		this.orderAmount = orderAmount;
	}

	public Integer getTransferInId() {
		return transferInId;
	}
	
	public void setTransferInId(Integer transferInId) {
		this.transferInId = transferInId;
	}
	
	public String getFolioNo() {
		return folioNo;
	}
	public void setFolioNo(String folioNo) {
		this.folioNo = folioNo;
	}
	public String getLumpsumOrderId() {
		return lumpsumOrderId;
	}
	public void setLumpsumOrderId(String lumpsumOrderId) {
		this.lumpsumOrderId = lumpsumOrderId;
	}
	
	
	public String getBseOrderId() {
		return bseOrderId;
	}
	public void setBseOrderId(String bseOrderId) {
		this.bseOrderId = bseOrderId;
	}
	public String getMandateId() {
		return mandateId;
	}
	public void setMandateId(String mandateId) {
		this.mandateId = mandateId;
	}
	
	public String getPaymentOptions() {
		return paymentOptions;
	}
	public void setPaymentOptions(String paymentOptions) {
		this.paymentOptions = paymentOptions;
	}
	
	
	public String getFinalResponse() {
		return finalResponse;
	}
	public void setFinalResponse(String finalResponse) {
		this.finalResponse = finalResponse;
	}
	
	public String getPaymentAmount() {
		return paymentAmount;
	}
	public void setPaymentAmount(String paymentAmount) {
		this.paymentAmount = paymentAmount;
	}
	
	public int getmPBundleId() {
		return mPBundleId;
	}
	public void setmPBundleId(int mPBundleId) {
		this.mPBundleId = mPBundleId;
	}
	
	public String getOrderCategory() {
		return orderCategory;
	}
	public void setOrderCategory(String orderCategory) {
		this.orderCategory = orderCategory;
	}
	
	public String getModelPortfolioCategory() {
		return modelPortfolioCategory;
	}
	
	public void setModelPortfolioCategory(String modelPortfolioCategory) {
		this.modelPortfolioCategory = modelPortfolioCategory;
	}
	
	public void setOrderDate(String orderDate) {
		this.orderDate = orderDate;
	}
	public String getOrderDate() {
		return orderDate;
	}
	
	public String getGoalName() {
		return goalName;
	}
	public void setGoalName(String goalName) {
		this.goalName = goalName;
	}
	public Integer getGoalId() {
		return goalId;
	}
	public void setGoalId(Integer goalId) {
		this.goalId = goalId;
	}
	public Integer getSchemeId() {
		return schemeId;
	}
	public void setSchemeId(Integer schemeId) {
		this.schemeId = schemeId;
	}
	public String getSchemeCode() {
		return schemeCode;
	}
	public void setSchemeCode(String schemeCode) {
		this.schemeCode = schemeCode;
	}
	public Integer getUserId() {
		return userId;
	}
	public void setUserId(Integer userId) {
		this.userId = userId;
	}
	public double getAmount() {
		return amount;
	}
	public void setAmount(double amount) {
		this.amount = amount;
	}
	public String getSchemeName() {
		return schemeName;
	}
	public void setSchemeName(String schemeName) {
		this.schemeName = schemeName;
	}
	public String getDayOfSip() {
		return dayOfSip;
	}
	public void setDayOfSip(String dayOfSip) {
		this.dayOfSip = dayOfSip;
	}
	public String getInvestmentType() {
		return investmentType;
	}
	public void setInvestmentType(String investmentType) {
		this.investmentType = investmentType;
	}
	public Integer getOrderId() {
		return orderId;
	}
	public void setOrderId(Integer orderId) {
		this.orderId = orderId;
	}
	public String getStatus() {
		return status;
	}
	public void setStatus(String status) {
		this.status = status;
	}
	public Date getCreationDate() {
		return creationDate;
	}
	public void setCreationDate(Date creationDate) {
		this.creationDate = creationDate;
	}
	
	public String getType() {
		return type;
	}
	
	public void setType(String type) {
		this.type = type;
	}
	
	public Integer getMinimumSipAmount() {
		return minimumSipAmount;
	}
	
	public void setMinimumSipAmount(Integer minimumSipAmount) {
		this.minimumSipAmount = minimumSipAmount;
	}
	public String getAmcCode() {
		return amcCode;
	}
	public void setAmcCode(String amcCode) {
		this.amcCode = amcCode;
	}
	
	public String[] getSipDateList() {
		return sipDateList;
	}
	
	public void setSipDateList(String[] sipDateList) {
		this.sipDateList = sipDateList;
	}
	
	public String getSipDate() {
		return sipDate;
	}
	
	public void setSipDate(String sipDate) {
		this.sipDate = sipDate;
	}
	
	public String getSipRegnId() {
		return sipRegnId;
	}
	
	public void setSipRegnId(String sipRegnId) {
		this.sipRegnId = sipRegnId;
	}
}
