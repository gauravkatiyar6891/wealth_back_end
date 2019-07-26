package com.moptra.go4wealth.prs.imageupload;

public class GetPasswordResponse {

	private String statusCode;
	private String encryotedPassword;
	
	public String getStatusCode() {
		return statusCode;
	}
	public void setStatusCode(String statusCode) {
		this.statusCode = statusCode;
	}
	public String getEncryotedPassword() {
		return encryotedPassword;
	}
	public void setEncryotedPassword(String encryotedPassword) {
		this.encryotedPassword = encryotedPassword;
	}
	
}
