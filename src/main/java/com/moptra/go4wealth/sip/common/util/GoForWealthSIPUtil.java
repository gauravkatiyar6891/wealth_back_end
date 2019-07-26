package com.moptra.go4wealth.sip.common.util;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.sql.Timestamp;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Collection;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Properties;
import java.util.Random;
import java.util.Set;
import java.util.TimeZone;

import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import javax.servlet.http.HttpServletRequest;

import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.CellType;
import org.apache.poi.ss.usermodel.CreationHelper;
import org.apache.poi.ss.usermodel.FillPatternType;
import org.apache.poi.ss.usermodel.Font;
import org.apache.poi.ss.usermodel.IndexedColors;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.apache.tomcat.util.codec.binary.Base64;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpHeaders;

import com.fasterxml.jackson.annotation.JsonFilter;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectWriter;
import com.fasterxml.jackson.databind.ser.impl.SimpleBeanPropertyFilter;
import com.fasterxml.jackson.databind.ser.impl.SimpleFilterProvider;
import com.moptra.go4wealth.configuration.JwtConfiguration;
import com.moptra.go4wealth.security.UserSession;
import com.moptra.go4wealth.sip.common.constant.GoForWealthSIPConstants;
import com.moptra.go4wealth.sip.common.enums.GoForWealthSIPErrorMessageEnum;
import com.moptra.go4wealth.sip.common.exception.GoForWealthSIPException;
import com.moptra.go4wealth.sip.common.rest.GoForWealthSIPResponseInfo;
import com.moptra.go4wealth.sip.model.AgeSlabDTO;
import com.moptra.go4wealth.sip.model.CalculateSipRequestDto;
import com.moptra.go4wealth.sip.model.CalculateSipResponseDto;
import com.moptra.go4wealth.sip.model.GoalDTO;
import com.moptra.go4wealth.sip.model.IncomeDTO;
import com.moptra.go4wealth.sip.model.KidsSlabDTO;
import com.moptra.go4wealth.sip.model.MaritalDTO;

import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;


public class GoForWealthSIPUtil {

	private static Logger logger = LoggerFactory.getLogger(GoForWealthSIPUtil.class);

	private static boolean loadPropertiesFile(String fileName,Properties properties){
		InputStream inputStream = null;
		try {
			logger.info("In loadPropertiesFile()");
			// Load properties file
			inputStream = GoForWealthSIPUtil.class.getClassLoader().getResourceAsStream(fileName);
			if(GoForWealthStringUtil.isNull(inputStream)){
				logger.error("Could not read the property file : " + fileName);
				throw new FileNotFoundException();
			}
			else{
				properties.load(inputStream);
				logger.info("Properties file available : " + fileName);
				return true;
			}

		} catch (FileNotFoundException e) {
			logger.error("Properties file not found" + fileName, e);
			if(GoForWealthStringUtil.isNull(inputStream)){
				logger.info("Loading property file for junits using relative path.");
				try {
					inputStream = GoForWealthSIPUtil.class.getClassLoader().getResourceAsStream(GoForWealthSIPConstants.PROPERTIES_FILE_PATH_PREFIX_FOR_JUNIT
							+ fileName);
					if(GoForWealthStringUtil.isNull(inputStream)){
						logger.error("Could not read the property file : " + fileName);
						throw new FileNotFoundException();
					}
					else{
						properties.load(inputStream);
						logger.info("Properties file available : " + GoForWealthSIPConstants.PROPERTIES_FILE_PATH_PREFIX_FOR_JUNIT + fileName);
						return true;
					}
				} catch (FileNotFoundException e1) {
					logger.error("Properties file not found :"+ fileName, e);
				} catch (IOException e1) {
					logger.error("IO exception while loading Properties file :" +  fileName, e);
				}
			}

		} catch (IOException e) {
			logger.error("IO exception while loading Properties file" + fileName, e);
		} catch (Exception e) {
			logger.error("IO exception while loading Properties file" + fileName, e);
		} finally {
			try {
				if(!GoForWealthStringUtil.isNull(inputStream))
				{
					inputStream.close();
				}
			} catch (IOException e) {
				logger.error(
						"exception while closing input stream for Properties file" + fileName,
						e);

			}
		}
		return false;
	}
	
	public static GoForWealthSIPResponseInfo getSuccessResponseInfo(String status,String message, Object data) {
		GoForWealthSIPResponseInfo responseInfo = new GoForWealthSIPResponseInfo();
		responseInfo.setStatus(status);
		responseInfo.setMessage(message);
		if (!GoForWealthStringUtil.isNull(data))
			responseInfo.setData(data);
		return responseInfo;
	}

	public static GoForWealthSIPResponseInfo getErrorResponseInfo(String status, String message, Exception e) {
		logger.error("Exception occurred = ", e);
		GoForWealthSIPResponseInfo responseInfo = new GoForWealthSIPResponseInfo();
		responseInfo.setStatus(status);
		responseInfo.setMessage(message);
		responseInfo.setData(GoForWealthSIPConstants.BLANK);
		responseInfo.setTotalRecords(GoForWealthSIPConstants.ZERO);
		return responseInfo;
	}

	public static GoForWealthSIPResponseInfo getExceptionResponseInfo(GoForWealthSIPException e) {
		logger.error("Exception occurred = ", e);
		GoForWealthSIPResponseInfo responseInfo = new GoForWealthSIPResponseInfo();
		responseInfo.setStatus(e.getErrorCode());
		responseInfo.setMessage(e.getErrorMessage());
		responseInfo.setData(GoForWealthSIPConstants.BLANK);
		responseInfo.setTotalRecords(GoForWealthSIPConstants.ZERO);
		return responseInfo;
	}

	public static GoForWealthSIPResponseInfo getExceptionResponseInfo(GoForWealthSIPException e, Object data) {
		logger.error("Exception occurred = ", e);
		GoForWealthSIPResponseInfo responseInfo = new GoForWealthSIPResponseInfo();
		responseInfo.setStatus(e.getErrorCode());
		responseInfo.setMessage(e.getErrorMessage());
		responseInfo.setData(data);
		responseInfo.setTotalRecords(GoForWealthSIPConstants.ZERO);
		return responseInfo;
	}

	public static void logRequest(HttpServletRequest request, HttpHeaders headers, Object obj) {
		try {
			if (!GoForWealthStringUtil.isNull(request)) {
				logger.info("REST API Called: "
						+ request.getRequestURL()+GoForWealthSIPConstants.QUESTION_MARK+request.getQueryString() );

				logger.info("Request Headers Start:-");
				if (!GoForWealthStringUtil.isNull(headers)) {
					for (Entry<String, List<String>> entry : headers.entrySet()) {
						logger.info(entry.getKey() + ": "
								+ entry.getValue());
					}
				}
				logger.info("Request Headers End:-");
			}
			if(!GoForWealthStringUtil.isNull(obj))
			{
				String requestPayload = getJsonFromObject(obj, null);
				logger.info("REST Requet Payload: "
						+ requestPayload );
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public static String getJsonFromObject(Object obj, Set<String> propertiesToIgnore) throws JsonProcessingException {
		@JsonFilter("by name")
		abstract class IgnoreStringsMixIn{ }  
		String requestPayload=null;
		if(!GoForWealthStringUtil.isNull(obj))
		{
			ObjectMapper mapper=new ObjectMapper();
			if(!GoForWealthSIPUtil.isEmptyCollection(propertiesToIgnore))
			{
				mapper.addMixInAnnotations(Object.class,IgnoreStringsMixIn.class );
				mapper.setSerializationInclusion(Include.NON_NULL);
				ObjectWriter writer = mapper.writer(new SimpleFilterProvider().addFilter("by name", SimpleBeanPropertyFilter.serializeAllExcept(propertiesToIgnore)));
				requestPayload = writer.writeValueAsString(obj); 
			}
			else
			{
				requestPayload=mapper.writeValueAsString(obj);
			}
		}
		return requestPayload;
	}

	public static Object getObjectFromJson(String json, Class className)
			throws IOException {
		if (!GoForWealthStringUtil.isNull(className) && !GoForWealthStringUtil.isNull(json)) {
			ObjectMapper mapper = new ObjectMapper();
			Object object = mapper.readValue(json, className);
			return object;
		} else {
			return null;
		}
	}

	public static void logResponse(Object responseInfo,
			HttpHeaders headers) {
		try {
			//TODO Change in toString method of response object
			logger.info("Response Info Start: ");
			if (!GoForWealthStringUtil.isNull(responseInfo)) {

				if (!GoForWealthStringUtil.isNull(headers)) {
						for (Entry<String, List<String>> entry : headers.entrySet()) {
							logger.info("Request Param Name = "
									+ entry.getKey() + "Request Param Value = "
									+ entry.getValue());
					}
				}
				String responsePayload = getJsonFromObject(responseInfo, null);
				logger.info("Response Payload : "+responsePayload);
			}
			logger.info("Response Info End: ");
		} catch (Exception e) {
			e.printStackTrace();
		}
	}


	public static String getValueFromHeader(HttpHeaders headers,
			String headerKey) throws GoForWealthSIPException {
		logger.info("In getValueFromHeader()");
		List<String> headerValues = headers.get(headerKey);
		if (GoForWealthStringUtil.isNull(headerValues)
				|| GoForWealthSIPUtil.isEmptyCollection(headerValues)
				|| headerValues.size() > 1) {
			logger.error("Either no header present or multiple headers present");
			throw new GoForWealthSIPException(
					GoForWealthSIPErrorMessageEnum.INVALID_HEADER_CODE.getValue(),
					GoForWealthSIPErrorMessageEnum.INVALID_HEADER_MESSAGE.getValue());
		}
		return headerValues.get(0);
	}

	public static List<String> getOptionalValueFromHeader(HttpHeaders headers,
			String headerKey) {
		logger.info("In getOptionalValueFromHeader()");
		List<String> headerValues = headers.get(headerKey);
		logger.info("Out getOptionalValueFromHeader()");
		return headerValues;
	}
	/**
	 * Method will return true if collection is empty , false otherwise
	 * 
	 * @param collection
	 * @return <code>true</code>,<code>false</code>
	 */
	public static boolean isEmptyCollection(Collection<?> collection) {
		boolean isEmpty = false;

		if (collection == null)
			isEmpty = true;
		else {
			if (collection.isEmpty())
				isEmpty = true;
		}
		return isEmpty;
	}

	/**
	 * Method will return true if map is empty , false otherwise
	 * 
	 * @param map
	 * @return <code>true</code>,<code>false</code>
	 */
	public static boolean isEmptyMap(Map<?,?> map) {
		boolean isEmpty = false;

		if (map == null)
			isEmpty = true;
		else {
			if (map.isEmpty())
				isEmpty = true;
		}
		return isEmpty;
	}

	
	public static String encodeBase64(byte[] inputBytes){
		if(!GoForWealthStringUtil.isNull(inputBytes) && inputBytes.length>0){
			byte[] encodedBytes = Base64.encodeBase64(inputBytes);
			String encodedString = new String(encodedBytes);
			return encodedString;
		}
		return null;
	}
	public static byte[] decodeBase64(String encodedString){
		if(!GoForWealthStringUtil.isNullOrEmpty(encodedString)){
			byte[] decodedBytes = Base64.decodeBase64(encodedString.getBytes());
			return decodedBytes;
		}
		return null;
	}
	/** Read the given binary file, and return its contents as a byte array. 
	 * @throws GoForWealthSIPException */
	public static byte[] read(String aInputFileName) throws GoForWealthSIPException {
		logger.info("Reading in binary file named : " + aInputFileName);
		File file = new File(aInputFileName);
		logger.info("File size: " + file.length());
		byte[] result = new byte[(int) file.length()];
		try {
			InputStream input = null;
			try {
				int totalBytesRead = 0;
				input = new BufferedInputStream(new FileInputStream(file));
				while (totalBytesRead < result.length) {
					int bytesRemaining = result.length - totalBytesRead;
					int bytesRead = input.read(result, totalBytesRead,
							bytesRemaining);
					if (bytesRead > 0) {
						totalBytesRead = totalBytesRead + bytesRead;
					}
				}
				logger.info("Num bytes read: " + totalBytesRead);
			} finally {
				logger.info("Closing input stream.");
				input.close();
			}
		} catch (FileNotFoundException ex) {
			logger.error("File with name " + aInputFileName + " not found for encryption.");
			throw new GoForWealthSIPException(GoForWealthSIPErrorMessageEnum.ENCRYPTION_ERROR_CODE.getValue(), GoForWealthSIPErrorMessageEnum.ENCRYPTION_ERROR_MESSAGE.getValue());
		} catch (IOException ex) {
			logger.error(ex.getMessage());
			throw new GoForWealthSIPException(GoForWealthSIPErrorMessageEnum.ENCRYPTION_ERROR_CODE.getValue(), GoForWealthSIPErrorMessageEnum.ENCRYPTION_ERROR_MESSAGE.getValue());
		}
		return result;
	}

	/**
	 * Write a byte array to the given file. Writing binary data is
	 * significantly simpler than reading it.
	 * @throws GoForWealthSIPException 
	 */
	public static void write(byte[] aInput, String aOutputFileName) throws GoForWealthSIPException {
		logger.info("Writing binary file : " + aOutputFileName);
		try {
			OutputStream output = null;
			try {
				output = new BufferedOutputStream(new FileOutputStream(
						aOutputFileName));
				output.write(aInput);
			} finally {
				output.close();
			}
		} catch (FileNotFoundException ex) {
			logger.error("File with name " + aOutputFileName + " not found for encryption.");
			throw new GoForWealthSIPException(GoForWealthSIPErrorMessageEnum.ENCRYPTION_ERROR_CODE.getValue(), GoForWealthSIPErrorMessageEnum.ENCRYPTION_ERROR_MESSAGE.getValue());
		} catch (IOException ex) {
			logger.error(ex.getMessage());
			throw new GoForWealthSIPException(GoForWealthSIPErrorMessageEnum.ENCRYPTION_ERROR_CODE.getValue(), GoForWealthSIPErrorMessageEnum.ENCRYPTION_ERROR_MESSAGE.getValue());
		}
	}
	
	public static void removeFile(List<String> fileNamesList) throws GoForWealthSIPException{
		for(String fileName : fileNamesList){
			File file = new File(fileName);
			if(file.delete()){
				logger.info(file.getName() + " is deleted during encryption!");
			}else{
				logger.info("File delete operation during is failed. : " + "FileName: " + fileName);
				throw new GoForWealthSIPException(GoForWealthSIPErrorMessageEnum.ENCRYPTION_ERROR_CODE.getValue(), GoForWealthSIPErrorMessageEnum.ENCRYPTION_ERROR_MESSAGE.getValue());
			}
		}
	}

	public static Map<String, Integer> validatePagination(int page, int limit,boolean accessFromDB) {
		int startIndex, endIndex;
		Map<String, Integer> paginationValuesMap = new HashMap<String, Integer>();
		if (limit != -1) {
			if (page < GoForWealthSIPConstants.ONE || limit < GoForWealthSIPConstants.ONE) {
				if (page == GoForWealthSIPConstants.ZERO && limit == GoForWealthSIPConstants.ZERO) {
					startIndex = GoForWealthSIPConstants.ONE;
					endIndex = GoForWealthSIPConstants.TEN;
				} else
					return null;
			} else {
				if (page == GoForWealthSIPConstants.ONE) {
					startIndex = page;
				} else {
					int lastPage = page - GoForWealthSIPConstants.ONE;
					startIndex = lastPage * limit + GoForWealthSIPConstants.ONE;
				}
				endIndex = limit;
			}
		} else {
			startIndex = GoForWealthSIPConstants.ONE;
			endIndex = limit;
		}
		if (!accessFromDB) {
			if (page >= 0 && limit >= 0) {
				if (page == 0 && limit == 0) {
					startIndex = 0;
					endIndex = 10;
				} else {
					startIndex = page * limit;
					endIndex = (page + 1) * limit;
				}
			} else
				return null;
		}
		paginationValuesMap.put(GoForWealthSIPConstants.START_INDEX_KEY, startIndex);
		paginationValuesMap.put(GoForWealthSIPConstants.END_INDEX_KEY, endIndex);
		return paginationValuesMap;
	}

	/**
	 * Get Duplicate records in a list.
	 * @param list
	 * @return List
	 */
	public static List getDuplicatesFromList(List list) {

		final Set setToReturn = new HashSet();
		final Set tempSet = new HashSet();

		for (Object obj : list) {
			if (!tempSet.add(obj)) {
				setToReturn.add(obj);
			}
		}
		return new ArrayList(setToReturn);
	}

	/**
	 * This API is used to send email 
	 * 
	 * 
	 * @param mailTo
	 * @param mailFrom
	 * @param subject
	 * @param emailBody
	 * @return
	 */
	public static boolean sendEmail(String smtpServerIP, String mailTo, String mailFrom, String subject, String emailBody){


		boolean mailSent = false;		
		logger.info("Inside sendEmail() with from address: "+mailFrom+" & to adrress: "+mailTo+"\n Subject:"+ subject+"\n body: "+emailBody);

		String host = smtpServerIP;                   

		Properties properties = System.getProperties();
		properties.setProperty("mail.smtp.host", host);

		Session session = Session.getDefaultInstance(properties);

		try {
			logger.info("About to send Email....");
			MimeMessage message = new MimeMessage(session);       
			message.setFrom(new InternetAddress(mailFrom));                
			message.addRecipient(Message.RecipientType.TO, new InternetAddress(mailTo));
			message.setSubject(subject); 
			message.setText(emailBody);

			Transport.send(message);   
			mailSent = true;
			logger.info("Email Sent successfully....");
		}catch (MessagingException mex) {
			logger.info("Email Exception caught while sending email: "+ mex);
			mex.printStackTrace();
		}catch(Exception e) {
			logger.info("Exception caught while sending email: "+ e);
			e.printStackTrace();
		}
		logger.info("Exiting sendEmail()");
		return mailSent;
	}

	public static String decryptClientId(String encClientId, String imei, String iccid){
		int iccidStart = encClientId.lastIndexOf(iccid, encClientId.length());
		if(iccidStart == -1)
			return GoForWealthSIPConstants.BLANK;
		encClientId = encClientId.substring(0, iccidStart);
		int imeiStart = encClientId.lastIndexOf(imei, encClientId.length());
		if(imeiStart == -1)
			return GoForWealthSIPConstants.BLANK;
		encClientId = encClientId.substring(0,imeiStart);
		return encClientId;
	}

	public static long currentTimeStampInUtc()
	{
		Calendar calendar=Calendar.getInstance(TimeZone.getTimeZone(GoForWealthSIPConstants.UTC));
		return calendar.getTimeInMillis();
	}

	public static long convertTimeStampToUtc(long timestamp)
	{
		Calendar calendar=Calendar.getInstance();
		calendar.setTimeInMillis(timestamp);
		calendar.setTimeZone(TimeZone.getTimeZone("UTC"));
		long timeInMillis = calendar.getTimeInMillis();
		return timeInMillis;
	}
	public static String convertTimestampToDate(long timestampInMilliSeconds){
		Timestamp stamp = new Timestamp(timestampInMilliSeconds);
		Date date = new Date(stamp.getTime());
		return date.toString();
	}

	public static String parseListForProcedure(List<String> list)
	{
		String parsedString = "";
		String str = null;
		if (list == null || (list != null && list.size() == 0))
		{
			return null;
		}
		Iterator<String> itr = list.iterator();
		while (itr.hasNext())
		{
			str = itr.next();
			parsedString = parsedString + str + ",";
		}
		if (parsedString.indexOf(",") != -1)
		{
			parsedString = parsedString.substring(0, parsedString.length() - 1);
		}
		//		parsedString = parsedString + "'";
		return parsedString;
	}

	public static boolean isEmpty(String dataStr)
	{
		boolean isEmpty = false;
		if (dataStr == null || (dataStr != null && (dataStr.length() == 0 || dataStr.trim().length() == 0)))
			isEmpty = true;
		return isEmpty;
	}

	/**
	 * Converts the object to String. If obj is null, blank string is returned
	 * 
	 * @param obj
	 * @return {@link String}
	 */
	public static String obj2Str(Object obj)
	{
		String str = "";
		if (obj != null)
		{
			str = obj.toString();
		}
		return str;
	}

	/**
	 * Generate checksum of given string (concatenates ascii values of characters in string)
	 * @param String
	 * @return
	 */
	public static String generateChecksum(String String) {
		StringBuffer checksum=new StringBuffer();
		byte[] bytes = String.getBytes();
		for(byte byt:bytes)
		{
			checksum.append(byt);
		}
		return checksum.toString();
	}
	
	
	public static String generateUUID() throws GoForWealthSIPException {
		try {
			// Initialize SecureRandom
			// This is a lengthy operation, to be done only upon
			// initialization of the application
			SecureRandom prng = SecureRandom.getInstance("SHA1PRNG");

			// generate a random number
			String randomNum = String.valueOf(currentTimeStampInUtc());
			logger.info("Random number used for UUID Genaration: " + randomNum);
			// get its digest
			MessageDigest sha = MessageDigest.getInstance("SHA-1");
			byte[] result = sha.digest(randomNum.getBytes());
			String uuid = hexEncode(result);
			logger.info("UUID Generated: " + uuid);
			return uuid;
		} catch (NoSuchAlgorithmException ex) {
			logger.info("Error while genarting UUID: " + ex);
			throw new GoForWealthSIPException(GoForWealthSIPErrorMessageEnum.FAILURE_CODE.getValue(),GoForWealthSIPErrorMessageEnum.FAILURE_MESSAGE.getValue());
		}
	}

	private static String hexEncode(byte[] aInput){
		StringBuilder result = new StringBuilder();
		char[] digits = {'0', '1', '2', '3', '4','5','6','7','8','9','a','b','c','d','e','f'};
		for (int idx = 0; idx < aInput.length; ++idx) {
			byte b = aInput[idx];
			result.append(digits[ (b&0xf0) >> 4 ]);
			result.append(digits[ b&0x0f]);
		}
		return result.toString();
	}

	public static List<String> getListFromCommaSepratedString(String string) {
		List<String> stringList = new ArrayList<>();
		if(GoForWealthStringUtil.isNullOrEmpty(string))
			return stringList;
		String[] stringArray = string.split(",");
		stringList = Arrays.asList(stringArray);
		return stringList;
	}
	
	public static String generateJwtToken(JwtConfiguration jwtConfiguration,UserSession userSession) throws JsonProcessingException {
		LocalDateTime expirationTime = LocalDateTime.now().plusMinutes(jwtConfiguration.expirationTimeMin);
		String token = Jwts.builder()
				.setSubject(new ObjectMapper().writeValueAsString(userSession))
				.setExpiration(Date.from(expirationTime.atZone(ZoneId.systemDefault()).toInstant()))
				.signWith(SignatureAlgorithm.HS512, jwtConfiguration.secret.getBytes())
				.compact();
		return token;
	}

	public static String parseJwtToken(String token,JwtConfiguration jwtConfiguration) {
		String subjectJson = Jwts.parser()
				.setSigningKey(jwtConfiguration.secret.getBytes())
				.parseClaimsJws(token)
				.getBody()
				.getSubject();	
		return subjectJson;
	}

	public static String generateOtpToken(JwtConfiguration jwtConfiguration, String generateOTP) {
		LocalDateTime expirationTime = LocalDateTime.now().plusMinutes(jwtConfiguration.otpExpirationTimeMin);
		String token = Jwts.builder()
				.setSubject(generateOTP)
				.setExpiration(Date.from(expirationTime.atZone(ZoneId.systemDefault()).toInstant()))
				.signWith(SignatureAlgorithm.HS512, jwtConfiguration.secret.getBytes())
				.compact();
		return token;
	}

	public static void createCell(Row row, short index, String value) {
		if (value == null) {
			row.createCell(index, CellType.BLANK);
		} else {
			row.createCell(index, CellType.STRING).setCellValue(value);
		}
	}

	public static String formatDateWithPattern(Date value, String pattern) {
		if (value == null) {
			return null;
		}
		DateFormat formatter = new SimpleDateFormat(pattern);
		return formatter.format(value);
	}

	public static Workbook createExportFileForGoals(Workbook wb, List<GoalDTO> goalDTOs) {
		if(wb==null)
			wb = new XSSFWorkbook();
		CreationHelper createHelper = wb.getCreationHelper();
		Sheet sheet = wb.createSheet("goals");
		// Create a row and put some cells in it. Rows are 0 based.
		int index = 0;
		short cellIndex = 0;
		Row headerRow = sheet.createRow(index++);
		headerRow.createCell(cellIndex++).setCellValue("GOALID");
		headerRow.createCell(cellIndex++).setCellValue("GOALNAME");
		headerRow.createCell(cellIndex++).setCellValue("GOALBUCKET");
		headerRow.createCell(cellIndex++).setCellValue("SHOWTOPROFILETYPE");
		headerRow.createCell(cellIndex++).setCellValue("COSTOFGOAL");
		CellStyle headerStyle = wb.createCellStyle();
		headerStyle.setFillForegroundColor(IndexedColors.SKY_BLUE.getIndex());
		headerStyle.setFillPattern(FillPatternType.SOLID_FOREGROUND);
		Font font = wb.createFont();
		font.setColor(IndexedColors.BLACK.getIndex());
		font.setBold(true);
		headerStyle.setFont(font);
		for (short i = 0; i < cellIndex; i++) {
			headerRow.getCell(i).setCellStyle(headerStyle);
		}
		CellStyle moneyCellStyle = wb.createCellStyle();
        moneyCellStyle.setDataFormat(createHelper.createDataFormat().getFormat("#,##0.00"));
		for (GoalDTO goalDTO : goalDTOs) {
			cellIndex = 0;
			Row row = sheet.createRow(index++);
			GoForWealthSIPUtil.createCell(row, cellIndex++, goalDTO.getGoalId(),null);
			GoForWealthSIPUtil.createCell(row, cellIndex++, goalDTO.getGoalName());
			GoForWealthSIPUtil.createCell(row, cellIndex++, goalDTO.getGoalBucket().getGoalBucketName());
			GoForWealthSIPUtil.createCell(row, cellIndex++, goalDTO.getShowToProfileType().toString());
			GoForWealthSIPUtil.createCell(row, cellIndex++, goalDTO.getCostOfGoal(),moneyCellStyle);
		}
		return wb;
	}
	
	public static void createCell(Row row, short index, Number value, CellStyle cellStyle) {
		if (value == null) {
			Cell cell = row.createCell(index, CellType.BLANK);
			if (cellStyle != null) {
				cell.setCellStyle(cellStyle);
			}
		}else {
			Cell cell = row.createCell(index, CellType.NUMERIC);
			cell.setCellValue(value.doubleValue());
			if(cellStyle != null) {
				cell.setCellStyle(cellStyle);
			}
		}
	}

	public static Workbook createExportFileForGoalsCombRel(Workbook wb) {
		if(wb==null)
			wb = new XSSFWorkbook();
		Sheet sheet = wb.createSheet("GoalsCombinationRelation");
		// Create a row and put some cells in it. Rows are 0 based.
		int index = 0;
		short cellIndex = 0;
		Row headerRow = sheet.createRow(index++);		
		headerRow.createCell(cellIndex++).setCellValue("GOALID");
		headerRow.createCell(cellIndex++).setCellValue("SIPPROFILECOMBINATIONID");
		headerRow.createCell(cellIndex++).setCellValue("CUSTOMERCODE");
		CellStyle headerStyle = wb.createCellStyle();
		headerStyle.setFillForegroundColor(IndexedColors.SKY_BLUE.getIndex());
		headerStyle.setFillPattern(FillPatternType.SOLID_FOREGROUND);
		Font font = wb.createFont();
		font.setColor(IndexedColors.BLACK.getIndex());
		font.setBold(true);
		headerStyle.setFont(font);
		for (short i = 0; i < cellIndex; i++) {
			headerRow.getCell(i).setCellStyle(headerStyle);
		}
		return wb;
	}

	public static Workbook createExportFileForIncome(List<IncomeDTO> incomeDTOs) {
		Workbook wb = new XSSFWorkbook();
		Sheet sheet = wb.createSheet("Income Slab");
		// Create a row and put some cells in it. Rows are 0 based.
		int index = 0;
		short cellIndex = 0;
		Row headerRow = sheet.createRow(index++);
		headerRow.createCell(cellIndex++).setCellValue("INCOMESLABNAME");
		headerRow.createCell(cellIndex++).setCellValue("INCOMESlABCODE");
		CellStyle headerStyle = wb.createCellStyle();
		headerStyle.setFillForegroundColor(IndexedColors.SKY_BLUE.getIndex());
		headerStyle.setFillPattern(FillPatternType.SOLID_FOREGROUND);
		Font font = wb.createFont();
		font.setColor(IndexedColors.BLACK.getIndex());
		font.setBold(true);
		headerStyle.setFont(font);
		for(short i = 0; i < cellIndex; i++) {
			headerRow.getCell(i).setCellStyle(headerStyle);
		}
		for(IncomeDTO incomeDTO : incomeDTOs) {
			cellIndex = 0;
			Row row = sheet.createRow(index++);
			GoForWealthSIPUtil.createCell(row, cellIndex++, incomeDTO.getIncomeSlabName());
			GoForWealthSIPUtil.createCell(row, cellIndex++, incomeDTO.getIncomeSlabCode());
		}
		return wb;
	}

	public static Workbook createExportFileForAgeSlab(Workbook wb, List<AgeSlabDTO> ageSlabDTOs) {
		if(wb==null) 
			wb = new XSSFWorkbook();
		Sheet sheet = wb.createSheet("Age Slab");
		// Create a row and put some cells in it. Rows are 0 based.
		int index = 0;
		short cellIndex = 0;
		Row headerRow = sheet.createRow(index++);
		headerRow.createCell(cellIndex++).setCellValue("AGESLABNAME");
		headerRow.createCell(cellIndex++).setCellValue("AGESLABCODE");
		CellStyle headerStyle = wb.createCellStyle();
		headerStyle.setFillForegroundColor(IndexedColors.SKY_BLUE.getIndex());
		headerStyle.setFillPattern(FillPatternType.SOLID_FOREGROUND);
		Font font = wb.createFont();
		font.setColor(IndexedColors.BLACK.getIndex());
		font.setBold(true);
		headerStyle.setFont(font);
		for (short i = 0; i < cellIndex; i++) {
			headerRow.getCell(i).setCellStyle(headerStyle);
		}
		for (AgeSlabDTO incomeDTO : ageSlabDTOs) {
			cellIndex = 0;
			Row row = sheet.createRow(index++);
			GoForWealthSIPUtil.createCell(row, cellIndex++, incomeDTO.getAgeSlabName());
			GoForWealthSIPUtil.createCell(row, cellIndex++, incomeDTO.getAgeSlabCode());
		}
		return wb;
	}
	
	public static Workbook createExportFileForMaritalSlab(Workbook wb, List<MaritalDTO> maritalDTOs) {
		if(wb==null) 
			wb = new XSSFWorkbook();
		Sheet sheet = wb.createSheet("Marital Slab");
		// Create a row and put some cells in it. Rows are 0 based.
		int index = 0;
		short cellIndex = 0;
		Row headerRow = sheet.createRow(index++);
		headerRow.createCell(cellIndex++).setCellValue("AGESLABNAME");
		headerRow.createCell(cellIndex++).setCellValue("AGESLABCODE");
		CellStyle headerStyle = wb.createCellStyle();
		headerStyle.setFillForegroundColor(IndexedColors.SKY_BLUE.getIndex());
		headerStyle.setFillPattern(FillPatternType.SOLID_FOREGROUND);
		Font font = wb.createFont();
		font.setColor(IndexedColors.BLACK.getIndex());
		font.setBold(true);
		headerStyle.setFont(font);
		for(short i = 0; i < cellIndex; i++) {
			headerRow.getCell(i).setCellStyle(headerStyle);
		}
		for(MaritalDTO incomeDTO : maritalDTOs) {
			cellIndex = 0;
			Row row = sheet.createRow(index++);
			GoForWealthSIPUtil.createCell(row, cellIndex++, incomeDTO.getMaritalStatusName());
			GoForWealthSIPUtil.createCell(row, cellIndex++, incomeDTO.getMaritalStatusCode());
		}
		return wb;
	}

	public static Workbook createExportFileForKidsSlab(Workbook wb, List<KidsSlabDTO> kidsSlabDTOs) {
		if(wb==null) 
			wb = new XSSFWorkbook();
		Sheet sheet = wb.createSheet("Kids Slab");
		// Create a row and put some cells in it. Rows are 0 based.
		int index = 0;
		short cellIndex = 0;
		Row headerRow = sheet.createRow(index++);
		headerRow.createCell(cellIndex++).setCellValue("KIDSSLABNAME");
		headerRow.createCell(cellIndex++).setCellValue("KIDSSLABCODE");
		CellStyle headerStyle = wb.createCellStyle();
		headerStyle.setFillForegroundColor(IndexedColors.SKY_BLUE.getIndex());
		headerStyle.setFillPattern(FillPatternType.SOLID_FOREGROUND);
		Font font = wb.createFont();
		font.setColor(IndexedColors.BLACK.getIndex());
		font.setBold(true);
		headerStyle.setFont(font);
		for(short i = 0; i < cellIndex; i++) {
			headerRow.getCell(i).setCellStyle(headerStyle);
		}
		for(KidsSlabDTO incomeDTO : kidsSlabDTOs) {
			cellIndex = 0;
			Row row = sheet.createRow(index++);
			GoForWealthSIPUtil.createCell(row, cellIndex++, incomeDTO.getKidsSlabName());
			GoForWealthSIPUtil.createCell(row, cellIndex++, incomeDTO.getKidsSlabCode());
		}
		return wb;
	}
	
	public static String generateRandomString(Integer length, boolean isOnlyNumber){
		StringBuilder sb = new StringBuilder(length);
		Random rand = new SecureRandom();
		for (int i = 0; i < length; i++) {
			if(isOnlyNumber)
				sb.append(GoForWealthSIPConstants.NUMBERS[rand.nextInt(GoForWealthSIPConstants.NUMBERS.length)]);
			sb.append(GoForWealthSIPConstants.CHARS[rand.nextInt(GoForWealthSIPConstants.CHARS.length)]);
		}
		return sb.toString();
	}

	public static String generateJwtToken(UserSession userSession, JwtConfiguration jwtConfiguration, LocalDateTime expirationTime) throws JsonProcessingException {
		return  Jwts.builder()
				.setSubject(new ObjectMapper().writeValueAsString(userSession))
				.setExpiration(Date.from(expirationTime.atZone(ZoneId.systemDefault()).toInstant()))
				.signWith(SignatureAlgorithm.HS512, jwtConfiguration.secret.getBytes())
				.compact();
	}

	public static Workbook createExportFileForCalculatedSip(CalculateSipResponseDto calculateSipResponseDto,CalculateSipRequestDto sipRequestDto) {
		Workbook wb = new XSSFWorkbook();
		CreationHelper createHelper = wb.getCreationHelper();
		Sheet sheet = wb.createSheet("user_sip_data");
		CellStyle moneyCellStyle = wb.createCellStyle();
	    moneyCellStyle.setDataFormat(createHelper.createDataFormat().getFormat("#,##0.00"));
	    CellStyle headerStyle = wb.createCellStyle();
	    headerStyle.setFillForegroundColor(IndexedColors.SKY_BLUE.getIndex());
		headerStyle.setFillPattern(FillPatternType.SOLID_FOREGROUND);
		Font font = wb.createFont();
		font.setColor(IndexedColors.BLACK.getIndex());
		font.setBold(true);
		headerStyle.setFont(font);
		// Create a row and put some cells in it. Rows are 0 based.
		int index = 0;
		short cellIndex = 0;
		Row headerRow = sheet.createRow(index++);
		headerRow.createCell(cellIndex++).setCellValue("CITY");
		headerRow.createCell(cellIndex++).setCellValue("INCOME");
		headerRow.createCell(cellIndex++).setCellValue("MARITAL STATUS");
		headerRow.createCell(cellIndex++).setCellValue("KIDS");
		for (short i = 0; i < cellIndex; i++) {
			headerRow.getCell(i).setCellStyle(headerStyle);
		}			
		Row row1 = sheet.createRow(index++);
		cellIndex = 0;
		GoForWealthSIPUtil.createCell(row1, cellIndex++, calculateSipResponseDto.getCityName());
		GoForWealthSIPUtil.createCell(row1, cellIndex++, sipRequestDto.getIncomeVal()+"Lakh");
		GoForWealthSIPUtil.createCell(row1, cellIndex++, calculateSipResponseDto.getMaritalStatusName());
		GoForWealthSIPUtil.createCell(row1, cellIndex++, sipRequestDto.getKids(),null);
		index++;
		cellIndex = 0;
		Row headerRow1 = sheet.createRow(index++);
		headerRow1.createCell(cellIndex++).setCellValue("GOALNAME");
		headerRow1.createCell(cellIndex++).setCellValue("GOAL DURATION");
		headerRow1.createCell(cellIndex++).setCellValue("COSTOFGOAL");
		headerRow1.createCell(cellIndex++).setCellValue("RATE OF INTEREST(ROI)");
		headerRow1.createCell(cellIndex++).setCellValue("INFLATION");
		headerRow1.createCell(cellIndex++).setCellValue("FUTURE VALUE");
		headerRow1.createCell(cellIndex++).setCellValue("MONTHLY SIP AMOUNT");
		for (short i = 0; i < cellIndex; i++) {
			headerRow1.getCell(i).setCellStyle(headerStyle);
		}
		for (GoalDTO goalDTO : calculateSipResponseDto.getGoalList()) {
			cellIndex = 0;
			Row row = sheet.createRow(index++);
			GoForWealthSIPUtil.createCell(row, cellIndex++, goalDTO.getGoalName());
			GoForWealthSIPUtil.createCell(row, cellIndex++, goalDTO.getGoalBucket().getGoalBucketName());
			GoForWealthSIPUtil.createCell(row, cellIndex++, goalDTO.getCostOfGoal(),moneyCellStyle);
			GoForWealthSIPUtil.createCell(row, cellIndex++, goalDTO.getRate(),null);
			GoForWealthSIPUtil.createCell(row, cellIndex++, goalDTO.getInflation(),null);
			GoForWealthSIPUtil.createCell(row, cellIndex++, goalDTO.getFutureValue(),moneyCellStyle);
			GoForWealthSIPUtil.createCell(row, cellIndex++, goalDTO.getPmtFutureValue(),moneyCellStyle);
		}
		return wb;
	}


}
