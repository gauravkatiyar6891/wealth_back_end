package com.moptra.go4wealth.bean;

import static javax.persistence.GenerationType.IDENTITY;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import org.hibernate.annotations.CreationTimestamp;


@Entity
@Table(name = "user_enquiry", catalog = "go4wealthdb")
public class UserEnquiry {

	private Integer userEnquiryId;
	private String name;
	private String email;
	private String mobile;
	private String query;
	private Date updateDate;
	private String status;
	
	public UserEnquiry() {
	}
	
	public UserEnquiry(String name, String email,String mobile, String query,Date updateDate,String status) {
		this.name = name;
		this.email = email;
		this.mobile = mobile;
		this.query = query;
		this.updateDate = updateDate;
		this.status = status;
	}
	
	@Id
	@GeneratedValue(strategy = IDENTITY)
	@Column(name = "USER_ENQUIRY_ID", unique = true, nullable = false)
	public Integer getUserEnquiryId() {
		return userEnquiryId;
	}

	public void setUserEnquiryId(Integer userEnquiryId) {
		this.userEnquiryId = userEnquiryId;
	}

	@Column(name = "NAME", length = 512)
	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	@Column(name = "EMAIL", length = 512)
	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	@Column(name = "MOBILE", length = 512)
	public String getMobile() {
		return mobile;
	}

	public void setMobile(String mobile) {
		this.mobile = mobile;
	}

	@Column(name = "QUERY", length = 2500)
	public String getQuery() {
		return query;
	}

	public void setQuery(String query) {
		this.query = query;
	}

	@CreationTimestamp
	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "UPDATE_DATE")
	public Date getUpdateDate() {
		return updateDate;
	}

	public void setUpdateDate(Date updateDate) {
		this.updateDate = updateDate;
	}

	@Column(name = "STATUS")
	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}	
}