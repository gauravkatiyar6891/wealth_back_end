package com.moptra.go4wealth.bean;
// Generated May 14, 2018 1:41:21 PM by Hibernate Tools 5.1.0.Alpha1

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import static javax.persistence.GenerationType.IDENTITY;
import javax.persistence.Id;
import javax.persistence.Table;

/**
 * IncomeSlabs generated by hbm2java
 */
@Entity
@Table(name = "income_slabs", catalog = "go4wealthdb")
public class IncomeSlabs implements java.io.Serializable {

	private Integer incomeSlabId;
	private String incomeSlabName;
	private Integer incomeFrom;
	private Integer incomeTo;
	private String incomeSlabCode;
	private String incomePercentage;

	public IncomeSlabs() {
	}

	public IncomeSlabs(String incomeSlabName, Integer incomeFrom, Integer incomeTo, String incomeSlabCode,String incomePercentage) {
		this.incomeSlabName = incomeSlabName;
		this.incomeFrom = incomeFrom;
		this.incomeTo = incomeTo;
		this.incomeSlabCode = incomeSlabCode;
		this.incomePercentage = incomePercentage;
	}

	@Id
	@GeneratedValue(strategy = IDENTITY)
	@Column(name = "INCOME_SLAB_ID", unique = true, nullable = false)
	public Integer getIncomeSlabId() {
		return this.incomeSlabId;
	}

	public void setIncomeSlabId(Integer incomeSlabId) {
		this.incomeSlabId = incomeSlabId;
	}

	@Column(name = "INCOME_SLAB_NAME", length = 126)
	public String getIncomeSlabName() {
		return this.incomeSlabName;
	}

	public void setIncomeSlabName(String incomeSlabName) {
		this.incomeSlabName = incomeSlabName;
	}

	@Column(name = "INCOME_FROM")
	public Integer getIncomeFrom() {
		return this.incomeFrom;
	}

	public void setIncomeFrom(Integer incomeFrom) {
		this.incomeFrom = incomeFrom;
	}

	@Column(name = "INCOME_TO")
	public Integer getIncomeTo() {
		return this.incomeTo;
	}

	public void setIncomeTo(Integer incomeTo) {
		this.incomeTo = incomeTo;
	}

	@Column(name = "INCOME_SLAB_CODE", length = 5)
	public String getIncomeSlabCode() {
		return this.incomeSlabCode;
	}

	public void setIncomeSlabCode(String incomeSlabCode) {
		this.incomeSlabCode = incomeSlabCode;
	}

	@Column(name="INCOME_PERCENTAGE", length=512)
	public String getIncomePercentage() {
		return incomePercentage;
	}

	public void setIncomePercentage(String incomePercentage) {
		this.incomePercentage = incomePercentage;
	}

}
