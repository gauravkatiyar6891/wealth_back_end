package com.moptra.go4wealth.uma.common.exception;

@SuppressWarnings("serial")
public class GoForWealthUMAException extends Exception {

	String errorCode = null;
	String errorMessage = null;

	public GoForWealthUMAException(String message) {
		super(message);
		this.errorMessage = message;
	}

	public GoForWealthUMAException(String errorCode, String message) {
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
