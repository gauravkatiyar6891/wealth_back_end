package com.moptra.go4wealth.prs.payment;

public class GetPasswordRequest {
	
	public String UserId;
	public String Password;
	public String MemberId;
	public String PassKey;
	public GetPasswordRequest(String UserId, String Password, String MemberId,String PassKey) {
		super();
		this.UserId = UserId;
		this.Password = Password;
		this.MemberId = MemberId;
		this.PassKey=PassKey;
	}
	
     
}

