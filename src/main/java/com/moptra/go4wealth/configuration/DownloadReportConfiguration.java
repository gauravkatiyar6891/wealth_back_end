package com.moptra.go4wealth.configuration;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@Component
@ConfigurationProperties(prefix="downloadreport")
public class DownloadReportConfiguration {

	public int expirationTimeMin;
	public String mailSenderAddress;
	public String downloadReportUrl;
	
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
	public String getDownloadReportUrl() {
		return downloadReportUrl;
	}
	public void setDownloadReportUrl(String downloadReportUrl) {
		this.downloadReportUrl = downloadReportUrl;
	}
}