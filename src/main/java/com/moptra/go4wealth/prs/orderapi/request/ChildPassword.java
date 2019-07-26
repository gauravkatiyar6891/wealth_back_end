package com.moptra.go4wealth.prs.orderapi.request;

public class ChildPassword {

	public String MemberId;
	public String PassKey;
	public String Password;
	public String RequestType;
	public String UserId;
	public ChildPassword(String MemberId,String PassKey, String Password,String RequestType,String UserId) {
		super();
		this.MemberId = MemberId;
		this.PassKey=PassKey;
		this.Password = Password;
		this.RequestType=RequestType;
		this.UserId = UserId;
	}
}
