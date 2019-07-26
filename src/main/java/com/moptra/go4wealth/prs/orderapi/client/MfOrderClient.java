package com.moptra.go4wealth.prs.orderapi.client;

import java.net.URL;

import javax.xml.namespace.QName;

import com.moptra.go4wealth.prs.orderapi.in.bsestarmf.MFOrderEntry;
import com.moptra.go4wealth.prs.orderapi.tempuri.MFOrder;



public class MfOrderClient {
	private static final QName SERVICE_NAME = new QName("http://tempuri.org/", "MFOrder");
	public static MFOrderEntry getPort(){
		URL wsdlURL = MFOrder.WSDL_LOCATION;
		MFOrder ss = new MFOrder(wsdlURL, SERVICE_NAME);
        MFOrderEntry port = ss.getWSHttpBindingMFOrderEntry(); 
		return port;
	}
	
	public static void main(String...args) {
		System.out.println("Before Calling getPasswod Method");
		System.out.println("response======"+MfOrderClient.getPort().getPassword("0123", "mf@abc", "abcdef1234"));

		
	}
}
