package com.moptra.go4wealth.admin.service;

import java.io.IOException;
import java.util.List;

import javax.servlet.http.HttpServletResponse;

import org.springframework.security.core.Authentication;

import com.moptra.go4wealth.admin.common.exception.GoForWealthAdminException;
import com.moptra.go4wealth.admin.model.AdminBlogRequest;
import com.moptra.go4wealth.admin.model.BlogCategoryDTO;
import com.moptra.go4wealth.admin.model.BlogDTO;
import com.moptra.go4wealth.admin.model.CategoryListResponseDTO;
import com.moptra.go4wealth.admin.model.ContactUsRequestDTO;
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
import com.moptra.go4wealth.bean.Blog;
import com.moptra.go4wealth.bean.Seo;
import com.moptra.go4wealth.bean.TopSchemes;
import com.moptra.go4wealth.bean.User;
import com.moptra.go4wealth.prs.model.FundSchemeDTO;
import com.moptra.go4wealth.prs.model.OrdersDTO;
import com.moptra.go4wealth.prs.orderapi.request.PortFolioDataDTO;
import com.moptra.go4wealth.uma.model.UserLoginResponseDTO;
import com.moptra.go4wealth.uma.service.GoForWealthUMAService;

public interface GoForWealthAdminService {

	public void deleteBlogById(Integer blogId,int userId) throws GoForWealthAdminException;
	
	public Blog getImageById(Integer blogId,HttpServletResponse response) throws IOException;

	public List<BlogDTO> getAllBlogs(int userId) throws GoForWealthAdminException;
	
	public void saveBlog(AdminBlogRequest adminBlogRequest, int userId) throws GoForWealthAdminException;

	public BlogDTO getBlogById(int blogId,HttpServletResponse response,int userId)throws IOException, GoForWealthAdminException;

	public BlogDTO updateAdminBlog(AdminBlogRequest adminBlogRequest,int blogId,int userId) throws GoForWealthAdminException;

	public Seo getSeoInfo(String pageName);

	public List<SchemeDTO> getAllSchemes(int count,int userId) throws GoForWealthAdminException;
	
	public void saveTopSchemes(List<String> schemeCode,int userId) throws GoForWealthAdminException;
	
	public List<SchemeDTO> getAllTopSchemes(int userId) throws GoForWealthAdminException;

	public void deactivateTopSchemes(List<String> schemeCodes,int userId) throws GoForWealthAdminException;

	public List<UsersListDTO> getAllUsers(int count,int userId) throws GoForWealthAdminException;
	
	List<UserExportExcelDataDto> getAllUsers(String type);
	
	public UserInfoDto getUserInfoById(int id,int userId) throws GoForWealthAdminException;

	public int[] getTotalPagesForPagination(int userId) throws GoForWealthAdminException;
	
	public String completeOnboarding(int id,Authentication authentication) throws GoForWealthAdminException;

	public String addCategory(String category,int userId) throws GoForWealthAdminException;

	public List<BlogCategoryDTO> getAllBlogCategory(int userId) throws GoForWealthAdminException;

	public List<BlogDTO> getBlogByCategoryId(int categoryId,int userId) throws GoForWealthAdminException;

	public String contactUsFormData(ContactUsRequestDTO contactUsRequestDTO);

	public List<TestimonialResponseDTO> getTestimonialData(int userId) throws GoForWealthAdminException;

	public void getTestimonialImageById(int imageId, HttpServletResponse response) throws IOException;

	public List<BlogDTO> getRelatedBlog(int id,int userId) throws GoForWealthAdminException;

	public String saveTestimonial(TestiMonialRequestDTO testimonialRequestDTO,int userId) throws GoForWealthAdminException;

	public TestimonialResponseDTO getTestimonialById(int id,int userId) throws GoForWealthAdminException;

	public String updateTestimonial(TestiMonialRequestDTO testimonialRequestDTO, int id,int userId) throws GoForWealthAdminException;

	public void deleteTestimonialByID(int id,int userId) throws GoForWealthAdminException;

	public List<TestimonialResponseDTO> getUserTestimonialData() throws GoForWealthAdminException;

	public boolean getUserAuthority(Integer userId);

	public List<Seo> getAllSeos(Integer userId) throws GoForWealthAdminException;

	public Seo updateSeo(Seo updateSeoRequest, Integer userId) throws GoForWealthAdminException;

	public boolean saveExcelDataToTopSchemeUsingBase64(List<TopSchemes> uploadTopShemes);
	
	List<FundSchemeDTO> getAllSchemes();
		
	boolean updateSchemeWithPriority(List<FundSchemeDTO> fundSchemeDtoList);

	public List<UsersListDTO> searchUserByKeyword(String userName, Integer userId) throws GoForWealthAdminException;

	public List<OrdersDTO> getAllOrders(int count);

	public int getTotalNumberOfOrders() throws GoForWealthAdminException;

	public String uploadVideoViaBase64(Integer userId, SchemeUploadRequest fileDetails) throws IOException, GoForWealthAdminException;

	public String saveHomeVideoUrl(Integer userId, String videoUrl) throws GoForWealthAdminException;

	public String getHomeVideoUrl();

	public List<CategoryListResponseDTO> getCategoryList();

	public void sendReportAllRegisteredUserToAdmin(GoForWealthUMAService goForWealthUMAService);

	public UserDetailedDataDTO getUserDetailedForAdminDashboard(User user);

	public String checkAuthorties(Integer userId);
	
	boolean updateAmfi(List<FundSchemeDTO> fundSchemeDtoList);
	
	boolean updateAmfiWithIsin(List<FundSchemeDTO> fundSchemeDtoList);
	
	boolean uploadModelPortfolioData(List<FundSchemeDTO> fundSchemeDtoList);
	
	UserLoginResponseDTO getUser(Integer userId);

	public String updateKycStatus(int userId);
	
	String updateGoalSequence(List<SchemeUploadRequest> goalDtoList);
	
	List<SchemeKeywordWithSeoResponse> getSchemeKeywordForSeo();
	
	List<UserEnquiryDto> getUserEnquiry();
	
	boolean updateEnquiryStatus(UserEnquiryDto userEnquiryDto);
	
	boolean deleteUserEnquiry(Integer userEnquiryId);
	
	List<OrderExportExcelDto> getConfirmedOrdersList(String type,String subType);

	List<OrderExportExcelDto> getPurchasedOrdersList(String type,String subType);

	List<OrderExportExcelDto> getCancledOrdersList(String type,String subType);
	
	List<OrderExportExcelDto> getPendingOrdersList(String type,String subType);
}