package com.moptra.go4wealth.sip.common.enums;

public enum GoForWealthSIPErrorMessageEnum {

	SUCCESS_CODE("200"),
	SUCCESS_MESSAGE("Success"),

	PARTIAL_SUCCESS_CODE("250"),
	PARTIAL_SUCCESS_MESSAGE("Partial Success"),

	FAILURE_CODE("404"),
	FAILURE_MESSAGE("Failure"),

	SESSION_EXPIRED_CODE("420"),
	SESSION_EXPIRED_MESSAGE("Session Expired."),

	DATA_NOT_FOUND_CODE("503"),
	DATA_NOT_FOUND_MESSAGE("Data Not Found."),
	USER_DETAIL_NOT_EXIST("User Does Not Exist!"),

	ENCRYPTION_ERROR_CODE("545"), 
	ENCRYPTION_ERROR_MESSAGE("Can not encrypt the challenge."),

	INVALID_HEADER_CODE("505"),
	INVALID_HEADER_MESSAGE("Invalid Header."),

	COMMON_ERROR_CODE("500"),
	INVALID_REQ_PAYLOAD_MESSAGE("Invalid request payload."),
	INTERNAL_SERVER_ERROR("Something went wrong!!!"), 
	MOBILE_NO_ALREADY_REGISTERD("Mobile number already registered."),
	INVALID_OTP("Invalid Otp"), 
	OTP_EXPIRED("Otp has been expired."), 
	ACCESS_DENIED_CODE("403"),
	FORMAT_NOT_SUPPORTED("Given Format Is Not Supported."),
	ACCESS_DENIED_MESSAGE("Access Denied"), 
	INVALID_GOAL_BUCKET("Invalid Goal Bucket");
	

	private String value;

	private GoForWealthSIPErrorMessageEnum(String value) {
		this.value = value;
	}

	public String getValue() {
		return value;
	}
}
