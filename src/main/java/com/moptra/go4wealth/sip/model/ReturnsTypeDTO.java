package com.moptra.go4wealth.sip.model;

import java.util.List;

public class ReturnsTypeDTO {

	private Integer returnsTypeId;
	private String returnsType;
	private String riskProfile;
	private String riskProfileMsg;
	private String generalSipMessageAfterRisk;
	private List<SuggestSchemeDTO> suggestSchemeList;

	public ReturnsTypeDTO() {
		super();
	}

	public ReturnsTypeDTO(Integer returnsTypeId, String returnsType) {
		this.returnsTypeId = returnsTypeId;
		this.returnsType = returnsType;
	}

	public ReturnsTypeDTO(String returnsType, String riskProfile) {
		super();
		this.returnsType = returnsType + "_" + riskProfile;
		this.riskProfile = returnsType + " " + riskProfile;
	}

	public Integer getReturnsTypeId() {
		return returnsTypeId;
	}

	public void setReturnsTypeId(Integer returnsTypeId) {
		this.returnsTypeId = returnsTypeId;
	}

	public String getReturnsType() {
		return returnsType;
	}

	public void setReturnsType(String returnsType) {
		this.returnsType = returnsType;
	}

	public String getRiskProfile() {
		return riskProfile;
	}

	public void setRiskProfile(String riskProfile) {
		this.riskProfile = riskProfile;
	}

	public String getRiskProfileMsg() {
		return riskProfileMsg;
	}

	public void setRiskProfileMsg(String riskProfileMsg) {
		this.riskProfileMsg = riskProfileMsg;
	}

	public String getGeneralSipMessageAfterRisk() {
		return generalSipMessageAfterRisk;
	}

	public void setGeneralSipMessageAfterRisk(String generalSipMessageAfterRisk) {
		this.generalSipMessageAfterRisk = generalSipMessageAfterRisk;
	}

	public List<SuggestSchemeDTO> getSuggestSchemeList() {
		return suggestSchemeList;
	}

	public void setSuggestSchemeList(List<SuggestSchemeDTO> suggestSchemeList) {
		this.suggestSchemeList = suggestSchemeList;
	}

}
