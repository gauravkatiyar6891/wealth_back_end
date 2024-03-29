package com.moptra.go4wealth.bean;
// Generated 14 Aug, 2018 1:19:07 PM by Hibernate Tools 5.1.0.Alpha1

import static javax.persistence.GenerationType.IDENTITY;

import java.math.BigDecimal;
import javax.persistence.AttributeOverride;
import javax.persistence.AttributeOverrides;
import javax.persistence.Column;
import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

/**
 * UserAssetItems generated by hbm2java
 */
@Entity
@Table(name = "user_asset_items", catalog = "go4wealthdb")
public class UserAssetItems implements java.io.Serializable {

	private Integer userAssetItemId;
	private AssetClass assetClass;
	private GoalsOrder goalsOrder;
	private BigDecimal assetValue;
	private BigDecimal futureValue;
	private Integer goalsOrderItemId;
	
	public UserAssetItems() {
	}

	public UserAssetItems(Integer userAssetItemId, AssetClass assetClass, GoalsOrder goalsOrder,
			BigDecimal assetValue,Integer goalsOrderItemId) {
		this.userAssetItemId = userAssetItemId;
		this.assetClass = assetClass;
		this.goalsOrder = goalsOrder;
		this.assetValue = assetValue;
		this.goalsOrderItemId = goalsOrderItemId;
	}

	@Id
	@GeneratedValue(strategy = IDENTITY)
	@Column(name = "user_asset_item_id", unique = true, nullable = false)
	public Integer getUserAssetItemId() {
		return userAssetItemId;
	}

	public void setUserAssetItemId(Integer userAssetItemId) {
		this.userAssetItemId = userAssetItemId;
	}

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "ASSET_CLASS_ID")
	public AssetClass getAssetClass() {
		return this.assetClass;
	}

	public void setAssetClass(AssetClass assetClass) {
		this.assetClass = assetClass;
	}

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "GOALS_ORDER_ID")
	public GoalsOrder getGoalsOrder() {
		return this.goalsOrder;
	}

	public void setGoalsOrder(GoalsOrder goalsOrder) {
		this.goalsOrder = goalsOrder;
	}

	@Column(name = "ASSET_VALUE", precision = 20, scale = 5)
	public BigDecimal getAssetValue() {
		return this.assetValue;
	}

	public void setAssetValue(BigDecimal assetValue) {
		this.assetValue = assetValue;
	}

	@Column(name = "FUTURE_VALUE", precision = 20, scale = 5)
	public BigDecimal getFutureValue() {
		return futureValue;
	}

	public void setFutureValue(BigDecimal futureValue) {
		this.futureValue = futureValue;
	}

	@Column(name = "GOALS_ORDER_ITEM_ID")
	public Integer getGoalsOrderItemId() {
		return goalsOrderItemId;
	}

	public void setGoalsOrderItemId(Integer goalsOrderItemId) {
		this.goalsOrderItemId = goalsOrderItemId;
	}


}
