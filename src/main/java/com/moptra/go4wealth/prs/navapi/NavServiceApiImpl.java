package com.moptra.go4wealth.prs.navapi;

import java.net.URL;
import java.util.List;

import javax.xml.bind.JAXBElement;
import javax.xml.namespace.QName;

import org.json.JSONArray;
import org.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.ResourceAccessException;
import org.springframework.web.client.RestTemplate;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.gson.Gson;
import com.moptra.go4wealth.bean.SystemProperties;
import com.moptra.go4wealth.prs.common.enums.GoForWealthPRSErrorMessageEnum;
import com.moptra.go4wealth.prs.common.exception.GoForWealthPRSException;
import com.moptra.go4wealth.prs.model.NavApiResponseDTO;
import com.moptra.go4wealth.prs.navapi.icra.authentication.AuthService;
import com.moptra.go4wealth.prs.navapi.icra.authentication.IAuthenticationService;
import com.moptra.go4wealth.prs.navapi.icra.datacontracts.AuthToken;
import com.moptra.go4wealth.repository.SystemPropertiesRepository;

@Component
public class NavServiceApiImpl {

	@Autowired
	SystemPropertiesRepository systemPropertiesRepository;
	
	private static final QName SERVICE_NAME = new QName("http://ICRA.SOAP.DataProdiver/Authentication", "AuthService");
	private static Logger logger = LoggerFactory.getLogger(NavServiceApiImpl.class);
	
	public static IAuthenticationService getPort(){
		URL wsdlURL = AuthService.WSDL_LOCATION;
		AuthService ss = new AuthService(wsdlURL, SERVICE_NAME);
        IAuthenticationService port = ss.getBasicHttpBindingIAuthenticationService();
        return port;
	}
	
	public String getNavPassword() {
		String navUserId = "";
		String navPassword="";
		IAuthenticationService port = NavServiceApiImpl.getPort();
		List<SystemProperties> systemPropertiesList=  systemPropertiesRepository.findAll();
		for (SystemProperties systemProperties : systemPropertiesList) {
			if(systemProperties.getId().getPropertyKey().equals("NavUserId")){
				navUserId = systemProperties.getId().getPropertyValue();
			}else if(systemProperties.getId().getPropertyKey().equals("NavPassword")){
				navPassword = systemProperties.getId().getPropertyValue();
			}
		}
		
		AuthToken authToken=port.logIn(navUserId, navPassword.getBytes());
		JAXBElement<String> securitykey = authToken.getSecurityKey();
		String authKey = securitykey.getValue();
		logger.info("authKey : " + authKey);
		return authKey;
	}

	public String getCompressedKey(){
		final String securityKey=getNavPassword();
		String compressedKey="";
		String url="http://dataservice.icraonline.com/REST/ICRAOnlineDataService.svc/API/V1/GetCompressedKey?AuthKey="+securityKey;
		HttpHeaders httpHeaders = new HttpHeaders();
		httpHeaders.setContentType(MediaType.APPLICATION_XML);
		try {
			compressedKey = callNavApiToGetCompressedKey(url);
		} catch (GoForWealthPRSException e) {
			e.printStackTrace();
		}
		return compressedKey;
	}
	
	public NavApiResponseDTO getNavData(String amfiCode){
		String key=getCompressedKey();
		NavApiResponseDTO navResponse=null;
		//String url="http://dataservice.icraonline.com/REST/ICRAOnlineDataService.svc/api/v1/"+key+"/GetMFNavRecords/?AmfiCodes="+amfiCode+"&NavDate="+date;
		String url = "http://DATASERVICE.ICRAONLINE.COM/REST/ICRAONLINEDATASERVICE.SVC/API/V1/"+key+"/GETMFSCHEMEPERFORMANCE/?AMFICODES="+amfiCode;
		HttpHeaders httpHeaders = new HttpHeaders();
		httpHeaders.setContentType(MediaType.APPLICATION_XML);
		try {
			navResponse = callNavApiToGetNavData(url);
		} catch (GoForWealthPRSException e) {
			e.printStackTrace();
		}
		return navResponse;
	}
	
	public static String callNavApiToGetCompressedKey(String url) throws GoForWealthPRSException {
		String response=null;
		Object responseObj = null;
		try {
			RestTemplate restTemplate = new RestTemplate();
			ResponseEntity<Object> responseEntity = restTemplate.exchange( url, HttpMethod.GET, null, Object.class);
			if (responseEntity != null) {
				responseObj = responseEntity.getBody();		
				JSONObject jsonObject = new JSONObject(responseObj.toString());
				logger.info("Compressed Key===  "+jsonObject.getString("ApiKey"));
				response=jsonObject.getString("ApiKey");
			}			
			if(response==null){
				throw new GoForWealthPRSException(GoForWealthPRSErrorMessageEnum.FAILURE_CODE.getValue(),GoForWealthPRSErrorMessageEnum.FAILURE_MESSAGE.getValue());
			}
		}catch (ResourceAccessException ex) {
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
	
	public static NavApiResponseDTO callNavApiToGetNavData(String url) throws GoForWealthPRSException {
		NavApiResponseDTO navApiResponseDTO= null;
		Object responseObj = null;
		try {
			RestTemplate restTemplate = new RestTemplate();
			ResponseEntity<Object> responseEntity = restTemplate.exchange( url, HttpMethod.GET, null, Object.class);
			if (responseEntity != null) {
				responseObj = responseEntity.getBody();	
				JSONArray jsonArray= new JSONArray(responseObj.toString());
				System.out.println("Result : " +jsonArray);
				JSONObject jsonObject = jsonArray.getJSONObject(0);
				if(!jsonObject.getBoolean("IsSucess")){
					if(!jsonObject.getString("Information").equals("null")){
						Gson gson = new Gson();
						 navApiResponseDTO = gson.fromJson(jsonObject.toString(), NavApiResponseDTO.class);
						//response=jsonObject.getString("Information");
					}
				}
				else{
					JSONObject jsonObject12 = jsonObject.getJSONObject("Result");
					System.out.println("Result : " + jsonObject12);
					Gson gson = new Gson();
					navApiResponseDTO = gson.fromJson(jsonObject12.toString(), NavApiResponseDTO.class);
					//response=String.valueOf(jsonObject12);
				}
				
			}			
			if(navApiResponseDTO==null){
				throw new GoForWealthPRSException(GoForWealthPRSErrorMessageEnum.FAILURE_CODE.getValue(),GoForWealthPRSErrorMessageEnum.FAILURE_MESSAGE.getValue());
			}
		} catch (ResourceAccessException ex) {
			logger.error("Exception Occured : ", ex);
			return new NavApiResponseDTO();
			/*throw new GoForWealthPRSException(
					GoForWealthPRSErrorMessageEnum.FAILURE_CODE.getValue(),
					GoForWealthPRSErrorMessageEnum.FAILURE_MESSAGE.getValue());*/
		} catch (HttpClientErrorException ex) {
			logger.error("Exception Occured : ", ex);
			return new NavApiResponseDTO();
			/*throw new GoForWealthPRSException(
					GoForWealthPRSErrorMessageEnum.FAILURE_CODE.getValue(),
					GoForWealthPRSErrorMessageEnum.FAILURE_MESSAGE.getValue());*/
		} catch (Exception e) {
			logger.error("Exception Occured : ", e);
			return new NavApiResponseDTO();
			/*throw new GoForWealthPRSException(
					GoForWealthPRSErrorMessageEnum.FAILURE_CODE.getValue(),
					GoForWealthPRSErrorMessageEnum.FAILURE_MESSAGE.getValue());*/
		}
		logger.info("Out invokeURL()");
		return navApiResponseDTO;
		
	}
	
	public Object getMFSchemeMaster(String amfiCode){
		String key = getCompressedKey();
		Object mfSchemeMasterResponse = null;
		String url="http://dataservice.icraonline.com/REST/ICRAOnlineDataService.svc/api/v1/"+key+"/GetMFSchemeMaster?AmfiCodes="+amfiCode;
		HttpHeaders httpHeaders = new HttpHeaders();
		httpHeaders.setContentType(MediaType.APPLICATION_XML);
		try {
			mfSchemeMasterResponse = callApiToGetMFSchemeMasterData(url);
		} catch (GoForWealthPRSException e) {
			e.printStackTrace();
		}
		return mfSchemeMasterResponse;
	}
		
	public static Object callApiToGetMFSchemeMasterData(String url) throws GoForWealthPRSException {
		String response=null;
		Object responseObj = null;
		try {
			RestTemplate restTemplate = new RestTemplate();
			ResponseEntity<Object> responseEntity = restTemplate.exchange( url, HttpMethod.GET, null, Object.class);
			if (responseEntity != null) {
				responseObj = responseEntity.getBody();	
				/*JSONArray jsonArray= new JSONArray(responseObj.toString());
				JSONObject jsonObject = jsonArray.getJSONObject(0);
				if(!jsonObject.getBoolean("IsSucess")){
					if(!jsonObject.getString("Information").equals("null")){
						response=jsonObject.getString("Information");
					}
				}else{
					JSONObject jsonObjectRef = jsonObject.getJSONObject("Result");
					response = jsonObjectRef.toString();
				}*/
			}
			/*if(response==null){
				throw new GoForWealthPRSException(GoForWealthPRSErrorMessageEnum.FAILURE_CODE.getValue(),GoForWealthPRSErrorMessageEnum.FAILURE_MESSAGE.getValue());
			}*/
		}catch(ResourceAccessException ex) {
			logger.error("Exception Occured : ", ex);
			throw new GoForWealthPRSException(GoForWealthPRSErrorMessageEnum.FAILURE_CODE.getValue(),GoForWealthPRSErrorMessageEnum.FAILURE_MESSAGE.getValue());
		}catch(HttpClientErrorException ex) {
			logger.error("Exception Occured : ", ex);
			throw new GoForWealthPRSException(GoForWealthPRSErrorMessageEnum.FAILURE_CODE.getValue(),GoForWealthPRSErrorMessageEnum.FAILURE_MESSAGE.getValue());
		}catch(Exception e) {
			logger.error("Exception Occured : ", e);
			throw new GoForWealthPRSException(GoForWealthPRSErrorMessageEnum.FAILURE_CODE.getValue(),GoForWealthPRSErrorMessageEnum.FAILURE_MESSAGE.getValue());
		}
		logger.info("Out invokeURL()");
		return responseObj;
	}


}
