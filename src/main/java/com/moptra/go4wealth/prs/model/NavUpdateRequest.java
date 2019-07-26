package com.moptra.go4wealth.prs.model;

import java.util.List;

public class NavUpdateRequest {
	
	private String date;
	private List<SchemeForNav> schemes;
	
	public List<SchemeForNav> getSchemes() {
		return schemes;
	}
	public void setSchemes(List<SchemeForNav> schemes) {
		this.schemes = schemes;
	}
	
	public String getDate() {
		return date;
	}
	public void setDate(String date) {
		this.date = date;
	}
}