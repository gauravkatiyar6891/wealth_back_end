package com.moptra.go4wealth.prs.mfuploadapi.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.moptra.go4wealth.prs.mfuploadapi.BseStarMf.ImpUploadServiceClient;
import com.moptra.go4wealth.prs.mfuploadapi.model.FatcaRequest;
import com.moptra.go4wealth.prs.mfuploadapi.model.MandateRequest;
import com.moptra.go4wealth.prs.mfuploadapi.model.PaymentStatusRequest;
import com.moptra.go4wealth.prs.mfuploadapi.model.UccRequest;
import com.moptra.go4wealth.prs.model.ChangePassword;

@Component
public class MandateService {
	
	@Autowired
	ImpUploadServiceClient impUploadServiceClient;
	
	public String getPassword() {
		String encryptedPasswordRes = impUploadServiceClient.getPassword();
		System.out.println("Encrypt Password :" + encryptedPasswordRes);
		String encryptedPassword = null;
		String response = null;
		if(encryptedPasswordRes.split("\\|")[0].equals("100")){
			encryptedPassword = encryptedPasswordRes.split("\\|")[1];
		}else{
			response = encryptedPasswordRes.split("\\|")[1];
			return response;
		}
		return encryptedPassword;
	}
	
	public String uccRegistration(UccRequest uccRequest) {
		String encryptedPasswordRes = impUploadServiceClient.getPassword(uccRequest.getGetPasswordRequest());
		System.out.println("Encrypt Password :" + encryptedPasswordRes);
		String encryptedPassword = null;
		String response = null;
		if(encryptedPasswordRes.split("\\|")[0].equals("100")){
			encryptedPassword = encryptedPasswordRes.split("\\|")[1];
		}else{
			response = encryptedPasswordRes.split("\\|")[1];
			return response;
		}
		uccRequest.setEncryptedPassword(encryptedPassword);
		response = impUploadServiceClient.uccRegistration(uccRequest);
		System.out.println("UCC Registration Response : " + response);
		return response;
	}
	
	public String registerMandate(MandateRequest mandateRequest) {
		String encryptedPasswordRes = impUploadServiceClient.getPassword(mandateRequest.getGetPasswordRequest());
		System.out.println("Encrypt Password :" + encryptedPasswordRes);
		String encryptedPassword = null;
		String response = null;
		if(encryptedPasswordRes.split("\\|")[0].equals("100")){
			encryptedPassword = encryptedPasswordRes.split("\\|")[1];
		}else{
			response = encryptedPasswordRes.split("\\|")[1];
			return response;
		}
		mandateRequest.setEncryptedPassword(encryptedPassword);
		response = impUploadServiceClient.registerMandate(mandateRequest);
		System.out.println("mandate Response: "+response);
		return response;
	}
	
	public String doFatca(FatcaRequest fatcaRequest){
		String encryptedPasswordRes = impUploadServiceClient.getPassword(fatcaRequest.getGetPasswordRequest());
		String encryptedPassword = null;
		String response = null;
		if(encryptedPasswordRes.split("\\|")[0].equals("100")){
			encryptedPassword = encryptedPasswordRes.split("\\|")[1];
		}else{
			response = encryptedPasswordRes.split("\\|")[1];
			return response;
		}
		fatcaRequest.setEncryptedPassword(encryptedPassword);		
		String fatcaResponse=null;
		fatcaResponse = impUploadServiceClient.doFatca(fatcaRequest);
		return fatcaResponse;
	}
	
	public String getPaymentStatus(PaymentStatusRequest paymentStatusRequest){
		String encryptedPasswordRes = impUploadServiceClient.getPassword(paymentStatusRequest.getGetPasswordRequest());
		String encryptedPassword = null;
		String response = null;
		if(encryptedPasswordRes.split("\\|")[0].equals("100")){
			encryptedPassword = encryptedPasswordRes.split("\\|")[1];
			System.out.println("Get Payment Status Password ===  "+encryptedPassword);
		}else{
			response = encryptedPasswordRes.split("\\|")[1];
			return response;
		}
		paymentStatusRequest.setPassword(encryptedPassword);		
		String paymentResponse=null;
		paymentResponse = impUploadServiceClient.getPaymentStatus(paymentStatusRequest);
		System.out.println("Payment Status Response === "+paymentResponse);
		
		if(paymentResponse.split("\\|")[0].equals("100")){
			paymentResponse = paymentResponse.split("\\|")[1];
		}else{
			response = paymentResponse.split("\\|")[1];
		}
		return paymentResponse;
	}

	
     public String changePassword(ChangePassword changePassword){
    	 String encryptedPasswordRes = impUploadServiceClient.getPassword(changePassword.getGetPasswordRequest());
 		System.out.println("Encrypt Password :" + encryptedPasswordRes);
 		String encryptedPassword = null;
 		String response = null;
 		if(encryptedPasswordRes.split("\\|")[0].equals("100")){
 			encryptedPassword = encryptedPasswordRes.split("\\|")[1];
 		}else{
 			response = encryptedPasswordRes.split("\\|")[1];
 			return response;
 		}
 		changePassword.setEncryptedPassword(encryptedPassword);
 		response = impUploadServiceClient.changePassword(changePassword);
    	 return response;
     }

}
