package com.moptra.go4wealth.prs.model;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

public class FundSchemeDTO {
	
	private Integer schemeId;
	private String schemeCode;
	private Integer uniqueNo;
	private String fundHouse;
	private String schemeCategory;
	private String schemeSubCategory;
	private int amfiCode;
	private String rtaCode;
	private String isinCode;
	private String benchmarkCode;
	private String amcSchemeCode;
	private String schemeName;
	private String fundName;
	private String schemeType;
	private String option;
	private String plan;
	private String rating;
	private String schemeLaunchDate;
	private String schemeEndDate;
	private Date nfoDate;
	private String faceValue;
	private String status;
	private String registrarTransferAgent;
	private String risk;
	private String year;
	private String return_;
	private String minInvestment;
	private String sipAllowed;
	private String minSipAmount;
	private Integer minimumPurchaseAmount;
	private String maximumPurchaseAmount;
	private String currentNav;
	private String currentDate;
	private List<SchemeRecentViewDTO> schemeRecentViewDTOList;
	private List<SimilarSchemeDTO> similarSchemeDTOList;
	private String schemeKeyword;
	private String purchaseAllowed;
	private String redemptionAllowed;
	private String priority;
	private String showScheme;
	private String amcCode;
	private String sipAllowedDate;
	private String sipMaxInstallmentAmount;
	private String sipMinInstallmentNumber;
	
	private String amfiCodes;
	private String channelPartnerCode;
	private String amcName;
	private String isin;
	
	private Integer schemeYears;
	
	private String description;
	private String features;
	private String investmentGoals;
	private String portfolioCategoryName;
	private String portfolioCategoryKeyword;
	private String portfolioCategoryMinSipAmount;
	private String[] sipDateList;
	private String sipDate;
	private String finalMinSipAmount;
	private String finalMinLumpsumAmount;
	private String portfolioCategoryMinLumpsumAmount;
	private String oneYearReturn;
	private String threeYearReturn;
	private String fiveYearReturn;
	private String information;
	private int totalPurchageAmount;
	private String isOrderAllowed;
	private String isEnachEnable;
	private String isBillerEnable;
	private String fundManager;
	private String investmentObjective;
	private String isIsipAllowed;
	private String sipMaximumGap;
	private BigDecimal minAdditionalAmount;
	
	public BigDecimal getMinAdditionalAmount() {
		return minAdditionalAmount;
	}
	public void setMinAdditionalAmount(BigDecimal minAdditionalAmount) {
		this.minAdditionalAmount = minAdditionalAmount;
	}
	
	private int sequence;
	
	public void setSequence(int sequence) {
		this.sequence = sequence;
	}
	
	public int getSequence() {
		return sequence;
	}
	
	public String getIsIsipAllowed() {
		return isIsipAllowed;
	}
	
	public void setIsIsipAllowed(String isIsipAllowed) {
		this.isIsipAllowed = isIsipAllowed;
	}
	
	public String getIsBillerEnable() {
		return isBillerEnable;
	}
	
	public void setIsBillerEnable(String isBillerEnable) {
		this.isBillerEnable = isBillerEnable;
	}
	
	public String getIsEnachEnable() {
		return isEnachEnable;
	}
	public void setIsEnachEnable(String isEnachEnable) {
		this.isEnachEnable = isEnachEnable;
	}
	
	
	public String getIsOrderAllowed() {
		return isOrderAllowed;
	}
	public void setIsOrderAllowed(String isOrderAllowed) {
		this.isOrderAllowed = isOrderAllowed;
	}
	
	public int getTotalPurchageAmount() {
		return totalPurchageAmount;
	}
	public void setTotalPurchageAmount(int totalPurchageAmount) {
		this.totalPurchageAmount = totalPurchageAmount;
	}
	
	
	
	public String getPortfolioCategoryMinLumpsumAmount() {
		return portfolioCategoryMinLumpsumAmount;
	}
	public void setPortfolioCategoryMinLumpsumAmount(String portfolioCategoryMinLumpsumAmount) {
		this.portfolioCategoryMinLumpsumAmount = portfolioCategoryMinLumpsumAmount;
	}
	
	public void setIsin(String isin) {
		this.isin = isin;
	}
	public String getIsin() {
		return isin;
	}
	
	public void setSipMaxInstallmentAmount(String sipMaxInstallmentAmount) {
		this.sipMaxInstallmentAmount = sipMaxInstallmentAmount;
	}
	
	public String getSipMaxInstallmentAmount() {
		return sipMaxInstallmentAmount;
	}
	
	public void setSipMinInstallmentNumber(String sipMinInstallmentNumber) {
		this.sipMinInstallmentNumber = sipMinInstallmentNumber;
	}
	
	public String getSipMinInstallmentNumber() {
		return sipMinInstallmentNumber;
	}
	
	public void setSipAllowedDate(String sipAllowedDate) {
		this.sipAllowedDate = sipAllowedDate;
	}
	public String getSipAllowedDate() {
		return sipAllowedDate;
	}
	
	public String getCurrentDate() {
		return currentDate;
	}
	
	public void setCurrentDate(String currentDate) {
		this.currentDate = currentDate;
	}
	public void setCurrentNav(String currentNav) {
		this.currentNav = currentNav;
	}
	
	public String getCurrentNav() {
		return currentNav;
	}
	public Integer getSchemeId() {
		return schemeId;
	}
	public void setSchemeId(Integer schemeId) {
		this.schemeId = schemeId;
	}
	public String getFundHouse() {
		return fundHouse;
	}
	public void setFundHouse(String fundHouse) {
		this.fundHouse = fundHouse;
	}
	public String getSchemeCategory() {
		return schemeCategory;
	}
	public void setSchemeCategory(String schemeCategory) {
		this.schemeCategory = schemeCategory;
	}
	public String getSchemeSubCategory() {
		return schemeSubCategory;
	}
	public void setSchemeSubCategory(String schemeSubCategory) {
		this.schemeSubCategory = schemeSubCategory;
	}
	public int getAmfiCode() {
		return amfiCode;
	}
	public void setAmfiCode(int amfiCode) {
		this.amfiCode = amfiCode;
	}
	public String getRtaCode() {
		return rtaCode;
	}
	public void setRtaCode(String rtaCode) {
		this.rtaCode = rtaCode;
	}
	public String getIsinCode() {
		return isinCode;
	}
	public void setIsinCode(String isinCode) {
		this.isinCode = isinCode;
	}
	public String getBenchmarkCode() {
		return benchmarkCode;
	}
	public void setBenchmarkCode(String benchmarkCode) {
		this.benchmarkCode = benchmarkCode;
	}
	public String getAmcSchemeCode() {
		return amcSchemeCode;
	}
	public void setAmcSchemeCode(String amcSchemeCode) {
		this.amcSchemeCode = amcSchemeCode;
	}
	public String getSchemeName() {
		return schemeName;
	}
	public void setSchemeName(String schemeName) {
		this.schemeName = schemeName;
	}
	public String getFundName() {
		return fundName;
	}
	public void setFundName(String fundName) {
		this.fundName = fundName;
	}
	public String getSchemeType() {
		return schemeType;
	}
	public void setSchemeType(String schemeType) {
		this.schemeType = schemeType;
	}
	public String getOption() {
		return option;
	}
	public void setOption(String option) {
		this.option = option;
	}
	public String getPlan() {
		return plan;
	}
	public void setPlan(String plan) {
		this.plan = plan;
	}
	public String getRating() {
		return rating;
	}
	public void setRating(String rating) {
		this.rating = rating;
	}
	public String getSchemeLaunchDate() {
		return schemeLaunchDate;
	}
	public void setSchemeLaunchDate(String schemeLaunchDate) {
		this.schemeLaunchDate = schemeLaunchDate;
	}
	public Date getNfoDate() {
		return nfoDate;
	}
	public void setNfoDate(Date nfoDate) {
		this.nfoDate = nfoDate;
	}
	public String getFaceValue() {
		return faceValue;
	}
	public void setFaceValue(String faceValue) {
		this.faceValue = faceValue;
	}
	public String getStatus() {
		return status;
	}
	public void setStatus(String status) {
		this.status = status;
	}
	public String getRegistrarTransferAgent() {
		return registrarTransferAgent;
	}
	public void setRegistrarTransferAgent(String registrarTransferAgent) {
		this.registrarTransferAgent = registrarTransferAgent;
	}
	public String getRisk() {
		return risk;
	}
	public void setRisk(String risk) {
		this.risk = risk;
	}
	public String getYear() {
		return year;
	}
	public void setYear(String year) {
		this.year = year;
	}
	public String getReturn_() {
		return return_;
	}
	public void setReturn_(String return_) {
		this.return_ = return_;
	}
	public String getMinInvestment() {
		return minInvestment;
	}
	public void setMinInvestment(String minInvestment) {
		this.minInvestment = minInvestment;
	}
	public String getSipAllowed() {
		return sipAllowed;
	}
	public void setSipAllowed(String sipAllowed) {
		this.sipAllowed = sipAllowed;
	}
	public String getMinSipAmount() {
		return minSipAmount;
	}
	public void setMinSipAmount(String minSipAmount) {
		this.minSipAmount = minSipAmount;
	}
	public String getSchemeEndDate() {
		return schemeEndDate;
	}
	public void setSchemeEndDate(String schemeEndDate) {
		this.schemeEndDate = schemeEndDate;
	}
	public Integer getMinimumPurchaseAmount() {
		return minimumPurchaseAmount;
	}
	public void setMinimumPurchaseAmount(Integer minimumPurchaseAmount) {
		this.minimumPurchaseAmount = minimumPurchaseAmount;
	}
	public String getMaximumPurchaseAmount() {
		return maximumPurchaseAmount;
	}
	public void setMaximumPurchaseAmount(String maximumPurchaseAmount) {
		this.maximumPurchaseAmount = maximumPurchaseAmount;
	}
	public String getSchemeCode() {
		return schemeCode;
	}
	public void setSchemeCode(String schemeCode) {
		this.schemeCode = schemeCode;
	}
	public Integer getUniqueNo() {
		return uniqueNo;
	}
	public void setUniqueNo(Integer uniqueNo) {
		this.uniqueNo = uniqueNo;
	}
	public List<SchemeRecentViewDTO> getSchemeRecentViewDTOList() {
		return schemeRecentViewDTOList;
	}
	public void setSchemeRecentViewDTOList(List<SchemeRecentViewDTO> schemeRecentViewDTOList) {
		this.schemeRecentViewDTOList = schemeRecentViewDTOList;
	}
	public List<SimilarSchemeDTO> getSimilarSchemeDTOList() {
		return similarSchemeDTOList;
	}
	public void setSimilarSchemeDTOList(List<SimilarSchemeDTO> similarSchemeDTOList) {
		this.similarSchemeDTOList = similarSchemeDTOList;
	}
	public String getSchemeKeyword() {
		return schemeKeyword;
	}
	public void setSchemeKeyword(String schemeKeyword) {
		this.schemeKeyword = schemeKeyword;
	}

	public String getPurchaseAllowed() {
		return purchaseAllowed;
	}

	public void setPurchaseAllowed(String purchaseAllowed) {
		this.purchaseAllowed = purchaseAllowed;
	}

	public String getRedemptionAllowed() {
		return redemptionAllowed;
	}

	public void setRedemptionAllowed(String redemptionAllowed) {
		this.redemptionAllowed = redemptionAllowed;
	}

	public String getPriority() {
		return priority;
	}

	public void setPriority(String priority) {
		this.priority = priority;
	}

	public String getShowScheme() {
		return showScheme;
	}

	public void setShowScheme(String showScheme) {
		this.showScheme = showScheme;
	}

	public String getAmcCode() {
		return amcCode;
	}

	public void setAmcCode(String amcCode) {
		this.amcCode = amcCode;
	}

	public String getAmfiCodes() {
		return amfiCodes;
	}

	public void setAmfiCodes(String amfiCodes) {
		this.amfiCodes = amfiCodes;
	}

	public String getChannelPartnerCode() {
		return channelPartnerCode;
	}

	public void setChannelPartnerCode(String channelPartnerCode) {
		this.channelPartnerCode = channelPartnerCode;
	}

	public String getAmcName() {
		return amcName;
	}

	public void setAmcName(String amcName) {
		this.amcName = amcName;
	}
	
	public Integer getSchemeYears() {
		return schemeYears;
	}
	
	public void setSchemeYears(Integer schemeYears) {
		this.schemeYears = schemeYears;
	}
	
	
	
	
	
	public String getPortfolioCategoryName() {
		return portfolioCategoryName;
	}
	public void setPortfolioCategoryName(String portfolioCategoryName) {
		this.portfolioCategoryName = portfolioCategoryName;
	}
	public String getPortfolioCategoryKeyword() {
		return portfolioCategoryKeyword;
	}
	public void setPortfolioCategoryKeyword(String portfolioCategoryKeyword) {
		this.portfolioCategoryKeyword = portfolioCategoryKeyword;
	}
	public String getPortfolioCategoryMinSipAmount() {
		return portfolioCategoryMinSipAmount;
	}
	public void setPortfolioCategoryMinSipAmount(String portfolioCategoryMinSipAmount) {
		this.portfolioCategoryMinSipAmount = portfolioCategoryMinSipAmount;
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
	public String getFinalMinSipAmount() {
		return finalMinSipAmount;
	}
	public void setFinalMinSipAmount(String finalMinSipAmount) {
		this.finalMinSipAmount = finalMinSipAmount;
	}
	public String getFinalMinLumpsumAmount() {
		return finalMinLumpsumAmount;
	}
	public void setFinalMinLumpsumAmount(String finalMinLumpsumAmount) {
		this.finalMinLumpsumAmount = finalMinLumpsumAmount;
	}
	public String getDescription() {
		return description;
	}
	public void setDescription(String description) {
		this.description = description;
	}
	public String getFeatures() {
		return features;
	}
	public void setFeatures(String features) {
		this.features = features;
	}
	public String getInvestmentGoals() {
		return investmentGoals;
	}
	public void setInvestmentGoals(String investmentGoals) {
		this.investmentGoals = investmentGoals;
	}
	public String getOneYearReturn() {
		return oneYearReturn;
	}
	public void setOneYearReturn(String oneYearReturn) {
		this.oneYearReturn = oneYearReturn;
	}
	public String getThreeYearReturn() {
		return threeYearReturn;
	}
	public void setThreeYearReturn(String threeYearReturn) {
		this.threeYearReturn = threeYearReturn;
	}
	public String getFiveYearReturn() {
		return fiveYearReturn;
	}
	public void setFiveYearReturn(String fiveYearReturn) {
		this.fiveYearReturn = fiveYearReturn;
	}

	public String getInformation() {
		return information;
	}
	public void setInformation(String information) {
		this.information = information;
	}

	public String getFundManager() {
		return fundManager;
	}

	public void setFundManager(String fundManager) {
		this.fundManager = fundManager;
	}

	public String getInvestmentObjective() {
		return investmentObjective;
	}

	public void setInvestmentObjective(String investmentObjective) {
		this.investmentObjective = investmentObjective;
	}

	public String getSipMaximumGap() {
		return sipMaximumGap;
	}

	public void setSipMaximumGap(String sipMaximumGap) {
		this.sipMaximumGap = sipMaximumGap;
	}
	
}