package com.moptra.go4wealth.prs.orderapi.request;

import java.math.BigDecimal;

public class AddToCartRequestDTO {

	private Integer schemeId;
	private BigDecimal amount;
	
	
	public Integer getSchemeId() {
		return schemeId;
	}
	public void setSchemeId(Integer schemeId) {
		this.schemeId = schemeId;
	}
	public BigDecimal getAmount() {
		return amount;
	}
	public void setAmount(BigDecimal amount) {
		this.amount = amount;
	}
	
	
}
