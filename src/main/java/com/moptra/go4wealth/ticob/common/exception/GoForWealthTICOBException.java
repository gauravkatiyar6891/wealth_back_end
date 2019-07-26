package com.moptra.go4wealth.ticob.common.exception;

@SuppressWarnings("serial")
public class GoForWealthTICOBException extends Exception {

	String errorCode = null;
	String errorMessage = null;

	public GoForWealthTICOBException(String message) {
		super(message);
		this.errorMessage = message;
	}

	public GoForWealthTICOBException(String errorCode, String message) {
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
