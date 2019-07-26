/*package com.moptra.go4wealth.configuration;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@Component
@ConfigurationProperties(prefix="jwt")
public class JwtConfiguration {

    public int expirationTimeMin;
    public String secret;
    public String headerName;
    public int otpExpirationTimeMin;

    public int getExpirationTimeMin() {
        return expirationTimeMin;
    }

    public void setExpirationTimeMin(int expirationTimeMin) {
        this.expirationTimeMin = expirationTimeMin;
    }

    public String getSecret() {
        return secret;
    }

    public void setSecret(String secret) {
        this.secret = secret;
    }

    public String getHeaderName() {
        return headerName;
    }

    public void setHeaderName(String headerName) {
        this.headerName = headerName;
    }

	public void setOtpExpirationTimeMin(int otpExpirationTimeMin) {
		this.otpExpirationTimeMin = otpExpirationTimeMin;
	}

}
*/


package com.moptra.go4wealth.configuration;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@Component
@ConfigurationProperties(prefix="jwt")
public class JwtConfiguration {

    public int expirationTimeMin;
    public int androidExpirationTimeMin;
    public String secret;
    public String headerName;
    public int otpExpirationTimeMin;
    public String Platform;
    
    
    public int getAndroidExpirationTimeMin() {
		return androidExpirationTimeMin;
	}
    public void setAndroidExpirationTimeMin(int androidExpirationTimeMin) {
		this.androidExpirationTimeMin = androidExpirationTimeMin;
	}
    
    
  public String getPlatform() {
	return Platform;
     }
  public void setPlatform(String platform) {
	Platform = platform;
    }
  
    public int getExpirationTimeMin() {
        return expirationTimeMin;
    }

    public void setExpirationTimeMin(int expirationTimeMin) {
        this.expirationTimeMin = expirationTimeMin;
    }

    public String getSecret() {
        return secret;
    }

    public void setSecret(String secret) {
        this.secret = secret;
    }

    public String getHeaderName() {
        return headerName;
    }

    public void setHeaderName(String headerName) {
        this.headerName = headerName;
    }

	public void setOtpExpirationTimeMin(int otpExpirationTimeMin) {
		this.otpExpirationTimeMin = otpExpirationTimeMin;
	}

}