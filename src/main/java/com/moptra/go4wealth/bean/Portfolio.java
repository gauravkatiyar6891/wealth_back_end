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
@Table(name = "portfolio", catalog = "go4wealthdb")
public class Portfolio {
	
	private Integer portfolioId;
	private Integer portfolioCategoryId;
	private String schemeCode;
	private String schemeName;
	private String status;
	private Date updateDate;
	
	public Portfolio() {
	}
	
	public Portfolio(Integer portfolioCategoryId,String schemeCode, String schemeName,String status,Date updateDate) {
		this.portfolioCategoryId = portfolioCategoryId;
		this.schemeCode = schemeCode;
		this.schemeName = schemeName;
		this.status = status;
		this.updateDate = updateDate;
	}

	@Id
	@GeneratedValue(strategy = IDENTITY)
	@Column(name = "PORTFOLIO_ID", unique = true, nullable = false)
	public Integer getPortfolioId() {
		return portfolioId;
	}

	public void setPortfolioId(Integer portfolioId) {
		this.portfolioId = portfolioId;
	}

	@Column(name = "PORTFOLIO_CATEGORY_ID", length = 50)
	public Integer getPortfolioCategoryId() {
		return portfolioCategoryId;
	}

	public void setPortfolioCategoryId(Integer portfolioCategoryId) {
		this.portfolioCategoryId = portfolioCategoryId;
	}

	@Column(name = "SCHEME_CODE", length = 512)
	public String getSchemeCode() {
		return schemeCode;
	}

	public void setSchemeCode(String schemeCode) {
		this.schemeCode = schemeCode;
	}

	@Column(name = "SCHEME_NAME", length = 512)
	public String getSchemeName() {
		return schemeName;
	}

	public void setSchemeName(String schemeName) {
		this.schemeName = schemeName;
	}

	@Column(name = "STATUS", length = 512)
	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
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
}