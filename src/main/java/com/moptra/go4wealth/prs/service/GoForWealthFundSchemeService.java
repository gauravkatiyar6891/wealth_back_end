package com.moptra.go4wealth.prs.service;

import java.io.IOException;
import java.util.List;

import org.apache.poi.EncryptedDocumentException;
import org.apache.poi.openxml4j.exceptions.InvalidFormatException;
import org.json.JSONException;
import org.springframework.web.multipart.MultipartFile;

import com.moptra.go4wealth.admin.model.RedumptionResponse;
import com.moptra.go4wealth.admin.model.SchemeUploadRequest;
import com.moptra.go4wealth.bean.NavMaster;
import com.moptra.go4wealth.bean.Orders;
import com.moptra.go4wealth.bean.Scheme;
import com.moptra.go4wealth.bean.User;
import com.moptra.go4wealth.prs.common.exception.GoForWealthPRSException;
import com.moptra.go4wealth.prs.common.rest.GoForWealthPRSResponseInfo;
import com.moptra.go4wealth.prs.model.AddToCartDTO;
import com.moptra.go4wealth.prs.model.FundSchemeDTO;
import com.moptra.go4wealth.prs.model.NavCalculateResponse;
import com.moptra.go4wealth.prs.model.NavDto;
import com.moptra.go4wealth.prs.model.NavUpdateRequest;
import com.moptra.go4wealth.prs.model.PrsDTO;
import com.moptra.go4wealth.prs.model.ResponseDto;
import com.moptra.go4wealth.prs.model.SearchSchemeRequest;
import com.moptra.go4wealth.prs.model.UnitAllocationResponse;
import com.moptra.go4wealth.prs.model.UserGoalResponse;
import com.moptra.go4wealth.prs.orderapi.request.PortFolioDataDTO;

public interface GoForWealthFundSchemeService {

	boolean addScheme(List<Scheme> schemeList);

	boolean addSchemeviaBase64(byte[] byteArray);

	boolean addSchemeForSipviaBase64(byte[] byteArray);

	boolean addNavViaBase64(byte[] byteArray);

	List<FundSchemeDTO> getAllScheme(String schemeType, int offset);

	List<FundSchemeDTO> getAllSchemeWithAuth(int offset);

	boolean addNavMasterData(List<NavMaster> navMasterList);

	List<FundSchemeDTO> getAllTopScheme();

	boolean saveBankIfscCodes(MultipartFile userExcelUpload)throws EncryptedDocumentException, InvalidFormatException, IOException;

	FundSchemeDTO getSchemeDetails(String schemeCode);

	AddToCartDTO addToCart(AddToCartDTO addToCartDTO);

	List<AddToCartDTO> getCartOrder(Integer userId, String type);

	String confirmOrder(Integer userId, Integer orderId);

	AddToCartDTO getCartOrderByOrder(Integer userId, Integer orderId);

	List<String> getAllSchemeType();

	List<FundSchemeDTO> searchScheme(SearchSchemeRequest searchSchemeRequest);

	FundSchemeDTO getSchemeDetailsById(Integer schemeId, User user);

	FundSchemeDTO getSchemeDetailsByCode(String schemeCode);

	GoForWealthPRSResponseInfo cancelOrder(Integer orderId, Integer userId);

	String makePayment(List<AddToCartDTO> addToCartDTO, Integer userId) throws GoForWealthPRSException;

	List<AddToCartDTO> getOrderTransaction(Integer userId,String folioNo,String schemeCode);

	//List<PortFolioDataDTO> getPortfolio(Integer userId);

	//AddToCartDTO getPortfolioDetails(Integer orderId);

	//String redeemOrder(Integer orderId, Integer userId, String amount, String type);

	String switchScheme(Integer orderId, String schemeCode, Integer userId);

	boolean updateSchemeWithKeyword();

	boolean updateSchemeWithDisplay();

	boolean deleteDirectScheme();

	FundSchemeDTO getSchemeDetailBySchemeCode(String toSchemeCode);

	NavCalculateResponse calculateSipNavData(Double amount, String schemeCode, int year, String calculateType);

	String getPaymentStatus(int orderId);
	
	boolean updateSchemeWithPriority();

	List<UnitAllocationResponse> getAwaitingPaymentRecord(Integer userId);

	public void getOnlyStrings();

	String deleteOrder(Integer userId, Integer orderId);

	List<UserGoalResponse> getUserGoals(Integer userId);

	String updateUserAssignedAndPredefineGoals(Integer oldGoalId, Integer goalId, Integer orderId, Integer userId);

	Object getOrderStatus(String clientCode, String Filler1, String Filler2, String Filler3, String fromDate,
			String memberCode, String OrderNo, String orderstatus, String orderType, String password,
			String settlementType, String subOrderType, String toDate, String transactionType, String userId);

	Object getAllotementStatement(String clientCode, String Filler1, String Filler2, String Filler3, String fromDate,
			String memberCode, String OrderNo, String orderstatus, String orderType, String password,
			String settlementType, String subOrderType, String toDate, String userId);

	void updatePaymentStatus(Orders orders, User user);

	void updateOrderStatus(Integer ordersId, Integer userId, String bseOrderId, String callForInstallment);

	String uploadNavDateAndCurrentNavForSchemeViaTxt(byte[] byteArray);

	PrsDTO getMandateStatus(Integer userId);

	void updateCurrentNavAndCurrentValueForOrder();

	String getEmandateAuthenticationUrl(Integer userId);

	void updateOrderRedemptionStatus();

	String updateSipDate(Integer userId, AddToCartDTO addToCartDTO);

	void updateNatchAndBillerStatusOfUserWithStatusNotApproved();

	List<AddToCartDTO> getAdminCartOrder(Integer userId, String type);

	List<AddToCartDTO> getOrderTransactionForAdmin(Integer userId);

	String uploadLumpsumSchemeTextFile(byte[] byteArray);

	String uploadSipSchemeTextFile(byte[] byteArray);

	String uploadSchemeWithICRAPassingAmfiCode() throws JSONException;

	String uploadNavDateAndCurrentNavForSchemeViaAutoDownload(NavUpdateRequest navUpdateRequest);

	FundSchemeDTO getSchemeDetailsByKeyword(String schemeKeyword);

	void updateUserDebitedSipInstallmentsThroughDates();

	boolean updateSchemeName(List<SchemeUploadRequest> schemeUploadRequestList);

	void updateInvalidOrderStatusWithIO();
	
	List<FundSchemeDTO> getAllRecommendedScheme();

	List<RedumptionResponse> getRedumptionDetail(String folioNo , String schemeCode);

	List<AddToCartDTO> getActiveSipsList(User user);

	void updateOrderAndAllotementWithStatusAP();

	void sendReportToUserAfterAllotement(String string);

	boolean userInvestmentGoals(UserGoalResponse userGoalResponse, Integer userId);

	void updateBillerStatus(List<PrsDTO> prsDTO);

	List<PrsDTO> getBillerStatus();

	void savePreviousDateToDBForSipInstallment();

	void updateOrderRedemptionStatusForFollio();

	List<PortFolioDataDTO> getConsoliDatedFollio(Integer userId);

	String redeemOrderByFollio(Integer userId, AddToCartDTO addToCartDTO);

	List<ResponseDto> checkAmcForFolio(String amcCode,User user);

	void updateNavFromNode();

	AddToCartDTO getOrderDetailsById(Integer orderId);

	String getNavFromAmfi(List<NavDto> navDto);

}