package com.moptra.go4wealth.prs.kyc;

import java.security.cert.X509Certificate;

import javax.net.ssl.SSLContext;

import org.apache.http.conn.ssl.SSLConnectionSocketFactory;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.ssl.TrustStrategy;
import org.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.http.client.HttpComponentsClientHttpRequestFactory;
import org.springframework.stereotype.Component;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.ResourceAccessException;
import org.springframework.web.client.RestTemplate;

import com.moptra.go4wealth.configuration.EkycVerificationConfiguration;
import com.moptra.go4wealth.prs.common.enums.GoForWealthPRSErrorMessageEnum;
import com.moptra.go4wealth.prs.common.exception.GoForWealthPRSException;
import com.moptra.go4wealth.prs.model.AdharVerificationDTO;

@Component
public class KycCallServiceApi {

	private static Logger logger = LoggerFactory.getLogger(KycCallServiceApi.class);

	@Autowired
	EkycVerificationConfiguration eKycVerificationconfiguration;

	public JSONObject checkPanVerification(String panNo) throws GoForWealthPRSException {
		String url = "https://relmf.com/rmf/mowblyserver/ssapi/rmf/prod/SimplySave/getpankyccheck?pan='"+panNo+"'&fund='RMF'";
		logger.info("KYC URL - " + url);
	    JSONObject response = KycCallServiceApi.getBseResponse(url,HttpMethod.GET);
	    logger.info("PAN KYC Response : "+ response);
	    return response;
	}

	public JSONObject adharVerification(AdharVerificationDTO adharVerificationDTO) throws GoForWealthPRSException {
		//String url="https://relmf.com/rmf/mowblyserver/ssapi/rmf/prod/SimplySave/GetAadharEkyc_Checking?AadharNo="+adharVerificationDTO.getAdharNo()+"&Pan="+adharVerificationDTO.getPanNo()+"&InvName="+adharVerificationDTO.getInvName()+"&Mobile="+adharVerificationDTO.getMobile()+"&Email="+adharVerificationDTO.getEmail()+"&ResponseUrl=https://localhost:4200/onboarding";
		String url="https://relmf.com/rmf/mowblyserver/ssapi/rmf/prod/SimplySave/GetAadharEkyc_Checking?AadharNo="+adharVerificationDTO.getAdharNo()+"&Pan="+adharVerificationDTO.getPanNo()+"&InvName="+adharVerificationDTO.getInvName()+"&Mobile="+adharVerificationDTO.getMobile()+"&Email="+adharVerificationDTO.getEmail()+"&ResponseUrl="+eKycVerificationconfiguration.ekycVerificationResponseUrl;
		logger.info("KYC Adhar URL - " + url);
	    JSONObject response = KycCallServiceApi.getBseResponse(url,HttpMethod.GET);
	    logger.info("Aadhaar KYC Response : " + response);
	    return response;
	}

	public JSONObject adharVerificationKarvy(AdharVerificationDTO adharVerificationDTO) throws GoForWealthPRSException {
		String url="https://relmf.com/rmf/mowblyserver/ssapi/rmf/prod/SimplySave/GetAadharEkyc_Checking?AadharNo="+adharVerificationDTO.getAdharNo()+"&Pan="+adharVerificationDTO.getPanNo()+"&InvName="+adharVerificationDTO.getInvName()+"&Mobile="+adharVerificationDTO.getMobile()+"&Email="+adharVerificationDTO.getEmail()+"&ResponseUrl="+eKycVerificationconfiguration.ekycVerificationResponseUrl;
		logger.info("KYC Adhar URL - " + url);
	    JSONObject response = KycCallServiceApi.getBseResponse(url,HttpMethod.GET);
	    logger.info("Aadhaar KYC Response : " + response);
	    return response;
	}
	
	/*public static JSONObject getBseResponse(String url,HttpMethod httpMethod) throws GoForWealthPRSException {
		JSONObject jsonObject = null;
		Object responseObj = null;
		try {
			RestTemplate restTemplate = new RestTemplate();
			ResponseEntity<String> responseEntity = restTemplate.getForEntity(url,String.class);
			if(responseEntity != null) {
				responseObj = responseEntity.getBody();
				jsonObject = new JSONObject(responseObj.toString());
			}
			if(jsonObject==null){
				throw new GoForWealthPRSException(GoForWealthPRSErrorMessageEnum.FAILURE_CODE.getValue(),GoForWealthPRSErrorMessageEnum.PARTIAL_SUCCESS_MESSAGE.getValue());
			}
		} catch (ResourceAccessException ex) {
			logger.error("Exception Occured : ", ex);
			throw new GoForWealthPRSException(GoForWealthPRSErrorMessageEnum.FAILURE_CODE.getValue(),GoForWealthPRSErrorMessageEnum.PARTIAL_SUCCESS_MESSAGE.getValue());
		} catch (HttpClientErrorException ex) {
			logger.error("Exception Occured : ", ex);
			throw new GoForWealthPRSException(GoForWealthPRSErrorMessageEnum.FAILURE_CODE.getValue(),GoForWealthPRSErrorMessageEnum.PARTIAL_SUCCESS_MESSAGE.getValue());
		} catch (Exception e) {
			logger.error("Exception Occured : ", e);
			throw new GoForWealthPRSException(GoForWealthPRSErrorMessageEnum.FAILURE_CODE.getValue(),GoForWealthPRSErrorMessageEnum.PARTIAL_SUCCESS_MESSAGE.getValue());
		}
		logger.info("Out invokeURL()");
		return jsonObject;
	}*/

	public static JSONObject getBseResponse(String url,HttpMethod httpMethod) throws GoForWealthPRSException {
		JSONObject jsonObject = null;
		Object responseObj = null;
		try {
			TrustStrategy acceptingTrustStrategy = (X509Certificate[] chain, String authType) -> true;
		    SSLContext sslContext = org.apache.http.ssl.SSLContexts.custom().loadTrustMaterial(null, acceptingTrustStrategy).build();
		    SSLConnectionSocketFactory csf = new SSLConnectionSocketFactory(sslContext);
		    CloseableHttpClient httpClient = HttpClients.custom().setSSLSocketFactory(csf).build();
		    HttpComponentsClientHttpRequestFactory requestFactory = new HttpComponentsClientHttpRequestFactory();
		    requestFactory.setHttpClient(httpClient);
		    RestTemplate restTemplate = new RestTemplate(requestFactory);
			ResponseEntity<String> responseEntity = restTemplate.getForEntity(url,String.class);
			if(responseEntity != null) {
				responseObj = responseEntity.getBody();
				jsonObject = new JSONObject(responseObj.toString());
			}
			if(jsonObject==null){
				throw new GoForWealthPRSException(GoForWealthPRSErrorMessageEnum.FAILURE_CODE.getValue(),GoForWealthPRSErrorMessageEnum.PARTIAL_SUCCESS_MESSAGE.getValue());
			}
		} catch (ResourceAccessException ex) {
			logger.error("Exception Occured : ", ex);
			throw new GoForWealthPRSException(GoForWealthPRSErrorMessageEnum.FAILURE_CODE.getValue(),GoForWealthPRSErrorMessageEnum.PARTIAL_SUCCESS_MESSAGE.getValue());
		} catch (HttpClientErrorException ex) {
			logger.error("Exception Occured : ", ex);
			throw new GoForWealthPRSException(GoForWealthPRSErrorMessageEnum.FAILURE_CODE.getValue(),GoForWealthPRSErrorMessageEnum.PARTIAL_SUCCESS_MESSAGE.getValue());
		} catch (Exception e) {
			logger.error("Exception Occured : ", e);
			throw new GoForWealthPRSException(GoForWealthPRSErrorMessageEnum.FAILURE_CODE.getValue(),GoForWealthPRSErrorMessageEnum.PARTIAL_SUCCESS_MESSAGE.getValue());
		}
		logger.info("Out invokeURL()");
		return jsonObject;
	}



}