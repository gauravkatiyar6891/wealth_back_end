package com.moptra.go4wealth.uma.common.enums;

public enum MobileVerification {
	NOTVERIFIED(0),VERIFIED(1);

	private Integer status;

	private MobileVerification(Integer status) {
		this.status = status;
	}

	public Integer getStatus() {
		return status;
	}
}

