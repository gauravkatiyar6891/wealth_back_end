package com.moptra.go4wealth.prs.web;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;
import java.util.ListIterator;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.codec.binary.Base64;
import org.apache.poi.EncryptedDocumentException;
import org.apache.poi.openxml4j.exceptions.InvalidFormatException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.moptra.go4wealth.admin.model.RedumptionResponse;
import com.moptra.go4wealth.admin.model.SchemeUploadRequest;
import com.moptra.go4wealth.bean.IndianIfscCodes;
import com.moptra.go4wealth.bean.NavMaster;
import com.moptra.go4wealth.bean.User;
import com.moptra.go4wealth.prs.common.enums.GoForWealthPRSErrorMessageEnum;
import com.moptra.go4wealth.prs.common.exception.GoForWealthPRSException;
import com.moptra.go4wealth.prs.common.rest.GoForWealthPRSResponseInfo;
import com.moptra.go4wealth.prs.common.util.GoForWealthPRSUtil;
import com.moptra.go4wealth.prs.model.AddToCartDTO;
import com.moptra.go4wealth.prs.model.FundSchemeDTO;
import com.moptra.go4wealth.prs.model.NavCalculateResponse;
import com.moptra.go4wealth.prs.model.NavDto;
import com.moptra.go4wealth.prs.model.NavUpdateRequest;
import com.moptra.go4wealth.prs.model.PrsDTO;
import com.moptra.go4wealth.prs.model.ResponseDto;
import com.moptra.go4wealth.prs.model.SearchSchemeRequest;
import com.moptra.go4wealth.prs.model.UnitAllocationResponse;
import com.moptra.go4wealth.prs.model.UserGoalResponse;
import com.moptra.go4wealth.prs.orderapi.request.PortFolioDataDTO;
import com.moptra.go4wealth.prs.service.GoForWealthFundSchemeService;
import com.moptra.go4wealth.repository.IndianIfscCodesRepository;
import com.moptra.go4wealth.repository.OrderUniqueRefRepository;
import com.moptra.go4wealth.repository.UserRepository;
import com.moptra.go4wealth.security.UserPrincipal;
import com.moptra.go4wealth.util.WebUtils;

@RestController
@RequestMapping("${server.contextPath}/prs/fund-scheme/")
public class GoForWealthFundSchemeController {

	private static Logger logger = LoggerFactory.getLogger(GoForWealthFundSchemeController.class);

	@Autowired
	GoForWealthFundSchemeService goForWealthFundSchemeService;

	@Autowired
	IndianIfscCodesRepository indianIfscCodesRepository;

	@Autowired
	UserRepository userRepository;

	@Autowired
	OrderUniqueRefRepository orderUniqueRefRepository;

	String errorResponse;


	/**
	 * @param navMasterTextFile
	 * @return GoForWealthPRSResponseInfo
	 */
	//************** unsecured url ********************************************//
	@RequestMapping(value = "/api/upload/navMaster", method = RequestMethod.POST)
	public GoForWealthPRSResponseInfo saveNavMasterTextFileDataToDb(@RequestParam("navMasterTextFile") MultipartFile navMaster) {
		logger.info("In saveNavMasterTextFileDataToDb()");
		if (WebUtils.userHasAuthority(WebUtils.ADMIN_ROLE_NAME)) {
			throw new AccessDeniedException(GoForWealthPRSErrorMessageEnum.ACCESS_DENIED_MESSAGE.getValue());
		}
		BufferedReader br;
		List<String> result = new ArrayList<>();
		List<NavMaster> navMasterList = new ArrayList<>();
		try {
			String line;
			InputStream is = navMaster.getInputStream();
			br = new BufferedReader(new InputStreamReader(is));
			while ((line = br.readLine()) != null) {
				result.add(line);
			}
			ListIterator<String> itr = result.listIterator(1);
			while (itr.hasNext()) {
				String str = itr.next();
				String[] arr = str.split("\\|");
				NavMaster navMasterRef = new NavMaster();
				navMasterRef.setNavDate(arr[0]);
				navMasterRef.setSchemeCode(arr[1]);
				navMasterRef.setSchemeName(arr[2]);
				navMasterRef.setRtaSchemeCode(arr[3]);
				navMasterRef.setDividendReinvestFlag(arr[4]);
				navMasterRef.setIsinCode(arr[5]);
				navMasterRef.setNavValue(arr[6]);
				navMasterRef.setRtaCode(arr[7]);
				navMasterList.add(navMasterRef);
			}
			boolean flag = goForWealthFundSchemeService.addNavMasterData(navMasterList);
			if (!flag) {
				return GoForWealthPRSUtil.getSuccessResponseInfo(GoForWealthPRSErrorMessageEnum.FAILURE_CODE.getValue(),GoForWealthPRSErrorMessageEnum.FAILURE_MESSAGE.getValue(),null);
			}
		} catch (IOException e) {
			return GoForWealthPRSUtil.getSuccessResponseInfo(GoForWealthPRSErrorMessageEnum.INTERNAL_SERVER_ERROR.getValue(),GoForWealthPRSErrorMessageEnum.INTERNAL_SERVER_ERROR.getValue(), null);
		}
		logger.info("Out saveNavMasterTextFileDataToDb()");
		return GoForWealthPRSUtil.getSuccessResponseInfo(GoForWealthPRSErrorMessageEnum.SUCCESS_CODE.getValue(),GoForWealthPRSErrorMessageEnum.SUCCESS_MESSAGE.getValue(),null);
	}

	//********************************** unsecured url ******************************************//
	@CrossOrigin
	@RequestMapping(value = "api/upload/saveExcelDataToDbUsingBase64", method = RequestMethod.POST)
	public GoForWealthPRSResponseInfo saveExcelDataToDbUsingBase64(@RequestParam("userExcelUpload") MultipartFile userExcelUpload) throws IOException, EncryptedDocumentException, InvalidFormatException {
		logger.info("IN saveNavMasterTextFileDataToDb()");
		boolean result = goForWealthFundSchemeService.saveBankIfscCodes(userExcelUpload);
		return GoForWealthPRSUtil.getSuccessResponseInfo(GoForWealthPRSErrorMessageEnum.SUCCESS_CODE.getValue(),GoForWealthPRSErrorMessageEnum.SUCCESS_MESSAGE.getValue(), result);
	}

	//*********** unsecured url *******************************************//
	@RequestMapping(value = "/api/getAllScheme", method = RequestMethod.POST)
	public GoForWealthPRSResponseInfo getAllScheme(@RequestParam("schemeType")String schemeType, @RequestParam("offset") int offset) {
		logger.info("In getAllScheme(offset)");
		GoForWealthPRSResponseInfo responseInfo = null;
		List<FundSchemeDTO> fundSchemeDTOList = goForWealthFundSchemeService.getAllScheme(schemeType,offset);
		if (fundSchemeDTOList != null && fundSchemeDTOList.size() > 0) {
			responseInfo = GoForWealthPRSUtil.getSuccessResponseInfo(GoForWealthPRSErrorMessageEnum.SUCCESS_CODE.getValue(),GoForWealthPRSErrorMessageEnum.SUCCESS_MESSAGE.getValue(),fundSchemeDTOList);
			responseInfo.setTotalRecords(fundSchemeDTOList.size());
		} else {
			responseInfo = new GoForWealthPRSResponseInfo();
			responseInfo.setStatus(GoForWealthPRSErrorMessageEnum.DATA_NOT_FOUND_CODE.getValue());
			responseInfo.setMessage(GoForWealthPRSErrorMessageEnum.DATA_NOT_FOUND_MESSAGE.getValue());
		}
		logger.info("Out getAllScheme()");
		return responseInfo;
	}

	//*************************** unsecured url ***************************************//
	@RequestMapping(value = "/api/getAllSchemeAuth/{offset}", method = RequestMethod.GET)
	public GoForWealthPRSResponseInfo getAllSchemeAuth(@PathVariable("offset") int offset) {
		logger.info("In getAllScheme(offset)");
		GoForWealthPRSResponseInfo responseInfo;
		List<FundSchemeDTO> fundSchemeDTOList = goForWealthFundSchemeService.getAllSchemeWithAuth(offset);
		if (fundSchemeDTOList != null && fundSchemeDTOList.size() > 0) {
			responseInfo = GoForWealthPRSUtil.getSuccessResponseInfo(GoForWealthPRSErrorMessageEnum.SUCCESS_CODE.getValue(),GoForWealthPRSErrorMessageEnum.SUCCESS_MESSAGE.getValue(), fundSchemeDTOList);
			responseInfo.setTotalRecords(fundSchemeDTOList.size());
		} else {
			responseInfo = new GoForWealthPRSResponseInfo();
			responseInfo.setStatus(GoForWealthPRSErrorMessageEnum.DATA_NOT_FOUND_CODE.getValue());
			responseInfo.setMessage(GoForWealthPRSErrorMessageEnum.DATA_NOT_FOUND_MESSAGE.getValue());
		}
		logger.info("Out getAllScheme()");
		return responseInfo;
	}

	/**
	 * 
	 * @param textFile
	 * @return GoForWealthSIPResponseInfo
	*/
	//*************** unsecured url *****************************************//
	@RequestMapping(value = "/api/getAllTopScheme", method = RequestMethod.GET)
	public GoForWealthPRSResponseInfo getAllTopScheme() {
		logger.info("In getAllTopScheme()");
		GoForWealthPRSResponseInfo responseInfo;
		List<FundSchemeDTO> fundSchemeDTOList = goForWealthFundSchemeService.getAllTopScheme();
		if (fundSchemeDTOList != null && fundSchemeDTOList.size() > 0) {
			responseInfo = GoForWealthPRSUtil.getSuccessResponseInfo(GoForWealthPRSErrorMessageEnum.SUCCESS_CODE.getValue(),GoForWealthPRSErrorMessageEnum.SUCCESS_MESSAGE.getValue(), fundSchemeDTOList);
			responseInfo.setTotalRecords(fundSchemeDTOList.size());
		} else {
			responseInfo = new GoForWealthPRSResponseInfo();
			responseInfo.setStatus(GoForWealthPRSErrorMessageEnum.DATA_NOT_FOUND_CODE.getValue());
			responseInfo.setMessage(GoForWealthPRSErrorMessageEnum.DATA_NOT_FOUND_MESSAGE.getValue());
		}
		logger.info("Out getAllTopScheme()");
		return responseInfo;
	}

	/**
	 * 
	 * @param schemeCode
	 * @return GoForWealthPRSResponseInfo
	 */
	/*
	@RequestMapping(value="/api/getFundSchemeDetail/{schemeCode}",method=RequestMethod.GET) 
	public GoForWealthPRSResponseInfo getFundSchemeDetailBySchemeCode(@PathVariable("schemeCode")String schemeCode) { 
		logger.info("In getFundSchemeDetailBySchemeCode()");
	 	GoForWealthPRSResponseInfo responseInfo; FundSchemeDTO fundSchemeDTO =
	 	goForWealthFundSchemeService.getFundSchemeDetailBySchemeCode(schemeCode);
	 	if(fundSchemeDTO != null) { responseInfo = GoForWealthPRSUtil.getSuccessResponseInfo(GoForWealthPRSErrorMessageEnum.SUCCESS_CODE.getValue(),GoForWealthPRSErrorMessageEnum.SUCCESS_MESSAGE.getValue(),fundSchemeDTO); 
	 	} else { 
	 		responseInfo = oForWealthPRSUtil.getErrorResponseInfo(GoForWealthPRSErrorMessageEnum.DATA_NOT_FOUND_CODE.getValue(),GoForWealthPRSErrorMessageEnum.DATA_NOT_FOUND_MESSAGE.getValue(),null); 
	 	}
	 	logger.info("Out getFundSchemeDetailBySchemeCode()"); 
	 	return responseInfo; 
	}
	*/


	//******** unsecured url **************//
	@GetMapping(value = "/api/allSchemeType")
	public GoForWealthPRSResponseInfo getAllSchemeType() {
		logger.info("In getAllSchemeType()");
		GoForWealthPRSResponseInfo responseInfo;
		ArrayList<String> schemeTypeList = (ArrayList<String>) goForWealthFundSchemeService.getAllSchemeType();
		if (schemeTypeList != null && schemeTypeList.size() > 0) {
			responseInfo = GoForWealthPRSUtil.getSuccessResponseInfo(GoForWealthPRSErrorMessageEnum.SUCCESS_CODE.getValue(),GoForWealthPRSErrorMessageEnum.SUCCESS_MESSAGE.getValue(), schemeTypeList);
			responseInfo.setTotalRecords(schemeTypeList.size());
		} else {
			responseInfo = new GoForWealthPRSResponseInfo();
			responseInfo.setStatus(GoForWealthPRSErrorMessageEnum.DATA_NOT_FOUND_CODE.getValue());
			responseInfo.setMessage(GoForWealthPRSErrorMessageEnum.DATA_NOT_FOUND_MESSAGE.getValue());
		}
		logger.info("Out getAllSchemeType()");
		return responseInfo;
	}

	//********* unsecured url *************//
	@PostMapping(value = "/api/searchScheme")
	public GoForWealthPRSResponseInfo searchScheme(@RequestBody SearchSchemeRequest searchSchemeRequest) {
		logger.info("In searchScheme()");
		GoForWealthPRSResponseInfo responseInfo;
		List<FundSchemeDTO> fundSchemeDTOList = goForWealthFundSchemeService.searchScheme(searchSchemeRequest);
		if (fundSchemeDTOList != null && fundSchemeDTOList.size() > 0) {
			responseInfo = GoForWealthPRSUtil.getSuccessResponseInfo(GoForWealthPRSErrorMessageEnum.SUCCESS_CODE.getValue(),GoForWealthPRSErrorMessageEnum.SUCCESS_MESSAGE.getValue(), fundSchemeDTOList);
			responseInfo.setTotalRecords(fundSchemeDTOList.size());
		} else {
			responseInfo = new GoForWealthPRSResponseInfo();
			responseInfo.setStatus(GoForWealthPRSErrorMessageEnum.DATA_NOT_FOUND_CODE.getValue());
			responseInfo.setMessage(GoForWealthPRSErrorMessageEnum.DATA_NOT_FOUND_MESSAGE.getValue());
		}
		logger.info("Out searchScheme()");
		return responseInfo;
	}

	//******** unsecured url ******************//
	@PostMapping(value = "/api/searchSchemeAuth")
	public GoForWealthPRSResponseInfo searchSchemeAuth(@RequestBody SearchSchemeRequest searchSchemeRequest) {
		logger.info("In searchScheme()");
		GoForWealthPRSResponseInfo responseInfo;
		List<FundSchemeDTO> fundSchemeDTOList = goForWealthFundSchemeService.searchScheme(searchSchemeRequest);
		if (fundSchemeDTOList != null && fundSchemeDTOList.size() > 0) {
			responseInfo = GoForWealthPRSUtil.getSuccessResponseInfo(GoForWealthPRSErrorMessageEnum.SUCCESS_CODE.getValue(),GoForWealthPRSErrorMessageEnum.SUCCESS_MESSAGE.getValue(),fundSchemeDTOList);
			responseInfo.setTotalRecords(fundSchemeDTOList.size());
		} else {
			responseInfo = new GoForWealthPRSResponseInfo();
			responseInfo.setStatus(GoForWealthPRSErrorMessageEnum.DATA_NOT_FOUND_CODE.getValue());
			responseInfo.setMessage(GoForWealthPRSErrorMessageEnum.DATA_NOT_FOUND_MESSAGE.getValue());
		}
		logger.info("Out searchScheme()");
		return responseInfo;
	}

	
	/**
	 * getSchemeDetails() 
	 * @param schemeCode
	 * @return
	*/
	//********************************** unsecured url ***********************************//
	@RequestMapping(value = "/api/getSchemeDetails/{schemeCode}", method = RequestMethod.GET)
	public GoForWealthPRSResponseInfo getSchemeDetails(@PathVariable("schemeCode") String schemeCode) {
		logger.info("In getSchemeDetails()");
		GoForWealthPRSResponseInfo responseInfo;
		FundSchemeDTO fundSchemeDTO = goForWealthFundSchemeService.getSchemeDetails(schemeCode);
		if (fundSchemeDTO != null) {
			responseInfo = GoForWealthPRSUtil.getSuccessResponseInfo(GoForWealthPRSErrorMessageEnum.SUCCESS_CODE.getValue(),GoForWealthPRSErrorMessageEnum.SUCCESS_MESSAGE.getValue(),fundSchemeDTO);
		} else {
			responseInfo = new GoForWealthPRSResponseInfo();
			responseInfo.setStatus(GoForWealthPRSErrorMessageEnum.DATA_NOT_FOUND_CODE.getValue());
			responseInfo.setMessage(GoForWealthPRSErrorMessageEnum.DATA_NOT_FOUND_MESSAGE.getValue());
		}
		logger.info("Out getSchemeDetails()");
		return responseInfo;
	}

	//*************** secured url **************************************//
	@RequestMapping(value = "/api/addToCart", method = RequestMethod.POST)
	public GoForWealthPRSResponseInfo addToCart(@RequestBody AddToCartDTO addToCartDTO, Authentication auth) {
		logger.info("In addToCart()");
		UserPrincipal userSession = (UserPrincipal) auth.getPrincipal();
		GoForWealthPRSResponseInfo responseInfo = null;
		if (userSession != null) {
			logger.info("In addToCart() wiht UserId : " + userSession.getUser().getUserId());
			User user = userRepository.findUserByRole(userSession.getUser().getUserId(), "USER");
			if (user != null) {
				addToCartDTO.setUserId(userSession.getUser().getUserId());
				addToCartDTO = goForWealthFundSchemeService.addToCart(addToCartDTO);
				responseInfo = GoForWealthPRSUtil.getSuccessResponseInfo(GoForWealthPRSErrorMessageEnum.SUCCESS_CODE.getValue(),GoForWealthPRSErrorMessageEnum.SUCCESS_MESSAGE.getValue(),addToCartDTO);
			} else {
				responseInfo = GoForWealthPRSUtil.getSuccessResponseInfo(GoForWealthPRSErrorMessageEnum.ACCESS_DENIED_CODE.getValue(),GoForWealthPRSErrorMessageEnum.ACCESS_DENIED_MESSAGE.getValue(),null);
			}
		} else {
			responseInfo = GoForWealthPRSUtil.getSuccessResponseInfo(GoForWealthPRSErrorMessageEnum.ACCESS_DENIED_CODE.getValue(),GoForWealthPRSErrorMessageEnum.ACCESS_DENIED_MESSAGE.getValue(),null);
		}
		logger.info("Out addToCart()");
		return responseInfo;
	}

	//***************** secured url *************************************//
	@RequestMapping(value = "/api/getCartOrder", method = RequestMethod.GET)
	public GoForWealthPRSResponseInfo getCartOrder(@RequestParam("type") String type, Authentication auth) {
		logger.info("In getCartOrder()");
		UserPrincipal userSession = (UserPrincipal) auth.getPrincipal();
		GoForWealthPRSResponseInfo responseInfo = null;
		List<AddToCartDTO> cartOrderList = new ArrayList<>();
		if (userSession != null) {
			logger.info("In getCartOrder() with UserId : " + userSession.getUser().getUserId());
			User user = userRepository.findUserByRole(userSession.getUser().getUserId(), "USER");
			if (user != null) {
				cartOrderList = goForWealthFundSchemeService.getCartOrder(userSession.getUser().getUserId(), type);
				responseInfo = GoForWealthPRSUtil.getSuccessResponseInfo(GoForWealthPRSErrorMessageEnum.SUCCESS_CODE.getValue(),GoForWealthPRSErrorMessageEnum.SUCCESS_MESSAGE.getValue(), cartOrderList);
			} else {
				responseInfo = GoForWealthPRSUtil.getSuccessResponseInfo(GoForWealthPRSErrorMessageEnum.ACCESS_DENIED_CODE.getValue(),GoForWealthPRSErrorMessageEnum.ACCESS_DENIED_MESSAGE.getValue(),null);
			}
		} else {
			responseInfo = GoForWealthPRSUtil.getSuccessResponseInfo(GoForWealthPRSErrorMessageEnum.ACCESS_DENIED_CODE.getValue(),GoForWealthPRSErrorMessageEnum.ACCESS_DENIED_MESSAGE.getValue(),null);
		}
		logger.info("Out getCartOrder()");
		return responseInfo;
	}

	/**
	 * 
	 * @param orderId
	 * @param auth
	 * @return
	*/
	//********************************** secured url ****************************//
	@RequestMapping(value = "/api/getCartOrderByOrder", method = RequestMethod.GET)
	public GoForWealthPRSResponseInfo getCartOrderByOrder(@RequestParam("orderId") Integer orderId,Authentication auth) {
		logger.info("In getCartOrderByOrder()");
		UserPrincipal userSession = (UserPrincipal) auth.getPrincipal();
		GoForWealthPRSResponseInfo responseInfo = null;
		AddToCartDTO addToCartDTO = new AddToCartDTO();
		if (userSession != null) {
			logger.info("In getCartOrderByOrder() with UserId : " + userSession.getUser().getUserId());
			User user = userRepository.findUserByRole(userSession.getUser().getUserId(), "USER");
			if (user != null) {
				addToCartDTO = goForWealthFundSchemeService.getCartOrderByOrder(userSession.getUser().getUserId(),orderId);
				responseInfo = GoForWealthPRSUtil.getSuccessResponseInfo(GoForWealthPRSErrorMessageEnum.SUCCESS_CODE.getValue(),GoForWealthPRSErrorMessageEnum.SUCCESS_MESSAGE.getValue(),addToCartDTO);
			} else {
				responseInfo = GoForWealthPRSUtil.getSuccessResponseInfo(GoForWealthPRSErrorMessageEnum.ACCESS_DENIED_CODE.getValue(),GoForWealthPRSErrorMessageEnum.ACCESS_DENIED_MESSAGE.getValue(),null);
			}
		} else {
			responseInfo = GoForWealthPRSUtil.getSuccessResponseInfo(GoForWealthPRSErrorMessageEnum.ACCESS_DENIED_CODE.getValue(),GoForWealthPRSErrorMessageEnum.ACCESS_DENIED_MESSAGE.getValue(), null);
		}
		logger.info("Out getCartOrderByOrder()");
		return responseInfo;
	}

	//********************************** secured url **********************//
	@RequestMapping(value = "/api/confirmOrder", method = RequestMethod.POST)
	public GoForWealthPRSResponseInfo confirmOrder(@RequestParam("orderId") Integer orderId, Authentication auth) {
		logger.info("In confirmOrder()");
		UserPrincipal userSession = (UserPrincipal) auth.getPrincipal();
		String response = "Failure";
		GoForWealthPRSResponseInfo responseInfo = null;
		if (userSession != null) {
			logger.info("In confirmOrder() with UserId : " + userSession.getUser().getUserId());
			User user = userRepository.findUserByRole(userSession.getUser().getUserId(), "USER");
			if (user != null) {
				response = goForWealthFundSchemeService.confirmOrder(userSession.getUser().getUserId(), orderId);
				System.out.print("Response : " + response);
				if(response.contains("DUPLICAT UNIQUE REF NO")){
					responseInfo = GoForWealthPRSUtil.getSuccessResponseInfo(GoForWealthPRSErrorMessageEnum.FAILURE_CODE.getValue(),GoForWealthPRSErrorMessageEnum.FAILURE_MESSAGE.getValue(),response);
				}else if(response.contains("FAILED")){
					responseInfo = GoForWealthPRSUtil.getSuccessResponseInfo(GoForWealthPRSErrorMessageEnum.FAILURE_CODE.getValue(),GoForWealthPRSErrorMessageEnum.FAILURE_MESSAGE.getValue(),response);
				}else{
					responseInfo = GoForWealthPRSUtil.getSuccessResponseInfo(GoForWealthPRSErrorMessageEnum.SUCCESS_CODE.getValue(),GoForWealthPRSErrorMessageEnum.SUCCESS_MESSAGE.getValue(),response);
				}
				/*
				if(!response.equals("FAILED: DUPLICAT UNIQUE REF NO.")){
					responseInfo = GoForWealthPRSUtil.getSuccessResponseInfo(GoForWealthPRSErrorMessageEnum.SUCCESS_CODE.getValue(),GoForWealthPRSErrorMessageEnum.SUCCESS_MESSAGE.getValue(),response);
				}else{ 
					confirmOrder(orderId, auth); 
				}
				*/
			} else {
				responseInfo = GoForWealthPRSUtil.getSuccessResponseInfo(GoForWealthPRSErrorMessageEnum.ACCESS_DENIED_CODE.getValue(),GoForWealthPRSErrorMessageEnum.ACCESS_DENIED_MESSAGE.getValue(),null);
			}
		} else {
			responseInfo = GoForWealthPRSUtil.getSuccessResponseInfo(GoForWealthPRSErrorMessageEnum.ACCESS_DENIED_CODE.getValue(),GoForWealthPRSErrorMessageEnum.ACCESS_DENIED_MESSAGE.getValue(), null);
		}
		logger.info("Out confirmOrder()");
		return responseInfo;
	}

	//********************************** secured url ****************************************//
	@RequestMapping(value = "/api/getSchemeDetailsById/{schemeId}", method = RequestMethod.GET)
	public GoForWealthPRSResponseInfo getSchemeDetailsById(@PathVariable("schemeId") Integer schemeId,Authentication auth) {
		logger.info("In getSchemeDetailsById()");
		UserPrincipal userSession = (UserPrincipal) auth.getPrincipal();
		GoForWealthPRSResponseInfo responseInfo = null;
		if (userSession != null) {
			logger.info("In getSchemeDetailsById() with UserId : " + userSession.getUser().getUserId());
			User user = userRepository.findUserByRole(userSession.getUser().getUserId(), "USER");
			if (user != null) {
				FundSchemeDTO fundSchemeDTO = goForWealthFundSchemeService.getSchemeDetailsById(schemeId, user);
				if (fundSchemeDTO != null) {
					responseInfo = GoForWealthPRSUtil.getSuccessResponseInfo(GoForWealthPRSErrorMessageEnum.SUCCESS_CODE.getValue(),GoForWealthPRSErrorMessageEnum.SUCCESS_MESSAGE.getValue(), fundSchemeDTO);
				} else {
					responseInfo = new GoForWealthPRSResponseInfo();
					responseInfo.setStatus(GoForWealthPRSErrorMessageEnum.DATA_NOT_FOUND_CODE.getValue());
					responseInfo.setMessage(GoForWealthPRSErrorMessageEnum.DATA_NOT_FOUND_MESSAGE.getValue());
				}
			} else {
				responseInfo = GoForWealthPRSUtil.getSuccessResponseInfo(GoForWealthPRSErrorMessageEnum.ACCESS_DENIED_CODE.getValue(),GoForWealthPRSErrorMessageEnum.ACCESS_DENIED_MESSAGE.getValue(), null);
			}
		} else {
			responseInfo = GoForWealthPRSUtil.getSuccessResponseInfo(GoForWealthPRSErrorMessageEnum.ACCESS_DENIED_CODE.getValue(),GoForWealthPRSErrorMessageEnum.ACCESS_DENIED_MESSAGE.getValue(),null);
		}
		logger.info("Out getSchemeDetailsById()");
		return responseInfo;
	}

	//********************************** secured url ********************************************//
	@RequestMapping(value = "/api/getSchemeDetailsByCode/{schemeCode}", method = RequestMethod.GET)
	public GoForWealthPRSResponseInfo getSchemeDetailsByCode(@PathVariable("schemeCode") String schemeCode,Authentication auth) {
		logger.info("In getSchemeDetailsByCode()");
		UserPrincipal userSession = (UserPrincipal) auth.getPrincipal();
		GoForWealthPRSResponseInfo responseInfo = null;
		if (userSession != null) {
			logger.info("In getSchemeDetailsByCode() with UserId :  " + userSession.getUser().getUserId());
			User user = userRepository.findUserByRole(userSession.getUser().getUserId(),"USER");
			if (user != null) {
				FundSchemeDTO fundSchemeDTO = goForWealthFundSchemeService.getSchemeDetailsByCode(schemeCode);
				if (fundSchemeDTO != null) {
					responseInfo = GoForWealthPRSUtil.getSuccessResponseInfo(GoForWealthPRSErrorMessageEnum.SUCCESS_CODE.getValue(),GoForWealthPRSErrorMessageEnum.SUCCESS_MESSAGE.getValue(),fundSchemeDTO);
				} else {
					responseInfo = new GoForWealthPRSResponseInfo();
					responseInfo.setStatus(GoForWealthPRSErrorMessageEnum.DATA_NOT_FOUND_CODE.getValue());
					responseInfo.setMessage(GoForWealthPRSErrorMessageEnum.DATA_NOT_FOUND_MESSAGE.getValue());
				}
			} else {
				responseInfo = GoForWealthPRSUtil.getSuccessResponseInfo(GoForWealthPRSErrorMessageEnum.ACCESS_DENIED_CODE.getValue(),GoForWealthPRSErrorMessageEnum.ACCESS_DENIED_MESSAGE.getValue(),null);
			}
		} else {
			responseInfo = GoForWealthPRSUtil.getSuccessResponseInfo(GoForWealthPRSErrorMessageEnum.ACCESS_DENIED_CODE.getValue(),GoForWealthPRSErrorMessageEnum.ACCESS_DENIED_MESSAGE.getValue(),null);
		}
		logger.info("Out getSchemeDetailsById()");
		return responseInfo;
	}

	//********************* secured url **********************************//
	@RequestMapping(value = "/api/cancelOrder", method = RequestMethod.POST)
	public GoForWealthPRSResponseInfo cancelOrder(@RequestParam("orderId") Integer orderId, Authentication auth) {
		logger.info("In cancelOrder()");
		UserPrincipal userSession = (UserPrincipal) auth.getPrincipal();
		GoForWealthPRSResponseInfo responseInfo = null;
		if (userSession != null) {
			logger.info("In cancelOrder() with UserId : " + userSession.getUser().getUserId());
			User user = userRepository.findUserByRole(userSession.getUser().getUserId(), "USER");
			if (user != null) {
				responseInfo = goForWealthFundSchemeService.cancelOrder(orderId, userSession.getUser().getUserId());
			} else {
				responseInfo = GoForWealthPRSUtil.getSuccessResponseInfo(GoForWealthPRSErrorMessageEnum.ACCESS_DENIED_CODE.getValue(),GoForWealthPRSErrorMessageEnum.ACCESS_DENIED_MESSAGE.getValue(),null);
			}
		} else {
			responseInfo = GoForWealthPRSUtil.getSuccessResponseInfo(GoForWealthPRSErrorMessageEnum.ACCESS_DENIED_CODE.getValue(),GoForWealthPRSErrorMessageEnum.ACCESS_DENIED_MESSAGE.getValue(),null);
		}
		logger.info("Out cancelOrder()");
		return responseInfo;
	}

	//******************** secured url *******************************//
	@RequestMapping(value = "/api/payment", method = RequestMethod.POST)
	public GoForWealthPRSResponseInfo makePayment(@RequestBody List<AddToCartDTO> addToCartDTO/*@RequestParam("orderId") Integer orderId,@RequestParam("amount") String amount*/, Authentication auth) throws GoForWealthPRSException {
		logger.info("In makePayment()");
		UserPrincipal userSession = (UserPrincipal) auth.getPrincipal();
		String response = "Failure";
		GoForWealthPRSResponseInfo responseInfo = null;
		if (userSession != null) {
			logger.info("In makePayment() with UserId :  " + userSession.getUser().getUserId());
			User user = userRepository.findUserByRole(userSession.getUser().getUserId(), "USER");
			if (user != null) {
				response = goForWealthFundSchemeService.makePayment(addToCartDTO, userSession.getUser().getUserId());
				responseInfo = GoForWealthPRSUtil.getSuccessResponseInfo(GoForWealthPRSErrorMessageEnum.SUCCESS_CODE.getValue(),GoForWealthPRSErrorMessageEnum.SUCCESS_MESSAGE.getValue(),response);
			} else {
				responseInfo = GoForWealthPRSUtil.getSuccessResponseInfo(GoForWealthPRSErrorMessageEnum.ACCESS_DENIED_CODE.getValue(),GoForWealthPRSErrorMessageEnum.ACCESS_DENIED_MESSAGE.getValue(),null);
			}
		} else {
			responseInfo = GoForWealthPRSUtil.getSuccessResponseInfo(GoForWealthPRSErrorMessageEnum.ACCESS_DENIED_CODE.getValue(),GoForWealthPRSErrorMessageEnum.ACCESS_DENIED_MESSAGE.getValue(), null);
		}
		logger.info("Out makePayment()");
		return responseInfo;
	}

	//********************** secured url ***********************************//
	@RequestMapping(value = "/api/getTransactions/{schemeCode}", method = RequestMethod.GET)
	public GoForWealthPRSResponseInfo getOrderTransaction(@RequestParam("folioNo") String folioNo,@PathVariable("schemeCode") String schemeCode, Authentication auth) {
		logger.info("In getOrderTransaction()");
		UserPrincipal userSession = (UserPrincipal) auth.getPrincipal();
		GoForWealthPRSResponseInfo responseInfo;
		List<AddToCartDTO> cartOrderList = new ArrayList<>();
		if (userSession != null) {
			logger.info("In getOrderTransaction() with UserId :  " + userSession.getUser().getUserId());
			User user = userRepository.findUserByRole(userSession.getUser().getUserId(), "USER");
			if (user != null) {
				cartOrderList = goForWealthFundSchemeService.getOrderTransaction(userSession.getUser().getUserId(),folioNo,schemeCode);
				responseInfo = GoForWealthPRSUtil.getSuccessResponseInfo(GoForWealthPRSErrorMessageEnum.SUCCESS_CODE.getValue(),GoForWealthPRSErrorMessageEnum.SUCCESS_MESSAGE.getValue(), cartOrderList);
			} else {
				responseInfo = GoForWealthPRSUtil.getSuccessResponseInfo(GoForWealthPRSErrorMessageEnum.ACCESS_DENIED_CODE.getValue(),GoForWealthPRSErrorMessageEnum.ACCESS_DENIED_MESSAGE.getValue(),null);
			}
		} else {
			responseInfo = GoForWealthPRSUtil.getSuccessResponseInfo(GoForWealthPRSErrorMessageEnum.ACCESS_DENIED_CODE.getValue(),GoForWealthPRSErrorMessageEnum.ACCESS_DENIED_MESSAGE.getValue(),null);
		}
		logger.info("Out getOrderTransaction()");
		return responseInfo;
	}

	//******************** secured url ***********************************//
	/*@RequestMapping(value = "/api/getPortfolio1", method = RequestMethod.GET)
	public GoForWealthPRSResponseInfo getPortfolio(Authentication auth) {
		logger.info("In getPortfolio()");
		UserPrincipal userSession = (UserPrincipal) auth.getPrincipal();
		GoForWealthPRSResponseInfo responseInfo;
		List<PortFolioDataDTO> portFolioDataDTO = new ArrayList<>();
		if (userSession != null) {
			logger.info("In getPortfolio() with UserId : " + userSession.getUser().getUserId());
			User user = userRepository.findUserByRole(userSession.getUser().getUserId(), "USER");
			if (user != null) {
				portFolioDataDTO = goForWealthFundSchemeService.getPortfolio(userSession.getUser().getUserId());
				responseInfo = GoForWealthPRSUtil.getSuccessResponseInfo(GoForWealthPRSErrorMessageEnum.SUCCESS_CODE.getValue(),GoForWealthPRSErrorMessageEnum.SUCCESS_MESSAGE.getValue(),portFolioDataDTO);
			} else {
				responseInfo = GoForWealthPRSUtil.getSuccessResponseInfo(GoForWealthPRSErrorMessageEnum.ACCESS_DENIED_CODE.getValue(),GoForWealthPRSErrorMessageEnum.ACCESS_DENIED_MESSAGE.getValue(),null);
			}
		} else {
			responseInfo = GoForWealthPRSUtil.getSuccessResponseInfo(GoForWealthPRSErrorMessageEnum.ACCESS_DENIED_CODE.getValue(),GoForWealthPRSErrorMessageEnum.ACCESS_DENIED_MESSAGE.getValue(),null);
		}
		logger.info("Out getPortfolio()");
		return responseInfo;
	}*/

	//************************************* secured url ***********************************//
	/*@RequestMapping(value = "/api/getPortFolioDetails/{orderId}", method = RequestMethod.GET)
	public GoForWealthPRSResponseInfo getPortFolioDetails(@PathVariable("orderId") Integer orderId,Authentication auth) {
		logger.info("In getPortFolioDetails()");
		UserPrincipal userSession = (UserPrincipal) auth.getPrincipal();
		GoForWealthPRSResponseInfo responseInfo;
		AddToCartDTO portFolioDetails = null;
		if (userSession != null) {
			logger.info("In getPortFolioDetails() with UserId : " + userSession.getUser().getUserId());
			User user = userRepository.findUserByRole(userSession.getUser().getUserId(), "USER");
			if (user != null) {
				portFolioDetails = goForWealthFundSchemeService.getPortfolioDetails(orderId);
				if (portFolioDetails != null) {
					responseInfo = GoForWealthPRSUtil.getSuccessResponseInfo(GoForWealthPRSErrorMessageEnum.SUCCESS_CODE.getValue(),GoForWealthPRSErrorMessageEnum.SUCCESS_MESSAGE.getValue(), portFolioDetails);
				} else {
					responseInfo = new GoForWealthPRSResponseInfo();
					responseInfo.setStatus(GoForWealthPRSErrorMessageEnum.DATA_NOT_FOUND_CODE.getValue());
					responseInfo.setMessage(GoForWealthPRSErrorMessageEnum.DATA_NOT_FOUND_MESSAGE.getValue());
				}
			} else {
				responseInfo = GoForWealthPRSUtil.getSuccessResponseInfo(GoForWealthPRSErrorMessageEnum.ACCESS_DENIED_CODE.getValue(),GoForWealthPRSErrorMessageEnum.ACCESS_DENIED_MESSAGE.getValue(), null);
			}
		} else {
			responseInfo = GoForWealthPRSUtil.getSuccessResponseInfo(GoForWealthPRSErrorMessageEnum.ACCESS_DENIED_CODE.getValue(),GoForWealthPRSErrorMessageEnum.ACCESS_DENIED_MESSAGE.getValue(), null);
		}
		logger.info("Out getPortFolioDetails()");
		return responseInfo;
	}*/

	//********************************** secured url ***************************************************//
	@RequestMapping(value = "/api/getSchemeDetailBySchemeCode/{toSchemeCode}", method = RequestMethod.GET)
	public GoForWealthPRSResponseInfo getSchemeDetailBySchemeCode(@PathVariable("toSchemeCode") String toSchemeCode,Authentication auth) {
		logger.info("In getSchemeDetailBySchemeCode()");
		UserPrincipal userSession = (UserPrincipal) auth.getPrincipal();
		GoForWealthPRSResponseInfo responseInfo = null;
		FundSchemeDTO schemeDataToSwitch = null;
		if (userSession != null) {
			logger.info("In getSchemeDetailBySchemeCode() with UserId : " + userSession.getUser().getUserId());
			User user = userRepository.findUserByRole(userSession.getUser().getUserId(), "USER");
			if (user != null) {
				schemeDataToSwitch = goForWealthFundSchemeService.getSchemeDetailBySchemeCode(toSchemeCode);
				if (schemeDataToSwitch != null) {
					responseInfo = GoForWealthPRSUtil.getSuccessResponseInfo(GoForWealthPRSErrorMessageEnum.SUCCESS_CODE.getValue(),GoForWealthPRSErrorMessageEnum.SUCCESS_MESSAGE.getValue(),schemeDataToSwitch);
				} else {
					responseInfo = new GoForWealthPRSResponseInfo();
					responseInfo.setStatus(GoForWealthPRSErrorMessageEnum.DATA_NOT_FOUND_CODE.getValue());
					responseInfo.setMessage(GoForWealthPRSErrorMessageEnum.DATA_NOT_FOUND_MESSAGE.getValue());
				}
			} else {
				responseInfo = GoForWealthPRSUtil.getSuccessResponseInfo(GoForWealthPRSErrorMessageEnum.ACCESS_DENIED_CODE.getValue(),GoForWealthPRSErrorMessageEnum.ACCESS_DENIED_MESSAGE.getValue(),null);
			}
		} else {
			responseInfo = GoForWealthPRSUtil.getSuccessResponseInfo(GoForWealthPRSErrorMessageEnum.ACCESS_DENIED_CODE.getValue(),GoForWealthPRSErrorMessageEnum.ACCESS_DENIED_MESSAGE.getValue(),null);
		}
		logger.info("Out getSchemeDetailBySchemeCode()");
		return responseInfo;
	}

	//********************************** secured url ****************//
	/*@RequestMapping(value = "/api/redeem1", method = RequestMethod.POST)
	public GoForWealthPRSResponseInfo redeemOrder(@RequestParam("orderId") Integer orderId,@RequestParam("amount") String amount, @RequestParam("type") String type ,Authentication auth) {
		logger.info("In redeemOrder()");
		UserPrincipal userSession = (UserPrincipal) auth.getPrincipal();
		String response = "Failure";
		GoForWealthPRSResponseInfo responseInfo = null;
		if (userSession != null) {
			logger.info("In redeemOrder() with UserId : " + userSession.getUser().getUserId());
			User user = userRepository.findUserByRole(userSession.getUser().getUserId(), "USER");
			if (user != null) {
				response = goForWealthFundSchemeService.redeemOrder(orderId, userSession.getUser().getUserId(), amount,type);
				responseInfo = GoForWealthPRSUtil.getSuccessResponseInfo(GoForWealthPRSErrorMessageEnum.SUCCESS_CODE.getValue(),GoForWealthPRSErrorMessageEnum.SUCCESS_MESSAGE.getValue(),response);
			} else {
				responseInfo = GoForWealthPRSUtil.getSuccessResponseInfo(GoForWealthPRSErrorMessageEnum.ACCESS_DENIED_CODE.getValue(),GoForWealthPRSErrorMessageEnum.ACCESS_DENIED_MESSAGE.getValue(),null);
			}
		} else {
			responseInfo = GoForWealthPRSUtil.getSuccessResponseInfo(GoForWealthPRSErrorMessageEnum.ACCESS_DENIED_CODE.getValue(),GoForWealthPRSErrorMessageEnum.ACCESS_DENIED_MESSAGE.getValue(),null);
		}
		logger.info("Out redeemOrder()");
		return responseInfo;
	}*/

	//************************** secured url ************************//
	@RequestMapping(value = "/api/switch", method = RequestMethod.POST)
	public GoForWealthPRSResponseInfo switchScheme(@RequestParam("orderId") Integer orderId,@RequestParam("schemeCode") String schemeCode, Authentication auth) {
		logger.info("In switchScheme()");
		UserPrincipal userSession = (UserPrincipal) auth.getPrincipal();
		String response = "Failure";
		GoForWealthPRSResponseInfo responseInfo = null;
		if (userSession != null) {
			User user = userRepository.findUserByRole(userSession.getUser().getUserId(), "USER");
			if (user != null) {
				response = goForWealthFundSchemeService.switchScheme(orderId, schemeCode,userSession.getUser().getUserId());
				responseInfo = GoForWealthPRSUtil.getSuccessResponseInfo(GoForWealthPRSErrorMessageEnum.SUCCESS_CODE.getValue(),GoForWealthPRSErrorMessageEnum.SUCCESS_MESSAGE.getValue(),response);
			} else {
				responseInfo = GoForWealthPRSUtil.getSuccessResponseInfo(GoForWealthPRSErrorMessageEnum.ACCESS_DENIED_CODE.getValue(),GoForWealthPRSErrorMessageEnum.ACCESS_DENIED_MESSAGE.getValue(),null);
			}
		} else {
			responseInfo = GoForWealthPRSUtil.getSuccessResponseInfo(GoForWealthPRSErrorMessageEnum.ACCESS_DENIED_CODE.getValue(),GoForWealthPRSErrorMessageEnum.ACCESS_DENIED_MESSAGE.getValue(),null);
		}
		logger.info("Out switchScheme()");
		return responseInfo;
	}

	//********************************** unsecured url ***************************//
	@RequestMapping(value = "/api/calculateSipNavData", method = RequestMethod.POST)
	public GoForWealthPRSResponseInfo calculateSipNavData(@RequestParam("amount") String amount,@RequestParam("schemeCode") String schemeCode, @RequestParam("year") String year,@RequestParam("calculateType") String calculateType) {
		GoForWealthPRSResponseInfo responseInfo = null;
		if (amount.equals("0") || amount.equals("")) {
			String response = "Failure";
			responseInfo = GoForWealthPRSUtil.getSuccessResponseInfo(GoForWealthPRSErrorMessageEnum.DATA_NOT_FOUND_CODE.getValue(),GoForWealthPRSErrorMessageEnum.DATA_NOT_FOUND_MESSAGE.getValue(), response);
			return responseInfo;
		}
		int years = Integer.parseInt(year);
		Double amounts = Double.parseDouble(amount);
		NavCalculateResponse navCalculateResponse = goForWealthFundSchemeService.calculateSipNavData(amounts,schemeCode, years, calculateType);
		responseInfo = GoForWealthPRSUtil.getSuccessResponseInfo(GoForWealthPRSErrorMessageEnum.SUCCESS_CODE.getValue(),GoForWealthPRSErrorMessageEnum.SUCCESS_MESSAGE.getValue(), navCalculateResponse);
		return responseInfo;
	}

	//********************************** secured url ***********************************//
	@RequestMapping(value = "/api/getPaymentStatus/{orderId}", method = RequestMethod.POST)
	public GoForWealthPRSResponseInfo getPaymentStatus(@PathVariable("orderId") int orderId, Authentication auth) {
		logger.info("In getPaymentStatus()");
		UserPrincipal userSession = (UserPrincipal) auth.getPrincipal();
		GoForWealthPRSResponseInfo responseInfo = null;
		String response = null;
		if (userSession != null) {
			logger.info("In getPaymentStatus() with UserId : " + userSession.getUser().getUserId());
			User user = userRepository.findUserByRole(userSession.getUser().getUserId(), "USER");
			if (user != null) {
				response = goForWealthFundSchemeService.getPaymentStatus(orderId);
				if (response.equals("")) {
					responseInfo = new GoForWealthPRSResponseInfo();
					responseInfo.setStatus(GoForWealthPRSErrorMessageEnum.DATA_NOT_FOUND_CODE.getValue());
					responseInfo.setMessage(GoForWealthPRSErrorMessageEnum.DATA_NOT_FOUND_MESSAGE.getValue());
				} else {
					responseInfo = GoForWealthPRSUtil.getSuccessResponseInfo(GoForWealthPRSErrorMessageEnum.SUCCESS_CODE.getValue(),GoForWealthPRSErrorMessageEnum.SUCCESS_MESSAGE.getValue(),response);
				}
			} else {
				responseInfo = GoForWealthPRSUtil.getSuccessResponseInfo(GoForWealthPRSErrorMessageEnum.ACCESS_DENIED_CODE.getValue(),GoForWealthPRSErrorMessageEnum.ACCESS_DENIED_MESSAGE.getValue(),null);
			}
		} else {
			responseInfo = GoForWealthPRSUtil.getSuccessResponseInfo(GoForWealthPRSErrorMessageEnum.ACCESS_DENIED_CODE.getValue(),GoForWealthPRSErrorMessageEnum.ACCESS_DENIED_MESSAGE.getValue(),null);
		}
		logger.info("Out getPaymentStatus()");
		return responseInfo;
	}

	//******************************** secured url ***********************************//
	@RequestMapping(value = "/api/getAwaitingPaymentRecord", method = RequestMethod.GET)
	public GoForWealthPRSResponseInfo getAwaitingPaymentRecord(Authentication auth) {
		logger.info("In getAwaitingPaymentRecord()");
		UserPrincipal userSession = (UserPrincipal) auth.getPrincipal();
		GoForWealthPRSResponseInfo responseInfo = null;
		List<UnitAllocationResponse> unitAllocationResponse = new ArrayList<>();
		if (userSession != null) {
			logger.info("In getAwaitingPaymentRecord() with UserId :  " + userSession.getUser().getUserId());
			User user = userRepository.findUserByRole(userSession.getUser().getUserId(), "USER");
			if (user != null) {
				unitAllocationResponse = goForWealthFundSchemeService.getAwaitingPaymentRecord(userSession.getUser().getUserId());
				responseInfo = GoForWealthPRSUtil.getSuccessResponseInfo(GoForWealthPRSErrorMessageEnum.SUCCESS_CODE.getValue(),GoForWealthPRSErrorMessageEnum.SUCCESS_MESSAGE.getValue(),unitAllocationResponse);
			} else {
				responseInfo = GoForWealthPRSUtil.getSuccessResponseInfo(GoForWealthPRSErrorMessageEnum.ACCESS_DENIED_CODE.getValue(),GoForWealthPRSErrorMessageEnum.ACCESS_DENIED_MESSAGE.getValue(), null);
			}
		} else {
			responseInfo = GoForWealthPRSUtil.getSuccessResponseInfo(GoForWealthPRSErrorMessageEnum.ACCESS_DENIED_CODE.getValue(),GoForWealthPRSErrorMessageEnum.ACCESS_DENIED_MESSAGE.getValue(),null);
		}
		logger.info("Out getAwaitingPaymentRecord()");
		return responseInfo;
	}

	//******************** secured url ***********************************//
	@RequestMapping(value = "/api/deleteOrder", method = RequestMethod.POST)
	public GoForWealthPRSResponseInfo deleteOrder(@RequestParam("orderId") Integer orderId, Authentication auth) {
		logger.info("In deleteOrder()");
		UserPrincipal userSession = (UserPrincipal) auth.getPrincipal();
		String response = "Failure";
		GoForWealthPRSResponseInfo responseInfo = null;
		if (userSession != null) {
			logger.info("In deleteOrder() with UserId : " + userSession.getUser().getUserId());
			User user = userRepository.findUserByRole(userSession.getUser().getUserId(), "USER");
			if (user != null) {
				response = goForWealthFundSchemeService.deleteOrder(userSession.getUser().getUserId(), orderId);
				responseInfo = GoForWealthPRSUtil.getSuccessResponseInfo(GoForWealthPRSErrorMessageEnum.SUCCESS_CODE.getValue(),GoForWealthPRSErrorMessageEnum.SUCCESS_MESSAGE.getValue(),response);
			} else {
				responseInfo = GoForWealthPRSUtil.getSuccessResponseInfo(GoForWealthPRSErrorMessageEnum.ACCESS_DENIED_CODE.getValue(),GoForWealthPRSErrorMessageEnum.ACCESS_DENIED_MESSAGE.getValue(),null);
			}
		} else {
			responseInfo = GoForWealthPRSUtil.getSuccessResponseInfo(GoForWealthPRSErrorMessageEnum.ACCESS_DENIED_CODE.getValue(),GoForWealthPRSErrorMessageEnum.ACCESS_DENIED_MESSAGE.getValue(),null);
		}
		logger.info("Out deleteOrder()");
		return responseInfo;
	}

	//******************** secured url ***********************************//
	@RequestMapping(value = "/api/getUserGoals", method = RequestMethod.GET)
	public GoForWealthPRSResponseInfo getUserGoals(Authentication authentication) {
		logger.info("In getUserGoals()");
		UserPrincipal userPrincipal = (UserPrincipal) authentication.getPrincipal();
		GoForWealthPRSResponseInfo responseInfo = null;
		List<UserGoalResponse> userGoalResponseList = null;
		if (userPrincipal != null) {
			logger.info("In getUserGoals() with UserId : " + userPrincipal.getUser().getUserId());
			User user = userRepository.findUserByRole(userPrincipal.getUser().getUserId(), "USER");
			if(user != null) {
				userGoalResponseList = goForWealthFundSchemeService.getUserGoals(userPrincipal.getUser().getUserId());
				responseInfo = GoForWealthPRSUtil.getSuccessResponseInfo(GoForWealthPRSErrorMessageEnum.SUCCESS_CODE.getValue(),GoForWealthPRSErrorMessageEnum.SUCCESS_MESSAGE.getValue(),userGoalResponseList);
			}else{
				responseInfo = GoForWealthPRSUtil.getSuccessResponseInfo(GoForWealthPRSErrorMessageEnum.ACCESS_DENIED_CODE.getValue(),GoForWealthPRSErrorMessageEnum.ACCESS_DENIED_MESSAGE.getValue(),null);
			}
		}else{
			responseInfo = GoForWealthPRSUtil.getSuccessResponseInfo(GoForWealthPRSErrorMessageEnum.ACCESS_DENIED_CODE.getValue(),GoForWealthPRSErrorMessageEnum.ACCESS_DENIED_MESSAGE.getValue(),null);
		}
		logger.info("Out getUserGoals()");
		return responseInfo;
	}

	//********************************** secured url *********************************************//
	@RequestMapping(value = "/api/updateUserAssignedAndPredefineGoals", method = RequestMethod.POST)
	public GoForWealthPRSResponseInfo updateUserAssignedAndPredefineGoals(@RequestParam("oldGoalId") Integer oldGoalId,@RequestParam("goalId") Integer goalId, @RequestParam("orderId") Integer orderId,Authentication authentication) {
		logger.info("In updateUserAssignedAndPredefineGoals()");
		UserPrincipal userPrincipal = (UserPrincipal) authentication.getPrincipal();
		GoForWealthPRSResponseInfo responseInfo = null;
		String response = null;
		if (userPrincipal != null) {
			logger.info("In updateUserAssignedAndPredefineGoals() with UserId : " + userPrincipal.getUser().getUserId());
			User user = userRepository.findUserByRole(userPrincipal.getUser().getUserId(), "USER");
			if (user != null) {
				response = goForWealthFundSchemeService.updateUserAssignedAndPredefineGoals(oldGoalId, goalId, orderId,userPrincipal.getUser().getUserId());
				responseInfo = GoForWealthPRSUtil.getSuccessResponseInfo(GoForWealthPRSErrorMessageEnum.SUCCESS_CODE.getValue(),GoForWealthPRSErrorMessageEnum.SUCCESS_MESSAGE.getValue(),response);
			} else {
				responseInfo = GoForWealthPRSUtil.getSuccessResponseInfo(GoForWealthPRSErrorMessageEnum.ACCESS_DENIED_CODE.getValue(),GoForWealthPRSErrorMessageEnum.ACCESS_DENIED_MESSAGE.getValue(),null);
			}
		} else {
			responseInfo = GoForWealthPRSUtil.getSuccessResponseInfo(GoForWealthPRSErrorMessageEnum.ACCESS_DENIED_CODE.getValue(),GoForWealthPRSErrorMessageEnum.ACCESS_DENIED_MESSAGE.getValue(),null);
		}
		logger.info("Out updateUserAssignedAndPredefineGoals()");
		return responseInfo;
	}

	public void getOnlyStrings() {
		List<IndianIfscCodes> indianIfsc = indianIfscCodesRepository.findAll();
		for (IndianIfscCodes indianIfscCodes : indianIfsc) {
			String branchName = indianIfscCodes.getBranchName();
			Pattern pattern = Pattern.compile("[^a-z A-Z]");
			Matcher matcher = pattern.matcher(branchName);
			String number = matcher.replaceAll("");
			System.out.println(number);
		}
	}

	//*********************** secured url ************************************//
	@RequestMapping(value = "/api/getMandateStatus", method = RequestMethod.GET)
	public GoForWealthPRSResponseInfo getMandateStatus(Authentication authentication) {
		logger.info("In getMandateStatus()");
		UserPrincipal userPrincipal = (UserPrincipal) authentication.getPrincipal();
		GoForWealthPRSResponseInfo responseInfo = null;
		PrsDTO prsDto = new PrsDTO();
		if (userPrincipal != null) {
			logger.info("In getMandateStatus() with UserId : " + userPrincipal.getUser().getUserId());
			User user = userRepository.findUserByRole(userPrincipal.getUser().getUserId(), "USER");
			if (user != null) {
				prsDto = goForWealthFundSchemeService.getMandateStatus(userPrincipal.getUser().getUserId());
				if (prsDto.getMandateStatus().equals("")) {
					responseInfo = GoForWealthPRSUtil.getSuccessResponseInfo(GoForWealthPRSErrorMessageEnum.FAILURE_CODE.getValue(),GoForWealthPRSErrorMessageEnum.FAILURE_MESSAGE.getValue(), prsDto);
				} else {
					responseInfo = GoForWealthPRSUtil.getSuccessResponseInfo(GoForWealthPRSErrorMessageEnum.SUCCESS_CODE.getValue(),GoForWealthPRSErrorMessageEnum.SUCCESS_MESSAGE.getValue(), prsDto);
				}
			} else {
				responseInfo = GoForWealthPRSUtil.getSuccessResponseInfo(GoForWealthPRSErrorMessageEnum.ACCESS_DENIED_CODE.getValue(),GoForWealthPRSErrorMessageEnum.ACCESS_DENIED_MESSAGE.getValue(), null);
			}
		} else {
			responseInfo = GoForWealthPRSUtil.getSuccessResponseInfo(GoForWealthPRSErrorMessageEnum.ACCESS_DENIED_CODE.getValue(),GoForWealthPRSErrorMessageEnum.ACCESS_DENIED_MESSAGE.getValue(), null);
		}
		logger.info("Out getMandateStatus()");
		return responseInfo;
	}

	//********************************** secured url ***********************************//
	@RequestMapping(value = "/api/getEmandateAuthenticationUrl", method = RequestMethod.GET)
	public GoForWealthPRSResponseInfo getEmandateAutheticationUrl(Authentication auth) {
		logger.info("In getEmandateAuthenticationUrl()");
		GoForWealthPRSResponseInfo responseInfo = null;
		UserPrincipal userPrincipal = (UserPrincipal) auth.getPrincipal();
		String emandateAuthenticationUrl = "";
		if (userPrincipal != null) {
			logger.info("In getEmandateAuthenticationUrl() with UserId : " + userPrincipal.getUser().getUserId());
			User user = userRepository.findUserByRole(userPrincipal.getUser().getUserId(), "USER");
			if (user != null) {
				emandateAuthenticationUrl = goForWealthFundSchemeService.getEmandateAuthenticationUrl(userPrincipal.getUser().getUserId());
				responseInfo = GoForWealthPRSUtil.getSuccessResponseInfo(GoForWealthPRSErrorMessageEnum.SUCCESS_CODE.getValue(),GoForWealthPRSErrorMessageEnum.SUCCESS_MESSAGE.getValue(),emandateAuthenticationUrl);
			} else {
				responseInfo = GoForWealthPRSUtil.getSuccessResponseInfo(GoForWealthPRSErrorMessageEnum.ACCESS_DENIED_CODE.getValue(),GoForWealthPRSErrorMessageEnum.ACCESS_DENIED_MESSAGE.getValue(),null);
			}
		} else {
			responseInfo = GoForWealthPRSUtil.getSuccessResponseInfo(GoForWealthPRSErrorMessageEnum.ACCESS_DENIED_CODE.getValue(),GoForWealthPRSErrorMessageEnum.ACCESS_DENIED_MESSAGE.getValue(),null);
		}
		logger.info("Out getEmandateAuthenticationUrl()");
		return responseInfo;
	}

	//********************************** secured url ***********************//
	@RequestMapping(value = "/api/updateSipDate", method = RequestMethod.POST)
	public GoForWealthPRSResponseInfo updateSipDate(@RequestBody AddToCartDTO addToCartDTO, Authentication auth) {
		logger.info("In updateSipDate()");
		UserPrincipal userSession = (UserPrincipal) auth.getPrincipal();
		GoForWealthPRSResponseInfo responseInfo = null;
		String response = "";
		if (userSession != null) {
			User user = userRepository.findUserByRole(userSession.getUser().getUserId(), "USER");
			if (user != null) {
				response = goForWealthFundSchemeService.updateSipDate(userSession.getUser().getUserId(), addToCartDTO);
				responseInfo = GoForWealthPRSUtil.getSuccessResponseInfo(GoForWealthPRSErrorMessageEnum.SUCCESS_CODE.getValue(),GoForWealthPRSErrorMessageEnum.SUCCESS_MESSAGE.getValue(), response);
			} else {
				responseInfo = GoForWealthPRSUtil.getSuccessResponseInfo(GoForWealthPRSErrorMessageEnum.ACCESS_DENIED_CODE.getValue(),GoForWealthPRSErrorMessageEnum.ACCESS_DENIED_MESSAGE.getValue(), null);
			}
		} else {
			responseInfo = GoForWealthPRSUtil.getSuccessResponseInfo(GoForWealthPRSErrorMessageEnum.ACCESS_DENIED_CODE.getValue(),GoForWealthPRSErrorMessageEnum.ACCESS_DENIED_MESSAGE.getValue(), null);
		}
		logger.info("Out updateSipDate()");
		return responseInfo;
	}

	@RequestMapping(value = "/upload/updateNav/updateSchemesNavViaTxt", produces = "application/json", method = RequestMethod.POST)
	public GoForWealthPRSResponseInfo uploadNavDateAndCurrentNavForSchemeViaTxt(@RequestBody SchemeUploadRequest schemeUploadRequest, Authentication auth) {
		logger.info("In saveSchemeTextFileDataToDbUsingBase64()");
		String response = "";
		GoForWealthPRSResponseInfo responseInfo = null;
		try {
			if (schemeUploadRequest.getBase64() == null) {
				return GoForWealthPRSUtil.getErrorResponseInfo(GoForWealthPRSErrorMessageEnum.DATA_NOT_FOUND_CODE.getValue(),GoForWealthPRSErrorMessageEnum.DATA_NOT_FOUND_MESSAGE.getValue(), null);
			}
			String base64 = schemeUploadRequest.getBase64();
			byte[] byteArray = Base64.decodeBase64(base64);
			response = goForWealthFundSchemeService.uploadNavDateAndCurrentNavForSchemeViaTxt(byteArray);
			responseInfo = GoForWealthPRSUtil.getSuccessResponseInfo(GoForWealthPRSErrorMessageEnum.SUCCESS_CODE.getValue(),GoForWealthPRSErrorMessageEnum.SUCCESS_MESSAGE.getValue(),response);
		} catch (Exception e) {
			return GoForWealthPRSUtil.getSuccessResponseInfo(GoForWealthPRSErrorMessageEnum.INTERNAL_SERVER_ERROR.getValue(),GoForWealthPRSErrorMessageEnum.INTERNAL_SERVER_ERROR.getValue(), null);
		}
		logger.info("Out savesSchemeTextFileDataToDb()");
		return responseInfo;
	}

	@RequestMapping(value = "/upload/updateNav/updateSchemesNavViaAutoDownload", produces = "application/json", method = RequestMethod.POST)
	public GoForWealthPRSResponseInfo uploadNavDateAndCurrentNavForSchemeViaAutoDownload(@RequestBody NavUpdateRequest navUpdateRequest,HttpServletRequest request) {
		logger.info("In uploadNavDateAndCurrentNavForSchemeViaAutoDownload()");
		String response = "";
		GoForWealthPRSResponseInfo responseInfo = null;
		String headerValue = request.getHeader("NodeSchedulerAuth");
		System.out.println("Header : " + headerValue);
		if(headerValue.equals("BIS7CS3#SDVL#03NDND87GC34NP@N93H")){
			response = goForWealthFundSchemeService.uploadNavDateAndCurrentNavForSchemeViaAutoDownload(navUpdateRequest);
			responseInfo = GoForWealthPRSUtil.getSuccessResponseInfo(GoForWealthPRSErrorMessageEnum.SUCCESS_CODE.getValue(),GoForWealthPRSErrorMessageEnum.SUCCESS_MESSAGE.getValue(), response);
		}else{
			responseInfo = GoForWealthPRSUtil.getSuccessResponseInfo(GoForWealthPRSErrorMessageEnum.ACCESS_DENIED_CODE.getValue(),GoForWealthPRSErrorMessageEnum.ACCESS_DENIED_MESSAGE.getValue(), response);
		}
		logger.info("Out uploadNavDateAndCurrentNavForSchemeViaAutoDownload()");
		return responseInfo;
	}

	//********************************** unsecured url ***********************************************//
	@RequestMapping(value = "/api/getSchemeDetailsByKeyword/{schemeKeyword}", method = RequestMethod.GET)
	public GoForWealthPRSResponseInfo getSchemeDetailsByKeyword(@PathVariable("schemeKeyword") String schemeKeyword) {
		logger.info("In getSchemeDetailsByKeyword()");
		GoForWealthPRSResponseInfo responseInfo;
		FundSchemeDTO fundSchemeDTO = goForWealthFundSchemeService.getSchemeDetailsByKeyword(schemeKeyword);
		if(fundSchemeDTO != null) {
			responseInfo = GoForWealthPRSUtil.getSuccessResponseInfo(GoForWealthPRSErrorMessageEnum.SUCCESS_CODE.getValue(),GoForWealthPRSErrorMessageEnum.SUCCESS_MESSAGE.getValue(), fundSchemeDTO);
		} else {
			responseInfo = new GoForWealthPRSResponseInfo();
			responseInfo.setStatus(GoForWealthPRSErrorMessageEnum.DATA_NOT_FOUND_CODE.getValue());
			responseInfo.setMessage(GoForWealthPRSErrorMessageEnum.DATA_NOT_FOUND_MESSAGE.getValue());
		}
		logger.info("Out getSchemeDetailsByKeyword()");
		return responseInfo;
	}

	//**************************** unsecured url ***********************************//
	@RequestMapping(value = "/api/getAllRecommendedScheme", method = RequestMethod.GET)
	public GoForWealthPRSResponseInfo getAllRecommendedScheme() {
		logger.info("In getAllRecommendedScheme");
		GoForWealthPRSResponseInfo responseInfo = null;
		List<FundSchemeDTO> fundSchemeDTOList = goForWealthFundSchemeService.getAllRecommendedScheme();
		if (fundSchemeDTOList != null && fundSchemeDTOList.size() > 0) {
			responseInfo = GoForWealthPRSUtil.getSuccessResponseInfo(GoForWealthPRSErrorMessageEnum.SUCCESS_CODE.getValue(),GoForWealthPRSErrorMessageEnum.SUCCESS_MESSAGE.getValue(), fundSchemeDTOList);
			responseInfo.setTotalRecords(fundSchemeDTOList.size());
		}else{
			responseInfo = new GoForWealthPRSResponseInfo();
			responseInfo.setStatus(GoForWealthPRSErrorMessageEnum.DATA_NOT_FOUND_CODE.getValue());
			responseInfo.setMessage(GoForWealthPRSErrorMessageEnum.DATA_NOT_FOUND_MESSAGE.getValue());
		}
		logger.info("Out getAllRecommendedScheme()");
		return responseInfo;
	}

	@RequestMapping(value = "/getRedumptionDetail/{schemeCode}", method = RequestMethod.GET)
	public GoForWealthPRSResponseInfo getRedumptionDetail(@RequestParam("folioNo") String folioNo,@PathVariable("schemeCode") String schemeCode) {
		logger.info("In getRedumptionDetail");
		GoForWealthPRSResponseInfo responseInfo = null;
		List<RedumptionResponse> redumptionResponses = goForWealthFundSchemeService.getRedumptionDetail(folioNo, schemeCode);
		if (redumptionResponses != null && redumptionResponses.size() > 0) {
			responseInfo = GoForWealthPRSUtil.getSuccessResponseInfo(GoForWealthPRSErrorMessageEnum.SUCCESS_CODE.getValue(),GoForWealthPRSErrorMessageEnum.SUCCESS_MESSAGE.getValue(), redumptionResponses);
		}else{
			responseInfo = new GoForWealthPRSResponseInfo();
			responseInfo.setStatus(GoForWealthPRSErrorMessageEnum.DATA_NOT_FOUND_CODE.getValue());
			responseInfo.setMessage(GoForWealthPRSErrorMessageEnum.DATA_NOT_FOUND_MESSAGE.getValue());
		}
		logger.info("Out getRedumptionDetail()");
		return responseInfo;
	}

	//******************* secured url ***********************************//
	@RequestMapping(value = "/getSipOrderList", method = RequestMethod.GET)
	public GoForWealthPRSResponseInfo getSipOrderListToStopSip(Authentication auth) {
		logger.info("In getSipOrderListToStopSip()");
		UserPrincipal userSession = (UserPrincipal) auth.getPrincipal();
		GoForWealthPRSResponseInfo responseInfo = null;
		List<AddToCartDTO> addToCartDTOsList = null;
		if (userSession != null) {
			logger.info("In getSipOrderListToStopSip() with UserId :  " + userSession.getUser().getUserId());
			User user = userRepository.findUserByRole(userSession.getUser().getUserId(), "USER");
			if (user != null) {
				addToCartDTOsList = goForWealthFundSchemeService.getActiveSipsList(user);
				responseInfo = GoForWealthPRSUtil.getSuccessResponseInfo(GoForWealthPRSErrorMessageEnum.SUCCESS_CODE.getValue(),GoForWealthPRSErrorMessageEnum.SUCCESS_MESSAGE.getValue(), addToCartDTOsList);
			}else{
				responseInfo = GoForWealthPRSUtil.getSuccessResponseInfo(GoForWealthPRSErrorMessageEnum.ACCESS_DENIED_CODE.getValue(),GoForWealthPRSErrorMessageEnum.ACCESS_DENIED_MESSAGE.getValue(), null);
			}
		}else{
			responseInfo = GoForWealthPRSUtil.getSuccessResponseInfo(GoForWealthPRSErrorMessageEnum.ACCESS_DENIED_CODE.getValue(),GoForWealthPRSErrorMessageEnum.ACCESS_DENIED_MESSAGE.getValue(), null);
		}
		logger.info("Out getSipOrderListToStopSip()");
		return responseInfo;
	}

	//********************************* secured url ****************************//
	@RequestMapping(value = "/api/userInvestmentGoal", method = RequestMethod.POST)
	public GoForWealthPRSResponseInfo userInvestmentGoals(@RequestBody UserGoalResponse userGoalResponse,Authentication authentication) {
		logger.info("In userInvestmentGoals()");
		UserPrincipal userPrincipal = (UserPrincipal) authentication.getPrincipal();
		GoForWealthPRSResponseInfo responseInfo = null;
		boolean response = false;
		if(userPrincipal != null) {
			logger.info("In userInvestmentGoals() with UserId : " + userPrincipal.getUser().getUserId());
			User user = userRepository.findUserByRole(userPrincipal.getUser().getUserId(), "USER");
			if(user != null) {
				response = goForWealthFundSchemeService.userInvestmentGoals(userGoalResponse,userPrincipal.getUser().getUserId());
				if(response)
					responseInfo = GoForWealthPRSUtil.getSuccessResponseInfo(GoForWealthPRSErrorMessageEnum.SUCCESS_CODE.getValue(),GoForWealthPRSErrorMessageEnum.SUCCESS_MESSAGE.getValue(),response);
				else
					responseInfo = GoForWealthPRSUtil.getErrorResponseInfo(GoForWealthPRSErrorMessageEnum.FAILURE_CODE.getValue(),GoForWealthPRSErrorMessageEnum.FAILURE_MESSAGE.getValue(),null);
			}else{
				responseInfo = GoForWealthPRSUtil.getSuccessResponseInfo(GoForWealthPRSErrorMessageEnum.ACCESS_DENIED_CODE.getValue(),GoForWealthPRSErrorMessageEnum.ACCESS_DENIED_MESSAGE.getValue(),null);
			}
		}else{
			responseInfo = GoForWealthPRSUtil.getSuccessResponseInfo(GoForWealthPRSErrorMessageEnum.ACCESS_DENIED_CODE.getValue(),GoForWealthPRSErrorMessageEnum.ACCESS_DENIED_MESSAGE.getValue(),null);
		}
		logger.info("Out userInvestmentGoals()");
		return responseInfo;
	}

	//************** unsecured url **********************************************//
	@RequestMapping(value = "/api/updateBillerStatus", method = RequestMethod.POST)
	public void updateBillerStatus(@RequestBody List<PrsDTO> prsDTO) {
		logger.info("In updateBillerStatus()");
		if(prsDTO != null){
			goForWealthFundSchemeService.updateBillerStatus(prsDTO);
		}
		logger.info("Out updateBillerStatus()");
	}

	//*************** unsecured url *****************************************//
	@RequestMapping(value = "/api/getBillerStatus", method = RequestMethod.GET)
	public GoForWealthPRSResponseInfo getBillerStatus() {
		logger.info("In updateBillerStatus()");
		List<PrsDTO> orDtosList = null;
		GoForWealthPRSResponseInfo responseInfo = null;
		orDtosList = goForWealthFundSchemeService.getBillerStatus();
		logger.info("Out updateBillerStatus()");
		responseInfo = GoForWealthPRSUtil.getSuccessResponseInfo(GoForWealthPRSErrorMessageEnum.SUCCESS_CODE.getValue(),GoForWealthPRSErrorMessageEnum.SUCCESS_MESSAGE.getValue(),orDtosList);
		return responseInfo;
	}

	//******************** secured url ***********************************//
		//@RequestMapping(value = "/api/getConsoliDatedFollio", method = RequestMethod.GET)
	      @RequestMapping(value = "/api/getPortfolio", method = RequestMethod.GET)
		public GoForWealthPRSResponseInfo getConsoliDatedFollio(Authentication auth) {
			logger.info("In getPortfolio()");
			UserPrincipal userSession = (UserPrincipal) auth.getPrincipal();
			GoForWealthPRSResponseInfo responseInfo;
			List<PortFolioDataDTO> portFolioDataDTO = new ArrayList<>();
			if (userSession != null) {
				logger.info("In getPortfolio() with UserId : " + userSession.getUser().getUserId());
				User user = userRepository.findUserByRole(userSession.getUser().getUserId(), "USER");
				if (user != null) {
					portFolioDataDTO = goForWealthFundSchemeService.getConsoliDatedFollio(userSession.getUser().getUserId());
					responseInfo = GoForWealthPRSUtil.getSuccessResponseInfo(GoForWealthPRSErrorMessageEnum.SUCCESS_CODE.getValue(),GoForWealthPRSErrorMessageEnum.SUCCESS_MESSAGE.getValue(),portFolioDataDTO);
				} else {
					responseInfo = GoForWealthPRSUtil.getSuccessResponseInfo(GoForWealthPRSErrorMessageEnum.ACCESS_DENIED_CODE.getValue(),GoForWealthPRSErrorMessageEnum.ACCESS_DENIED_MESSAGE.getValue(),null);
				}
			} else {
				responseInfo = GoForWealthPRSUtil.getSuccessResponseInfo(GoForWealthPRSErrorMessageEnum.ACCESS_DENIED_CODE.getValue(),GoForWealthPRSErrorMessageEnum.ACCESS_DENIED_MESSAGE.getValue(),null);
			}
			logger.info("Out getPortfolio()");
			return responseInfo;
		}
		
		@RequestMapping(value="/api/checkAmcForFolio/{amcCode}", method=RequestMethod.GET)
	    public GoForWealthPRSResponseInfo checkAmcForFolio(@PathVariable("amcCode")String amcCode,Authentication auth){
	    	logger.info("In checkAmcForFolio()");
	  		GoForWealthPRSResponseInfo responseInfo = null;
	  		List<ResponseDto> folioNoList = null;
	  		UserPrincipal userSession = (UserPrincipal)auth.getPrincipal();
	  		if(userSession != null){
	  			logger.info("In checkAmcForFolio() with userId : " + userSession.getUser().getUserId());
	  			User user = userRepository.findUserByRole(userSession.getUser().getUserId(),"USER");
	  			if(user != null){
	  				folioNoList = goForWealthFundSchemeService.checkAmcForFolio(amcCode,user);
	  				responseInfo = GoForWealthPRSUtil.getSuccessResponseInfo(GoForWealthPRSErrorMessageEnum.SUCCESS_CODE.getValue(),GoForWealthPRSErrorMessageEnum.SUCCESS_MESSAGE.getValue(),folioNoList);
	  			}else{
	  				responseInfo = GoForWealthPRSUtil.getSuccessResponseInfo(GoForWealthPRSErrorMessageEnum.ACCESS_DENIED_CODE.getValue(), GoForWealthPRSErrorMessageEnum.ACCESS_DENIED_MESSAGE.getValue(),null);
	  			}
	  		}else{
	  			responseInfo = GoForWealthPRSUtil.getSuccessResponseInfo(GoForWealthPRSErrorMessageEnum.ACCESS_DENIED_CODE.getValue(), GoForWealthPRSErrorMessageEnum.ACCESS_DENIED_MESSAGE.getValue(),null);
	  		}
	    	logger.info("Out checkAmcForFolio()");
	    	return responseInfo;
	    }

	    //********************************** secured url ****************//
	    
	  	//@RequestMapping(value = "/api/redeemOrderByFollio", method = RequestMethod.POST)
		@RequestMapping(value = "/api/redeem", method = RequestMethod.POST)
	  	public GoForWealthPRSResponseInfo redeemOrderByFollio(@RequestBody AddToCartDTO addToCartDTO , Authentication auth) {
	  		logger.info("In redeemOrder()");
	  		UserPrincipal userSession = (UserPrincipal) auth.getPrincipal();
	  		String response = "Failure";
	  		GoForWealthPRSResponseInfo responseInfo = null;
	  		if (userSession != null) {
	  			logger.info("In redeemOrder() with UserId : " + userSession.getUser().getUserId());
	  			User user = userRepository.findUserByRole(userSession.getUser().getUserId(), "USER");
	  			if (user != null) {
	  				response = goForWealthFundSchemeService.redeemOrderByFollio(userSession.getUser().getUserId(), addToCartDTO);
	  				responseInfo = GoForWealthPRSUtil.getSuccessResponseInfo(GoForWealthPRSErrorMessageEnum.SUCCESS_CODE.getValue(),GoForWealthPRSErrorMessageEnum.SUCCESS_MESSAGE.getValue(),response);
	  			} else {
	  				responseInfo = GoForWealthPRSUtil.getSuccessResponseInfo(GoForWealthPRSErrorMessageEnum.ACCESS_DENIED_CODE.getValue(),GoForWealthPRSErrorMessageEnum.ACCESS_DENIED_MESSAGE.getValue(),null);
	  			}
	  		} else {
	  			responseInfo = GoForWealthPRSUtil.getSuccessResponseInfo(GoForWealthPRSErrorMessageEnum.ACCESS_DENIED_CODE.getValue(),GoForWealthPRSErrorMessageEnum.ACCESS_DENIED_MESSAGE.getValue(),null);
	  		}
	  		logger.info("Out redeemOrder()");
	  		return responseInfo;
	  	}
	  	
		@RequestMapping(value = "/getOrderDetailsById/{orderId}", method = RequestMethod.GET)
		public GoForWealthPRSResponseInfo getRedumptionDetail(@PathVariable("orderId") Integer orderId) {
			logger.info("In getOrderDetailsById");
			GoForWealthPRSResponseInfo responseInfo = null;
			AddToCartDTO orders = goForWealthFundSchemeService.getOrderDetailsById(orderId);
			if (orderId != null) {
				responseInfo = GoForWealthPRSUtil.getSuccessResponseInfo(GoForWealthPRSErrorMessageEnum.SUCCESS_CODE.getValue(),GoForWealthPRSErrorMessageEnum.SUCCESS_MESSAGE.getValue(), orders);
			}else{
				responseInfo = new GoForWealthPRSResponseInfo();
				responseInfo.setStatus(GoForWealthPRSErrorMessageEnum.DATA_NOT_FOUND_CODE.getValue());
				responseInfo.setMessage(GoForWealthPRSErrorMessageEnum.DATA_NOT_FOUND_MESSAGE.getValue());
			}
			logger.info("Out getOrderDetailsById()");
			return responseInfo;
		}
		
		
		@RequestMapping(value = "/getNavFromAmfi", method = RequestMethod.POST)
	  	public String getNavFromAmfi(@RequestBody List<NavDto> navDto) {
	  		logger.info("In getNavFromAmfi()");
	  		String response = "Failure";
	  		
	  		response = goForWealthFundSchemeService.getNavFromAmfi(navDto);
	  				
	  		logger.info("Out getNavFromAmfi()");
	  		return response;
	  	}
		
		
		@RequestMapping(value = "/test", method = RequestMethod.GET)
	  	public String test() {
	  		logger.info("In getNavFromAmfi()");
	  		String response = "Failure";
	  		
	  		 //goForWealthFundSchemeService.updateOrderRedemptionStatus();
	  		goForWealthFundSchemeService.updateUserDebitedSipInstallmentsThroughDates();
	  				
	  		logger.info("Out getNavFromAmfi()");
	  		return response;
	  	}
}