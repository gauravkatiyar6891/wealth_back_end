package com.moptra.go4wealth.prs.orderapi.request;

public class GetMandateDetailRequest {

	public String FromDate;
	public String ToDate;
	public String MemberCode;
	public String ClientCode;
	public String MandateId;
	public String EncryptedPassword;
	
	
	public GetMandateDetailRequest(String fromDate, String toDate, String memberCode, String clientCode,
			String mandateId, String encryptedPassword) {
		super();
		FromDate = fromDate;
		ToDate = toDate;
		MemberCode = memberCode;
		ClientCode = clientCode;
		MandateId = mandateId;
		EncryptedPassword = encryptedPassword;
	}
	
}
