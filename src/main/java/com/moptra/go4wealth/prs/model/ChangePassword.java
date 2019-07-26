package com.moptra.go4wealth.prs.model;

import com.moptra.go4wealth.prs.mfuploadapi.model.GetPasswordRequest;

public class ChangePassword {

	private GetPasswordRequest getPasswordRequest;
	private String userId;
	private String encryptedPassword;
	private String flag;
	private String oldPassword;
	private String newPassword;
	private String confPassword;
	

	public String getFlag() {
		return flag;
	}
	public void setFlag(String flag) {
		this.flag = flag;
	}
	public String getOldPassword() {
		return oldPassword;
	}
	public void setOldPassword(String oldPassword) {
		this.oldPassword = oldPassword;
	}
	public String getNewPassword() {
		return newPassword;
	}
	public void setNewPassword(String newPassword) {
		this.newPassword = newPassword;
	}
	public String getConfPassword() {
		return confPassword;
	}
	public void setConfPassword(String confPassword) {
		this.confPassword = confPassword;
	}
	public GetPasswordRequest getGetPasswordRequest() {
		return getPasswordRequest;
	}
	public void setGetPasswordRequest(GetPasswordRequest getPasswordRequest) {
		this.getPasswordRequest = getPasswordRequest;
	}
	public String getUserId() {
		return userId;
	}
	public void setUserId(String userId) {
		this.userId = userId;
	}
	public String getEncryptedPassword() {
		return encryptedPassword;
	}
	public void setEncryptedPassword(String encryptedPassword) {
		this.encryptedPassword = encryptedPassword;
	}



}
