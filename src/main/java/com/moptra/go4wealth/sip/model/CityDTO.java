package com.moptra.go4wealth.sip.model;

public class CityDTO {
	
	private Integer cityId;
	private String cityName;
	
	public CityDTO() {
		super();
	}
	
	public CityDTO(Integer cityId, String cityName) {
		this.cityId=cityId;
		this.cityName=cityName;
	}
	
	public Integer getCityId() {
		return cityId;
	}
	public void setCityId(Integer cityId) {
		this.cityId = cityId;
	}
	public String getCityName() {
		return cityName;
	}
	public void setCityName(String cityName) {
		this.cityName = cityName;
	}

	@Override
	public String toString() {
		return "CityDTO [cityId=" + cityId + ", cityName=" + cityName + "]";
	}
	
}
