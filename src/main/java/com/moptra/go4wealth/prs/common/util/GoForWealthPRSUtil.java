package com.moptra.go4wealth.prs.common.util;

import java.awt.Color;
import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.StringWriter;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.sql.Timestamp;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Collection;
import java.util.Comparator;
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
import java.util.concurrent.TimeUnit;

import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import javax.servlet.http.HttpServletRequest;
import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;

import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.pdmodel.PDPage;
import org.apache.pdfbox.pdmodel.PDPageContentStream;
import org.apache.pdfbox.pdmodel.PDPageTree;
import org.apache.pdfbox.pdmodel.font.PDFont;
import org.apache.pdfbox.pdmodel.font.PDType0Font;
import org.apache.pdfbox.pdmodel.graphics.image.PDImageXObject;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
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
import com.moptra.go4wealth.prs.common.constant.GoForWealthPRSConstants;
import com.moptra.go4wealth.prs.common.enums.GoForWealthPRSErrorMessageEnum;
import com.moptra.go4wealth.prs.common.exception.GoForWealthPRSException;
import com.moptra.go4wealth.prs.common.rest.GoForWealthPRSResponseInfo;
import com.moptra.go4wealth.prs.imageupload.FileUploadRequest;
import com.moptra.go4wealth.prs.imageupload.GetPasswordRequest;
import com.moptra.go4wealth.prs.imageupload.MandateFormUploadRequest;
import com.moptra.go4wealth.prs.model.AOFPdfFormRequestDTO;
import com.moptra.go4wealth.prs.model.MandatePdfFormRequestDTO;
import com.moptra.go4wealth.security.UserSession;

import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;

public class GoForWealthPRSUtil {

	private static Logger logger = LoggerFactory.getLogger(GoForWealthPRSUtil.class);

	private static boolean loadPropertiesFile(String fileName,Properties properties){
		InputStream inputStream = null;
		try {
			logger.info("In loadPropertiesFile()");
			// Load properties file
			inputStream = GoForWealthPRSUtil.class.getClassLoader().getResourceAsStream(fileName);
			if(GoForWealthPRSStringUtil.isNull(inputStream)){
				logger.error("Could not read the property file : " + fileName);
				throw new FileNotFoundException();
			}else{
				properties.load(inputStream);
				logger.info("Properties file available : " + fileName);
				return true;
			}
		} catch (FileNotFoundException e) {
			logger.error("Properties file not found" + fileName, e);
			if(GoForWealthPRSStringUtil.isNull(inputStream)){
				logger.info("Loading property file for junits using relative path.");
				try {
					inputStream = GoForWealthPRSUtil.class.getClassLoader().getResourceAsStream(GoForWealthPRSConstants.PROPERTIES_FILE_PATH_PREFIX_FOR_JUNIT + fileName);
					if(GoForWealthPRSStringUtil.isNull(inputStream)){
						logger.error("Could not read the property file : " + fileName);
						throw new FileNotFoundException();
					}else{
						properties.load(inputStream);
						logger.info("Properties file available : " + GoForWealthPRSConstants.PROPERTIES_FILE_PATH_PREFIX_FOR_JUNIT + fileName);
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
				if(!GoForWealthPRSStringUtil.isNull(inputStream)){
					inputStream.close();
				}
			} catch (IOException e) {
				logger.error("exception while closing input stream for Properties file" + fileName,e);
			}
		}
		return false;
	}

	public static GoForWealthPRSResponseInfo getSuccessResponseInfo(String status,String message, Object data) {
		GoForWealthPRSResponseInfo responseInfo = new GoForWealthPRSResponseInfo();
		responseInfo.setStatus(status);
		responseInfo.setMessage(message);
		if (!GoForWealthPRSStringUtil.isNull(data))
			responseInfo.setData(data);
		return responseInfo;
	}

	public static GoForWealthPRSResponseInfo getErrorResponseInfo(String status, String message, Exception e) {
		logger.error("Exception occurred = ", e);
		GoForWealthPRSResponseInfo responseInfo = new GoForWealthPRSResponseInfo();
		responseInfo.setStatus(status);
		responseInfo.setMessage(message);
		responseInfo.setData(GoForWealthPRSConstants.BLANK);
		responseInfo.setTotalRecords(GoForWealthPRSConstants.ZERO);
		return responseInfo;
	}

	public static GoForWealthPRSResponseInfo getExceptionResponseInfo(GoForWealthPRSException e) {
		logger.error("Exception occurred = ", e);
		GoForWealthPRSResponseInfo responseInfo = new GoForWealthPRSResponseInfo();
		responseInfo.setStatus(e.getErrorCode());
		responseInfo.setMessage(e.getErrorMessage());
		responseInfo.setData(GoForWealthPRSConstants.BLANK);
		responseInfo.setTotalRecords(GoForWealthPRSConstants.ZERO);
		return responseInfo;
	}

	public static GoForWealthPRSResponseInfo getExceptionResponseInfo(GoForWealthPRSException e, Object data) {
		logger.error("Exception occurred = ", e);
		GoForWealthPRSResponseInfo responseInfo = new GoForWealthPRSResponseInfo();
		responseInfo.setStatus(e.getErrorCode());
		responseInfo.setMessage(e.getErrorMessage());
		responseInfo.setData(data);
		responseInfo.setTotalRecords(GoForWealthPRSConstants.ZERO);
		return responseInfo;
	}

	public static void logRequest(HttpServletRequest request, HttpHeaders headers, Object obj) {
		try {
			if (!GoForWealthPRSStringUtil.isNull(request)) {
				logger.info("REST API Called: " + request.getRequestURL()+GoForWealthPRSConstants.QUESTION_MARK+request.getQueryString() );
				logger.info("Request Headers Start:-");
				if (!GoForWealthPRSStringUtil.isNull(headers)) {
					for (Entry<String, List<String>> entry : headers.entrySet()) {
						logger.info(entry.getKey() + ": "	+ entry.getValue());
					}
				}
				logger.info("Request Headers End:-");
			}
			if(!GoForWealthPRSStringUtil.isNull(obj)) {
				String requestPayload = getJsonFromObject(obj, null);
				logger.info("REST Requet Payload: " + requestPayload );
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public static String getJsonFromObject(Object obj, Set<String> propertiesToIgnore) throws JsonProcessingException {
		@JsonFilter("by name")
		abstract class IgnoreStringsMixIn{ }  
		String requestPayload=null;
		if(!GoForWealthPRSStringUtil.isNull(obj)) {
			ObjectMapper mapper=new ObjectMapper();
			if(!GoForWealthPRSUtil.isEmptyCollection(propertiesToIgnore)) {
				mapper.addMixInAnnotations(Object.class,IgnoreStringsMixIn.class );
				mapper.setSerializationInclusion(Include.NON_NULL);
				ObjectWriter writer = mapper.writer(new SimpleFilterProvider().addFilter("by name", SimpleBeanPropertyFilter.serializeAllExcept(propertiesToIgnore)));
				requestPayload = writer.writeValueAsString(obj); 
			}else{
				requestPayload=mapper.writeValueAsString(obj);
			}
		}
		return requestPayload;
	}

	public static Object getObjectFromJson(String json, Class className) throws IOException {
		if (!GoForWealthPRSStringUtil.isNull(className) && !GoForWealthPRSStringUtil.isNull(json)) {
			ObjectMapper mapper = new ObjectMapper();
			Object object = mapper.readValue(json, className);
			return object;
		} else {
			return null;
		}
	}

	public static void logResponse(Object responseInfo,HttpHeaders headers) {
		try {
			//TODO Change in toString method of response object
			logger.info("Response Info Start: ");
			if (!GoForWealthPRSStringUtil.isNull(responseInfo)) {
				if (!GoForWealthPRSStringUtil.isNull(headers)) {
						for (Entry<String, List<String>> entry : headers.entrySet()) {
							logger.info("Request Param Name = " + entry.getKey() + "Request Param Value = " + entry.getValue());
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

	public static String getValueFromHeader(HttpHeaders headers,String headerKey) throws GoForWealthPRSException {
		logger.info("In getValueFromHeader()");
		List<String> headerValues = headers.get(headerKey);
		if (GoForWealthPRSStringUtil.isNull(headerValues) || GoForWealthPRSUtil.isEmptyCollection(headerValues) || headerValues.size() > 1) {
			logger.error("Either no header present or multiple headers present");
			throw new GoForWealthPRSException(GoForWealthPRSErrorMessageEnum.INVALID_HEADER_CODE.getValue(),GoForWealthPRSErrorMessageEnum.INVALID_HEADER_MESSAGE.getValue());
		}
		return headerValues.get(0);
	}

	public static List<String> getOptionalValueFromHeader(HttpHeaders headers,String headerKey) {
		logger.info("In getOptionalValueFromHeader()");
		List<String> headerValues = headers.get(headerKey);
		logger.info("Out getOptionalValueFromHeader()");
		return headerValues;
	}

	/**
	 * Method will return true if collection is empty , false otherwise
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
		if(!GoForWealthPRSStringUtil.isNull(inputBytes) && inputBytes.length>0){
			byte[] encodedBytes = Base64.encodeBase64(inputBytes);
			String encodedString = new String(encodedBytes);
			return encodedString;
		}
		return null;
	}

	public static byte[] decodeBase64(String encodedString){
		if(!GoForWealthPRSStringUtil.isNullOrEmpty(encodedString)){
			byte[] decodedBytes = Base64.decodeBase64(encodedString.getBytes());
			return decodedBytes;
		}
		return null;
	}

	/* Read the given binary file, and return its contents as a byte array. 
	 * @throws GoForWealthPRSException  
	*/
	public static byte[] read(String aInputFileName) throws GoForWealthPRSException {
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
					int bytesRead = input.read(result, totalBytesRead,bytesRemaining);
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
			throw new GoForWealthPRSException(GoForWealthPRSErrorMessageEnum.ENCRYPTION_ERROR_CODE.getValue(), GoForWealthPRSErrorMessageEnum.ENCRYPTION_ERROR_MESSAGE.getValue());
		} catch (IOException ex) {
			logger.error(ex.getMessage());
			throw new GoForWealthPRSException(GoForWealthPRSErrorMessageEnum.ENCRYPTION_ERROR_CODE.getValue(), GoForWealthPRSErrorMessageEnum.ENCRYPTION_ERROR_MESSAGE.getValue());
		}
		return result;
	}

	/**
	 * Write a byte array to the given file. Writing binary data is
	 * significantly simpler than reading it.
	 * @throws GoForWealthPRSException 
	*/
	public static void write(byte[] aInput, String aOutputFileName) throws GoForWealthPRSException {
		logger.info("Writing binary file : " + aOutputFileName);
		try {
			OutputStream output = null;
			try {
				output = new BufferedOutputStream(new FileOutputStream(aOutputFileName));
				output.write(aInput);
			} finally {
				output.close();
			}
		} catch (FileNotFoundException ex) {
			logger.error("File with name " + aOutputFileName + " not found for encryption.");
			throw new GoForWealthPRSException(GoForWealthPRSErrorMessageEnum.ENCRYPTION_ERROR_CODE.getValue(), GoForWealthPRSErrorMessageEnum.ENCRYPTION_ERROR_MESSAGE.getValue());
		} catch (IOException ex) {
			logger.error(ex.getMessage());
			throw new GoForWealthPRSException(GoForWealthPRSErrorMessageEnum.ENCRYPTION_ERROR_CODE.getValue(), GoForWealthPRSErrorMessageEnum.ENCRYPTION_ERROR_MESSAGE.getValue());
		}
	}

	public static void removeFile(List<String> fileNamesList) throws GoForWealthPRSException{
		for(String fileName : fileNamesList){
			File file = new File(fileName);
			if(file.delete()){
				logger.info(file.getName() + " is deleted during encryption!");
			}else{
				logger.info("File delete operation during is failed. : " + "FileName: " + fileName);
				throw new GoForWealthPRSException(GoForWealthPRSErrorMessageEnum.ENCRYPTION_ERROR_CODE.getValue(), GoForWealthPRSErrorMessageEnum.ENCRYPTION_ERROR_MESSAGE.getValue());
			}
		}
	}

	public static Map<String, Integer> validatePagination(int page, int limit,boolean accessFromDB) {
		int startIndex, endIndex;
		Map<String, Integer> paginationValuesMap = new HashMap<String, Integer>();
		if (limit != -1) {
			if (page < GoForWealthPRSConstants.ONE || limit < GoForWealthPRSConstants.ONE) {
				if (page == GoForWealthPRSConstants.ZERO && limit == GoForWealthPRSConstants.ZERO) {
					startIndex = GoForWealthPRSConstants.ONE;
					endIndex = GoForWealthPRSConstants.TEN;
				} else
					return null;
			} else {
				if (page == GoForWealthPRSConstants.ONE) {
					startIndex = page;
				} else {
					int lastPage = page - GoForWealthPRSConstants.ONE;
					startIndex = lastPage * limit + GoForWealthPRSConstants.ONE;
				}
				endIndex = limit;
			}
		} else {
			startIndex = GoForWealthPRSConstants.ONE;
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
		paginationValuesMap.put(GoForWealthPRSConstants.START_INDEX_KEY, startIndex);
		paginationValuesMap.put(GoForWealthPRSConstants.END_INDEX_KEY, endIndex);
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
			return GoForWealthPRSConstants.BLANK;
		encClientId = encClientId.substring(0, iccidStart);
		int imeiStart = encClientId.lastIndexOf(imei, encClientId.length());
		if(imeiStart == -1)
			return GoForWealthPRSConstants.BLANK;
		encClientId = encClientId.substring(0,imeiStart);
		return encClientId;
	}

	public static long currentTimeStampInUtc(){
		Calendar calendar=Calendar.getInstance(TimeZone.getTimeZone(GoForWealthPRSConstants.UTC));
		return calendar.getTimeInMillis();
	}

	public static long convertTimeStampToUtc(long timestamp){
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

	public static String parseListForProcedure(List<String> list) {
		String parsedString = "";
		String str = null;
		if (list == null || (list != null && list.size() == 0)){
			return null;
		}
		Iterator<String> itr = list.iterator();
		while (itr.hasNext()){
			str = itr.next();
			parsedString = parsedString + str + ",";
		}
		if (parsedString.indexOf(",") != -1){
			parsedString = parsedString.substring(0, parsedString.length() - 1);
		}
		//parsedString = parsedString + "'";
		return parsedString;
	}

	public static boolean isEmpty(String dataStr){
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
	public static String obj2Str(Object obj){
		String str = "";
		if (obj != null){
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
		for(byte byt:bytes){
			checksum.append(byt);
		}
		return checksum.toString();
	}

	public static String generateUUID() throws GoForWealthPRSException {
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
			throw new GoForWealthPRSException(GoForWealthPRSErrorMessageEnum.FAILURE_CODE.getValue(),GoForWealthPRSErrorMessageEnum.FAILURE_MESSAGE.getValue());
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
		if(GoForWealthPRSStringUtil.isNullOrEmpty(string))
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

	public static String generateAofFormPdf(AOFPdfFormRequestDTO aof, String pdfPath) throws IOException{
		PDDocument document = PDDocument.load(GoForWealthPRSUtil.class.getClassLoader().getResourceAsStream("pdfTemplates/AOF_FORM_TEMPLATE.pdf"));
        /*please uncomment below code get text positions*/
        /*PDFTextStripper stripper = new PDFTextStripper()
        	 {
                 @Override
                 protected void startPage(PDPage page) throws IOException
                 {
                     startOfLine = true;
                     super.startPage(page);
                 }

                 @Override
                 protected void writeLineSeparator() throws IOException
                 {
                     startOfLine = true;
                     super.writeLineSeparator();
                 }

                 @Override
                 protected void writeString(String text, List<TextPosition> textPositions) throws IOException
                 {
                     
                    		 TextPosition firstProsition = textPositions.get(0);
                             writeString(String.format("[x=%s]", firstProsition.getXDirAdj()));
                             writeString(String.format("[y=%s]", firstProsition.getYDirAdj()));
                     super.writeString(text, textPositions);
                 }
                 boolean startOfLine = true;
             };
        	
        
        stripper.getTextLineMatrix();
        stripper.getTextMatrix();
        String text = stripper.getText(document);
        System.out.println(text);*/
        PDPageTree pages =  document.getDocumentCatalog().getPages();
        for(int i=0;i<pages.getCount();i++){
        	PDPage page=document.getDocumentCatalog().getPages().get(i);
        	fillAofFormPdf(document, page,i,aof);	
        }
        ByteArrayOutputStream output = new ByteArrayOutputStream();
        //document.save(output);
        
        document.save(new File(pdfPath));
        document.close();
        return pdfPath;
    }

	private static void fillAofFormPdf(PDDocument document, PDPage page, int pageNum, AOFPdfFormRequestDTO aof) throws IOException {
		PDPageContentStream contentStream = new PDPageContentStream(document, page,true,true);

    	float pageHeight = page.getCropBox().getHeight();
    	page.getCropBox().getWidth();
    	float textLeft = 0;
    	float textTop = pageHeight;
    	if(pageNum==0){
    		contentStream.setFont(loadFont(document), 11);
    		
    		//[x=12.0][y=109.5]Broker/Agent Code ARN: [x=155.75][y=109.5]12345 [x=299.5][y=109.5]SUB-BROKER [x=443.25][y=109.5]EUIN
    		drawStringAtPosition(contentStream, textLeft+155.75f, textTop-109.5f,aof.getArnCode());
    		drawStringAtPosition(contentStream, textLeft+371.38f, textTop-109.5f,aof.getSubBrokerCode());
    		drawStringAtPosition(contentStream, textLeft+515.0f, textTop-109.5f,aof.getEuin());
    		/*[x=12.0][y=128.85999]Unit Folder Information
    		[x=12.0][y=148.22998]Name of the First Applicant : [x=155.75][y=148.22998]KRISHAN KUMAR*/
    		drawStringAtPosition(contentStream, textLeft+155.75f, textTop-148.2f,aof.getFirstApplicant());
    		
    		//[x=12.0][y=167.59003]PAN Number :     AXTPK8600J [x=227.63][y=167.59003]KYC :  Date Of Birth :  15-08-1984
    		drawStringAtPosition(contentStream, textLeft+120.75f, textTop-167.59003f,aof.getPanNumber1());
    		drawStringAtPosition(contentStream, textLeft+227.63f+32.5f, textTop-167.59003f,aof.getKyc1());
    		drawStringAtPosition(contentStream, textLeft+468.88f, textTop-167.59003f,aof.getDob1());
    		
    		//[x=12.0][y=186.95001]Name of Guardian:    PAN: 
    		drawStringAtPosition(contentStream, textLeft+120.75f, textTop-186.95001f,aof.getNameOfGuardian());
    		
    		//[x=12.0][y=206.31]Contact Address:   H.no. 871 A
    		drawStringAtPosition(contentStream, textLeft+120.75f, textTop-206.31f,aof.getAddress());
    		
    		//[x=12.0][y=225.68]Bhoor colony
    		drawStringAtPosition(contentStream, textLeft+125.0f, textTop-225.68f,aof.getAddressLine1());
    		
    		//[x=12.0][y=245.03998]Gali no. 5 
    		drawStringAtPosition(contentStream, textLeft+125.0f, textTop-245.03998f,aof.getAddressLine2());
    		
    		//[x=12.0][y=264.40002]City:   Faridabad [x=155.75][y=264.40002]Pincode:   121001 [x=299.5][y=264.40002]State:  HARYANA [x=443.25][y=264.40002]Country:   INDIA
    		drawStringAtPosition(contentStream, textLeft+44.5f, textTop-264.40002f,aof.getCity1());
    		drawStringAtPosition(contentStream, textLeft+201.2f, textTop-264.40002f,aof.getPincode1());
    		drawStringAtPosition(contentStream, textLeft+338.5f, textTop-264.40002f,aof.getState1());
    		drawStringAtPosition(contentStream, textLeft+495.25f, textTop-264.40002f,aof.getCountry1());
    		
    		//[x=12.0][y=283.77002]Tel.(Off):  Tel.(Res):  Email:  singla.krishan@gmail.com
    		drawStringAtPosition(contentStream, textLeft+410.0f, textTop-283.77002f,aof.getEmail());
    		//[x=12.0][y=303.13]Fax(Off):  Fax(Res):  Mobile:  9871252962
    		drawStringAtPosition(contentStream, textLeft+416.88f, textTop-303.13f,aof.getMobile());
    		
    		//[x=12.0][y=322.49]Mode of Holding:  SINGLE [x=371.38][y=322.49]Occupation:  SERVICE
    		drawStringAtPosition(contentStream, textLeft+125.0f, textTop-322.49f,aof.getModeOfHolding());
    		drawStringAtPosition(contentStream, textLeft+442.88f, textTop-322.49f,aof.getOccupation());
    		
    		//[x=12.0][y=341.86]Name of the Second Applicant :
    		drawStringAtPosition(contentStream, textLeft+155.75f, textTop-341.86f,aof.getSecondApplicant());
    		
    		//[x=12.0][y=361.22]PAN Number :  KYC :  Date Of Birth : 
    		drawStringAtPosition(contentStream, textLeft+90.0f, textTop-361.22f,aof.getPanNumber2());
    		drawStringAtPosition(contentStream, textLeft+299.0f, textTop-361.22f,aof.getPanNumber2());
    		drawStringAtPosition(contentStream, textLeft+442.88f, textTop-361.22f,aof.getDob2());
    		
    		//[x=12.0][y=380.58]Name of the Third Applicant :
    		drawStringAtPosition(contentStream, textLeft+155.75f, textTop-380.58f,aof.getThirdApplicant());
    		
    		//[x=12.0][y=399.94]PAN Number :  KYC :  Date Of Birth :
    		drawStringAtPosition(contentStream, textLeft+90.0f, textTop-399.94f,aof.getPanNumber3());
    		drawStringAtPosition(contentStream, textLeft+299.0f, textTop-399.94f,aof.getKyc3());
    		drawStringAtPosition(contentStream, textLeft+442.88f, textTop-399.94f,aof.getDob3());
    		
    		//[x=12.0][y=419.31]Other Details of Sole / 1st Applicant
    		drawStringAtPosition(contentStream, textLeft+190.0f, textTop-419.31f,aof.getOtherDetails());
    		
    		//[x=12.0][y=438.67]Overseas Address(In case of NRI Investor):
    		drawStringAtPosition(contentStream, textLeft+190f, textTop-438.67f,aof.getOverseasAddress());
    		
    		//[x=12.0][y=458.03]City:  Pincode:  Country:
    		drawStringAtPosition(contentStream, textLeft+44.0f, textTop-458.03f,aof.getOtherCity());
    		drawStringAtPosition(contentStream, textLeft+155.75f+52.0f, textTop-458.03f,aof.getOtherPincode());
    		drawStringAtPosition(contentStream, textLeft+442.0f, textTop-458.03f,aof.getOtherCountry());
    		
    		//[x=12.0][y=477.4]Bank Mandate Details
    		//[x=12.0][y=496.76]Name of Bank:  ICICI Bank - Retail Net Banking [x=371.38][y=496.76]Branch:  NEW DELHI - JORBAGH?�
    		drawStringAtPosition(contentStream, textLeft+96.0f, textTop-496.76f,aof.getNameOfBank());
    		drawStringAtPosition(contentStream, textLeft+371.38f+45.5f, textTop-496.76f,aof.getBranch());
    		
    		//[x=12.0][y=516.12]A/C No.:  028601504171 [x=155.75][y=516.12]A/C Type:  SAVINGS [x=371.38][y=516.12]IFSC Code:  ICIC0000286
    		drawStringAtPosition(contentStream, textLeft+12.0f+52.0f, textTop-516.12f,aof.getAccountNo());
    		drawStringAtPosition(contentStream, textLeft+155.75f+52.0f, textTop-516.12f,aof.getAccountType());
    		drawStringAtPosition(contentStream, textLeft+371.38f+65.0f, textTop-516.12f,aof.getIfscCode());
    		
    		//[x=12.0][y=535.49]Bank Address:  11 JOR BAGH MARKET, DELHI?�110003?�
    		drawStringAtPosition(contentStream, textLeft+12.0f+84.5f, textTop-535.49f,aof.getBankAddress());
    		
    		//[x=12.0][y=554.85]City:  Pincode:  State:  DELHI [x=443.25][y=554.85]Country:  INDIA
    		drawStringAtPosition(contentStream, textLeft+44.5f, textTop-554.85f,aof.getBankCity());
    		drawStringAtPosition(contentStream, textLeft+201.2f, textTop-554.85f,aof.getBankPincode());
    		drawStringAtPosition(contentStream, textLeft+338.5f, textTop-554.85f,aof.getBankState());
    		drawStringAtPosition(contentStream, textLeft+495.25f, textTop-554.85f,aof.getBankCountry());
    		
    		//[x=12.0][y=574.20996]Nomination Details
    		//[x=12.0][y=593.57]Nominee Name:  Relationship: 
    		drawStringAtPosition(contentStream, textLeft+125f+58.5f, textTop-593.57f,aof.getNomineeName());
    		drawStringAtPosition(contentStream, textLeft+371.38f+65.0f, textTop-593.57f,aof.getRelationship());
    		
    		//[x=12.0][y=612.94]Guardian Name(If Nominee is Minor):
    		drawStringAtPosition(contentStream, textLeft+12.0f+157.5f, textTop-612.94f,aof.getGurdianName());	
    		
    		//[x=12.0][y=632.3]Nominee Address:
    		drawStringAtPosition(contentStream, textLeft+12.0f+157.5f, textTop-632.3f,aof.getNomineeAddress());
    		
    		//[x=12.0][y=651.66003]City:  Pincode:  State:
    		drawStringAtPosition(contentStream, textLeft+44.0f, textTop-651.66003f,aof.getNomineecity());
    		drawStringAtPosition(contentStream, textLeft+155.75f+52.0f, textTop-651.66003f,aof.getNomineePincode());
    		drawStringAtPosition(contentStream, textLeft+442.0f, textTop-651.66003f,aof.getNomineeState());
    		
    		//[x=12.0][y=730.89]Date : [x=299.5][y=730.89]Place :
    		drawStringAtPosition(contentStream, textLeft+12.0f+25.0f, textTop-730.89f,aof.getDate());
    		drawStringAtPosition(contentStream, textLeft+299.5f+31.5f, textTop-730.89f,aof.getPlace());
    		
    		PDImageXObject pdImage = PDImageXObject.createFromFile(aof.getFirstApplicantSignature(), document);
    		//[x=12.0][y=671.03]Declaration and Signature
    		//contentStream.beginText();
    		//contentStream.newLineAtOffset(textLeft-12.0f, textTop-671f);
    		//[x=12.0][y=779.86]1st applicant Signature : [x=155.75][y=779.86]2nd applicant Signature : [x=371.38][y=779.86]3rd applicant Signature :*/
    		contentStream.drawImage(pdImage,textLeft+12.0f, textTop-770.86f, 143f, 35f);
    		//contentStream.endText();
    		/*  
    		
    		[x=12.0][y=684.53]I/We confirm that details provided by me/us are true and correct. The ARN holder has disclosed to me/us all the commission (In the form of trail
    		[x=12.0][y=698.03]commission or any other mode), payable to him for the different competing Schemes of various Mutual Fund From amongst which the scheme
    		[x=12.0][y=711.53]is being recommended to me/us.
    		
    		[x=578.83][y=759.25].
    		[x=12.0][y=779.86]1st applicant Signature : [x=155.75][y=779.86]2nd applicant Signature : [x=371.38][y=779.86]3rd applicant Signature :*/
    		//[x=97.6][y=79.25]S163234 [x=282.45][y=79.25]21654 [x=459.85][y=79.25]1502981
    		
    		//please increase x position like x=12+88
    		
    		//[x=155.75][y=148.22998]KRISHAN KUMAR
    	}
    	contentStream.close();
	}
	
	private static PDFont loadFont(PDDocument document) throws IOException {
        // Create a new font object by loading a TrueType font into the document
    	PDFont font = PDType0Font.load(document, GoForWealthPRSUtil.class.getClassLoader().getResourceAsStream("fonts/Gentium-I.ttf"));	
    	return font;
    }

    private static void drawStringAtPosition(PDPageContentStream contentStream, float textLeft, float textTop, String text) throws IOException {
        if (!GoForWealthPRSStringUtil.isNullOrEmpty(text)) {
            contentStream.beginText();
            contentStream.newLineAtOffset(textLeft, textTop);
            contentStream.showText(text);
            contentStream.setStrokingColor(Color.BLACK);
            contentStream.setNonStrokingColor(Color.BLACK);
            contentStream.endText();
        }
    }
    
    
    public static String jaxbObjectToXMLForPassword(GetPasswordRequest passwordRequest) {
	    String xmlString = "";
	    try {
	        JAXBContext context = JAXBContext.newInstance(GetPasswordRequest.class);
	        Marshaller m = context.createMarshaller();
	        
	        m.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, Boolean.TRUE); // To format XML

	        StringWriter sw = new StringWriter();
	        m.marshal(passwordRequest, sw);
	        xmlString = sw.toString();

	    } catch (JAXBException e) {
	        e.printStackTrace();
	    }

	    return xmlString;
	}
	
	public static String jaxbObjectToXMLForUpload(FileUploadRequest fileUploadRequest) {
	    String xmlString = "";
	    try {
	        JAXBContext context = JAXBContext.newInstance(FileUploadRequest.class);
	        Marshaller m = context.createMarshaller();
	        
	        m.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, Boolean.TRUE); // To format XML

	        StringWriter sw = new StringWriter();
	        m.marshal(fileUploadRequest, sw);
	        xmlString = sw.toString();

	    } catch (JAXBException e) {
	        e.printStackTrace();
	    }

	    return xmlString;
	}
	
	
	public static String jaxbObjectToXMLForMandateUpload(MandateFormUploadRequest mandateFormUploadRequest) {
	    String xmlString = "";
	    try {
	        JAXBContext context = JAXBContext.newInstance(MandateFormUploadRequest.class);
	        Marshaller m = context.createMarshaller();
	        
	        m.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, Boolean.TRUE); // To format XML

	        StringWriter sw = new StringWriter();
	        m.marshal(mandateFormUploadRequest, sw);
	        xmlString = sw.toString();

	    } catch (JAXBException e) {
	        e.printStackTrace();
	    }

	    return xmlString;
	}
	
	public static byte[] fileToByteArray(String path) throws IOException {
	    File imagefile = new File(path);
	    byte[] data = new byte[(int) imagefile.length()];
	    FileInputStream fis = new FileInputStream(imagefile);
	    fis.read(data);
	    fis.close();
	    return data;
	}
	
	public static int getHourFromTimestamp(Date date1){
		int hour=0;
		String day=null;
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy/MM/dd h:mm:ss a");
		String str = sdf.format(date1);
		Date date=null;
		try {
			 date = sdf.parse(str);
		} catch (ParseException e) {
			e.printStackTrace();
		}
		Calendar cal = Calendar.getInstance();
		 cal.setTimeZone(TimeZone.getTimeZone("IST"));
		 cal.setTime(date);
		 hour = cal.get(Calendar.HOUR);
		  return hour;
	}
	
	public static String getDayFromTimestamp(Date date1){
		int hour=0;
		String day=null;
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy/MM/dd h:mm:ss a");
		String str = sdf.format(date1);
		Date date=null;
		try {
			 date = sdf.parse(str);
		} catch (ParseException e) {
			e.printStackTrace();
		}
		Calendar cal = Calendar.getInstance();
		 cal.setTimeZone(TimeZone.getTimeZone("IST"));
		 cal.setTime(date);
		 DateFormat format2=new SimpleDateFormat("EEEE"); 
		  day=format2.format(date);
		  return day;
	}

	public static Map<String,String> getOrderBefore3PmOr1Pm(Date date) {
		Map<String,String> dateMap = new HashMap<String, String>();
		SimpleDateFormat timeFormat = new SimpleDateFormat("HH:mm");
		SimpleDateFormat dayFormat = new SimpleDateFormat("E");
		try {
			if(timeFormat.parse(timeFormat.format(date)).after(timeFormat.parse("13:00")))
			{
			    dateMap.put("1pm","false");
			}else{
				dateMap.put("1pm","true");
			}
			
			if(timeFormat.parse(timeFormat.format(date)).after(timeFormat.parse("15:00")))
			{
			    dateMap.put("3pm","false");
			}else{
				dateMap.put("3pm","true");
			}
			
			
			if(dayFormat.format(date).equalsIgnoreCase("Sat") || dayFormat.format(date).equalsIgnoreCase("Sun")){
				dateMap.put("day","false");
			}else{
				dateMap.put("day","true");
			}
			
			
			if(dayFormat.format(date).equalsIgnoreCase("Sat")){
				dateMap.put("sat","true");
			}else{
				dateMap.put("sat","false");
			}
			
			if(dayFormat.format(date).equalsIgnoreCase("Sun")){
				dateMap.put("sun","true");
			}else{
				dateMap.put("sun","false");
			}
			
			if(dayFormat.format(date).equalsIgnoreCase("Fri")){
				dateMap.put("fri","true");
			}else{
				dateMap.put("fri","false");
			}
			
			if(dayFormat.format(date).equalsIgnoreCase("Mon")){
				dateMap.put("mon","true");
			}else{
				dateMap.put("mon","false");
			}
			
		} catch (ParseException e) {
			e.printStackTrace();
		}
		
		return dateMap;
	}

		public static int getDiffFromTwoDatesExceptSatAndSun(String startDate){
			int response =0;
			SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
	        String endDate = sdf.format(new Date());

	        Date date1 = null;
	        Date date2 = null;
			try {
				date1 = sdf.parse(startDate);
				date2 = sdf.parse(endDate);
			} catch (ParseException e) {
				e.printStackTrace();
			}
	       
	        long getDiff = date2.getTime() - date1.getTime();
            long getDaysDiff = TimeUnit.MILLISECONDS.toDays(getDiff);
	        String date = String.valueOf(getDaysDiff);
	       
	        Calendar calender = Calendar.getInstance();
	         calender.setTime(date1);
	         Calendar calender1 = Calendar.getInstance();
	         calender1.setTime(date2);
	         int sundays = 0;
	         
	         while(calender1.after(calender)) {
	             if(calender.get(Calendar.DAY_OF_WEEK)==Calendar.SUNDAY || calender.get(Calendar.DAY_OF_WEEK)==Calendar.SATURDAY)
	                 sundays++;
	             calender.add(Calendar.DATE,1);
	         }
	         response = Integer.valueOf(date)-sundays;
	        return response;
	}

	public static String getSipStartDate(String value) {
		int MILLIS_IN_DAY = 1000*60*60 * 24;
		SimpleDateFormat dateFormat = new SimpleDateFormat("dd MMM yyyy");
		SimpleDateFormat reqFormat = new SimpleDateFormat("dd");
		Date currentDate = new Date();
		String fromDate = dateFormat.format(currentDate);
		String result = null;
		Date dateSelectedFrom = null;
		try{
			dateSelectedFrom = dateFormat.parse(fromDate);;
		}catch(Exception e){
		}
		try{
			while(true) {
				String	date_1 = dateFormat.format(dateSelectedFrom.getTime() + MILLIS_IN_DAY*1);
				dateSelectedFrom = dateFormat.parse(date_1);
				Date plusOneDate = reqFormat.parse(date_1);
				String plusDateInString = reqFormat.format(plusOneDate);
				if(plusDateInString.equals(value)){
					result = date_1;
					break;
				}
			}
		}catch(Exception e){
			System.out.println("error");
		}
		return result;
	}

	public static Map<String,String> getNextDateOrPreviousDateFromGivenDate(Date date){
		Map<String,String> dateMap = new HashMap<String, String>();
		SimpleDateFormat dateFormat = new SimpleDateFormat("dd MMM yyyy");
		Date currentDate = date;
		String fromDate = dateFormat.format(currentDate);
		int MILLIS_IN_DAY = 1000 * 60 * 60 * 24;
		Date dateSelectedFrom = null;
		try {
			dateSelectedFrom = dateFormat.parse(fromDate);
		}catch(Exception e) {
			e.printStackTrace();
		}
		String date_00 = dateFormat.format(dateSelectedFrom.getTime() - MILLIS_IN_DAY);
		String date_0 = dateFormat.format(dateSelectedFrom.getTime());
		String date_1 = dateFormat.format(dateSelectedFrom.getTime() + MILLIS_IN_DAY*27);
		String date_2 = dateFormat.format(dateSelectedFrom.getTime() + MILLIS_IN_DAY*2);
		String date_3 = dateFormat.format(dateSelectedFrom.getTime() + MILLIS_IN_DAY*3);
		String date_4 = dateFormat.format(dateSelectedFrom.getTime() + MILLIS_IN_DAY*4);
	    String date_5 = dateFormat.format(dateSelectedFrom.getTime() + MILLIS_IN_DAY*5);
	    String date_6 = dateFormat.format(dateSelectedFrom.getTime() + MILLIS_IN_DAY*6);
	    String date_7 = dateFormat.format(dateSelectedFrom.getTime() + MILLIS_IN_DAY*7);
	    String date_8 = dateFormat.format(dateSelectedFrom.getTime() + MILLIS_IN_DAY*8);
	    String date_9 = dateFormat.format(dateSelectedFrom.getTime() + MILLIS_IN_DAY*9);
	    String date_10 = dateFormat.format(dateSelectedFrom.getTime() + MILLIS_IN_DAY*10);
	    String date_11 = dateFormat.format(dateSelectedFrom.getTime() + MILLIS_IN_DAY*11);
	    String date_12 = dateFormat.format(dateSelectedFrom.getTime() + MILLIS_IN_DAY*12);
	    String date_13 = dateFormat.format(dateSelectedFrom.getTime() + MILLIS_IN_DAY*13);
	    String date_14 = dateFormat.format(dateSelectedFrom.getTime() + MILLIS_IN_DAY*14);
	    String date_15 = dateFormat.format(dateSelectedFrom.getTime() + MILLIS_IN_DAY*15);
	    String date_16 = dateFormat.format(dateSelectedFrom.getTime() + MILLIS_IN_DAY*16);
	    String date_17 = dateFormat.format(dateSelectedFrom.getTime() + MILLIS_IN_DAY*17);
	    String date_18 = dateFormat.format(dateSelectedFrom.getTime() + MILLIS_IN_DAY*18);
	    String date_19 = dateFormat.format(dateSelectedFrom.getTime() + MILLIS_IN_DAY*19);
	    String date_20 = dateFormat.format(dateSelectedFrom.getTime() + MILLIS_IN_DAY*20);
	    String date_21 = dateFormat.format(dateSelectedFrom.getTime() + MILLIS_IN_DAY*21);
	    String date_22 = dateFormat.format(dateSelectedFrom.getTime() + MILLIS_IN_DAY*22);
	    String date_23 = dateFormat.format(dateSelectedFrom.getTime() + MILLIS_IN_DAY*23);
	    String date_24 = dateFormat.format(dateSelectedFrom.getTime() + MILLIS_IN_DAY*24);
	    String date_25 = dateFormat.format(dateSelectedFrom.getTime() + MILLIS_IN_DAY*25);
	    String date_26 = dateFormat.format(dateSelectedFrom.getTime() + MILLIS_IN_DAY*26);
	    String date_27 = dateFormat.format(dateSelectedFrom.getTime() + MILLIS_IN_DAY*27);
	    String date_28 = dateFormat.format(dateSelectedFrom.getTime() + MILLIS_IN_DAY*28);
	    String date_29 = dateFormat.format(dateSelectedFrom.getTime() + MILLIS_IN_DAY*29);
	    String date_30 = dateFormat.format(dateSelectedFrom.getTime() + MILLIS_IN_DAY*30);
	    String date_31 = dateFormat.format(dateSelectedFrom.getTime() + MILLIS_IN_DAY*31);
	    dateMap.put("-1",date_00);
	    dateMap.put("0",date_0);
	    dateMap.put("1",date_1);
	    dateMap.put("2",date_2);
	    dateMap.put("3",date_3);
	    dateMap.put("4",date_4);
	    dateMap.put("5",date_5);
	    dateMap.put("6",date_6);
	    dateMap.put("7",date_7);
	    dateMap.put("8",date_8);
	    dateMap.put("9",date_9);
	    dateMap.put("10",date_10);
	    dateMap.put("11",date_11);
	    dateMap.put("12",date_12);
	    dateMap.put("13",date_13);
	    dateMap.put("14",date_14);
	    dateMap.put("15",date_15);
	    dateMap.put("16",date_16);
	    dateMap.put("17",date_17);
	    dateMap.put("18",date_18);
	    dateMap.put("19",date_19);
	    dateMap.put("20",date_20);
	    dateMap.put("21",date_21);
	    dateMap.put("22",date_22);
	    dateMap.put("23",date_23);
	    dateMap.put("24",date_24);
	    dateMap.put("25",date_25);
	    dateMap.put("26",date_26);
	    dateMap.put("27",date_27);
	    dateMap.put("28",date_28);
	    dateMap.put("29",date_29);
	    dateMap.put("30",date_30);
	    dateMap.put("31",date_31);
	    return dateMap;
	}

	public static Map<String,String> getPreviousDateFormatData(Date date){
		Map<String,String> dateMap = new HashMap<String, String>();
		SimpleDateFormat dateFormat = new SimpleDateFormat("dd MMM yyyy");
		Date currentDate = date;
		String fromDate = dateFormat.format(currentDate);
		int MILLIS_IN_DAY = 1000 * 60 * 60 * 24;
		Date dateSelectedFrom = null;
		try {
			dateSelectedFrom = dateFormat.parse(fromDate);
		}catch(Exception e) {
			e.printStackTrace();
		}
		String date_0 = dateFormat.format(dateSelectedFrom.getTime());
		String date_1 = dateFormat.format(dateSelectedFrom.getTime() - MILLIS_IN_DAY);
		String date_2 = dateFormat.format(dateSelectedFrom.getTime() - MILLIS_IN_DAY*2);
		String date_3 = dateFormat.format(dateSelectedFrom.getTime() - MILLIS_IN_DAY*3);
		String date_4 = dateFormat.format(dateSelectedFrom.getTime() - MILLIS_IN_DAY*4);
		dateMap.put("0",date_0);
		dateMap.put("-1",date_1);
		dateMap.put("-2",date_2);
		dateMap.put("-3",date_3);
		dateMap.put("-4",date_4);
		return dateMap;
	}

	public static String getPreviousDate(Date date){
		SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
		Date currentDate = date;
		String fromDate = dateFormat.format(currentDate);
		int MILLIS_IN_DAY = 1000 * 60 * 60 * 24;
		Date dateSelectedFrom = null;
		try {
			dateSelectedFrom = dateFormat.parse(fromDate);
		} catch(Exception e) {
			e.printStackTrace();
		}
		String date_1 = dateFormat.format(dateSelectedFrom.getTime() - MILLIS_IN_DAY);
		return date_1;
	}

	/*
	public static List<MailbackUserPortfolioData> readDataFromExcelAndStoreToDatabase(MultipartFile excelfile) {
		List<MailbackUserPortfolioData> mailbackUserPortfolioDataList = new ArrayList<MailbackUserPortfolioData>();
		try {
			int i = 1;
			//XSSFWorkbook workbook = new XSSFWorkbook(excelfile.getInputStream());//for .slsx
			HSSFWorkbook workbook = new HSSFWorkbook(excelfile.getInputStream());//for .xls
			// Creates a worksheet object representing the first sheet
			//XSSFSheet worksheet = workbook.getSheetAt(0);
			HSSFSheet worksheet = workbook.getSheetAt(0);
			while (i <= worksheet.getLastRowNum()) {
				MailbackUserPortfolioData mailbackUserPortfolioData= new MailbackUserPortfolioData();
				//XSSFRow xrow = worksheet.getRow(i++);
				HSSFRow xrow = worksheet.getRow(i++);
				if (xrow.getCell(0).getStringCellValue() != null && !xrow.getCell(0).getStringCellValue().isEmpty() &&  !xrow.getCell(0).getStringCellValue().equals(" ") && !xrow.getCell(0).getStringCellValue().equals("")) {
					mailbackUserPortfolioData.setAmcCode(xrow.getCell(0).getStringCellValue());
				}
				if (xrow.getCell(1).getStringCellValue() != null&& !xrow.getCell(1).getStringCellValue().isEmpty() &&  !xrow.getCell(1).getStringCellValue().equals(" ") && !xrow.getCell(1).getStringCellValue().equals("")) {
					mailbackUserPortfolioData.setFolioNo(xrow.getCell(1).getStringCellValue());
				}
				if (xrow.getCell(2).getStringCellValue() != null&& !xrow.getCell(2).getStringCellValue().isEmpty() &&  !xrow.getCell(2).getStringCellValue().equals(" ") && !xrow.getCell(2).getStringCellValue().equals("")) {
					mailbackUserPortfolioData.setProdCode(xrow.getCell(2).getStringCellValue());
				}
				if (xrow.getCell(3).getStringCellValue() != null&& !xrow.getCell(3).getStringCellValue().isEmpty() &&  !xrow.getCell(3).getStringCellValue().equals(" ") && !xrow.getCell(3).getStringCellValue().equals("")) {
					mailbackUserPortfolioData.setScheme(xrow.getCell(3).getStringCellValue());
				}				
				if (xrow.getCell(4).getStringCellValue() != null&& !xrow.getCell(4).getStringCellValue().isEmpty() &&  !xrow.getCell(4).getStringCellValue().equals(" ") && !xrow.getCell(4).getStringCellValue().equals("")) {
					mailbackUserPortfolioData.setInvName(xrow.getCell(4).getStringCellValue());
				}
				if (xrow.getCell(5).getStringCellValue() != null&& !xrow.getCell(5).getStringCellValue().isEmpty() &&  !xrow.getCell(5).getStringCellValue().equals(" ") && !xrow.getCell(5).getStringCellValue().equals("")) {
					mailbackUserPortfolioData.setTrxnType(xrow.getCell(5).getStringCellValue());
				}
				if (xrow.getCell(6).getNumericCellValue()!=0) {
					int value = new BigDecimal(xrow.getCell(6).getNumericCellValue()).setScale(0, RoundingMode.HALF_UP).intValue();
					mailbackUserPortfolioData.setTrxnNo(String.valueOf(value));
				}		
				if (xrow.getCell(7).getStringCellValue() != null&& !xrow.getCell(7).getStringCellValue().isEmpty() &&  !xrow.getCell(7).getStringCellValue().equals(" ") && !xrow.getCell(7).getStringCellValue().equals("")) {
					mailbackUserPortfolioData.setTrxnMode(xrow.getCell(7).getStringCellValue());
				}
				if (xrow.getCell(8).getStringCellValue() != null&& !xrow.getCell(8).getStringCellValue().isEmpty() &&  !xrow.getCell(8).getStringCellValue().equals(" ") && !xrow.getCell(8).getStringCellValue().equals("")) {
					mailbackUserPortfolioData.setTrxnStat(xrow.getCell(8).getStringCellValue());
				}
				if (xrow.getCell(9).getStringCellValue() != null&& !xrow.getCell(9).getStringCellValue().isEmpty() &&  !xrow.getCell(9).getStringCellValue().equals(" ") && !xrow.getCell(9).getStringCellValue().equals("")) {
					mailbackUserPortfolioData.setUserCode(xrow.getCell(9).getStringCellValue());
				}
				
				if (xrow.getCell(10).getNumericCellValue() != 0) {

					int value = new BigDecimal(xrow.getCell(6).getNumericCellValue()).setScale(0, RoundingMode.HALF_UP).intValue();
					mailbackUserPortfolioData.setUsrTrxNo(String.valueOf(value));
					}
				
				if (xrow.getCell(11).getDateCellValue()!=null) {
					DataFormatter dataFormatter = new DataFormatter();
					SimpleDateFormat sdf= new SimpleDateFormat("dd/MM/yyyy");
					String cellStringValue = dataFormatter.formatCellValue(xrow.getCell(11));

					Date date=sdf.parse(cellStringValue);
					SimpleDateFormat sdf1= new SimpleDateFormat("dd-MM-yyyy");
					mailbackUserPortfolioData.setTradDate(sdf1.format(date));
					}
				
				if (xrow.getCell(12).getNumericCellValue() != 0) {

					DataFormatter dataFormatter = new DataFormatter();
					SimpleDateFormat sdf= new SimpleDateFormat("mm/DD/yyyy");
					String cellStringValue = dataFormatter.formatCellValue(xrow.getCell(11));

					Date date=sdf.parse(cellStringValue);
					SimpleDateFormat sdf1= new SimpleDateFormat("mm-DD-yyyy");
					mailbackUserPortfolioData.setPostDate(sdf1.format(date));
					}
				
				if (xrow.getCell(13).getNumericCellValue() !=0) {

					mailbackUserPortfolioData.setPurPrice(String.valueOf(xrow.getCell(13).getNumericCellValue()));
					}
				
				if (xrow.getCell(14).getNumericCellValue() != 0) {

					mailbackUserPortfolioData.setUnits(String.valueOf(xrow.getCell(14).getNumericCellValue()));
					}
				
				if (xrow.getCell(15).getNumericCellValue() !=0) {

					mailbackUserPortfolioData.setAmount(String.valueOf(xrow.getCell(15).getNumericCellValue()));
					}

				
				if (xrow.getCell(16).getStringCellValue() != null&& !xrow.getCell(16).getStringCellValue().isEmpty() && 
						!xrow.getCell(16).getStringCellValue().equals(" ") && !xrow.getCell(16).getStringCellValue().equals("")) {

					mailbackUserPortfolioData.setBrokCode(xrow.getCell(16).getStringCellValue());
					}
					
					if (xrow.getCell(17).getStringCellValue() != null&& !xrow.getCell(17).getStringCellValue().isEmpty() && 
							!xrow.getCell(17).getStringCellValue().equals(" ") && !xrow.getCell(17).getStringCellValue().equals("")) {

						mailbackUserPortfolioData.setSubBrok(xrow.getCell(17).getStringCellValue());
						}
					
					if (xrow.getCell(18).getNumericCellValue() != 0) {

						mailbackUserPortfolioData.setBrokPerc(String.valueOf(xrow.getCell(18).getNumericCellValue()));
						}
					
					if (xrow.getCell(19).getNumericCellValue() != 0) {

						mailbackUserPortfolioData.setBrokComm(String.valueOf(xrow.getCell(19).getNumericCellValue()));
						}
					
					if (xrow.getCell(20).getStringCellValue() != null&& !xrow.getCell(20).getStringCellValue().isEmpty() && 
							!xrow.getCell(20).getStringCellValue().equals(" ") && !xrow.getCell(20).getStringCellValue().equals("")) {

						mailbackUserPortfolioData.setAltFolio(xrow.getCell(20).getStringCellValue());
						}
					
					if (xrow.getCell(21).getNumericCellValue() != 0) {
						DataFormatter dataFormatter = new DataFormatter();
						SimpleDateFormat sdf= new SimpleDateFormat("mm/DD/yyyy");
						String cellStringValue = dataFormatter.formatCellValue(xrow.getCell(21));

						Date date=sdf.parse(cellStringValue);
						SimpleDateFormat sdf1= new SimpleDateFormat("mm-DD-yyyy");
						mailbackUserPortfolioData.setRepDate(sdf1.format(date));
						}
					
					if (xrow.getCell(22).getStringCellValue() != null&& !xrow.getCell(22).getStringCellValue().isEmpty() && 
							!xrow.getCell(22).getStringCellValue().equals(" ") && !xrow.getCell(22).getStringCellValue().equals("")) {

						mailbackUserPortfolioData.setTime1(xrow.getCell(22).getStringCellValue());
						}
					
					if (xrow.getCell(23).getStringCellValue() != null&& !xrow.getCell(23).getStringCellValue().isEmpty() && 
							!xrow.getCell(23).getStringCellValue().equals(" ") && !xrow.getCell(23).getStringCellValue().equals("")) {

						mailbackUserPortfolioData.setTrxnSubType(xrow.getCell(23).getStringCellValue());
						}
					
					if (xrow.getCell(24).getStringCellValue() != null&& !xrow.getCell(24).getStringCellValue().isEmpty() && 
							!xrow.getCell(24).getStringCellValue().equals(" ") && !xrow.getCell(24).getStringCellValue().equals("")) {

						mailbackUserPortfolioData.setApplicationNo(xrow.getCell(24).getStringCellValue());
						}
					
					if (xrow.getCell(25).getStringCellValue() != null&& !xrow.getCell(25).getStringCellValue().isEmpty() && 
							!xrow.getCell(25).getStringCellValue().equals(" ") && !xrow.getCell(25).getStringCellValue().equals("")) {

						mailbackUserPortfolioData.setTrxnNature(xrow.getCell(25).getStringCellValue());
						}
					
					if (xrow.getCell(26).getNumericCellValue() != 0) {

						mailbackUserPortfolioData.setTax(String.valueOf(xrow.getCell(26).getNumericCellValue()));
						}
					
					if (xrow.getCell(27).getNumericCellValue() != 0) {

						mailbackUserPortfolioData.setTotalTax(String.valueOf(xrow.getCell(27).getNumericCellValue()));
						}
					
					if (xrow.getCell(28).getStringCellValue() != null&& !xrow.getCell(28).getStringCellValue().isEmpty() && 
							!xrow.getCell(28).getStringCellValue().equals(" ") && !xrow.getCell(28).getStringCellValue().equals("")) {

						mailbackUserPortfolioData.setTe15h(xrow.getCell(28).getStringCellValue());
						}
					
					if (xrow.getCell(29).getStringCellValue() != null&& !xrow.getCell(29).getStringCellValue().isEmpty() && 
							!xrow.getCell(29).getStringCellValue().equals(" ") && !xrow.getCell(29).getStringCellValue().equals("")) {

						mailbackUserPortfolioData.setMicrNo(xrow.getCell(29).getStringCellValue());
						}
					
					if (xrow.getCell(30).getStringCellValue() != null&& !xrow.getCell(30).getStringCellValue().isEmpty() && 
							!xrow.getCell(30).getStringCellValue().equals(" ") && !xrow.getCell(30).getStringCellValue().equals("")) {

						mailbackUserPortfolioData.setRemarks(xrow.getCell(30).getStringCellValue());
						}
					
					if (xrow.getCell(31).getStringCellValue() != null&& !xrow.getCell(31).getStringCellValue().isEmpty() && 
							!xrow.getCell(31).getStringCellValue().equals(" ") && !xrow.getCell(31).getStringCellValue().equals("")) {

						mailbackUserPortfolioData.setSwFlag(xrow.getCell(31).getStringCellValue());
						}
					
					if (xrow.getCell(32).getStringCellValue() != null&& !xrow.getCell(31).getStringCellValue().isEmpty() && 
							!xrow.getCell(32).getStringCellValue().equals(" ") && !xrow.getCell(32).getStringCellValue().equals("")) {

						mailbackUserPortfolioData.setOldFolio(xrow.getCell(32).getStringCellValue());
						}
						
						if (xrow.getCell(33).getNumericCellValue() !=0) {

							mailbackUserPortfolioData.setSeqNo(String.valueOf(xrow.getCell(33).getNumericCellValue()));
							}
						
						if (xrow.getCell(34).getStringCellValue() != null&& !xrow.getCell(34).getStringCellValue().isEmpty() && 
								!xrow.getCell(34).getStringCellValue().equals(" ") && !xrow.getCell(34).getStringCellValue().equals("")) {

							mailbackUserPortfolioData.setReinvestFlag(xrow.getCell(34).getStringCellValue());
							}
						
						if (xrow.getCell(35).getStringCellValue() != null&& !xrow.getCell(35).getStringCellValue().isEmpty() && 
								!xrow.getCell(35).getStringCellValue().equals(" ") && !xrow.getCell(35).getStringCellValue().equals("")) {

							mailbackUserPortfolioData.setMultBrok(xrow.getCell(35).getStringCellValue());
							}
						
						if (xrow.getCell(36).getNumericCellValue() !=0) {

							mailbackUserPortfolioData.setStt(String.valueOf(xrow.getCell(36).getNumericCellValue()));
							}
						
						if (xrow.getCell(37).getStringCellValue() != null&& !xrow.getCell(37).getStringCellValue().isEmpty() && 
								!xrow.getCell(37).getStringCellValue().equals(" ") && !xrow.getCell(37).getStringCellValue().equals("")) {

							mailbackUserPortfolioData.setLocation(xrow.getCell(37).getStringCellValue());
							}
						
						if (xrow.getCell(38).getStringCellValue() != null&& !xrow.getCell(38).getStringCellValue().isEmpty() && 
								!xrow.getCell(38).getStringCellValue().equals(" ") && !xrow.getCell(38).getStringCellValue().equals("")) {

							mailbackUserPortfolioData.setSchemeType(xrow.getCell(38).getStringCellValue());
							}
						
						if (xrow.getCell(39).getStringCellValue() != null&& !xrow.getCell(39).getStringCellValue().isEmpty() && 
								!xrow.getCell(39).getStringCellValue().equals(" ") && !xrow.getCell(39).getStringCellValue().equals("")) {

							mailbackUserPortfolioData.setTaxStatus(xrow.getCell(39).getStringCellValue());
							}
						
						if (xrow.getCell(40).getNumericCellValue() != 0) {

							mailbackUserPortfolioData.setLoads(String.valueOf(xrow.getCell(40).getNumericCellValue()));
							}
						
						if (xrow.getCell(41).getStringCellValue() != null&& !xrow.getCell(41).getStringCellValue().isEmpty() && 
								!xrow.getCell(41).getStringCellValue().equals(" ") && !xrow.getCell(41).getStringCellValue().equals("")) {

							mailbackUserPortfolioData.setScanRefNo(xrow.getCell(41).getStringCellValue());
							}
						
						if (xrow.getCell(42).getStringCellValue() != null&& !xrow.getCell(42).getStringCellValue().isEmpty() && 
								!xrow.getCell(42).getStringCellValue().equals(" ") && !xrow.getCell(42).getStringCellValue().equals("")) {

							mailbackUserPortfolioData.setPan(xrow.getCell(42).getStringCellValue());
							}
						
						if (xrow.getCell(43).getNumericCellValue() != 0) {

							mailbackUserPortfolioData.setInvIin(String.valueOf(xrow.getCell(43).getNumericCellValue()));
							}
						
						if (xrow.getCell(44).getStringCellValue() != null&& !xrow.getCell(44).getStringCellValue().isEmpty() && 
								!xrow.getCell(44).getStringCellValue().equals(" ") && !xrow.getCell(44).getStringCellValue().equals("")) {

							mailbackUserPortfolioData.setTargSrcScheme(xrow.getCell(44).getStringCellValue());
							}
						
						if (xrow.getCell(45).getStringCellValue() != null&& !xrow.getCell(45).getStringCellValue().isEmpty() && 
								!xrow.getCell(45).getStringCellValue().equals(" ") && !xrow.getCell(45).getStringCellValue().equals("")) {

							mailbackUserPortfolioData.setTrxnTypeFlag(xrow.getCell(45).getStringCellValue());
							}
						
						if (xrow.getCell(46).getStringCellValue() != null&& !xrow.getCell(46).getStringCellValue().isEmpty() && 
								!xrow.getCell(46).getStringCellValue().equals(" ") && !xrow.getCell(46).getStringCellValue().equals("")) {

							mailbackUserPortfolioData.setTicobTrtype(xrow.getCell(46).getStringCellValue());
							}
						
						if (xrow.getCell(47).getStringCellValue() != null&& !xrow.getCell(47).getStringCellValue().isEmpty() && 
								!xrow.getCell(47).getStringCellValue().equals(" ") && !xrow.getCell(47).getStringCellValue().equals("")) {

							mailbackUserPortfolioData.setTicobTrno(xrow.getCell(47).getStringCellValue());
							}
						
						if (xrow.getCell(48).getStringCellValue() != null&& !xrow.getCell(48).getStringCellValue().isEmpty() && 
								!xrow.getCell(48).getStringCellValue().equals(" ") && !xrow.getCell(48).getStringCellValue().equals("")) {

							mailbackUserPortfolioData.setTicobPostedDate(xrow.getCell(48).getStringCellValue());
							}
							
							if (xrow.getCell(49).getStringCellValue() != null&& !xrow.getCell(49).getStringCellValue().isEmpty() && 
									!xrow.getCell(49).getStringCellValue().equals(" ") && !xrow.getCell(49).getStringCellValue().equals("")) {

								mailbackUserPortfolioData.setDpId(xrow.getCell(49).getStringCellValue());
								}
							
							if (xrow.getCell(50).getNumericCellValue() != 0) {

								mailbackUserPortfolioData.setTrxnCharges(String.valueOf(xrow.getCell(50).getNumericCellValue()));
								}
							
							if (xrow.getCell(51).getNumericCellValue() != 0) {

								mailbackUserPortfolioData.setEligibAmt(String.valueOf(xrow.getCell(51).getNumericCellValue()));
								}
							
							if (xrow.getCell(52).getStringCellValue() != null&& !xrow.getCell(52).getStringCellValue().isEmpty() && 
									!xrow.getCell(52).getStringCellValue().equals(" ") && !xrow.getCell(52).getStringCellValue().equals("")) {

								mailbackUserPortfolioData.setSrcOfTxn(xrow.getCell(52).getStringCellValue());
								}
							
							if (xrow.getCell(53).getStringCellValue() != null&& !xrow.getCell(53).getStringCellValue().isEmpty() && 
									!xrow.getCell(53).getStringCellValue().equals(" ") && !xrow.getCell(53).getStringCellValue().equals("")) {

								mailbackUserPortfolioData.setTrxnSuffix(xrow.getCell(53).getStringCellValue());
								}
							
							if (xrow.getCell(54).getStringCellValue() != null&& !xrow.getCell(54).getStringCellValue().isEmpty() && 
									!xrow.getCell(54).getStringCellValue().equals(" ") && !xrow.getCell(54).getStringCellValue().equals("")) {

								mailbackUserPortfolioData.setSipTrxnNo(xrow.getCell(54).getStringCellValue());
								}
							
							if (xrow.getCell(55).getStringCellValue() != null&& !xrow.getCell(55).getStringCellValue().isEmpty() && 
									!xrow.getCell(55).getStringCellValue().equals(" ") && !xrow.getCell(55).getStringCellValue().equals("")) {

								mailbackUserPortfolioData.setTerLocation(xrow.getCell(55).getStringCellValue());
								}
							
							if (xrow.getCell(56).getStringCellValue() != null&& !xrow.getCell(56).getStringCellValue().isEmpty() && 
									!xrow.getCell(56).getStringCellValue().equals(" ") && !xrow.getCell(56).getStringCellValue().equals("")) {

								mailbackUserPortfolioData.setEuin(xrow.getCell(56).getStringCellValue());
								}
							
							if (xrow.getCell(57).getStringCellValue() != null&& !xrow.getCell(57).getStringCellValue().isEmpty() && 
									!xrow.getCell(57).getStringCellValue().equals(" ") && !xrow.getCell(57).getStringCellValue().equals("")) {

								mailbackUserPortfolioData.setEuinValid(xrow.getCell(57).getStringCellValue());
								}
							
							if (xrow.getCell(58).getStringCellValue() != null&& !xrow.getCell(58).getStringCellValue().isEmpty() && 
									!xrow.getCell(58).getStringCellValue().equals(" ") && !xrow.getCell(58).getStringCellValue().equals("")) {

								mailbackUserPortfolioData.setEuinOpted(xrow.getCell(58).getStringCellValue());
								}
							
							if (xrow.getCell(59).getStringCellValue() != null&& !xrow.getCell(59).getStringCellValue().isEmpty() && 
									!xrow.getCell(59).getStringCellValue().equals(" ") && !xrow.getCell(59).getStringCellValue().equals("")) {

								mailbackUserPortfolioData.setSubBrkArn(xrow.getCell(59).getStringCellValue());
								}
							
							if (xrow.getCell(60).getStringCellValue() != null&& !xrow.getCell(60).getStringCellValue().isEmpty() && 
									!xrow.getCell(60).getStringCellValue().equals(" ") && !xrow.getCell(60).getStringCellValue().equals("")) {

								mailbackUserPortfolioData.setExchDcFlag(xrow.getCell(60).getStringCellValue());
								}
							
							if (xrow.getCell(61).getStringCellValue() != null&& !xrow.getCell(61).getStringCellValue().isEmpty() && 
									!xrow.getCell(61).getStringCellValue().equals(" ") && !xrow.getCell(61).getStringCellValue().equals("")) {

								mailbackUserPortfolioData.setSrcBrkCode(xrow.getCell(61).getStringCellValue());
								}
							
							if (xrow.getCell(62).getStringCellValue() != null&& !xrow.getCell(62).getStringCellValue().isEmpty() && 
									!xrow.getCell(62).getStringCellValue().equals(" ") && !xrow.getCell(62).getStringCellValue().equals("")) {

								mailbackUserPortfolioData.setSysRegnDate(xrow.getCell(62).getStringCellValue());
								}
							
							if (xrow.getCell(63).getStringCellValue() != null&& !xrow.getCell(63).getStringCellValue().isEmpty() && 
									!xrow.getCell(63).getStringCellValue().equals(" ") && !xrow.getCell(63).getStringCellValue().equals("")) {

								mailbackUserPortfolioData.setAcNo(xrow.getCell(63).getStringCellValue());
								}
							
							if (xrow.getCell(64).getStringCellValue() != null&& !xrow.getCell(64).getStringCellValue().isEmpty() && 
									!xrow.getCell(64).getStringCellValue().equals(" ") && !xrow.getCell(64).getStringCellValue().equals("")) {

								mailbackUserPortfolioData.setBankName(xrow.getCell(64).getStringCellValue());
								}
								Cell cell=xrow.getCell(65);
								if(cell.getCellType() == Cell.CELL_TYPE_NUMERIC){
									mailbackUserPortfolioData.setReversalCode(String.valueOf(xrow.getCell(65).getNumericCellValue()));
								}
								
                              if(cell.getCellType() == Cell.CELL_TYPE_STRING){
                              	mailbackUserPortfolioData.setReversalCode(xrow.getCell(65).getStringCellValue());
								}
								
								if (xrow.getCell(66).getStringCellValue() != null&& !xrow.getCell(66).getStringCellValue().isEmpty() && 
										!xrow.getCell(66).getStringCellValue().equals(" ") && !xrow.getCell(66).getStringCellValue().equals("")) {

									mailbackUserPortfolioData.setExchangeFlag(xrow.getCell(66).getStringCellValue());
									}
								
								if (xrow.getCell(67).getStringCellValue() != null&& !xrow.getCell(67).getStringCellValue().isEmpty() && 
										!xrow.getCell(67).getStringCellValue().equals(" ") && !xrow.getCell(67).getStringCellValue().equals("")) {

									mailbackUserPortfolioData.setCaInitiatedDate(xrow.getCell(67).getStringCellValue());
									}
								
								if (xrow.getCell(68).getStringCellValue() != null&& !xrow.getCell(67).getStringCellValue().isEmpty() && 
										!xrow.getCell(68).getStringCellValue().equals(" ") && !xrow.getCell(68).getStringCellValue().equals("")) {

									mailbackUserPortfolioData.setGstStateCode(xrow.getCell(68).getStringCellValue());
									}
								
								if (xrow.getCell(69).getNumericCellValue() != 0) {

									mailbackUserPortfolioData.setIgstAmount(String.valueOf(xrow.getCell(69).getNumericCellValue()));
									}
								
								if (xrow.getCell(70).getNumericCellValue() != 0) {

									mailbackUserPortfolioData.setCgstAmount(String.valueOf(xrow.getCell(70).getNumericCellValue()));
									}
								
								if (xrow.getCell(71).getNumericCellValue() != 0) {

									mailbackUserPortfolioData.setSgstAmount(String.valueOf(xrow.getCell(70).getNumericCellValue()));
									}
								
								if (xrow.getCell(72).getStringCellValue() != null&& !xrow.getCell(72).getStringCellValue().isEmpty() && 
										!xrow.getCell(72).getStringCellValue().equals(" ") && !xrow.getCell(72).getStringCellValue().equals("")) {

									mailbackUserPortfolioData.setRevRemark(xrow.getCell(72).getStringCellValue());
									}
								
				if (xrow.getCell(2).getStringCellValue() != null && !xrow.getCell(2).getStringCellValue().isEmpty()) {
					if (xrow.getCell(2).getStringCellValue().trim().length() != 0) {
						sipProfileComb.setCustomerCode(xrow.getCell(2).getStringCellValue());
						goalCombRel.setCutomorCode(xrow.getCell(2).getStringCellValue());
						goalCombRel.setSipProfileComb(sipProfileComb);		

					}
				}
				mailbackUserPortfolioDataList.add(mailbackUserPortfolioData);
			}
			workbook.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return mailbackUserPortfolioDataList;
	}
	*/

	public static String createDayOfSip(String sipDate){
		if(Integer.parseInt(sipDate) % 100 == 11 || Integer.parseInt(sipDate) % 100 == 12 || Integer.parseInt(sipDate) % 100 == 13) {
			sipDate = sipDate.concat("th");
		}else if(Integer.parseInt(sipDate)%10==1){
			sipDate = sipDate.concat("st");
		}else if(Integer.parseInt(sipDate)%10==2){
			sipDate = sipDate.concat("nd");
		}else if(Integer.parseInt(sipDate)%10==3){
			sipDate = sipDate.concat("rd");
		}else{
			sipDate = sipDate.concat("th");
		}
		return sipDate;
	}

	public static String  generateMandateFormPdf(MandatePdfFormRequestDTO mpf, String pdfPath)  throws IOException{
		PDDocument document = PDDocument.load(GoForWealthPRSUtil.class.getClassLoader().getResourceAsStream("pdfTemplates/MANDATE_FORM_TEMPLATE.pdf"));
		PDPageTree pages =  document.getDocumentCatalog().getPages();
		for(int i=0;i<pages.getCount();i++){
			PDPage page=document.getDocumentCatalog().getPages().get(i);
		    fillMandateFormPdf(document, page,i,mpf);	
		}
		ByteArrayOutputStream output = new ByteArrayOutputStream();
		//document.save(output);
		document.save(new File(pdfPath));
		document.close();
		return pdfPath;	
	}

	private static void fillMandateFormPdf(PDDocument document, PDPage page, int pageNum, MandatePdfFormRequestDTO mpf) throws IOException {
		PDPageContentStream contentStream = new PDPageContentStream(document, page,true,true);
		float pageHeight = page.getCropBox().getHeight();
		page.getCropBox().getWidth();
		float textLeft = 0;
		float textTop = pageHeight;
		if(pageNum==0){
			contentStream.setFont(loadFont(document), 11);
			//Date
			float dateLeftIncremnt = 481.0f;
			char[] date = mpf.getDate().toCharArray();
			for(int i =0;i<date.length;i++){
				drawStringAtPosition(contentStream, textLeft+dateLeftIncremnt, textTop-53.5f,String.valueOf(date[i]));
				dateLeftIncremnt = dateLeftIncremnt+14;
			}
			//Sponser Code
			drawStringAtPosition(contentStream, textLeft+227.2f, textTop-72.5f,mpf.getSponserBankCode());
			//Utility Code
			drawStringAtPosition(contentStream, textLeft+440.2f, textTop-72.5f,mpf.getUtilityCode());
			//Account Number
			float accLeftIncremnt = 170.95f;
			char[] bankAcc = mpf.getBankAccNo().toCharArray();
			for(int i =0;i<mpf.getBankAccNo().length();i++){
				drawStringAtPosition(contentStream, textLeft+accLeftIncremnt, textTop-110.5f,String.valueOf(bankAcc[i]));
				accLeftIncremnt = accLeftIncremnt+13;
			}
			//Ifsc Code
			float ifscLeftIncremnt = 283.63f;
			char[] ifscCode = mpf.getIfscCode().toCharArray();
			for(int i =0;i<mpf.getIfscCode().length();i++){
				drawStringAtPosition(contentStream, textLeft+ifscLeftIncremnt, textTop-127.2f,String.valueOf(ifscCode[i]));
				ifscLeftIncremnt = ifscLeftIncremnt+13;
			}
			//Tick For Form Type====>Create ,Modify, Cancel
			float formTypeTopIncremnt = 87.95f;
			if(mpf.getFormType().equals("Create")){
				PDImageXObject pdImage = PDImageXObject.createFromFile(mpf.getTickImageValue(), document);
				contentStream.drawImage(pdImage, textLeft+75.75f, textTop-formTypeTopIncremnt, 10.2f, 12.2f);
			}else if(mpf.getFormType().equals("Modify")){
				PDImageXObject pdImage = PDImageXObject.createFromFile(mpf.getTickImageValue(), document);
				formTypeTopIncremnt = formTypeTopIncremnt+12;
				contentStream.drawImage(pdImage, textLeft+75.75f, textTop-formTypeTopIncremnt, 10.2f, 12.2f);
			}else if(mpf.getFormType().equals("Cancel")){
				formTypeTopIncremnt = formTypeTopIncremnt+23;
				PDImageXObject pdImage = PDImageXObject.createFromFile(mpf.getTickImageValue(), document);
				contentStream.drawImage(pdImage, textLeft+75.75f, textTop-formTypeTopIncremnt, 10.2f, 12.2f);
			}
			//Tick For Account Type
			float accTypeLeftIncremnt = 454.95f;
			if(mpf.getAccType().equals("SB")){
				PDImageXObject pdImage = PDImageXObject.createFromFile(mpf.getTickImageValue(), document);
				contentStream.drawImage(pdImage, textLeft+accTypeLeftIncremnt, textTop-87.86f, 10.2f, 12.2f);
			}else if(mpf.getAccType().equals("CA")){
				PDImageXObject pdImage = PDImageXObject.createFromFile(mpf.getTickImageValue(), document);
				accTypeLeftIncremnt = accTypeLeftIncremnt+13;
				contentStream.drawImage(pdImage, textLeft+accTypeLeftIncremnt, textTop-87.86f, 10.2f, 12.2f);
			}else if(mpf.getAccType().equals("SB-NRE")){
				PDImageXObject pdImage = PDImageXObject.createFromFile(mpf.getTickImageValue(), document);
				accTypeLeftIncremnt = accTypeLeftIncremnt+40;
				contentStream.drawImage(pdImage, textLeft+accTypeLeftIncremnt, textTop-87.86f, 10.2f, 12.2f);
			}else if(mpf.getAccType().equals("SB-NRO")){
				PDImageXObject pdImage = PDImageXObject.createFromFile(mpf.getTickImageValue(), document);
				accTypeLeftIncremnt = accTypeLeftIncremnt+66;
				contentStream.drawImage(pdImage, textLeft+accTypeLeftIncremnt, textTop-87.86f, 10.2f, 12.2f);
			}else if(mpf.getAccType().equals("OTHER")){
				PDImageXObject pdImage = PDImageXObject.createFromFile(mpf.getTickImageValue(), document);
				accTypeLeftIncremnt = accTypeLeftIncremnt+90;
				contentStream.drawImage(pdImage, textLeft+accTypeLeftIncremnt, textTop-87.86f, 10.2f, 12.2f);
			}
			//Bank Name
			drawStringAtPosition(contentStream, textLeft+90.75f, textTop-127.2f,mpf.getBankName());
			//Amount
			drawStringAtPosition(contentStream, textLeft+110.75f, textTop-146.2f,mpf.getAmountInWorld());
			//Amount
			drawStringAtPosition(contentStream, textLeft+480.0f, textTop-145.5f,mpf.getAmountInNumber());
			//Mandate Number
			drawStringAtPosition(contentStream, textLeft+170.75f, textTop-182.95001f,mpf.getMandateNumber());
			//Phone Number
			drawStringAtPosition(contentStream, textLeft+390.75f, textTop-182.95001f,mpf.getMobileNo());
			//Client Code
			drawStringAtPosition(contentStream, textLeft+170.75f, textTop-200.59003f,mpf.getClientCode());
			//Email
			drawStringAtPosition(contentStream, textLeft+390.75f, textTop-200.59003f,mpf.getEmail());
			//From Date
			float fromDateLeftIncremnt = 82.95f;
			char[] fromDate = mpf.getFromDate().toCharArray();
			for(int i =0;i<date.length;i++){
				drawStringAtPosition(contentStream, textLeft+fromDateLeftIncremnt, textTop-243.5f,String.valueOf(fromDate[i]));
				fromDateLeftIncremnt = fromDateLeftIncremnt+13;
			}
			//Tick For Untill Cancelled.
			PDImageXObject pdImage = PDImageXObject.createFromFile(mpf.getTickImageValue(), document);
			//contentStream.drawImage(pdImage, textLeft+70.50f, textTop-278.86f);
			contentStream.drawImage(pdImage, textLeft+75.50f, textTop-275.86f, 10.2f, 12.2f);
		}
		contentStream.close();
	}

	public static String getSipPreviousDate(String sipRegnDate){
		SimpleDateFormat dateFormat = new SimpleDateFormat("dd MMM yyyy");
		String prevDate = "";
		String fromDate = sipRegnDate;
		int MILLIS_IN_DAY = 1000 * 60 * 60 * 24;
		Date dateSelectedFrom = null;
		try{
			dateSelectedFrom = dateFormat.parse(fromDate);
		}catch(Exception e){
			e.printStackTrace();
		}
		prevDate = dateFormat.format(dateSelectedFrom.getTime() - MILLIS_IN_DAY);
		return prevDate;
	}

	public static LocalDate getMinDateFromListOfDate(List<String> orderDate) {
		List<LocalDate> dateList = new ArrayList<LocalDate>();
		try {
			for (String date : orderDate) {
				dateList.add(new SimpleDateFormat("yyyy-MM-dd").parse(date).toInstant().atZone(ZoneId.systemDefault()).toLocalDate());
			}
		} catch (ParseException e) {
			e.printStackTrace();
		}
		LocalDate minDate = dateList.stream().min(Comparator.comparing(LocalDate::toEpochDay)).get();
		return minDate;
	}



}