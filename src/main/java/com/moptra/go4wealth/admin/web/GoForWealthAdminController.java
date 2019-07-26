package com.moptra.go4wealth.admin.web;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.file.Files;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.ListIterator;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.codec.binary.Base64;
import org.apache.poi.EncryptedDocumentException;
import org.apache.poi.openxml4j.exceptions.InvalidFormatException;
import org.apache.poi.ss.usermodel.Workbook;
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

import com.fasterxml.jackson.databind.ObjectMapper;
import com.moptra.go4wealth.admin.common.enums.GoForWealthAdminErrorMessageEnum;
import com.moptra.go4wealth.admin.common.exception.GoForWealthAdminException;
import com.moptra.go4wealth.admin.common.rest.GoForWealthAdminResponseInfo;
import com.moptra.go4wealth.admin.common.util.GoForWealthAdminUtil;
import com.moptra.go4wealth.admin.model.AdminBlogRequest;
import com.moptra.go4wealth.admin.model.BlogCategoryDTO;
import com.moptra.go4wealth.admin.model.BlogDTO;
import com.moptra.go4wealth.admin.model.CategoryListResponseDTO;
import com.moptra.go4wealth.admin.model.ContactUsRequestDTO;
import com.moptra.go4wealth.admin.model.NavUploadRequest;
import com.moptra.go4wealth.admin.model.OrderExportExcelDto;
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
import com.moptra.go4wealth.admin.service.GoForWealthAdminService;
import com.moptra.go4wealth.bean.NavMaster;
import com.moptra.go4wealth.bean.Seo;
import com.moptra.go4wealth.bean.StoreConf;
import com.moptra.go4wealth.bean.TopSchemes;
import com.moptra.go4wealth.bean.User;
import com.moptra.go4wealth.configuration.JwtConfiguration;
import com.moptra.go4wealth.prs.common.constant.GoForWealthPRSConstants;
import com.moptra.go4wealth.prs.common.enums.GoForWealthPRSErrorMessageEnum;
import com.moptra.go4wealth.prs.common.exception.GoForWealthPRSException;
import com.moptra.go4wealth.prs.common.rest.GoForWealthPRSResponseInfo;
import com.moptra.go4wealth.prs.common.util.GoForWealthPRSUtil;
import com.moptra.go4wealth.prs.model.AddToCartDTO;
import com.moptra.go4wealth.prs.model.FundSchemeDTO;
import com.moptra.go4wealth.prs.model.OrdersDTO;
import com.moptra.go4wealth.prs.orderapi.request.PortFolioDataDTO;
import com.moptra.go4wealth.prs.service.GoForWealthFundSchemeService;
import com.moptra.go4wealth.prs.service.GoForWealthPRSEmandateService;
import com.moptra.go4wealth.repository.StoreConfRepository;
import com.moptra.go4wealth.repository.UserRepository;
import com.moptra.go4wealth.security.UserPrincipal;
import com.moptra.go4wealth.security.UserSession;
import com.moptra.go4wealth.sip.common.enums.GoForWealthSIPErrorMessageEnum;
import com.moptra.go4wealth.sip.common.exception.GoForWealthSIPException;
import com.moptra.go4wealth.ticob.common.enums.GoForWealthTICOBErrorMessageEnum;
import com.moptra.go4wealth.ticob.common.rest.GoForWealthTICOBResponseInfo;
import com.moptra.go4wealth.ticob.common.util.GoForWealthTICOBUtil;
import com.moptra.go4wealth.ticob.model.TransferInMasterDTO;
import com.moptra.go4wealth.ticob.model.TransferInRequestDTO;
import com.moptra.go4wealth.ticob.service.GoForWealthTicobService;
import com.moptra.go4wealth.uma.common.constant.GoForWealthUMAConstants;
import com.moptra.go4wealth.uma.common.enums.GoForWealthErrorMessageEnum;
import com.moptra.go4wealth.uma.model.UserLoginResponseDTO;
import com.moptra.go4wealth.util.WebUtils;

import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.MalformedJwtException;
import io.jsonwebtoken.SignatureException;

@RestController
@RequestMapping("${server.contextPath}/admin")
public class GoForWealthAdminController {

	private static Logger logger = LoggerFactory.getLogger(GoForWealthAdminController.class);

	@Autowired
	GoForWealthAdminService goForWealthAdminService;

	@Autowired
	GoForWealthFundSchemeService goForWealthFundSchemeService;

	@Autowired
	UserRepository userRepository;

	@Autowired
	GoForWealthPRSEmandateService goForWealthPRSEmandateService;

	@Autowired
	StoreConfRepository storeConfRepository;

	@Autowired
	JwtConfiguration jwtConfiguration;
	
	@Autowired
	GoForWealthTicobService goForWealthTicobService;

	@PostMapping("/saveBlog")
	public GoForWealthAdminResponseInfo saveBlog(@RequestBody AdminBlogRequest adminBlogRequest,Authentication authentication) throws GoForWealthAdminException {
		logger.info("In saveBlog()");
		GoForWealthAdminResponseInfo responseInfo=null;
		if(authentication != null){
			UserPrincipal usersession = (UserPrincipal) authentication.getPrincipal();
			if(usersession != null){
				User user = userRepository.findUserByRole(usersession.getUser().getUserId(), "ADMIN");
				if(user != null){
					goForWealthAdminService.saveBlog(adminBlogRequest,usersession.getUser().getUserId());
					List<BlogDTO> blogList=goForWealthAdminService.getAllBlogs(usersession.getUser().getUserId());
					if(blogList != null && blogList.size() > 0) {
						responseInfo = GoForWealthAdminUtil.getSuccessResponseInfo(GoForWealthAdminErrorMessageEnum.SUCCESS_CODE.getValue(), GoForWealthAdminErrorMessageEnum.SUCCESS_MESSAGE.getValue(), blogList);
						responseInfo.setTotalRecords(blogList.size());
					} else {
						responseInfo = new GoForWealthAdminResponseInfo();
						responseInfo.setStatus(GoForWealthAdminErrorMessageEnum.DATA_NOT_FOUND_CODE.getValue());
						responseInfo.setMessage(GoForWealthAdminErrorMessageEnum.DATA_NOT_FOUND_MESSAGE.getValue());
					}
				}else{
					responseInfo = GoForWealthAdminUtil.getSuccessResponseInfo(GoForWealthErrorMessageEnum.ACCESS_DENIED_CODE.getValue(),GoForWealthErrorMessageEnum.ACCESS_DENIED_MESSAGE.getValue(),null);
				}
			}else{
				responseInfo = GoForWealthAdminUtil.getSuccessResponseInfo(GoForWealthErrorMessageEnum.ACCESS_DENIED_CODE.getValue(),GoForWealthErrorMessageEnum.ACCESS_DENIED_MESSAGE.getValue(),null);
			}
		}else{
			responseInfo = GoForWealthAdminUtil.getSuccessResponseInfo(GoForWealthErrorMessageEnum.ACCESS_DENIED_CODE.getValue(),GoForWealthErrorMessageEnum.ACCESS_DENIED_MESSAGE.getValue(),null);
		}
		logger.info("Out saveBlog()");
		return responseInfo;
	}

	@GetMapping("/getAllBlogs")
    public GoForWealthAdminResponseInfo getAllBlogs(Authentication authentication) throws GoForWealthAdminException  {
		logger.info("In getAllBlogs()");
		GoForWealthAdminResponseInfo responseInfo=null;
		if(authentication != null){
			UserPrincipal usersession = (UserPrincipal) authentication.getPrincipal();
			if(usersession != null){
				User user = userRepository.findUserByRole(usersession.getUser().getUserId(), "ADMIN");
				if(user != null){
					List<BlogDTO> blogList=goForWealthAdminService.getAllBlogs(usersession.getUser().getUserId());
					if(blogList != null && blogList.size() > 0) {
						responseInfo = GoForWealthAdminUtil.getSuccessResponseInfo(GoForWealthAdminErrorMessageEnum.SUCCESS_CODE.getValue(), GoForWealthAdminErrorMessageEnum.SUCCESS_MESSAGE.getValue(), blogList);
						responseInfo.setTotalRecords(blogList.size());
					} else {
						responseInfo = new GoForWealthAdminResponseInfo();
						responseInfo.setStatus(GoForWealthAdminErrorMessageEnum.DATA_NOT_FOUND_CODE.getValue());
						responseInfo.setMessage(GoForWealthAdminErrorMessageEnum.DATA_NOT_FOUND_MESSAGE.getValue());
					}
				}else{
					responseInfo = GoForWealthAdminUtil.getSuccessResponseInfo(GoForWealthErrorMessageEnum.ACCESS_DENIED_CODE.getValue(),GoForWealthErrorMessageEnum.ACCESS_DENIED_MESSAGE.getValue(),null);
				}
			}else{
				responseInfo = GoForWealthAdminUtil.getSuccessResponseInfo(GoForWealthErrorMessageEnum.ACCESS_DENIED_CODE.getValue(),GoForWealthErrorMessageEnum.ACCESS_DENIED_MESSAGE.getValue(), null);
			}
		}else{
			responseInfo = GoForWealthAdminUtil.getSuccessResponseInfo(GoForWealthErrorMessageEnum.ACCESS_DENIED_CODE.getValue(),GoForWealthErrorMessageEnum.ACCESS_DENIED_MESSAGE.getValue(), null);
		}
		logger.info("Out getAllBlogs()");
		return responseInfo;
	}

	@GetMapping("/getImageById/{blogId}")
	public void getImageById(@PathVariable("blogId")int blogId,HttpServletResponse response) throws IOException, GoForWealthAdminException {
		goForWealthAdminService.getImageById(blogId,response);
	}
	
	@GetMapping("/getBlogById/{blogId}")
	public GoForWealthAdminResponseInfo getBlogById(@PathVariable("blogId") int blogId,HttpServletResponse response,Authentication authentication) throws IOException, GoForWealthAdminException {
		logger.info("In getBlogById()");
		GoForWealthAdminResponseInfo responseInfo= null;
		if(authentication != null){
			UserPrincipal usersession = (UserPrincipal) authentication.getPrincipal();
			if(usersession != null){
				User user = userRepository.findUserByRole(usersession.getUser().getUserId(), "ADMIN");
				if(user != null){
					BlogDTO blog=goForWealthAdminService.getBlogById(blogId,response,usersession.getUser().getUserId());
					if(blog != null ) {
						responseInfo = GoForWealthAdminUtil.getSuccessResponseInfo(GoForWealthAdminErrorMessageEnum.SUCCESS_CODE.getValue(), GoForWealthAdminErrorMessageEnum.SUCCESS_MESSAGE.getValue(), blog);
					} else {
						responseInfo = new GoForWealthAdminResponseInfo();
						responseInfo.setStatus(GoForWealthAdminErrorMessageEnum.DATA_NOT_FOUND_CODE.getValue());
						responseInfo.setMessage(GoForWealthAdminErrorMessageEnum.DATA_NOT_FOUND_MESSAGE.getValue());
					}
				}else{
					responseInfo = GoForWealthAdminUtil.getSuccessResponseInfo(GoForWealthErrorMessageEnum.ACCESS_DENIED_CODE.getValue(),GoForWealthErrorMessageEnum.ACCESS_DENIED_MESSAGE.getValue(), null);
				}
			}else{
				responseInfo = GoForWealthAdminUtil.getSuccessResponseInfo(GoForWealthErrorMessageEnum.ACCESS_DENIED_CODE.getValue(),GoForWealthErrorMessageEnum.ACCESS_DENIED_MESSAGE.getValue(), null);
			}
		}else{
			responseInfo = GoForWealthAdminUtil.getSuccessResponseInfo(GoForWealthErrorMessageEnum.ACCESS_DENIED_CODE.getValue(),GoForWealthErrorMessageEnum.ACCESS_DENIED_MESSAGE.getValue(), null);
		}
		logger.info("Out getBlogById()");
		return responseInfo;
	}

	@PostMapping("/updateBlogById/{blogId}")
	public GoForWealthAdminResponseInfo updateBlogById(@RequestBody AdminBlogRequest adminBlogRequest,@PathVariable("blogId") int blogId,Authentication authentication) throws GoForWealthAdminException{
		logger.info("In updateBlogById()");
		GoForWealthAdminResponseInfo responseInfo=null;
		if(authentication != null){
			UserPrincipal usersession = (UserPrincipal) authentication.getPrincipal();
			if(usersession != null){
				User user = userRepository.findUserByRole(usersession.getUser().getUserId(), "ADMIN");
				if(user != null){
					BlogDTO blog=goForWealthAdminService.updateAdminBlog(adminBlogRequest,blogId,usersession.getUser().getUserId());
					if(blog != null ) {
						responseInfo = GoForWealthAdminUtil.getSuccessResponseInfo(GoForWealthAdminErrorMessageEnum.SUCCESS_CODE.getValue(), GoForWealthAdminErrorMessageEnum.SUCCESS_MESSAGE.getValue(), blog);
					}else{
						responseInfo = new GoForWealthAdminResponseInfo();
						responseInfo.setStatus(GoForWealthAdminErrorMessageEnum.DATA_NOT_FOUND_CODE.getValue());
						responseInfo.setMessage(GoForWealthAdminErrorMessageEnum.DATA_NOT_FOUND_MESSAGE.getValue());
					}
				}else{
					responseInfo = GoForWealthAdminUtil.getSuccessResponseInfo(GoForWealthErrorMessageEnum.ACCESS_DENIED_CODE.getValue(),GoForWealthErrorMessageEnum.ACCESS_DENIED_MESSAGE.getValue(), null);
				}				
			}else{
				responseInfo = GoForWealthAdminUtil.getSuccessResponseInfo(GoForWealthErrorMessageEnum.ACCESS_DENIED_CODE.getValue(),GoForWealthErrorMessageEnum.ACCESS_DENIED_MESSAGE.getValue(), null);
			}
		}else{
			responseInfo = GoForWealthAdminUtil.getSuccessResponseInfo(GoForWealthErrorMessageEnum.ACCESS_DENIED_CODE.getValue(),GoForWealthErrorMessageEnum.ACCESS_DENIED_MESSAGE.getValue(), null);
		}
		logger.info("Out updateBlogById()");
		return responseInfo;
	}

	@PostMapping("/deleteBlogById/{blogId}")
	public GoForWealthAdminResponseInfo deleteBlogById(@PathVariable("blogId") int blogId,Authentication authentication) throws GoForWealthAdminException {
		logger.info("In deleteBlogById()");
		GoForWealthAdminResponseInfo responseInfo=null;
		if(authentication != null){
			UserPrincipal usersession = (UserPrincipal) authentication.getPrincipal();
			if(usersession != null){
				User user = userRepository.findUserByRole(usersession.getUser().getUserId(), "ADMIN");
				if(user != null){
					goForWealthAdminService.deleteBlogById(blogId,usersession.getUser().getUserId());
					responseInfo=GoForWealthAdminUtil.getSuccessResponseInfo(GoForWealthAdminErrorMessageEnum.SUCCESS_CODE.getValue(), GoForWealthAdminErrorMessageEnum.SUCCESS_MESSAGE.getValue(),null);
				}else{
					responseInfo = GoForWealthAdminUtil.getSuccessResponseInfo(GoForWealthErrorMessageEnum.ACCESS_DENIED_CODE.getValue(),GoForWealthErrorMessageEnum.ACCESS_DENIED_MESSAGE.getValue(), null);
				}
			}else{
				responseInfo = GoForWealthAdminUtil.getSuccessResponseInfo(GoForWealthErrorMessageEnum.ACCESS_DENIED_CODE.getValue(),GoForWealthErrorMessageEnum.ACCESS_DENIED_MESSAGE.getValue(), null);
			}
		}else{
			responseInfo = GoForWealthAdminUtil.getSuccessResponseInfo(GoForWealthErrorMessageEnum.ACCESS_DENIED_CODE.getValue(),GoForWealthErrorMessageEnum.ACCESS_DENIED_MESSAGE.getValue(), null);
		}
		logger.info("Out deleteBlogById()");
		return responseInfo;
	}

	@PostMapping("/getSeoInfo/{pageName}")
	public GoForWealthAdminResponseInfo getSeoInfo(@PathVariable("pageName") String pageName,HttpServletResponse response) throws IOException, GoForWealthAdminException {
		logger.info("In getSeoInfo()");
		Seo scoInfo= goForWealthAdminService.getSeoInfo(pageName);
	    GoForWealthAdminResponseInfo responseInfo;
	    if(scoInfo != null ) {
	    	responseInfo = GoForWealthAdminUtil.getSuccessResponseInfo(GoForWealthAdminErrorMessageEnum.SUCCESS_CODE.getValue(), GoForWealthAdminErrorMessageEnum.SUCCESS_MESSAGE.getValue(), scoInfo);
	    }else{
	    	responseInfo = new GoForWealthAdminResponseInfo();
	    	responseInfo.setStatus(GoForWealthAdminErrorMessageEnum.DATA_NOT_FOUND_CODE.getValue());
	    	responseInfo.setMessage(GoForWealthAdminErrorMessageEnum.DATA_NOT_FOUND_MESSAGE.getValue());
	    }
	    logger.info("Out getSeoInfo()");
	    return responseInfo;
	}

	@GetMapping("/getAllSchemes/{count}")
	public GoForWealthAdminResponseInfo getAllSchemes(@PathVariable("count") int count,Authentication authentication) throws GoForWealthAdminException  {	
		logger.info("In getAllSchemes()");
		GoForWealthAdminResponseInfo responseInfo=null;
		if(authentication != null){
			UserPrincipal usersession = (UserPrincipal) authentication.getPrincipal();
			if(usersession != null){
				User user = userRepository.findUserByRole(usersession.getUser().getUserId(), "ADMIN");
				if(user != null){
					List<SchemeDTO> topSchemeList = goForWealthAdminService.getAllSchemes(count,usersession.getUser().getUserId());
					if(!topSchemeList.isEmpty()) {
						responseInfo = GoForWealthAdminUtil.getSuccessResponseInfo(GoForWealthAdminErrorMessageEnum.SUCCESS_CODE.getValue(), GoForWealthAdminErrorMessageEnum.SUCCESS_MESSAGE.getValue(), topSchemeList);
					}else{
						responseInfo = new GoForWealthAdminResponseInfo();
						responseInfo.setStatus(GoForWealthAdminErrorMessageEnum.DATA_NOT_FOUND_CODE.getValue());
						responseInfo.setMessage(GoForWealthAdminErrorMessageEnum.DATA_NOT_FOUND_MESSAGE.getValue());
					}
				}else{
					responseInfo = GoForWealthAdminUtil.getSuccessResponseInfo(GoForWealthErrorMessageEnum.ACCESS_DENIED_CODE.getValue(),GoForWealthErrorMessageEnum.ACCESS_DENIED_MESSAGE.getValue(), null);
				}
			}else{
				responseInfo = GoForWealthAdminUtil.getSuccessResponseInfo(GoForWealthErrorMessageEnum.ACCESS_DENIED_CODE.getValue(),GoForWealthErrorMessageEnum.ACCESS_DENIED_MESSAGE.getValue(), null);
			}
		}else{
			responseInfo = GoForWealthAdminUtil.getSuccessResponseInfo(GoForWealthErrorMessageEnum.ACCESS_DENIED_CODE.getValue(),GoForWealthErrorMessageEnum.ACCESS_DENIED_MESSAGE.getValue(), null);
		}
		logger.info("Out getAllSchemes()");
		return responseInfo;
	}

	@PostMapping("/saveTopSchemes/{schemeCodes}")
	public GoForWealthAdminResponseInfo saveTopSchemes(@PathVariable("schemeCodes") List<String> schemeCode,Authentication authentication) throws GoForWealthAdminException {
		logger.info("In saveTopSchemes()");
		GoForWealthAdminResponseInfo responseInfo=null;
		if(authentication != null){
			UserPrincipal usersession = (UserPrincipal) authentication.getPrincipal();
			if(usersession != null){
				User user = userRepository.findUserByRole(usersession.getUser().getUserId(), "ADMIN");
				if(user != null){
					goForWealthAdminService.saveTopSchemes(schemeCode,usersession.getUser().getUserId());
					responseInfo = GoForWealthAdminUtil.getSuccessResponseInfo(GoForWealthAdminErrorMessageEnum.SUCCESS_CODE.getValue(), GoForWealthAdminErrorMessageEnum.SUCCESS_MESSAGE.getValue(),null);
				}else{
					responseInfo = GoForWealthAdminUtil.getSuccessResponseInfo(GoForWealthErrorMessageEnum.ACCESS_DENIED_CODE.getValue(),GoForWealthErrorMessageEnum.ACCESS_DENIED_MESSAGE.getValue(), null);
				}
			}else{
				responseInfo = GoForWealthAdminUtil.getSuccessResponseInfo(GoForWealthErrorMessageEnum.ACCESS_DENIED_CODE.getValue(),GoForWealthErrorMessageEnum.ACCESS_DENIED_MESSAGE.getValue(), null);
			}
		}else{
			responseInfo = GoForWealthAdminUtil.getSuccessResponseInfo(GoForWealthErrorMessageEnum.ACCESS_DENIED_CODE.getValue(),GoForWealthErrorMessageEnum.ACCESS_DENIED_MESSAGE.getValue(), null);
		}
		logger.info("Out saveTopSchemes()");
		return responseInfo;
	}

	@GetMapping("/getAllTopSchemes")
	public GoForWealthAdminResponseInfo getAllTopSchemes(Authentication authentication) throws GoForWealthAdminException {
		logger.info("In getAllTopSchemes()");
		GoForWealthAdminResponseInfo responseInfo=null;
		if(authentication != null){
			UserPrincipal usersession = (UserPrincipal) authentication.getPrincipal();
			if(usersession != null){
				User user = userRepository.findUserByRole(usersession.getUser().getUserId(), "ADMIN");
				if(user != null){
					List<SchemeDTO> topSchemeList = goForWealthAdminService.getAllTopSchemes(usersession.getUser().getUserId());
					if(!topSchemeList.isEmpty()) {
						responseInfo = GoForWealthAdminUtil.getSuccessResponseInfo(GoForWealthAdminErrorMessageEnum.SUCCESS_CODE.getValue(), GoForWealthAdminErrorMessageEnum.SUCCESS_MESSAGE.getValue(), topSchemeList);
					} else {
						responseInfo = new GoForWealthAdminResponseInfo();
						responseInfo.setStatus(GoForWealthAdminErrorMessageEnum.DATA_NOT_FOUND_CODE.getValue());
						responseInfo.setMessage(GoForWealthAdminErrorMessageEnum.DATA_NOT_FOUND_MESSAGE.getValue());
					}
				}else{
					responseInfo = GoForWealthAdminUtil.getSuccessResponseInfo(GoForWealthErrorMessageEnum.ACCESS_DENIED_CODE.getValue(),GoForWealthErrorMessageEnum.ACCESS_DENIED_MESSAGE.getValue(), null);
				}
			}else{
				responseInfo = GoForWealthAdminUtil.getSuccessResponseInfo(GoForWealthErrorMessageEnum.ACCESS_DENIED_CODE.getValue(),GoForWealthErrorMessageEnum.ACCESS_DENIED_MESSAGE.getValue(), null);
			}
		}else{
			responseInfo = GoForWealthAdminUtil.getSuccessResponseInfo(GoForWealthErrorMessageEnum.ACCESS_DENIED_CODE.getValue(),GoForWealthErrorMessageEnum.ACCESS_DENIED_MESSAGE.getValue(), null);
		}
		logger.info("Out getAllTopSchemes()");
		return responseInfo;
	}

	@PostMapping("/deactivateTopSchemes/{schemeCodes}")
	public GoForWealthAdminResponseInfo deactivateTopSchemes(@PathVariable("schemeCodes") List<String> schemeCodes,Authentication authentication) throws GoForWealthAdminException   {
		logger.info("In deactivateTopSchemes()");
		GoForWealthAdminResponseInfo responseInfo=null;
		if(authentication != null){
			UserPrincipal usersession = (UserPrincipal) authentication.getPrincipal();
			if(usersession != null){
				User user = userRepository.findUserByRole(usersession.getUser().getUserId(), "ADMIN");
				if(user != null){
					goForWealthAdminService.deactivateTopSchemes(schemeCodes,usersession.getUser().getUserId());
					responseInfo = GoForWealthAdminUtil.getSuccessResponseInfo(GoForWealthAdminErrorMessageEnum.SUCCESS_CODE.getValue(), GoForWealthAdminErrorMessageEnum.SUCCESS_MESSAGE.getValue(), null);
				}else{
					responseInfo = GoForWealthAdminUtil.getSuccessResponseInfo(GoForWealthErrorMessageEnum.ACCESS_DENIED_CODE.getValue(),GoForWealthErrorMessageEnum.ACCESS_DENIED_MESSAGE.getValue(), null);
				}
			}else{
				responseInfo = GoForWealthAdminUtil.getSuccessResponseInfo(GoForWealthErrorMessageEnum.ACCESS_DENIED_CODE.getValue(),GoForWealthErrorMessageEnum.ACCESS_DENIED_MESSAGE.getValue(), null);
			}
		}else{
			responseInfo = GoForWealthAdminUtil.getSuccessResponseInfo(GoForWealthErrorMessageEnum.ACCESS_DENIED_CODE.getValue(),GoForWealthErrorMessageEnum.ACCESS_DENIED_MESSAGE.getValue(), null);
		}
		logger.info("Out deactivateTopSchemes()");
		return responseInfo;
	}
	
	/**
	 * 
	 * @param schemeUploadRequest
	 * @return GoForWealthAdminResponseInfo
	 */
	@RequestMapping(value = "/uploadScheme/saveTextDataToDbUsingBase64", produces = "application/json", method = RequestMethod.POST)
	public GoForWealthAdminResponseInfo saveSchemeTextFileDataToDbUsingBase64(@RequestBody SchemeUploadRequest schemeUploadRequest, Authentication auth) {
		logger.info("In saveSchemeTextFileDataToDbUsingBase64()");
	  	GoForWealthAdminResponseInfo responseInfo=null;
	  	UserPrincipal usersession = (UserPrincipal) auth.getPrincipal();
	  	if(usersession != null){
	  		User user = userRepository.findUserByRole(usersession.getUser().getUserId(), "ADMIN");
	  		if(user != null){
	  			try {
	  				if(schemeUploadRequest.getBase64()==null){
	  					return GoForWealthAdminUtil.getErrorResponseInfo(GoForWealthAdminErrorMessageEnum.DATA_NOT_FOUND_CODE.getValue(), GoForWealthAdminErrorMessageEnum.DATA_NOT_FOUND_MESSAGE.getValue(), null);
	  				}
	  				String base64=schemeUploadRequest.getBase64();
	  				byte[] byteArray = Base64.decodeBase64(base64);
	  				//goForWealthFundSchemeService.addSchemeForSipviaBase64(byteArray);
	  				boolean flag = goForWealthFundSchemeService.addSchemeviaBase64(byteArray);
	  				if(flag){
	  					boolean result = goForWealthFundSchemeService.updateSchemeWithKeyword();
	  					if(result){
	  						goForWealthFundSchemeService.deleteDirectScheme();
	  					}
	  				}
	  				if(!flag) {
	  					responseInfo = GoForWealthAdminUtil.getSuccessResponseInfo(GoForWealthAdminErrorMessageEnum.FAILURE_CODE.getValue(),GoForWealthAdminErrorMessageEnum.FAILURE_MESSAGE.getValue(),null);
	  				}
	  			} catch (Exception e) {
	  				responseInfo = GoForWealthAdminUtil.getSuccessResponseInfo(GoForWealthAdminErrorMessageEnum.INTERNAL_SERVER_ERROR.getValue(),GoForWealthAdminErrorMessageEnum.INTERNAL_SERVER_ERROR.getValue(),null);
	  			}
	  			responseInfo = GoForWealthAdminUtil.getSuccessResponseInfo(GoForWealthAdminErrorMessageEnum.SUCCESS_CODE.getValue(),GoForWealthAdminErrorMessageEnum.SUCCESS_MESSAGE.getValue(),null);
	  			logger.info("Out savesSchemeTextFileDataToDb()");
	  		}else{
	  			responseInfo = GoForWealthAdminUtil.getSuccessResponseInfo(GoForWealthErrorMessageEnum.ACCESS_DENIED_CODE.getValue(),GoForWealthErrorMessageEnum.ACCESS_DENIED_MESSAGE.getValue(), null);
	  		}
	  	}else{
	  		responseInfo = GoForWealthAdminUtil.getSuccessResponseInfo(GoForWealthErrorMessageEnum.ACCESS_DENIED_CODE.getValue(),GoForWealthErrorMessageEnum.ACCESS_DENIED_MESSAGE.getValue(), null);
	  	}
	  	logger.info("Out saveSchemeTextFileDataToDbUsingBase64()");
		return responseInfo;
    }


	/**
	 * 
	 * @param navUploadRequest
	 * @return GoForWealthAdminResponseInfo
	 */
	@RequestMapping(value="/uploadNav/saveNavTextFileDataToDbUsingBase64", method=RequestMethod.POST)
	public GoForWealthAdminResponseInfo saveNavMasterTextFileDataToDbUsingBase64(@RequestBody NavUploadRequest navUploadRequest) {
		logger.info("In saveNavMasterTextFileDataToDbUsingBase64()");
		if(WebUtils.userHasAuthority(WebUtils.ADMIN_ROLE_NAME)) {
			throw new AccessDeniedException(GoForWealthAdminErrorMessageEnum.ACCESS_DENIED_MESSAGE.getValue());
		}
		try {
			String base64 = navUploadRequest.getBase64();
			byte[] byteArray = Base64.decodeBase64(base64);
			boolean flag = goForWealthFundSchemeService.addNavViaBase64(byteArray);
			if(!flag)
				return GoForWealthAdminUtil.getSuccessResponseInfo(GoForWealthAdminErrorMessageEnum.FAILURE_CODE.getValue(), GoForWealthAdminErrorMessageEnum.FAILURE_MESSAGE.getValue(),null);
		} catch(Exception e) {
			return GoForWealthAdminUtil.getSuccessResponseInfo(GoForWealthAdminErrorMessageEnum.INTERNAL_SERVER_ERROR.getValue(), GoForWealthAdminErrorMessageEnum.INTERNAL_SERVER_ERROR.getValue(),null);	
		}
		logger.info("Out saveNavMasterTextFileDataToDbUsingBase64()");
		return GoForWealthAdminUtil.getSuccessResponseInfo(GoForWealthAdminErrorMessageEnum.SUCCESS_CODE.getValue(), GoForWealthAdminErrorMessageEnum.SUCCESS_MESSAGE.getValue(),null);
	}


	/**
	 * 
	 * @param navMasterTextFile
	 * @return GoForWealthPRSResponseInfo
	*/
	@RequestMapping(value="/api/upload/navMaster", method=RequestMethod.POST)
	public GoForWealthAdminResponseInfo saveNavMasterTextFileDataToDb(@RequestParam("navMasterTextFile") MultipartFile navMaster) {
		logger.info("In saveNavMasterTextFileDataToDb()");
		if(WebUtils.userHasAuthority(WebUtils.ADMIN_ROLE_NAME)) {
			throw new AccessDeniedException(GoForWealthAdminErrorMessageEnum.ACCESS_DENIED_MESSAGE.getValue());
		}
		BufferedReader br;
		List<String> result = new ArrayList<>();
		List<NavMaster> navMasterList = new ArrayList<>();
		try {
			String line;
		    InputStream is = navMaster.getInputStream();
		    br = new BufferedReader(new InputStreamReader(is));
		    while((line = br.readLine()) != null) {
		    	result.add(line);
		    }
		    ListIterator<String> itr = result.listIterator(1);
		    while(itr.hasNext()) {
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
		    if(!flag) {
		    	return GoForWealthAdminUtil.getSuccessResponseInfo(GoForWealthAdminErrorMessageEnum.FAILURE_CODE.getValue(), GoForWealthAdminErrorMessageEnum.FAILURE_MESSAGE.getValue(),null);
		    }
		} catch (IOException e) {
			return GoForWealthAdminUtil.getSuccessResponseInfo(GoForWealthAdminErrorMessageEnum.INTERNAL_SERVER_ERROR.getValue(), GoForWealthAdminErrorMessageEnum.INTERNAL_SERVER_ERROR.getValue(),null);
		}
		logger.info("Out saveNavMasterTextFileDataToDb()");
		return GoForWealthAdminUtil.getSuccessResponseInfo(GoForWealthAdminErrorMessageEnum.SUCCESS_CODE.getValue(), GoForWealthAdminErrorMessageEnum.SUCCESS_MESSAGE.getValue(),null);		
	}

	@GetMapping("/getAllUsers/{count}")
	public GoForWealthAdminResponseInfo getAllUsers(Authentication authentication,@PathVariable("count") int count) throws GoForWealthAdminException{
		logger.info("In getAllUsers()");
		GoForWealthAdminResponseInfo responseInfo=null;
		if(authentication != null){
			UserPrincipal usersession = (UserPrincipal) authentication.getPrincipal();
			if(usersession != null){
				User user = userRepository.findUserByRole(usersession.getUser().getUserId(), "ADMIN");
				if(user !=null){
					List<UsersListDTO> usersList=goForWealthAdminService.getAllUsers(count,usersession.getUser().getUserId());
					if(!usersList.isEmpty()) {
						responseInfo = GoForWealthAdminUtil.getSuccessResponseInfo(GoForWealthAdminErrorMessageEnum.SUCCESS_CODE.getValue(), GoForWealthAdminErrorMessageEnum.SUCCESS_MESSAGE.getValue(), usersList);
					} else {
						responseInfo = new GoForWealthAdminResponseInfo();
						responseInfo.setStatus(GoForWealthPRSErrorMessageEnum.DATA_NOT_FOUND_CODE.getValue());
						responseInfo.setMessage(GoForWealthPRSErrorMessageEnum.DATA_NOT_FOUND_MESSAGE.getValue());
					}
				}else{
					responseInfo = GoForWealthAdminUtil.getSuccessResponseInfo(GoForWealthErrorMessageEnum.ACCESS_DENIED_CODE.getValue(),GoForWealthErrorMessageEnum.ACCESS_DENIED_MESSAGE.getValue(), null);
				}
			}else{
				responseInfo = GoForWealthAdminUtil.getSuccessResponseInfo(GoForWealthErrorMessageEnum.ACCESS_DENIED_CODE.getValue(),GoForWealthErrorMessageEnum.ACCESS_DENIED_MESSAGE.getValue(), null);
			}
		}else{
			responseInfo = GoForWealthAdminUtil.getSuccessResponseInfo(GoForWealthErrorMessageEnum.ACCESS_DENIED_CODE.getValue(),GoForWealthErrorMessageEnum.ACCESS_DENIED_MESSAGE.getValue(), null);
		}
		logger.info("Out getAllUsers()");
		return responseInfo;
	}

	@GetMapping("/getUserDetailedForAdminDashboard")
	public GoForWealthAdminResponseInfo getUserDetailedForAdminDashboard(Authentication authentication){
		logger.info("In getUserDetailedForAdminDashboard()");
		GoForWealthAdminResponseInfo responseInfo = null;
		if(authentication != null){
			UserPrincipal usersession = (UserPrincipal) authentication.getPrincipal();
			if(usersession != null){
				User user = userRepository.findUserByRole(usersession.getUser().getUserId(), "ADMIN");
				UserDetailedDataDTO userDetailedDataDTO = null;
				if(user != null){
					userDetailedDataDTO = goForWealthAdminService.getUserDetailedForAdminDashboard(user);
					if(userDetailedDataDTO != null) {
						responseInfo = GoForWealthAdminUtil.getSuccessResponseInfo(GoForWealthAdminErrorMessageEnum.SUCCESS_CODE.getValue(), GoForWealthAdminErrorMessageEnum.SUCCESS_MESSAGE.getValue(), userDetailedDataDTO);
					}else{
						responseInfo = new GoForWealthAdminResponseInfo();
						responseInfo.setStatus(GoForWealthAdminErrorMessageEnum.DATA_NOT_FOUND_CODE.getValue());
						responseInfo.setMessage(GoForWealthAdminErrorMessageEnum.DATA_NOT_FOUND_MESSAGE.getValue());
					}
				}else{
					responseInfo = GoForWealthAdminUtil.getSuccessResponseInfo(GoForWealthErrorMessageEnum.ACCESS_DENIED_CODE.getValue(),GoForWealthErrorMessageEnum.ACCESS_DENIED_MESSAGE.getValue(),null);
				}
			}else{
				responseInfo = GoForWealthAdminUtil.getSuccessResponseInfo(GoForWealthErrorMessageEnum.ACCESS_DENIED_CODE.getValue(),GoForWealthErrorMessageEnum.ACCESS_DENIED_MESSAGE.getValue(),null);
			}
		}else{
			responseInfo = GoForWealthAdminUtil.getSuccessResponseInfo(GoForWealthErrorMessageEnum.ACCESS_DENIED_CODE.getValue(),GoForWealthErrorMessageEnum.ACCESS_DENIED_MESSAGE.getValue(), null);
		}
		logger.info("Out getUserDetailedForAdminDashboard()");
		return responseInfo;
	}

	@PostMapping("/getUserInfoById/{id}")
	public GoForWealthAdminResponseInfo getUserInfoById(@PathVariable("id") int id,Authentication authentication) throws GoForWealthAdminException{
		logger.info("In getUserInfoById()");
		GoForWealthAdminResponseInfo responseInfo=null;
		if(authentication != null){
			UserPrincipal usersession = (UserPrincipal) authentication.getPrincipal();
			if(usersession != null){
				User user = userRepository.findUserByRole(usersession.getUser().getUserId(), "ADMIN");
				if(user != null){
					UserInfoDto userInfoDto = goForWealthAdminService.getUserInfoById(id,usersession.getUser().getUserId());
					if(userInfoDto != null) {
						responseInfo = GoForWealthAdminUtil.getSuccessResponseInfo(GoForWealthAdminErrorMessageEnum.SUCCESS_CODE.getValue(), GoForWealthAdminErrorMessageEnum.SUCCESS_MESSAGE.getValue(), userInfoDto);
					} else {
						responseInfo = new GoForWealthAdminResponseInfo();
						responseInfo.setStatus(GoForWealthPRSErrorMessageEnum.DATA_NOT_FOUND_CODE.getValue());
						responseInfo.setMessage(GoForWealthPRSErrorMessageEnum.DATA_NOT_FOUND_MESSAGE.getValue());
					}
				}else{
					responseInfo = GoForWealthAdminUtil.getSuccessResponseInfo(GoForWealthErrorMessageEnum.ACCESS_DENIED_CODE.getValue(),GoForWealthErrorMessageEnum.ACCESS_DENIED_MESSAGE.getValue(), null);
				}
			}else{
				responseInfo = GoForWealthAdminUtil.getSuccessResponseInfo(GoForWealthErrorMessageEnum.ACCESS_DENIED_CODE.getValue(),GoForWealthErrorMessageEnum.ACCESS_DENIED_MESSAGE.getValue(), null);
			}
		}else{
			responseInfo = GoForWealthAdminUtil.getSuccessResponseInfo(GoForWealthErrorMessageEnum.ACCESS_DENIED_CODE.getValue(),GoForWealthErrorMessageEnum.ACCESS_DENIED_MESSAGE.getValue(), null);
		}
		logger.info("Out getUserInfoById()");
		return responseInfo;
	}

	@GetMapping("/getTotalPagesForPagination")
	public GoForWealthAdminResponseInfo getTotalPagesForPagination(Authentication authentication) throws GoForWealthAdminException{
		logger.info("In getTotalPagesForPagination()");
		GoForWealthAdminResponseInfo responseInfo=null;
		if(authentication != null){
			UserPrincipal usersession = (UserPrincipal) authentication.getPrincipal();
			if(usersession != null){
				User user = userRepository.findUserByRole(usersession.getUser().getUserId(), "ADMIN");
				if(user != null){
					int[] totalPages=goForWealthAdminService.getTotalPagesForPagination(usersession.getUser().getUserId());
					if(totalPages.length!=0) {
						responseInfo = GoForWealthAdminUtil.getSuccessResponseInfo(GoForWealthAdminErrorMessageEnum.SUCCESS_CODE.getValue(), GoForWealthAdminErrorMessageEnum.SUCCESS_MESSAGE.getValue(), totalPages);
					} else {
						responseInfo = new GoForWealthAdminResponseInfo();
						responseInfo.setStatus(GoForWealthPRSErrorMessageEnum.DATA_NOT_FOUND_CODE.getValue());
						responseInfo.setMessage(GoForWealthPRSErrorMessageEnum.DATA_NOT_FOUND_MESSAGE.getValue());
					}
				}else{
					responseInfo = GoForWealthAdminUtil.getSuccessResponseInfo(GoForWealthErrorMessageEnum.ACCESS_DENIED_CODE.getValue(),GoForWealthErrorMessageEnum.ACCESS_DENIED_MESSAGE.getValue(), null);
				}
			}else{
				responseInfo = GoForWealthAdminUtil.getSuccessResponseInfo(GoForWealthErrorMessageEnum.ACCESS_DENIED_CODE.getValue(),GoForWealthErrorMessageEnum.ACCESS_DENIED_MESSAGE.getValue(), null);
			}
		}else{
			responseInfo = GoForWealthAdminUtil.getSuccessResponseInfo(GoForWealthErrorMessageEnum.ACCESS_DENIED_CODE.getValue(),GoForWealthErrorMessageEnum.ACCESS_DENIED_MESSAGE.getValue(), null);
		}
		logger.info("Out getTotalPagesForPagination()");
		return responseInfo;
	}

	@PostMapping("/completeOnboarding/{id}")
	public GoForWealthAdminResponseInfo completeOnboarding(@PathVariable("id") int id,Authentication authentication) throws GoForWealthAdminException {
		logger.info("In completeOnboarding()");
		GoForWealthAdminResponseInfo responseInfo=null;
		if(authentication != null){
			UserPrincipal usersession = (UserPrincipal) authentication.getPrincipal();
			if(usersession != null){
				User user = userRepository.findUserByRole(usersession.getUser().getUserId(), "ADMIN");
				if(user != null){
					String response =goForWealthAdminService.completeOnboarding(id,authentication);
					  if(response.equals("success")){
						  responseInfo = GoForWealthAdminUtil.getSuccessResponseInfo(GoForWealthAdminErrorMessageEnum.SUCCESS_CODE.getValue(), GoForWealthAdminErrorMessageEnum.USER_MANDATE_COMPLETED_SUCCESSFULLY.getValue(), response);
					  }
				}else{
					responseInfo = GoForWealthAdminUtil.getSuccessResponseInfo(GoForWealthErrorMessageEnum.ACCESS_DENIED_CODE.getValue(),GoForWealthErrorMessageEnum.ACCESS_DENIED_MESSAGE.getValue(), null);
				}
			}else{
				responseInfo = GoForWealthAdminUtil.getSuccessResponseInfo(GoForWealthErrorMessageEnum.ACCESS_DENIED_CODE.getValue(),GoForWealthErrorMessageEnum.ACCESS_DENIED_MESSAGE.getValue(), null);
			}
		}else{
			responseInfo = GoForWealthAdminUtil.getSuccessResponseInfo(GoForWealthErrorMessageEnum.ACCESS_DENIED_CODE.getValue(),GoForWealthErrorMessageEnum.ACCESS_DENIED_MESSAGE.getValue(),null);
		}
		logger.info("Out completeOnboarding()");
		return responseInfo;
	}

	@PostMapping("/addCategory/{category}")
	public GoForWealthAdminResponseInfo addCategory(@PathVariable("category") String category,Authentication authentication) throws GoForWealthAdminException{
		logger.info("In addCategory()");
		GoForWealthAdminResponseInfo responseInfo=null;
		if(authentication != null){
			UserPrincipal usersession = (UserPrincipal) authentication.getPrincipal();
			if(usersession != null){
				User user = userRepository.findUserByRole(usersession.getUser().getUserId(), "ADMIN");
				if(user != null){
					String response=goForWealthAdminService.addCategory(category,usersession.getUser().getUserId());
					if(response.equals("success")){
						   responseInfo = GoForWealthAdminUtil.getSuccessResponseInfo(GoForWealthAdminErrorMessageEnum.SUCCESS_CODE.getValue(), GoForWealthAdminErrorMessageEnum.SUCCESS_MESSAGE.getValue(), response);
					}else if(response.equals("categoryEsixt")){
						responseInfo = new GoForWealthAdminResponseInfo();
						responseInfo.setStatus(GoForWealthPRSErrorMessageEnum.CATEGORY_ALREADY_EXISTS_CODE.getValue());
						responseInfo.setMessage(GoForWealthPRSErrorMessageEnum.DATA_NOT_SAVED.getValue());
					}else {
						responseInfo = new GoForWealthAdminResponseInfo();
						responseInfo.setStatus(GoForWealthPRSErrorMessageEnum.DATA_NOT_FOUND_CODE.getValue());
						responseInfo.setMessage(GoForWealthPRSErrorMessageEnum.DATA_NOT_SAVED.getValue());
					}
				}else{
					responseInfo = GoForWealthAdminUtil.getSuccessResponseInfo(GoForWealthErrorMessageEnum.ACCESS_DENIED_CODE.getValue(),GoForWealthErrorMessageEnum.ACCESS_DENIED_MESSAGE.getValue(), null); 
				}
			}else{
				responseInfo = GoForWealthAdminUtil.getSuccessResponseInfo(GoForWealthErrorMessageEnum.ACCESS_DENIED_CODE.getValue(),GoForWealthErrorMessageEnum.ACCESS_DENIED_MESSAGE.getValue(), null); 
			}
		}else{
			responseInfo = GoForWealthAdminUtil.getSuccessResponseInfo(GoForWealthErrorMessageEnum.ACCESS_DENIED_CODE.getValue(),GoForWealthErrorMessageEnum.ACCESS_DENIED_MESSAGE.getValue(), null); 
		}
		logger.info("Out addCategory()");
		return responseInfo;
	}

	@GetMapping("/getAllBlogCategory")
	public GoForWealthAdminResponseInfo getAllBlogCategory(Authentication authentication) throws GoForWealthAdminException{
		logger.info("In getAllBlogCategory()");
		GoForWealthAdminResponseInfo responseInfo=null;
		if(authentication != null){
			UserPrincipal usersession = (UserPrincipal) authentication.getPrincipal();
			if(usersession != null){
				User user = userRepository.findUserByRole(usersession.getUser().getUserId(), "ADMIN");
				if(user != null){
					List<BlogCategoryDTO> blogCategoryDTOList=goForWealthAdminService.getAllBlogCategory(usersession.getUser().getUserId());
					if(!blogCategoryDTOList.isEmpty()){
						responseInfo = GoForWealthAdminUtil.getSuccessResponseInfo(GoForWealthAdminErrorMessageEnum.SUCCESS_CODE.getValue(), GoForWealthAdminErrorMessageEnum.SUCCESS_MESSAGE.getValue(), blogCategoryDTOList);
					}else {
						responseInfo = new GoForWealthAdminResponseInfo();
						responseInfo.setStatus(GoForWealthPRSErrorMessageEnum.DATA_NOT_FOUND_CODE.getValue());
						responseInfo.setMessage(GoForWealthPRSErrorMessageEnum.DATA_NOT_SAVED.getValue());
					}
				}else{
					responseInfo = GoForWealthAdminUtil.getSuccessResponseInfo(GoForWealthErrorMessageEnum.ACCESS_DENIED_CODE.getValue(),GoForWealthErrorMessageEnum.ACCESS_DENIED_MESSAGE.getValue(), null);
				}
			}else{
				responseInfo = GoForWealthAdminUtil.getSuccessResponseInfo(GoForWealthErrorMessageEnum.ACCESS_DENIED_CODE.getValue(),GoForWealthErrorMessageEnum.ACCESS_DENIED_MESSAGE.getValue(), null); 
			}
		}else{
			responseInfo = GoForWealthAdminUtil.getSuccessResponseInfo(GoForWealthErrorMessageEnum.ACCESS_DENIED_CODE.getValue(),GoForWealthErrorMessageEnum.ACCESS_DENIED_MESSAGE.getValue(), null); 
		}
		logger.info("In getAllBlogCategory()");
		return responseInfo;
	}

	@PostMapping("/getBlogByCategory/{categoryId}")
	public GoForWealthAdminResponseInfo getBlogByCategory(@PathVariable("categoryId") int categoryId,Authentication authentication) throws GoForWealthAdminException{
		logger.info("In getBlogByCategory()");
		GoForWealthAdminResponseInfo responseInfo=null;
		if(authentication != null){
			UserPrincipal usersession = (UserPrincipal) authentication.getPrincipal();
			if(usersession != null){
				User user = userRepository.findUserByRole(usersession.getUser().getUserId(), "ADMIN");
				if(user != null){
					List<BlogDTO> blogDTOList=goForWealthAdminService.getBlogByCategoryId(categoryId,usersession.getUser().getUserId());
					if(!blogDTOList.isEmpty()){
						   responseInfo = GoForWealthAdminUtil.getSuccessResponseInfo(GoForWealthAdminErrorMessageEnum.SUCCESS_CODE.getValue(), GoForWealthAdminErrorMessageEnum.SUCCESS_MESSAGE.getValue(), blogDTOList);
					}else{
						responseInfo = new GoForWealthAdminResponseInfo();
						responseInfo.setStatus(GoForWealthPRSErrorMessageEnum.DATA_NOT_FOUND_CODE.getValue());
						responseInfo.setMessage(GoForWealthPRSErrorMessageEnum.DATA_NOT_SAVED.getValue());
					}
				}else{
					responseInfo = GoForWealthAdminUtil.getSuccessResponseInfo(GoForWealthErrorMessageEnum.ACCESS_DENIED_CODE.getValue(),GoForWealthErrorMessageEnum.ACCESS_DENIED_MESSAGE.getValue(), null);
				}
			}else{
				responseInfo = GoForWealthAdminUtil.getSuccessResponseInfo(GoForWealthErrorMessageEnum.ACCESS_DENIED_CODE.getValue(),GoForWealthErrorMessageEnum.ACCESS_DENIED_MESSAGE.getValue(), null); 
			}
		}else{
			responseInfo = GoForWealthAdminUtil.getSuccessResponseInfo(GoForWealthErrorMessageEnum.ACCESS_DENIED_CODE.getValue(),GoForWealthErrorMessageEnum.ACCESS_DENIED_MESSAGE.getValue(), null);
		}
		logger.info("Out getBlogByCategory()");
		return responseInfo;
	}

	@PostMapping("/contactUsFormData")
	public GoForWealthAdminResponseInfo contactUsFormData(@RequestBody ContactUsRequestDTO contactUsRequestDTO) throws GoForWealthAdminException{
		logger.info("In contactUsFormData()");
		GoForWealthAdminResponseInfo responseInfo=null;
		String response=goForWealthAdminService.contactUsFormData(contactUsRequestDTO);
		if(response != null){
			responseInfo = GoForWealthAdminUtil.getSuccessResponseInfo(GoForWealthAdminErrorMessageEnum.SUCCESS_CODE.getValue(), GoForWealthAdminErrorMessageEnum.SUCCESS_MESSAGE.getValue(), response);
		}else {
			responseInfo = new GoForWealthAdminResponseInfo();
			responseInfo.setStatus(GoForWealthPRSErrorMessageEnum.DATA_NOT_FOUND_CODE.getValue());
			responseInfo.setMessage(GoForWealthPRSErrorMessageEnum.DATA_NOT_SAVED.getValue());
		}
		logger.info("Out contactUsFormData()");
		return responseInfo;
	}

	@PostMapping("/saveTestimonial")
	public GoForWealthAdminResponseInfo saveTestimonial(@RequestBody TestiMonialRequestDTO testimonialRequestDTO,Authentication authentication) throws GoForWealthAdminException{
		logger.info("In saveTestimonial()");
		GoForWealthAdminResponseInfo responseInfo=null;
		if(authentication != null){
			UserPrincipal usersession = (UserPrincipal) authentication.getPrincipal();
			if(usersession != null){
				User user = userRepository.findUserByRole(usersession.getUser().getUserId(), "ADMIN");
				if(user != null){
					String response=goForWealthAdminService.saveTestimonial(testimonialRequestDTO,usersession.getUser().getUserId());
					if(response != null){
						   responseInfo = GoForWealthAdminUtil.getSuccessResponseInfo(GoForWealthAdminErrorMessageEnum.SUCCESS_CODE.getValue(), GoForWealthAdminErrorMessageEnum.SUCCESS_MESSAGE.getValue(), response);
					}else {
						responseInfo = new GoForWealthAdminResponseInfo();
						responseInfo.setStatus(GoForWealthPRSErrorMessageEnum.DATA_NOT_FOUND_CODE.getValue());
						responseInfo.setMessage(GoForWealthPRSErrorMessageEnum.DATA_NOT_SAVED.getValue());
					}
				}else{
					responseInfo = GoForWealthAdminUtil.getSuccessResponseInfo(GoForWealthErrorMessageEnum.ACCESS_DENIED_CODE.getValue(),GoForWealthErrorMessageEnum.ACCESS_DENIED_MESSAGE.getValue(), null);
				}
			}else{
				responseInfo = GoForWealthAdminUtil.getSuccessResponseInfo(GoForWealthErrorMessageEnum.ACCESS_DENIED_CODE.getValue(),GoForWealthErrorMessageEnum.ACCESS_DENIED_MESSAGE.getValue(), null);
			}
		}else{
			responseInfo = GoForWealthAdminUtil.getSuccessResponseInfo(GoForWealthErrorMessageEnum.ACCESS_DENIED_CODE.getValue(),GoForWealthErrorMessageEnum.ACCESS_DENIED_MESSAGE.getValue(), null);
		}
		logger.info("Out saveTestimonial()");
		return responseInfo;
	}

	@GetMapping("/getTestimonialData")
	public GoForWealthAdminResponseInfo getTestimonialData(Authentication authentication) throws GoForWealthAdminException{
		logger.info("In getTestimonialData()");
		GoForWealthAdminResponseInfo responseInfo=null;
		if(authentication != null){
			UserPrincipal usersession = (UserPrincipal) authentication.getPrincipal();
			if(usersession != null){
				User user = userRepository.findUserByRole(usersession.getUser().getUserId(), "ADMIN");
				if(user != null){
					List<TestimonialResponseDTO> testimonialList=goForWealthAdminService.getTestimonialData(usersession.getUser().getUserId());
					if(!testimonialList.isEmpty()){
						   responseInfo = GoForWealthAdminUtil.getSuccessResponseInfo(GoForWealthAdminErrorMessageEnum.SUCCESS_CODE.getValue(), GoForWealthAdminErrorMessageEnum.SUCCESS_MESSAGE.getValue(), testimonialList);
					}else {
						responseInfo = new GoForWealthAdminResponseInfo();
						responseInfo.setStatus(GoForWealthPRSErrorMessageEnum.DATA_NOT_FOUND_CODE.getValue());
						responseInfo.setMessage(GoForWealthPRSErrorMessageEnum.DATA_NOT_FOUND_MESSAGE.getValue());
					}
				}else{
					responseInfo = GoForWealthAdminUtil.getSuccessResponseInfo(GoForWealthErrorMessageEnum.ACCESS_DENIED_CODE.getValue(),GoForWealthErrorMessageEnum.ACCESS_DENIED_MESSAGE.getValue(), null);
				}
			}else{
				responseInfo = GoForWealthAdminUtil.getSuccessResponseInfo(GoForWealthErrorMessageEnum.ACCESS_DENIED_CODE.getValue(),GoForWealthErrorMessageEnum.ACCESS_DENIED_MESSAGE.getValue(), null);
			}
		}else{
			responseInfo = GoForWealthAdminUtil.getSuccessResponseInfo(GoForWealthErrorMessageEnum.ACCESS_DENIED_CODE.getValue(),GoForWealthErrorMessageEnum.ACCESS_DENIED_MESSAGE.getValue(), null);
		}
		logger.info("Out getTestimonialData()");
		return responseInfo;
	}

	@GetMapping("/getTestimonialImageById/{testimonialId}")
	public void getTestimonialImageById(@PathVariable("testimonialId")int testimonialId,HttpServletResponse response) throws IOException, GoForWealthAdminException {
		goForWealthAdminService.getTestimonialImageById(testimonialId,response);
	}

	@PostMapping("/getRelatedBlog/{id}")
	public GoForWealthAdminResponseInfo getRelatedBlog(@PathVariable("id") int id,Authentication authentication) throws IOException, GoForWealthAdminException {
		logger.info("In getRelatedBlog()");
		GoForWealthAdminResponseInfo responseInfo=null;
		if(authentication != null){
			UserPrincipal usersession = (UserPrincipal) authentication.getPrincipal();
			if(usersession != null){
				User user = userRepository.findUserByRole(usersession.getUser().getUserId(), "ADMIN");
				if(user != null){
					List<BlogDTO> blogList= goForWealthAdminService.getRelatedBlog(id,usersession.getUser().getUserId());
					if(!blogList.isEmpty()){
						responseInfo = GoForWealthAdminUtil.getSuccessResponseInfo(GoForWealthAdminErrorMessageEnum.SUCCESS_CODE.getValue(), GoForWealthAdminErrorMessageEnum.SUCCESS_MESSAGE.getValue(), blogList);
					}else {
						responseInfo = new GoForWealthAdminResponseInfo();
						responseInfo.setStatus(GoForWealthPRSErrorMessageEnum.DATA_NOT_FOUND_CODE.getValue());
						responseInfo.setMessage(GoForWealthPRSErrorMessageEnum.DATA_NOT_SAVED.getValue());
					}
				}else{
					responseInfo = GoForWealthAdminUtil.getSuccessResponseInfo(GoForWealthErrorMessageEnum.ACCESS_DENIED_CODE.getValue(),GoForWealthErrorMessageEnum.ACCESS_DENIED_MESSAGE.getValue(), null);
				}
			}else{
				responseInfo = GoForWealthAdminUtil.getSuccessResponseInfo(GoForWealthErrorMessageEnum.ACCESS_DENIED_CODE.getValue(),GoForWealthErrorMessageEnum.ACCESS_DENIED_MESSAGE.getValue(), null);
			}
		}else{
			responseInfo = GoForWealthAdminUtil.getSuccessResponseInfo(GoForWealthErrorMessageEnum.ACCESS_DENIED_CODE.getValue(),GoForWealthErrorMessageEnum.ACCESS_DENIED_MESSAGE.getValue(), null);
		}
		logger.info("Out getRelatedBlog()");
		return responseInfo;
	}

	@PostMapping("/getTestimonialById/{id}")
	public GoForWealthAdminResponseInfo getTestimonialData(@PathVariable("id") int id,Authentication authentication) throws GoForWealthAdminException{
		logger.info("In getTestimonialData()");
		GoForWealthAdminResponseInfo responseInfo=null;
		if(authentication != null){
			UserPrincipal usersession = (UserPrincipal) authentication.getPrincipal();
			if(usersession != null){
				User user = userRepository.findUserByRole(usersession.getUser().getUserId(), "ADMIN");
				if(user != null){
					TestimonialResponseDTO testimonialData=goForWealthAdminService.getTestimonialById(id,usersession.getUser().getUserId());
					if(testimonialData!=null){
						responseInfo = GoForWealthAdminUtil.getSuccessResponseInfo(GoForWealthAdminErrorMessageEnum.SUCCESS_CODE.getValue(), GoForWealthAdminErrorMessageEnum.SUCCESS_MESSAGE.getValue(), testimonialData);
					}else {
						responseInfo = new GoForWealthAdminResponseInfo();
						responseInfo.setStatus(GoForWealthPRSErrorMessageEnum.DATA_NOT_FOUND_CODE.getValue());
						responseInfo.setMessage(GoForWealthPRSErrorMessageEnum.DATA_NOT_SAVED.getValue());
					}
				}else{
					responseInfo = GoForWealthAdminUtil.getSuccessResponseInfo(GoForWealthErrorMessageEnum.ACCESS_DENIED_CODE.getValue(),GoForWealthErrorMessageEnum.ACCESS_DENIED_MESSAGE.getValue(), null);
				}
			}else{
				responseInfo = GoForWealthAdminUtil.getSuccessResponseInfo(GoForWealthErrorMessageEnum.ACCESS_DENIED_CODE.getValue(),GoForWealthErrorMessageEnum.ACCESS_DENIED_MESSAGE.getValue(), null);
			}
		}else{
			responseInfo = GoForWealthAdminUtil.getSuccessResponseInfo(GoForWealthErrorMessageEnum.ACCESS_DENIED_CODE.getValue(),GoForWealthErrorMessageEnum.ACCESS_DENIED_MESSAGE.getValue(), null);
		}
		logger.info("Out getTestimonialData()");
		return responseInfo;
	}

	@PostMapping("/updateTestimonial/{id}")
	public GoForWealthAdminResponseInfo updateTestimonial(@RequestBody TestiMonialRequestDTO testimonialRequestDTO,@PathVariable("id") int id,Authentication authentication) throws GoForWealthAdminException{
		logger.info("In updateTestimonial()");
		GoForWealthAdminResponseInfo responseInfo=null;
		if(authentication != null){
			UserPrincipal usersession = (UserPrincipal) authentication.getPrincipal();
			if(usersession != null){
				User user = userRepository.findUserByRole(usersession.getUser().getUserId(), "ADMIN");
				if(user != null){
					String response=goForWealthAdminService.updateTestimonial(testimonialRequestDTO,id,usersession.getUser().getUserId());
					if(response != null){
						responseInfo = GoForWealthAdminUtil.getSuccessResponseInfo(GoForWealthAdminErrorMessageEnum.SUCCESS_CODE.getValue(), GoForWealthAdminErrorMessageEnum.SUCCESS_MESSAGE.getValue(), response);
					}else {
						responseInfo = new GoForWealthAdminResponseInfo();
						responseInfo.setStatus(GoForWealthPRSErrorMessageEnum.DATA_NOT_FOUND_CODE.getValue());
						responseInfo.setMessage(GoForWealthPRSErrorMessageEnum.DATA_NOT_SAVED.getValue());
					}
				}else{
					responseInfo = GoForWealthAdminUtil.getSuccessResponseInfo(GoForWealthErrorMessageEnum.ACCESS_DENIED_CODE.getValue(),GoForWealthErrorMessageEnum.ACCESS_DENIED_MESSAGE.getValue(), null);
				}
			}else{
				responseInfo = GoForWealthAdminUtil.getSuccessResponseInfo(GoForWealthErrorMessageEnum.ACCESS_DENIED_CODE.getValue(),GoForWealthErrorMessageEnum.ACCESS_DENIED_MESSAGE.getValue(), null);
			}
		}else{
			responseInfo = GoForWealthAdminUtil.getSuccessResponseInfo(GoForWealthErrorMessageEnum.ACCESS_DENIED_CODE.getValue(),GoForWealthErrorMessageEnum.ACCESS_DENIED_MESSAGE.getValue(), null);
		}
		logger.info("Out updateTestimonial()");
		return responseInfo;
	}

	@PostMapping("/deleteTestimonial/{id}")
	public GoForWealthAdminResponseInfo deleteTestimonialByID(@PathVariable("id") int id,Authentication authentication) throws GoForWealthAdminException {
		logger.info("In deleteTestimonialById()");
		GoForWealthAdminResponseInfo responseInfo=null;
		if(authentication != null){
			UserPrincipal usersession = (UserPrincipal) authentication.getPrincipal();
			if(usersession != null){
				User user = userRepository.findUserByRole(usersession.getUser().getUserId(), "ADMIN");
				if(user != null){
					goForWealthAdminService.deleteTestimonialByID(id,usersession.getUser().getUserId());
					responseInfo=GoForWealthAdminUtil.getSuccessResponseInfo(GoForWealthAdminErrorMessageEnum.SUCCESS_CODE.getValue(), GoForWealthAdminErrorMessageEnum.SUCCESS_MESSAGE.getValue(),null);
				}else{
					responseInfo = GoForWealthAdminUtil.getSuccessResponseInfo(GoForWealthErrorMessageEnum.ACCESS_DENIED_CODE.getValue(),GoForWealthErrorMessageEnum.ACCESS_DENIED_MESSAGE.getValue(), null);
				}
			}else{
				responseInfo = GoForWealthAdminUtil.getSuccessResponseInfo(GoForWealthErrorMessageEnum.ACCESS_DENIED_CODE.getValue(),GoForWealthErrorMessageEnum.ACCESS_DENIED_MESSAGE.getValue(), null);
			}
		}else{
			responseInfo = GoForWealthAdminUtil.getSuccessResponseInfo(GoForWealthErrorMessageEnum.ACCESS_DENIED_CODE.getValue(),GoForWealthErrorMessageEnum.ACCESS_DENIED_MESSAGE.getValue(),null);
		}
		logger.info("Out deleteTestimonialById()");
		return responseInfo;
	}

	@GetMapping("/getUserTestimonialData")
	public GoForWealthAdminResponseInfo getUserTestimonialData(Authentication authentication) throws GoForWealthAdminException{
		logger.info("In getUserTestimonialData()");
		GoForWealthAdminResponseInfo responseInfo=null;
		if(authentication!=null){
			UserPrincipal userPrincipal=(UserPrincipal) authentication.getPrincipal();
			if(userPrincipal != null){
				boolean role=goForWealthAdminService.getUserAuthority(userPrincipal.getUser().getUserId());
				if(role){
					throw new GoForWealthAdminException(GoForWealthErrorMessageEnum.ADMIN_ACCESS_DENIED_CODE.getValue(),GoForWealthErrorMessageEnum.ADMIN_ACCESS_DENIED_MESSAGE.getValue());
				}else{	
					List<TestimonialResponseDTO> testimonialList=goForWealthAdminService.getUserTestimonialData();
					if(!testimonialList.isEmpty()){
						responseInfo = GoForWealthAdminUtil.getSuccessResponseInfo(GoForWealthAdminErrorMessageEnum.SUCCESS_CODE.getValue(), GoForWealthAdminErrorMessageEnum.SUCCESS_MESSAGE.getValue(), testimonialList);
					}else {
						responseInfo = new GoForWealthAdminResponseInfo();
						responseInfo.setStatus(GoForWealthPRSErrorMessageEnum.DATA_NOT_FOUND_CODE.getValue());
						responseInfo.setMessage(GoForWealthPRSErrorMessageEnum.DATA_NOT_SAVED.getValue());
					}
				}
			}
		}else{
			List<TestimonialResponseDTO> testimonialList=goForWealthAdminService.getUserTestimonialData();
			if(!testimonialList.isEmpty()){
				responseInfo = GoForWealthAdminUtil.getSuccessResponseInfo(GoForWealthAdminErrorMessageEnum.SUCCESS_CODE.getValue(), GoForWealthAdminErrorMessageEnum.SUCCESS_MESSAGE.getValue(), testimonialList);
			}else {
				responseInfo = new GoForWealthAdminResponseInfo();
				responseInfo.setStatus(GoForWealthPRSErrorMessageEnum.DATA_NOT_FOUND_CODE.getValue());
				responseInfo.setMessage(GoForWealthPRSErrorMessageEnum.DATA_NOT_SAVED.getValue());
			}
		}
		logger.info("Out getUserTestimonialData()");
		return responseInfo;
	}

	@GetMapping("/getAllSeos")
	public GoForWealthAdminResponseInfo getAllSeos(Authentication authentication) throws GoForWealthAdminException{
		logger.info("In getAllSeos()");
		GoForWealthAdminResponseInfo responseInfo=null;
		if(authentication != null){
			UserPrincipal usersession = (UserPrincipal) authentication.getPrincipal();
			if(usersession != null){
				User user = userRepository.findUserByRole(usersession.getUser().getUserId(), "ADMIN");
				if(user != null){
					List<Seo> usersList=goForWealthAdminService.getAllSeos(usersession.getUser().getUserId());
					if(!usersList.isEmpty()) {
						responseInfo = GoForWealthAdminUtil.getSuccessResponseInfo(GoForWealthAdminErrorMessageEnum.SUCCESS_CODE.getValue(), GoForWealthAdminErrorMessageEnum.SUCCESS_MESSAGE.getValue(), usersList);
					} else {
						responseInfo = new GoForWealthAdminResponseInfo();
						responseInfo.setStatus(GoForWealthPRSErrorMessageEnum.DATA_NOT_FOUND_CODE.getValue());
						responseInfo.setMessage(GoForWealthPRSErrorMessageEnum.DATA_NOT_FOUND_MESSAGE.getValue());
					}
				}else{
					responseInfo = GoForWealthAdminUtil.getSuccessResponseInfo(GoForWealthErrorMessageEnum.ACCESS_DENIED_CODE.getValue(),GoForWealthErrorMessageEnum.ACCESS_DENIED_MESSAGE.getValue(), null);
				}
			}else{
				responseInfo = GoForWealthAdminUtil.getSuccessResponseInfo(GoForWealthErrorMessageEnum.ACCESS_DENIED_CODE.getValue(),GoForWealthErrorMessageEnum.ACCESS_DENIED_MESSAGE.getValue(), null);
			}
		}else{
			responseInfo = GoForWealthAdminUtil.getSuccessResponseInfo(GoForWealthErrorMessageEnum.ACCESS_DENIED_CODE.getValue(),GoForWealthErrorMessageEnum.ACCESS_DENIED_MESSAGE.getValue(), null);
		}
		logger.info("Out getAllSeos()");
		return responseInfo;
	}

	@PostMapping("/updateSeo")
	public GoForWealthAdminResponseInfo updateSeoById(@RequestBody Seo updateSeoRequest,Authentication authentication) throws GoForWealthAdminException {
		logger.info("In updateSeoById");
		GoForWealthAdminResponseInfo responseInfo=null;
		if(authentication != null){
			UserPrincipal usersession = (UserPrincipal) authentication.getPrincipal();
			if(usersession != null){
				User user = userRepository.findUserByRole(usersession.getUser().getUserId(), "ADMIN");
				if(user != null){
					Seo updateSeo=goForWealthAdminService.updateSeo(updateSeoRequest,usersession.getUser().getUserId());
					if(updateSeo != null ) {
						responseInfo = GoForWealthAdminUtil.getSuccessResponseInfo(GoForWealthAdminErrorMessageEnum.SUCCESS_CODE.getValue(), GoForWealthAdminErrorMessageEnum.SUCCESS_MESSAGE.getValue(), updateSeo);
					} else {
						responseInfo = new GoForWealthAdminResponseInfo();
						responseInfo.setStatus(GoForWealthAdminErrorMessageEnum.DATA_NOT_FOUND_CODE.getValue());
						responseInfo.setMessage(GoForWealthAdminErrorMessageEnum.DATA_NOT_FOUND_MESSAGE.getValue());
					}
				}else{
					responseInfo = GoForWealthAdminUtil.getSuccessResponseInfo(GoForWealthErrorMessageEnum.ACCESS_DENIED_CODE.getValue(),GoForWealthErrorMessageEnum.ACCESS_DENIED_MESSAGE.getValue(), null);
				}
			}else{
				responseInfo = GoForWealthAdminUtil.getSuccessResponseInfo(GoForWealthErrorMessageEnum.ACCESS_DENIED_CODE.getValue(),GoForWealthErrorMessageEnum.ACCESS_DENIED_MESSAGE.getValue(), null);
			}
		}else{
			responseInfo = GoForWealthAdminUtil.getSuccessResponseInfo(GoForWealthErrorMessageEnum.ACCESS_DENIED_CODE.getValue(),GoForWealthErrorMessageEnum.ACCESS_DENIED_MESSAGE.getValue(), null);
		}
		logger.info("Out updateSeoById()");
		return responseInfo;
	}

	/**
	 * 
	 * @param schemeUploadRequest
	 * @return GoForWealthAdminResponseInfo
	 * @throws GoForWealthAdminException 
	*/
	@CrossOrigin
	@RequestMapping(value = "/uploadTopScheme", method = RequestMethod.POST)
	public GoForWealthAdminResponseInfo saveExcelDataToTopSchemeUsingBase64(@RequestBody List<TopSchemes> uploadTopShemes,Authentication authentication) throws IOException, EncryptedDocumentException, InvalidFormatException, GoForWealthAdminException {
		logger.info("In uploadTopScheme()");
		GoForWealthAdminResponseInfo responseInfo=null;
		if(authentication != null){
			UserPrincipal usersession = (UserPrincipal) authentication.getPrincipal();
			if(usersession != null){
				User user = userRepository.findUserByRole(usersession.getUser().getUserId(), "ADMIN");
				if(user != null){
					try {
						boolean flag = goForWealthAdminService.saveExcelDataToTopSchemeUsingBase64(uploadTopShemes);
						if(flag){
						}
						if(!flag) {
							responseInfo = GoForWealthAdminUtil.getSuccessResponseInfo(GoForWealthErrorMessageEnum.ACCESS_DENIED_CODE.getValue(),GoForWealthErrorMessageEnum.ACCESS_DENIED_MESSAGE.getValue(), null);
						}
					} catch (Exception e) {
						responseInfo =  GoForWealthAdminUtil.getSuccessResponseInfo(GoForWealthAdminErrorMessageEnum.INTERNAL_SERVER_ERROR.getValue(), GoForWealthAdminErrorMessageEnum.INTERNAL_SERVER_ERROR.getValue(),null);
					}
					responseInfo = GoForWealthAdminUtil.getSuccessResponseInfo(GoForWealthAdminErrorMessageEnum.SUCCESS_CODE.getValue(), GoForWealthAdminErrorMessageEnum.SUCCESS_MESSAGE.getValue(),null);
				}else{
					responseInfo = GoForWealthAdminUtil.getSuccessResponseInfo(GoForWealthErrorMessageEnum.ACCESS_DENIED_CODE.getValue(),GoForWealthErrorMessageEnum.ACCESS_DENIED_MESSAGE.getValue(), null);
				}
			}else{
				responseInfo = GoForWealthAdminUtil.getSuccessResponseInfo(GoForWealthErrorMessageEnum.ACCESS_DENIED_CODE.getValue(),GoForWealthErrorMessageEnum.ACCESS_DENIED_MESSAGE.getValue(), null);
			}
		}else{
			responseInfo = GoForWealthAdminUtil.getSuccessResponseInfo(GoForWealthErrorMessageEnum.ACCESS_DENIED_CODE.getValue(),GoForWealthErrorMessageEnum.ACCESS_DENIED_MESSAGE.getValue(), null);
		}
		logger.info("Out uploadTopScheme()");
		return responseInfo;
	}

	/**
	 * 
	 * @param response
	 * @param auth
	 * @throws GoForWealthSIPException
	 * @throws GoForWealthAdminException
	*/
	@GetMapping(value = "/export/excel/scheme")
    public void exportScheme(HttpServletResponse response,Authentication auth) throws GoForWealthSIPException, GoForWealthAdminException {
		logger.info("In exportScheme()");
		try {
			List<FundSchemeDTO> fundSchemeDTO = goForWealthAdminService.getAllSchemes();
			List<FundSchemeDTO> fundSchemeDTOList = new ArrayList<>();
			for (int i = 0; i < 50; i++) {
				FundSchemeDTO fundSchemeDTOObj = new FundSchemeDTO();
				fundSchemeDTOObj.setSchemeCode(fundSchemeDTO.get(i).getSchemeCode());
				fundSchemeDTOObj.setSchemeName(fundSchemeDTO.get(i).getSchemeName());
				fundSchemeDTOObj.setSchemeType(fundSchemeDTO.get(i).getSchemeType());
				fundSchemeDTOObj.setShowScheme(fundSchemeDTO.get(i).getShowScheme());
				fundSchemeDTOObj.setPriority(fundSchemeDTO.get(i).getPriority());
				fundSchemeDTOObj.setIsinCode(fundSchemeDTO.get(i).getIsinCode());
				fundSchemeDTOObj.setPurchaseAllowed(fundSchemeDTO.get(i).getPurchaseAllowed());
				fundSchemeDTOObj.setMinimumPurchaseAmount(fundSchemeDTO.get(i).getMinimumPurchaseAmount());
				fundSchemeDTOObj.setMaximumPurchaseAmount(fundSchemeDTO.get(i).getMaximumPurchaseAmount());
				fundSchemeDTOObj.setRedemptionAllowed(fundSchemeDTO.get(i).getRedemptionAllowed());
				fundSchemeDTOObj.setSipAllowed(fundSchemeDTO.get(i).getSipAllowed());
				fundSchemeDTOList.add(fundSchemeDTOObj);
			}
			Workbook wb = GoForWealthAdminUtil.createExportFileForScheme(fundSchemeDTOList);
			response.setContentType("application/xls");
			response.setHeader("Content-Disposition","attachment; filename=schemeList.xls");
			wb.write(response.getOutputStream());
			response.flushBuffer();
			logger.info("Out exportScheme()");
		} catch (IOException ex) {
			logger.info("Error writing file to output stream. ", ex);
			throw new GoForWealthSIPException(GoForWealthSIPErrorMessageEnum.COMMON_ERROR_CODE.getValue(),"IOError writing file to output stream");
		}
	}

	@CrossOrigin
	@RequestMapping(value = "upload/schemePriority/saveExcelDataToDbUsingBase64", produces = "application/json", method = RequestMethod.POST)
	public GoForWealthAdminResponseInfo saveExcelDataToDbUsingBase64(@RequestBody SchemeUploadRequest schemeUploadRequest) throws GoForWealthSIPException {
		GoForWealthAdminResponseInfo goForWealthAdminResponseInfo = new GoForWealthAdminResponseInfo();
		String base64Excel=schemeUploadRequest.getBase64();
		byte[] excelByteArry =Base64.decodeBase64(base64Excel);
		List<FundSchemeDTO> fundSchemeDtoList = GoForWealthAdminUtil.getExcelFromByte(excelByteArry);
		boolean flag = goForWealthAdminService.updateSchemeWithPriority(fundSchemeDtoList);
		if(flag){
			goForWealthAdminResponseInfo.setStatus(GoForWealthAdminErrorMessageEnum.SUCCESS_CODE.getValue());
			goForWealthAdminResponseInfo.setMessage(GoForWealthAdminErrorMessageEnum.SUCCESS_MESSAGE.getValue());
		}else{
			goForWealthAdminResponseInfo.setStatus(GoForWealthAdminErrorMessageEnum.COMMON_ERROR_CODE.getValue());
			goForWealthAdminResponseInfo.setMessage(GoForWealthAdminErrorMessageEnum.INTERNAL_SERVER_ERROR.getValue());
		}
		return goForWealthAdminResponseInfo;
	}

	@RequestMapping(value="/getUserOrders/{userID}", method=RequestMethod.GET)
	public GoForWealthAdminResponseInfo getUserOrders(@PathVariable("userID") int userID,@RequestParam("type") String type, Authentication auth) {
		logger.info("In getUserOrders()== userID === " + userID + " type === " + type);
		UserPrincipal userSession = (UserPrincipal)auth.getPrincipal();
		GoForWealthAdminResponseInfo responseInfo = null;
		List<AddToCartDTO> cartOrderList = new ArrayList<>();
		if(userSession!=null){
			User user = userRepository.findUserByRole(userSession.getUser().getUserId(), "ADMIN");
			if(user != null){
				cartOrderList = goForWealthFundSchemeService.getAdminCartOrder(userID, type);
				responseInfo = GoForWealthAdminUtil.getSuccessResponseInfo(GoForWealthPRSErrorMessageEnum.SUCCESS_CODE.getValue(),GoForWealthPRSErrorMessageEnum.SUCCESS_MESSAGE.getValue(),cartOrderList);
			}else{
				responseInfo = GoForWealthAdminUtil.getSuccessResponseInfo(GoForWealthErrorMessageEnum.ACCESS_DENIED_CODE.getValue(),GoForWealthErrorMessageEnum.ACCESS_DENIED_MESSAGE.getValue(), null);
			}
		}else{
			responseInfo = GoForWealthAdminUtil.getSuccessResponseInfo(GoForWealthErrorMessageEnum.ACCESS_DENIED_CODE.getValue(),GoForWealthErrorMessageEnum.ACCESS_DENIED_MESSAGE.getValue(), null);
		}
		logger.info("Out getUserOrders()");
		return responseInfo;
	}

	@RequestMapping(value="/getPortfolioByUserId/{userId}", method=RequestMethod.GET)
	public GoForWealthAdminResponseInfo getPortfolioByUserId(@PathVariable("userId") int userId, Authentication auth) {
		logger.info("In getPortfolioByUserId()== userID === "+userId);
		UserPrincipal userSession = (UserPrincipal)auth.getPrincipal();
		GoForWealthAdminResponseInfo responseInfo = null;
		List<PortFolioDataDTO> cartOrderList = new ArrayList<>();
		if(userSession!=null){			
			User user = userRepository.findUserByRole(userSession.getUser().getUserId(), "ADMIN");
			if(user != null){
				cartOrderList = goForWealthFundSchemeService.getConsoliDatedFollio(userId);
				responseInfo = GoForWealthAdminUtil.getSuccessResponseInfo(GoForWealthPRSErrorMessageEnum.SUCCESS_CODE.getValue(), GoForWealthPRSErrorMessageEnum.SUCCESS_MESSAGE.getValue(), cartOrderList);
			}else{
				responseInfo = GoForWealthAdminUtil.getSuccessResponseInfo(GoForWealthErrorMessageEnum.ACCESS_DENIED_CODE.getValue(),GoForWealthErrorMessageEnum.ACCESS_DENIED_MESSAGE.getValue(), null);
			}
		}else{
			responseInfo = GoForWealthAdminUtil.getSuccessResponseInfo(GoForWealthErrorMessageEnum.ACCESS_DENIED_CODE.getValue(),GoForWealthErrorMessageEnum.ACCESS_DENIED_MESSAGE.getValue(), null);
		}
		logger.info("Out getPortfolioByUserId()");
		return responseInfo;
	}

	@GetMapping("/searchUserByKeyword/{userName}")
	public GoForWealthAdminResponseInfo searchUserByKeyword(Authentication authentication,@PathVariable("userName") String userName) throws GoForWealthAdminException{
		logger.info("In searchUserByKeyword()");
		GoForWealthAdminResponseInfo responseInfo=null;
		if(authentication != null){
			UserPrincipal usersession = (UserPrincipal) authentication.getPrincipal();
			if(usersession != null){
				User user = userRepository.findUserByRole(usersession.getUser().getUserId(), "ADMIN");
				if(user != null){
					List<UsersListDTO> usersList=goForWealthAdminService.searchUserByKeyword(userName,usersession.getUser().getUserId());
					if(!usersList.isEmpty()) {
						responseInfo = GoForWealthAdminUtil.getSuccessResponseInfo(GoForWealthAdminErrorMessageEnum.SUCCESS_CODE.getValue(), GoForWealthAdminErrorMessageEnum.SUCCESS_MESSAGE.getValue(), usersList);
					} else {
						responseInfo = new GoForWealthAdminResponseInfo();
						responseInfo.setStatus(GoForWealthPRSErrorMessageEnum.DATA_NOT_FOUND_CODE.getValue());
						responseInfo.setMessage(GoForWealthPRSErrorMessageEnum.DATA_NOT_FOUND_MESSAGE.getValue());
					}
				}else{
					responseInfo = GoForWealthAdminUtil.getSuccessResponseInfo(GoForWealthErrorMessageEnum.ACCESS_DENIED_CODE.getValue(),GoForWealthErrorMessageEnum.ACCESS_DENIED_MESSAGE.getValue(), null);
				}
			}else{
				responseInfo = GoForWealthAdminUtil.getSuccessResponseInfo(GoForWealthErrorMessageEnum.ACCESS_DENIED_CODE.getValue(),GoForWealthErrorMessageEnum.ACCESS_DENIED_MESSAGE.getValue(), null);
			}
		}else{
			responseInfo = GoForWealthAdminUtil.getSuccessResponseInfo(GoForWealthErrorMessageEnum.ACCESS_DENIED_CODE.getValue(),GoForWealthErrorMessageEnum.ACCESS_DENIED_MESSAGE.getValue(), null);
		}
		logger.info("Out searchUserByKeyword()");
		return responseInfo;
	}
			
	@RequestMapping(value="/getUserTransactions/{userId}", method=RequestMethod.GET)
	public GoForWealthPRSResponseInfo getTransactionsByUserId(@PathVariable("userId") int userId, Authentication auth) {
		logger.info("In getUserTransactions()== userID === "+userId);
		UserPrincipal userSession = (UserPrincipal)auth.getPrincipal();
		GoForWealthPRSResponseInfo responseInfo;
		List<AddToCartDTO> cartOrderList = new ArrayList<>();
		if(userSession!=null){			
			cartOrderList = goForWealthFundSchemeService.getOrderTransactionForAdmin(userId);
		}
		responseInfo = GoForWealthPRSUtil.getSuccessResponseInfo(GoForWealthPRSErrorMessageEnum.SUCCESS_CODE.getValue(), GoForWealthPRSErrorMessageEnum.SUCCESS_MESSAGE.getValue(),cartOrderList);
		logger.info("Out getUserTransactions()");
		return responseInfo;
	}

	@CrossOrigin
	@RequestMapping(value = "upload/schemePriority/savePrioritySchemes", produces = "application/json", method = RequestMethod.POST)
	public GoForWealthAdminResponseInfo savePrioritySchemes(@RequestBody List<FundSchemeDTO> fundSchemeDTO) throws GoForWealthSIPException {
		GoForWealthAdminResponseInfo goForWealthAdminResponseInfo = new GoForWealthAdminResponseInfo();
		boolean flag = goForWealthAdminService.updateSchemeWithPriority(fundSchemeDTO);
		if(flag){
			goForWealthAdminResponseInfo.setStatus(GoForWealthAdminErrorMessageEnum.SUCCESS_CODE.getValue());
			goForWealthAdminResponseInfo.setMessage(GoForWealthAdminErrorMessageEnum.SUCCESS_MESSAGE.getValue());
		}else{
			goForWealthAdminResponseInfo.setStatus(GoForWealthAdminErrorMessageEnum.COMMON_ERROR_CODE.getValue());
			goForWealthAdminResponseInfo.setMessage(GoForWealthAdminErrorMessageEnum.INTERNAL_SERVER_ERROR.getValue());
		}
		return goForWealthAdminResponseInfo;
	}		

	@RequestMapping(value = "/getAllOrders/{count}", method = RequestMethod.GET)
	public GoForWealthAdminResponseInfo getAllOrders(@PathVariable("count") int count,Authentication authentication) throws GoForWealthAdminException {
		logger.info("In getAllOrders() count === "+count);
		GoForWealthAdminResponseInfo responseInfo = null;
		if (authentication != null) {
			UserPrincipal usersession = (UserPrincipal) authentication.getPrincipal();
			if (usersession != null) {
				User user = userRepository.findUserByRole(usersession.getUser().getUserId(), "ADMIN");
				if (user != null) {
					List<OrdersDTO> cartOrderList = new ArrayList<>();
					try {
						cartOrderList = goForWealthAdminService.getAllOrders(count);
						if (!cartOrderList.isEmpty()) {
							responseInfo = GoForWealthAdminUtil.getSuccessResponseInfo(GoForWealthAdminErrorMessageEnum.SUCCESS_CODE.getValue(),GoForWealthAdminErrorMessageEnum.SUCCESS_MESSAGE.getValue(), cartOrderList);
						} else {
							responseInfo = new GoForWealthAdminResponseInfo();
							responseInfo.setStatus(GoForWealthPRSErrorMessageEnum.DATA_NOT_FOUND_CODE.getValue());
							responseInfo.setMessage(GoForWealthPRSErrorMessageEnum.DATA_NOT_FOUND_MESSAGE.getValue());
						}
					} catch (Exception e) {
						return GoForWealthAdminUtil.getSuccessResponseInfo(GoForWealthAdminErrorMessageEnum.INTERNAL_SERVER_ERROR.getValue(),GoForWealthAdminErrorMessageEnum.INTERNAL_SERVER_ERROR.getValue(), null);
					}
				}else{
					responseInfo = GoForWealthAdminUtil.getSuccessResponseInfo(GoForWealthErrorMessageEnum.ACCESS_DENIED_CODE.getValue(),GoForWealthErrorMessageEnum.ACCESS_DENIED_MESSAGE.getValue(), null);
				}
			}else{
				responseInfo = GoForWealthAdminUtil.getSuccessResponseInfo(GoForWealthErrorMessageEnum.ACCESS_DENIED_CODE.getValue(),GoForWealthErrorMessageEnum.ACCESS_DENIED_MESSAGE.getValue(), null);
			}
		}else{
			responseInfo = GoForWealthAdminUtil.getSuccessResponseInfo(GoForWealthErrorMessageEnum.ACCESS_DENIED_CODE.getValue(),GoForWealthErrorMessageEnum.ACCESS_DENIED_MESSAGE.getValue(), null);
		}
		logger.info("Out getAllOrders()");
		return responseInfo;
	}

	@RequestMapping(value = "/getTotalNumberOfOrders", method = RequestMethod.GET)
	public GoForWealthAdminResponseInfo getTotalNumberOfOrders(Authentication authentication) throws GoForWealthAdminException {
		logger.info("In getTotalNumberOfOrders()");
		GoForWealthAdminResponseInfo responseInfo = null;
		if (authentication != null) {
			UserPrincipal usersession = (UserPrincipal) authentication.getPrincipal();
			if (usersession != null) {
				User user = userRepository.findUserByRole(usersession.getUser().getUserId(), "ADMIN");
				if (user != null) {
					try {
						int count = goForWealthAdminService.getTotalNumberOfOrders();
						if (count>0) {
							responseInfo = GoForWealthAdminUtil.getSuccessResponseInfo(GoForWealthAdminErrorMessageEnum.SUCCESS_CODE.getValue(),GoForWealthAdminErrorMessageEnum.SUCCESS_MESSAGE.getValue(), count);
						} else {
							responseInfo = new GoForWealthAdminResponseInfo();
							responseInfo.setStatus(GoForWealthPRSErrorMessageEnum.DATA_NOT_FOUND_CODE.getValue());
							responseInfo.setMessage(GoForWealthPRSErrorMessageEnum.DATA_NOT_FOUND_MESSAGE.getValue());
						}
					} catch (Exception e) {
						return GoForWealthAdminUtil.getSuccessResponseInfo(GoForWealthAdminErrorMessageEnum.INTERNAL_SERVER_ERROR.getValue(),GoForWealthAdminErrorMessageEnum.INTERNAL_SERVER_ERROR.getValue(), null);
					}
				}else{
					responseInfo = GoForWealthAdminUtil.getSuccessResponseInfo(GoForWealthErrorMessageEnum.ACCESS_DENIED_CODE.getValue(),GoForWealthErrorMessageEnum.ACCESS_DENIED_MESSAGE.getValue(), null);
				}
			}else{
				responseInfo = GoForWealthAdminUtil.getSuccessResponseInfo(GoForWealthErrorMessageEnum.ACCESS_DENIED_CODE.getValue(),GoForWealthErrorMessageEnum.ACCESS_DENIED_MESSAGE.getValue(), null);
			}
		}else{
			responseInfo = GoForWealthAdminUtil.getSuccessResponseInfo(GoForWealthErrorMessageEnum.ACCESS_DENIED_CODE.getValue(),GoForWealthErrorMessageEnum.ACCESS_DENIED_MESSAGE.getValue(), null);
		}
		logger.info("Out getTotalNumberOfOrders()");
		return responseInfo;
	}

	@PostMapping("/uploadVideoViaBase64")
	public GoForWealthAdminResponseInfo uploadVideoViaBase64(@RequestBody SchemeUploadRequest fileDetails, Authentication auth) throws IOException, GoForWealthPRSException, GoForWealthAdminException{
		logger.info("Inside the uploadVideoViaBase64");
		GoForWealthAdminResponseInfo responseInfo = null;
		UserPrincipal userSession = (UserPrincipal)auth.getPrincipal();
		String response = null;
		if(userSession!=null){
			User user = userRepository.findUserByRole(userSession.getUser().getUserId(), "ADMIN");
			if(user != null){
				response = goForWealthAdminService.uploadVideoViaBase64(userSession.getUser().getUserId(), fileDetails);
				responseInfo = GoForWealthAdminUtil.getSuccessResponseInfo(GoForWealthPRSErrorMessageEnum.SUCCESS_CODE.getValue(),GoForWealthPRSErrorMessageEnum.SUCCESS_MESSAGE.getValue(),response);
			}else{
				responseInfo = GoForWealthAdminUtil.getSuccessResponseInfo(GoForWealthErrorMessageEnum.ACCESS_DENIED_CODE.getValue(),GoForWealthErrorMessageEnum.ACCESS_DENIED_MESSAGE.getValue(),null);
			}
		}else{
			responseInfo = GoForWealthAdminUtil.getSuccessResponseInfo(GoForWealthErrorMessageEnum.ACCESS_DENIED_CODE.getValue(),GoForWealthErrorMessageEnum.ACCESS_DENIED_MESSAGE.getValue(),null);
		}
		logger.info("Out the uploadVideoViaBase64");
		return responseInfo;
	}

	@PostMapping("/saveHomeVideoUrl")
	public GoForWealthAdminResponseInfo saveHomeVideoUrl(@RequestBody SchemeUploadRequest fileDetails, Authentication auth) throws IOException, GoForWealthPRSException, GoForWealthAdminException{
		logger.info("Inside the saveHomeVideoUrl"); 
		GoForWealthAdminResponseInfo responseInfo = null;
		UserPrincipal userSession = (UserPrincipal)auth.getPrincipal();
		String response = null;
		String videoUrl=fileDetails.getFileName();
		if(userSession!=null){
			User user = userRepository.findUserByRole(userSession.getUser().getUserId(), "ADMIN");
			if(user != null){
				response = goForWealthAdminService.saveHomeVideoUrl(userSession.getUser().getUserId(), videoUrl);
				responseInfo = GoForWealthAdminUtil.getSuccessResponseInfo(GoForWealthPRSErrorMessageEnum.SUCCESS_CODE.getValue(),GoForWealthPRSErrorMessageEnum.SUCCESS_MESSAGE.getValue(),response);
			}else{
				responseInfo = GoForWealthAdminUtil.getSuccessResponseInfo(GoForWealthErrorMessageEnum.ACCESS_DENIED_CODE.getValue(),GoForWealthErrorMessageEnum.ACCESS_DENIED_MESSAGE.getValue(),null);
			}
		}else{
			responseInfo = GoForWealthAdminUtil.getSuccessResponseInfo(GoForWealthErrorMessageEnum.ACCESS_DENIED_CODE.getValue(),GoForWealthErrorMessageEnum.ACCESS_DENIED_MESSAGE.getValue(),null);
		}
		logger.info("Out the saveHomeVideoUrl");
		return responseInfo;
	}

	@RequestMapping(value = "/getHomeVideoUrl", method = RequestMethod.GET)
	public GoForWealthAdminResponseInfo getHomeVideoUrl() throws IOException, GoForWealthAdminException {
		logger.info("Inside the getHomeVideoUrl"); 
		GoForWealthAdminResponseInfo responseInfo = null;
		String response = null;
		response = goForWealthAdminService.getHomeVideoUrl();
		if(response!=null){
			responseInfo = GoForWealthAdminUtil.getSuccessResponseInfo(GoForWealthAdminErrorMessageEnum.SUCCESS_CODE.getValue(),GoForWealthAdminErrorMessageEnum.SUCCESS_MESSAGE.getValue(), response);
		}else{
			responseInfo = new GoForWealthAdminResponseInfo();
			responseInfo.setStatus(GoForWealthPRSErrorMessageEnum.DATA_NOT_FOUND_CODE.getValue());
			responseInfo.setMessage(GoForWealthPRSErrorMessageEnum.DATA_NOT_FOUND_MESSAGE.getValue());
		}
		logger.info("Out the getHomeVideoUrl");
		return responseInfo;
	}

	@GetMapping("/showHomeVideo")
	public void showHomeVideo(HttpServletResponse res) throws IOException {
		logger.info("showHomeVideo called");
		String location = goForWealthAdminService.getHomeVideoUrl();
		res.setContentType("video/mp4");
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

	@RequestMapping(value = "/getCategoryList")
	public GoForWealthAdminResponseInfo getCategoryList(Authentication authentication){
		logger.info("In getCategoryList()");
		GoForWealthAdminResponseInfo responseInfo=null;
		List<CategoryListResponseDTO> categoryListResponseDTOsList=goForWealthAdminService.getCategoryList();
		responseInfo =GoForWealthAdminUtil.getSuccessResponseInfo(GoForWealthAdminErrorMessageEnum.SUCCESS_CODE.getValue(),GoForWealthAdminErrorMessageEnum.SUCCESS_MESSAGE.getValue(), categoryListResponseDTOsList);
		logger.info("Out getCategoryList()");
		return responseInfo;
	}

	@CrossOrigin
	@RequestMapping(value = "upload/updateAmfi/updateSchemes", produces = "application/json", method = RequestMethod.POST)
	public GoForWealthAdminResponseInfo updateAmfi(@RequestBody List<FundSchemeDTO> fundSchemeDTO) throws GoForWealthSIPException {
		logger.info("In updateAmfi()");
		GoForWealthAdminResponseInfo goForWealthAdminResponseInfo = new GoForWealthAdminResponseInfo();
		boolean flag = goForWealthAdminService.updateAmfi(fundSchemeDTO);
		if(flag){
			goForWealthAdminResponseInfo.setStatus(GoForWealthAdminErrorMessageEnum.SUCCESS_CODE.getValue());
			goForWealthAdminResponseInfo.setMessage(GoForWealthAdminErrorMessageEnum.SUCCESS_MESSAGE.getValue());
		}else{
			goForWealthAdminResponseInfo.setStatus(GoForWealthAdminErrorMessageEnum.COMMON_ERROR_CODE.getValue());
			goForWealthAdminResponseInfo.setMessage(GoForWealthAdminErrorMessageEnum.INTERNAL_SERVER_ERROR.getValue());
		}
		logger.info("Out updateAmfi()");
		return goForWealthAdminResponseInfo;
	}

	@CrossOrigin
	@RequestMapping(value = "upload/updateAmfiWithIsin", produces = "application/json", method = RequestMethod.POST)
	public GoForWealthAdminResponseInfo updateAmfiWithIsin(@RequestBody List<FundSchemeDTO> fundSchemeDTO,Authentication auth) throws GoForWealthSIPException {
		logger.info("In updateAmfiWithIsin()");
		GoForWealthAdminResponseInfo goForWealthAdminResponseInfo =  new GoForWealthAdminResponseInfo();
		UserPrincipal userSession = (UserPrincipal)auth.getPrincipal();
		boolean flag = false;
		if(userSession != null){
			User user = userRepository.findUserByRole(userSession.getUser().getUserId(),"ADMIN");
			if(user != null){
				flag = goForWealthAdminService.updateAmfiWithIsin(fundSchemeDTO);
				if(flag){
					goForWealthAdminResponseInfo.setStatus(GoForWealthAdminErrorMessageEnum.SUCCESS_CODE.getValue());
					goForWealthAdminResponseInfo.setMessage(GoForWealthAdminErrorMessageEnum.SUCCESS_MESSAGE.getValue());
				}else{
					goForWealthAdminResponseInfo.setStatus(GoForWealthAdminErrorMessageEnum.COMMON_ERROR_CODE.getValue());
					goForWealthAdminResponseInfo.setMessage(GoForWealthAdminErrorMessageEnum.INTERNAL_SERVER_ERROR.getValue());
				}
			}else{
				goForWealthAdminResponseInfo = GoForWealthAdminUtil.getSuccessResponseInfo(GoForWealthErrorMessageEnum.ACCESS_DENIED_CODE.getValue(),GoForWealthErrorMessageEnum.ACCESS_DENIED_MESSAGE.getValue(), null);
			}
		}else{
			goForWealthAdminResponseInfo = GoForWealthAdminUtil.getSuccessResponseInfo(GoForWealthErrorMessageEnum.ACCESS_DENIED_CODE.getValue(),GoForWealthErrorMessageEnum.ACCESS_DENIED_MESSAGE.getValue(), null);
		}
		logger.info("Out updateAmfiWithIsin()");
		return goForWealthAdminResponseInfo;
	}

	@CrossOrigin
	@RequestMapping(value = "upload/modelportfolio",method = RequestMethod.POST)
	public GoForWealthAdminResponseInfo uploadModelPortfolioData(@RequestBody List<FundSchemeDTO> fundSchemeDTO,Authentication auth) {
		logger.info("In uploadPortfolioData()");
		GoForWealthAdminResponseInfo goForWealthAdminResponseInfo = new GoForWealthAdminResponseInfo();
		UserPrincipal userSession = (UserPrincipal)auth.getPrincipal();
		if(userSession != null){
			User user = userRepository.findUserByRole(userSession.getUser().getUserId(),"ADMIN");
			if(user != null){
				boolean flag = goForWealthAdminService.uploadModelPortfolioData(fundSchemeDTO);
				if(flag){
					goForWealthAdminResponseInfo.setStatus(GoForWealthAdminErrorMessageEnum.SUCCESS_CODE.getValue());
					goForWealthAdminResponseInfo.setMessage(GoForWealthAdminErrorMessageEnum.SUCCESS_MESSAGE.getValue());
				}else{
					goForWealthAdminResponseInfo.setStatus(GoForWealthAdminErrorMessageEnum.COMMON_ERROR_CODE.getValue());
					goForWealthAdminResponseInfo.setMessage(GoForWealthAdminErrorMessageEnum.INTERNAL_SERVER_ERROR.getValue());
				}
			}else{
				goForWealthAdminResponseInfo = GoForWealthAdminUtil.getSuccessResponseInfo(GoForWealthErrorMessageEnum.ACCESS_DENIED_CODE.getValue(),GoForWealthErrorMessageEnum.ACCESS_DENIED_MESSAGE.getValue(),null);
			}
		}else{
			goForWealthAdminResponseInfo = GoForWealthAdminUtil.getSuccessResponseInfo(GoForWealthErrorMessageEnum.ACCESS_DENIED_CODE.getValue(),GoForWealthErrorMessageEnum.ACCESS_DENIED_MESSAGE.getValue(),null);
		}
		logger.info("Out uploadPortfolioData()");
		return goForWealthAdminResponseInfo;
	}

	@PostMapping("/getUser")
	public GoForWealthAdminResponseInfo getUser(Authentication auth) throws GoForWealthAdminException, IOException {			
		logger.info("In getUser()");
		GoForWealthAdminResponseInfo goForWealthAdminResponseInfo = null;
		UserLoginResponseDTO userLoginResponseDTO = null;
		if(auth!=null){
			UserPrincipal userSession = (UserPrincipal)auth.getPrincipal();
		    if (userSession != null) {
		    	User user = userRepository.findUserByRole(userSession.getUser().getUserId(), "ADMIN");
		    	if(user != null){
		    		userLoginResponseDTO = goForWealthAdminService.getUser(userSession.getUser().getUserId());
		    		goForWealthAdminResponseInfo = GoForWealthAdminUtil.getSuccessResponseInfo(GoForWealthErrorMessageEnum.SUCCESS_CODE.getValue(), GoForWealthErrorMessageEnum.SUCCESS_MESSAGE.getValue(), userLoginResponseDTO);
		    	}else{
		    		goForWealthAdminResponseInfo = GoForWealthAdminUtil.getSuccessResponseInfo(GoForWealthErrorMessageEnum.ACCESS_DENIED_CODE.getValue(), GoForWealthErrorMessageEnum.ACCESS_DENIED_MESSAGE.getValue(), userLoginResponseDTO);
		    	}
		    }else{
		    	goForWealthAdminResponseInfo = GoForWealthAdminUtil.getSuccessResponseInfo(GoForWealthErrorMessageEnum.ACCESS_DENIED_CODE.getValue(),GoForWealthErrorMessageEnum.ACCESS_DENIED_MESSAGE.getValue(), userLoginResponseDTO);
		    }
		}else{
	    	goForWealthAdminResponseInfo = GoForWealthAdminUtil.getSuccessResponseInfo(GoForWealthErrorMessageEnum.ACCESS_DENIED_CODE.getValue(),GoForWealthErrorMessageEnum.ACCESS_DENIED_MESSAGE.getValue(), userLoginResponseDTO);
	    }
		logger.info("Out getUser()");
		return goForWealthAdminResponseInfo;
	}

	@PostMapping("/updateKycstatus/{userId}")
	public GoForWealthAdminResponseInfo updateKycstatus(@PathVariable("userId") int userId,Authentication authentication) throws GoForWealthAdminException{
		logger.info("In updateKycstatus()");
		String response = "";
		GoForWealthAdminResponseInfo goForWealthAdminResponseInfo = null;
		
		UserPrincipal userSession = (UserPrincipal) authentication.getPrincipal();
		if(userSession != null){
			User user = userRepository.findUserByRole(userSession.getUser().getUserId(), "ADMIN");
			if(user != null){
				response = goForWealthAdminService.updateKycStatus(userId);
				goForWealthAdminResponseInfo = GoForWealthAdminUtil.getSuccessResponseInfo(GoForWealthErrorMessageEnum.SUCCESS_CODE.getValue(), GoForWealthErrorMessageEnum.SUCCESS_MESSAGE.getValue(), response);
			}else{
				goForWealthAdminResponseInfo = GoForWealthAdminUtil.getSuccessResponseInfo(GoForWealthErrorMessageEnum.ACCESS_DENIED_CODE.getValue(),
		    			GoForWealthErrorMessageEnum.ACCESS_DENIED_MESSAGE.getValue(), response);
			}
		}else{
			goForWealthAdminResponseInfo = GoForWealthAdminUtil.getSuccessResponseInfo(GoForWealthErrorMessageEnum.ACCESS_DENIED_CODE.getValue(),
	    			GoForWealthErrorMessageEnum.ACCESS_DENIED_MESSAGE.getValue(), response);
		}
		logger.info("Out updateKycstatus()");
		return goForWealthAdminResponseInfo;
		
	}
	
	
	 /**
	  * 
	  * @return
	  * @throws IOException
	 * @throws GoForWealthAdminException 
	  */
	 @PostMapping("/updateGoalsSequence")
	 public GoForWealthAdminResponseInfo updateGoalsSequence(@RequestBody List<SchemeUploadRequest> goalDtoList,Authentication authentication) throws IOException, GoForWealthAdminException {
	 	 logger.info("In updateGoalsSequence()");
	 	 GoForWealthAdminResponseInfo goForWealthAdminResponseInfo = null;
	 	 UserPrincipal userSession = (UserPrincipal) authentication.getPrincipal();
	 	 String response = "";
	 	 if(userSession !=null){
	 		 User user = userRepository.findUserByRole(userSession.getUser().getUserId(), "ADMIN");
	 		 if(user != null){
	 			 response = goForWealthAdminService.updateGoalSequence(goalDtoList);
	 			 goForWealthAdminResponseInfo = GoForWealthAdminUtil.getSuccessResponseInfo(GoForWealthErrorMessageEnum.SUCCESS_CODE.getValue(), GoForWealthErrorMessageEnum.SUCCESS_MESSAGE.getValue(), response);
	 		 }else{
				goForWealthAdminResponseInfo = GoForWealthAdminUtil.getSuccessResponseInfo(GoForWealthErrorMessageEnum.ACCESS_DENIED_CODE.getValue(), GoForWealthErrorMessageEnum.ACCESS_DENIED_MESSAGE.getValue(), response);
	 		 }
	 	 }else{
	    	 goForWealthAdminResponseInfo = GoForWealthAdminUtil.getSuccessResponseInfo(GoForWealthErrorMessageEnum.ACCESS_DENIED_CODE.getValue(), GoForWealthErrorMessageEnum.ACCESS_DENIED_MESSAGE.getValue(),response);
	     }
	 	 logger.info("Out updateGoalsSequence()");
	 	 return goForWealthAdminResponseInfo;
	 }


	 /**
	   * 
	   * @param navUploadRequest
	   * @return GoForWealthAdminResponseInfo
	 */
	 @RequestMapping(value = "/uploadNav/saveNavTextFileDataToDbUsingBase64", produces = "application/json", method = RequestMethod.POST)
	 public GoForWealthAdminResponseInfo saveNavTextFileDataToDbUsingBase64(@RequestBody SchemeUploadRequest navUploadRequest, Authentication auth) {
		 logger.info("In saveNavTextFileDataToDbUsingBase64()");
		 GoForWealthAdminResponseInfo responseInfo=null;
		 UserPrincipal usersession = (UserPrincipal) auth.getPrincipal();
		 if(usersession != null){
			 User user = userRepository.findUserByRole(usersession.getUser().getUserId(), "ADMIN");
		  	 if(user != null){
		  	 	 try {
		  	 		if(navUploadRequest.getBase64()==null){
		  	 			return GoForWealthAdminUtil.getErrorResponseInfo(GoForWealthAdminErrorMessageEnum.DATA_NOT_FOUND_CODE.getValue(), GoForWealthAdminErrorMessageEnum.DATA_NOT_FOUND_MESSAGE.getValue(), null);
		  	 		}
		  	 		String base64=navUploadRequest.getBase64();
		  	 		byte[] byteArray = Base64.decodeBase64(base64);
		  	 		//goForWealthFundSchemeService.addSchemeForSipviaBase64(byteArray);
		  	 		//boolean flag = goForWealthFundSchemeService.addSchemeviaBase64(byteArray);
		  	 		goForWealthFundSchemeService.uploadNavDateAndCurrentNavForSchemeViaTxt(byteArray);
		  	 	 }catch(Exception e) {
		  				responseInfo = GoForWealthAdminUtil.getSuccessResponseInfo(GoForWealthAdminErrorMessageEnum.INTERNAL_SERVER_ERROR.getValue(), GoForWealthAdminErrorMessageEnum.INTERNAL_SERVER_ERROR.getValue(),null);
		  		 }
		  		 responseInfo = GoForWealthAdminUtil.getSuccessResponseInfo(GoForWealthAdminErrorMessageEnum.SUCCESS_CODE.getValue(), GoForWealthAdminErrorMessageEnum.SUCCESS_MESSAGE.getValue(),null);
		  		 logger.info("Out saveNavTextFileDataToDbUsingBase64()");
		  	 }else{
		  		 responseInfo = GoForWealthAdminUtil.getSuccessResponseInfo(GoForWealthErrorMessageEnum.ACCESS_DENIED_CODE.getValue(), GoForWealthErrorMessageEnum.ACCESS_DENIED_MESSAGE.getValue(), null);
		  	 }
		 }else{
			 responseInfo = GoForWealthAdminUtil.getSuccessResponseInfo(GoForWealthErrorMessageEnum.ACCESS_DENIED_CODE.getValue(), GoForWealthErrorMessageEnum.ACCESS_DENIED_MESSAGE.getValue(), null);
		 }
		 return responseInfo;
	  }

	 
	 @RequestMapping(value = "/uploadLumpsumSchemeTextFile", produces = "application/json", method = RequestMethod.POST)
		public GoForWealthAdminResponseInfo uploadLumpsumSchemeTextFile(@RequestBody SchemeUploadRequest schemeUploadRequest, Authentication auth) {
		  	logger.info("In saveSchemeTextFileDataToDbUsingBase64()");
		  	GoForWealthAdminResponseInfo responseInfo=null;
		  	UserPrincipal usersession = (UserPrincipal) auth.getPrincipal();
		  	if(usersession != null){
		  		User user = userRepository.findUserByRole(usersession.getUser().getUserId(), "ADMIN");
		  		if(user != null){
		  			
		  				if(schemeUploadRequest.getBase64()==null){
		  					return GoForWealthAdminUtil.getErrorResponseInfo(GoForWealthAdminErrorMessageEnum.DATA_NOT_FOUND_CODE.getValue(), GoForWealthAdminErrorMessageEnum.DATA_NOT_FOUND_MESSAGE.getValue(), null);
		  				}
		  				String base64=schemeUploadRequest.getBase64();
		  				byte[] byteArray = Base64.decodeBase64(base64);
		  				
		  				String response = goForWealthFundSchemeService.uploadLumpsumSchemeTextFile(byteArray);
		  				
		  				responseInfo = GoForWealthAdminUtil.getSuccessResponseInfo(GoForWealthAdminErrorMessageEnum.SUCCESS_CODE.getValue(), GoForWealthAdminErrorMessageEnum.SUCCESS_MESSAGE.getValue(),response);
		  				
		  			logger.info("Out savesSchemeTextFileDataToDb()");
		  		}else{
		  			responseInfo = GoForWealthAdminUtil.getSuccessResponseInfo(GoForWealthErrorMessageEnum.ACCESS_DENIED_CODE.getValue(), 
							GoForWealthErrorMessageEnum.ACCESS_DENIED_MESSAGE.getValue(), null);
		  		}
		  	}else{
		  		responseInfo = GoForWealthAdminUtil.getSuccessResponseInfo(GoForWealthErrorMessageEnum.ACCESS_DENIED_CODE.getValue(), 
						GoForWealthErrorMessageEnum.ACCESS_DENIED_MESSAGE.getValue(), null);
		  	}
			
			return responseInfo;
	    }
	 
	 @RequestMapping(value = "/uploadSipSchemeTextFile", produces = "application/json", method = RequestMethod.POST)
		public GoForWealthAdminResponseInfo uploadSipSchemeTextFile(@RequestBody SchemeUploadRequest schemeUploadRequest, Authentication auth) {
		  	logger.info("In saveSchemeTextFileDataToDbUsingBase64()");
		  	GoForWealthAdminResponseInfo responseInfo=null;
		  	UserPrincipal usersession = (UserPrincipal) auth.getPrincipal();
		  	if(usersession != null){
		  		User user = userRepository.findUserByRole(usersession.getUser().getUserId(), "ADMIN");
		  		if(user != null){
		  			
		  				if(schemeUploadRequest.getBase64()==null){
		  					return GoForWealthAdminUtil.getErrorResponseInfo(GoForWealthAdminErrorMessageEnum.DATA_NOT_FOUND_CODE.getValue(), GoForWealthAdminErrorMessageEnum.DATA_NOT_FOUND_MESSAGE.getValue(), null);
		  				}
		  				String base64=schemeUploadRequest.getBase64();
		  				byte[] byteArray = Base64.decodeBase64(base64);
		  				
		  				String response = goForWealthFundSchemeService.uploadSipSchemeTextFile(byteArray);
		  				
		  				responseInfo = GoForWealthAdminUtil.getSuccessResponseInfo(GoForWealthAdminErrorMessageEnum.SUCCESS_CODE.getValue(), GoForWealthAdminErrorMessageEnum.SUCCESS_MESSAGE.getValue(),response);
		  				
		  			logger.info("Out savesSchemeTextFileDataToDb()");
		  		}else{
		  			responseInfo = GoForWealthAdminUtil.getSuccessResponseInfo(GoForWealthErrorMessageEnum.ACCESS_DENIED_CODE.getValue(), 
							GoForWealthErrorMessageEnum.ACCESS_DENIED_MESSAGE.getValue(), null);
		  		}
		  	}else{
		  		responseInfo = GoForWealthAdminUtil.getSuccessResponseInfo(GoForWealthErrorMessageEnum.ACCESS_DENIED_CODE.getValue(), 
						GoForWealthErrorMessageEnum.ACCESS_DENIED_MESSAGE.getValue(), null);
		  	}
			
			return responseInfo;
	    }

	/* 
	 @RequestMapping(value = "/uploadSchemeWithICRAPassingAmfiCode", produces = "application/json", method = RequestMethod.POST)
		public GoForWealthAdminResponseInfo uploadSchemeWithICRAPassingAmfiCode(Authentication auth) throws JSONException {
		  	logger.info("In saveSchemeTextFileDataToDbUsingBase64()");
		  	GoForWealthAdminResponseInfo responseInfo=null;
		  	UserPrincipal usersession = (UserPrincipal) auth.getPrincipal();
		  	if(usersession != null){
		  		User user = userRepository.findUserByRole(usersession.getUser().getUserId(), "ADMIN");
		  		if(user != null){
		  			
		  				String response = goForWealthFundSchemeService.uploadSchemeWithICRAPassingAmfiCode();
		  				
		  				responseInfo = GoForWealthAdminUtil.getSuccessResponseInfo(GoForWealthAdminErrorMessageEnum.SUCCESS_CODE.getValue(), GoForWealthAdminErrorMessageEnum.SUCCESS_MESSAGE.getValue(),response);
		  			
		  		}else{
		  			responseInfo = GoForWealthAdminUtil.getSuccessResponseInfo(GoForWealthErrorMessageEnum.ACCESS_DENIED_CODE.getValue(), 
							GoForWealthErrorMessageEnum.ACCESS_DENIED_MESSAGE.getValue(), null);
		  		}
		  	}else{
		  		responseInfo = GoForWealthAdminUtil.getSuccessResponseInfo(GoForWealthErrorMessageEnum.ACCESS_DENIED_CODE.getValue(), 
						GoForWealthErrorMessageEnum.ACCESS_DENIED_MESSAGE.getValue(), null);
		 ` 	}
			return responseInfo;
	    }*/
	 
	 
	 @PostMapping("/getImagess")
	 public GoForWealthAdminResponseInfo getImages(@RequestBody UserInfoDto userInfoDto,Authentication auth) throws GoForWealthPRSException {
		logger.info("In getImages()");
		UserPrincipal userSession = (UserPrincipal)auth.getPrincipal();
		GoForWealthAdminResponseInfo responseInfo = null;
		if(userSession!=null){
				User user = userRepository.findUserByRole(userSession.getUser().getUserId(), "ADMIN");
				if(user != null){
					String location = goForWealthPRSEmandateService.showImage(userInfoDto.getUserId(),userInfoDto.getImageType());
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
						responseInfo=GoForWealthAdminUtil.getSuccessResponseInfo(GoForWealthSIPErrorMessageEnum.SUCCESS_CODE.getValue(), GoForWealthSIPErrorMessageEnum.SUCCESS_MESSAGE.getValue(), imageDataString);
					}else{
						responseInfo=new GoForWealthAdminResponseInfo();
						responseInfo.setMessage(GoForWealthSIPErrorMessageEnum.DATA_NOT_FOUND_MESSAGE.getValue());
						responseInfo.setStatus(GoForWealthSIPErrorMessageEnum.DATA_NOT_FOUND_CODE.getValue());
						responseInfo.setTotalRecords(0);
					}
				}
				else{
					responseInfo=GoForWealthAdminUtil.getSuccessResponseInfo(GoForWealthSIPErrorMessageEnum.ACCESS_DENIED_CODE.getValue(), GoForWealthSIPErrorMessageEnum.ACCESS_DENIED_MESSAGE.getValue(),null);
				}				
			}
		else{
				responseInfo=GoForWealthAdminUtil.getSuccessResponseInfo(GoForWealthSIPErrorMessageEnum.ACCESS_DENIED_CODE.getValue(), GoForWealthSIPErrorMessageEnum.ACCESS_DENIED_MESSAGE.getValue(),null);
		}
			logger.info("Out getImages()");
			return responseInfo;
	 }

	 @GetMapping("/getSchemeKeywordForSeo")
	 public GoForWealthAdminResponseInfo getSchemeKeywordForSeo(HttpServletRequest request) throws GoForWealthPRSException {
	 	 logger.info("In getSchemeKeywordForSeo()");
	 	 String headerValue = request.getHeader("NodeSchedulerAuth");
		 System.out.println("Header : " + headerValue);
		 GoForWealthAdminResponseInfo responseInfo = null;
		 if(headerValue.equals("BIS7CS3#SDVL#03NDND87GC34NP@N93H")){
			 List<SchemeKeywordWithSeoResponse> keywordList = goForWealthAdminService.getSchemeKeywordForSeo();
			 responseInfo=GoForWealthAdminUtil.getSuccessResponseInfo(GoForWealthSIPErrorMessageEnum.SUCCESS_CODE.getValue(),GoForWealthSIPErrorMessageEnum.SUCCESS_MESSAGE.getValue(),keywordList);			 
		 }else{
			 responseInfo=GoForWealthAdminUtil.getSuccessResponseInfo(GoForWealthSIPErrorMessageEnum.ACCESS_DENIED_CODE.getValue(),GoForWealthSIPErrorMessageEnum.ACCESS_DENIED_MESSAGE.getValue(),"");
		 }
		 logger.info("Out getSchemeKeywordForSeo()");
		 return responseInfo;
	 }

	 @RequestMapping(value = "/getUserEnquiry",method = RequestMethod.GET)
	 public GoForWealthAdminResponseInfo getUserEnquiry(Authentication auth) {
	 	 logger.info("In getUserEnquiry()");
	 	 GoForWealthAdminResponseInfo responseInfo=null;
	 	 List<UserEnquiryDto> userEnquiryDtoList = null;
	 	 UserPrincipal usersession = (UserPrincipal) auth.getPrincipal();
	 	 if(usersession != null){
	 	 	 User user = userRepository.findUserByRole(usersession.getUser().getUserId(), "ADMIN");
	 	  	 if(user != null){
	 	  		userEnquiryDtoList = goForWealthAdminService.getUserEnquiry();
	 	  	 	responseInfo = GoForWealthAdminUtil.getSuccessResponseInfo(GoForWealthAdminErrorMessageEnum.SUCCESS_CODE.getValue(), GoForWealthAdminErrorMessageEnum.SUCCESS_MESSAGE.getValue(),userEnquiryDtoList);
	 	  	 }else{
	 	  		 responseInfo = GoForWealthAdminUtil.getSuccessResponseInfo(GoForWealthErrorMessageEnum.ACCESS_DENIED_CODE.getValue(),GoForWealthErrorMessageEnum.ACCESS_DENIED_MESSAGE.getValue(), null);
	 	  	 }
	 	 }else{
	 		 responseInfo = GoForWealthAdminUtil.getSuccessResponseInfo(GoForWealthErrorMessageEnum.ACCESS_DENIED_CODE.getValue(),GoForWealthErrorMessageEnum.ACCESS_DENIED_MESSAGE.getValue(), null);
	 	 }
	 	 logger.info("Out getUserEnquiry()");
	 	 return responseInfo;
	 }
	 
	 @RequestMapping(value="/updateEnquiryStatus",method=RequestMethod.POST)
	 public GoForWealthAdminResponseInfo updateEnquiryStatus(@RequestBody UserEnquiryDto userEnquiryDto,Authentication auth) {
	 	 logger.info("In updateEnquiryStatus()");
	 	 GoForWealthAdminResponseInfo responseInfo=null;
	 	 UserPrincipal usersession = (UserPrincipal) auth.getPrincipal();
	 	 if(usersession != null){
	 	 	 User user = userRepository.findUserByRole(usersession.getUser().getUserId(), "ADMIN");
	 	  	 if(user != null){
	 	  		if(goForWealthAdminService.updateEnquiryStatus(userEnquiryDto))
	 	  			responseInfo = GoForWealthAdminUtil.getSuccessResponseInfo(GoForWealthAdminErrorMessageEnum.SUCCESS_CODE.getValue(), GoForWealthAdminErrorMessageEnum.SUCCESS_MESSAGE.getValue(),true);
	 	  		else
	 	  			responseInfo = GoForWealthAdminUtil.getSuccessResponseInfo(GoForWealthAdminErrorMessageEnum.INTERNAL_SERVER_ERROR.getValue(),GoForWealthAdminErrorMessageEnum.FAILURE_MESSAGE.getValue(),false);
	 	  	 }else{
	 	  		 responseInfo = GoForWealthAdminUtil.getSuccessResponseInfo(GoForWealthErrorMessageEnum.ACCESS_DENIED_CODE.getValue(),GoForWealthErrorMessageEnum.ACCESS_DENIED_MESSAGE.getValue(), null);
	 	  	 }
	 	 }else{
	 		 responseInfo = GoForWealthAdminUtil.getSuccessResponseInfo(GoForWealthErrorMessageEnum.ACCESS_DENIED_CODE.getValue(),GoForWealthErrorMessageEnum.ACCESS_DENIED_MESSAGE.getValue(), null);
	 	 }
	 	 logger.info("Out updateEnquiryStatus()");
	 	 return responseInfo;
	 }

	 @RequestMapping(value="/deleteEnquiry/{enquiryId}",method=RequestMethod.POST)
	 public GoForWealthAdminResponseInfo deleteUserEnquiry(@PathVariable Integer enquiryId, Authentication auth) {
	 	 logger.info("In deleteUserEnquiry()");
	 	 GoForWealthAdminResponseInfo responseInfo=null;
	 	 UserPrincipal usersession = (UserPrincipal) auth.getPrincipal();
	 	 if(usersession != null){
	 	 	 User user = userRepository.findUserByRole(usersession.getUser().getUserId(), "ADMIN");
	 	  	 if(user != null){
	 	  		if(goForWealthAdminService.deleteUserEnquiry(enquiryId))
	 	  			responseInfo = GoForWealthAdminUtil.getSuccessResponseInfo(GoForWealthAdminErrorMessageEnum.SUCCESS_CODE.getValue(), GoForWealthAdminErrorMessageEnum.SUCCESS_MESSAGE.getValue(),true);
	 	  		else
	 	  			responseInfo = GoForWealthAdminUtil.getSuccessResponseInfo(GoForWealthAdminErrorMessageEnum.INTERNAL_SERVER_ERROR.getValue(),GoForWealthAdminErrorMessageEnum.FAILURE_MESSAGE.getValue(),false);
	 	  	 }else{
	 	  		 responseInfo = GoForWealthAdminUtil.getSuccessResponseInfo(GoForWealthErrorMessageEnum.ACCESS_DENIED_CODE.getValue(),GoForWealthErrorMessageEnum.ACCESS_DENIED_MESSAGE.getValue(), null);
	 	  	 }
	 	 }else{
	 		 responseInfo = GoForWealthAdminUtil.getSuccessResponseInfo(GoForWealthErrorMessageEnum.ACCESS_DENIED_CODE.getValue(),GoForWealthErrorMessageEnum.ACCESS_DENIED_MESSAGE.getValue(), null);
	 	 }
	 	 logger.info("Out deleteUserEnquiry()");
	 	 return responseInfo;
	 }
	 
	 @GetMapping(value = "/api/export/excel/download")
	 public void exportUsersList(@RequestParam("token") String token,HttpServletResponse response,Authentication auth) throws GoForWealthAdminException {
	  	logger.info("In exportUsersList()");
	  	try {
	  		String subjectJson = Jwts.parser().setSigningKey(jwtConfiguration.secret.getBytes()).parseClaimsJws(token).getBody().getSubject();
	  		UserSession userSession = new ObjectMapper().readValue(subjectJson, UserSession.class);
	  		if(userSession != null){
	  			User user = userRepository.findUserByRole(userSession.getProfileId(),"ADMIN");
	  			if(user != null){
	  				List<UserExportExcelDataDto> usersList = goForWealthAdminService.getAllUsers("Registered User");
	  				Workbook wb = GoForWealthAdminUtil.createExportFileForUserData(usersList,"Registered User");
	  				response.setContentType("application/xls");
	  				response.setHeader("Content-Disposition", "attachment; filename=Registered_user_report_"+ GoForWealthAdminUtil.formatDateWithPattern(new Date(), "yyyyMMdd") + ".xls");
	  				wb.write(response.getOutputStream());
	  				response.flushBuffer();
	  			}else{
	  				throw new GoForWealthAdminException(GoForWealthAdminErrorMessageEnum.ACCESS_DENIED_CODE.getValue(),GoForWealthAdminErrorMessageEnum.ACCESS_DENIED_MESSAGE.getValue());
	  			}
	    	}else{
	    		throw new GoForWealthAdminException(GoForWealthAdminErrorMessageEnum.ACCESS_DENIED_CODE.getValue(),GoForWealthAdminErrorMessageEnum.ACCESS_DENIED_MESSAGE.getValue());
	    	}
	  	}catch(ExpiredJwtException ex){
	  		throw new GoForWealthAdminException(GoForWealthAdminErrorMessageEnum.COMMON_ERROR_CODE.getValue(),GoForWealthUMAConstants.TOKEN_EXPIRED);
		}catch(MalformedJwtException | SignatureException ex){
			throw new GoForWealthAdminException(GoForWealthAdminErrorMessageEnum.COMMON_ERROR_CODE.getValue(),GoForWealthUMAConstants.WRONG_TOKEN);
		}catch (IOException ex) {
			throw new GoForWealthAdminException(GoForWealthAdminErrorMessageEnum.COMMON_ERROR_CODE.getValue(),"IOError writing file to output stream");
		}catch(Exception ex){
			throw new GoForWealthAdminException(GoForWealthAdminErrorMessageEnum.COMMON_ERROR_CODE.getValue(), GoForWealthAdminErrorMessageEnum.INTERNAL_SERVER_ERROR.getValue());
		}
	 	logger.info("Out exportUsersList()");
	 }

	 @GetMapping(value = "/api/export/excel/download/confirmedOrders")
	 public void exportUsersConfirmedOrder(@RequestParam("token")String token,HttpServletResponse response,Authentication auth) throws GoForWealthAdminException {
	 	 logger.info("In exportUsersConfirmedOrder()");
	 	 try {
	 	 	 String subjectJson = Jwts.parser().setSigningKey(jwtConfiguration.secret.getBytes()).parseClaimsJws(token).getBody().getSubject();
	 	 	 UserSession userSession = new ObjectMapper().readValue(subjectJson, UserSession.class);
	 	 	 if(userSession != null){
	 	 	 	 User user = userRepository.findUserByRole(userSession.getProfileId(),"ADMIN");
	 	 	 	 if(user != null){
	 	 	 	 	 List<OrderExportExcelDto> ordersList = goForWealthAdminService.getConfirmedOrdersList("External","All");
	 	 	 	 	 Workbook wb = GoForWealthAdminUtil.createExportFileForOrders(ordersList,"Confirmed");
	 	 	 	 	 response.setContentType("application/xls");
	 	 	 	 	 response.setHeader("Content-Disposition", "attachment; filename=Confirmed_orders_report_"+ GoForWealthAdminUtil.formatDateWithPattern(new Date(), "yyyyMMdd") + ".xls");
	 	 	 	 	 wb.write(response.getOutputStream());
	 	 	 	 	 response.flushBuffer();
	 	 	 	 }else{
	 	 	 		 throw new GoForWealthAdminException(GoForWealthAdminErrorMessageEnum.ACCESS_DENIED_CODE.getValue(),GoForWealthAdminErrorMessageEnum.ACCESS_DENIED_MESSAGE.getValue());
	 	 	  	 }
	 	 	 }else{
	 	 		throw new GoForWealthAdminException(GoForWealthAdminErrorMessageEnum.ACCESS_DENIED_CODE.getValue(),GoForWealthAdminErrorMessageEnum.ACCESS_DENIED_MESSAGE.getValue());
	 	 	 }
	 	 }catch(ExpiredJwtException ex){
	 	 	 throw new GoForWealthAdminException(GoForWealthAdminErrorMessageEnum.COMMON_ERROR_CODE.getValue(),GoForWealthUMAConstants.TOKEN_EXPIRED);
	 	 }catch(MalformedJwtException | SignatureException ex){
	 	 	 throw new GoForWealthAdminException(GoForWealthAdminErrorMessageEnum.COMMON_ERROR_CODE.getValue(),GoForWealthUMAConstants.WRONG_TOKEN);
	 	 }catch (IOException ex) {
	 	 	 throw new GoForWealthAdminException(GoForWealthAdminErrorMessageEnum.COMMON_ERROR_CODE.getValue(),"IOError writing file to output stream");
	 	 }catch(Exception ex){
	 	 	 throw new GoForWealthAdminException(GoForWealthAdminErrorMessageEnum.COMMON_ERROR_CODE.getValue(), GoForWealthAdminErrorMessageEnum.INTERNAL_SERVER_ERROR.getValue());
	  	 }
	  	 logger.info("Out exportUsersConfirmedOrder()");
	 }
	 
	 @GetMapping(value = "/api/export/excel/download/purchasedOrders")
	 public void exportUsersPurchasedOrder(@RequestParam("token")String token,HttpServletResponse response,Authentication auth) throws GoForWealthAdminException {
	 	 logger.info("In exportUsersPurchasedOrder()");
	 	 try {
	 	 	 String subjectJson = Jwts.parser().setSigningKey(jwtConfiguration.secret.getBytes()).parseClaimsJws(token).getBody().getSubject();
	 	 	 UserSession userSession = new ObjectMapper().readValue(subjectJson, UserSession.class);
	 	 	 if(userSession != null){
	 	 	 	 User user = userRepository.findUserByRole(userSession.getProfileId(),"ADMIN");
	 	 	 	 if(user != null){
	 	 	 	 	 List<OrderExportExcelDto> ordersList = goForWealthAdminService.getPurchasedOrdersList("External","All");
	 	 	 	 	 Workbook wb = GoForWealthAdminUtil.createExportFileForOrders(ordersList,"Purchased");
	 	 	 	 	 response.setContentType("application/xls");
	 	 	 	 	 response.setHeader("Content-Disposition", "attachment; filename=Purchased_orders_report_"+ GoForWealthAdminUtil.formatDateWithPattern(new Date(), "yyyyMMdd") + ".xls");
	 	 	 	 	 wb.write(response.getOutputStream());
	 	 	 	 	 response.flushBuffer();
	 	 	 	 }else{
	 	 	 		 throw new GoForWealthAdminException(GoForWealthAdminErrorMessageEnum.ACCESS_DENIED_CODE.getValue(),GoForWealthAdminErrorMessageEnum.ACCESS_DENIED_MESSAGE.getValue());
	 	 	  	 }
	 	 	 }else{
	 	 		throw new GoForWealthAdminException(GoForWealthAdminErrorMessageEnum.ACCESS_DENIED_CODE.getValue(),GoForWealthAdminErrorMessageEnum.ACCESS_DENIED_MESSAGE.getValue());
	 	 	 }
	 	 }catch(ExpiredJwtException ex){
	 	 	 throw new GoForWealthAdminException(GoForWealthAdminErrorMessageEnum.COMMON_ERROR_CODE.getValue(),GoForWealthUMAConstants.TOKEN_EXPIRED);
	 	 }catch(MalformedJwtException | SignatureException ex){
	 	 	 throw new GoForWealthAdminException(GoForWealthAdminErrorMessageEnum.COMMON_ERROR_CODE.getValue(),GoForWealthUMAConstants.WRONG_TOKEN);
	 	 }catch (IOException ex) {
	 	 	 throw new GoForWealthAdminException(GoForWealthAdminErrorMessageEnum.COMMON_ERROR_CODE.getValue(),"IOError writing file to output stream");
	 	 }catch(Exception ex){
	 	 	 throw new GoForWealthAdminException(GoForWealthAdminErrorMessageEnum.COMMON_ERROR_CODE.getValue(), GoForWealthAdminErrorMessageEnum.INTERNAL_SERVER_ERROR.getValue());
	  	 }
	  	 logger.info("Out exportUsersPurchasedOrder()");
	 }
	 
	 @GetMapping(value = "/api/export/excel/download/canceledOrders")
	 public void exportUsersCanceledOrder(@RequestParam("token")String token,HttpServletResponse response,Authentication auth) throws GoForWealthAdminException {
	 	 logger.info("In exportUsersCanceledOrder()");
	 	 try {
	 	 	 String subjectJson = Jwts.parser().setSigningKey(jwtConfiguration.secret.getBytes()).parseClaimsJws(token).getBody().getSubject();
	 	 	 UserSession userSession = new ObjectMapper().readValue(subjectJson, UserSession.class);
	 	 	 if(userSession != null){
	 	 	 	 User user = userRepository.findUserByRole(userSession.getProfileId(),"ADMIN");
	 	 	 	 if(user != null){
	 	 	 	 	 List<OrderExportExcelDto> ordersList = goForWealthAdminService.getCancledOrdersList("External","All");
	 	 	 	 	 Workbook wb = GoForWealthAdminUtil.createExportFileForOrders(ordersList,"Canceled");
	 	 	 	 	 response.setContentType("application/xls");
	 	 	 	 	 response.setHeader("Content-Disposition", "attachment; filename=Canceled_orders_report_"+ GoForWealthAdminUtil.formatDateWithPattern(new Date(), "yyyyMMdd") + ".xls");
	 	 	 	 	 wb.write(response.getOutputStream());
	 	 	 	 	 response.flushBuffer();
	 	 	 	 }else{
	 	 	 		 throw new GoForWealthAdminException(GoForWealthAdminErrorMessageEnum.ACCESS_DENIED_CODE.getValue(),GoForWealthAdminErrorMessageEnum.ACCESS_DENIED_MESSAGE.getValue());
	 	 	  	 }
	 	 	 }else{
	 	 		throw new GoForWealthAdminException(GoForWealthAdminErrorMessageEnum.ACCESS_DENIED_CODE.getValue(),GoForWealthAdminErrorMessageEnum.ACCESS_DENIED_MESSAGE.getValue());
	 	 	 }
	 	 }catch(ExpiredJwtException ex){
	 	 	 throw new GoForWealthAdminException(GoForWealthAdminErrorMessageEnum.COMMON_ERROR_CODE.getValue(),GoForWealthUMAConstants.TOKEN_EXPIRED);
	 	 }catch(MalformedJwtException | SignatureException ex){
	 	 	 throw new GoForWealthAdminException(GoForWealthAdminErrorMessageEnum.COMMON_ERROR_CODE.getValue(),GoForWealthUMAConstants.WRONG_TOKEN);
	 	 }catch (IOException ex) {
	 	 	 throw new GoForWealthAdminException(GoForWealthAdminErrorMessageEnum.COMMON_ERROR_CODE.getValue(),"IOError writing file to output stream");
	 	 }catch(Exception ex){
	 	 	 throw new GoForWealthAdminException(GoForWealthAdminErrorMessageEnum.COMMON_ERROR_CODE.getValue(), GoForWealthAdminErrorMessageEnum.INTERNAL_SERVER_ERROR.getValue());
	  	 }
	  	 logger.info("Out exportUsersCanceledOrder()");
	 }
	 
	 @GetMapping(value = "/api/export/excel/download/onboardingCompletedUsers")
	 public void exportOnboardingCompletedUsers(@RequestParam("token")String token,HttpServletResponse response,Authentication auth) throws GoForWealthAdminException {
		 logger.info("In exportOnboardingCompletedUsers()");
	 	 try {
	 	 	 String subjectJson = Jwts.parser().setSigningKey(jwtConfiguration.secret.getBytes()).parseClaimsJws(token).getBody().getSubject();
	 	 	 UserSession userSession = new ObjectMapper().readValue(subjectJson, UserSession.class);
	 	 	 if(userSession != null){
	 	 	 	 User user = userRepository.findUserByRole(userSession.getProfileId(),"ADMIN");
	 	 	 	 if(user != null){
	 	 	 	 	 List<UserExportExcelDataDto> onboardingCompletedUserList = goForWealthAdminService.getAllUsers("Onboarding Completed User");
	 	 	 	 	 Workbook wb = GoForWealthAdminUtil.createExportFileForUserData(onboardingCompletedUserList,"Onboarding Completed");
	 	 	 	 	 response.setContentType("application/xls");
	 	 	 	 	 response.setHeader("Content-Disposition", "attachment; filename=Onboarding_completed_user_report_"+ GoForWealthAdminUtil.formatDateWithPattern(new Date(), "yyyyMMdd") + ".xls");
	 	 	 	 	 wb.write(response.getOutputStream());
	 	 	 	 	 response.flushBuffer();
	 	 	 	 }else{
	 	 	 		 throw new GoForWealthAdminException(GoForWealthAdminErrorMessageEnum.ACCESS_DENIED_CODE.getValue(),GoForWealthAdminErrorMessageEnum.ACCESS_DENIED_MESSAGE.getValue());
	 	 	  	 }
	 	 	 }else{
	 	 		throw new GoForWealthAdminException(GoForWealthAdminErrorMessageEnum.ACCESS_DENIED_CODE.getValue(),GoForWealthAdminErrorMessageEnum.ACCESS_DENIED_MESSAGE.getValue());
	 	 	 }
	 	 }catch(ExpiredJwtException ex){
	 	 	 throw new GoForWealthAdminException(GoForWealthAdminErrorMessageEnum.COMMON_ERROR_CODE.getValue(),GoForWealthUMAConstants.TOKEN_EXPIRED);
	 	 }catch(MalformedJwtException | SignatureException ex){
	 	 	 throw new GoForWealthAdminException(GoForWealthAdminErrorMessageEnum.COMMON_ERROR_CODE.getValue(),GoForWealthUMAConstants.WRONG_TOKEN);
	 	 }catch (IOException ex) {
	 	 	 throw new GoForWealthAdminException(GoForWealthAdminErrorMessageEnum.COMMON_ERROR_CODE.getValue(),"IOError writing file to output stream");
	 	 }catch(Exception ex){
	 	 	 throw new GoForWealthAdminException(GoForWealthAdminErrorMessageEnum.COMMON_ERROR_CODE.getValue(), GoForWealthAdminErrorMessageEnum.INTERNAL_SERVER_ERROR.getValue());
	  	 }
	  	 logger.info("Out exportOnboardingCompletedUsers()");
	 }
	 
	 @GetMapping(value = "/api/export/excel/download/onboardingNotCompletedUsers")
	 public void exportOnboardingNotCompletedUsers(@RequestParam("token")String token,HttpServletResponse response,Authentication auth) throws GoForWealthAdminException {
		 logger.info("In exportOnboardingNotCompletedUsers()");
	 	 try {
	 	 	 String subjectJson = Jwts.parser().setSigningKey(jwtConfiguration.secret.getBytes()).parseClaimsJws(token).getBody().getSubject();
	 	 	 UserSession userSession = new ObjectMapper().readValue(subjectJson, UserSession.class);
	 	 	 if(userSession != null){
	 	 	 	 User user = userRepository.findUserByRole(userSession.getProfileId(),"ADMIN");
	 	 	 	 if(user != null){
	 	 	 	 	 List<UserExportExcelDataDto> onboardingNotCompletedUserList = goForWealthAdminService.getAllUsers("Onboarding Not Completed User");
	 	 	 	 	 Workbook wb = GoForWealthAdminUtil.createExportFileForUserData(onboardingNotCompletedUserList,"Onboarding Not Completed");
	 	 	 	 	 response.setContentType("application/xls");
	 	 	 	 	 response.setHeader("Content-Disposition", "attachment; filename=Onboarding_not_completed_user_report_"+ GoForWealthAdminUtil.formatDateWithPattern(new Date(), "yyyyMMdd") + ".xls");
	 	 	 	 	 wb.write(response.getOutputStream());
	 	 	 	 	 response.flushBuffer();
	 	 	 	 }else{
	 	 	 		 throw new GoForWealthAdminException(GoForWealthAdminErrorMessageEnum.ACCESS_DENIED_CODE.getValue(),GoForWealthAdminErrorMessageEnum.ACCESS_DENIED_MESSAGE.getValue());
	 	 	  	 }
	 	 	 }else{
	 	 		throw new GoForWealthAdminException(GoForWealthAdminErrorMessageEnum.ACCESS_DENIED_CODE.getValue(),GoForWealthAdminErrorMessageEnum.ACCESS_DENIED_MESSAGE.getValue());
	 	 	 }
	 	 }catch(ExpiredJwtException ex){
	 	 	 throw new GoForWealthAdminException(GoForWealthAdminErrorMessageEnum.COMMON_ERROR_CODE.getValue(),GoForWealthUMAConstants.TOKEN_EXPIRED);
	 	 }catch(MalformedJwtException | SignatureException ex){
	 	 	 throw new GoForWealthAdminException(GoForWealthAdminErrorMessageEnum.COMMON_ERROR_CODE.getValue(),GoForWealthUMAConstants.WRONG_TOKEN);
	 	 }catch (IOException ex) {
	 	 	 throw new GoForWealthAdminException(GoForWealthAdminErrorMessageEnum.COMMON_ERROR_CODE.getValue(),"IOError writing file to output stream");
	 	 }catch(Exception ex){
	 	 	 throw new GoForWealthAdminException(GoForWealthAdminErrorMessageEnum.COMMON_ERROR_CODE.getValue(), GoForWealthAdminErrorMessageEnum.INTERNAL_SERVER_ERROR.getValue());
	  	 }
	  	 logger.info("Out exportOnboardingNotCompletedUsers()");
	 }
	 
	 @GetMapping(value = "/api/export/excel/download/kycCompletedUsers")
	 public void exportKycCompletedUsers(@RequestParam("token")String token,HttpServletResponse response,Authentication auth) throws GoForWealthAdminException {
		 logger.info("In exportKycCompletedUsers()");
	 	 try {
	 	 	 String subjectJson = Jwts.parser().setSigningKey(jwtConfiguration.secret.getBytes()).parseClaimsJws(token).getBody().getSubject();
	 	 	 UserSession userSession = new ObjectMapper().readValue(subjectJson, UserSession.class);
	 	 	 if(userSession != null){
	 	 	 	 User user = userRepository.findUserByRole(userSession.getProfileId(),"ADMIN");
	 	 	 	 if(user != null){
	 	 	 	 	 List<UserExportExcelDataDto> kycCompletedUserList = goForWealthAdminService.getAllUsers("Kyc Completed User");
	 	 	 	 	 Workbook wb = GoForWealthAdminUtil.createExportFileForUserData(kycCompletedUserList,"Kyc Completed");
	 	 	 	 	 response.setContentType("application/xls");
	 	 	 	 	 response.setHeader("Content-Disposition", "attachment; filename=Kyc_completed_user_report_"+ GoForWealthAdminUtil.formatDateWithPattern(new Date(), "yyyyMMdd") + ".xls");
	 	 	 	 	 wb.write(response.getOutputStream());
	 	 	 	 	 response.flushBuffer();
	 	 	 	 }else{
	 	 	 		 throw new GoForWealthAdminException(GoForWealthAdminErrorMessageEnum.ACCESS_DENIED_CODE.getValue(),GoForWealthAdminErrorMessageEnum.ACCESS_DENIED_MESSAGE.getValue());
	 	 	  	 }
	 	 	 }else{
	 	 		throw new GoForWealthAdminException(GoForWealthAdminErrorMessageEnum.ACCESS_DENIED_CODE.getValue(),GoForWealthAdminErrorMessageEnum.ACCESS_DENIED_MESSAGE.getValue());
	 	 	 }
	 	 }catch(ExpiredJwtException ex){
	 	 	 throw new GoForWealthAdminException(GoForWealthAdminErrorMessageEnum.COMMON_ERROR_CODE.getValue(),GoForWealthUMAConstants.TOKEN_EXPIRED);
	 	 }catch(MalformedJwtException | SignatureException ex){
	 	 	 throw new GoForWealthAdminException(GoForWealthAdminErrorMessageEnum.COMMON_ERROR_CODE.getValue(),GoForWealthUMAConstants.WRONG_TOKEN);
	 	 }catch (IOException ex) {
	 	 	 throw new GoForWealthAdminException(GoForWealthAdminErrorMessageEnum.COMMON_ERROR_CODE.getValue(),"IOError writing file to output stream");
	 	 }catch(Exception ex){
	 	 	 throw new GoForWealthAdminException(GoForWealthAdminErrorMessageEnum.COMMON_ERROR_CODE.getValue(), GoForWealthAdminErrorMessageEnum.INTERNAL_SERVER_ERROR.getValue());
	  	 }
	  	 logger.info("Out exportKycCompletedUsers()");
	 }
	 
	 @GetMapping(value = "/api/export/excel/download/kycNotCompletedUsers")
	 public void exportKycNotCompletedUsers(@RequestParam("token")String token,HttpServletResponse response,Authentication auth) throws GoForWealthAdminException {
		 logger.info("In exportKycNotCompleteUsers()");
	 	 try {
	 	 	 String subjectJson = Jwts.parser().setSigningKey(jwtConfiguration.secret.getBytes()).parseClaimsJws(token).getBody().getSubject();
	 	 	 UserSession userSession = new ObjectMapper().readValue(subjectJson, UserSession.class);
	 	 	 if(userSession != null){
	 	 	 	 User user = userRepository.findUserByRole(userSession.getProfileId(),"ADMIN");
	 	 	 	 if(user != null){
	 	 	 	 	 List<UserExportExcelDataDto> kycNotCompletedUserList = goForWealthAdminService.getAllUsers("Kyc Not Completed User");
	 	 	 	 	 Workbook wb = GoForWealthAdminUtil.createExportFileForUserData(kycNotCompletedUserList,"Kyc Not Completed");
	 	 	 	 	 response.setContentType("application/xls");
	 	 	 	 	 response.setHeader("Content-Disposition", "attachment; filename=Kyc_not_completed_user_report_"+ GoForWealthAdminUtil.formatDateWithPattern(new Date(), "yyyyMMdd") + ".xls");
	 	 	 	 	 wb.write(response.getOutputStream());
	 	 	 	 	 response.flushBuffer();
	 	 	 	 }else{
	 	 	 		 throw new GoForWealthAdminException(GoForWealthAdminErrorMessageEnum.ACCESS_DENIED_CODE.getValue(),GoForWealthAdminErrorMessageEnum.ACCESS_DENIED_MESSAGE.getValue());
	 	 	  	 }
	 	 	 }else{
	 	 		throw new GoForWealthAdminException(GoForWealthAdminErrorMessageEnum.ACCESS_DENIED_CODE.getValue(),GoForWealthAdminErrorMessageEnum.ACCESS_DENIED_MESSAGE.getValue());
	 	 	 }
	 	 }catch(ExpiredJwtException ex){
	 	 	 throw new GoForWealthAdminException(GoForWealthAdminErrorMessageEnum.COMMON_ERROR_CODE.getValue(),GoForWealthUMAConstants.TOKEN_EXPIRED);
	 	 }catch(MalformedJwtException | SignatureException ex){
	 	 	 throw new GoForWealthAdminException(GoForWealthAdminErrorMessageEnum.COMMON_ERROR_CODE.getValue(),GoForWealthUMAConstants.WRONG_TOKEN);
	 	 }catch (IOException ex) {
	 	 	 throw new GoForWealthAdminException(GoForWealthAdminErrorMessageEnum.COMMON_ERROR_CODE.getValue(),"IOError writing file to output stream");
	 	 }catch(Exception ex){
	 	 	 throw new GoForWealthAdminException(GoForWealthAdminErrorMessageEnum.COMMON_ERROR_CODE.getValue(), GoForWealthAdminErrorMessageEnum.INTERNAL_SERVER_ERROR.getValue());
	  	 }
	  	 logger.info("Out exportKycNotCompletedUsers()");
	 }
	 
	 @GetMapping(value = "/api/export/excel/download/todayRegisteredUsers")
	 public void exportTodayRegisteredUsers(@RequestParam("token")String token,HttpServletResponse response,Authentication auth) throws GoForWealthAdminException {
		 logger.info("In exportTodayRegisteredUsers()");
	 	 try {
	 	 	 String subjectJson = Jwts.parser().setSigningKey(jwtConfiguration.secret.getBytes()).parseClaimsJws(token).getBody().getSubject();
	 	 	 UserSession userSession = new ObjectMapper().readValue(subjectJson, UserSession.class);
	 	 	 if(userSession != null){
	 	 	 	 User user = userRepository.findUserByRole(userSession.getProfileId(),"ADMIN");
	 	 	 	 if(user != null){
	 	 	 	 	 List<UserExportExcelDataDto> todayRegisteredUserList = goForWealthAdminService.getAllUsers("Today Registered User");
	 	 	 	 	 Workbook wb = GoForWealthAdminUtil.createExportFileForUserData(todayRegisteredUserList,"Today Registered User");
	 	 	 	 	 response.setContentType("application/xls");
	 	 	 	 	 response.setHeader("Content-Disposition", "attachment; filename=Today_registered_user_report_"+ GoForWealthAdminUtil.formatDateWithPattern(new Date(), "yyyyMMdd") + ".xls");
	 	 	 	 	 wb.write(response.getOutputStream());
	 	 	 	 	 response.flushBuffer();
	 	 	 	 }else{
	 	 	 		 throw new GoForWealthAdminException(GoForWealthAdminErrorMessageEnum.ACCESS_DENIED_CODE.getValue(),GoForWealthAdminErrorMessageEnum.ACCESS_DENIED_MESSAGE.getValue());
	 	 	  	 }
	 	 	 }else{
	 	 		throw new GoForWealthAdminException(GoForWealthAdminErrorMessageEnum.ACCESS_DENIED_CODE.getValue(),GoForWealthAdminErrorMessageEnum.ACCESS_DENIED_MESSAGE.getValue());
	 	 	 }
	 	 }catch(ExpiredJwtException ex){
	 	 	 throw new GoForWealthAdminException(GoForWealthAdminErrorMessageEnum.COMMON_ERROR_CODE.getValue(),GoForWealthUMAConstants.TOKEN_EXPIRED);
	 	 }catch(MalformedJwtException | SignatureException ex){
	 	 	 throw new GoForWealthAdminException(GoForWealthAdminErrorMessageEnum.COMMON_ERROR_CODE.getValue(),GoForWealthUMAConstants.WRONG_TOKEN);
	 	 }catch (IOException ex) {
	 	 	 throw new GoForWealthAdminException(GoForWealthAdminErrorMessageEnum.COMMON_ERROR_CODE.getValue(),"IOError writing file to output stream");
	 	 }catch(Exception ex){
	 	 	 throw new GoForWealthAdminException(GoForWealthAdminErrorMessageEnum.COMMON_ERROR_CODE.getValue(), GoForWealthAdminErrorMessageEnum.INTERNAL_SERVER_ERROR.getValue());
	  	 }
	  	 logger.info("Out exportTodayRegisteredUsers()");
	 }
	 
	 @GetMapping(value = "/api/export/excel/download/todayConfirmedOrder")
	 public void exportTodayConfirmedOrder(@RequestParam("token")String token,HttpServletResponse response,Authentication auth) throws GoForWealthAdminException {
		 logger.info("In exportTodayConfirmedOrder()");
	 	 try {
	 	 	 String subjectJson = Jwts.parser().setSigningKey(jwtConfiguration.secret.getBytes()).parseClaimsJws(token).getBody().getSubject();
	 	 	 UserSession userSession = new ObjectMapper().readValue(subjectJson, UserSession.class);
	 	 	 if(userSession != null){
	 	 	 	 User user = userRepository.findUserByRole(userSession.getProfileId(),"ADMIN");
	 	 	 	 if(user != null){
	 	 	 		 List<OrderExportExcelDto> ordersList = goForWealthAdminService.getConfirmedOrdersList("External","Today");
	 	 	 	 	 Workbook wb = GoForWealthAdminUtil.createExportFileForOrders(ordersList,"Confirmed");
	 	 	 	 	 response.setContentType("application/xls");
	 	 	 	 	 response.setHeader("Content-Disposition", "attachment; filename=Today_confirmed_orders_report_"+ GoForWealthAdminUtil.formatDateWithPattern(new Date(), "yyyyMMdd") + ".xls");
	 	 	 	 	 wb.write(response.getOutputStream());
	 	 	 	 	 response.flushBuffer();
	 	 	 	 }else{
	 	 	 		 throw new GoForWealthAdminException(GoForWealthAdminErrorMessageEnum.ACCESS_DENIED_CODE.getValue(),GoForWealthAdminErrorMessageEnum.ACCESS_DENIED_MESSAGE.getValue());
	 	 	  	 }
	 	 	 }else{
	 	 		throw new GoForWealthAdminException(GoForWealthAdminErrorMessageEnum.ACCESS_DENIED_CODE.getValue(),GoForWealthAdminErrorMessageEnum.ACCESS_DENIED_MESSAGE.getValue());
	 	 	 }
	 	 }catch(ExpiredJwtException ex){
	 	 	 throw new GoForWealthAdminException(GoForWealthAdminErrorMessageEnum.COMMON_ERROR_CODE.getValue(),GoForWealthUMAConstants.TOKEN_EXPIRED);
	 	 }catch(MalformedJwtException | SignatureException ex){
	 	 	 throw new GoForWealthAdminException(GoForWealthAdminErrorMessageEnum.COMMON_ERROR_CODE.getValue(),GoForWealthUMAConstants.WRONG_TOKEN);
	 	 }catch (IOException ex) {
	 	 	 throw new GoForWealthAdminException(GoForWealthAdminErrorMessageEnum.COMMON_ERROR_CODE.getValue(),"IOError writing file to output stream");
	 	 }catch(Exception ex){
	 	 	 throw new GoForWealthAdminException(GoForWealthAdminErrorMessageEnum.COMMON_ERROR_CODE.getValue(), GoForWealthAdminErrorMessageEnum.INTERNAL_SERVER_ERROR.getValue());
	  	 }
	  	 logger.info("Out exportTodayConfirmedOrder()");
	 }
	 
	 @GetMapping(value = "/api/export/excel/download/todayPurchasedOrder")
	 public void exportTodayPurchasedOrder(@RequestParam("token")String token,HttpServletResponse response,Authentication auth) throws GoForWealthAdminException {
		 logger.info("In exportTodayPurchasedOrder()");
	 	 try {
	 	 	 String subjectJson = Jwts.parser().setSigningKey(jwtConfiguration.secret.getBytes()).parseClaimsJws(token).getBody().getSubject();
	 	 	 UserSession userSession = new ObjectMapper().readValue(subjectJson, UserSession.class);
	 	 	 if(userSession != null){
	 	 	 	 User user = userRepository.findUserByRole(userSession.getProfileId(),"ADMIN");
	 	 	 	 if(user != null){
	 	 	 		 List<OrderExportExcelDto> ordersList = goForWealthAdminService.getPurchasedOrdersList("External","Today");
	 	 	 	 	 Workbook wb = GoForWealthAdminUtil.createExportFileForOrders(ordersList,"Purchased");
	 	 	 	 	 response.setContentType("application/xls");
	 	 	 	 	 response.setHeader("Content-Disposition", "attachment; filename=Today_purchased_orders_report_"+ GoForWealthAdminUtil.formatDateWithPattern(new Date(), "yyyyMMdd") + ".xls");
	 	 	 	 	 wb.write(response.getOutputStream());
	 	 	 	 	 response.flushBuffer();
	 	 	 	 }else{
	 	 	 		 throw new GoForWealthAdminException(GoForWealthAdminErrorMessageEnum.ACCESS_DENIED_CODE.getValue(),GoForWealthAdminErrorMessageEnum.ACCESS_DENIED_MESSAGE.getValue());
	 	 	  	 }
	 	 	 }else{
	 	 		throw new GoForWealthAdminException(GoForWealthAdminErrorMessageEnum.ACCESS_DENIED_CODE.getValue(),GoForWealthAdminErrorMessageEnum.ACCESS_DENIED_MESSAGE.getValue());
	 	 	 }
	 	 }catch(ExpiredJwtException ex){
	 	 	 throw new GoForWealthAdminException(GoForWealthAdminErrorMessageEnum.COMMON_ERROR_CODE.getValue(),GoForWealthUMAConstants.TOKEN_EXPIRED);
	 	 }catch(MalformedJwtException | SignatureException ex){
	 	 	 throw new GoForWealthAdminException(GoForWealthAdminErrorMessageEnum.COMMON_ERROR_CODE.getValue(),GoForWealthUMAConstants.WRONG_TOKEN);
	 	 }catch (IOException ex) {
	 	 	 throw new GoForWealthAdminException(GoForWealthAdminErrorMessageEnum.COMMON_ERROR_CODE.getValue(),"IOError writing file to output stream");
	 	 }catch(Exception ex){
	 	 	 throw new GoForWealthAdminException(GoForWealthAdminErrorMessageEnum.COMMON_ERROR_CODE.getValue(), GoForWealthAdminErrorMessageEnum.INTERNAL_SERVER_ERROR.getValue());
	  	 }
	  	 logger.info("Out exportTodayPurchasedOrder()");
	 }
	 
	 @GetMapping(value = "/api/export/excel/download/todayCanceledOrder")
	 public void exportTodayCanceledOrder(@RequestParam("token")String token,HttpServletResponse response,Authentication auth) throws GoForWealthAdminException {
		 logger.info("In exportTodayCanceledOrder()");
	 	 try {
	 	 	 String subjectJson = Jwts.parser().setSigningKey(jwtConfiguration.secret.getBytes()).parseClaimsJws(token).getBody().getSubject();
	 	 	 UserSession userSession = new ObjectMapper().readValue(subjectJson, UserSession.class);
	 	 	 if(userSession != null){
	 	 	 	 User user = userRepository.findUserByRole(userSession.getProfileId(),"ADMIN");
	 	 	 	 if(user != null){
	 	 	 		 List<OrderExportExcelDto> ordersList = goForWealthAdminService.getCancledOrdersList("External","Today");
	 	 	 	 	 Workbook wb = GoForWealthAdminUtil.createExportFileForOrders(ordersList,"Canceled");
	 	 	 	 	 response.setContentType("application/xls");
	 	 	 	 	 response.setHeader("Content-Disposition", "attachment; filename=Today_canceled_orders_report_"+ GoForWealthAdminUtil.formatDateWithPattern(new Date(), "yyyyMMdd") + ".xls");
	 	 	 	 	 wb.write(response.getOutputStream());
	 	 	 	 	 response.flushBuffer();
	 	 	 	 }else{
	 	 	 		 throw new GoForWealthAdminException(GoForWealthAdminErrorMessageEnum.ACCESS_DENIED_CODE.getValue(),GoForWealthAdminErrorMessageEnum.ACCESS_DENIED_MESSAGE.getValue());
	 	 	  	 }
	 	 	 }else{
	 	 		throw new GoForWealthAdminException(GoForWealthAdminErrorMessageEnum.ACCESS_DENIED_CODE.getValue(),GoForWealthAdminErrorMessageEnum.ACCESS_DENIED_MESSAGE.getValue());
	 	 	 }
	 	 }catch(ExpiredJwtException ex){
	 	 	 throw new GoForWealthAdminException(GoForWealthAdminErrorMessageEnum.COMMON_ERROR_CODE.getValue(),GoForWealthUMAConstants.TOKEN_EXPIRED);
	 	 }catch(MalformedJwtException | SignatureException ex){
	 	 	 throw new GoForWealthAdminException(GoForWealthAdminErrorMessageEnum.COMMON_ERROR_CODE.getValue(),GoForWealthUMAConstants.WRONG_TOKEN);
	 	 }catch (IOException ex) {
	 	 	 throw new GoForWealthAdminException(GoForWealthAdminErrorMessageEnum.COMMON_ERROR_CODE.getValue(),"IOError writing file to output stream");
	 	 }catch(Exception ex){
	 	 	 throw new GoForWealthAdminException(GoForWealthAdminErrorMessageEnum.COMMON_ERROR_CODE.getValue(), GoForWealthAdminErrorMessageEnum.INTERNAL_SERVER_ERROR.getValue());
	  	 }
	  	 logger.info("Out exportTodayCanceledOrder()");
	 }
	 
	 @GetMapping(value = "/api/export/excel/download/todayOnboardingCompletedUser")
	 public void exportTodayOnboardingCompletedUser(@RequestParam("token")String token,HttpServletResponse response,Authentication auth) throws GoForWealthAdminException {
		 logger.info("In exportTodayOnboardingCompletedUser()");
	 	 try {
	 	 	 String subjectJson = Jwts.parser().setSigningKey(jwtConfiguration.secret.getBytes()).parseClaimsJws(token).getBody().getSubject();
	 	 	 UserSession userSession = new ObjectMapper().readValue(subjectJson, UserSession.class);
	 	 	 if(userSession != null){
	 	 	 	 User user = userRepository.findUserByRole(userSession.getProfileId(),"ADMIN");
	 	 	 	 if(user != null){
	 	 	 		 List<UserExportExcelDataDto> usersList = goForWealthAdminService.getAllUsers("Today Onboarding Completed User");
	 	 	 		 Workbook wb = GoForWealthAdminUtil.createExportFileForUserData(usersList,"Today Onboarding Completed User");
	 	 	 	 	 response.setContentType("application/xls");
	 	 	 	 	 response.setHeader("Content-Disposition", "attachment; filename=Today_onboarding_completed_user_report_"+ GoForWealthAdminUtil.formatDateWithPattern(new Date(), "yyyyMMdd") + ".xls");
	 	 	 	 	 wb.write(response.getOutputStream());
	 	 	 	 	 response.flushBuffer();
	 	 	 	 }else{
	 	 	 		 throw new GoForWealthAdminException(GoForWealthAdminErrorMessageEnum.ACCESS_DENIED_CODE.getValue(),GoForWealthAdminErrorMessageEnum.ACCESS_DENIED_MESSAGE.getValue());
	 	 	  	 }
	 	 	 }else{
	 	 		throw new GoForWealthAdminException(GoForWealthAdminErrorMessageEnum.ACCESS_DENIED_CODE.getValue(),GoForWealthAdminErrorMessageEnum.ACCESS_DENIED_MESSAGE.getValue());
	 	 	 }
	 	 }catch(ExpiredJwtException ex){
	 	 	 throw new GoForWealthAdminException(GoForWealthAdminErrorMessageEnum.COMMON_ERROR_CODE.getValue(),GoForWealthUMAConstants.TOKEN_EXPIRED);
	 	 }catch(MalformedJwtException | SignatureException ex){
	 	 	 throw new GoForWealthAdminException(GoForWealthAdminErrorMessageEnum.COMMON_ERROR_CODE.getValue(),GoForWealthUMAConstants.WRONG_TOKEN);
	 	 }catch (IOException ex) {
	 	 	 throw new GoForWealthAdminException(GoForWealthAdminErrorMessageEnum.COMMON_ERROR_CODE.getValue(),"IOError writing file to output stream");
	 	 }catch(Exception ex){
	 	 	 throw new GoForWealthAdminException(GoForWealthAdminErrorMessageEnum.COMMON_ERROR_CODE.getValue(), GoForWealthAdminErrorMessageEnum.INTERNAL_SERVER_ERROR.getValue());
	  	 }
	  	 logger.info("Out exportTodayOnboardingCompletedUser()");
	 }
	 
	 @GetMapping(value = "/api/export/excel/download/todayKycCompletedUser")
	 public void exportTodayKycCompletedUser(@RequestParam("token")String token,HttpServletResponse response,Authentication auth) throws GoForWealthAdminException {
		 logger.info("In exportTodayKycCompletedUser()");
	 	 try {
	 	 	 String subjectJson = Jwts.parser().setSigningKey(jwtConfiguration.secret.getBytes()).parseClaimsJws(token).getBody().getSubject();
	 	 	 UserSession userSession = new ObjectMapper().readValue(subjectJson, UserSession.class);
	 	 	 if(userSession != null){
	 	 	 	 User user = userRepository.findUserByRole(userSession.getProfileId(),"ADMIN");
	 	 	 	 if(user != null){
	 	 	 		List<UserExportExcelDataDto> usersList = goForWealthAdminService.getAllUsers("Today Kyc Completed User");
	 	 	 		 Workbook wb = GoForWealthAdminUtil.createExportFileForUserData(usersList,"Today Kyc Completed User");
	 	 	 	 	 response.setContentType("application/xls");
	 	 	 	 	 response.setHeader("Content-Disposition", "attachment; filename=Today_kyc_completed_report_"+ GoForWealthAdminUtil.formatDateWithPattern(new Date(), "yyyyMMdd") + ".xls");
	 	 	 	 	 wb.write(response.getOutputStream());
	 	 	 	 	 response.flushBuffer();
	 	 	 	 }else{
	 	 	 		 throw new GoForWealthAdminException(GoForWealthAdminErrorMessageEnum.ACCESS_DENIED_CODE.getValue(),GoForWealthAdminErrorMessageEnum.ACCESS_DENIED_MESSAGE.getValue());
	 	 	  	 }
	 	 	 }else{
	 	 		throw new GoForWealthAdminException(GoForWealthAdminErrorMessageEnum.ACCESS_DENIED_CODE.getValue(),GoForWealthAdminErrorMessageEnum.ACCESS_DENIED_MESSAGE.getValue());
	 	 	 }
	 	 }catch(ExpiredJwtException ex){
	 	 	 throw new GoForWealthAdminException(GoForWealthAdminErrorMessageEnum.COMMON_ERROR_CODE.getValue(),GoForWealthUMAConstants.TOKEN_EXPIRED);
	 	 }catch(MalformedJwtException | SignatureException ex){
	 	 	 throw new GoForWealthAdminException(GoForWealthAdminErrorMessageEnum.COMMON_ERROR_CODE.getValue(),GoForWealthUMAConstants.WRONG_TOKEN);
	 	 }catch (IOException ex) {
	 	 	 throw new GoForWealthAdminException(GoForWealthAdminErrorMessageEnum.COMMON_ERROR_CODE.getValue(),"IOError writing file to output stream");
	 	 }catch(Exception ex){
	 	 	 throw new GoForWealthAdminException(GoForWealthAdminErrorMessageEnum.COMMON_ERROR_CODE.getValue(), GoForWealthAdminErrorMessageEnum.INTERNAL_SERVER_ERROR.getValue());
	  	 }
	  	 logger.info("Out exportTodayKycCompletedUser()");
	 }
	 
	 @GetMapping(value = "/api/export/excel/download/pendingOrders")
	 public void exportPendingOrder(@RequestParam("token")String token,HttpServletResponse response,Authentication auth) throws GoForWealthAdminException {
		 logger.info("In exportPendingOrder()");
	 	 try {
	 	 	 String subjectJson = Jwts.parser().setSigningKey(jwtConfiguration.secret.getBytes()).parseClaimsJws(token).getBody().getSubject();
	 	 	 UserSession userSession = new ObjectMapper().readValue(subjectJson, UserSession.class);
	 	 	 if(userSession != null){
	 	 	 	 User user = userRepository.findUserByRole(userSession.getProfileId(),"ADMIN");
	 	 	 	 if(user != null){
	 	 	 		 List<OrderExportExcelDto> ordersList = goForWealthAdminService.getPendingOrdersList("External","All");
	 	 	 	 	 Workbook wb = GoForWealthAdminUtil.createExportFileForOrders(ordersList,"Pending");
	 	 	 	 	 response.setContentType("application/xls");
	 	 	 	 	 response.setHeader("Content-Disposition", "attachment; filename=Payment_awaiting_orders_report_"+ GoForWealthAdminUtil.formatDateWithPattern(new Date(), "yyyyMMdd") + ".xls");
	 	 	 	 	 wb.write(response.getOutputStream());
	 	 	 	 	 response.flushBuffer();
	 	 	 	 }else{
	 	 	 		 throw new GoForWealthAdminException(GoForWealthAdminErrorMessageEnum.ACCESS_DENIED_CODE.getValue(),GoForWealthAdminErrorMessageEnum.ACCESS_DENIED_MESSAGE.getValue());
	 	 	  	 }
	 	 	 }else{
	 	 		throw new GoForWealthAdminException(GoForWealthAdminErrorMessageEnum.ACCESS_DENIED_CODE.getValue(),GoForWealthAdminErrorMessageEnum.ACCESS_DENIED_MESSAGE.getValue());
	 	 	 }
	 	 }catch(ExpiredJwtException ex){
	 	 	 throw new GoForWealthAdminException(GoForWealthAdminErrorMessageEnum.COMMON_ERROR_CODE.getValue(),GoForWealthUMAConstants.TOKEN_EXPIRED);
	 	 }catch(MalformedJwtException | SignatureException ex){
	 	 	 throw new GoForWealthAdminException(GoForWealthAdminErrorMessageEnum.COMMON_ERROR_CODE.getValue(),GoForWealthUMAConstants.WRONG_TOKEN);
	 	 }catch (IOException ex) {
	 	 	 throw new GoForWealthAdminException(GoForWealthAdminErrorMessageEnum.COMMON_ERROR_CODE.getValue(),"IOError writing file to output stream");
	 	 }catch(Exception ex){
	 	 	 throw new GoForWealthAdminException(GoForWealthAdminErrorMessageEnum.COMMON_ERROR_CODE.getValue(), GoForWealthAdminErrorMessageEnum.INTERNAL_SERVER_ERROR.getValue());
	  	 }
	  	 logger.info("Out exportPendingOrder()");
	 }
	 
	 /*
	 @PostMapping("/ticob/uploadTransferInMasterUserData")
	 public GoForWealthTICOBResponseInfo uploadTransferInMasterUserData(@RequestBody List<TransferInMasterDTO> transferInUserDetailsDTOList,Authentication auth){
	 	 logger.info("In uploadTransferInMasterUserData()");
	 	 GoForWealthTICOBResponseInfo goForWealthTICOBResponseInfo = null;
	 	 UserPrincipal userSession = (UserPrincipal)auth.getPrincipal();
	 	 if(userSession != null){
	 	 	 String result = "";
	 	 	 User user = userRepository.findUserByRole(userSession.getUser().getUserId(),"ADMIN");
	 	 	 if(user != null){
	 	 	 	 result = goForWealthTicobService.uploadTransferInMasterUserData(transferInUserDetailsDTOList);
	 	 	 	 if(result.equals(GoForWealthTICOBErrorMessageEnum.SUCCESS_MESSAGE.getValue()))
	 	 	 		 goForWealthTICOBResponseInfo = GoForWealthTICOBUtil.getSuccessResponseInfo(GoForWealthTICOBErrorMessageEnum.SUCCESS_CODE.getValue(), GoForWealthTICOBErrorMessageEnum.SUCCESS_MESSAGE.getValue(),result);
	 	 	 	 else
	 	 	 		 goForWealthTICOBResponseInfo = GoForWealthTICOBUtil.getSuccessResponseInfo(GoForWealthTICOBErrorMessageEnum.SUCCESS_CODE.getValue(), GoForWealthTICOBErrorMessageEnum.SUCCESS_MESSAGE.getValue(),result);
	 	 	 }else{
	 	 		goForWealthTICOBResponseInfo = GoForWealthTICOBUtil.getSuccessResponseInfo(GoForWealthTICOBErrorMessageEnum.ACCESS_DENIED_CODE.getValue(), GoForWealthTICOBErrorMessageEnum.ACCESS_DENIED_MESSAGE.getValue(),"");
	 	 	 }
	 	 }else{
	 	 	goForWealthTICOBResponseInfo = GoForWealthTICOBUtil.getSuccessResponseInfo(GoForWealthTICOBErrorMessageEnum.ACCESS_DENIED_CODE.getValue(), GoForWealthTICOBErrorMessageEnum.ACCESS_DENIED_MESSAGE.getValue(),"");
	 	 }
	 	 logger.info("Out uploadTransferInMasterUserData()");
	 	 return goForWealthTICOBResponseInfo;
	 }
	 */

	 /*
	 @PostMapping("ticob/uploadCamsReport")
	 public GoForWealthAdminResponseInfo uploadCamsReport(@RequestBody List<TransferInRequestDTO> tranferInRequestDTO ,Authentication authentication){
	 	 logger.info("In uploadCamsReport");
	 	 String response = "";
	 	 GoForWealthAdminResponseInfo adminResponseInfo= null;
	 	 if(authentication!= null){
	 	 	 UserPrincipal userSession = (UserPrincipal)authentication.getPrincipal();
	 	 	 if(userSession != null){
	 	 	 	 User user = userRepository.findUserByRole(userSession.getUser().getUserId(), "ADMIN");
	 	 	 	 if(user != null){
	 	 	 	 	 response = goForWealthTicobService.uploadCamsReport(tranferInRequestDTO);
	 	 	 	 	 adminResponseInfo = GoForWealthAdminUtil.getSuccessResponseInfo(GoForWealthErrorMessageEnum.SUCCESS_CODE.getValue(),GoForWealthErrorMessageEnum.SUCCESS_MESSAGE.getValue(), response);
	 	 	 	 }else{
	 	 	 		 adminResponseInfo = GoForWealthAdminUtil.getErrorResponseInfo(GoForWealthErrorMessageEnum.ACCESS_DENIED_CODE.getValue(),GoForWealthErrorMessageEnum.ACCESS_DENIED_MESSAGE.getValue(),null);
	 	 	 	 }
	 	 	 }else{
	 	 		 adminResponseInfo = GoForWealthAdminUtil.getErrorResponseInfo(GoForWealthErrorMessageEnum.ACCESS_DENIED_CODE.getValue(),GoForWealthErrorMessageEnum.ACCESS_DENIED_MESSAGE.getValue(),null);
	 	 	 }
	 	}else{
	 		adminResponseInfo = GoForWealthAdminUtil.getErrorResponseInfo(GoForWealthAdminErrorMessageEnum.ACCESS_DENIED_CODE.getValue(),GoForWealthErrorMessageEnum.ADMIN_ACCESS_DENIED_MESSAGE.getValue(), null);
	 	}
	 	logger.info("Out uploadCamsReport");
	 	return adminResponseInfo;
	 }
	 */

	 /*
	 @PostMapping("ticob/uploadKarvyReport")
	 public GoForWealthAdminResponseInfo uploadKarvyReport(@RequestBody List<TransferInRequestDTO> tranferInRequestDTO ,Authentication authentication){
	 	 logger.info("In InsertKarvyReport");
	 	 String response = "";
	 	 GoForWealthAdminResponseInfo adminResponseInfo= null;
	 	 if(authentication!= null){
	 	 	 UserPrincipal userSession = (UserPrincipal)authentication.getPrincipal();
	 	 	 if(userSession != null){
	 	 	 	 User user = userRepository.findUserByRole(userSession.getUser().getUserId(), "ADMIN");
	 	 	 	 if(user != null){
	 	 	 		 response = goForWealthTicobService.uploadKarvyReports(tranferInRequestDTO);
	 	 	 		 if(response.equals("success")){
	 	 	 			adminResponseInfo = GoForWealthAdminUtil.getSuccessResponseInfo(GoForWealthErrorMessageEnum.SUCCESS_CODE.getValue(),GoForWealthErrorMessageEnum.SUCCESS_MESSAGE.getValue(), response); 
	 	 	 		 }else{
	 	 	 			adminResponseInfo = GoForWealthAdminUtil.getSuccessResponseInfo(GoForWealthErrorMessageEnum.COMMON_ERROR_CODE.getValue(),GoForWealthErrorMessageEnum.FAILURE_MESSAGE.getValue(), response);
	 	 	 		 }
	 	 	 	 }else{
	 	 	 		adminResponseInfo = GoForWealthAdminUtil.getErrorResponseInfo(GoForWealthErrorMessageEnum.ACCESS_DENIED_CODE.getValue(),GoForWealthErrorMessageEnum.ACCESS_DENIED_MESSAGE.getValue(),null);
	 	 		 }
	 	 	}else{
	 	 		adminResponseInfo = GoForWealthAdminUtil.getErrorResponseInfo(GoForWealthErrorMessageEnum.ACCESS_DENIED_CODE.getValue(),GoForWealthErrorMessageEnum.ACCESS_DENIED_MESSAGE.getValue(),null);
	 	 	}
	 	 }else{
	 		adminResponseInfo = GoForWealthAdminUtil.getErrorResponseInfo(GoForWealthAdminErrorMessageEnum.ACCESS_DENIED_CODE.getValue(),GoForWealthErrorMessageEnum.ADMIN_ACCESS_DENIED_MESSAGE.getValue(), null);
	 	 }
	 	 logger.info("Out InsertKarvyReport");
	 	 return adminResponseInfo;
	 }
	 */

	 @PostMapping("ticob/uploadTransferInRecord")
	 public GoForWealthAdminResponseInfo uploadTransferInRecord(@RequestBody List<TransferInRequestDTO> tranferInRequestDTO ,Authentication authentication){
		 logger.info("In uploadTransferInRecord()");
	 	 String response = "";
	 	 GoForWealthAdminResponseInfo adminResponseInfo= null;
	 	 if(authentication!= null){
	 	 	 UserPrincipal userSession = (UserPrincipal)authentication.getPrincipal();
	 	 	 if(userSession != null){
	 	 	 	 User user = userRepository.findUserByRole(userSession.getUser().getUserId(), "ADMIN");
	 	 	 	 if(user != null){
	 	 	 	 	 response = goForWealthTicobService.uploadTransferInRecord(tranferInRequestDTO);
	 	 	 	 	 adminResponseInfo = GoForWealthAdminUtil.getSuccessResponseInfo(GoForWealthErrorMessageEnum.SUCCESS_CODE.getValue(),GoForWealthErrorMessageEnum.SUCCESS_MESSAGE.getValue(), response);
	 	 	 	 }else{
	 	 	 		 adminResponseInfo = GoForWealthAdminUtil.getErrorResponseInfo(GoForWealthErrorMessageEnum.ACCESS_DENIED_CODE.getValue(),GoForWealthErrorMessageEnum.ACCESS_DENIED_MESSAGE.getValue(),null);
	 	 	 	 }
	 	 	 }else{
	 	 		 adminResponseInfo = GoForWealthAdminUtil.getErrorResponseInfo(GoForWealthErrorMessageEnum.ACCESS_DENIED_CODE.getValue(),GoForWealthErrorMessageEnum.ACCESS_DENIED_MESSAGE.getValue(),null);
	 	 	 }
	 	}else{
	 		adminResponseInfo = GoForWealthAdminUtil.getErrorResponseInfo(GoForWealthAdminErrorMessageEnum.ACCESS_DENIED_CODE.getValue(),GoForWealthErrorMessageEnum.ADMIN_ACCESS_DENIED_MESSAGE.getValue(), null);
	 	}
	 	logger.info("Out uploadTransferInRecord()");
	 	return adminResponseInfo;
	 }


}