package com.moptra.go4wealth.prs.orderapi.request;

public class MandateAuthenticationCheckRequest {
	
	public String ClientCode;
	public String MandateID;
	public String MemberCode;
	public String Password;
	public String UserId;
	
	public MandateAuthenticationCheckRequest(String clientCode, String mandateID, String memberCode, String password,String userId) {
		super();
		ClientCode = clientCode;
		MandateID = mandateID;
		MemberCode = memberCode;
		Password = password;
		UserId = userId;
	}
	
	
}
