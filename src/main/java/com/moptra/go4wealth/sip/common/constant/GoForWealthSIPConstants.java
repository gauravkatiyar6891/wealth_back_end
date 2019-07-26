package com.moptra.go4wealth.sip.common.constant;

public interface GoForWealthSIPConstants {
    
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
	public static final String MAIL_SEND_SUCCESS_MESSAGE = "Mail sent which you will need to open to continue. You may need to check your spam folder.";
	public static final String OTP_SEND_SUCCESS_MESSAGE = "OTP has been sent on your mobile number.";
	public static final String MOBILE_NUMBER_VERIFIED_SUCCESS_MESSAGE = "Mobile number verified successfully.";
	public static final String OTP_VERIFIED_SUCCESS_MESSAGE = "OTP verified successfully";
	public static final String AGENT_REGISTER_SECCESSFULLY = "Agent Registerd successfully.";
	public static final String PASSWORD_CHANGED_SUCCESSFULLY = "Password Has Changed Successfully.";
	
	public static final String ADMIN_ROLE_NAME = "ADMIN_ROLE";
	public static final String AGENT_ROLE_NAME = "AGENT_ROLE";
	public static final String MANAGER_ROLE_NAME = "MANAGER_ROLE";
	public static final String USER_ROLE_NAME = "USER_ROLE";
	public static final String SIP_HEADER_NAME = "sip_session_token";
	public static final long SIP_TOKEN_EXPIRATION_TIME = 1440;
	public static final char[] CHARS = {'A', 'B', 'C', 'D', 'E', 'F', 'G', 'H', 'I', 'J', 'K', 'L',
			'M', 'N', 'O', 'P', 'Q', 'R', 'S', 'T', 'U', 'V', 'W', 'X', 'Y', 'Z', '0', '1', '2', '3',
			'4', '5', '6', '7', '8', '9'};

	public static final char[] NUMBERS = { '0', '1', '2', '3','4', '5', '6', '7', '8', '9'};
	
	public static final String DEFAULT_RISK_PROFILE="HIGH_HIGH";
	public static final String DEFAULT_FUND_TYPE="M+S";
	public static final String GUEST_USER = "GUEST_USER";
	public static final String DOWNLOAD_REPORTURL_SESSION_OUT="Your session is out, please send request again!";
	
	/** Extra parameter of retirement calculation **/
	public static final Integer AVERAGE_AGE_AFTER_RETIRE = 80;
	public static final Integer AGE_TO_RETIRE = 60;
	public static final Double RETIREMENT_ROI = 8.0;
	public static final Double REDUCTION_IN_EXPENSES_POST_RETIREMENT=0.3;

}
