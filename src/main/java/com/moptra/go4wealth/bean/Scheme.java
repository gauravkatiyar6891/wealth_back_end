package com.moptra.go4wealth.bean;
// Generated 22 May, 2018 5:18:39 PM by Hibernate Tools 5.1.0.Alpha1

import static javax.persistence.GenerationType.IDENTITY;

import java.math.BigDecimal;
import java.util.Date;
import java.util.HashSet;
import java.util.Set;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

/**
 * Scheme generated by hbm2java
 */
@Entity
@Table(name = "scheme", catalog = "go4wealthdb")
public class Scheme implements java.io.Serializable {

	private Integer schemeId;
	private Integer uniqueNo;
	private String schemeCode;
	private String rtaSchemeCode;
	private String amcSchemeCode;
	private String isin;
	private String amcCode;
	private String schemeType;
	private String schemePlan;
	private String schemeName;
	private String purchaseAllowed;
	private String purchaseTransactionMode;
	private Integer minimumPurchaseAmount;
	private String additionalPurchaseAmount;
	private String maximumPurchaseAmount;
	private String purchaseAmountMultiplier;
	private String purchaseCutoffTime;
	private String redemptionAllowed;
	private String redemptionTransactionMode;
	private String minimumRedemptionQty;
	private String redemptionQtyMultiplier;
	private String maximumRedemptionQty;
	private String redemptionAmountMinimum;
	private String redemptionAmountMaximum;
	private String redemptionAmountMultiple;
	private String redemptionCutoffTime;
	private String rtaAgentCode;
	private String amcActiveFlag;
	private String dividendReinvestmentFlag;
	private String sipFlag;
	private String stpFlag;
	private String swpFlag;
	private String switchFlag;
	private String settlementType;
	private String amcInd;
	private String faceValue;
	private String startDate;
	private String endDate;
	private String exitLoadFlag;
	private String exitLoad;
	private String lockInPeriodFlag;
	private String lockInPeriod;
	private String channelPartnerCode;
	private int amfiCode;
	private String benchmarkCode;
	private String rating;
	private Date nfoDate;
	private String status;
	private String registrarTransferAgent;
	private String risk;
	private String year;
	private String returnValue;
	private String minSipAmount;
	private String fund_Nature;
	private String field2;
	private String keyword;
	private Set<OrderItem> orderItems = new HashSet<OrderItem>(0);
	private Set<SchemeRecentView> schemeRecentViews = new HashSet<SchemeRecentView>(0);
	private String display;
	private String priority;
	
	private String sipTransactionMode;
	private String sipFrequency;
	private String sipDates;
	private String sipMinimumGap;
	private String sipMaximumGap;
	private String sipStatus;
	private BigDecimal sipMinimumInstallmentAmount;
	private BigDecimal sipMaximumInstallmentAmount;
	private String sipMultiplierAmount;
	private String sipMinimumInstallmentNumber;
	private String sipMaximumInstallmentNumber;
	private String sipInstallmentGap;
	private String navDate;
	private String currentNav;
	private String fundManager;
	private String investmentObjective;
	private String schemeOptions;
	private String schemeCategory;
	private String schemeSubCategory;
	private String classification;

	public Scheme() {
	}

	public Scheme(int amfiCode, Date nfoDate,String navDate,String currentNav) {
		this.amfiCode = amfiCode;
		this.nfoDate = nfoDate;
		this.navDate = navDate;
		this.currentNav = currentNav;
	}

	public Scheme(Integer uniqueNo, String schemeCode, String rtaSchemeCode, String amcSchemeCode, String isin,
			String amcCode, String schemeType, String schemePlan, String schemeName, String purchaseAllowed,
			String purchaseTransactionMode, Integer minimumPurchaseAmount, String additionalPurchaseAmount,
			String maximumPurchaseAmount, String purchaseAmountMultiplier, String purchaseCutoffTime,
			String redemptionAllowed, String redemptionTransactionMode, String minimumRedemptionQty,
			String redemptionQtyMultiplier, String maximumRedemptionQty, String redemptionAmountMinimum,
			String redemptionAmountMaximum, String redemptionAmountMultiple, String redemptionCutoffTime,
			String rtaAgentCode, String amcActiveFlag, String dividendReinvestmentFlag, String sipFlag, String stpFlag,
			String swpFlag, String switchFlag, String settlementType, String amcInd, String faceValue, String startDate,
			String endDate, String exitLoadFlag, String exitLoad, String lockInPeriodFlag, String lockInPeriod,
			String channelPartnerCode, int amfiCode, String benchmarkCode, String rating, Date nfoDate, String status,
			String registrarTransferAgent, String risk, String year, String returnValue, String minSipAmount,
			String fund_Nature, String field2, Set<OrderItem> orderItems, Set<SchemeRecentView> schemeRecentViews,String keyword,
			String display,String priority,String sipTransactionMode, String sipFrequency, String sipDates, String sipMinimumGap, 
			String sipMaximumGap,String sipStatus, BigDecimal sipMinimumInstallmentAmount, BigDecimal sipMaximumInstallmentAmount,
			String sipMultiplierAmount, String sipMinimumInstallmentNumber, String sipMaximumInstallmentNumber,
			String sipInstallmentGap,String navDate,String currentNav,String fundManager,String investmentObjective,String schemeOptions,String schemeCategory,String schemeSubCategory,String classification) {
		this.uniqueNo = uniqueNo;
		this.schemeCode = schemeCode;
		this.rtaSchemeCode = rtaSchemeCode;
		this.amcSchemeCode = amcSchemeCode;
		this.isin = isin;
		this.amcCode = amcCode;
		this.schemeType = schemeType;
		this.schemePlan = schemePlan;
		this.schemeName = schemeName;
		this.purchaseAllowed = purchaseAllowed;
		this.purchaseTransactionMode = purchaseTransactionMode;
		this.minimumPurchaseAmount = minimumPurchaseAmount;
		this.additionalPurchaseAmount = additionalPurchaseAmount;
		this.maximumPurchaseAmount = maximumPurchaseAmount;
		this.purchaseAmountMultiplier = purchaseAmountMultiplier;
		this.purchaseCutoffTime = purchaseCutoffTime;
		this.redemptionAllowed = redemptionAllowed;
		this.redemptionTransactionMode = redemptionTransactionMode;
		this.minimumRedemptionQty = minimumRedemptionQty;
		this.redemptionQtyMultiplier = redemptionQtyMultiplier;
		this.maximumRedemptionQty = maximumRedemptionQty;
		this.redemptionAmountMinimum = redemptionAmountMinimum;
		this.redemptionAmountMaximum = redemptionAmountMaximum;
		this.redemptionAmountMultiple = redemptionAmountMultiple;
		this.redemptionCutoffTime = redemptionCutoffTime;
		this.rtaAgentCode = rtaAgentCode;
		this.amcActiveFlag = amcActiveFlag;
		this.dividendReinvestmentFlag = dividendReinvestmentFlag;
		this.sipFlag = sipFlag;
		this.stpFlag = stpFlag;
		this.swpFlag = swpFlag;
		this.switchFlag = switchFlag;
		this.settlementType = settlementType;
		this.amcInd = amcInd;
		this.faceValue = faceValue;
		this.startDate = startDate;
		this.endDate = endDate;
		this.exitLoadFlag = exitLoadFlag;
		this.exitLoad = exitLoad;
		this.lockInPeriodFlag = lockInPeriodFlag;
		this.lockInPeriod = lockInPeriod;
		this.channelPartnerCode = channelPartnerCode;
		this.amfiCode = amfiCode;
		this.benchmarkCode = benchmarkCode;
		this.rating = rating;
		this.nfoDate = nfoDate;
		this.status = status;
		this.registrarTransferAgent = registrarTransferAgent;
		this.risk = risk;
		this.year = year;
		this.returnValue = returnValue;
		this.minSipAmount = minSipAmount;
		this.fund_Nature = fund_Nature;
		this.field2 = field2;
		this.orderItems = orderItems;
		this.schemeRecentViews = schemeRecentViews;
		this.keyword = keyword;
		this.display = display;
		this.priority = priority;
		this.sipTransactionMode = sipTransactionMode;
		this.sipFrequency = sipFrequency;
		this.sipDates = sipDates;
		this.sipMinimumGap = sipMinimumGap;
		this.sipMaximumGap = sipMaximumGap;
		this.sipStatus = sipStatus;
		this.sipMinimumInstallmentAmount = sipMinimumInstallmentAmount;
		this.sipMaximumInstallmentAmount = sipMaximumInstallmentAmount;
		this.sipMultiplierAmount = sipMultiplierAmount;
		this.sipMinimumInstallmentNumber = sipMinimumInstallmentNumber;
		this.sipMaximumInstallmentNumber = sipMaximumInstallmentNumber;
		this.sipInstallmentGap = sipInstallmentGap;
		this.navDate = navDate;
		this.currentNav = currentNav;
		this.fundManager = fundManager;
		this.schemeSubCategory = schemeSubCategory;
		this.schemeCategory = schemeCategory;
		this.schemeOptions = schemeOptions;
		this.investmentObjective = investmentObjective;
		this.classification = classification;
	}

	@Id
	@GeneratedValue(strategy = IDENTITY)
	@Column(name = "SCHEME_ID", unique = true, nullable = false)
	public Integer getSchemeId() {
		return this.schemeId;
	}

	public void setSchemeId(Integer schemeId) {
		this.schemeId = schemeId;
	}

	@Column(name = "UNIQUE_NO")
	public Integer getUniqueNo() {
		return this.uniqueNo;
	}

	public void setUniqueNo(Integer uniqueNo) {
		this.uniqueNo = uniqueNo;
	}

	@Column(name = "SCHEME_CODE", length = 126)
	public String getSchemeCode() {
		return this.schemeCode;
	}

	public void setSchemeCode(String schemeCode) {
		this.schemeCode = schemeCode;
	}

	@Column(name = "RTA_SCHEME_CODE")
	public String getRtaSchemeCode() {
		return this.rtaSchemeCode;
	}

	public void setRtaSchemeCode(String rtaSchemeCode) {
		this.rtaSchemeCode = rtaSchemeCode;
	}

	@Column(name = "AMC_SCHEME_CODE")
	public String getAmcSchemeCode() {
		return this.amcSchemeCode;
	}

	public void setAmcSchemeCode(String amcSchemeCode) {
		this.amcSchemeCode = amcSchemeCode;
	}

	@Column(name = "ISIN", length = 126)
	public String getIsin() {
		return this.isin;
	}

	public void setIsin(String isin) {
		this.isin = isin;
	}

	@Column(name = "AMC_CODE", length = 126)
	public String getAmcCode() {
		return this.amcCode;
	}

	public void setAmcCode(String amcCode) {
		this.amcCode = amcCode;
	}

	@Column(name = "SCHEME_TYPE", length = 126)
	public String getSchemeType() {
		return this.schemeType;
	}

	public void setSchemeType(String schemeType) {
		this.schemeType = schemeType;
	}

	@Column(name = "SCHEME_PLAN", length = 126)
	public String getSchemePlan() {
		return this.schemePlan;
	}

	public void setSchemePlan(String schemePlan) {
		this.schemePlan = schemePlan;
	}

	@Column(name = "SCHEME_NAME", length = 256)
	public String getSchemeName() {
		return this.schemeName;
	}

	public void setSchemeName(String schemeName) {
		this.schemeName = schemeName;
	}

	@Column(name = "PURCHASE_ALLOWED", length = 5)
	public String getPurchaseAllowed() {
		return this.purchaseAllowed;
	}

	public void setPurchaseAllowed(String purchaseAllowed) {
		this.purchaseAllowed = purchaseAllowed;
	}

	@Column(name = "PURCHASE_TRANSACTION_MODE", length = 126)
	public String getPurchaseTransactionMode() {
		return this.purchaseTransactionMode;
	}

	public void setPurchaseTransactionMode(String purchaseTransactionMode) {
		this.purchaseTransactionMode = purchaseTransactionMode;
	}

	@Column(name = "MINIMUM_PURCHASE_AMOUNT", length = 126)
	public Integer getMinimumPurchaseAmount() {
		return this.minimumPurchaseAmount;
	}

	public void setMinimumPurchaseAmount(Integer minimumPurchaseAmount) {
		this.minimumPurchaseAmount = minimumPurchaseAmount;
	}

	@Column(name = "ADDITIONAL_PURCHASE_AMOUNT", length = 126)
	public String getAdditionalPurchaseAmount() {
		return this.additionalPurchaseAmount;
	}

	public void setAdditionalPurchaseAmount(String additionalPurchaseAmount) {
		this.additionalPurchaseAmount = additionalPurchaseAmount;
	}

	@Column(name = "MAXIMUM_PURCHASE_AMOUNT", length = 126)
	public String getMaximumPurchaseAmount() {
		return this.maximumPurchaseAmount;
	}

	public void setMaximumPurchaseAmount(String maximumPurchaseAmount) {
		this.maximumPurchaseAmount = maximumPurchaseAmount;
	}

	@Column(name = "PURCHASE_AMOUNT_MULTIPLIER", length = 126)
	public String getPurchaseAmountMultiplier() {
		return this.purchaseAmountMultiplier;
	}

	public void setPurchaseAmountMultiplier(String purchaseAmountMultiplier) {
		this.purchaseAmountMultiplier = purchaseAmountMultiplier;
	}

	@Column(name = "PURCHASE_CUTOFF_TIME", length = 126)
	public String getPurchaseCutoffTime() {
		return this.purchaseCutoffTime;
	}

	public void setPurchaseCutoffTime(String purchaseCutoffTime) {
		this.purchaseCutoffTime = purchaseCutoffTime;
	}

	@Column(name = "REDEMPTION_ALLOWED", length = 5)
	public String getRedemptionAllowed() {
		return this.redemptionAllowed;
	}

	public void setRedemptionAllowed(String redemptionAllowed) {
		this.redemptionAllowed = redemptionAllowed;
	}

	@Column(name = "REDEMPTION_TRANSACTION_MODE", length = 126)
	public String getRedemptionTransactionMode() {
		return this.redemptionTransactionMode;
	}

	public void setRedemptionTransactionMode(String redemptionTransactionMode) {
		this.redemptionTransactionMode = redemptionTransactionMode;
	}

	@Column(name = "MINIMUM_REDEMPTION_QTY", length = 126)
	public String getMinimumRedemptionQty() {
		return this.minimumRedemptionQty;
	}

	public void setMinimumRedemptionQty(String minimumRedemptionQty) {
		this.minimumRedemptionQty = minimumRedemptionQty;
	}

	@Column(name = "REDEMPTION_QTY_MULTIPLIER", length = 126)
	public String getRedemptionQtyMultiplier() {
		return this.redemptionQtyMultiplier;
	}

	public void setRedemptionQtyMultiplier(String redemptionQtyMultiplier) {
		this.redemptionQtyMultiplier = redemptionQtyMultiplier;
	}

	@Column(name = "MAXIMUM_REDEMPTION_QTY", length = 126)
	public String getMaximumRedemptionQty() {
		return this.maximumRedemptionQty;
	}

	public void setMaximumRedemptionQty(String maximumRedemptionQty) {
		this.maximumRedemptionQty = maximumRedemptionQty;
	}

	@Column(name = "REDEMPTION_AMOUNT_MINIMUM", length = 126)
	public String getRedemptionAmountMinimum() {
		return this.redemptionAmountMinimum;
	}

	public void setRedemptionAmountMinimum(String redemptionAmountMinimum) {
		this.redemptionAmountMinimum = redemptionAmountMinimum;
	}

	@Column(name = "REDEMPTION_AMOUNT_MAXIMUM", length = 126)
	public String getRedemptionAmountMaximum() {
		return this.redemptionAmountMaximum;
	}

	public void setRedemptionAmountMaximum(String redemptionAmountMaximum) {
		this.redemptionAmountMaximum = redemptionAmountMaximum;
	}

	@Column(name = "REDEMPTION_AMOUNT_MULTIPLE", length = 126)
	public String getRedemptionAmountMultiple() {
		return this.redemptionAmountMultiple;
	}

	public void setRedemptionAmountMultiple(String redemptionAmountMultiple) {
		this.redemptionAmountMultiple = redemptionAmountMultiple;
	}

	@Column(name = "REDEMPTION_CUTOFF_TIME", length = 126)
	public String getRedemptionCutoffTime() {
		return this.redemptionCutoffTime;
	}

	public void setRedemptionCutoffTime(String redemptionCutoffTime) {
		this.redemptionCutoffTime = redemptionCutoffTime;
	}

	@Column(name = "RTA_AGENT_CODE", length = 126)
	public String getRtaAgentCode() {
		return this.rtaAgentCode;
	}

	public void setRtaAgentCode(String rtaAgentCode) {
		this.rtaAgentCode = rtaAgentCode;
	}

	@Column(name = "AMC_ACTIVE_FLAG", length = 126)
	public String getAmcActiveFlag() {
		return this.amcActiveFlag;
	}

	public void setAmcActiveFlag(String amcActiveFlag) {
		this.amcActiveFlag = amcActiveFlag;
	}

	@Column(name = "DIVIDEND_REINVESTMENT_FLAG", length = 5)
	public String getDividendReinvestmentFlag() {
		return this.dividendReinvestmentFlag;
	}

	public void setDividendReinvestmentFlag(String dividendReinvestmentFlag) {
		this.dividendReinvestmentFlag = dividendReinvestmentFlag;
	}

	@Column(name = "SIP_FLAG", length = 5)
	public String getSipFlag() {
		return this.sipFlag;
	}

	public void setSipFlag(String sipFlag) {
		this.sipFlag = sipFlag;
	}

	@Column(name = "STP_FLAG", length = 5)
	public String getStpFlag() {
		return this.stpFlag;
	}

	public void setStpFlag(String stpFlag) {
		this.stpFlag = stpFlag;
	}

	@Column(name = "SWP_FLAG", length = 5)
	public String getSwpFlag() {
		return this.swpFlag;
	}

	public void setSwpFlag(String swpFlag) {
		this.swpFlag = swpFlag;
	}

	@Column(name = "SWITCH_FLAG", length = 5)
	public String getSwitchFlag() {
		return this.switchFlag;
	}

	public void setSwitchFlag(String switchFlag) {
		this.switchFlag = switchFlag;
	}

	@Column(name = "SETTLEMENT_TYPE", length = 126)
	public String getSettlementType() {
		return this.settlementType;
	}

	public void setSettlementType(String settlementType) {
		this.settlementType = settlementType;
	}

	@Column(name = "AMC_IND", length = 126)
	public String getAmcInd() {
		return this.amcInd;
	}

	public void setAmcInd(String amcInd) {
		this.amcInd = amcInd;
	}

	@Column(name = "FACE_VALUE", length = 126)
	public String getFaceValue() {
		return this.faceValue;
	}

	public void setFaceValue(String faceValue) {
		this.faceValue = faceValue;
	}

	@Column(name = "START_DATE", length = 126)
	public String getStartDate() {
		return this.startDate;
	}

	public void setStartDate(String startDate) {
		this.startDate = startDate;
	}

	@Column(name = "END_DATE", length = 126)
	public String getEndDate() {
		return this.endDate;
	}

	public void setEndDate(String endDate) {
		this.endDate = endDate;
	}

	@Column(name = "EXIT_LOAD_FLAG", length = 126)
	public String getExitLoadFlag() {
		return this.exitLoadFlag;
	}

	public void setExitLoadFlag(String exitLoadFlag) {
		this.exitLoadFlag = exitLoadFlag;
	}

	@Column(name = "EXIT_LOAD", length = 512)
	public String getExitLoad() {
		return this.exitLoad;
	}

	public void setExitLoad(String exitLoad) {
		this.exitLoad = exitLoad;
	}

	@Column(name = "LOCK_IN_PERIOD_FLAG", length = 126)
	public String getLockInPeriodFlag() {
		return this.lockInPeriodFlag;
	}

	public void setLockInPeriodFlag(String lockInPeriodFlag) {
		this.lockInPeriodFlag = lockInPeriodFlag;
	}

	@Column(name = "LOCK_IN_PERIOD", length = 126)
	public String getLockInPeriod() {
		return this.lockInPeriod;
	}

	public void setLockInPeriod(String lockInPeriod) {
		this.lockInPeriod = lockInPeriod;
	}

	@Column(name = "CHANNEL_PARTNER_CODE", length = 126)
	public String getChannelPartnerCode() {
		return this.channelPartnerCode;
	}

	public void setChannelPartnerCode(String channelPartnerCode) {
		this.channelPartnerCode = channelPartnerCode;
	}

	@Column(name = "AMFI_CODE", nullable = false)
	public int getAmfiCode() {
		return this.amfiCode;
	}

	public void setAmfiCode(int amfiCode) {
		this.amfiCode = amfiCode;
	}

	@Column(name = "BENCHMARK_CODE", length = 126)
	public String getBenchmarkCode() {
		return this.benchmarkCode;
	}

	public void setBenchmarkCode(String benchmarkCode) {
		this.benchmarkCode = benchmarkCode;
	}

	@Column(name = "RATING", length = 126)
	public String getRating() {
		return this.rating;
	}

	public void setRating(String rating) {
		this.rating = rating;
	}

	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "NFO_DATE", nullable = false, length = 19)
	public Date getNfoDate() {
		return this.nfoDate;
	}

	public void setNfoDate(Date nfoDate) {
		this.nfoDate = nfoDate;
	}

	@Column(name = "STATUS", length = 126)
	public String getStatus() {
		return this.status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	@Column(name = "REGISTRAR_TRANSFER_AGENT", length = 126)
	public String getRegistrarTransferAgent() {
		return this.registrarTransferAgent;
	}

	public void setRegistrarTransferAgent(String registrarTransferAgent) {
		this.registrarTransferAgent = registrarTransferAgent;
	}

	@Column(name = "RISK", length = 126)
	public String getRisk() {
		return this.risk;
	}

	public void setRisk(String risk) {
		this.risk = risk;
	}

	@Column(name = "YEAR", length = 126)
	public String getYear() {
		return this.year;
	}

	public void setYear(String year) {
		this.year = year;
	}
	
	@Column(name = "MIN_SIP_AMOUNT", length = 126)
	public String getMinSipAmount() {
		return this.minSipAmount;
	}

	public void setMinSipAmount(String minSipAmount) {
		this.minSipAmount = minSipAmount;
	}

	@Column(name = "Fund_Nature", length = 512)
	public String getFund_Nature() {
		return fund_Nature;
	}
	
	public void setFund_Nature(String fund_Nature) {
		this.fund_Nature = fund_Nature;
	}

	@Column(name = "FIELD2", length = 126)
	public String getField2() {
		return this.field2;
	}

	public void setField2(String field2) {
		this.field2 = field2;
	}

	@OneToMany(fetch = FetchType.LAZY, mappedBy = "scheme")
	public Set<OrderItem> getOrderItems() {
		return this.orderItems;
	}

	public void setOrderItems(Set<OrderItem> orderItems) {
		this.orderItems = orderItems;
	}

	@Column(name = "RETURN_VALUE", length = 126)
	public String getReturnValue() {
		return returnValue;
	}

	public void setReturnValue(String returnValue) {
		this.returnValue = returnValue;
	}
	
	@OneToMany(fetch = FetchType.LAZY, mappedBy = "scheme", cascade=CascadeType.ALL)
	public Set<SchemeRecentView> getSchemeRecentViews() {
		return this.schemeRecentViews;
	}

	public void setSchemeRecentViews(Set<SchemeRecentView> schemeRecentViews) {
		this.schemeRecentViews = schemeRecentViews;
	}

	@Column(name="KEYWORD", length=256)
	public String getKeyword() {
		return keyword;
	}

	public void setKeyword(String keyword) {
		this.keyword = keyword;
	}

	@Column(name="DISPLAY", length=50)
	public String getDisplay() {
		return display;
	}

	public void setDisplay(String display) {
		this.display = display;
	}

	@Column(name="PRIORITY", length=50)
	public String getPriority() {
		return priority;
	}

	public void setPriority(String priority) {
		this.priority = priority;
	}

	
	@Column(name="SIP_TRANSACTION_MODE", length=50)
	public String getSipTransactionMode() {
		return sipTransactionMode;
	}

	public void setSipTransactionMode(String sipTransactionMode) {
		this.sipTransactionMode = sipTransactionMode;
	}

	@Column(name="SIP_FREQUENCY", length=50)
	public String getSipFrequency() {
		return sipFrequency;
	}

	public void setSipFrequency(String sipFrequency) {
		this.sipFrequency = sipFrequency;
	}

	@Column(name="SIP_DATES", length=500)
	public String getSipDates() {
		return sipDates;
	}

	public void setSipDates(String sipDates) {
		this.sipDates = sipDates;
	}

	@Column(name="SIP_MINIMUM_GAP", length=50)
	public String getSipMinimumGap() {
		return sipMinimumGap;
	}

	public void setSipMinimumGap(String sipMinimumGap) {
		this.sipMinimumGap = sipMinimumGap;
	}

	@Column(name="SIP_MAXIMUM_GAP", length=50)
	public String getSipMaximumGap() {
		return sipMaximumGap;
	}

	public void setSipMaximumGap(String sipMaximumGap) {
		this.sipMaximumGap = sipMaximumGap;
	}

	@Column(name="SIP_STATUS", length=50)
	public String getSipStatus() {
		return sipStatus;
	}

	public void setSipStatus(String sipStatus) {
		this.sipStatus = sipStatus;
	}

	@Column(name="SIP_MINIMUM_INSTALLMENT_AMOUNT", length=50)
	public BigDecimal getSipMinimumInstallmentAmount() {
		return sipMinimumInstallmentAmount;
	}

	public void setSipMinimumInstallmentAmount(BigDecimal sipMinimumInstallmentAmount) {
		this.sipMinimumInstallmentAmount = sipMinimumInstallmentAmount;
	}

	@Column(name="SIP_MAXIMUM_INSTALLMENT_AMOUNT", length=50)
	public BigDecimal getSipMaximumInstallmentAmount() {
		return sipMaximumInstallmentAmount;
	}

	public void setSipMaximumInstallmentAmount(BigDecimal sipMaximumInstallmentAmount) {
		this.sipMaximumInstallmentAmount = sipMaximumInstallmentAmount;
	}

	@Column(name="SIP_MULTIPLIER_AMOUNT", length=50)
	public String getSipMultiplierAmount() {
		return sipMultiplierAmount;
	}

	public void setSipMultiplierAmount(String sipMultiplierAmount) {
		this.sipMultiplierAmount = sipMultiplierAmount;
	}

	@Column(name="SIP_MINIMUM_INSTALLMENT_NUMBERS", length=50)
	public String getSipMinimumInstallmentNumber() {
		return sipMinimumInstallmentNumber;
	}

	public void setSipMinimumInstallmentNumber(String sipMinimumInstallmentNumber) {
		this.sipMinimumInstallmentNumber = sipMinimumInstallmentNumber;
	}

	@Column(name="SIP_MAXIMUM_INSTALLMENT_NUMBERS", length=50)
	public String getSipMaximumInstallmentNumber() {
		return sipMaximumInstallmentNumber;
	}

	public void setSipMaximumInstallmentNumber(String sipMaximumInstallmentNumber) {
		this.sipMaximumInstallmentNumber = sipMaximumInstallmentNumber;
	}

	@Column(name="SIP_INSTALLMENT_GAP", length=50)
	public String getSipInstallmentGap() {
		return sipInstallmentGap;
	}

	public void setSipInstallmentGap(String sipInstallmentGap) {
		this.sipInstallmentGap = sipInstallmentGap;
	}
	
	@Column(name="Current_Nav", length=50)
	public String getCurrentNav() {
		return currentNav;
	}
	public void setCurrentNav(String currentNav) {
		this.currentNav = currentNav;
	}
	@Column(name="Nav_Date", length=50)
	public String getNavDate() {
		return navDate;
	}
	public void setNavDate(String navDate) {
		this.navDate = navDate;
	}

	@Column(name = "FUND_MANAGER", length = 512)
	public String getFundManager() {
		return fundManager;
	}

	public void setFundManager(String fundManager) {
		this.fundManager = fundManager;
	}

	@Column(name = "INVESTMENT_OBJECTIVE", length = 5000)
	public String getInvestmentObjective() {
		return investmentObjective;
	}

	public void setInvestmentObjective(String investmentObjective) {
		this.investmentObjective = investmentObjective;
	}

	@Column(name = "SCHEME_OPTIONS", length = 512)
	public String getSchemeOptions() {
		return schemeOptions;
	}

	public void setSchemeOptions(String schemeOptions) {
		this.schemeOptions = schemeOptions;
	}

	@Column(name = "SCHEME_CATEGORY", length = 512)
	public String getSchemeCategory() {
		return schemeCategory;
	}

	public void setSchemeCategory(String schemeCategory) {
		this.schemeCategory = schemeCategory;
	}

	@Column(name = "SCHEME_SUB_CATEGORY", length = 512)
	public String getSchemeSubCategory() {
		return schemeSubCategory;
	}

	public void setSchemeSubCategory(String schemeSubCategory) {
		this.schemeSubCategory = schemeSubCategory;
	}

	@Column(name = "CLASSIFICATION", length = 512)
	public String getClassification() {
		return classification;
	}

	public void setClassification(String classification) {
		this.classification = classification;
	}

}
