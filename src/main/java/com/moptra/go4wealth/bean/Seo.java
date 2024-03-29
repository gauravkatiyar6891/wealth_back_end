package com.moptra.go4wealth.bean;
// Generated 24 May, 2018 4:59:05 PM by Hibernate Tools 5.1.0.Alpha1

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import static javax.persistence.GenerationType.IDENTITY;
import javax.persistence.Id;
import javax.persistence.Table;

/**
 * Sco generated by hbm2java
 */
@Entity
@Table(name = "seo", catalog = "go4wealthdb")
public class Seo implements java.io.Serializable {

	private Integer seoId;
	private String pageName;
	private String siteUrl;
	private String metaDescription;
	private String metaKeywords;
	private String title;

	public Seo() {
	}

	public Seo(String pageName) {
		this.pageName = pageName;
	}

	public Seo(String pageName, String siteUrl, String metaDescription, String metaKeywords, String title) {
		this.pageName = pageName;
		this.siteUrl = siteUrl;
		this.metaDescription = metaDescription;
		this.metaKeywords = metaKeywords;
		this.title = title;
	}

	@Id
	@GeneratedValue(strategy = IDENTITY)

	@Column(name = "SEO_ID", unique = true, nullable = false)
	public Integer getSeoId() {
		return this.seoId;
	}

	public void setSeoId(Integer seoId) {
		this.seoId = seoId;
	}

	@Column(name = "PAGE_NAME", nullable = false, length = 50)
	public String getPageName() {
		return this.pageName;
	}

	public void setPageName(String pageName) {
		this.pageName = pageName;
	}

	@Column(name = "SITE_URL", length = 60)
	public String getSiteUrl() {
		return this.siteUrl;
	}

	public void setSiteUrl(String siteUrl) {
		this.siteUrl = siteUrl;
	}

	@Column(name = "META_DESCRIPTION", length = 190)
	public String getMetaDescription() {
		return this.metaDescription;
	}

	public void setMetaDescription(String metaDescription) {
		this.metaDescription = metaDescription;
	}

	@Column(name = "META_KEYWORDS", length = 190)
	public String getMetaKeywords() {
		return this.metaKeywords;
	}

	public void setMetaKeywords(String metaKeywords) {
		this.metaKeywords = metaKeywords;
	}

	@Column(name = "TITLE", length = 190)
	public String getTitle() {
		return this.title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

}
