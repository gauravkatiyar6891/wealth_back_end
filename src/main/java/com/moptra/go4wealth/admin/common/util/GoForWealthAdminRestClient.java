package com.moptra.go4wealth.admin.common.util;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.ResourceAccessException;
import org.springframework.web.client.RestTemplate;

import com.moptra.go4wealth.admin.common.enums.GoForWealthAdminErrorMessageEnum;
import com.moptra.go4wealth.admin.common.exception.GoForWealthAdminException;


public class GoForWealthAdminRestClient {

	private static Logger logger = LoggerFactory.getLogger(GoForWealthAdminRestClient.class);


	@SuppressWarnings("unchecked")
	public static Object invokeURL(String url, Object requestObject,Class responsePojo, HttpHeaders headersToSend, HttpMethod httpMethod) throws GoForWealthAdminException {
		Object responseObj = null;
		try {
			logger.info("In invokeURL()");
			RestTemplate restTemplate = new RestTemplate();

			HttpEntity<?> requestEntity = new HttpEntity<Object>(requestObject,	headersToSend);
			logger.info("URL to Invoke : " + url);
			logger.info("Request Entity going to :" + requestEntity);
			logger.info("Request Entity JSON going to :" + GoForWealthAdminUtil.getJsonFromObject(requestEntity,null));
			ResponseEntity<Object> responseEntity = restTemplate.exchange(url,httpMethod, requestEntity, responsePojo);
			logger.info("Response Entity got is :"+ responseEntity);

			if (responseEntity != null) {
				responseObj = responseEntity.getBody();
			}
			logger.info("Response body of the Response Entity is: "+ responseObj);
		} catch (ResourceAccessException ex) {
			logger.error("Exception Occured : ", ex);
			throw new GoForWealthAdminException(
					GoForWealthAdminErrorMessageEnum.FAILURE_CODE.getValue(),
					GoForWealthAdminErrorMessageEnum.FAILURE_MESSAGE.getValue());
		} catch (HttpClientErrorException ex) {
			logger.error("Exception Occured : ", ex);
			throw new GoForWealthAdminException(
					GoForWealthAdminErrorMessageEnum.FAILURE_CODE.getValue(),
					GoForWealthAdminErrorMessageEnum.FAILURE_MESSAGE.getValue());
		} catch (Exception e) {
			logger.error("Exception Occured : ", e);
			throw new GoForWealthAdminException(
					GoForWealthAdminErrorMessageEnum.FAILURE_CODE.getValue(),
					GoForWealthAdminErrorMessageEnum.FAILURE_MESSAGE.getValue());
		}
		logger.info("Out invokeURL()");
		return responseObj;
	}

}
