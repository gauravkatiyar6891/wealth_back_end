package com.moptra.go4wealth.admin.model;

public class CategoryListResponseDTO {
	private Integer assetClassInternalId;
	private String fundType;
	private String fundTypeCode;
	
	
	public Integer getAssetClassInternalId() {
		return assetClassInternalId;
	}
	public void setAssetClassInternalId(Integer assetClassInternalId) {
		this.assetClassInternalId = assetClassInternalId;
	}
	public String getFundType() {
		return fundType;
	}
	public void setFundType(String fundType) {
		this.fundType = fundType;
	}
	public String getFundTypeCode() {
		return fundTypeCode;
	}
	public void setFundTypeCode(String fundTypeCode) {
		this.fundTypeCode = fundTypeCode;
	}

	
}
