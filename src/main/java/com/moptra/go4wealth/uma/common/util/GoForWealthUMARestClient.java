package com.moptra.go4wealth.uma.common.util;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.ResourceAccessException;
import org.springframework.web.client.RestTemplate;

import com.moptra.go4wealth.uma.common.enums.GoForWealthErrorMessageEnum;
import com.moptra.go4wealth.uma.common.exception.GoForWealthUMAException;


public class GoForWealthUMARestClient {

	private static Logger logger = LoggerFactory.getLogger(GoForWealthUMARestClient.class);


	@SuppressWarnings("unchecked")
	public static Object invokeURL(String url, Object requestObject,Class responsePojo, HttpHeaders headersToSend, HttpMethod httpMethod) throws GoForWealthUMAException {
		Object responseObj = null;
		try {
			logger.info("In invokeURL()");
			RestTemplate restTemplate = new RestTemplate();

			HttpEntity<?> requestEntity = new HttpEntity<Object>(requestObject,	headersToSend);
			logger.info("URL to Invoke : " + url);
			logger.info("Request Entity going to :" + requestEntity);
			logger.info("Request Entity JSON going to :" + GoForWealthUMAUtil.getJsonFromObject(requestEntity,null));
			ResponseEntity<Object> responseEntity = restTemplate.exchange(url,httpMethod, requestEntity, responsePojo);
			logger.info("Response Entity got is :"+ responseEntity);

			if (responseEntity != null) {
				responseObj = responseEntity.getBody();
			}
			logger.info("Response body of the Response Entity is: "+ responseObj);
		} catch (ResourceAccessException ex) {
			logger.error("Exception Occured : ", ex);
			throw new GoForWealthUMAException(
					GoForWealthErrorMessageEnum.FAILURE_CODE.getValue(),
					GoForWealthErrorMessageEnum.FAILURE_MESSAGE.getValue());
		} catch (HttpClientErrorException ex) {
			logger.error("Exception Occured : ", ex);
			throw new GoForWealthUMAException(
					GoForWealthErrorMessageEnum.FAILURE_CODE.getValue(),
					GoForWealthErrorMessageEnum.FAILURE_MESSAGE.getValue());
		} catch (Exception e) {
			logger.error("Exception Occured : ", e);
			throw new GoForWealthUMAException(
					GoForWealthErrorMessageEnum.FAILURE_CODE.getValue(),
					GoForWealthErrorMessageEnum.FAILURE_MESSAGE.getValue());
		}
		logger.info("Out invokeURL()");
		return responseObj;
	}

}
