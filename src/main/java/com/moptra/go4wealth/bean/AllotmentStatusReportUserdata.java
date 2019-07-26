// Generated 1 Oct, 2018 5:03:23 PM by Hibernate Tools 5.1.0.Alpha1
package com.moptra.go4wealth.bean;
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
 * AllotmentStatusReportUserdata generated by hbm2java
 */
@Entity
@Table(name = "allotment_status_report_userdata", catalog = "go4wealthdb")
public class AllotmentStatusReportUserdata implements java.io.Serializable {

	private Integer allotmentStatusReportUserdataId;
	private BigDecimal allottedAmt;
	private BigDecimal allottedNav;
	private BigDecimal allottedUnit;
	private BigDecimal amount;
	private String beneficiaryId;
	private String branchCode;
	private String clientCode;
	private String clientName;
	private String dpcFlag;
	private String dpTrans;
	private String euin;
	private String euinDecl;
	private String folioNo;
	private String isin;
	private String internalRefNo;
	private String kycFlag;
	private String memberCode;
	private String orderDate;
	private Integer orderNo;
	private String orderType;
	private Integer qty;
	private String rtaSchemeCode;
	private String rtaTransNo;
	private String remarks;
	private String reportDate;
	private String sipRegnDate;
	private Long sipRegnNo;
	private Integer stt;
	private String schemeCode;
	private String settNo;
	private String settType;
	private String subBrCode;
	private String subOrderType;
	private Integer userId;
	private String validFlag;
	private String lastUpdatedDate;
	private String createdDate;
	private String status;
	private Integer allotMailStatus;
	private BigDecimal field5;
	private String redumptionRemark;
	private Integer goalId;
	private String goalName;

	public AllotmentStatusReportUserdata() {
	}

	public AllotmentStatusReportUserdata(BigDecimal allottedAmt, BigDecimal allottedNav, BigDecimal allottedUnit, BigDecimal amount,
			String beneficiaryId, String branchCode, String clientCode, String clientName, String dpcFlag,
			String dpTrans, String euin, String euinDecl, String folioNo, String isin, String internalRefNo,
			String kycFlag, String memberCode, String orderDate, Integer orderNo, String orderType, Integer qty,
			String rtaSchemeCode, String rtaTransNo, String remarks, String reportDate, String sipRegnDate,
			Long sipRegnNo, Integer stt, String schemeCode, String settNo, String settType, String subBrCode,
			String subOrderType, Integer userId, String validFlag, String field1, String field2, String status,
			Integer allotMailStatus, BigDecimal field5,String redumptionRemark,Integer goalId,String goalName) {
		this.allottedAmt = allottedAmt;
		this.allottedNav = allottedNav;
		this.allottedUnit = allottedUnit;
		this.amount = amount;
		this.beneficiaryId = beneficiaryId;
		this.branchCode = branchCode;
		this.clientCode = clientCode;
		this.clientName = clientName;
		this.dpcFlag = dpcFlag;
		this.dpTrans = dpTrans;
		this.euin = euin;
		this.euinDecl = euinDecl;
		this.folioNo = folioNo;
		this.isin = isin;
		this.internalRefNo = internalRefNo;
		this.kycFlag = kycFlag;
		this.memberCode = memberCode;
		this.orderDate = orderDate;
		this.orderNo = orderNo;
		this.orderType = orderType;
		this.qty = qty;
		this.rtaSchemeCode = rtaSchemeCode;
		this.rtaTransNo = rtaTransNo;
		this.remarks = remarks;
		this.reportDate = reportDate;
		this.sipRegnDate = sipRegnDate;
		this.sipRegnNo = sipRegnNo;
		this.stt = stt;
		this.schemeCode = schemeCode;
		this.settNo = settNo;
		this.settType = settType;
		this.subBrCode = subBrCode;
		this.subOrderType = subOrderType;
		this.userId = userId;
		this.validFlag = validFlag;
		this.lastUpdatedDate = field1;
		this.createdDate = field2;
		this.status = status;
		this.allotMailStatus = allotMailStatus;
		this.field5 = field5;
		this.redumptionRemark = redumptionRemark;
		this.goalId = goalId;
		this.goalName = goalName;
	}

	@Id
	@GeneratedValue(strategy = IDENTITY)

	@Column(name = "Allotment_Status_Report_Userdata_Id", unique = true, nullable = false)
	public Integer getAllotmentStatusReportUserdataId() {
		return this.allotmentStatusReportUserdataId;
	}

	public void setAllotmentStatusReportUserdataId(Integer allotmentStatusReportUserdataId) {
		this.allotmentStatusReportUserdataId = allotmentStatusReportUserdataId;
	}

	@Column(name = "Allotted_Amt")
	public BigDecimal getAllottedAmt() {
		return this.allottedAmt;
	}

	public void setAllottedAmt(BigDecimal allottedAmt) {
		this.allottedAmt = allottedAmt;
	}

	@Column(name = "Allotted_Nav")
	public BigDecimal getAllottedNav() {
		return this.allottedNav;
	}

	public void setAllottedNav(BigDecimal allottedNav) {
		this.allottedNav = allottedNav;
	}

	@Column(name = "Allotted_Unit")
	public BigDecimal getAllottedUnit() {
		return this.allottedUnit;
	}

	public void setAllottedUnit(BigDecimal allottedUnit) {
		this.allottedUnit = allottedUnit;
	}

	@Column(name = "Amount")
	public BigDecimal getAmount() {
		return this.amount;
	}

	public void setAmount(BigDecimal amount) {
		this.amount = amount;
	}

	@Column(name = "Beneficiary_Id", length = 126)
	public String getBeneficiaryId() {
		return this.beneficiaryId;
	}

	public void setBeneficiaryId(String beneficiaryId) {
		this.beneficiaryId = beneficiaryId;
	}

	@Column(name = "Branch_Code", length = 126)
	public String getBranchCode() {
		return this.branchCode;
	}

	public void setBranchCode(String branchCode) {
		this.branchCode = branchCode;
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

	@Column(name = "DPC_Flag", length = 126)
	public String getDpcFlag() {
		return this.dpcFlag;
	}

	public void setDpcFlag(String dpcFlag) {
		this.dpcFlag = dpcFlag;
	}

	@Column(name = "DP_Trans", length = 126)
	public String getDpTrans() {
		return this.dpTrans;
	}

	public void setDpTrans(String dpTrans) {
		this.dpTrans = dpTrans;
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

	@Column(name = "Internal_Ref_No", length = 126)
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

	@Column(name = "Member_Code", length = 126)
	public String getMemberCode() {
		return this.memberCode;
	}

	public void setMemberCode(String memberCode) {
		this.memberCode = memberCode;
	}

	@Column(name = "Order_Date", length = 126)
	public String getOrderDate() {
		return this.orderDate;
	}

	public void setOrderDate(String orderDate) {
		this.orderDate = orderDate;
	}

	@Column(name = "Order_No")
	public Integer getOrderNo() {
		return this.orderNo;
	}

	public void setOrderNo(Integer orderNo) {
		this.orderNo = orderNo;
	}

	@Column(name = "Order_Type", length = 126)
	public String getOrderType() {
		return this.orderType;
	}

	public void setOrderType(String orderType) {
		this.orderType = orderType;
	}

	@Column(name = "Qty")
	public Integer getQty() {
		return this.qty;
	}

	public void setQty(Integer qty) {
		this.qty = qty;
	}

	@Column(name = "RTA_Scheme_Code", length = 126)
	public String getRtaSchemeCode() {
		return this.rtaSchemeCode;
	}

	public void setRtaSchemeCode(String rtaSchemeCode) {
		this.rtaSchemeCode = rtaSchemeCode;
	}

	@Column(name = "RTA_Trans_No", length = 126)
	public String getRtaTransNo() {
		return this.rtaTransNo;
	}

	public void setRtaTransNo(String rtaTransNo) {
		this.rtaTransNo = rtaTransNo;
	}

	@Column(name = "Remarks", length = 126)
	public String getRemarks() {
		return this.remarks;
	}

	public void setRemarks(String remarks) {
		this.remarks = remarks;
	}

	@Column(name = "Report_Date", length = 126)
	public String getReportDate() {
		return this.reportDate;
	}

	public void setReportDate(String reportDate) {
		this.reportDate = reportDate;
	}

	
	@Column(name = "SIP_Regn_Date", length = 126)
	public String getSipRegnDate() {
		return this.sipRegnDate;
	}

	public void setSipRegnDate(String sipRegnDate) {
		this.sipRegnDate = sipRegnDate;
	}

	@Column(name = "SIP_Regn_No")
	public Long getSipRegnNo() {
		return this.sipRegnNo;
	}

	public void setSipRegnNo(Long sipRegnNo) {
		this.sipRegnNo = sipRegnNo;
	}

	@Column(name = "STT")
	public Integer getStt() {
		return this.stt;
	}

	public void setStt(Integer stt) {
		this.stt = stt;
	}

	@Column(name = "Scheme_Code", length = 126)
	public String getSchemeCode() {
		return this.schemeCode;
	}

	public void setSchemeCode(String schemeCode) {
		this.schemeCode = schemeCode;
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

	@Column(name = "Sub_Order_Type", length = 126)
	public String getSubOrderType() {
		return this.subOrderType;
	}

	public void setSubOrderType(String subOrderType) {
		this.subOrderType = subOrderType;
	}

	@Column(name = "User_Id")
	public Integer getUserId() {
		return this.userId;
	}

	public void setUserId(Integer userId) {
		this.userId = userId;
	}

	@Column(name = "Valid_Flag", length = 126)
	public String getValidFlag() {
		return this.validFlag;
	}

	public void setValidFlag(String validFlag) {
		this.validFlag = validFlag;
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
