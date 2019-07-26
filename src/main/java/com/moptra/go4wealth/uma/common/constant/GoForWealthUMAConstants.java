package com.moptra.go4wealth.uma.common.constant;

public interface GoForWealthUMAConstants {
    
	// Account status constants
	public static final int ACCOUNT_ACTIVE = 1;
	public static final int ACCOUNT_PENDING = 0;
	public static final int ACCOUNT_DEACTIVETED= -1;

	//Numerical Constants
	public static final int ZERO = 0;
	public static final int NEGATIVE_ONE = -1;
	public static final int TEN = 10;
	public static final int ONE = 1;
	public static final int THOUSAND = 1000;

	//Spring Required Constants.
	public static final String CLASSPATH = "classpath:";

	//Extras
	public static final String UTC = "UTC";
	public static final String START_INDEX_KEY = "startIndex";
	public static final String END_INDEX_KEY = "endIndex";
	public static final String PROPERTIES_FILE_PATH_PREFIX_FOR_JUNIT = "./";
	public static final String DEFAULT_TIME_ZONE = "DefaultTimeZone";
	public static final String LOG_LEVEL = "logLevel";

	//Special Characters
	public static final String LEFT_PARENTHESIS ="(";
	public static final String RIGHT_PARENTHESIS =")";
	public static final String SPACE =" ";
	public static final String FORWARD_SLASH = "/";
	public static final String DOUBLE_BACKWARD_SLASH = "\\";
	public static final String SINGLE_QUOTE = "'";
	public static final String BLANK = "";
	public static final String COMMA_SEPARATOR = ",";
	public static final String QUESTION_MARK = "?";
	public static final String HASH = "#";
	public static final String AMPERSAND_OPERATOR = "&";
	public static final String EQUAL_TO_OPERATOR = "=";
	public static final String NOT_AVAILABLE = "N/A";
	
	public static final String MAIL_SEND_FAILURE_MESSAGE = "Something went wrong while sending mail.";
	public static final String MAIL_SEND_SUCCESS_MESSAGE = "Email has been sent to your inbox. Please check in your spam if email not received in inbox.";
	public static final String OTP_SEND_TO_EMAIL_SUCCESS_MESSAGE = "OTP has been sent on your email .";
	public static final String OTP_SEND_SUCCESS_MESSAGE = "OTP has been sent on your mobile number and email";
	public static final String MOBILE_NUMBER_VERIFIED_SUCCESS_MESSAGE = "Mobile number verified successfully.";
	public static final String OTP_VERIFIED_SUCCESS_MESSAGE = "OTP verified successfully";
	public static final String AGENT_REGISTER_SECCESSFULLY = "Agent Registerd successfully.";
	public static final String PASSWORD_CHANGED_SUCCESSFULLY = "Password Has Changed Successfully.";
	public static final String EMAIL_ID_VERIFIED_SUCCESS_MESSAGE = "EmailId verified successfully.";
	public static final String EMAIL_VERIFICATION_SESSION_OUT = "Your session is out, please send request again!";
	public static final String TOKEN_EXPIRED = "Token expired!";
	public static final String WRONG_TOKEN = "Wrong token!";
	public static final String USER_NOT_EXIST = "User does not exist!";
	public static final String SUCCESS = "success";
	public static final String FAILURE = "Failure";
	public static final String MOBILE_NUMBER_ALREADY_EXIST = "Mobile Number already exist.";
	
	
//	Mobile verification Code,
	public static final String MOBILE_VERIFICATION_EMAIL_SUBJECT = "Your OTP for verifying  Go4wealth registered Mobile Number ";
	public static final String FORGOT_PASSWORD_EMAIL_SUBJECT = "Your OTP for resetting Go4wealth Password";
	public static final String FORGOT_PASSWORD_EMAIL_MESSAGE = "Your One Time Password (OTP) for resetting the password for your Go4wealth.com profile is 83CQZ8 Please enter this code in the OTP code box listed on the page.";
	public static final String CHANGED_PASSWORD_EMAIL_SUBJECT = "Password Changed Successfully";
	public static final String FORGOT = "forgot";
	public static final String REGISTER = "register";
	public static final String ONBOARDING = "onboarding";
	
	
	
	public static final String FORGETPASSWORD_HEADER_NAME = "FORGETPASSWORD_HEADER_NAME";
	public static final long FORGETPASSWORD_TOKEN_EXPIRATION_TIME = 1440;
	

}
