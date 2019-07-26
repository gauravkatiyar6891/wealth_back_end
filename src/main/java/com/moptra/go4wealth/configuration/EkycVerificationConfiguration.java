package com.moptra.go4wealth.configuration;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@Component
@ConfigurationProperties(prefix="ekyc")
public class EkycVerificationConfiguration {

	public String ekycVerificationResponseUrl;
	public String ekycVerificationRemoveQueryParamUrl;

	public void setEkycVerificationRemoveQueryParamUrl(String ekycVerificationRemoveQueryParamUrl) {
		this.ekycVerificationRemoveQueryParamUrl = ekycVerificationRemoveQueryParamUrl;
	}
	
	public String getEkycVerificationRemoveQueryParamUrl() {
		return ekycVerificationRemoveQueryParamUrl;
	}
	
	public String getEkycVerificationResponseUrl() {
		return ekycVerificationResponseUrl;
	}

	public void setEkycVerificationResponseUrl(String ekycVerificationResponseUrl) {
		this.ekycVerificationResponseUrl = ekycVerificationResponseUrl;
	}
	
}