package com.moptra.go4wealth.uma.common.enums;

public enum GoForWealthErrorMessageEnum {

	
	ACCESS_DENIED_MESSAGE("Access denied"), 
	ACCESS_DENIED_CODE("403"),
	
	ADMIN_ACCESS_DENIED_CODE("400"),
	ADMIN_ACCESS_DENIED_MESSAGE("Admin Access denied."),
	
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
	INTERNAL_SERVER_ERROR("Internal server error"), 
	MOBILE_NO_ALREADY_REGISTERD("Mobile number or email already registered."),
	MOBILE_NO_ALREADY_USED("Mobile number already used by other person"),
	INVALID_OTP("Invalid Otp"), 
	OTP_EXPIRED("Otp has been expired."),
	MOBILE_NO_ALREADY_EXIST_NOT_VERIFIED("Mobile number already registered and please verify your mobile number"), 
	EMAIL_ID_ALREADY_EXIST("Email Id already exist."), 
	EMAIL_ID_ALREADY_EXIST_NOT_VERIFIED("Email Id already exist and please verify your account"), 
	EMAIL_ID_ALREADY_REGISTERD("Email Id Already Registerd");
	

	private String value;

	private GoForWealthErrorMessageEnum(String value) {
		this.value = value;
	}

	public String getValue() {
		return value;
	}
}
