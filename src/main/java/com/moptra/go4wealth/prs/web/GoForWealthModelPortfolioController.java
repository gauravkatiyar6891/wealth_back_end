
package com.moptra.go4wealth.prs.web;

import java.util.ArrayList;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.moptra.go4wealth.bean.User;
import com.moptra.go4wealth.prs.common.enums.GoForWealthPRSErrorMessageEnum;
import com.moptra.go4wealth.prs.common.exception.GoForWealthPRSException;
import com.moptra.go4wealth.prs.common.rest.GoForWealthPRSResponseInfo;
import com.moptra.go4wealth.prs.common.util.GoForWealthPRSUtil;
import com.moptra.go4wealth.prs.model.AddToCartDTO;
import com.moptra.go4wealth.prs.model.FundSchemeDTO;
import com.moptra.go4wealth.prs.service.GoForWealthModelportfolioService;
import com.moptra.go4wealth.repository.UserRepository;
import com.moptra.go4wealth.security.UserPrincipal;

@RestController
@RequestMapping("${server.contextPath}/prs/modelportfolio/")
public class GoForWealthModelPortfolioController {

	private static Logger logger = LoggerFactory.getLogger(GoForWealthModelPortfolioController.class);

	@Autowired
	GoForWealthModelportfolioService goForWealthModelportfolioService;
	
	@Autowired
	UserRepository userRepository;

	//************** unsecured ***********************************************//
	@RequestMapping(value = "/getModelportfolioList",method = RequestMethod.GET)
	public GoForWealthPRSResponseInfo getModelportfolioList(){
		logger.info("In getModelportfolioList()");
		GoForWealthPRSResponseInfo responseInfo = null;
		List<FundSchemeDTO> fundSchemeDtoList = null;
		fundSchemeDtoList = goForWealthModelportfolioService.getModelportfolioList();
		if(fundSchemeDtoList.size()>0){
			responseInfo = GoForWealthPRSUtil.getSuccessResponseInfo(GoForWealthPRSErrorMessageEnum.SUCCESS_CODE.getValue(),GoForWealthPRSErrorMessageEnum.SUCCESS_MESSAGE.getValue(),fundSchemeDtoList);
		}else{
			responseInfo = GoForWealthPRSUtil.getSuccessResponseInfo(GoForWealthPRSErrorMessageEnum.FAILURE_CODE.getValue(),GoForWealthPRSErrorMessageEnum.FAILURE_MESSAGE.getValue(),fundSchemeDtoList);
		}
		logger.info("Out getModelportfolioList()");
		return responseInfo;
	}

	//************** unsecured **********************************************************************//
	@RequestMapping(value="/getModelportfolioDetailByCategory/{portfolioName}",method=RequestMethod.GET)
	public GoForWealthPRSResponseInfo getModelportfolioDetailByCategory(@PathVariable("portfolioName")String portfolioName){
		logger.info("In getModelportfolioDetailByCategory()");
		GoForWealthPRSResponseInfo responseInfo = null;
		List<FundSchemeDTO> fundSchemeDtoList = null;
		fundSchemeDtoList = goForWealthModelportfolioService.getModelportfolioDetailByCategory(portfolioName);
		if(fundSchemeDtoList.size()>0){
			responseInfo = GoForWealthPRSUtil.getSuccessResponseInfo(GoForWealthPRSErrorMessageEnum.SUCCESS_CODE.getValue(),GoForWealthPRSErrorMessageEnum.SUCCESS_MESSAGE.getValue(),fundSchemeDtoList);
		}else{
			responseInfo = GoForWealthPRSUtil.getSuccessResponseInfo(GoForWealthPRSErrorMessageEnum.FAILURE_CODE.getValue(),GoForWealthPRSErrorMessageEnum.FAILURE_MESSAGE.getValue(),fundSchemeDtoList);
		}
		logger.info("Out getModelportfolioDetailByCategory()");
		return responseInfo;
	}

	//******************* secured *************************************************************************//
	@RequestMapping(value="/getModelportfolioDetailByCategorySecured/{portfolioName}",method=RequestMethod.GET)
	public GoForWealthPRSResponseInfo getModelportfolioDetailByCategorySecured(@PathVariable("portfolioName")String portfolioName,Authentication auth){
		logger.info("In getModelportfolioDetailByCategorySecured()");
		GoForWealthPRSResponseInfo responseInfo = null;
		List<FundSchemeDTO> fundSchemeDtoList = null;
		UserPrincipal userSession = (UserPrincipal)auth.getPrincipal();
		if(userSession!=null){
			logger.info("In getModelportfolioDetailByCategorySecured() with UserId : " + userSession.getUser().getUserId());
			fundSchemeDtoList = goForWealthModelportfolioService.getModelportfolioDetailByCategorySecured(portfolioName,userSession.getUser().getUserId());
			if(fundSchemeDtoList.size()>0){
				responseInfo = GoForWealthPRSUtil.getSuccessResponseInfo(GoForWealthPRSErrorMessageEnum.SUCCESS_CODE.getValue(),GoForWealthPRSErrorMessageEnum.SUCCESS_MESSAGE.getValue(),fundSchemeDtoList);
			}else{
				responseInfo = GoForWealthPRSUtil.getSuccessResponseInfo(GoForWealthPRSErrorMessageEnum.FAILURE_CODE.getValue(),GoForWealthPRSErrorMessageEnum.FAILURE_MESSAGE.getValue(),fundSchemeDtoList);
			}
		}else{
			responseInfo = GoForWealthPRSUtil.getSuccessResponseInfo(GoForWealthPRSErrorMessageEnum.ACCESS_DENIED_CODE.getValue(), GoForWealthPRSErrorMessageEnum.ACCESS_DENIED_MESSAGE.getValue(), null);
		}
		logger.info("Out getModelportfolioDetailByCategorySecured()");
		return responseInfo;
	}

	//***************** secured ************************************//
	@RequestMapping(value="/api/addToCart", method=RequestMethod.POST)
	public GoForWealthPRSResponseInfo addToCart(@RequestBody List<AddToCartDTO> addToCartDTO, Authentication auth) {
		logger.info("In addToCart()");
		UserPrincipal userSession = (UserPrincipal)auth.getPrincipal();
		GoForWealthPRSResponseInfo responseInfo = null;
		if(userSession!=null){
			logger.info("In addToCart() with UserId : " + userSession.getUser().getUserId());
			User user = userRepository.findUserByRole(userSession.getUser().getUserId(), "USER");
			if(user !=null){
				addToCartDTO = goForWealthModelportfolioService.addToCart(addToCartDTO,user);
				responseInfo = GoForWealthPRSUtil.getSuccessResponseInfo(GoForWealthPRSErrorMessageEnum.SUCCESS_CODE.getValue(), GoForWealthPRSErrorMessageEnum.SUCCESS_MESSAGE.getValue(), addToCartDTO);
			}else{
				responseInfo = GoForWealthPRSUtil.getSuccessResponseInfo(GoForWealthPRSErrorMessageEnum.ACCESS_DENIED_CODE.getValue(), GoForWealthPRSErrorMessageEnum.ACCESS_DENIED_MESSAGE.getValue(), null);
			}
		}else{
			responseInfo = GoForWealthPRSUtil.getSuccessResponseInfo(GoForWealthPRSErrorMessageEnum.ACCESS_DENIED_CODE.getValue(), GoForWealthPRSErrorMessageEnum.ACCESS_DENIED_MESSAGE.getValue(), null);
		}
		logger.info("Out addToCart()");
		return responseInfo;
	}

	//****************** secured url ***********************************//
	@RequestMapping(value="/api/confirmOrder", method=RequestMethod.POST)
	public GoForWealthPRSResponseInfo confirmOrder(@RequestBody List<AddToCartDTO> addToCartDTOs, Authentication auth) {
		logger.info("In confirmOrder()");
		UserPrincipal userSession = (UserPrincipal)auth.getPrincipal();
		List<AddToCartDTO> orderList = null;
		GoForWealthPRSResponseInfo responseInfo = null;		
		if(userSession!=null){
			logger.info("In confirmOrder() with UserId : " + userSession.getUser().getUserId());
			User user = userRepository.findUserByRole(userSession.getUser().getUserId(), "USER");
			if(user != null){
				orderList = goForWealthModelportfolioService.confirmOrder(userSession.getUser().getUserId(), addToCartDTOs);
				responseInfo = GoForWealthPRSUtil.getSuccessResponseInfo(GoForWealthPRSErrorMessageEnum.SUCCESS_CODE.getValue(), GoForWealthPRSErrorMessageEnum.SUCCESS_MESSAGE.getValue(), orderList);
				
			}else{
				responseInfo = GoForWealthPRSUtil.getSuccessResponseInfo(GoForWealthPRSErrorMessageEnum.ACCESS_DENIED_CODE.getValue(), GoForWealthPRSErrorMessageEnum.ACCESS_DENIED_MESSAGE.getValue(), null);
			}	
		}else{
			responseInfo = GoForWealthPRSUtil.getSuccessResponseInfo(GoForWealthPRSErrorMessageEnum.ACCESS_DENIED_CODE.getValue(), GoForWealthPRSErrorMessageEnum.ACCESS_DENIED_MESSAGE.getValue(), null);
		}
		logger.info("Out confirmOrder()");
		return responseInfo;
	}

	//********************************** secured url *****************//
	@RequestMapping(value="/api/cancelOrder", method=RequestMethod.POST)
	public GoForWealthPRSResponseInfo cancelOrder(@RequestBody List<AddToCartDTO> addToCartDTOs, Authentication auth) {
		logger.info("In cancelOrder()");
		UserPrincipal userSession = (UserPrincipal)auth.getPrincipal();		
		GoForWealthPRSResponseInfo responseInfo = null;
		List<AddToCartDTO> orderList = null;
		if(userSession!=null){
			logger.info("In cancelOrder() with UserId : " + userSession.getUser().getUserId());
			User user = userRepository.findUserByRole(userSession.getUser().getUserId(), "USER");
			if(user != null){
				orderList = goForWealthModelportfolioService.cancelOrder(addToCartDTOs,userSession.getUser().getUserId());
				responseInfo = GoForWealthPRSUtil.getSuccessResponseInfo(GoForWealthPRSErrorMessageEnum.SUCCESS_CODE.getValue(), GoForWealthPRSErrorMessageEnum.SUCCESS_MESSAGE.getValue(), orderList);
			}else{
				responseInfo = GoForWealthPRSUtil.getSuccessResponseInfo(GoForWealthPRSErrorMessageEnum.ACCESS_DENIED_CODE.getValue(), GoForWealthPRSErrorMessageEnum.ACCESS_DENIED_MESSAGE.getValue(), null);
			}
		}else{
			responseInfo = GoForWealthPRSUtil.getSuccessResponseInfo(GoForWealthPRSErrorMessageEnum.ACCESS_DENIED_CODE.getValue(), GoForWealthPRSErrorMessageEnum.ACCESS_DENIED_MESSAGE.getValue(), null);
		}
		logger.info("Out cancelOrder()");
		return responseInfo;
	}

	//************* secured url **************************************//
	@RequestMapping(value="/api/deleteOrder", method=RequestMethod.POST)
	public GoForWealthPRSResponseInfo deleteOrder(@RequestBody Integer orderId, Authentication auth) {
		logger.info("In deleteOrder()");
		UserPrincipal userSession = (UserPrincipal)auth.getPrincipal();
		String response="Failure";
		GoForWealthPRSResponseInfo responseInfo = null;		
		if(userSession!=null){
			logger.info("In deleteOrder() with UserId : " + userSession.getUser().getUserId());
			User user = userRepository.findUserByRole(userSession.getUser().getUserId(), "USER");
			if(user != null){
				response = goForWealthModelportfolioService.deleteOrder(userSession.getUser().getUserId(), orderId);
				responseInfo = GoForWealthPRSUtil.getSuccessResponseInfo(GoForWealthPRSErrorMessageEnum.SUCCESS_CODE.getValue(), GoForWealthPRSErrorMessageEnum.SUCCESS_MESSAGE.getValue(), response);
			}else{
				responseInfo = GoForWealthPRSUtil.getSuccessResponseInfo(GoForWealthPRSErrorMessageEnum.ACCESS_DENIED_CODE.getValue(),GoForWealthPRSErrorMessageEnum.ACCESS_DENIED_MESSAGE.getValue(), null);
			}
		}else{
			responseInfo = GoForWealthPRSUtil.getSuccessResponseInfo(GoForWealthPRSErrorMessageEnum.ACCESS_DENIED_CODE.getValue(),GoForWealthPRSErrorMessageEnum.ACCESS_DENIED_MESSAGE.getValue(), null);
		}
		logger.info("Out deleteOrder()");
		return responseInfo;
	}

	//********************************* secured url **********************************//
	@RequestMapping(value="/api/getCartOrderByOrder/{bundelId}", method=RequestMethod.GET)
	public GoForWealthPRSResponseInfo getCartOrderByOrder(@PathVariable ("bundelId") int bundelId , Authentication auth) {
		logger.info("In getCartOrderByOrder()");
		UserPrincipal userSession = (UserPrincipal)auth.getPrincipal();
		GoForWealthPRSResponseInfo responseInfo = null;
		List<AddToCartDTO> addToCartDTO = new ArrayList<AddToCartDTO>();
		if(userSession!=null){
			logger.info("In getCartOrderByOrder() with UserId :  " + userSession.getUser().getUserId());
			User user = userRepository.findUserByRole(userSession.getUser().getUserId(), "USER");
			if(user != null){
				addToCartDTO = goForWealthModelportfolioService.getCartOrderByOrder(userSession.getUser().getUserId(), bundelId);
				responseInfo = GoForWealthPRSUtil.getSuccessResponseInfo(GoForWealthPRSErrorMessageEnum.SUCCESS_CODE.getValue(), GoForWealthPRSErrorMessageEnum.SUCCESS_MESSAGE.getValue(), addToCartDTO);
			}else{
				responseInfo = GoForWealthPRSUtil.getSuccessResponseInfo(GoForWealthPRSErrorMessageEnum.ACCESS_DENIED_CODE.getValue(), GoForWealthPRSErrorMessageEnum.ACCESS_DENIED_MESSAGE.getValue(), null);
			}
		}else{
			responseInfo = GoForWealthPRSUtil.getSuccessResponseInfo(GoForWealthPRSErrorMessageEnum.ACCESS_DENIED_CODE.getValue(), GoForWealthPRSErrorMessageEnum.ACCESS_DENIED_MESSAGE.getValue(), null);
		}
		logger.info("Out getCartOrderByOrder()");
		return responseInfo;
	}
	
	//******************* secured url ****************************//
	@RequestMapping(value="/api/payment", method=RequestMethod.POST)
	public GoForWealthPRSResponseInfo makePayment(@RequestBody List<AddToCartDTO> addToCartDTO ,Authentication auth) throws GoForWealthPRSException {
		logger.info("In makePayment()");
		UserPrincipal userSession = (UserPrincipal)auth.getPrincipal();
		String response="Failure";
		GoForWealthPRSResponseInfo responseInfo = null;
		if(userSession!=null){
			logger.info("In makePayment() with UserId : " + userSession.getUser().getUserId());
			User user = userRepository.findUserByRole(userSession.getUser().getUserId(), "USER");
			if(user != null){
				response = goForWealthModelportfolioService.makePayment(addToCartDTO, userSession.getUser().getUserId());
				responseInfo = GoForWealthPRSUtil.getSuccessResponseInfo(GoForWealthPRSErrorMessageEnum.SUCCESS_CODE.getValue(), GoForWealthPRSErrorMessageEnum.SUCCESS_MESSAGE.getValue(),response);
			}else{
				responseInfo = GoForWealthPRSUtil.getSuccessResponseInfo(GoForWealthPRSErrorMessageEnum.ACCESS_DENIED_CODE.getValue(), GoForWealthPRSErrorMessageEnum.ACCESS_DENIED_MESSAGE.getValue(),null);
			}
		}else{
			responseInfo = GoForWealthPRSUtil.getSuccessResponseInfo(GoForWealthPRSErrorMessageEnum.ACCESS_DENIED_CODE.getValue(),GoForWealthPRSErrorMessageEnum.ACCESS_DENIED_MESSAGE.getValue(),null);
		}
		logger.info("Out makePayment()");
		return responseInfo;
	}

	//********************* secured url ***********************************//
	@RequestMapping(value="/api/getPaymentStatus", method=RequestMethod.POST)
	public GoForWealthPRSResponseInfo getPaymentStatus(@RequestBody Integer mpBundleId, Authentication auth) {
		logger.info("In getPaymentStatus()");
		UserPrincipal userSession = (UserPrincipal)auth.getPrincipal();
		GoForWealthPRSResponseInfo responseInfo = null;
		List<AddToCartDTO>  response = null;
		if(userSession!=null){
			logger.info("Out getPaymentStatus() with UserId : " + userSession.getUser().getUserId());
			User user = userRepository.findUserByRole(userSession.getUser().getUserId(), "USER");
			if(user != null){
				response = goForWealthModelportfolioService.getPaymentStatus(mpBundleId);
				if(response.equals("")) {
					responseInfo = new GoForWealthPRSResponseInfo();
					responseInfo.setStatus(GoForWealthPRSErrorMessageEnum.DATA_NOT_FOUND_CODE.getValue());
					responseInfo.setMessage(GoForWealthPRSErrorMessageEnum.DATA_NOT_FOUND_MESSAGE.getValue());
				}else {
					responseInfo = GoForWealthPRSUtil.getSuccessResponseInfo(GoForWealthPRSErrorMessageEnum.SUCCESS_CODE.getValue(), GoForWealthPRSErrorMessageEnum.SUCCESS_MESSAGE.getValue(), response);
				}
			}else{
				responseInfo = GoForWealthPRSUtil.getSuccessResponseInfo(GoForWealthPRSErrorMessageEnum.ACCESS_DENIED_CODE.getValue(),GoForWealthPRSErrorMessageEnum.ACCESS_DENIED_MESSAGE.getValue(), null);
			}
		}else{
			responseInfo = GoForWealthPRSUtil.getSuccessResponseInfo(GoForWealthPRSErrorMessageEnum.ACCESS_DENIED_CODE.getValue(),GoForWealthPRSErrorMessageEnum.ACCESS_DENIED_MESSAGE.getValue(), null);
		}
		logger.info("Out getPaymentStatus()");
		return responseInfo;
	}

	//*********************** secured url **********************************//
	@RequestMapping(value="/api/getUserOrderByType", method=RequestMethod.GET)
	public GoForWealthPRSResponseInfo getUserOrderByType(@RequestParam("type") String type , Authentication auth) {
		logger.info("In getUserOrderByType()");
		UserPrincipal userSession = (UserPrincipal)auth.getPrincipal();
		GoForWealthPRSResponseInfo responseInfo = null;
		List<AddToCartDTO> addToCartDTO = new ArrayList<AddToCartDTO>();
		if(userSession!=null){
			logger.info("In getUserOrderByType() with UserId : " + userSession.getUser().getUserId());
			User user = userRepository.findUserByRole(userSession.getUser().getUserId(), "USER");
			if(user != null){
				addToCartDTO = goForWealthModelportfolioService.getUserOrderByType(userSession.getUser().getUserId(),type);
				responseInfo = GoForWealthPRSUtil.getSuccessResponseInfo(GoForWealthPRSErrorMessageEnum.SUCCESS_CODE.getValue(), GoForWealthPRSErrorMessageEnum.SUCCESS_MESSAGE.getValue(), addToCartDTO);
			}else{
				responseInfo = GoForWealthPRSUtil.getSuccessResponseInfo(GoForWealthPRSErrorMessageEnum.ACCESS_DENIED_CODE.getValue(), GoForWealthPRSErrorMessageEnum.ACCESS_DENIED_MESSAGE.getValue(), null);
			}
		}else{
			responseInfo = GoForWealthPRSUtil.getSuccessResponseInfo(GoForWealthPRSErrorMessageEnum.ACCESS_DENIED_CODE.getValue(), GoForWealthPRSErrorMessageEnum.ACCESS_DENIED_MESSAGE.getValue(), null);
		}
		logger.info("Out getUserOrderByType()");
		return responseInfo;
	}

	//******************************* secured url **********************************//
	@RequestMapping(value="/api/getUserOrderListByBundleId", method=RequestMethod.POST)
	public GoForWealthPRSResponseInfo getUserOrderListByBundleId(@RequestBody Integer bundleId , Authentication auth) {
		logger.info("In getUserOrderListByBundleId()");
		UserPrincipal userSession = (UserPrincipal)auth.getPrincipal();
		GoForWealthPRSResponseInfo responseInfo = null;
		List<AddToCartDTO> addToCartDTO = new ArrayList<AddToCartDTO>();
		if(userSession!=null){
			logger.info("In getUserOrderListByBundleId() with UserId : " + userSession.getUser().getUserId());
			User user = userRepository.findUserByRole(userSession.getUser().getUserId(), "USER");
			if(user != null){
				addToCartDTO = goForWealthModelportfolioService.getUserOrderListByBundleId(userSession.getUser().getUserId(),bundleId);
				responseInfo = GoForWealthPRSUtil.getSuccessResponseInfo(GoForWealthPRSErrorMessageEnum.SUCCESS_CODE.getValue(), GoForWealthPRSErrorMessageEnum.SUCCESS_MESSAGE.getValue(), addToCartDTO);
			}else{
				responseInfo = GoForWealthPRSUtil.getSuccessResponseInfo(GoForWealthPRSErrorMessageEnum.ACCESS_DENIED_CODE.getValue(), GoForWealthPRSErrorMessageEnum.ACCESS_DENIED_MESSAGE.getValue(), null);
			}
		}else{
			responseInfo = GoForWealthPRSUtil.getSuccessResponseInfo(GoForWealthPRSErrorMessageEnum.ACCESS_DENIED_CODE.getValue(), GoForWealthPRSErrorMessageEnum.ACCESS_DENIED_MESSAGE.getValue(), null);
		}
		logger.info("Out getUserOrderListByBundleId()");
		return responseInfo;
	}

	//**************** secured url ***********************************//
	@RequestMapping(value="/api/getUserOrder", method=RequestMethod.GET)
	public GoForWealthPRSResponseInfo getUserOrder(Authentication auth) {
		logger.info("In getUserOrder()");
		UserPrincipal userSession = (UserPrincipal)auth.getPrincipal();
		GoForWealthPRSResponseInfo responseInfo = null;
		if(userSession!=null){
			logger.info("In getUserOrder() with UserId : " + userSession.getUser().getUserId());
			User user = userRepository.findUserByRole(userSession.getUser().getUserId(), "USER");
			if(user != null){
				FundSchemeDTO fundSchemeDTO = goForWealthModelportfolioService.getUserOrderByType(user);
				responseInfo = GoForWealthPRSUtil.getSuccessResponseInfo(GoForWealthPRSErrorMessageEnum.SUCCESS_CODE.getValue(), GoForWealthPRSErrorMessageEnum.SUCCESS_MESSAGE.getValue(), fundSchemeDTO);
			}else{
				responseInfo = GoForWealthPRSUtil.getSuccessResponseInfo(GoForWealthPRSErrorMessageEnum.ACCESS_DENIED_CODE.getValue(), GoForWealthPRSErrorMessageEnum.ACCESS_DENIED_MESSAGE.getValue(), null);
			}
		}else{
			responseInfo = GoForWealthPRSUtil.getSuccessResponseInfo(GoForWealthPRSErrorMessageEnum.ACCESS_DENIED_CODE.getValue(), GoForWealthPRSErrorMessageEnum.ACCESS_DENIED_MESSAGE.getValue(), null);
		}
		logger.info("Out getUserOrder()");
		return responseInfo;
	}


}