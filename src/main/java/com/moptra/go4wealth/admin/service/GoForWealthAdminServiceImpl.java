package com.moptra.go4wealth.admin.service;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Locale;
import java.util.Optional;
import java.util.Set;

import javax.mail.Message;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletResponse;

import org.apache.poi.ss.usermodel.Workbook;
import org.apache.tomcat.util.codec.binary.Base64;
import org.json.JSONArray;
import org.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.data.domain.PageRequest;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.moptra.go4wealth.admin.common.constant.GoForWealthAdminConstants;
import com.moptra.go4wealth.admin.common.enums.GoForWealthAdminErrorMessageEnum;
import com.moptra.go4wealth.admin.common.exception.GoForWealthAdminException;
import com.moptra.go4wealth.admin.common.util.GoForWealthAdminUtil;
import com.moptra.go4wealth.admin.model.AdminBlogRequest;
import com.moptra.go4wealth.admin.model.BlogCategoryDTO;
import com.moptra.go4wealth.admin.model.BlogDTO;
import com.moptra.go4wealth.admin.model.CategoryListResponseDTO;
import com.moptra.go4wealth.admin.model.ContactUsRequestDTO;
import com.moptra.go4wealth.admin.model.OrderExportExcelDto;
import com.moptra.go4wealth.admin.model.RedumptionResponse;
import com.moptra.go4wealth.admin.model.SchemeDTO;
import com.moptra.go4wealth.admin.model.SchemeKeywordWithSeoResponse;
import com.moptra.go4wealth.admin.model.SchemeUploadRequest;
import com.moptra.go4wealth.admin.model.TestiMonialRequestDTO;
import com.moptra.go4wealth.admin.model.TestimonialResponseDTO;
import com.moptra.go4wealth.admin.model.UserDetailedDataDTO;
import com.moptra.go4wealth.admin.model.UserEnquiryDto;
import com.moptra.go4wealth.admin.model.UserExportExcelDataDto;
import com.moptra.go4wealth.admin.model.UserInfoDto;
import com.moptra.go4wealth.admin.model.UsersListDTO;
import com.moptra.go4wealth.bean.AddressProof;
import com.moptra.go4wealth.bean.AllotmentStatusReportUserdata;
import com.moptra.go4wealth.bean.AssetClassInternal;
import com.moptra.go4wealth.bean.Blog;
import com.moptra.go4wealth.bean.BlogCategory;
import com.moptra.go4wealth.bean.ConsolidatedPortfollio;
import com.moptra.go4wealth.bean.ContactUs;
import com.moptra.go4wealth.bean.GoalBucket;
import com.moptra.go4wealth.bean.Goals;
import com.moptra.go4wealth.bean.IsipAllowedBankList;
import com.moptra.go4wealth.bean.OnboardingStatus;
import com.moptra.go4wealth.bean.OrderItem;
import com.moptra.go4wealth.bean.Orders;
import com.moptra.go4wealth.bean.PanDetails;
import com.moptra.go4wealth.bean.Portfolio;
import com.moptra.go4wealth.bean.PortfolioCategory;
import com.moptra.go4wealth.bean.Scheme;
import com.moptra.go4wealth.bean.Seo;
import com.moptra.go4wealth.bean.StoreConf;
import com.moptra.go4wealth.bean.Testimonial;
import com.moptra.go4wealth.bean.TopSchemes;
import com.moptra.go4wealth.bean.TransferIn;
import com.moptra.go4wealth.bean.User;
import com.moptra.go4wealth.bean.UserEnquiry;
import com.moptra.go4wealth.configuration.EmailVerificationConfiguration;
import com.moptra.go4wealth.prs.common.constant.GoForWealthPRSConstants;
import com.moptra.go4wealth.prs.common.util.EncryptUserDetail;
import com.moptra.go4wealth.prs.common.util.GoForWealthPRSUtil;
import com.moptra.go4wealth.prs.model.AddressProofDTO;
import com.moptra.go4wealth.prs.model.FundSchemeDTO;
import com.moptra.go4wealth.prs.model.OrdersDTO;
import com.moptra.go4wealth.prs.navapi.NavService;
import com.moptra.go4wealth.prs.orderapi.request.PortFolioDataDTO;
import com.moptra.go4wealth.prs.service.GoForWealthPRSEmandateService;
import com.moptra.go4wealth.repository.AddressProofRepository;
import com.moptra.go4wealth.repository.AdminRepository;
import com.moptra.go4wealth.repository.AllotmentStatusReportUserdataRepository;
import com.moptra.go4wealth.repository.AssetClassInternalRepository;
import com.moptra.go4wealth.repository.BlogCategoryRepository;
import com.moptra.go4wealth.repository.ConsolidatedFollioRepository;
import com.moptra.go4wealth.repository.ContactUsRepository;
import com.moptra.go4wealth.repository.GoalBucketRepository;
import com.moptra.go4wealth.repository.GoalsRepository;
import com.moptra.go4wealth.repository.OnboardingStatusRepository;
import com.moptra.go4wealth.repository.OrderRepository;
import com.moptra.go4wealth.repository.PortfolioCategoryRepository;
import com.moptra.go4wealth.repository.PortfolioRepository;
import com.moptra.go4wealth.repository.SchemeRepository;
import com.moptra.go4wealth.repository.SeoRepository;
import com.moptra.go4wealth.repository.StoreConfRepository;
import com.moptra.go4wealth.repository.TestimonialRepository;
import com.moptra.go4wealth.repository.TopSchemeRepository;
import com.moptra.go4wealth.repository.TransferInRepository;
import com.moptra.go4wealth.repository.UserEnquryRepository;
import com.moptra.go4wealth.repository.UserRepository;
import com.moptra.go4wealth.sip.common.enums.GoalBucketCodeEnum;
import com.moptra.go4wealth.uma.common.constant.GoForWealthUMAConstants;
import com.moptra.go4wealth.uma.common.enums.GoForWealthErrorMessageEnum;
import com.moptra.go4wealth.uma.model.UserLoginResponseDTO;
import com.moptra.go4wealth.uma.service.GoForWealthUMAService;
import com.moptra.go4wealth.util.MailUtility;

import sun.misc.BASE64Decoder;

/**
 * 
 * @author ranjeet
 *
 */
@Service
public class GoForWealthAdminServiceImpl implements GoForWealthAdminService {

	private static final String IMAGE_TYPE_JPG = ".jpeg";
	private static final String TYPE_IMAGE = "IMAGE";
	private static final String TYPE_VIDEO = "VIDEO";
	private static final String IMAGE_CONTENT_TYPE = "image/jpeg";
	private static final String BLOG_IMAGE_NAME = "blogImages_";
	private static final String TESTIMONIAL_IMAGE_NAME = "testimonialImages_";

	@Autowired
	AdminRepository adminRepository;

	@Autowired
	SchemeRepository schemeRepository;

	@Autowired
	TopSchemeRepository topSchemeRepository;

	@Autowired
	UserRepository userRepository;

	@Autowired
	AddressProofRepository addressProofRepository;

	/*@Autowired
	AddressRepository addressRepository;*/

	@Autowired
	OnboardingStatusRepository onboardingStatusRepository;

	@Autowired
	StoreConfRepository storeConfRepository;

	@Autowired
	GoForWealthPRSEmandateService goForWealthPRSEmandateService;

	@Autowired
	BlogCategoryRepository blogCategoryRepository;

	@Autowired
	OrderRepository orderRepository;

	@Autowired
	TestimonialRepository testimonialRepository;

	@Autowired
	ContactUsRepository contactUsRepository;

	@Autowired
	SeoRepository seoRepository;

	@Autowired
	private JavaMailSender mailSender;

	@Autowired
	private MessageSource messageSource;

	@Autowired
	private EmailVerificationConfiguration emailVerificationConfiguration;

	@Autowired
	EncryptUserDetail encryptUserDetail;

	@Autowired
	AssetClassInternalRepository assetClassInternalRepository;

	@Autowired
	MailUtility mailUtility;

	@Autowired
	NavService navService;

	@Autowired
	UserEnquryRepository userEnquryRepository;

	@Autowired
	PortfolioRepository portfolioRepository;

	@Autowired
	PortfolioCategoryRepository portfolioCategoryRepository;

	@Autowired
	private GoalsRepository goalsRepository;
	
	@Autowired
	private GoalBucketRepository goalBucketRepository;
	
	@Autowired
	UserEnquryRepository userEnquiryRepository;
	
	@Autowired
	TransferInRepository transferInRepository;
	
	@Autowired
	AllotmentStatusReportUserdataRepository allotmentStatusReportUserdataRepository;
	
	@Autowired
	ConsolidatedFollioRepository consolidatedFollioRepository;

	private static Logger logger = LoggerFactory.getLogger(GoForWealthAdminServiceImpl.class);

	@Override
	@Transactional
	public void saveBlog(AdminBlogRequest adminBlogRequest, int userId) throws GoForWealthAdminException {
		User user = userRepository.findUserByRole(userId, "ADMIN");
		if (user == null) {
			throw new GoForWealthAdminException(GoForWealthErrorMessageEnum.ACCESS_DENIED_CODE.getValue(),GoForWealthErrorMessageEnum.ACCESS_DENIED_MESSAGE.getValue());
		}
		StoreConf imageLocation = storeConfRepository.findByKeyword("IMAGE_LOCATION");
		StoreConf blogLocation = storeConfRepository.findByKeyword("BLOGIMAGE_LOCATION");
		StoreConf doubleSlash = storeConfRepository.findByKeyword(GoForWealthAdminConstants.DOUBLE_BACKWARD_SLASH);
		StoreConf forwardSlash = storeConfRepository.findByKeyword(GoForWealthAdminConstants.FORWARD_SLASH);
		Blog blog = new Blog();
		BlogCategory blogCategory = new BlogCategory();
		blog.setBlogCategory(blogCategory);
		blog.getBlogCategory().setBlogCategoryId(adminBlogRequest.getCategoryId());
		blog.setFinalWords(adminBlogRequest.getFinalWord());
		blog.setKeyWords(adminBlogRequest.getKeywords());
		blog.setLongDescription(adminBlogRequest.getLongDescription());
		blog.setPostedBy(adminBlogRequest.getPostedBy());
		blog.setShortDescription(adminBlogRequest.getShortDescription());
		blog.setMediaType(TYPE_IMAGE);
		blog.setPostDate(new Date());
		blog.setTitle(adminBlogRequest.getTitle());
		blog = adminRepository.save(blog);
		String path = imageLocation.getKeywordValue() + doubleSlash.getKeywordValue() + blogLocation.getKeywordValue();
		String base64Data = adminBlogRequest.getBase64String();
		byte[] fileByteArry = Base64.decodeBase64(base64Data);
		String destPath = path + doubleSlash.getKeywordValue() + BLOG_IMAGE_NAME + blog.getBlogId() + IMAGE_TYPE_JPG;
		String destPath4db = blogLocation.getKeywordValue() + forwardSlash.getKeywordValue() + BLOG_IMAGE_NAME+ blog.getBlogId() + IMAGE_TYPE_JPG;
		blog.setUrl(destPath4db);
		File file = new File(destPath);
		try {
			OutputStream os = new FileOutputStream(file);
			os.write(fileByteArry);
			os.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
		adminRepository.save(blog);
	}

	@Override
	@Transactional
	public void deleteBlogById(Integer blogId, int userId) throws GoForWealthAdminException {
		User user = userRepository.findUserByRole(userId, "ADMIN");
		if (user == null) {
			throw new GoForWealthAdminException(GoForWealthErrorMessageEnum.ACCESS_DENIED_CODE.getValue(),GoForWealthErrorMessageEnum.ACCESS_DENIED_MESSAGE.getValue());
		}
		adminRepository.deleteById(blogId);
	}

	@Override
	public Blog getImageById(Integer blogId, HttpServletResponse response) throws IOException {
		StoreConf imageLocation = storeConfRepository.findByKeyword("IMAGE_LOCATION");
		Blog blog = adminRepository.getOne(blogId);
		ServletOutputStream out;
		FileInputStream fin;
		BufferedInputStream bin;
		out = response.getOutputStream();
		BufferedOutputStream bout = new BufferedOutputStream(out);
		try {
			fin = new FileInputStream(imageLocation.getKeywordValue() + blog.getUrl());
			bin = new BufferedInputStream(fin);
			int ch = 0;
			response.setContentType(IMAGE_CONTENT_TYPE);
			while ((ch = bin.read()) != -1) {
				bout.write(ch);
			}
			bin.close();
			fin.close();
			bout.close();
			out.close();
		} catch (FileNotFoundException ex) {
			ex.printStackTrace();
		} catch (Exception ex) {
			ex.printStackTrace();
		} finally {
			bout.close();
			out.close();
		}
		return blog;
	}

	@Override
	public List<BlogDTO> getAllBlogs(int userId) throws GoForWealthAdminException {
		User user = userRepository.findUserByRole(userId, "ADMIN");
		if (user == null) {
			throw new GoForWealthAdminException(GoForWealthErrorMessageEnum.ACCESS_DENIED_CODE.getValue(),GoForWealthErrorMessageEnum.ACCESS_DENIED_MESSAGE.getValue());
		}
		List<Blog> blogList = adminRepository.findAll();
		SimpleDateFormat sdf = new SimpleDateFormat("dd MMMM yyyy");
		List<BlogDTO> blogDtoList = new ArrayList<BlogDTO>();
		for (Blog blog : blogList) {
			BlogDTO blogDTO = new BlogDTO();
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
	public BlogDTO getBlogById(int blogId, HttpServletResponse response, int userId)throws IOException, GoForWealthAdminException {
		User user = userRepository.findUserByRole(userId, "ADMIN");
		if (user == null) {
			throw new GoForWealthAdminException(GoForWealthErrorMessageEnum.ACCESS_DENIED_CODE.getValue(),GoForWealthErrorMessageEnum.ACCESS_DENIED_MESSAGE.getValue());
		}
		Blog blog = adminRepository.getBlogByBlogId(blogId);
		BlogDTO blogDTO = new BlogDTO();
		if (blog != null) {
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
	@Transactional
	public BlogDTO updateAdminBlog(AdminBlogRequest adminBlogRequest, int blogId, int userId)throws GoForWealthAdminException {
		User user = userRepository.findUserByRole(userId, "ADMIN");
		if (user == null) {
			throw new GoForWealthAdminException(GoForWealthErrorMessageEnum.ACCESS_DENIED_CODE.getValue(),GoForWealthErrorMessageEnum.ACCESS_DENIED_MESSAGE.getValue());
		}
		StoreConf imageLocation = storeConfRepository.findByKeyword("IMAGE_LOCATION");
		StoreConf blogLocation = storeConfRepository.findByKeyword("BLOGIMAGE_LOCATION");
		StoreConf doubleSlash = storeConfRepository.findByKeyword(GoForWealthAdminConstants.DOUBLE_BACKWARD_SLASH);
		StoreConf forwardSlash = storeConfRepository.findByKeyword(GoForWealthAdminConstants.FORWARD_SLASH);
		Blog blog = adminRepository.getBlogByBlogId(blogId);
		BlogCategory blogCategory = new BlogCategory();
		blog.setBlogCategory(blogCategory);
		blog.getBlogCategory().setBlogCategoryId(adminBlogRequest.getCategoryId());
		blog.setFinalWords(adminBlogRequest.getFinalWord());
		blog.setKeyWords(adminBlogRequest.getKeywords());
		blog.setLongDescription(adminBlogRequest.getLongDescription());
		blog.setPostedBy(adminBlogRequest.getPostedBy());
		blog.setShortDescription(adminBlogRequest.getShortDescription());
		blog.setMediaType(TYPE_IMAGE);
		blog.setPostDate(new Date());
		blog.setTitle(adminBlogRequest.getTitle());
		String path = imageLocation.getKeywordValue() + doubleSlash.getKeywordValue() + blogLocation.getKeywordValue();
		if (adminBlogRequest.getBase64String() != null) {
			String base64Data = adminBlogRequest.getBase64String();
			byte[] fileByteArry = Base64.decodeBase64(base64Data);
			String destPath = path + doubleSlash.getKeywordValue() + BLOG_IMAGE_NAME + blog.getBlogId()+ IMAGE_TYPE_JPG;
			String destPath4db = blogLocation.getKeywordValue() + forwardSlash.getKeywordValue() + BLOG_IMAGE_NAME+ blog.getBlogId() + IMAGE_TYPE_JPG;
			blog.setUrl(destPath4db);
			File file = new File(destPath);
			try {
				OutputStream os = new FileOutputStream(file);
				os.write(fileByteArry);
				os.close();
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		blog = adminRepository.save(blog);
		BlogDTO blogDto = new BlogDTO();
		blogDto.setBlogId(blog.getBlogId());
		return blogDto;
	}

	@Override
	public Seo getSeoInfo(String pageName) {
		Seo seoInfo = adminRepository.fetchScoInfo(pageName);
		return seoInfo;
	}

	@Override
	public List<SchemeDTO> getAllSchemes(int count, int userId) throws GoForWealthAdminException {
		User user = userRepository.findUserByRole(userId, "ADMIN");
		if (user == null) {
			throw new GoForWealthAdminException(GoForWealthErrorMessageEnum.ACCESS_DENIED_CODE.getValue(),GoForWealthErrorMessageEnum.ACCESS_DENIED_MESSAGE.getValue());
		}
		List<Scheme> schemeList = new ArrayList<Scheme>();
		schemeList = schemeRepository.findAllSchemes(new PageRequest(count, 10));
		return setValuesInSchemeDTO(schemeList);
	}

	@Override
	public void saveTopSchemes(List<String> schemeCodes, int userId) throws GoForWealthAdminException {
		logger.info("In saveTopSchemes()");
		User user = userRepository.findUserByRole(userId, "ADMIN");
		if (user == null) {
			throw new GoForWealthAdminException(GoForWealthErrorMessageEnum.ACCESS_DENIED_CODE.getValue(),GoForWealthErrorMessageEnum.ACCESS_DENIED_MESSAGE.getValue());
		}
		List<TopSchemes> topSchemesList = topSchemeRepository.findAll();
		if(topSchemesList.size()>0){
			for (TopSchemes topSchemesObj : topSchemesList) {
				topSchemesObj.setDeactivatedDate(new Date());
				topSchemesObj.setStatus("Deactive");
				topSchemeRepository.save(topSchemesObj);
				Scheme scheme = schemeRepository.findBySchemeCode(topSchemesObj.getSchemeCode());
				if (scheme != null) {
					scheme.setPriority("3");
					schemeRepository.save(scheme);
				}
			}
		}
		schemeCodes.forEach(schemeCode -> {
			try {
				TopSchemes topSchemes = topSchemeRepository.getSchemeBySchemeCode(schemeCode);
				topSchemes.setCreatedDate(new Date());
				topSchemes.setStatus("Active");
				Scheme scheme = schemeRepository.findBySchemeCode(schemeCode);
				if (scheme != null) {
					scheme.setPriority("1");
					schemeRepository.save(scheme);
				}
				topSchemeRepository.save(topSchemes);
			} catch (Exception npe) {
				TopSchemes topSchemes = new TopSchemes();
				topSchemes.setSchemeCode(schemeCode);
				topSchemes.setCreatedDate(new Date());
				topSchemes.setDeactivatedDate(new Date());
				topSchemes.setStatus("Active");
				topSchemeRepository.saveAndFlush(topSchemes);
				Scheme scheme = schemeRepository.findBySchemeCode(schemeCode);
				if (scheme != null) {
					scheme.setPriority("1");
					schemeRepository.save(scheme);
				}
			}
		});
		logger.info("Out saveTopSchemes()");
	}

	@Override
	public List<SchemeDTO> getAllTopSchemes(int userId) throws GoForWealthAdminException {
		User user = userRepository.findUserByRole(userId, "ADMIN");
		if (user == null) {
			throw new GoForWealthAdminException(GoForWealthErrorMessageEnum.ACCESS_DENIED_CODE.getValue(),GoForWealthErrorMessageEnum.ACCESS_DENIED_MESSAGE.getValue());
		}
		List<Scheme> schemeList = schemeRepository.findAllTopSchemes();
		return setValuesInSchemeDTO(schemeList);
	}

	private List<SchemeDTO> setValuesInSchemeDTO(List<Scheme> schemeList) {
		List<SchemeDTO> topSchemeList = new ArrayList<SchemeDTO>();
		for (Scheme scheme : schemeList) {
			SchemeDTO topSchemes = new SchemeDTO();
			topSchemes.setMinSipAmount(scheme.getMinSipAmount());
			topSchemes.setOption("");
			topSchemes.setSchemeCode(scheme.getSchemeCode());
			topSchemes.setRisk(scheme.getRisk());
			topSchemes.setStatus(scheme.getStatus());
			topSchemes.setSchemeId(scheme.getSchemeId());
			topSchemes.setSchemeName(scheme.getSchemeName());
			topSchemes.setSchemeType(scheme.getSchemeType());
			topSchemeList.add(topSchemes);
		}
		return topSchemeList;
	}

	@Override
	public void deactivateTopSchemes(List<String> schemeCodes, int userId) throws GoForWealthAdminException {
		User user = userRepository.findUserByRole(userId, "ADMIN");
		if (user == null) {
			throw new GoForWealthAdminException(GoForWealthErrorMessageEnum.ACCESS_DENIED_CODE.getValue(),GoForWealthErrorMessageEnum.ACCESS_DENIED_MESSAGE.getValue());
		}
		schemeCodes.forEach(schemeCode -> {
			try {
				TopSchemes topSchemes = new TopSchemes();
				topSchemes = topSchemeRepository.getSchemeBySchemeCode(schemeCode);
				topSchemes.setDeactivatedDate(new Date());
				topSchemes.setStatus("Deactive");
				topSchemes.setSequence(0);
				topSchemeRepository.saveAndFlush(topSchemes);
				Scheme scheme = schemeRepository.findBySchemeCode(schemeCode);
				if (scheme != null) {
					scheme.setPriority("3");
					schemeRepository.save(scheme);
				}
				topSchemeRepository.save(topSchemes);
			} catch (NullPointerException npe) {
			}
		});
	}

	@Override
	public List<UsersListDTO> getAllUsers(int count, int userId) throws GoForWealthAdminException {
		User user = userRepository.findUserByRole(userId, "ADMIN");
		if (user == null) {
			throw new GoForWealthAdminException(GoForWealthErrorMessageEnum.ACCESS_DENIED_CODE.getValue(),GoForWealthErrorMessageEnum.ACCESS_DENIED_MESSAGE.getValue());
		}
		List<User> userList = userRepository.findUserExcludeAdmin(new PageRequest(count, 15));
		if (userList.isEmpty()) {
			throw new GoForWealthAdminException(GoForWealthAdminErrorMessageEnum.DATA_NOT_FOUND_CODE.getValue(),GoForWealthAdminErrorMessageEnum.DATA_NOT_FOUND_MESSAGE.getValue());
		}
		List<UsersListDTO> usersListDTO = new ArrayList<UsersListDTO>();
		for (User usersDTO : userList) {
			UsersListDTO userDTO = new UsersListDTO();
			userDTO.setEmail(usersDTO.getEmail());
			userDTO.setMobileNumber(usersDTO.getMobileNumber());
			userDTO.setUserId(usersDTO.getUserId());
			userDTO.setUserName(usersDTO.getFirstName() + " " + usersDTO.getLastName());
			if (usersDTO.getOnboardingStatus() == null) {
				throw new GoForWealthAdminException(GoForWealthAdminErrorMessageEnum.ONBOARDING_DATA_NOT_FOUND_CODE.getValue(),GoForWealthAdminErrorMessageEnum.ONBOARDING_DATA_NOT_FOUND_MESSAGE.getValue());
			}
			userDTO.setClientCode(usersDTO.getOnboardingStatus().getClientCode());
			if (usersDTO.getOnboardingStatus().getOverallStatus() == 1) {
				userDTO.setOverAllStatus(GoForWealthAdminConstants.OVER_ALL_ONBORADING_STATUS_DONE);
			} else {
				userDTO.setOverAllStatus(GoForWealthAdminConstants.OVER_ALL_ONBORADING_STATUS_NOTDONE);
			}
			usersListDTO.add(userDTO);
		}
		return usersListDTO;
	}
	
	@Override
	public List<UserExportExcelDataDto> getAllUsers(String type) {
		List<User> userList = userRepository.findUserExceptAdmin();
		List<UserExportExcelDataDto> usersList = new ArrayList<>();
		List<UserExportExcelDataDto> onboardingCompletedUserList = new ArrayList<>();
		List<UserExportExcelDataDto> onboardingNotCompletedUserList = new ArrayList<>();
		List<UserExportExcelDataDto> kycCompletedUserList = new ArrayList<>();
		List<UserExportExcelDataDto> kycNotCompletedUserList = new ArrayList<>();
		List<UserExportExcelDataDto> todayRegisteredUserList = new ArrayList<>();
		List<UserExportExcelDataDto> todayOnboardingCompletedUserList = new ArrayList<>();
		List<UserExportExcelDataDto> todayKycCompletedUserList = new ArrayList<>();
		boolean onboardingFlag = false;
		boolean kycFlag = false;
		boolean todayRegisteredUserFlag = false;
		boolean todayOnboardingCompletedUserFlag = false;
		boolean todayKycCompletedUserFlag = false;
		SimpleDateFormat sdf = new SimpleDateFormat("dd MMMM yyyy");
		for (User user: userList) {
			UserExportExcelDataDto userDto = new UserExportExcelDataDto();
			kycFlag = false;
			todayKycCompletedUserFlag = false;
			todayOnboardingCompletedUserFlag = false;
			if(sdf.format(user.getCreatedTimestamp()).equals(sdf.format(new Date()))){
				todayRegisteredUserFlag = true;
			}else{
				todayRegisteredUserFlag = false;
			}
			if(sdf.format(user.getUpdatedDateTime()).equals(sdf.format(new Date()))){
				if(user.getPanDetails()!=null){
					if(user.getPanDetails().getVerified()!=null){
						if(user.getPanDetails().getVerified().equals("verified")){
							todayKycCompletedUserFlag = true;
						}
					}
				}
			}
			userDto.setFirstName(user.getFirstName());
			userDto.setLastName(user.getLastName());
			userDto.setMobileNumber(user.getMobileNumber());
			userDto.setEmail(user.getEmail());
			if(user.getEmailVerified() == 1)
				userDto.setEmailVerified("Verified");
			else
				userDto.setEmailVerified("Not Verified");
			if(user.getMobileVerified() == 1)
				userDto.setMobileVerified("Verified");
			else
				userDto.setMobileVerified("Not Verified");
			userDto.setCreatedTimestamp(sdf.format(user.getCreatedTimestamp()));
			if(user.getOnboardingStatus() != null) {
				userDto.setClientCode(user.getOnboardingStatus().getClientCode());
				userDto.setIsipId(user.getOnboardingStatus().getIsipMandateNumber());
				userDto.setIsipStatus(user.getOnboardingStatus().getBillerStatus());
				userDto.setXsipId(user.getOnboardingStatus().getMandateNumber());
				userDto.setXsipStatus(user.getOnboardingStatus().getEnachStatus());
				if(user.getOnboardingStatus().getOverallStatus() == 1){
					userDto.setOverallStatus(GoForWealthAdminConstants.OVER_ALL_ONBORADING_STATUS_DONE);
					onboardingFlag = true;
					if(sdf.format(user.getUpdatedDateTime()).equals(sdf.format(new Date()))){
						todayOnboardingCompletedUserFlag = true;
					}
				}else{
					userDto.setOverallStatus(GoForWealthAdminConstants.OVER_ALL_ONBORADING_STATUS_NOTDONE);
					onboardingFlag = false;
				}
			}
			if(user.getPanDetails() != null){
				String decreptedPanNoRef = "";
				if (!user.getPanDetails().getPanNo().equals("")) {
					try {
						decreptedPanNoRef = encryptUserDetail.decrypt(user.getPanDetails().getPanNo());
					} catch (Exception ex) {
						ex.printStackTrace();
					}
				}
				userDto.setPanNumber(decreptedPanNoRef.toUpperCase());
				userDto.setPanVerified(user.getPanDetails().getVerified());
				if(user.getPanDetails().getVerified() != null){
					if(user.getPanDetails().getVerified().equals("verified")){
						kycFlag = true;
					}else{
						kycFlag = false;
					}
				}
				userDto.setGender(user.getPanDetails().getGender());
				if(user.getPanDetails().getDateOfBirth() != null) {
					userDto.setDateOfBirth(sdf.format(user.getPanDetails().getDateOfBirth()));
				}
				String occupation = user.getPanDetails().getOccupation();
				if(occupation != null){
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
				}
				userDto.setOccupation(occupation);			
			}
			// User Kyc Details
			if(user.getKycDetails() != null) {
				userDto.setMotherName(user.getKycDetails().getMotherName());
				userDto.setMaritalStatus(user.getKycDetails().getMaritalStatus());
				userDto.setFatherName(user.getKycDetails().getFatherName());
			}
			// User Bank Details
			if(user.getBankDetails() != null) {
				String decryptBankAccountNo = "";
				try {
					decryptBankAccountNo = encryptUserDetail.decrypt(user.getBankDetails().getAccountNo());
				} catch (Exception ex) {
					ex.printStackTrace();
				}
				userDto.setAccountNo(decryptBankAccountNo);
				userDto.setIfsc(user.getBankDetails().getIfsc());
				userDto.setBankName(user.getBankDetails().getBankName());
				userDto.setBankBranch(user.getBankDetails().getBankBranch());
				userDto.setNomineeName(user.getBankDetails().getNomineeName());
				userDto.setNomineeRelation(user.getBankDetails().getNomineeRelation());
				userDto.setAccountType(user.getBankDetails().getAccountType());
			}
			// User Address Details
			Set<AddressProof> addressProof = user.getAddressProofs();
			if (!addressProof.isEmpty()) {
				for(AddressProof addressProof2 : addressProof) {
					userDto.setAddressLine1(addressProof2.getField2());
					userDto.setAddressLine2(addressProof2.getField3());
					userDto.setPincode(addressProof2.getPincode());
					userDto.setCity(addressProof2.getCity());
					userDto.setState(addressProof2.getState());
					String addressType = addressProof2.getAddressType();
					if(addressType != null){
						if(addressType.equals("2")){
							userDto.setAddressType("Residential");
						}else if(addressType.equals("3")){
							userDto.setAddressType("Business");
						}else{
							userDto.setAddressType("Others");
						}
					}
					String incomeSlab = addressProof2.getIncomeSlab();
					if(incomeSlab != null){
						if(incomeSlab.equals("31")){
							userDto.setIncomeSlab("Below 1 Lakh");
						}else if(incomeSlab.equals("32")){
							userDto.setIncomeSlab("1-5 Lacs");
						}else if(incomeSlab.equals("33")){
							userDto.setIncomeSlab("5-10 Lacs");
						}else if(incomeSlab.equals("34")){
							userDto.setIncomeSlab("10-25 Lacs");
						}else if(incomeSlab.equals("35")){
							userDto.setIncomeSlab("25 Lacs-1 Crore");
						}else{
							userDto.setIncomeSlab("Above 1 Crore");
						}
					}
					String pep = addressProof2.getPep();
					if(pep != null){
						if(pep.equals("Y")){
							userDto.setPep("Yes");
						}else if(pep.equals("N")){
							userDto.setPep("No");
						}else{
							userDto.setPep("Related to PEP");
						}
					}
				}
			}
			usersList.add(userDto);
			if(onboardingFlag){
				onboardingCompletedUserList.add(userDto);
			}else{
				onboardingNotCompletedUserList.add(userDto);
			}
			if(kycFlag){
				kycCompletedUserList.add(userDto);
			}else{
				if(user.getPanDetails() != null && user.getPanDetails().getVerified()!=null)
					kycNotCompletedUserList.add(userDto);
			}
			if(todayRegisteredUserFlag){
				todayRegisteredUserList.add(userDto);
			}
			if(todayOnboardingCompletedUserFlag){
				todayOnboardingCompletedUserList.add(userDto);
			}
			if(todayKycCompletedUserFlag){
				todayKycCompletedUserList.add(userDto);
			}
		}
		if(type.equals("Registered User")){
			return usersList;
		}else if(type.equals("Today Registered User")){
			return todayRegisteredUserList;
		}else if(type.equals("Onboarding Completed User")){
			return onboardingCompletedUserList;
		}else if(type.equals("Onboarding Not Completed User")){
			return onboardingNotCompletedUserList;
		}else if(type.equals("Kyc Completed User")){
			return kycCompletedUserList;
		}else if(type.equals("Kyc Not Completed User")){
			return kycNotCompletedUserList;
		}else if(type.equals("Today Onboarding Completed User")){
			return todayOnboardingCompletedUserList;
		}else if(type.equals("Today Kyc Completed User")){
			return todayKycCompletedUserList;
		}else{
			return null;
		}
	}
	
	@Override
	public UserInfoDto getUserInfoById(int id, int userId) throws GoForWealthAdminException {
		User user1 = userRepository.findUserByRole(userId, "ADMIN");
		if (user1 == null) {
			throw new GoForWealthAdminException(GoForWealthErrorMessageEnum.ACCESS_DENIED_CODE.getValue(),GoForWealthErrorMessageEnum.ACCESS_DENIED_MESSAGE.getValue());
		}
		User user = userRepository.getOne(id);
		if (user == null) {
			throw new GoForWealthAdminException(GoForWealthAdminErrorMessageEnum.DATA_NOT_FOUND_CODE.getValue(),GoForWealthAdminErrorMessageEnum.DATA_NOT_FOUND_MESSAGE.getValue());
		}
		user.getOnboardingStatus().getKycStatus();
		SimpleDateFormat sdf = new SimpleDateFormat("dd MMMM yyyy");
		UserInfoDto userInfoDto = new UserInfoDto();
		userInfoDto.setUserId(user.getUserId());
		userInfoDto.setUsername(user.getFirstName() + " " + user.getLastName());
		userInfoDto.setMobileNumber(user.getMobileNumber());
		userInfoDto.setRegisterType(user.getRegisterType());
		userInfoDto.setEmail(user.getEmail());
		userInfoDto.setFirstName(user.getFirstName());
		userInfoDto.setLastName(user.getLastName());
		userInfoDto.setGender(user.getGender());
		if (user.getEmailVerified() == 1) {
			userInfoDto.setEmailVerified(GoForWealthAdminConstants.STATUS_DONE);
		} else {
			userInfoDto.setEmailVerified(GoForWealthAdminConstants.STATUS_NOT_DONE);
		}
		if (user.getMobileVerified() == 1) {
			userInfoDto.setMobileVerified(GoForWealthAdminConstants.STATUS_DONE);
		} else {
			userInfoDto.setMobileVerified(GoForWealthAdminConstants.STATUS_NOT_DONE);
		}
		if (user.getDateOfBirth() != null) {
			userInfoDto.setDateOfBirth(sdf.format(user.getDateOfBirth()));
		}
		if (user.getCreatedTimestamp() != null) {
			userInfoDto.setCreatedTimestamp(sdf.format(user.getCreatedTimestamp()));
		}
		if (user.getUpdatedDateTime() != null) {
			userInfoDto.setUpdatedDateTime(sdf.format(user.getUpdatedDateTime()));
		}
		// User Profile Info
		/*if (user.getUserProfile() != null) {
			userInfoDto.setUserProfileId(user.getUserProfile().getUserProfileId());
			userInfoDto.setAadhaarNumber(user.getUserProfile().getAadhaarNumber());
			userInfoDto.setPanNumber(user.getUserProfile().getPanNumber());
		}*/
		// User Bank Details
		if (user.getBankDetails() != null) {
			/*** ***/
			String decryptBankAccountNo = "";
			try {
				decryptBankAccountNo = encryptUserDetail.decrypt(user.getBankDetails().getAccountNo());
				String dummyChars = "";
				for (int i = 0; i < decryptBankAccountNo.length() - 4; i++)
					dummyChars += "X";
				decryptBankAccountNo = decryptBankAccountNo.substring(0, 2) + dummyChars+ decryptBankAccountNo.substring(decryptBankAccountNo.length()-2);
			} catch (Exception ex) {
				ex.printStackTrace();
			}
			userInfoDto.setAccountNo(decryptBankAccountNo);
			//userInfoDto.setAccountNo(user.getBankDetails().getAccountNo());
			userInfoDto.setIfsc(user.getBankDetails().getIfsc().toUpperCase());
			userInfoDto.setConfirm(user.getBankDetails().getConfirm());
			userInfoDto.setBankName(user.getBankDetails().getBankName());
			userInfoDto.setBankAddress(user.getBankDetails().getBankAddress());
			userInfoDto.setBankBranch(user.getBankDetails().getBankBranch());
			userInfoDto.setNomineeName(user.getBankDetails().getNomineeName());
			userInfoDto.setNomineeRelation(user.getBankDetails().getNomineeRelation());
			if (user.getBankDetails().getSignatureImage() == null) {
				userInfoDto.setSignatureImage(false);
			} else {
				userInfoDto.setSignatureImage(true);
			}
			userInfoDto.setAccountType(user.getBankDetails().getAccountType());
			userInfoDto.setMicrCode(user.getBankDetails().getMicrCode());
			if (user.getBankDetails().getStartDate() != null) {
				userInfoDto.setStartDate(sdf.format(user.getBankDetails().getStartDate()));
			}
			if (user.getBankDetails().getEndDate() != null) {
				userInfoDto.setEndDate(sdf.format(user.getBankDetails().getEndDate()));
			}
		}
		// User Pan Details
		if (user.getPanDetails() != null) {
			if (user.getPanDetails().getDateOfBirth() != null) {
				userInfoDto.setPanDateOfBirth(sdf.format(user.getPanDetails().getDateOfBirth()));
			}
			userInfoDto.setPanGender(user.getPanDetails().getGender());
			userInfoDto.setPanVerified(user.getPanDetails().getVerified());
			userInfoDto.setPanCardSelfieVideo(user.getPanDetails().getPanCardSelfieVideo());
			userInfoDto.setPanFullName(user.getPanDetails().getFullName());
			userInfoDto.setpOccupation(user.getPanDetails().getOccupation());

			/*** ***/
			String decryptPanNo = "";
			try {
				decryptPanNo = encryptUserDetail.decrypt(user.getPanDetails().getPanNo());
				decryptPanNo = decryptPanNo.substring(0, 2) + "XX" + decryptPanNo.charAt(5) + "X"+ decryptPanNo.substring(7);
			} catch (Exception ex) {
				ex.printStackTrace();
			}
			userInfoDto.setPanNumber(decryptPanNo);
		}
		// User Address Details
		Set<AddressProof> addressProof = user.getAddressProofs();
		if (!addressProof.isEmpty()) {
			for (AddressProof addressProof2 : addressProof) {
				userInfoDto.setAddressProofId(addressProof2.getAddressProofId());
				userInfoDto.setAddressProofName(addressProof2.getAddressProofName());
				/*** ***/
				String decryptAadharNo = "";
				try {
					if (addressProof2.getAddressProofNo() != null) {
						decryptAadharNo = encryptUserDetail.decrypt(addressProof2.getAddressProofNo());
					}
				} catch (Exception ex) {
					ex.printStackTrace();
				}
				userInfoDto.setAadharStatus(addressProof2.getVerified());
				userInfoDto.setAddressProofNo(decryptAadharNo);
				if (addressProof2.getFrontImage() != null) {
					userInfoDto.setFrontImage(true);
				} else {
					userInfoDto.setFrontImage(false);
				}
				if (addressProof2.getBackImage() != null) {
					userInfoDto.setBackImage(true);
				} else {
					userInfoDto.setBackImage(false);
				}
				userInfoDto.setAddress(addressProof2.getAddress());
				userInfoDto.setAddressLine1(addressProof2.getField2());
				userInfoDto.setAddressLine2(addressProof2.getField3());
				userInfoDto.setPincode(addressProof2.getPincode());
				userInfoDto.setCity(addressProof2.getCity());
				userInfoDto.setState(addressProof2.getState());
			}
		}
		/*if (user.getAddresses().size() > 0) {
			Set<Address> address = user.getAddresses();
			for (Address address2 : address) {
				userInfoDto.setCountry(address2.getCountry());
			}
		}*/
		// User Kyc Details
		if (user.getKycDetails() != null) {
			userInfoDto.setMotherName(user.getKycDetails().getMotherName());
			userInfoDto.setMaritalStatus(user.getKycDetails().getMaritalStatus());
			if (user.getKycDetails().getPhotograph() != null) {
				userInfoDto.setUserImage(true);
			} else {
				userInfoDto.setUserImage(false);
			}
			if (user.getKycDetails().getPanCardImage() != null) {
				userInfoDto.setPanCardImage(true);
			} else {
				userInfoDto.setPanCardImage(false);
			}
			userInfoDto.setFatherName(user.getKycDetails().getFatherName());
		}
		// User Onboarding status Details
		
		if(user.getPanDetails() != null){
			String panVerified = user.getPanDetails().getVerified();
			if(panVerified.equals("verified")){
				OnboardingStatus onboardingStatus =  user.getOnboardingStatus();
				onboardingStatus.setKycStatus(1);
				onboardingStatusRepository.save(onboardingStatus);		
			}
		}
		if (user.getOnboardingStatus() != null) {
			if (user.getOnboardingStatus().getKycStatus() == 1) {
				userInfoDto.setKycStatus(GoForWealthAdminConstants.STATUS_DONE);
			} else {
				userInfoDto.setKycStatus(GoForWealthAdminConstants.STATUS_NOT_DONE);
			}
			if (user.getOnboardingStatus().getMandateStatus() == 100) {
				userInfoDto.setMandateStatus(GoForWealthAdminConstants.STATUS_DONE);
			} else {
				userInfoDto.setMandateStatus(GoForWealthAdminConstants.STATUS_NOT_DONE);
			}
			if (user.getOnboardingStatus().getUccStatus() == 100) {
				userInfoDto.setUccStatus(GoForWealthAdminConstants.STATUS_DONE);
			} else {
				userInfoDto.setUccStatus(GoForWealthAdminConstants.STATUS_NOT_DONE);
			}
			if (user.getOnboardingStatus().getFatcaStatus() == 100) {
				userInfoDto.setFatcaStatus(GoForWealthAdminConstants.STATUS_DONE);
			} else {
				userInfoDto.setFatcaStatus(GoForWealthAdminConstants.STATUS_NOT_DONE);
			}
			if (user.getOnboardingStatus().getAofStatus() == 100) {
				userInfoDto.setAofStatus(GoForWealthAdminConstants.STATUS_DONE);
			} else {
				userInfoDto.setAofStatus(GoForWealthAdminConstants.STATUS_NOT_DONE);
			}
			userInfoDto.setKycResponse(user.getOnboardingStatus().getKycResponse());
			userInfoDto.setMandateResponse(user.getOnboardingStatus().getMandateResponse());
			userInfoDto.setUccResponse(user.getOnboardingStatus().getUccResponse());
			userInfoDto.setFatcaResponse(user.getOnboardingStatus().getFatcaResponse());
			userInfoDto.setAofResponse(user.getOnboardingStatus().getAofResponse());
			if (user.getOnboardingStatus().getOverallStatus() == 1) {
				userInfoDto.setOverallStatus(GoForWealthAdminConstants.STATUS_DONE);
			} else {
				userInfoDto.setOverallStatus(GoForWealthAdminConstants.STATUS_NOT_DONE);
			}
			userInfoDto.setMandateNumber(user.getOnboardingStatus().getMandateNumber());
			userInfoDto.setIsipId(user.getOnboardingStatus().getIsipMandateNumber());
			userInfoDto.setIsipStatus(user.getOnboardingStatus().getBillerStatus());
			userInfoDto.setXsipId(user.getOnboardingStatus().getMandateNumber());
			userInfoDto.setXsipStatus(user.getOnboardingStatus().getEnachStatus());
		}
		return userInfoDto;
	}

	@Override
	public int[] getTotalPagesForPagination(int userId) throws GoForWealthAdminException {
		User user = userRepository.findUserByRole(userId, "ADMIN");
		if (user == null) {
			throw new GoForWealthAdminException(GoForWealthErrorMessageEnum.ACCESS_DENIED_CODE.getValue(),GoForWealthErrorMessageEnum.ACCESS_DENIED_MESSAGE.getValue());
		}
		List<User> allUserList = userRepository.findUserExceptAdmin();
		if (allUserList == null) {
			throw new GoForWealthAdminException(GoForWealthAdminErrorMessageEnum.DATA_NOT_FOUND_CODE.getValue(),GoForWealthAdminErrorMessageEnum.DATA_NOT_FOUND_MESSAGE.getValue());
		}
		int totalNoOfPages = 0;
		Double totalPages = (Double) (allUserList.size() / 15.0);
		BigDecimal bd = new BigDecimal((totalPages - Math.floor(totalPages)) * 100);
		// bd = bd.setScale(4,RoundingMode.HALF_DOWN);
		if (bd.intValue() > 0) {
			totalNoOfPages = totalPages.intValue() + 1;
		} else {
			totalNoOfPages = totalPages.intValue();
			int[] arrayOfPages = new int[totalNoOfPages];
			for (int i = totalPages.intValue(); i > 0; i--) {
				arrayOfPages[i - 1] = i;
			}
			return arrayOfPages;
		}
		int[] arrayOfPages1 = new int[totalNoOfPages];
		for (int i = totalNoOfPages; i > 0; i--) {
			arrayOfPages1[i - 1] = i;
		}
		return arrayOfPages1;
	}

	@Override
	public String completeOnboarding(int id, Authentication authentication) throws GoForWealthAdminException {
		User user = userRepository.getOne(id);
		String response = null;
		if (user != null) {
			int frontImage = 0;
			if (user.getKycDetails() != null) {
				user.getOnboardingStatus().setKycStatus(user.getOnboardingStatus().getKycStatus());
				if (user.getBankDetails() != null) {
					if (user.getPanDetails() != null) {
						if (!user.getAddressProofs().isEmpty()) {
							Set<AddressProof> addressProof = user.getAddressProofs();
							if (!addressProof.isEmpty()) {
								for (AddressProof addressProof2 : addressProof) {
									if (addressProof2.getFrontImage() != null) {
										frontImage++;
									}
								}
								if (frontImage == 0) {
									throw new GoForWealthAdminException(GoForWealthAdminErrorMessageEnum.DATA_NOT_FOUND_CODE.getValue(),GoForWealthAdminErrorMessageEnum.AADHAR_IMAGE_NOT_FOUND_MESSAGE.getValue());
								}
								if (user.getBankDetails().getSignatureImage() != null) {
									if (user.getKycDetails().getPhotograph() != null) {
										if (user.getKycDetails().getPanCardImage() != null) {
											response = goForWealthPRSEmandateService.mandateUserByAdmin(user, true);
											userRepository.save(user);
										} else {
											throw new GoForWealthAdminException(GoForWealthAdminErrorMessageEnum.DATA_NOT_FOUND_CODE.getValue(),GoForWealthAdminErrorMessageEnum.PAN_IMAGE_NOT_FOUND_MESSAGE.getValue());
										}
									} else {
										throw new GoForWealthAdminException(GoForWealthAdminErrorMessageEnum.DATA_NOT_FOUND_CODE.getValue(),GoForWealthAdminErrorMessageEnum.USER_IMAGE_NOT_FOUND_MESSAGE.getValue());
									}
								} else {
									throw new GoForWealthAdminException(GoForWealthAdminErrorMessageEnum.DATA_NOT_FOUND_CODE.getValue(),GoForWealthAdminErrorMessageEnum.USER_SIGNATURE_NOT_FOUND_MESSAGE.getValue());
								}
							}
						} else {
							throw new GoForWealthAdminException(GoForWealthAdminErrorMessageEnum.DATA_NOT_FOUND_CODE.getValue(),GoForWealthAdminErrorMessageEnum.ADDRESS_DATA_NOT_FOUND_MESSAGE.getValue());
						}
					} else {
						throw new GoForWealthAdminException(GoForWealthAdminErrorMessageEnum.DATA_NOT_FOUND_CODE.getValue(),GoForWealthAdminErrorMessageEnum.PAN_DATA_NOT_FOUND_MESSAGE.getValue());
					}
				} else {
					throw new GoForWealthAdminException(GoForWealthAdminErrorMessageEnum.DATA_NOT_FOUND_CODE.getValue(),GoForWealthAdminErrorMessageEnum.BANK_DATA_NOT_FOUND_MESSAGE.getValue());
				}
			} else {
				throw new GoForWealthAdminException(GoForWealthAdminErrorMessageEnum.DATA_NOT_FOUND_CODE.getValue(),GoForWealthAdminErrorMessageEnum.KYC_DATA_NOT_FOUND_MESSAGE.getValue());
			}
		}
		return response;
	}

	@Override
	public String addCategory(String category, int userId) throws GoForWealthAdminException {
		User user = userRepository.findUserByRole(userId, "ADMIN");
		if (user == null) {
			throw new GoForWealthAdminException(GoForWealthErrorMessageEnum.ACCESS_DENIED_CODE.getValue(),GoForWealthErrorMessageEnum.ACCESS_DENIED_MESSAGE.getValue());
		}
		BlogCategory blogCategory = new BlogCategory();
		blogCategory.setBlogCategoryName(category);
		BlogCategory isCategoryEsixt = blogCategoryRepository.findByCategoryName(category);
		if (isCategoryEsixt != null) {
			return "categoryEsixt";
		}
		BlogCategory blogCategory2 = blogCategoryRepository.save(blogCategory);
		if (blogCategory2 == null) {
			return "failure";
		}
		blogCategory2.getBlogCategoryName();
		return "success";
	}

	@Override
	public List<BlogCategoryDTO> getAllBlogCategory(int userId) throws GoForWealthAdminException {
		User user = userRepository.findUserByRole(userId, "ADMIN");
		if (user == null) {
			throw new GoForWealthAdminException(GoForWealthErrorMessageEnum.ACCESS_DENIED_CODE.getValue(),GoForWealthErrorMessageEnum.ACCESS_DENIED_MESSAGE.getValue());
		}
		List<BlogCategory> blogCategoriesList = blogCategoryRepository.findAll();
		if (blogCategoriesList.isEmpty()) {
			throw new GoForWealthAdminException(GoForWealthAdminErrorMessageEnum.DATA_NOT_FOUND_CODE.getValue(),GoForWealthAdminErrorMessageEnum.KYC_DATA_NOT_FOUND_MESSAGE.getValue());
		}
		List<BlogCategoryDTO> blogCategoryDTOList = new ArrayList<BlogCategoryDTO>();
		for (BlogCategory blogCategory : blogCategoriesList) {
			BlogCategoryDTO blogCategoryDTO = new BlogCategoryDTO();
			blogCategoryDTO.setBlogCategoryId(blogCategory.getBlogCategoryId());
			blogCategoryDTO.setBlogCategoryName(blogCategory.getBlogCategoryName());
			blogCategoryDTOList.add(blogCategoryDTO);
		}
		return blogCategoryDTOList;
	}

	@Override
	public List<BlogDTO> getBlogByCategoryId(int categoryId, int userId) throws GoForWealthAdminException {
		User user = userRepository.findUserByRole(userId, "ADMIN");
		if (user == null) {
			throw new GoForWealthAdminException(GoForWealthErrorMessageEnum.ACCESS_DENIED_CODE.getValue(),GoForWealthErrorMessageEnum.ACCESS_DENIED_MESSAGE.getValue());
		}
		List<Blog> blogList = adminRepository.getBlogByCategoryId(categoryId);
		if (blogList.isEmpty()) {
			throw new GoForWealthAdminException(GoForWealthErrorMessageEnum.DATA_NOT_FOUND_CODE.getValue(),GoForWealthErrorMessageEnum.DATA_NOT_FOUND_MESSAGE.getValue());
		}
		List<BlogDTO> blogDTOList = new ArrayList<BlogDTO>();
		SimpleDateFormat sdf = new SimpleDateFormat("dd MMMM yyyy");
		for (Blog blog : blogList) {
			BlogDTO blogDTO = new BlogDTO();
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
	public String contactUsFormData(ContactUsRequestDTO contactUsRequestDTO) {
		ContactUs contactUs = new ContactUs();
		String response = null;
		contactUs.setEmail(contactUsRequestDTO.getEmail());
		contactUs.setMessage(contactUsRequestDTO.getMessage());
		contactUs.setName(contactUsRequestDTO.getName());
		contactUs.setSubject(contactUsRequestDTO.getSubject());
		contactUs = contactUsRepository.save(contactUs);
		try {
			MimeMessage mail = mailSender.createMimeMessage();
			mail.setFrom(new InternetAddress(emailVerificationConfiguration.mailSenderAddress,messageSource.getMessage("verifyemail.mailSenderPersonalName", null, Locale.ENGLISH)));
			mail.setRecipient(Message.RecipientType.TO, new InternetAddress(contactUs.getEmail()));
			mail.setSubject("Team Go4Wealth");
			mail.setContent("Thanks to connect with us,We have received your valuable message, We will connect soon with you : ","text/html");
			mailSender.send(mail);
			response = "success";
		} catch (Exception e) {}
		return response;
	}

	@Override
	public List<TestimonialResponseDTO> getTestimonialData(int userId) throws GoForWealthAdminException {
		User user = userRepository.findUserByRole(userId, "ADMIN");
		if (user == null) {
			throw new GoForWealthAdminException(GoForWealthErrorMessageEnum.ACCESS_DENIED_CODE.getValue(),GoForWealthErrorMessageEnum.ACCESS_DENIED_MESSAGE.getValue());
		}
		List<Testimonial> testimonialList = testimonialRepository.findAll();
		if (testimonialList.isEmpty()) {
			List<TestimonialResponseDTO> testimonialResponseDTOs = new ArrayList<TestimonialResponseDTO>();
			return testimonialResponseDTOs;
		}
		List<TestimonialResponseDTO> testimonialResponseDTOs = new ArrayList<TestimonialResponseDTO>();
		for (Testimonial testimonial : testimonialList) {
			TestimonialResponseDTO testimonialResponseDTO = new TestimonialResponseDTO();
			if (testimonial.getComments() != null) {
				testimonialResponseDTO.setComments(testimonial.getComments());
			}
			if (testimonial.getCompanyName() != null) {
				testimonialResponseDTO.setCompanyName(testimonial.getCompanyName());
			}
			if (testimonial.getDesignation() != null) {
				testimonialResponseDTO.setDesignation(testimonial.getDesignation());
			}
			if (testimonial.getImageUrl() != null) {
				testimonialResponseDTO.setImageUrl(testimonial.getImageUrl());
			}
			if (testimonial.getName() != null) {
				testimonialResponseDTO.setName(testimonial.getName());
			}
			testimonialResponseDTO.setTestimonialId(testimonial.getTestimonialId());
			testimonialResponseDTOs.add(testimonialResponseDTO);
		}
		return testimonialResponseDTOs;
	}

	@Override
	public void getTestimonialImageById(int imageId, HttpServletResponse response) throws IOException {
		Testimonial testimonial = testimonialRepository.getOne(imageId);
		ServletOutputStream out;
		FileInputStream fin;
		BufferedInputStream bin;
		out = response.getOutputStream();
		BufferedOutputStream bout = new BufferedOutputStream(out);
		try {
			fin = new FileInputStream(testimonial.getImageUrl());
			bin = new BufferedInputStream(fin);
			int ch = 0;
			response.setContentType(IMAGE_CONTENT_TYPE);
			while ((ch = bin.read()) != -1) {
				bout.write(ch);
			}
			bin.close();
			fin.close();
			bout.close();
			out.close();
		} catch (FileNotFoundException ex) {
			ex.printStackTrace();
		} catch (Exception ex) {
			ex.printStackTrace();
		} finally {
			bout.close();
			out.close();
		}
	}

	@Override
	public List<BlogDTO> getRelatedBlog(int id, int userId) throws GoForWealthAdminException {
		User user = userRepository.findUserByRole(userId, "ADMIN");
		if (user == null) {
			throw new GoForWealthAdminException(GoForWealthErrorMessageEnum.ACCESS_DENIED_CODE.getValue(),GoForWealthErrorMessageEnum.ACCESS_DENIED_MESSAGE.getValue());
		}
		Blog blog1 = adminRepository.getBlogByBlogId(id);
		List<Blog> blogList = adminRepository.getBlogByCategoryId(blog1.getBlogCategory().getBlogCategoryId(),new PageRequest(0, 6));
		List<BlogDTO> blogDTOList = new ArrayList<BlogDTO>();
		SimpleDateFormat sdf = new SimpleDateFormat("dd MMMM yyyy");
		for (Blog blog : blogList) {
			if (id != blog.getBlogId()) {
				BlogDTO blogDTO = new BlogDTO();
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
	public String saveTestimonial(TestiMonialRequestDTO testiMonialRequestDTO, int userId)throws GoForWealthAdminException {
		User user = userRepository.findUserByRole(userId, "ADMIN");
		if (user == null) {
			throw new GoForWealthAdminException(GoForWealthErrorMessageEnum.ACCESS_DENIED_CODE.getValue(),GoForWealthErrorMessageEnum.ACCESS_DENIED_MESSAGE.getValue());
		}
		StoreConf imageLocation = storeConfRepository.findByKeyword("IMAGE_LOCATION");
		StoreConf testimonialLocation = storeConfRepository.findByKeyword("TESTIMONIAL_LOCATION");
		StoreConf doubleSlash = storeConfRepository.findByKeyword(GoForWealthAdminConstants.DOUBLE_BACKWARD_SLASH);
		Testimonial testimonial = new Testimonial();
		if (testiMonialRequestDTO.getComments() != null) {
			testimonial.setComments(testiMonialRequestDTO.getComments());
		}
		if (testiMonialRequestDTO.getCompany() != null) {
			testimonial.setCompanyName(testiMonialRequestDTO.getCompany());
		}
		if (testiMonialRequestDTO.getDesignation() != null) {
			testimonial.setDesignation(testiMonialRequestDTO.getDesignation());
		}
		if (testiMonialRequestDTO.getName() != null) {
			testimonial.setName(testiMonialRequestDTO.getName());
		}
		testimonial = testimonialRepository.save(testimonial);
		String path = imageLocation.getKeywordValue() + doubleSlash.getKeywordValue()+ testimonialLocation.getKeywordValue();
		if (testiMonialRequestDTO.getBase64String() != null) {
			String base64Data = testiMonialRequestDTO.getBase64String();
			byte[] fileByteArry = Base64.decodeBase64(base64Data);
			String destPath = path + doubleSlash.getKeywordValue() + TESTIMONIAL_IMAGE_NAME+ testimonial.getTestimonialId() + IMAGE_TYPE_JPG;
			testimonial.setImageUrl(destPath);
			File file = new File(destPath);
			try {
				OutputStream os = new FileOutputStream(file);
				os.write(fileByteArry);
				os.close();
			} catch (Exception e) {
				e.printStackTrace();
			}
			testimonialRepository.save(testimonial);
		}
		return "success";
	}

	@Override
	public TestimonialResponseDTO getTestimonialById(int id, int userId) throws GoForWealthAdminException {
		User user = userRepository.findUserByRole(userId, "ADMIN");
		if (user == null) {
			throw new GoForWealthAdminException(GoForWealthErrorMessageEnum.ACCESS_DENIED_CODE.getValue(),GoForWealthErrorMessageEnum.ACCESS_DENIED_MESSAGE.getValue());
		}
		Testimonial testimonial = testimonialRepository.getOne(id);
		TestimonialResponseDTO testimonialResponseDTO = new TestimonialResponseDTO();
		testimonialResponseDTO.setComments(testimonial.getComments());
		testimonialResponseDTO.setCompanyName(testimonial.getCompanyName());
		testimonialResponseDTO.setDesignation(testimonial.getDesignation());
		testimonialResponseDTO.setImageUrl(testimonial.getImageUrl());
		testimonialResponseDTO.setName(testimonial.getName());
		testimonialResponseDTO.setTestimonialId(testimonial.getTestimonialId());
		return testimonialResponseDTO;
	}

	@Override
	public String updateTestimonial(TestiMonialRequestDTO testiMonialRequestDTO, int id, int userId)throws GoForWealthAdminException {
		User user = userRepository.findUserByRole(userId, "ADMIN");
		if (user == null) {
			throw new GoForWealthAdminException(GoForWealthErrorMessageEnum.ACCESS_DENIED_CODE.getValue(),GoForWealthErrorMessageEnum.ACCESS_DENIED_MESSAGE.getValue());
		}
		StoreConf imageLocation = storeConfRepository.findByKeyword("IMAGE_LOCATION");
		StoreConf testimonialLocation = storeConfRepository.findByKeyword("TESTIMONIAL_LOCATION");
		StoreConf doubleSlash = storeConfRepository.findByKeyword(GoForWealthAdminConstants.DOUBLE_BACKWARD_SLASH);
		Testimonial testimonial = testimonialRepository.getOne(id);
		if (testiMonialRequestDTO.getComments() != null) {
			testimonial.setComments(testiMonialRequestDTO.getComments());
		}
		if (testiMonialRequestDTO.getCompany() != null) {
			testimonial.setCompanyName(testiMonialRequestDTO.getCompany());
		}
		if (testiMonialRequestDTO.getDesignation() != null) {
			testimonial.setDesignation(testiMonialRequestDTO.getDesignation());
		}
		if (testiMonialRequestDTO.getName() != null) {
			testimonial.setName(testiMonialRequestDTO.getName());
		}
		testimonial = testimonialRepository.save(testimonial);
		String path = imageLocation.getKeywordValue() + doubleSlash.getKeywordValue()+ testimonialLocation.getKeywordValue();
		if (testiMonialRequestDTO.getBase64String() != null) {
			String base64Data = testiMonialRequestDTO.getBase64String();
			byte[] fileByteArry = Base64.decodeBase64(base64Data);
			String destPath = path + doubleSlash.getKeywordValue() + TESTIMONIAL_IMAGE_NAME+ testimonial.getTestimonialId() + IMAGE_TYPE_JPG;
			testimonial.setImageUrl(destPath);
			File file = new File(destPath);
			try {
				OutputStream os = new FileOutputStream(file);
				os.write(fileByteArry);
				os.close();
			} catch (Exception e) {
				e.printStackTrace();
			}
			testimonialRepository.save(testimonial);
		}
		return "success";
	}

	@Override
	public void deleteTestimonialByID(int id, int userId) throws GoForWealthAdminException {
		User user = userRepository.findUserByRole(userId, "ADMIN");
		if (user == null) {
			throw new GoForWealthAdminException(GoForWealthErrorMessageEnum.ACCESS_DENIED_CODE.getValue(),GoForWealthErrorMessageEnum.ACCESS_DENIED_MESSAGE.getValue());
		}
		testimonialRepository.deleteById(id);
	}

	@Override
	public List<TestimonialResponseDTO> getUserTestimonialData() throws GoForWealthAdminException {
		List<Testimonial> testimonialList = testimonialRepository.findAll();
		if (testimonialList.isEmpty()) {
			throw new GoForWealthAdminException(GoForWealthAdminErrorMessageEnum.DATA_NOT_FOUND_CODE.getValue(),GoForWealthAdminErrorMessageEnum.DATA_NOT_FOUND_MESSAGE.getValue());
		}
		List<TestimonialResponseDTO> testimonialResponseDTOs = new ArrayList<TestimonialResponseDTO>();
		for (Testimonial testimonial : testimonialList) {
			TestimonialResponseDTO testimonialResponseDTO = new TestimonialResponseDTO();
			if (testimonial.getComments() != null) {
				testimonialResponseDTO.setComments(testimonial.getComments());
			}
			if (testimonial.getCompanyName() != null) {
				testimonialResponseDTO.setCompanyName(testimonial.getCompanyName());
			}
			if (testimonial.getDesignation() != null) {
				testimonialResponseDTO.setDesignation(testimonial.getDesignation());
			}
			if (testimonial.getImageUrl() != null) {
				testimonialResponseDTO.setImageUrl(testimonial.getImageUrl());
			}
			if (testimonial.getName() != null) {
				testimonialResponseDTO.setName(testimonial.getName());
			}
			testimonialResponseDTO.setTestimonialId(testimonial.getTestimonialId());
			testimonialResponseDTOs.add(testimonialResponseDTO);
		}
		return testimonialResponseDTOs;
	}

	@Override
	public boolean getUserAuthority(Integer userId) {
		User user = userRepository.findUserByRole(userId, "ADMIN");
		if (user == null) {
			return false;
		} else {
			return true;
		}
	}

	@Override
	public List<Seo> getAllSeos(Integer userId) throws GoForWealthAdminException {
		User user = userRepository.findUserByRole(userId, "ADMIN");
		if (user == null) {
			throw new GoForWealthAdminException(GoForWealthErrorMessageEnum.ACCESS_DENIED_CODE.getValue(),GoForWealthErrorMessageEnum.ACCESS_DENIED_MESSAGE.getValue());
		}
		List<Seo> seoList = seoRepository.findAll();
		if (seoList.isEmpty()) {
			throw new GoForWealthAdminException(GoForWealthErrorMessageEnum.DATA_NOT_FOUND_CODE.getValue(),GoForWealthErrorMessageEnum.DATA_NOT_FOUND_MESSAGE.getValue());
		}
		return seoList;
	}

	@Override
	public Seo updateSeo(Seo updateSeoRequest, Integer userId) throws GoForWealthAdminException {
		User user = userRepository.findUserByRole(userId, "ADMIN");
		if (user == null) {
			throw new GoForWealthAdminException(GoForWealthErrorMessageEnum.ACCESS_DENIED_CODE.getValue(),GoForWealthErrorMessageEnum.ACCESS_DENIED_MESSAGE.getValue());
		}
		Seo seo = seoRepository.save(updateSeoRequest);
		return seo;
	}

	@Override
	public boolean saveExcelDataToTopSchemeUsingBase64(List<TopSchemes> uploadTopShemes) {
		logger.info("In saveExcelDataToTopSchemeUsingBase64()");
		List<TopSchemes> topSchemesList = topSchemeRepository.findAll();
		if(topSchemesList.size()>0){
			for (TopSchemes topSchemesObj : topSchemesList) {
				topSchemesObj.setDeactivatedDate(new Date());
				topSchemesObj.setStatus("Deactive");
				topSchemesObj.setSequence(0);
				topSchemeRepository.save(topSchemesObj);
				Scheme scheme = schemeRepository.findBySchemeCode(topSchemesObj.getSchemeCode());
				if (scheme != null) {
					scheme.setPriority("3");
					schemeRepository.save(scheme);
				}
			}
		}
		uploadTopShemes.forEach(uploadTopSheme -> {
			try {
				if (uploadTopSheme.getSchemeCode().equals("1") || uploadTopSheme.getSchemeCode().equals("2") || uploadTopSheme.getSchemeCode().equals("3"))
					uploadTopSheme.setSchemeCode("0" + uploadTopSheme.getSchemeCode());
				TopSchemes topSchemes = topSchemeRepository.getSchemeBySchemeCode(uploadTopSheme.getSchemeCode());
				Scheme scheme = schemeRepository.findBySchemeCode(uploadTopSheme.getSchemeCode());
				if (scheme != null) {
					scheme.setPriority("1");
					schemeRepository.save(scheme);
				}
				topSchemes.setCreatedDate(new Date());
				topSchemes.setDeactivatedDate(new Date());
				topSchemes.setStatus("Active");
				topSchemes.setSequence(uploadTopSheme.getSequence());
				topSchemeRepository.save(topSchemes);
			} catch (Exception npe) {
				TopSchemes topSchemes = new TopSchemes();
				topSchemes.setSchemeCode(uploadTopSheme.getSchemeCode());
				topSchemes.setSchemeName(uploadTopSheme.getSchemeName());
				topSchemes.setCreatedDate(new Date());
				topSchemes.setDeactivatedDate(new Date());
				topSchemes.setStatus("Active");
				topSchemes.setSequence(uploadTopSheme.getSequence());
				String property = "MF";
				if (uploadTopSheme.getSchemeCategory().contains(property))
					topSchemes.setSchemeCategory(uploadTopSheme.getSchemeCategory());
				else
					topSchemes.setSchemeCategory(uploadTopSheme.getSchemeCategory() + " " + property);
				Scheme scheme = schemeRepository.findBySchemeCode(uploadTopSheme.getSchemeCode());
				if (scheme != null) {
					scheme.setPriority("1");
					schemeRepository.save(scheme);
				}
				topSchemeRepository.saveAndFlush(topSchemes);
			}
		});
		logger.info("Out saveExcelDataToTopSchemeUsingBase64()");
		return true;
	}

	@Override
	public List<FundSchemeDTO> getAllSchemes() {
		List<Scheme> scheme = schemeRepository.findAll();
		List<FundSchemeDTO> fundSchemeDtoList = new ArrayList<>();
		if (!scheme.isEmpty()) {
			for (Scheme scheme2 : scheme) {
				FundSchemeDTO fundSchemeDto = new FundSchemeDTO();
				fundSchemeDto.setSchemeCode(scheme2.getSchemeCode());
				fundSchemeDto.setSchemeName(scheme2.getSchemeName());
				fundSchemeDto.setIsinCode(scheme2.getIsin());
				fundSchemeDto.setMinimumPurchaseAmount(scheme2.getMinimumPurchaseAmount());
				fundSchemeDto.setMaximumPurchaseAmount(scheme2.getMaximumPurchaseAmount());
				fundSchemeDto.setSchemeType(scheme2.getSchemeType());
				fundSchemeDto.setPurchaseAllowed(scheme2.getPurchaseAllowed());
				fundSchemeDto.setSipAllowed(scheme2.getSipFlag());
				fundSchemeDto.setRedemptionAllowed(scheme2.getRedemptionAllowed());
				fundSchemeDto.setStatus(scheme2.getStartDate());
				fundSchemeDto.setSchemeEndDate(scheme2.getEndDate());
				if (scheme2.getDisplay().equals("0"))
					fundSchemeDto.setShowScheme("Y");
				else
					fundSchemeDto.setShowScheme("N");
				fundSchemeDto.setPriority(scheme2.getPriority());
				fundSchemeDtoList.add(fundSchemeDto);
			}
		}
		return fundSchemeDtoList;
	}

	@Override
	public boolean updateSchemeWithPriority(List<FundSchemeDTO> fundSchemeDtoList) {
		boolean flag = false;
		try {
			for (FundSchemeDTO fundSchemeDTO : fundSchemeDtoList) {
				Scheme schemeObj = schemeRepository.findBySchemeCode(fundSchemeDTO.getSchemeCode());
				if (schemeObj != null) {
					schemeObj.setPriority(fundSchemeDTO.getPriority());
					schemeRepository.save(schemeObj);
				}
			}
			flag = true;
		} catch (Exception ex) {
			flag = false;
			ex.printStackTrace();
		}
		return flag;
	}

	@Override
	public List<UsersListDTO> searchUserByKeyword(String userName, Integer userId) throws GoForWealthAdminException {
		User user = userRepository.findUserByRole(userId, "ADMIN");
		List<UsersListDTO> usersListDTO = new ArrayList<UsersListDTO>();
		if (user != null) {
			List<User> userList = userRepository.searchUserExcludeAdmin(userName);
			if (!userList.isEmpty()) {
				for (User usersDTO : userList) {
					if (!usersDTO.getUserRoles().isEmpty() && usersDTO.getUserRoles().iterator().next().getId().getRoleId() != 1) {
						UsersListDTO userDTO = new UsersListDTO();
						userDTO.setEmail(usersDTO.getEmail());
						userDTO.setMobileNumber(usersDTO.getMobileNumber());
						userDTO.setUserId(usersDTO.getUserId());
						userDTO.setUserName(usersDTO.getFirstName() + " " + usersDTO.getLastName());
						if (usersDTO.getOnboardingStatus()!=null) {
							userDTO.setClientCode(usersDTO.getOnboardingStatus().getClientCode());
							if (usersDTO.getOnboardingStatus().getOverallStatus() == 1) {
								userDTO.setOverAllStatus(GoForWealthAdminConstants.OVER_ALL_ONBORADING_STATUS_DONE);
							} else {
								userDTO.setOverAllStatus(GoForWealthAdminConstants.OVER_ALL_ONBORADING_STATUS_NOTDONE);
							}
						}
						usersListDTO.add(userDTO);
					}
				}
			}
		}
		return usersListDTO;
	}

	@Override
	public List<OrdersDTO> getAllOrders(int count) {
		List<OrdersDTO> ordersDTOList = new ArrayList<>();
		OrdersDTO ordersDTO = new OrdersDTO();
		Set<OrderItem> orderItems;
		List<Orders> ordersList = orderRepository.findAllOrders(new PageRequest(count, 15));
		for (Orders orders : ordersList) {
			orderItems = orders.getOrderItems();
			for (OrderItem orderItem : orderItems) {
				ordersDTO = new OrdersDTO();
				ordersDTO.setOrderId(orderItem.getOrders().getOrdersId());
				ordersDTO.setAmount(orderItem.getProductprice().doubleValue());
				ordersDTO.setDayOfSip(orderItem.getDescription());
				ordersDTO.setSchemeId(orderItem.getScheme().getSchemeId());
				ordersDTO.setSchemeName(orderItem.getScheme().getSchemeName());
				ordersDTO.setStatus(orderItem.getStatus());
				ordersDTO.setCreationDate(orderItem.getLastupdate());
				ordersDTO.setType(orders.getType());
				ordersDTO.setUserId(orders.getUser().getUserId());
				ordersDTO.setUsername(orders.getUser().getFirstName() + " " + orders.getUser().getLastName());
				ordersDTO.setEmail(orders.getUser().getEmail());
				ordersDTOList.add(ordersDTO);
			}
		}
		return ordersDTOList;
	}

	@Override
	public int getTotalNumberOfOrders() throws GoForWealthAdminException {
		int count = (int) orderRepository.count();
		if (count == 0) {
			throw new GoForWealthAdminException(GoForWealthAdminErrorMessageEnum.DATA_NOT_FOUND_CODE.getValue(),GoForWealthAdminErrorMessageEnum.DATA_NOT_FOUND_MESSAGE.getValue());
		}
		return count;
	}

	@Override
	@Transactional
	public String uploadVideoViaBase64(Integer userId, SchemeUploadRequest fileDetails) throws IOException, GoForWealthAdminException {
		User user = userRepository.findUserByRole(userId, "ADMIN");
		if (user == null) {
			throw new GoForWealthAdminException(GoForWealthErrorMessageEnum.ACCESS_DENIED_CODE.getValue(),GoForWealthErrorMessageEnum.ACCESS_DENIED_MESSAGE.getValue());
		}
		StoreConf imageUrl = storeConfRepository.findByKeyword(GoForWealthPRSConstants.IMAGE_LOCATION);
		StoreConf doubleSlash = storeConfRepository.findByKeyword(GoForWealthPRSConstants.DOUBLE_BACKWARD_SLASH);
		StoreConf forwardSlash = storeConfRepository.findByKeyword(GoForWealthPRSConstants.FORWARD_SLASH);
		String response = null;
		@SuppressWarnings("restriction")
		BASE64Decoder decoder = new BASE64Decoder();
		@SuppressWarnings("restriction")
		byte[] imgBytes = decoder.decodeBuffer(fileDetails.getBase64());
		String fileType = null;
		if (fileDetails.getFileType() != null)
			fileType = fileDetails.getFileType().split("/")[1];
		if (fileType == null)throw new GoForWealthAdminException(GoForWealthAdminErrorMessageEnum.DATA_NOT_SAVED_MESSAGE.getValue());
		File imgOutFile = null;
		String filePath = "";
		if (fileDetails.getFileType().split("/")[0].equals("video")) {
			StoreConf userImageLocation = storeConfRepository.findByKeyword(GoForWealthAdminConstants.HOMEVIDEO_LOCATION);
			String userImagePath = imageUrl.getKeywordValue() + userImageLocation.getKeywordValue();
			String userImagePath4db = userImageLocation.getKeywordValue();
			Path path = Paths.get(userImagePath);
			if (!Files.exists(path))
				Files.createDirectories(path);
			filePath = userImagePath + doubleSlash.getKeywordValue() + GoForWealthAdminConstants.HOMEVIDEO_NAME + "." + fileType;
			userImagePath4db = userImagePath + forwardSlash.getKeywordValue() + GoForWealthAdminConstants.HOMEVIDEO_NAME + "." + fileType;
			imgOutFile = new File(filePath);
			StoreConf homeVideoUrl = storeConfRepository.findByKeyword(GoForWealthAdminConstants.HOMEVIDEO_URL);
			homeVideoUrl.setKeywordValue(userImagePath4db);
			storeConfRepository.save(homeVideoUrl);
			response = GoForWealthAdminConstants.SUCCESS;
		}
		if (imgOutFile == null) {
			throw new GoForWealthAdminException(GoForWealthAdminErrorMessageEnum.DATA_NOT_SAVED_MESSAGE.getValue());
		}
		if (!imgOutFile.exists()) {
			imgOutFile.createNewFile();
		}
		FileOutputStream outputStream = new FileOutputStream(imgOutFile);
		outputStream.write(imgBytes);
		outputStream.close();
		return response;
	}

	@Override
	public String saveHomeVideoUrl(Integer userId, String videoUrl) throws GoForWealthAdminException {
		User user = userRepository.findUserByRole(userId, "ADMIN");
		if (user == null) {
			throw new GoForWealthAdminException(GoForWealthErrorMessageEnum.ACCESS_DENIED_CODE.getValue(),GoForWealthErrorMessageEnum.ACCESS_DENIED_MESSAGE.getValue());
		}
		StoreConf homeVideoUrl = storeConfRepository.findByKeyword(GoForWealthAdminConstants.HOMEVIDEO_URL);
		homeVideoUrl.setKeywordValue(videoUrl);
		storeConfRepository.save(homeVideoUrl);
		return GoForWealthAdminConstants.SUCCESS;
	}

	@Override
	public String getHomeVideoUrl() {
		String videoUrl = "";
		StoreConf homeVideoUrl = storeConfRepository.findByKeyword(GoForWealthAdminConstants.HOMEVIDEO_URL);
		videoUrl = homeVideoUrl.getKeywordValue();
		return videoUrl;
	}

	@Override
	public List<CategoryListResponseDTO> getCategoryList() {
		List<AssetClassInternal> assetClassInrtnalList = assetClassInternalRepository.findAll();
		List<CategoryListResponseDTO> categoryListResponseDTOs = new ArrayList<>();
		if (!assetClassInrtnalList.isEmpty()) {
			for (AssetClassInternal assetClassInrtnal : assetClassInrtnalList) {
				CategoryListResponseDTO categoryListResponseDTO = new CategoryListResponseDTO();
				categoryListResponseDTO.setAssetClassInternalId(assetClassInrtnal.getAssetClassInternalId());
				categoryListResponseDTO.setFundType(assetClassInrtnal.getFundType());
				categoryListResponseDTO.setFundTypeCode(assetClassInrtnal.getFundTypeCode());
				categoryListResponseDTOs.add(categoryListResponseDTO);
			}
		}
		return categoryListResponseDTOs;
	}

	@Override
	public void sendReportAllRegisteredUserToAdmin(GoForWealthUMAService goForWealthUMAService) {
		try {
			DateFormat format = new SimpleDateFormat("yyyy-MM-dd");
			Date todayDate = new Date();
			String todayDateStr = format.format(todayDate);
			List<User> userObj = userRepository.findAll();
			List<UsersListDTO> usersListDtos = new ArrayList<>();
			if (userObj.size() > 0) {
				for (User user : userObj) {
					String userRegisterDate = format.format(user.getCreatedTimestamp());
					if (userRegisterDate.equals(todayDateStr)) {
						if (!user.getRegisterType().equals("GUEST_USER")) {
							if (user != null) {
								UsersListDTO userListDto = new UsersListDTO();
								userListDto.setFullName(user.getFirstName() + " " + user.getLastName());
								userListDto.setEmail(user.getEmail());
								userListDto.setMobileNumber(user.getMobileNumber());
								if (user.getEmailVerified() == 1) {
									userListDto.setEmailVerified("Verified");
								} else {
									userListDto.setEmailVerified("not-verified");
								}
								if (user.getMobileVerified() == 1) {
									userListDto.setMobileVerified("Verified");
								} else {
									userListDto.setMobileVerified("not-verified");
								}
								if (user.getPanDetails() != null) {
									if (user.getPanDetails().getPanNo() != null) {
										if (user.getPanDetails().getVerified().equals(GoForWealthPRSConstants.PAN_NUMBER_VERIFIED)) {
											userListDto.setPanVerified("verified");
										} else {
											userListDto.setPanVerified("not-verified");
										}
										if (user.getPanDetails().getPanNo().equals("") || user.getPanDetails().getPanNo() == null) {
											userListDto.setPanNumber("");
										} else {
											try {
												userListDto.setPanNumber(encryptUserDetail.decrypt(user.getPanDetails().getPanNo()));
											} catch (Exception ex) {
												ex.printStackTrace();
											}
										}
										userListDto.setPancardName(user.getPanDetails().getFullName());
										if (user.getPanDetails().getDateOfBirth() != null)
											userListDto.setDob(format.format(user.getPanDetails().getDateOfBirth()));
										if (user.getPanDetails().getGender() != null)
											userListDto.setGender(user.getPanDetails().getGender());
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
									userListDto.setAadhaarNumber(decryptAadharNo);
									userListDto.setCity(addressProof.getCity());
									userListDto.setPinCode(addressProof.getPincode());
									userListDto.setState(addressProof.getState());
									userListDto.setAadhaarVerified(addressProof.getVerified());
								}
								if (user.getOnboardingStatus().getOverallStatus() == 1) {
									userListDto.setOnboardingStatus("Done");
								} else {
									userListDto.setOnboardingStatus("Not Done");
								}
								usersListDtos.add(userListDto);
							}
						}
					}
				}
				if (usersListDtos.size() > 0) {
					Workbook wb = GoForWealthAdminUtil.createExportFileForReport(usersListDtos);
					StoreConf imageUrl = storeConfRepository.findByKeyword(GoForWealthAdminConstants.IMAGE_LOCATION);
					StoreConf doubleSlash = storeConfRepository.findByKeyword(GoForWealthPRSConstants.DOUBLE_BACKWARD_SLASH);
					StoreConf RegisteredUserExcelReportLocation = storeConfRepository.findByKeyword(GoForWealthAdminConstants.REGISTERED_USER_EXCEL_REPORT_LOCATION);
					String excelPath = imageUrl.getKeywordValue() + RegisteredUserExcelReportLocation.getKeywordValue();
					Path path = Paths.get(excelPath);
					File imgOutFile = null;
					String filePath = "";
					String fileType = "xls";
					if (!Files.exists(path))
						Files.createDirectories(path);
					filePath = excelPath + doubleSlash.getKeywordValue() + GoForWealthAdminConstants.REGISTERED_USER_REPORT_FILE_PREFIX + todayDateStr + "." + fileType;
					imgOutFile = new File(filePath);
					if (!imgOutFile.exists()) {
						imgOutFile.createNewFile();
					}
					FileOutputStream outputStream = new FileOutputStream(imgOutFile);
					wb.write(outputStream);
					outputStream.close();
					String file1 = filePath;
					FileInputStream imageInFile = new FileInputStream(file1);
					byte imageData[] = new byte[(int) file1.length()];
					imageInFile.read(imageData);
					byte[] encodedBytes = Base64.encodeBase64(imageData);
					String encodeString = new String(encodedBytes);
					String convertedImageData = Base64.encodeBase64URLSafeString(imageData);
					StoreConf storeConfObj = storeConfRepository.findByKeyword(GoForWealthAdminConstants.EMAIL_TO_SEND_USER_REPORT);
					String emailTo = storeConfObj.getKeywordValue();
					final String emailSubject = "Today Registered User Report";
					final String emailText = "Dear Sir,<br/>Please find the attached file.<br/><br/><br/>Regards,<br/>Go4wealth Admin Team<br/><br/>Note: This is a system generated e-mail, please do not reply to it.<br/><br/>This message is intended only for the person or entity to which it is addressed and may contain confidential and/or<br/>privileged information. If you have received this message in error, please notify the sender immediately and delete<br/>this message from your system.";
					File fileSend = new File(filePath);
					byte[] fileData = null;
					try {
						fileData = org.apache.commons.io.IOUtils.toByteArray(new FileInputStream(fileSend));
					} catch (IOException ex) {}
					logger.info("In sendReportAllRegisteredUserToAdmin() : " + new Date());
					mailUtility.baselineExampleWithAttechment(emailTo, emailSubject, emailText, fileData, fileSend,encodeString);
				} else {
					Workbook wb = GoForWealthAdminUtil.createExportFileForReport(usersListDtos);
					StoreConf imageUrl = storeConfRepository.findByKeyword(GoForWealthAdminConstants.IMAGE_LOCATION);
					StoreConf doubleSlash = storeConfRepository.findByKeyword(GoForWealthPRSConstants.DOUBLE_BACKWARD_SLASH);
					StoreConf RegisteredUserExcelReportLocation = storeConfRepository.findByKeyword(GoForWealthAdminConstants.REGISTERED_USER_EXCEL_REPORT_LOCATION);
					String excelPath = imageUrl.getKeywordValue() + RegisteredUserExcelReportLocation.getKeywordValue();
					Path path = Paths.get(excelPath);
					File imgOutFile = null;
					String filePath = "";
					String fileType = "xls";
					if (!Files.exists(path))
						Files.createDirectories(path);
					filePath = excelPath + doubleSlash.getKeywordValue() + GoForWealthAdminConstants.REGISTERED_USER_REPORT_FILE_PREFIX + todayDateStr + "." + fileType;
					imgOutFile = new File(filePath);
					if (!imgOutFile.exists()) {
						imgOutFile.createNewFile();
					}
					FileOutputStream outputStream = new FileOutputStream(imgOutFile);
					wb.write(outputStream);
					outputStream.close();
					String file1 = filePath;
					FileInputStream imageInFile = new FileInputStream(file1);
					byte imageData[] = new byte[(int) file1.length()];
					imageInFile.read(imageData);
					byte[] encodedBytes = Base64.encodeBase64(imageData);
					String encodeString = new String(encodedBytes);
					String convertedImageData = Base64.encodeBase64URLSafeString(imageData);
					StoreConf storeConfObj = storeConfRepository.findByKeyword(GoForWealthAdminConstants.EMAIL_TO_SEND_USER_REPORT);
					String emailTo = storeConfObj.getKeywordValue();
					final String emailSubject = "Today Registered User Report";
					final String emailText = "Dear Sir,<br/>Please find the attached file.<br/><br/><br/>Regards,<br/>Go4wealth Admin Team<br/><br/>Note: This is a system generated e-mail, please do not reply to it.<br/><br/>This message is intended only for the person or entity to which it is addressed and may contain confidential and/or<br/>privileged information. If you have received this message in error, please notify the sender immediately and delete<br/>this message from your system.";
					File fileSend = new File(filePath);
					byte[] fileData = null;
					try {
						fileData = org.apache.commons.io.IOUtils.toByteArray(new FileInputStream(fileSend));
					} catch (IOException ex) {}
					logger.info("In sendReportAllRegisteredUserToAdmin() : " + new Date());
					mailUtility.baselineExampleWithAttechment(emailTo, emailSubject, emailText, fileData, fileSend,encodeString);
					System.out.println("User's NOT Found");
				}
			}
		} catch (Exception ex) {}
		logger.info("Out sendReportAllRegisteredUserToAdmin() : " + new Date());
	}

	@Override
	public UserDetailedDataDTO getUserDetailedForAdminDashboard(User user) {
		int registeredUserWithOnboarding = 0;
		int registeredUserWithoutOnboarding = 0;
		int registeredUserWithKyc = 0;
		int registeredUserWithoutKyc = 0;
		int todayRegisteredUser = 0;
		int todayRegisteredUserWithOnboarding = 0;
		//int todayRegisterUserWithoutOnboarding = 0;
		int todayRegisteredUserWithKyc = 0;
		//int todayRegisteredUserWithoutKyc = 0;
		int pendingOrders = 0;
		
		int totalNoOfOrder = 0;
		int noOfConfirmedOrder = 0;
		int noOfPurchasedOrder = 0;
		int noOfCanceledOrder = 0;
		int todayOrder = 0;
		int todayPurchasedOrder = 0;
		int todayConfirmedOrder = 0;
		int todayCanceledOrder = 0;
		int noOfSIPOrders = 0;
		int noOfLumpsumOrders = 0;
		
		UserDetailedDataDTO userDetailedDataDTO = new UserDetailedDataDTO();
		List<User> userList = userRepository.findUserExceptAdmin();
		SimpleDateFormat sdf = new SimpleDateFormat("dd MMMM yyyy");
		if (!userList.isEmpty()) {
			userDetailedDataDTO.setTotalNoOfRegisterdUser(userList.size());
			userDetailedDataDTO.setRegisteredUser(userList.size());
			for (User user2 : userList) {
				String userDate = sdf.format(user2.getUpdatedDateTime());
				String todayDate = sdf.format(new Date());
				if(sdf.format(user2.getCreatedTimestamp()).equals(sdf.format(new Date()))){
					todayRegisteredUser++;
				}
				if (user2.getOnboardingStatus() != null) {
					if (user2.getOnboardingStatus().getOverallStatus() == 1) {
						registeredUserWithOnboarding++;
						if(userDate.equals(todayDate)){
							todayRegisteredUserWithOnboarding++;
						}
					}else{
						registeredUserWithoutOnboarding++;
					}
				}
				if(user2.getPanDetails() != null) {
					if(user2.getPanDetails().getVerified() != null) {
						if(user2.getPanDetails().getVerified().equals("verified")) {
							registeredUserWithKyc++;
							if(userDate.equals(todayDate)){
								todayRegisteredUserWithKyc++;
							}
						}else{
							registeredUserWithoutKyc++;
						}
					}
				}
			}
			userDetailedDataDTO.setUserListWithNoKyc(registeredUserWithoutKyc);
			userDetailedDataDTO.setUserListWithKycCompleted(registeredUserWithKyc);
			userDetailedDataDTO.setUserListWithNoOnboarding(registeredUserWithoutOnboarding);
			userDetailedDataDTO.setUserListWithOnboardingCompleted(registeredUserWithOnboarding);			
			userDetailedDataDTO.setRegisteredUserWithKyc(registeredUserWithKyc);
			userDetailedDataDTO.setRegisteredUserWithoutKyc(registeredUserWithoutKyc);
			userDetailedDataDTO.setRegisteredUserWithOnboarding(registeredUserWithOnboarding);
			userDetailedDataDTO.setRegisterUserWithoutOnboarding(registeredUserWithoutOnboarding);
			userDetailedDataDTO.setTodayRegisteredUser(todayRegisteredUser);
			userDetailedDataDTO.setTodayRegisteredUserWithKyc(todayRegisteredUserWithKyc);
			//userDetailedDataDTO.setTodayRegisteredUserWithoutKyc(todayRegisteredUserWithoutKyc);
			userDetailedDataDTO.setTodayRegisteredUserWithOnboarding(todayRegisteredUserWithOnboarding);
			//userDetailedDataDTO.setTodayRegisterUserWithoutOnboarding(todayRegisterUserWithoutOnboarding);
			noOfPurchasedOrder = getPurchasedOrdersList("Internal","All").size();
			noOfConfirmedOrder = getConfirmedOrdersList("Internal","All").size();
			noOfCanceledOrder = getCancledOrdersList("Internal","All").size();
			List<Orders> ordersList = orderRepository.findAll();
			Set<String> regId = new HashSet<>();
			if (!ordersList.isEmpty()) {
				for (Orders orders : ordersList) {
					if(orders.getType().equals("SIP")){
						if(orders.getField2() != null && !orders.getField2().equals("N")){
							if(orders.getStatus().equals("M") || orders.getStatus().equals("X") || orders.getStatus().equals("C") || orders.getStatus().equals("AD") || orders.getStatus().equals("AP") || orders.getStatus().equals("PA") || orders.getStatus().contentEquals("AF")){
								if(!regId.contains(orders.getField2()))
									regId.add(orders.getField2());
							}
						}
					}else{
						if(orders.getStatus().equals("M") || orders.getStatus().equals("X") || orders.getStatus().equals("C") || orders.getStatus().equals("AD") || orders.getStatus().equals("AP") || orders.getStatus().equals("PA")){
							noOfLumpsumOrders++;
						}
					}
					if(orders.getStatus().equals("PA") || orders.getStatus().equals("AP")){
						pendingOrders++;
					}
					if(sdf.format(orders.getTimeplaced()).equals(sdf.format(new Date()))){
						todayOrder++;
						if(orders.getStatus() != null){
							if(orders.getStatus().equals("M")){
								todayConfirmedOrder++;
							}
							if(orders.getStatus().equals("C") || orders.getStatus().equals("AD")){
								todayPurchasedOrder++;
							}
						}
					}
					if((sdf.format(orders.getLastupdate()).equals(sdf.format(new Date())))){
						if(orders.getStatus()!= null){
							if(orders.getStatus().equals("X")){
								todayCanceledOrder++;
							}
						}
					}
				}
				totalNoOfOrder = noOfPurchasedOrder + noOfConfirmedOrder + noOfCanceledOrder+pendingOrders;
				userDetailedDataDTO.setTotalNoOfOrder(totalNoOfOrder);
				userDetailedDataDTO.setNoOfCanceledOrder(noOfCanceledOrder);
				userDetailedDataDTO.setNoOfPurchasedOrder(noOfPurchasedOrder);
				userDetailedDataDTO.setNoOfConfirmedOrder(noOfConfirmedOrder);
				userDetailedDataDTO.setTotalOrderList(totalNoOfOrder);
				userDetailedDataDTO.setTotalOrderListWithConfirmedState(noOfConfirmedOrder);
				userDetailedDataDTO.setTotalOrderListWithPaymentDone(noOfPurchasedOrder);
				userDetailedDataDTO.setNoOfCanceledOrder(noOfCanceledOrder);
				userDetailedDataDTO.setTodayOrder(todayOrder);
				userDetailedDataDTO.setTodayCanceledOrder(todayCanceledOrder);
				userDetailedDataDTO.setTodayConfirmedOrder(todayConfirmedOrder);
				userDetailedDataDTO.setTodayPurchasedOrder(todayPurchasedOrder);
				userDetailedDataDTO.setPendingOrders(pendingOrders);
				userDetailedDataDTO.setNoOfLumpsum(noOfLumpsumOrders);
				userDetailedDataDTO.setNoOfSIP(regId.size());
			}
		}
		return userDetailedDataDTO;
	}

	@Override
	public String checkAuthorties(Integer userId) {
		String authorizedUser = null;
		User user = adminRepository.findUserByRole(userId, "ADMIN");
		if (user == null) {
			authorizedUser = "false";
		} else {
			authorizedUser = "true";
		}
		return authorizedUser;
	}

	@Override
	public boolean updateAmfi(List<FundSchemeDTO> fundSchemeDtoList) {
		boolean flag = false;
		Object responseObj = null;
		String isin = "";
		String rtaCode = "";
		String response = "";
		int count = 0;
		logger.info("In updateAmfi() method");
		try {
			for (FundSchemeDTO fundSchemeDTO : fundSchemeDtoList) {
				System.out.println("No. of record ======" + count++);
				if (!fundSchemeDTO.getIsin().equals("NULL")) {
					List<Scheme> scheme = schemeRepository.getSchemeListByIsin(fundSchemeDTO.getIsin());
					if (!scheme.isEmpty()) {
						for (Scheme scheme2 : scheme) {
							if (fundSchemeDTO.getAmfiCodes().equals("--")) {
							} else {
								scheme2.setAmfiCode(Integer.parseInt(fundSchemeDTO.getAmfiCodes()));
								schemeRepository.save(scheme2);
							}
						}
					} else {
						if (!fundSchemeDTO.getAmfiCodes().equals("--")) {
							responseObj = navService.getMFSchemeMaster(fundSchemeDTO.getAmfiCodes());
							JSONArray jsonArray = new JSONArray(responseObj.toString());
							JSONObject jsonObject = jsonArray.getJSONObject(0);
							JSONObject jsonObjectRef = jsonObject.getJSONObject("Result");
							response = jsonObjectRef.toString();
							if (!jsonObjectRef.getString("Plans").equals("Direct")) {
								Scheme scheme2 = new Scheme();
								scheme2.setSchemeName(jsonObjectRef.optString("Scheme_Name", null));
								scheme2.setSchemePlan(jsonObjectRef.optString("Plans", null));
								scheme2.setSchemeType(jsonObjectRef.optString("Category", null));
								scheme2.setFaceValue(String.valueOf(jsonObjectRef.getDouble("Face_Value")));
								scheme2.setBenchmarkCode(jsonObjectRef.optString("BENCHMARK_CODE", null));
								scheme2.setIsin(jsonObjectRef.optString("ISIN_Codes", null));
								scheme2.setRtaSchemeCode(jsonObjectRef.optString("RTA_CODE", null));
								scheme2.setAmfiCode(Integer.parseInt(jsonObjectRef.optString("AMFI_Code")));
								scheme2.setRtaAgentCode(jsonObjectRef.optString("Registrar_Agent", null));
								scheme2.setRisk(jsonObjectRef.optString("Risk_Level", null));
								if (jsonObjectRef.getString("Available_For_Investment").equals("YES")) {
									scheme2.setPurchaseAllowed("Y");
								}
								if (jsonObjectRef.getString("Available_For_Investment").equals("NO")) {
									scheme2.setPurchaseAllowed("N");
								}
								scheme2.setMinimumPurchaseAmount(jsonObjectRef.getInt("MIN_INVESTMENT"));
								scheme2.setAdditionalPurchaseAmount(String.valueOf(jsonObjectRef.getDouble("Additional_Investment")));
								scheme2.setSipFlag(jsonObjectRef.optString("IS_SIP_Allowed", null));
								scheme2.setSipMinimumInstallmentAmount(new BigDecimal(jsonObjectRef.getDouble("MIN_SIP_AMOUNT")));
								scheme2.setMinSipAmount(String.valueOf(jsonObjectRef.getDouble("MIN_SIP_AMOUNT")));
								scheme2.setSchemeCode(fundSchemeDTO.getSchemeCode());
								String schemeName = jsonObjectRef.optString("Scheme_Name", null);
								String schemeNameTemp = schemeName.replace(' ', '-');
								if (fundSchemeDTO.getSchemeCode().contains("-L1")) {
									schemeNameTemp += "-L1";
								}
								scheme2.setKeyword(schemeNameTemp);
								if (fundSchemeDTO.getSchemeCode().contains("L1")) {
									scheme2.setDisplay("1");
								} else {
									scheme2.setDisplay("0");
								}
								scheme2.setPriority("3");
								schemeRepository.save(scheme2);
							}
						}
					}
				} else {
					if (!fundSchemeDTO.getRtaCode().equals("NULL")) {
						List<Scheme> scheme = schemeRepository.getSchemeListByrtaCode(fundSchemeDTO.getRtaCode());
						if (!scheme.isEmpty()) {
							for (Scheme scheme2 : scheme) {
								if (fundSchemeDTO.getAmfiCodes().equals("--")) {
								} else {
									scheme2.setAmfiCode(Integer.parseInt(fundSchemeDTO.getAmfiCodes()));
									schemeRepository.save(scheme2);
								}
							}
						} else {
							if (!fundSchemeDTO.getAmfiCodes().equals("--")) {
								responseObj = navService.getMFSchemeMaster(fundSchemeDTO.getAmfiCodes());
								JSONArray jsonArray = new JSONArray(responseObj.toString());
								JSONObject jsonObject = jsonArray.getJSONObject(0);
								JSONObject jsonObjectRef = jsonObject.getJSONObject("Result");
								response = jsonObjectRef.toString();
								if (!jsonObjectRef.getString("Plans").equals("Direct")) {
									Scheme scheme2 = new Scheme();
									scheme2.setSchemeName(jsonObjectRef.optString("Scheme_Name", null));
									scheme2.setSchemePlan(jsonObjectRef.optString("Plans", null));
									scheme2.setSchemeType(jsonObjectRef.optString("Category", null));
									scheme2.setFaceValue(String.valueOf(jsonObjectRef.getDouble("Face_Value")));
									scheme2.setBenchmarkCode(jsonObjectRef.optString("BENCHMARK_CODE", null));
									scheme2.setIsin(jsonObjectRef.optString("ISIN_Codes", null));
									scheme2.setRtaSchemeCode(jsonObjectRef.optString("RTA_CODE", null));
									scheme2.setAmfiCode(Integer.parseInt(jsonObjectRef.optString("AMFI_Code")));
									scheme2.setRtaAgentCode(jsonObjectRef.optString("Registrar_Agent", null));
									scheme2.setRisk(jsonObjectRef.optString("Risk_Level", null));
									if (jsonObjectRef.getString("Available_For_Investment").equals("YES")) {
										scheme2.setPurchaseAllowed("Y");
									}
									if (jsonObjectRef.getString("Available_For_Investment").equals("NO")) {
										scheme2.setPurchaseAllowed("N");
									}
									scheme2.setMinimumPurchaseAmount(jsonObjectRef.getInt("MIN_INVESTMENT"));
									scheme2.setAdditionalPurchaseAmount(String.valueOf(jsonObjectRef.getDouble("Additional_Investment")));
									scheme2.setSipFlag(jsonObjectRef.optString("IS_SIP_Allowed", null));
									scheme2.setSipMinimumInstallmentAmount(new BigDecimal(jsonObjectRef.getDouble("MIN_SIP_AMOUNT")));
									scheme2.setMinSipAmount(String.valueOf(jsonObjectRef.getDouble("MIN_SIP_AMOUNT")));
									scheme2.setSchemeCode(fundSchemeDTO.getSchemeCode());
									String schemeName = jsonObjectRef.optString("Scheme_Name", null);
									String schemeNameTemp = schemeName.replace(' ', '-');
									if (fundSchemeDTO.getSchemeCode().contains("-L1")) {
										schemeNameTemp += "-L1";
									}
									scheme2.setKeyword(schemeNameTemp);
									if (fundSchemeDTO.getSchemeCode().contains("L1")) {
										scheme2.setDisplay("1");
									} else {
										scheme2.setDisplay("0");
									}
									scheme2.setPriority("3");
									schemeRepository.save(scheme2);
								}
							}
						}
					} else {
					}
				}
			}
			flag = true;
		} catch (Exception ex) {
			flag = false;
			ex.printStackTrace();
		}
		logger.info("Out updateAmfi() method");
		return flag;
	}

	@Override
	public boolean updateAmfiWithIsin(List<FundSchemeDTO> fundSchemeDtoList) {
		boolean flag = false;
		logger.info("In updateAmfiWithIsin() method");
		try {
			int count = 0;
			for (FundSchemeDTO fundSchemeDTO : fundSchemeDtoList) {
				if (fundSchemeDTO.getAmfiCodes() == null && fundSchemeDTO.getAmfiCodes().equals("")) {
					System.out.println("Amfi Blank : " + count++);
				} else {
					String amfiCode = fundSchemeDTO.getAmfiCodes();
					Object response = navService.getMFSchemeMaster(amfiCode);
					System.out.println("Response : " + response.toString());
					JSONArray jsonArray = new JSONArray(response.toString());
					JSONObject jsonObject = jsonArray.getJSONObject(0);
					JSONObject jsonObj = jsonObject.getJSONObject("Result");
					String isinCode = jsonObj.getString("ISIN_Codes");
					String sipAllowed = jsonObj.getString("IS_SIP_Allowed");
					UserEnquiry userEnquryObj = new UserEnquiry();
					userEnquryObj.setName(amfiCode);
					userEnquryObj.setEmail(isinCode);
					userEnquryObj.setQuery(jsonObject.optString("Risk_Level", null));
					userEnquryObj.setMobile(sipAllowed);
					userEnquryRepository.save(userEnquryObj);
				}
			}
			flag = true;
		} catch (Exception ex) {
			flag = false;
			ex.printStackTrace();
		}
		logger.info("Out updateAmfiWithIsin() method");
		return flag;
	}

	@Override
	public boolean uploadModelPortfolioData(List<FundSchemeDTO> fundSchemeDtoList) {
		boolean flag = false;
		logger.info("In uploadPortfolioData() method");
		try {
			boolean validFlag = isValid(fundSchemeDtoList);
			if(!validFlag)
				return false;
			List<PortfolioCategory> portfolioCategoryList = portfolioCategoryRepository.findAll();
			if (portfolioCategoryList.size() > 0) {
				for (FundSchemeDTO fundSchemeDTOObj : fundSchemeDtoList) {
					PortfolioCategory portfolioCategory = portfolioCategoryRepository.findByCategoryName(fundSchemeDTOObj.getPortfolioCategoryName());
					if (portfolioCategory != null) {
						List<Portfolio> portfolioList = portfolioRepository.findByPortfolioCategoryId(portfolioCategory.getPortfolioCategoryId());
						if (portfolioList.size() > 0) {
							for (Portfolio portfolioObjRef : portfolioList) {
								portfolioRepository.delete(portfolioObjRef);
							}
						}
					}
				}
				for (FundSchemeDTO fundSchemeDTO : fundSchemeDtoList) {
					PortfolioCategory portfolioCategory = portfolioCategoryRepository.findByCategoryName(fundSchemeDTO.getPortfolioCategoryName());
					if (portfolioCategory != null) {
						Portfolio portfolio = portfolioRepository.findBySchemeCodeAndPortfolioCategoryId(fundSchemeDTO.getSchemeCode(), portfolioCategory.getPortfolioCategoryId());
						if (portfolio != null) {
							portfolioCategory.setDescription(fundSchemeDTO.getDescription());
							portfolioCategory.setFeatures(fundSchemeDTO.getFeatures());
							portfolioCategory.setInvestmentGoals(fundSchemeDTO.getInvestmentGoals());
							portfolioCategoryRepository.save(portfolioCategory);
						} else {
							Portfolio portfolioObj = new Portfolio();
							portfolioObj.setPortfolioCategoryId(portfolioCategory.getPortfolioCategoryId());
							portfolioObj.setSchemeCode(fundSchemeDTO.getSchemeCode());
							portfolioObj.setSchemeName(fundSchemeDTO.getSchemeName());
							portfolioObj.setStatus("Active");
							portfolioObj.setUpdateDate(new Date());
							portfolioRepository.save(portfolioObj);
						}
					} else {
						addModelPortfolio(fundSchemeDTO);
					}
				}
			} else {
				for (FundSchemeDTO fundSchemeDTO : fundSchemeDtoList) {
					addModelPortfolio(fundSchemeDTO);
				}
			}
			flag = true;
		} catch (Exception ex) {
			flag = false;
			ex.printStackTrace();
		}
		logger.info("Out uploadPortfolioData() method");
		return flag;
	}

	public void addModelPortfolio(FundSchemeDTO fundSchemeDTO) {
		PortfolioCategory portfolioCategoryObj = portfolioCategoryRepository.findByCategoryName(fundSchemeDTO.getPortfolioCategoryName());
		if (portfolioCategoryObj != null) {
			Portfolio portfolio = new Portfolio();
			portfolio.setPortfolioCategoryId(portfolioCategoryObj.getPortfolioCategoryId());
			portfolio.setSchemeCode(fundSchemeDTO.getSchemeCode());
			portfolio.setSchemeName(fundSchemeDTO.getSchemeName());
			portfolio.setStatus("Active");
			portfolio.setUpdateDate(new Date());
			portfolioRepository.save(portfolio);
		} else {
			PortfolioCategory portfolioCategory = new PortfolioCategory();
			portfolioCategory.setCategoryName(fundSchemeDTO.getPortfolioCategoryName());
			portfolioCategory.setDescription(fundSchemeDTO.getDescription());
			portfolioCategory.setFeatures(fundSchemeDTO.getFeatures());
			portfolioCategory.setInvestmentGoals(fundSchemeDTO.getInvestmentGoals());
			String tempCategoryKeyword = fundSchemeDTO.getPortfolioCategoryName().replace(' ', '-');
			portfolioCategory.setCategoryKeyword(tempCategoryKeyword.replace('|', '-'));
			portfolioCategory = portfolioCategoryRepository.save(portfolioCategory);
			Portfolio portfolio = new Portfolio();
			portfolio.setPortfolioCategoryId(portfolioCategory.getPortfolioCategoryId());
			portfolio.setSchemeCode(fundSchemeDTO.getSchemeCode());
			portfolio.setSchemeName(fundSchemeDTO.getSchemeName());
			portfolio.setStatus("Active");
			portfolio.setUpdateDate(new Date());
			portfolioRepository.save(portfolio);
		}
	}

	@Override
	public UserLoginResponseDTO getUser(Integer userId) {
		User user = userRepository.getOne(userId);
		UserLoginResponseDTO userLoginResponseDTO = new UserLoginResponseDTO();
		try {
			if (user != null) {
				userLoginResponseDTO.setUserName(user.getFirstName() + " " + user.getLastName());
				userLoginResponseDTO.setEmail(user.getEmail());
				userLoginResponseDTO.setMobile(user.getMobileNumber());
				userLoginResponseDTO.setRegisterType(user.getRegisterType());
				userLoginResponseDTO.setStatus(user.getOnboardingStatus().getOverallStatus());
				userLoginResponseDTO.setFullName(user.getFirstName() + " " + user.getLastName());
				userLoginResponseDTO.setMessage(GoForWealthUMAConstants.SUCCESS);
			} else {
				userLoginResponseDTO.setMessage(GoForWealthUMAConstants.USER_NOT_EXIST);
			}
		} catch (Exception ex) {
			userLoginResponseDTO.setMessage(GoForWealthUMAConstants.USER_NOT_EXIST);
		}
		return userLoginResponseDTO;
	}

	@Override
	public String updateKycStatus(int userId) {
		String emailSubject = "";
		String emailContent = "";
		String response = "Failure";
		User user = userRepository.getOne(userId);
		if (user != null) {
			OnboardingStatus onboardingStatus = user.getOnboardingStatus();
			onboardingStatus.setKycStatus(1);
			onboardingStatus.setKycResponse("Updated By Admin");
			user.setOnboardingStatus(onboardingStatus);
			PanDetails panDetails = user.getPanDetails();
			panDetails.setVerified("verified");
			user.setPanDetails(panDetails);
			userRepository.save(user);
			String userName = user.getFirstName() == null ? user.getUsername() : user.getFirstName() + " " + user.getLastName();
			emailSubject = messageSource.getMessage("kyc.status.mailSubject", null, Locale.ENGLISH);
			emailContent = messageSource.getMessage("kyc.status.mailBody", new String[] { userName }, Locale.ENGLISH);
			try {
				mailUtility.baselineExample(user.getEmail(), emailSubject, emailContent);
			} catch (IOException e) {
				e.printStackTrace();
			}
			response = "success";
		}
		return response;
	}

	@Override
	public String updateGoalSequence(List<SchemeUploadRequest> goalDtoList) {
		String response = "";
		try {
			for (SchemeUploadRequest goalUploadSequenceRequest : goalDtoList) {
				Goals goals = goalsRepository.getGoal(goalUploadSequenceRequest.getGoalName());
				if (goals != null) {
					goals.setShowToProfileType(Integer.parseInt(goalUploadSequenceRequest.getSequence()));
					goals.setCostOfGoal(new BigDecimal(goalUploadSequenceRequest.getGoalAmount()));
					goals.setDuration(Integer.parseInt(goalUploadSequenceRequest.getGoalDuration()));
					int duration = Integer.parseInt(goalUploadSequenceRequest.getGoalDuration());
					if (duration >0 && duration <=3) {
						Optional<GoalBucket> goalBucketOptional = goalBucketRepository.findByGoalBucketCode(GoalBucketCodeEnum.SHORT_TERM.getCode());
						if (goalBucketOptional.isPresent()){
						    GoalBucket goalBucket = goalBucketOptional.get();
						    goals.setGoalBucket(goalBucket);
						}
					}else if(duration >3 && duration <=7){
						Optional<GoalBucket> goalBucketOptional = goalBucketRepository.findByGoalBucketCode(GoalBucketCodeEnum.MEDIUM_TERM.getCode());
						if (goalBucketOptional.isPresent()){
						    GoalBucket goalBucket = goalBucketOptional.get();
						    goals.setGoalBucket(goalBucket);
						}
					}else{
						Optional<GoalBucket> goalBucketOptional = goalBucketRepository.findByGoalBucketCode(GoalBucketCodeEnum.LONG_TERM.getCode());
						if (goalBucketOptional.isPresent()){
						    GoalBucket goalBucket = goalBucketOptional.get();
						    goals.setGoalBucket(goalBucket);
						}
					}
					goalsRepository.save(goals);
					response = "success";
				}
			}
		} catch (Exception ex) {
			ex.printStackTrace();
			return response = "failed";
		}
		return response;
	}

	@Override
	public List<SchemeKeywordWithSeoResponse> getSchemeKeywordForSeo() {
		List<Scheme> schemeList = schemeRepository.getAllSchemesKeyword();
		List<SchemeKeywordWithSeoResponse> keywordWithSeoResponsesList = new ArrayList<SchemeKeywordWithSeoResponse>();
		if(!schemeList.isEmpty()){
			for (Scheme scheme : schemeList) {
				SchemeKeywordWithSeoResponse schemeKeywordWithSeoResponse = new SchemeKeywordWithSeoResponse();
				if(scheme.getKeyword() != null){
					schemeKeywordWithSeoResponse.setSchemeKeyword(scheme.getKeyword());
					schemeKeywordWithSeoResponse.setSchemeCode(scheme.getSchemeCode());
					schemeKeywordWithSeoResponse.setType(scheme.getSchemeCategory());
					schemeKeywordWithSeoResponse.setIsin(scheme.getIsin());
					keywordWithSeoResponsesList.add(schemeKeywordWithSeoResponse);
				}
			}
		}
		return keywordWithSeoResponsesList;
	}

	@Override
	public List<UserEnquiryDto> getUserEnquiry() {
		List<UserEnquiryDto> userEnquiryDtoList = new ArrayList<UserEnquiryDto>();
		List<UserEnquiry> userEnquiryList = userEnquiryRepository.findAll();
		if(userEnquiryList.size()>0){
			for (UserEnquiry userEnquiry : userEnquiryList) {
				UserEnquiryDto userEnquiryDto = new UserEnquiryDto();
				userEnquiryDto.setEnquiryId(userEnquiry.getUserEnquiryId());
				userEnquiryDto.setEmail(userEnquiry.getEmail());
				userEnquiryDto.setMobile(userEnquiry.getMobile());
				userEnquiryDto.setName(userEnquiry.getName());
				userEnquiryDto.setQuery(userEnquiry.getQuery());
				userEnquiryDto.setStatus(userEnquiry.getStatus());
				userEnquiryDtoList.add(userEnquiryDto);
			}
		}
		return userEnquiryDtoList;
	}
	
	@Override
	public boolean updateEnquiryStatus(UserEnquiryDto userEnquiryDto) {
		UserEnquiry userEnquiry = userEnquiryRepository.getOne(userEnquiryDto.getEnquiryId());
		if(userEnquiry != null){
			userEnquiry.setStatus(userEnquiryDto.getStatus());
			userEnquiryRepository.save(userEnquiry);
			return true;
		}else{
			return false;
		}
	}

	@Override
	public boolean deleteUserEnquiry(Integer userEnquiryId) {
		UserEnquiry userEnquiry = userEnquiryRepository.getOne(userEnquiryId);
		if(userEnquiry != null){
			userEnquiryRepository.delete(userEnquiry);
			return true;
		}else{
			return false;
		}
	}

	@Override
	public List<OrderExportExcelDto> getConfirmedOrdersList(String type,String subType) {
		List<OrderExportExcelDto> orderExportExcelList = new ArrayList<>();
		List<Orders> ordersList = orderRepository.findByStatus("M");
		SimpleDateFormat sdf = new SimpleDateFormat("dd MMMM yyyy");
		int noOfSIP = 0;
		int noOfLumpsum = 0;
		if(!ordersList.isEmpty()) {
			for (Orders orders : ordersList) {
				if(orders.getStatus().equals("M")){
					if(orders.getType().equals("SIP")){
						boolean flag = true;
						if(orders.getField2() != null && !orders.getField2().equals("N")){
							List<Orders> ordersLs = orderRepository.findByField2(orders.getField2());
							for (Orders orders2 : ordersLs) {
								if(orders2.getStatus().equals("AD") || orders2.getStatus().equals("C")){
									flag = false;
									break;
								}
							}
							if(flag){
								if(type.equals("Internal")){
									orderExportExcelList.add(new OrderExportExcelDto());
								}else{
									if(subType.equals("All")){
										noOfSIP++;
										OrderExportExcelDto orderExportExcelDto = createOrderExportExcelDto(orders,0,orders.getType(),"Confirmed");
										orderExportExcelList.add(orderExportExcelDto);
										orderExportExcelDto = null;
									}else{
										if(sdf.format(orders.getTimeplaced()).equals(sdf.format(new Date()))){
											if(subType.equals("Today")){
												OrderExportExcelDto orderExportExcelDto = createOrderExportExcelDto(orders,0,orders.getType(),"Confirmed");
												orderExportExcelList.add(orderExportExcelDto);
												orderExportExcelDto = null;
											}
										}
									}
								}
							}
						}
					}else{
						if(type.equals("Internal")){
							orderExportExcelList.add(new OrderExportExcelDto());
						}else{
							if(subType.equals("All")){
								noOfLumpsum++;
								OrderExportExcelDto orderExportExcelDto = createOrderExportExcelDto(orders,0,orders.getType(),"Confirmed");
								orderExportExcelList.add(orderExportExcelDto);
								orderExportExcelDto = null;
							}else{
								if(sdf.format(orders.getTimeplaced()).equals(sdf.format(new Date()))){
									if(subType.equals("Today")){
										OrderExportExcelDto orderExportExcelDto = createOrderExportExcelDto(orders,0,orders.getType(),"Confirmed");
										orderExportExcelList.add(orderExportExcelDto);
										orderExportExcelDto = null;
									}
								}
							}
						}
					}
				}
			}
		}
		return orderExportExcelList;
	}
	
	private OrderExportExcelDto createOrderExportExcelDto(Orders orders,int amount,String type,String subType){
		OrderExportExcelDto orderExportExcelDto = new OrderExportExcelDto();
		SimpleDateFormat sdf = new SimpleDateFormat("dd MMMM yyyy");
		Scheme schemeObj = schemeRepository.getOne(orders.getOrderItems().iterator().next().getScheme().getSchemeId());
		orderExportExcelDto.setOrderNo(orders.getOrdersId().toString());
		orderExportExcelDto.setOrderDate(sdf.format(orders.getTimeplaced()));
		orderExportExcelDto.setSchemeName(schemeObj.getSchemeName());
		orderExportExcelDto.setSchemeCode(schemeObj.getSchemeCode());
		orderExportExcelDto.setType(orders.getType());
		if(subType.equals("Confirmed")){
			orderExportExcelDto.setStatus("Confirmed/Payment not received");
			orderExportExcelDto.setAmount(String.valueOf(orders.getOrderItems().iterator().next().getProductprice().intValue()));
		}else if(subType.equals("Purchased")){
			if(amount==0){
				orderExportExcelDto.setAmount(String.valueOf(orders.getOrderItems().iterator().next().getProductprice().intValue()));
			}else{
				orderExportExcelDto.setAmount(String.valueOf(amount));
			}
			orderExportExcelDto.setStatus("Purchased/Payment received");
		}else if(subType.equals("Canceled")){
			orderExportExcelDto.setStatus("Canceled");
			if(amount ==0){
				orderExportExcelDto.setAmount(String.valueOf(orders.getOrderItems().iterator().next().getProductprice().intValue()));
			}else{
				orderExportExcelDto.setAmount(String.valueOf(amount));
			}
		}else{
			orderExportExcelDto.setStatus("Payment Awaiting");
			orderExportExcelDto.setAmount(String.valueOf(orders.getOrderItems().iterator().next().getProductprice().intValue()));
		}
		orderExportExcelDto.setAddressLine1(orders.getUser().getAddressProofs().iterator().next().getField2());
		orderExportExcelDto.setAddressLine2(orders.getUser().getAddressProofs().iterator().next().getField3());
		orderExportExcelDto.setCity(orders.getUser().getAddressProofs().iterator().next().getCity());
		orderExportExcelDto.setEmail(orders.getUser().getEmail());
		orderExportExcelDto.setFatherName(orders.getUser().getKycDetails().getFatherName());
		orderExportExcelDto.setInvesterId(orders.getUser().getOnboardingStatus().getClientCode());
		orderExportExcelDto.setInvesterName(orders.getUser().getFirstName()+ " "+orders.getUser().getLastName());
		orderExportExcelDto.setMaritalStatus(orders.getUser().getKycDetails().getMaritalStatus());
		orderExportExcelDto.setMobileNo(orders.getUser().getMobileNumber());
		orderExportExcelDto.setMotherName(orders.getUser().getKycDetails().getMotherName());
		orderExportExcelDto.setNomineeName(orders.getUser().getBankDetails().getNomineeName());
		orderExportExcelDto.setNomineeRelation(orders.getUser().getBankDetails().getNomineeRelation());
		orderExportExcelDto.setPincode(orders.getUser().getAddressProofs().iterator().next().getPincode());
		orderExportExcelDto.setState(orders.getUser().getAddressProofs().iterator().next().getState());
		orderExportExcelDto.setIfscCode(orders.getUser().getBankDetails().getIfsc().toUpperCase());
		orderExportExcelDto.setAccountType(orders.getUser().getBankDetails().getAccountType());
		orderExportExcelDto.setBankBranch(orders.getUser().getBankDetails().getBankBranch());
		orderExportExcelDto.setBankName(orders.getUser().getBankDetails().getBankName());
		String decryptBankAccountNo = "";
		try {
			decryptBankAccountNo = encryptUserDetail.decrypt(orders.getUser().getBankDetails().getAccountNo());
		} catch (Exception ex) {
			ex.printStackTrace();
		}
		orderExportExcelDto.setAccountNumber(decryptBankAccountNo);
		String decreptedPanNoRef = "";
		try {
			decreptedPanNoRef = encryptUserDetail.decrypt(orders.getUser().getPanDetails().getPanNo());
		}catch(Exception ex) {
			ex.printStackTrace();
		}
		orderExportExcelDto.setPan(decreptedPanNoRef.toUpperCase());
		String occupation = orders.getUser().getPanDetails().getOccupation();
		if(occupation != null){
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
		}
		orderExportExcelDto.setOccupation(occupation);
		String addressType = orders.getUser().getAddressProofs().iterator().next().getAddressType();
		if(addressType != null){
			if(addressType.equals("2")){
				orderExportExcelDto.setAddressType("Residential");
			}else if(addressType.equals("3")){
				orderExportExcelDto.setAddressType("Business");
			}else{
				orderExportExcelDto.setAddressType("Others");
			}
		}
		String incomeSlab = orders.getUser().getAddressProofs().iterator().next().getIncomeSlab();
		if(incomeSlab != null){
			if(incomeSlab.equals("31")){
				orderExportExcelDto.setIncomeSlab("Below 1 Lakh");
			}else if(incomeSlab.equals("32")){
				orderExportExcelDto.setIncomeSlab("1-5 Lacs");
			}else if(incomeSlab.equals("33")){
				orderExportExcelDto.setIncomeSlab("5-10 Lacs");
			}else if(incomeSlab.equals("34")){
				orderExportExcelDto.setIncomeSlab("10-25 Lacs");
			}else if(incomeSlab.equals("35")){
				orderExportExcelDto.setIncomeSlab("25 Lacs-1 Crore");
			}else{
				orderExportExcelDto.setIncomeSlab("Above 1 Crore");
			}
		}
		return orderExportExcelDto;
	}

	@Override
	public List<OrderExportExcelDto> getPurchasedOrdersList(String type,String subType) {
		List<OrderExportExcelDto> orderExportExcelList = new ArrayList<>();
		List<Orders> ordersList = orderRepository.findByStatus();
		List<String> sipId = new ArrayList<>();
		int noOfSIP = 0;
		int noOfLumpsum = 0;
		SimpleDateFormat sdf = new SimpleDateFormat("dd MMMM yyyy");
		if(!ordersList.isEmpty()) {
			for (Orders orders : ordersList) {
				if(orders.getStatus().equals("C") || orders.getStatus().equals("AD")){
					if(orders.getType().equals("SIP")){
						boolean flag = false;
						if(orders.getField2() != null || !orders.getField2().equals("N")){
							List<Orders> ordersLs = orderRepository.findByField2(orders.getField2());
							int amount = 0;
							for(Orders orders2 : ordersLs) {
								if(orders2.getStatus().equals("AD") || orders2.getStatus().equals("C")){
									amount += orders2.getOrderItems().iterator().next().getProductprice().intValue();
									flag = true;
								}
							}
							if(flag){
								boolean check = false;
								if(sipId.size()>0){
									if(sipId.contains(orders.getField2())){
										check = false;
									}else{
										check = true;
										sipId.add(orders.getField2());
									}
								}else{
									check = true;
									sipId.add(orders.getField2());
								}
								if(check){
									if(type.equals("Internal")){
										orderExportExcelList.add(new OrderExportExcelDto());
									}else{
										if(subType.equals("All")){
											noOfSIP++;
											OrderExportExcelDto orderExportExcelDto = createOrderExportExcelDto(orders,amount,orders.getType(),"Purchased");
											orderExportExcelList.add(orderExportExcelDto);
											orderExportExcelDto = null;
										}else{
											if(sdf.format(orders.getTimeplaced()).equals(sdf.format(new Date()))){
												OrderExportExcelDto orderExportExcelDto = createOrderExportExcelDto(orders,amount,orders.getType(),"Purchased");
												orderExportExcelList.add(orderExportExcelDto);
												orderExportExcelDto = null;
											}
										}
									}
								}
							}
						}
					}else{
						if(type.equals("Internal")){
							orderExportExcelList.add(new OrderExportExcelDto());
						}else{
							if(subType.equals("All")){
								noOfLumpsum++;
								OrderExportExcelDto orderExportExcelDto = createOrderExportExcelDto(orders,0,orders.getType(),"Purchased");
								orderExportExcelList.add(orderExportExcelDto);
								orderExportExcelDto = null;
							}else{
								if(sdf.format(orders.getTimeplaced()).equals(sdf.format(new Date()))){
									OrderExportExcelDto orderExportExcelDto = createOrderExportExcelDto(orders,0,orders.getType(),"Purchased");
									orderExportExcelList.add(orderExportExcelDto);
									orderExportExcelDto = null;
								}
							}
						}
					}
				}
			}
		}
		return orderExportExcelList;
	}

	@Override
	public List<OrderExportExcelDto> getCancledOrdersList(String type,String subType) {
		List<OrderExportExcelDto> orderExportExcelList = new ArrayList<>();
		List<Orders> ordersList = orderRepository.findByStatus("X");
		List<String> sipId = new ArrayList<>();
		int noOfSIP = 0;
		int noOfLumpsum = 0;
		SimpleDateFormat sdf = new SimpleDateFormat("dd MMMM yyyy");
		if(!ordersList.isEmpty()) {
			for (Orders orders : ordersList) {
				if(orders.getStatus().equals("X")){
					if(orders.getType().equals("SIP")){
						boolean flag = false;
						if(orders.getField2() != null && !orders.getField2().equals("N")){
							List<Orders> ordersLs = orderRepository.findByField2(orders.getField2());
							int amount = 0;
							for(Orders orders2 : ordersLs) {
								if(orders2.getStatus().equals("X")){
									amount += orders2.getOrderItems().iterator().next().getProductprice().intValue();
									flag = true;
								}
							}
							if(flag){
								boolean check = false;
								if(sipId.size()>0){
									if(sipId.contains(orders.getField2())){
										check = false;
									}else{
										check = true;
										sipId.add(orders.getField2());
									}
								}else{
									check = true;
									sipId.add(orders.getField2());
								}
								if(check){
									if(type.equals("Internal")){
										orderExportExcelList.add(new OrderExportExcelDto());
									}else{
										if(subType.equals("All")){
											noOfSIP++;
											OrderExportExcelDto orderExportExcelDto = createOrderExportExcelDto(orders,amount,orders.getType(),"Canceled");
											orderExportExcelList.add(orderExportExcelDto);
											orderExportExcelDto = null;
										}else{
											if(sdf.format(orders.getLastupdate()).equals(sdf.format(new Date()))){
												if(subType.equals("Today")){
													OrderExportExcelDto orderExportExcelDto = createOrderExportExcelDto(orders,amount,orders.getType(),"Canceled");
													orderExportExcelList.add(orderExportExcelDto);
													orderExportExcelDto = null;
												}
											}
										}
									}
								}
							}
						}
					}else{
						if(type.equals("Internal")){
							orderExportExcelList.add(new OrderExportExcelDto());
						}else{
							if(subType.equals("All")){
								noOfLumpsum++;
								OrderExportExcelDto orderExportExcelDto = createOrderExportExcelDto(orders,0,orders.getType(),"Canceled");
								orderExportExcelList.add(orderExportExcelDto);
								orderExportExcelDto = null;
							}else{
								if(sdf.format(orders.getLastupdate()).equals(sdf.format(new Date()))){
									if(subType.equals("Today")){
										OrderExportExcelDto orderExportExcelDto = createOrderExportExcelDto(orders,0,orders.getType(),"Canceled");
										orderExportExcelList.add(orderExportExcelDto);
										orderExportExcelDto = null;
									}
								}
							}
						}
					}
				}
			}
		}
		return orderExportExcelList;
	}
	
	@Override
	public List<OrderExportExcelDto> getPendingOrdersList(String type,String subType) {
		List<OrderExportExcelDto> orderExportExcelList = new ArrayList<>();
		List<Orders> ordersList = orderRepository.findAll();
		if(!ordersList.isEmpty()) {
			for (Orders orders : ordersList) {
				if(orders.getStatus().equals("AP") || orders.getStatus().equals("PA")){
					OrderExportExcelDto orderExportExcelDto = createOrderExportExcelDto(orders,0,orders.getType(),"Pending");
					orderExportExcelList.add(orderExportExcelDto);
					orderExportExcelDto = null;
				}
			}
		}
		return orderExportExcelList;
	}
	
	private boolean isValid(List<FundSchemeDTO> fundSchemeDtoList){
		boolean flag = false;
		for (FundSchemeDTO fundSchemeDTO : fundSchemeDtoList) {
			Scheme scheme = schemeRepository.findBySchemeCode(fundSchemeDTO.getSchemeCode());
			if(scheme != null && scheme.getPurchaseAllowed() != null && scheme.getSipFlag() != null){
				if(scheme.getPurchaseAllowed().equals("Y") && scheme.getSipFlag().equals("Y")){
					flag = true;
				}else{
					return flag = false;
				}
			}
		}
		return flag;
	}



}