package com.moptra.go4wealth.bean;
// Generated 18 Aug, 2018 3:59:01 PM by Hibernate Tools 5.1.0.Alpha1

import java.math.BigDecimal;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import static javax.persistence.GenerationType.IDENTITY;
import javax.persistence.Id;
import javax.persistence.Table;

/**
 * RedumptionManagement generated by hbm2java
 */
@Entity
@Table(name = "redumption_management", catalog = "go4wealthdb")
public class RedumptionManagement implements java.io.Serializable {

	private Integer redumptionManagementId;
	private String folioNumber;
	private String bseOrderId;
	private BigDecimal redumptionAmount;
	private BigDecimal availableAmount;
	private String redumptionDate;
	private BigDecimal redumptionUnit;
	private String status;
	private Long sipRegnId;
	private String schemeName;
	private String redeemOrderId;
	private Integer userId;
	private String redumptionType;
	private String schemeCode;
	private String redumptionRemark;
	private String isOrderAvailable;

	public RedumptionManagement() {
	}

	public RedumptionManagement(String folioNumber, String bseOrderId, BigDecimal redumptionAmount,
			BigDecimal availableAmount, String redumptionDate, BigDecimal redumptionUnit, String status, Long sipRegnId,
			String schemeName, String redeemOrderId, Integer userId, String redumptionType,String schemeCode,String redumptionRemark,String isOrderAvailable) {
		this.folioNumber = folioNumber;
		this.bseOrderId = bseOrderId;
		this.redumptionAmount = redumptionAmount;
		this.availableAmount = availableAmount;
		this.redumptionDate = redumptionDate;
		this.redumptionUnit = redumptionUnit;
		this.status = status;
		this.sipRegnId = sipRegnId;
		this.schemeName = schemeName;
		this.redeemOrderId = redeemOrderId;
		this.userId = userId;
		this.redumptionType = redumptionType;
		this.schemeCode = schemeCode;
		this.isOrderAvailable = isOrderAvailable;
		this.redumptionRemark = redumptionRemark;
	}

	@Id
	@GeneratedValue(strategy = IDENTITY)

	@Column(name = "REDUMPTION_MANAGEMENT_ID", unique = true, nullable = false)
	public Integer getRedumptionManagementId() {
		return this.redumptionManagementId;
	}

	public void setRedumptionManagementId(Integer redumptionManagementId) {
		this.redumptionManagementId = redumptionManagementId;
	}

	@Column(name = "FOLIO_NUMBER")
	public String getFolioNumber() {
		return this.folioNumber;
	}

	public void setFolioNumber(String folioNumber) {
		this.folioNumber = folioNumber;
	}

	@Column(name = "BSE_ORDER_ID", length = 126)
	public String getBseOrderId() {
		return this.bseOrderId;
	}

	public void setBseOrderId(String bseOrderId) {
		this.bseOrderId = bseOrderId;
	}

	@Column(name = "REDUMPTION_AMOUNT", precision = 20, scale = 5)
	public BigDecimal getRedumptionAmount() {
		return this.redumptionAmount;
	}

	public void setRedumptionAmount(BigDecimal redumptionAmount) {
		this.redumptionAmount = redumptionAmount;
	}

	@Column(name = "AVAILABLE_AMOUNT", precision = 20, scale = 5)
	public BigDecimal getAvailableAmount() {
		return this.availableAmount;
	}

	public void setAvailableAmount(BigDecimal availableAmount) {
		this.availableAmount = availableAmount;
	}

	@Column(name = "REDUMPTION_DATE", length = 126)
	public String getRedumptionDate() {
		return this.redumptionDate;
	}

	public void setRedumptionDate(String redumptionDate) {
		this.redumptionDate = redumptionDate;
	}

	@Column(name = "REDUMPTION_UNIT", precision = 20, scale = 5)
	public BigDecimal getRedumptionUnit() {
		return this.redumptionUnit;
	}

	public void setRedumptionUnit(BigDecimal redumptionUnit) {
		this.redumptionUnit = redumptionUnit;
	}

	@Column(name = "STATUS", length = 126)
	public String getStatus() {
		return this.status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	@Column(name = "SIP_Regn_No", length = 50)
	public Long getSipRegnId() {
		return sipRegnId;
	}

	public void setSipRegnId(Long sipRegnId) {
		this.sipRegnId = sipRegnId;
	}
	
	@Column(name = "Scheme_Name", length = 126)
	public String getSchemeName() {
		return schemeName;
	}

	public void setSchemeName(String schemeName) {
		this.schemeName = schemeName;
	}

	@Column(name = "REDEEM_ORDER_ID", length = 126)
	public String getRedeemOrderId() {
		return redeemOrderId;
	}

	public void setRedeemOrderId(String redeemOrderId) {
		this.redeemOrderId = redeemOrderId;
	}

	@Column(name = "USER_ID")
	public Integer getUserId() {
		return userId;
	}

	public void setUserId(Integer userId) {
		this.userId = userId;
	}
	
	@Column(name = "SCHEME_CODE", length = 126)
	public String getSchemeCode() {
		return schemeCode;
	}
	
	public void setSchemeCode(String schemeCode) {
		this.schemeCode = schemeCode;
	}

	@Column(name = "IS_ORDER_AVAILABLE" , length = 512)
	public String getIsOrderAvailable() {
		return isOrderAvailable;
	}
	public void setIsOrderAvailable(String isOrderAvailable) {
		this.isOrderAvailable = isOrderAvailable;
	}
	
	@Column(name = "REDUMPTION_REMARK" , length = 10)
	public String getRedumptionRemark() {
		return redumptionRemark;
	}
	public void setRedumptionRemark(String redumptionRemark) {
		this.redumptionRemark = redumptionRemark;
	}

	@Column(name = "REDUMPTION_TYPE", length=10)
	public String getRedumptionType() {
		return redumptionType;
	}

	public void setRedumptionType(String redumptionType) {
		this.redumptionType = redumptionType;
	}

}