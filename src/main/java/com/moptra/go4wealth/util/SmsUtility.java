package com.moptra.go4wealth.util;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.mashape.unirest.http.HttpResponse;
import com.mashape.unirest.http.Unirest;
import com.mashape.unirest.http.exceptions.UnirestException;
import com.moptra.go4wealth.bean.StoreConf;
import com.moptra.go4wealth.repository.StoreConfRepository;

@Component
public class SmsUtility {
	
	private static Logger logger = LoggerFactory.getLogger(SmsUtility.class);
	
	@Autowired
	StoreConfRepository storeConfRepository;
	
	public void sendOtpToMobile (int otp_length,String mobile,String smsOtp) {
		StoreConf authKeyObj = storeConfRepository.findByKeyword("SMS_AUTH_KEY");
		logger.info("In SmsUtility");
		System.out.println("SmsUtility smsUtility ");
		try {
			String authkey=authKeyObj.getKeywordValue();
			String message="Your%20Verification%20OTP%20is%20";
			String sender="G4WOTP";
			String otp_expiry="15";
			String dynamic_url="http://control.msg91.com/api/sendotp.php?template=template&otp_length="+otp_length+"&authkey="+authkey+"&message="+message+smsOtp+"&sender="+sender+"&mobile="+mobile+"&otp="+smsOtp+"&otp_expiry="+otp_expiry;
			HttpResponse<String> response = Unirest.post(dynamic_url).asString();
			logger.info(response.getBody());
			logger.info("Out SmsUtility");
		} catch (UnirestException e) {
			e.printStackTrace();
		}	
	}


}
