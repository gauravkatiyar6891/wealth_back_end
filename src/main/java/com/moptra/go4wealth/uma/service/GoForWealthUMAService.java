package com.moptra.go4wealth.uma.service;

import java.io.IOException;
import java.util.List;

import javax.servlet.http.HttpServletResponse;

import com.moptra.go4wealth.admin.common.exception.GoForWealthAdminException;
import com.moptra.go4wealth.admin.model.BlogCategoryDTO;
import com.moptra.go4wealth.admin.model.BlogDTO;
import com.moptra.go4wealth.bean.User;
import com.moptra.go4wealth.bean.UserRole;
import com.moptra.go4wealth.uma.common.exception.GoForWealthUMAException;
import com.moptra.go4wealth.uma.model.CommissionDetailDTO;
import com.moptra.go4wealth.uma.model.ForgetPasswordDTO;
import com.moptra.go4wealth.uma.model.RegisterAgentDTO;
import com.moptra.go4wealth.uma.model.UserAccountDTO;
import com.moptra.go4wealth.uma.model.UserDTO;
import com.moptra.go4wealth.uma.model.UserEnquryDTO;
import com.moptra.go4wealth.uma.model.UserLoginResponseDTO;

public interface GoForWealthUMAService {

	public String resetPassword(String emailId) throws GoForWealthUMAException;

	public UserLoginResponseDTO login(String userName, String password);

	public UserLoginResponseDTO getUser(Integer userId,String action);

	boolean resetPassword(UserDTO userDTO) throws GoForWealthUMAException;

	List<UserAccountDTO> getAllAccounts(Integer userId) throws GoForWealthUMAException;

	void deleteAccounts(String emailId);

	UserDTO getLoggedUserDetail(int userId) throws GoForWealthUMAException;

	//boolean editUserDetails(UserDTO registerUserDTO, int userId) throws GoForWealthUMAException;

	String registerByMobileNumber(UserDTO registerUserDTO) throws GoForWealthUMAException;
	
	String signupByGuestUser(UserDTO registerUserDTO) throws GoForWealthUMAException;

	String verifyMobileNumber(String otp, String mobileNumber);

	User signupByFacebook(UserDTO iotRegisterUserDTO) throws GoForWealthUMAException;

	String registerAgent(RegisterAgentDTO registerAgentDTO) throws GoForWealthUMAException;

	void editAgentDetails(RegisterAgentDTO registerAgentDTO, int userId) throws GoForWealthUMAException;

	String forgetPassword(String mobileNumber) throws GoForWealthUMAException;

	String forgetPasswordVerifyOtp(String mobileNumber, String otp);

	String changePassword(ForgetPasswordDTO forgetPasswordDTO, int userId) throws GoForWealthUMAException;

	User signupByGoogle(UserDTO iotRegisterUserDTO) throws GoForWealthUMAException;

	CommissionDetailDTO getCommissionDetails(int userId) throws GoForWealthUMAException;

	String resendOtp(String mobileNumber, String email, String type) throws GoForWealthUMAException;

	public String sendEmailVerification(Integer userId, UserDTO registerUserDTO) throws GoForWealthUMAException;

	public String emailVerification(Integer profileId) throws GoForWealthUMAException;

	public String mobileVerification(Integer profileId, String mobile) throws GoForWealthUMAException;

	public UserRole getUserRole(Integer userId);

	public List<BlogDTO> goForWealthUMAService();

	public List<BlogCategoryDTO> getUserBlogCategory() throws GoForWealthAdminException;

	public List<BlogDTO> getUserBlogByCategoryId(int categoryId) throws GoForWealthAdminException;

	public BlogDTO getUserBlogById(int blogId, HttpServletResponse response) throws GoForWealthAdminException, IOException;

	public List<BlogDTO> getUserRelatedBlog(int id) throws GoForWealthAdminException;

	public List<BlogDTO> getLatestBlog(Integer userId) throws GoForWealthAdminException;

	public boolean isUserExist(String userName) throws GoForWealthUMAException;

	//boolean sendOtpToMail(String userName, String subject, String email, String otp);
	
	boolean sendOtpToMail(String userName, String subject, String email, String otp, String mobibeNumber);
	
	String addEnqury(UserEnquryDTO userEnqury);
	
	Integer getUserIdByMobileNumber(String mobileNumber);
	
	String changeMaritalStatus(Integer userId,String maritalStatus);
	
	void deleteGuestUserRecords();
}