package com.moptra.go4wealth.admin.model;

import java.util.ArrayList;
import java.util.List;

public class SchemeDTO {

	private Integer topSchemeId;
	private Integer schemeId;
	private String schemeName;
	private String schemeCode;
	private String fundName;
	private String status;
	private String schemeType;
	private String option;
	private String plan;
	private String schemeLaunchDate;
	private String minSipAmount;
	private String risk;
	List<Integer> schemeIdList= new ArrayList<Integer>();
	
	public Integer getTopSchemeId() {
		return topSchemeId;
	}
	public void setTopSchemeId(Integer topSchemeId) {
		this.topSchemeId = topSchemeId;
	}
	public Integer getSchemeId() {
		return schemeId;
	}
	public void setSchemeId(Integer schemeId) {
		this.schemeId = schemeId;
	}
	
	public String getSchemeName() {
		return schemeName;
	}
	public void setSchemeName(String schemeName) {
		this.schemeName = schemeName;
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
	public String getSchemeLaunchDate() {
		return schemeLaunchDate;
	}
	public void setSchemeLaunchDate(String schemeLaunchDate) {
		this.schemeLaunchDate = schemeLaunchDate;
	}
	public String getMinSipAmount() {
		return minSipAmount;
	}
	public void setMinSipAmount(String minSipAmount) {
		this.minSipAmount = minSipAmount;
	}
	public String getRisk() {
		return risk;
	}
	public void setRisk(String risk) {
		this.risk = risk;
	}
	public List<Integer> getSchemeIdList() {
		return schemeIdList;
	}
	public void setSchemeIdList(List<Integer> schemeIdList) {
		this.schemeIdList = schemeIdList;
	}
	
	public String getFundName() {
		return fundName;
	}
	public void setFundName(String fundName) {
		this.fundName = fundName;
	}
	public String getStatus() {
		return status;
	}
	public void setStatus(String status) {
		this.status = status;
	}
	public String getSchemeCode() {
		return schemeCode;
	}
	public void setSchemeCode(String schemeCode) {
		this.schemeCode = schemeCode;
	}

}
