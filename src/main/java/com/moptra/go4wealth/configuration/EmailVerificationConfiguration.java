package com.moptra.go4wealth.configuration;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@Component
@ConfigurationProperties(prefix="verifyemail")
public class EmailVerificationConfiguration {

	public int expirationTimeMin;
	public String mailSenderAddress;
	public String verifyEmailUrl;
	
	public int getExpirationTimeMin() {
		return expirationTimeMin;
	}
	public void setExpirationTimeMin(int expirationTimeMin) {
		this.expirationTimeMin = expirationTimeMin;
	}
	public String getMailSenderAddress() {
		return mailSenderAddress;
	}
	public void setMailSenderAddress(String mailSenderAddress) {
		this.mailSenderAddress = mailSenderAddress;
	}
	public String getVerifyEmailUrl() {
		return verifyEmailUrl;
	}
	public void setVerifyEmailUrl(String verifyEmailUrl) {
		this.verifyEmailUrl = verifyEmailUrl;
	}
	
	
}
