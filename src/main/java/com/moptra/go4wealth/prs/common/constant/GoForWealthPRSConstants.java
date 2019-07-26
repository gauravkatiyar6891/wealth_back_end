package com.moptra.go4wealth.prs.common.constant;

public interface GoForWealthPRSConstants {
    
	// User Details Encryption Data 
	final public static String PBKDF2_ALGORITHM = "PBKDF2WithHmacSHA1";
	final public static int SALT_BYTES = 24;
	final public static int HASH_BYTES = 24;
	final public static int PBKDF2_ITERATIONS = 1000;
	final public static int ITERATION_INDEX = 0;
	final public static int SALT_INDEX = 1;
	final public static int PBKDF2_INDEX = 2;
	final public static String PRIVATE_KEY = "GO4WEALTHKEYSECRATE00000";
	
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
	public static final String NEW = "NEW";
	
	
	public static final String No_Token = "No Token";
	public static final String WRONG_TOKEN = "Wrong Token";
	public static final String USER_NOT_EXIST = "User does not exist";
	public static final String PAN_NUMBER_VERIFIED = "verified";
	public static final String PAN_NUMBER_NOT_VERIFIED = "not-verified";
	public static final String PAN_NUMBER_NOT_AVAILABLE = "not available";
	public static final String KYC_CHECK_FAILED = "KYC check failed. Please try again.";
	public static final String AADHAAR_NUMBER_VERIFIED = "verified";
	//public static final String PAN_NUMBER_NOT_VERIFIED = "You do not have your KYC yet. Nothing to worry, let us proceed ahead and get it done";
	public static final String SUCCESS = "success";
	public static final String ACCESS_DENIED = "Access Denied";
	
	
	
	public static final String SIGNATURE_FILE_PREFIX = "signature_";
	public static final String PANCARD_FILE_PREFIX = "pancard_";
	public static final String USER_IMAGE_FILE_PREFIX = "userimage_";

	public static final String ADHARCARD_FRONT_FILE_PREFIX = "adhar_front_";
	public static final String ADHARCARD_BACK_FILE_PREFIX = "adhar_back_";
	public static final String DUMMY_IMAGE_PATH = "DUMMY_IMAGE_PATH";
	public static final String ADDRESS_PROOF_ALREADY_EXIST = "Address proof number already exist.";
	public static final String ACCOUNT_NO_ALREADY_EXIST = "Account No already register.";
	public static final String ORDER_ADDED = "Order_Added_Successfully";
	public static final String USER_ID = "1887102";
	public static final String MEMBER_ID = "18871";
	public static final String PASSWORD = "123456!";
	
	public static final String IMAGE_LOCATION = "IMAGE_LOCATION";
	public static final String PDF_LOCATION = "PDF_LOCATION";
	public static final String MANDATE_PDF_LOCATION = "MANDATE_PDF_LOCATION";
	public static final String MANDATE_TIFF_LOCATION = "MANDATE_TIFF_LOCATION";
	public static final String TIFF_LOCATION = "TIFF_LOCATION";
	public static final String USERIMAGE_LOCATION = "USERIMAGE_LOCATION";
	public static final String PAN_LOCATION = "PAN_LOCATION";
	public static final String SIGNATURE_LOCATION = "SIGNATURE_LOCATION";
	public static final String ADHARCARD_LOCATION = "ADHARCARD_LOCATION";
	public static final String DUMMYIMAGE_LOCATION = "DUMMYIMAGE_LOCATION";
	public static final String PAYMENT_RESPONSE_HTML_LOCATION = "PAYMENT_RESPONSE_HTML_LOCATION";
	public static final String ALLOTEMENT_TIME = "ALLOTEMENT_TIME";
	public static final String REDUMPTION_TIME = "REDUMPTION_TIME";
	
	public static final String ORDER_CANCEL_STATUS = "X";
	public static final String INVALID_ORDER_STATUS = "IO";
	public static final String ORDER_DEACTIVATED_STATUS = "OD";
	public static final String ORDER_SWITCH_STATUS = "SW";
	public static final String ORDER_PENDING_STATUS = "P";
	public static final String ORDER_CONFIRM_STATUS = "M";
	public static final String ORDER_COMPLETE_STATUS = "C";
	public static final String ORDER_PAYMENT_AWITING = "PA";
	public static final String ORDER_REDEEM_STATUS = "R";
	public static final String PARTIAL_ORDER_REDEEM_STATUS = "PR";
	
	public static final String MODEL_PORTFOLIO_ORDER = "MPO";
	public static final String NAME_NOT_MATCH = "Name not as per PAN Card.";
	public static final String AMOUNT_LIMIT_WITHDRAWAL_FROM_BANK="AMOUNT_LIMIT_WITHDRAWAL_FROM_BANK";
	public static final String AMOUNT_LIMIT_WITHDRAWAL_FROM_BANK_IN_WORLD="AMOUNT_LIMIT_WITHDRAWAL_FROM_BANK_IN_WORLD";
	
	public static final String PAYMENT_REDIRECT_URL="PAYMENT_REDIRECT_URL";
	public static final String PAYMENT_API_URL="PAYMENT_API_URL";
	public static final String PAYMENT_PASSWORD_API_URL="PAYMENT_PASSWORD_API_URL";
	public static final String CHILD_ORDER_PASSWORD_URL="CHILD_ORDER_PASSWORD_URL";
	public static final String CHILD_ORDER_API_URL="CHILD_ORDER_API_URL";
	public static final String AOF_GET_PASSWORD_URL="AOF_GET_PASSWORD_URL";
	public static final String AOF_UPLOAD_URL = "AOF_UPLOAD_URL";
	public static final String MANDATE_UPLOAD_URL = "MANDATE_UPLOAD_URL";
	
	
	public static final String ORDER_STATUS_API_URL="ORDER_STATUS_API_URL";
	public static final String ALLOTMENT_STATEMENT_API_URL="ALLOTMENT_STATEMENT_API_URL";
	public static final String GET_TOKEN_ACCESS_API_URL="GET_TOKEN_ACCESS_API_URL";
	public static final String MANDATE_DETAIL_API_URL = "MANDATE_DETAIL_API_URL";
	public static final String MANDATE_AUTHENTICATION_API_URL = "MANDATE_AUTHENTICATION_API_URL";
	public static final String REDEMPTION_STATUS_API_URL = "REDEMPTION_STATUS_API_URL";
	public static final String CHANGE_PASSWORD_VALUE_NOT = "CHANGE_PASSWORD_VALUE_NOT";
	public static final String CHANGE_PASSWORD_VALUE_AT = "CHANGE_PASSWORD_VALUE_AT";
	public static final String NAV_DATA_SHEET_LOCATION = "NAV_DATA_SHEET_LOCATION";
	public static final String SET_HOUR_FOR_UPDATE_NATCH_AND_BILLER_STATUS = "SET_HOUR_FOR_UPDATE_NATCH_AND_BILLER_STATUS";
	public static final String SET_MINUTS_FOR_UPDATE_NATCH_AND_BILLER_STATUS = "SET_MINUTS_FOR_UPDATE_NATCH_AND_BILLER_STATUS";
	public static final String NODE_API_FOR_MAIL_REPORT = "NODE_API_FOR_MAIL_REPORT";
	public static final String PREVIOUD_DATE_FOR_SIP_INSTALLMENT_UPDATE = "PREVIOUD_DATE_FOR_SIP_INSTALLMENT_UPDATE";
	public static final String MAXIMUM_INSTALLMENT_FOR_SIP = "MAXIMUM_INSTALLMENT_FOR_SIP";
	public static final String NO_OF_ITERATION_FOR_PREVIOUS_SIP_INSTALLMENT = "NO_OF_ITERATION_FOR_PREVIOUS_SIP_INSTALLMENT";
	public static final String NODE_API_FOR_UPDATE_NAV = "NODE_API_FOR_UPDATE_NAV";
	public static final String NODE_API_TO_UPDATE_NAV_TO_NODE="NODE_API_TO_UPDATE_NAV_TO_NODE";

}