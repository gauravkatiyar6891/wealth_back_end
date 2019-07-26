package com.moptra.go4wealth.prs.orderapi.request;

public class ChildOrderDetailRequest {

	public String ClientCode;
	public String Date;
	public String EncryptedPassword;
	public String MemberCode;
	public String RegnNo;
	public String SystematicPlanType;
	public ChildOrderDetailRequest(String ClientCode, String Date, String EncryptedPassword,String MemberCode,
			String RegnNo,String SystematicPlanType) {
		super();
		this.ClientCode = ClientCode;
		this.Date = Date;
		this.EncryptedPassword = EncryptedPassword;
		this.MemberCode=MemberCode;
		this.RegnNo=RegnNo;
		this.SystematicPlanType=SystematicPlanType;
	}
	
	
}
