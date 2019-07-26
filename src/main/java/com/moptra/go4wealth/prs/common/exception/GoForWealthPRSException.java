package com.moptra.go4wealth.prs.common.exception;

@SuppressWarnings("serial")
public class GoForWealthPRSException extends Exception {

	String errorCode = null;
	String errorMessage = null;

	public GoForWealthPRSException(String message) {
		super(message);
		this.errorMessage = message;
	}

	public GoForWealthPRSException(String errorCode, String message) {
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
