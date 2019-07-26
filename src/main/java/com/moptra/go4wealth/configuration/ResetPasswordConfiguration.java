package com.moptra.go4wealth.configuration;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@Component
@ConfigurationProperties(prefix="resetpassword")
public class ResetPasswordConfiguration {

	public int expirationTimeMin;
	public String mailSenderAddress;
	public String changePasswordUrl;

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

	public String getChangePasswordUrl() {
		return changePasswordUrl;
	}

	public void setChangePasswordUrl(String changePasswordUrl) {
		this.changePasswordUrl = changePasswordUrl;
	}

}
