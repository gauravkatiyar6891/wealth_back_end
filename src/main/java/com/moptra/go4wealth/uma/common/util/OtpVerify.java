package com.moptra.go4wealth.uma.common.util;

import java.util.Date;

import org.springframework.security.crypto.password.PasswordEncoder;

import com.moptra.go4wealth.bean.Otp;
import com.moptra.go4wealth.uma.common.constant.GoForWealthUMAConstants;
import com.moptra.go4wealth.uma.common.enums.GoForWealthErrorMessageEnum;
import com.moptra.go4wealth.uma.common.exception.GoForWealthUMAException;

public class OtpVerify {
	
	/** 
	 * @param otp
	 * @param mobileNumber
	 * @return String
	 * @throws GoForWealthUMAException
	 */
	public static String verifyOtp(String otp, String mobileNumber, Otp otpRef, PasswordEncoder passwordEncoder) {
		Date now = new Date();
		Date previousDate = otpRef.getSentTime();
		long difference = now.getTime() - previousDate.getTime();
		long diffMinutes = difference / (60 * 1000) % 60;
		long diffHours = difference / (60 * 60 * 1000) % 24;
		long diffDays = difference / (24 * 60 * 60 * 1000);
		if(passwordEncoder.matches(otp,otpRef.getOtp())) {
			if(diffDays == 0 && diffHours == 0 && diffMinutes < 5) {
			} else {
				return GoForWealthErrorMessageEnum.OTP_EXPIRED.getValue();
			}
		} else {
			return GoForWealthErrorMessageEnum.INVALID_OTP.getValue();
		}
		return GoForWealthUMAConstants.OTP_VERIFIED_SUCCESS_MESSAGE;
	}
	
	
}