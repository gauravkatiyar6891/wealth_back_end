package com.moptra.go4wealth.prs.kyc;

import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.moptra.go4wealth.prs.common.exception.GoForWealthPRSException;
import com.moptra.go4wealth.prs.model.AdharVerificationDTO;

@Service
public class KycCallService {

	@Autowired
	KycCallServiceApi kycCallServiceApi;

	public JSONObject checkPanVerification(String panNo) throws GoForWealthPRSException{
		JSONObject response = kycCallServiceApi.checkPanVerification(panNo);
		return response;
	}

	public JSONObject adharVerification(AdharVerificationDTO adharVerificationDTO) throws GoForWealthPRSException{
		JSONObject response = kycCallServiceApi.adharVerification(adharVerificationDTO);
		System.out.println("Aadhar Response : " + response);
		return response;
	}

	public JSONObject afterVerification(AdharVerificationDTO adharVerificationDTO) throws GoForWealthPRSException{
		JSONObject response = kycCallServiceApi.adharVerification(adharVerificationDTO);
		System.out.println("Aadhar Response : " + response);
		return response;
	}


}