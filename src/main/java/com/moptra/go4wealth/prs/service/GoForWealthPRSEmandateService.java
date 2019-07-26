package com.moptra.go4wealth.prs.service;

import java.io.IOException;
import java.util.List;

import com.moptra.go4wealth.bean.User;
import com.moptra.go4wealth.prs.common.exception.GoForWealthPRSException;
import com.moptra.go4wealth.prs.mfuploadapi.model.MandateRequest;
import com.moptra.go4wealth.prs.model.AddressProofDTO;
import com.moptra.go4wealth.prs.model.BankDetailsDTO;
import com.moptra.go4wealth.prs.model.BasicDetailsDTO;
import com.moptra.go4wealth.prs.model.FileDetailsDTO;
import com.moptra.go4wealth.sip.model.CityDTO;

public interface GoForWealthPRSEmandateService {
	
	public String verifyPanNumber(Integer userId, String panNumber) throws GoForWealthPRSException;
	
	public String verifyAadhaarNumber(Integer userId, String aadhaarNumber,String mobileWithAadhaar,String addressProofName) throws GoForWealthPRSException;
	
	public String updateEkycStatus(Integer userId,String karvyResponse) throws GoForWealthPRSException;
	
	public String storeBasicDetails(Integer userId, BasicDetailsDTO basicDetails);

	public String storeBankDetails(Integer userId, MandateRequest mandateRequest);

	public String uploadSignature(Integer userId, FileDetailsDTO fileDetails) throws IOException, GoForWealthPRSException;
	
	public String showImage(Integer id, String type);
	
	public String showUserDocument(String name, String type);

    public String storeKycDetails(Integer userId, BasicDetailsDTO basicDetails);

	public String uploadPancard(Integer userId, FileDetailsDTO fileDetails) throws IOException;

	public String storeAddressProof(Integer userId, AddressProofDTO addressProofDTO) throws GoForWealthPRSException;

	public BankDetailsDTO verifyIfsc(Integer userId, String ifsc);
	
	String mandateUserByAdmin(User user, boolean isFatca);

	String storeAadhaarDetail(Integer userId, AddressProofDTO addressProofDTO) throws GoForWealthPRSException;

	public List<CityDTO> getCityListByState(String stateName);

	public String getUrl();

	public String updateEmandate(Integer userId, BankDetailsDTO bankDetailsDTO);

	//public String registerAndMandateUser(Integer userId);
	
	boolean updateOnboardingStatus(Integer userId);

	public String changePassword();
	
	public String downloadMandateFileImage(User user, String mandateFormType);

	public String uploadMandateFileImage(User user, FileDetailsDTO fileDetails);
}
