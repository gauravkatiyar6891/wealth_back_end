package com.moptra.go4wealth.admin.common.enums;

public enum GoForWealthAdminErrorMessageEnum {

	SUCCESS_CODE("200"),
	SUCCESS_MESSAGE("Success"),

	PARTIAL_SUCCESS_CODE("250"),
	PARTIAL_SUCCESS_MESSAGE("Partial Success"),

	FAILURE_CODE("404"),
	FAILURE_MESSAGE("Failure"),
	
	UNAUTHORIZED_CODE("401"),
	UNAUTHORIZED_MESSAGE("Unauthorized User"),

	SESSION_EXPIRED_CODE("420"),
	SESSION_EXPIRED_MESSAGE("Session Expired."),

	ONBOARDING_DATA_NOT_FOUND_CODE("503"),
	ONBOARDING_DATA_NOT_FOUND_MESSAGE("Onboarding Data Not Found."),
	
	KYC_DATA_NOT_FOUND_MESSAGE("Kyc Details Not Found Please Fill The Kyc Details ."),
	 BANK_DATA_NOT_FOUND_MESSAGE("Bank Details Not Found Please Fill The Bank Details ."),
	 ADDRESS_DATA_NOT_FOUND_MESSAGE("Address Proof Details Not Found Please Fill The Address Details ."),
	 PAN_DATA_NOT_FOUND_MESSAGE("Pan Details Not Found Please Fill The Pan Details ."),
	 AADHAR_IMAGE_NOT_FOUND_MESSAGE("Aadhar Image Not Found Please Upload Aadhar Image"),
	 USER_IMAGE_NOT_FOUND_MESSAGE("User Image Not Found Please Upload User Image"),
	 PAN_IMAGE_NOT_FOUND_MESSAGE("Pan Card Image Not Found Please Upload Pan Card Image"),
	 USER_SIGNATURE_NOT_FOUND_MESSAGE("User Signature Not Found Please Upload User Signature"),
	USER_MANDATE_COMPLETED_SUCCESSFULLY("User Mandate Completed Successfully"),
	DATA_NOT_SAVED("503"),
	DATA_NOT_SAVED_MESSAGE("Data Not Saved"),
	DATA_NOT_FOUND_CODE("503"),
	DATA_NOT_FOUND_MESSAGE("Data Not Found."),
	USER_DETAIL_NOT_EXIST("User Does Not Exist!"),

	ENCRYPTION_ERROR_CODE("545"), 
	ENCRYPTION_ERROR_MESSAGE("Can not encrypt the challenge."),
	
	INVALID_HEADER_CODE("505"),
	INVALID_HEADER_MESSAGE("Invalid Header."),
	
	ACCESS_DENIED_CODE("403"),
	ACCESS_DENIED_MESSAGE("Access Denied"), 
	
	COMMON_ERROR_CODE("500"),
	INVALID_REQ_PAYLOAD_MESSAGE("Invalid request payload."),
	INTERNAL_SERVER_ERROR("Internal server error"), 
	MOBILE_NO_ALREADY_REGISTERD("Mobile number already registered."),
	INVALID_OTP("Invalid Otp"), 
	OTP_EXPIRED("Otp has been expired."),
	MOBILE_NO_ALREADY_EXIST_NOT_VERIFIED("Mobile number already registered and please verify your mobile number");
	
	
	private String value;

	private GoForWealthAdminErrorMessageEnum(String value) {
		this.value = value;
	}

	public String getValue() {
		return value;
	}
}
