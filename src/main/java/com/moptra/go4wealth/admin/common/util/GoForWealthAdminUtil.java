package com.moptra.go4wealth.admin.common.util;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.ByteArrayInputStream;
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
import org.apache.poi.ss.usermodel.WorkbookFactory;
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
import com.moptra.go4wealth.admin.common.constant.GoForWealthAdminConstants;
import com.moptra.go4wealth.admin.common.enums.GoForWealthAdminErrorMessageEnum;
import com.moptra.go4wealth.admin.common.exception.GoForWealthAdminException;
import com.moptra.go4wealth.admin.common.rest.GoForWealthAdminResponseInfo;
import com.moptra.go4wealth.admin.model.OrderExportExcelDto;
import com.moptra.go4wealth.admin.model.UserExportExcelDataDto;
import com.moptra.go4wealth.admin.model.UsersListDTO;
import com.moptra.go4wealth.configuration.JwtConfiguration;
import com.moptra.go4wealth.prs.model.FundSchemeDTO;
import com.moptra.go4wealth.security.UserSession;

import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;


public class GoForWealthAdminUtil {

	private static Logger logger = LoggerFactory.getLogger(GoForWealthAdminUtil.class);

	private static boolean loadPropertiesFile(String fileName,Properties properties){
		InputStream inputStream = null;
		try {
			logger.info("In loadPropertiesFile()");
			// Load properties file
			inputStream = GoForWealthAdminUtil.class.getClassLoader().getResourceAsStream(fileName);
			if(GoForWealthAdminStringUtil.isNull(inputStream)){
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
			if(GoForWealthAdminStringUtil.isNull(inputStream)){
				logger.info("Loading property file for junits using relative path.");
				try {
					inputStream = GoForWealthAdminUtil.class.getClassLoader().getResourceAsStream(GoForWealthAdminConstants.PROPERTIES_FILE_PATH_PREFIX_FOR_JUNIT
							+ fileName);
					if(GoForWealthAdminStringUtil.isNull(inputStream)){
						logger.error("Could not read the property file : " + fileName);
						throw new FileNotFoundException();
					}
					else{
						properties.load(inputStream);
						logger.info("Properties file available : " + GoForWealthAdminConstants.PROPERTIES_FILE_PATH_PREFIX_FOR_JUNIT + fileName);
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
				if(!GoForWealthAdminStringUtil.isNull(inputStream))
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
	
	public static GoForWealthAdminResponseInfo getSuccessResponseInfo(String status,String message, Object data) {
		GoForWealthAdminResponseInfo responseInfo = new GoForWealthAdminResponseInfo();
		responseInfo.setStatus(status);
		responseInfo.setMessage(message);
		if (!GoForWealthAdminStringUtil.isNull(data))
			responseInfo.setData(data);
		return responseInfo;
	}

	public static GoForWealthAdminResponseInfo getErrorResponseInfo(String status, String message, Exception e) {
		logger.error("Exception occurred = ", e);
		GoForWealthAdminResponseInfo responseInfo = new GoForWealthAdminResponseInfo();
		responseInfo.setStatus(status);
		responseInfo.setMessage(message);
		responseInfo.setData(GoForWealthAdminConstants.BLANK);
		responseInfo.setTotalRecords(GoForWealthAdminConstants.ZERO);
		return responseInfo;
	}

	public static GoForWealthAdminResponseInfo getExceptionResponseInfo(GoForWealthAdminException e) {
		logger.error("Exception occurred = ", e);
		GoForWealthAdminResponseInfo responseInfo = new GoForWealthAdminResponseInfo();
		responseInfo.setStatus(e.getErrorCode());
		responseInfo.setMessage(e.getErrorMessage());
		responseInfo.setData(GoForWealthAdminConstants.BLANK);
		responseInfo.setTotalRecords(GoForWealthAdminConstants.ZERO);
		return responseInfo;
	}

	public static GoForWealthAdminResponseInfo getExceptionResponseInfo(GoForWealthAdminException e, Object data) {
		logger.error("Exception occurred = ", e);
		GoForWealthAdminResponseInfo responseInfo = new GoForWealthAdminResponseInfo();
		responseInfo.setStatus(e.getErrorCode());
		responseInfo.setMessage(e.getErrorMessage());
		responseInfo.setData(data);
		responseInfo.setTotalRecords(GoForWealthAdminConstants.ZERO);
		return responseInfo;
	}

	public static void logRequest(HttpServletRequest request, HttpHeaders headers, Object obj) {
		try {
			if (!GoForWealthAdminStringUtil.isNull(request)) {
				logger.info("REST API Called: "
						+ request.getRequestURL()+GoForWealthAdminConstants.QUESTION_MARK+request.getQueryString() );

				logger.info("Request Headers Start:-");
				if (!GoForWealthAdminStringUtil.isNull(headers)) {
					for (Entry<String, List<String>> entry : headers.entrySet()) {
						logger.info(entry.getKey() + ": "
								+ entry.getValue());
					}
				}
				logger.info("Request Headers End:-");
			}
			if(!GoForWealthAdminStringUtil.isNull(obj))
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
		if(!GoForWealthAdminStringUtil.isNull(obj))
		{
			ObjectMapper mapper=new ObjectMapper();
			if(!GoForWealthAdminUtil.isEmptyCollection(propertiesToIgnore))
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
		if (!GoForWealthAdminStringUtil.isNull(className) && !GoForWealthAdminStringUtil.isNull(json)) {
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
			if (!GoForWealthAdminStringUtil.isNull(responseInfo)) {

				if (!GoForWealthAdminStringUtil.isNull(headers)) {
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


	public static String getValueFromHeader(HttpHeaders headers,String headerKey) throws GoForWealthAdminException {
		logger.info("In getValueFromHeader()");
		List<String> headerValues = headers.get(headerKey);
		if (GoForWealthAdminStringUtil.isNull(headerValues)
				|| GoForWealthAdminUtil.isEmptyCollection(headerValues)
				|| headerValues.size() > 1) {
			logger.error("Either no header present or multiple headers present");
			throw new GoForWealthAdminException(
					GoForWealthAdminErrorMessageEnum.INVALID_HEADER_CODE.getValue(),
					GoForWealthAdminErrorMessageEnum.INVALID_HEADER_MESSAGE.getValue());
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
		if(!GoForWealthAdminStringUtil.isNull(inputBytes) && inputBytes.length>0){
			byte[] encodedBytes = Base64.encodeBase64(inputBytes);
			String encodedString = new String(encodedBytes);
			return encodedString;
		}
		return null;
	}

	public static byte[] decodeBase64(String encodedString){
		if(!GoForWealthAdminStringUtil.isNullOrEmpty(encodedString)){
			byte[] decodedBytes = Base64.decodeBase64(encodedString.getBytes());
			return decodedBytes;
		}
		return null;
	}
	
	
	/** 
	 * Read the given binary file, and return its contents as a byte array. 
	 * @throws GoForWealthAdminException 
	 * 
	 * 
	*/
	public static byte[] read(String aInputFileName) throws GoForWealthAdminException {
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
			throw new GoForWealthAdminException(GoForWealthAdminErrorMessageEnum.ENCRYPTION_ERROR_CODE.getValue(), GoForWealthAdminErrorMessageEnum.ENCRYPTION_ERROR_MESSAGE.getValue());
		} catch (IOException ex) {
			logger.error(ex.getMessage());
			throw new GoForWealthAdminException(GoForWealthAdminErrorMessageEnum.ENCRYPTION_ERROR_CODE.getValue(), GoForWealthAdminErrorMessageEnum.ENCRYPTION_ERROR_MESSAGE.getValue());
		}
		return result;
	}

	/**
	 * Write a byte array to the given file. Writing binary data is
	 * significantly simpler than reading it.
	 * @throws GoForWealthAdminException 
	 */
	public static void write(byte[] aInput, String aOutputFileName) throws GoForWealthAdminException {
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
			throw new GoForWealthAdminException(GoForWealthAdminErrorMessageEnum.ENCRYPTION_ERROR_CODE.getValue(), GoForWealthAdminErrorMessageEnum.ENCRYPTION_ERROR_MESSAGE.getValue());
		} catch (IOException ex) {
			logger.error(ex.getMessage());
			throw new GoForWealthAdminException(GoForWealthAdminErrorMessageEnum.ENCRYPTION_ERROR_CODE.getValue(), GoForWealthAdminErrorMessageEnum.ENCRYPTION_ERROR_MESSAGE.getValue());
		}
	}
	
	public static void removeFile(List<String> fileNamesList) throws GoForWealthAdminException{
		for(String fileName : fileNamesList){
			File file = new File(fileName);
			if(file.delete()){
				logger.info(file.getName() + " is deleted during encryption!");
			}else{
				logger.info("File delete operation during is failed. : " + "FileName: " + fileName);
				throw new GoForWealthAdminException(GoForWealthAdminErrorMessageEnum.ENCRYPTION_ERROR_CODE.getValue(), GoForWealthAdminErrorMessageEnum.ENCRYPTION_ERROR_MESSAGE.getValue());
			}
		}
	}


	public static Map<String, Integer> validatePagination(int page, int limit,boolean accessFromDB) {
		int startIndex, endIndex;
		Map<String, Integer> paginationValuesMap = new HashMap<String, Integer>();
		if (limit != -1) {
			if (page < GoForWealthAdminConstants.ONE || limit < GoForWealthAdminConstants.ONE) {
				if (page == GoForWealthAdminConstants.ZERO && limit == GoForWealthAdminConstants.ZERO) {
					startIndex = GoForWealthAdminConstants.ONE;
					endIndex = GoForWealthAdminConstants.TEN;
				} else
					return null;
			} else {
				if (page == GoForWealthAdminConstants.ONE) {
					startIndex = page;
				} else {
					int lastPage = page - GoForWealthAdminConstants.ONE;
					startIndex = lastPage * limit + GoForWealthAdminConstants.ONE;
				}
				endIndex = limit;
			}
		} else {
			startIndex = GoForWealthAdminConstants.ONE;
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
		paginationValuesMap.put(GoForWealthAdminConstants.START_INDEX_KEY, startIndex);
		paginationValuesMap.put(GoForWealthAdminConstants.END_INDEX_KEY, endIndex);
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
			return GoForWealthAdminConstants.BLANK;
		encClientId = encClientId.substring(0, iccidStart);
		int imeiStart = encClientId.lastIndexOf(imei, encClientId.length());
		if(imeiStart == -1)
			return GoForWealthAdminConstants.BLANK;
		encClientId = encClientId.substring(0,imeiStart);
		return encClientId;
	}

	public static long currentTimeStampInUtc() {
		Calendar calendar=Calendar.getInstance(TimeZone.getTimeZone(GoForWealthAdminConstants.UTC));
		return calendar.getTimeInMillis();
	}

	public static long convertTimeStampToUtc(long timestamp) {
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
	
	
	public static String generateUUID() throws GoForWealthAdminException {
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
			throw new GoForWealthAdminException(GoForWealthAdminErrorMessageEnum.FAILURE_CODE.getValue(),
					GoForWealthAdminErrorMessageEnum.FAILURE_MESSAGE.getValue());
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
		if(GoForWealthAdminStringUtil.isNullOrEmpty(string))
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
	
	
	public static Workbook createExportFileForScheme(List<FundSchemeDTO> fundSchemeDtoList) {
		Workbook wb = new XSSFWorkbook();
		CreationHelper createHelper = wb.getCreationHelper();
		Sheet sheet = wb.createSheet("scheme_list");
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
		Row headerRow1 = sheet.createRow(index++);
		headerRow1.createCell(cellIndex++).setCellValue("Scheme Code");
		headerRow1.createCell(cellIndex++).setCellValue("Scheme Name");
		headerRow1.createCell(cellIndex++).setCellValue("Scheme Type");
		headerRow1.createCell(cellIndex++).setCellValue("Isin");
		headerRow1.createCell(cellIndex++).setCellValue("Purchase Allowed");
		headerRow1.createCell(cellIndex++).setCellValue("Minimum Purchase Amount");
		headerRow1.createCell(cellIndex++).setCellValue("Maximum Purchase Amount");
		headerRow1.createCell(cellIndex++).setCellValue("Redemption Allowed");
		headerRow1.createCell(cellIndex++).setCellValue("SIP Allowed");
		headerRow1.createCell(cellIndex++).setCellValue("Show Scheme");
		headerRow1.createCell(cellIndex++).setCellValue("Priority");
		for (short i = 0; i < cellIndex; i++) {
			headerRow1.getCell(i).setCellStyle(headerStyle);
		}
		for (FundSchemeDTO fundSchemeDto: fundSchemeDtoList) {
			cellIndex = 0;
			Row row = sheet.createRow(index++);
			GoForWealthAdminUtil.createCell(row, cellIndex++, fundSchemeDto.getSchemeCode());
			GoForWealthAdminUtil.createCell(row, cellIndex++, fundSchemeDto.getSchemeName());
			GoForWealthAdminUtil.createCell(row, cellIndex++, fundSchemeDto.getSchemeType());
			GoForWealthAdminUtil.createCell(row, cellIndex++, fundSchemeDto.getIsinCode());
			GoForWealthAdminUtil.createCell(row, cellIndex++, fundSchemeDto.getPurchaseAllowed());
			GoForWealthAdminUtil.createCell(row, cellIndex++, fundSchemeDto.getMinimumPurchaseAmount().toString());
			GoForWealthAdminUtil.createCell(row, cellIndex++, fundSchemeDto.getMaximumPurchaseAmount());
			GoForWealthAdminUtil.createCell(row, cellIndex++, fundSchemeDto.getRedemptionAllowed());
			GoForWealthAdminUtil.createCell(row, cellIndex++, fundSchemeDto.getSipAllowed());
			GoForWealthAdminUtil.createCell(row, cellIndex++, fundSchemeDto.getShowScheme());
			GoForWealthAdminUtil.createCell(row, cellIndex++, fundSchemeDto.getPriority());
		}
		return wb;
	}
	
	public static List<FundSchemeDTO> getExcelFromByte(byte[] excelByteArray){
		List<FundSchemeDTO> fundSchemeDtoList = new ArrayList<FundSchemeDTO>();
		ByteArrayInputStream bin = new ByteArrayInputStream(excelByteArray);
		try{
			int ctr = 1;
			Workbook wb = WorkbookFactory.create(bin);
			Sheet sheet = wb.getSheetAt(0);
			Row headRow = null;
			Row row = null;
			Cell headerCell = null;				
			Cell cell = null;		
			boolean isNull = false;
			do {
				try {
					headRow =sheet.getRow(0);
					row = sheet.getRow(ctr);
					FundSchemeDTO fundSchemeDto = new FundSchemeDTO();
					for(int i=0;i<row.getLastCellNum();i++) {
						cell = row.getCell(i);          	
						headerCell=headRow.getCell(i);
						if(headerCell.toString().equalsIgnoreCase("Scheme Code") && headerCell.toString()!=null && !cell.toString().equals(null)){
							fundSchemeDto.setSchemeCode(cell.toString());
						}
						if(headerCell.toString().equalsIgnoreCase("Scheme Name") && headerCell.toString()!=null && !cell.toString().equals(null)){
							fundSchemeDto.setSchemeName(cell.toString());
						}if(headerCell.toString().equalsIgnoreCase("Scheme Type") && headerCell.toString()!=null && !cell.toString().equals(null)){
							fundSchemeDto.setSchemeType(cell.toString());
						}if(headerCell.toString().equalsIgnoreCase("Purchase Allowed") && headerCell.toString()!=null && !cell.toString().equals(null)){
							fundSchemeDto.setPurchaseAllowed(cell.toString());
						}if(headerCell.toString().equalsIgnoreCase("Minimum Purchase Amount") && headerCell.toString()!=null && !cell.toString().equals(null)){
							fundSchemeDto.setMinimumPurchaseAmount(cell.getCellType());
						}if(headerCell.toString().equalsIgnoreCase("Maximum Purchase Amount") && headerCell.toString()!=null && !cell.toString().equals(null)){
							fundSchemeDto.setMaximumPurchaseAmount(cell.toString());
						}if(headerCell.toString().equalsIgnoreCase("Sip Allowed") && headerCell.toString()!=null && !cell.toString().equals(null)){
							fundSchemeDto.setSipAllowed(cell.toString());
						}if(headerCell.toString().equalsIgnoreCase("Priority") && headerCell.toString()!=null && !cell.toString().equals(null)){
							fundSchemeDto.setPriority(cell.toString());
						} 
						ctr++;
						if(!fundSchemeDto.equals(null)){
							fundSchemeDtoList.add(fundSchemeDto);
						}
					} 
				}catch(Exception e) {
					isNull = true;
				}
			}while(isNull!=true);     
			bin.close();
		}catch(Exception ex){
			ex.printStackTrace();
		}
		return fundSchemeDtoList;
	}
	
	public static Workbook createExportFileForReport(List<UsersListDTO> usersListDto) {
		Workbook wb = new XSSFWorkbook();
		CreationHelper createHelper = wb.getCreationHelper();
		Sheet sheet = wb.createSheet("registered_user_report");
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
		Row headerRow1 = sheet.createRow(index++);
		headerRow1.createCell(cellIndex++).setCellValue("Full Name");
		headerRow1.createCell(cellIndex++).setCellValue("Mobile Number");
		headerRow1.createCell(cellIndex++).setCellValue("Email");
		headerRow1.createCell(cellIndex++).setCellValue("Email Verified");
		headerRow1.createCell(cellIndex++).setCellValue("Mobile Verified");
		headerRow1.createCell(cellIndex++).setCellValue("Gender");
		headerRow1.createCell(cellIndex++).setCellValue("PAN Number");
		headerRow1.createCell(cellIndex++).setCellValue("PAN Verified");
		headerRow1.createCell(cellIndex++).setCellValue("Aadhaar Number");
		headerRow1.createCell(cellIndex++).setCellValue("Aadhaar Verified");
		headerRow1.createCell(cellIndex++).setCellValue("DOB");
		headerRow1.createCell(cellIndex++).setCellValue("State");
		headerRow1.createCell(cellIndex++).setCellValue("City");
		headerRow1.createCell(cellIndex++).setCellValue("Onboarding Stauts");
		for(short i = 0; i < cellIndex; i++) {
			headerRow1.getCell(i).setCellStyle(headerStyle);
		}
		for (UsersListDTO userLoginResponse : usersListDto) {
			cellIndex = 0;
			Row row = sheet.createRow(index++);
			GoForWealthAdminUtil.createCell(row,cellIndex++,userLoginResponse.getFullName());
			GoForWealthAdminUtil.createCell(row,cellIndex++,userLoginResponse.getMobileNumber());
			GoForWealthAdminUtil.createCell(row,cellIndex++,userLoginResponse.getEmail());
			GoForWealthAdminUtil.createCell(row,cellIndex++,userLoginResponse.getEmailVerified());
			GoForWealthAdminUtil.createCell(row,cellIndex++,userLoginResponse.getMobileVerified());
			GoForWealthAdminUtil.createCell(row,cellIndex++,userLoginResponse.getGender());
			GoForWealthAdminUtil.createCell(row,cellIndex++,userLoginResponse.getPanNumber());
			GoForWealthAdminUtil.createCell(row,cellIndex++,userLoginResponse.getPanVerified());
			GoForWealthAdminUtil.createCell(row,cellIndex++,userLoginResponse.getAadhaarNumber());
			GoForWealthAdminUtil.createCell(row,cellIndex++,userLoginResponse.getAadhaarVerified());
			GoForWealthAdminUtil.createCell(row,cellIndex++,userLoginResponse.getDob());
			GoForWealthAdminUtil.createCell(row,cellIndex++,userLoginResponse.getState());
			GoForWealthAdminUtil.createCell(row,cellIndex++,userLoginResponse.getCity());
			GoForWealthAdminUtil.createCell(row,cellIndex++,userLoginResponse.getPinCode());
		}
		return wb;
	}
	
	public static void createCell(Row row, short index, String value) {
		if (value == null) {
			row.createCell(index, CellType.BLANK);
		} else {
			row.createCell(index, CellType.STRING).setCellValue(value);
		}
	}
	
	public static void createCell(Row row, short index, Number value, CellStyle cellStyle) {
		if (value == null) {
			Cell cell = row.createCell(index, CellType.BLANK);
			if (cellStyle != null) {
				cell.setCellStyle(cellStyle);
			}
		} else {
			Cell cell = row.createCell(index, CellType.NUMERIC);
			cell.setCellValue(value.doubleValue());
			if (cellStyle != null) {
				cell.setCellStyle(cellStyle);
			}
		}
	}

	
	public static Workbook createExportFileForUserData(List<UserExportExcelDataDto> userExportExcelDataDtoList,String type) {
		Workbook wb = new XSSFWorkbook();
		CreationHelper createHelper = wb.getCreationHelper();
		Sheet sheet = null;
		if(type.equals("Registered User")){
			sheet = wb.createSheet("Registered User Report");
		}else if(type.equals("Onboarding Completed")){
			sheet = wb.createSheet("Onboarding Completed User Report");
		}else if(type.equals("Onboarding Not Completed")){
			sheet = wb.createSheet("Onboarding Not Completed User Report");
		}else if(type.equals("Kyc Completed")){
			sheet = wb.createSheet("Kyc Completed User Report");
		}else if(type.equals("Kyc Not Completed")){
			sheet = wb.createSheet("Kyc Not Completed User Report");
		}else if(type.equals("Today Registered User")){
			sheet = wb.createSheet("Today Registered User Report");
		}else if(type.equals("Today Onboarding Completed User")){
			sheet = wb.createSheet("Today Onboarding Completed User");
		}else if(type.equals("Today Kyc Completed User")){
			sheet = wb.createSheet("Today Kyc Completed User");
		}
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
		Row headerRow1 = sheet.createRow(index++);
		headerRow1.createCell(cellIndex++).setCellValue("First Name");
		headerRow1.createCell(cellIndex++).setCellValue("Last Name");
		headerRow1.createCell(cellIndex++).setCellValue("Mobile Number");
		headerRow1.createCell(cellIndex++).setCellValue("Email");
		headerRow1.createCell(cellIndex++).setCellValue("Email Verified");
		headerRow1.createCell(cellIndex++).setCellValue("Mobile Verified");
		headerRow1.createCell(cellIndex++).setCellValue("Registered Date");
		headerRow1.createCell(cellIndex++).setCellValue("Gender");
		headerRow1.createCell(cellIndex++).setCellValue("DOB");
		headerRow1.createCell(cellIndex++).setCellValue("PAN Number");
		headerRow1.createCell(cellIndex++).setCellValue("PAN Verified");
		headerRow1.createCell(cellIndex++).setCellValue("Occupation");
		headerRow1.createCell(cellIndex++).setCellValue("Mother Name");
		headerRow1.createCell(cellIndex++).setCellValue("Father Name");
		headerRow1.createCell(cellIndex++).setCellValue("Marital Status");
		headerRow1.createCell(cellIndex++).setCellValue("Client Code");
		headerRow1.createCell(cellIndex++).setCellValue("Onboarding Stauts");
		headerRow1.createCell(cellIndex++).setCellValue("Bank Name");
		headerRow1.createCell(cellIndex++).setCellValue("Bank Branch");
		headerRow1.createCell(cellIndex++).setCellValue("Ifsc Code");
		headerRow1.createCell(cellIndex++).setCellValue("Account Type");
		headerRow1.createCell(cellIndex++).setCellValue("Account Number");
		headerRow1.createCell(cellIndex++).setCellValue("Nominee Name");
		headerRow1.createCell(cellIndex++).setCellValue("Nominee Relation");
		headerRow1.createCell(cellIndex++).setCellValue("ISIP ID");
		headerRow1.createCell(cellIndex++).setCellValue("ISIP Status");
		headerRow1.createCell(cellIndex++).setCellValue("XSIP ID");
		headerRow1.createCell(cellIndex++).setCellValue("XSIP Status");
		headerRow1.createCell(cellIndex++).setCellValue("Address Type");
		headerRow1.createCell(cellIndex++).setCellValue("Address Line 1");
		headerRow1.createCell(cellIndex++).setCellValue("Address Line 2");
		headerRow1.createCell(cellIndex++).setCellValue("City");
		headerRow1.createCell(cellIndex++).setCellValue("State");
		headerRow1.createCell(cellIndex++).setCellValue("Pincode");
		headerRow1.createCell(cellIndex++).setCellValue("Income Slab");
		headerRow1.createCell(cellIndex++).setCellValue("Politically Exposed Person");
		
		for(short i = 0; i < cellIndex; i++) {
			headerRow1.getCell(i).setCellStyle(headerStyle);
		}
		for (UserExportExcelDataDto userExportExcelDataDto : userExportExcelDataDtoList) {
			cellIndex = 0;
			Row row = sheet.createRow(index++);
			GoForWealthAdminUtil.createCell(row,cellIndex++,userExportExcelDataDto.getFirstName());
			GoForWealthAdminUtil.createCell(row,cellIndex++,userExportExcelDataDto.getLastName());
			GoForWealthAdminUtil.createCell(row,cellIndex++,userExportExcelDataDto.getMobileNumber());
			GoForWealthAdminUtil.createCell(row,cellIndex++,userExportExcelDataDto.getEmail());
			GoForWealthAdminUtil.createCell(row,cellIndex++,userExportExcelDataDto.getEmailVerified());
			GoForWealthAdminUtil.createCell(row,cellIndex++,userExportExcelDataDto.getMobileVerified());
			GoForWealthAdminUtil.createCell(row,cellIndex++,userExportExcelDataDto.getCreatedTimestamp());
			GoForWealthAdminUtil.createCell(row,cellIndex++,userExportExcelDataDto.getGender());
			GoForWealthAdminUtil.createCell(row,cellIndex++,userExportExcelDataDto.getDateOfBirth());
			GoForWealthAdminUtil.createCell(row,cellIndex++,userExportExcelDataDto.getPanNumber());
			GoForWealthAdminUtil.createCell(row,cellIndex++,userExportExcelDataDto.getPanVerified());
			GoForWealthAdminUtil.createCell(row,cellIndex++,userExportExcelDataDto.getOccupation());
			GoForWealthAdminUtil.createCell(row,cellIndex++,userExportExcelDataDto.getMotherName());
			GoForWealthAdminUtil.createCell(row,cellIndex++,userExportExcelDataDto.getFatherName());
			GoForWealthAdminUtil.createCell(row,cellIndex++,userExportExcelDataDto.getMaritalStatus());
			GoForWealthAdminUtil.createCell(row,cellIndex++,userExportExcelDataDto.getClientCode());
			GoForWealthAdminUtil.createCell(row,cellIndex++,userExportExcelDataDto.getOverallStatus());
			GoForWealthAdminUtil.createCell(row,cellIndex++,userExportExcelDataDto.getBankName());
			GoForWealthAdminUtil.createCell(row,cellIndex++,userExportExcelDataDto.getBankBranch());
			GoForWealthAdminUtil.createCell(row,cellIndex++,userExportExcelDataDto.getIfsc());
			GoForWealthAdminUtil.createCell(row,cellIndex++,userExportExcelDataDto.getAccountType());
			GoForWealthAdminUtil.createCell(row,cellIndex++,userExportExcelDataDto.getAccountNo());
			GoForWealthAdminUtil.createCell(row,cellIndex++,userExportExcelDataDto.getNomineeName());
			GoForWealthAdminUtil.createCell(row,cellIndex++,userExportExcelDataDto.getNomineeRelation());
			GoForWealthAdminUtil.createCell(row,cellIndex++,userExportExcelDataDto.getIsipId());
			GoForWealthAdminUtil.createCell(row,cellIndex++,userExportExcelDataDto.getIsipStatus());
			GoForWealthAdminUtil.createCell(row,cellIndex++,userExportExcelDataDto.getXsipId());
			GoForWealthAdminUtil.createCell(row,cellIndex++,userExportExcelDataDto.getXsipStatus());
			GoForWealthAdminUtil.createCell(row,cellIndex++,userExportExcelDataDto.getAddressType());
			GoForWealthAdminUtil.createCell(row,cellIndex++,userExportExcelDataDto.getAddressLine1());
			GoForWealthAdminUtil.createCell(row,cellIndex++,userExportExcelDataDto.getAddressLine2());
			GoForWealthAdminUtil.createCell(row,cellIndex++,userExportExcelDataDto.getCity());
			GoForWealthAdminUtil.createCell(row,cellIndex++,userExportExcelDataDto.getState());
			GoForWealthAdminUtil.createCell(row,cellIndex++,userExportExcelDataDto.getPincode());
			GoForWealthAdminUtil.createCell(row,cellIndex++,userExportExcelDataDto.getIncomeSlab());
			GoForWealthAdminUtil.createCell(row,cellIndex++,userExportExcelDataDto.getPep());
		}
		return wb;
	}

	public static String formatDateWithPattern(Date value, String pattern) {
		if (value == null) {
			return null;
		}
		DateFormat formatter = new SimpleDateFormat(pattern);
		return formatter.format(value);
	}
	
	
	public static Workbook createExportFileForOrders(List<OrderExportExcelDto> OrderExportExcelDtoList,String type) {
		Workbook wb = new XSSFWorkbook();
		CreationHelper createHelper = wb.getCreationHelper();
		Sheet sheet =  null;
		if(type.equals("Canceled")){
			sheet = wb.createSheet("Canceled Orders Report");
		}else if(type.equals("Confirmed")){
			sheet = wb.createSheet("Confirmed Orders Report");
		}else if(type.equals("Purchased")){
			sheet = wb.createSheet("Purchased Orders Report");
		}else if(type.equals("Pending")){
			sheet = wb.createSheet("Payment Awaiting Orders Report");
		}
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
		Row headerRow1 = sheet.createRow(index++);
		headerRow1.createCell(cellIndex++).setCellValue("Order No");
		headerRow1.createCell(cellIndex++).setCellValue("Scheme Code");
		headerRow1.createCell(cellIndex++).setCellValue("Scheme Name");
		headerRow1.createCell(cellIndex++).setCellValue("Order Type");
		headerRow1.createCell(cellIndex++).setCellValue("Status");
		headerRow1.createCell(cellIndex++).setCellValue("Placed Date");
		headerRow1.createCell(cellIndex++).setCellValue("Amount");
		headerRow1.createCell(cellIndex++).setCellValue("Client Code");
		headerRow1.createCell(cellIndex++).setCellValue("Inv Name");
		headerRow1.createCell(cellIndex++).setCellValue("Inv Email");
		headerRow1.createCell(cellIndex++).setCellValue("Inv Mobile");
		headerRow1.createCell(cellIndex++).setCellValue("PAN No");
		headerRow1.createCell(cellIndex++).setCellValue("Occupation");
		headerRow1.createCell(cellIndex++).setCellValue("Marital Status");
		headerRow1.createCell(cellIndex++).setCellValue("Bank Name");
		headerRow1.createCell(cellIndex++).setCellValue("Bank Branch");
		headerRow1.createCell(cellIndex++).setCellValue("IFSC Code");
		headerRow1.createCell(cellIndex++).setCellValue("Account Type");
		headerRow1.createCell(cellIndex++).setCellValue("Account No");
		headerRow1.createCell(cellIndex++).setCellValue("Nominee Name");
		headerRow1.createCell(cellIndex++).setCellValue("Nominee Relation");
		headerRow1.createCell(cellIndex++).setCellValue("Income");
		headerRow1.createCell(cellIndex++).setCellValue("Father Name");
		headerRow1.createCell(cellIndex++).setCellValue("Mother Name");
		headerRow1.createCell(cellIndex++).setCellValue("Address Type");
		headerRow1.createCell(cellIndex++).setCellValue("City");
		headerRow1.createCell(cellIndex++).setCellValue("State");
		headerRow1.createCell(cellIndex++).setCellValue("Pincode");
		headerRow1.createCell(cellIndex++).setCellValue("Address Line 1");
		headerRow1.createCell(cellIndex++).setCellValue("Address Line 2");
		
		for(short i = 0; i < cellIndex; i++) {
			headerRow1.getCell(i).setCellStyle(headerStyle);
		}
		for (OrderExportExcelDto orderExportExcelDto : OrderExportExcelDtoList) {
			cellIndex = 0;
			Row row = sheet.createRow(index++);
			GoForWealthAdminUtil.createCell(row,cellIndex++,orderExportExcelDto.getOrderNo());
			GoForWealthAdminUtil.createCell(row,cellIndex++,orderExportExcelDto.getSchemeCode());
			GoForWealthAdminUtil.createCell(row,cellIndex++,orderExportExcelDto.getSchemeName());
			GoForWealthAdminUtil.createCell(row,cellIndex++,orderExportExcelDto.getType());
			GoForWealthAdminUtil.createCell(row,cellIndex++,orderExportExcelDto.getStatus());
			GoForWealthAdminUtil.createCell(row,cellIndex++,orderExportExcelDto.getOrderDate());
			GoForWealthAdminUtil.createCell(row,cellIndex++,orderExportExcelDto.getAmount());
			GoForWealthAdminUtil.createCell(row,cellIndex++,orderExportExcelDto.getInvesterId());
			GoForWealthAdminUtil.createCell(row,cellIndex++,orderExportExcelDto.getInvesterName());
			GoForWealthAdminUtil.createCell(row,cellIndex++,orderExportExcelDto.getEmail());
			GoForWealthAdminUtil.createCell(row,cellIndex++,orderExportExcelDto.getMobileNo());
			GoForWealthAdminUtil.createCell(row,cellIndex++,orderExportExcelDto.getPan());
			GoForWealthAdminUtil.createCell(row,cellIndex++,orderExportExcelDto.getOccupation());
			GoForWealthAdminUtil.createCell(row,cellIndex++,orderExportExcelDto.getMaritalStatus());
			GoForWealthAdminUtil.createCell(row,cellIndex++,orderExportExcelDto.getBankName());
			GoForWealthAdminUtil.createCell(row,cellIndex++,orderExportExcelDto.getBankBranch());
			GoForWealthAdminUtil.createCell(row,cellIndex++,orderExportExcelDto.getIfscCode());
			GoForWealthAdminUtil.createCell(row,cellIndex++,orderExportExcelDto.getAccountType());
			GoForWealthAdminUtil.createCell(row,cellIndex++,orderExportExcelDto.getAccountNumber());
			GoForWealthAdminUtil.createCell(row,cellIndex++,orderExportExcelDto.getNomineeName());
			GoForWealthAdminUtil.createCell(row,cellIndex++,orderExportExcelDto.getNomineeRelation());
			GoForWealthAdminUtil.createCell(row,cellIndex++,orderExportExcelDto.getIncomeSlab());
			GoForWealthAdminUtil.createCell(row,cellIndex++,orderExportExcelDto.getFatherName());
			GoForWealthAdminUtil.createCell(row,cellIndex++,orderExportExcelDto.getMotherName());
			GoForWealthAdminUtil.createCell(row,cellIndex++,orderExportExcelDto.getAddressType());
			GoForWealthAdminUtil.createCell(row,cellIndex++,orderExportExcelDto.getCity());
			GoForWealthAdminUtil.createCell(row,cellIndex++,orderExportExcelDto.getState());
			GoForWealthAdminUtil.createCell(row,cellIndex++,orderExportExcelDto.getPincode());
			GoForWealthAdminUtil.createCell(row,cellIndex++,orderExportExcelDto.getAddressLine1());
			GoForWealthAdminUtil.createCell(row,cellIndex++,orderExportExcelDto.getAddressLine2());
		}
		return wb;
	}


}
