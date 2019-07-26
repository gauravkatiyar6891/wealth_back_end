package com.moptra.go4wealth.ticob.common.enums;

public enum GoForWealthTICOBErrorMessageEnum {

	SUCCESS_CODE("200"),
	SUCCESS_MESSAGE("Success"),

	PARTIAL_SUCCESS_CODE("250"),
	PARTIAL_SUCCESS_MESSAGE("Partial Success"),
	
	NO_TOKEN("No Token"),
	UNAUTHORIZED_CODE("401"),
	UNAUTHORIZED_MESSAGE("Unauthorized User"),

	ACCESS_DENIED_MESSAGE("Access denied"),
	ACCESS_DENIED_CODE("403"),
	
	FAILURE_CODE("404"),
	FAILURE_MESSAGE("Failure"),

	SESSION_EXPIRED_CODE("420"),
	SESSION_EXPIRED_MESSAGE("Session Expired."),

	DATA_NOT_FOUND_CODE("503"),
	DATA_NOT_FOUND_MESSAGE("Data Not Found."),
	USER_DETAIL_NOT_EXIST("User Does Not Exist!"),
	DATA_NOT_SAVED("Data Not Saved"),

	ENCRYPTION_ERROR_CODE("545"), 
	ENCRYPTION_ERROR_MESSAGE("Can not encrypt the challenge."),

	INVALID_HEADER_CODE("505"),
	INVALID_HEADER_MESSAGE("Invalid Header."),

	COMMON_ERROR_CODE("500"),
	INVALID_REQ_PAYLOAD_MESSAGE("Invalid request payload."),
	INTERNAL_SERVER_ERROR("Internal server error"), 
	MOBILE_NO_ALREADY_REGISTERD("Mobile number already registered."),
	INVALID_OTP("Invalid Otp"), 
	OTP_EXPIRED("Otp has been expired."), 
	PAN_NUMBER_ALREADY_REGISTERED("Pan Number already registered."),
	CATEGORY_ALREADY_EXISTS_CODE("600");

	private String value;

	private GoForWealthTICOBErrorMessageEnum(String value) {
		this.value = value;
	}

	public String getValue() {
		return value;
	}

}
