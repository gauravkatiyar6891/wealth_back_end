package com.moptra.go4wealth.sip.common.exception;

@SuppressWarnings("serial")
public class GoForWealthSIPException extends Exception {

	String errorCode = null;
	String errorMessage = null;

	public GoForWealthSIPException(String message) {
		super(message);
		this.errorMessage = message;
	}

	public GoForWealthSIPException(String errorCode, String message) {
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
