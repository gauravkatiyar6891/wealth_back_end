package com.moptra.go4wealth.ticob.model;

import java.math.BigDecimal;

public class TransferInRequestDTO {

	private String folioNo;
	private String scheme;
	private String schemeCode;
	private String transactionNumber;
	private String investerName;
	private String tradDate;
	private String postDate;
	private BigDecimal purPrice;
	private BigDecimal unit;
	private BigDecimal amount;
	private String reportDate;
	private String pan;
	private String transactionType;
	private String prodCode;
	private String rtaSchemeCode;
	private String isin;
	private String investmentType;
	private String startedOn;
	private Integer uniqueId;
	private String rtaAgent;
	
	public String getRtaSchemeCode() {
		return rtaSchemeCode;
	}
	public void setRtaSchemeCode(String rtaSchemeCode) {
		this.rtaSchemeCode = rtaSchemeCode;
	}
	public String getIsin() {
		return isin;
	}
	public void setIsin(String isin) {
		this.isin = isin;
	}
	public String getFolioNo() {
		return folioNo;
	}
	public void setFolioNo(String folioNo) {
		this.folioNo = folioNo;
	}
	public String getScheme() {
		return scheme;
	}
	public void setScheme(String scheme) {
		this.scheme = scheme;
	}
	public String getTransactionNumber() {
		return transactionNumber;
	}
	public void setTransactionNumber(String transactionNumber) {
		this.transactionNumber = transactionNumber;
	}
	public String getInvesterName() {
		return investerName;
	}
	public void setInvesterName(String investerName) {
		this.investerName = investerName;
	}
	public String getTradDate() {
		return tradDate;
	}
	public void setTradDate(String tradDate) {
		this.tradDate = tradDate;
	}
	public String getPostDate() {
		return postDate;
	}
	public void setPostDate(String postDate) {
		this.postDate = postDate;
	}
	public BigDecimal getPurPrice() {
		return purPrice;
	}
	public void setPurPrice(BigDecimal purPrice) {
		this.purPrice = purPrice;
	}
	public BigDecimal getUnit() {
		return unit;
	}
	public void setUnit(BigDecimal unit) {
		this.unit = unit;
	}
	public BigDecimal getAmount() {
		return amount;
	}
	public void setAmount(BigDecimal amount) {
		this.amount = amount;
	}
	public String getReportDate() {
		return reportDate;
	}
	public void setReportDate(String reportDate) {
		this.reportDate = reportDate;
	}
	public String getPan() {
		return pan;
	}
	public void setPan(String pan) {
		this.pan = pan;
	}
	public String getProdCode() {
		return prodCode;
	}
	public void setProdCode(String prodCode) {
		this.prodCode = prodCode;
	}
	public String getInvestmentType() {
		return investmentType;
	}
	public void setInvestmentType(String investmentType) {
		this.investmentType = investmentType;
	}
	public String getSchemeCode() {
		return schemeCode;
	}
	public void setSchemeCode(String schemeCode) {
		this.schemeCode = schemeCode;
	}
	public String getStartedOn() {
		return startedOn;
	}
	public void setStartedOn(String startedOn) {
		this.startedOn = startedOn;
	}
	public Integer getUniqueId() {
		return uniqueId;
	}
	public void setUniqueId(Integer uniqueId) {
		this.uniqueId = uniqueId;
	}
	public String getTransactionType() {
		return transactionType;
	}
	public void setTransactionType(String transactionType) {
		this.transactionType = transactionType;
	}
	public String getRtaAgent() {
		return rtaAgent;
	}
	public void setRtaAgent(String rtaAgent) {
		this.rtaAgent = rtaAgent;
	}
}