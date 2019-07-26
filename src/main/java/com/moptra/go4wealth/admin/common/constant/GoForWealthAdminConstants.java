package com.moptra.go4wealth.admin.common.constant;

public interface GoForWealthAdminConstants {
    
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
	public static final String FORWARD_SLASH = "FORWARD_SLASH";
	public static final String DOUBLE_BACKWARD_SLASH = "DOUBLE_BACKWARD_SLASH";
	public static final String SINGLE_QUOTE = "'";
	public static final String BLANK = "";
	public static final String COMMA_SEPARATOR = ",";
	public static final String QUESTION_MARK = "?";
	public static final String HASH = "#";
	public static final String AMPERSAND_OPERATOR = "&";
	public static final String EQUAL_TO_OPERATOR = "=";
	public static final String NOT_AVAILABLE = "N/A";
	
	public static final String MAIL_SEND_FAILURE_MESSAGE = "Something went wrong while sending mail.";
	public static final String MAIL_SEND_SUCCESS_MESSAGE = "Mail sent which you will need to open to continue. You may need to check your spam folder.";
	public static final String OTP_SEND_SUCCESS_MESSAGE = "OTP has been sent on your mobile number.";
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
	public static final String OVER_ALL_ONBORADING_STATUS_DONE = "Done";
	public static final String OVER_ALL_ONBORADING_STATUS_NOTDONE = "Not Done";
	public static final String STATUS_DONE = "Done";
	public static final String STATUS_NOT_DONE = "Not Done";
	
	public static final String HOMEVIDEO_LOCATION = "HOMEVIDEO_LOCATION";
	public static final String HOMEVIDEO_NAME = "homevideo";
	public static final String HOMEVIDEO_URL = "HOMEVIDEO_URL";
	public static final String IMAGE_LOCATION = "IMAGE_LOCATION";
	public static final String REGISTERED_USER_EXCEL_REPORT_LOCATION = "REGISTERED_USER_EXCEL_REPORT_LOCATION";
	public static final String REGISTERED_USER_REPORT_FILE_PREFIX="report_";
	public static final String EMAIL_TO_SEND_USER_REPORT = "EMAIL_TO_SEND_USER_REPORT";
}
