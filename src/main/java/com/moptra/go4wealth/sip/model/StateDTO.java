package com.moptra.go4wealth.sip.model;

public class StateDTO {

	private Integer stateId;
	private String stateName;
	
	public StateDTO() {
		super();
	}
	
	public StateDTO(Integer stateId, String stateName) {
		this.stateId=stateId;
		this.stateName=stateName;
	}
	
	public Integer getStateId() {
		return stateId;
	}

	public void setStateId(Integer stateId) {
		this.stateId = stateId;
	}

	public String getStateName() {
		return stateName;
	}

	public void setStateName(String stateName) {
		this.stateName = stateName;
	}

	@Override
	public String toString() {
		return "CityDTO [stateId=" + stateId + ", stateName=" + stateName + "]";
	}
}