package com.moptra.go4wealth.admin.model;

public class TestimonialResponseDTO {

	private String name;
	private String designation;
	private String companyName;
	private String imageUrl;
	private String comments;
	private int testimonialId;
	
	public int getTestimonialId() {
		return testimonialId;
	}
	
	public void setTestimonialId(int testimonialId) {
		this.testimonialId = testimonialId;
	}
	
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getDesignation() {
		return designation;
	}
	public void setDesignation(String designation) {
		this.designation = designation;
	}
	public String getCompanyName() {
		return companyName;
	}
	public void setCompanyName(String companyName) {
		this.companyName = companyName;
	}
	public String getImageUrl() {
		return imageUrl;
	}
	public void setImageUrl(String imageUrl) {
		this.imageUrl = imageUrl;
	}
	public String getComments() {
		return comments;
	}
	public void setComments(String comments) {
		this.comments = comments;
	}
	
	
}
