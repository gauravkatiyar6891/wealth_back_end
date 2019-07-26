

package com.moptra.go4wealth.prs.imageupload;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement(name="PasswordRequest")

public class GetPasswordRequest {
	
	//@XmlElementRef(name = "UserId", type = GetPasswordRequest.class, required = true)
	private String UserId;
	//@XmlElementRef(name = "Password", type = GetPasswordRequest.class, required = true)
	private String Password;
	//@XmlElementRef(name = "MemberId", type = GetPasswordRequest.class, required = true)
	private String MemberId;
	
	/*public GetPasswordRequest(String userId, String password, String memberId) {
		super();
		UserId = userId;
		Password = password;
		MemberId = memberId;
	}*/
	

	@XmlElement(name="UserId")
	public String getUserId() {
		return UserId;
	}
	public void setUserId(String userId) {
		this.UserId = userId;
	}
	
	@XmlElement(name="Password")
	public String getPassword() {
		return Password;
	}
	public void setPassword(String password) {
		this.Password = password;
	}
	@XmlElement(name="MemberId")
	public String getMemberId() {
		return MemberId;
	}
	public void setMemberId(String memberId) {
		this.MemberId = memberId;
	}
	
	
}
