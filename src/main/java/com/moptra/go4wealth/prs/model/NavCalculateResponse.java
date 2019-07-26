package com.moptra.go4wealth.prs.model;

public class NavCalculateResponse {

	private double currentNav;
	private double worthAmount;
	private double interest;
	private String currentDate;
	private String message;
	private double oneYearinterest;
	private double threeYearinterest;
	private double fiveYearinterest;
	
	public double getOneYearinterest() {
		return oneYearinterest;
	}
	public void setOneYearinterest(double oneYearinterest) {
		this.oneYearinterest = oneYearinterest;
	}
	public double getThreeYearinterest() {
		return threeYearinterest;
	}
	public void setThreeYearinterest(double threeYearinterest) {
		this.threeYearinterest = threeYearinterest;
	}
	public String getMessage() {
		return message;
	}
	public void setMessage(String message) {
		this.message = message;
	}
	public String getCurrentDate() {
		return currentDate;
	}
	public void setCurrentDate(String currentDate) {
		this.currentDate = currentDate;
	}
	public double getCurrentNav() {
		return currentNav;
	}
	public void setCurrentNav(double currentNav) {
		this.currentNav = currentNav;
	}
	public double getWorthAmount() {
		return worthAmount;
	}
	public void setWorthAmount(double worthAmount) {
		this.worthAmount = worthAmount;
	}
	public double getInterest() {
		return interest;
	}
	public void setInterest(double interest) {
		this.interest = interest;
	}
	public double getFiveYearinterest() {
		return fiveYearinterest;
	}
	public void setFiveYearinterest(double fiveYearinterest) {
		this.fiveYearinterest = fiveYearinterest;
	}	
}
