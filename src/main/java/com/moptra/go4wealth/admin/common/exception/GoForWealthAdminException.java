package com.moptra.go4wealth.admin.common.exception;

@SuppressWarnings("serial")
public class GoForWealthAdminException extends Exception {

	String errorCode = null;
	String errorMessage = null;

	public GoForWealthAdminException(String message) {
		super(message);
		this.errorMessage = message;
	}

	public GoForWealthAdminException(String errorCode, String message) {
		super(message);
		this.errorCode = errorCode;
		this.errorMessage = message;
	}

	public String getErrorCode() {
		return errorCode;
	}

	public String getErrorMessage() {
		return errorMessage;
	}

}
