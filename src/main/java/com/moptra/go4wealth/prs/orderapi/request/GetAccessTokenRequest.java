package com.moptra.go4wealth.prs.orderapi.request;

public class GetAccessTokenRequest {

	public String RequestType;
	public String UserId;
	public String MemberId;
	public String Password;
	public String PassKey;
	
	
	public GetAccessTokenRequest(String requestType, String userId, String memberId, String password, String passKey) {
		super();
		RequestType = requestType;
		UserId = userId;
		MemberId = memberId;
		Password = password;
		PassKey = passKey;
	}
	
	
}
