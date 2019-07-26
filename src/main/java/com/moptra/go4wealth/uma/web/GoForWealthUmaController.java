package com.moptra.go4wealth.uma.web;

import java.io.IOException;
import java.time.LocalDateTime;
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
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.moptra.go4wealth.admin.common.enums.GoForWealthAdminErrorMessageEnum;
import com.moptra.go4wealth.admin.common.exception.GoForWealthAdminException;
import com.moptra.go4wealth.admin.common.rest.GoForWealthAdminResponseInfo;
import com.moptra.go4wealth.admin.common.util.GoForWealthAdminUtil;
import com.moptra.go4wealth.admin.model.BlogCategoryDTO;
import com.moptra.go4wealth.admin.model.BlogDTO;
import com.moptra.go4wealth.bean.User;
import com.moptra.go4wealth.bean.UserRole;
import com.moptra.go4wealth.configuration.JwtConfiguration;
import com.moptra.go4wealth.prs.common.enums.GoForWealthPRSErrorMessageEnum;
import com.moptra.go4wealth.repository.UserRepository;
import com.moptra.go4wealth.security.JwtUserProfileAuthenticationFilter;
import com.moptra.go4wealth.security.UserPrincipal;
import com.moptra.go4wealth.security.UserSession;
import com.moptra.go4wealth.sip.common.enums.GoForWealthSIPErrorMessageEnum;
import com.moptra.go4wealth.sip.common.exception.GoForWealthSIPException;
import com.moptra.go4wealth.uma.common.constant.GoForWealthUMAConstants;
import com.moptra.go4wealth.uma.common.enums.GoForWealthErrorMessageEnum;
import com.moptra.go4wealth.uma.common.exception.GoForWealthUMAException;
import com.moptra.go4wealth.uma.common.rest.GoForWealthUMAResponseInfo;
import com.moptra.go4wealth.uma.common.util.GoForWealthUMAUtil;
import com.moptra.go4wealth.uma.model.ForgetPasswordDTO;
import com.moptra.go4wealth.uma.model.RegisterAgentDTO;
import com.moptra.go4wealth.uma.model.UserAccountDTO;
import com.moptra.go4wealth.uma.model.UserDTO;
import com.moptra.go4wealth.uma.model.UserEnquryDTO;
import com.moptra.go4wealth.uma.model.UserLoginResponseDTO;
import com.moptra.go4wealth.uma.service.GoForWealthUMAService;

import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.MalformedJwtException;
import io.jsonwebtoken.SignatureException;

@RestController
@RequestMapping("${server.contextPath}/secured/uma")
public class GoForWealthUmaController {
	
	private static Logger logger = LoggerFactory.getLogger(GoForWealthUmaController.class);

	@Autowired
	private GoForWealthUMAService goForWealthUMAService;
	
	@Autowired
	JwtConfiguration jwtConfiguration;
	
	JwtUserProfileAuthenticationFilter jwtUserProfileAuthenticationFilter;
	
	@Autowired
	UserRepository userRepository;
	
	@GetMapping("/test")
	public String secured(){
		System.out.println("Inside secured()");
		return "Hello user !!! : ";
    }
	
	/**
	 * 
	 * @param exception
	 * @return GoForWealthUMAResponseInfo
	*/
	@ExceptionHandler({GoForWealthUMAException.class})
	public GoForWealthUMAResponseInfo handleException(GoForWealthUMAException exception) {
		return GoForWealthUMAUtil.getExceptionResponseInfo(exception);
	}

	/**
	 * 
	 * @param exception
	 * @return GoForWealthUMAResponseInfo
	*/
	@ExceptionHandler({Exception.class})
	public GoForWealthUMAResponseInfo handleException(Exception exception) {
		return GoForWealthUMAUtil.getErrorResponseInfo(GoForWealthErrorMessageEnum.COMMON_ERROR_CODE.getValue(), GoForWealthErrorMessageEnum.FAILURE_MESSAGE.getValue(), exception);
	}
	
	/**
	 * 
	 * @param  userName
	 * @return GoForWealthUMAResponseInfo
	 * @throws GoForWealthUMAException
	 * @throws JsonProcessingException 
	*/
	@GetMapping("/isUserExist/{userName}")
	public GoForWealthUMAResponseInfo isUserExist(@PathVariable("userName")String userName) throws GoForWealthUMAException {
		boolean message = false;
		logger.info("In isUserExist() user name == "+userName);
		message = goForWealthUMAService.isUserExist(userName);
		logger.info("Out iSUserExist()");
		return GoForWealthUMAUtil.getSuccessResponseInfo(GoForWealthErrorMessageEnum.SUCCESS_CODE.getValue(), GoForWealthErrorMessageEnum.SUCCESS_MESSAGE.getValue(),message);
	}
	
	/**
	 * 
	 * @param registerUserDTO
	 * @return GoForWealthUMAResponseInfo
	 * @throws GoForWealthUMAException
	 * @throws JsonProcessingException 
	*/
	@PostMapping("/signupByFacebook")
	public GoForWealthUMAResponseInfo signupByFacebook(@RequestBody UserDTO registerUserDTO, HttpServletResponse res,HttpServletRequest req) throws GoForWealthUMAException, JsonProcessingException {
		logger.info("In signupByFacebook()");
		logger.info("registerUserDTO === "+registerUserDTO.toString());
		User user = goForWealthUMAService.signupByFacebook(registerUserDTO);
		UserSession userSession = new UserSession(user.getUserId());
		//res.addHeader("name", user.getFirstName());
		res.addHeader("name", user.getFirstName()+" "+user.getLastName());
		res.addHeader("id", user.getUserId()+"");
		UserRole userRole = goForWealthUMAService.getUserRole(user.getUserId());		
		res.addHeader("role", userRole.getRole().getRoleName());		
		jwtUserProfileAuthenticationFilter.addTokenToResponse(jwtConfiguration, res, userSession,req);
		logger.info("Out signupByFacebook()");
		return GoForWealthUMAUtil.getSuccessResponseInfo(GoForWealthErrorMessageEnum.SUCCESS_CODE.getValue(), GoForWealthErrorMessageEnum.SUCCESS_MESSAGE.getValue(), null);
	}
	
	/**
	 * 
	 * @param registerUserDTO
	 * @return GoForWealthUMAResponseInfo
	 * @throws GoForWealthUMAException
	*/
	@PostMapping("/signupByGoogle")
	public GoForWealthUMAResponseInfo signupByGoogle(@RequestBody UserDTO registerUserDTO, HttpServletResponse res,HttpServletRequest req) throws GoForWealthUMAException, JsonProcessingException {
		logger.info("In signupByGoogle()");
		User user = goForWealthUMAService.signupByGoogle(registerUserDTO);
		UserSession userSession = new UserSession(user.getUserId());
		//res.addHeader("name", user.getFirstName());
		res.addHeader("name", user.getFirstName()+" "+user.getLastName());
		res.addHeader("id", user.getUserId()+"");
		UserRole userRole = goForWealthUMAService.getUserRole(user.getUserId());		
		res.addHeader("role", userRole.getRole().getRoleName());	
		jwtUserProfileAuthenticationFilter.addTokenToResponse(jwtConfiguration, res, userSession,req);
		logger.info("Out signupByGoogle()");
		return GoForWealthUMAUtil.getSuccessResponseInfo(GoForWealthErrorMessageEnum.SUCCESS_CODE.getValue(), GoForWealthErrorMessageEnum.SUCCESS_MESSAGE.getValue(), null);
	}	
	
	/**
	 * 
	 * @param registerUserDTO
	 * @return GoForWealthUMAResponseInfo
	 * @throws GoForWealthUMAException 
	*/
	@PostMapping("/signupByMobile")
	public GoForWealthUMAResponseInfo register(@RequestBody UserDTO registerUserDTO) throws GoForWealthUMAException {
		logger.info("In signupByMobile()");
		String message = null;
		registerUserDTO.setRole("USER");
		message = goForWealthUMAService.registerByMobileNumber(registerUserDTO);
		logger.info("Out signupByMobile()");
		return GoForWealthUMAUtil.getSuccessResponseInfo(GoForWealthErrorMessageEnum.SUCCESS_CODE.getValue(), GoForWealthErrorMessageEnum.SUCCESS_MESSAGE.getValue(), message);
	}
	
	/**
	 * 
	 * @param registerUserDTO
	 * @return GoForWealthUMAResponseInfo
	 * @throws GoForWealthUMAException 
	*/
	@PostMapping("/signupByGuestUser")
	public GoForWealthUMAResponseInfo signupByGuestUser(@RequestBody UserDTO registerUserDTO) throws GoForWealthUMAException {
		logger.info("In signupByGuestUser()");
		String message = null;
		registerUserDTO.setRole("USER");
		message = goForWealthUMAService.signupByGuestUser(registerUserDTO);
		logger.info("Out signupByGuestUser()");
		return GoForWealthUMAUtil.getSuccessResponseInfo(GoForWealthErrorMessageEnum.SUCCESS_CODE.getValue(), GoForWealthErrorMessageEnum.SUCCESS_MESSAGE.getValue(), message);
	}

	/**
	 * 
	 * @param registerAgentDTO
	 * @return GoForWealthUMAResponseInfo
	 * @throws GoForWealthUMAException 
	*/
	@PostMapping("/agent/register")
	public GoForWealthUMAResponseInfo agentRegister(@RequestBody RegisterAgentDTO registerAgentDTO) throws GoForWealthUMAException {
		logger.info("In agentRegister()");
		String message = null;
		message = goForWealthUMAService.registerAgent(registerAgentDTO);
		logger.info("Out agentRegister()");
		return GoForWealthUMAUtil.getSuccessResponseInfo(GoForWealthErrorMessageEnum.SUCCESS_CODE.getValue(), GoForWealthErrorMessageEnum.SUCCESS_MESSAGE.getValue(), message);
	}  
	  
	/**
	 * 
	 * @param registerUserDTO
	 * @return
	 * @throws GoForWealthUMAException
	*/
	@PostMapping("/verify/otp/mobileNumber")
	public GoForWealthUMAResponseInfo verifyMobileNumber(@RequestBody UserDTO registerUserDTO) {
		logger.info("Inside  verifyMobileNumber()");
		String message = null;
		message = goForWealthUMAService.verifyMobileNumber(registerUserDTO.getOtp(),registerUserDTO.getMobileNumber());
		logger.info("Out verifyMobileNumber()");
		return GoForWealthUMAUtil.getSuccessResponseInfo(GoForWealthErrorMessageEnum.SUCCESS_CODE.getValue(),GoForWealthErrorMessageEnum.SUCCESS_MESSAGE.getValue(), message);
	}
	 
	/**
	  * 
	  * @param mobileNumber
	  * @return GoForWealthUMAResponseInfo
	  * @throws GoForWealthUMAException
	*/
	@PostMapping("/resendOtp")
	public GoForWealthUMAResponseInfo resendOtp(@RequestParam("mobileNumber") String mobileNumber, @RequestParam("email") String email, @RequestParam("type") String type) throws GoForWealthUMAException {
		logger.info("In resendOtp()");
		String message = null;
		message = goForWealthUMAService.resendOtp(mobileNumber,email,type);
		logger.info("Out resendOtp()");
		return GoForWealthUMAUtil.getSuccessResponseInfo(GoForWealthErrorMessageEnum.SUCCESS_CODE.getValue(), GoForWealthErrorMessageEnum.SUCCESS_MESSAGE.getValue(), message);
	} 
	 
	/**
	 * 
	 * @param mobileNumber
	 * @return GoForWealthUMAResponseInfo
	 * @throws GoForWealthUMAException
	*/
	@PostMapping("/forgetPassword/{mobileNumber}")
	public GoForWealthUMAResponseInfo forgetPassword(@PathVariable("mobileNumber")String mobileNumber) throws GoForWealthUMAException {
		String message = null;
		logger.info("In forgetPassword()");
		message = goForWealthUMAService.forgetPassword(mobileNumber);
		logger.info("Out forgetPassword()");
		return GoForWealthUMAUtil.getSuccessResponseInfo(GoForWealthErrorMessageEnum.SUCCESS_CODE.getValue(), GoForWealthErrorMessageEnum.SUCCESS_MESSAGE.getValue(),message);
	}  
 
	/**
	 * 
	 * @param userDTO
	 * @return GoForWealthUMAResponseInfo
	 * @throws GoForWealthUMAException
	*/
	@PostMapping("/forgetPassword/verify/otp")
	public GoForWealthUMAResponseInfo forgetPasswordVerifyOtp(@RequestBody UserDTO userDTO,HttpServletRequest request, HttpServletResponse response,Authentication authentication) {
		logger.info("In forgetPasswordVerifyOtp()");
		String message = null;
	 	message = goForWealthUMAService.forgetPasswordVerifyOtp(userDTO.getMobileNumber(), userDTO.getOtp());
	 	if(message.equals("OTP verified successfully")){
	 		LocalDateTime expirationTime = LocalDateTime.now().plusMinutes(GoForWealthUMAConstants.FORGETPASSWORD_TOKEN_EXPIRATION_TIME * 365);
	 		String token = "";
	 		try {
	 			Integer userId = goForWealthUMAService.getUserIdByMobileNumber(userDTO.getMobileNumber());
	 			UserSession userSession = new UserSession(userId);
	 			token = GoForWealthUMAUtil.generateJwtToken(jwtConfiguration,userSession,expirationTime);
	 		}catch(JsonProcessingException e) {
	 			try {
	 				throw new GoForWealthSIPException(GoForWealthSIPErrorMessageEnum.COMMON_ERROR_CODE.getValue(),"IOError parsing object to json string");
				}catch (GoForWealthSIPException e1) {
					e1.printStackTrace();
				}
	 		}
	 		response.addHeader(GoForWealthUMAConstants.FORGETPASSWORD_HEADER_NAME, token);
	 	}
	 	logger.info("Out forgetPasswordVerifyOtp()");
	 	return GoForWealthUMAUtil.getSuccessResponseInfo(GoForWealthErrorMessageEnum.SUCCESS_CODE.getValue(), GoForWealthErrorMessageEnum.SUCCESS_MESSAGE.getValue(), message);
	}
	 
	/**
	 *
	 * @param userDTO
	 * @return GoForWealthUMAResponseInfo
	 * @throws GoForWealthUMAException
	*/
	@PostMapping("/resetPassword")
	public GoForWealthUMAResponseInfo resetPassword(@RequestBody UserDTO userDTO,HttpServletRequest request,Authentication authentication) throws GoForWealthUMAException {
		logger.info("Inside resetPassword()");
		String token = request.getHeader(GoForWealthUMAConstants.FORGETPASSWORD_HEADER_NAME);
		boolean result=false;
		if(token !=null && !token.equals("")){
			try {
				String objectJson = GoForWealthUMAUtil.parseJwtToken(token, jwtConfiguration);
				UserSession userSession = (UserSession) GoForWealthUMAUtil.getObjectFromJson(objectJson,UserSession.class);
				if(userSession != null){
					result = goForWealthUMAService.resetPassword(userDTO);
				}
			}catch (IOException e) {
				throw new GoForWealthUMAException(GoForWealthErrorMessageEnum.COMMON_ERROR_CODE.getValue(),"IOError parsing json string");
			}
		}
		logger.info("Out resetPassword()");
		if(result){
			return GoForWealthUMAUtil.getSuccessResponseInfo(GoForWealthErrorMessageEnum.SUCCESS_CODE.getValue(), GoForWealthErrorMessageEnum.SUCCESS_MESSAGE.getValue(),result);
		}else{
			return GoForWealthUMAUtil.getSuccessResponseInfo(GoForWealthErrorMessageEnum.FAILURE_CODE.getValue(), GoForWealthErrorMessageEnum.FAILURE_MESSAGE.getValue(),result);
		}
	}
	
	/**
	 * 
	 * @param emailId
	 * @return
	 * @throws GoForWealthUMAException
	*/
	@GetMapping("/resetPasswordMail")
	public GoForWealthUMAResponseInfo resetPasswordMail(@RequestParam("emailId") String emailId) throws GoForWealthUMAException {
		String message=null;
		message = goForWealthUMAService.resetPassword(emailId);
		logger.info("Out resetPasswordMail()");
		return GoForWealthUMAUtil.getSuccessResponseInfo(GoForWealthErrorMessageEnum.SUCCESS_CODE.getValue(), GoForWealthErrorMessageEnum.SUCCESS_MESSAGE.getValue(), message);
	}

	/**
	* 
	* @param forgetPasswordDTO
	* @return GoForWealthUMAResponseInfo
	* @throws GoForWealthUMAException
	*/
	@PostMapping("/changePassword")
	public GoForWealthUMAResponseInfo changePassword(@RequestBody ForgetPasswordDTO forgetPasswordDTO,Authentication authentication) throws GoForWealthUMAException {	 
		logger.info("In Change Password ---------");
		GoForWealthUMAResponseInfo goForWealthUMAResponseInfo = null;
		UserPrincipal userSession = (UserPrincipal) authentication.getPrincipal();
		if(userSession != null){
			User user = userRepository.findUserByRole(userSession.getUser().getUserId(), "USER");
			if(user != null){
				int userId = userSession.getUser().getUserId();
				String message = null;	
				message = goForWealthUMAService.changePassword(forgetPasswordDTO,userId);
				goForWealthUMAResponseInfo = GoForWealthUMAUtil.getSuccessResponseInfo(GoForWealthErrorMessageEnum.SUCCESS_CODE.getValue(), GoForWealthErrorMessageEnum.SUCCESS_MESSAGE.getValue(), message);
			}else{
				goForWealthUMAResponseInfo=GoForWealthUMAUtil.getSuccessResponseInfo(GoForWealthErrorMessageEnum.ACCESS_DENIED_CODE.getValue(),GoForWealthErrorMessageEnum.ACCESS_DENIED_MESSAGE.getValue(),null);
			}
		}else{
			goForWealthUMAResponseInfo=GoForWealthUMAUtil.getSuccessResponseInfo(GoForWealthErrorMessageEnum.ACCESS_DENIED_CODE.getValue(),GoForWealthErrorMessageEnum.ACCESS_DENIED_MESSAGE.getValue(),null);
		}
		return goForWealthUMAResponseInfo;
	}

	/**
	 * 
	 * @param authentication
	 * @return GoForWealthUMAResponseInfo
	 * @throws GoForWealthUMAException
	*/
	@GetMapping("/loggedUserDetail")
	public GoForWealthUMAResponseInfo getLoggedUserDetail(Authentication authentication) throws GoForWealthUMAException {
		logger.info("In getLoggedUserDetail()");
		GoForWealthUMAResponseInfo goForWealthUMAResponseInfo = null;
		UserPrincipal userSession = (UserPrincipal) authentication.getPrincipal();
		if(userSession != null){
			User user = userRepository.findUserByRole(userSession.getUser().getUserId(), "USER");
			if(user != null){
				UserDTO userDto = null;
				userDto = goForWealthUMAService.getLoggedUserDetail(userSession.getUser().getUserId());
				logger.info("Out getLoggedUserDetail()");
				goForWealthUMAResponseInfo = GoForWealthUMAUtil.getSuccessResponseInfo(GoForWealthErrorMessageEnum.SUCCESS_CODE.getValue(),GoForWealthErrorMessageEnum.SUCCESS_MESSAGE.getValue(),userDto);
			}else{
				goForWealthUMAResponseInfo=GoForWealthUMAUtil.getSuccessResponseInfo(GoForWealthErrorMessageEnum.ACCESS_DENIED_CODE.getValue(),GoForWealthErrorMessageEnum.ACCESS_DENIED_MESSAGE.getValue(),null);
			}
		}else{
			goForWealthUMAResponseInfo=GoForWealthUMAUtil.getSuccessResponseInfo(GoForWealthErrorMessageEnum.ACCESS_DENIED_CODE.getValue(),GoForWealthErrorMessageEnum.ACCESS_DENIED_MESSAGE.getValue(),null);
		}
		return goForWealthUMAResponseInfo;
	}
	
	/**
	 * 
	 * @param auth
	 * @return
	 * @throws GoForWealthUMAException
	 * @throws IOException
	*/
	@PostMapping("/getUser/{action}")
	public GoForWealthUMAResponseInfo getUser(@PathVariable("action")String action,Authentication auth) throws GoForWealthUMAException, IOException {
		logger.info("In getUser()");
		GoForWealthUMAResponseInfo goForWealthUMAResponseInfo = null;
		UserLoginResponseDTO userLoginResponseDTO = null;
		if(auth!=null){
			UserPrincipal userSession = (UserPrincipal)auth.getPrincipal();
		    if(userSession != null) {
		    	logger.info("In getUser() with UserId :  " + userSession.getUser().getUserId());
		    	User user = userRepository.findUserByRole(userSession.getUser().getUserId(), "USER");
		    	if(user!= null){
		    		userLoginResponseDTO = goForWealthUMAService.getUser(userSession.getUser().getUserId(),action);
			    	goForWealthUMAResponseInfo = GoForWealthUMAUtil.getSuccessResponseInfo(GoForWealthErrorMessageEnum.SUCCESS_CODE.getValue(),GoForWealthErrorMessageEnum.SUCCESS_MESSAGE.getValue(),userLoginResponseDTO);
		    	}else{
		    		goForWealthUMAResponseInfo = GoForWealthUMAUtil.getSuccessResponseInfo(GoForWealthErrorMessageEnum.ACCESS_DENIED_CODE.getValue(), GoForWealthErrorMessageEnum.ACCESS_DENIED_MESSAGE.getValue(),userLoginResponseDTO);
		    	}
		    }else{
		    	userLoginResponseDTO = new UserLoginResponseDTO();
		    	userLoginResponseDTO.setMessage(GoForWealthUMAConstants.USER_NOT_EXIST);
		    	goForWealthUMAResponseInfo = GoForWealthUMAUtil.getSuccessResponseInfo(GoForWealthErrorMessageEnum.ACCESS_DENIED_CODE.getValue(), GoForWealthErrorMessageEnum.ACCESS_DENIED_MESSAGE.getValue(), userLoginResponseDTO);
		    }
		}else{
	    	userLoginResponseDTO = new UserLoginResponseDTO();
	    	userLoginResponseDTO.setMessage(GoForWealthUMAConstants.USER_NOT_EXIST);
	    	goForWealthUMAResponseInfo = GoForWealthUMAUtil.getSuccessResponseInfo(GoForWealthErrorMessageEnum.ACCESS_DENIED_CODE.getValue(), GoForWealthErrorMessageEnum.ACCESS_DENIED_MESSAGE.getValue(), userLoginResponseDTO);
	    }
		logger.info("Out getUser()");
		return goForWealthUMAResponseInfo;
	}

    /**
     *
     * @param authentication
     * @param request
     * @return GoForWealthUMAResponseInfo
     * @throws 
    */
	@GetMapping("/getAllUsers")
	public GoForWealthUMAResponseInfo getAllUsers(Authentication authentication,HttpServletRequest request) throws GoForWealthUMAException {
		logger.info("In getAllUsers()");
		List<UserAccountDTO> data = null;
		GoForWealthUMAResponseInfo goForWealthUMAResponseInfo = null;
		UserPrincipal userSession = (UserPrincipal)authentication.getPrincipal();
		if(userSession != null){
			User user1 = userRepository.findUserByRole(userSession.getUser().getUserId(), "USER");
			if(user1 != null){
				User user = ((UserPrincipal)authentication.getPrincipal()).getUser();
				data = goForWealthUMAService.getAllAccounts(user.getUserId());
				logger.info("Out getAllUsers()");
				goForWealthUMAResponseInfo = GoForWealthUMAUtil.getSuccessResponseInfo(GoForWealthErrorMessageEnum.SUCCESS_CODE.getValue(), GoForWealthErrorMessageEnum.SUCCESS_MESSAGE.getValue(),data);
			}else{
				goForWealthUMAResponseInfo=GoForWealthUMAUtil.getSuccessResponseInfo(GoForWealthErrorMessageEnum.ACCESS_DENIED_CODE.getValue(),GoForWealthErrorMessageEnum.ACCESS_DENIED_MESSAGE.getValue(),null);
			}
		}else{
			goForWealthUMAResponseInfo=GoForWealthUMAUtil.getSuccessResponseInfo(GoForWealthErrorMessageEnum.ACCESS_DENIED_CODE.getValue(),GoForWealthErrorMessageEnum.ACCESS_DENIED_MESSAGE.getValue(),null);
		}
		return goForWealthUMAResponseInfo;
	}

	/**
	 * 
	 * @param emailId
	 * @return GoForWealthUMAResponseInfo
	*/
	@PostMapping("/deleteUser")
	public GoForWealthUMAResponseInfo deleteUser(@RequestParam("emailId") String emailId,Authentication authentication) {
		String message=null;
		GoForWealthUMAResponseInfo goForWealthUMAResponseInfo = null;
		UserPrincipal userSession = (UserPrincipal)authentication.getPrincipal();
		if(userSession != null){
			User user = userRepository.findUserByRole(userSession.getUser().getUserId(), "USER");
			if(user != null){
				goForWealthUMAService.deleteAccounts(emailId);
				message = "success";
				logger.info("Out getAllAccounts()");
				goForWealthUMAResponseInfo = GoForWealthUMAUtil.getSuccessResponseInfo(GoForWealthErrorMessageEnum.SUCCESS_CODE.getValue(), GoForWealthErrorMessageEnum.SUCCESS_MESSAGE.getValue(), message);
			}else{
				goForWealthUMAResponseInfo=GoForWealthUMAUtil.getSuccessResponseInfo(GoForWealthErrorMessageEnum.ACCESS_DENIED_CODE.getValue(),GoForWealthErrorMessageEnum.ACCESS_DENIED_MESSAGE.getValue(),null);
			}
		}else{
			goForWealthUMAResponseInfo=GoForWealthUMAUtil.getSuccessResponseInfo(GoForWealthErrorMessageEnum.ACCESS_DENIED_CODE.getValue(),GoForWealthErrorMessageEnum.ACCESS_DENIED_MESSAGE.getValue(),null);
		}
		return goForWealthUMAResponseInfo;
	}

	/**
	 * 
	 * @param registerUserDTO
	 * @return GoForWealthUMAResponseInfo
	*/
	/*@PostMapping("/editUser")
	public GoForWealthUMAResponseInfo editUser(@RequestBody UserDTO registerUserDTO, Authentication authentication) throws GoForWealthUMAException {
		logger.info("In editUser()");
		GoForWealthUMAResponseInfo goForWealthUMAResponseInfo = null;
		UserPrincipal userSession = (UserPrincipal) authentication.getPrincipal();
		if(userSession != null){
			User user = userRepository.findUserByRole(userSession.getUser().getUserId(), "USER");
			if(user != null){
				goForWealthUMAService.editUserDetails(registerUserDTO, userSession.getUser().getUserId());
				logger.info("Out editUser()");
				goForWealthUMAResponseInfo = GoForWealthUMAUtil.getSuccessResponseInfo(GoForWealthErrorMessageEnum.SUCCESS_CODE.getValue(),GoForWealthErrorMessageEnum.SUCCESS_MESSAGE.getValue(),null);
			}else{
				goForWealthUMAResponseInfo=GoForWealthUMAUtil.getSuccessResponseInfo(GoForWealthErrorMessageEnum.ACCESS_DENIED_CODE.getValue(),GoForWealthErrorMessageEnum.ACCESS_DENIED_MESSAGE.getValue(),null);
			}
		}else{
			goForWealthUMAResponseInfo=GoForWealthUMAUtil.getSuccessResponseInfo(GoForWealthErrorMessageEnum.ACCESS_DENIED_CODE.getValue(),GoForWealthErrorMessageEnum.ACCESS_DENIED_MESSAGE.getValue(),null);
		}
		return goForWealthUMAResponseInfo;
	}*/

	/**
	 * 
	 * @param otp
	 * @param mobileNumber
	 * @return GoForWealthUMAResponseInfo
	 * @param registerAgentDTO
	 * @return GoForWealthUMAResponseInfo
	*/
	@PostMapping("/editAgent")
	public GoForWealthUMAResponseInfo editAgent(@RequestBody RegisterAgentDTO registerAgentDTO, Authentication authentication) throws GoForWealthUMAException {
		logger.info("In editUser()");
		//UserSession userSession = (UserSession) authentication.getPrincipal();
		//int userId = userSession.getProfileId();
		int userId=6;//need to remove 
		goForWealthUMAService.editAgentDetails(registerAgentDTO, userId);
		logger.info("Out editUser()");
		return GoForWealthUMAUtil.getSuccessResponseInfo(GoForWealthErrorMessageEnum.SUCCESS_CODE.getValue(),GoForWealthErrorMessageEnum.SUCCESS_MESSAGE.getValue(), null);
	}
	
	/**
	 * 
	 * @param authentication
	 * @return
	 * @throws GoForWealthUMAException
	*/
	@GetMapping("/agent/commission")
	public GoForWealthUMAResponseInfo getCommissionDetails(Authentication authentication) throws GoForWealthUMAException {
		logger.info("In getCommissionDetails()");
		UserSession userSession = (UserSession) authentication.getPrincipal();
		int userId = userSession.getProfileId();
		goForWealthUMAService.getCommissionDetails(userId);
		logger.info("Out getCommissionDetails()");
		return GoForWealthUMAUtil.getSuccessResponseInfo(GoForWealthErrorMessageEnum.SUCCESS_CODE.getValue(), GoForWealthErrorMessageEnum.SUCCESS_MESSAGE.getValue(), null);
	}

	/**
	  * 
	  * @param req
	  * @return
	  * @throws GoForWealthUMAException
	  * @throws IOException
	*/
	@PostMapping("/verifyToken")
	public GoForWealthUMAResponseInfo verifyToken(HttpServletRequest req) throws GoForWealthUMAException, IOException {
		logger.info("In verifyToken()");
		String header = req.getHeader(jwtConfiguration.headerName);
		String subjectJson = Jwts.parser()
	    			.setSigningKey(jwtConfiguration.secret.getBytes())
	    			.parseClaimsJws(header)
	    			.getBody()
	    			.getSubject();
	    UserSession userSession = new ObjectMapper().readValue(subjectJson, UserSession.class);
	    String message = null;
	    if (userSession != null) {
	    	message = GoForWealthUMAConstants.SUCCESS;	
	    }else{
	    	message = GoForWealthUMAConstants.FAILURE;;
	    }
	    logger.info("User Id : " + userSession.getProfileId());
		logger.info("Out verifyToken()");
		return GoForWealthUMAUtil.getSuccessResponseInfo(GoForWealthErrorMessageEnum.SUCCESS_CODE.getValue(), GoForWealthErrorMessageEnum.SUCCESS_MESSAGE.getValue(), message);
	}
 
	/**
	  * 
	  * @param auth
	  * @return
	  * @throws GoForWealthUMAException
	  * @throws IOException
	*/
	@PostMapping("/sendEmailVerificationUrl")
	public GoForWealthUMAResponseInfo sendEmailVerificationUrl(@RequestBody UserDTO registerUserDTO, Authentication auth) throws GoForWealthUMAException, IOException {
		logger.info("In sendEmailVerificationUrl()");
	 	GoForWealthUMAResponseInfo goForWealthUMAResponseInfo = null;
	 	UserPrincipal userSession = (UserPrincipal)auth.getPrincipal();
	 	String message = null;
	    if(userSession != null) {
	    	logger.info("In sendEmailVerificationUrl() with UserId : " + userSession.getUser().getUserId() + ".... Email : " + registerUserDTO.getEmail());
	    	User user = userRepository.findUserByRole(userSession.getUser().getUserId(), "USER");
	    	if(user != null){
	    		message = goForWealthUMAService.sendEmailVerification(userSession.getUser().getUserId(), registerUserDTO);
	 	    	GoForWealthUMAUtil.getSuccessResponseInfo(GoForWealthErrorMessageEnum.SUCCESS_CODE.getValue(),GoForWealthErrorMessageEnum.SUCCESS_MESSAGE.getValue(), message);
	    	 }else{
	    		message = GoForWealthUMAConstants.EMAIL_VERIFICATION_SESSION_OUT;
	  	    	goForWealthUMAResponseInfo=GoForWealthUMAUtil.getSuccessResponseInfo(GoForWealthErrorMessageEnum.ACCESS_DENIED_CODE.getValue(),GoForWealthErrorMessageEnum.ACCESS_DENIED_MESSAGE.getValue(),message);
	    	 }
	    }else {
	    	message = GoForWealthUMAConstants.EMAIL_VERIFICATION_SESSION_OUT;
	    	goForWealthUMAResponseInfo=GoForWealthUMAUtil.getSuccessResponseInfo(GoForWealthErrorMessageEnum.ACCESS_DENIED_CODE.getValue(),GoForWealthErrorMessageEnum.ACCESS_DENIED_MESSAGE.getValue(),message);
	    }
	 	logger.info("Out sendEmailVerificationUrl()");
	 	return goForWealthUMAResponseInfo;
	}

	/**
	  * 
	  * @param token
	  * @return
	  * @throws GoForWealthUMAException 
	*/
	@GetMapping("/verify-email")
	public GoForWealthUMAResponseInfo verifyEmail(@RequestParam("token") String token) throws IOException, GoForWealthUMAException {
		logger.info("In verifyEmail()");
		String message = null;
		try{
			String subjectJson = Jwts.parser()
	    			.setSigningKey(jwtConfiguration.secret.getBytes())
	    			.parseClaimsJws(token)
	    			.getBody()
	    			.getSubject();
			UserSession userSession = new ObjectMapper().readValue(subjectJson, UserSession.class);
	    	if (userSession != null) {
	    		message = goForWealthUMAService.emailVerification(userSession.getProfileId());	    				
	    	}else{
	    		message = GoForWealthUMAConstants.USER_NOT_EXIST;
	    	}
		}catch(ExpiredJwtException ex){
			message = GoForWealthUMAConstants.TOKEN_EXPIRED;
		}catch(MalformedJwtException | SignatureException ex){
			message = GoForWealthUMAConstants.WRONG_TOKEN;
		}catch(Exception ex){
			throw new GoForWealthUMAException(GoForWealthErrorMessageEnum.COMMON_ERROR_CODE.getValue(), GoForWealthErrorMessageEnum.INTERNAL_SERVER_ERROR.getValue());
		}
		logger.info("Out verifyEmail()");
		return GoForWealthUMAUtil.getSuccessResponseInfo(GoForWealthErrorMessageEnum.SUCCESS_CODE.getValue(), GoForWealthErrorMessageEnum.SUCCESS_MESSAGE.getValue(), message);
	}

	@PostMapping("/verify-mobile")
	public GoForWealthUMAResponseInfo verifyMobile(@RequestParam("mobile") String mobile, Authentication auth) throws IOException, GoForWealthUMAException {
		logger.info("In verifyMobile()");
		GoForWealthUMAResponseInfo goForWealthUMAResponseInfo = null;
		UserPrincipal userSession = (UserPrincipal)auth.getPrincipal();
		String message = null;
	    if (userSession != null) {
	    	logger.info("In verifyMobile() with UserId : " + userSession.getUser().getUserId());
	    	User user = userRepository.findUserByRole(userSession.getUser().getUserId(), "USER");
	    	if(user != null){
	    		logger.info("In verifyMobile() with userId : " + userSession.getUser().getUserId());
		    	message = goForWealthUMAService.mobileVerification(userSession.getUser().getUserId(), mobile);
		    	goForWealthUMAResponseInfo = GoForWealthUMAUtil.getSuccessResponseInfo(GoForWealthErrorMessageEnum.SUCCESS_CODE.getValue(),GoForWealthErrorMessageEnum.SUCCESS_MESSAGE.getValue(),message);
		    }else{
		    	goForWealthUMAResponseInfo=GoForWealthUMAUtil.getSuccessResponseInfo(GoForWealthErrorMessageEnum.ACCESS_DENIED_CODE.getValue(),GoForWealthErrorMessageEnum.ACCESS_DENIED_MESSAGE.getValue(),null);
		    }
	    }else{
	    	goForWealthUMAResponseInfo=GoForWealthUMAUtil.getSuccessResponseInfo(GoForWealthErrorMessageEnum.ACCESS_DENIED_CODE.getValue(),GoForWealthErrorMessageEnum.ACCESS_DENIED_MESSAGE.getValue(),null);
	    }
		logger.info("Out verifyMobile()");
		return goForWealthUMAResponseInfo;
	}

	@GetMapping("/getUserBlogList")
	public GoForWealthAdminResponseInfo getUserBlogList() throws GoForWealthAdminException {
		GoForWealthAdminResponseInfo responseInfo=null;
	 	List<BlogDTO> blogList=goForWealthUMAService.goForWealthUMAService();
	 	if(blogList != null && blogList.size() > 0) {
	 		responseInfo = GoForWealthAdminUtil.getSuccessResponseInfo(GoForWealthAdminErrorMessageEnum.SUCCESS_CODE.getValue(), GoForWealthAdminErrorMessageEnum.SUCCESS_MESSAGE.getValue(), blogList);
	 		responseInfo.setTotalRecords(blogList.size());
	 	}else{
	 		responseInfo = new GoForWealthAdminResponseInfo();
	 		responseInfo.setStatus(GoForWealthAdminErrorMessageEnum.DATA_NOT_FOUND_CODE.getValue());
	 		responseInfo.setMessage(GoForWealthAdminErrorMessageEnum.DATA_NOT_FOUND_MESSAGE.getValue());
	 	}
	 	return responseInfo;
	}

	@GetMapping("/getUserBlogCategory")
	public GoForWealthAdminResponseInfo getUserBlogCategory() throws GoForWealthAdminException{
		logger.info("In getUserBlogCategory()");
		GoForWealthAdminResponseInfo responseInfo=null;
		List<BlogCategoryDTO> blogCategoryDTOList=goForWealthUMAService.getUserBlogCategory();
		if(!blogCategoryDTOList.isEmpty()){
			responseInfo = GoForWealthAdminUtil.getSuccessResponseInfo(GoForWealthAdminErrorMessageEnum.SUCCESS_CODE.getValue(), GoForWealthAdminErrorMessageEnum.SUCCESS_MESSAGE.getValue(), blogCategoryDTOList);
		}else {
			responseInfo = new GoForWealthAdminResponseInfo();
			responseInfo.setStatus(GoForWealthPRSErrorMessageEnum.DATA_NOT_FOUND_CODE.getValue());
			responseInfo.setMessage(GoForWealthPRSErrorMessageEnum.DATA_NOT_SAVED.getValue());
		}
		logger.info("Out getUserBlogCategory()");
		return responseInfo;
	}

	@PostMapping("/getUserBlogByCategory/{categoryId}")
	public GoForWealthAdminResponseInfo getUserBlogByCategory(@PathVariable("categoryId") int categoryId) throws GoForWealthAdminException{
		logger.info("In getUserBlogByCategory()");
		GoForWealthAdminResponseInfo responseInfo=null;
		List<BlogDTO> blogDTOList=goForWealthUMAService.getUserBlogByCategoryId(categoryId);
		if(!blogDTOList.isEmpty()){
			responseInfo = GoForWealthAdminUtil.getSuccessResponseInfo(GoForWealthAdminErrorMessageEnum.SUCCESS_CODE.getValue(), GoForWealthAdminErrorMessageEnum.SUCCESS_MESSAGE.getValue(), blogDTOList);
		}else {
			responseInfo = new GoForWealthAdminResponseInfo();
			responseInfo.setStatus(GoForWealthPRSErrorMessageEnum.DATA_NOT_FOUND_CODE.getValue());
			responseInfo.setMessage(GoForWealthPRSErrorMessageEnum.DATA_NOT_SAVED.getValue());
		}
		logger.info("Out getUserBlogCategory()");
		return responseInfo;
	}

	@GetMapping("/getUserBlogById/{blogId}")
	public GoForWealthAdminResponseInfo getUserBlogById(@PathVariable("blogId") int blogId,HttpServletResponse response) throws IOException, GoForWealthAdminException {
		logger.info("In getUserBlogById()");
		GoForWealthAdminResponseInfo responseInfo=null;
		BlogDTO blog=goForWealthUMAService.getUserBlogById(blogId,response);
		if(blog != null ) {
			responseInfo = GoForWealthAdminUtil.getSuccessResponseInfo(GoForWealthAdminErrorMessageEnum.SUCCESS_CODE.getValue(), GoForWealthAdminErrorMessageEnum.SUCCESS_MESSAGE.getValue(), blog);
		}else{
			responseInfo = new GoForWealthAdminResponseInfo();
			responseInfo.setStatus(GoForWealthAdminErrorMessageEnum.DATA_NOT_FOUND_CODE.getValue());
			responseInfo.setMessage(GoForWealthAdminErrorMessageEnum.DATA_NOT_FOUND_MESSAGE.getValue());
		}
		logger.info("Out getUserBlogById()");
		return responseInfo;
	} 

	@PostMapping("/getUserRelatedBlog/{id}")
	public GoForWealthAdminResponseInfo getUserRelatedBlog(@PathVariable("id") int id) throws IOException, GoForWealthAdminException {
		logger.info("In getUserRelatedBlog()");
		GoForWealthAdminResponseInfo responseInfo=null;
		List<BlogDTO> blogList= goForWealthUMAService.getUserRelatedBlog(id);
		if(!blogList.isEmpty()){
			responseInfo = GoForWealthAdminUtil.getSuccessResponseInfo(GoForWealthErrorMessageEnum.SUCCESS_CODE.getValue(), GoForWealthErrorMessageEnum.SUCCESS_MESSAGE.getValue(), blogList);
		}else{
			responseInfo = new GoForWealthAdminResponseInfo();
			responseInfo.setStatus(GoForWealthPRSErrorMessageEnum.DATA_NOT_FOUND_CODE.getValue());
			responseInfo.setMessage(GoForWealthPRSErrorMessageEnum.DATA_NOT_SAVED.getValue());
		}
		logger.info("Out getUserRelatedBlog()");
		return responseInfo;
	}

	@GetMapping("/getLatestBlog")
	public GoForWealthAdminResponseInfo getLatestBlog(Authentication authentication) throws IOException, GoForWealthAdminException {
		logger.info("In getLatestBlog()");
		GoForWealthAdminResponseInfo responseInfo=null;
		if(authentication != null){
			UserPrincipal usersession = (UserPrincipal) authentication.getPrincipal();
		 	if(usersession != null){
		 		List<BlogDTO> latestBlogList= goForWealthUMAService.getLatestBlog(usersession.getUser().getUserId());
		 		if(!latestBlogList.isEmpty()){
		 			responseInfo = GoForWealthAdminUtil.getSuccessResponseInfo(GoForWealthAdminErrorMessageEnum.SUCCESS_CODE.getValue(), GoForWealthAdminErrorMessageEnum.SUCCESS_MESSAGE.getValue(), latestBlogList);
		 		}else{
		 			responseInfo = new GoForWealthAdminResponseInfo();
		 			responseInfo.setStatus(GoForWealthPRSErrorMessageEnum.DATA_NOT_FOUND_CODE.getValue());
		 			responseInfo.setMessage(GoForWealthPRSErrorMessageEnum.DATA_NOT_SAVED.getValue());
		 		}
		 	}else{
		 		responseInfo = GoForWealthAdminUtil.getSuccessResponseInfo(GoForWealthErrorMessageEnum.FAILURE_CODE.getValue(), GoForWealthErrorMessageEnum.USER_DETAIL_NOT_EXIST.getValue(),null);
		 	}
		}else{
			responseInfo = GoForWealthAdminUtil.getSuccessResponseInfo(GoForWealthErrorMessageEnum.FAILURE_CODE.getValue(), GoForWealthErrorMessageEnum.USER_DETAIL_NOT_EXIST.getValue(),null);
		}
		logger.info("Out getUserRelatedBlog()");
		return responseInfo;
	}

	@PostMapping("/enquiry")
	public GoForWealthUMAResponseInfo addEnquiry(@RequestBody UserEnquryDTO userEnqury){
		logger.info("In addEnquriy()");
		String response = "";
		response = goForWealthUMAService.addEnqury(userEnqury);
		if(response.equals("success")){
			logger.info("Out addEnquiry()");
			return GoForWealthUMAUtil.getSuccessResponseInfo(GoForWealthErrorMessageEnum.SUCCESS_CODE.getValue(),GoForWealthErrorMessageEnum.SUCCESS_MESSAGE.getValue(),response);
		}else{
			logger.info("Out addEnquiry() With Error");
			return GoForWealthUMAUtil.getSuccessResponseInfo(GoForWealthErrorMessageEnum.FAILURE_CODE.getValue(), GoForWealthErrorMessageEnum.FAILURE_MESSAGE.getValue(),response);
		}
	}

	//----------------secured----------------------------------//
	@RequestMapping(value="/changeMaritalStatus", method=RequestMethod.GET)
	public GoForWealthUMAResponseInfo changeMaritalStatus(@RequestParam("maritalStatus")String maritalStatus,Authentication auth) {
		logger.info("In changeMaritalStatus()");
		UserPrincipal userSession = (UserPrincipal)auth.getPrincipal();
		GoForWealthUMAResponseInfo responseInfo = null;
		String message = "";
		if(userSession!=null){
			logger.info("In changeMaritalStatus() with UserId : " + userSession.getUser().getUserId());
			User user = userRepository.findUserByRole(userSession.getUser().getUserId(), "USER");
			if(user !=null){
				message = goForWealthUMAService.changeMaritalStatus(user.getUserId(),maritalStatus);
				if(message!=null){
					responseInfo = GoForWealthUMAUtil.getSuccessResponseInfo(GoForWealthErrorMessageEnum.SUCCESS_CODE.getValue(), GoForWealthErrorMessageEnum.SUCCESS_MESSAGE.getValue(),message);
				}else{
					responseInfo = GoForWealthUMAUtil.getSuccessResponseInfo(GoForWealthErrorMessageEnum.FAILURE_CODE.getValue(), GoForWealthErrorMessageEnum.FAILURE_MESSAGE.getValue(),message);
				}
			}else{
				responseInfo = GoForWealthUMAUtil.getSuccessResponseInfo(GoForWealthErrorMessageEnum.ACCESS_DENIED_CODE.getValue(), GoForWealthErrorMessageEnum.ACCESS_DENIED_MESSAGE.getValue(), null);
			}
		}else{
			responseInfo = GoForWealthUMAUtil.getSuccessResponseInfo(GoForWealthErrorMessageEnum.ACCESS_DENIED_CODE.getValue(), GoForWealthErrorMessageEnum.ACCESS_DENIED_MESSAGE.getValue(), null);
		}
		logger.info("Out changeMaritalStatus()");
		return responseInfo;
	}
	
	//************ secured url ***********************/
	@GetMapping(value="/checkIsEmailVerified")
	public GoForWealthUMAResponseInfo checkIsEmailVerified(Authentication auth){
		logger.info("In checkIsEmailVerified()");
		GoForWealthUMAResponseInfo responseInfo = null;
		UserPrincipal userSession = (UserPrincipal)auth.getPrincipal();
		if(userSession != null){
			logger.info("In checkIsEmailVerified() with UserId : " + userSession.getUser().getUserId());
			User user = userRepository.findUserByRole(userSession.getUser().getUserId(),"USER");
			if(user != null){
				if(user.getEmailVerified()==1){
					responseInfo = GoForWealthUMAUtil.getSuccessResponseInfo(GoForWealthErrorMessageEnum.SUCCESS_CODE.getValue(),GoForWealthErrorMessageEnum.SUCCESS_MESSAGE.getValue(),true);
				}else{
					responseInfo = GoForWealthUMAUtil.getSuccessResponseInfo(GoForWealthErrorMessageEnum.SUCCESS_CODE.getValue(),GoForWealthErrorMessageEnum.SUCCESS_MESSAGE.getValue(),false);
				}
			}else{
				responseInfo = GoForWealthUMAUtil.getSuccessResponseInfo(GoForWealthErrorMessageEnum.ACCESS_DENIED_CODE.getValue(),GoForWealthErrorMessageEnum.ACCESS_DENIED_MESSAGE.getValue(),"");
			}
		}else{
			responseInfo = GoForWealthUMAUtil.getSuccessResponseInfo(GoForWealthErrorMessageEnum.ACCESS_DENIED_CODE.getValue(),GoForWealthErrorMessageEnum.ACCESS_DENIED_MESSAGE.getValue(),"");
		}
		logger.info("Out checkIsEmailVerified()");
		return responseInfo;
	}


}