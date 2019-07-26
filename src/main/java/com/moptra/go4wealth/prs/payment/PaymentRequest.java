package com.moptra.go4wealth.prs.payment;

public class PaymentRequest {

	public String AccNo;
	public String BankID;
	public String ClientCode;
	public String EncryptedPassword;
	public String IFSC;
	public String LogOutURL;
	public String MemberCode;
	public String Mode;
	public String Orders[];
	public String TotalAmount;
	
	public PaymentRequest(String AccNo, String BankID, String ClientCode, String EncryptedPassword, String IFSC,
			String LogOutURL, String MemberCode, String Mode, String[] Orders, String TotalAmount) {
		super();
		this.AccNo = AccNo;
		this.BankID = BankID;
		this.ClientCode = ClientCode;
		this.EncryptedPassword = EncryptedPassword;
		this.IFSC = IFSC;
		this.LogOutURL = LogOutURL;
		this.MemberCode = MemberCode;
		this.Mode = Mode;
		this.Orders = Orders;
		this.TotalAmount = TotalAmount;
	}
	
}
