package com.moptra.go4wealth.sip.common.util;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.ResourceAccessException;
import org.springframework.web.client.RestTemplate;

import com.moptra.go4wealth.sip.common.enums.GoForWealthSIPErrorMessageEnum;
import com.moptra.go4wealth.sip.common.exception.GoForWealthSIPException;


public class GoForWealthSIPRestClient {

	private static Logger logger = LoggerFactory.getLogger(GoForWealthSIPRestClient.class);

	@SuppressWarnings("unchecked")
	public static Object invokeURL(String url, Object requestObject,Class responsePojo, HttpHeaders headersToSend, HttpMethod httpMethod) throws GoForWealthSIPException {
		Object responseObj = null;
		try {
			logger.info("In invokeURL()");
			RestTemplate restTemplate = new RestTemplate();

			HttpEntity<?> requestEntity = new HttpEntity<Object>(requestObject,	headersToSend);
			logger.info("URL to Invoke : " + url);
			logger.info("Request Entity going to :" + requestEntity);
			logger.info("Request Entity JSON going to :" + GoForWealthSIPUtil.getJsonFromObject(requestEntity,null));
			ResponseEntity<Object> responseEntity = restTemplate.exchange(url,httpMethod, requestEntity, responsePojo);
			logger.info("Response Entity got is :"+ responseEntity);

			if (responseEntity != null) {
				responseObj = responseEntity.getBody();
			}
			logger.info("Response body of the Response Entity is: "+ responseObj);
		} catch (ResourceAccessException ex) {
			logger.error("Exception Occured : ", ex);
			throw new GoForWealthSIPException(
					GoForWealthSIPErrorMessageEnum.FAILURE_CODE.getValue(),
					GoForWealthSIPErrorMessageEnum.FAILURE_MESSAGE.getValue());
		} catch (HttpClientErrorException ex) {
			logger.error("Exception Occured : ", ex);
			throw new GoForWealthSIPException(
					GoForWealthSIPErrorMessageEnum.FAILURE_CODE.getValue(),
					GoForWealthSIPErrorMessageEnum.FAILURE_MESSAGE.getValue());
		} catch (Exception e) {
			logger.error("Exception Occured : ", e);
			throw new GoForWealthSIPException(
					GoForWealthSIPErrorMessageEnum.FAILURE_CODE.getValue(),
					GoForWealthSIPErrorMessageEnum.FAILURE_MESSAGE.getValue());
		}
		logger.info("Out invokeURL()");
		return responseObj;
	}

}
