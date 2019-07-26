package com.moptra.go4wealth.prs.web;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.nio.file.Files;
import java.util.List;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.codec.binary.Base64;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.moptra.go4wealth.bean.StoreConf;
import com.moptra.go4wealth.bean.User;
import com.moptra.go4wealth.prs.common.constant.GoForWealthPRSConstants;
import com.moptra.go4wealth.prs.common.enums.GoForWealthPRSErrorMessageEnum;
import com.moptra.go4wealth.prs.common.exception.GoForWealthPRSException;
import com.moptra.go4wealth.prs.common.rest.GoForWealthPRSResponseInfo;
import com.moptra.go4wealth.prs.common.util.GoForWealthPRSUtil;
import com.moptra.go4wealth.prs.mfuploadapi.model.MandateRequest;
import com.moptra.go4wealth.prs.model.AddressProofDTO;
import com.moptra.go4wealth.prs.model.BankDetailsDTO;
import com.moptra.go4wealth.prs.model.BasicDetailsDTO;
import com.moptra.go4wealth.prs.model.FileDetailsDTO;
import com.moptra.go4wealth.prs.service.GoForWealthPRSEmandateService;
import com.moptra.go4wealth.repository.StoreConfRepository;
import com.moptra.go4wealth.repository.UserRepository;
import com.moptra.go4wealth.security.UserPrincipal;
import com.moptra.go4wealth.sip.common.enums.GoForWealthSIPErrorMessageEnum;
import com.moptra.go4wealth.sip.common.rest.GoForWealthSIPResponseInfo;
import com.moptra.go4wealth.sip.common.util.GoForWealthSIPUtil;
import com.moptra.go4wealth.sip.model.CityDTO;

@RestController
@RequestMapping("${server.contextPath}/e-mandate/")
public class GoForWealthEmandateController {

	private static Logger logger = LoggerFactory.getLogger(GoForWealthEmandateController.class);

	@Autowired
	GoForWealthPRSEmandateService goForWealthPRSEmandateService;

	@Autowired
	StoreConfRepository storeConfRepository;

	@Autowired
	UserRepository userRepository;

	/**
	 * 
	 * @param exception
	 * @return
	*/
	@ExceptionHandler({GoForWealthPRSException.class})
	public GoForWealthPRSResponseInfo handleException(GoForWealthPRSException exception) {
		return GoForWealthPRSUtil.getExceptionResponseInfo(exception);
	}

	/**
	 * 
	 * @param exception
	 * @return
	*/
	@ExceptionHandler({Exception.class})
	public GoForWealthPRSResponseInfo handleException(Exception exception) {
		return GoForWealthPRSUtil.getErrorResponseInfo(GoForWealthPRSErrorMessageEnum.COMMON_ERROR_CODE.getValue(), GoForWealthPRSErrorMessageEnum.FAILURE_MESSAGE.getValue(), exception);
	}

	/**
	 * 
	 * @param panNumber
	 * @param auth
	 * @return GoForWealthPRSResponseInfo
	 * @throws GoForWealthPRSException 
	*/
	@PostMapping("/verifyPanNumber")
	public GoForWealthPRSResponseInfo verifyPanNumber(/*@RequestParam("panNumber") String panNumber, */ @RequestBody String panNumber,Authentication auth) throws GoForWealthPRSException{
		logger.info("In verifyPanNumber()");
		UserPrincipal userSession = (UserPrincipal)auth.getPrincipal();
		String response = null;
		GoForWealthPRSResponseInfo goForWealthPRSResponseInfo = null;
		if(userSession!=null){
			logger.info("In verifyPanNumber() with UserId : " + userSession.getUser().getUserId());
			User user = userRepository.findUserByRole(userSession.getUser().getUserId(), "USER");
			if(user != null){
				response = goForWealthPRSEmandateService.verifyPanNumber(userSession.getUser().getUserId(), panNumber.toUpperCase());	
				goForWealthPRSResponseInfo =  GoForWealthPRSUtil.getSuccessResponseInfo(GoForWealthPRSErrorMessageEnum.SUCCESS_CODE.getValue(), GoForWealthPRSErrorMessageEnum.SUCCESS_MESSAGE.getValue(),response);
			}else{
				goForWealthPRSResponseInfo=GoForWealthPRSUtil.getSuccessResponseInfo(GoForWealthSIPErrorMessageEnum.ACCESS_DENIED_CODE.getValue(),GoForWealthSIPErrorMessageEnum.ACCESS_DENIED_MESSAGE.getValue(),null);
			}
		}else{
			goForWealthPRSResponseInfo=GoForWealthPRSUtil.getSuccessResponseInfo(GoForWealthSIPErrorMessageEnum.ACCESS_DENIED_CODE.getValue(),GoForWealthSIPErrorMessageEnum.ACCESS_DENIED_MESSAGE.getValue(),null);
		}
		logger.info("Out verifyPanNumber()");
		return goForWealthPRSResponseInfo;
	}

	/**
	 * 
	 * @param aadhaarNumber
	 * @param auth
	 * @return GoForWealthPRSResponseInfo
	 * @throws GoForWealthPRSException 
	*/
	@PostMapping("/verifyAadhaarNumber")
	public GoForWealthPRSResponseInfo verifyAadhaarNumber(@RequestBody AddressProofDTO addressProofDto,Authentication auth) throws GoForWealthPRSException {
		logger.info("In verifyAadhaarNumber()");
		UserPrincipal userSession = (UserPrincipal)auth.getPrincipal();
		String response = null;
		GoForWealthPRSResponseInfo goForWealthPRSResponseInfo = null;
		if(userSession!=null){
			logger.info("In verifyAadhaarNumber() with UserId : " + userSession.getUser().getUserId());
			User user = userRepository.findUserByRole(userSession.getUser().getUserId(), "USER");
			if(user != null){
				response = goForWealthPRSEmandateService.verifyAadhaarNumber(userSession.getUser().getUserId(),addressProofDto.getAddressProofNo(),addressProofDto.getMobileWithAadhaar(),addressProofDto.getAddressProofName());	
				goForWealthPRSResponseInfo =  GoForWealthPRSUtil.getSuccessResponseInfo(GoForWealthPRSErrorMessageEnum.SUCCESS_CODE.getValue(), GoForWealthPRSErrorMessageEnum.SUCCESS_MESSAGE.getValue(),response);
			}else{
				goForWealthPRSResponseInfo=GoForWealthPRSUtil.getSuccessResponseInfo(GoForWealthSIPErrorMessageEnum.ACCESS_DENIED_CODE.getValue(),GoForWealthSIPErrorMessageEnum.ACCESS_DENIED_MESSAGE.getValue(),null);
			}
		}else{
			goForWealthPRSResponseInfo=GoForWealthPRSUtil.getSuccessResponseInfo(GoForWealthSIPErrorMessageEnum.ACCESS_DENIED_CODE.getValue(),GoForWealthSIPErrorMessageEnum.ACCESS_DENIED_MESSAGE.getValue(),null);
		}
		logger.info("Out verifyAadhaarNumber()");
		return goForWealthPRSResponseInfo;
	}

	/**
	 * 
	 * @param aadhaarNumber
	 * @param auth
	 * @return GoForWealthPRSResponseInfo
	 * @throws GoForWealthPRSException 
	*/
	@PostMapping("/updateEkycStatus")
	public GoForWealthPRSResponseInfo updateEkycStatus(@RequestParam("karvyResponse")String karvyResponse, Authentication auth) throws GoForWealthPRSException {
		logger.info("In updateEkycStatus()");
		UserPrincipal userSession = (UserPrincipal)auth.getPrincipal();
		String response = null;
		GoForWealthPRSResponseInfo goForWealthPRSResponseInfo = null;
		if(userSession!=null){
			logger.info("In updateEkycStatus() with UserId : " + userSession.getUser().getUserId());
			User user = userRepository.findUserByRole(userSession.getUser().getUserId(), "USER");
			if(user != null){
				response = goForWealthPRSEmandateService.updateEkycStatus(userSession.getUser().getUserId(),karvyResponse);
				goForWealthPRSResponseInfo =  GoForWealthPRSUtil.getSuccessResponseInfo(GoForWealthPRSErrorMessageEnum.SUCCESS_CODE.getValue(),GoForWealthPRSErrorMessageEnum.SUCCESS_MESSAGE.getValue(),response);
			}else{
				goForWealthPRSResponseInfo=GoForWealthPRSUtil.getSuccessResponseInfo(GoForWealthSIPErrorMessageEnum.ACCESS_DENIED_CODE.getValue(),GoForWealthSIPErrorMessageEnum.ACCESS_DENIED_MESSAGE.getValue(),null);
			}
		}else{
			goForWealthPRSResponseInfo=GoForWealthPRSUtil.getSuccessResponseInfo(GoForWealthSIPErrorMessageEnum.ACCESS_DENIED_CODE.getValue(),GoForWealthSIPErrorMessageEnum.ACCESS_DENIED_MESSAGE.getValue(),null);
		}
		logger.info("Out updateEkycStatus()");
		return goForWealthPRSResponseInfo;
	}

	/**
	 * 
	 * @param basicDetails
	 * @param auth
	 * @return GoForWealthPRSResponseInfo
	*/
	@PostMapping("/storeBasicDetails")
	public GoForWealthPRSResponseInfo storeBasicDetails(@RequestBody BasicDetailsDTO basicDetails, Authentication auth){
		logger.info("In storeBasicDetails()");
		UserPrincipal userSession = (UserPrincipal)auth.getPrincipal();
		String response = null;
		GoForWealthPRSResponseInfo goForWealthPRSResponseInfo = null;
		if(userSession!=null){
			logger.info("In storeBasicDetails() with UserId : " + userSession.getUser().getUserId());
			User user = userRepository.findUserByRole(userSession.getUser().getUserId(), "USER");
			if(user != null){
				response = goForWealthPRSEmandateService.storeBasicDetails(userSession.getUser().getUserId(), basicDetails);
				goForWealthPRSResponseInfo = GoForWealthPRSUtil.getSuccessResponseInfo(GoForWealthPRSErrorMessageEnum.SUCCESS_CODE.getValue(), GoForWealthPRSErrorMessageEnum.SUCCESS_MESSAGE.getValue(),response);
			}else{
				goForWealthPRSResponseInfo=GoForWealthPRSUtil.getSuccessResponseInfo(GoForWealthSIPErrorMessageEnum.ACCESS_DENIED_CODE.getValue(),GoForWealthSIPErrorMessageEnum.ACCESS_DENIED_MESSAGE.getValue(),null);
			}
		}else{
			goForWealthPRSResponseInfo=GoForWealthPRSUtil.getSuccessResponseInfo(GoForWealthSIPErrorMessageEnum.ACCESS_DENIED_CODE.getValue(),GoForWealthSIPErrorMessageEnum.ACCESS_DENIED_MESSAGE.getValue(),null);
		}
		logger.info("Out storeBasicDetails()");
		return goForWealthPRSResponseInfo;
	}

	/**
	 * 
	 * @param mandateRequest
	 * @param auth
	 * @return GoForWealthPRSResponseInfo
	*/
	@PostMapping("/storeBankDetails")
	public GoForWealthPRSResponseInfo storeBankDetails(@RequestBody MandateRequest mandateRequest, Authentication auth){
		logger.info("In storeBankDetails()");
		UserPrincipal userSession = (UserPrincipal)auth.getPrincipal();
		String response = null;
		GoForWealthPRSResponseInfo goForWealthPRSResponseInfo = null;
		if(userSession!=null){
			logger.info("In storeBankDetails() with UserId : " + userSession.getUser().getUserId());
			User user = userRepository.findUserByRole(userSession.getUser().getUserId(), "USER");
			if(user != null){
				response = goForWealthPRSEmandateService.storeBankDetails(userSession.getUser().getUserId(), mandateRequest);
				goForWealthPRSResponseInfo = GoForWealthPRSUtil.getSuccessResponseInfo(GoForWealthPRSErrorMessageEnum.SUCCESS_CODE.getValue(),GoForWealthPRSErrorMessageEnum.SUCCESS_MESSAGE.getValue(),response);
			}else{
				goForWealthPRSResponseInfo=GoForWealthPRSUtil.getSuccessResponseInfo(GoForWealthSIPErrorMessageEnum.ACCESS_DENIED_CODE.getValue(),GoForWealthSIPErrorMessageEnum.ACCESS_DENIED_MESSAGE.getValue(),null);
			}
		}else{
			goForWealthPRSResponseInfo=GoForWealthPRSUtil.getSuccessResponseInfo(GoForWealthSIPErrorMessageEnum.ACCESS_DENIED_CODE.getValue(),GoForWealthSIPErrorMessageEnum.ACCESS_DENIED_MESSAGE.getValue(),null);
		}
		logger.info("Out storeBankDetails()");
		return goForWealthPRSResponseInfo;
	}

	@PostMapping("/uploadSignature")
	public GoForWealthPRSResponseInfo uploadSignature(@RequestBody FileDetailsDTO fileDetails, Authentication auth) throws IOException, GoForWealthPRSException{
		logger.info("In uploadSignature()");
		UserPrincipal userSession = (UserPrincipal)auth.getPrincipal();
		String response = null;
		GoForWealthPRSResponseInfo goForWealthPRSResponseInfo = null;
		if(userSession!=null){
			logger.info("In uploadSignature() with UserId : " + userSession.getUser().getUserId());
			User user = userRepository.findUserByRole(userSession.getUser().getUserId(), "USER");
			if(user != null){
				response = goForWealthPRSEmandateService.uploadSignature(userSession.getUser().getUserId(), fileDetails);
				goForWealthPRSResponseInfo = GoForWealthPRSUtil.getSuccessResponseInfo(GoForWealthPRSErrorMessageEnum.SUCCESS_CODE.getValue(),GoForWealthPRSErrorMessageEnum.SUCCESS_MESSAGE.getValue(),response);
			}else{
				goForWealthPRSResponseInfo=GoForWealthPRSUtil.getSuccessResponseInfo(GoForWealthSIPErrorMessageEnum.ACCESS_DENIED_CODE.getValue(),GoForWealthSIPErrorMessageEnum.ACCESS_DENIED_MESSAGE.getValue(),null);
			}
		}else{
			goForWealthPRSResponseInfo=GoForWealthPRSUtil.getSuccessResponseInfo(GoForWealthSIPErrorMessageEnum.ACCESS_DENIED_CODE.getValue(),GoForWealthSIPErrorMessageEnum.ACCESS_DENIED_MESSAGE.getValue(),null);
		}
		logger.info("Out uploadSignature()");
		return goForWealthPRSResponseInfo;
	}

	@PostMapping("/storeKycDetails")
	public GoForWealthPRSResponseInfo storeKycDetails(@RequestBody BasicDetailsDTO basicDetails, Authentication auth){
		logger.info("In storeKycDetails()");
		UserPrincipal userSession = (UserPrincipal)auth.getPrincipal();
		String response = null;
		GoForWealthPRSResponseInfo goForWealthPRSResponseInfo = null;
		if(userSession!=null){
			logger.info("In storeKycDetails() with UserId : " + userSession.getUser().getUserId());
			User user = userRepository.findUserByRole(userSession.getUser().getUserId(), "USER");
			if(user != null){
				response = goForWealthPRSEmandateService.storeKycDetails(userSession.getUser().getUserId(), basicDetails);
				goForWealthPRSResponseInfo = GoForWealthPRSUtil.getSuccessResponseInfo(GoForWealthPRSErrorMessageEnum.SUCCESS_CODE.getValue(),GoForWealthPRSErrorMessageEnum.SUCCESS_MESSAGE.getValue(), response);
			}else{
				goForWealthPRSResponseInfo=GoForWealthPRSUtil.getSuccessResponseInfo(GoForWealthSIPErrorMessageEnum.ACCESS_DENIED_CODE.getValue(),GoForWealthSIPErrorMessageEnum.ACCESS_DENIED_MESSAGE.getValue(),null);
			}
		}else{
			goForWealthPRSResponseInfo=GoForWealthPRSUtil.getSuccessResponseInfo(GoForWealthSIPErrorMessageEnum.ACCESS_DENIED_CODE.getValue(),GoForWealthSIPErrorMessageEnum.ACCESS_DENIED_MESSAGE.getValue(),null);
		}
		logger.info("Out storeKycDetails()");
		return goForWealthPRSResponseInfo;
	}

	@GetMapping("/image/{type}/{id}")
	public void showImage(@PathVariable("type")String type, @PathVariable("id")Integer id, HttpServletResponse res) throws IOException {
		String location = goForWealthPRSEmandateService.showImage(id, type);
		StoreConf userImageLocation = storeConfRepository.findByKeyword(GoForWealthPRSConstants.IMAGE_LOCATION);
		location=userImageLocation.getKeywordValue()+location;
		res.setContentType(MediaType.IMAGE_JPEG_VALUE);
		ServletOutputStream out;
		FileInputStream fin ;
		BufferedInputStream bin;
		out = res.getOutputStream();
		BufferedOutputStream bout = new BufferedOutputStream(out);
		try{
			fin = new FileInputStream(location);
			bin = new BufferedInputStream(fin);
			int ch =0; ;
			while((ch=bin.read())!=-1){
				bout.write(ch);
			}
			bin.close();
			fin.close();
			bout.close();
			out.close();
		}catch(FileNotFoundException ex){
			ex.printStackTrace();
		}catch(Exception ex){
			ex.printStackTrace();
		}finally{
			bout.close();
			out.close();
			location="";
		}
	}

	/*
	@GetMapping("/image/{type}/{name}")
	public void showUserDocument(@PathVariable("type")String type, @PathVariable("name")String name,HttpServletResponse res) throws IOException {
		String location = goForWealthPRSEmandateService.showUserDocument(name,type);
		StoreConf userImageLocation = storeConfRepository.findByKeyword(GoForWealthPRSConstants.IMAGE_LOCATION);
		location=userImageLocation.getKeywordValue()+location;
		res.setContentType(MediaType.IMAGE_JPEG_VALUE);
		ServletOutputStream out;
		FileInputStream fin ;
		BufferedInputStream bin;
		out = res.getOutputStream();
		BufferedOutputStream bout = new BufferedOutputStream(out);
		try{
			fin = new FileInputStream(location);
			bin = new BufferedInputStream(fin);
			int ch =0; ;
			while((ch=bin.read())!=-1){
				bout.write(ch);
			}
			bin.close();
			fin.close();
			bout.close();
			out.close();
		}catch(FileNotFoundException ex){
			ex.printStackTrace();
		}catch(Exception ex){
			ex.printStackTrace();
		}finally{
			bout.close();
			out.close();
			location="";
		}
	}
	*/

	@GetMapping("/getImages/{type}")
	public GoForWealthSIPResponseInfo getProfileImage(@PathVariable("type")String type,Authentication auth) throws GoForWealthPRSException {
		logger.info("In getProfileImage()");
		UserPrincipal userSession = (UserPrincipal)auth.getPrincipal();
		GoForWealthSIPResponseInfo responseInfo = null;
		if(userSession!=null){
			logger.info("In getProfileImage() with UserId : " + userSession.getUser().getUserId());
			User user = userRepository.findUserByRole(userSession.getUser().getUserId(), "USER");
			if(user != null){
				String location = goForWealthPRSEmandateService.showImage(userSession.getUser().getUserId(),type);
				StoreConf userImageLocation = storeConfRepository.findByKeyword(GoForWealthPRSConstants.IMAGE_LOCATION);
				location=userImageLocation.getKeywordValue()+location;
				String imageDataString = null;
				File file = new File(location);
				try {
					String fileType = Files.probeContentType(file.toPath());
		            FileInputStream imageInFile = new FileInputStream(file);
		            byte imageData[] = new byte[(int) file.length()];
		            imageInFile.read(imageData);
		            imageDataString = Base64.encodeBase64String(imageData);
		            imageDataString="data:"+fileType+";base64,"+imageDataString;
		            imageInFile.close();
		        } catch (FileNotFoundException e) {
		        	logger.error("Image not found" + e);
		        } catch (IOException ioe) {
		            logger.error("Exception while reading the Image " + ioe);
		        }
				if(imageDataString!=null){
					responseInfo=GoForWealthSIPUtil.getSuccessResponseInfo(GoForWealthSIPErrorMessageEnum.SUCCESS_CODE.getValue(), GoForWealthSIPErrorMessageEnum.SUCCESS_MESSAGE.getValue(), imageDataString);
				}else{
					responseInfo=new GoForWealthSIPResponseInfo();
					responseInfo.setMessage(GoForWealthSIPErrorMessageEnum.DATA_NOT_FOUND_MESSAGE.getValue());
					responseInfo.setStatus(GoForWealthSIPErrorMessageEnum.DATA_NOT_FOUND_CODE.getValue());
					responseInfo.setTotalRecords(0);
				}
			}
			else{
				responseInfo=GoForWealthSIPUtil.getSuccessResponseInfo(GoForWealthSIPErrorMessageEnum.ACCESS_DENIED_CODE.getValue(), GoForWealthSIPErrorMessageEnum.ACCESS_DENIED_MESSAGE.getValue(),null);
			}
		}else{
			responseInfo=GoForWealthSIPUtil.getSuccessResponseInfo(GoForWealthSIPErrorMessageEnum.ACCESS_DENIED_CODE.getValue(), GoForWealthSIPErrorMessageEnum.ACCESS_DENIED_MESSAGE.getValue(),null);
		}
		logger.info("Out getProfileImage()");
		return responseInfo;
	}

	@GetMapping("/imageString/{type}/{userId}")
	public GoForWealthSIPResponseInfo getImageString(@PathVariable("type")String type, @PathVariable("userId")Integer userId, Authentication auth) throws GoForWealthPRSException {
		logger.info("getImageString userId ====  "+userId+ ", type == "+type);
		UserPrincipal userSession = (UserPrincipal)auth.getPrincipal();
		GoForWealthSIPResponseInfo responseInfo = null;
		if(userSession!=null){
			User user = userRepository.findUserByRole(userSession.getUser().getUserId(), "USER");
			if(user != null){
				String location = goForWealthPRSEmandateService.showImage(userId, type);
				StoreConf userImageLocation = storeConfRepository.findByKeyword(GoForWealthPRSConstants.IMAGE_LOCATION);
				location=userImageLocation.getKeywordValue()+location;
				String imageDataString = null;
				File file = new File(location);
				try {
					String  fileType = Files.probeContentType(file.toPath());
			        // Reading a Image file from file system
			        FileInputStream imageInFile = new FileInputStream(file);
			        byte imageData[] = new byte[(int) file.length()];
			        imageInFile.read(imageData);
			        // Converting Image byte array into Base64 String
			        // data:image/png;base64,
			        imageDataString = Base64.encodeBase64String(imageData);//new String(imageData);
			        imageDataString="data:"+fileType+";base64,"+imageDataString;
			        imageInFile.close();
			        logger.info("Image Successfully Manipulated!");
				} catch (FileNotFoundException e) {
					logger.error("Image not found" + e);
			    } catch (IOException ioe) {
			    	logger.error("Exception while reading the Image " + ioe);
			    }
				if(imageDataString!=null){
					responseInfo=GoForWealthSIPUtil.getSuccessResponseInfo(GoForWealthSIPErrorMessageEnum.SUCCESS_CODE.getValue(), GoForWealthSIPErrorMessageEnum.SUCCESS_MESSAGE.getValue(), imageDataString);
				}else{
					responseInfo=new GoForWealthSIPResponseInfo();
					responseInfo.setMessage(GoForWealthSIPErrorMessageEnum.DATA_NOT_FOUND_MESSAGE.getValue());
					responseInfo.setStatus(GoForWealthSIPErrorMessageEnum.DATA_NOT_FOUND_CODE.getValue());
					responseInfo.setTotalRecords(0);
				}
			}else{
				responseInfo=GoForWealthSIPUtil.getSuccessResponseInfo(GoForWealthSIPErrorMessageEnum.ACCESS_DENIED_CODE.getValue(),GoForWealthSIPErrorMessageEnum.ACCESS_DENIED_MESSAGE.getValue(),null);
			}
		}else{
			responseInfo=GoForWealthSIPUtil.getSuccessResponseInfo(GoForWealthSIPErrorMessageEnum.ACCESS_DENIED_CODE.getValue(),GoForWealthSIPErrorMessageEnum.ACCESS_DENIED_MESSAGE.getValue(),null);
		}
		logger.info("Out getImageString()");
		return responseInfo;
	}

	/**
	 * 
	 * @param mandateRequest
	 * @param auth
	 * @return GoForWealthPRSResponseInfo
	 * @throws GoForWealthPRSException 
	*/
	@PostMapping("/storeAddressProof")
	public GoForWealthPRSResponseInfo storeAddressProof(@RequestBody AddressProofDTO addressProofDTO , Authentication auth) throws GoForWealthPRSException{
		logger.info("In storeAddressProof()");
		UserPrincipal userSession = (UserPrincipal)auth.getPrincipal();
		String response = null;
		GoForWealthPRSResponseInfo goForWealthPRSResponseInfo = null;
		if(userSession!=null){
			logger.info("In storeAddressProof() with UserId : " + userSession.getUser().getUserId());
			User user = userRepository.findUserByRole(userSession.getUser().getUserId(), "USER");
			if(user != null){
				response = goForWealthPRSEmandateService.storeAddressProof(userSession.getUser().getUserId(), addressProofDTO);
				goForWealthPRSResponseInfo = GoForWealthPRSUtil.getSuccessResponseInfo(GoForWealthPRSErrorMessageEnum.SUCCESS_CODE.getValue(),GoForWealthPRSErrorMessageEnum.SUCCESS_MESSAGE.getValue(), response);
			}else{
				goForWealthPRSResponseInfo=GoForWealthPRSUtil.getSuccessResponseInfo(GoForWealthSIPErrorMessageEnum.ACCESS_DENIED_CODE.getValue(),GoForWealthSIPErrorMessageEnum.ACCESS_DENIED_MESSAGE.getValue(),null);
			}
		}else{
			goForWealthPRSResponseInfo=GoForWealthPRSUtil.getSuccessResponseInfo(GoForWealthSIPErrorMessageEnum.ACCESS_DENIED_CODE.getValue(),GoForWealthSIPErrorMessageEnum.ACCESS_DENIED_MESSAGE.getValue(),null);
		}
		logger.info("Out storeAddressProof()");
		return goForWealthPRSResponseInfo;
	}

	@PostMapping("/verify-ifsc")
	public GoForWealthPRSResponseInfo verifyIfsc(@RequestParam("ifsc") String ifsc , Authentication auth){
		logger.info("In verifyIfsc()");
		UserPrincipal userSession = (UserPrincipal)auth.getPrincipal();
		GoForWealthPRSResponseInfo goForWealthPRSResponseInfo = null;
		BankDetailsDTO bankDetails = null;
		if(userSession!=null){
			logger.info("In verifyIfsc() with UserId : " + userSession.getUser().getUserId());
			User user = userRepository.findUserByRole(userSession.getUser().getUserId(), "USER");
			if(user != null){
				bankDetails = goForWealthPRSEmandateService.verifyIfsc(userSession.getUser().getUserId(), ifsc);
				goForWealthPRSResponseInfo = GoForWealthPRSUtil.getSuccessResponseInfo(GoForWealthPRSErrorMessageEnum.SUCCESS_CODE.getValue(),GoForWealthPRSErrorMessageEnum.SUCCESS_MESSAGE.getValue(),bankDetails);
			}else{
				goForWealthPRSResponseInfo=GoForWealthPRSUtil.getSuccessResponseInfo(GoForWealthSIPErrorMessageEnum.ACCESS_DENIED_CODE.getValue(),GoForWealthSIPErrorMessageEnum.ACCESS_DENIED_MESSAGE.getValue(),null);
			}
		}else{
			goForWealthPRSResponseInfo=GoForWealthPRSUtil.getSuccessResponseInfo(GoForWealthSIPErrorMessageEnum.ACCESS_DENIED_CODE.getValue(),GoForWealthSIPErrorMessageEnum.ACCESS_DENIED_MESSAGE.getValue(),null);
		}
		logger.info("Out verifyIfsc()");
		return goForWealthPRSResponseInfo;
	}

	/**
	 * 
	 * @param stateId
	 * @param auth
	 * @return
	 * @throws GoForWealthPRSException 
	*/
	@GetMapping("/getCityByState/{stateName}")
	public GoForWealthSIPResponseInfo getCityByStateId(@PathVariable("stateName") String stateName, Authentication auth) throws GoForWealthPRSException {
		logger.info("In getCityByState () : " + stateName);
		GoForWealthSIPResponseInfo responseInfo = null;
		UserPrincipal userSession = (UserPrincipal)auth.getPrincipal();
		if(userSession != null){
			logger.info("In getCityByState() with UserId : " + userSession.getUser().getUserId());
			User user = userRepository.findUserByRole(userSession.getUser().getUserId(), "USER");
			if(user != null){
				List<CityDTO> cityDTOs=goForWealthPRSEmandateService.getCityListByState(stateName);		
				if(cityDTOs!=null&&cityDTOs.size()>0){
					responseInfo=GoForWealthSIPUtil.getSuccessResponseInfo(GoForWealthSIPErrorMessageEnum.SUCCESS_CODE.getValue(), GoForWealthSIPErrorMessageEnum.SUCCESS_MESSAGE.getValue(), cityDTOs);
					responseInfo.setTotalRecords(cityDTOs.size());	
				}else{
					responseInfo=new GoForWealthSIPResponseInfo();
					responseInfo.setMessage(GoForWealthSIPErrorMessageEnum.DATA_NOT_FOUND_MESSAGE.getValue());
					responseInfo.setStatus(GoForWealthSIPErrorMessageEnum.DATA_NOT_FOUND_CODE.getValue());
					responseInfo.setTotalRecords(0);
				}
			}else{
				responseInfo=GoForWealthSIPUtil.getSuccessResponseInfo(GoForWealthSIPErrorMessageEnum.ACCESS_DENIED_CODE.getValue(),GoForWealthSIPErrorMessageEnum.ACCESS_DENIED_MESSAGE.getValue(),null);
			}
		}else{
			responseInfo=GoForWealthSIPUtil.getSuccessResponseInfo(GoForWealthSIPErrorMessageEnum.ACCESS_DENIED_CODE.getValue(),GoForWealthSIPErrorMessageEnum.ACCESS_DENIED_MESSAGE.getValue(),null);
		}
		logger.info("Out getCityByState()");
		return responseInfo;
	}

	@PostMapping("/getUrl")
	public GoForWealthSIPResponseInfo getUrl(Authentication auth) throws GoForWealthPRSException {
		UserPrincipal userSession = (UserPrincipal)auth.getPrincipal();
		GoForWealthSIPResponseInfo responseInfo = null;
		if(userSession != null){
			User user = userRepository.findUserByRole(userSession.getUser().getUserId(), "USER");
			if(user != null){
				String response=goForWealthPRSEmandateService.getUrl();
				if(!response.equals("")){
					responseInfo=GoForWealthSIPUtil.getSuccessResponseInfo(GoForWealthSIPErrorMessageEnum.SUCCESS_CODE.getValue(),GoForWealthSIPErrorMessageEnum.SUCCESS_MESSAGE.getValue(),response);
				}else{
					responseInfo=new GoForWealthSIPResponseInfo();
					responseInfo.setMessage(GoForWealthSIPErrorMessageEnum.DATA_NOT_FOUND_MESSAGE.getValue());
					responseInfo.setStatus(GoForWealthSIPErrorMessageEnum.DATA_NOT_FOUND_CODE.getValue());
					responseInfo.setTotalRecords(0);
				}
			}else{
				responseInfo=GoForWealthSIPUtil.getSuccessResponseInfo(GoForWealthSIPErrorMessageEnum.ACCESS_DENIED_CODE.getValue(),GoForWealthSIPErrorMessageEnum.ACCESS_DENIED_MESSAGE.getValue(),null);
			}
		}else{
			responseInfo=GoForWealthSIPUtil.getSuccessResponseInfo(GoForWealthSIPErrorMessageEnum.ACCESS_DENIED_CODE.getValue(),GoForWealthSIPErrorMessageEnum.ACCESS_DENIED_MESSAGE.getValue(),null);
		}
		return responseInfo;
	}

	@PostMapping("/updateEmandate")
	public GoForWealthPRSResponseInfo updateEmandate(@RequestBody BankDetailsDTO bankDetailsDTO, Authentication auth) throws GoForWealthPRSException {
		logger.info("In updateEmandate()");
		GoForWealthPRSResponseInfo responseInfo = null;
		UserPrincipal userSession = (UserPrincipal)auth.getPrincipal();
		String response = null;
		if(userSession!=null){
			logger.info("In updateEmandate with UserId : " + userSession.getUser().getUserId());
			User user = userRepository.findUserByRole(userSession.getUser().getUserId(), "USER");
			if(user != null){
				response = goForWealthPRSEmandateService.updateEmandate(userSession.getUser().getUserId(),bankDetailsDTO);
				if(response.equals("success")){
					goForWealthPRSEmandateService.updateOnboardingStatus(user.getUserId());
					responseInfo = GoForWealthPRSUtil.getSuccessResponseInfo(GoForWealthPRSErrorMessageEnum.SUCCESS_CODE.getValue(),GoForWealthPRSErrorMessageEnum.SUCCESS_MESSAGE.getValue(),response);
				}else{
					responseInfo = GoForWealthPRSUtil.getSuccessResponseInfo(GoForWealthPRSErrorMessageEnum.FAILURE_CODE.getValue(),GoForWealthPRSErrorMessageEnum.FAILURE_MESSAGE.getValue(),response);
				}
			}else{
				responseInfo=GoForWealthPRSUtil.getSuccessResponseInfo(GoForWealthSIPErrorMessageEnum.ACCESS_DENIED_CODE.getValue(),GoForWealthSIPErrorMessageEnum.ACCESS_DENIED_MESSAGE.getValue(),null);
			}
		}else{
			responseInfo=GoForWealthPRSUtil.getSuccessResponseInfo(GoForWealthSIPErrorMessageEnum.ACCESS_DENIED_CODE.getValue(),GoForWealthSIPErrorMessageEnum.ACCESS_DENIED_MESSAGE.getValue(),null);
		}
		logger.info("Out updateEmandate()");
		return responseInfo;
	}

	@PostMapping("/downloadMandateFileImage")
	public GoForWealthSIPResponseInfo downloadMandateFileImage(@RequestBody String mandateFormType , Authentication auth) throws GoForWealthPRSException {
		logger.info("In downloadMandateFileImage()");
		UserPrincipal userSession = (UserPrincipal)auth.getPrincipal();
		GoForWealthSIPResponseInfo responseInfo = null;
		if(userSession!=null){
			User user = userRepository.findUserByRole(userSession.getUser().getUserId(), "USER");
			if(user != null){
				String imageDataString = null;
				if(user.getOnboardingStatus().getOverallStatus() == 1){
					imageDataString = goForWealthPRSEmandateService.downloadMandateFileImage(userSession.getUser(),mandateFormType);
					if(imageDataString!=null){
						responseInfo=GoForWealthSIPUtil.getSuccessResponseInfo(GoForWealthSIPErrorMessageEnum.SUCCESS_CODE.getValue(), GoForWealthSIPErrorMessageEnum.SUCCESS_MESSAGE.getValue(), imageDataString);
					}else{
						responseInfo=new GoForWealthSIPResponseInfo();
						responseInfo.setMessage(GoForWealthSIPErrorMessageEnum.DATA_NOT_FOUND_MESSAGE.getValue());
						responseInfo.setStatus(GoForWealthSIPErrorMessageEnum.DATA_NOT_FOUND_CODE.getValue());
						responseInfo.setTotalRecords(0);
					}
				}else{
					responseInfo=new GoForWealthSIPResponseInfo();
					responseInfo.setMessage(GoForWealthSIPErrorMessageEnum.DATA_NOT_FOUND_MESSAGE.getValue());
					responseInfo.setStatus(GoForWealthSIPErrorMessageEnum.DATA_NOT_FOUND_CODE.getValue());
					responseInfo.setTotalRecords(0);
				}
			}else{
				responseInfo=GoForWealthSIPUtil.getSuccessResponseInfo(GoForWealthSIPErrorMessageEnum.ACCESS_DENIED_CODE.getValue(), GoForWealthSIPErrorMessageEnum.ACCESS_DENIED_MESSAGE.getValue(),null);
			}
		}else{
			responseInfo=GoForWealthSIPUtil.getSuccessResponseInfo(GoForWealthSIPErrorMessageEnum.ACCESS_DENIED_CODE.getValue(), GoForWealthSIPErrorMessageEnum.ACCESS_DENIED_MESSAGE.getValue(),null);
		}
		logger.info("Out downloadMandateFileImage()");
		return responseInfo;
	}

	@PostMapping("/uploadMandateFileImage")
	public GoForWealthSIPResponseInfo uploadMandateFileImage(@RequestBody FileDetailsDTO fileDetails , Authentication auth) throws GoForWealthPRSException {
		logger.info("In uploadMandateFileImage()");
		UserPrincipal userSession = (UserPrincipal)auth.getPrincipal();
		GoForWealthSIPResponseInfo responseInfo = null;
		if(userSession!=null){
			logger.info("In uploadMandateFileImage() with UserId : " + userSession.getUser().getUserId());
			User user = userRepository.findUserByRole(userSession.getUser().getUserId(), "USER");
			if(user != null){
				if(fileDetails.getImageType().equals("image/png") || fileDetails.getImageType().equals("image/tiff") || fileDetails.getImageType().equals("image/jpeg") || fileDetails.getImageType().equals("image/jpg")){
					String imageDataString = null;
				    imageDataString = goForWealthPRSEmandateService.uploadMandateFileImage(userSession.getUser(),fileDetails);
					if(imageDataString!=null){
						responseInfo=GoForWealthSIPUtil.getSuccessResponseInfo(GoForWealthSIPErrorMessageEnum.SUCCESS_CODE.getValue(), GoForWealthSIPErrorMessageEnum.SUCCESS_MESSAGE.getValue(), imageDataString);
					}else{
						responseInfo=new GoForWealthSIPResponseInfo();
						responseInfo.setMessage(GoForWealthSIPErrorMessageEnum.DATA_NOT_FOUND_MESSAGE.getValue());
						responseInfo.setStatus(GoForWealthSIPErrorMessageEnum.DATA_NOT_FOUND_CODE.getValue());
						responseInfo.setTotalRecords(0);
					}
				}else{
					responseInfo=GoForWealthSIPUtil.getSuccessResponseInfo(GoForWealthSIPErrorMessageEnum.ACCESS_DENIED_CODE.getValue(),GoForWealthSIPErrorMessageEnum.FORMAT_NOT_SUPPORTED.getValue(),null);
				}
			}
			else{
				responseInfo=GoForWealthSIPUtil.getSuccessResponseInfo(GoForWealthSIPErrorMessageEnum.ACCESS_DENIED_CODE.getValue(), GoForWealthSIPErrorMessageEnum.ACCESS_DENIED_MESSAGE.getValue(),null);
			}
		}else{
			responseInfo=GoForWealthSIPUtil.getSuccessResponseInfo(GoForWealthSIPErrorMessageEnum.ACCESS_DENIED_CODE.getValue(),GoForWealthSIPErrorMessageEnum.ACCESS_DENIED_MESSAGE.getValue(),null);
		}
		logger.info("Out uploadMandateFileImage()");
		return responseInfo;
	}


}
