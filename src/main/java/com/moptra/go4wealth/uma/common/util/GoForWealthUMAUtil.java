package com.moptra.go4wealth.uma.common.util;

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
import java.util.Set;
import java.util.TimeZone;

import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import javax.servlet.http.HttpServletRequest;

import org.apache.tomcat.util.codec.binary.Base64;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpHeaders;
import org.springframework.security.core.Authentication;

import com.fasterxml.jackson.annotation.JsonFilter;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectWriter;
import com.fasterxml.jackson.databind.ser.impl.SimpleBeanPropertyFilter;
import com.fasterxml.jackson.databind.ser.impl.SimpleFilterProvider;
import com.moptra.go4wealth.configuration.JwtConfiguration;
import com.moptra.go4wealth.security.UserSession;
import com.moptra.go4wealth.uma.common.constant.GoForWealthUMAConstants;
import com.moptra.go4wealth.uma.common.enums.GoForWealthErrorMessageEnum;
import com.moptra.go4wealth.uma.common.exception.GoForWealthUMAException;
import com.moptra.go4wealth.uma.common.rest.GoForWealthUMAResponseInfo;

import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;


public class GoForWealthUMAUtil {

	private static Logger logger = LoggerFactory.getLogger(GoForWealthUMAUtil.class);

	private static boolean loadPropertiesFile(String fileName,Properties properties){
		InputStream inputStream = null;
		try {
			logger.info("In loadPropertiesFile()");
			// Load properties file
			inputStream = GoForWealthUMAUtil.class.getClassLoader().getResourceAsStream(fileName);
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
					inputStream = GoForWealthUMAUtil.class.getClassLoader().getResourceAsStream(GoForWealthUMAConstants.PROPERTIES_FILE_PATH_PREFIX_FOR_JUNIT
							+ fileName);
					if(GoForWealthStringUtil.isNull(inputStream)){
						logger.error("Could not read the property file : " + fileName);
						throw new FileNotFoundException();
					}
					else{
						properties.load(inputStream);
						logger.info("Properties file available : " + GoForWealthUMAConstants.PROPERTIES_FILE_PATH_PREFIX_FOR_JUNIT + fileName);
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
	
	public static GoForWealthUMAResponseInfo getSuccessResponseInfo(String status,String message, Object data) {
		GoForWealthUMAResponseInfo responseInfo = new GoForWealthUMAResponseInfo();
		responseInfo.setStatus(status);
		responseInfo.setMessage(message);
		if (!GoForWealthStringUtil.isNull(data))
			responseInfo.setData(data);
		return responseInfo;
	}

	public static GoForWealthUMAResponseInfo getErrorResponseInfo(String status, String message, Exception e) {
		logger.error("Exception occurred = ", e);
		GoForWealthUMAResponseInfo responseInfo = new GoForWealthUMAResponseInfo();
		responseInfo.setStatus(status);
		responseInfo.setMessage(message);
		responseInfo.setData(GoForWealthUMAConstants.BLANK);
		responseInfo.setTotalRecords(GoForWealthUMAConstants.ZERO);
		return responseInfo;
	}

	public static GoForWealthUMAResponseInfo getExceptionResponseInfo(GoForWealthUMAException e) {
		logger.error("Exception occurred = ", e);
		GoForWealthUMAResponseInfo responseInfo = new GoForWealthUMAResponseInfo();
		responseInfo.setStatus(e.getErrorCode());
		responseInfo.setMessage(e.getErrorMessage());
		responseInfo.setData(GoForWealthUMAConstants.BLANK);
		responseInfo.setTotalRecords(GoForWealthUMAConstants.ZERO);
		return responseInfo;
	}

	public static GoForWealthUMAResponseInfo getExceptionResponseInfo(GoForWealthUMAException e, Object data) {
		logger.error("Exception occurred = ", e);
		GoForWealthUMAResponseInfo responseInfo = new GoForWealthUMAResponseInfo();
		responseInfo.setStatus(e.getErrorCode());
		responseInfo.setMessage(e.getErrorMessage());
		responseInfo.setData(data);
		responseInfo.setTotalRecords(GoForWealthUMAConstants.ZERO);
		return responseInfo;
	}

	public static void logRequest(HttpServletRequest request, HttpHeaders headers, Object obj) {
		try {
			if (!GoForWealthStringUtil.isNull(request)) {
				logger.info("REST API Called: "
						+ request.getRequestURL()+GoForWealthUMAConstants.QUESTION_MARK+request.getQueryString() );

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
			if(!GoForWealthUMAUtil.isEmptyCollection(propertiesToIgnore))
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
			String headerKey) throws GoForWealthUMAException {
		logger.info("In getValueFromHeader()");
		List<String> headerValues = headers.get(headerKey);
		if (GoForWealthStringUtil.isNull(headerValues)
				|| GoForWealthUMAUtil.isEmptyCollection(headerValues)
				|| headerValues.size() > 1) {
			logger.error("Either no header present or multiple headers present");
			throw new GoForWealthUMAException(
					GoForWealthErrorMessageEnum.INVALID_HEADER_CODE.getValue(),
					GoForWealthErrorMessageEnum.INVALID_HEADER_MESSAGE.getValue());
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
	 * @throws GoForWealthUMAException */
	public static byte[] read(String aInputFileName) throws GoForWealthUMAException {
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
			throw new GoForWealthUMAException(GoForWealthErrorMessageEnum.ENCRYPTION_ERROR_CODE.getValue(), GoForWealthErrorMessageEnum.ENCRYPTION_ERROR_MESSAGE.getValue());
		} catch (IOException ex) {
			logger.error(ex.getMessage());
			throw new GoForWealthUMAException(GoForWealthErrorMessageEnum.ENCRYPTION_ERROR_CODE.getValue(), GoForWealthErrorMessageEnum.ENCRYPTION_ERROR_MESSAGE.getValue());
		}
		return result;
	}

	/**
	 * Write a byte array to the given file. Writing binary data is
	 * significantly simpler than reading it.
	 * @throws GoForWealthUMAException 
	 */
	public static void write(byte[] aInput, String aOutputFileName) throws GoForWealthUMAException {
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
			throw new GoForWealthUMAException(GoForWealthErrorMessageEnum.ENCRYPTION_ERROR_CODE.getValue(), GoForWealthErrorMessageEnum.ENCRYPTION_ERROR_MESSAGE.getValue());
		} catch (IOException ex) {
			logger.error(ex.getMessage());
			throw new GoForWealthUMAException(GoForWealthErrorMessageEnum.ENCRYPTION_ERROR_CODE.getValue(), GoForWealthErrorMessageEnum.ENCRYPTION_ERROR_MESSAGE.getValue());
		}
	}
	public static void removeFile(List<String> fileNamesList) throws GoForWealthUMAException{
		for(String fileName : fileNamesList){
			File file = new File(fileName);
			if(file.delete()){
				logger.info(file.getName() + " is deleted during encryption!");
			}else{
				logger.info("File delete operation during is failed. : " + "FileName: " + fileName);
				throw new GoForWealthUMAException(GoForWealthErrorMessageEnum.ENCRYPTION_ERROR_CODE.getValue(), GoForWealthErrorMessageEnum.ENCRYPTION_ERROR_MESSAGE.getValue());
			}
		}

	}
	

	public static Map<String, Integer> validatePagination(int page, int limit,
			boolean accessFromDB) {
		int startIndex, endIndex;
		Map<String, Integer> paginationValuesMap = new HashMap<String, Integer>();
		if (limit != -1) {
			if (page < GoForWealthUMAConstants.ONE || limit < GoForWealthUMAConstants.ONE) {
				if (page == GoForWealthUMAConstants.ZERO && limit == GoForWealthUMAConstants.ZERO) {
					startIndex = GoForWealthUMAConstants.ONE;
					endIndex = GoForWealthUMAConstants.TEN;
				} else
					return null;
			} else {
				if (page == GoForWealthUMAConstants.ONE) {
					startIndex = page;
				} else {
					int lastPage = page - GoForWealthUMAConstants.ONE;
					startIndex = lastPage * limit + GoForWealthUMAConstants.ONE;
				}
				endIndex = limit;
			}
		} else {
			startIndex = GoForWealthUMAConstants.ONE;
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

		paginationValuesMap.put(GoForWealthUMAConstants.START_INDEX_KEY, startIndex);
		paginationValuesMap.put(GoForWealthUMAConstants.END_INDEX_KEY, endIndex);
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
			return GoForWealthUMAConstants.BLANK;
		encClientId = encClientId.substring(0, iccidStart);
		int imeiStart = encClientId.lastIndexOf(imei, encClientId.length());
		if(imeiStart == -1)
			return GoForWealthUMAConstants.BLANK;
		encClientId = encClientId.substring(0,imeiStart);
		return encClientId;
	}

	public static long currentTimeStampInUtc()
	{
		Calendar calendar=Calendar.getInstance(TimeZone.getTimeZone(GoForWealthUMAConstants.UTC));
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
	
	
	public static String generateUUID() throws GoForWealthUMAException {
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
			throw new GoForWealthUMAException(GoForWealthErrorMessageEnum.FAILURE_CODE.getValue(),
					GoForWealthErrorMessageEnum.FAILURE_MESSAGE.getValue());
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
	
	public static String generateJwtToken(JwtConfiguration jwtConfiguration,UserSession userSession,LocalDateTime expirationTime) throws JsonProcessingException {
		String token = Jwts.builder()
				.setSubject(new ObjectMapper().writeValueAsString(userSession))
				.setExpiration(Date.from(expirationTime.atZone(ZoneId.systemDefault()).toInstant()))
				.signWith(SignatureAlgorithm.HS512, jwtConfiguration.secret.getBytes())
				.compact();
		return token;
	}
}
