package com.moptra.go4wealth.prs.imageupload;

import java.io.IOException;

import org.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.ResourceAccessException;
import org.springframework.web.client.RestTemplate;

import com.moptra.go4wealth.bean.StoreConf;
import com.moptra.go4wealth.prs.common.constant.GoForWealthPRSConstants;
import com.moptra.go4wealth.prs.common.enums.GoForWealthPRSErrorMessageEnum;
import com.moptra.go4wealth.prs.common.exception.GoForWealthPRSException;
import com.moptra.go4wealth.prs.common.util.GoForWealthPRSUtil;
import com.moptra.go4wealth.repository.StoreConfRepository;

@Component
public class ImageUploadCallApi {

	@Autowired
	StoreConfRepository storeConfRepository;
	private static Logger logger = LoggerFactory.getLogger(ImageUploadCallApi.class);
	
	public String getPassword(GetPasswordRequest getPasswordRequest) throws GoForWealthPRSException{		
		String password=null;
		String data=GoForWealthPRSUtil.jaxbObjectToXMLForPassword(getPasswordRequest);
		HttpHeaders httpHeaders = new HttpHeaders();
		httpHeaders.setContentType(MediaType.APPLICATION_XML);
		
		StoreConf storeConf=storeConfRepository.findByKeyword(GoForWealthPRSConstants.AOF_GET_PASSWORD_URL);
		String url = storeConf.getKeywordValue();
		//String url="http://www.bsestarmf.in/StarMFFileUploadService/StarMFFileUploadService.svc/GetPassword";
	    password=ImageUploadCallApi.getBseResponse(url, data, httpHeaders, HttpMethod.POST);
		return password;
	}
	
	
	
	public String uploadImage(FileUploadRequest fileUploadRequest) throws IOException, GoForWealthPRSException{		
			
			String fileUploadData=GoForWealthPRSUtil.jaxbObjectToXMLForUpload(fileUploadRequest);
			HttpHeaders httpHeaders = new HttpHeaders();
			httpHeaders.setContentType(MediaType.APPLICATION_XML);
			StoreConf storeConf=storeConfRepository.findByKeyword(GoForWealthPRSConstants.AOF_UPLOAD_URL);
			String url = storeConf.getKeywordValue();
			//String url="http://www.bsestarmf.in/StarMFFileUploadService/StarMFFileUploadService.svc/UploadFile";
			System.out.println("AOF requeset: "+fileUploadData);
		    String uploadImageResponse=	ImageUploadCallApi.getBseResponse(url, fileUploadData, httpHeaders, HttpMethod.POST);
		
		return uploadImageResponse;
	}
	
	
	public static String getBseResponse(String url, String jsonResponse, HttpHeaders headersToSend,
			HttpMethod httpMethod) throws GoForWealthPRSException {
		
		String response=null;
		Object responseObj = null;
		try {
			RestTemplate restTemplate = new RestTemplate();
			HttpEntity<Object> requestEntity = new HttpEntity<Object>(jsonResponse,headersToSend);
			ResponseEntity<String> responseEntity = restTemplate.postForEntity(url, requestEntity, String.class);

			if (responseEntity != null) {
				responseObj = responseEntity.getBody();
				JSONObject jsonObject = new JSONObject(responseObj.toString());
				logger.info(jsonObject.getString("ResponseString"));
				response=jsonObject.getString("Status")+"|"+jsonObject.getString("ResponseString");
				
			}			
			if(response==null){
				throw new GoForWealthPRSException(GoForWealthPRSErrorMessageEnum.FAILURE_CODE.getValue(),GoForWealthPRSErrorMessageEnum.FAILURE_MESSAGE.getValue());
			}
		} catch (ResourceAccessException ex) {
			logger.error("Exception Occured : ", ex);
			throw new GoForWealthPRSException(
					GoForWealthPRSErrorMessageEnum.FAILURE_CODE.getValue(),
					GoForWealthPRSErrorMessageEnum.FAILURE_MESSAGE.getValue());
		} catch (HttpClientErrorException ex) {
			logger.error("Exception Occured : ", ex);
			throw new GoForWealthPRSException(
					GoForWealthPRSErrorMessageEnum.FAILURE_CODE.getValue(),
					GoForWealthPRSErrorMessageEnum.FAILURE_MESSAGE.getValue());
		} catch (Exception e) {
			logger.error("Exception Occured : ", e);
			throw new GoForWealthPRSException(
					GoForWealthPRSErrorMessageEnum.FAILURE_CODE.getValue(),
					GoForWealthPRSErrorMessageEnum.FAILURE_MESSAGE.getValue());
		}
		logger.info("Out invokeURL()");
		return response;
		
	}



	public String uploadMandateFile(MandateFormUploadRequest mandateFormUploadRequest) {
		
		String fileUploadData=GoForWealthPRSUtil.jaxbObjectToXMLForMandateUpload(mandateFormUploadRequest);
		HttpHeaders httpHeaders = new HttpHeaders();
		httpHeaders.setContentType(MediaType.APPLICATION_XML);
		StoreConf storeConf=storeConfRepository.findByKeyword(GoForWealthPRSConstants.MANDATE_UPLOAD_URL);
		String url = storeConf.getKeywordValue();
		
		System.out.println("Mandate Upload requeset: ======== "+fileUploadData);
	    String uploadImageResponse = "";
		try {
			uploadImageResponse = ImageUploadCallApi.getMandateBseResponse(url, fileUploadData, httpHeaders, HttpMethod.POST);
			System.out.println("Mandate Upload Response == :"+uploadImageResponse);
		} catch (GoForWealthPRSException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	
	   return uploadImageResponse;
	}

	
	public static String getMandateBseResponse(String url, String jsonResponse, HttpHeaders headersToSend,
			HttpMethod httpMethod) throws GoForWealthPRSException {
		
		String response=null;
		Object responseObj = null;
		try {
			RestTemplate restTemplate = new RestTemplate();
			HttpEntity<Object> requestEntity = new HttpEntity<Object>(jsonResponse,headersToSend);
			ResponseEntity<String> responseEntity = restTemplate.postForEntity(url, requestEntity, String.class);

			if (responseEntity != null) {
				responseObj = responseEntity.getBody();
				JSONObject jsonObject = new JSONObject(responseObj.toString());
				logger.info(jsonObject.getString("ResponseString"));
				response=jsonObject.getString("Status")+"|"+jsonObject.getString("ResponseString");
				
			}			
			if(response==null){
				throw new GoForWealthPRSException(GoForWealthPRSErrorMessageEnum.FAILURE_CODE.getValue(),GoForWealthPRSErrorMessageEnum.FAILURE_MESSAGE.getValue());
			}
		} catch (ResourceAccessException ex) {
			logger.error("Exception Occured : ", ex);
			throw new GoForWealthPRSException(
					GoForWealthPRSErrorMessageEnum.FAILURE_CODE.getValue(),
					GoForWealthPRSErrorMessageEnum.FAILURE_MESSAGE.getValue());
		} catch (HttpClientErrorException ex) {
			logger.error("Exception Occured : ", ex);
			throw new GoForWealthPRSException(
					GoForWealthPRSErrorMessageEnum.FAILURE_CODE.getValue(),
					GoForWealthPRSErrorMessageEnum.FAILURE_MESSAGE.getValue());
		} catch (Exception e) {
			logger.error("Exception Occured : ", e);
			throw new GoForWealthPRSException(
					GoForWealthPRSErrorMessageEnum.FAILURE_CODE.getValue(),
					GoForWealthPRSErrorMessageEnum.FAILURE_MESSAGE.getValue());
		}
		logger.info("Out invokeURL()");
		return response;
		
	}
	
}
