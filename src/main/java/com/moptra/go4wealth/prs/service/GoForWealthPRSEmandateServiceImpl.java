package com.moptra.go4wealth.prs.service;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.Iterator;
import java.util.List;

import javax.imageio.ImageIO;
import javax.transaction.Transactional;

import org.apache.commons.io.FileUtils;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.rendering.ImageType;
import org.apache.pdfbox.rendering.PDFRenderer;
import org.apache.pdfbox.tools.imageio.ImageIOUtil;
import org.json.JSONException;
import org.json.JSONObject;
import org.opensaml.xml.util.Base64;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.moptra.go4wealth.bean.AddressCity;
import com.moptra.go4wealth.bean.AddressProof;
import com.moptra.go4wealth.bean.AddressState;
import com.moptra.go4wealth.bean.BankDetails;
import com.moptra.go4wealth.bean.IndianIfscCodes;
import com.moptra.go4wealth.bean.IsipAllowedBankList;
import com.moptra.go4wealth.bean.KycDetails;
import com.moptra.go4wealth.bean.OnboardingStatus;
import com.moptra.go4wealth.bean.PanDetails;
import com.moptra.go4wealth.bean.StoreConf;
import com.moptra.go4wealth.bean.SystemProperties;
import com.moptra.go4wealth.bean.SystemPropertiesId;
import com.moptra.go4wealth.bean.User;
import com.moptra.go4wealth.bean.UserAdditionalBankinfo;
import com.moptra.go4wealth.bean.UserMandateHistory;
import com.moptra.go4wealth.configuration.EkycVerificationConfiguration;
import com.moptra.go4wealth.prs.common.constant.GoForWealthPRSConstants;
import com.moptra.go4wealth.prs.common.enums.GoForWealthPRSErrorMessageEnum;
import com.moptra.go4wealth.prs.common.exception.GoForWealthPRSException;
import com.moptra.go4wealth.prs.common.util.EncryptUserDetail;
import com.moptra.go4wealth.prs.common.util.GoForWealthPRSUtil;
import com.moptra.go4wealth.prs.imageupload.FileUploadRequest;
import com.moptra.go4wealth.prs.imageupload.ImageUploadService;
import com.moptra.go4wealth.prs.imageupload.MandateFormUploadRequest;
import com.moptra.go4wealth.prs.kyc.KycCallService;
import com.moptra.go4wealth.prs.mfuploadapi.model.FatcaRequest;
import com.moptra.go4wealth.prs.mfuploadapi.model.GetPasswordRequest;
import com.moptra.go4wealth.prs.mfuploadapi.model.MandateRequest;
import com.moptra.go4wealth.prs.mfuploadapi.model.UccRequest;
import com.moptra.go4wealth.prs.mfuploadapi.service.MandateService;
import com.moptra.go4wealth.prs.model.AOFPdfFormRequestDTO;
import com.moptra.go4wealth.prs.model.AddressProofDTO;
import com.moptra.go4wealth.prs.model.AdharVerificationDTO;
import com.moptra.go4wealth.prs.model.BankDetailsDTO;
import com.moptra.go4wealth.prs.model.BasicDetailsDTO;
import com.moptra.go4wealth.prs.model.ChangePassword;
import com.moptra.go4wealth.prs.model.FileDetailsDTO;
import com.moptra.go4wealth.prs.model.MandatePdfFormRequestDTO;
import com.moptra.go4wealth.repository.AddressCityRepository;
import com.moptra.go4wealth.repository.AddressProofRepository;
import com.moptra.go4wealth.repository.AddressStateRepository;
import com.moptra.go4wealth.repository.BankDetailsRepository;
import com.moptra.go4wealth.repository.IndianIfscCodesRepository;
import com.moptra.go4wealth.repository.IsipAllowedBankListRepository;
import com.moptra.go4wealth.repository.KycDetailsRepository;
import com.moptra.go4wealth.repository.OnboardingStatusRepository;
import com.moptra.go4wealth.repository.PanDetailsRepository;
import com.moptra.go4wealth.repository.StoreConfRepository;
import com.moptra.go4wealth.repository.SystemPropertiesRepository;
import com.moptra.go4wealth.repository.UserAdditionalBankinfoRepository;
import com.moptra.go4wealth.repository.UserMandateHistoryRepository;
import com.moptra.go4wealth.repository.UserRepository;
import com.moptra.go4wealth.sip.model.CityDTO;
import com.moptra.go4wealth.uma.common.util.OtpGenerator;

import sun.misc.BASE64Decoder;

@Service
public class GoForWealthPRSEmandateServiceImpl implements GoForWealthPRSEmandateService {

	private static Logger logger = LoggerFactory.getLogger(GoForWealthPRSEmandateServiceImpl.class);

	@Autowired
	UserRepository userRepository;

	@Autowired
	PanDetailsRepository panDetailsRepository;

	@Autowired
	BankDetailsRepository bankDetailsRepository;

	@Autowired
	private KycDetailsRepository kycDetailsRepository;

	@Autowired
	private AddressProofRepository addressProofRepository;

	@Autowired
	private AddressCityRepository addressCityRepository;

	@Autowired
	private AddressStateRepository addressStateRepository;

	@Autowired
	private IndianIfscCodesRepository indianIfscCodesRepository;

	@Autowired
	private MandateService mandateService;

	@Autowired
	ImageUploadService imageUploadService;

	@Autowired
	StoreConfRepository storeConfRepository;

	@Autowired
	KycCallService kycCallService;

	@Autowired
	EncryptUserDetail encryptUserDetail;

	@Autowired
	SystemPropertiesRepository systemPropertiesRepository;

	@Autowired
	EkycVerificationConfiguration eKycVerificationconfiguration;

	@Autowired
	UserAdditionalBankinfoRepository userAdditionalBankinfoRepository;

	@Autowired
	OnboardingStatusRepository onboardingStatusRepository;

	@Autowired
	UserMandateHistoryRepository userMandateHistoryRepository;

	@Autowired
	IsipAllowedBankListRepository isipAllowedBankListRepository;

	@Override
	public String verifyPanNumber(Integer userId, String panNumber) throws GoForWealthPRSException {
		User user = userRepository.getOne(userId);
		String response = null;
		if (user != null) {
			PanDetails panDetails = null;
			if (user.getPanDetails() != null) {
				List<PanDetails> panDetailsList = panDetailsRepository.findAll();
				for (PanDetails panDetails2 : panDetailsList) {
					if (!panDetails2.getPanNo().equals("")) {
						try {
							String decreptedPanNoRef = encryptUserDetail.decrypt(panDetails2.getPanNo());
							if (panNumber.equals(decreptedPanNoRef.toUpperCase())) {
								panDetails = panDetails2;
								break;
							}
						} catch (Exception ex) {
							ex.printStackTrace();
						}
					}
				}
				// panDetails = panDetailsRepository.findByPanNo(panNumber);
				if (panDetails == null) {
					panDetails = user.getPanDetails();
					try {
						panDetails.setPanNo(encryptUserDetail.encrypt(panNumber));
						panDetails.setVerified(GoForWealthPRSConstants.PAN_NUMBER_NOT_VERIFIED);
						panDetails.setUser(user);
						panDetailsRepository.save(panDetails);
						user.getOnboardingStatus().setKycStatus(0);
						userRepository.save(user);
					} catch (Exception ex) {
						ex.printStackTrace();
					}
					// call kyc api and return
					JSONObject kycResponse = kycCallService.checkPanVerification(panNumber);
					System.out.println("KYC Response = " + kycResponse.toString());
					try {
						if (kycResponse.getString("KycStatus").equals("KYC Registered")
								|| kycResponse.getString("KycStatus").equals("KRA Verified")
								|| kycResponse.getString("KycStatus").contains("Verified")
								|| kycResponse.getString("KycStatus").equals("Old KYC-Registered")) {
							response = GoForWealthPRSConstants.PAN_NUMBER_VERIFIED; // "verified";
							panDetails.setVerified(GoForWealthPRSConstants.PAN_NUMBER_VERIFIED);
							String name = getName(kycResponse.getString("Name"));
							panDetails.setField2(name);
							panDetails.setFullName(name);
							panDetails.setOccupation("");
							panDetails.setDateOfBirth(null);
							panDetails.setGender("");
						}
						else {
							response = GoForWealthPRSConstants.PAN_NUMBER_NOT_VERIFIED; // "not-verified";
							panDetails.setVerified(GoForWealthPRSConstants.PAN_NUMBER_NOT_VERIFIED);
							panDetails.setField2("");
							panDetails.setFullName("");
							panDetails.setOccupation("");
							panDetails.setDateOfBirth(null);
							panDetails.setGender("");
						}
					} catch (JSONException e) {
						e.printStackTrace();
					}
					panDetailsRepository.save(panDetails);
				} else {
					String decryptedPanNo = "";
					try {
						decryptedPanNo = encryptUserDetail.decrypt(user.getPanDetails().getPanNo());
					} catch (Exception ex) {
						ex.printStackTrace();
					}
					if (panNumber.equals(decryptedPanNo.toUpperCase())) {
						response = user.getPanDetails().getVerified();
					} else {
						response = GoForWealthPRSErrorMessageEnum.PAN_NUMBER_ALREADY_REGISTERED.getValue();
					}
				}
			} else {
				List<PanDetails> panDetailsList = panDetailsRepository.findAll();
				for (PanDetails panDetails2 : panDetailsList) {
					try {
						String decreptedPanNoRef = encryptUserDetail.decrypt(panDetails2.getPanNo());
						if (panNumber.equals(decreptedPanNoRef.toUpperCase())) {
							panDetails = panDetails2;
							break;
						}
					} catch (Exception ex) {
						ex.printStackTrace();
					}
				}
				if (panDetails != null) {
					response = GoForWealthPRSErrorMessageEnum.PAN_NUMBER_ALREADY_REGISTERED.getValue();
				} else {
					panDetails = new PanDetails();
					try {
						panDetails.setPanNo(encryptUserDetail.encrypt(panNumber));
						panDetails.setVerified(GoForWealthPRSConstants.PAN_NUMBER_NOT_VERIFIED);
						panDetails.setUser(user);
						panDetailsRepository.save(panDetails);
						user.getOnboardingStatus().setKycStatus(0);
						userRepository.save(user);
					} catch (Exception ex) {
						ex.printStackTrace();
					}
					panDetails.setUser(user);
					JSONObject kycResponse = kycCallService.checkPanVerification(panNumber);
					try {
						if (kycResponse.getString("KycStatus").equals("KYC Registered")
								|| kycResponse.getString("KycStatus").equals("KRA Verified")
								|| kycResponse.getString("KycStatus").contains("Verified")
								|| kycResponse.getString("KycStatus").equals("Old KYC-Registered")) {
							response = GoForWealthPRSConstants.PAN_NUMBER_VERIFIED;
							panDetails.setVerified(GoForWealthPRSConstants.PAN_NUMBER_VERIFIED);
							String name = getName(kycResponse.getString("Name"));
							panDetails.setField2(name);
							panDetails.setFullName(name);
							panDetails.setOccupation("");
							panDetails.setDateOfBirth(null);
							panDetails.setGender("");
						}else {
							response = GoForWealthPRSConstants.PAN_NUMBER_NOT_VERIFIED;// "not-verified";
							panDetails.setVerified(GoForWealthPRSConstants.PAN_NUMBER_NOT_VERIFIED);
						}
					} catch (JSONException e) {
						e.printStackTrace();
					}
					panDetailsRepository.save(panDetails);
				}
			}
			if (response.equals("verified")) {
				user.getOnboardingStatus().setKycStatus(1);
				userRepository.save(user);
			} else {
				user.getOnboardingStatus().setKycStatus(0);
				userRepository.save(user);
			}
			// call the kyc service and modify response variable a/c to it's
			// response
		} else {
			response = GoForWealthPRSConstants.USER_NOT_EXIST;
		}
		return response;
	}

	private String getName(String name) {
		String nameRes = "";
		String[] nameStr = name.trim().split(" +");
		for (int i = 0; i < nameStr.length; i++) {
			if (i == 0)
				nameRes = nameRes + nameStr[i].trim();
			else
				nameRes = nameRes + " " + nameStr[i].trim();
		}
		return nameRes;
	}

	@Override
	public String storeBasicDetails(Integer userId, BasicDetailsDTO basicDetails) {
		User user = userRepository.getOne(userId);
		String response = null;
		if (user != null) {
			
				PanDetails panDetails = user.getPanDetails();
				if(panDetails != null){
				// panDetails.setFullName(basicDetails.getFullName());
				panDetails.setDateOfBirth(basicDetails.getDob());
				panDetails.setGender(basicDetails.getGender());
				panDetails.setOccupation(basicDetails.getOccupation());
				if (panDetails.getField2() != null && panDetails.getField2().equalsIgnoreCase(basicDetails.getFullName())) {
					panDetails.setFullName(basicDetails.getFullName());
					panDetailsRepository.save(panDetails);
					response = GoForWealthPRSConstants.SUCCESS;
				} else if (panDetails.getField2() == null || panDetails.getField2().equals("")) {
					panDetails.setFullName(basicDetails.getFullName());
					panDetailsRepository.save(panDetails);
					response = GoForWealthPRSConstants.SUCCESS;
				} else {
					response = GoForWealthPRSConstants.NAME_NOT_MATCH;
				}
			}else {
				return "Pan Detail Does Not Exist";
			}
		} else {
			response = GoForWealthPRSConstants.USER_NOT_EXIST;
		}
		if (user != null) {
			if (user.getAddressProofs() != null && user.getAddressProofs().size() > 0) {
				Iterator<AddressProof> itr = user.getAddressProofs().iterator();
				while (itr.hasNext()) {
					AddressProof addressProof = itr.next();
					addressProof.setAddressProofName("User Address");
					addressProof.setPincode(basicDetails.getPinCode());
					addressProof.setState(basicDetails.getState());
					addressProof.setCity(basicDetails.getCity());
					addressProof.setAddressType(basicDetails.getAddressType());
					addressProof.setIncomeSlab(basicDetails.getIncomeSlab());
					addressProof.setPep(basicDetails.getPep());
					if (basicDetails.getAddressLine2() != null && !basicDetails.getAddressLine2().equals(""))
						addressProof.setAddress(basicDetails.getAddressLine1() + " " + basicDetails.getAddressLine2());
					else
						addressProof.setAddress(basicDetails.getAddressLine1());
					addressProof.setField2(basicDetails.getAddressLine1());
					addressProof.setField3(basicDetails.getAddressLine2());
					addressProofRepository.save(addressProof);
				}
			} else {
				AddressProof addressProof = new AddressProof();
				addressProof.setAddressProofName("User Address");
				if (basicDetails.getAddressLine2() != null && !basicDetails.getAddressLine2().equals(""))
					addressProof.setAddress(basicDetails.getAddressLine1() + " " + basicDetails.getAddressLine2());
				else
					addressProof.setAddress(basicDetails.getAddressLine1());
				addressProof.setField2(basicDetails.getAddressLine1());
				addressProof.setField3(basicDetails.getAddressLine2());
				addressProof.setAddress(basicDetails.getAddress());
				addressProof.setPincode(basicDetails.getPinCode());
				addressProof.setState(basicDetails.getState());
				addressProof.setCity(basicDetails.getCity());
				addressProof.setAddressType(basicDetails.getAddressType());
				addressProof.setIncomeSlab(basicDetails.getIncomeSlab());
				addressProof.setPep(basicDetails.getPep());
				addressProof.setUser(user);
				addressProofRepository.save(addressProof);
			}
		} else {
			response = GoForWealthPRSConstants.USER_NOT_EXIST;
		}
		return response;
	}

	@Override
	public String storeBankDetails(Integer userId, MandateRequest mandateRequest) {
		DateFormat df = new SimpleDateFormat("yyyy-MM-dd");
		User user = userRepository.getOne(userId);
		String response = null;
		BankDetails bankDetails = null;
		//BankDetails bankDetailsTemp = bankDetailsRepository.findBankDetailsByAccountNo(bankAccountNo);
		List<BankDetails> bankDetailsList = bankDetailsRepository.findAll();
		boolean isAccountNoExists = false;
		int id = 0;
		for (BankDetails bankDetails2 : bankDetailsList) {
			try {
				String decreyptBankAccountNo = encryptUserDetail.decrypt(bankDetails2.getAccountNo());
				if (decreyptBankAccountNo.equals(mandateRequest.getAccountNumber())) {
					isAccountNoExists = true;
					id = bankDetails2.getUserId();
					break;
				}
			} catch (Exception ex) {
				ex.printStackTrace();
			}
		}
		if (user != null) {
			if (user.getBankDetails() == null) {
				bankDetails = new BankDetails();
				// if(bankDetailsTemp !=null){
				if (isAccountNoExists) {
					response = GoForWealthPRSConstants.ACCOUNT_NO_ALREADY_EXIST;
					//return response;
				}
			} else {
				bankDetails = user.getBankDetails();
				//if(bankDetailsTemp!=null && bankDetailsTemp.getUserId() != bankDetails.getUserId()){
				if(isAccountNoExists== true && id != bankDetails.getUserId()){
					response = GoForWealthPRSConstants.ACCOUNT_NO_ALREADY_EXIST;
					//return response;
				}
			}
			bankDetails.setBankName(mandateRequest.getBankName());
			bankDetails.setIfsc(mandateRequest.getIfscCode().toUpperCase());
			bankDetails.setAccountType(mandateRequest.getAccountType());
			bankDetails.setNomineeName(mandateRequest.getNomineeName());
			bankDetails.setNomineeRelation(mandateRequest.getNomineeRelation());
			// bankDetails.setAccountNo(mandateRequest.getAccountNumber());
			try {
				bankDetails.setAccountNo(encryptUserDetail.encrypt(mandateRequest.getAccountNumber()));
				Calendar date = Calendar.getInstance();
				date.setTime(new Date());
				bankDetails.setStartDate(df.parse(df.format(date.getTime())));
				date.add(Calendar.YEAR, 100);
				bankDetails.setEndDate(df.parse((df.format(date.getTime()))));
			} catch (Exception ex) {
				ex.printStackTrace();
			}
			bankDetails.setMicrCode(mandateRequest.getMicrCode());
			bankDetails.setBankAddress(mandateRequest.getBankAddress());
			bankDetails.setBankBranch(mandateRequest.getBankBranch());
			bankDetails.setUser(user);
			bankDetailsRepository.save(bankDetails);
			response = GoForWealthPRSConstants.SUCCESS;
		} else {
			response = GoForWealthPRSConstants.USER_NOT_EXIST;
		}
		return response;
	}

	@Override
	@Transactional
	public String uploadSignature(Integer userId, FileDetailsDTO fileDetails) throws IOException, GoForWealthPRSException {
		User user = userRepository.getOne(userId);
		StoreConf imageUrl = storeConfRepository.findByKeyword(GoForWealthPRSConstants.IMAGE_LOCATION);
		StoreConf doubleSlash = storeConfRepository.findByKeyword(GoForWealthPRSConstants.DOUBLE_BACKWARD_SLASH);
		StoreConf forwardSlash = storeConfRepository.findByKeyword(GoForWealthPRSConstants.FORWARD_SLASH);
		String response = null;
		if(user!=null){
			BASE64Decoder decoder = new BASE64Decoder();
			byte[] imgBytes = decoder.decodeBuffer(fileDetails.getSignatureString().split(",")[1]);
			String lowerCaseBase64Str= fileDetails.getSignatureString().toLowerCase();
			String fileType=null;
			if(lowerCaseBase64Str.indexOf("jpeg")!=-1){
				fileType="jpeg";
			}else if(lowerCaseBase64Str.indexOf("tiff")!=-1){
				fileType="tiff";
			}else if(lowerCaseBase64Str.indexOf("png")!=-1){
				fileType="png";
			}else if(lowerCaseBase64Str.indexOf("jpg")!=-1){
				fileType="jpg";
			}
			if(fileType==null)
				throw new GoForWealthPRSException(GoForWealthPRSErrorMessageEnum.INVALID_REQ_PAYLOAD_MESSAGE.getValue());
			//File imgOutFile = new File(GoForWealthPRSConstants.SIGNATURE_URL+user.getUserId()+"."+fileDetails.getFileName().split("\\.")[1]);
			File imgOutFile=null;
			String filePath="";
			if(fileDetails.getFileName().equals("signature")){
				StoreConf signatureLocation = storeConfRepository.findByKeyword(GoForWealthPRSConstants.SIGNATURE_LOCATION);
				String signaturePath = imageUrl.getKeywordValue()+signatureLocation.getKeywordValue();
				Path path = Paths.get(signaturePath);
				if (!Files.exists(path))
					Files.createDirectories(path);
				filePath =signaturePath+doubleSlash.getKeywordValue()+GoForWealthPRSConstants.SIGNATURE_FILE_PREFIX+user.getUserId()+"."+fileType;
				String  signaturePath4db =signatureLocation.getKeywordValue()+forwardSlash.getKeywordValue()+GoForWealthPRSConstants.SIGNATURE_FILE_PREFIX+user.getUserId()+"."+fileType;
				imgOutFile = new File(filePath);
				BankDetails bankDetails = user.getBankDetails();
				bankDetails.setSignatureImage(signaturePath4db);
				bankDetailsRepository.save(bankDetails);
				//-------------- kyc done manually -----------
				//user.getOnboardingStatus().setKycStatus(1);
				//user.getOnboardingStatus().setKycResponse("KYC REGISTERED SUCCESSFULLY");
				//user.getPanDetails().setVerified(GoForWealthPRSConstants.PAN_NUMBER_VERIFIED);
				//--------------------------------------------
				//System.out.println("-------------------"+response);
			}else if(fileDetails.getFileName().equals("userImage")){
				StoreConf userImageLocation = storeConfRepository.findByKeyword(GoForWealthPRSConstants.USERIMAGE_LOCATION);
				String userImagePath = imageUrl.getKeywordValue()+userImageLocation.getKeywordValue();
				String userImagePath4db = userImageLocation.getKeywordValue();
				Path path = Paths.get(userImagePath);
				if (!Files.exists(path))
					Files.createDirectories(path);
				filePath = userImagePath+doubleSlash.getKeywordValue()+GoForWealthPRSConstants.USER_IMAGE_FILE_PREFIX+user.getUserId()+"."+fileType;
				userImagePath4db = userImagePath4db+forwardSlash.getKeywordValue()+GoForWealthPRSConstants.USER_IMAGE_FILE_PREFIX+user.getUserId()+"."+fileType;
				imgOutFile = new File(filePath);
				KycDetails kycDetails = user.getKycDetails();
				if(kycDetails==null){
					KycDetails newKycDetails =new KycDetails();
					newKycDetails.setUser(user);
					newKycDetails.setPhotograph(userImagePath4db);
					kycDetailsRepository.save(newKycDetails);
				}else{
					kycDetails.setPhotograph(userImagePath4db);
					kycDetailsRepository.save(kycDetails);
				}
				//KycDetails newKycDetails =new KycDetails();
				//kycDetails.setUser(user);
				response = GoForWealthPRSConstants.SUCCESS;
			}else if(fileDetails.getFileName().equals("pancard")){
				StoreConf pancardLocation = storeConfRepository.findByKeyword(GoForWealthPRSConstants.PAN_LOCATION);
				String pancardPath = imageUrl.getKeywordValue()+pancardLocation.getKeywordValue();
				String pancardPath4db = pancardLocation.getKeywordValue();
				Path path = Paths.get(pancardPath);
				if (!Files.exists(path))
					Files.createDirectories(path);
				filePath = pancardPath+doubleSlash.getKeywordValue()+GoForWealthPRSConstants.PANCARD_FILE_PREFIX+user.getUserId()+"."+fileType;
				pancardPath4db=pancardPath4db+forwardSlash.getKeywordValue()+GoForWealthPRSConstants.PANCARD_FILE_PREFIX+user.getUserId()+"."+fileType;
				imgOutFile = new File(filePath);
				KycDetails kycDetails = user.getKycDetails();
				kycDetails.setPanCardImage(pancardPath4db);
				kycDetailsRepository.save(kycDetails);
				response = GoForWealthPRSConstants.SUCCESS;
			}else if(fileDetails.getFileName().equals("adharcard")){
				StoreConf adharcardLocation = storeConfRepository.findByKeyword(GoForWealthPRSConstants.ADHARCARD_LOCATION);
				String adharcardPath = imageUrl.getKeywordValue()+adharcardLocation.getKeywordValue();
				AddressProof addressProof = addressProofRepository.findAddressProofByUserId(user.getUserId());
				if(addressProof==null){
					addressProof = new AddressProof();
					addressProof.setUser(user);
					addressProof.setAddressProofName("adharcard");
				}
				Path path = Paths.get(adharcardPath);
				if (!Files.exists(path))
					Files.createDirectories(path);
				filePath = adharcardPath+doubleSlash.getKeywordValue()+GoForWealthPRSConstants.ADHARCARD_FRONT_FILE_PREFIX+user.getUserId()+"."+fileType;
				String adharcardFrontPath4db = adharcardLocation.getKeywordValue()+forwardSlash.getKeywordValue()+GoForWealthPRSConstants.ADHARCARD_FRONT_FILE_PREFIX+user.getUserId()+"."+fileType;
				imgOutFile = new File(filePath);
				FileOutputStream outputStream = new FileOutputStream(imgOutFile);
				outputStream.write(imgBytes);       	  
				outputStream.close();
				addressProof.setFrontImage(adharcardFrontPath4db);
				filePath = adharcardPath+doubleSlash.getKeywordValue()+GoForWealthPRSConstants.ADHARCARD_BACK_FILE_PREFIX+user.getUserId()+"."+fileType;
				String adharcardBackPath4db = adharcardLocation.getKeywordValue()+forwardSlash.getKeywordValue()+GoForWealthPRSConstants.ADHARCARD_BACK_FILE_PREFIX+user.getUserId()+"."+fileType;
				imgOutFile = new File(filePath);
				outputStream = new FileOutputStream(imgOutFile);
				imgBytes = decoder.decodeBuffer(fileDetails.getBackAdharCardString().split(",")[1]);
				outputStream.write(imgBytes);       	  
				outputStream.close();
				addressProof.setBackImage(adharcardBackPath4db);
				addressProofRepository.save(addressProof);
				response = GoForWealthPRSConstants.SUCCESS;
				return response;
			}
			if(imgOutFile==null){
				throw new GoForWealthPRSException(GoForWealthPRSErrorMessageEnum.INVALID_REQ_PAYLOAD_MESSAGE.getValue());
			}
			if(!imgOutFile.exists()){
				imgOutFile.createNewFile();
			}
			FileOutputStream outputStream = new FileOutputStream(imgOutFile);  
			outputStream.write(imgBytes);       	  
			outputStream.close();
			//response = GoForWealthPRSConstants.SUCCESS;
			if(fileDetails.getFileName().equals("signature")){
				response = registerAndMandateUser(user,fileDetails.isFatca());
			}
		}else{
			response = GoForWealthPRSConstants.USER_NOT_EXIST;
		}
		return response;
	}

	@Override
	public String showImage(Integer id, String type) {
		User user = userRepository.getOne(id);
		StoreConf imageLocation = storeConfRepository.findByKeyword(GoForWealthPRSConstants.IMAGE_LOCATION);
		StoreConf dummyImageLocation = storeConfRepository.findByKeyword(GoForWealthPRSConstants.DUMMYIMAGE_LOCATION);
		StoreConf dummyImagePath = storeConfRepository.findByKeyword(GoForWealthPRSConstants.DUMMY_IMAGE_PATH);
		String response = null;
		if(user!=null){
			if(type.equals("signature") && user.getBankDetails()!=null)
				response = user.getBankDetails().getSignatureImage();
			else if(type.equals("userImage")){
				if(user.getKycDetails()!=null){
					response = user.getKycDetails().getPhotograph();
				}
				if(response==null){
					//response = imageLocation.getKeywordValue()+dummyImageLocation.getKeywordValue()+GoForWealthPRSConstants.DUMMY_IMAGE_PATH;
					response = dummyImageLocation.getKeywordValue()+dummyImagePath.getKeywordValue();
				}
			}				
			else if(type.equals("pancard"))
				response = user.getKycDetails().getPanCardImage();
			else if(type.equals("adharcardfront"))
				response = user.getAddressProofs()!=null&&user.getAddressProofs().size()>0?user.getAddressProofs().iterator().next().getFrontImage():null;
			else if(type.equals("adharcardback"))
				response = user.getAddressProofs()!=null&&user.getAddressProofs().size()>0?user.getAddressProofs().iterator().next().getBackImage():null;
		}
		System.out.println("response === "+response);
		return response;
	}

	@Override
	public String showUserDocument(String name,String type) {
		User user = userRepository.findByMobileNumber(name);
		StoreConf imageLocation = storeConfRepository.findByKeyword(GoForWealthPRSConstants.IMAGE_LOCATION);
		StoreConf dummyImageLocation = storeConfRepository.findByKeyword(GoForWealthPRSConstants.DUMMYIMAGE_LOCATION);
		StoreConf dummyImagePath = storeConfRepository.findByKeyword(GoForWealthPRSConstants.DUMMY_IMAGE_PATH);
		String response = null;
		if(user!=null){
			if(type.equals("signature") && user.getBankDetails()!=null)
				response = user.getBankDetails().getSignatureImage();
			else if(type.equals("userImage")){
				if(user.getKycDetails()!=null){
					response = user.getKycDetails().getPhotograph();
				}
				if(response==null){
					//response = imageLocation.getKeywordValue()+dummyImageLocation.getKeywordValue()+GoForWealthPRSConstants.DUMMY_IMAGE_PATH;
					response = dummyImageLocation.getKeywordValue()+dummyImagePath.getKeywordValue();
				}
			}
			else if(type.equals("pancard"))
				response = user.getKycDetails().getPanCardImage();
			else if(type.equals("adharcardfront"))
				response = user.getAddressProofs()!=null&&user.getAddressProofs().size()>0?user.getAddressProofs().iterator().next().getFrontImage():null;
			else if(type.equals("adharcardback"))
				response = user.getAddressProofs()!=null&&user.getAddressProofs().size()>0?user.getAddressProofs().iterator().next().getBackImage():null;
		}
		return response;
	}

	@Override
	public String storeKycDetails(Integer userId, BasicDetailsDTO basicDetails) {
		User user = userRepository.getOne(userId);
		String response = null;
		KycDetails kycDetails = null;
		if(user!=null){
			kycDetails = user.getKycDetails();
			if(kycDetails == null){
				kycDetails = new KycDetails();
			}
			kycDetails.setMotherName(basicDetails.getMotherName());
			kycDetails.setFatherName(basicDetails.getFatherName());
			kycDetails.setMaritalStatus(basicDetails.getMaritalStatus());
			kycDetails.setUser(user);
			kycDetailsRepository.save(kycDetails);
			response = GoForWealthPRSConstants.SUCCESS;
		}else{
			response = GoForWealthPRSConstants.USER_NOT_EXIST;
		}
		return response;
	}

	@Override
	public String uploadPancard(Integer userId, FileDetailsDTO fileDetails) throws IOException {
		User user = userRepository.getOne(userId);
		StoreConf imageLocation = storeConfRepository.findByKeyword(GoForWealthPRSConstants.IMAGE_LOCATION);
		StoreConf pancardLocation = storeConfRepository.findByKeyword(GoForWealthPRSConstants.PAN_LOCATION);
		StoreConf doubleSlash = storeConfRepository.findByKeyword(GoForWealthPRSConstants.DOUBLE_BACKWARD_SLASH);
		String pancardPath = imageLocation.getKeywordValue()+pancardLocation.getKeywordValue();
		String response = null;
		if(user!=null){
			BASE64Decoder decoder = new BASE64Decoder();
	        byte[] imgBytes = decoder.decodeBuffer(fileDetails.getSignatureString().split(",")[1]);
	        String lowerCaseBase64Str= fileDetails.getSignatureString().toLowerCase();
	        String fileType="png";
	        if(lowerCaseBase64Str.indexOf("jpeg")!=-1){
	        	fileType="jpeg";
	        }else if(lowerCaseBase64Str.indexOf("tiff")!=-1){
	        	fileType="tiff";
	        }else if(lowerCaseBase64Str.indexOf("pdf")!=-1){
	        	fileType="pdf";
	        }
	       //File imgOutFile = new File(GoForWealthPRSConstants.SIGNATURE_URL+user.getUserId()+"."+fileDetails.getFileName().split("\\.")[1]);
	        Path path = Paths.get(pancardPath);
	        if (!Files.exists(path))
	        	Files.createDirectories(path);
	        File imgOutFile = new File(pancardPath+doubleSlash.getKeywordValue()+GoForWealthPRSConstants.PANCARD_FILE_PREFIX+user.getUserId()+"."+fileType);
	        if(!imgOutFile.exists()){
	        	imgOutFile.createNewFile();
	        }
	        FileOutputStream outputStream = new FileOutputStream(imgOutFile);  
	        outputStream.write(imgBytes);       	  
     	   	outputStream.close();
	        response = GoForWealthPRSConstants.SUCCESS;
		}else{
			response = GoForWealthPRSConstants.USER_NOT_EXIST;
		}
		return response;
	}

	@Override
	public String storeAddressProof(Integer userId, AddressProofDTO addressProofDTO) throws GoForWealthPRSException {
		User user = userRepository.getOne(userId);
		String response = null;
		AddressProof addressProof = null;
		if(user!=null){
			if(user.getAddressProofs()!=null&&user.getAddressProofs().size()>0){
				Iterator<AddressProof> itr = user.getAddressProofs().iterator();
				while(itr.hasNext()){
					AddressProof addressProofTemp = itr.next();
					if(addressProofTemp.getAddressProofName().equals(addressProofDTO.getAddressProofName()) && addressProofTemp.getAddressProofNo() != null){
						addressProof = addressProofTemp;
						if(addressProof.getAddressProofNo()==null){
							List<AddressProof> addressProofTempList = addressProofRepository.findAll();
							for (AddressProof addressProof2 : addressProofTempList) {
								if(addressProof2.getAddressProofNo() != null && !addressProof2.getAddressProofNo().equals("")) {
									try {
										String decreptedAadharNoRef = encryptUserDetail.decrypt(addressProof2.getAddressProofNo());
										if(addressProofDTO.getAddressProofNo().equals(decreptedAadharNoRef)) {
											addressProofTemp = addressProof2;
											break;
										}
									}catch(Exception ex) {
										ex.printStackTrace();
									}
								}
							}
							//addressProofTemp = addressProofRepository.findAddressProofByAddressProofNo(addressProofDTO.getAddressProofNo());
							if(addressProofTemp!=null){
								response = GoForWealthPRSConstants.ADDRESS_PROOF_ALREADY_EXIST;
								return response;
							}
						}
					}else{
						//addressProof = addressProofRepository.findAddressProofByAddressProofNo(addressProofDTO.getAddressProofNo());
						List<AddressProof> addressProofList = addressProofRepository.findAll();
						for (AddressProof addressProof2 : addressProofList) {
							if(addressProof2.getAddressProofNo() != null && !addressProof2.getAddressProofNo().equals("")) {
								try {
									String decreptedAadharNoRef = encryptUserDetail.decrypt(addressProof2.getAddressProofNo());
									if(addressProofDTO.getAddressProofNo().equals(decreptedAadharNoRef)) {
										addressProof = addressProof2;
										break;
									}
								}catch(Exception ex) {
									ex.printStackTrace();
								}
							}
						}
						if(addressProof!=null){
							response = GoForWealthPRSConstants.ADDRESS_PROOF_ALREADY_EXIST;
							return response;
						}
						addressProof = new AddressProof();
						addressProof.setAddressProofName(addressProofDTO.getAddressProofName());
						addressProof.setUser(user);
					}
				}
			}else{
				//addressProof = addressProofRepository.findAddressProofByAddressProofNo(addressProofDTO.getAddressProofNo());
				List<AddressProof> addressProofList = addressProofRepository.findAll();
				for (AddressProof addressProof2 : addressProofList) {
					if(!addressProof2.getAddressProofNo().equals("")) {
						try {
							String decreptedAadharNoRef = encryptUserDetail.decrypt(addressProof2.getAddressProofNo());
							if(addressProofDTO.getAddressProofNo().equals(decreptedAadharNoRef)) {
								addressProof = addressProof2;
								break;
							}
						}catch(Exception ex) {
							ex.printStackTrace();
						}
					}
				}
				if(addressProof!=null){
					response = GoForWealthPRSConstants.ADDRESS_PROOF_ALREADY_EXIST;
					return response;
				}
				addressProof = new AddressProof();
				addressProof.setAddressProofName(addressProofDTO.getAddressProofName());
				addressProof.setUser(user);
			}
			addressProof.setAddress(addressProofDTO.getAddressLine1()+""+addressProofDTO.getAddressLine2());
			addressProof.setField2(addressProofDTO.getAddressLine1());
			addressProof.setField3(addressProofDTO.getAddressLine2());
			addressProof.setCity(addressProofDTO.getCity());
			addressProof.setState(addressProofDTO.getState());
			addressProof.setPincode(addressProofDTO.getPincode());
			try {
				String encreptedAadharNoRef = encryptUserDetail.encrypt(addressProofDTO.getAddressProofNo());
				addressProof.setAddressProofNo(encreptedAadharNoRef);
			}catch(Exception ex) {
				ex.printStackTrace();
			}
			//addressProof.setAddressProofNo(addressProofDTO.getAddressProofNo());
			addressProofRepository.save(addressProof);
			response = GoForWealthPRSConstants.SUCCESS;
		}else{
			response = GoForWealthPRSConstants.USER_NOT_EXIST;
		}
		return response;
	}
	
	@Override
	public BankDetailsDTO verifyIfsc(Integer userId, String ifsc) {
		boolean isFound = false;
		BankDetailsDTO bankDetails = new BankDetailsDTO();
		IndianIfscCodes indianIfscCodes = indianIfscCodesRepository.findByIfscCode(ifsc);
		if(indianIfscCodes!=null){
			bankDetails.setBankName(indianIfscCodes.getBankName());
			bankDetails.setBankBranch(indianIfscCodes.getBranchName());
			String address = indianIfscCodes.getAddress() +" "+ indianIfscCodes.getState();
			bankDetails.setBankAddress(address);
			bankDetails.setMicrCode(indianIfscCodes.getMircCode());
			bankDetails.setMessage("success");
			List<IsipAllowedBankList> isipAllowedbankList = isipAllowedBankListRepository.findAll();
			if(!isipAllowedbankList.isEmpty()){
				for (IsipAllowedBankList isipAllowedBankList2 : isipAllowedbankList) {
					if(indianIfscCodes.getBankName().contains(isipAllowedBankList2.getBankName().toUpperCase())){
						isFound = true;
						break;
					}
				}
			}
			if(isFound){
				bankDetails.setIsipAllowedStatus("true");
			}else{
				bankDetails.setIsipAllowedStatus("false");
			}
		}
		else{
			bankDetails.setMessage("Ifsc Code not available!");
		}
		return bankDetails;
	}

	@Override
	public String changePassword() {
		String response = "";
		String oldPassword = null;
		String changePass = null;
		OtpGenerator otpGenerator = new OtpGenerator(5,true);
		String passKey = otpGenerator.generateOTP();
		ChangePassword changePassword = new ChangePassword();
		GetPasswordRequest getPasswordRequest = new GetPasswordRequest();
		getPasswordRequest.setPasskey(passKey);
		List<SystemProperties> systemPropertiesList=  systemPropertiesRepository.findAll();
		if(!systemPropertiesList.isEmpty()){
			for (SystemProperties systemProperties : systemPropertiesList) {
				if(systemProperties.getId().getPropertyKey().equals("UserId")){
					getPasswordRequest.setUserId(systemProperties.getId().getPropertyValue());
				}else if(systemProperties.getId().getPropertyKey().equals("MemberId")){
					getPasswordRequest.setMemberId(systemProperties.getId().getPropertyValue());
				}else if(systemProperties.getId().getPropertyKey().equals("Password")){
					getPasswordRequest.setPassword(systemProperties.getId().getPropertyValue());
				}
			}
			oldPassword = getPasswordRequest.getPassword();
			if(getPasswordRequest.getPassword().contains("!")){
				StoreConf changePasswordValueAt = storeConfRepository.findByKeyword(GoForWealthPRSConstants.CHANGE_PASSWORD_VALUE_AT);
				changePass = changePasswordValueAt.getKeywordValue();
			}else if(getPasswordRequest.getPassword().contains("@")){
				StoreConf changePasswordValueNot = storeConfRepository.findByKeyword(GoForWealthPRSConstants.CHANGE_PASSWORD_VALUE_NOT);
				changePass = changePasswordValueNot.getKeywordValue();
			}
			changePassword.setFlag("04");
			changePassword.setOldPassword(getPasswordRequest.getPassword());
			if(changePass == null){
				return "";
			}else{
				changePassword.setNewPassword(changePass);
				changePassword.setConfPassword(changePass);
			}
			changePassword.setGetPasswordRequest(getPasswordRequest);
			changePassword.setUserId(getPasswordRequest.getUserId());
		    response = mandateService.changePassword(changePassword);
		    System.out.println("Change PAssword Response ================  "+response);
		    if(response.contains("PASSWORD CHANGED SUCCESSFULLY")){
		    	SystemPropertiesId systemPropertiesId = null;
		    	SystemProperties systemProperties = systemPropertiesRepository.getPassword(oldPassword);
		    	if(systemProperties != null){
		    		systemPropertiesRepository.delete(systemProperties);
		    		systemPropertiesId = systemProperties.getId();
			    	systemPropertiesId.setPropertyValue(changePass);
		    		systemProperties.setId(systemPropertiesId);
			    	systemPropertiesRepository.save(systemProperties);
		    	}
		    }
		}
		System.out.println("Out Change Password");
		return response;
	}

	public String registerAndMandateUser(User user, boolean isFatca) {
		String response = null;
		String code=null;
		String message = null;
		String[] res = null;	
		UccRequest uccRequest = getUccRequest(user);
		response = mandateService.uccRegistration(uccRequest);
		res = response.split("\\|");
		if(res.length>1){
			code = res[0];
			message = res[1];
			if(user.getOnboardingStatus()==null){
				OnboardingStatus onboardingStatus = new OnboardingStatus();
				onboardingStatus.setUccStatus(Integer.parseInt(code));
				onboardingStatus.setUccResponse(message);
				onboardingStatus.setUser(user);
				user.setOnboardingStatus(onboardingStatus);
			}else{
				user.getOnboardingStatus().setUccStatus(100);
				user.getOnboardingStatus().setUccResponse(message);
			}
			if(code.equals("100")){
				response = registerMandate(user, uccRequest.getGetPasswordRequest(), isFatca);
				user.getOnboardingStatus().setClientCode(uccRequest.getClientCode());
				if(response.equals("success")){
					user.getOnboardingStatus().setOverallStatus(1);
					user.getOnboardingStatus().setLastUpdated(new Date());
					user.setUpdatedDateTime(new Date());
				}else{
					user.getOnboardingStatus().setOverallStatus(0);
					user.getOnboardingStatus().setLastUpdated(new Date());
					user.setUpdatedDateTime(new Date());
				}
			}else{
				response = message;//"Problem in UCC registration. Please contact admin.";
			}
		}else{
			if(user.getOnboardingStatus()==null){
				OnboardingStatus onboardingStatus = new OnboardingStatus();					
				onboardingStatus.setUccResponse(res[0]);
				onboardingStatus.setUser(user);
				user.setOnboardingStatus(onboardingStatus);
			}else{
				user.getOnboardingStatus().setUccResponse(res[0]);
			}
			response = res[0];//"Problem in UCC registration. Please contact admin.";
		}
		return response;
	}

	private String registerMandate(User user, GetPasswordRequest getPasswordRequest, boolean isFatca) {
		String response = null;
		String code=null;
		String message = null;
		String[] res = null;
		if(user.getOnboardingStatus().getIsipMandateStatus() != 100 || user.getOnboardingStatus().getMandateStatus() != 100){
			try{
				MandateRequest iSIPmandateRequest = getMandateRequest(user, getPasswordRequest,"I");
				response = mandateService.registerMandate(iSIPmandateRequest);
				res = response.split("\\|");
				if(res.length>1){
					code = res[0];
					message = res[1];
					user.getOnboardingStatus().setIsipMandateStatus(Integer.parseInt(code));
					user.getOnboardingStatus().setIsipMandateResponse(message);
					if(code.equals("100")){
						//update existing record
						List<UserMandateHistory> userMandateHistoryList = userMandateHistoryRepository.findByUserId(user.getUserId());
						if(!userMandateHistoryList.isEmpty()){
							for (UserMandateHistory userMandateHistory2 : userMandateHistoryList) {
								if(userMandateHistory2.getMandateType().equals("ISIP")){
									if(userMandateHistory2.getMandateId().equals(user.getOnboardingStatus().getIsipMandateNumber())){
										userMandateHistory2.setStatus(user.getOnboardingStatus().getBillerStatus());
										userMandateHistory2.setUpdatedDate(new Date());
										userMandateHistory2.setOrderPlaceFlag("0");
										userMandateHistoryRepository.save(userMandateHistory2);
										System.out.println("User Mandate History Table Updated Successfully For ISIP Data.");
									}
								}
							}
							//And Insert new Record.
							UserMandateHistory userMandateHistory= new UserMandateHistory();
							userMandateHistory.setBankIfsc(user.getBankDetails().getIfsc());
							userMandateHistory.setCreatedDate(new Date());
							userMandateHistory.setMandateCreatedDate(new Date());
							userMandateHistory.setMandateId(res[2]);
							userMandateHistory.setMandateType("ISIP");
							userMandateHistory.setStatus("Pending");
							userMandateHistory.setUpdatedDate(new Date());
							userMandateHistory.setUserBankAccount(user.getBankDetails().getAccountNo());
							userMandateHistory.setUserId(user.getUserId());
							userMandateHistory.setOrderPlaceFlag("1");
							userMandateHistoryRepository.save(userMandateHistory);
							System.out.println("User Mandate History Table Inserted Successfully For ISIP Data.");
						}else{
							UserMandateHistory userMandateHistory= new UserMandateHistory();
							userMandateHistory.setBankIfsc(user.getBankDetails().getIfsc());
							userMandateHistory.setCreatedDate(new Date());
							userMandateHistory.setMandateCreatedDate(new Date());
							userMandateHistory.setMandateId(res[2]);
							userMandateHistory.setMandateType("ISIP");
							userMandateHistory.setStatus("Pending");
							userMandateHistory.setUpdatedDate(new Date());
							userMandateHistory.setUserBankAccount(user.getBankDetails().getAccountNo());
							userMandateHistory.setUserId(user.getUserId());
							userMandateHistory.setOrderPlaceFlag("1");
							userMandateHistoryRepository.save(userMandateHistory);
							System.out.println("User Mandate History Table Only Inserted Successfully For ISIP Data.");
						}
						user.getOnboardingStatus().setIsipMandateNumber(res[2]);
					}else{
						user.getOnboardingStatus().setBillerStatus("Failed");
					}
				}
			}catch(Exception e){
			}
			//code started for XSIP Mandate Registration.
			MandateRequest xSIPmandateRequest = getMandateRequest(user, getPasswordRequest,"X");
			response = mandateService.registerMandate(xSIPmandateRequest);
			res = response.split("\\|");
			if(res.length>1){
				code = res[0];
				message = res[1];
				user.getOnboardingStatus().setMandateStatus(Integer.parseInt(code));
				user.getOnboardingStatus().setMandateResponse(message);
				if(code.equals("100")){
					//update existing record
					List<UserMandateHistory> userMandateHistoryList = userMandateHistoryRepository.findByUserId(user.getUserId());
					if(!userMandateHistoryList.isEmpty()){
						for (UserMandateHistory userMandateHistory2 : userMandateHistoryList) {
							if(userMandateHistory2.getMandateType().equals("XSIP")){
								if(userMandateHistory2.getMandateId().equals(user.getOnboardingStatus().getMandateNumber())){
									userMandateHistory2.setStatus(user.getOnboardingStatus().getEnachStatus());
									userMandateHistory2.setUpdatedDate(new Date());
									userMandateHistoryRepository.save(userMandateHistory2);
									userMandateHistory2.setOrderPlaceFlag("0");
									System.out.println("User Mandate History Table Updated Successfully For XSIP Data.");
								}
							}
						}
						//And Insert new Record.
						UserMandateHistory userMandateHistory= new UserMandateHistory();
						userMandateHistory.setBankIfsc(user.getBankDetails().getIfsc());
						userMandateHistory.setCreatedDate(new Date());
						userMandateHistory.setMandateCreatedDate(new Date());
						userMandateHistory.setMandateId(res[2]);
						userMandateHistory.setMandateType("XSIP");
						userMandateHistory.setStatus("Pending");
						userMandateHistory.setUpdatedDate(new Date());
						userMandateHistory.setUserBankAccount(user.getBankDetails().getAccountNo());
						userMandateHistory.setUserId(user.getUserId());
						userMandateHistory.setOrderPlaceFlag("1");
						userMandateHistoryRepository.save(userMandateHistory);
						System.out.println("User Mandate History Table Inserted Successfully For XSIP Data.");
					}else{
						UserMandateHistory userMandateHistory= new UserMandateHistory();
						userMandateHistory.setBankIfsc(user.getBankDetails().getIfsc());
						userMandateHistory.setCreatedDate(new Date());
						userMandateHistory.setMandateCreatedDate(new Date());
						userMandateHistory.setMandateId(res[2]);
						userMandateHistory.setMandateType("XSIP");
						userMandateHistory.setStatus("Pending");
						userMandateHistory.setUpdatedDate(new Date());
						userMandateHistory.setUserBankAccount(user.getBankDetails().getAccountNo());
						userMandateHistory.setUserId(user.getUserId());
						userMandateHistory.setOrderPlaceFlag("1");
						userMandateHistoryRepository.save(userMandateHistory);
						System.out.println("User Mandate History Table Only Inserted Successfully For XSIP Data.");
					}
					user.getOnboardingStatus().setMandateNumber(res[2]);
					if(isFatca){
						response = registerFATCA(user, getPasswordRequest);
					}else{
						try {
							response = uploadAOF(user, getPasswordRequest);
						} catch (GoForWealthPRSException | IOException e) {
							response = "Internal server error!";
							e.printStackTrace();
						}
					}
				}else{
					user.getOnboardingStatus().setEnachStatus("Failed");
					//"Problem in Mandate registration.Skip Mandate Process Continue For Fatca And Aof Completion";
					if(isFatca){
						response = registerFATCA(user, getPasswordRequest);
					}else{
						try {
							response = uploadAOF(user, getPasswordRequest);
						} catch (GoForWealthPRSException | IOException e) {
							response = "Internal server error!";
							e.printStackTrace();
						}
					}
					response = message;
				}
			}else{
				user.getOnboardingStatus().setMandateResponse(res[0]);
				response = res[0];//"Problem in Mandate registration. Please contact admin.";
				response = registerFATCA(user, getPasswordRequest);
			}
		}else{
			if(isFatca){
				response = registerFATCA(user, getPasswordRequest);
			}else{
				try {
					response = uploadAOF(user, getPasswordRequest);
				} catch (GoForWealthPRSException | IOException e) {
					response = "Internal server error!";
					e.printStackTrace();
				}
			}
		}
		return response;
	}

	private String registerFATCA(User user, GetPasswordRequest getPasswordRequest) {
		String response = null;
		String code=null;
		String message = null;
		String[] res = null;	
		FatcaRequest fatcaRequest = getFATCARequest(user, getPasswordRequest);
		response = mandateService.doFatca(fatcaRequest);
		res = response.split("\\|");
		if(res.length>1){
			code = res[0];
			message = res[1];
			user.getOnboardingStatus().setFatcaStatus(Integer.parseInt(code));
			user.getOnboardingStatus().setFatcaResponse(message);
			if(code.equals("100")){
				//response = "success";//call aof reg service
				try {
					response = uploadAOF(user,getPasswordRequest);
					if(response.equals("File Uploaded Successfully.")){
						response = "success";
					}
					//{"Filler":null,"ResponseString":"File Uploaded Successfully.","Status":"100"}
					System.out.println(response);
				} catch (GoForWealthPRSException | IOException e) {
					response = "Internal server error!";
					e.printStackTrace();
				}
			}else{
				//"Problem in FATCA registration. Skip Fatca Continue with Aof";
				try {
					response = uploadAOF(user,getPasswordRequest);
					if(response.equals("File Uploaded Successfully.")){
						response = "success";
					}
					System.out.println(response);
				} catch (GoForWealthPRSException | IOException e) {
					response = "Internal server error!";
					e.printStackTrace();
				}
				response = message;
			}
		}else{
			user.getOnboardingStatus().setFatcaResponse(res[0]);
			response = res[0];//"Problem in FATCA registration. Please contact admin.";
		}
		return response;
	}

	private FatcaRequest getFATCARequest(User user, GetPasswordRequest getPasswordRequest) {
		DateFormat df = new SimpleDateFormat("MM/dd/yyyy");
		FatcaRequest fatcaRequest = new FatcaRequest();
		fatcaRequest.setUserId(getPasswordRequest.getUserId());
		fatcaRequest.setFlag("01");
		fatcaRequest.setGetPasswordRequest(getPasswordRequest);
		/** **/
	    if(user.getPanDetails() != null){
	    	String encryptededPanNoRef = user.getPanDetails().getPanNo();
	    	String decryptedPanNoRef = "";
	    	try {
	    		decryptedPanNoRef = encryptUserDetail.decrypt(encryptededPanNoRef);
	    	} catch(Exception ex) {
	    		ex.printStackTrace();
	    	}
	    	if(user.getPanDetails().getOccupation().equals("01")){
	    		fatcaRequest.setSrceWealt("02");
	    	}else if(user.getPanDetails().getOccupation().equals("02")){
	    		fatcaRequest.setSrceWealt("02");
	    	}else{
	    		fatcaRequest.setSrceWealt("08");
	    	}
	    	fatcaRequest.setPanRp(decryptedPanNoRef);//if pan no. is provided no need to fill blow 4 fields other wise mendatory
	    	fatcaRequest.setTpin1(decryptedPanNoRef);
	    	fatcaRequest.setId1Type("C");//<option value="C">PAN Card</option>
	    }
		fatcaRequest.setPekrn("");
		fatcaRequest.setInvName(user.getPanDetails().getFullName());
		fatcaRequest.setDob(df.format(user.getPanDetails().getDateOfBirth()));
		fatcaRequest.setFrName("");
		fatcaRequest.setSpName("");
		fatcaRequest.setTaxStatus("01");//01==Indivisuals
		fatcaRequest.setDataSrc("P");
		if(user.getAddressProofs()!=null&&user.getAddressProofs().size()>0){
			Iterator<AddressProof> itr = user.getAddressProofs().iterator();
			while(itr.hasNext()){
				AddressProof addressProof = itr.next();
				fatcaRequest.setAddrType(addressProof.getAddressType());
				fatcaRequest.setIncSlab(addressProof.getIncomeSlab());
				fatcaRequest.setPepFlag(addressProof.getPep());
			}
		}
		fatcaRequest.setPoBirInc("IN");//Place Of Birth IN==INDIA
		fatcaRequest.setCoBirInc("IN");//Country of birth
		fatcaRequest.setTaxRes1("IN");
		fatcaRequest.setTaxRes2("");
		fatcaRequest.setTpin2("");
		fatcaRequest.setId2Type("");
		fatcaRequest.setTaxRes3("");
		fatcaRequest.setTpin3("");
		fatcaRequest.setId3Type("");
		fatcaRequest.setTaxRes4("");
		fatcaRequest.setTpin4("");
		fatcaRequest.setId4Type("");
		fatcaRequest.setCorpServs("");//M for Non-Individuals
		fatcaRequest.setNetWorth("");//M for Non-Individuals
		fatcaRequest.setNwDate("");//M for Non-Individuals
		fatcaRequest.setOccCode(user.getPanDetails().getOccupation());//<option value="41">Private Sector Service</option>
		if(user.getPanDetails().getOccupation().equals("01")){
			fatcaRequest.setOccType("B");
		}else if(user.getPanDetails().getOccupation().equals("02") || user.getPanDetails().getOccupation().equals("03") || user.getPanDetails().getOccupation().equals("04")){
			fatcaRequest.setOccType("S");
		}else{
			fatcaRequest.setOccType("O");//Others
		}
		fatcaRequest.setExempCode("");//M for Non-Individuals
		fatcaRequest.setFfiDrnfe("");//M for Non-Individuals
		fatcaRequest.setGiinNo("");//M for Non-Individuals
		fatcaRequest.setSprEntity("");
		fatcaRequest.setGiinNa("");
		fatcaRequest.setGiinExemc("");
		fatcaRequest.setNffeCatg("");
		fatcaRequest.setActNfeSc("");
		fatcaRequest.setNatureBus("");
		fatcaRequest.setRelListed("");
		fatcaRequest.setExchName("B");
		fatcaRequest.setUboAppl("N");
		fatcaRequest.setUboCount("");
		fatcaRequest.setUboName("");
		fatcaRequest.setUboPan("");
		fatcaRequest.setUboNation("");
		fatcaRequest.setUboAdd1("");
		fatcaRequest.setUboAdd2("");
		fatcaRequest.setUboAdd3("");
		fatcaRequest.setUboCity("");
		fatcaRequest.setUboPin("");
		fatcaRequest.setUboState("");
		fatcaRequest.setUboCntry("");
		fatcaRequest.setUboAddTy("");
		fatcaRequest.setUboCtr("");
		fatcaRequest.setUboTin("");
		fatcaRequest.setUboIdTy("");
		fatcaRequest.setUboCob("");
		fatcaRequest.setUboDob("");
		fatcaRequest.setUboGender("");
		fatcaRequest.setUboFrNam("");
		fatcaRequest.setUboOcc("");
		fatcaRequest.setUboOccTy("");
		fatcaRequest.setUboTel("");
		fatcaRequest.setUboMobile("");
		fatcaRequest.setUboCode("");
		fatcaRequest.setUboHolPc("");
		fatcaRequest.setSdfFlag("");
		fatcaRequest.setUboDf("N");
		fatcaRequest.setAadhaarRp("");
		fatcaRequest.setNewChange("N");
		fatcaRequest.setLogName("");
		//fatcaRequest.setLogName("18-191-39-16#23-Nov-15;16:4");
		fatcaRequest.setFiller1("");
		fatcaRequest.setFiller2("");
		return fatcaRequest;
	}

	private MandateRequest getMandateRequest(User user, GetPasswordRequest getPasswordRequest,String mandateType) {
		DateFormat df = new SimpleDateFormat("dd/MM/yyyy");
		MandateRequest mandateRequest = new MandateRequest();
		mandateRequest.setGetPasswordRequest(getPasswordRequest);
		mandateRequest.setClientCode("G4W"+user.getUserId());
		StoreConf amount = storeConfRepository.findByKeyword(GoForWealthPRSConstants.AMOUNT_LIMIT_WITHDRAWAL_FROM_BANK);
		mandateRequest.setAmount(amount.getKeywordValue());
		mandateRequest.setMandateType(mandateType);
		String encryptededAccountNoRef = user.getBankDetails().getAccountNo();
		String decryptedAccountNoRef = "";
		try {
			decryptedAccountNoRef = encryptUserDetail.decrypt(encryptededAccountNoRef);
		} catch(Exception ex) {
			ex.printStackTrace();
		}
		mandateRequest.setAccountNumber(decryptedAccountNoRef);
		mandateRequest.setAccountType(user.getBankDetails().getAccountType());
		mandateRequest.setIfscCode(user.getBankDetails().getIfsc());
		mandateRequest.setMicrCode("");
		mandateRequest.setStartDate(df.format(user.getBankDetails().getStartDate()));
		mandateRequest.setEndDate(df.format(user.getBankDetails().getEndDate()));
		mandateRequest.setUserId(getPasswordRequest.getUserId());
		mandateRequest.setFlag("06");
		return mandateRequest;
	}

	private UccRequest getUccRequest(User user) {
		DateFormat df = new SimpleDateFormat("dd/MM/yyyy");
		OtpGenerator otpGenerator = new OtpGenerator(5,true);
		String passKey = otpGenerator.generateOTP();
		UccRequest uccRequest = new UccRequest();
		GetPasswordRequest getPasswordRequest = new GetPasswordRequest();
		getPasswordRequest.setPasskey(passKey);
		List<SystemProperties> systemPropertiesList=  systemPropertiesRepository.findAll();
		for (SystemProperties systemProperties : systemPropertiesList) {
			if(systemProperties.getId().getPropertyKey().equals("UserId")){
				getPasswordRequest.setUserId(systemProperties.getId().getPropertyValue());
			}else if(systemProperties.getId().getPropertyKey().equals("MemberId")){
				getPasswordRequest.setMemberId(systemProperties.getId().getPropertyValue());
			}else if(systemProperties.getId().getPropertyKey().equals("Password")){
				getPasswordRequest.setPassword(systemProperties.getId().getPropertyValue());
			}
		}
		uccRequest.setGetPasswordRequest(getPasswordRequest);
		uccRequest.setUserId(GoForWealthPRSConstants.USER_ID);
		uccRequest.setFlag("02");
		uccRequest.setClientCode("G4W"+user.getUserId());
		uccRequest.setClientHolding("SI");//SI===single/ JO==joint/ AS
		uccRequest.setClientTaxStatus("01");
		uccRequest.setClientOccupationCode(user.getPanDetails().getOccupation());
		uccRequest.setClientAppName1(user.getPanDetails().getFullName());
		uccRequest.setClientAppName2("");
		uccRequest.setClientAppName3("");
		uccRequest.setClientDob(df.format(user.getPanDetails().getDateOfBirth()));
		uccRequest.setClientGender(user.getPanDetails().getGender());
		uccRequest.setClientFather(user.getKycDetails().getFatherName());
		/** **/
		String encryptededPanNoRef = user.getPanDetails().getPanNo();
		String decryptedPanNoRef = "";
		try {
			decryptedPanNoRef = encryptUserDetail.decrypt(encryptededPanNoRef);
		} catch(Exception ex) {
			ex.printStackTrace();
		}
		uccRequest.setClientPan(decryptedPanNoRef);
		if(user.getBankDetails().getNomineeName()!=null){
			uccRequest.setClientNominee(user.getBankDetails().getNomineeName());
		}else{
			uccRequest.setClientNominee("");
		}
		if(user.getBankDetails().getNomineeRelation()!= null){
			uccRequest.setClientNomineeRelation(user.getBankDetails().getNomineeRelation());
		}else{
			uccRequest.setClientNomineeRelation("");
		}
		uccRequest.setClientGuardianPan("");
		uccRequest.setClientType("P");//P- Physical /D-Demat
		uccRequest.setClientDefaultDp("");
		uccRequest.setClientCDSLDPId("");
		uccRequest.setClientCDSLCLTId("");
		uccRequest.setClientNSDLDPId("");
		uccRequest.setClientNSDLCLTId("");
		uccRequest.setClientAccType1(user.getBankDetails().getAccountType());
		/** **/
		String encryptededAccountNoRef = user.getBankDetails().getAccountNo();
		String decryptedAccountNoRef = "";
		try {
			decryptedAccountNoRef = encryptUserDetail.decrypt(encryptededAccountNoRef);
		} catch(Exception ex) {
			ex.printStackTrace();
		}
		uccRequest.setClientAccNo1(decryptedAccountNoRef);
		uccRequest.setClientMICRNo1("");
		uccRequest.setClientIFSCCode1(user.getBankDetails().getIfsc());
		uccRequest.setDefaultBankFlag1("Y");
		uccRequest.setClientAccType2("");
		uccRequest.setClientAccNo2("");
		uccRequest.setClientMICRNo2("");
		uccRequest.setClientIFSCCode2("");
		uccRequest.setDefaultBankFlag2("");
		uccRequest.setClientAccType3("");
		uccRequest.setClientAccNo3("");
		uccRequest.setClientMICRNo3("");
		uccRequest.setClientIFSCCode3("");
		uccRequest.setDefaultBankFlag3("");
		uccRequest.setClientAccType4("");
		uccRequest.setClientAccNo4("");
		uccRequest.setClientMICRNo4("");
		uccRequest.setClientIFSCCode4("");
		uccRequest.setDefaultBankFlag4("");
		uccRequest.setClientAccType5("");
		uccRequest.setClientAccNo5("");
		uccRequest.setClientMICRNo5("");
		uccRequest.setClientIFSCCode5("");
		uccRequest.setDefaultBankFlag5("");
		uccRequest.setClientChequeName5("");
		/*
		uccRequest.setClientAccNo("31041869121");
		uccRequest.setClientMICRNo("804042507");
		uccRequest.setClientIFSCCode("SBIN0012603");
		uccRequest.setClientChequeName("");
		uccRequest.setClientAdd1("Noida sector 63");
		uccRequest.setClientAdd2("G block");
		uccRequest.setClientAdd3("");
		*/
		if(user.getAddressProofs() != null && user.getAddressProofs().size()>0){
			Iterator<AddressProof> itr = user.getAddressProofs().iterator();
			while(itr.hasNext()){
				AddressProof addressProof = itr.next();
				//uccRequest.setClientAdd1(addressProof.getAddress());
				uccRequest.setClientAdd1(addressProof.getField2());
				if(addressProof.getField3() != null && !addressProof.getField3().equals("")){
					uccRequest.setClientAdd2(addressProof.getField3());
				}else{
					uccRequest.setClientAdd2("--");
				}		
				uccRequest.setClientCity(addressProof.getCity());
				AddressState addressState =	addressStateRepository.getStateCodeByStateName(addressProof.getState());
				if(addressState != null){
					uccRequest.setClientState(addressState.getAddressStateCode());
				}else{
					uccRequest.setClientState("OH");
				}
				uccRequest.setClientPincode(addressProof.getPincode());
				uccRequest.setClientCountry("India");
			}
		}
		uccRequest.setClientEmail(user.getEmail());
		uccRequest.setClientCommMode("P");//P Physical/E Electronic/M MOBILE
		uccRequest.setClientDivPayMode("02");
		uccRequest.setClientResiPhone("");
		uccRequest.setClientResiFax("");
		uccRequest.setClientOfficePhone("");
		uccRequest.setClientOfficeFax("");
		uccRequest.setClientPan2("");
		uccRequest.setClientPan3("");
		uccRequest.setMapinNo("");
		uccRequest.setCM_FORADD1("");
		uccRequest.setCM_FORADD2("");
		uccRequest.setCM_FORADD3("");
		uccRequest.setCM_FORCITY("");
		uccRequest.setCM_FORPINCODE("");
		uccRequest.setCM_FORSTATE("");
		uccRequest.setCM_FORCOUNTRY("");
		uccRequest.setCM_FORRESIPHONE("");
		uccRequest.setCM_FORRESIFAX("");
		uccRequest.setCM_FOROFFPHONE("");
		uccRequest.setCM_FOROFFFAX("");
		uccRequest.setCM_MOBILE(user.getMobileNumber());
		return uccRequest;
	}

	private String uploadAOF(User user, GetPasswordRequest getPasswordRequest) throws GoForWealthPRSException, IOException  {
		String tiffPath = generatePdf(user, getPasswordRequest);
		com.moptra.go4wealth.prs.imageupload.GetPasswordRequest getPasswordRequest2 = new com.moptra.go4wealth.prs.imageupload.GetPasswordRequest();
		getPasswordRequest2.setMemberId(getPasswordRequest.getMemberId());
		getPasswordRequest2.setPassword(getPasswordRequest.getPassword());
		getPasswordRequest2.setUserId(getPasswordRequest.getUserId());
		String password = imageUploadService.getPassword(getPasswordRequest2);
		if(password!=null){
			String tiffFileName = getPasswordRequest.getMemberId()+getTiffName("G4W"+user.getUserId());
			byte[] bytes = GoForWealthPRSUtil.fileToByteArray(tiffPath);
		    String imageBase64= Base64.encodeBytes(bytes);
			FileUploadRequest fileUploadRequest= new FileUploadRequest();
			fileUploadRequest.setClientCode("G4W"+user.getUserId());
			fileUploadRequest.setDocType("NRM");
			fileUploadRequest.setEncrypedPassword(password);
			fileUploadRequest.setFileName(tiffFileName);
			fileUploadRequest.setFiller1("");
			fileUploadRequest.setFiller2("");
			fileUploadRequest.setFlag("UCC");
			fileUploadRequest.setMemberCode(getPasswordRequest.getMemberId());
			fileUploadRequest.setUserId(getPasswordRequest.getUserId());
			fileUploadRequest.setpFileBytes(imageBase64);			
			String response = imageUploadService.uploadFile(fileUploadRequest);
			System.out.println("AOF Upload Response: "+response);
			String[] res = response.split("\\|");
			if(res.length>1){
				String code = res[0];
				String message = res[1];
				user.getOnboardingStatus().setAofStatus(Integer.parseInt(code));
				user.getOnboardingStatus().setAofResponse(message);
				if(code.equals("100")){					
					response = "success";
				}else if(message.contains("PAN NO ALREADY APPROVED")){
					response = "success";
					user.getOnboardingStatus().setAofStatus(100);
				}else if(message.contains("IMAGE IS ALREADY AVAILABLE AND IMAGE STATUS IS PENDING")){
					response = "success";
					user.getOnboardingStatus().setAofStatus(100);
				}else{
					response = message;//"Problem in AOF registration. Please contact admin.";
				}
			}else{
				user.getOnboardingStatus().setAofResponse(res[0]);
				response = res[0];//"Problem in AOF registration. Please contact admin.";
			}
			return response;
		}else{
			return "Problem in AOF registration Password. Please contact admin.";
		}
	}

	public String downloadMandateFileImage(User user,String mandateFormType){
		String imageDataString = null;
		GetPasswordRequest getPasswordRequest = new GetPasswordRequest();
		OtpGenerator otpGenerator = new OtpGenerator(5,true);
		String passKey = otpGenerator.generateOTP();
		getPasswordRequest.setPasskey(passKey);
		List<SystemProperties> systemPropertiesList=  systemPropertiesRepository.findAll();
		for (SystemProperties systemProperties : systemPropertiesList) {
			if(systemProperties.getId().getPropertyKey().equals("UserId")){
				getPasswordRequest.setUserId(systemProperties.getId().getPropertyValue());
			}else if(systemProperties.getId().getPropertyKey().equals("MemberId")){
				getPasswordRequest.setMemberId(systemProperties.getId().getPropertyValue());
			}else if(systemProperties.getId().getPropertyKey().equals("Password")){
				getPasswordRequest.setPassword(systemProperties.getId().getPropertyValue());
			}
		}
		String tiffPath = generateMandatePdf(user, getPasswordRequest,mandateFormType);
		File file = new File(tiffPath);
		try {
			String fileType = Files.probeContentType(file.toPath());
            FileInputStream imageInFile = new FileInputStream(file);
            byte imageData[] = new byte[(int) file.length()];
            imageInFile.read(imageData);
            imageDataString = org.apache.commons.codec.binary.Base64.encodeBase64String(imageData);
            imageDataString="data:"+fileType+";base64,"+imageDataString;
            imageInFile.close();
        } catch (FileNotFoundException e) {
        	logger.error("Image not found" + e);
        } catch (IOException ioe) {
        	logger.error("Exception while reading the Image " + ioe);
        }
		return imageDataString;
	}

	private String generateMandatePdf(User user, GetPasswordRequest getPasswordRequest,String mandateFormType){
		String tiffPath=null;
		StoreConf imageLocation = storeConfRepository.findByKeyword(GoForWealthPRSConstants.IMAGE_LOCATION);
		StoreConf mandatePdfLocation = storeConfRepository.findByKeyword(GoForWealthPRSConstants.MANDATE_PDF_LOCATION);
		StoreConf doubleSlash = storeConfRepository.findByKeyword(GoForWealthPRSConstants.DOUBLE_BACKWARD_SLASH);
		String pdfPath = imageLocation.getKeywordValue()+mandatePdfLocation.getKeywordValue();
		StoreConf amountInNumber = storeConfRepository.findByKeyword(GoForWealthPRSConstants.AMOUNT_LIMIT_WITHDRAWAL_FROM_BANK);
		StoreConf amountInWorld = storeConfRepository.findByKeyword(GoForWealthPRSConstants.AMOUNT_LIMIT_WITHDRAWAL_FROM_BANK_IN_WORLD);
		try {
			MandatePdfFormRequestDTO mpf = new MandatePdfFormRequestDTO();
			mpf.setAccType(user.getBankDetails().getAccountType());
			mpf.setAmountInNumber(amountInNumber.getKeywordValue());
			mpf.setAmountInWorld(amountInWorld.getKeywordValue());
			String decryptBankAccountNo = "";
			try {
				decryptBankAccountNo = encryptUserDetail.decrypt(user.getBankDetails().getAccountNo());
			} catch(Exception ex) { 
				ex.printStackTrace();
			}
			mpf.setBankAccNo(decryptBankAccountNo);
			//mpf.setBankAccNo("09211050004545");
			mpf.setBankName(user.getBankDetails().getBankName());
			mpf.setClientCode(user.getOnboardingStatus().getClientCode());
			mpf.setAccType(user.getBankDetails().getAccountType());
			SimpleDateFormat sdf1 = new SimpleDateFormat("ddMMyyyy");
			mpf.setDate(sdf1.format(new Date()));
			mpf.setEmail(user.getEmail());
			mpf.setFormType(mandateFormType);//create , modify , cancel
			mpf.setFromDate(sdf1.format(new Date()));
			mpf.setIfscCode(user.getBankDetails().getIfsc());
			mpf.setMandateNumber(user.getOnboardingStatus().getMandateNumber());
			mpf.setMicrNumber("");
			mpf.setMobileNo(user.getMobileNumber());
			mpf.setSponserBankCode("CITI000PIGW");
			mpf.setUtilityCode("CITI00002000000037");
			mpf.setTickImageValue(imageLocation.getKeywordValue()+doubleSlash.getKeywordValue()+"tick.png");
			String path = pdfPath+doubleSlash.getKeywordValue()+"pdf_"+user.getUserId()+".pdf";
			GoForWealthPRSUtil.generateMandateFormPdf(mpf,path);
			tiffPath = convertMandatePdfToTiff(path, user, getPasswordRequest);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return tiffPath;
	}

	private String generatePdf(User user, GetPasswordRequest getPasswordRequest) {
		String tiffPath=null;
		StoreConf imageLocation = storeConfRepository.findByKeyword(GoForWealthPRSConstants.IMAGE_LOCATION);
		StoreConf pdfLocation = storeConfRepository.findByKeyword(GoForWealthPRSConstants.PDF_LOCATION);
		StoreConf doubleSlash = storeConfRepository.findByKeyword(GoForWealthPRSConstants.DOUBLE_BACKWARD_SLASH);
		String pdfPath = imageLocation.getKeywordValue()+pdfLocation.getKeywordValue();
		SimpleDateFormat df = new SimpleDateFormat("dd-MM-yyyy");
		try {
			AOFPdfFormRequestDTO aof = new AOFPdfFormRequestDTO();
			aof.setArnCode("144511");
			aof.setSubBrokerCode("");
			aof.setEuin("");	
			aof.setFirstApplicant(user.getPanDetails().getFullName());
			/** **/
			String encryptededPanNoRef = user.getPanDetails().getPanNo();
			String decryptedPanNoRef = "";
			try {
				decryptedPanNoRef = encryptUserDetail.decrypt(encryptededPanNoRef);
			} catch(Exception ex) {
				ex.printStackTrace();
			}
			//aof.setPanNumber1(user.getPanDetails().getPanNo());
			aof.setPanNumber1(decryptedPanNoRef);
			aof.setKyc1("");
			aof.setDob1(df.format(user.getPanDetails().getDateOfBirth()));
			//aof.setNameOfGuardian(user.getKycDetails().getFatherName());
			aof.setNameOfGuardian("");
			AddressProof addressProof = new AddressProof();
			for(AddressProof address: user.getAddressProofs()){
				addressProof.setAddress(address.getAddress());
				addressProof.setState(address.getState());
				addressProof.setCity(address.getCity());
				addressProof.setPincode(address.getPincode());
			}
			aof.setAddress(addressProof.getAddress());
			//aof.setAddressLine1("");
			//aof.setAddressLine2("");
			aof.setAddressLine1(addressProof.getField2());
			aof.setAddressLine2(addressProof.getField3());
			aof.setCity1(addressProof.getCity());
			aof.setPincode1(addressProof.getPincode());
			aof.setState1(addressProof.getState());
			aof.setCountry1("India");
			aof.setEmail(user.getEmail());
			aof.setMobile(user.getMobileNumber());
			aof.setModeOfHolding("Single");
			String occupation = user.getPanDetails().getOccupation();
			if(occupation.equals("01")){
				occupation = "Business";
			}else if(occupation.equals("02")){
				occupation = "Services";
			}else if(occupation.equals("03")){
				occupation = "Professional";
			}else if(occupation.equals("04")){
				occupation = "Agriculture";
			}else if(occupation.equals("05")){
				occupation = "Retired";
			}else if(occupation.equals("06")){
				occupation = "Housewife";
			}else if(occupation.equals("07")){
				occupation = "Student";
			}else if(occupation.equals("08")){
				occupation = "Others";
			}else if(occupation.equals("09")){
				occupation = "Doctor";
			}else if(occupation.equals("41")){
				occupation = "Private Sector Service";
			}else if(occupation.equals("42")){
				occupation = "Public Sector Service";
			}else if(occupation.equals("43")){
				occupation = "Forex Dealer";
			}else if(occupation.equals("44")){
				occupation = "Government Service";
			}else if(occupation.equals("99")){
				occupation = "Unknown / Not Applicable";
			}
			aof.setOccupation(occupation);
			aof.setSecondApplicant("");
			aof.setPanNumber2("2");
			aof.setKyc2("");
			aof.setDob2("");
			aof.setThirdApplicant("");
			aof.setPanNumber3("");
			aof.setKyc3("");
			aof.setDob3("");
			aof.setOtherDetails("");
			aof.setOverseasAddress("");
			aof.setOtherCity("");
			aof.setOtherPincode("");
			aof.setOtherCountry("");
			aof.setNameOfBank(user.getBankDetails().getBankName());
			aof.setBranch(user.getBankDetails().getBankBranch());
			/** **/
			String encryptededAccountNoRef = user.getBankDetails().getAccountNo();
			String decryptedAccountNoRef = "";
			try {
				decryptedAccountNoRef = encryptUserDetail.decrypt(encryptededAccountNoRef);
			} catch(Exception ex) {
				ex.printStackTrace();
			}
			//aof.setAccountNo(user.getBankDetails().getAccountNo());
			aof.setAccountNo(decryptedAccountNoRef);
			aof.setAccountType(user.getBankDetails().getAccountType());
			aof.setIfscCode(user.getBankDetails().getIfsc());
			aof.setBankCity("");
			aof.setBankPincode("");
			aof.setBankState("");
			aof.setBankAddress(user.getBankDetails().getBankAddress());
			aof.setBankCountry("");
			aof.setGurdianName("");
			aof.setNomineeName("");
			aof.setNomineeAddress("");
			aof.setNomineePincode("");
			aof.setNomineeState("");
			aof.setNomineecity("");
			aof.setRelationship("");
			aof.setDate(df.format(new Date()));
			aof.setPlace("");
			aof.setFirstApplicantSignature(imageLocation.getKeywordValue()+user.getBankDetails().getSignatureImage());
			String path = pdfPath+doubleSlash.getKeywordValue()+"pdf_"+user.getUserId()+".pdf";
			GoForWealthPRSUtil.generateAofFormPdf(aof,path);
			tiffPath = convertPdfToTiff(path, user, getPasswordRequest);
		}catch(IOException e){
			e.printStackTrace();
		}
		return tiffPath;
	}

	private String convertPdfToTiff(String path, User user, GetPasswordRequest getPasswordRequest) {
		String tiffPath=null;
		StoreConf imageLocation = storeConfRepository.findByKeyword(GoForWealthPRSConstants.IMAGE_LOCATION);
		StoreConf tiffLocation = storeConfRepository.findByKeyword(GoForWealthPRSConstants.TIFF_LOCATION);
		StoreConf doubleSlash = storeConfRepository.findByKeyword(GoForWealthPRSConstants.DOUBLE_BACKWARD_SLASH);
		String aofPath = imageLocation.getKeywordValue()+tiffLocation.getKeywordValue();
		try {
			PDDocument document = PDDocument.load(new File(path));
			ImageIO.scanForPlugins();
			PDFRenderer pdfRenderer = new PDFRenderer(document);
			for (int page = 0; page < document.getNumberOfPages(); ++page){
				BufferedImage bim = pdfRenderer.renderImageWithDPI(page, 300, ImageType.RGB);
			    tiffPath = aofPath+doubleSlash.getKeywordValue()+getPasswordRequest.getMemberId()+getTiffName("G4W"+user.getUserId());
			    ImageIOUtil.writeImage(bim, tiffPath, 300);
			}
			document.close();
	    } catch (Exception e) {
	        e.printStackTrace();
	    }	
		return tiffPath;
	}
	
	private String convertMandatePdfToTiff(String path, User user, GetPasswordRequest getPasswordRequest) {
		String tiffPath=null;
		StoreConf imageLocation = storeConfRepository.findByKeyword(GoForWealthPRSConstants.IMAGE_LOCATION);
		StoreConf tiffLocation = storeConfRepository.findByKeyword(GoForWealthPRSConstants.MANDATE_TIFF_LOCATION);
		StoreConf doubleSlash = storeConfRepository.findByKeyword(GoForWealthPRSConstants.DOUBLE_BACKWARD_SLASH);
		String aofPath = imageLocation.getKeywordValue()+tiffLocation.getKeywordValue();
		try {
			PDDocument document = PDDocument.load(new File(path));
			ImageIO.scanForPlugins();
			PDFRenderer pdfRenderer = new PDFRenderer(document);
			for (int page = 0; page < document.getNumberOfPages(); ++page){
				BufferedImage bim = pdfRenderer.renderImageWithDPI(page, 300, ImageType.RGB);
				tiffPath = aofPath+doubleSlash.getKeywordValue()+getPasswordRequest.getMemberId()+getTiffName("G4W"+user.getUserId());
			    ImageIOUtil.writeImage(bim, tiffPath, 300);
			}
			document.close();
	    } catch (Exception e) {
	        e.printStackTrace();
	    }
		return tiffPath;
	}

	public String getTiffName(String clientCode){		
		Calendar cal = Calendar.getInstance();
		cal.setTime(new Date());
		int year = cal.get(Calendar.YEAR);
		int month = cal.get(Calendar.MONTH)+1;
		String monthString = "";
		String dayString = "";
		if(month<10){
			monthString = "0"+month;
		}else{
			monthString = ""+month;
		}
		int day = cal.get(Calendar.DAY_OF_MONTH);
		if(day<10){
			dayString = "0"+day;
		}else{
			dayString = ""+day;
		}
		String tiffName = clientCode+dayString+monthString+year+".tiff";
		return tiffName;
	}

	@Override
	public String mandateUserByAdmin(User user,boolean isFatca){
		String response = registerAndMandateUser(user, isFatca);
		return response;
	}

	@Override
	public String verifyAadhaarNumber(Integer userId, String aadhaarNumber,String mobileWithAadhaar,String addressProofName) throws GoForWealthPRSException {
		AddressProofDTO addressProofDTO = new AddressProofDTO();
		addressProofDTO.setAddressProofNo(aadhaarNumber);
		addressProofDTO.setMobileWithAadhaar(mobileWithAadhaar);
		addressProofDTO.setAddressProofName(addressProofName);
		String response = storeAadhaarDetail(userId, addressProofDTO);
		/*
		AdharVerificationDTO aadharVerificationDTO = new AdharVerificationDTO();
		aadharVerificationDTO.setAdharNo(aadhaarNumber);
		aadharVerificationDTO.setEmail(userObj.getEmail());
		aadharVerificationDTO.setInvName(userObj.getFirstName());
		aadharVerificationDTO.setMobile(mobileWithAadhaar);
		String encryptededPanNoRef = userObj.getPanDetails().getPanNo();
		String decryptedPanNoRef = "";
		try {
			decryptedPanNoRef = encryptUserDetail.decrypt(encryptededPanNoRef);
		} catch(Exception ex) {
			ex.printStackTrace();
		}
		aadharVerificationDTO.setPanNo(decryptedPanNoRef);
		JSONObject jsonResponse = kycCallService.adharVerification(aadharVerificationDTO);
		System.out.println("JSON Response : " + jsonResponse);
		String response = "";
		try {
			if(jsonResponse.getString("ReturnMsg").equalsIgnoreCase("Success")) {
				//response = GoForWealthPRSConstants.SUCCESS;
				response = jsonResponse.getString("RequiredUrl");	
			}
			else {
				response = jsonResponse.getString("ReturnMsg");
			}
		} catch (JSONException e) {
			e.printStackTrace();
		}
		*/
		return response;
	}

	@Override
	public String storeAadhaarDetail(Integer userId, AddressProofDTO addressProofDTO) throws GoForWealthPRSException {
		User user = userRepository.getOne(userId);
		String response = null;
		AddressProof addressProof = null;
		if(user!=null){
			if(user.getAddressProofs()!=null&&user.getAddressProofs().size()>0){
				Iterator<AddressProof> itr = user.getAddressProofs().iterator();
				while(itr.hasNext()){
					AddressProof addressProofTemp = itr.next();
					if(addressProofTemp.getAddressProofName().equals(addressProofDTO.getAddressProofName()) && addressProofTemp.getAddressProofNo() != null){
						addressProof = addressProofTemp;
						if(addressProof.getAddressProofNo()==null){
							List<AddressProof> addressProofTempList = addressProofRepository.findAll();
							for (AddressProof addressProof2 : addressProofTempList) {
								if(addressProof2.getAddressProofNo() != null && !addressProof2.getAddressProofNo().equals("")) {
									try {
										String decreptedAadharNoRef = encryptUserDetail.decrypt(addressProof2.getAddressProofNo());
										if(addressProofDTO.getAddressProofNo().equals(decreptedAadharNoRef)) {
											addressProofTemp = addressProof2;
											break;
										}
									}catch(Exception ex) {
										ex.printStackTrace();
									}
								}
							}
							//addressProofTemp = addressProofRepository.findAddressProofByAddressProofNo(addressProofDTO.getAddressProofNo());
							if(addressProofTemp!=null){
								response = GoForWealthPRSConstants.ADDRESS_PROOF_ALREADY_EXIST;
								return response;
							}
						}else{
							response = GoForWealthPRSConstants.SUCCESS;
						}
					}else{
						//addressProof = addressProofRepository.findAddressProofByAddressProofNo(addressProofDTO.getAddressProofNo());
						List<AddressProof> addressProofList = addressProofRepository.findAll();
						for (AddressProof addressProof2 : addressProofList) {
							if(addressProof2.getAddressProofNo() != null && !addressProof2.getAddressProofNo().equals("")) {
								try {
									String decreptedAadharNoRef = encryptUserDetail.decrypt(addressProof2.getAddressProofNo());
									if(addressProofDTO.getAddressProofNo().equals(decreptedAadharNoRef)) {
										addressProof = addressProof2;
										break;
									}
								}catch(Exception ex) {
									ex.printStackTrace();
								}
							}
						}
						if(addressProof!=null){
							response = GoForWealthPRSConstants.ADDRESS_PROOF_ALREADY_EXIST;
							return response;
						}
						addressProof = new AddressProof();
						addressProof.setAddressProofName(addressProofDTO.getAddressProofName());
						addressProof.setVerified("not-verified");
						addressProof.setUser(user);
					}
				}
			}else{
				//addressProof = addressProofRepository.findAddressProofByAddressProofNo(addressProofDTO.getAddressProofNo());
				List<AddressProof> addressProofList = addressProofRepository.findAll();
				for (AddressProof addressProof2 : addressProofList) {
					if(addressProof2.getAddressProofNo() != null && !addressProof2.getAddressProofNo().equals("")) {
						try {
							String decreptedAadharNoRef = encryptUserDetail.decrypt(addressProof2.getAddressProofNo());
							if(addressProofDTO.getAddressProofNo().equals(decreptedAadharNoRef)) {
								addressProof = addressProof2;
								break;
							}
						}catch(Exception ex) {
							ex.printStackTrace();
						}
					}
				}
				if(addressProof!=null){
					response = GoForWealthPRSConstants.ADDRESS_PROOF_ALREADY_EXIST;
					return response;
				}
				addressProof = new AddressProof();
				addressProof.setAddressProofName(addressProofDTO.getAddressProofName());
				addressProof.setVerified("verified");
				addressProof.setUser(user);
				addressProofRepository.save(addressProof);
				response = GoForWealthPRSConstants.SUCCESS;
			}
			if(!addressProofDTO.getAddressProofNo().equals("")){
				addressProof.setAddress(addressProofDTO.getAddress());
				addressProof.setCity(addressProofDTO.getCity());
				addressProof.setState(addressProofDTO.getState());
				addressProof.setPincode(addressProofDTO.getPincode());
				addressProof.setMobileWithAadhaar(addressProofDTO.getMobileWithAadhaar());
				addressProof.setVerified("not-verified");
				try {
					if(!addressProofDTO.getAddressProofNo().equals("")){
						String encreptedAadharNoRef = encryptUserDetail.encrypt(addressProofDTO.getAddressProofNo());
						addressProof.setAddressProofNo(encreptedAadharNoRef);
					}	
				}catch(Exception ex) {
					ex.printStackTrace();
				}
				//addressProof.setAddressProofNo(addressProofDTO.getAddressProofNo());
				addressProofRepository.save(addressProof);
				response = GoForWealthPRSConstants.SUCCESS;
				AdharVerificationDTO aadharVerificationDTO = new AdharVerificationDTO();
				aadharVerificationDTO.setAdharNo(addressProofDTO.getAddressProofNo());
				aadharVerificationDTO.setEmail(user.getEmail());
				//aadharVerificationDTO.setInvName(user.getFirstName());
				aadharVerificationDTO.setInvName(user.getFirstName()+" "+user.getLastName());
				aadharVerificationDTO.setMobile(addressProofDTO.getMobileWithAadhaar());
				String encryptededPanNoRef = user.getPanDetails().getPanNo();
				String decryptedPanNoRef = "";
				try {
					decryptedPanNoRef = encryptUserDetail.decrypt(encryptededPanNoRef);
				} catch(Exception ex) {
					ex.printStackTrace();
				}
				aadharVerificationDTO.setPanNo(decryptedPanNoRef);
				if(!addressProofDTO.getAddressProofNo().equals("")){
					JSONObject jsonResponse = kycCallService.adharVerification(aadharVerificationDTO);
					System.out.println("JSON Response : " + jsonResponse);
					try {
						if(jsonResponse.getString("ReturnMsg").equalsIgnoreCase("Success")) {
							//response = GoForWealthPRSConstants.SUCCESS;
							response = jsonResponse.getString("RequiredUrl");	
						}
						else {
							response = jsonResponse.getString("ReturnMsg");
						}
					} catch (JSONException e) {
						e.printStackTrace();
					}
				}
			}
		}else{
			response = GoForWealthPRSConstants.USER_NOT_EXIST;
		}
		return response;
	}

	@Override
	public String updateEkycStatus(Integer userId,String karvyResponse) throws GoForWealthPRSException {
		User userObj = userRepository.getOne(userId);
		String response = null;
		if(userObj != null) {
			if(karvyResponse.equals("Success")) {
				OnboardingStatus onboardingStatusObj = userObj.getOnboardingStatus();
				onboardingStatusObj.setKycStatus(1);
				PanDetails panDetails = userObj.getPanDetails();
				panDetails.setVerified("verified");
				//remove aadhar status
				/*
				Iterator<AddressProof> itr = userObj.getAddressProofs().iterator();
				while(itr.hasNext()) {
					AddressProof addressProofObj = itr.next();
					addressProofObj.setVerified("verified");
				}
				*/
				userRepository.save(userObj);
				response = GoForWealthPRSConstants.SUCCESS;
			} else {
				OnboardingStatus onboardingStatusObj = userObj.getOnboardingStatus();
				onboardingStatusObj.setKycStatus(0);
				PanDetails panDetails = userObj.getPanDetails();
				panDetails.setVerified("not-verified");
				//remove aadhar status
				/*
				Iterator<AddressProof> itr = userObj.getAddressProofs().iterator();
				while(itr.hasNext()) {
					AddressProof addressProofObj = itr.next();
					addressProofObj.setVerified("not-verified");
				}
				*/
				userRepository.save(userObj);
				response = GoForWealthPRSConstants.SUCCESS;
			}
		} else {
			response = GoForWealthPRSConstants.USER_NOT_EXIST;
		}
		return response;
	}

	@Override
	public List<CityDTO> getCityListByState(String stateName) {
		List<CityDTO> cityDtoList = new ArrayList<>();
		AddressState addressState = addressStateRepository.getStateIdByStateName(stateName);
		if(addressState != null){
			int stateId = addressState.getAddressStateId();
			List<AddressCity> addressCityList = addressCityRepository.getCityListByStateId(stateId);
			if(addressCityList != null){
				for (AddressCity addressCity : addressCityList) {
					CityDTO cityDTO = new CityDTO();
					cityDTO.setCityId(addressCity.getAddressCityId());
					cityDTO.setCityName(addressCity.getAddressCityname());
					cityDtoList.add(cityDTO);
				}
			}
		}
		return cityDtoList;
	}

	@Override
	public String getUrl() {
		String response = eKycVerificationconfiguration.ekycVerificationRemoveQueryParamUrl;
		return response;
	}

	@Override
	public String updateEmandate(Integer userId, BankDetailsDTO bankDetailsDTO) {
		User user = userRepository.getOne(userId);
		String response = "";
		String oldBanAccNo = "";
		String newBanAccNo = bankDetailsDTO.getAccountNumber();
		BankDetails bankDetails = user.getBankDetails();
		try{
			 oldBanAccNo =encryptUserDetail.decrypt(bankDetails.getAccountNo());
		}catch(Exception e){
			e.printStackTrace();
		}
		boolean isFatca = true;
		BankDetails bankDetailsTemp = null;
		UserAdditionalBankinfo userAdditionalBankInfoObj= userAdditionalBankinfoRepository.findByUserId(userId);
		UserAdditionalBankinfo userAdditionalBankinfoObjRef=null;
		if(userAdditionalBankInfoObj !=null){
			userAdditionalBankInfoObj.setAccountNumber(bankDetails.getAccountNo());
			userAdditionalBankInfoObj.setAccountType(bankDetails.getAccountType());
			userAdditionalBankInfoObj.setBankAddress(bankDetails.getBankAddress());
			userAdditionalBankInfoObj.setBankBranch(bankDetails.getBankBranch());
			userAdditionalBankInfoObj.setBankName(bankDetails.getBankName());
			userAdditionalBankInfoObj.setIfscCode(bankDetails.getIfsc());
			userAdditionalBankInfoObj.setMicrCode(bankDetails.getMicrCode());
			userAdditionalBankInfoObj.setStatus("DEACTIVATE");
			userAdditionalBankInfoObj.setUserId(user.getUserId());
			userAdditionalBankinfoObjRef = userAdditionalBankinfoRepository.save(userAdditionalBankInfoObj);
		}else{
			UserAdditionalBankinfo userAdditionalBankInfo = new UserAdditionalBankinfo();
			userAdditionalBankInfo.setAccountNumber(bankDetails.getAccountNo());
			userAdditionalBankInfo.setAccountType(bankDetails.getAccountType());
			userAdditionalBankInfo.setBankAddress(bankDetails.getBankAddress());
			userAdditionalBankInfo.setBankBranch(bankDetails.getBankBranch());
			userAdditionalBankInfo.setBankName(bankDetails.getBankName());
			userAdditionalBankInfo.setIfscCode(bankDetails.getIfsc());
			userAdditionalBankInfo.setMicrCode(bankDetails.getMicrCode());
			userAdditionalBankInfo.setStatus("DEACTIVATE");
			userAdditionalBankInfo.setUserId(user.getUserId());
			userAdditionalBankinfoObjRef = userAdditionalBankinfoRepository.save(userAdditionalBankInfo);
		}
		if(user!=null){
			bankDetailsTemp = user.getBankDetails();
			try {
				bankDetailsTemp.setAccountNo(encryptUserDetail.encrypt(bankDetailsDTO.getAccountNumber()));
			} catch (Exception e) {
				e.printStackTrace();
			}
			bankDetailsTemp.setAccountType(bankDetailsTemp.getAccountType());
			bankDetailsTemp.setBankAddress(bankDetailsDTO.getBankAddress());
			bankDetailsTemp.setBankBranch(bankDetailsDTO.getBankBranch());
			bankDetailsTemp.setBankName(bankDetailsDTO.getBankName());
			bankDetailsTemp.setIfsc(bankDetailsDTO.getIfscCode());
			bankDetailsTemp.setMicrCode(bankDetailsDTO.getMicrCode());
			bankDetailsRepository.save(bankDetailsTemp);
			OnboardingStatus onboardingStatus = user.getOnboardingStatus();
			if(onboardingStatus != null){
				if(!newBanAccNo.equals(oldBanAccNo)){
					onboardingStatus.setMandateStatus(0);
					onboardingStatus.setIsipMandateStatus(0);
					onboardingStatusRepository.save(onboardingStatus);
				}
			}
			
			response = registerAndMandateUser(user,isFatca);	
			if(response.equals("success")){
			}else{
				BankDetails bankDetailsObj = bankDetailsRepository.getOne(user.getUserId());
				bankDetailsObj.setAccountNo(userAdditionalBankinfoObjRef.getAccountNumber());
				bankDetailsObj.setAccountType(userAdditionalBankinfoObjRef.getAccountType());
				bankDetailsObj.setBankAddress(userAdditionalBankinfoObjRef.getBankAddress());
				bankDetailsObj.setBankBranch(userAdditionalBankinfoObjRef.getBankBranch());
				bankDetailsObj.setBankName(userAdditionalBankinfoObjRef.getBankName());
				bankDetailsObj.setIfsc(userAdditionalBankinfoObjRef.getIfscCode());
				bankDetailsObj.setMicrCode(userAdditionalBankinfoObjRef.getMicrCode());
				bankDetailsRepository.save(bankDetailsObj);
			}
		}
		return response;
	}

	@Override
	public boolean updateOnboardingStatus(Integer userId) {
		OnboardingStatus onboardingStatus = onboardingStatusRepository.getOne(userId);
		onboardingStatus.setOverallStatus(1);
		onboardingStatus.setEnachStatus("Pending");
		onboardingStatus.setBillerStatus("Pending");
		onboardingStatus.setUploadMandateStatus(null);
		onboardingStatus.setField2("");
		OnboardingStatus onboardingStatusObj = onboardingStatusRepository.save(onboardingStatus);
		if(onboardingStatusObj !=null){
			return true;
		}else{
			return false;
		}
	}

	@Override
	public String uploadMandateFileImage(User user, FileDetailsDTO fileDetails) {
		String memberId = "";
		String response = "";
		com.moptra.go4wealth.prs.imageupload.GetPasswordRequest getPasswordRequest2 = new com.moptra.go4wealth.prs.imageupload.GetPasswordRequest();
		List<SystemProperties> systemPropertiesList=  systemPropertiesRepository.findAll();
		for (SystemProperties systemProperties : systemPropertiesList) {
			if(systemProperties.getId().getPropertyKey().equals("UserId")){
				getPasswordRequest2.setUserId(systemProperties.getId().getPropertyValue());
			}else if(systemProperties.getId().getPropertyKey().equals("MemberId")){
				memberId = systemProperties.getId().getPropertyValue();
				getPasswordRequest2.setMemberId(systemProperties.getId().getPropertyValue());
			}else if(systemProperties.getId().getPropertyKey().equals("Password")){
				getPasswordRequest2.setPassword(systemProperties.getId().getPropertyValue());
			}
		}
		String password = null;
		try {
			password = imageUploadService.getPassword(getPasswordRequest2);
		} catch (GoForWealthPRSException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		if(password!=null){
			MandateFormUploadRequest mandateFormUploadRequest = new MandateFormUploadRequest();
			mandateFormUploadRequest.setClientCode(user.getOnboardingStatus().getClientCode());
			mandateFormUploadRequest.setEncrypedPassword(password);
			mandateFormUploadRequest.setFiller1("CITI00002000000037");
			mandateFormUploadRequest.setFiller2("BILLDESK");
			mandateFormUploadRequest.setFlag("SCAN_MANDATE");
			mandateFormUploadRequest.setImageName(user.getOnboardingStatus().getMandateNumber()+".tiff");
			mandateFormUploadRequest.setImageType("image/tiff");
			mandateFormUploadRequest.setMandateId(user.getOnboardingStatus().getMandateNumber());
			mandateFormUploadRequest.setMandateType("XSIP");
			mandateFormUploadRequest.setMemberCode(memberId);
			mandateFormUploadRequest.setpFileBytes(fileDetails.getSignatureString());
			String tiffPath=null;
			StoreConf imageLocation = storeConfRepository.findByKeyword(GoForWealthPRSConstants.IMAGE_LOCATION);
			StoreConf tiffLocation = storeConfRepository.findByKeyword(GoForWealthPRSConstants.MANDATE_TIFF_LOCATION);
			StoreConf doubleSlash = storeConfRepository.findByKeyword(GoForWealthPRSConstants.DOUBLE_BACKWARD_SLASH);
			String aofPath = imageLocation.getKeywordValue()+tiffLocation.getKeywordValue();
			 tiffPath = aofPath+doubleSlash.getKeywordValue()+"Upload_"+memberId+getTiffName("G4W"+user.getUserId());
			 File file = new File(tiffPath);
			 byte[] bytes = org.apache.commons.codec.binary.Base64.decodeBase64(fileDetails.getSignatureString());
			 try {
				FileUtils.writeByteArrayToFile(file, bytes);
			} catch (IOException e) {
				e.printStackTrace();
				return "Problem in Mandate registration. Please contact admin.";
			}
			response = imageUploadService.uploadMandateFile(mandateFormUploadRequest);
			System.out.println("AOF Upload Response: "+response);
			String[] res = response.split("\\|");
			if(res.length>1){
				String code = res[0];
				String message = res[1];
				response = message;	
				if(code.equals("100")){							
					user.getOnboardingStatus().setUploadMandateStatus("Initiated");
					userRepository.save(user);
				}
			}else{
				return "Problem in Mandate registration. Please contact admin.";
			}
		}else{
			return "Problem in Mandate registration Password. Please contact admin.";
		}
		return response;
	}


}
