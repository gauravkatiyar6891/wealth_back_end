package com.moptra.go4wealth.prs.payment.client;

import java.net.URL;

import javax.xml.namespace.QName;

import com.moptra.go4wealth.prs.payment.tempuri.IStarMFPaymentGatewayService;
import com.moptra.go4wealth.prs.payment.tempuri.StarMFPaymentGatewayService;

public class MfPaymentGatewayClient {

	 private static final QName SERVICE_NAME = new QName("http://tempuri.org/", "StarMFPaymentGatewayService");
	
	 public static IStarMFPaymentGatewayService getPort(){

		 URL wsdlURL = StarMFPaymentGatewayService.WSDL_LOCATION;
		 StarMFPaymentGatewayService ss = new StarMFPaymentGatewayService(wsdlURL, SERVICE_NAME);
		 IStarMFPaymentGatewayService port = ss.getWSHttpBindingIStarMFPaymentGatewayService();
		 return port;

	 }
	
	 public static void main(String...args) {
		 System.out.println("Invoking getPassword...");
		 com.moptra.go4wealth.prs.payment.starmfpaymentgatewayservice.PasswordRequest _getPassword_param = null;
		 com.moptra.go4wealth.prs.payment.starmfpaymentgatewayservice.Response _getPassword__return = getPort().getPassword(_getPassword_param);
		 System.out.println("getPassword.result=" + _getPassword__return);
	 }
}
