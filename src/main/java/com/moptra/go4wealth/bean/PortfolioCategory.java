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
@Table(name = "portfolio_category", catalog = "go4wealthdb")
public class PortfolioCategory {

	private Integer portfolioCategoryId;
	private String categoryName;
	private String categoryKeyword;
	private Date updateDate;
	private String description;
	private String investmentGoals;
	private String features;
	
	public PortfolioCategory() {
	}
	
	public PortfolioCategory(String categoryName,Date updateDate) {
		this.categoryName = categoryName;
		this.updateDate = updateDate;
	}

	@Id
	@GeneratedValue(strategy = IDENTITY)
	@Column(name = "PORTFOLIO_CATEGORY_ID", unique = true, nullable = false)
	public Integer getPortfolioCategoryId() {
		return portfolioCategoryId;
	}

	public void setPortfolioCategoryId(Integer portfolioCategoryId) {
		this.portfolioCategoryId = portfolioCategoryId;
	}

	@Column(name = "CATEGORY_NAME", length = 512)
	public String getCategoryName() {
		return categoryName;
	}

	public void setCategoryName(String categoryName) {
		this.categoryName = categoryName;
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

	@Column(name="CATEGORY_KEYWORD", length=512)
	public String getCategoryKeyword() {
		return categoryKeyword;
	}

	public void setCategoryKeyword(String categoryKeyword) {
		this.categoryKeyword = categoryKeyword;
	}

	@Column(name="DESCRIPTION", length=5000)
	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	@Column(name="INVESTMENT_GOALS", length=5000)
	public String getInvestmentGoals() {
		return investmentGoals;
	}

	public void setInvestmentGoals(String investmentGoals) {
		this.investmentGoals = investmentGoals;
	}

	@Column(name="FEATURES", length=5000)
	public String getFeatures() {
		return features;
	}

	public void setFeatures(String features) {
		this.features = features;
	}

}
