package com.moptra.go4wealth.bean;
// Generated 1 Oct, 2018 5:03:23 PM by Hibernate Tools 5.1.0.Alpha1

import java.math.BigDecimal;
import java.util.Date;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import static javax.persistence.GenerationType.IDENTITY;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

/**
 * OrderStatusReportUserdata generated by hbm2java
 */
@Entity
@Table(name = "order_status_report_userdata", catalog = "go4wealthdb")
public class OrderStatusReportUserdata implements java.io.Serializable {

	private Integer orderStatusReportUserdataId;
	private String allUnits;
	private BigDecimal amount;
	private String buySell;
	private String buySellType;
	private String clientCode;
	private String clientName;
	private String dpc;
	private String dpfolioNo;
	private String dptrans;
	private String date;
	private String euin;
	private String euinDecl;
	private String entryBy;
	private String firstOrder;
	private String folioNo;
	private String isin;
	private String internalRefNo;
	private String kycFlag;
	private String minRedeemFlag;
	private String memberCode;
	private String memberRemarks;
	private String orderNumber;
	private String orderRemarks;
	private String orderStatus;
	private String orderType;
	private String sipregnDate;
	private Long sipRegnNo;
	private String schemeCode;
	private String schemeName;
	private String settNo;
	private String settType;
	private String subBrCode;
	private String subBrokerArnCode;
	private String subOrderType;
	private String time;
	private BigDecimal units;
	private String lastUpdatedDate;
	private String createdDate;
	private String status;
	private Integer allotMailStatus;
	private BigDecimal field5;
	private String redumptionRemark;
	private Integer goalId;
	private String goalName;

	public OrderStatusReportUserdata() {
	}

	public OrderStatusReportUserdata(String allUnits, BigDecimal amount, String buySell, String buySellType,
			String clientCode, String clientName, String dpc, String dpfolioNo, String dptrans, String date, String euin,
			String euinDecl, String entryBy, String firstOrder, String folioNo, String isin, String internalRefNo,
			String kycFlag, String minRedeemFlag, String memberCode, String memberRemarks, String orderNumber,
			String orderRemarks, String orderStatus, String orderType, String sipregnDate, Long sipRegnNo,
			String schemeCode, String schemeName, String settNo, String settType, String subBrCode,
			String subBrokerArnCode, String subOrderType, String time, BigDecimal units, String field1, String field2,
			String status, Integer allotMailStatus, BigDecimal field5,String redumptionRemark,Integer goalId,String goalName) {
		this.allUnits = allUnits;
		this.amount = amount;
		this.buySell = buySell;
		this.buySellType = buySellType;
		this.clientCode = clientCode;
		this.clientName = clientName;
		this.dpc = dpc;
		this.dpfolioNo = dpfolioNo;
		this.dptrans = dptrans;
		this.date = date;
		this.euin = euin;
		this.euinDecl = euinDecl;
		this.entryBy = entryBy;
		this.firstOrder = firstOrder;
		this.folioNo = folioNo;
		this.isin = isin;
		this.internalRefNo = internalRefNo;
		this.kycFlag = kycFlag;
		this.minRedeemFlag = minRedeemFlag;
		this.memberCode = memberCode;
		this.memberRemarks = memberRemarks;
		this.orderNumber = orderNumber;
		this.orderRemarks = orderRemarks;
		this.orderStatus = orderStatus;
		this.orderType = orderType;
		this.sipregnDate = sipregnDate;
		this.sipRegnNo = sipRegnNo;
		this.schemeCode = schemeCode;
		this.schemeName = schemeName;
		this.settNo = settNo;
		this.settType = settType;
		this.subBrCode = subBrCode;
		this.subBrokerArnCode = subBrokerArnCode;
		this.subOrderType = subOrderType;
		this.time = time;
		this.units = units;
		this.lastUpdatedDate = field1;
		this.createdDate = field2;
		this.status = status;
		this.allotMailStatus = allotMailStatus;
		this.field5 = field5;
		this.redumptionRemark =redumptionRemark;
		this.goalId = goalId;
		this.goalName = goalName;
	}

	@Id
	@GeneratedValue(strategy = IDENTITY)

	@Column(name = "Order_Status_Report_Userdata_Id", unique = true, nullable = false)
	public Integer getOrderStatusReportUserdataId() {
		return this.orderStatusReportUserdataId;
	}

	public void setOrderStatusReportUserdataId(Integer orderStatusReportUserdataId) {
		this.orderStatusReportUserdataId = orderStatusReportUserdataId;
	}

	@Column(name = "ALL_Units", length = 126)
	public String getAllUnits() {
		return this.allUnits;
	}

	public void setAllUnits(String allUnits) {
		this.allUnits = allUnits;
	}

	@Column(name = "Amount")
	public BigDecimal getAmount() {
		return this.amount;
	}

	public void setAmount(BigDecimal amount) {
		this.amount = amount;
	}

	@Column(name = "Buy_Sell", length = 126)
	public String getBuySell() {
		return this.buySell;
	}

	public void setBuySell(String buySell) {
		this.buySell = buySell;
	}

	@Column(name = "Buy_Sell_Type", length = 126)
	public String getBuySellType() {
		return this.buySellType;
	}

	public void setBuySellType(String buySellType) {
		this.buySellType = buySellType;
	}

	@Column(name = "Client_Code", length = 126)
	public String getClientCode() {
		return this.clientCode;
	}

	public void setClientCode(String clientCode) {
		this.clientCode = clientCode;
	}

	@Column(name = "Client_Name", length = 126)
	public String getClientName() {
		return this.clientName;
	}

	public void setClientName(String clientName) {
		this.clientName = clientName;
	}

	@Column(name = "DPC", length = 126)
	public String getDpc() {
		return this.dpc;
	}

	public void setDpc(String dpc) {
		this.dpc = dpc;
	}

	@Column(name = "DPFolio_No", length = 126)
	public String getDpfolioNo() {
		return this.dpfolioNo;
	}

	public void setDpfolioNo(String dpfolioNo) {
		this.dpfolioNo = dpfolioNo;
	}

	@Column(name = "DPTrans", length = 126)
	public String getDptrans() {
		return this.dptrans;
	}

	public void setDptrans(String dptrans) {
		this.dptrans = dptrans;
	}


	@Column(name = "Date", length = 126)
	public String getDate() {
		return this.date;
	}

	public void setDate(String date) {
		this.date = date;
	}

	@Column(name = "EUIN", length = 126)
	public String getEuin() {
		return this.euin;
	}

	public void setEuin(String euin) {
		this.euin = euin;
	}

	@Column(name = "EUIN_Decl", length = 126)
	public String getEuinDecl() {
		return this.euinDecl;
	}

	public void setEuinDecl(String euinDecl) {
		this.euinDecl = euinDecl;
	}

	@Column(name = "Entry_By", length = 126)
	public String getEntryBy() {
		return this.entryBy;
	}

	public void setEntryBy(String entryBy) {
		this.entryBy = entryBy;
	}

	@Column(name = "First_Order", length = 126)
	public String getFirstOrder() {
		return this.firstOrder;
	}

	public void setFirstOrder(String firstOrder) {
		this.firstOrder = firstOrder;
	}

	@Column(name = "Folio_No", length = 126)
	public String getFolioNo() {
		return this.folioNo;
	}

	public void setFolioNo(String folioNo) {
		this.folioNo = folioNo;
	}

	@Column(name = "ISIN", length = 126)
	public String getIsin() {
		return this.isin;
	}

	public void setIsin(String isin) {
		this.isin = isin;
	}

	@Column(name = "Internal_RefNo", length = 126)
	public String getInternalRefNo() {
		return this.internalRefNo;
	}

	public void setInternalRefNo(String internalRefNo) {
		this.internalRefNo = internalRefNo;
	}

	@Column(name = "KYC_Flag", length = 126)
	public String getKycFlag() {
		return this.kycFlag;
	}

	public void setKycFlag(String kycFlag) {
		this.kycFlag = kycFlag;
	}

	@Column(name = "MIN_Redeem_Flag", length = 126)
	public String getMinRedeemFlag() {
		return this.minRedeemFlag;
	}

	public void setMinRedeemFlag(String minRedeemFlag) {
		this.minRedeemFlag = minRedeemFlag;
	}

	@Column(name = "Member_Code", length = 126)
	public String getMemberCode() {
		return this.memberCode;
	}

	public void setMemberCode(String memberCode) {
		this.memberCode = memberCode;
	}

	@Column(name = "Member_Remarks", length = 126)
	public String getMemberRemarks() {
		return this.memberRemarks;
	}

	public void setMemberRemarks(String memberRemarks) {
		this.memberRemarks = memberRemarks;
	}

	@Column(name = "Order_Number", length = 126)
	public String getOrderNumber() {
		return this.orderNumber;
	}

	public void setOrderNumber(String orderNumber) {
		this.orderNumber = orderNumber;
	}

	@Column(name = "Order_Remarks", length = 126)
	public String getOrderRemarks() {
		return this.orderRemarks;
	}

	public void setOrderRemarks(String orderRemarks) {
		this.orderRemarks = orderRemarks;
	}

	@Column(name = "Order_Status", length = 126)
	public String getOrderStatus() {
		return this.orderStatus;
	}

	public void setOrderStatus(String orderStatus) {
		this.orderStatus = orderStatus;
	}

	@Column(name = "Order_Type", length = 126)
	public String getOrderType() {
		return this.orderType;
	}

	public void setOrderType(String orderType) {
		this.orderType = orderType;
	}

	
	@Column(name = "SIPRegn_Date", length = 126)
	public String getSipregnDate() {
		return this.sipregnDate;
	}

	public void setSipregnDate(String sipregnDate) {
		this.sipregnDate = sipregnDate;
	}

	@Column(name = "SIP_Regn_No")
	public Long getSipRegnNo() {
		return this.sipRegnNo;
	}

	public void setSipRegnNo(Long sipRegnNo) {
		this.sipRegnNo = sipRegnNo;
	}

	@Column(name = "Scheme_Code", length = 126)
	public String getSchemeCode() {
		return this.schemeCode;
	}

	public void setSchemeCode(String schemeCode) {
		this.schemeCode = schemeCode;
	}

	@Column(name = "Scheme_Name", length = 126)
	public String getSchemeName() {
		return this.schemeName;
	}

	public void setSchemeName(String schemeName) {
		this.schemeName = schemeName;
	}

	@Column(name = "Sett_No", length = 126)
	public String getSettNo() {
		return this.settNo;
	}

	public void setSettNo(String settNo) {
		this.settNo = settNo;
	}

	@Column(name = "Sett_Type", length = 126)
	public String getSettType() {
		return this.settType;
	}

	public void setSettType(String settType) {
		this.settType = settType;
	}

	@Column(name = "Sub_Br_Code", length = 126)
	public String getSubBrCode() {
		return this.subBrCode;
	}

	public void setSubBrCode(String subBrCode) {
		this.subBrCode = subBrCode;
	}

	@Column(name = "Sub_Broker_ARN_Code", length = 126)
	public String getSubBrokerArnCode() {
		return this.subBrokerArnCode;
	}

	public void setSubBrokerArnCode(String subBrokerArnCode) {
		this.subBrokerArnCode = subBrokerArnCode;
	}

	@Column(name = "Sub_Order_Type", length = 126)
	public String getSubOrderType() {
		return this.subOrderType;
	}

	public void setSubOrderType(String subOrderType) {
		this.subOrderType = subOrderType;
	}

	@Column(name = "Time", length = 126)
	public String getTime() {
		return this.time;
	}

	public void setTime(String time) {
		this.time = time;
	}

	@Column(name = "Units")
	public BigDecimal getUnits() {
		return this.units;
	}

	public void setUnits(BigDecimal units) {
		this.units = units;
	}

	
	public void setLastUpdatedDate(String lastUpdatedDate) {
		this.lastUpdatedDate = lastUpdatedDate;
	}

	@Column(name = "Last_Updated_Date", length = 126)
	public String getLastUpdatedDate() {
		return lastUpdatedDate;
	}
	
	
	public void setCreatedDate(String createdDate) {
		this.createdDate = createdDate;
	}
	
	@Column(name = "Created_Date", length = 126)
	public String getCreatedDate() {
		return createdDate;
	}


	@Column(name = "STATUS", length = 126)
	public String getStatus() {
		return status;
	}
	
	public void setStatus(String status) {
		this.status = status;
	}

	@Column(name = "Allot_Mail_Status")
	public Integer getAllotMailStatus() {
		return allotMailStatus;
	}
	
	public void setAllotMailStatus(Integer allotMailStatus) {
		this.allotMailStatus = allotMailStatus;
	}

	@Column(name = "FIELD5", precision = 20, scale = 5)
	public BigDecimal getField5() {
		return this.field5;
	}

	public void setField5(BigDecimal field5) {
		this.field5 = field5;
	}
	@Column(name = "REDUMPTION_REMARK" , length = 512)
	public String getRedumptionRemark() {
		return redumptionRemark;
	}
	
	public void setRedumptionRemark(String redumptionRemark) {
		this.redumptionRemark = redumptionRemark;
	}
	
	@Column(name = "GOAL_ID")
	public Integer getGoalId() {
		return goalId;
	}
	
	public void setGoalId(Integer goalId) {
		this.goalId = goalId;
	}
	
	@Column(name = "GOAL_NAME")
	public String getGoalName() {
		return goalName;
	}
	public void setGoalName(String goalName) {
		this.goalName = goalName;
	}
}
