package com.moptra.go4wealth.sip.model;

import com.moptra.go4wealth.bean.IncomeSlabs;

public class IncomeDTO {

	private Integer incomeSlabId;
	private String incomeSlabName;
	private String incomeSlabCode;
	private Integer incomeFrom;
	private Integer incomeTo;
	
	public IncomeDTO(Integer incomeSlabId, String incomeSlabName) {
		this.incomeSlabId = incomeSlabId;
		this.incomeSlabName = incomeSlabName;
	}
	
	
	public IncomeDTO(IncomeSlabs incomeSlabs) {
		this.incomeSlabName= incomeSlabs.getIncomeSlabName();
		this.incomeSlabCode= incomeSlabs.getIncomeSlabCode();
	}
	

	public IncomeDTO() {
		super();
	}

	public Integer getIncomeSlabId() {
		return incomeSlabId;
	}

	public void setIncomeSlabId(Integer incomeSlabId) {
		this.incomeSlabId = incomeSlabId;
	}

	public String getIncomeSlabName() {
		return incomeSlabName;
	}

	public void setIncomeSlabName(String incomeSlabName) {
		this.incomeSlabName = incomeSlabName;
	}


	public String getIncomeSlabCode() {
		return incomeSlabCode;
	}


	public void setIncomeSlabCode(String incomeSlabCode) {
		this.incomeSlabCode = incomeSlabCode;
	}

	public Integer getIncomeFrom() {
		return incomeFrom;
	}

	public void setIncomeFrom(Integer incomeFrom) {
		this.incomeFrom = incomeFrom;
	}

	public Integer getIncomeTo() {
		return incomeTo;
	}

	public void setIncomeTo(Integer incomeTo) {
		this.incomeTo = incomeTo;
	}
}
