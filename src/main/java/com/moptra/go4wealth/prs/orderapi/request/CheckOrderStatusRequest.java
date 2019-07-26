package com.moptra.go4wealth.prs.orderapi.request;

public class CheckOrderStatusRequest {

	
	public String ClientCode;
	public String Filler1;
	public String Filler2;
	public String Filler3;
	public String FromDate;
	public String MemberCode;
	public String OrderNo;
	public String OrderStatus;
	public String OrderType;
	public String Password;
	public String SettType;
	public String SubOrderType;
	public String ToDate;
	public String TransType;
	public String UserId;
	
	public CheckOrderStatusRequest(String clientCode, String filler1, String filler2, String filler3, String fromDate,
			String memberCode, String orderNo, String orderStatus, String orderType, String password,
			String settlementType, String subOrderType, String toDate, String transactionType, String userId) {
		super();
		ClientCode = clientCode;
		Filler1 = filler1;
		Filler2 = filler2;
		Filler3 = filler3;
		FromDate = fromDate;
		MemberCode = memberCode;
		OrderNo = orderNo;
		OrderStatus = orderStatus;
		OrderType = orderType;
		Password = password;
		SettType = settlementType;
		SubOrderType = subOrderType;
		ToDate = toDate;
		TransType = transactionType;
		UserId = userId;
	}
	
	
	
	
}
