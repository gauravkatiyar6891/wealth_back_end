package com.moptra.go4wealth.uma.service;

import java.io.IOException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Locale;
import java.util.Optional;
import java.util.Set;

import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.context.NoSuchMessageException;
import org.springframework.data.domain.PageRequest;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.moptra.go4wealth.admin.common.enums.GoForWealthAdminErrorMessageEnum;
import com.moptra.go4wealth.admin.common.exception.GoForWealthAdminException;
import com.moptra.go4wealth.admin.model.BlogCategoryDTO;
import com.moptra.go4wealth.admin.model.BlogDTO;
import com.moptra.go4wealth.bean.AddressProof;
import com.moptra.go4wealth.bean.Blog;
import com.moptra.go4wealth.bean.BlogCategory;
import com.moptra.go4wealth.bean.GoalOrderItems;
import com.moptra.go4wealth.bean.Goals;
import com.moptra.go4wealth.bean.GoalsOrder;
import com.moptra.go4wealth.bean.KycDetails;
import com.moptra.go4wealth.bean.OnboardingStatus;
import com.moptra.go4wealth.bean.Orders;
import com.moptra.go4wealth.bean.Otp;
import com.moptra.go4wealth.bean.Role;
import com.moptra.go4wealth.bean.User;
import com.moptra.go4wealth.bean.UserAssetItems;
import com.moptra.go4wealth.bean.UserEnquiry;
import com.moptra.go4wealth.bean.UserRole;
import com.moptra.go4wealth.bean.UserRoleId;
import com.moptra.go4wealth.configuration.EmailVerificationConfiguration;
import com.moptra.go4wealth.configuration.JwtConfiguration;
import com.moptra.go4wealth.configuration.ResetPasswordConfiguration;
import com.moptra.go4wealth.prs.common.constant.GoForWealthPRSConstants;
import com.moptra.go4wealth.prs.common.util.EncryptUserDetail;
import com.moptra.go4wealth.prs.model.AddressProofDTO;
import com.moptra.go4wealth.repository.AdminRepository;
import com.moptra.go4wealth.repository.AgentProfileRepository;
import com.moptra.go4wealth.repository.AssetClassRepository;
import com.moptra.go4wealth.repository.BlogCategoryRepository;
import com.moptra.go4wealth.repository.GoalBucketRepository;
import com.moptra.go4wealth.repository.GoalOrderItemsRepository;
import com.moptra.go4wealth.repository.GoalsOrderRepository;
import com.moptra.go4wealth.repository.GoalsRepository;
import com.moptra.go4wealth.repository.KycDetailsRepository;
import com.moptra.go4wealth.repository.OrderRepository;
import com.moptra.go4wealth.repository.OtpRepository;
import com.moptra.go4wealth.repository.RiskBearingCapacityRepository;
import com.moptra.go4wealth.repository.RoleRepository;
import com.moptra.go4wealth.repository.UserAssetItemsRepository;
import com.moptra.go4wealth.repository.UserEnquryRepository;
import com.moptra.go4wealth.repository.UserRepository;
import com.moptra.go4wealth.repository.UserRoleRepository;
import com.moptra.go4wealth.security.UserSession;
import com.moptra.go4wealth.sip.service.GoForWealthSIPService;
import com.moptra.go4wealth.sip.service.GoForWealthSIPServiceImpl;
import com.moptra.go4wealth.uma.common.constant.GoForWealthUMAConstants;
import com.moptra.go4wealth.uma.common.enums.GoForWealthErrorMessageEnum;
import com.moptra.go4wealth.uma.common.enums.MobileVerification;
import com.moptra.go4wealth.uma.common.enums.UserStatus;
import com.moptra.go4wealth.uma.common.exception.GoForWealthUMAException;
import com.moptra.go4wealth.uma.common.util.GoForWealthUMAUtil;
import com.moptra.go4wealth.uma.common.util.OtpGenerator;
import com.moptra.go4wealth.uma.common.util.OtpVerify;
import com.moptra.go4wealth.uma.model.CommissionDetailDTO;
import com.moptra.go4wealth.uma.model.ForgetPasswordDTO;
import com.moptra.go4wealth.uma.model.RegisterAgentDTO;
import com.moptra.go4wealth.uma.model.UserAccountDTO;
import com.moptra.go4wealth.uma.model.UserDTO;
import com.moptra.go4wealth.uma.model.UserEnquryDTO;
import com.moptra.go4wealth.uma.model.UserLoginResponseDTO;
import com.moptra.go4wealth.util.MailUtility;
import com.moptra.go4wealth.util.SmsUtility;

/**
 * 
 * @author ranjeet
 *
 */
@Service
public class GoForWealthServiceImpl implements GoForWealthUMAService {

	@Autowired
	AgentProfileRepository agentProfileRepository;

	@Autowired
	UserRepository userRepository;

	@Autowired
	RoleRepository roleRepository;

	/*@Autowired
	UserProfileRepository userProfileRepository;

	@Autowired
	AddressRepository addressRepository;*/

	@Autowired
	UserRoleRepository userRoleRepository;

	@Autowired
	OtpRepository otpRepository;

	@Autowired
	JwtConfiguration jwtConfiguration;

	@Autowired
	OrderRepository orderRepository;

	@Autowired
	private ResetPasswordConfiguration resetPasswordConfiguration;

	@Autowired
	private EmailVerificationConfiguration emailVerificationConfiguration;

	@Autowired
	private JavaMailSender mailSender;

	@Autowired
	private MessageSource messageSource;

	@Autowired
	private PasswordEncoder passwordEncoder;

	@Autowired
	AdminRepository adminRepository;

	@Autowired
	BlogCategoryRepository blogCategoryRepository;

	@Autowired
	EncryptUserDetail encryptUserDetail;

	@Autowired
	SmsUtility smsUtility;

	@Autowired
	MailUtility mailUtility;

	@Autowired
	private GoalsOrderRepository goalsOrderRepository;

	@Autowired
	private GoalOrderItemsRepository goalOrderItemsRepository;

	@Autowired
	UserEnquryRepository userEnquryRepository;

	@Autowired
	KycDetailsRepository kycDetailsRepository;

	@Autowired
	private UserAssetItemsRepository userAssetItemsRepository;

	@Autowired
	private GoalsRepository goalsRepository;

	@Autowired
	private GoForWealthSIPServiceImpl goForWealthSIPServiceImpl;

	@Autowired
	private GoalBucketRepository goalBucketRepository;

	@Autowired
	private RiskBearingCapacityRepository riskBearingCapacityRepository;

	@Autowired
	private AssetClassRepository assetClassRepository;

	@Autowired
	private GoForWealthSIPService goForWealthSIPService;

	private static Logger logger = LoggerFactory.getLogger(GoForWealthServiceImpl.class);

	@Override
	public String resetPassword(String emailId) throws GoForWealthUMAException {
		User user = null;
		String token = null;
		user = userRepository.findByUsername(emailId);
		if (user == null) {
			throw new GoForWealthUMAException(GoForWealthErrorMessageEnum.DATA_NOT_FOUND_CODE.getValue(),GoForWealthErrorMessageEnum.USER_DETAIL_NOT_EXIST.getValue());
		}
		// generate token
		UserSession userSession = new UserSession(user.getUserId());
		try {
			token = GoForWealthUMAUtil.generateJwtToken(jwtConfiguration, userSession);
		} catch (JsonProcessingException e) {
			throw new GoForWealthUMAException(GoForWealthErrorMessageEnum.COMMON_ERROR_CODE.getValue(), e.getMessage());
		}
		//MimeMessage mail = mailSender.createMimeMessage();
		try {
			//mail.setFrom(new InternetAddress(resetPasswordConfiguration.mailSenderAddress, messageSource.getMessage("resetpassword.mailSenderPersonalName", null, Locale.ENGLISH)));
			//mail.setRecipient(Message.RecipientType.TO, new InternetAddress(user.getEmail()));
			//String userName = user.getFirstName() == null ? user.getUsername() : user.getFirstName();
			String userName = user.getFirstName() == null ? user.getUsername() : user.getFirstName()+" "+user.getLastName();
			String changePasswordUrl = resetPasswordConfiguration.changePasswordUrl + token;
			//mail.setSubject(messageSource.getMessage("resetpassword.mailSubject", null, Locale.ENGLISH));
			//mail.setContent(messageSource.getMessage("resetpassword.mailBody", new String[] { userName, changePasswordUrl }, Locale.ENGLISH), "text/html");
			mailUtility.baselineExample(user.getEmail(), messageSource.getMessage("resetpassword.mailSubject", null, Locale.ENGLISH), messageSource.getMessage("resetpassword.mailBody", new String[] { userName, changePasswordUrl }, Locale.ENGLISH));
		} catch (NoSuchMessageException | IOException e) {
			throw new GoForWealthUMAException(GoForWealthErrorMessageEnum.COMMON_ERROR_CODE.getValue(),GoForWealthUMAConstants.MAIL_SEND_FAILURE_MESSAGE);
		}
		//mailSender.send(mail);
		return GoForWealthUMAConstants.MAIL_SEND_SUCCESS_MESSAGE;
	}

	@Override
	@Transactional
	public User signupByFacebook(UserDTO iotRegisterUserDTO) throws GoForWealthUMAException {
		User user = new User();
		try {	
			user = userRepository.findByUsername(iotRegisterUserDTO.getEmail());
			if(user==null){
				user = userRepository.findByEmail(iotRegisterUserDTO.getEmail());						
			}
			if(user==null){
				user = new User();
				user.setUsername(iotRegisterUserDTO.getEmail());
				//user.setEmail(iotRegisterUserDTO.getEmail());
				user.setPassword(iotRegisterUserDTO.getFacebookId());
				user.setMobileNumber(iotRegisterUserDTO.getMobileNumber());
				user.setEmailVerified(0);
				user.setMobileVerified(0);
				//user.setFirstName(iotRegisterUserDTO.getFirstName());
				user.setFirstName(iotRegisterUserDTO.getFirstName()+" "+iotRegisterUserDTO.getLastName());
				user.setRegisterType(iotRegisterUserDTO.getRegisterType().getRegType());
				user.setStatus(UserStatus.ACTIVE.getStatus());		
				OnboardingStatus onboardingStatus = new OnboardingStatus();
				onboardingStatus.setKycStatus(0);
				onboardingStatus.setMandateStatus(0);
				onboardingStatus.setUccStatus(0);
				onboardingStatus.setFatcaStatus(0);
				onboardingStatus.setAofStatus(0);
				onboardingStatus.setOverallStatus(0);
				onboardingStatus.setUser(user);
				user.setOnboardingStatus(onboardingStatus);
				userRepository.save(user);
				Role roleRef = roleRepository.findByRoleName("USER");
				if(roleRef != null) { 
					UserRole userRole = new UserRole();
					UserRoleId userRoleId = new UserRoleId();
					userRoleId.setRoleId(roleRef.getRoleId());
					userRoleId.setUserId(user.getUserId());
					userRole.setRole(roleRef);
					userRole.setUser(user);
					userRole.setId(userRoleId);
					userRoleRepository.save(userRole);
					//Set<UserRole> userRoles = new HashSet<>();
					//userRoles.add(userRole);
					//ser.setUserRoles(userRoles);
				}
			}
		} catch(Exception ex) {
			System.err.println(ex.getMessage());
			throw new GoForWealthUMAException(GoForWealthErrorMessageEnum.COMMON_ERROR_CODE.getValue(), ex.getMessage());
		}
		return user;
	}

	@Override
	@Transactional
	public User signupByGoogle(UserDTO iotRegisterUserDTO) throws GoForWealthUMAException {
		User user = new User();
		try {
			user = userRepository.findByUsername(iotRegisterUserDTO.getEmail());
			if(user==null){
				user = userRepository.findByEmail(iotRegisterUserDTO.getEmail());
			}
			if(user==null){
				user = new User();
				user.setUsername(iotRegisterUserDTO.getEmail());
				user.setEmail(iotRegisterUserDTO.getEmail());
				user.setPassword(iotRegisterUserDTO.getGoogleId());
				user.setEmailVerified(1);
				user.setMobileNumber(iotRegisterUserDTO.getMobileNumber());
				user.setMobileVerified(0);
				//user.setFirstName(iotRegisterUserDTO.getFirstName());
				user.setFirstName(iotRegisterUserDTO.getFirstName()+" "+iotRegisterUserDTO.getLastName());
				user.setRegisterType(iotRegisterUserDTO.getRegisterType().getRegType());
				user.setStatus(UserStatus.ACTIVE.getStatus());
				OnboardingStatus onboardingStatus = new OnboardingStatus();
				onboardingStatus.setKycStatus(0);
				onboardingStatus.setMandateStatus(0);
				onboardingStatus.setUccStatus(0);
				onboardingStatus.setFatcaStatus(0);
				onboardingStatus.setAofStatus(0);
				onboardingStatus.setOverallStatus(0);
				onboardingStatus.setUser(user);
				user.setOnboardingStatus(onboardingStatus);
				userRepository.save(user);
				Role roleRef = roleRepository.findByRoleName("USER");
				if(roleRef != null) { 
					 UserRole userRole = new UserRole();
					 UserRoleId userRoleId = new UserRoleId();
					 userRoleId.setRoleId(roleRef.getRoleId());
					 userRoleId.setUserId(user.getUserId());
					 userRole.setRole(roleRef);
					 userRole.setUser(user);
					 userRole.setId(userRoleId);
					 userRoleRepository.save(userRole);
					 Set<UserRole> userRoles = new HashSet<>();
					 userRoles.add(userRole);
					 user.setUserRoles(userRoles);
				}
			}
		} catch(Exception ex) {
			throw new GoForWealthUMAException(GoForWealthErrorMessageEnum.COMMON_ERROR_CODE.getValue(), ex.getMessage());
		}
		return user;
	}

	@Override
	@Transactional
	public String registerByMobileNumber(UserDTO registerUserDTO) throws GoForWealthUMAException {
		User user = userRepository.findByMobileNumber(registerUserDTO.getMobileNumber());
		if(user!= null) {
			if(user.getStatus().equals(UserStatus.GUEST.getStatus()))
				throw new GoForWealthUMAException(GoForWealthErrorMessageEnum.COMMON_ERROR_CODE.getValue(), GoForWealthErrorMessageEnum.MOBILE_NO_ALREADY_EXIST_NOT_VERIFIED.getValue());
			else
				throw new GoForWealthUMAException(GoForWealthErrorMessageEnum.COMMON_ERROR_CODE.getValue(), GoForWealthErrorMessageEnum.MOBILE_NO_ALREADY_REGISTERD.getValue());
		}
		user = new User();
		user.setMobileNumber(registerUserDTO.getMobileNumber());
		user.setFirstName(registerUserDTO.getFirstName());
		user.setLastName(registerUserDTO.getLastName());
		user.setEmail(registerUserDTO.getEmail());
		user.setPassword(passwordEncoder.encode(registerUserDTO.getPassword()));
		user.setCreatedTimestamp(new Date());
		user.setUsername(registerUserDTO.getMobileNumber()+"");
		user.setEmailVerified(0);
		user.setMobileVerified(0);
		user.setRegisterType(registerUserDTO.getRegisterType().getRegType());
		user.setStatus(UserStatus.GUEST.getStatus());
		OtpGenerator otpGenerator = new OtpGenerator(4,true);
		String otp = otpGenerator.generateOTP();
		/** Send OTP to mail **//*
		sendOtpToMail(registerUserDTO.getEmail(), otp);
		logger.info("otp---------------: " + otp);
		Otp otpRef = new Otp();
		otpRef.setOtp(passwordEncoder.encode(otp));
		otpRef.setSentTime(new Date());
		otpRef.setMobileNumber(registerUserDTO.getMobileNumber());
		otpRepository.save(otpRef);*/
		/* Send OTP to mail */
		try{
			String userName = registerUserDTO.getFirstName()+" "+registerUserDTO.getLastName();
			String subject=GoForWealthUMAConstants.REGISTER;
			sendOtpToMail(userName,subject,registerUserDTO.getEmail(), otp,registerUserDTO.getMobileNumber());
		} catch(Exception ex) {
			throw new GoForWealthUMAException(GoForWealthErrorMessageEnum.COMMON_ERROR_CODE.getValue(), ex.getMessage());
		}
		//sendOtpToMail(registerUserDTO.getEmail(), otp);
		logger.info("otp---------------: " + otp);
		/*Otp otpRef = new Otp();
		otpRef.setOtp(passwordEncoder.encode(otp));
		otpRef.setSentTime(new Date());
		otpRef.setMobileNumber(registerUserDTO.getMobileNumber());
		otpRepository.save(otpRef);*/
		Otp otpRef=null;
		try {
			otpRef = otpRepository.findByMobileNumber(registerUserDTO.getMobileNumber());
			if(otpRef == null) {
				Otp otpObj = new Otp();
				otpObj.setOtp(passwordEncoder.encode(otp));
				otpObj.setSentTime(new Date());
				otpObj.setMobileNumber(registerUserDTO.getMobileNumber());
				otpRepository.save(otpObj);
			} else {
				otpRef.setOtp(passwordEncoder.encode(otp));
				otpRef.setSentTime(new Date());
				otpRepository.save(otpRef);
			}
		} catch(Exception ex) {
			throw new GoForWealthUMAException(GoForWealthErrorMessageEnum.COMMON_ERROR_CODE.getValue(), ex.getMessage());
		}
		OnboardingStatus onboardingStatus = new OnboardingStatus();
		onboardingStatus.setKycStatus(0);
		onboardingStatus.setMandateStatus(0);
		onboardingStatus.setUccStatus(0);
		onboardingStatus.setFatcaStatus(0);
		onboardingStatus.setAofStatus(0);
		onboardingStatus.setOverallStatus(0);
		onboardingStatus.setIsipMandateStatus(0);
		onboardingStatus.setBillerStatus("Pending");
		onboardingStatus.setEnachStatus("Pending");
		onboardingStatus.setUser(user);
		user.setOnboardingStatus(onboardingStatus);
		user = userRepository.save(user);
		Role roleRef = roleRepository.findByRoleName(registerUserDTO.getRole());
		if(roleRef != null) { 
			 UserRole userRole = new UserRole();
			 UserRoleId userRoleId = new UserRoleId();
			 userRoleId.setRoleId(roleRef.getRoleId());
			 userRoleId.setUserId(user.getUserId());
			 userRole.setRole(roleRef);
			 userRole.setUser(user);
			 userRole.setId(userRoleId);
			 userRoleRepository.save(userRole);
		}
		return GoForWealthUMAConstants.OTP_SEND_SUCCESS_MESSAGE;
	}
	
	
	@Override
	@Transactional
	public String signupByGuestUser(UserDTO registerUserDTO) throws GoForWealthUMAException {
		User user = userRepository.findByMobileNumber(registerUserDTO.getMobileNumber());
		if (user != null) {
			if (user.getStatus().equals(UserStatus.GUEST.getStatus()))
				throw new GoForWealthUMAException(GoForWealthErrorMessageEnum.COMMON_ERROR_CODE.getValue(),GoForWealthErrorMessageEnum.MOBILE_NO_ALREADY_EXIST_NOT_VERIFIED.getValue());
			else
				throw new GoForWealthUMAException(GoForWealthErrorMessageEnum.COMMON_ERROR_CODE.getValue(),GoForWealthErrorMessageEnum.MOBILE_NO_ALREADY_REGISTERD.getValue());
		}
		/*
		if(user==null) {
			user = userRepository.findByEmail(registerUserDTO.getEmail());
		}
		if(user != null) {
			if(user.getStatus().equals(UserStatus.GUEST.getStatus()))
				throw new GoForWealthUMAException(GoForWealthErrorMessageEnum.COMMON_ERROR_CODE.getValue(), GoForWealthErrorMessageEnum.EMAIL_ID_ALREADY_EXIST_NOT_VERIFIED.getValue());
			else
				throw new GoForWealthUMAException(GoForWealthErrorMessageEnum.COMMON_ERROR_CODE.getValue(), GoForWealthErrorMessageEnum.EMAIL_ID_ALREADY_REGISTERD.getValue());
		}
		*/
		GoalsOrder goalsOrder = goalsOrderRepository.getOne((registerUserDTO.getGoalsOrderId()));
		user = userRepository.getOne(goalsOrder.getUser().getUserId());
		user.setMobileNumber(registerUserDTO.getMobileNumber());
		user.setFirstName(registerUserDTO.getFirstName());
		user.setLastName(registerUserDTO.getLastName());
		user.setEmail(registerUserDTO.getEmail());
		user.setPassword(passwordEncoder.encode(registerUserDTO.getPassword()));
		user.setCreatedTimestamp(new Date());
		user.setUsername(registerUserDTO.getMobileNumber()+"");
		user.setEmailVerified(0);
		user.setMobileVerified(0);
		user.setRegisterType(registerUserDTO.getRegisterType().getRegType());
		user.setStatus(UserStatus.GUEST.getStatus());
		OtpGenerator otpGenerator = new OtpGenerator(4,true);
		String otp = otpGenerator.generateOTP();
		/** Send OTP to mail **//*
		sendOtpToMail(registerUserDTO.getEmail(), otp);
		logger.info("otp---------------: " + otp);
		Otp otpRef = new Otp();
		otpRef.setOtp(passwordEncoder.encode(otp));
		otpRef.setSentTime(new Date());
		otpRef.setMobileNumber(registerUserDTO.getMobileNumber());
		otpRepository.save(otpRef);*/
		/* Send OTP to mail */
		try{
			//String userName = registerUserDTO.getFirstName();
			String userName = registerUserDTO.getFirstName()+" "+registerUserDTO.getLastName();
			String subject=GoForWealthUMAConstants.REGISTER;
			sendOtpToMail(userName,subject,registerUserDTO.getEmail(), otp,registerUserDTO.getMobileNumber());
		} catch(Exception ex) {
			throw new GoForWealthUMAException(GoForWealthErrorMessageEnum.COMMON_ERROR_CODE.getValue(), ex.getMessage());
		}
		//sendOtpToMail(registerUserDTO.getEmail(), otp);
		logger.info("otp---------------: " + otp);
		/*Otp otpRef = new Otp();
		otpRef.setOtp(passwordEncoder.encode(otp));
		otpRef.setSentTime(new Date());
		otpRef.setMobileNumber(registerUserDTO.getMobileNumber());
		otpRepository.save(otpRef);*/
		Otp otpRef=null;
		try {
			otpRef = otpRepository.findByMobileNumber(registerUserDTO.getMobileNumber());
			if(otpRef == null) {
				Otp otpObj = new Otp();
				otpObj.setOtp(passwordEncoder.encode(otp));
				otpObj.setSentTime(new Date());
				otpObj.setMobileNumber(registerUserDTO.getMobileNumber());
				otpRepository.save(otpObj);
			} else {
				otpRef.setOtp(passwordEncoder.encode(otp));
				otpRef.setSentTime(new Date());
				otpRepository.save(otpRef);
			}
		} catch(Exception ex) {
			throw new GoForWealthUMAException(GoForWealthErrorMessageEnum.COMMON_ERROR_CODE.getValue(), ex.getMessage());
		}
		OnboardingStatus onboardingStatus = user.getOnboardingStatus();
		if(onboardingStatus!=null){
			onboardingStatus.setKycStatus(0);
			onboardingStatus.setMandateStatus(0);
			onboardingStatus.setUccStatus(0);
			onboardingStatus.setFatcaStatus(0);
			onboardingStatus.setAofStatus(0);
			onboardingStatus.setOverallStatus(0);
			onboardingStatus.setIsipMandateStatus(0);
			onboardingStatus.setBillerStatus("Pending");
			onboardingStatus.setEnachStatus("Pending");
			onboardingStatus.setUser(user);
			user.setOnboardingStatus(onboardingStatus);
		}else{
			OnboardingStatus onboardingStatuss = new OnboardingStatus();
			onboardingStatuss.setKycStatus(0);
			onboardingStatuss.setMandateStatus(0);
			onboardingStatuss.setUccStatus(0);
			onboardingStatuss.setFatcaStatus(0);
			onboardingStatuss.setAofStatus(0);
			onboardingStatuss.setOverallStatus(0);
			onboardingStatuss.setIsipMandateStatus(0);
			onboardingStatuss.setBillerStatus("Pending");
			onboardingStatuss.setEnachStatus("Pending");
			onboardingStatuss.setUser(user);
			user.setOnboardingStatus(onboardingStatuss);
		}
		user = userRepository.save(user);
		Role roleRef = roleRepository.findByRoleName(registerUserDTO.getRole());
		if(roleRef != null) { 
			 UserRole userRole = new UserRole();
			 UserRoleId userRoleId = new UserRoleId();
			 userRoleId.setRoleId(roleRef.getRoleId());
			 userRoleId.setUserId(user.getUserId());
			 userRole.setRole(roleRef);
			 userRole.setUser(user);
			 userRole.setId(userRoleId);
			 userRoleRepository.save(userRole);
		}
		return GoForWealthUMAConstants.OTP_SEND_SUCCESS_MESSAGE;
	}
	
	
	@Override
	public UserLoginResponseDTO login(String userName, String password) {
		return null;
    }
	
	
	@Override
	@Transactional
	public String registerAgent(RegisterAgentDTO registerAgentDTO) throws GoForWealthUMAException {
		User user = userRepository.findByMobileNumber(registerAgentDTO.getMobileNumber());
		if(user!= null) {
			throw new GoForWealthUMAException(GoForWealthErrorMessageEnum.COMMON_ERROR_CODE.getValue(), GoForWealthErrorMessageEnum.MOBILE_NO_ALREADY_REGISTERD.getValue());
		}
		user = new User();
		user.setMobileNumber(registerAgentDTO.getMobileNumber());
		user.setPassword(passwordEncoder.encode(registerAgentDTO.getPassword()));
		user.setCreatedTimestamp(new Date());
		user.setUsername(registerAgentDTO.getMobileNumber()+"");
		user.setRegisterType(registerAgentDTO.getRegisterType().getRegType());
		//TODO need validation code for email,arnCode mobileNumber,aadhaarNumber 
		user = userRepository.save(user);
		Role roleRef=roleRepository.findByRoleName(registerAgentDTO.getRole());
		if(roleRef != null){
			UserRole userRole= new UserRole();
			UserRoleId userRoleId= new UserRoleId();
			userRoleId.setRoleId(roleRef.getRoleId());
			userRoleId.setUserId(user.getUserId());
			userRole.setId(userRoleId);
			userRole.setRole(roleRef);
			userRole.setUser(user);
			userRoleRepository.save(userRole);
		}
		else if(roleRef == null){
			throw new GoForWealthUMAException(GoForWealthErrorMessageEnum.INTERNAL_SERVER_ERROR.getValue());
		}
		return GoForWealthUMAConstants.AGENT_REGISTER_SECCESSFULLY;
	}
	
	
	
	/*@Override
	@Transactional
	public boolean editUserDetails(UserDTO registerUserDTO, int userId) throws GoForWealthUMAException {
		User userRef = userRepository.findByUserId(userId);
		boolean flag = false;
		try {
			SimpleDateFormat sdf = new SimpleDateFormat("dd-MM-yyyy");
			String dateOfBirth = registerUserDTO.getDateOfBirth();
			Date dateRef = sdf.parse(dateOfBirth);
			if(userRef != null) {
				UserProfile userProfile = userRef.getUserProfile();
				if(userProfile == null) {
					UserProfile userProfileRef = new UserProfile();
					userProfileRef.setAadhaarNumber(registerUserDTO.getAadhaarNumber());
					userProfileRef.setPanNumber(registerUserDTO.getPanNumber());
					userProfileRef.setCreatedTimestamp(new Date());
					userProfileRef.setUpdatedDateTime(new Date());
					userProfileRef.setUser(userRef);
					userProfileRepository.save(userProfileRef);
				} else {
					userProfile.setAadhaarNumber(registerUserDTO.getAadhaarNumber());
					userProfile.setPanNumber(registerUserDTO.getPanNumber());
					userProfile.setCreatedTimestamp(new Date());
					userProfile.setUpdatedDateTime(new Date());
					userProfile.setUser(userRef);
				}
				Set<Address> addressRef = userRef.getAddresses();
				if(addressRef.isEmpty()) {
					Address address = new Address();
					address.setCountry(registerUserDTO.getCountry());
					address.setState(registerUserDTO.getState());
					address.setCity(registerUserDTO.getCity());
					address.setPincode(registerUserDTO.getPincode());
					address.setUser(userRef);
					addressRepository.save(address);
				}
				for (Address address2 : addressRef) {
					address2.setCountry(registerUserDTO.getCountry());
					address2.setState(registerUserDTO.getState());
					address2.setCity(registerUserDTO.getCity());
					address2.setPincode(registerUserDTO.getPincode());
					address2.setUser(userRef);
					userRef.getAddresses().add(address2);
				}
				userRef.setFirstName(registerUserDTO.getFirstName());
				userRef.setLastName(registerUserDTO.getLastName());
				userRef.setGender(registerUserDTO.getGender());
				userRef.setMobileNumber(registerUserDTO.getMobileNumber());
				userRef.setDateOfBirth(dateRef);
				userRef.setUpdatedDateTime(new Date());
				userRef.setUserProfile(userProfile);
				userRepository.save(userRef);
				flag = true;
	        }
	    } catch(Exception ex) {
	    	throw new GoForWealthUMAException(GoForWealthErrorMessageEnum.COMMON_ERROR_CODE.getValue(), ex.getMessage());
	    }
		return flag;
	}*/

	@Override
	public UserLoginResponseDTO getUser(Integer userId,String action) {
		DateFormat format = new SimpleDateFormat("yyyy-MM-dd");
		User user = userRepository.getOne(userId);
		UserLoginResponseDTO userLoginResponseDTO = new UserLoginResponseDTO();
		if (user != null) {
			userLoginResponseDTO.setUserId(user.getUserId());
			// userLoginResponseDTO.setUserName(user.getFirstName());
			userLoginResponseDTO.setUserName(user.getFirstName() + " " + user.getLastName());
			userLoginResponseDTO.setEmail(user.getEmail());
			userLoginResponseDTO.setMobile(user.getMobileNumber());
			userLoginResponseDTO.setRegisterType(user.getRegisterType());
			userLoginResponseDTO.setStatus(user.getOnboardingStatus().getOverallStatus());
			// userLoginResponseDTO.setFullName(user.getFirstName());
			userLoginResponseDTO.setFullName(user.getFirstName() + " " + user.getLastName());
			if (user.getEmailVerified() == 1) {
				userLoginResponseDTO.setEmailVerified(true);
			} else {
				userLoginResponseDTO.setEmailVerified(false);
			}
			if (user.getMobileVerified() == MobileVerification.VERIFIED.getStatus()) {
				userLoginResponseDTO.setMobileVerified(true);
			} else {
				userLoginResponseDTO.setMobileVerified(false);
			}
			if (user.getPanDetails() != null) {
				if (user.getPanDetails().getPanNo() != null) {
					if (user.getPanDetails().getVerified().equals(GoForWealthPRSConstants.PAN_NUMBER_VERIFIED)) {
						userLoginResponseDTO.setPanVerified(true);
					} else {
						userLoginResponseDTO.setPanVerified(false);
					}
					/*** ***/
					if (user.getPanDetails().getPanNo().equals("") || user.getPanDetails().getPanNo() == null) {
						userLoginResponseDTO.setPanNumber("");
					} else {
						try {
							String panNumber = encryptUserDetail.decrypt(user.getPanDetails().getPanNo());
							if(!action.equals("fullData"))panNumber = panNumber.substring(0, 2) + "XX" + panNumber.charAt(5) + "X"+ panNumber.substring(7);	
							userLoginResponseDTO.setPanNumber(panNumber.toUpperCase());
							
						} catch (Exception ex) {
							ex.printStackTrace();
						}
					}
					// userLoginResponseDTO.setPanNumber(user.getPanDetails().getPanNo());
					userLoginResponseDTO.setPancardName(user.getPanDetails().getFullName());
					if (user.getPanDetails().getDateOfBirth() != null)
						userLoginResponseDTO.setDob(format.format(user.getPanDetails().getDateOfBirth()));
					if (user.getPanDetails().getGender() != null)
						userLoginResponseDTO.setGender(user.getPanDetails().getGender());
					if (user.getPanDetails().getOccupation() != null) {
						userLoginResponseDTO.setOccupation(user.getPanDetails().getOccupation());
					}
				}
			}
			if (user.getBankDetails() != null) {
				userLoginResponseDTO.setBankName(user.getBankDetails().getBankName());
				userLoginResponseDTO.setIfscCode(user.getBankDetails().getIfsc());
				/*** ***/
				String decryptBankAccountNo = "";
				try {
					decryptBankAccountNo = encryptUserDetail.decrypt(user.getBankDetails().getAccountNo());
					int noOfLength = decryptBankAccountNo.length();
					String dummyChars = "";
					for (int i = 0; i < noOfLength - 4; i++)
						dummyChars += "X";
					if(!action.equals("fullData"))decryptBankAccountNo = decryptBankAccountNo.substring(0, 2) + dummyChars+ decryptBankAccountNo.substring(noOfLength - 2);
					
				} catch (Exception ex) {
					ex.printStackTrace();
				}
				userLoginResponseDTO.setAccountNumber(decryptBankAccountNo);
				userLoginResponseDTO.setAccountType(user.getBankDetails().getAccountType());
				userLoginResponseDTO.setMicrCode(user.getBankDetails().getMicrCode());
				userLoginResponseDTO.setNomineeName(user.getBankDetails().getNomineeName());
				userLoginResponseDTO.setNomineeRelation(user.getBankDetails().getNomineeRelation());
				userLoginResponseDTO.setStartDate(format.format(user.getBankDetails().getStartDate()));
				userLoginResponseDTO.setEndDate(format.format(user.getBankDetails().getEndDate()));
				userLoginResponseDTO.setBankAddress(user.getBankDetails().getBankAddress());
				userLoginResponseDTO.setBankBranch(user.getBankDetails().getBankBranch());
				if (user.getBankDetails().getBankAddress() != null) {
					userLoginResponseDTO.setIfscCodeVerified(true);
				} else {
					userLoginResponseDTO.setIfscCodeVerified(false);
				}
				userLoginResponseDTO.setUserId(user.getUserId());
				if (user.getBankDetails().getSignatureImage() != null) {
					userLoginResponseDTO.setSignatureImageExist(true);
				} else {
					userLoginResponseDTO.setSignatureImageExist(false);
				}
			}
			if (user.getKycDetails() != null) {
				userLoginResponseDTO.setMotherName(user.getKycDetails().getMotherName());
				userLoginResponseDTO.setFatherName(user.getKycDetails().getFatherName());
				userLoginResponseDTO.setMaritalStatus(user.getKycDetails().getMaritalStatus());
				if (user.getKycDetails().getPhotograph() != null) {
					userLoginResponseDTO.setUserImageExist(true);
				} else {
					userLoginResponseDTO.setUserImageExist(false);
				}
				if (user.getKycDetails().getPanCardImage() != null) {
					userLoginResponseDTO.setPancardImageExist(true);
				} else {
					userLoginResponseDTO.setPancardImageExist(false);
				}
			}
			if (user.getAddressProofs() != null && user.getAddressProofs().size() > 0) {
				AddressProof addressProof = user.getAddressProofs().iterator().next();
				AddressProofDTO addressProofDTO = new AddressProofDTO();
				addressProofDTO.setAddressProofName(addressProof.getAddressProofName());
				/*** ***/
				String decryptAadharNo = "";
				try {
					if (addressProof.getAddressProofNo() != null && !addressProof.getAddressProofNo().equals("")) {
						decryptAadharNo = encryptUserDetail.decrypt(addressProof.getAddressProofNo());
					}
				} catch (Exception ex) {
					ex.printStackTrace();
				}
				addressProofDTO.setAddressProofNo(decryptAadharNo);
				addressProofDTO.setCity(addressProof.getCity());
				addressProofDTO.setPincode(addressProof.getPincode());
				addressProofDTO.setState(addressProof.getState());
				addressProofDTO.setAddress(addressProof.getAddress());
				addressProofDTO.setAddressLine1(addressProof.getField2());
				addressProofDTO.setAddressLine2(addressProof.getField3());
				addressProofDTO.setMobileWithAadhaar(addressProof.getMobileWithAadhaar());
				addressProofDTO.setVerified(addressProof.getVerified());
				if (addressProof.getVerified() != null) {
					if (addressProof.getVerified().equals(GoForWealthPRSConstants.AADHAAR_NUMBER_VERIFIED)) {
						addressProofDTO.setAadhaarVerified(true);
					} else {
						addressProofDTO.setAadhaarVerified(false);
					}
				}
				userLoginResponseDTO.setAddressProof(addressProofDTO);
				if (addressProof.getFrontImage() != null && addressProof.getBackImage() != null) {
					userLoginResponseDTO.setAdharcardImageExist(true);
				} else {
					userLoginResponseDTO.setAdharcardImageExist(false);
				}
				userLoginResponseDTO.getAddressProof().setAddressType(addressProof.getAddressType());
				userLoginResponseDTO.getAddressProof().setIncomeSlab(addressProof.getIncomeSlab());
				userLoginResponseDTO.getAddressProof().setPep(addressProof.getPep());
			}
			userLoginResponseDTO.setUserOverallStatus(user.getOnboardingStatus().getOverallStatus());
			userLoginResponseDTO.setMessage(GoForWealthUMAConstants.SUCCESS);
			userLoginResponseDTO.setMandateStatus(user.getOnboardingStatus().getEnachStatus());
			userLoginResponseDTO.setIsipId(user.getOnboardingStatus().getIsipMandateNumber());
			userLoginResponseDTO.setIsipStatus(user.getOnboardingStatus().getBillerStatus());
			userLoginResponseDTO.setXsipId(user.getOnboardingStatus().getMandateNumber());
			userLoginResponseDTO.setXsipStatus(user.getOnboardingStatus().getEnachStatus());
			userLoginResponseDTO.setUccClientCode(user.getOnboardingStatus().getClientCode());
			userLoginResponseDTO.setMandateStatus(String.valueOf(user.getOnboardingStatus().getMandateStatus()));
			userLoginResponseDTO.setUploadMandateStatus(user.getOnboardingStatus().getUploadMandateStatus());
			List<Orders> orderList = orderRepository.getOrderByUserId(userId);
			if (orderList.isEmpty()) {
				userLoginResponseDTO.setOrderStatus("false");
			} else {
				userLoginResponseDTO.setOrderStatus("true");
			}
			userLoginResponseDTO.setUserGoalExist(goForWealthSIPService.isUserGoalExist(userId));
			userLoginResponseDTO.setGoalSize(user.getGoalOrderItemses().size());
		} else {
			userLoginResponseDTO.setMessage(GoForWealthUMAConstants.USER_NOT_EXIST);
		}
		return userLoginResponseDTO;
	}

	@Override
	public UserDTO getLoggedUserDetail(int userId) throws GoForWealthUMAException {
		UserDTO userDto = null;
		try {
			User user = userRepository.getOne(userId);
			if (user != null) {
				userDto = new UserDTO();
				userDto.setUserName(user.getUsername());
				Set<UserRole> userRole = user.getUserRoles();
				if (!userRole.isEmpty()) {
					for (UserRole userRoleRef : userRole) {
						userDto.setRole(userRoleRef.getRole().getRoleName());
					}
				}
			}
		} catch (Exception ex) {
			throw new GoForWealthUMAException(GoForWealthErrorMessageEnum.COMMON_ERROR_CODE.getValue(),GoForWealthErrorMessageEnum.FAILURE_MESSAGE.getValue());
		}
		return userDto;
	}

	@Override
	public List<UserAccountDTO> getAllAccounts(Integer userId) throws GoForWealthUMAException {
		List<User> userListRef = userRepository.findAll();
		if (userListRef.isEmpty()) {
			throw new GoForWealthUMAException(GoForWealthErrorMessageEnum.DATA_NOT_FOUND_CODE.getValue(),
					GoForWealthErrorMessageEnum.DATA_NOT_FOUND_MESSAGE.getValue());
		}
		List<UserAccountDTO> userAccountDTOList = new ArrayList<>();
		SimpleDateFormat sdf = new SimpleDateFormat("dd-MM-yyyy");
		for (User user : userListRef) {
			UserAccountDTO userAccountDTO = new UserAccountDTO();
			userAccountDTO.setUserId(user.getUserId());
			// userAccountDTO.setName(user.getFirstName());
			userAccountDTO.setName(user.getFirstName() + " " + user.getLastName());
			userAccountDTO.setEmail(user.getUsername());
			userAccountDTO.setGender(user.getGender());
			userAccountDTO.setDateOfBirth(sdf.format(user.getDateOfBirth()));
			userAccountDTO.setMobileNumber(user.getMobileNumber());
			userAccountDTO.setRegisterDate(sdf.format(user.getCreatedTimestamp()));
			userAccountDTO.setUserAccountStatus(user.getStatus());
			userAccountDTO.setRegisterType(user.getRegisterType());
			//userAccountDTO.setAadhaarNumber(user.getUserProfile().getAadhaarNumber());
			//userAccountDTO.setPanNumber(user.getUserProfile().getPanNumber());
			/*Set<Address> addressRef = user.getAddresses();
			for (Address address : addressRef) {
				userAccountDTO.setCountry(address.getCountry());
				userAccountDTO.setState(address.getState());
				userAccountDTO.setCity(address.getCity());
				userAccountDTO.setPincode(address.getPincode());
			}*/
			Set<UserRole> userRoleSet = user.getUserRoles();
			for (UserRole userRole : userRoleSet) {
				userAccountDTO.setUserRole(userRole.getRole().getRoleName());
			}
			userAccountDTOList.add(userAccountDTO);
		}
		return userAccountDTOList;
	}

	@Override
	public void deleteAccounts(String emailId) {
		// TODO Auto-generated method stub
	}

	@Override
	@Transactional
	public String verifyMobileNumber(String otp, String mobileNumber) {
		User user = userRepository.findByMobileNumber(mobileNumber);
		Otp otpRef = otpRepository.findByMobileNumber(mobileNumber);
		logger.info("OTP: " + otp + "... Mobile No : " + mobileNumber);
		if (otpRef == null) {
			return GoForWealthErrorMessageEnum.USER_DETAIL_NOT_EXIST.getValue();
		}
		String encodedOtp = otpRef.getOtp();
		Date now = new Date();
		Date previousDate = otpRef.getSentTime();
		long difference = now.getTime() - previousDate.getTime();
		long diffMinutes = difference / (60 * 1000) % 60;
		long diffHours = difference / (60 * 60 * 1000) % 24;
		long diffDays = difference / (24 * 60 * 60 * 1000);
		if (passwordEncoder.matches(otp, encodedOtp)) {
			if (diffDays == 0 && diffHours == 0 && diffMinutes < 5) {
				user.setStatus(UserStatus.ACTIVE.getStatus());
				user.setMobileVerified(MobileVerification.VERIFIED.getStatus());
				userRepository.save(user);
				otpRepository.delete(otpRef);
			} else {
				logger.info(GoForWealthErrorMessageEnum.OTP_EXPIRED.getValue());
				return GoForWealthErrorMessageEnum.OTP_EXPIRED.getValue();
			}
		} else {
			logger.info(GoForWealthErrorMessageEnum.INVALID_OTP.getValue());
			return GoForWealthErrorMessageEnum.INVALID_OTP.getValue();
		}
		logger.info("OTP Verified Successfully");
		return GoForWealthUMAConstants.MOBILE_NUMBER_VERIFIED_SUCCESS_MESSAGE;
	}

	@Override
	public String forgetPassword(String mobileNumber) throws GoForWealthUMAException {
		User userRef = userRepository.findByMobileNumber(mobileNumber);
		if (userRef != null) {
			Otp otpRef = otpRepository.findByMobileNumber(mobileNumber);
			String otp = "";
			if (otpRef == null) {
				OtpGenerator otpGenerator = new OtpGenerator(4, true);
				otp = otpGenerator.generateOTP();
				logger.info("Generated Otp Is: " + otp);
				Otp object = new Otp();
				object.setOtp(passwordEncoder.encode(otp));
				object.setSentTime(new Date());
				object.setMobileNumber(mobileNumber);
				otpRepository.save(object);
				// TODO code to send otp to mobile number
			} else {
				OtpGenerator otpGenerator = new OtpGenerator(4, true);
				otp = otpGenerator.generateOTP();
				logger.info("Generated Otp Is: " + otp);
				otpRef.setOtp(passwordEncoder.encode(otp));
				otpRef.setSentTime(new Date());
				otpRef.setMobileNumber(mobileNumber);
				otpRepository.save(otpRef);
			}
			try {
				String userName = userRef.getFirstName() == null ? userRef.getUsername() : userRef.getFirstName() + " " + userRef.getLastName();
				String subject = GoForWealthUMAConstants.FORGOT;
				sendOtpToMail(userName, subject, userRef.getEmail(), otp, userRef.getMobileNumber());
			} catch (Exception ex) {
				throw new GoForWealthUMAException(GoForWealthErrorMessageEnum.COMMON_ERROR_CODE.getValue(),ex.getMessage());
			}
		} else {
			throw new GoForWealthUMAException(GoForWealthErrorMessageEnum.DATA_NOT_FOUND_CODE.getValue(),GoForWealthErrorMessageEnum.USER_DETAIL_NOT_EXIST.getValue());
		}
		return GoForWealthUMAConstants.OTP_SEND_SUCCESS_MESSAGE;
	}

	@Override
	public String forgetPasswordVerifyOtp(String mobileNumber, String otp) {
		Otp otpRef = otpRepository.findByMobileNumber(mobileNumber);
		if (otpRef == null) {
			return GoForWealthErrorMessageEnum.USER_DETAIL_NOT_EXIST.getValue();
		} else {
			String message = OtpVerify.verifyOtp(otp, mobileNumber, otpRef, passwordEncoder);
			otpRepository.delete(otpRef);
			logger.info("Message ================: " + message);
			return message;
		}
	}

	@Override
	@Transactional
	public boolean resetPassword(UserDTO userDTO) throws GoForWealthUMAException {
		boolean flag = false;
		User userRef = userRepository.findByMobileNumber(userDTO.getMobileNumber());
		try {
			if (userRef == null) {
				throw new GoForWealthUMAException(GoForWealthErrorMessageEnum.DATA_NOT_FOUND_CODE.getValue(),
						GoForWealthErrorMessageEnum.USER_DETAIL_NOT_EXIST.getValue());
			} else {
				userRef.setPassword(passwordEncoder.encode(userDTO.getPassword()));
				userRepository.save(userRef);
				flag = true;
			}
		} catch (Exception ex) {
			throw new GoForWealthUMAException(GoForWealthErrorMessageEnum.COMMON_ERROR_CODE.getValue(),
					ex.getMessage());
		}
		return flag;
	}

	@Override
	public String changePassword(ForgetPasswordDTO forgetPasswordDTO, int userId) throws GoForWealthUMAException {
		User userRef = userRepository.getOne(userId);
		if (userRef == null) {
			throw new GoForWealthUMAException(GoForWealthErrorMessageEnum.DATA_NOT_FOUND_CODE.getValue(),GoForWealthAdminErrorMessageEnum.USER_DETAIL_NOT_EXIST.getValue());
		}
		try {
			String userName = userRef.getFirstName() == null ? userRef.getUsername() : userRef.getFirstName() + " " + userRef.getLastName();
			String otp = null;
			String subject = GoForWealthUMAConstants.CHANGED_PASSWORD_EMAIL_SUBJECT;
			boolean flag = true;
			if (flag) {
				userRef.setPassword(passwordEncoder.encode(forgetPasswordDTO.getNewPassword()));
				userRepository.save(userRef);
			} else {
				throw new Exception();
			}
		} catch (Exception ex) {
			throw new GoForWealthUMAException(GoForWealthErrorMessageEnum.COMMON_ERROR_CODE.getValue(),ex.getMessage());
		}
		return GoForWealthUMAConstants.PASSWORD_CHANGED_SUCCESSFULLY;
	}

	@Override
	public void editAgentDetails(RegisterAgentDTO registerAgentDTO, int userId) throws GoForWealthUMAException {
		// TODO Auto-generated method stub
	}

	@Override
	public CommissionDetailDTO getCommissionDetails(int userId) throws GoForWealthUMAException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	@Transactional
	public String resendOtp(String mobileNumber, String email, String type) throws GoForWealthUMAException {
		String subject = type;
		OtpGenerator otpGenerator = new OtpGenerator(4, false);
		Otp otpRef = null;
		String otp = otpGenerator.generateOTP();
		User userObj = userRepository.findByMobileNumber(mobileNumber);
		logger.info("OTP : " + otp + "...Mobile No : " + mobileNumber);
		try {
			otpRef = otpRepository.findByMobileNumber(mobileNumber);
			if (otpRef == null) {
				Otp otpObj = new Otp();
				otpObj.setOtp(passwordEncoder.encode(otp));
				otpObj.setSentTime(new Date());
				otpObj.setMobileNumber(mobileNumber);
				otpRepository.save(otpObj);
			} else {
				otpRef.setOtp(passwordEncoder.encode(otp));
				otpRef.setSentTime(new Date());
				otpRepository.save(otpRef);
			}		
		} catch(Exception ex) {
			throw new GoForWealthUMAException(GoForWealthErrorMessageEnum.COMMON_ERROR_CODE.getValue(), ex.getMessage());
		}
		try{
			String userName = userObj.getFirstName() == null ? userObj.getUsername() : userObj.getFirstName()+" "+userObj.getLastName();
			sendOtpToMail(userName,subject ,userObj.getEmail(),otp,userObj.getMobileNumber());
		} catch(Exception ex) {
			throw new GoForWealthUMAException(GoForWealthErrorMessageEnum.COMMON_ERROR_CODE.getValue(), ex.getMessage());
		}
		return "Success";
	}

	@Override
	public String sendEmailVerification(Integer userId, UserDTO registerUserDTO) throws GoForWealthUMAException {
		User user = null;
		String token = null;
		user = userRepository.getOne(userId);
		if(user == null) {
			throw new GoForWealthUMAException(GoForWealthErrorMessageEnum.DATA_NOT_FOUND_CODE.getValue(),GoForWealthErrorMessageEnum.USER_DETAIL_NOT_EXIST.getValue());
		}
		user.setEmail(registerUserDTO.getEmail());
		userRepository.save(user);
		//generate token
		UserSession userSession = new UserSession(user.getUserId());
		try {
			token = GoForWealthUMAUtil.generateJwtToken(jwtConfiguration, userSession);
		}catch (JsonProcessingException e) {
			throw new GoForWealthUMAException(GoForWealthErrorMessageEnum.COMMON_ERROR_CODE.getValue(),e.getMessage());
		}
		//MimeMessage mail = mailSender.createMimeMessage();
		try {
			/*
			 	mail.setFrom(new InternetAddress(emailVerificationConfiguration.mailSenderAddress, messageSource.getMessage("verifyemail.mailSenderPersonalName", null, Locale.ENGLISH)));
				mail.setRecipient(Message.RecipientType.TO, new InternetAddress(user.getEmail()));
				mail.setSubject(messageSource.getMessage("verifyemail.mailSubject", null, Locale.ENGLISH));
				mail.setContent(messageSource.getMessage("verifyemail.mailBody", new String[] { userName, verifyEmailUrl }, Locale.ENGLISH), "text/html");
			*/
			String userName = user.getFirstName() == null ? user.getUsername() : user.getFirstName()+" "+user.getLastName();
			String verifyEmailLink ="<a href="+ emailVerificationConfiguration.verifyEmailUrl + token +"> here </a>";
			String verifyEmailUrl =emailVerificationConfiguration.verifyEmailUrl + token;
			String type = "support";
			mailUtility.baselineExamples(user.getEmail(), messageSource.getMessage("verifyemail.mailSubject", null, Locale.ENGLISH), messageSource.getMessage("verifyemail.mailBody", new String[] { userName,verifyEmailLink, verifyEmailUrl }, Locale.ENGLISH),type);
		} catch (NoSuchMessageException | IOException e) {
			e.printStackTrace();
		}
		return GoForWealthUMAConstants.MAIL_SEND_SUCCESS_MESSAGE;
	}

	@Override
	public String emailVerification(Integer profileId) throws GoForWealthUMAException {
		User user = null;
		user = userRepository.getOne(profileId);
		if(user == null) {
			throw new GoForWealthUMAException(GoForWealthErrorMessageEnum.DATA_NOT_FOUND_CODE.getValue(),GoForWealthErrorMessageEnum.USER_DETAIL_NOT_EXIST.getValue());
		}
		user.setEmailVerified(1);
		userRepository.save(user);
		logger.info("UserId : " + profileId + "...." + GoForWealthUMAConstants.EMAIL_ID_VERIFIED_SUCCESS_MESSAGE);
		return GoForWealthUMAConstants.EMAIL_ID_VERIFIED_SUCCESS_MESSAGE;
	}
	
	
	@Override
	public boolean sendOtpToMail(String userName,String subject,String email, String otp, String mobibeNumber) {
		boolean flag = false;
		//MimeMessage mail = mailSender.createMimeMessage();
		String emailSubject="";
		String emailContent="";
		try {
			//mail.setFrom(new InternetAddress(emailVerificationConfiguration.mailSenderAddress, messageSource.getMessage("verifyemail.mailSenderPersonalName", null, Locale.ENGLISH)));
			//mail.setRecipient(Message.RecipientType.TO, new InternetAddress(email));
			if(subject.equals(GoForWealthUMAConstants.FORGOT)){
				/*
				emailSubject=GoForWealthUMAConstants.FORGOT_PASSWORD_EMAIL_SUBJECT;
				emailContent=messageSource.getMessage("forgotPass.sendOtp.mailBody", new String[] { userName, otp }, Locale.ENGLISH);
				mailUtility.baselineExample(email, emailSubject, emailContent);
				*/
				//mail.setSubject(GoForWealthUMAConstants.FORGOT_PASSWORD_EMAIL_SUBJECT);
				//mail.setContent(messageSource.getMessage("forgotPass.sendOtp.mailBody", new String[] { userName, otp }, Locale.ENGLISH), "text/html");
			}else if(subject.equals(GoForWealthUMAConstants.REGISTER)){
				/*
				emailSubject=GoForWealthUMAConstants.MOBILE_VERIFICATION_EMAIL_SUBJECT;
				emailContent=messageSource.getMessage("mobileVerify.sendOtp.mailBody", new String[] { userName, otp }, Locale.ENGLISH);
				mailUtility.baselineExample(email, emailSubject, emailContent);
				*/
				//mail.setSubject(GoForWealthUMAConstants.MOBILE_VERIFICATION_EMAIL_SUBJECT);
				//mail.setContent(messageSource.getMessage("mobileVerify.sendOtp.mailBody", new String[] { userName, otp }, Locale.ENGLISH), "text/html");
			}else if(subject.equals(GoForWealthUMAConstants.ONBOARDING)){
				/*
				emailSubject=GoForWealthUMAConstants.MOBILE_VERIFICATION_EMAIL_SUBJECT;
				emailContent=messageSource.getMessage("mobileVerify.sendOtp.mailBody", new String[] { userName, otp }, Locale.ENGLISH);
				mailUtility.baselineExample(email, emailSubject, emailContent);
				*/
				//mail.setSubject(GoForWealthUMAConstants.MOBILE_VERIFICATION_EMAIL_SUBJECT);
				//mail.setContent(messageSource.getMessage("mobileVerify.sendOtp.mailBody", new String[] { userName, otp }, Locale.ENGLISH), "text/html");*/
			}else if(subject.equals(GoForWealthUMAConstants.CHANGED_PASSWORD_EMAIL_SUBJECT)){
				emailSubject=subject;
				emailContent=messageSource.getMessage("changedPassword.successfully.mailBody", new String[] { userName}, Locale.ENGLISH);
				mailUtility.baselineExample(email, emailSubject, emailContent);
				/*mail.setSubject(subject);
				mail.setContent(messageSource.getMessage("changedPassword.successfully.mailBody", new String[] { userName}, Locale.ENGLISH), "text/html");
				*/
			}
			//mailSender.send(mail);
			flag = true;
		/*} catch (NoSuchMessageException | MessagingException | IOException e) {
			throw new GoForWealthUMAException(GoForWealthErrorMessageEnum.COMMON_ERROR_CODE.getValue(),GoForWealthUMAConstants.MAIL_SEND_FAILURE_MESSAGE);
		}
		*/
		} catch ( NoSuchMessageException | IOException e) {
			e.printStackTrace();
		}
		if(mobibeNumber!=null)
			smsUtility.sendOtpToMobile(otp.length(), mobibeNumber, otp);
		return flag;
	}

	@Override
	public String mobileVerification(Integer profileId, String mobile) throws GoForWealthUMAException {
		String subject = GoForWealthUMAConstants.ONBOARDING;
		//String subject=GoForWealthUMAConstants.MOBILE_VERIFICATION_EMAIL_SUBJECT;
		User userObj = userRepository.findByMobileNumber(mobile);
		if(userObj != null) {
			int userId = userObj.getUserId();
			if(userId == profileId) {
				Otp otpRef = otpRepository.findByMobileNumber(mobile);
				OtpGenerator otpGenerator = new OtpGenerator(4,true);
				String otp = otpGenerator.generateOTP();
				logger.info("otp-------: " + otp + "..... Mobile No : " + mobile);
				if(otpRef != null) {
					userObj.setMobileNumber(mobile);
					userObj.setMobileVerified(MobileVerification.NOTVERIFIED.getStatus());
					userObj.setUsername(mobile);
					otpRef.setOtp(passwordEncoder.encode(otp));
					otpRef.setSentTime(new Date());
					otpRepository.save(otpRef);
					userRepository.save(userObj);
				} else {
					userObj.setMobileNumber(mobile);
					userObj.setMobileVerified(MobileVerification.NOTVERIFIED.getStatus());
					userObj.setUsername(mobile);
					Otp otpObj = new Otp();
					otpObj.setOtp(passwordEncoder.encode(otp));
					otpObj.setSentTime(new Date());
					otpObj.setMobileNumber(mobile);
					otpRepository.save(otpObj);
					userRepository.save(userObj);
				}
				// send mail.
				//String userName = userObj.getFirstName() == null ? userObj.getUsername() : userObj.getFirstName();
				String userName = userObj.getFirstName() == null ? userObj.getUsername() : userObj.getFirstName()+" "+userObj.getLastName();
				sendOtpToMail(userName,subject,userObj.getEmail(),otp,userObj.getMobileNumber());
				return GoForWealthUMAConstants.OTP_SEND_SUCCESS_MESSAGE;
			} else {
				throw new GoForWealthUMAException(GoForWealthErrorMessageEnum.COMMON_ERROR_CODE.getValue(),GoForWealthErrorMessageEnum.MOBILE_NO_ALREADY_USED.getValue());
			}
		}else {
			User user = userRepository.getOne(profileId);
			if(user == null) {
				throw new GoForWealthUMAException(GoForWealthErrorMessageEnum.DATA_NOT_FOUND_CODE.getValue(),GoForWealthErrorMessageEnum.USER_DETAIL_NOT_EXIST.getValue());
			} else {
				if(!user.getRegisterType().equals("MO")) {
					user.setMobileNumber(mobile);
					user.setMobileVerified(MobileVerification.NOTVERIFIED.getStatus());
					OtpGenerator otpGenerator = new OtpGenerator(4,true);
					String otp = otpGenerator.generateOTP();
					logger.info("otp---------------: " + otp);
					Otp otpRef = new Otp();
					otpRef.setOtp(passwordEncoder.encode(otp));
					otpRef.setSentTime(new Date());
					otpRef.setMobileNumber(mobile);
					otpRepository.save(otpRef);
					userRepository.save(user);
					// send mail.
					//String userName = user.getFirstName() == null ? user.getUsername() : user.getLastName();
					String userName = user.getFirstName() == null ? user.getUsername() : user.getLastName()+" "+user.getLastName();
					sendOtpToMail(userName,subject,user.getEmail(),otp,user.getMobileNumber());
					return GoForWealthUMAConstants.OTP_SEND_SUCCESS_MESSAGE;
				}
				throw new GoForWealthUMAException(GoForWealthErrorMessageEnum.DATA_NOT_FOUND_CODE.getValue(),GoForWealthErrorMessageEnum.USER_DETAIL_NOT_EXIST.getValue());
			}
		}
		/**
		User user = null;
		user = userRepository.getOne(profileId);
		if(user == null) {
			throw new GoForWealthUMAException(GoForWealthErrorMessageEnum.DATA_NOT_FOUND_CODE.getValue(),GoForWealthErrorMessageEnum.USER_DETAIL_NOT_EXIST.getValue());
		}
		User tempUser = userRepository.findByMobileNumber(mobile);
		if(tempUser!=null && profileId !=tempUser.getUserId()){
			throw new GoForWealthUMAException(GoForWealthErrorMessageEnum.COMMON_ERROR_CODE.getValue(),GoForWealthErrorMessageEnum.MOBILE_NO_ALREADY_REGISTERD.getValue());
		}
		if(tempUser != null && tempUser.getMobileVerified() != 1) {
			Otp otpRef = otpRepository.findByMobileNumber(mobile);
			OtpGenerator otpGenerator = new OtpGenerator(4,true);
			String otp = otpGenerator.generateOTP();
			// TODO code to send otp to mobile number,
			logger.info("otp---------------: " + otp);
			if(otpRef != null) {
				otpRef.setOtp(passwordEncoder.encode(otp));
				otpRef.setSentTime(new Date());
				otpRepository.save(otpRef);
			}
		} 
		user.setMobileNumber(mobile);
		user.setMobileVerified(MobileVerification.NOTVERIFIED.getStatus());
		OtpGenerator otpGenerator = new OtpGenerator(4,true);
		String otp = otpGenerator.generateOTP();
		// TODO code to send otp to mobile number,
		logger.info("otp---------------: " + otp);
		Otp otpRef = new Otp();
		otpRef.setOtp(passwordEncoder.encode(otp));
		otpRef.setSentTime(new Date());
		otpRef.setMobileNumber(mobile);
		otpRepository.save(otpRef);
		user = userRepository.save(user);
		return GoForWealthUMAConstants.OTP_SEND_SUCCESS_MESSAGE;
		**/
	}
	
	@Override
	public UserRole getUserRole(Integer userId) {
		UserRoleId userRoleId = new UserRoleId();
		Role role = roleRepository.findByRoleName("USER");
		userRoleId.setUserId(userId);
		userRoleId.setRoleId(role.getRoleId());
		UserRole userRole = userRoleRepository.getOne(userRoleId);
		return userRole;
	}

	@Override
	public List<BlogDTO> goForWealthUMAService() {
		List<Blog> blogList=adminRepository.findAll();
		SimpleDateFormat sdf = new SimpleDateFormat("dd MMMM yyyy");
		List<BlogDTO> blogDtoList=new ArrayList<BlogDTO>();
		for (Blog blog : blogList) {
			BlogDTO blogDTO= new BlogDTO();
			blogDTO.setBlogId(blog.getBlogId());
			blogDTO.setCategoryId(blog.getBlogCategory().getBlogCategoryId());
			blogDTO.setFinalWord(blog.getFinalWords());
			blogDTO.setKeywords(blog.getKeyWords());
			blogDTO.setLongDescription(blog.getLongDescription());
			blogDTO.setPostedBy(blog.getPostedBy());
			blogDTO.setShortDescription(blog.getShortDescription());
			blogDTO.setPostDate(sdf.format(blog.getPostDate()));
			blogDTO.setTitle(blog.getTitle());
			blogDTO.setUrl(blog.getUrl());
			blogDtoList.add(blogDTO);	
		}
		return blogDtoList;
	}

	@Override
	public List<BlogCategoryDTO> getUserBlogCategory() throws GoForWealthAdminException {
		List<BlogCategory> blogCategoriesList=blogCategoryRepository.findAll();
		if(blogCategoriesList.isEmpty()){
			throw new GoForWealthAdminException(GoForWealthAdminErrorMessageEnum.DATA_NOT_FOUND_CODE.getValue(),GoForWealthAdminErrorMessageEnum.DATA_NOT_FOUND_MESSAGE.getValue());
		}
		List<BlogCategoryDTO> blogCategoryDTOList= new ArrayList<BlogCategoryDTO>();
		for (BlogCategory blogCategory : blogCategoriesList) {
			BlogCategoryDTO blogCategoryDTO= new BlogCategoryDTO();
			blogCategoryDTO.setBlogCategoryId(blogCategory.getBlogCategoryId());
			blogCategoryDTO.setBlogCategoryName(blogCategory.getBlogCategoryName());
			blogCategoryDTOList.add(blogCategoryDTO);
		}
		return blogCategoryDTOList;
	}

	@Override
	public List<BlogDTO> getUserBlogByCategoryId(int categoryId) throws GoForWealthAdminException {
		List<Blog> blogList=adminRepository.getBlogByCategoryId(categoryId);
		if(blogList.isEmpty()){
			throw new GoForWealthAdminException(GoForWealthErrorMessageEnum.DATA_NOT_FOUND_CODE.getValue(),GoForWealthErrorMessageEnum.DATA_NOT_FOUND_MESSAGE.getValue());
		}
		List<BlogDTO> blogDTOList= new ArrayList<BlogDTO>();
		SimpleDateFormat sdf = new SimpleDateFormat("dd MMMM yyyy");
		for (Blog blog : blogList) {
			BlogDTO blogDTO= new BlogDTO();
			blogDTO.setBlogId(blog.getBlogId());
			blogDTO.setCategoryId(blog.getBlogCategory().getBlogCategoryId());
			blogDTO.setFinalWord(blog.getFinalWords());
			blogDTO.setKeywords(blog.getKeyWords());
			blogDTO.setLongDescription(blog.getLongDescription());
			blogDTO.setPostDate(sdf.format(blog.getPostDate()));
			blogDTO.setShortDescription(blog.getShortDescription());
			blogDTO.setPostedBy(blog.getPostedBy());
			blogDTO.setTitle(blog.getTitle());
			blogDTO.setUrl(blog.getUrl());
			blogDTOList.add(blogDTO);
		}
		return blogDTOList;
	}

	@Override
	public BlogDTO getUserBlogById(int blogId, HttpServletResponse response) throws IOException, GoForWealthAdminException {
		Blog blog = adminRepository.getBlogByBlogId(blogId);
		BlogDTO blogDTO= new BlogDTO();
		if(blog != null) {
			SimpleDateFormat sdf = new SimpleDateFormat("dd MMMM yyyy");	
			blogDTO.setBlogId(blog.getBlogId());
			blogDTO.setCategoryId(blog.getBlogCategory().getBlogCategoryId());
			blogDTO.setCategoryName(blog.getBlogCategory().getBlogCategoryName());
			blogDTO.setFinalWord(blog.getFinalWords());
			blogDTO.setKeywords(blog.getKeyWords());
			blogDTO.setLongDescription(blog.getLongDescription());
			blogDTO.setPostDate(sdf.format(blog.getPostDate()));
			blogDTO.setShortDescription(blog.getShortDescription());
			blogDTO.setPostedBy(blog.getPostedBy());
			blogDTO.setTitle(blog.getTitle());
			blogDTO.setUrl(blog.getUrl());
		}
		return blogDTO;
	}

	@Override
	public List<BlogDTO> getUserRelatedBlog(int id) throws GoForWealthAdminException {	
		Blog blog1=adminRepository.getBlogByBlogId(id);
		List<Blog> blogList=adminRepository.getBlogByCategoryId(blog1.getBlogCategory().getBlogCategoryId(),new PageRequest(0,4));
		List<BlogDTO> blogDTOList= new ArrayList<BlogDTO>();
		SimpleDateFormat sdf = new SimpleDateFormat("dd MMMM yyyy");
		for (Blog blog : blogList) {
			if(id!=blog.getBlogId()){
				BlogDTO blogDTO= new BlogDTO();
				blogDTO.setBlogId(blog.getBlogId());
				blogDTO.setCategoryId(blog.getBlogCategory().getBlogCategoryId());
				blogDTO.setFinalWord(blog.getFinalWords());
				blogDTO.setKeywords(blog.getKeyWords());
				blogDTO.setLongDescription(blog.getLongDescription());
				blogDTO.setPostDate(sdf.format(blog.getPostDate()));
				blogDTO.setShortDescription(blog.getShortDescription());
				blogDTO.setPostedBy(blog.getPostedBy());
				blogDTO.setTitle(blog.getTitle());
				blogDTO.setUrl(blog.getUrl());
				blogDTOList.add(blogDTO);
			}
		}
		return blogDTOList;
	}

	@Override
	public List<BlogDTO> getLatestBlog(Integer userId) throws GoForWealthAdminException {
		User user = userRepository.findUserByRole(userId, "USER");
		if(user == null) {
			throw new GoForWealthAdminException(GoForWealthErrorMessageEnum.ACCESS_DENIED_CODE.getValue(),GoForWealthErrorMessageEnum.ACCESS_DENIED_MESSAGE.getValue());
		}
		List<BlogDTO> blogDTOList= new ArrayList<BlogDTO>();
		SimpleDateFormat sdf = new SimpleDateFormat("dd MMMM yyyy");
		List<Blog> blogList=adminRepository.getLatestBlog(new PageRequest(0,4));
		if(!blogList.isEmpty()){
			for (Blog blog : blogList) {
				BlogDTO blogDTO= new BlogDTO();
				blogDTO.setBlogId(blog.getBlogId());
				blogDTO.setCategoryId(blog.getBlogCategory().getBlogCategoryId());
				blogDTO.setFinalWord(blog.getFinalWords());
				blogDTO.setKeywords(blog.getKeyWords());
				blogDTO.setLongDescription(blog.getLongDescription());
				blogDTO.setPostDate(sdf.format(blog.getPostDate()));
				blogDTO.setShortDescription(blog.getShortDescription());
				blogDTO.setPostedBy(blog.getPostedBy());
				blogDTO.setTitle(blog.getTitle());
				blogDTO.setUrl(blog.getUrl());
				blogDTOList.add(blogDTO);
			}
		}
		return blogDTOList;
	}

	@Override
	public boolean isUserExist(String userName) throws GoForWealthUMAException {
		logger.info(" userName in servece  before === "+userName);
		boolean flag = false;
		User user = null;
		try {
			user = userRepository.findByUsername(userName);
			if(user != null) {
				flag = true;
			}
		} catch(Exception ex) {
			throw new GoForWealthUMAException(GoForWealthErrorMessageEnum.COMMON_ERROR_CODE.getValue(), ex.getMessage());
		}
		return flag;
	}

	@Override
	public String addEnqury(UserEnquryDTO userEnquryDto) {
		String response = "";
		try {
			UserEnquiry userEnquryObj = new UserEnquiry();
			userEnquryObj.setName(userEnquryDto.getName());
			userEnquryObj.setEmail(userEnquryDto.getEmail());
			userEnquryObj.setMobile(userEnquryDto.getMobile());
			userEnquryObj.setQuery(userEnquryDto.getQuery());
			userEnquryObj.setUpdateDate(new Date());
			userEnquryRepository.save(userEnquryObj);
			response = "success";
		} catch (Exception ex) {
			response = "failure";
			ex.printStackTrace();
		}
		return response;
	}

	@Override
	public Integer getUserIdByMobileNumber(String mobileNumber) {
		Integer userId = 0;
		try {
			User user = userRepository.findByMobileNumber(mobileNumber);
			userId = user.getUserId();
		} catch (Exception ex) {
			ex.printStackTrace();
		}
		return userId;
	}

	@Override
	public String changeMaritalStatus(Integer userId, String maritalStatus) {
		String message = "";
		try {
			User user = userRepository.getOne(userId);
			KycDetails kycDetailsObj = user.getKycDetails();
			kycDetailsObj.setMaritalStatus(maritalStatus);
			KycDetails kycDetailsRef = kycDetailsRepository.save(kycDetailsObj);
			message = kycDetailsRef.getMaritalStatus();
		} catch (Exception ex) {
			message = "";
			ex.printStackTrace();
		}
		return message;
	}

	@Override
	public void deleteGuestUserRecords() {
		List<User> userList = userRepository.findByRegisterType("GUEST_USER");
		for (User user : userList) {
			Date userDate = user.getUpdatedDateTime();
			Date todayDate = new Date();
			int differenceInDays = (int) ((todayDate.getTime() - userDate.getTime()) / (1000 * 60 * 60 * 24));
			if (differenceInDays > 15) {
				Set<GoalsOrder> goalsOrderSet = user.getGoalsOrders();
				if (goalsOrderSet.size() > 0) {
					GoalsOrder guestUserGolasOrderObj = goalsOrderRepository.findByUserId(goalsOrderSet.iterator().next().getUser().getUserId());
					if (guestUserGolasOrderObj != null) {
						if (guestUserGolasOrderObj.getGoalOrderItemses().size() > 0) {
							Set<GoalOrderItems> goalOrderItemsSet = guestUserGolasOrderObj.getGoalOrderItemses();
							List<GoalOrderItems> goalOrderItemsList = new ArrayList<>(goalOrderItemsSet);
							for (int i = 0; i < goalOrderItemsList.size(); i++) {
								Goals goalsObj = goalsRepository.findByGoalId(goalOrderItemsList.get(i).getGoals().getGoalId());
								goalOrderItemsRepository.delete(goalOrderItemsList.get(i));
								if (goalsObj.getGoalType().equals("UD")) {
									goalsRepository.delete(goalsObj);
								}
							}
						}
						if (guestUserGolasOrderObj != null) {
							List<UserAssetItems> userAssetItems = userAssetItemsRepository.findByGoalsOrderId(goalsOrderSet.iterator().next().getGoalsOrderId());
							for (UserAssetItems userAssetItemsObj : userAssetItems) {
								userAssetItemsRepository.delete(userAssetItemsObj);
							}
							GoalsOrder guestUserGoalsOrderRef = goalsOrderRepository.findByUserId(guestUserGolasOrderObj.getUser().getUserId());
							goalsOrderRepository.delete(guestUserGoalsOrderRef);
							Optional<User> userOptionalRef = userRepository.findById(guestUserGolasOrderObj.getUser().getUserId());
							if (userOptionalRef.isPresent()) {
								userRepository.delete(userOptionalRef.get());
							}
						}
					}
				} else {
					userRepository.delete(user);
				}
			}
		}
	}



}