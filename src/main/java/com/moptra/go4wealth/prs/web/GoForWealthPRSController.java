package com.moptra.go4wealth.prs.web;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.moptra.go4wealth.bean.User;
import com.moptra.go4wealth.prs.common.constant.GoForWealthPRSConstants;
import com.moptra.go4wealth.prs.common.enums.GoForWealthPRSErrorMessageEnum;
import com.moptra.go4wealth.prs.common.exception.GoForWealthPRSException;
import com.moptra.go4wealth.prs.common.rest.GoForWealthPRSResponseInfo;
import com.moptra.go4wealth.prs.common.util.GoForWealthPRSUtil;
import com.moptra.go4wealth.prs.model.SchemeRecentViewDTO;
import com.moptra.go4wealth.prs.model.UserPortfolioDataDTO;
import com.moptra.go4wealth.prs.payment.PaymentService;
import com.moptra.go4wealth.prs.service.GoForWealthPRSService;
import com.moptra.go4wealth.repository.UserRepository;
import com.moptra.go4wealth.security.UserPrincipal;

@RestController
@RequestMapping("${server.contextPath}/prs/")
public class GoForWealthPRSController {

	@Autowired 
	GoForWealthPRSService goForWealthPRSService;

	@Autowired
	PaymentService paymentService;

	@Autowired
	UserRepository userRepository;

	private static Logger logger = LoggerFactory.getLogger(GoForWealthPRSController.class);

	@GetMapping("/ekycStatus")
	public GoForWealthPRSResponseInfo checkEkycStatus(Authentication auth) {
		logger.info("Inside checkEkycStatus()");
		UserPrincipal userPrincipal = (UserPrincipal)auth.getPrincipal();
		String message = null;
		if(userPrincipal != null) {
			User user = userRepository.findUserByRole(userPrincipal.getUser().getUserId(), "USER");
			if(user != null){
				message = goForWealthPRSService.checkEkycStatus(userPrincipal.getUser().getUserId());
				if(message == null) {
					GoForWealthPRSResponseInfo goForWealthPRSResponseInfo = new GoForWealthPRSResponseInfo();
					goForWealthPRSResponseInfo.setStatus(GoForWealthPRSErrorMessageEnum.DATA_NOT_FOUND_CODE.getValue());
					goForWealthPRSResponseInfo.setMessage(GoForWealthPRSErrorMessageEnum.DATA_NOT_FOUND_MESSAGE.getValue());
					goForWealthPRSResponseInfo.setTotalRecords(0);
					goForWealthPRSResponseInfo.setData("");
					return goForWealthPRSResponseInfo;
				}
			}else{
				message = GoForWealthPRSConstants.ACCESS_DENIED;
			}
		} else {
			message = GoForWealthPRSConstants.WRONG_TOKEN;
		}
		logger.info("Out checkEkycStatus()");
		return GoForWealthPRSUtil.getSuccessResponseInfo(GoForWealthPRSErrorMessageEnum.SUCCESS_CODE.getValue(), GoForWealthPRSErrorMessageEnum.SUCCESS_MESSAGE.getValue(), message);
	}

	@PostMapping("/addToWatchlist/{schemeCode}")
	public GoForWealthPRSResponseInfo addToWatchlist(@PathVariable("schemeCode")String schemeCode,Authentication auth) throws GoForWealthPRSException {
		logger.info("In addToWatchlist()");
		UserPrincipal userSession = (UserPrincipal)auth.getPrincipal();
		GoForWealthPRSResponseInfo goForWealthPRSResponseInfo = new GoForWealthPRSResponseInfo();
		if(userSession == null){
			goForWealthPRSResponseInfo.setStatus(GoForWealthPRSErrorMessageEnum.ACCESS_DENIED_CODE.getValue());
			goForWealthPRSResponseInfo.setMessage(GoForWealthPRSErrorMessageEnum.ACCESS_DENIED_MESSAGE.getValue());
		}else{
			User user = userRepository.findUserByRole(userSession.getUser().getUserId(), "USER");
			if(user !=null){
				logger.info("In addToWatchlist() with userId: " +  userSession.getUser().getUserId());
				boolean isSaved = goForWealthPRSService.addToWatchlist(userSession.getUser().getUserId(),schemeCode);
				if(isSaved){
					goForWealthPRSResponseInfo.setStatus(GoForWealthPRSErrorMessageEnum.SUCCESS_CODE.getValue());
					goForWealthPRSResponseInfo.setMessage(GoForWealthPRSErrorMessageEnum.SUCCESS_MESSAGE.getValue());
				}else{
					goForWealthPRSResponseInfo.setStatus(GoForWealthPRSErrorMessageEnum.FAILURE_CODE.getValue());
					goForWealthPRSResponseInfo.setMessage(GoForWealthPRSErrorMessageEnum.FAILURE_MESSAGE.getValue());
				}
			}else{
				goForWealthPRSResponseInfo.setStatus(GoForWealthPRSErrorMessageEnum.ACCESS_DENIED_CODE.getValue());
				goForWealthPRSResponseInfo.setMessage(GoForWealthPRSErrorMessageEnum.ACCESS_DENIED_MESSAGE.getValue());
			}
		}
		logger.info("Out addToWatchlist()");
		return goForWealthPRSResponseInfo;
	}

	@GetMapping("/removeAndPurchaseFromWatchlist")
	public GoForWealthPRSResponseInfo removeAndPurchaseFromWatchlist(@RequestParam("operationType")String operationType,@RequestParam("schemeCode")String schemeCode,Authentication auth){
		logger.info("In removeAndPurchaseFromWatchlist()");
		UserPrincipal userSession = (UserPrincipal)auth.getPrincipal();
		GoForWealthPRSResponseInfo goForWealthPRSResponseInfo = new GoForWealthPRSResponseInfo();
		if(userSession == null){
			goForWealthPRSResponseInfo.setStatus(GoForWealthPRSErrorMessageEnum.UNAUTHORIZED_CODE.getValue());
			goForWealthPRSResponseInfo.setMessage(GoForWealthPRSErrorMessageEnum.UNAUTHORIZED_MESSAGE.getValue());
		}else{
			User user = userRepository.findUserByRole(userSession.getUser().getUserId(), "USER");
			if(user != null){
				logger.info("In removeAndPurchaseFromWatchlist() with userId: " + userSession.getUser().getUserId());
				boolean isEffected = goForWealthPRSService.removeAndPurchaseFromWatchlist(userSession.getUser().getUserId(),schemeCode,operationType);
				if(isEffected){
					goForWealthPRSResponseInfo.setStatus(GoForWealthPRSErrorMessageEnum.SUCCESS_CODE.getValue());
					goForWealthPRSResponseInfo.setMessage(GoForWealthPRSErrorMessageEnum.SUCCESS_MESSAGE.getValue());
				}else{
					goForWealthPRSResponseInfo.setStatus(GoForWealthPRSErrorMessageEnum.FAILURE_CODE.getValue());
					goForWealthPRSResponseInfo.setMessage(GoForWealthPRSErrorMessageEnum.FAILURE_MESSAGE.getValue());
				}
			}else{
				goForWealthPRSResponseInfo.setStatus(GoForWealthPRSErrorMessageEnum.ACCESS_DENIED_CODE.getValue());
				goForWealthPRSResponseInfo.setMessage(GoForWealthPRSErrorMessageEnum.ACCESS_DENIED_MESSAGE.getValue());
			}
		}
		logger.info("Out removeAndPurchaseFromWatchlist()");
		return goForWealthPRSResponseInfo;
	}

	@GetMapping("/getWatchlist")
	public GoForWealthPRSResponseInfo getWatchlist(Authentication auth){
		logger.info("In getWatchlist()");
		UserPrincipal userSession = (UserPrincipal)auth.getPrincipal();
		GoForWealthPRSResponseInfo goForWealthPRSResponseInfo = new GoForWealthPRSResponseInfo();
		if(userSession == null){
			goForWealthPRSResponseInfo.setStatus(GoForWealthPRSErrorMessageEnum.ACCESS_DENIED_CODE.getValue());
			goForWealthPRSResponseInfo.setMessage(GoForWealthPRSErrorMessageEnum.ACCESS_DENIED_MESSAGE.getValue());
		}else{
			User user = userRepository.findUserByRole(userSession.getUser().getUserId(), "USER");
			if(user != null){
				logger.info("In getWatchlist() with userId: " + userSession.getUser().getUserId());
				List<SchemeRecentViewDTO> watchlistDto = goForWealthPRSService.getWatchlist(userSession.getUser().getUserId());
				if(watchlistDto.isEmpty()){
					goForWealthPRSResponseInfo.setStatus(GoForWealthPRSErrorMessageEnum.DATA_NOT_FOUND_CODE.getValue());
					goForWealthPRSResponseInfo.setMessage(GoForWealthPRSErrorMessageEnum.DATA_NOT_FOUND_MESSAGE.getValue());
				}else{
					goForWealthPRSResponseInfo.setStatus(GoForWealthPRSErrorMessageEnum.SUCCESS_CODE.getValue());
					goForWealthPRSResponseInfo.setMessage(GoForWealthPRSErrorMessageEnum.SUCCESS_MESSAGE.getValue());
					goForWealthPRSResponseInfo.setData(watchlistDto);
					goForWealthPRSResponseInfo.setTotalRecords(watchlistDto.size());
				}
			}else{
				goForWealthPRSResponseInfo.setStatus(GoForWealthPRSErrorMessageEnum.ACCESS_DENIED_CODE.getValue());
				goForWealthPRSResponseInfo.setMessage(GoForWealthPRSErrorMessageEnum.ACCESS_DENIED_MESSAGE.getValue());
			}
		}
		logger.info("Out getWatchlist()");
		return goForWealthPRSResponseInfo;
	}

	/*
	@GetMapping("/getUserDailyPortfolioData")
	public GoForWealthPRSResponseInfo getUserPortfolioData(Authentication auth){
		logger.info("In getUserDailyPortfolioData()");
		UserPrincipal userSession = (UserPrincipal)auth.getPrincipal();
		GoForWealthPRSResponseInfo goForWealthPRSResponseInfo = new GoForWealthPRSResponseInfo();
		if(userSession == null){
			goForWealthPRSResponseInfo.setStatus(GoForWealthPRSErrorMessageEnum.UNAUTHORIZED_CODE.getValue());
			goForWealthPRSResponseInfo.setMessage(GoForWealthPRSErrorMessageEnum.UNAUTHORIZED_MESSAGE.getValue());
		}else{
			logger.info("In getUserPortfolioData() with userId: " + userSession.getUser().getUserId());
			List<UserPortfolioDataDTO> userPortfolioDataDto = goForWealthPRSService.getUserDailyPortfolioData(userSession.getUser().getUserId());
			if(userPortfolioDataDto.isEmpty()){
				goForWealthPRSResponseInfo.setStatus(GoForWealthPRSErrorMessageEnum.DATA_NOT_FOUND_CODE.getValue());
				goForWealthPRSResponseInfo.setMessage(GoForWealthPRSErrorMessageEnum.DATA_NOT_FOUND_MESSAGE.getValue());
			}else{
				goForWealthPRSResponseInfo.setStatus(GoForWealthPRSErrorMessageEnum.SUCCESS_CODE.getValue());
				goForWealthPRSResponseInfo.setMessage(GoForWealthPRSErrorMessageEnum.SUCCESS_MESSAGE.getValue());
				goForWealthPRSResponseInfo.setData(userPortfolioDataDto);
				goForWealthPRSResponseInfo.setTotalRecords(userPortfolioDataDto.size());
			}
		}
		logger.info("Out getUserPortfolioData()");
		return goForWealthPRSResponseInfo;
	}
	*/



}