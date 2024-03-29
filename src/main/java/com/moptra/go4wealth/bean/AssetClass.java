package com.moptra.go4wealth.bean;
// Generated May 15, 2018 4:33:52 PM by Hibernate Tools 5.1.0.Alpha1

import java.math.BigDecimal;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import static javax.persistence.GenerationType.IDENTITY;
import javax.persistence.Id;
import javax.persistence.Table;

/**
 * AssetClass generated by hbm2java
 */
@Entity
@Table(name = "asset_class", catalog = "go4wealthdb")
public class AssetClass implements java.io.Serializable {

	private Integer assetClassId;
	private String assetClass;
	private BigDecimal roiExptd;
	private BigDecimal inflAdjustedReturn;

	public AssetClass() {
	}

	public AssetClass(String assetClass) {
		this.assetClass = assetClass;
	}

	public AssetClass(String assetClass, BigDecimal roiExptd, BigDecimal inflAdjustedReturn) {
		this.assetClass = assetClass;
		this.roiExptd = roiExptd;
		this.inflAdjustedReturn = inflAdjustedReturn;
	}

	@Id
	@GeneratedValue(strategy = IDENTITY)

	@Column(name = "ASSET_CLASS_ID", unique = true, nullable = false)
	public Integer getAssetClassId() {
		return this.assetClassId;
	}

	public void setAssetClassId(Integer assetClassId) {
		this.assetClassId = assetClassId;
	}

	@Column(name = "ASSET_CLASS", nullable = false, length = 126)
	public String getAssetClass() {
		return this.assetClass;
	}

	public void setAssetClass(String assetClass) {
		this.assetClass = assetClass;
	}

	@Column(name = "ROI_EXPTD", precision = 10, scale = 5)
	public BigDecimal getRoiExptd() {
		return this.roiExptd;
	}

	public void setRoiExptd(BigDecimal roiExptd) {
		this.roiExptd = roiExptd;
	}

	@Column(name = "INFL_ADJUSTED_RETURN", precision = 10, scale = 5)
	public BigDecimal getInflAdjustedReturn() {
		return this.inflAdjustedReturn;
	}

	public void setInflAdjustedReturn(BigDecimal inflAdjustedReturn) {
		this.inflAdjustedReturn = inflAdjustedReturn;
	}

}
