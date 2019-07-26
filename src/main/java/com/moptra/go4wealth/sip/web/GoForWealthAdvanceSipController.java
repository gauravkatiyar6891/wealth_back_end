package com.moptra.go4wealth.sip.web;

import java.io.IOException;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.moptra.go4wealth.bean.User;
import com.moptra.go4wealth.repository.UserRepository;
import com.moptra.go4wealth.security.UserPrincipal;
import com.moptra.go4wealth.sip.common.enums.GoForWealthSIPErrorMessageEnum;
import com.moptra.go4wealth.sip.common.exception.GoForWealthSIPException;
import com.moptra.go4wealth.sip.common.rest.GoForWealthSIPResponseInfo;
import com.moptra.go4wealth.sip.common.util.GoForWealthSIPUtil;
import com.moptra.go4wealth.sip.model.AssetClassDTO;
import com.moptra.go4wealth.sip.model.GoalDTO;
import com.moptra.go4wealth.sip.model.ReturnsTypeDTO;
import com.moptra.go4wealth.sip.model.UserGoalDto;
import com.moptra.go4wealth.sip.service.GoForWealthSIPService;
import com.moptra.go4wealth.uma.common.constant.GoForWealthUMAConstants;
import com.moptra.go4wealth.uma.common.enums.GoForWealthErrorMessageEnum;
import com.moptra.go4wealth.uma.common.exception.GoForWealthUMAException;
import com.moptra.go4wealth.uma.common.rest.GoForWealthUMAResponseInfo;
import com.moptra.go4wealth.uma.common.util.GoForWealthUMAUtil;

@RestController
@RequestMapping("${server.contextPath}/sip/advance")
public class GoForWealthAdvanceSipController {

	private static Logger logger = LoggerFactory.getLogger(GoForWealthAdvanceSipController.class);

	@Autowired
	private GoForWealthSIPService goForWealthSIPService;
	
	@Autowired
	UserRepository userRepository;

	/**
	 * 
	 * @param exception
	 * @return GoForWealthUMAResponseInfo
	 */
	@ExceptionHandler({ GoForWealthSIPException.class })
	public GoForWealthSIPResponseInfo handleException(GoForWealthSIPException exception) {
		return GoForWealthSIPUtil.getExceptionResponseInfo(exception);
	}

	/**
	 * 
	 * @param exception
	 * @return
	 */
	@ExceptionHandler({ Exception.class })
	public GoForWealthSIPResponseInfo handleException(Exception exception) {
		return GoForWealthSIPUtil.getErrorResponseInfo(GoForWealthSIPErrorMessageEnum.COMMON_ERROR_CODE.getValue(),GoForWealthSIPErrorMessageEnum.FAILURE_MESSAGE.getValue(),exception);
	}

	@GetMapping("/getAssetClassList")
	public GoForWealthSIPResponseInfo getAssetClassList() throws GoForWealthSIPException {
		logger.info("In getAssetClassList()");
		List<AssetClassDTO> assetClassDTOs = goForWealthSIPService.getAssetClassList();
		GoForWealthSIPResponseInfo responseInfo;
		if (assetClassDTOs != null && assetClassDTOs.size() > 0) {
			responseInfo = GoForWealthSIPUtil.getSuccessResponseInfo(GoForWealthSIPErrorMessageEnum.SUCCESS_CODE.getValue(),GoForWealthSIPErrorMessageEnum.SUCCESS_MESSAGE.getValue(), assetClassDTOs);
			responseInfo.setTotalRecords(assetClassDTOs.size());
		} else {
			responseInfo = new GoForWealthSIPResponseInfo();
			responseInfo.setMessage(GoForWealthSIPErrorMessageEnum.DATA_NOT_FOUND_MESSAGE.getValue());
			responseInfo.setStatus(GoForWealthSIPErrorMessageEnum.DATA_NOT_FOUND_CODE.getValue());
			responseInfo.setTotalRecords(0);
		}
		logger.info("Out getAssetClassList()");
		return responseInfo;
	}

	@GetMapping("/getRiskProfileList")
	public GoForWealthSIPResponseInfo getRiskProfileList() throws GoForWealthSIPException {
		logger.info("In getRiskProfileList()");
		List<ReturnsTypeDTO> returnsTypeDTOs = goForWealthSIPService.getRiskProfileList();
		GoForWealthSIPResponseInfo responseInfo;
		if (returnsTypeDTOs != null && returnsTypeDTOs.size() > 0) {
			responseInfo = GoForWealthSIPUtil.getSuccessResponseInfo(GoForWealthSIPErrorMessageEnum.SUCCESS_CODE.getValue(),GoForWealthSIPErrorMessageEnum.SUCCESS_MESSAGE.getValue(), returnsTypeDTOs);
			responseInfo.setTotalRecords(returnsTypeDTOs.size());
		} else {
			responseInfo = new GoForWealthSIPResponseInfo();
			responseInfo.setMessage(GoForWealthSIPErrorMessageEnum.DATA_NOT_FOUND_MESSAGE.getValue());
			responseInfo.setStatus(GoForWealthSIPErrorMessageEnum.DATA_NOT_FOUND_CODE.getValue());
			responseInfo.setTotalRecords(0);
		}
		logger.info("Out getRiskProfileList()");
		return responseInfo;
	}
	
	@GetMapping("/getPredefinedGoal")
	public GoForWealthSIPResponseInfo getPredefinedGoal() throws GoForWealthSIPException {
		logger.info("In getPredefinedGoal()");
		List<GoalDTO> goalDtoList = goForWealthSIPService.getPredefinedGoalList();
		GoForWealthSIPResponseInfo responseInfo;
		if (goalDtoList != null && goalDtoList.size() > 0) {
			responseInfo = GoForWealthSIPUtil.getSuccessResponseInfo(GoForWealthSIPErrorMessageEnum.SUCCESS_CODE.getValue(),GoForWealthSIPErrorMessageEnum.SUCCESS_MESSAGE.getValue(),goalDtoList);
			responseInfo.setTotalRecords(goalDtoList.size());
		} else {
			responseInfo = new GoForWealthSIPResponseInfo();
			responseInfo.setMessage(GoForWealthSIPErrorMessageEnum.DATA_NOT_FOUND_MESSAGE.getValue());
			responseInfo.setStatus(GoForWealthSIPErrorMessageEnum.DATA_NOT_FOUND_CODE.getValue());
			responseInfo.setTotalRecords(0);
		}
		logger.info("Out getPredefinedGoal()");
		return responseInfo;
	}

	@PostMapping("/createGoal")
	public GoForWealthSIPResponseInfo createGoal(@RequestBody UserGoalDto createGoalDto, Authentication auth) throws GoForWealthSIPException {
		logger.info("In createGoal()");
		GoForWealthSIPResponseInfo responseInfo = new GoForWealthSIPResponseInfo();
		UserPrincipal userSession = (UserPrincipal)auth.getPrincipal();
		if(userSession != null){
			logger.info("In createGoal() with UserId : " + userSession.getUser().getUserId());
			User user = userRepository.findUserByRole(userSession.getUser().getUserId(), "USER");
			if(user != null){
				boolean result = false;
				result = goForWealthSIPService.createUserGoal(createGoalDto,userSession.getUser().getUserId());
				if(result) {
					responseInfo = GoForWealthSIPUtil.getSuccessResponseInfo(GoForWealthSIPErrorMessageEnum.SUCCESS_CODE.getValue(),GoForWealthSIPErrorMessageEnum.SUCCESS_MESSAGE.getValue(),result);
				}else{
					responseInfo.setMessage(GoForWealthSIPErrorMessageEnum.DATA_NOT_FOUND_MESSAGE.getValue());
					responseInfo.setStatus(GoForWealthSIPErrorMessageEnum.DATA_NOT_FOUND_CODE.getValue());
					responseInfo.setTotalRecords(0);
				}
			}else{
				responseInfo.setMessage(GoForWealthSIPErrorMessageEnum.ACCESS_DENIED_CODE.getValue());
				responseInfo.setStatus(GoForWealthSIPErrorMessageEnum.ACCESS_DENIED_MESSAGE.getValue());
			}
		}else{
			responseInfo.setMessage(GoForWealthSIPErrorMessageEnum.ACCESS_DENIED_CODE.getValue());
			responseInfo.setStatus(GoForWealthSIPErrorMessageEnum.ACCESS_DENIED_MESSAGE.getValue());
		}
		logger.info("Out createGoal()");
		return responseInfo;
	}

	@PostMapping("/replaceGoal")
	public GoForWealthUMAResponseInfo replaceGoal(@RequestBody UserGoalDto replaceGoalDto,Authentication auth) throws IOException, GoForWealthUMAException {
		logger.info("In replaceGoal()");
		GoForWealthUMAResponseInfo goForWealthUMAResponseInfo = null;
	 	UserPrincipal userSession = (UserPrincipal)auth.getPrincipal();
	 	if(userSession != null) {
	 		logger.info("In replaceGoal() with UserId : " + userSession.getUser().getUserId());
	 		User user = userRepository.findUserByRole(userSession.getUser().getUserId(), "USER");
	 		if(user != null){
	 			String result = "";
	 			result = goForWealthSIPService.replaceUserGoal(replaceGoalDto,userSession.getUser().getUserId());
	 			if(result.equals(GoForWealthUMAConstants.SUCCESS)){
	 				goForWealthUMAResponseInfo = GoForWealthUMAUtil.getSuccessResponseInfo(GoForWealthErrorMessageEnum.SUCCESS_CODE.getValue(), GoForWealthErrorMessageEnum.SUCCESS_MESSAGE.getValue(),result);
	 			}else{
	 				goForWealthUMAResponseInfo = GoForWealthUMAUtil.getSuccessResponseInfo(GoForWealthErrorMessageEnum.FAILURE_CODE.getValue(), GoForWealthErrorMessageEnum.FAILURE_MESSAGE.getValue(),result);
	 			}
	 		}else{
	 			goForWealthUMAResponseInfo=GoForWealthUMAUtil.getSuccessResponseInfo(GoForWealthSIPErrorMessageEnum.ACCESS_DENIED_CODE.getValue(),GoForWealthSIPErrorMessageEnum.ACCESS_DENIED_MESSAGE.getValue(),null);
	 		}
	    }else{
	    	goForWealthUMAResponseInfo=GoForWealthUMAUtil.getSuccessResponseInfo(GoForWealthSIPErrorMessageEnum.ACCESS_DENIED_CODE.getValue(),GoForWealthSIPErrorMessageEnum.ACCESS_DENIED_MESSAGE.getValue(),null);
	    }
	 	logger.info("Out replaceGoal()");
	 	return goForWealthUMAResponseInfo;
	}

	@GetMapping("/getUserRiskProfileV2/{riskSum}")
	public GoForWealthSIPResponseInfo getUserRiskProfileV2(@PathVariable("riskSum")Integer riskSum,HttpServletRequest request,HttpServletResponse response,Authentication auth) throws GoForWealthSIPException,IOException {
		logger.info("In getUserRiskProfileV2()");
		GoForWealthSIPResponseInfo responseInfo = new GoForWealthSIPResponseInfo();
		ReturnsTypeDTO returnsTypeDTOs = goForWealthSIPService.getUserRiskProfileV2(riskSum);
		if(returnsTypeDTOs != null) {
			responseInfo = GoForWealthSIPUtil.getSuccessResponseInfo(GoForWealthSIPErrorMessageEnum.SUCCESS_CODE.getValue(),GoForWealthSIPErrorMessageEnum.SUCCESS_MESSAGE.getValue(),returnsTypeDTOs);
			responseInfo.setTotalRecords(1);
		}else{
			responseInfo.setMessage(GoForWealthSIPErrorMessageEnum.DATA_NOT_FOUND_MESSAGE.getValue());
			responseInfo.setStatus(GoForWealthSIPErrorMessageEnum.DATA_NOT_FOUND_CODE.getValue());
			responseInfo.setTotalRecords(0);
		}
		logger.info("Out getRiskProfileList()");
		return responseInfo;
	}

	@GetMapping("/getGoalOrderDetailV2")
	public GoForWealthSIPResponseInfo getGoalOrderDetailV2(Authentication auth) throws GoForWealthSIPException {
		logger.info("In getGoalOrderDetailV2()");
		GoForWealthSIPResponseInfo responseInfo = new GoForWealthSIPResponseInfo();
		UserPrincipal userSession = (UserPrincipal)auth.getPrincipal();
		if(userSession != null){
			logger.info("In getGoalOrderDetailV2() with UserId : " + userSession.getUser().getUserId());
			User user = userRepository.findUserByRole(userSession.getUser().getUserId(), "USER");
			if(user != null){
				int userId = userSession.getUser().getUserId();
				int orderId = 0;
				UserGoalDto userGoalDto = goForWealthSIPService.getGoalOrderDetailV2(userId,orderId);
				if (userGoalDto != null) {
					responseInfo = GoForWealthSIPUtil.getSuccessResponseInfo(GoForWealthSIPErrorMessageEnum.SUCCESS_CODE.getValue(),GoForWealthSIPErrorMessageEnum.SUCCESS_MESSAGE.getValue(),userGoalDto);
					responseInfo.setTotalRecords(1);
				}else{
					responseInfo.setMessage(GoForWealthSIPErrorMessageEnum.DATA_NOT_FOUND_MESSAGE.getValue());
					responseInfo.setStatus(GoForWealthSIPErrorMessageEnum.DATA_NOT_FOUND_CODE.getValue());
					responseInfo.setTotalRecords(0);
				}
			}else{
				responseInfo.setMessage(GoForWealthSIPErrorMessageEnum.ACCESS_DENIED_CODE.getValue());
				responseInfo.setStatus(GoForWealthSIPErrorMessageEnum.ACCESS_DENIED_MESSAGE.getValue());
			}
		}else{
			responseInfo.setMessage(GoForWealthSIPErrorMessageEnum.ACCESS_DENIED_CODE.getValue());
			responseInfo.setStatus(GoForWealthSIPErrorMessageEnum.ACCESS_DENIED_MESSAGE.getValue());
		}
		logger.info("Out getGoalOrderDetailV2()");
		return responseInfo;
	}


}
