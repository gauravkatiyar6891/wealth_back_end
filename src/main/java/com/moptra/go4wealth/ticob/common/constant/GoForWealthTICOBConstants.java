package com.moptra.go4wealth.ticob.common.constant;

public interface GoForWealthTICOBConstants {

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
	
	//Response Messages
	public static final String SUCCESS_MESSAGE = "success";
	public static final String DATA_NOT_SAVED = "No Fresh Record Inserted.";

}
