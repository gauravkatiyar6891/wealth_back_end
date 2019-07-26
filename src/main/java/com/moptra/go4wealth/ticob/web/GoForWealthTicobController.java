package com.moptra.go4wealth.ticob.web;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.moptra.go4wealth.bean.User;
import com.moptra.go4wealth.prs.model.AddToCartDTO;
import com.moptra.go4wealth.repository.UserRepository;
import com.moptra.go4wealth.security.UserPrincipal;
import com.moptra.go4wealth.ticob.common.enums.GoForWealthTICOBErrorMessageEnum;
import com.moptra.go4wealth.ticob.common.rest.GoForWealthTICOBResponseInfo;
import com.moptra.go4wealth.ticob.common.util.GoForWealthTICOBUtil;
import com.moptra.go4wealth.ticob.model.TransferInRecordDTO;
import com.moptra.go4wealth.ticob.service.GoForWealthTicobService;

@RestController
@RequestMapping("${server.contextPath}/ticob")
public class GoForWealthTicobController {

	private static Logger ticobLogger = LoggerFactory.getLogger(GoForWealthTicobController.class);

	@Autowired
	GoForWealthTicobService goForWealthTicobService;

	@Autowired
	UserRepository userRepository;

	@GetMapping("/getTransferInRecords")
	public GoForWealthTICOBResponseInfo getTransferInRecords(Authentication auth) {
		ticobLogger.info("In getTransferInRecords()");
		GoForWealthTICOBResponseInfo goForWealthTICOBResponse = null;
		UserPrincipal userSession = (UserPrincipal)auth.getPrincipal();
		if(userSession != null){
			ticobLogger.info("In getTransferInRecords() with UserId : " + userSession.getUser().getUserId());
			User user = userRepository.findUserByRole(userSession.getUser().getUserId(), "USER");
			if(user != null){
				List<TransferInRecordDTO> transferInRecords = goForWealthTicobService.getTransferInRecord(userSession.getUser().getUserId());
				goForWealthTICOBResponse = GoForWealthTICOBUtil.getSuccessResponseInfo(GoForWealthTICOBErrorMessageEnum.SUCCESS_CODE.getValue(), GoForWealthTICOBErrorMessageEnum.SUCCESS_MESSAGE.getValue(),transferInRecords);
			}else{
				goForWealthTICOBResponse = GoForWealthTICOBUtil.getErrorResponseInfo(GoForWealthTICOBErrorMessageEnum.ACCESS_DENIED_CODE.getValue(),GoForWealthTICOBErrorMessageEnum.ACCESS_DENIED_MESSAGE.getValue(),null);
			}
		}else{
			goForWealthTICOBResponse = GoForWealthTICOBUtil.getErrorResponseInfo(GoForWealthTICOBErrorMessageEnum.ACCESS_DENIED_CODE.getValue(),GoForWealthTICOBErrorMessageEnum.ACCESS_DENIED_MESSAGE.getValue(),null);
		}
		ticobLogger.info("Out getTransferInRecords()");
		return goForWealthTICOBResponse;
	}

	@PostMapping("/placeTicobSipOrder")
	public GoForWealthTICOBResponseInfo placeTicobSipOrder(@RequestBody AddToCartDTO addToCartDTO,Authentication authentication){
		ticobLogger.info("In placeTicobSipOrder");
		GoForWealthTICOBResponseInfo goForWealthTICOBResponseInfo = null;
		UserPrincipal userSession = (UserPrincipal) authentication.getPrincipal();
		if(userSession != null){
			ticobLogger.info("In placeTicobSipOrder() with UserId : " + userSession.getUser().getUserId());
			User user = userSession.getUser();
			String response = "";
			if(user != null){
				response = goForWealthTicobService.placeTicobSipOrder(addToCartDTO,user);
				if(response.equals("Failed")){
					goForWealthTICOBResponseInfo = GoForWealthTICOBUtil.getSuccessResponseInfo(GoForWealthTICOBErrorMessageEnum.FAILURE_CODE.getValue(),GoForWealthTICOBErrorMessageEnum.FAILURE_MESSAGE.getValue(),response);
				}else{
					goForWealthTICOBResponseInfo = GoForWealthTICOBUtil.getSuccessResponseInfo(GoForWealthTICOBErrorMessageEnum.SUCCESS_CODE.getValue(),GoForWealthTICOBErrorMessageEnum.SUCCESS_MESSAGE.getValue(),response);
				}
			}else{
				goForWealthTICOBResponseInfo = GoForWealthTICOBUtil.getErrorResponseInfo(GoForWealthTICOBErrorMessageEnum.ACCESS_DENIED_CODE.getValue(),GoForWealthTICOBErrorMessageEnum.ACCESS_DENIED_MESSAGE.getValue(),null);
			}
		}else{
			goForWealthTICOBResponseInfo = GoForWealthTICOBUtil.getErrorResponseInfo(GoForWealthTICOBErrorMessageEnum.ACCESS_DENIED_CODE.getValue(),GoForWealthTICOBErrorMessageEnum.ACCESS_DENIED_MESSAGE.getValue(),null);
		}
		ticobLogger.info("Out placeTicobSipOrder");
		return goForWealthTICOBResponseInfo;
	}

	@PostMapping("/placeTicobLumpsumOrder")
	public GoForWealthTICOBResponseInfo placeTicobLumpsumOrder(@RequestBody AddToCartDTO addToCartDTO, Authentication authentication){
		ticobLogger.info("In placeLumpsumOrder");
		GoForWealthTICOBResponseInfo goForWealthTICOBResponseInfo = null;
		UserPrincipal userSession = (UserPrincipal) authentication.getPrincipal();
		if(userSession != null){
			ticobLogger.info("In placeLumpsumOrder() with UserId : " + userSession.getUser().getUserId());
			User user = userSession.getUser();
			String response = "";
			if(user != null){
				response = goForWealthTicobService.placeTicobLumpsumOrder(addToCartDTO,user);
				if(response.equals("Failed")){
					goForWealthTICOBResponseInfo = GoForWealthTICOBUtil.getSuccessResponseInfo(GoForWealthTICOBErrorMessageEnum.FAILURE_CODE.getValue(),GoForWealthTICOBErrorMessageEnum.FAILURE_MESSAGE.getValue(),response);
				}else{
					goForWealthTICOBResponseInfo = GoForWealthTICOBUtil.getSuccessResponseInfo(GoForWealthTICOBErrorMessageEnum.SUCCESS_CODE.getValue(),GoForWealthTICOBErrorMessageEnum.SUCCESS_MESSAGE.getValue(),response);
				}
			}else{
				goForWealthTICOBResponseInfo = GoForWealthTICOBUtil.getErrorResponseInfo(GoForWealthTICOBErrorMessageEnum.ACCESS_DENIED_CODE.getValue(),GoForWealthTICOBErrorMessageEnum.ACCESS_DENIED_MESSAGE.getValue(),null);
			}
		}else{
			goForWealthTICOBResponseInfo = GoForWealthTICOBUtil.getErrorResponseInfo(GoForWealthTICOBErrorMessageEnum.ACCESS_DENIED_CODE.getValue(),GoForWealthTICOBErrorMessageEnum.ACCESS_DENIED_MESSAGE.getValue(),null);
		}
		ticobLogger.info("Out placeLumpsumOrder");
		return goForWealthTICOBResponseInfo;
	}


}