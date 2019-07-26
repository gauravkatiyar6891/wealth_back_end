package com.moptra.go4wealth.prs.service;

import static java.util.Calendar.DATE;
import static java.util.Calendar.MONTH;
import static java.util.Calendar.YEAR;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Writer;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.text.DecimalFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.ListIterator;
import java.util.Locale;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.transaction.Transactional;
import javax.xml.namespace.QName;

import org.apache.poi.EncryptedDocumentException;
import org.apache.poi.openxml4j.exceptions.InvalidFormatException;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.ss.usermodel.WorkbookFactory;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.context.NoSuchMessageException;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.multipart.MultipartFile;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.moptra.go4wealth.Go4WealthWebApplication;
import com.moptra.go4wealth.admin.common.util.GoForWealthAdminUtil;
import com.moptra.go4wealth.admin.model.RedumptionResponse;
import com.moptra.go4wealth.admin.model.SchemeUploadRequest;
import com.moptra.go4wealth.bean.AllotmentStatusReportUserdata;
import com.moptra.go4wealth.bean.ConsolidatedPortfollio;
import com.moptra.go4wealth.bean.GoalOrderItems;
import com.moptra.go4wealth.bean.Goals;
import com.moptra.go4wealth.bean.IndianIfscCodes;
import com.moptra.go4wealth.bean.IsipAllowedBankList;
import com.moptra.go4wealth.bean.MaintainCurrentNavCurrentValueForOrders;
import com.moptra.go4wealth.bean.NavMaster;
import com.moptra.go4wealth.bean.OnboardingStatus;
import com.moptra.go4wealth.bean.OrderItem;
import com.moptra.go4wealth.bean.OrderStatusReportUserdata;
import com.moptra.go4wealth.bean.OrderUniqueRef;
import com.moptra.go4wealth.bean.Orders;
import com.moptra.go4wealth.bean.Ppcpayinst;
import com.moptra.go4wealth.bean.Ppcpaytran;
import com.moptra.go4wealth.bean.RedemptionStatusReportUserdata;
import com.moptra.go4wealth.bean.RedumptionManagement;
import com.moptra.go4wealth.bean.Scheme;
import com.moptra.go4wealth.bean.SchemeRecentView;
import com.moptra.go4wealth.bean.Schemes_Map;
import com.moptra.go4wealth.bean.StoreConf;
import com.moptra.go4wealth.bean.SystemProperties;
import com.moptra.go4wealth.bean.TopSchemes;
import com.moptra.go4wealth.bean.TransferIn;
import com.moptra.go4wealth.bean.User;
import com.moptra.go4wealth.configuration.EmailVerificationConfiguration;
import com.moptra.go4wealth.prs.common.constant.GoForWealthPRSConstants;
import com.moptra.go4wealth.prs.common.enums.GoForWealthPRSErrorMessageEnum;
import com.moptra.go4wealth.prs.common.exception.GoForWealthPRSException;
import com.moptra.go4wealth.prs.common.rest.GoForWealthPRSResponseInfo;
import com.moptra.go4wealth.prs.common.util.EncryptUserDetail;
import com.moptra.go4wealth.prs.common.util.GoForWealthPRSUtil;
import com.moptra.go4wealth.prs.mfuploadapi.model.PaymentStatusRequest;
import com.moptra.go4wealth.prs.mfuploadapi.service.MandateService;
import com.moptra.go4wealth.prs.model.AddToCartDTO;
import com.moptra.go4wealth.prs.model.EveryAllotementReportData;
import com.moptra.go4wealth.prs.model.EveryAllotementReportSchemeData;
import com.moptra.go4wealth.prs.model.FundSchemeDTO;
import com.moptra.go4wealth.prs.model.NavApiResponseDTO;
import com.moptra.go4wealth.prs.model.NavCalculateResponse;
import com.moptra.go4wealth.prs.model.NavDto;
import com.moptra.go4wealth.prs.model.NavUpdateRequest;
import com.moptra.go4wealth.prs.model.PrsDTO;
import com.moptra.go4wealth.prs.model.ResponseDto;
import com.moptra.go4wealth.prs.model.SchemeForNav;
import com.moptra.go4wealth.prs.model.SchemeRecentViewDTO;
import com.moptra.go4wealth.prs.model.SearchSchemeRequest;
import com.moptra.go4wealth.prs.model.SimilarSchemeDTO;
import com.moptra.go4wealth.prs.model.UnitAllocationResponse;
import com.moptra.go4wealth.prs.model.UserGoalResponse;
import com.moptra.go4wealth.prs.navapi.NavService;
import com.moptra.go4wealth.prs.orderapi.OrderMfService;
import com.moptra.go4wealth.prs.orderapi.request.CheckOrderStatusRequest;
import com.moptra.go4wealth.prs.orderapi.request.GetPasswordRequest;
import com.moptra.go4wealth.prs.orderapi.request.MandateDetailsRequestParam;
import com.moptra.go4wealth.prs.orderapi.request.OrderAllotmentRequest;
import com.moptra.go4wealth.prs.orderapi.request.OrderEntryRequest;
import com.moptra.go4wealth.prs.orderapi.request.OrderRedemptionStatusRequest;
import com.moptra.go4wealth.prs.orderapi.request.PortFolioDataDTO;
import com.moptra.go4wealth.prs.orderapi.request.SwitchOrderEntryRequest;
import com.moptra.go4wealth.prs.orderapi.request.XsipOrderEntryRequest;
import com.moptra.go4wealth.prs.orderapi.response.OrderEntryResponse;
import com.moptra.go4wealth.prs.orderapi.response.SwitchOrderEntryResponse;
import com.moptra.go4wealth.prs.orderapi.response.XsipOrderEntryResponse;
import com.moptra.go4wealth.prs.payment.PaymentService;
import com.moptra.go4wealth.repository.AllotmentStatusReportUserdataRepository;
import com.moptra.go4wealth.repository.ConsolidatedFollioRepository;
import com.moptra.go4wealth.repository.GoalOrderItemsRepository;
import com.moptra.go4wealth.repository.GoalsRepository;
import com.moptra.go4wealth.repository.IndianIfscCodesRepository;
import com.moptra.go4wealth.repository.IsipAllowedBankListRepository;
import com.moptra.go4wealth.repository.MaintainCurrentNavCurrentValueForOrdersRepository;
import com.moptra.go4wealth.repository.NavMasterRepository;
import com.moptra.go4wealth.repository.OnboardingStatusRepository;
import com.moptra.go4wealth.repository.OrderItemRepository;
import com.moptra.go4wealth.repository.OrderRepository;
import com.moptra.go4wealth.repository.OrderStatusReportUserdataRepository;
import com.moptra.go4wealth.repository.OrderUniqueRefRepository;
import com.moptra.go4wealth.repository.PpcpayinstRepository;
import com.moptra.go4wealth.repository.RedemptionStatusReportUserdataRepository;
import com.moptra.go4wealth.repository.RedumptionManagementepository;
import com.moptra.go4wealth.repository.SchemeRecentViewRepository;
import com.moptra.go4wealth.repository.SchemeRepository;
import com.moptra.go4wealth.repository.SchemesMapRepository;
import com.moptra.go4wealth.repository.StoreConfRepository;
import com.moptra.go4wealth.repository.SystemPropertiesRepository;
import com.moptra.go4wealth.repository.TopSchemeRepository;
import com.moptra.go4wealth.repository.TransferInRepository;
import com.moptra.go4wealth.repository.UserEnquryRepository;
import com.moptra.go4wealth.repository.UserRepository;
import com.moptra.go4wealth.repository.ppcpaytranRepository;
import com.moptra.go4wealth.uma.common.enums.UserStatus;
import com.moptra.go4wealth.uma.common.util.OtpGenerator;
import com.moptra.go4wealth.util.MailUtility;

@Service
public class GoForWealthFundSchemeServiceImpl implements GoForWealthFundSchemeService {

	private static final QName SERVICE_NAME = new QName("http://tempuri.org/", "StarMFWebService");
	
	private static Logger logger = LoggerFactory.getLogger(GoForWealthFundSchemeServiceImpl.class);
	
	@Autowired
	private EmailVerificationConfiguration emailVerificationConfiguration;

	@Autowired
	private JavaMailSender mailSender;

	@Autowired
	private MessageSource messageSource;

	@Autowired
	SchemeRepository schemeRepository;

	@Autowired
	NavMasterRepository navMasterRepository;

	@Autowired
	IndianIfscCodesRepository indianIfscCodeRepository;

	@Autowired
	GoForWealthFundSchemeService goForWealthFundSchemeService;

	@Autowired
	UserRepository userRepository;

	@Autowired
	OrderRepository orderRepository;

	@Autowired
	OrderMfService orderMfService;

	@Autowired
	SystemPropertiesRepository systemPropertiesRepository;

	@Autowired
	OrderUniqueRefRepository orderUniqueRefRepository;

	@Autowired
	SchemeRecentViewRepository schemeRecentViewRepository;

	@Autowired
	OrderItemRepository orderItemRepository;

	@Autowired
	NavService navService;

	@Autowired
	PaymentService paymentService;

	@Autowired
	PpcpayinstRepository ppcpayinstRepository;

	@Autowired
	ppcpaytranRepository ppcpaytranRepository;

	@Autowired
	StoreConfRepository storeConfRepository;

	@Autowired
	MandateService mandateService;

	@Autowired
	IndianIfscCodesRepository indianIfscCodesRepository;

	@Autowired
	RedumptionManagementepository redumptionManagementepository;

	@Autowired
	GoalsRepository goalsRepository;

	@Autowired
	GoalOrderItemsRepository goalOrderItemsRepository;

	@Autowired
	MailUtility mailUtility;

	@Autowired
	AllotmentStatusReportUserdataRepository allotmentStatusReportUserdataRepository;

	@Autowired
	OrderStatusReportUserdataRepository orderStatusReportUserdataRepository;
	
	@Autowired
	OnboardingStatusRepository onboardingStatusRepository;

	@Autowired
	RedemptionStatusReportUserdataRepository redemptionStatusReportUserdataRepository;

	@Autowired
	MaintainCurrentNavCurrentValueForOrdersRepository maintainCurrentNavCurrentValueForOrdersRepository;

	@Autowired
	IsipAllowedBankListRepository isipAllowedBankListRepository;
	
	@Autowired
	UserEnquryRepository userEnquiryRepository;
	
	@Autowired
	TopSchemeRepository topSchemeRepository;
	
	@Autowired
	EncryptUserDetail encryptUserDetail;
	
	@Autowired
	TransferInRepository transferInRepository;
	
	@Autowired
	ConsolidatedFollioRepository consolidatedFollioRepository;
	
	@Autowired
	SchemesMapRepository schemesMapRepository;
	
	public GoForWealthFundSchemeServiceImpl(JavaMailSender mailSender) {
		this.mailSender = mailSender;
	}

	@Override
	public List<FundSchemeDTO> getAllScheme(String schemeType,int offset) {
		List<FundSchemeDTO> fundSchemeDTOList = new ArrayList<>();
		List<Scheme> schemeList = null;
		if(schemeType.equals("WithDividend"))
			schemeList = schemeRepository.getAllSchemesWithDividend(new PageRequest(offset, 8));
		else
			schemeList = schemeRepository.getAllSchemes(new PageRequest(offset, 8));
		for (Scheme scheme : schemeList) {
			FundSchemeDTO fundSchemeDTO = new FundSchemeDTO();
			fundSchemeDTO.setSchemeId(scheme.getSchemeId());
			fundSchemeDTO.setSchemeCode(scheme.getSchemeCode());
			fundSchemeDTO.setPlan(scheme.getSchemePlan());
			fundSchemeDTO.setSchemeName(scheme.getSchemeName());
			fundSchemeDTO.setSchemeType(scheme.getSchemeType());
			fundSchemeDTO.setSchemeLaunchDate(scheme.getStartDate());
			fundSchemeDTO.setSchemeEndDate(scheme.getEndDate());
			fundSchemeDTO.setAmcSchemeCode(scheme.getAmcSchemeCode());
			fundSchemeDTO.setAmfiCode(scheme.getAmfiCode());
			fundSchemeDTO.setBenchmarkCode(scheme.getBenchmarkCode());
			fundSchemeDTO.setFaceValue(scheme.getFaceValue());
			fundSchemeDTO.setMinSipAmount(scheme.getMinSipAmount());
			fundSchemeDTO.setReturn_(scheme.getReturnValue());
			fundSchemeDTO.setRisk(scheme.getRisk());
			fundSchemeDTO.setRtaCode(scheme.getRtaSchemeCode());
			fundSchemeDTO.setSipAllowed(scheme.getSipFlag());
			fundSchemeDTO.setMinimumPurchaseAmount(scheme.getMinimumPurchaseAmount());
			fundSchemeDTO.setMaximumPurchaseAmount(scheme.getMaximumPurchaseAmount());
			fundSchemeDTO.setStatus(scheme.getStatus());
			fundSchemeDTO.setSchemeKeyword(scheme.getKeyword());
			fundSchemeDTO.setAmcCode(scheme.getAmcCode());
			fundSchemeDTO.setPriority(scheme.getPriority());
			fundSchemeDTOList.add(fundSchemeDTO);
		}
		return fundSchemeDTOList;
	}

	@Override
	public List<FundSchemeDTO> getAllTopScheme() {
		List<FundSchemeDTO> fundSchemeDTOList = new ArrayList<>();
		List<Scheme> schemesList = schemeRepository.findFiveTopSchemes();
		fundSchemeDTOList = setValuesInFundSchemeDTO(schemesList);
		return fundSchemeDTOList;
	}

	private List<FundSchemeDTO> setValuesInFundSchemeDTO(List<Scheme> schemeList) {
		List<FundSchemeDTO> fundSchemeDTOList = new ArrayList<>();
		for (Scheme scheme : schemeList) {
			FundSchemeDTO fundSchemeDTO = null;
			if(scheme.getSchemeName().contains("Growth")){
				fundSchemeDTO = new FundSchemeDTO();
				fundSchemeDTO.setSchemeId(scheme.getSchemeId());
				fundSchemeDTO.setSchemeCode(scheme.getSchemeCode());
				fundSchemeDTO.setPlan(scheme.getSchemePlan());
				fundSchemeDTO.setSchemeName(scheme.getSchemeName());
				fundSchemeDTO.setSchemeType(scheme.getSchemeType());
				fundSchemeDTO.setSchemeLaunchDate(scheme.getStartDate());
				fundSchemeDTO.setSchemeEndDate(scheme.getEndDate());
				fundSchemeDTO.setAmcSchemeCode(scheme.getAmcSchemeCode());
				fundSchemeDTO.setAmfiCode(scheme.getAmfiCode());
				fundSchemeDTO.setBenchmarkCode(scheme.getBenchmarkCode());
				fundSchemeDTO.setFaceValue(scheme.getFaceValue());
				fundSchemeDTO.setMinSipAmount(scheme.getMinSipAmount());
				fundSchemeDTO.setReturn_(scheme.getReturnValue());
				fundSchemeDTO.setRisk(scheme.getRisk());
				fundSchemeDTO.setRtaCode(scheme.getRtaSchemeCode());
				fundSchemeDTO.setSipAllowed(scheme.getSipFlag());
				fundSchemeDTO.setMinimumPurchaseAmount(scheme.getMinimumPurchaseAmount());
				fundSchemeDTO.setMaximumPurchaseAmount(scheme.getMaximumPurchaseAmount());
				fundSchemeDTO.setStatus(scheme.getStatus());
				fundSchemeDTO.setSchemeKeyword(scheme.getKeyword());
				fundSchemeDTO.setAmcCode(scheme.getAmcCode());
				fundSchemeDTOList.add(fundSchemeDTO);
			}
		}
		for (Scheme scheme : schemeList) {
			FundSchemeDTO fundSchemeDTO = null;
			if(!scheme.getSchemeName().contains("Growth")){
			    fundSchemeDTO = new FundSchemeDTO();
				fundSchemeDTO.setSchemeId(scheme.getSchemeId());
				fundSchemeDTO.setSchemeCode(scheme.getSchemeCode());
				fundSchemeDTO.setPlan(scheme.getSchemePlan());
				fundSchemeDTO.setSchemeName(scheme.getSchemeName());
				fundSchemeDTO.setSchemeType(scheme.getSchemeType());
				fundSchemeDTO.setSchemeLaunchDate(scheme.getStartDate());
				fundSchemeDTO.setSchemeEndDate(scheme.getEndDate());
				fundSchemeDTO.setAmcSchemeCode(scheme.getAmcSchemeCode());
				fundSchemeDTO.setAmfiCode(scheme.getAmfiCode());
				fundSchemeDTO.setBenchmarkCode(scheme.getBenchmarkCode());
				fundSchemeDTO.setFaceValue(scheme.getFaceValue());
				fundSchemeDTO.setMinSipAmount(scheme.getMinSipAmount());
				fundSchemeDTO.setReturn_(scheme.getReturnValue());
				fundSchemeDTO.setRisk(scheme.getRisk());
				fundSchemeDTO.setRtaCode(scheme.getRtaSchemeCode());
				fundSchemeDTO.setSipAllowed(scheme.getSipFlag());
				fundSchemeDTO.setMinimumPurchaseAmount(scheme.getMinimumPurchaseAmount());
				fundSchemeDTO.setMaximumPurchaseAmount(scheme.getMaximumPurchaseAmount());
				fundSchemeDTO.setStatus(scheme.getStatus());
				fundSchemeDTO.setSchemeKeyword(scheme.getKeyword());
				fundSchemeDTO.setAmcCode(scheme.getAmcCode());
				fundSchemeDTOList.add(fundSchemeDTO);
			}
		}
		return fundSchemeDTOList;
	}

	@Override
	public boolean saveBankIfscCodes(MultipartFile userExcelUpload)throws EncryptedDocumentException, InvalidFormatException, IOException {
		List<IndianIfscCodes> indianIfscCodeslist = new ArrayList<>();
		InputStream inp = userExcelUpload.getInputStream();
		int ctr = 1;
		Workbook wb = WorkbookFactory.create(inp);
		Sheet sheet = wb.getSheetAt(0);
		Row headRow = null;
		Row row = null;
		Cell headerCell = null;
		Cell cell = null;
		boolean isNull = false;
		do {
			try {
				headRow = sheet.getRow(0);
				row = sheet.getRow(ctr);
				IndianIfscCodes indianIfscCodes = new IndianIfscCodes();
				for (int i = 0; i < row.getLastCellNum(); i++) {
					String branchName = "";
					cell = row.getCell(i);
					headerCell = headRow.getCell(i);
					if (headerCell.toString().equalsIgnoreCase("BANK") && headerCell.toString() != null && !cell.toString().equals(null))
						indianIfscCodes.setBankName(cell.toString());
					if (headerCell.toString().equalsIgnoreCase("IFSC") && headerCell.toString() != null && !cell.toString().equals(null))
						indianIfscCodes.setIfscCode(cell.toString());
					if (headerCell.toString().equalsIgnoreCase("MICR CODE") && headerCell.toString() != null && !cell.toString().equals(null)) {
						cell.setCellType(Cell.CELL_TYPE_STRING);
						indianIfscCodes.setMircCode(cell.toString());
					}
					if (headerCell.toString().equalsIgnoreCase("BRANCH") && headerCell.toString() != null && !cell.toString().equals(null))
						branchName = cell.toString();
					Pattern pattern = Pattern.compile("[^a-zA-Z0-9\\s-()_]");
					Matcher matcher = pattern.matcher(branchName);
					String freshBranchName = matcher.replaceAll("");
					indianIfscCodes.setBranchName(freshBranchName);
					if (headerCell.toString().equalsIgnoreCase("ADDRESS") && headerCell.toString() != null && !cell.toString().equals(null))
						indianIfscCodes.setAddress(cell.toString());
					if (headerCell.toString().equalsIgnoreCase("CONTACT") && headerCell.toString() != null && !cell.toString().equals(null)) {
						cell.setCellType(Cell.CELL_TYPE_STRING);
						indianIfscCodes.setContact(cell.toString());
					}
					if (headerCell.toString().equalsIgnoreCase("CITY") && headerCell.toString() != null && !cell.toString().equals(null))
						indianIfscCodes.setCity(cell.toString());
					if (headerCell.toString().equalsIgnoreCase("DISTRICT") && headerCell.toString() != null && !cell.toString().equals(null))
						indianIfscCodes.setDistrict(cell.toString());
					if (headerCell.toString().equalsIgnoreCase("STATE") && headerCell.toString() != null && !cell.toString().equals(null))
						indianIfscCodes.setState(cell.toString());
				}
				ctr++;
				if (!indianIfscCodes.equals(null))
					indianIfscCodeslist.add(indianIfscCodes);
			} catch (Exception e) {
				isNull = true;
			}
		} while (isNull != true);
		inp.close();
		if (indianIfscCodeslist.size() > 0)
			indianIfscCodeRepository.saveAll(indianIfscCodeslist);
		return true;
	}

	@Override
	public boolean addSchemeviaBase64(byte[] byteArray) {
		logger.info("In addSchemeviaBase64()");
		boolean flag = false;
		List<String> result = new ArrayList<>();
		List<Scheme> schemeList = new ArrayList<>();
		String line;
		InputStream is = null;
		BufferedReader br = null;
		try {
			is = new ByteArrayInputStream(byteArray);
			br = new BufferedReader(new InputStreamReader(is));
			while ((line = br.readLine()) != null) {
				result.add(line);
			}
			ListIterator<String> itr = result.listIterator(1);
			while (itr.hasNext()) {
				String str = itr.next();
				String[] arr = str.split("\\|", -1);
				Scheme scheme = new Scheme();
				scheme.setUniqueNo(Integer.valueOf(arr[0]));
				scheme.setSchemeCode(arr[1]);
				scheme.setRtaSchemeCode(arr[2]);
				scheme.setAmcSchemeCode(arr[3]);
				scheme.setIsin(arr[4]);
				scheme.setAmcCode(arr[5]);
				scheme.setSchemeType(arr[6]);
				scheme.setSchemePlan(arr[7]);
				scheme.setSchemeName(arr[8]);
				scheme.setPurchaseAllowed(arr[9]);
				scheme.setPurchaseTransactionMode(arr[10]);
				String temp = arr[11];
				String minAmount[] = temp.split("\\.");
				scheme.setMinimumPurchaseAmount(Integer.valueOf(minAmount[0]));
				scheme.setAdditionalPurchaseAmount(arr[12]);
				scheme.setMaximumPurchaseAmount(arr[13]);
				scheme.setPurchaseAmountMultiplier(arr[14]);
				scheme.setPurchaseCutoffTime(arr[15]);
				scheme.setRedemptionAllowed(arr[16]);
				scheme.setRedemptionTransactionMode(arr[17]);
				scheme.setMinimumRedemptionQty(arr[18]);
				scheme.setRedemptionQtyMultiplier(arr[19]);
				scheme.setMaximumRedemptionQty(arr[20]);
				scheme.setRedemptionAmountMinimum(arr[21]);
				scheme.setRedemptionAmountMaximum(arr[22]);
				scheme.setRedemptionAmountMultiple(arr[23]);
				scheme.setRedemptionCutoffTime(arr[24]);
				scheme.setRtaAgentCode(arr[25]);
				scheme.setAmcActiveFlag(arr[26]);
				scheme.setDividendReinvestmentFlag(arr[27]);
				scheme.setSipFlag(arr[28]);
				scheme.setStpFlag(arr[29]);
				scheme.setSwpFlag(arr[30]);
				scheme.setSwitchFlag(arr[31]);
				scheme.setSettlementType(arr[32]);
				scheme.setAmcInd(arr[33]);
				scheme.setFaceValue(arr[34]);
				scheme.setStartDate(arr[35]);
				scheme.setEndDate(arr[36]);
				scheme.setExitLoadFlag(arr[37]);
				scheme.setExitLoad(arr[38]);
				scheme.setLockInPeriodFlag(arr[39]);
				scheme.setLockInPeriod(arr[40]);
				scheme.setChannelPartnerCode(arr[41]);
				scheme.setAmfiCode(0);
				scheme.setBenchmarkCode("");
				scheme.setRating("");
				scheme.setNfoDate(new Date());
				scheme.setStatus("");
				scheme.setRegistrarTransferAgent("");
				scheme.setRisk("");
				scheme.setYear("");
				scheme.setMinSipAmount("");
				scheme.setField2("");
				if (arr[1].contains("-L1"))
					scheme.setDisplay("1");
				else if(arr[1].contains("-L0"))
					scheme.setDisplay("1");
				else if(arr[1].contains("-L1-I"))
					scheme.setDisplay("1");
				else if(arr[1].endsWith("-I"))
					scheme.setDisplay("1");
				else
					scheme.setDisplay("0");
				scheme.setPriority("3");
				schemeList.add(scheme);
			}
			flag = goForWealthFundSchemeService.addScheme(schemeList);
			if (!flag) {
				return false;
			}
		} catch (IOException e) {
			e.printStackTrace();
			return false;
		}
		logger.info("Out addSchemeviaBase64()");
		return flag;
	}

	@Override
	public boolean addSchemeForSipviaBase64(byte[] byteArray) {
		logger.info("In addSchemeviaBase64()");
		List<String> result = new ArrayList<>();
		String line;
		InputStream is = null;
		BufferedReader br = null;
		try {
			is = new ByteArrayInputStream(byteArray);
			br = new BufferedReader(new InputStreamReader(is));
			while ((line = br.readLine()) != null) {
				result.add(line);
			}
			ListIterator<String> itr = result.listIterator(1);
			while (itr.hasNext()) {
				String str = itr.next();
				String[] arr = str.split("\\|", -1);
				Scheme scheme = schemeRepository.findBySchemeCode(arr[2]);
				if (scheme != null) {
					if (scheme.getSipDates().equals(null) || scheme.getSipDates().equals("")) {
						// if(scheme.getSipFrequency().equals("DAILY")){
						scheme.setSipTransactionMode(arr[4]);
						scheme.setSipFrequency(arr[5]);
						scheme.setSipDates(arr[6]);
						// scheme.setSipDates("1,2,3,4,5,6,7,8,9,10,11,12,13,14,15,16,17,18,19,20,21,22,23,24,25,26,27,28");
						scheme.setSipMinimumGap(arr[7]);
						scheme.setSipMaximumGap(arr[8]);
						scheme.setSipInstallmentGap(arr[9]);
						scheme.setSipStatus(arr[10]);
						scheme.setSipMinimumInstallmentAmount(new BigDecimal(arr[11]));
						scheme.setSipMaximumInstallmentAmount(new BigDecimal(arr[12]));
						scheme.setSipMultiplierAmount(arr[13]);
						scheme.setSipMinimumInstallmentNumber(arr[14]);
						scheme.setSipMaximumInstallmentNumber(arr[15]);
						scheme.setMinSipAmount(arr[11]);
						schemeRepository.save(scheme);
						// }
					}
				}
			}
		} catch (IOException e) {
			e.printStackTrace();
			return false;
		}
		logger.info("Out addSchemeviaBase64()");
		return true;
	}

	@Override
	@Transactional
	public boolean addScheme(List<Scheme> schemeList) {
		logger.info("In addScheme()");
		boolean flag = false;
		List<Scheme> schemeListWithoutDirectScheme = new ArrayList<>();
		for (Scheme scheme : schemeList) {
			if (scheme.getSchemePlan().equals("DIRECT")) {
			} else {
				schemeListWithoutDirectScheme.add(scheme);
			}
		}
		List<Scheme> schemeListRef = schemeRepository.findAll();
		if (schemeListRef != null) {
			for (Scheme scheme : schemeListWithoutDirectScheme) {
				Scheme schemeObj = schemeRepository.findBySchemeCode(scheme.getSchemeCode());
				if (schemeObj != null) {
					flag = true;
					schemeObj.setUniqueNo(scheme.getUniqueNo());
					schemeObj.setSchemeCode(scheme.getSchemeCode());
					schemeObj.setRtaSchemeCode(scheme.getRtaSchemeCode());
					schemeObj.setAmcSchemeCode(scheme.getAmcSchemeCode());
					schemeObj.setIsin(scheme.getIsin());
					schemeObj.setAmcCode(scheme.getAmcCode());
					schemeObj.setSchemeType(scheme.getSchemeType());
					schemeObj.setSchemePlan(scheme.getSchemePlan());
					schemeObj.setSchemeName(scheme.getSchemeName());
					schemeObj.setPurchaseAllowed(scheme.getPurchaseAllowed());
					schemeObj.setPurchaseTransactionMode(scheme.getPurchaseTransactionMode());
					schemeObj.setMinimumPurchaseAmount(scheme.getMinimumPurchaseAmount());
					schemeObj.setAdditionalPurchaseAmount(scheme.getAdditionalPurchaseAmount());
					schemeObj.setMaximumPurchaseAmount(scheme.getMaximumPurchaseAmount());
					schemeObj.setPurchaseAmountMultiplier(scheme.getPurchaseAmountMultiplier());
					schemeObj.setPurchaseCutoffTime(scheme.getPurchaseCutoffTime());
					schemeObj.setRedemptionAllowed(scheme.getRedemptionAllowed());
					schemeObj.setRedemptionTransactionMode(scheme.getRedemptionTransactionMode());
					schemeObj.setMinimumRedemptionQty(scheme.getMinimumRedemptionQty());
					schemeObj.setRedemptionQtyMultiplier(scheme.getRedemptionQtyMultiplier());
					schemeObj.setMaximumRedemptionQty(scheme.getMaximumRedemptionQty());
					schemeObj.setRedemptionAmountMinimum(scheme.getRedemptionAmountMinimum());
					schemeObj.setRedemptionAmountMaximum(scheme.getRedemptionAmountMaximum());
					schemeObj.setRedemptionAmountMultiple(scheme.getRedemptionAmountMultiple());
					schemeObj.setRedemptionCutoffTime(scheme.getRedemptionCutoffTime());
					schemeObj.setRtaAgentCode(scheme.getRtaAgentCode());
					schemeObj.setAmcActiveFlag(scheme.getAmcActiveFlag());
					schemeObj.setDividendReinvestmentFlag(scheme.getDividendReinvestmentFlag());
					schemeObj.setSipFlag(scheme.getSipFlag());
					schemeObj.setStpFlag(scheme.getStpFlag());
					schemeObj.setSwpFlag(scheme.getSwpFlag());
					schemeObj.setSwitchFlag(scheme.getSwitchFlag());
					schemeObj.setSettlementType(scheme.getSettlementType());
					schemeObj.setAmcInd(scheme.getAmcInd());
					schemeObj.setFaceValue(scheme.getFaceValue());
					schemeObj.setStartDate(scheme.getStartDate());
					schemeObj.setEndDate(scheme.getEndDate());
					schemeObj.setExitLoadFlag(scheme.getExitLoadFlag());
					schemeObj.setExitLoad(scheme.getExitLoad());
					schemeObj.setLockInPeriodFlag(scheme.getLockInPeriodFlag());
					schemeObj.setLockInPeriod(scheme.getLockInPeriod());
					schemeObj.setChannelPartnerCode(scheme.getChannelPartnerCode());
					schemeObj.setAmfiCode(0);
					schemeObj.setBenchmarkCode("");
					schemeObj.setRating("");
					schemeObj.setNfoDate(new Date());
					schemeObj.setStatus("");
					schemeObj.setRegistrarTransferAgent("");
					schemeObj.setRisk("");
					schemeObj.setYear("");
					schemeObj.setMinSipAmount("");
					schemeObj.setField2("");
					if (scheme.getSchemeCode().contains("-L1")) {
						schemeObj.setDisplay("1");
					} else if(scheme.getSchemeCode().contains("-L0")){
						schemeObj.setDisplay("1");
					}else if(scheme.getSchemeCode().contains("-L1-I")){
						schemeObj.setDisplay("1");
					}else if(scheme.getSchemeCode().endsWith("-I")){
						schemeObj.setDisplay("1");
					}else {
						schemeObj.setDisplay("0");
					}
					schemeObj.setPriority("3");
					Scheme sh = schemeRepository.save(schemeObj);
					if (sh != null)
						flag = true;
					else
						flag = false;
				} else {
					Scheme schemeObjRef = new Scheme();
					schemeObjRef.setUniqueNo(scheme.getUniqueNo());
					schemeObjRef.setSchemeCode(scheme.getSchemeCode());
					schemeObjRef.setRtaSchemeCode(scheme.getRtaSchemeCode());
					schemeObjRef.setAmcSchemeCode(scheme.getAmcSchemeCode());
					schemeObjRef.setIsin(scheme.getIsin());
					schemeObjRef.setAmcCode(scheme.getAmcCode());
					schemeObjRef.setSchemeType(scheme.getSchemeType());
					schemeObjRef.setSchemePlan(scheme.getSchemePlan());
					schemeObjRef.setSchemeName(scheme.getSchemeName());
					schemeObjRef.setPurchaseAllowed(scheme.getPurchaseAllowed());
					schemeObjRef.setPurchaseTransactionMode(scheme.getPurchaseTransactionMode());
					schemeObjRef.setMinimumPurchaseAmount(scheme.getMinimumPurchaseAmount());
					schemeObjRef.setAdditionalPurchaseAmount(scheme.getAdditionalPurchaseAmount());
					schemeObjRef.setMaximumPurchaseAmount(scheme.getMaximumPurchaseAmount());
					schemeObjRef.setPurchaseAmountMultiplier(scheme.getPurchaseAmountMultiplier());
					schemeObjRef.setPurchaseCutoffTime(scheme.getPurchaseCutoffTime());
					schemeObjRef.setRedemptionAllowed(scheme.getRedemptionAllowed());
					schemeObjRef.setRedemptionTransactionMode(scheme.getRedemptionTransactionMode());
					schemeObjRef.setMinimumRedemptionQty(scheme.getMinimumRedemptionQty());
					schemeObjRef.setRedemptionQtyMultiplier(scheme.getRedemptionQtyMultiplier());
					schemeObjRef.setMaximumRedemptionQty(scheme.getMaximumRedemptionQty());
					schemeObjRef.setRedemptionAmountMinimum(scheme.getRedemptionAmountMinimum());
					schemeObjRef.setRedemptionAmountMaximum(scheme.getRedemptionAmountMaximum());
					schemeObjRef.setRedemptionAmountMultiple(scheme.getRedemptionAmountMultiple());
					schemeObjRef.setRedemptionCutoffTime(scheme.getRedemptionCutoffTime());
					schemeObjRef.setRtaAgentCode(scheme.getRtaAgentCode());
					schemeObjRef.setAmcActiveFlag(scheme.getAmcActiveFlag());
					schemeObjRef.setDividendReinvestmentFlag(scheme.getDividendReinvestmentFlag());
					schemeObjRef.setSipFlag(scheme.getSipFlag());
					schemeObjRef.setStpFlag(scheme.getStpFlag());
					schemeObjRef.setSwpFlag(scheme.getSwpFlag());
					schemeObjRef.setSwitchFlag(scheme.getSwitchFlag());
					schemeObjRef.setSettlementType(scheme.getSettlementType());
					schemeObjRef.setAmcInd(scheme.getAmcInd());
					schemeObjRef.setFaceValue(scheme.getFaceValue());
					schemeObjRef.setStartDate(scheme.getStartDate());
					schemeObjRef.setEndDate(scheme.getEndDate());
					schemeObjRef.setExitLoadFlag(scheme.getExitLoadFlag());
					schemeObjRef.setExitLoad(scheme.getExitLoad());
					schemeObjRef.setLockInPeriodFlag(scheme.getLockInPeriodFlag());
					schemeObjRef.setLockInPeriod(scheme.getLockInPeriod());
					schemeObjRef.setChannelPartnerCode(scheme.getChannelPartnerCode());
					schemeObjRef.setAmfiCode(0);
					schemeObjRef.setBenchmarkCode("");
					schemeObjRef.setRating("");
					schemeObjRef.setNfoDate(new Date());
					schemeObjRef.setStatus("");
					schemeObjRef.setRegistrarTransferAgent("");
					schemeObjRef.setRisk("");
					schemeObjRef.setYear("");
					schemeObjRef.setMinSipAmount("");
					schemeObjRef.setField2("");
					if (scheme.getSchemeCode().contains("-L1")) {
						schemeObjRef.setDisplay("1");
					} else if(scheme.getSchemeCode().contains("-L0")){
						schemeObjRef.setDisplay("1");
					}else if(scheme.getSchemeCode().contains("-L1-I")){
						schemeObjRef.setDisplay("1");
					}else if(scheme.getSchemeCode().endsWith("-I")){
						schemeObjRef.setDisplay("1");
					}else {
						schemeObjRef.setDisplay("0");
					}
					schemeObjRef.setPriority("3");
					Scheme sh = schemeRepository.save(schemeObjRef);
					if (sh != null)
						flag = true;
					else
						flag = false;
				}
			}
		} else {
			schemeList = schemeRepository.saveAll(schemeListWithoutDirectScheme);
			if (schemeList != null && schemeList.size() > 0)
				flag = true;
		}
		logger.info("Out addScheme()");
		return flag;
	}

	@Override
	public boolean addNavViaBase64(byte[] byteArray) {
		boolean flag = false;
		List<String> result = new ArrayList<>();
		List<NavMaster> navMasterList = new ArrayList<>();
		String line;
		InputStream is = null;
		BufferedReader br = null;
		try {
			is = new ByteArrayInputStream(byteArray);
			br = new BufferedReader(new InputStreamReader(is));
			while ((line = br.readLine()) != null) {
				result.add(line);
			}
			ListIterator<String> itr = result.listIterator(1);
			while (itr.hasNext()) {
				String str = itr.next();
				String[] arr = str.split("\\|", -1);
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
			flag = goForWealthFundSchemeService.addNavMasterData(navMasterList);
			if (!flag) {
				return false;
			}
		} catch (IOException e) {
			e.printStackTrace();
			return false;
		}
		return flag;
	}
	
	@Override
	@Transactional
	public boolean addNavMasterData(List<NavMaster> navMasterList) {
		boolean flag = false;
		navMasterRepository.deleteAll();
		navMasterList = navMasterRepository.saveAll(navMasterList);
		if (navMasterList != null && navMasterList.size() > 0)
			flag = true;
		return flag;
	}

	@Override
	public FundSchemeDTO getSchemeDetails(String schemeCode) {
		FundSchemeDTO fundSchemeDTO = new FundSchemeDTO();
		Scheme scheme = schemeRepository.findBySchemeCode(schemeCode);
		if (scheme != null) {
			fundSchemeDTO.setSchemeId(scheme.getSchemeId());
			fundSchemeDTO.setSchemeCode(scheme.getSchemeCode());
			fundSchemeDTO.setUniqueNo(scheme.getUniqueNo());
			fundSchemeDTO.setSchemeName(scheme.getSchemeName());
			fundSchemeDTO.setSchemeType(scheme.getSchemeType());
			fundSchemeDTO.setPlan(scheme.getSchemePlan());
			fundSchemeDTO.setFaceValue(scheme.getFaceValue());
			fundSchemeDTO.setMinimumPurchaseAmount(scheme.getMinimumPurchaseAmount());
			fundSchemeDTO.setMinSipAmount(scheme.getMinSipAmount());
			fundSchemeDTO.setSipAllowed(scheme.getSipFlag());
			fundSchemeDTO.setPurchaseAllowed(scheme.getPurchaseAllowed());
			fundSchemeDTO.setSchemeLaunchDate(scheme.getStartDate());
			fundSchemeDTO.setSchemeEndDate(scheme.getEndDate());

			if (scheme.getFundManager() != null && !scheme.getFundManager().equals("")) {
				fundSchemeDTO.setFundManager(scheme.getFundManager());
			} else {
				fundSchemeDTO.setFundManager("N/A");
			}
			if (scheme.getInvestmentObjective() != null && !scheme.getInvestmentObjective().equals("")) {
				fundSchemeDTO.setInvestmentObjective(scheme.getInvestmentObjective());
			} else {
				fundSchemeDTO.setInvestmentObjective("N/A");
			}
			if (scheme.getBenchmarkCode() != null && !scheme.getBenchmarkCode().equals("")) {
				fundSchemeDTO.setBenchmarkCode(scheme.getBenchmarkCode());

			} else {
				fundSchemeDTO.setBenchmarkCode("N/A");
			}
			if (scheme.getSchemeOptions() != null && !scheme.getSchemeOptions().equals("")) {
				fundSchemeDTO.setOption(scheme.getSchemeOptions());
			} else {
				fundSchemeDTO.setOption("N/A");
			}
			if (scheme.getSchemeSubCategory() != null && !scheme.getSchemeSubCategory().equals("")) {
				fundSchemeDTO.setSchemeSubCategory(scheme.getSchemeSubCategory());
			} else {
				fundSchemeDTO.setSchemeSubCategory("N/A");
			}
			if (scheme.getSchemeCategory() != null && !scheme.getSchemeCategory().equals("")) {
				fundSchemeDTO.setSchemeCategory(scheme.getSchemeCategory());
			} else {
				fundSchemeDTO.setSchemeCategory("N/A");
			}
			if (scheme.getRisk() != null && !scheme.getRisk().equals("")) {
				fundSchemeDTO.setRisk(scheme.getRisk());
			}
			SimpleDateFormat sdf1 = new SimpleDateFormat("yy/MM/dd");
			Date dateRef = new Date(scheme.getStartDate());
			String todayDates = sdf1.format(new Date());
			String schemeLaunchDate = sdf1.format(dateRef);
			Date todayDateRef = null;
			Date schemeLaunchDateRef = null;
			try {
				schemeLaunchDateRef = sdf1.parse(schemeLaunchDate);
				todayDateRef = sdf1.parse(todayDates);
			} catch (Exception ex) {
				ex.printStackTrace();
			}
			int schemeYears = getDiffYears(schemeLaunchDateRef, todayDateRef);
			fundSchemeDTO.setSchemeYears(schemeYears);
			List<SchemeRecentViewDTO> schemeRecentViewDTOs = getSchemeRecentView(scheme);
			fundSchemeDTO.setSchemeRecentViewDTOList(schemeRecentViewDTOs);
			List<SimilarSchemeDTO> similarSchemeDTOList = getSimilarSchemeDTO(scheme);
			fundSchemeDTO.setSimilarSchemeDTOList(similarSchemeDTOList);
			String currentNav = scheme.getCurrentNav();
			String navDate = scheme.getNavDate();
			try {
				SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
				SimpleDateFormat sdf12 = new SimpleDateFormat("dd MMMM yyyy");
				if (currentNav != null) {
					currentNav = new BigDecimal(currentNav).setScale(2, RoundingMode.HALF_UP).toString();
					fundSchemeDTO.setCurrentNav(currentNav);
					Date date1 = sdf.parse(navDate);
					navDate = sdf12.format(date1);
					fundSchemeDTO.setCurrentDate(navDate);
				} else {
					fundSchemeDTO.setCurrentNav("N/A");
					if (navDate != null) {
						Date date1 = sdf.parse(navDate);
						navDate = sdf12.format(date1);
						fundSchemeDTO.setCurrentDate(navDate);
					}
				}
			} catch (ParseException e) {
				e.printStackTrace();
			}
			if (scheme != null) {
				if (scheme.getAmfiCode() != 0) {
					NavApiResponseDTO navApiResponseDTO = navService.getNavData(String.valueOf(scheme.getAmfiCode()));
					if (navApiResponseDTO != null && navApiResponseDTO.getInformation() == null) {
						if (navApiResponseDTO.getONE_YEAR() != null) {
							fundSchemeDTO.setOneYearReturn(new BigDecimal(navApiResponseDTO.getONE_YEAR()).setScale(2, RoundingMode.HALF_UP).toString());
						}
						if (navApiResponseDTO.getTHREE_YEAR() != null) {
							fundSchemeDTO.setThreeYearReturn(new BigDecimal(navApiResponseDTO.getTHREE_YEAR()).setScale(2, RoundingMode.HALF_UP).toString());
						}
						if (navApiResponseDTO.getFIVE_YEAR() != null) {
							fundSchemeDTO.setFiveYearReturn(new BigDecimal(navApiResponseDTO.getFIVE_YEAR()).setScale(2, RoundingMode.HALF_UP).toString());
						}
					}
				}
			}
			return fundSchemeDTO;
		}else {
			return fundSchemeDTO = null;
		}
	}

	private String getPreviousDate(String date) {
		String previousDateObj = "";
		try {
			SimpleDateFormat sdf = new SimpleDateFormat("dd MMMM yyyy");
			Date previousDateTemp = sdf.parse(date);
			Calendar cal1 = Calendar.getInstance();
			cal1.setTime(previousDateTemp);
			cal1.add(Calendar.DAY_OF_YEAR, -1);
			Date previousDate = cal1.getTime();
			previousDateObj = sdf.format(previousDate);
		} catch (Exception ex) {
			ex.printStackTrace();
		}
		return previousDateObj;
	}

	private List<SimilarSchemeDTO> getSimilarSchemeDTO(Scheme scheme) {
		List<Scheme> SchemeRef = schemeRepository.findBySchemeType(scheme.getSchemeType());
		List<SimilarSchemeDTO> similarSchemeDTOList = new ArrayList<>();
		int i = 0;
		for (Scheme schemeObj : SchemeRef) {
			i = i + 1;
			if (i < 9) {
				SimilarSchemeDTO similarSchemeDTO = new SimilarSchemeDTO();
				similarSchemeDTO.setSchemeId(schemeObj.getSchemeId());
				similarSchemeDTO.setSchemeCode(schemeObj.getSchemeCode());
				similarSchemeDTO.setSchemeName(schemeObj.getSchemeName());
				similarSchemeDTO.setMinInvestment(schemeObj.getMinimumPurchaseAmount());
				similarSchemeDTO.setSchemePlan(schemeObj.getSchemePlan());
				similarSchemeDTO.setSchemeType(schemeObj.getSchemeType());
				similarSchemeDTO.setAmcCode(schemeObj.getAmcCode());
				similarSchemeDTOList.add(similarSchemeDTO);
			}
		}
		return similarSchemeDTOList;
	}

	private List<SchemeRecentViewDTO> getSchemeRecentView(Scheme scheme) {
		List<SchemeRecentViewDTO> schemeRecentViewDTOs = new ArrayList<>();
		SchemeRecentViewDTO schemeRecentViewDTO = new SchemeRecentViewDTO();
		List<SchemeRecentView> schemeRecentViews = schemeRecentViewRepository.findSchemeByDate();
		SchemeRecentView schemeRecentView = new SchemeRecentView();
		if (schemeRecentViews.size() == 5) {
			for (SchemeRecentView schemeRecentViewRef : schemeRecentViews) {
				if (schemeRecentViewRef.getScheme().getSchemeId() == scheme.getSchemeId()) {
					schemeRecentViewRepository.delete(schemeRecentViewRef);
				}
			}
			schemeRecentView = schemeRecentViews.get(0);
			schemeRecentView.setScheme(scheme);
			schemeRecentView.setLastUpdated(new Date());
			schemeRecentViewRepository.save(schemeRecentView);
		} else {
			boolean flag = false;
			for (SchemeRecentView schemeRecentViewRef : schemeRecentViews) {
				if (schemeRecentViewRef.getScheme().getSchemeId() == scheme.getSchemeId()) {
					schemeRecentViewRef.setScheme(scheme);
					schemeRecentViewRef.setLastUpdated(new Date());
					schemeRecentViewRepository.save(schemeRecentViewRef);
					flag = true;
				}
			}
			if(flag==false){
				schemeRecentView.setScheme(scheme);
				schemeRecentView.setLastUpdated(new Date());
				schemeRecentViewRepository.save(schemeRecentView);
			}
		}
		List<SchemeRecentView> schemeRecentViewList = schemeRecentViewRepository.findSchemeByDesc();
		for (SchemeRecentView schemeRecent : schemeRecentViewList) {
			scheme = schemeRecent.getScheme();
			schemeRecentViewDTO = new SchemeRecentViewDTO();
			schemeRecentViewDTO.setSchemeCode(scheme.getSchemeCode());
			schemeRecentViewDTO.setSchemeId(scheme.getSchemeId());
			schemeRecentViewDTO.setMinInvestment(scheme.getMinimumPurchaseAmount());
			schemeRecentViewDTO.setSchemeName(scheme.getSchemeName());
			schemeRecentViewDTO.setSchemePlan(scheme.getSchemePlan());
			schemeRecentViewDTO.setSchemeType(scheme.getSchemeType());
			schemeRecentViewDTO.setAmcCode(scheme.getAmcCode());
			schemeRecentViewDTO.setMinSipAmount(scheme.getMinSipAmount());
			schemeRecentViewDTO.setSchemeLaunchDate(scheme.getStartDate());
			schemeRecentViewDTO.setSchemeKeyword(scheme.getKeyword());
			schemeRecentViewDTOs.add(schemeRecentViewDTO);
		}
		return schemeRecentViewDTOs;
	}

	@Override
	public List<String> getAllSchemeType() {
		List<String> schemeTypeList = schemeRepository.getAllSchemeType();
		return schemeTypeList;
	}

	@Override
	public List<FundSchemeDTO> searchScheme(SearchSchemeRequest searchSchemeRequest) {
		List<FundSchemeDTO> fundSchemeDTOList = new ArrayList<>();
		List<Scheme> schemeList = new ArrayList<>();
		boolean DIVIDEND_REINVESTMENT_FLAG = false;
		String schemeType = searchSchemeRequest.getSchemeCategory();
		String schemeCategory = searchSchemeRequest.getSchemeType();
		searchSchemeRequest.setSchemeType(schemeType);
		searchSchemeRequest.setSchemeCategory(schemeCategory);
		if(searchSchemeRequest.getSchemeCategory() != null && !searchSchemeRequest.getSchemeCategory().equals("")){
			if(searchSchemeRequest.getSchemeCategory().equals("Growth")){
				DIVIDEND_REINVESTMENT_FLAG = false;
			}else{
				DIVIDEND_REINVESTMENT_FLAG = true;
			}
		}
		if (searchSchemeRequest != null) {
			if ((searchSchemeRequest.getSchemeType() != null && !searchSchemeRequest.getSchemeType().equals("")) && (searchSchemeRequest.getSchemeCategory() != null && !searchSchemeRequest.getSchemeCategory().equals("")) && (searchSchemeRequest.getSchemeName() != null && !searchSchemeRequest.getSchemeName().equals(""))) {
				if(DIVIDEND_REINVESTMENT_FLAG)
					schemeList = schemeRepository.findBySchemeNameAndSchemeTypeAndDividend(searchSchemeRequest.getSchemeType(),searchSchemeRequest.getSchemeName(), new PageRequest(searchSchemeRequest.getOffset(), 8));
				else
					schemeList = schemeRepository.findBySchemeNameAndSchemeTypeAndGrowth(searchSchemeRequest.getSchemeType(),searchSchemeRequest.getSchemeName(), new PageRequest(searchSchemeRequest.getOffset(), 8));
			}else if ((searchSchemeRequest.getSchemeType() != null && !searchSchemeRequest.getSchemeType().equals("")) && (searchSchemeRequest.getSchemeName() != null && !searchSchemeRequest.getSchemeName().equals(""))) {
				schemeList = schemeRepository.findBySchemeNameAndSchemeType(searchSchemeRequest.getSchemeType(),searchSchemeRequest.getSchemeName(), new PageRequest(searchSchemeRequest.getOffset(), 8));
			}else if ((searchSchemeRequest.getSchemeName() != null && !searchSchemeRequest.getSchemeName().equals("")) && (searchSchemeRequest.getSchemeCategory() != null)) {
				if(DIVIDEND_REINVESTMENT_FLAG)
					schemeList = schemeRepository.findBySchemeNameAndDividend(searchSchemeRequest.getSchemeName(),new PageRequest(searchSchemeRequest.getOffset(),8));
				else
					schemeList = schemeRepository.findBySchemeNameAndGrowth(searchSchemeRequest.getSchemeName(), new PageRequest(searchSchemeRequest.getOffset(),8));
			}else if ((searchSchemeRequest.getSchemeType() != null && !searchSchemeRequest.getSchemeType().equals("")) && (searchSchemeRequest.getSchemeCategory() != null && !searchSchemeRequest.getSchemeCategory().equals(""))) {
				if(DIVIDEND_REINVESTMENT_FLAG)
					schemeList = schemeRepository.findBySchemeTypeAndDividend(searchSchemeRequest.getSchemeType(),new PageRequest(searchSchemeRequest.getOffset(), 8));
				else
					schemeList = schemeRepository.findBySchemeTypeAndGrowth(searchSchemeRequest.getSchemeType(),new PageRequest(searchSchemeRequest.getOffset(), 8));
			}else if ((searchSchemeRequest.getSchemeName() != null && !searchSchemeRequest.getSchemeName().equals(""))) {
				schemeList = schemeRepository.findBySchemeName(searchSchemeRequest.getSchemeName(),new PageRequest(searchSchemeRequest.getOffset(), 8));
			}else if ((searchSchemeRequest.getSchemeType() != null && !searchSchemeRequest.getSchemeType().equals(""))) {
				schemeList = schemeRepository.findBySchemeType(searchSchemeRequest.getSchemeType(),new PageRequest(searchSchemeRequest.getOffset(), 8));
			}else if (searchSchemeRequest.getSchemeCategory() != null && !searchSchemeRequest.getSchemeCategory().equals("")) {
				if(DIVIDEND_REINVESTMENT_FLAG){
					System.out.println("Dividend");
					schemeList = schemeRepository.findByDividend(new PageRequest(searchSchemeRequest.getOffset(), 8));
				}else{
					schemeList = schemeRepository.findByGrowth(new PageRequest(searchSchemeRequest.getOffset(), 8));
				}
			}else{
				schemeList = schemeRepository.getAllSchemes(new PageRequest(searchSchemeRequest.getOffset(), 8));
			}
		}
		fundSchemeDTOList = setValuesInFundSchemeDTO(schemeList);
		return fundSchemeDTOList;
	}

	@Override
	public AddToCartDTO addToCart(AddToCartDTO addToCartDTO) {
		try {
			User user = userRepository.getOne(addToCartDTO.getUserId());
			//Scheme scheme = schemeRepository.getOne(addToCartDTO.getSchemeId());
			Scheme scheme = schemeRepository.findBySchemeCode(addToCartDTO.getSchemeCode());
			Orders orders = new Orders();
			orders.setUser(user);
			if (addToCartDTO.getModelPortfolioCategory() != null && addToCartDTO.getModelPortfolioCategory() != "") {
				orders.setModelPortfolioCategory(addToCartDTO.getModelPortfolioCategory());
				orders.setOrderCategory(GoForWealthPRSConstants.MODEL_PORTFOLIO_ORDER);
			}
			orders.setLastupdate(new Date());
			orders.setStartdate(new Date());
			orders.setExpiredate(new Date());
			orders.setTimeplaced(new Date());
			if (addToCartDTO.getInvestmentType().equals("SIP")) {
				if (addToCartDTO.getPaymentOptions() != null) {
					orders.setPaymentOptions(addToCartDTO.getPaymentOptions());
				} else if (user.getOnboardingStatus().getMandateNumber() != null) {
					orders.setPaymentOptions("Natch");
				} else if (user.getOnboardingStatus().getIsipMandateNumber() != null) {
					orders.setPaymentOptions("Biller");
				} else {
					orders.setPaymentOptions("Pending");
				}
			}
			orders.setType(addToCartDTO.getInvestmentType());
			orders.setTotaladjustment(new BigDecimal(0));
			orders.setTotalproduct(new BigDecimal(1));
			orders.setTotaltax(new BigDecimal(0));
			if (addToCartDTO.getGoalId() != null) {
				orders.setGoalId(addToCartDTO.getGoalId());
				orders.setGoalName(addToCartDTO.getGoalName());
				GoalOrderItems goalOrderItems = goalOrderItemsRepository.findByGoalOrderItemId(addToCartDTO.getGoalId());
				if(goalOrderItems != null){
					goalOrderItems.setField1(1);
					goalOrderItemsRepository.save(goalOrderItems);
				}
			}
			orders.setStatus(GoForWealthPRSConstants.ORDER_PENDING_STATUS);
			if(addToCartDTO.getFolioNo() != null && !addToCartDTO.getFolioNo().equals(""))
				orders.setFollioNo(addToCartDTO.getFolioNo());
			Set<OrderItem> orderItems = new HashSet<>();
			OrderItem orderItem = new OrderItem();
			orderItem.setOrders(orders);
			orderItem.setLastcreate(new Date());
			orderItem.setLastupdate(new Date());
			orderItem.setTimereleased(new Date());
			orderItem.setTimeshiped(new Date());
			orderItem.setProductprice(new BigDecimal(addToCartDTO.getAmount()));
			orderItem.setQuantity(1);
			orderItem.setStatus(GoForWealthPRSConstants.ORDER_PENDING_STATUS);
			orderItem.setTaxamount(new BigDecimal(0));
			orderItem.setTotaladjustment(new BigDecimal(0));
			if(!addToCartDTO.getDayOfSip().equals("") && addToCartDTO.getDayOfSip() != null){
				orderItem.setField2(addToCartDTO.getDayOfSip());
			}
			if(addToCartDTO.getAmount()>=200000){
				String schemeCode1  =getSchemeCodeWithL1(scheme.getRtaSchemeCode(), scheme.getIsin(), scheme.getSchemeCode());
				Scheme scheme2 = schemeRepository.findBySchemeCode(schemeCode1);
				if(!schemeCode1.equals("") && scheme2 != null){
					orderItem.setScheme(scheme2);
					orderItem.setField3(schemeCode1);
				}else{
					orderItem.setScheme(scheme);
					orderItem.setField3(addToCartDTO.getSchemeCode());
				}
			}else{
				orderItem.setScheme(scheme);
				orderItem.setField3(addToCartDTO.getSchemeCode());
			}
			if (addToCartDTO.getInvestmentType().equals("LUMPSUM")) {
				SimpleDateFormat sdf = new SimpleDateFormat("dd MMMM yyyy");
				orderItem.setDescription(sdf.format(new Date()));
				addToCartDTO.setCreationDate(new Date());
			} else {
				if(!addToCartDTO.getDayOfSip().equals("") && addToCartDTO.getDayOfSip() != null){
					String sipStartDate = null;
					String getDayOfSip = null;
					String dayOfSip = addToCartDTO.getDayOfSip();
					if (dayOfSip.length() == 3) {
						String day = dayOfSip.substring(0, 1);
						getDayOfSip = "0" + day;
					}
					if (dayOfSip.length() == 4) {
						getDayOfSip = dayOfSip.substring(0, 2);
					}
					sipStartDate = GoForWealthPRSUtil.getSipStartDate(getDayOfSip);
					orderItem.setDescription(sipStartDate);
				}
			}
			orderItems.add(orderItem);
			orders.setOrderItems(orderItems);
			orderRepository.save(orders);
			addToCartDTO.setOrderId(orders.getOrdersId());
			String status = "success";
			addToCartDTO.setStatus(status);
		} catch (Exception e) {
			e.printStackTrace();
			addToCartDTO.setStatus("Internal Server Error!");
		}
		return addToCartDTO;
	}

	@Override
	public List<AddToCartDTO> getAdminCartOrder(Integer userId, String type) {
		List<AddToCartDTO> cartList = new ArrayList<>();
		AddToCartDTO addToCartDTO = new AddToCartDTO();
		Set<OrderItem> orderItems;
		String mandateId = null;
		String bseOrderId = null;
		String lumpsumOrderId = null;
		List<Orders> ordersList = null;
		if (type.equals("SIP")) {
			ordersList = orderRepository.getSipUserOrderForAdmin(userId, type);
		} else if (type.equals("LUMPSUM")) {
			ordersList = orderRepository.getLumpsumUserOrderForAdmin(userId, type);
		}
		for (Orders orders : ordersList) {
			if (orders.getField2() != null && !orders.getField2().equals("N") && orders.getType().equals("SIP")) {
				bseOrderId = orders.getBseOrderId();
			}
			if (orders.getType().equals("LUMPSUM")) {
				lumpsumOrderId = orders.getBseOrderId();
			}
			if (orders.getMandateId() != null) {
				mandateId = orders.getMandateId();
			}
			orderItems = orders.getOrderItems();
			for (OrderItem orderItem : orderItems) {
				addToCartDTO = new AddToCartDTO();
				addToCartDTO.setOrderId(orderItem.getOrders().getOrdersId());
				addToCartDTO.setAmount(orderItem.getProductprice().doubleValue());
				if (type.equals("SIP")) {
					addToCartDTO.setDayOfSip(orderItem.getField2());
				} else if (type.equals("LUMPSUM")) {
					addToCartDTO.setDayOfSip(orderItem.getDescription());
				}
				addToCartDTO.setSchemeId(orderItem.getScheme().getSchemeId());
				addToCartDTO.setSchemeName(orderItem.getScheme().getSchemeName());
				addToCartDTO.setType(orderItem.getScheme().getSchemeType());
				addToCartDTO.setStatus(orderItem.getStatus());
				addToCartDTO.setCreationDate(orderItem.getLastupdate());
				addToCartDTO.setInvestmentType(orders.getType());
				if (mandateId != null && !mandateId.equals("")) {
					addToCartDTO.setMandateId(mandateId);
				} else {
					addToCartDTO.setMandateId("Not Created");
				}
				if (bseOrderId != null && !bseOrderId.equals("")) {
					addToCartDTO.setBseOrderId(bseOrderId);
				} else {
					addToCartDTO.setBseOrderId("Not Created");
				}
				if (lumpsumOrderId != null && !lumpsumOrderId.equals("")) {
					addToCartDTO.setLumpsumOrderId(lumpsumOrderId);
				} else {
					addToCartDTO.setLumpsumOrderId("Not Created");
				}
				cartList.add(addToCartDTO);
			}
		}
		return cartList;
	}

	@Override
	public List<AddToCartDTO> getCartOrder(Integer userId, String type) {
		List<AddToCartDTO> cartList = new ArrayList<>();
		AddToCartDTO addToCartDTO = new AddToCartDTO();
		Set<OrderItem> orderItems;
		String mandateId = null;
		String bseOrderId = null;
		String lumpsumOrderId = null;
		List<Orders> ordersList = null;
		if (type.equals("SIP")) {
			ordersList = orderRepository.findOrdersByUserSip(userId, type);
		}
		if (type.equals("LUMPSUM")) {
			ordersList = orderRepository.findOrdersByUserLumpsum(userId, type);
		}
		if (type.equals("Investment")) {
			ordersList = orderRepository.findOrdersByUserInvestment(userId);
		}
		for (Orders orders : ordersList) {
			String goalName = null;
			int goalId = 0;
			if(orders.getGoalId() != null) {
				GoalOrderItems goalOrderitems =  goalOrderItemsRepository.findByGoalOrderItemId(orders.getGoalId());
				goalName = goalOrderitems.getDescription();
				goalId = orders.getGoalId();
			}
			if (orders.getField2() != null && !orders.getField2().equals("N") && orders.getType().equals("SIP")) {
				bseOrderId = orders.getBseOrderId();
			}else{
				bseOrderId = null;
			}
			if (orders.getType().equals("LUMPSUM")) {
				lumpsumOrderId = orders.getBseOrderId();
			}
			if (orders.getMandateId() != null) {
				mandateId = orders.getMandateId();
			}
			orderItems = orders.getOrderItems();
			for (OrderItem orderItem : orderItems) {
				if (orderItem.getOrderCategory() == null) {
					addToCartDTO = new AddToCartDTO();
					addToCartDTO.setOrderId(orderItem.getOrders().getOrdersId());
					addToCartDTO.setAmount(orderItem.getProductprice().doubleValue());
					if (type.equals("SIP")) {
						addToCartDTO.setSipDate(orderItem.getField2());
					} else if (type.equals("LUMPSUM")) {
						addToCartDTO.setSipDate(orderItem.getDescription());
					}
					addToCartDTO.setDayOfSip(orderItem.getField2());
					if (type.equals("SIP") && orderItem.getStatus().equals("P")) {
						getNextSipDate(orderItem, addToCartDTO);
					}
					addToCartDTO.setSchemeId(orderItem.getScheme().getSchemeId());
					addToCartDTO.setSchemeName(orderItem.getScheme().getSchemeName());
					addToCartDTO.setStatus(orderItem.getStatus());
					addToCartDTO.setCreationDate(orderItem.getLastupdate());
					addToCartDTO.setInvestmentType(orders.getType());
					if (goalName != null && !goalName.equals("")) {
						addToCartDTO.setGoalId(goalId);
						addToCartDTO.setGoalName(goalName);
					}
					if (mandateId != null && !mandateId.equals("")) {
						addToCartDTO.setMandateId(mandateId);
					} else {
						addToCartDTO.setMandateId("Not Created");
					}
					if (bseOrderId != null && !bseOrderId.equals("")) {
						addToCartDTO.setBseOrderId(bseOrderId);
					} else {
						addToCartDTO.setBseOrderId("Not Created");
					}
					if (lumpsumOrderId != null && !lumpsumOrderId.equals("")) {
						addToCartDTO.setLumpsumOrderId(lumpsumOrderId);
					} else {
						addToCartDTO.setLumpsumOrderId("Not Created");
					}
					if (type.equals("LUMPSUM")) {
						cartList.add(addToCartDTO);
					} else {
						cartList.add(addToCartDTO);
					}
					Scheme scheme = schemeRepository.getOne(orderItem.getScheme().getSchemeId());
					if(scheme != null){
						addToCartDTO.setAmcCode(scheme.getAmcCode());
					}
				}
			}
		}
		return cartList;
	}

	@Override
	public AddToCartDTO getCartOrderByOrder(Integer userId, Integer orderId) {
		Orders orders = orderRepository.getOne(orderId);
		AddToCartDTO addToCartDTO = new AddToCartDTO();
		for (OrderItem orderItem : orders.getOrderItems()) {
			addToCartDTO = new AddToCartDTO();
			addToCartDTO.setOrderId(orderItem.getOrders().getOrdersId());
			addToCartDTO.setAmount(orderItem.getProductprice().doubleValue());
			addToCartDTO.setDayOfSip(orderItem.getField2());
			addToCartDTO.setSchemeId(orderItem.getScheme().getSchemeId());
			addToCartDTO.setSchemeName(orderItem.getScheme().getSchemeName());
			addToCartDTO.setStatus(orderItem.getStatus());
		}
		return addToCartDTO;
	}

	@Override
	public String confirmOrder(Integer userId, Integer orderId) {
		String response = "success";
		Orders orders = orderRepository.findByOrdersId(orderId);
		if (orders.getType().equals("SIP")) {
			response = confirmSipOrder(userId, orderId);
		} else {
			User user = userRepository.findByUserId(userId);
			OrderUniqueRef orderUniqueRef = orderUniqueRefRepository.getOne(1);
			String uniqueRefNo = orderUniqueRef.getOrderUniqueRefNo();
			long refNo = Long.valueOf(uniqueRefNo);
			refNo = refNo + 1;
			orderUniqueRef.setOrderUniqueRefNo(refNo + "");
			orderUniqueRefRepository.save(orderUniqueRef);
			String schemeCode = "";
			String orderValue = "";
			String paymentDate = "";
			String schemeName = "";
			for (OrderItem orderItem : orders.getOrderItems()) {
				schemeCode = orderItem.getScheme().getSchemeCode();
				orderValue = orderItem.getProductprice().toString();
				paymentDate = orderItem.getDescription();
				schemeName = orderItem.getScheme().getSchemeName();
			}
			OrderEntryRequest orderEntryRequest = new OrderEntryRequest();
			GetPasswordRequest getPasswordRequest = new GetPasswordRequest();
			List<SystemProperties> systemPropertiesList = systemPropertiesRepository.findAll();
			for (SystemProperties systemProperties : systemPropertiesList) {
				if (systemProperties.getId().getPropertyKey().equals("UserId")) {
					getPasswordRequest.setUserId(systemProperties.getId().getPropertyValue());
				} else if (systemProperties.getId().getPropertyKey().equals("MemberId")) {
					getPasswordRequest.setMemberId(systemProperties.getId().getPropertyValue());
				} else if (systemProperties.getId().getPropertyKey().equals("Password")) {
					getPasswordRequest.setPassword(systemProperties.getId().getPropertyValue());
				}
			}
			OtpGenerator otpGenerator = new OtpGenerator(5, true);
			String passKey = otpGenerator.generateOTP();
			getPasswordRequest.setPassKey(passKey);
			String passwordResponse = orderMfService.getPassword(getPasswordRequest);
			String[] res = passwordResponse.split("\\|");
			if (res[0].equals("100")) {
				orderEntryRequest.setTransCode("NEW");// Order :New/Modification/Cancellation
				orderEntryRequest.setUniqueRefNo(uniqueRefNo);
				orderEntryRequest.setOrderId("");
				orderEntryRequest.setUserID(getPasswordRequest.getUserId());
				orderEntryRequest.setMemberId(getPasswordRequest.getMemberId());
				orderEntryRequest.setClientCode(user.getOnboardingStatus().getClientCode());
				orderEntryRequest.setSchemeCd(schemeCode);
				orderEntryRequest.setBuySell("P");// Type of transaction i.e.Purchase or Redemption
				if(orders.getFollioNo()!=null && !orders.getFollioNo().equals("")){   // Buy/Sell type i.e. FRESH/ADDITIONAL
					orderEntryRequest.setBuySellType("ADDITIONAL");
					orderEntryRequest.setFolioNo(orders.getFollioNo());     // Incase demat transacti this field will be blan and mandatory in case of physical redemption and physical purchase+additional
				} else{
					orderEntryRequest.setBuySellType("FRESH");
					orderEntryRequest.setFolioNo("");
				}
				orderEntryRequest.setDpTxn("P");// CDSL/NSDL/PHYSICAL,,P = PHYSICAL
				orderEntryRequest.setOrderVal(orderValue);// Purchase/Redemption  amount(redemption amount only incase of physical redemption)
				orderEntryRequest.setQty("");
				orderEntryRequest.setAllRedeem("Y");// All units flag, If this Flag is"Y" then units and amount column should be blank
				orderEntryRequest.setRemarks("");
				orderEntryRequest.setKycStatus("Y");// KYC status of client
				orderEntryRequest.setRefNo("");// Internal referance number
				orderEntryRequest.setSubBrCode("");// Sub Broker code
				orderEntryRequest.setEuin("");// EUIN number
				orderEntryRequest.setEuinVal("N");// EUIN decleration Y/N
				orderEntryRequest.setMinRedeem("Y");// Minimum redemption flag Y/N
				orderEntryRequest.setDpc("N");// DPC flag for purchase transactions Y/N
				orderEntryRequest.setIpAdd("");
				orderEntryRequest.setPassword(res[1]);
				orderEntryRequest.setPassKey(getPasswordRequest.getPassKey());
				orderEntryRequest.setParma1("");
				orderEntryRequest.setParma2("");
				orderEntryRequest.setParma3("");
				OrderEntryResponse orderEntryResponse = orderMfService.getOrderEntry(orderEntryRequest);
				if (orderEntryResponse.getOrderId() != null && !orderEntryResponse.getOrderId().equals("0")) {
					response = orderEntryResponse.getBseRemarks();
					orders.setBseOrderId(orderEntryResponse.getOrderId());
					orders.setStatus(GoForWealthPRSConstants.ORDER_CONFIRM_STATUS);
					orders.setLastupdate(new Date());
					for (OrderItem orderItem : orders.getOrderItems()) {
						orderItem.setStatus(GoForWealthPRSConstants.ORDER_CONFIRM_STATUS);
					}
					String emailSubject = "";
					String emailContent = "";
					try {
						DecimalFormat df = new DecimalFormat("##");
						String amount = df.format((Math.round(Double.valueOf(orderValue) * 100.0) / 100.0));
						String sipId = orders.getBseOrderId();
					    String userName = user.getFirstName() == null ? user.getUsername(): user.getFirstName() + " " + user.getLastName();
						if (orders.getType().equals("LUMPSUM")) {
							emailSubject = messageSource.getMessage("lumpsum.start.mailSubject", null, Locale.ENGLISH);
							emailContent = messageSource.getMessage("lumpsum.start.mailBody",new String[] { userName, schemeName, amount, paymentDate, sipId }, Locale.ENGLISH);
						}
						mailUtility.baselineExample(user.getEmail(), emailSubject, emailContent);
					} catch (NoSuchMessageException | IOException e) {
						e.printStackTrace();
					}
				} else {
					response = orderEntryResponse.getBseRemarks();
					orders.setField3(response); // set as order error response
				}
				orderRepository.save(orders);
			}
		}
		return response;
	}

	private String confirmSipOrder(Integer userId, Integer orderId) {
		String MemberId = "";
		String paymentOptions = "";
		Orders orders = orderRepository.findByOrdersId(orderId);
		boolean isFirstOrderOfTheDay = false;
		SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
		List<String> startDateList = new ArrayList<>();
		String response = "success";
		User user = userRepository.findByUserId(userId);
		OrderUniqueRef orderUniqueRef = orderUniqueRefRepository.findByOrderUniqueRefId(1);
		String uniqueRefNo = orderUniqueRef.getOrderUniqueRefNo();
		long refNo = Long.valueOf(uniqueRefNo);
		refNo = refNo + 1;
		orderUniqueRef.setOrderUniqueRefNo(refNo + "");
		orderUniqueRefRepository.save(orderUniqueRef);
		String schemeCode = "";
		String orderValue = "";
		String paymentDate = "";
		String schemeName = "";
		String sipStartDate = null;
		for (OrderItem orderItem : orders.getOrderItems()) {
			schemeCode = orderItem.getScheme().getSchemeCode();
			orderValue = orderItem.getProductprice().toString();
			paymentDate = orderItem.getField2();
			schemeName = orderItem.getScheme().getSchemeName();
			sipStartDate = orderItem.getDescription();
		}
		XsipOrderEntryRequest xsipOrderEntryRequest = new XsipOrderEntryRequest();
		GetPasswordRequest getPasswordRequest = new GetPasswordRequest();
		List<SystemProperties> systemPropertiesList = systemPropertiesRepository.findAll();
		for (SystemProperties systemProperties : systemPropertiesList) {
			if (systemProperties.getId().getPropertyKey().equals("UserId")) {
				getPasswordRequest.setUserId(systemProperties.getId().getPropertyValue());
			} else if (systemProperties.getId().getPropertyKey().equals("MemberId")) {
				getPasswordRequest.setMemberId(systemProperties.getId().getPropertyValue());
				MemberId = systemProperties.getId().getPropertyValue();
			} else if (systemProperties.getId().getPropertyKey().equals("Password")) {
				getPasswordRequest.setPassword(systemProperties.getId().getPropertyValue());
			}
		}
		OtpGenerator otpGenerator = new OtpGenerator(5, true);
		String passKey = otpGenerator.generateOTP();
		getPasswordRequest.setPassKey(passKey);
		String passwordResponse = orderMfService.getPassword(getPasswordRequest);
		String[] res = passwordResponse.split("\\|");
		if (res[0].equals("100")) {
			xsipOrderEntryRequest.setTransactionCode("NEW");
			xsipOrderEntryRequest.setUserID(getPasswordRequest.getUserId());
			xsipOrderEntryRequest.setPassKey(getPasswordRequest.getPassKey());
			xsipOrderEntryRequest.setPassword(res[1]);
			OnboardingStatus onboardingStatus = user.getOnboardingStatus();
			if (onboardingStatus != null) {
				if (!onboardingStatus.getEnachStatus().equals("APPROVED") && !onboardingStatus.getBillerStatus().equals("APPROVED")) {
					if (orders.getPaymentOptions().equals("Natch")) {
						xsipOrderEntryRequest.setParma2("");
						xsipOrderEntryRequest.setMandateID(user.getOnboardingStatus().getMandateNumber());
						paymentOptions = "Natch";
					} else if (orders.getPaymentOptions().equals("Biller")) {
						xsipOrderEntryRequest.setMandateID("");
						xsipOrderEntryRequest.setParma2(user.getOnboardingStatus().getIsipMandateNumber());
						paymentOptions = "Biller";
					} else {
						xsipOrderEntryRequest.setParma2("");
						xsipOrderEntryRequest.setMandateID(user.getOnboardingStatus().getMandateNumber());
						paymentOptions = "Natch";
					}
				} else if (onboardingStatus.getEnachStatus().equals("APPROVED")) {
					xsipOrderEntryRequest.setParma2("");
					xsipOrderEntryRequest.setMandateID(user.getOnboardingStatus().getMandateNumber());
					paymentOptions = "Natch";

				} else if (onboardingStatus.getBillerStatus().equals("APPROVED")) {
					xsipOrderEntryRequest.setMandateID("");
					xsipOrderEntryRequest.setParma2(user.getOnboardingStatus().getIsipMandateNumber());
					paymentOptions = "Biller";
				} else {
					xsipOrderEntryRequest.setParma2("");
					xsipOrderEntryRequest.setMandateID(user.getOnboardingStatus().getMandateNumber());
					paymentOptions = "Natch";
				}
			}
			xsipOrderEntryRequest.setMemberCode(getPasswordRequest.getMemberId());
			xsipOrderEntryRequest.setSchemeCode(schemeCode);
			Date date1 = null;
			try {
				date1 = new SimpleDateFormat("dd MMM yyyy").parse(sipStartDate);
			} catch (ParseException e) {
				e.printStackTrace();
			}
			xsipOrderEntryRequest.setStartDate(sdf.format(date1));
			xsipOrderEntryRequest.setSubberCode("");
			xsipOrderEntryRequest.setUniqueRefNo(uniqueRefNo);
			xsipOrderEntryRequest.setTransMode("P");
			xsipOrderEntryRequest.setXsipRegID("");
			xsipOrderEntryRequest.setBrokerage("");
			xsipOrderEntryRequest.setClientCode(user.getOnboardingStatus().getClientCode());
			xsipOrderEntryRequest.setDpc("N");
			xsipOrderEntryRequest.setDpTxnMode("P");
			xsipOrderEntryRequest.setEuin("");
			xsipOrderEntryRequest.setEuinVal("N");
			List<Orders> orderList = orderRepository.getOrderByUserId(user.getUserId());
			for (Orders orders2 : orderList) {
				startDateList.add(sdf.format(orders2.getStartdate()));
			}
			String currentDate = sdf.format(new Date());
			for (String startData : startDateList) {
				if (startData.equals(currentDate)) {
					isFirstOrderOfTheDay = true;
				}
			}
			if (isFirstOrderOfTheDay) {
				xsipOrderEntryRequest.setFirstOrderFlag(/* "N" */"Y");
			} else {
				xsipOrderEntryRequest.setFirstOrderFlag("Y");
			}
			if(orders.getFollioNo() != null && !orders.getFollioNo().equals(""))
				xsipOrderEntryRequest.setFolioNo(orders.getFollioNo());
			else
				xsipOrderEntryRequest.setFolioNo("");
			xsipOrderEntryRequest.setFrequencyAllowed("1");
			xsipOrderEntryRequest.setFrequencyType("MONTHLY");
			xsipOrderEntryRequest.setInstallmentAmount(orderValue);
			xsipOrderEntryRequest.setInternalRefNo("");
			xsipOrderEntryRequest.setIpAdd("");
		    StoreConf noOfInstallment = storeConfRepository.findByKeyword(GoForWealthPRSConstants.MAXIMUM_INSTALLMENT_FOR_SIP);
		    if(noOfInstallment != null){
		    	xsipOrderEntryRequest.setNoOfInstallment(noOfInstallment.getKeywordValue());
		    }else{
		    	xsipOrderEntryRequest.setNoOfInstallment("1188");
		    }
			xsipOrderEntryRequest.setRemarks("");
			xsipOrderEntryRequest.setParma1("");
			xsipOrderEntryRequest.setParma3("");
			XsipOrderEntryResponse xsipOrderEntryResonse = orderMfService.getXsipOrderEntry(xsipOrderEntryRequest);
			if (xsipOrderEntryResonse.getXsipRegId() != null && !xsipOrderEntryResonse.getXsipRegId().equals("0")) {
				response = xsipOrderEntryResonse.getBseRemarks();
				try {
					SimpleDateFormat dateFormat = new SimpleDateFormat("dd MMM yyyy");
					String date = dateFormat.format(new Date());
					String resp = getChildOrderId(user.getOnboardingStatus().getClientCode(), date,"", MemberId,xsipOrderEntryResonse.getXsipRegId(), "XSIP", systemPropertiesRepository);
					if (resp.equals("101")) {
						orders.setBseOrderId(xsipOrderEntryResonse.getXsipRegId());
						orders.setField2("N");
					} else {
						orders.setBseOrderId(resp);
						orders.setField2(xsipOrderEntryResonse.getXsipRegId());
					}
				} catch (GoForWealthPRSException e1) {
					e1.printStackTrace();
				}
				orders.setStartdate(new Date());
				orders.setStatus(GoForWealthPRSConstants.ORDER_CONFIRM_STATUS);
				orders.setPaymentOptions(paymentOptions);
				orders.setLastupdate(new Date());
				if (paymentOptions.equals("Natch")) {
					orders.setMandateId(xsipOrderEntryRequest.getMandateID());
				} else if (paymentOptions.equals("Biller")) {
					orders.setMandateId(xsipOrderEntryRequest.getParma2());
				}
				for (OrderItem orderItem : orders.getOrderItems()) {
					orderItem.setStatus(GoForWealthPRSConstants.ORDER_CONFIRM_STATUS);
				}
				// sending mail to user
				String emailSubject = "";
				String emailContent = "";
				try {
					DecimalFormat df = new DecimalFormat("##");
					String orderType = "";
					if (paymentOptions.equals("Natch")) {
						orderType = "X-SIP";
					} else if (paymentOptions.equals("Biller")) {
						orderType = "I-SIP";
					} else {
						orderType = "X-SIP";
					}
					String amount = df.format((Math.round(Double.valueOf(orderValue) * 100.0) / 100.0));
					String sipId = orders.getBseOrderId();
					String userName = user.getFirstName() == null ? user.getUsername() : user.getFirstName() + " " + user.getLastName();
					if (orders.getType().equals("SIP")) {
						emailSubject = messageSource.getMessage("sip.start.mailSubject", new String[] { orderType },Locale.ENGLISH);
						emailContent = messageSource.getMessage("sip.start.mailBody",new String[] { userName, orderType, schemeName, amount, paymentDate, sipId },Locale.ENGLISH);
						mailUtility.baselineExample(user.getEmail(), emailSubject, emailContent);
					}
				} catch (NoSuchMessageException | IOException e) {
					e.printStackTrace();
				}
			} else {
				response = xsipOrderEntryResonse.getBseRemarks();
				orders.setField3(response); // set as order error response
			}
			orderRepository.save(orders);
		}
		return response;
	}

	@Override
	public GoForWealthPRSResponseInfo cancelOrder(Integer orderId, Integer userId) {
		String response = "";
		GoForWealthPRSResponseInfo responseInfo = null;
		User user = userRepository.getOne(userId);
		Orders orders = orderRepository.getOne(orderId);
		int goalId = 0;
		if(orders.getGoalId() != null){
			goalId = orders.getGoalId();
		}
		if (orders.getType().equals("SIP")) {
			responseInfo = cancelSipOrder(user, orders);
		} else {
			OrderUniqueRef orderUniqueRef = orderUniqueRefRepository.getOne(1);
			String uniqueRefNo = orderUniqueRef.getOrderUniqueRefNo();
			long refNo = Long.valueOf(uniqueRefNo);
			refNo = refNo + 1;
			orderUniqueRef.setOrderUniqueRefNo(refNo + "");
			orderUniqueRefRepository.save(orderUniqueRef);
			String schemeCode = "";
			String orderValue = "";
			String paymentDate = "";
			String schemeName = "";
			for (OrderItem orderItem : orders.getOrderItems()) {
				schemeCode = orderItem.getScheme().getSchemeCode();
				orderValue = orderItem.getProductprice().toString();
				paymentDate = orderItem.getDescription();
				schemeName = orderItem.getScheme().getSchemeName();
			}
			OrderEntryRequest orderEntryRequest = new OrderEntryRequest();
			GetPasswordRequest getPasswordRequest = new GetPasswordRequest();
			List<SystemProperties> systemPropertiesList = systemPropertiesRepository.findAll();
			for (SystemProperties systemProperties : systemPropertiesList) {
				if (systemProperties.getId().getPropertyKey().equals("UserId")) {
					getPasswordRequest.setUserId(systemProperties.getId().getPropertyValue());
				} else if (systemProperties.getId().getPropertyKey().equals("MemberId")) {
					getPasswordRequest.setMemberId(systemProperties.getId().getPropertyValue());
				} else if (systemProperties.getId().getPropertyKey().equals("Password")) {
					getPasswordRequest.setPassword(systemProperties.getId().getPropertyValue());
				}
			}
			OtpGenerator otpGenerator = new OtpGenerator(5, true);
			String passKey = otpGenerator.generateOTP();
			getPasswordRequest.setPassKey(passKey);
			String passwordResponse = orderMfService.getPassword(getPasswordRequest);
			String[] res = passwordResponse.split("\\|");
			if (res[0].equals("100")) {
				orderEntryRequest.setTransCode("CXL");
				orderEntryRequest.setUniqueRefNo(uniqueRefNo);
				orderEntryRequest.setOrderId(orders.getBseOrderId());
				orderEntryRequest.setUserID(getPasswordRequest.getUserId());
				orderEntryRequest.setMemberId(getPasswordRequest.getMemberId());
				orderEntryRequest.setClientCode(user.getOnboardingStatus().getClientCode());
				orderEntryRequest.setSchemeCd(schemeCode);
				orderEntryRequest.setBuySell("P");
				orderEntryRequest.setBuySellType("FRESH");
				orderEntryRequest.setDpTxn("P");
				orderEntryRequest.setOrderVal(orderValue);
				orderEntryRequest.setQty("");
				orderEntryRequest.setAllRedeem("Y");
				orderEntryRequest.setFolioNo("");
				orderEntryRequest.setRemarks("");
				orderEntryRequest.setKycStatus("Y");
				orderEntryRequest.setRefNo("");
				orderEntryRequest.setSubBrCode("");
				orderEntryRequest.setEuin("");
				orderEntryRequest.setEuinVal("N");
				orderEntryRequest.setMinRedeem("Y");
				orderEntryRequest.setDpc("N");
				orderEntryRequest.setIpAdd("");
				orderEntryRequest.setPassword(res[1]);
				orderEntryRequest.setPassKey(getPasswordRequest.getPassKey());
				orderEntryRequest.setParma1("");
				orderEntryRequest.setParma2("");
				orderEntryRequest.setParma3("");
				OrderEntryResponse orderEntryResponse = orderMfService.getOrderEntry(orderEntryRequest);
				if ((orderEntryResponse.getOrderId() != null  && !orderEntryResponse.getOrderId().equals("0")
						&& !orderEntryResponse.getBseRemarks().contains("FAILED: PASSWORD EXPIRED")
						&& !orderEntryResponse.getBseRemarks().contains("DUPLICAT UNIQUE REF NO."))
						|| (orderEntryResponse.getBseRemarks().contains("FAILED: ORDER NOT FOUND FOR ORDER ID"))) {
					if (orderEntryResponse.getBseRemarks().contains("FAILED: ORDER NOT FOUND FOR ORDER ID")) {
						response = "CXL CONF: Your Request to Cancel Order No" + orderEntryRequest.getOrderId() + " is confirmed.";
					} else {
						response = orderEntryResponse.getBseRemarks();
					}
					responseInfo = GoForWealthPRSUtil.getSuccessResponseInfo(GoForWealthPRSErrorMessageEnum.SUCCESS_CODE.getValue(),GoForWealthPRSErrorMessageEnum.SUCCESS_MESSAGE.getValue(), response);
					orders.setStatus(GoForWealthPRSConstants.ORDER_CANCEL_STATUS);
					if(orders.getGoalId() != null){
						orders.setGoalId(null);
						orders.setGoalName(null);
					}
					for (OrderItem orderItem : orders.getOrderItems()) {
						orderItem.setStatus(GoForWealthPRSConstants.ORDER_CANCEL_STATUS);
					}
					String emailSubject = "";
					String emailContent = "";
					try {
						DecimalFormat df = new DecimalFormat("##");
						String amount = df.format((Math.round(Double.valueOf(orderValue) * 100.0) / 100.0));
						String sipId = orders.getBseOrderId();
						String userName = user.getFirstName() == null ? user.getUsername() : user.getFirstName() + " " + user.getLastName();
						if (orders.getType().equals("LUMPSUM")) {
							emailSubject = messageSource.getMessage("lumpsum.cancellation.mailSubject", null,Locale.ENGLISH);
							emailContent = messageSource.getMessage("lumpsum.cancellation.mailBody",new String[] { userName, schemeName, amount, paymentDate, sipId }, Locale.ENGLISH);
							mailUtility.baselineExample(user.getEmail(), emailSubject, emailContent);
						}
					} catch (NoSuchMessageException | IOException e) {
						e.printStackTrace();
					}
				} else {
					responseInfo = GoForWealthPRSUtil.getSuccessResponseInfo(GoForWealthPRSErrorMessageEnum.FAILURE_CODE.getValue(),GoForWealthPRSErrorMessageEnum.FAILURE_MESSAGE.getValue(),orderEntryResponse.getBseRemarks());
				}
				orders.setField3(orderEntryResponse.getBseRemarks());
				orders.setLastupdate(new Date());
				orderRepository.save(orders);
			}
		}
		if(responseInfo.getMessage().equals("Success")){
			if(goalId != 0){
				if(orderRepository.findByGoalId(goalId).isEmpty()){
					GoalOrderItems goalOrderItems =  goalOrderItemsRepository.findByGoalOrderItemId(goalId);
					goalOrderItems.setField1(0);
					goalOrderItemsRepository.save(goalOrderItems);
				}
			}
		}
		return responseInfo;
	}

	private GoForWealthPRSResponseInfo cancelSipOrder(User user, Orders orders) {
		GoForWealthPRSResponseInfo responseInfo = null;
		OrderUniqueRef orderUniqueRef = orderUniqueRefRepository.getOne(1);
		String uniqueRefNo = orderUniqueRef.getOrderUniqueRefNo();
		long refNo = Long.valueOf(uniqueRefNo);
		refNo = refNo + 1;
		orderUniqueRef.setOrderUniqueRefNo(refNo + "");
		orderUniqueRefRepository.save(orderUniqueRef);
		String schemeCode = "";
		String orderValue = "";
		String paymentDate = "";
		String datOfSip = "";
		String schemeName = "";
		for (OrderItem orderItem : orders.getOrderItems()) {
			schemeCode = orderItem.getScheme().getSchemeCode();
			orderValue = orderItem.getProductprice().toString();
			paymentDate = orderItem.getDescription();
			datOfSip = orderItem.getField2();
			schemeName = orderItem.getScheme().getSchemeName();
		}
		XsipOrderEntryRequest xsipOrderEntryRequest = new XsipOrderEntryRequest();
		GetPasswordRequest getPasswordRequest = new GetPasswordRequest();
		List<SystemProperties> systemPropertiesList = systemPropertiesRepository.findAll();
		for (SystemProperties systemProperties : systemPropertiesList) {
			if (systemProperties.getId().getPropertyKey().equals("UserId")) {
				getPasswordRequest.setUserId(systemProperties.getId().getPropertyValue());
			} else if (systemProperties.getId().getPropertyKey().equals("MemberId")) {
				getPasswordRequest.setMemberId(systemProperties.getId().getPropertyValue());
			} else if (systemProperties.getId().getPropertyKey().equals("Password")) {
				getPasswordRequest.setPassword(systemProperties.getId().getPropertyValue());
			}
		}
		OtpGenerator otpGenerator = new OtpGenerator(5, true);
		String passKey = otpGenerator.generateOTP();
		getPasswordRequest.setPassKey(passKey);
		String passwordResponse = orderMfService.getPassword(getPasswordRequest);
		String[] res = passwordResponse.split("\\|");
		if (res[0].equals("100")) {
			xsipOrderEntryRequest.setTransactionCode("CXL");
			xsipOrderEntryRequest.setUserID(getPasswordRequest.getUserId());
			xsipOrderEntryRequest.setPassKey(getPasswordRequest.getPassKey());
			xsipOrderEntryRequest.setPassword(res[1]);
			if (orders.getPaymentOptions().equals("Natch")) {
				xsipOrderEntryRequest.setParma2("");
				xsipOrderEntryRequest.setMandateID(user.getOnboardingStatus().getMandateNumber());
			} else if (orders.getPaymentOptions().equals("Biller")) {
				xsipOrderEntryRequest.setParma2(user.getOnboardingStatus().getIsipMandateNumber());
				xsipOrderEntryRequest.setMandateID("");
			} else {
				xsipOrderEntryRequest.setMandateID(user.getOnboardingStatus().getMandateNumber());
			}
			xsipOrderEntryRequest.setMemberCode(getPasswordRequest.getMemberId());
			xsipOrderEntryRequest.setSchemeCode(schemeCode);
			xsipOrderEntryRequest.setStartDate("");
			xsipOrderEntryRequest.setSubberCode("");
			xsipOrderEntryRequest.setUniqueRefNo(uniqueRefNo);
			xsipOrderEntryRequest.setTransMode("P");
			if (orders.getField2() != null && !orders.getField2().equals("N")) {
				xsipOrderEntryRequest.setXsipRegID(orders.getField2());
			} else {
				xsipOrderEntryRequest.setXsipRegID(orders.getBseOrderId());
			}
			xsipOrderEntryRequest.setBrokerage("");
			xsipOrderEntryRequest.setClientCode(user.getOnboardingStatus().getClientCode());
			xsipOrderEntryRequest.setDpc("N");
			xsipOrderEntryRequest.setDpTxnMode("P");
			xsipOrderEntryRequest.setEuin("");
			xsipOrderEntryRequest.setEuinVal("N");
			xsipOrderEntryRequest.setFirstOrderFlag("");
			xsipOrderEntryRequest.setFolioNo("");
			xsipOrderEntryRequest.setFrequencyAllowed("1");
			xsipOrderEntryRequest.setFrequencyType("MONTHLY");
			xsipOrderEntryRequest.setInstallmentAmount("");
			xsipOrderEntryRequest.setInternalRefNo("");
			xsipOrderEntryRequest.setIpAdd("");
			xsipOrderEntryRequest.setNoOfInstallment("");
			xsipOrderEntryRequest.setRemarks("");
			xsipOrderEntryRequest.setParma1("");
			xsipOrderEntryRequest.setParma3("");
			XsipOrderEntryResponse xsipOrderEntryResonse = orderMfService.getXsipOrderEntry(xsipOrderEntryRequest);//X-SIP OR I-SIP CAN BE CANCELLED ONLY PRIOR TO 6 DAYS OF TRIGGER DATE
			if (xsipOrderEntryResonse.getXsipRegId() != null && !xsipOrderEntryResonse.getXsipRegId().equals("0")
					&& !xsipOrderEntryResonse.getBseRemarks().contains("X-SIP OR I-SIP CAN BE CANCELLED ONLY PRIOR TO")
					&& !xsipOrderEntryResonse.getBseRemarks().contains("FAILED: PASSWORD EXPIRED")
					&& !xsipOrderEntryResonse.getBseRemarks().contains("DUPLICAT UNIQUE REF NO.")
					|| xsipOrderEntryResonse.getBseRemarks().contains("IS CANCELLED SUCCESSFULLY")) {
				if(xsipOrderEntryResonse.getBseRemarks().contains("X-SIP OR I-SIP HAS BEEN ALREADY CANCELLED OR NOT EXISTS")){
					responseInfo = GoForWealthPRSUtil.getSuccessResponseInfo(GoForWealthPRSErrorMessageEnum.SUCCESS_CODE.getValue(),GoForWealthPRSErrorMessageEnum.SUCCESS_MESSAGE.getValue(),"X-SIP OR I-SIP Cancelled Successfully");
				}else{
					responseInfo = GoForWealthPRSUtil.getSuccessResponseInfo(GoForWealthPRSErrorMessageEnum.SUCCESS_CODE.getValue(),GoForWealthPRSErrorMessageEnum.SUCCESS_MESSAGE.getValue(),xsipOrderEntryResonse.getBseRemarks());
				}	
				List<Orders> orderList  = orderRepository.findByField2(orders.getField2());
				if(!orderList.isEmpty()){
					for (Orders orders2 : orderList) {
						if(orders2.getStatus().equals("C") || orders2.getStatus().equals("AD") || orders2.getStatus().equals("AP") || orders2.getStatus().equals("PR")){
							orders2.setStatus(GoForWealthPRSConstants.ORDER_DEACTIVATED_STATUS);
							orders2.setLastupdate(new Date());
							if(orders2.getGoalId() != null){
								orders2.setGoalId(null);
								orders2.setGoalName(null);
							}
							for (OrderItem orderItem : orders2.getOrderItems()) {
								orderItem.setStatus(GoForWealthPRSConstants.ORDER_DEACTIVATED_STATUS);
								orderItemRepository.save(orderItem);
							}
							List<Ppcpayinst> ppcpayinst1 = ppcpayinstRepository.findByOrderIdWithStatusAPOrAD(orders2.getOrdersId());
							if(!ppcpayinst1.isEmpty()){
								for (Ppcpayinst ppcpayinst2 : ppcpayinst1) {
									ppcpayinst2.setState(GoForWealthPRSConstants.ORDER_DEACTIVATED_STATUS);
									Ppcpaytran ppcpaytran = ppcpayinst2.getPpcpaytran();
									ppcpaytran.setState(1);
									ppcpayinstRepository.save(ppcpayinst2);
									ppcpaytranRepository.save(ppcpaytran);
									break;
								}
							}
							AllotmentStatusReportUserdata allotStatusReportUserData = allotmentStatusReportUserdataRepository.findByOrderNo(Integer.valueOf(orders2.getBseOrderId()));
							OrderStatusReportUserdata osrUserDate = orderStatusReportUserdataRepository.getOrderStatusByOrderId(orders2.getBseOrderId());
							if(allotStatusReportUserData != null && osrUserDate != null){
								allotStatusReportUserData.setStatus(GoForWealthPRSConstants.ORDER_DEACTIVATED_STATUS);
								osrUserDate.setStatus(GoForWealthPRSConstants.ORDER_DEACTIVATED_STATUS);
								allotmentStatusReportUserdataRepository.save(allotStatusReportUserData);
								orderStatusReportUserdataRepository.save(osrUserDate);
							}			
							orders.setLastupdate(new Date());
							orders.setField3(xsipOrderEntryResonse.getBseRemarks());
							orderRepository.save(orders2);
						}else if(orders2.getStatus().equals("M") || orders2.getStatus().equals("IO")){
							orders2.setStatus(GoForWealthPRSConstants.ORDER_CANCEL_STATUS);
							orders2.setLastupdate(new Date());
							if(orders2.getGoalId() != null){
								orders2.setGoalId(null);
								orders2.setGoalName(null);
							}
							for (OrderItem orderItem : orders2.getOrderItems()) {
								orderItem.setStatus(GoForWealthPRSConstants.ORDER_CANCEL_STATUS);
								orderItemRepository.save(orderItem);
							}
							orders.setLastupdate(new Date());
							orders.setField3(xsipOrderEntryResonse.getBseRemarks());
							orderRepository.save(orders2);
						}
					}
				}
				String emailSubject = "";
				String emailContent = "";
				try {
					//MaintainCurrentNavCurrentValueForOrders mcn = maintainCurrentNavCurrentValueForOrdersRepository.findBySipRegnNo(Long.parseLong(orders.getField2()));
					String amount = "";
					/*if(mcn == null){
						 amount = orders.getOrderItems().iterator().next().getProductprice().setScale(2, RoundingMode.HALF_UP).toString();
					}else{
						 amount = mcn.getCurrentAmount().setScale(2, RoundingMode.HALF_UP).toString();
					}*/
					 amount = orders.getOrderItems().iterator().next().getProductprice().setScale(2, RoundingMode.HALF_UP).toString();
					String userName = user.getFirstName() == null ? user.getUsername() : user.getFirstName();
					if (orders.getType().equals("SIP")) {
						String sipId = orders.getField2();
						emailSubject = messageSource.getMessage("sip.cancellation.mailSubject", null, Locale.ENGLISH);
						emailContent = messageSource.getMessage("sip.cancellation.mailBody",new String[] { userName, schemeName, amount, datOfSip, sipId }, Locale.ENGLISH);
						mailUtility.baselineExample(user.getEmail(), emailSubject, emailContent);
					}
				} catch (NoSuchMessageException | IOException e) {
					e.printStackTrace();
				}
			} else if(xsipOrderEntryResonse.getBseRemarks().contains("FAILED: PASSWORD EXPIRED")){
				responseInfo = GoForWealthPRSUtil.getSuccessResponseInfo(GoForWealthPRSErrorMessageEnum.FAILURE_CODE.getValue(),GoForWealthPRSErrorMessageEnum.FAILURE_MESSAGE.getValue(),"Something went wrong. Please try after some time");
			}else{
				responseInfo = GoForWealthPRSUtil.getSuccessResponseInfo(GoForWealthPRSErrorMessageEnum.FAILURE_CODE.getValue(),GoForWealthPRSErrorMessageEnum.FAILURE_MESSAGE.getValue(),xsipOrderEntryResonse.getBseRemarks());
			}
		}
		return responseInfo;
	}

	@Override
	public FundSchemeDTO getSchemeDetailsById(Integer schemeId, User user) {
		int totalPurchageAmount = 0;
		int totalPurchageAmountRef = 0;
		boolean isFound = false;
		FundSchemeDTO fundSchemeDTO = new FundSchemeDTO();
		Scheme scheme = schemeRepository.getOne(schemeId);
		fundSchemeDTO.setSchemeId(scheme.getSchemeId());
		fundSchemeDTO.setSchemeCode(scheme.getSchemeCode());
		fundSchemeDTO.setUniqueNo(scheme.getUniqueNo());
		fundSchemeDTO.setSchemeName(scheme.getSchemeName());
		fundSchemeDTO.setSchemeType(scheme.getSchemeType());
		fundSchemeDTO.setPlan(scheme.getSchemePlan());
		fundSchemeDTO.setFaceValue(scheme.getFaceValue());
		fundSchemeDTO.setMinimumPurchaseAmount(scheme.getMinimumPurchaseAmount());
		fundSchemeDTO.setSipAllowed(scheme.getSipFlag());
		fundSchemeDTO.setPurchaseAllowed(scheme.getPurchaseAllowed());
		fundSchemeDTO.setMinAdditionalAmount(new BigDecimal(scheme.getAdditionalPurchaseAmount()));
		fundSchemeDTO.setSipMaximumGap(scheme.getSipMaximumGap());
		fundSchemeDTO.setAmcCode(scheme.getAmcCode());
		String schemeCode = getSchemeCodeWithL1(scheme.getRtaSchemeCode(), scheme.getIsin(), scheme.getSchemeCode());/*scheme.getSchemeCode() + "-L1";*/
		Scheme schemeWithL1 = schemeRepository.findBySchemeCodeWithL1(schemeCode);
		if (schemeWithL1 != null) {
			if (!schemeWithL1.getMaximumPurchaseAmount().equals("0.000")) {
				fundSchemeDTO.setMaximumPurchaseAmount(schemeWithL1.getMaximumPurchaseAmount());
			} else {
				fundSchemeDTO.setMaximumPurchaseAmount("999999999.000");
			}
		} else {
			fundSchemeDTO.setMaximumPurchaseAmount(scheme.getMaximumPurchaseAmount());
		}
		//fundSchemeDTO.setMaximumPurchaseAmount(scheme.getMaximumPurchaseAmount());
		fundSchemeDTO.setSchemeLaunchDate(scheme.getStartDate());
		fundSchemeDTO.setSchemeEndDate(scheme.getEndDate());
		if (scheme.getMinSipAmount() != null) {
			fundSchemeDTO.setMinSipAmount(scheme.getMinSipAmount());
		}
		if (scheme.getSipDates() != null) {
			fundSchemeDTO.setSipAllowedDate(scheme.getSipDates());
		}
		DecimalFormat df = new DecimalFormat("##");
		if (schemeWithL1 != null) {
			if (schemeWithL1.getSipMaximumInstallmentAmount() != null && !schemeWithL1.getSipMaximumInstallmentAmount().toString().equals("0.00000")) {
				fundSchemeDTO.setSipMaxInstallmentAmount(df.format((Math.round(Double.valueOf(schemeWithL1.getSipMaximumInstallmentAmount().toString()) * 100.0) / 100.0)));
			} else {
				if (scheme.getSipMaximumInstallmentAmount() != null) {
					fundSchemeDTO.setSipMaxInstallmentAmount(df.format((Math.round(Double.valueOf(scheme.getSipMaximumInstallmentAmount().toString()) * 100.0) / 100.0)));
				}
			}
		} else {
			if (scheme.getSipMaximumInstallmentAmount() != null) {
				fundSchemeDTO.setSipMaxInstallmentAmount(df.format((Math.round(Double.valueOf(scheme.getSipMaximumInstallmentAmount().toString()) * 100.0) / 100.0)));
			}
		}
		if (scheme.getSipMinimumInstallmentNumber() != null) {
			fundSchemeDTO.setSipMinInstallmentNumber(scheme.getSipMinimumInstallmentNumber());
		}
		List<Orders> ordersList = orderRepository.getOrderDetailWithStatusCAndPA(user.getUserId());
		if (!ordersList.isEmpty()) {
			for (Orders orders : ordersList) {
				OrderItem orderItem = orderItemRepository.getOrderItemByOrderId(orders.getOrdersId());
				if (orderItem != null) {
					if (orders.getType().equals("SIP")) {
						int totalPurchageAmountRef1 = orderItem.getProductprice().intValueExact() * 12;
						totalPurchageAmountRef = totalPurchageAmountRef + totalPurchageAmountRef1;
					}
					if (orders.getType().equals("LUMPSUM")) {
						totalPurchageAmountRef = totalPurchageAmountRef + orderItem.getProductprice().intValueExact();
					}
				}
			}
			totalPurchageAmount = totalPurchageAmountRef;
		} else {
			totalPurchageAmount = 0;
		}
		fundSchemeDTO.setTotalPurchageAmount(totalPurchageAmount);
		String kycStatus = "";
		int overAllStatus = user.getOnboardingStatus().getOverallStatus();
		if (user.getPanDetails().getVerified() != null) {
			kycStatus = user.getPanDetails().getVerified();
		}
		if (kycStatus.equals("verified") && overAllStatus == 1) {
			fundSchemeDTO.setIsOrderAllowed("true");
		}
		if (kycStatus.equals("not-verified") && overAllStatus == 1) {
			fundSchemeDTO.setIsOrderAllowed("false");
		}
		OnboardingStatus onboardingStatus = user.getOnboardingStatus();
		if (onboardingStatus != null) {
			if (onboardingStatus.getMandateNumber() != null && !onboardingStatus.getEnachStatus().equals("APPROVED")) {
				fundSchemeDTO.setIsEnachEnable("true");
			} else {
				fundSchemeDTO.setIsEnachEnable("false");
			}
			if (onboardingStatus.getIsipMandateNumber() != null && !onboardingStatus.getBillerStatus().equals("APPROVED")) {
				fundSchemeDTO.setIsBillerEnable("true");
			} else {
				fundSchemeDTO.setIsBillerEnable("false");
			}
		}
		List<IsipAllowedBankList> isipAllowedbankList = isipAllowedBankListRepository.findAll();
		if (!isipAllowedbankList.isEmpty()) {
			for (IsipAllowedBankList isipAllowedBankList2 : isipAllowedbankList) {
				if (user.getBankDetails().getBankName().contains(isipAllowedBankList2.getBankName().toUpperCase())) {
					isFound = true;
					break;
				}
			}
		}
		if (isFound) {
			fundSchemeDTO.setIsIsipAllowed("true");
		} else {
			fundSchemeDTO.setIsIsipAllowed("false");
		}
		return fundSchemeDTO;
	}

	@Override
	public FundSchemeDTO getSchemeDetailsByCode(String schemeCode) {
		FundSchemeDTO fundSchemeDTO = new FundSchemeDTO();
		Scheme scheme = schemeRepository.findBySchemeCode(schemeCode);
		fundSchemeDTO.setSchemeId(scheme.getSchemeId());
		fundSchemeDTO.setSchemeCode(scheme.getSchemeCode());
		fundSchemeDTO.setUniqueNo(scheme.getUniqueNo());
		fundSchemeDTO.setSchemeName(scheme.getSchemeName());
		fundSchemeDTO.setSchemeType(scheme.getSchemeType());
		fundSchemeDTO.setPlan(scheme.getSchemePlan());
		fundSchemeDTO.setFaceValue(scheme.getFaceValue());
		fundSchemeDTO.setMinimumPurchaseAmount(scheme.getMinimumPurchaseAmount());
		fundSchemeDTO.setSipAllowed(scheme.getSipFlag());
		fundSchemeDTO.setPurchaseAllowed(scheme.getPurchaseAllowed());
		fundSchemeDTO.setMaximumPurchaseAmount(scheme.getMaximumPurchaseAmount());
		fundSchemeDTO.setSchemeLaunchDate(scheme.getStartDate());
		fundSchemeDTO.setSchemeEndDate(scheme.getEndDate());
		if (scheme.getMinSipAmount() != null) {
			fundSchemeDTO.setMinSipAmount(scheme.getMinSipAmount());
		}
		if (scheme.getSipDates() != null) {
			fundSchemeDTO.setSipAllowedDate(scheme.getSipDates());
		}
		DecimalFormat df = new DecimalFormat("##");
		if (scheme.getSipMaximumInstallmentAmount() != null) {
			fundSchemeDTO.setSipMaxInstallmentAmount(df.format((Math.round(Double.valueOf(scheme.getSipMaximumInstallmentAmount().toString()) * 100.0) / 100.0)));
		}
		if (scheme.getSipMinimumInstallmentNumber() != null) {
			fundSchemeDTO.setSipMinInstallmentNumber(scheme.getSipMinimumInstallmentNumber());
		}
		return fundSchemeDTO;
	}

	@Override
	public String makePayment(List<AddToCartDTO> addToCartDTO, Integer userId) throws GoForWealthPRSException {
		String paymentResponse = "";
		String paymentAmount = addToCartDTO.get(0).getPaymentAmount();
//		String idList = addToCartDTO.get(0).getOrderId().toString();
		String idList = "";
		User user = userRepository.getOne(userId);
		List<Integer> orderList = new ArrayList<Integer>();
		for (AddToCartDTO addToCartDTO2 : addToCartDTO) {
			orderList.add(new Integer(addToCartDTO2.getOrderId()));
			idList += addToCartDTO2.getOrderId()+"_";
		}
		
		List<String> bseOrderList = orderRepository.findByOrdersId(orderList);
		//String[] OrderList = new String[orderId.size()];
		
		//Orders order = orderRepository.getOne(orderId);
		if (!bseOrderList.isEmpty()) {
			//String[] orderList = new String[1];
			String[] bsePaymentOrderList = bseOrderList.toArray(new String[bseOrderList.size()]);
			//String[] Order = new String[1];
		//	Order[0] = order.getBseOrderId();
			// calling payment api	
			paymentResponse = paymentService.doPayment(userId, bsePaymentOrderList, paymentAmount,idList);
			if (!paymentResponse.contains("Please check your mail") && !paymentResponse.contains("Payment initiated for given OrderNo. Please wait some time.")) {
				StoreConf imageUrl = storeConfRepository.findByKeyword(GoForWealthPRSConstants.IMAGE_LOCATION);
				StoreConf path1 = storeConfRepository.findByKeyword(GoForWealthPRSConstants.PAYMENT_RESPONSE_HTML_LOCATION);
				StoreConf forwardSlash = storeConfRepository.findByKeyword(GoForWealthPRSConstants.FORWARD_SLASH);
				String path = imageUrl.getKeywordValue() + path1.getKeywordValue();
				String destPath = path + forwardSlash.getKeywordValue() + userId.toString() + "_" + idList + ".html";
				
				
				for (AddToCartDTO addToCartDTO2 : addToCartDTO) {
					Orders order = orderRepository.findByOrdersId(addToCartDTO2.getOrderId());
					Ppcpayinst ppcpayinst= new Ppcpayinst();
					ppcpayinst.setAmount(new BigDecimal(addToCartDTO2.getOrderAmount()));
					ppcpayinst.setAccountNo(user.getBankDetails().getAccountNo());
					ppcpayinst.setOrders(order);
					ppcpayinst.setState("P");
					ppcpayinst.setApprovingamount(new BigDecimal(addToCartDTO2.getOrderAmount()));
					ppcpayinst.setApprovedamount(new BigDecimal(0.0));
					ppcpayinst.setCreditedamount(new BigDecimal(0.0));
					ppcpayinst.setCreditingamount(new BigDecimal(addToCartDTO2.getOrderAmount()));
					ppcpayinst.setCurrency("INR");
					ppcpayinst.setMarkfordelete(0);
					Ppcpaytran ppcpaytran= new Ppcpaytran();
					ppcpaytran.setPpcpayinst(ppcpayinst);
					ppcpaytran.setProcessedamount(new BigDecimal(0.0));
					ppcpaytran.setState(0);
					ppcpaytranRepository.save(ppcpaytran);
				}
				BufferedWriter bufferedWriter = null;
				try {
					File myFile = new File(destPath);
					if (!myFile.exists()) {
						myFile.createNewFile();
					}
					Writer writer = new FileWriter(myFile);
					bufferedWriter = new BufferedWriter(writer);
					bufferedWriter.write(paymentResponse);
				} catch (IOException e) {
					e.printStackTrace();
				} finally{
					try{
						if(bufferedWriter != null) bufferedWriter.close();
					} catch(Exception ex){

					}
				}
				
				
				
				/*BufferedWriter bufferedWriter = null;
				try {
					File myFile = new File(destPath);
					if (!myFile.exists()) {
						myFile.createNewFile();
					}
					Writer writer = new FileWriter(myFile);
					bufferedWriter = new BufferedWriter(writer);
					bufferedWriter.write(paymentResponse);
				} catch (IOException e) {
					e.printStackTrace();
				} finally {
					try {
						if (bufferedWriter != null)
							bufferedWriter.close();
						} catch (Exception ex) {
						}
				}
				Ppcpayinst ppcpayinst = new Ppcpayinst();
				ppcpayinst.setAmount(new BigDecimal(amount));
				ppcpayinst.setAccountNo(user.getBankDetails().getAccountNo());
				ppcpayinst.setOrders(order);
				ppcpayinst.setState("P");
				ppcpayinst.setApprovingamount(new BigDecimal(amount));
				ppcpayinst.setApprovedamount(new BigDecimal(0.0));
				ppcpayinst.setCreditedamount(new BigDecimal(0.0));
				ppcpayinst.setCreditingamount(new BigDecimal(amount));
				ppcpayinst.setCurrency("INR");
				ppcpayinst.setMarkfordelete(0);
				// ppcpayinst= ppcpayinstRepository.save(ppcpayinst);
				Ppcpaytran ppcpaytran = new Ppcpaytran();
				ppcpaytran.setPpcpayinst(ppcpayinst);
				ppcpaytran.setProcessedamount(new BigDecimal(0.0));
				ppcpaytran.setState(0);
				ppcpaytranRepository.save(ppcpaytran);*/
			}
		}
		return paymentResponse;
	}

	@Override
	public List<AddToCartDTO> getOrderTransactionForAdmin(Integer userId) {
		List<AddToCartDTO> cartList = new ArrayList<>();
		AddToCartDTO addToCartDTO = new AddToCartDTO();
		List<Orders> ordersList = orderRepository.findUserOrderWithStatusMAndC(userId);
		for (Orders orders : ordersList) {
			List<Ppcpayinst> ppcpayinst = ppcpayinstRepository.getAllUserTransactionByOrders(orders.getOrdersId());
			if (!ppcpayinst.isEmpty()) {
				for (Ppcpayinst ppcpayinst2 : ppcpayinst) {
					Orders orders1 = orderRepository.getOne(orders.getOrdersId());
					OrderItem orderItem = orderItemRepository.getOrderItemByOrderId(orders.getOrdersId());
					addToCartDTO = new AddToCartDTO();
					String transacId = orders1.getBseOrderId();
					Integer transId = Integer.parseInt(transacId);
					addToCartDTO.setOrderId(transId.intValue());
					addToCartDTO.setAmount(orderItem.getProductprice().doubleValue());
					addToCartDTO.setDayOfSip(orderItem.getDescription());
					addToCartDTO.setSchemeId(orderItem.getScheme().getSchemeId());
					addToCartDTO.setSchemeName(orderItem.getScheme().getSchemeName());
					addToCartDTO.setStatus(ppcpayinst2.getState());
					if (ppcpayinst2.getState().equals("C")) {
						AllotmentStatusReportUserdata allotmentStatusReportUserdatas = allotmentStatusReportUserdataRepository.getUserDataByOrderNumber(transId);
						if (allotmentStatusReportUserdatas == null) {
							SimpleDateFormat sdf = new SimpleDateFormat("dd MMMM yyyy");
							String orderDate = sdf.format(ppcpayinst2.getTimeupdated());
							addToCartDTO.setOrderDate(orderDate);
						} else {
							addToCartDTO.setOrderDate(allotmentStatusReportUserdatas.getOrderDate());
						}
					} else {
						SimpleDateFormat sdf = new SimpleDateFormat("dd MMMM yyyy");
						String orderDate = sdf.format(ppcpayinst2.getTimeupdated());
						addToCartDTO.setOrderDate(orderDate);
					}
					addToCartDTO.setType(orders1.getType());
					cartList.add(addToCartDTO);
				}
			}
		}
		return cartList;
	}

	@Override
	public List<AddToCartDTO> getOrderTransaction(Integer userId,String folioNo,String schemeCode) {
		List<AddToCartDTO> cartList = new ArrayList<>();
		List<Ppcpayinst> ppcpayinst = null;
		List<String> bseOrderIdList = new ArrayList<>();
		List<Orders> ordersList = new ArrayList<>();
		List<AllotmentStatusReportUserdata> allotmentStatusReportUserdataList = allotmentStatusReportUserdataRepository.findByFolioNoAndSchemeCode(folioNo,schemeCode);
		if(!allotmentStatusReportUserdataList.isEmpty()){
			for (AllotmentStatusReportUserdata allotmentStatusReportUserdata : allotmentStatusReportUserdataList) {
				bseOrderIdList.add(allotmentStatusReportUserdata.getOrderNo().toString());
			}
			 ordersList = orderRepository.findByBseOrderId(bseOrderIdList);
		}
		if(!ordersList.isEmpty()){
			for (Orders orders : ordersList) {
				if(orders != null){
						ppcpayinst = ppcpayinstRepository.getAllUserTransactionByOrders(orders.getOrdersId());
						cartList = addTransactionDetailToList(ppcpayinst, cartList, orders);
				}
			}
		}
		return cartList;
	}
	
	/*
	@Override
	public List<AddToCartDTO> getOrderTransaction(Integer userId,String folioNo,String schemeCode) {
		List<AddToCartDTO> cartList = new ArrayList<>();
		List<Ppcpayinst> ppcpayinst = null;
		List<String> bseOrderIdList = new ArrayList<>();
		List<Orders> ordersList = new ArrayList<>();
		List<AllotmentStatusReportUserdata> allotmentStatusReportUserdataList = allotmentStatusReportUserdataRepository.findByFolioNoAndSchemeCode(folioNo,schemeCode);
		if(!allotmentStatusReportUserdataList.isEmpty()){
			for (AllotmentStatusReportUserdata allotmentStatusReportUserdata : allotmentStatusReportUserdataList) {
				bseOrderIdList.add(allotmentStatusReportUserdata.getOrderNo().toString());
			}
			 
		}
		if(!bseOrderIdList.isEmpty()){
			for (String bseOrderIdList1 : bseOrderIdList) {
				Orders order = orderRepository.findByBseOrderId(bseOrderIdList1);
						if(order != null){
							ordersList.add(order);
						}
			}
		}
		if(!ordersList.isEmpty()){
			for (Orders orders : ordersList) {
				if(orders != null){
					if(orders.getType().equals("SIP")){
						List<Orders> orderList = orderRepository.getOrderListByRegnId(orders.getField2());
						for (Orders orders2 : orderList) {
							if(orders2.getStatus().equals("C") 
								|| orders2.getStatus().equals("AD")
								|| orders2.getStatus().equals("AP")
								|| orders2.getStatus().equals("AF")
								|| orders2.getStatus().equals("PR")
								|| orders2.getStatus().equals("R")
								|| orders2.getStatus().equals("OD")
							){
								ppcpayinst = ppcpayinstRepository.getAllUserTransactionByOrders(orders2.getOrdersId());
								cartList = addTransactionDetailToList(ppcpayinst, cartList, orders2);	
							}
						}
					}else{
						ppcpayinst = ppcpayinstRepository.getAllUserTransactionByOrders(orders.getOrdersId());
						cartList = addTransactionDetailToList(ppcpayinst, cartList, orders);
					}
				}
			}
		}
		return cartList;
	}
	*/
	
	public List<AddToCartDTO> addTransactionDetailToList(List<Ppcpayinst> ppcpayinst,List<AddToCartDTO> cartList,Orders orders){
		if(!ppcpayinst.isEmpty()){
			for (Ppcpayinst ppcpayinst2 : ppcpayinst) {
				OrderItem orderItem = orderItemRepository.getOrderItemByOrderId(orders.getOrdersId());
				AddToCartDTO addToCartDTO = new AddToCartDTO();
				String transacId = orders.getBseOrderId();
				Integer transId = Integer.parseInt(transacId);
				addToCartDTO.setOrderId(transId.intValue());
				addToCartDTO.setAmount(orderItem.getProductprice().doubleValue());
				addToCartDTO.setDayOfSip(orderItem.getDescription());
				addToCartDTO.setSchemeId(orderItem.getScheme().getSchemeId());
				addToCartDTO.setSchemeName(orderItem.getScheme().getSchemeName());
				addToCartDTO.setStatus(ppcpayinst2.getState());
				if (ppcpayinst2.getState().equals("C")
						|| ppcpayinst2.getState().equals("AD")
						|| ppcpayinst2.getState().equals("AP")
						|| ppcpayinst2.getState().equals("PR")
						|| ppcpayinst2.getState().equals("R")
						|| ppcpayinst2.getState().equals("OD")
					) {
						AllotmentStatusReportUserdata allotmentStatusReportUserdatas = allotmentStatusReportUserdataRepository.getUserDataByOrderNumber(transId);
						if (allotmentStatusReportUserdatas == null) {
							SimpleDateFormat sdf = new SimpleDateFormat("dd MMMM yyyy");
							String orderDate = sdf.format(ppcpayinst2.getTimeupdated());
							addToCartDTO.setOrderDate(orderDate);
						} else {
							addToCartDTO.setOrderDate(allotmentStatusReportUserdatas.getOrderDate());
						}
					} else {
						SimpleDateFormat sdf = new SimpleDateFormat("dd MMMM yyyy");
						String orderDate = sdf.format(ppcpayinst2.getTimeupdated());
						addToCartDTO.setOrderDate(orderDate);
					}
					addToCartDTO.setType(orders.getType());
					cartList.add(addToCartDTO);
				}
			}
			return cartList;
	}

	/*@Override
	public List<PortFolioDataDTO> getPortfolio(Integer userId) {
		*//** New Code Change Start **//*
		List<PortFolioDataDTO> portFolioDataDTOListFinal = new ArrayList<>();
		List<PortFolioDataDTO> transferInRecordList = new ArrayList<>();
		*//** New Code Change End **//*
		
		List<PortFolioDataDTO> portFolioDataDTOList = new ArrayList<PortFolioDataDTO>();
		List<String> orderNumber = new ArrayList<String>();
		String goalName = null;
		int goalId = 0;
		User user = userRepository.findByUserId(userId);
		String ClientCode = user.getOnboardingStatus().getClientCode();
		List<AllotmentStatusReportUserdata> allotmentStatusReportUserdatas = new ArrayList<AllotmentStatusReportUserdata>();
		List<OrderStatusReportUserdata> orderStatusReportUserdatasList = orderStatusReportUserdataRepository.getDetailByClientCode(ClientCode);
		if(!orderStatusReportUserdatasList.isEmpty()){
			for (OrderStatusReportUserdata orderStatusReportUserdata : orderStatusReportUserdatasList) {
				if(orderStatusReportUserdata.getSipRegnNo() !=0){
					List<OrderStatusReportUserdata> filteredOrderNoList = orderStatusReportUserdataRepository.getDebitedInstallmentLastUpdated(orderStatusReportUserdata.getSipRegnNo());
					int index = filteredOrderNoList.size()-1;
					orderNumber.add(filteredOrderNoList.get(index).getOrderNumber());
				}	
			}
		}
		List<OrderStatusReportUserdata> orderStatusReportUserdatasLumpSumList = orderStatusReportUserdataRepository.findByClientCode(ClientCode);
		if(!orderStatusReportUserdatasLumpSumList.isEmpty()){
			for (OrderStatusReportUserdata orderStatusReportUserdata : orderStatusReportUserdatasLumpSumList) {
				if(orderStatusReportUserdata.getSipRegnNo() ==0){
					orderNumber.add(orderStatusReportUserdata.getOrderNumber());
				}
			}
		}
		for (String orderNo : orderNumber) {
			AllotmentStatusReportUserdata allotmentStatusReportUserdata = allotmentStatusReportUserdataRepository.findByBseOrderId(Integer.parseInt(orderNo));
			if(allotmentStatusReportUserdata != null){
				allotmentStatusReportUserdatas.add(allotmentStatusReportUserdata);
			}
		}
		if (!allotmentStatusReportUserdatas.isEmpty()) {
			for (AllotmentStatusReportUserdata allotmentStatusReportUserdata : allotmentStatusReportUserdatas) {
				BigDecimal allotedUnit = new BigDecimal(0);
				BigDecimal investedAmount = new BigDecimal(0);
				BigDecimal currentAmount = new BigDecimal(0);
				PortFolioDataDTO portFolioDataDTO = new PortFolioDataDTO();
				portFolioDataDTO.setFollioNo(allotmentStatusReportUserdata.getFolioNo());
				//portFolioDataDTO.setNav(new BigDecimal(allotmentStatusReportUserdata.getAllottedNav().setScale(2, RoundingMode.HALF_UP).toString()));
				Orders orders = orderRepository.getOrderDataByBseOrderId(allotmentStatusReportUserdata.getOrderNo().toString());
				OrderItem orderItem = orderItemRepository.getOrderItemByOrderId(orders.getOrdersId());
				String currentNav = "";
				currentNav = orderItem.getScheme().getCurrentNav();
				if (currentNav == null) {
					currentNav = "0.0";
				}
				OrderStatusReportUserdata orderStatusReportUserdata = orderStatusReportUserdataRepository.getOrderStatusByOrderId(allotmentStatusReportUserdata.getOrderNo().toString());
				*//** New Code Change Start **//*
				if(orderStatusReportUserdata.getSipRegnNo() != null && orderStatusReportUserdata.getSipRegnNo()!=0)
					portFolioDataDTO.setSipRegNo(orderStatusReportUserdata.getSipRegnNo().toString());
				*//** New Code Change End **//*
				try{
					if(orders.getType().equals("SIP")){
						MaintainCurrentNavCurrentValueForOrders mcncvForOrder = maintainCurrentNavCurrentValueForOrdersRepository.findBySipRegnNo(orderStatusReportUserdata.getSipRegnNo());
						SimpleDateFormat sf = new SimpleDateFormat("yyyy-MM-dd");
						SimpleDateFormat sf1 = new SimpleDateFormat("dd/MM/yyyy");
						Date date = sf1.parse(allotmentStatusReportUserdata.getSipRegnDate());
						String startedDate = sf.format(date);
						portFolioDataDTO.setStatedOn(startedDate);
						if(mcncvForOrder == null){
							MaintainCurrentNavCurrentValueForOrders mcn=calculateCurrentValueAndCurrentUnit(orderStatusReportUserdata,"SIP",currentNav,userId,allotmentStatusReportUserdata);
								investedAmount = mcn.getInvestedAmount();
								allotedUnit = mcn.getCurrentUnit();
								currentAmount = mcn.getCurrentAmount();
						}else{
							investedAmount = mcncvForOrder.getInvestedAmount();
							allotedUnit = mcncvForOrder.getCurrentUnit();
							currentAmount = mcncvForOrder.getCurrentAmount();
						}
					}else{
						MaintainCurrentNavCurrentValueForOrders mcncvForOrder = maintainCurrentNavCurrentValueForOrdersRepository.getDetailByOrderId(allotmentStatusReportUserdata.getOrderNo());
						if(mcncvForOrder == null){
							MaintainCurrentNavCurrentValueForOrders mcn=calculateCurrentValueAndCurrentUnit(orderStatusReportUserdata,"LUMPSUM",currentNav,userId,allotmentStatusReportUserdata);
							investedAmount = mcn.getInvestedAmount();
							allotedUnit = mcn.getCurrentUnit();
							currentAmount = mcn.getCurrentAmount();
						}else{
							investedAmount = mcncvForOrder.getInvestedAmount();
							allotedUnit = mcncvForOrder.getCurrentUnit();
							currentAmount = mcncvForOrder.getCurrentAmount();
						}
						portFolioDataDTO.setStatedOn(allotmentStatusReportUserdata.getOrderDate().toString());
					}
				}catch(Exception e){
					e.printStackTrace();
				}
				portFolioDataDTO.setType(orders.getType());
				portFolioDataDTO.setOrderId(orders.getOrdersId());
				Scheme scheme = schemeRepository.findBySchemeCode(allotmentStatusReportUserdata.getSchemeCode());
				if(scheme != null){
					portFolioDataDTO.setSchemeType(scheme.getSchemeType());
					portFolioDataDTO.setSchemeName(scheme.getSchemeName());
				}else{
					portFolioDataDTO.setSchemeType("Not Available");
					portFolioDataDTO.setSchemeName(orderStatusReportUserdata.getSchemeName());
				}
				if (orders.getType().equals("SIP")) {
					if (orderItem.getField2() != null) {
						portFolioDataDTO.setSipDate(orderItem.getField2());
					}

				} else if (orders.getType().equals("LUMPSUM")) {
					if (orderItem.getDescription() != null) {
						portFolioDataDTO.setSipDate(orderItem.getDescription());
					}
				}
				portFolioDataDTO.setAmcCode(orderItem.getScheme().getAmcCode());
				if (orderItem.getScheme().getNavDate() != null) {
					portFolioDataDTO.setCurrentDate(orderItem.getScheme().getNavDate());
				}
				portFolioDataDTO.setStatus(orderItem.getStatus());
				if (orders.getGoalId() != null) {
					goalName = orders.getGoalName();
					goalId = orders.getGoalId();
					if (goalId != 0) {
						portFolioDataDTO.setGoalId(goalId);
						portFolioDataDTO.setGoalName(goalName);
					}
				}
				
				RedumptionResponse redumptionResponse = checkRedumptionAllowed(orders.getOrdersId(), userId);
				if(redumptionResponse != null){
					portFolioDataDTO.setTotalAmount(redumptionResponse.getTotalAmount());
					portFolioDataDTO.setMinimumRedumptionAmount(new BigDecimal(redumptionResponse.getMinimumRedumptionAmount()));
					portFolioDataDTO.setIsRedumptionAllowed(redumptionResponse.getIsRedumptionAllowed());
					
				}
				
				portFolioDataDTO.setCurrentNav((new BigDecimal(currentNav).setScale(2, RoundingMode.HALF_UP)));
                portFolioDataDTO.setCurrentAmount(currentAmount.setScale(2,RoundingMode.HALF_UP));
				portFolioDataDTO.setAllotedUnit(allotedUnit.setScale(2, RoundingMode.HALF_UP));
				portFolioDataDTO.setInvestedAmount(investedAmount.setScale(2, RoundingMode.HALF_UP));
				portFolioDataDTO.setSchemeCode(scheme.getSchemeCode());
				
				if (user.getOnboardingStatus().getMandateNumber() != null && !user.getOnboardingStatus().getEnachStatus().equals("APPROVED")) {
					portFolioDataDTO.setEnachEnable(true);
				} else {
					portFolioDataDTO.setEnachEnable(false);
				}
				if (user.getOnboardingStatus().getIsipMandateNumber() != null && !user.getOnboardingStatus().getBillerStatus().equals("APPROVED")) {
					portFolioDataDTO.setBillerEnable(true);
				} else {
					portFolioDataDTO.setBillerEnable(false);
				}
				List<IsipAllowedBankList> isipAllowedbankList = isipAllowedBankListRepository.findAll();
				boolean isFound = false;
				if (!isipAllowedbankList.isEmpty()) {
					for (IsipAllowedBankList isipAllowedBankList2 : isipAllowedbankList) {
						if (user.getBankDetails().getBankName().contains(isipAllowedBankList2.getBankName().toUpperCase())) {
							isFound = true;
							break;
						}
					}
				}
				if(scheme.getSipDates() != null && !scheme.getSipDates().equals(""))
					portFolioDataDTO.setAvailableSipDate(scheme.getSipDates().split(","));
				portFolioDataDTO.setIsipAllowed(isFound ? true : false);
				portFolioDataDTO.setMinLumpsumAmount(new BigDecimal(scheme.getMinimumPurchaseAmount()));
				portFolioDataDTO.setMinSipAmount(new BigDecimal(scheme.getMinSipAmount()));
				portFolioDataDTO.setSipAllowed(scheme.getSipFlag().equals("Y") ? true : false);
				portFolioDataDTO.setLumpsumAllowed(scheme.getPurchaseAllowed().equals("Y") ? true : false);
				portFolioDataDTOList.add(portFolioDataDTO);
			}
			// Get data from transfer-in table based on userID and sipRegnNo.
			List<TransferIn> transferInList = transferInRepository.findByUserId(userId);
			for (TransferIn transferIn : transferInList) {
				boolean isFound = false;
				for (PortFolioDataDTO portFolioDataDto : portFolioDataDTOList) {
					if(transferIn.getSipRegNo().equals(portFolioDataDto.getSipRegNo())){
						Scheme schemeObj = schemeRepository.findBySchemeCode(portFolioDataDto.getSchemeCode());
						portFolioDataDto.setInvestedAmount(portFolioDataDto.getInvestedAmount().add(transferIn.getAmount()));
						portFolioDataDto.setAllotedUnit(portFolioDataDto.getAllotedUnit().add(transferIn.getUnit()));
						portFolioDataDto.setCurrentAmount(portFolioDataDto.getCurrentAmount().add(transferIn.getUnit().multiply(new BigDecimal(schemeObj.getCurrentNav()))));
						isFound = true;
						break;
					}
				}
				if(!isFound){
					List<Scheme> schemeList = null;
					if(transferIn.getRtaAgent().equals("Cams")){
						schemeList = schemeRepository.findByChannelPartnerCode(transferIn.getChannelPartnerCode());
					}else{
						schemeList = schemeRepository.findByRtaSchemeCodeAndIsin(transferIn.getRtaSchemeCode(),transferIn.getIsin());
					}
					Scheme scheme = null;
					for (Scheme schemeObj : schemeList) {
						if( (!schemeObj.getSchemeCode().contains("-LO")) && (!schemeObj.getSchemeCode().contains("-L1")) && (!schemeObj.getSchemeCode().contains("-L1-I")) && (!schemeObj.getSchemeCode().endsWith("-I"))){
							scheme = schemeObj;
						}
					}
					PortFolioDataDTO portFolioDataDtoObj = new PortFolioDataDTO();
					portFolioDataDtoObj.setFollioNo(transferIn.getFollioNumber());
					portFolioDataDtoObj.setAllotedUnit(transferIn.getUnit());
					portFolioDataDtoObj.setInvestedAmount(transferIn.getAmount());
					portFolioDataDtoObj.setCurrentAmount(transferIn.getUnit().multiply(new BigDecimal(scheme.getCurrentNav())));
					portFolioDataDtoObj.setAmcCode(scheme.getAmcCode());
					portFolioDataDtoObj.setBillerEnable(portFolioDataDTOList.get(0).isBillerEnable());
					portFolioDataDtoObj.setEnachEnable(portFolioDataDTOList.get(0).isEnachEnable());
					if(scheme.getSipDates() != null && !scheme.getSipDates().equals(""))
						portFolioDataDtoObj.setAvailableSipDate(scheme.getSipDates().split(","));
					portFolioDataDtoObj.setIsipAllowed(portFolioDataDTOList.get(0).isIsipAllowed());
					portFolioDataDtoObj.setIsRedumptionAllowed(scheme.getRedemptionAllowed().equals("Y") ? "Yes" : "No");
					portFolioDataDtoObj.setLumpsumAllowed(scheme.getPurchaseAllowed().equals("Y") ? true : false);
					portFolioDataDtoObj.setSipAllowed(scheme.getSipFlag().equals("Y") ? true : false);
					portFolioDataDtoObj.setMinimumRedumptionAmount(new BigDecimal(scheme.getRedemptionAmountMinimum()));
					portFolioDataDtoObj.setMinLumpsumAmount(new BigDecimal(scheme.getMinimumPurchaseAmount()));
					portFolioDataDtoObj.setMinSipAmount(new BigDecimal(scheme.getMinSipAmount()));
					portFolioDataDtoObj.setSchemeCode(scheme.getSchemeCode());
					portFolioDataDtoObj.setSchemeName(scheme.getSchemeName());
					portFolioDataDtoObj.setSchemeType(scheme.getSchemeType());
					portFolioDataDtoObj.setSipRegNo(transferIn.getSipRegNo());
					portFolioDataDtoObj.setSipDate("");
					portFolioDataDtoObj.setOrderId(0);
					portFolioDataDtoObj.setGoalId(0);
					portFolioDataDtoObj.setGoalName("");
					portFolioDataDtoObj.setStatedOn("");
					portFolioDataDtoObj.setStatus("");
					portFolioDataDtoObj.setTotalAmount(new BigDecimal(0));
					portFolioDataDtoObj.setType(transferIn.getInvestmentType());
					transferInRecordList.add(portFolioDataDtoObj);
				}
			}
		}
		portFolioDataDTOListFinal.addAll(portFolioDataDTOList);
		portFolioDataDTOListFinal.addAll(transferInRecordList);
	}
		//return portFolioDataDTOListFinal;
		return portFolioDataDTOList;
		
	}*/
/*
	@Override
	public AddToCartDTO getPortfolioDetails(Integer orderId) {
		AddToCartDTO addToCartDTO = new AddToCartDTO();
		Orders orders = orderRepository.getOne(orderId);
		for (OrderItem orderItem : orders.getOrderItems()) {
			addToCartDTO.setOrderId(orderItem.getOrders().getOrdersId());
			addToCartDTO.setAmount(orderItem.getProductprice().doubleValue());
			addToCartDTO.setDayOfSip(orderItem.getDescription());
			addToCartDTO.setSchemeId(orderItem.getScheme().getSchemeId());
			addToCartDTO.setSchemeName(orderItem.getScheme().getSchemeName());
			addToCartDTO.setStatus(orderItem.getStatus());
			addToCartDTO.setCreationDate(orderItem.getLastupdate());
			addToCartDTO.setType(orders.getType());
			addToCartDTO.setMinimumSipAmount(orderItem.getScheme().getMinimumPurchaseAmount());
			addToCartDTO.setSchemeCode(orderItem.getScheme().getSchemeCode());
			addToCartDTO.setSchemeId(orderItem.getScheme().getSchemeId());
		}
		return addToCartDTO;
	}
*/
	@Override
	public FundSchemeDTO getSchemeDetailBySchemeCode(String toSchemeCode) {
		FundSchemeDTO fundSchemeDTO = new FundSchemeDTO();
		//SimpleDateFormat sdf = new SimpleDateFormat("dd MMMM yyyy");
		Scheme scheme = orderRepository.getSchemeDetailBySchemeCode(toSchemeCode);
		fundSchemeDTO.setSchemeId(scheme.getSchemeId());
		fundSchemeDTO.setSchemeCode(scheme.getSchemeCode());
		fundSchemeDTO.setSchemeName(scheme.getSchemeName());
		fundSchemeDTO.setSchemeLaunchDate(scheme.getStartDate());
		fundSchemeDTO.setSchemeType(scheme.getSchemeType());
		fundSchemeDTO.setPlan(scheme.getSchemePlan());
		fundSchemeDTO.setMinimumPurchaseAmount(scheme.getMinimumPurchaseAmount());
		return fundSchemeDTO;
	}

	/*@Override
	public String redeemOrder(Integer orderId, Integer userId, String redemptionAmount,String type) {
		String response = "failure";
		String schemeName = null;
		String date = null;
		String amount = null;
		User user = userRepository.getOne(userId);
		Orders orders = orderRepository.getOne(orderId);
		OrderUniqueRef orderUniqueRef = orderUniqueRefRepository.getOne(1);
		String uniqueRefNo = orderUniqueRef.getOrderUniqueRefNo();
		long refNo = Long.valueOf(uniqueRefNo);
		refNo = refNo + 1;
		orderUniqueRef.setOrderUniqueRefNo(refNo + "");
		orderUniqueRefRepository.save(orderUniqueRef);
		String schemeCode = "";
		String bseOrderId = orders.getBseOrderId();
		for (OrderItem orderItem : orders.getOrderItems()) {
			schemeCode = orderItem.getScheme().getSchemeCode();
			schemeName = orderItem.getScheme().getSchemeName();
			amount = redemptionAmount;
			date = orderItem.getField2();
		}
		OrderEntryRequest orderEntryRequest = new OrderEntryRequest();
		GetPasswordRequest getPasswordRequest = new GetPasswordRequest();
		List<SystemProperties> systemPropertiesList = systemPropertiesRepository.findAll();
		for (SystemProperties systemProperties : systemPropertiesList) {
			if (systemProperties.getId().getPropertyKey().equals("UserId")) {
				getPasswordRequest.setUserId(systemProperties.getId().getPropertyValue());
			} else if (systemProperties.getId().getPropertyKey().equals("MemberId")) {
				getPasswordRequest.setMemberId(systemProperties.getId().getPropertyValue());
			} else if (systemProperties.getId().getPropertyKey().equals("Password")) {
				getPasswordRequest.setPassword(systemProperties.getId().getPropertyValue());
			}
		}
		OtpGenerator otpGenerator = new OtpGenerator(5, true);
		String passKey = otpGenerator.generateOTP();
		getPasswordRequest.setPassKey(passKey);
		String passwordResponse = orderMfService.getPassword(getPasswordRequest);
		String[] res = passwordResponse.split("\\|");
		if (res[0].equals("100")) {
			orderEntryRequest.setTransCode("NEW");
			orderEntryRequest.setUniqueRefNo(uniqueRefNo);
			//orderEntryRequest.setOrderId(bseOrderId);
			orderEntryRequest.setOrderId("");
			orderEntryRequest.setUserID(getPasswordRequest.getUserId());
			orderEntryRequest.setMemberId(getPasswordRequest.getMemberId());
			orderEntryRequest.setClientCode(user.getOnboardingStatus().getClientCode());
			orderEntryRequest.setSchemeCd(schemeCode);
			orderEntryRequest.setBuySell("R");
			orderEntryRequest.setBuySellType("FRESH");
			orderEntryRequest.setDpTxn("P");
			orderEntryRequest.setQty("");
			if(type.equals("PR")){
				orderEntryRequest.setOrderVal(redemptionAmount);
				orderEntryRequest.setAllRedeem("N");	
			}else if(type.equals("FR")){
				orderEntryRequest.setOrderVal("");
				orderEntryRequest.setAllRedeem("Y");
			}
			OrderStatusReportUserdata orderStatusReportUserdata = orderStatusReportUserdataRepository.getOrderStatusByOrderId(bseOrderId);
			orderEntryRequest.setFolioNo(orderStatusReportUserdata.getFolioNo());
			orderEntryRequest.setRemarks("");
			orderEntryRequest.setKycStatus("Y");
			orderEntryRequest.setRefNo("");
			orderEntryRequest.setSubBrCode("");
			orderEntryRequest.setEuin("");
			orderEntryRequest.setEuinVal("N");
			orderEntryRequest.setMinRedeem("Y");
			orderEntryRequest.setDpc("N");
			orderEntryRequest.setIpAdd("");
			orderEntryRequest.setPassword(res[1]);
			orderEntryRequest.setPassKey(getPasswordRequest.getPassKey());
			orderEntryRequest.setParma1("");
			orderEntryRequest.setParma2("");
			orderEntryRequest.setParma3("");
			OrderEntryResponse orderEntryResponse = orderMfService.getOrderEntryForRedeem(orderEntryRequest);
			if (orderEntryResponse.getOrderId() != null && !orderEntryResponse.getBseRemarks().contains("FAILED")) {
				response = orderEntryResponse.getBseRemarks();
				if(orders.getType().equals("LUMPSUM")){
					orders.setField3(orderEntryResponse.getBseRemarks());
					if(type.equals("PR")){
						orders.setStatus(GoForWealthPRSConstants.PARTIAL_ORDER_REDEEM_STATUS);
						for (OrderItem orderItem : orders.getOrderItems()) {
							orderItem.setStatus(GoForWealthPRSConstants.PARTIAL_ORDER_REDEEM_STATUS);
						}
					}else if(type.equals("FR")){
						orders.setStatus(GoForWealthPRSConstants.ORDER_REDEEM_STATUS);
						for (OrderItem orderItem : orders.getOrderItems()) {
							orderItem.setStatus(GoForWealthPRSConstants.ORDER_REDEEM_STATUS);
						}
					}
				}else if(orders.getType().equals("SIP")){
					List<Orders> orderList = orderRepository.findByField2(orders.getField2());
					if(!orderList.isEmpty()){
						for (Orders orders2 : orderList) {
							if(orders2.getStatus().equals("C") || orders2.getStatus().equals("AP")|| orders2.getStatus().equals("AD")){
								orders2.setField3(orderEntryResponse.getBseRemarks());
								orders2.setLastupdate(new Date());
								if(type.equals("PR")){
									orders2.setStatus(GoForWealthPRSConstants.PARTIAL_ORDER_REDEEM_STATUS);
									for (OrderItem orderItem : orders2.getOrderItems()) {
										orderItem.setStatus(GoForWealthPRSConstants.PARTIAL_ORDER_REDEEM_STATUS);
									}
								}else if(type.equals("FR")){
									orders2.setStatus(GoForWealthPRSConstants.ORDER_REDEEM_STATUS);
									for (OrderItem orderItem : orders2.getOrderItems()) {
										orderItem.setStatus(GoForWealthPRSConstants.ORDER_REDEEM_STATUS);
									}
								}
								orderRepository.save(orders2);
							}
						}
					}
				}
				RedumptionManagement redumptionManagement = new RedumptionManagement();
				redumptionManagement.setRedeemOrderId(orderEntryResponse.getOrderId());
				redumptionManagement.setBseOrderId(bseOrderId);
				redumptionManagement.setFolioNumber(orderStatusReportUserdata.getFolioNo());
				redumptionManagement.setRedumptionAmount(new BigDecimal(redemptionAmount));
				SimpleDateFormat sdf = new SimpleDateFormat("dd MMM yyyy");
				String currentDate = sdf.format(new Date());
				redumptionManagement.setRedumptionDate(currentDate);
				redumptionManagement.setSchemeName(schemeName);
				redumptionManagement.setStatus("P");
				if(orders.getType().equals("SIP")){
					redumptionManagement.setSipRegnId(orderStatusReportUserdata.getSipRegnNo());
				}else if(orders.getType().equals("LUMPSUM")){
					redumptionManagement.setSipRegnId(0L);
				}
				redumptionManagement.setUserId(userId);
				redumptionManagementepository.save(redumptionManagement);
				String emailSubject = "";
				String emailContent = "";
				DecimalFormat df = new DecimalFormat("##");
				String amount1 = df.format((Math.round(Double.valueOf(amount) * 100.0) / 100.0));
				try {
					String userName = user.getFirstName() == null ? user.getUsername(): user.getFirstName() + " " + user.getLastName();
					if (orders.getType().equals("SIP")) {
						emailSubject = messageSource.getMessage("sip.redeem.mailSubject", null, Locale.ENGLISH);
						emailContent = messageSource.getMessage("sip.redeem.mailBody",new String[] { userName, orderEntryResponse.getBseRemarks() , schemeName, amount1, date }, Locale.ENGLISH);
					}
					if (orders.getType().equals("LUMPSUM")) {
						emailSubject = messageSource.getMessage("lumpsum.redeem.mailSubject", null, Locale.ENGLISH);
						emailContent = messageSource.getMessage("lumpsum.redeem.mailBody",new String[] { userName, orderEntryResponse.getBseRemarks() , schemeName, amount1, date }, Locale.ENGLISH);
					}
					mailUtility.baselineExample(user.getEmail(), emailSubject, emailContent);
				} catch (NoSuchMessageException | IOException e) {
					e.printStackTrace();
				}
			} else {
				response = orderEntryResponse.getBseRemarks();
				orders.setField3(response);
				String emailSubject = "";
				String emailContent = "";
				DecimalFormat df = new DecimalFormat("##");
				String amount1 = df.format((Math.round(Double.valueOf(amount) * 100.0) / 100.0));
				try {
					String userName = user.getFirstName() == null ? user.getUsername(): user.getFirstName() + " " + user.getLastName();
					if (orders.getType().equals("SIP")) {
						emailSubject = messageSource.getMessage("sip.redeem.fail.mailSubject", null, Locale.ENGLISH) + " "+ response;
						emailContent = messageSource.getMessage("sip.redeem.fail.mailBody",new String[] { userName, orderEntryResponse.getBseRemarks() , schemeName, amount1, date }, Locale.ENGLISH);
					}
					if (orders.getType().equals("LUMPSUM")) {
						emailSubject = messageSource.getMessage("lumpsum.redeem.fail.mailSubject", null, Locale.ENGLISH)+ " " + response;
						emailContent = messageSource.getMessage("lumpsum.redeem.fail.mailBody",new String[] { userName, orderEntryResponse.getBseRemarks() , schemeName, amount1, date }, Locale.ENGLISH);
					}
					mailUtility.baselineExample(user.getEmail(), emailSubject, emailContent);
				} catch (NoSuchMessageException | IOException e) {
					e.printStackTrace();
				}
			}
			orders.setLastupdate(new Date());
			orderRepository.save(orders);
		}
		return response;
	}
*/
	@Override
	public String switchScheme(Integer orderId, String schemeCode, Integer userId) {
		String schemeName = "";
		String newSchemeName = "";
		String sipId = "";
		String newSipId = "";
		SwitchOrderEntryResponse res = null;
		String response = "failure";
		User user = userRepository.getOne(userId);
		Orders orders = orderRepository.getOne(orderId);
		OrderUniqueRef orderUniqueRef = orderUniqueRefRepository.getOne(1);
		String uniqueRefNo = orderUniqueRef.getOrderUniqueRefNo();
		long refNo = Long.valueOf(uniqueRefNo);
		refNo = refNo + 1;
		orderUniqueRef.setOrderUniqueRefNo(refNo + "");
		orderUniqueRefRepository.save(orderUniqueRef);
		String fromSchemeCode = "";
		String bseOrderId = orders.getBseOrderId();
		sipId = orders.getBseOrderId();
		for (OrderItem orderItem : orders.getOrderItems()) {
			fromSchemeCode = orderItem.getScheme().getSchemeCode();
			schemeName = orderItem.getScheme().getSchemeName();
		}
		SwitchOrderEntryRequest switchOrderEntryRequest = new SwitchOrderEntryRequest();
		GetPasswordRequest getPasswordRequest = new GetPasswordRequest();
		List<SystemProperties> systemPropertiesList = systemPropertiesRepository.findAll();
		for (SystemProperties systemProperties : systemPropertiesList) {
			if (systemProperties.getId().getPropertyKey().equals("UserId")) {
				getPasswordRequest.setUserId(systemProperties.getId().getPropertyValue());
			} else if (systemProperties.getId().getPropertyKey().equals("MemberId")) {
				getPasswordRequest.setMemberId(systemProperties.getId().getPropertyValue());
			} else if (systemProperties.getId().getPropertyKey().equals("Password")) {
				getPasswordRequest.setPassword(systemProperties.getId().getPropertyValue());
			}
		}
		OtpGenerator otpGenerator = new OtpGenerator(5, true);
		String passKey = otpGenerator.generateOTP();
		getPasswordRequest.setPassKey(passKey);
		String passwordRes = orderMfService.getPassword(getPasswordRequest);
		if (passwordRes.split("\\|")[0].equals("100")) {
			String password = passwordRes.split("\\|")[1];
			switchOrderEntryRequest.setTransCode("NEW");
			switchOrderEntryRequest.setTransNo(uniqueRefNo);
			switchOrderEntryRequest.setOrderId(orders.getBseOrderId());// order id will be changed after switching
			switchOrderEntryRequest.setUserID(getPasswordRequest.getUserId());
			switchOrderEntryRequest.setMemberId(getPasswordRequest.getMemberId());
			switchOrderEntryRequest.setClientCode(user.getOnboardingStatus().getClientCode());
			switchOrderEntryRequest.setFromSchemeCd(fromSchemeCode);
			switchOrderEntryRequest.setToSchemeCd(schemeCode);
			switchOrderEntryRequest.setBuySell("SO");// Switchout/switchin SO/SI
			switchOrderEntryRequest.setBuySellType("FRESH");
			switchOrderEntryRequest.setDpTxn("P");
			switchOrderEntryRequest.setSwitchAmount("");
			switchOrderEntryRequest.setSwitchUnits("");
			switchOrderEntryRequest.setAllUnitsFlag("Y");
			switchOrderEntryRequest.setFolioNo("12345685");
			switchOrderEntryRequest.setRemarks("");
			switchOrderEntryRequest.setKycStatus("Y");
			switchOrderEntryRequest.setSubBrCode("");
			switchOrderEntryRequest.setEuin("");
			switchOrderEntryRequest.setEuinVal("N");
			switchOrderEntryRequest.setMinRedeem("Y");
			switchOrderEntryRequest.setIpAdd("");
			switchOrderEntryRequest.setPassword(password);
			switchOrderEntryRequest.setPassKey(getPasswordRequest.getPassKey());
			switchOrderEntryRequest.setParma1("");
			switchOrderEntryRequest.setParma2("");
			switchOrderEntryRequest.setParma3("");
			res = orderMfService.getSwitchOrderEntry(switchOrderEntryRequest);
			if (!res.getSuccessFlag().contains("FAILED")) {
				response = res.getSuccessFlag();
				// orders.setField3(res.getSuccessFlag());
				orders.setStatus(GoForWealthPRSConstants.ORDER_SWITCH_STATUS);
				orders.setTimeplaced(new Date());
				orders.setLastupdate(new Date());
				Set<OrderItem> orderItems = new HashSet<>();
				OrderItem orderItem = orderItemRepository.getOrderItemByOrderId(orderId);
				orderItem.setOrders(orders);
				orderItem.setStatus(GoForWealthPRSConstants.ORDER_SWITCH_STATUS);
				orderItem.setLastupdate(new Date());
				orderItems.add(orderItem);
				orders.setOrderItems(orderItems);
			} else {
				response = res.getSuccessFlag().replace("FAILED", "");
				orders.setField3(res.getSuccessFlag());
			}
			orderRepository.save(orders);
			//****************************************
			if (!res.getSuccessFlag().contains("FAILED")) {
				User user1 = userRepository.getOne(userId);
				Scheme scheme1 = schemeRepository.findBySchemeCode(schemeCode);
				newSchemeName = scheme1.getSchemeName();
				newSipId = res.getOrderId();
				Orders orders1 = new Orders();
				orders1.setUser(user1);
				orders1.setLastupdate(new Date());
				orders1.setStartdate(new Date());
				orders1.setExpiredate(new Date());
				orders1.setTimeplaced(new Date());
				orders1.setBseOrderId(res.getOrderId());
				orders1.setField3(res.getSuccessFlag());
				orders1.setField1(orders.getOrdersId());
				orders1.setType(orders.getType());
				orders1.setTotaladjustment(orders.getTotaladjustment());
				orders1.setTotalproduct(orders.getTotalproduct());
				orders1.setTotaltax(orders.getTotaltax());
				orders1.setStatus(GoForWealthPRSConstants.ORDER_COMPLETE_STATUS);
				Set<OrderItem> orderItems1 = new HashSet<>();
				OrderItem orderItem2 = orderItemRepository.getOrderItemByOrderId(orderId);
				OrderItem orderItem1 = new OrderItem();
				orderItem1.setOrders(orders1);
				orderItem1.setLastcreate(new Date());
				orderItem1.setLastupdate(new Date());
				orderItem1.setTimereleased(new Date());
				orderItem1.setTimeshiped(new Date());
				orderItem1.setProductprice(orderItem2.getProductprice());
				orderItem1.setQuantity(orderItem2.getQuantity());
				orderItem1.setScheme(scheme1);
				orderItem1.setStatus(GoForWealthPRSConstants.ORDER_COMPLETE_STATUS);
				orderItem1.setTaxamount(orderItem2.getTaxamount());
				orderItem1.setTotaladjustment(orderItem2.getTotaladjustment());
				orderItem1.setDescription(orderItem2.getDescription());
				orderItems1.add(orderItem1);
				orders1.setOrderItems(orderItems1);
				orderRepository.save(orders1);
			}
			//******************************************
		} else {
			response = passwordRes.split("\\|")[1];
		}
		String successMsg = res.getSuccessFlag();
		/*** Mail Send Code Start After Switch Successfull ***/
		String emailSubject = "";
		String emailContent = "";
		// MimeMessage mail = mailSender.createMimeMessage();
		try {
			String customerUrnNumber = "BSE000000092286";
			// mail.setFrom(new
			// InternetAddress(emailVerificationConfiguration.mailSenderAddress,
			// messageSource.getMessage("verifyemail.mailSenderPersonalName",
			// null, Locale.ENGLISH)));
			// mail.setRecipient(Message.RecipientType.TO, new
			// InternetAddress(user.getEmail()));
			// String userName = user.getFirstName() == null ? user.getUsername() : user.getFirstName();
			String userName = user.getFirstName() == null ? user.getUsername(): user.getFirstName() + " " + user.getLastName();
			if (orders.getType().equals("SIP")) {
				emailSubject = messageSource.getMessage("sip.switch.mailSubject", null, Locale.ENGLISH);
				emailContent = messageSource.getMessage("sip.switch.mailBody", new String[] { userName, schemeName,sipId, newSchemeName, newSipId, customerUrnNumber, successMsg }, Locale.ENGLISH);
				// mail.setSubject(messageSource.getMessage("sip.switch.mailSubject",
				// null, Locale.ENGLISH));
				// mail.setContent(messageSource.getMessage("sip.switch.mailBody",
				// new String[] { userName,
				// schemeName,sipId,newSchemeName,newSipId,customerUrnNumber
				// ,successMsg}, Locale.ENGLISH), "text/html");
			}
			if (orders.getType().equals("LUMPSUM")) {
				emailSubject = messageSource.getMessage("lumpsum.switch.mailSubject", null, Locale.ENGLISH);
				emailContent = messageSource.getMessage("lumpsum.switch.mailBody", new String[] { userName, schemeName,sipId, newSchemeName, newSipId, customerUrnNumber, successMsg }, Locale.ENGLISH);
				// mail.setSubject(messageSource.getMessage("lumpsum.switch.mailSubject",
				// null, Locale.ENGLISH));
				// mail.setContent(messageSource.getMessage("lumpsum.switch.mailBody",
				// new String[] { userName,
				// schemeName,sipId,newSchemeName,newSipId,customerUrnNumber,successMsg
				// }, Locale.ENGLISH), "text/html");
			}
			// mailSender.send(mail);
			mailUtility.baselineExample(user.getEmail(), emailSubject, emailContent);
		} catch (NoSuchMessageException | IOException e) {
			e.printStackTrace();
		}
		/*** Mail Send Code End After Switch Successfull ***/
		return response;
	}

	@Override
	public boolean updateSchemeWithKeyword() {
		boolean flag = false;
		List<Scheme> schemeList = schemeRepository.findAll();
		for (Scheme scheme : schemeList) {
			String schemeName = scheme.getSchemeName();
			String schemeNameTemp = schemeName.replace(' ', '-');
			if (scheme.getSchemeCode().contains("-L1-I")) {
				schemeNameTemp += "-L1-I";
			}
			if (scheme.getSchemeCode().contains("-L1")) {
				schemeNameTemp += "-L1";
			}
			if(scheme.getSchemeCode().contains("-L0")){
				schemeNameTemp += "-L0";
			}
			if(scheme.getSchemeCode().endsWith("-I")){
				schemeNameTemp += "-I";
			}
			scheme.setKeyword(schemeNameTemp);
			schemeNameTemp = "";
		}
		List<Scheme> schemeRefList = schemeRepository.saveAll(schemeList);
		if (!schemeRefList.isEmpty())
			flag = true;
		else
			flag = false;
		return flag;
	}

	@Override
	public boolean updateSchemeWithDisplay() {
		boolean flag = false;
		List<Scheme> schemeList = schemeRepository.findAll();
		for (Scheme scheme : schemeList) {
			if (scheme.getSchemeCode().contains("-L1-I") 
					|| scheme.getSchemeCode().contains("-L1") 
					|| scheme.getSchemeCode().contains("-L0") 
					||scheme.getSchemeCode().endsWith("-I")) {
				scheme.setDisplay("1");
			}else{
				scheme.setDisplay("0");
			}	
		}
		List<Scheme> schemeRefList = schemeRepository.saveAll(schemeList);
		if (!schemeRefList.isEmpty())
			flag = true;
		else
			flag = false;
		return flag;
	}

	@Override
	public boolean updateSchemeWithPriority() {
		boolean flag = false;
		List<Scheme> schemeList = schemeRepository.findAll();
		for (Scheme scheme : schemeList) {
			scheme.setPriority("3");
		}
		List<Scheme> schemeRefList = schemeRepository.saveAll(schemeList);
		if (!schemeRefList.isEmpty())
			flag = true;
		else
			flag = false;
		return flag;
	}

	@Override
	public boolean deleteDirectScheme() {
		boolean flag = false;
		List<Scheme> schemeList = schemeRepository.findAll();
		for (Scheme scheme : schemeList) {
			String schemePlan = scheme.getSchemePlan();
			if (schemePlan.equals("DIRECT")) {
				schemeRepository.delete(scheme);
			}
			flag = true;
		}
		return flag;
	}

	@Override
	public NavCalculateResponse calculateSipNavData(Double amount, String schemeCode, int year, String calculateType) {
		//Scheme scheme = schemeRepository.findBySchemeCode(schemeCode);
		//int schemeAmfiCode = scheme.getAmfiCode();
		//String amfiCode = String.valueOf(schemeAmfiCode);
		/*
		 * NavCalculateResponse navCalculateResponse=null; NavCalculateResponse
		 * navCalculateResponse1=null; NavCalculateResponse
		 * navCalculateResponse3=null; CurrentWorthCalculatorUtil
		 * currentWorthCalculatorUtil= new
		 * CurrentWorthCalculatorUtil(navService);
		 * if(calculateType.equals("SIP")){ //calling sip navCalculateResponse =
		 * currentWorthCalculatorUtil.calculateSipData(amount, year, amfiCode);
		 * navCalculateResponse1 =
		 * currentWorthCalculatorUtil.calculateSipData(amount, 1, amfiCode);
		 * navCalculateResponse3 =
		 * currentWorthCalculatorUtil.calculateSipData(amount, 3, amfiCode); }
		 * if(calculateType.equals("LUMPSUM")){ //calling lumpsum
		 * navCalculateResponse =
		 * currentWorthCalculatorUtil.calculateLumpsumData(amount, year,
		 * amfiCode); navCalculateResponse1 =
		 * currentWorthCalculatorUtil.calculateLumpsumData(amount, 1, amfiCode);
		 * navCalculateResponse3 =
		 * currentWorthCalculatorUtil.calculateLumpsumData(amount, 3, amfiCode);
		 * }
		 * 
		 * 
		 * navCalculateResponse.setOneYearinterest(navCalculateResponse1.
		 * getInterest());
		 * navCalculateResponse.setThreeYearinterest(navCalculateResponse3.
		 * getInterest()); SimpleDateFormat sdf = new SimpleDateFormat(
		 * "dd MMM yyyy"); navCalculateResponse.setCurrentDate(sdf.format(new
		 * Date()));
		 */
		NavCalculateResponse navCalculateResponse = new NavCalculateResponse();
		navCalculateResponse.setOneYearinterest(9.8d);
		navCalculateResponse.setThreeYearinterest(7.5d);
		navCalculateResponse.setFiveYearinterest(7.5d);
		SimpleDateFormat sdf = new SimpleDateFormat("dd MMM yyyy");
		navCalculateResponse.setCurrentDate(sdf.format(new Date()));
		return navCalculateResponse;
	}

	@Override
	public String getPaymentStatus(int orderId) {
		List<SystemProperties> systemPropertiesList = systemPropertiesRepository.findAll();
		Orders order = orderRepository.findByOrdersId(orderId);
		User user = userRepository.getUserByUserId(order.getUser().getUserId());
		com.moptra.go4wealth.prs.mfuploadapi.model.GetPasswordRequest getPasswordRequest = new com.moptra.go4wealth.prs.mfuploadapi.model.GetPasswordRequest();
		for (SystemProperties systemProperties : systemPropertiesList) {
			if (systemProperties.getId().getPropertyKey().equals("UserId")) {
				getPasswordRequest.setUserId(systemProperties.getId().getPropertyValue());
			} else if (systemProperties.getId().getPropertyKey().equals("MemberId")) {
				getPasswordRequest.setMemberId(systemProperties.getId().getPropertyValue());
			} else if (systemProperties.getId().getPropertyKey().equals("Password")) {
				getPasswordRequest.setPassword(systemProperties.getId().getPropertyValue());
			}
		}
		OtpGenerator otpGenerator = new OtpGenerator(5, true);
		String passKey = otpGenerator.generateOTP();
		getPasswordRequest.setPasskey(passKey);
		PaymentStatusRequest paymentStatusRequest = new PaymentStatusRequest();
		paymentStatusRequest.setClientCode(user.getOnboardingStatus().getClientCode());
		paymentStatusRequest.setFlag("11");
		paymentStatusRequest.setOrderNo(order.getBseOrderId());
		paymentStatusRequest.setPassword("");
		paymentStatusRequest.setSagment("BSEMF");
		paymentStatusRequest.setUserId(getPasswordRequest.getUserId());
		paymentStatusRequest.setGetPasswordRequest(getPasswordRequest);
		try {
			logger.info("paymentStatusRequest:" + GoForWealthAdminUtil.getJsonFromObject(paymentStatusRequest, null));
		} catch (JsonProcessingException e) {
			e.printStackTrace();
		}
		String response = mandateService.getPaymentStatus(paymentStatusRequest);
		try {
			logger.info("paymentStatusRequest:" + GoForWealthAdminUtil.getJsonFromObject(response, null));
		} catch (JsonProcessingException e) {
			e.printStackTrace();
		}
		if (response.contains("AWAITING FOR FUNDS CONFIRMATION") || response.contains("APPROVED")) {
			List<Ppcpayinst> ppcpayinst = ppcpayinstRepository.findByOrderId(orderId);
			for (Ppcpayinst ppinst : ppcpayinst) {
				ppinst.setAccountNo(user.getBankDetails().getAccountNo());
				ppinst.setState("PA");
				ppinst.setApprovedamount(new BigDecimal(ppinst.getApprovingamount().doubleValue()));
				ppinst.setCreditedamount(new BigDecimal(ppinst.getCreditingamount().doubleValue()));
				ppinst.setCurrency("INR");
				ppinst.setMarkfordelete(0);
				Ppcpaytran ppcpaytran = ppinst.getPpcpaytran();
				ppcpaytran.setUserId(order.getUser().getUserId());
				ppcpaytran.setPpcpayinst(ppinst);
				ppcpaytran.setProcessedamount(new BigDecimal(ppinst.getCreditingamount().doubleValue()));
				// ppcpaytran.setReasoncode(response);
				ppcpaytran.setResponsecode(response);
				// ppcpaytran.setReferencenumber("");
				ppcpaytran.setState(1);
				ppcpaytran.setTimecreated(new Date());
				ppcpaytran.setTimeupdated(new Date());
				// ppcpaytran.setTrackingid(trackingid);
				// ppcpaytran.setTransactionId(ppcpayinst.getTransactionId());
				// ppcpaytran.setTransactiontype(transactiontype);
				ppcpaytranRepository.save(ppcpaytran);
				// ppcpayinstRepository.save(ppinst);
				order.setStatus(GoForWealthPRSConstants.ORDER_PAYMENT_AWITING);
				order.setLastupdate(new Date());
				for (OrderItem orderItem : order.getOrderItems()) {
					orderItem.setStatus(GoForWealthPRSConstants.ORDER_PAYMENT_AWITING);
				}
				orderRepository.save(order);
				// sending mail to user
				String emailSubject = "";
				String emailContent = "";
				try {
					DecimalFormat df = new DecimalFormat("##");
					String amount = df.format((Math.round(Double.valueOf(ppinst.getCreditingamount().toString()) * 100.0) / 100.0));
					// String userName = user.getFirstName() == null ? user.getUsername() : user.getFirstName();
					String userName = user.getFirstName() == null ? user.getUsername(): user.getFirstName() + " " + user.getLastName();
					OrderItem orderItem = orderItemRepository.getOrderItemByOrderId(orderId);
					String schemeName = orderItem.getScheme().getSchemeName();
					SimpleDateFormat sdf = new SimpleDateFormat("dd MMM yyyy");
					String paymentDate = sdf.format(new Date());
					if (order.getType().equals("SIP")) {
						emailSubject = messageSource.getMessage("sip.payment.start.mailSubject", null, Locale.ENGLISH);
						emailContent = messageSource.getMessage("sip.payment.start.mailBody",new String[] { userName, schemeName, amount, paymentDate }, Locale.ENGLISH);
					}
					if (order.getType().equals("LUMPSUM")) {
						emailSubject = messageSource.getMessage("lumpsum.payment.start.mailSubject", null,Locale.ENGLISH);
						emailContent = messageSource.getMessage("lumpsum.payment.start.mailBody",new String[] { userName, schemeName, amount, paymentDate }, Locale.ENGLISH);
					}
					mailUtility.baselineExample(user.getEmail(), emailSubject, emailContent);
				} catch (NoSuchMessageException | IOException e) {
					return response;
				}
				break;
			}
		}
		if (response.contains("AWAITING FOR RESPONSE FROM BILLDESK")) {
			List<Ppcpayinst> ppcpayinst = ppcpayinstRepository.findByOrderId(orderId);
			for (Ppcpayinst ppinst : ppcpayinst) {
				ppinst.setAccountNo(user.getBankDetails().getAccountNo());
				ppinst.setState("F");
				ppinst.setApprovedamount(new BigDecimal(ppinst.getApprovingamount().doubleValue()));
				ppinst.setCreditedamount(new BigDecimal(ppinst.getCreditingamount().doubleValue()));
				ppinst.setCurrency("INR");
				ppinst.setMarkfordelete(1);
				Ppcpaytran ppcpaytran = ppinst.getPpcpaytran();
				ppcpaytran.setPpcpayinst(ppinst);
				ppcpaytran.setProcessedamount(new BigDecimal(ppinst.getCreditingamount().doubleValue()));
				// ppcpaytran.setReasoncode(response);
				ppcpaytran.setResponsecode(response);
				// ppcpaytran.setReferencenumber("");
				ppcpaytran.setState(0);
				ppcpaytran.setTimecreated(new Date());
				ppcpaytran.setTimeupdated(new Date());
				// ppcpaytran.setTrackingid(trackingid);
				// ppcpaytran.setTransactionId(ppcpayinst.getTransactionId());
				// ppcpaytran.setTransactiontype(transactiontype);
				ppcpaytranRepository.save(ppcpaytran);
				// ppcpayinstRepository.save(ppinst);
				String emailSubject = "";
				String emailContent = "";
				try {
					DecimalFormat df = new DecimalFormat("##");
					String amount = df.format((Math.round(Double.valueOf(ppinst.getCreditingamount().toString()) * 100.0) / 100.0));
					// String userName = user.getFirstName() == null ? user.getUsername() : user.getFirstName();
					String userName = user.getFirstName() == null ? user.getUsername(): user.getFirstName() + " " + user.getLastName();
					OrderItem orderItem = orderItemRepository.getOrderItemByOrderId(orderId);
					String schemeName = orderItem.getScheme().getSchemeName();
					SimpleDateFormat sdf = new SimpleDateFormat("dd MMM yyyy");
					String paymentDate = sdf.format(new Date());
					if (order.getType().equals("SIP")) {
						emailSubject = messageSource.getMessage("sip.payment.fail.mailSubject", null, Locale.ENGLISH);
						emailContent = messageSource.getMessage("sip.payment.fail.mailBody",new String[] { userName, schemeName, amount, paymentDate }, Locale.ENGLISH);
					}
					if (order.getType().equals("LUMPSUM")) {
						emailSubject = messageSource.getMessage("lumpsum.payment.fail.mailSubject", null,Locale.ENGLISH);
						emailContent = messageSource.getMessage("lumpsum.payment.fail.mailBody",new String[] { userName, schemeName, amount, paymentDate }, Locale.ENGLISH);
					}
					mailUtility.baselineExample(user.getEmail(), emailSubject, emailContent);
				} catch (NoSuchMessageException | IOException e) {
					return response;
				}
				break;
			}
		}
		return response;
	}
	
	//@Override
	public RedumptionResponse isRedumptionAllowed(String folioNo , String schemeCode) {
		String response = null;
		List<RedumptionManagement> redumptionManagementsList = null;
		BigDecimal amount = new BigDecimal(0);
		BigDecimal currentAmount = new BigDecimal(0);
		RedumptionResponse redumptionResponse = new RedumptionResponse();
		Scheme scheme = schemeRepository.findBySchemeCode(schemeCode);
		redumptionResponse.setSchemeName(scheme != null ? scheme.getSchemeName() : "Not Found");
		DecimalFormat df = new DecimalFormat("##");
		
		redumptionManagementsList = redumptionManagementepository.getRedumptionDataByFolioAndSchemeCodeWithStatusP(folioNo,schemeCode);
		ConsolidatedPortfollio consolidatedPortfollio = consolidatedFollioRepository.getDetailByFollioAndSchemeCode(folioNo, schemeCode);
		if(consolidatedPortfollio != null){
			currentAmount = consolidatedPortfollio.getCurrentAmount();
		}
		
		String minimumAmount = df.format((Math.round(Double.valueOf(scheme.getRedemptionAmountMinimum()) * 100.0) / 100.0));
		redumptionResponse.setMinimumRedumptionAmount(minimumAmount);
		if (!redumptionManagementsList.isEmpty()) {
			for (RedumptionManagement redumptionManagement : redumptionManagementsList) {
				amount = amount.add(redumptionManagement.getRedumptionAmount());
			}
			String totalReduemptionAmount = df.format((Math.round(amount.doubleValue() * 100.0) / 100.0));
			redumptionResponse.setTotalAmount(currentAmount.subtract(new BigDecimal(totalReduemptionAmount)).setScale(2, RoundingMode.HALF_UP));
		} else {
			redumptionResponse.setTotalAmount(currentAmount.setScale(2, RoundingMode.HALF_UP));
		}
		response = scheme.getRedemptionAllowed();
		if (response.equals("Y")) {
			redumptionResponse.setIsRedumptionAllowed("Yes");
		} else {
			redumptionResponse.setIsRedumptionAllowed("No");
		}
		return redumptionResponse;
	}

	@Override
	public List<UnitAllocationResponse> getAwaitingPaymentRecord(Integer userId) {
		List<UnitAllocationResponse> unitAllocationResponseList = new ArrayList<UnitAllocationResponse>();
		List<Ppcpaytran> ppcpaytrans = ppcpaytranRepository.getOrderWithAwiatingStatus(userId);
		for (Ppcpaytran ppcpaytran : ppcpaytrans) {
			UnitAllocationResponse unitAllocationResponse2 = new UnitAllocationResponse();
			SimpleDateFormat dateFormat = new SimpleDateFormat("dd MMM yyyy");
			String awaitingPaymentDate = dateFormat.format(ppcpaytran.getTimecreated());
			unitAllocationResponse2.setAwaitingPayment(awaitingPaymentDate);
			OrderItem orderItem = orderItemRepository.getOrderItemByOrderId(ppcpaytran.getPpcpayinst().getOrders().getOrdersId());
			DecimalFormat df = new DecimalFormat("##");
			String amount = df.format((Math.round(orderItem.getProductprice().doubleValue() * 100.0) / 100.0));
			unitAllocationResponse2.setOrderConfirmed(dateFormat.format(orderItem.getLastupdate()));
			unitAllocationResponse2.setAmount(amount);
			unitAllocationResponse2.setSchemeName(orderItem.getScheme().getSchemeName());
			unitAllocationResponse2.setSchemeAmcCode(orderItem.getScheme().getAmcCode());
			unitAllocationResponse2.setUnitAllocation("Next Three Working Days");
			unitAllocationResponse2.setUnitsInYourAccount("Next Four Working Days");
			unitAllocationResponseList.add(unitAllocationResponse2);
		}
		return unitAllocationResponseList;
	}

	//@Override
	public String getChildOrderId(String clientCode, String date, String password, String memberCode, String regnNo,String systmeticPlanType, SystemPropertiesRepository systemPropertiesRepository2)throws GoForWealthPRSException {
		String ClientCode = clientCode;
		String Date = date;
		String EncryptedPassword = password;
		String MemberCode = memberCode;
		String RegnNo = regnNo;
		String SystematicPlanType = systmeticPlanType;
		String id = orderMfService.getChildOrderDetail(ClientCode, Date, EncryptedPassword, MemberCode, RegnNo,SystematicPlanType, systemPropertiesRepository2);
		return id;
	}

	public void getOnlyStrings() {
		List<IndianIfscCodes> indianIfsc = indianIfscCodesRepository.findAll();
		for (IndianIfscCodes indianIfscCodes : indianIfsc) {
			String branchName = indianIfscCodes.getBranchName();
			Pattern pattern = Pattern.compile("[^a-zA-Z0-9\\s-()_]");
			Matcher matcher = pattern.matcher(branchName);
			String number = matcher.replaceAll("");
			indianIfscCodes.setBranchName(number);
			indianIfscCodesRepository.save(indianIfscCodes);
		}
	}

	@Override
	public String deleteOrder(Integer userId, Integer orderId) {
		Orders orders = orderRepository.getOne(orderId);
		orderRepository.deleteById(orderId);
		if(orders.getGoalId() != null){
			if(orderRepository.findByGoalId(orders.getGoalId()).isEmpty()){
				GoalOrderItems goalOrderItems =  goalOrderItemsRepository.findByGoalOrderItemId(orders.getGoalId());
				goalOrderItems.setField1(0);
				goalOrderItemsRepository.save(goalOrderItems);
			}
		}
		return "success";
	}

	@Override
	public List<UserGoalResponse> getUserGoals(Integer userId) {
		List<UserGoalResponse> userGoalResponseList = new ArrayList<UserGoalResponse>();
		List<GoalOrderItems> goalOrderItemsList = goalOrderItemsRepository.findByUserId(userId);
		if (!goalOrderItemsList.isEmpty()) {
			for(GoalOrderItems goalOrderItems : goalOrderItemsList) {
				UserGoalResponse userGoalResponse = new UserGoalResponse();
				userGoalResponse.setGoalId(goalOrderItems.getGoalOrderItemId());
				userGoalResponse.setGoalName(goalOrderItems.getDescription());
				userGoalResponseList.add(userGoalResponse);
			}
		}
		return userGoalResponseList;
	}

	@Override
	public String updateUserAssignedAndPredefineGoals(Integer oldGoalId, Integer goalId, Integer orderId,Integer userId) {
		Goals goals = goalsRepository.getOne(goalId);
		Orders order = orderRepository.getOne(orderId);
		order.setGoalId(goals.getGoalId());
		order.setGoalName(goals.getGoalName());
		orderRepository.save(order);
		return "success";
	}

	@Override
	public Object getOrderStatus(String clientCode, String Filler1, String Filler2, String Filler3, String fromDate,String memberCode, String OrderNo, String orderStatus, String orderType, String password,String settlementType, String subOrderType, String toDate, String transactionType, String userId) {
		CheckOrderStatusRequest checkOrderStatusRequest = new CheckOrderStatusRequest(clientCode, Filler1, Filler2,Filler3, fromDate, memberCode,OrderNo, orderStatus, orderType, password, settlementType, subOrderType, toDate, transactionType, userId);
		Object orderStatusResponse = orderMfService.getOrderStatus(checkOrderStatusRequest);
		/*
		 * mandateService.getOrderStatus(memberCode,userId ,password ,fromDate,
		 * toDate,clientCode,transactionType, orderType, subOrderType,
		 * orderstatus,settlementType,OrderNo,Filler1,Filler2,Filler3);
		 */
		return orderStatusResponse;
	}

	@Override
	public Object getAllotementStatement(String clientCode, String Filler1, String Filler2, String Filler3,String fromDate, String memberCode, String OrderNo, String orderstatus, String orderType, String password,String settlementType, String subOrderType, String toDate, String userId) {
		OrderAllotmentRequest orderAllotmentRequest = new OrderAllotmentRequest(clientCode, Filler1, Filler2, Filler3,fromDate, memberCode,OrderNo, orderstatus, orderType, password, settlementType, subOrderType, toDate, userId);
		Object allotmentStatementResponse = orderMfService.getAllotementStatement(orderAllotmentRequest);
		return allotmentStatementResponse;
	}

	@Override
	public void updatePaymentStatus(Orders order, User user) {
		logger.info("In updatePaymentStatus");
		List<SystemProperties> systemPropertiesList = systemPropertiesRepository.findAll();
		com.moptra.go4wealth.prs.mfuploadapi.model.GetPasswordRequest getPasswordRequest = new com.moptra.go4wealth.prs.mfuploadapi.model.GetPasswordRequest();
		for (SystemProperties systemProperties : systemPropertiesList) {
			if (systemProperties.getId().getPropertyKey().equals("UserId")) {
				getPasswordRequest.setUserId(systemProperties.getId().getPropertyValue());
			} else if (systemProperties.getId().getPropertyKey().equals("MemberId")) {
				getPasswordRequest.setMemberId(systemProperties.getId().getPropertyValue());
			} else if (systemProperties.getId().getPropertyKey().equals("Password")) {
				getPasswordRequest.setPassword(systemProperties.getId().getPropertyValue());
			}
		}
		OtpGenerator otpGenerator = new OtpGenerator(5, true);
		String passKey = otpGenerator.generateOTP();
		getPasswordRequest.setPasskey(passKey);
		PaymentStatusRequest paymentStatusRequest = new PaymentStatusRequest();
		paymentStatusRequest.setClientCode(user.getOnboardingStatus().getClientCode());
		paymentStatusRequest.setFlag("11");
		paymentStatusRequest.setOrderNo(order.getBseOrderId());
		paymentStatusRequest.setPassword("");
		paymentStatusRequest.setSagment("BSEMF");
		paymentStatusRequest.setUserId(getPasswordRequest.getUserId());
		paymentStatusRequest.setGetPasswordRequest(getPasswordRequest);
		
		try {
			logger.info("paymentStatusRequest:" + GoForWealthAdminUtil.getJsonFromObject(paymentStatusRequest, null));
		} catch (JsonProcessingException e) {
			e.printStackTrace();
		}
		String response = mandateService.getPaymentStatus(paymentStatusRequest);
		try {
			 logger.info("paymentStatusRequest:" + GoForWealthAdminUtil.getJsonFromObject(response, null));
		} catch (IOException e) {
			e.printStackTrace();
		}
		if (response.contains("AWAITING FOR FUNDS CONFIRMATION") || response.contains("APPROVED")) {
			List<Ppcpayinst> ppcpayinst = ppcpayinstRepository.findByOrderId(order.getOrdersId());
			if (!ppcpayinst.isEmpty()) {
				for (Ppcpayinst ppinst : ppcpayinst) {
					ppinst.setAccountNo(user.getBankDetails().getAccountNo());
					ppinst.setState("PA");
					ppinst.setApprovedamount(new BigDecimal(ppinst.getApprovingamount().doubleValue()));
					ppinst.setCreditedamount(new BigDecimal(ppinst.getCreditingamount().doubleValue()));
					ppinst.setCurrency("INR");
					ppinst.setMarkfordelete(0);
					Ppcpaytran ppcpaytran = ppinst.getPpcpaytran();
					ppcpaytran.setUserId(order.getUser().getUserId());
					ppcpaytran.setPpcpayinst(ppinst);
					ppcpaytran.setProcessedamount(new BigDecimal(ppinst.getCreditingamount().doubleValue()));
					// ppcpaytran.setReasoncode(response);
					ppcpaytran.setResponsecode(response);
					// ppcpaytran.setReferencenumber("");
					ppcpaytran.setState(1);
					ppcpaytran.setTimecreated(new Date());
					ppcpaytran.setTimeupdated(new Date());
					// ppcpaytran.setTrackingid(trackingid);
					// ppcpaytran.setTransactionId(ppcpayinst.getTransactionId());
					// ppcpaytran.setTransactiontype(transactiontype);
					ppcpaytranRepository.save(ppcpaytran);
					ppcpayinstRepository.save(ppinst);
					order.setStatus(GoForWealthPRSConstants.ORDER_PAYMENT_AWITING);
					order.setLastupdate(new Date());
					OrderItem orderItem = orderItemRepository.getOrderItemByOrderId(order.getOrdersId());
					orderItem.setStatus(GoForWealthPRSConstants.ORDER_PAYMENT_AWITING);
					if (orderItem != null) {
						orderItemRepository.save(orderItem);
						orderRepository.save(order);
					}
					break;
				}
			}else{
				OrderItem orderItem = orderItemRepository.getOrderItemByOrderId(order.getOrdersId());
				Ppcpayinst ppcpayinst1 = new Ppcpayinst();
				ppcpayinst1.setAmount(orderItem.getProductprice());
				ppcpayinst1.setAccountNo(user.getBankDetails().getAccountNo());
				ppcpayinst1.setOrders(order);
				ppcpayinst1.setState("PA");
				ppcpayinst1.setApprovingamount(orderItem.getProductprice());
				ppcpayinst1.setApprovedamount(orderItem.getProductprice());
				ppcpayinst1.setCreditedamount(orderItem.getProductprice());
				ppcpayinst1.setCreditingamount(orderItem.getProductprice());
				ppcpayinst1.setCurrency("INR");
				ppcpayinst1.setMarkfordelete(0);
				Ppcpaytran ppcpaytran = new Ppcpaytran();
				ppcpaytran.setUserId(order.getUser().getUserId());
				ppcpaytran.setResponsecode(response);
				ppcpaytran.setPpcpayinst(ppcpayinst1);
				ppcpaytran.setProcessedamount(new BigDecimal(0.0));
				ppcpaytran.setState(1);
				ppcpaytran.setTimecreated(new Date());
				ppcpaytran.setTimeupdated(new Date());
				ppcpaytranRepository.save(ppcpaytran);
				order.setStatus(GoForWealthPRSConstants.ORDER_PAYMENT_AWITING);
				order.setLastupdate(new Date());
				orderItem.setStatus(GoForWealthPRSConstants.ORDER_PAYMENT_AWITING);
				if (orderItem != null) {
					orderItemRepository.save(orderItem);
					orderRepository.save(order);
				}
			}
		}
		logger.info("Out updatePaymentStatus");
	}

	@Override
	@Transactional
	public void updateOrderStatus(Integer ordersId, Integer userId, String bseOrderId, String callForInstallment) {
		logger.info("In updateOrderStatus");
		String bseUserId = "";
		String memberCode = "";
		String password = "";
		User user = userRepository.getUserByUserId(userId);
		String clientCode = user.getOnboardingStatus().getClientCode();
		List<SystemProperties> systemPropertiesList = systemPropertiesRepository.findAll();
		for (SystemProperties systemProperties : systemPropertiesList) {
			if (systemProperties.getId().getPropertyKey().equals("UserId")) {
				bseUserId = systemProperties.getId().getPropertyValue();
			} else if (systemProperties.getId().getPropertyKey().equals("MemberId")) {
				memberCode = systemProperties.getId().getPropertyValue();
			} else if (systemProperties.getId().getPropertyKey().equals("Password")) {
				password = systemProperties.getId().getPropertyValue();
			}
		}
		CheckOrderStatusRequest checkOrderStatusRequest = new CheckOrderStatusRequest(clientCode, "", "", "", "",memberCode,bseOrderId, "", "", password, "","ALL", "", "", bseUserId);
		Object orderStatusResponse = orderMfService.getOrderStatus(checkOrderStatusRequest);
		System.out.println(orderStatusResponse);
		try {
			JSONObject jsonObject = new JSONObject(orderStatusResponse.toString());
			 String checkStatus = jsonObject.getString("Status");
			if (checkStatus.equals("101")) {
			} else {
				JSONArray jSONArray = jsonObject.getJSONArray("OrderDetails");
				int size = jSONArray.length();
				for (int i = 0; i < size; i++) {
					JSONObject JSONObject5 = new JSONObject(jSONArray.get(i).toString());
					if (JSONObject5.getString("OrderRemarks").equals("ALLOTMENT DONE")&& JSONObject5.getString("OrderStatus").equals("VALID")) {
						OrderStatusReportUserdata orderStatusReportUserdata = null;
						orderStatusReportUserdata = orderStatusReportUserdataRepository.getOrderStatusByOrderId(JSONObject5.getString("OrderNumber"));
						if(orderStatusReportUserdata== null){
							orderStatusReportUserdata = new OrderStatusReportUserdata();
							orderStatusReportUserdata.setAllotMailStatus(0);
						}
						orderStatusReportUserdata.setAllUnits(JSONObject5.getString("ALLUnits"));
						orderStatusReportUserdata.setAmount(new BigDecimal(JSONObject5.getDouble("Amount")).setScale(4, RoundingMode.HALF_UP));
						orderStatusReportUserdata.setBuySell(JSONObject5.getString("BuySell"));
						orderStatusReportUserdata.setBuySellType(JSONObject5.getString("BuySellType"));
						orderStatusReportUserdata.setClientCode(JSONObject5.getString("ClientCode"));
						orderStatusReportUserdata.setClientName(JSONObject5.getString("ClientName"));
						orderStatusReportUserdata.setDate(JSONObject5.getString("Date"));
						orderStatusReportUserdata.setDpc(JSONObject5.getString("DPC"));
						orderStatusReportUserdata.setDpfolioNo(JSONObject5.getString("DPFolioNo"));
						orderStatusReportUserdata.setDptrans(JSONObject5.getString("DPTrans"));
						orderStatusReportUserdata.setEntryBy(JSONObject5.getString("EntryBy"));
						orderStatusReportUserdata.setEuin(JSONObject5.getString("EUIN"));
						orderStatusReportUserdata.setEuinDecl(JSONObject5.getString("EUINDecl"));
						orderStatusReportUserdata.setFirstOrder(JSONObject5.getString("FirstOrder"));
						orderStatusReportUserdata.setFolioNo(JSONObject5.getString("FolioNo"));
						orderStatusReportUserdata.setInternalRefNo(JSONObject5.getString("InternalRefNo"));
						orderStatusReportUserdata.setIsin(JSONObject5.getString("ISIN"));
						orderStatusReportUserdata.setKycFlag(JSONObject5.getString("KYCFlag"));
						orderStatusReportUserdata.setMemberCode(JSONObject5.getString("MemberCode"));
						orderStatusReportUserdata.setMemberRemarks(JSONObject5.getString("MemberRemarks"));
						orderStatusReportUserdata.setMinRedeemFlag(JSONObject5.getString("MINRedeemFlag"));
						orderStatusReportUserdata.setOrderNumber(JSONObject5.getString("OrderNumber"));
						orderStatusReportUserdata.setOrderRemarks(JSONObject5.getString("OrderRemarks"));
						orderStatusReportUserdata.setOrderStatus(JSONObject5.getString("OrderStatus"));
						orderStatusReportUserdata.setOrderType(JSONObject5.getString("OrderType"));
						orderStatusReportUserdata.setSchemeCode(JSONObject5.getString("SchemeCode"));
						orderStatusReportUserdata.setSchemeName(JSONObject5.getString("SchemeName"));
						orderStatusReportUserdata.setSettNo(JSONObject5.getString("SettNo"));
						orderStatusReportUserdata.setSettType(JSONObject5.getString("SettType"));
						orderStatusReportUserdata.setSipregnDate(JSONObject5.getString("SIPRegnDate"));
						orderStatusReportUserdata.setSipRegnNo(JSONObject5.getLong("SIPRegnNo"));
						orderStatusReportUserdata.setSubBrCode(JSONObject5.getString("SubBrCode"));
						orderStatusReportUserdata.setSubBrokerArnCode(JSONObject5.getString("SubBrokerARNCode"));
						orderStatusReportUserdata.setSubOrderType(JSONObject5.getString("SubOrderType"));
						orderStatusReportUserdata.setTime(JSONObject5.getString("Time"));
						orderStatusReportUserdata.setUnits(new BigDecimal(JSONObject5.getDouble("Units")).setScale(4, RoundingMode.HALF_UP));
						SimpleDateFormat sf = new SimpleDateFormat("dd MM yyyy HH:mm:ss");
						orderStatusReportUserdata.setLastUpdatedDate(sf.format(new Date()));
						updateAllotemtStatusReport(memberCode, password, clientCode, bseOrderId, bseUserId, ordersId,orderStatusReportUserdata,callForInstallment);
					}else if (JSONObject5.getString("OrderStatus").equals("INVALID")){
						List<Ppcpayinst> ppcpayinst = ppcpayinstRepository.findByOrderIdWithStatusPA(ordersId);
						if(callForInstallment.equals("No")){
							Orders order = orderRepository.findByBseOrderId(bseOrderId);
							OrderItem orderItem = orderItemRepository.getOrderItemByOrderId(order.getOrdersId());
							if(!ppcpayinst.isEmpty() && order!= null && orderItem!= null){
								for (Ppcpayinst ppcpayinst2 : ppcpayinst) {
									ppcpayinst2.setState("F");	
									ppcpayinst2.setTimeupdated(new Date());
									Ppcpaytran ppcpaytran = ppcpayinst2.getPpcpaytran();
									ppcpaytran.setState(1);
									order.setStatus("IO");
									order.setField3(JSONObject5.getString("OrderRemarks"));
									order.setLastupdate(new Date());
									orderItem.setStatus("IO");
									ppcpayinstRepository.save(ppcpayinst2);
									ppcpaytranRepository.save(ppcpaytran);
									orderRepository.save(order);
									orderItemRepository.save(orderItem);
									break;
								}
							}
						}
					}
				}
			}
		} catch (Exception e) {
		}
		logger.info("Out updateOrderStatus");
	}

	@Transactional
	private void updateAllotemtStatusReport(String memberCode, String password, String clientCode, String bseOrderId,String bseUserId, int orderId,OrderStatusReportUserdata orderStatusReportUserdata,String callForInstallment) {
		logger.info("In updateAllotemtStatusReport");
		OrderAllotmentRequest orderAllotmentRequest = new OrderAllotmentRequest(clientCode, "", "", "", "", memberCode,bseOrderId, "", "", password, "", "ALL", "", bseUserId);
		Object allotmentStatementResponse = orderMfService.getAllotementStatement(orderAllotmentRequest);
		System.out.println(allotmentStatementResponse);
		try {
			JSONObject jsonObject = new JSONObject(allotmentStatementResponse.toString());
			String checkStatus = jsonObject.getString("Status");
			if (checkStatus.equals("101")) {
			} else {
				JSONArray jSONArray = jsonObject.getJSONArray("AllotmentDetails");
				int size = jSONArray.length();
				for (int i = 0; i < size; i++) {
					JSONObject JSONObject5 = new JSONObject(jSONArray.get(i).toString());
					AllotmentStatusReportUserdata allotmentStatusReportUserdata = null;
					allotmentStatusReportUserdata = allotmentStatusReportUserdataRepository.findByOrderNo(JSONObject5.getInt("OrderNo"));
					if(allotmentStatusReportUserdata== null){
						allotmentStatusReportUserdata = new AllotmentStatusReportUserdata();
						allotmentStatusReportUserdata.setAllotMailStatus(0);
					}
					allotmentStatusReportUserdata.setAllottedAmt(new BigDecimal(JSONObject5.getDouble("AllottedAmt")).setScale(4, RoundingMode.HALF_UP));
					allotmentStatusReportUserdata.setAllottedNav(new BigDecimal(JSONObject5.getDouble("AllottedNav")).setScale(4, RoundingMode.HALF_UP));
					allotmentStatusReportUserdata.setAllottedUnit(new BigDecimal(JSONObject5.getDouble("AllottedUnit")).setScale(4, RoundingMode.HALF_UP));
					allotmentStatusReportUserdata.setAmount(new BigDecimal(JSONObject5.getDouble("Amount")).setScale(4, RoundingMode.HALF_UP));
					/*
					allotmentStatusReportUserdata.setBeneficiaryId(beneficiaryId);
					allotmentStatusReportUserdata.setBranchCode(branchCode);
					*/
					allotmentStatusReportUserdata.setClientCode(JSONObject5.getString("ClientCode"));
					allotmentStatusReportUserdata.setClientName(JSONObject5.getString("ClientName"));
					/*
					allotmentStatusReportUserdata.setDpcFlag(dpcFlag);
					allotmentStatusReportUserdata.setDpTrans(dpTrans);
					allotmentStatusReportUserdata.setEuin(euin);
					allotmentStatusReportUserdata.setEuinDecl(euinDecl);
					*/
					allotmentStatusReportUserdata.setFolioNo(JSONObject5.getString("FolioNo"));
					/*
					allotmentStatusReportUserdata.setInternalRefNo(internalRefNo);
					allotmentStatusReportUserdata.setIsin(isin);
					allotmentStatusReportUserdata.setKycFlag(kycFlag)
					*/
					//allotmentStatusReportUserdata.setMemberCode(memberCode);
					allotmentStatusReportUserdata.setOrderDate(JSONObject5.getString("OrderDate"));
					allotmentStatusReportUserdata.setOrderNo(JSONObject5.getInt("OrderNo"));
					allotmentStatusReportUserdata.setOrderType(JSONObject5.getString("OrderType"));
					allotmentStatusReportUserdata.setQty(JSONObject5.getInt("Qty"));
					allotmentStatusReportUserdata.setRemarks(JSONObject5.getString("Remarks"));
					allotmentStatusReportUserdata.setReportDate(JSONObject5.getString("ReportDate"));
					/*
					allotmentStatusReportUserdata.setRtaSchemeCode(rtaSchemeCode);
					allotmentStatusReportUserdata.setRtaTransNo(rtaTransNo);
					*/
					allotmentStatusReportUserdata.setSchemeCode(JSONObject5.getString("SchemeCode"));
					/*
					allotmentStatusReportUserdata.setSettNo(settNo);
					allotmentStatusReportUserdata.setSettType(settType);
					*/
					allotmentStatusReportUserdata.setSipRegnDate(JSONObject5.getString("SIPRegnDate"));
				    allotmentStatusReportUserdata.setSipRegnNo(JSONObject5.getLong("SIPRegnNo"));
					/*
					allotmentStatusReportUserdata.setStt(stt);
					allotmentStatusReportUserdata.setSubBrCode(subBrCode);
					allotmentStatusReportUserdata.setSubOrderType(subOrderType);
					allotmentStatusReportUserdata.setUserId(userId);
					allotmentStatusReportUserdata.setValidFlag(validFlag);
					*/
					SimpleDateFormat sf = new SimpleDateFormat("dd MM yyyy HH:mm:ss");
					allotmentStatusReportUserdata.setLastUpdatedDate(sf.format(new Date()));
					/*
					orderStatusReportUserdataRepository.save(orderStatusReportUserdata);
					allotmentStatusReportUserdataRepository.save(allotmentStatusReportUserdata);
					*/
					Orders order = orderRepository.findByOrdersId(orderId);
					if (order != null) {
						if(callForInstallment.equals("No")){
							order.setStatus("C");
							orderStatusReportUserdata.setStatus("AD");
							allotmentStatusReportUserdata.setStatus("AD");
						}else{
							order.setStatus("AP");//Allotement Pending
							orderStatusReportUserdata.setStatus("AP");
							allotmentStatusReportUserdata.setStatus("AP");
						}
						if(order.getGoalId() != null && order.getGoalName() != null){
							orderStatusReportUserdata.setGoalId(order.getGoalId());
							orderStatusReportUserdata.setGoalName(order.getGoalName());
							allotmentStatusReportUserdata.setGoalId(order.getGoalId());
							allotmentStatusReportUserdata.setGoalName(order.getGoalName());
						}
						order.setLastupdate(new Date());
						orderRepository.save(order);
					}
					OrderItem orderItem = orderItemRepository.getOrderItemByOrderId(orderId);
					if (orderItem != null) {	
						if(callForInstallment.equals("No")){
							orderItem.setStatus("C");
						}else{
							orderItem.setStatus("AP");//Allotement Pending
						}
						orderItem.setLastupdate(new Date());
						orderItemRepository.save(orderItem);
					}
					List<Ppcpayinst> ppcpayinst = ppcpayinstRepository.findByOrderIdWithStatusPA(orderId);
					List<Ppcpayinst> ppcpayinst1 = ppcpayinstRepository.findByOrderIdWithStatusAP(orderId);
					if(callForInstallment.equals("No")){
						for (Ppcpayinst ppcpayinst2 : ppcpayinst) {
							ppcpayinst2.setState("C");	
							Ppcpaytran ppcpaytran = ppcpayinst2.getPpcpaytran();
							ppcpaytran.setState(1);
							ppcpayinstRepository.save(ppcpayinst2);
							ppcpaytranRepository.save(ppcpaytran);
							break;
						}
					}else if(callForInstallment.equals("Yes")){
						for (Ppcpayinst ppcpayinst2 : ppcpayinst1) {
							ppcpayinst2.setState("AP");//Allotement Pending
							Ppcpaytran ppcpaytran = ppcpayinst2.getPpcpaytran();
							ppcpaytran.setState(1);
							ppcpayinstRepository.save(ppcpayinst2);
							ppcpaytranRepository.save(ppcpaytran);
							break;
						}
					}
					orderStatusReportUserdataRepository.save(orderStatusReportUserdata);
					allotmentStatusReportUserdataRepository.save(allotmentStatusReportUserdata);
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		updateConsolidatedFollio();
		logger.info("Out updateAllotemtStatusReport");
	}

	public PrsDTO getMandateStatus(Integer userId) {
		PrsDTO prsDto = new PrsDTO();
		String mandateStatus = "";
		String remarks = "";
		User user = userRepository.getOne(userId);
		String bseUserId = "";
		String memberCode = "";
		String password = "";
		String clientCode = user.getOnboardingStatus().getClientCode();
		String mandateId = user.getOnboardingStatus().getMandateNumber();
		Date dateObj = user.getOnboardingStatus().getLastUpdated();
		List<SystemProperties> systemPropertiesList = systemPropertiesRepository.findAll();
		for (SystemProperties systemProperties : systemPropertiesList) {
			if (systemProperties.getId().getPropertyKey().equals("UserId")) {
				bseUserId = systemProperties.getId().getPropertyValue();
			} else if (systemProperties.getId().getPropertyKey().equals("MemberId")) {
				memberCode = systemProperties.getId().getPropertyValue();
			} else if (systemProperties.getId().getPropertyKey().equals("Password")) {
				password = systemProperties.getId().getPropertyValue();
			}
		}
		OtpGenerator otpGenerator = new OtpGenerator(5, true);
		String passKey = otpGenerator.generateOTP();
		Date todayDate = new Date();
		SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
		MandateDetailsRequestParam mandateDetailsRequestParam = new MandateDetailsRequestParam();
		mandateDetailsRequestParam.setClientCode(clientCode);
		mandateDetailsRequestParam.setEncryptedPassword("");
		mandateDetailsRequestParam.setFromDate(sdf.format(dateObj));
		mandateDetailsRequestParam.setMandateId(mandateId);
		mandateDetailsRequestParam.setMemberCode(memberCode);
		mandateDetailsRequestParam.setMemberId(memberCode);
		mandateDetailsRequestParam.setPassKey(passKey);
		mandateDetailsRequestParam.setPassword(password);
		mandateDetailsRequestParam.setRequestType("MANDATE");
		mandateDetailsRequestParam.setToDate(sdf.format(todayDate));
		mandateDetailsRequestParam.setUserId(bseUserId);
		Object mandateDetailResponse = orderMfService.getMandateDetails(mandateDetailsRequestParam);
		try {
			JSONObject jsonObject = new JSONObject(mandateDetailResponse.toString());
			String checkStatus = jsonObject.getString("Status");
			if (checkStatus.equals("101")) {
			} else {
				JSONArray jSONArray = jsonObject.getJSONArray("MandateDetails");
				int size = jSONArray.length();
				for (int i = 0; i < size; i++) {
					JSONObject JSONObject5 = new JSONObject(jSONArray.get(i).toString());
					mandateStatus = JSONObject5.getString("Status");
					if (!JSONObject5.getString("Remarks").equals("")) {
						remarks = JSONObject5.getString("Remarks");
					}
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		OnboardingStatus onboardingStatusObj = user.getOnboardingStatus();
		if (!mandateStatus.equals("")) {
			onboardingStatusObj.setEnachStatus(mandateStatus);
		} else {
			onboardingStatusObj.setEnachStatus("Pending");
		}
		onboardingStatusObj.setField2(remarks);
		onboardingStatusRepository.save(onboardingStatusObj);
		prsDto.setMandateStatus(mandateStatus);
		prsDto.setRemarks(remarks);
		return prsDto;
	}

	@Override
	public void updateCurrentNavAndCurrentValueForOrder() {
		/*System.out.println("updateCurrentNavAndCurrentValueForOrder In");
		SimpleDateFormat sf = new SimpleDateFormat("dd-MM-yyyy HH:mm:ss");
		List<MaintainCurrentNavCurrentValueForOrders> mcncvForOrderList = maintainCurrentNavCurrentValueForOrdersRepository.findAll();
		if(!mcncvForOrderList.isEmpty()){
			for (MaintainCurrentNavCurrentValueForOrders maintainCurrentNavCurrentValueForOrders : mcncvForOrderList) {
				BigDecimal currentAmount = new BigDecimal(0);
				BigDecimal currentUnit = new BigDecimal(0);
				BigDecimal investedAmount = new BigDecimal(0);
				Scheme scheme = schemeRepository.findBySchemeCode(maintainCurrentNavCurrentValueForOrders.getSchemeCode());
				String currentNav = scheme.getCurrentNav();
				if (currentNav != null) {
					currentNav = new BigDecimal(currentNav).toString();
				} else {
					currentNav = "0.0";
				}
				if(maintainCurrentNavCurrentValueForOrders.getSipRegnNo() != 0){
					List<OrderStatusReportUserdata> orderStatusReportUserdataList = orderStatusReportUserdataRepository.findBySipRegnNo(maintainCurrentNavCurrentValueForOrders.getSipRegnNo());
			        for (OrderStatusReportUserdata orderStatusReportUserdata2 : orderStatusReportUserdataList) {
			        	if(orderStatusReportUserdata2.getStatus().equals("AD") || orderStatusReportUserdata2.getStatus().equals("AP")){
			        		AllotmentStatusReportUserdata alloUserData =allotmentStatusReportUserdataRepository.findByOrderNo(Integer.parseInt(orderStatusReportUserdata2.getOrderNumber()));
							currentUnit = alloUserData.getAllottedUnit().add(currentUnit);
						    investedAmount = investedAmount.add(alloUserData.getAllottedAmt());	
						}
					}
			        currentAmount = currentUnit.multiply(new BigDecimal(currentNav));				
				}else if(maintainCurrentNavCurrentValueForOrders.getSipRegnNo() == 0){
					AllotmentStatusReportUserdata alloUserData =allotmentStatusReportUserdataRepository.findByOrderNo(maintainCurrentNavCurrentValueForOrders.getOrderId());
					currentUnit = alloUserData.getAllottedUnit().add(currentUnit);
				    investedAmount = investedAmount.add(alloUserData.getAllottedAmt());	
				    currentAmount = currentUnit.multiply(new BigDecimal(currentNav));
				}
				maintainCurrentNavCurrentValueForOrders.setCurrentUnit(currentUnit);
				maintainCurrentNavCurrentValueForOrders.setInvestedAmount(investedAmount);
				maintainCurrentNavCurrentValueForOrders.setCurrentAmount(currentAmount);
				maintainCurrentNavCurrentValueForOrders.setLastNavUpdatedDate(sf.format(new Date()));
				maintainCurrentNavCurrentValueForOrdersRepository.save(maintainCurrentNavCurrentValueForOrders);
			}
		}
		System.out.println("updateCurrentNavAndCurrentValueForOrder Out");*/
		updateConsolidatedFollio();
	}
	
	
		public void updateConsolidatedFollio(){
			System.out.println("updateConsolidatedFollio In");
			SimpleDateFormat sf = new SimpleDateFormat("dd-MM-yyyy HH:mm:ss");
			List<ConsolidatedPortfollio> conFollioList = consolidatedFollioRepository.findAll();
			if(!conFollioList.isEmpty()){
				for (ConsolidatedPortfollio conFollio : conFollioList) {
					boolean isFound = true;
					BigDecimal currentUnit = new BigDecimal(0);
					BigDecimal investedAmount = new BigDecimal(0);
					Scheme scheme = schemeRepository.findBySchemeCode(conFollio.getSchemeCode());
					String currentNav = scheme.getCurrentNav();
					if (currentNav != null) {
						currentNav = new BigDecimal(currentNav).toString();
					} else {
						currentNav = "0.0";
					}
					
					List<AllotmentStatusReportUserdata> allotmentStatusReportUserdatas = allotmentStatusReportUserdataRepository.
							getAllotementByFollioNoAndSchemeCode(conFollio.getFolioNo(), conFollio.getSchemeCode());
					if(!allotmentStatusReportUserdatas.isEmpty()){
						isFound = false;
						for (AllotmentStatusReportUserdata allotmentStatusReportUserdata2 : allotmentStatusReportUserdatas) {
							currentUnit = allotmentStatusReportUserdata2.getAllottedUnit().add(currentUnit);
						    investedAmount = investedAmount.add(allotmentStatusReportUserdata2.getAllottedAmt());	
					
						}
						if(conFollio.getTranferInId() != null){
							TransferIn transferIn = transferInRepository.findBytransferInId(conFollio.getTranferInId());
							if(transferIn != null){
								currentUnit = transferIn.getAvailableUnit().add(currentUnit);
								investedAmount = investedAmount.add(transferIn.getAmount());
							}
						}
						
						List<RedumptionManagement> redumptionManagementList  =redumptionManagementepository.getRedumptionDataByFolioAndSchemeCodeWithStatusC(conFollio.getFolioNo(), conFollio.getSchemeCode());
						if(!redumptionManagementList.isEmpty()){
							for (RedumptionManagement redumptionManagement : redumptionManagementList) {
								currentUnit = currentUnit.subtract(redumptionManagement.getRedumptionUnit());
							}
						}
						
						conFollio.setAllotedUnit(currentUnit);
						conFollio.setInvestedAmount(investedAmount);
						conFollio.setCurrentAmount(currentUnit.multiply(new BigDecimal(currentNav)));
						conFollio.setLastNavUpdatedDate(sf.format(new Date()));
						consolidatedFollioRepository.save(conFollio);
						
					}else if(conFollio.getTranferInId() != null && isFound){
							TransferIn transferIn = transferInRepository.findBytransferInId(conFollio.getTranferInId());
							if(transferIn != null){
								currentUnit = transferIn.getAvailableUnit().add(currentUnit);
								investedAmount = investedAmount.add(transferIn.getAmount());
								conFollio.setAllotedUnit(currentUnit);
								conFollio.setCurrentAmount(currentUnit.multiply(new BigDecimal(currentNav)));
								conFollio.setLastNavUpdatedDate(sf.format(new Date()));
								consolidatedFollioRepository.save(conFollio);
						}
					}
				}
			}
			System.out.println("updateConsolidatedFollio Out");
			
		}

	@Override
	public String getEmandateAuthenticationUrl(Integer userId) {
		String mandateAuthenticationUrl = "";
		User user = userRepository.getOne(userId);
		String bseUserId = "";
		String memberCode = "";
		String password = "";
		String clientCode = user.getOnboardingStatus().getClientCode();
		String mandateId = user.getOnboardingStatus().getMandateNumber();
		List<SystemProperties> systemPropertiesList = systemPropertiesRepository.findAll();
		for (SystemProperties systemProperties : systemPropertiesList) {
			if (systemProperties.getId().getPropertyKey().equals("UserId")) {
				bseUserId = systemProperties.getId().getPropertyValue();
			} else if (systemProperties.getId().getPropertyKey().equals("MemberId")) {
				memberCode = systemProperties.getId().getPropertyValue();
			} else if (systemProperties.getId().getPropertyKey().equals("Password")) {
				password = systemProperties.getId().getPropertyValue();
			}
		}
		MandateDetailsRequestParam mandateDetailsRequestParam = new MandateDetailsRequestParam();
		mandateDetailsRequestParam.setClientCode(clientCode);
		mandateDetailsRequestParam.setMandateId(mandateId);
		mandateDetailsRequestParam.setMemberCode(memberCode);
		mandateDetailsRequestParam.setPassword(password);
		mandateDetailsRequestParam.setUserId(bseUserId);
		Object mandateAuthenticationCheckResponse = orderMfService.getMandateAuthenticationUrl(mandateDetailsRequestParam);
		try {
			JSONObject jsonObject = new JSONObject(mandateAuthenticationCheckResponse.toString());
			String checkStatus = jsonObject.getString("Status");
			if (checkStatus.equals("101")) {
			} else {
				mandateAuthenticationUrl = jsonObject.getString("ResponseString");
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return mandateAuthenticationUrl;
	}

	@Override
	public void updateOrderRedemptionStatus() {
		String bseUserId = "";
		String memberCode = "";
		String password = "";
		List<SystemProperties> systemPropertiesList = systemPropertiesRepository.findAll();
		for (SystemProperties systemProperties : systemPropertiesList) {
			if (systemProperties.getId().getPropertyKey().equals("UserId")) {
				bseUserId = systemProperties.getId().getPropertyValue();
			} else if (systemProperties.getId().getPropertyKey().equals("MemberId")) {
				memberCode = systemProperties.getId().getPropertyValue();
			} else if (systemProperties.getId().getPropertyKey().equals("Password")) {
				password = systemProperties.getId().getPropertyValue();
			}
		}
		String status = "P";
		List<RedumptionManagement> redumptionManagementsList = redumptionManagementepository.findByStatus(status);
		if (!redumptionManagementsList.isEmpty()) {
			for (RedumptionManagement redumptionManagements : redumptionManagementsList) {
				if(redumptionManagements.getRedeemOrderId() != null)	{
					StoreConf redumptionTime = storeConfRepository.findByKeyword(GoForWealthPRSConstants.REDUMPTION_TIME);
					int redmtionTime =Integer.valueOf(redumptionTime.getKeywordValue());
					String dueDate = redumptionManagements.getRedumptionDate();
					SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
					int diffInDays = GoForWealthPRSUtil.getDiffFromTwoDatesExceptSatAndSun(sdf.format(new Date(dueDate)));//15/01/2019-23/01/2019  value == 8
					if(diffInDays>redmtionTime){
						OrderRedemptionStatusRequest orderRedemptionStatusRequest = new OrderRedemptionStatusRequest("", "", "","", "", memberCode, redumptionManagements.getRedeemOrderId(), "", "", password, "ALL", "ALL", "", bseUserId);
						Object redemptionDetail = orderMfService.updateOrderRedemptionStatus(orderRedemptionStatusRequest);
						try {
							JSONObject jsonObject = new JSONObject(redemptionDetail.toString());
							String checkStatus = jsonObject.getString("Status");
							 if(checkStatus.equals("100")){
								JSONArray jSONArray = jsonObject.getJSONArray("RedemptionDetails");
								int size = jSONArray.length();
								for (int i = 0; i < size; i++) {
									JSONObject JSONObject5 = new JSONObject(jSONArray.get(i).toString());
									RedemptionStatusReportUserdata redemptionStatusReportUserdata = new RedemptionStatusReportUserdata();
									redemptionStatusReportUserdata.setAmount(new BigDecimal(JSONObject5.getDouble("Amount")).setScale(2, RoundingMode.HALF_UP));
									redemptionStatusReportUserdata.setAmt(new BigDecimal(JSONObject5.getDouble("Amt")).setScale(2, RoundingMode.HALF_UP));
									redemptionStatusReportUserdata.setBeneficiaryId(JSONObject5.getString("BeneficiaryId"));
									redemptionStatusReportUserdata.setBranchCode(JSONObject5.getString("BranchCode"));
									redemptionStatusReportUserdata.setClientCode(JSONObject5.getString("ClientCode"));
									redemptionStatusReportUserdata.setClientName(JSONObject5.getString("ClientName"));
									redemptionStatusReportUserdata.setDpcFlag(JSONObject5.getString("DPCFlag"));
									redemptionStatusReportUserdata.setDpTrans(JSONObject5.getString("DPTrans"));
									redemptionStatusReportUserdata.setFolioNo(JSONObject5.getString("FolioNo"));
									redemptionStatusReportUserdata.setIsin(JSONObject5.getString("ISIN"));
									redemptionStatusReportUserdata.setMemberCode(JSONObject5.getString("MemberCode"));
									redemptionStatusReportUserdata.setNav(JSONObject5.getString("Nav"));
									redemptionStatusReportUserdata.setOrderDate(JSONObject5.getString("OrderDate"));
									redemptionStatusReportUserdata.setOrderNo(JSONObject5.getInt("OrderNo"));
									redemptionStatusReportUserdata.setOrderType(JSONObject5.getString("OrderType"));
									//redemptionStatusReportUserdata.setQty(JSONObject5.getDouble("Qty"));
									redemptionStatusReportUserdata.setRemarks(JSONObject5.getString("Remarks"));
									redemptionStatusReportUserdata.setReportDate(JSONObject5.getString("ReportDate"));
									redemptionStatusReportUserdata.setRtaSchemeCode(JSONObject5.getString("RTASchemeCode"));
									redemptionStatusReportUserdata.setRtaTransNo(JSONObject5.getString("RTATransNo"));
									redemptionStatusReportUserdata.setSchemeCode(JSONObject5.getString("SchemeCode"));
									redemptionStatusReportUserdata.setSettNo(JSONObject5.getString("SettNo"));
									redemptionStatusReportUserdata.setSettType(JSONObject5.getString("SettType"));
									//redemptionStatusReportUserdata.setStt(JSONObject5.getInt("STT"));
									redemptionStatusReportUserdata.setSubOrderType(JSONObject5.getString("SubOrderType"));
									redemptionStatusReportUserdata.setUnit(JSONObject5.getString("Unit"));
									redemptionStatusReportUserdata.setUserId(JSONObject5.getInt("UserId"));
									redemptionStatusReportUserdataRepository.save(redemptionStatusReportUserdata);
									redumptionManagements.setStatus("R");
									redumptionManagements.setRedumptionUnit(new BigDecimal(JSONObject5.getString("Unit")));
									redumptionManagementepository.save(redumptionManagements);
									if(redumptionManagements.getIsOrderAvailable().equals("N")){
										TransferIn transferIn = transferInRepository.findByFolioNumberAndSchemeCode(redumptionManagements.getFolioNumber(), redumptionManagements.getSchemeCode());
										if(transferIn != null){
											transferIn.setRedumptionStatus("C");
											transferIn.setAvailableUnit(redumptionManagements.getRedumptionUnit().add(transferIn.getAvailableUnit()).subtract(new BigDecimal(JSONObject5.getString("Unit"))));
											transferInRepository.save(transferIn);
										}
									}
								}
							}else{
								redumptionManagements.setStatus("F");
								redumptionManagementepository.save(redumptionManagements);
								if(redumptionManagements.getIsOrderAvailable().equals("N")){
									TransferIn transferIn = transferInRepository.findByFolioNumberAndSchemeCode(redumptionManagements.getFolioNumber(), redumptionManagements.getSchemeCode());
									if(transferIn != null){
										transferIn.setRedumptionStatus("F");
										transferIn.setAvailableUnit(redumptionManagements.getRedumptionUnit().add(transferIn.getAvailableUnit()));
										transferInRepository.save(transferIn);
									}
								}
							}
						} catch (Exception e) {
							e.printStackTrace();
						}
					}
				}
			}
		}
	}

	public static int getDiffYears(Date first, Date last) {
		Calendar a = getCalendar(first);
		Calendar b = getCalendar(last);
		int diff = b.get(YEAR) - a.get(YEAR);
		if (a.get(MONTH) > b.get(MONTH) || (a.get(MONTH) == b.get(MONTH) && a.get(DATE) > b.get(DATE))) {
			diff--;
		}
		return diff;
	}

	public static Calendar getCalendar(Date date) {
		Calendar cal = Calendar.getInstance(Locale.US);
		cal.setTime(date);
		return cal;
	}

	public boolean checkDatesWithSameMonth(String addToCartDate, String todayDate) {
		boolean isTrue = false;
		SimpleDateFormat sdf1 = new SimpleDateFormat("dd MMM yyyy");
		SimpleDateFormat sdf12 = new SimpleDateFormat("MMM");
		try {
			Date addToCartDate1 = sdf1.parse(addToCartDate);
			Date todayDate1 = sdf1.parse(todayDate);
			String addToCartDate12 = sdf12.format(addToCartDate1);
			String todayDate12 = sdf12.format(todayDate1);
			if (addToCartDate12.equals(todayDate12)) {
				isTrue = true;
			}
		} catch (ParseException e) {
			e.printStackTrace();
		}
		return isTrue;
	}

	public void getNextSipDate(OrderItem orderItem2, AddToCartDTO addToCartDTO) {
		String sipDatesList = orderItem2.getScheme().getSipDates();
		String sipStartDate = orderItem2.getDescription();
		String dayOfSip = "";
		String[] sipDates = sipDatesList.split(",");
		String addToCartDate = sipStartDate;
		SimpleDateFormat sdf1 = new SimpleDateFormat("dd MMM yyyy");
		String todayDate = sdf1.format(new Date());
		String isMinusValueContain = String.valueOf((addToCartDate.compareTo(todayDate)));
		boolean istrue = checkDatesWithSameMonth(addToCartDate, todayDate);
		if (sipStartDate != null) {
			if (addToCartDate.compareTo(todayDate) <= 0 || isMinusValueContain.contains("-")) {
				if (istrue) {
					String day = "";
					LocalDate currentDate = LocalDate.now();
					int dom = currentDate.getDayOfMonth();
					int nextDate = dom + 1;
					boolean found = false;
					for (int i = 0; i < sipDates.length; i++) {
						if (nextDate == Integer.parseInt(sipDates[i])) {
							day = String.valueOf(nextDate);
							dayOfSip = String.valueOf(nextDate);
							found = true;
							break;
						} else if (nextDate > Integer.parseInt(sipDates[i])) {
							found = false;
						} else {
							found = true;
							day = String.valueOf(sipDates[i]);
							dayOfSip = String.valueOf(sipDates[i]);
							break;
						}
					}
					if (!found) {
						day = sipDates[0];
						dayOfSip = sipDates[0];
					}
					if (day.length() == 1) {
						day = "0" + day;
					}
					sipStartDate = GoForWealthPRSUtil.getSipStartDate(day);
					orderItem2.setDescription(sipStartDate);
					orderItem2.setField2(GoForWealthPRSUtil.createDayOfSip(dayOfSip));
					orderItemRepository.save(orderItem2);
					addToCartDTO.setSipDate(GoForWealthPRSUtil.createDayOfSip(dayOfSip));
					String[] sipDateList = new String[sipDates.length];
					for (int i = 0; i < sipDates.length; i++) {
						String sipDate1 = new String();
						sipDate1 = GoForWealthPRSUtil.createDayOfSip(sipDates[i]);
						sipDateList[i] = sipDate1;
					}
					addToCartDTO.setSipDateList(sipDateList);
				} else {
					addToCartDTO.setSipDate(orderItem2.getField2());
					String[] sipDateList = new String[sipDates.length];
					for (int i = 0; i < sipDates.length; i++) {
						String sipDate1 = new String();
						sipDate1 = GoForWealthPRSUtil.createDayOfSip(sipDates[i]);
						sipDateList[i] = sipDate1;
					}
					addToCartDTO.setSipDateList(sipDateList);
				}
			} else {
				addToCartDTO.setSipDate(orderItem2.getField2());
				String[] sipDateList = new String[sipDates.length];
				for (int i = 0; i < sipDates.length; i++) {
					String sipDate1 = new String();
					sipDate1 = GoForWealthPRSUtil.createDayOfSip(sipDates[i]);
					sipDateList[i] = sipDate1;
				}
				addToCartDTO.setSipDateList(sipDateList);
			}
		}
	}

	@Override
	public String updateSipDate(Integer userId, AddToCartDTO addToCartDTO) {
		Orders order = orderRepository.findByOrdersId(addToCartDTO.getOrderId());
		OrderItem orderItem = orderItemRepository.getOrderItemByOrderId(addToCartDTO.getOrderId());
		String sipStartDate = null;
		String getDayOfSip = null;
		String dayOfSip = addToCartDTO.getDayOfSip();
		if (dayOfSip.length() == 3) {
			String day = dayOfSip.substring(0, 1);
			getDayOfSip = "0" + day;
		}
		if (dayOfSip.length() == 4) {
			getDayOfSip = dayOfSip.substring(0, 2);
		}
		sipStartDate = GoForWealthPRSUtil.getSipStartDate(getDayOfSip);
		orderItem.setDescription(sipStartDate);
		orderItem.setField2(addToCartDTO.getDayOfSip());
		orderItemRepository.save(orderItem);
		order.setTimeplaced(new Date());
		order.setLastupdate(new Date());
		order.setStartdate(new Date());
		orderRepository.save(order);
		return "success";
	}

	@Override
	public String uploadNavDateAndCurrentNavForSchemeViaTxt(byte[] byteArray) {
		logger.info("In uploadNavDateAndCurrentNavForSchemeViaTxt()");
		List<Scheme> schemeList = new ArrayList<Scheme>();
		NavUpdateRequest navUpdateRequest = new NavUpdateRequest();
		List<SchemeForNav> schemeForNavList = new ArrayList<>();
		String response = "";
		List<String> result = new ArrayList<>();
		String line;
		InputStream is = null;
		int count = 0;
		BufferedReader br = null;
		try {
			is = new ByteArrayInputStream(byteArray);
			br = new BufferedReader(new InputStreamReader(is));
			while ((line = br.readLine()) != null) {
				result.add(line);
			}
			ListIterator<String> itr = result.listIterator(1);
			List<Scheme> scheme1 = schemeRepository.findAll();
			while (itr.hasNext()) {
				String str = itr.next();
				String[] arr = str.split("\\|", -1);
				for (Scheme scheme : scheme1) {
					if (scheme.getSchemeCode().equals(arr[1])) {
						scheme.setNavDate(arr[0]);
						scheme.setCurrentNav(arr[6]);
						schemeList.add(scheme);
						navUpdateRequest.setDate(arr[0]);
						SchemeForNav schemeForNav = new SchemeForNav();
						schemeForNav.setCode(arr[1]);
						schemeForNav.setNav(Double.valueOf(arr[6]));
						schemeForNavList.add(schemeForNav);
						break;
					}
				}
			}
			navUpdateRequest.setSchemes(schemeForNavList);
			schemeRepository.saveAll(schemeList);
			response = "scuuess";
		} catch (IOException e) {
			e.printStackTrace();
			return "Failed";
		}
		//updateCurrentNavAndCurrentValueForOrder();
		updateConsolidatedFollio();
		if(response.equals("scuuess")){
			updateNavToNode(navUpdateRequest);
		}
		logger.info("Out uploadNavDateAndCurrentNavForSchemeViaTxt()");
		return response;
	}

	@Override
	public void updateNatchAndBillerStatusOfUserWithStatusNotApproved() {
		List<User> user = userRepository.getUserWithOnboardingCompleted();
		if (!user.isEmpty()) {
			for (User user2 : user) {
				try{
					if (!user2.getOnboardingStatus().getEnachStatus().equals("APPROVED")) {
						if (user2.getOnboardingStatus().getMandateNumber() != null) {
							getAndUpdateMandateStatus(user2, "Natch");
						}
						if (user2.getOnboardingStatus().getIsipMandateNumber() != null) {
							getAndUpdateMandateStatus(user2, "Biller");
						}
					}
				}catch(Exception e){
					e.printStackTrace();
				}
			}
		}
	}

	public void getAndUpdateMandateStatus(User user, String type) {
		String mandateStatus = "";
		String mandateId = "";
		String remarks = "";
		String bseUserId = "";
		String memberCode = "";
		String password = "";
		String reuqestType = "";
		String clientCode = user.getOnboardingStatus().getClientCode();
		if (type.equals("Natch")) {
			mandateId = user.getOnboardingStatus().getMandateNumber();
			reuqestType = "MANDATE";
		} else if (type.equals("Biller")) {
			mandateId = user.getOnboardingStatus().getIsipMandateNumber();
			reuqestType = "MANDATE";
		}
		Date dateObj = user.getOnboardingStatus().getLastUpdated();
		List<SystemProperties> systemPropertiesList = systemPropertiesRepository.findAll();
		for (SystemProperties systemProperties : systemPropertiesList) {
			if (systemProperties.getId().getPropertyKey().equals("UserId")) {
				bseUserId = systemProperties.getId().getPropertyValue();
			} else if (systemProperties.getId().getPropertyKey().equals("MemberId")) {
				memberCode = systemProperties.getId().getPropertyValue();
			} else if (systemProperties.getId().getPropertyKey().equals("Password")) {
				password = systemProperties.getId().getPropertyValue();
			}
		}
		OtpGenerator otpGenerator = new OtpGenerator(5, true);
		String passKey = otpGenerator.generateOTP();
		Date todayDate = new Date();
		SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
		MandateDetailsRequestParam mandateDetailsRequestParam = new MandateDetailsRequestParam();
		mandateDetailsRequestParam.setClientCode(clientCode);
		//mandateDetailsRequestParam.setClientCode("G4W573");
		mandateDetailsRequestParam.setEncryptedPassword("");
		mandateDetailsRequestParam.setFromDate(sdf.format(dateObj));
		mandateDetailsRequestParam.setMandateId(mandateId);
		//mandateDetailsRequestParam.setMandateId("BSE000000188291");
		mandateDetailsRequestParam.setMemberCode(memberCode);
		mandateDetailsRequestParam.setMemberId(memberCode);
		mandateDetailsRequestParam.setPassKey(passKey);
		mandateDetailsRequestParam.setPassword(password);
		mandateDetailsRequestParam.setRequestType(reuqestType);// I MANDATE,NACH// MANDATE
		mandateDetailsRequestParam.setToDate(sdf.format(todayDate));
		mandateDetailsRequestParam.setUserId(bseUserId);
		Object mandateDetailResponse = orderMfService.getMandateDetails(mandateDetailsRequestParam);
		try {
			JSONObject jsonObject = new JSONObject(mandateDetailResponse.toString());
			String checkStatus = jsonObject.getString("Status");
			if (checkStatus.equals("101")) {
			} else {
				JSONArray jSONArray = jsonObject.getJSONArray("MandateDetails");
				int size = jSONArray.length();
				for (int i = 0; i < size; i++) {
					JSONObject JSONObject5 = new JSONObject(jSONArray.get(i).toString());
					mandateStatus = JSONObject5.getString("Status");
					if (!JSONObject5.getString("Remarks").equals("")) {
						remarks = JSONObject5.getString("Remarks");
					}
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		OnboardingStatus onboardingStatusObj = user.getOnboardingStatus();
		if (type.equals("Natch")) {
			if (!mandateStatus.equals("")) {
				onboardingStatusObj.setEnachStatus(mandateStatus);
			}
			if (mandateStatus.equals("REJECTED") 
					|| mandateStatus.equals("INITIAL REJECTION")
					|| mandateStatus.equals("REJECTION AT NPCI PRIOR TO DESTINATION BANK")
					|| mandateStatus.equals("REJECTED BY SPONSOR BANK") 
					|| mandateStatus.equals("CANCELLED BY INVESTOR")
					|| mandateStatus.equals("RETURNED BY EXCHANGE")
				) {
				//onboardingStatusObj.setMandateStatus(101);
				onboardingStatusObj.setUploadMandateStatus("Failed");
			}
			onboardingStatusObj.setField2(remarks);
		} else if (type.equals("Biller")) {
			if (!mandateStatus.equals("")) {
				onboardingStatusObj.setBillerStatus(mandateStatus);
			}
			if (mandateStatus.equals("REJECTED") 
					|| mandateStatus.equals("INITIAL REJECTION")
					|| mandateStatus.equals("REJECTION AT NPCI PRIOR TO DESTINATION BANK")
					|| mandateStatus.equals("REJECTED BY SPONSOR BANK") 
					|| mandateStatus.equals("CANCELLED BY INVESTOR")
					|| mandateStatus.equals("RETURNED BY EXCHANGE")
				) {
				//onboardingStatusObj.setIsipMandateStatus(101);
			}
		}
		onboardingStatusRepository.save(onboardingStatusObj);
	}

	@Override
	public String uploadLumpsumSchemeTextFile(byte[] byteArray) {
		logger.info("In addSchemeviaBase64()");
		boolean isFound = true;
		int update =0;
		int insert = 0;
		int totalCount = 0;
		List<String> result = new ArrayList<>();
		List<Scheme> insertSchemeList = new ArrayList<>();
		List<Scheme> updateSchemeList = new ArrayList<>();
		String line;
		InputStream is = null;
		BufferedReader br = null;
		try {
			is = new ByteArrayInputStream(byteArray);
			br = new BufferedReader(new InputStreamReader(is));
			while ((line = br.readLine()) != null) {
				result.add(line);
			}
			ListIterator<String> itr = result.listIterator(1);
			List<Scheme> schemeList = schemeRepository.findAll();
			while (itr.hasNext()) {
				String str = itr.next();
				String[] arr = str.split("\\|", -1);
				totalCount++;
				for(Scheme scheme : schemeList){
					if(scheme.getSchemeCode().equals(arr[1])){
						scheme.setUniqueNo(Integer.valueOf(arr[0]));
						//scheme.setSchemeCode(arr[1]);
						scheme.setRtaSchemeCode(arr[2]);
						scheme.setAmcSchemeCode(arr[3]);
						scheme.setIsin(arr[4]);
						scheme.setAmcCode(arr[5]);
						scheme.setSchemeType(arr[6]);
						scheme.setSchemePlan(arr[7]);
						scheme.setSchemeName(arr[8]);
						scheme.setPurchaseAllowed(arr[9]);
						scheme.setPurchaseTransactionMode(arr[10]);
						String temp = arr[11];
						String minAmount[] = temp.split("\\.");
						try{
							scheme.setMinimumPurchaseAmount(Integer.parseInt(minAmount[0]));
						}
						catch(Exception e){
							e.printStackTrace();
							//break;
						}
						scheme.setAdditionalPurchaseAmount(arr[12]);
						scheme.setMaximumPurchaseAmount(arr[13]);
						scheme.setPurchaseAmountMultiplier(arr[14]);
						scheme.setPurchaseCutoffTime(arr[15]);
						scheme.setRedemptionAllowed(arr[16]);
						scheme.setRedemptionTransactionMode(arr[17]);
						scheme.setMinimumRedemptionQty(arr[18]);
						scheme.setRedemptionQtyMultiplier(arr[19]);
						scheme.setMaximumRedemptionQty(arr[20]);
						scheme.setRedemptionAmountMinimum(arr[21]);
						scheme.setRedemptionAmountMaximum(arr[22]);
						scheme.setRedemptionAmountMultiple(arr[23]);
						scheme.setRedemptionCutoffTime(arr[24]);
						scheme.setRtaAgentCode(arr[25]);
						scheme.setAmcActiveFlag(arr[26]);
						scheme.setDividendReinvestmentFlag(arr[27]);
						scheme.setSipFlag(arr[28]);
						scheme.setStpFlag(arr[29]);
						scheme.setSwpFlag(arr[30]);
						scheme.setSwitchFlag(arr[31]);
						scheme.setSettlementType(arr[32]);
						scheme.setAmcInd(arr[33]);
						//scheme.setFaceValue(arr[34]);
						scheme.setStartDate(arr[35]);
						scheme.setEndDate(arr[36]);
						scheme.setExitLoadFlag(arr[37]);
						scheme.setExitLoad(arr[38]);
						scheme.setLockInPeriodFlag(arr[39]);
						scheme.setLockInPeriod(arr[40]);
						scheme.setChannelPartnerCode(arr[41]);
						//scheme.setAmfiCode(0);
						//scheme.setBenchmarkCode("");
						//scheme.setRating("");
						scheme.setNfoDate(new Date());
						updateSchemeList.add(scheme);
						isFound = false;
						System.out.println("No Of Update ==="+update++);
						break;
					}else{
						isFound = true;
					}
				}
				if(isFound){
					if(!arr[7].equals("DIRECT")){
						Scheme scheme = new Scheme();
						scheme.setUniqueNo(Integer.valueOf(arr[0]));
						scheme.setSchemeCode(arr[1]);
						scheme.setRtaSchemeCode(arr[2]);
						scheme.setAmcSchemeCode(arr[3]);
						scheme.setIsin(arr[4]);
						scheme.setAmcCode(arr[5]);
						scheme.setSchemeType(arr[6]);
						scheme.setSchemePlan(arr[7]);
						scheme.setSchemeName(arr[8]);
						scheme.setPurchaseAllowed(arr[9]);
						scheme.setPurchaseTransactionMode(arr[10]);
						String temp = arr[11];
						String minAmount[] = temp.split("\\.");
						try{
							scheme.setMinimumPurchaseAmount(Integer.valueOf(minAmount[0]));	
						}catch(Exception e){
							e.printStackTrace();
							//break;
						}
						scheme.setAdditionalPurchaseAmount(arr[12]);
						scheme.setMaximumPurchaseAmount(arr[13]);
						scheme.setPurchaseAmountMultiplier(arr[14]);
						scheme.setPurchaseCutoffTime(arr[15]);
						scheme.setRedemptionAllowed(arr[16]);
						scheme.setRedemptionTransactionMode(arr[17]);
						scheme.setMinimumRedemptionQty(arr[18]);
						scheme.setRedemptionQtyMultiplier(arr[19]);
						scheme.setMaximumRedemptionQty(arr[20]);
						scheme.setRedemptionAmountMinimum(arr[21]);
						scheme.setRedemptionAmountMaximum(arr[22]);
						scheme.setRedemptionAmountMultiple(arr[23]);
						scheme.setRedemptionCutoffTime(arr[24]);
						scheme.setRtaAgentCode(arr[25]);
						scheme.setAmcActiveFlag(arr[26]);
						scheme.setDividendReinvestmentFlag(arr[27]);
						scheme.setSipFlag(arr[28]);
						scheme.setStpFlag(arr[29]);
						scheme.setSwpFlag(arr[30]);
						scheme.setSwitchFlag(arr[31]);
						scheme.setSettlementType(arr[32]);
						scheme.setAmcInd(arr[33]);
						scheme.setFaceValue(arr[34]);
						scheme.setStartDate(arr[35]);
						scheme.setEndDate(arr[36]);
						scheme.setExitLoadFlag(arr[37]);
						scheme.setExitLoad(arr[38]);
						scheme.setLockInPeriodFlag(arr[39]);
						scheme.setLockInPeriod(arr[40]);
						scheme.setChannelPartnerCode(arr[41]);
						scheme.setAmfiCode(0);
						scheme.setBenchmarkCode("");
						scheme.setRating("");
						scheme.setNfoDate(new Date());
						scheme.setStatus("");
						scheme.setRegistrarTransferAgent("");
						scheme.setRisk("");
						scheme.setYear("");
						scheme.setMinSipAmount("");
						scheme.setField2("");
						
						scheme.setPriority("3");
						String schemeNameTemp = arr[8].replace(' ', '-');
						String display = "0";
						if (arr[1].contains("-L1-I")) {
							schemeNameTemp += "-L1-I";
							display = "1";
						}
						else if (arr[1].contains("-L1")) {
							schemeNameTemp += "-L1";
							display = "1";
						}
						else if(arr[1].contains("-L0")){
							schemeNameTemp += "-L0";
							display = "1";
						}
						else if(arr[1].endsWith("-I")){
							schemeNameTemp += "-I";
							display = "1";
						}
						scheme.setKeyword(schemeNameTemp);
						scheme.setDisplay(display);
						insertSchemeList.add(scheme);
						System.out.println("NO Of Insert ===== "+insert++);
						//break;
					}
				}		
			}
			System.out.println("Total Count ===="+totalCount);
			System.out.println("Total No of Insert === "+insertSchemeList.size());
			System.out.println("Total No of Update === "+updateSchemeList.size());
			schemeRepository.saveAll(insertSchemeList);
			schemeRepository.saveAll(updateSchemeList);
		} catch (IOException e) {
			e.printStackTrace();
		}
		logger.info("Out addSchemeviaBase64()");
		return "Success";
	}

	@Override
	public String uploadSipSchemeTextFile(byte[] byteArray) {
		logger.info("In addSipSchemeviaBase64()");
		List<String> result = new ArrayList<>();
		List<Scheme> updateSchemeList = new ArrayList<Scheme>();
		String line;
		int count = 0;
		int update  = 0;
		InputStream is = null;
		BufferedReader br = null;
		try {
			is = new ByteArrayInputStream(byteArray);
			br = new BufferedReader(new InputStreamReader(is));
			while ((line = br.readLine()) != null) {
				result.add(line);
			}
			ListIterator<String> itr = result.listIterator(1);
			List<Scheme> schemeList = schemeRepository.findAll();
			while (itr.hasNext()) {
				count++;
				String str = itr.next();
				String[] arr = str.split("\\|", -1);
				if(!schemeList.isEmpty()){
					for (Scheme scheme : schemeList) {
						if(scheme.getSchemeCode().equals(arr[2]) && arr[5].equals("MONTHLY")){
							scheme.setSipTransactionMode(arr[4]);
							scheme.setSipDates(arr[6]);
							scheme.setSipFrequency(arr[5]);
							scheme.setSipMinimumGap(arr[7]);
							scheme.setSipMaximumGap(arr[8]);
							scheme.setSipInstallmentGap(arr[9]);
							scheme.setSipStatus(arr[10]);
							scheme.setSipMinimumInstallmentAmount(new BigDecimal(arr[11]));
							scheme.setSipMaximumInstallmentAmount(new BigDecimal(arr[12]));
							scheme.setSipMultiplierAmount(arr[13]);
							scheme.setSipMinimumInstallmentNumber(arr[14]);
							scheme.setSipMaximumInstallmentNumber(arr[15]);
							scheme.setMinSipAmount(arr[11]);
							updateSchemeList.add(scheme);
							System.out.println("No Of Update === "+update++);
							break;
						}		
					}
				}
			}
			System.out.println("Total No. Of Count === "+count);
			System.out.println("No Of Schemes To Update === "+updateSchemeList.size());
			schemeRepository.saveAll(updateSchemeList);
		} catch (IOException e) {
			e.printStackTrace();
			return "Internal server error";
		}
		logger.info("Out addSipSchemeviaBase64()");
		return "Success";
	}
	
	@Override
	public String uploadSchemeWithICRAPassingAmfiCode() throws JSONException {
		logger.info("In uploadSchemeWithICRAPassingAmfiCode");
		Object responseObj = null;
		String response = "";
		List<Scheme> schemeList = schemeRepository.findSchemeDateWithAmfiCode();
		List<Scheme> updateAmfiScheme = new ArrayList<Scheme>();
		if(!schemeList.isEmpty()){
			for (Scheme scheme2 : schemeList) {
				if(scheme2.getAmfiCode() != 0){
					responseObj = navService.getMFSchemeMaster(String.valueOf(scheme2.getAmfiCode()));
					JSONArray jsonArray = new JSONArray(responseObj.toString());
					JSONObject jsonObject = jsonArray.getJSONObject(0);
					try{
						if(jsonObject.getJSONObject("Result") != null){
							JSONObject jsonObjectRef = jsonObject.getJSONObject("Result");
							response = jsonObjectRef.toString();
							String isinCodes = jsonObjectRef.optString("ISIN_Codes", null);
							String isinPayout = jsonObjectRef.optString("ISIN_Payout", null);
							if((isinCodes != null && !isinCodes.equals("")) || (isinPayout != null && !isinPayout.equals(""))){
								if(isinCodes != null && !isinCodes.equals("")){
									if(isinPayout != null && !isinPayout.equals("")){
										if(isinCodes.equals(isinPayout)){
											List<Scheme> schemeListWithIsin = schemeRepository.findByIsin(isinCodes);
											for (Scheme scheme : schemeListWithIsin) {
												scheme.setSchemeName(jsonObjectRef.optString("Scheme_Name", null));
												scheme.setFund_Nature(jsonObjectRef.optString("Fund_Name", null));
												scheme.setSchemeCategory(jsonObjectRef.optString("Category", null));
												scheme.setSchemeSubCategory(jsonObjectRef.optString("Sub_Category", null));
												scheme.setSchemeOptions(jsonObjectRef.optString("Option", null));
												scheme.setFaceValue(String.valueOf(jsonObjectRef.getDouble("Face_Value")));
												scheme.setBenchmarkCode(jsonObjectRef.optString("BENCHMARK_CODE", null));
												scheme.setInvestmentObjective(jsonObjectRef.optString("Investment_Objective", null));
												scheme.setRisk(jsonObjectRef.optString("Risk_Level", null));
												scheme.setFundManager(jsonObjectRef.optString("Fund_Manager", null));
												scheme.setClassification(jsonObjectRef.optString("Classification", null));
												scheme.setAmfiCode(Integer.parseInt(jsonObjectRef.optString("AMFI_Code",null)));
												updateAmfiScheme.add(scheme);
											}
										}else{
											List<Scheme> schemeListWithIsinPayout = schemeRepository.findByIsin(isinPayout);
											for (Scheme schemeObj : schemeListWithIsinPayout) {
												schemeObj.setSchemeName(jsonObjectRef.optString("Scheme_Name", null));
												schemeObj.setFund_Nature(jsonObjectRef.optString("Fund_Name", null));
												schemeObj.setSchemeCategory(jsonObjectRef.optString("Category", null));
												schemeObj.setSchemeSubCategory(jsonObjectRef.optString("Sub_Category", null));
												schemeObj.setSchemeOptions(jsonObjectRef.optString("Option", null));
												schemeObj.setFaceValue(String.valueOf(jsonObjectRef.getDouble("Face_Value")));
												schemeObj.setBenchmarkCode(jsonObjectRef.optString("BENCHMARK_CODE", null));
												schemeObj.setInvestmentObjective(jsonObjectRef.optString("Investment_Objective", null));
												schemeObj.setRisk(jsonObjectRef.optString("Risk_Level", null));
												schemeObj.setFundManager(jsonObjectRef.optString("Fund_Manager", null));
												schemeObj.setClassification(jsonObjectRef.optString("Classification", null));
												schemeObj.setAmfiCode(Integer.parseInt(jsonObjectRef.optString("AMFI_Code",null)));
												updateAmfiScheme.add(schemeObj);
											}
											List<Scheme> schemeListWithIsinCodes = schemeRepository.findByIsin(isinCodes);
											for (Scheme schemeRef : schemeListWithIsinCodes) {
												schemeRef.setSchemeName(jsonObjectRef.optString("Scheme_Name", null));
												schemeRef.setFund_Nature(jsonObjectRef.optString("Fund_Name", null));
												schemeRef.setSchemeCategory(jsonObjectRef.optString("Category", null));
												schemeRef.setSchemeSubCategory(jsonObjectRef.optString("Sub_Category", null));
												schemeRef.setSchemeOptions(jsonObjectRef.optString("Option", null));
												schemeRef.setFaceValue(String.valueOf(jsonObjectRef.getDouble("Face_Value")));
												schemeRef.setBenchmarkCode(jsonObjectRef.optString("BENCHMARK_CODE", null));
												schemeRef.setInvestmentObjective(jsonObjectRef.optString("Investment_Objective", null));
												schemeRef.setRisk(jsonObjectRef.optString("Risk_Level", null));
												schemeRef.setFundManager(jsonObjectRef.optString("Fund_Manager", null));
												schemeRef.setClassification(jsonObjectRef.optString("Classification", null));
												schemeRef.setAmfiCode(Integer.parseInt(jsonObjectRef.optString("AMFI_Code",null)));
												updateAmfiScheme.add(schemeRef);
											}
										}
									}else{
										List<Scheme> schemeListWithIsinList = schemeRepository.findByIsin(isinCodes);
										for (Scheme schemeIsinRef : schemeListWithIsinList) {
											schemeIsinRef.setSchemeName(jsonObjectRef.optString("Scheme_Name", null));
											schemeIsinRef.setFund_Nature(jsonObjectRef.optString("Fund_Name", null));
											schemeIsinRef.setSchemeCategory(jsonObjectRef.optString("Category", null));
											schemeIsinRef.setSchemeSubCategory(jsonObjectRef.optString("Sub_Category", null));
											schemeIsinRef.setSchemeOptions(jsonObjectRef.optString("Option", null));
											schemeIsinRef.setFaceValue(String.valueOf(jsonObjectRef.getDouble("Face_Value")));
											schemeIsinRef.setBenchmarkCode(jsonObjectRef.optString("BENCHMARK_CODE", null));
											schemeIsinRef.setInvestmentObjective(jsonObjectRef.optString("Investment_Objective", null));
											schemeIsinRef.setRisk(jsonObjectRef.optString("Risk_Level", null));
											schemeIsinRef.setFundManager(jsonObjectRef.optString("Fund_Manager", null));
											schemeIsinRef.setClassification(jsonObjectRef.optString("Classification", null));
											schemeIsinRef.setAmfiCode(Integer.parseInt(jsonObjectRef.optString("AMFI_Code",null)));
											updateAmfiScheme.add(schemeIsinRef);
										}
									}
								}else{
									if(isinPayout != null && !isinPayout.equals("")){
										List<Scheme> schemeListWithIsinPayoutList = schemeRepository.findByIsin(isinPayout);
										for (Scheme schemeObjRef : schemeListWithIsinPayoutList) {
											schemeObjRef.setSchemeName(jsonObjectRef.optString("Scheme_Name", null));
											schemeObjRef.setFund_Nature(jsonObjectRef.optString("Fund_Name", null));
											schemeObjRef.setSchemeCategory(jsonObjectRef.optString("Category", null));
											schemeObjRef.setSchemeSubCategory(jsonObjectRef.optString("Sub_Category", null));
											schemeObjRef.setSchemeOptions(jsonObjectRef.optString("Option", null));
											schemeObjRef.setFaceValue(String.valueOf(jsonObjectRef.getDouble("Face_Value")));
											schemeObjRef.setBenchmarkCode(jsonObjectRef.optString("BENCHMARK_CODE", null));
											schemeObjRef.setInvestmentObjective(jsonObjectRef.optString("Investment_Objective", null));
											schemeObjRef.setRisk(jsonObjectRef.optString("Risk_Level", null));
											schemeObjRef.setFundManager(jsonObjectRef.optString("Fund_Manager", null));
											schemeObjRef.setClassification(jsonObjectRef.optString("Classification", null));
											schemeObjRef.setAmfiCode(Integer.parseInt(jsonObjectRef.optString("AMFI_Code",null)));
											updateAmfiScheme.add(schemeObjRef);
										}
									}
								}
							}else{
								System.out.println("===== ISIN Codes And ISIN Payout Both Are Blank =====");
							}
						}
					}catch(Exception e){
						e.printStackTrace();
					}
				}
			}
			if(updateAmfiScheme.size()>0){
				schemeRepository.saveAll(updateAmfiScheme);
			}
			response = "success";
		}
		logger.info("Out uploadSchemeWithICRAPassingAmfiCode");
		return response;
	}
	
	@Override
	public String uploadNavDateAndCurrentNavForSchemeViaAutoDownload(NavUpdateRequest navUpdateRequest) {
		List<Scheme> schemeList = new ArrayList<Scheme>();
		String response = "";
		try {
			List<Scheme> scheme1 = schemeRepository.findAll();
			String date = navUpdateRequest.getDate();
			for(SchemeForNav schemeForNav : navUpdateRequest.getSchemes()) {
				for (Scheme scheme : scheme1) {
					if (scheme.getSchemeCode().equals(schemeForNav.getCode())) {
						scheme.setNavDate(date);
						scheme.setCurrentNav(schemeForNav.getNav().toString());
						schemeList.add(scheme);
						break;
					}
				}
			}
			schemeRepository.saveAll(schemeList);
			response = "success";
		} catch (Exception e) {
			e.printStackTrace();
			return "Failed";
		}
		//updateCurrentNavAndCurrentValueForOrder();
		updateConsolidatedFollio();
		return response;
	}

	@Override
	public FundSchemeDTO getSchemeDetailsByKeyword(String keyword) {
		//String errorResponse = "No data found against your query.";
		FundSchemeDTO fundSchemeDTO = new FundSchemeDTO();
		List<Scheme> scheme = schemeRepository.findBykeyword(keyword);
		int size = scheme.size();
		for(int i=0; i<size;){
			if (scheme.size() > 0) {
				fundSchemeDTO.setSchemeId(scheme.get(i).getSchemeId());
				fundSchemeDTO.setSchemeCode(scheme.get(i).getSchemeCode());
				fundSchemeDTO.setUniqueNo(scheme.get(i).getUniqueNo());
				fundSchemeDTO.setSchemeName(scheme.get(i).getSchemeName());
				fundSchemeDTO.setSchemeType(scheme.get(i).getSchemeType());
				fundSchemeDTO.setPlan(scheme.get(i).getSchemePlan());
				fundSchemeDTO.setFaceValue(scheme.get(i).getFaceValue());
				fundSchemeDTO.setMinimumPurchaseAmount(scheme.get(i).getMinimumPurchaseAmount());
				fundSchemeDTO.setMinSipAmount(scheme.get(i).getMinSipAmount());
				fundSchemeDTO.setSipAllowed(scheme.get(i).getSipFlag());
				fundSchemeDTO.setPurchaseAllowed(scheme.get(i).getPurchaseAllowed());
				fundSchemeDTO.setSchemeLaunchDate(scheme.get(i).getStartDate());
				fundSchemeDTO.setSchemeEndDate(scheme.get(i).getEndDate());
				fundSchemeDTO.setSipMaximumGap(scheme.get(i).getSipMaximumGap());
				if (scheme.get(i).getSipDates() != null) {
					fundSchemeDTO.setSipAllowedDate(scheme.get(i).getSipDates());
				}
				if (scheme.get(i).getFundManager() != null && !scheme.get(i).getFundManager().equals("")) {
					fundSchemeDTO.setFundManager(scheme.get(i).getFundManager());
				} else {
					fundSchemeDTO.setFundManager("N/A");
				}
				if (scheme.get(i).getInvestmentObjective() != null && !scheme.get(i).getInvestmentObjective().equals("")) {
					fundSchemeDTO.setInvestmentObjective(scheme.get(i).getInvestmentObjective());
				} else {
					fundSchemeDTO.setInvestmentObjective("N/A");
				}
				if (scheme.get(i).getBenchmarkCode() != null && !scheme.get(i).getBenchmarkCode().equals("")) {
					fundSchemeDTO.setBenchmarkCode(scheme.get(i).getBenchmarkCode());
				} else {
					fundSchemeDTO.setBenchmarkCode("N/A");
				}
				if (scheme.get(i).getSchemeOptions() != null && !scheme.get(i).getSchemeOptions().equals("")) {
					fundSchemeDTO.setOption(scheme.get(i).getSchemeOptions());
				} else {
					fundSchemeDTO.setOption("N/A");
				}
				if (scheme.get(i).getSchemeSubCategory() != null && !scheme.get(i).getSchemeSubCategory().equals("")) {
					fundSchemeDTO.setSchemeSubCategory(scheme.get(i).getSchemeSubCategory());
				} else {
					fundSchemeDTO.setSchemeSubCategory("N/A");
				}
				if (scheme.get(i).getSchemeCategory() != null && !scheme.get(i).getSchemeCategory().equals("")) {
					fundSchemeDTO.setSchemeCategory(scheme.get(i).getSchemeCategory());
				} else {
					fundSchemeDTO.setSchemeCategory("N/A");
				}
				if (scheme.get(i).getRisk() != null && !scheme.get(i).getRisk().equals("")) {
					fundSchemeDTO.setRisk(scheme.get(i).getRisk());
				}
				SimpleDateFormat sdf1 = new SimpleDateFormat("yy/MM/dd");
				Date dateRef = new Date(scheme.get(i).getStartDate());
				String todayDates = sdf1.format(new Date());
				String schemeLaunchDate = sdf1.format(dateRef);
				Date todayDateRef = null;
				Date schemeLaunchDateRef = null;
				try {
					schemeLaunchDateRef = sdf1.parse(schemeLaunchDate);
					todayDateRef = sdf1.parse(todayDates);
				} catch (Exception ex) {
					ex.printStackTrace();
				}
				int schemeYears = getDiffYears(schemeLaunchDateRef, todayDateRef);
				fundSchemeDTO.setSchemeYears(schemeYears);
				List<SchemeRecentViewDTO> schemeRecentViewDTOs = getSchemeRecentView(scheme.get(i));
				fundSchemeDTO.setSchemeRecentViewDTOList(schemeRecentViewDTOs);
				List<SimilarSchemeDTO> similarSchemeDTOList = getSimilarSchemeDTO(scheme.get(i));
				fundSchemeDTO.setSimilarSchemeDTOList(similarSchemeDTOList);
				String currentNav = scheme.get(i).getCurrentNav();
				String navDate = scheme.get(i).getNavDate();
				try {
					SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
					SimpleDateFormat sdf12 = new SimpleDateFormat("dd MMMM yyyy");
					if (currentNav != null) {
						currentNav = new BigDecimal(currentNav).setScale(2, RoundingMode.HALF_UP).toString();
						fundSchemeDTO.setCurrentNav(currentNav);
						Date date1 = sdf.parse(navDate);
						navDate = sdf12.format(date1);
						fundSchemeDTO.setCurrentDate(navDate);
					} else {
						fundSchemeDTO.setCurrentNav("N/A");
						if (navDate != null) {
							Date date1 = sdf.parse(navDate);
							navDate = sdf12.format(date1);
							fundSchemeDTO.setCurrentDate(navDate);
						}
					}
				} catch (ParseException e) {
					e.printStackTrace();
				}
				if (scheme.size() > 0) {
					if (scheme.get(i).getAmfiCode() != 0) {
						try{
							NavApiResponseDTO navApiResponseDTO = navService.getNavData(String.valueOf(scheme.get(i).getAmfiCode()));
							if (navApiResponseDTO != null && navApiResponseDTO.getInformation() == null) {
								if (navApiResponseDTO.getONE_YEAR() != null) {
									fundSchemeDTO.setOneYearReturn(new BigDecimal(navApiResponseDTO.getONE_YEAR()).setScale(2, RoundingMode.HALF_UP).toString());
								}
								if (navApiResponseDTO.getTHREE_YEAR() != null) {
									fundSchemeDTO.setThreeYearReturn(new BigDecimal(navApiResponseDTO.getTHREE_YEAR()).setScale(2, RoundingMode.HALF_UP).toString());
								}
								if (navApiResponseDTO.getFIVE_YEAR() != null) {
									fundSchemeDTO.setFiveYearReturn(new BigDecimal(navApiResponseDTO.getFIVE_YEAR()).setScale(2, RoundingMode.HALF_UP).toString());
								}
							}
						}catch(Exception e){
							e.printStackTrace();
						}
					}
				}
				return fundSchemeDTO;
			}else {
				return fundSchemeDTO = null;
			}
		}
		return fundSchemeDTO = null;
	}

	@Override
	public List<FundSchemeDTO> getAllSchemeWithAuth(int offset) {
		List<FundSchemeDTO> fundSchemeDTOList = new ArrayList<>();
		//Page<Scheme> schemeList = schemeRepository.findAll(new PageRequest(offset, 8));
		List<Scheme> schemeList = schemeRepository.getAllSchemes(new PageRequest(offset, 8));
		for (Scheme scheme : schemeList) {
			FundSchemeDTO fundSchemeDTO = new FundSchemeDTO();
			fundSchemeDTO.setSchemeId(scheme.getSchemeId());
			fundSchemeDTO.setSchemeCode(scheme.getSchemeCode());
			fundSchemeDTO.setPlan(scheme.getSchemePlan());
			fundSchemeDTO.setSchemeName(scheme.getSchemeName());
			fundSchemeDTO.setSchemeType(scheme.getSchemeType());
			fundSchemeDTO.setSchemeLaunchDate(scheme.getStartDate());
			fundSchemeDTO.setSchemeEndDate(scheme.getEndDate());
			fundSchemeDTO.setAmcSchemeCode(scheme.getAmcSchemeCode());
			fundSchemeDTO.setAmfiCode(scheme.getAmfiCode());
			fundSchemeDTO.setBenchmarkCode(scheme.getBenchmarkCode());
			fundSchemeDTO.setFaceValue(scheme.getFaceValue());
			fundSchemeDTO.setMinSipAmount(scheme.getMinSipAmount());
			fundSchemeDTO.setReturn_(scheme.getReturnValue());
			fundSchemeDTO.setRisk(scheme.getRisk());
			fundSchemeDTO.setRtaCode(scheme.getRtaSchemeCode());
			fundSchemeDTO.setSipAllowed(scheme.getSipFlag());
			fundSchemeDTO.setMinimumPurchaseAmount(scheme.getMinimumPurchaseAmount());
			fundSchemeDTO.setMaximumPurchaseAmount(scheme.getMaximumPurchaseAmount());
			fundSchemeDTO.setStatus(scheme.getStatus());
			fundSchemeDTO.setSchemeKeyword(scheme.getKeyword());
			fundSchemeDTO.setAmcCode(scheme.getAmcCode());
			fundSchemeDTO.setPriority(scheme.getPriority());
			fundSchemeDTOList.add(fundSchemeDTO);
		}
		return fundSchemeDTOList;
	}

	@Override
	public void updateUserDebitedSipInstallmentsThroughDates(){
		String sipRegnDate ="";
		int noOfIteration = 0;
		List<String> sipRegnPreviousDateList = new ArrayList<>();
		StoreConf storeConf = storeConfRepository.findByKeyword(GoForWealthPRSConstants.PREVIOUD_DATE_FOR_SIP_INSTALLMENT_UPDATE);
		StoreConf storeConfObj = storeConfRepository.findByKeyword(GoForWealthPRSConstants.NO_OF_ITERATION_FOR_PREVIOUS_SIP_INSTALLMENT);
		sipRegnDate = storeConf.getKeywordValue();
		noOfIteration = Integer.parseInt(storeConfObj.getKeywordValue());
		sipRegnPreviousDateList.add(sipRegnDate);
		for (int i = 0; i < noOfIteration-1; i++) {
			String prevDate =  GoForWealthPRSUtil.getSipPreviousDate(sipRegnDate);
			sipRegnPreviousDateList.add(prevDate);
			sipRegnDate = prevDate;
		}
		for (String sipRegnDates : sipRegnPreviousDateList) {
			updateUserDebitedSipInstallments(sipRegnDates);
		}
	}

	public void updateUserDebitedSipInstallments(String sipRegnDate) {
		logger.info("In updateUserDebitedSipInstallments");
		List<Orders> orderList = orderRepository.getUniqueOrderRegnIdList();
		String MemberId = "";
		String bseUserId = "";
		String password = "";
		List<SystemProperties> systemPropertiesList = systemPropertiesRepository.findAll();
		for (SystemProperties systemProperties : systemPropertiesList) {
			if (systemProperties.getId().getPropertyKey().equals("MemberId")) {
				MemberId = systemProperties.getId().getPropertyValue();
			}else if (systemProperties.getId().getPropertyKey().equals("UserId")) {
				bseUserId = systemProperties.getId().getPropertyValue();
			} else if (systemProperties.getId().getPropertyKey().equals("Password")) {
				password = systemProperties.getId().getPropertyValue();
			} 
		}
		if(!orderList.isEmpty()){
			for (Orders orders : orderList) {
				if(orders.getField2() != null && !orders.getField2().equals("N") && orders.getType().equals("SIP")){
					try{
						User user = orders.getUser();
						String bseOrderId = getChildOrderId(user.getOnboardingStatus().getClientCode(), sipRegnDate,"", MemberId,orders.getField2(), "XSIP", systemPropertiesRepository);
						if(!bseOrderId.equals("101")){
							Orders isOrderExist = orderRepository.findByBseOrderId(bseOrderId);
							OrderItem orderItem = orderItemRepository.getOrderItemByOrderId(orders.getOrdersId());
							if (isOrderExist != null) {
								if(orders.getStatus().equals("M")){
									CheckOrderStatusRequest checkOrderStatusRequest = new CheckOrderStatusRequest(user.getOnboardingStatus().getClientCode(), "", "", "", "",MemberId,bseOrderId, "", "", password, "","ALL", "", "", bseUserId);
									getAndUpdateInvalidOrderStatus(checkOrderStatusRequest,orders,orderItem);
								}
							}else{
								PrsDTO prsDto = checkOrderAndAllotementStatus(user, bseOrderId);
								if(prsDto.getResponse().equals("success")){
									AddToCartDTO addToCartDTO = inserNewOrderForDebitedInstallment(user,orders,orderItem,orders.getField2(),bseOrderId);
									if(addToCartDTO.getStatus().equals("success")){
										Orders newInsertedOrderDetail = orderRepository.findByOrdersId(addToCartDTO.getOrderId());
										OrderItem newInsertedOrderItemDetail = orderItemRepository.getOrderItemByOrderId(addToCartDTO.getOrderId());
										String paymentInsertStatus = insertNewPaymentRecordForDebitedInstallment(newInsertedOrderDetail,newInsertedOrderItemDetail,user);
										if(paymentInsertStatus.equals("success")){
											updateOrderStatus(addToCartDTO.getOrderId(), user.getUserId(), bseOrderId,"Yes");
											//todo need to send mail to go4wealth while schduler is runing how many New allotement is updated today.
										}
									}
								}
							}
						}
					}catch(Exception e){
						e.printStackTrace();
					}
				}
			}
			//updateCurrentNavAndCurrentValueForOrder();
		}
		logger.info("Out updateUserDebitedSipInstallments");
	}

	public AddToCartDTO inserNewOrderForDebitedInstallment(User user , Orders order , OrderItem orderItemsData,String regnId,String bseOrderId) {
		AddToCartDTO addToCartDTO = new AddToCartDTO();
		try {
			Scheme scheme = schemeRepository.getOne(orderItemsData.getScheme().getSchemeId());
			Orders orders = new Orders();
			orders.setUser(user);
			orders.setLastupdate(new Date());
			orders.setStartdate(new Date());
			orders.setExpiredate(new Date());
			orders.setTimeplaced(new Date());
			orders.setPaymentOptions(order.getPaymentOptions());
			orders.setType(order.getType());
			orders.setTotaladjustment(new BigDecimal(0));
			orders.setTotalproduct(new BigDecimal(1));
			orders.setTotaltax(new BigDecimal(0));
			orders.setGoalId(order.getGoalId());
			orders.setGoalName(order.getGoalName());
			orders.setStatus("AP"); 
			orders.setField2(regnId);
			orders.setBseOrderId(bseOrderId);
			orders.setMandateId(order.getMandateId());
			if(order.getTranferInId() != null){
				orders.setTranferInId(order.getTranferInId());
			}
			if(order.getGoalId() != null){
				orders.setGoalId(order.getGoalId());
				orders.setGoalName(order.getGoalName());
			}
			//Inserting Order Items
			Set<OrderItem> orderItems = new HashSet<>();
			OrderItem orderItem = new OrderItem();
			orderItem.setOrders(orders);
			orderItem.setLastcreate(new Date());
			orderItem.setLastupdate(new Date());
			orderItem.setTimereleased(new Date());
			orderItem.setTimeshiped(new Date());
			orderItem.setProductprice(orderItemsData.getProductprice());
			orderItem.setQuantity(1);
			orderItem.setScheme(scheme);
			orderItem.setStatus("AP");
			orderItem.setTaxamount(new BigDecimal(0));
			orderItem.setTotaladjustment(new BigDecimal(0));
			orderItem.setField2(orderItemsData.getField2());
			orderItem.setField3(orderItemsData.getField3());
			/*
				String sipStartDate = null;
				String getDayOfSip = null;
				String dayOfSip = addToCartDTO.getDayOfSip();
				if (dayOfSip.length() == 3) {
					String day = dayOfSip.substring(0, 1);
					getDayOfSip = "0" + day;
				}
				if (dayOfSip.length() == 4) {
					getDayOfSip = dayOfSip.substring(0, 2);
				}
				sipStartDate = GoForWealthPRSUtil.getSipStartDate(getDayOfSip);
			*/
			orderItem.setDescription(orderItemsData.getDescription());
			orderItems.add(orderItem);
			orders.setOrderItems(orderItems);
			orders.setLastupdate(new Date());
			orderRepository.save(orders);
			addToCartDTO.setOrderId(orders.getOrdersId());
			String status = "success";
			addToCartDTO.setStatus(status);
		} catch (Exception e) {
			e.printStackTrace();
			addToCartDTO.setStatus("Falied");
		}
		return addToCartDTO;
	}

	public String insertNewPaymentRecordForDebitedInstallment(Orders newInsertedOrderDetail,OrderItem orderItem, User user){
		Ppcpayinst ppcpayinst = new Ppcpayinst();
		ppcpayinst.setAmount(orderItem.getProductprice());
		ppcpayinst.setAccountNo(user.getBankDetails().getAccountNo());
		ppcpayinst.setOrders(newInsertedOrderDetail);
		ppcpayinst.setState("AP");
		ppcpayinst.setApprovingamount(orderItem.getProductprice());
		ppcpayinst.setApprovedamount(new BigDecimal(0.0));
		ppcpayinst.setCreditedamount(new BigDecimal(0.0));
		ppcpayinst.setCreditingamount(orderItem.getProductprice());
		ppcpayinst.setCurrency("INR");
		ppcpayinst.setMarkfordelete(0);
		Ppcpaytran ppcpaytran = new Ppcpaytran();
		ppcpaytran.setPpcpayinst(ppcpayinst);
		ppcpaytran.setProcessedamount(new BigDecimal(0.0));
		ppcpaytran.setState(0);
		ppcpaytranRepository.save(ppcpaytran);
		return "success";
	}

	public PrsDTO checkOrderAndAllotementStatus(User user, String bseOrderId) {
		logger.info("In checkOrderAndAllotementStatus");
		PrsDTO reDto = null;
		String bseUserId = "";
		String memberCode = "";
		String password = "";
		String clientCode = user.getOnboardingStatus().getClientCode();
		List<SystemProperties> systemPropertiesList = systemPropertiesRepository.findAll();
		for (SystemProperties systemProperties : systemPropertiesList) {
			if (systemProperties.getId().getPropertyKey().equals("UserId")) {
				bseUserId = systemProperties.getId().getPropertyValue();
			} else if (systemProperties.getId().getPropertyKey().equals("MemberId")) {
				memberCode = systemProperties.getId().getPropertyValue();
			} else if (systemProperties.getId().getPropertyKey().equals("Password")) {
				password = systemProperties.getId().getPropertyValue();
			}
		}
		CheckOrderStatusRequest checkOrderStatusRequest = new CheckOrderStatusRequest(clientCode, "", "", "", "",memberCode,bseOrderId, "", "", password, "","ALL", "", "", bseUserId);
		Object orderStatusResponse = orderMfService.getOrderStatus(checkOrderStatusRequest);
		System.out.println(orderStatusResponse);
		try {
			JSONObject jsonObject = new JSONObject(orderStatusResponse.toString());
			String checkStatus = jsonObject.getString("Status");
			if (checkStatus.equals("101")) {
			} else {
				JSONArray jSONArray = jsonObject.getJSONArray("OrderDetails");
				int size = jSONArray.length();
				for (int i = 0; i < size; i++) {
					JSONObject JSONObject5 = new JSONObject(jSONArray.get(i).toString());
					reDto = new PrsDTO();
					if (JSONObject5.getString("OrderRemarks").equals("ALLOTMENT DONE")&& JSONObject5.getString("OrderStatus").equals("VALID")) {
						reDto.setOrderStatus(JSONObject5.getString("OrderStatus"));
						reDto.setRemarks(JSONObject5.getString("OrderRemarks"));
						reDto.setResponse("success");
					}else if(JSONObject5.getString("OrderStatus").equals("INVALID")){
						reDto.setOrderStatus(JSONObject5.getString("OrderStatus"));
						reDto.setRemarks(JSONObject5.getString("OrderRemarks"));
						reDto.setResponse("Failed");
					}
				}
			}
		} catch (Exception e) {
		}
		logger.info("Out checkOrderAndAllotementStatus");
		return reDto;
	}

	public boolean updateSchemeName(List<SchemeUploadRequest> schemeUploadRequestList){
		logger.info("In updateSchemeName()");
		List<Scheme> schemeList = new ArrayList<Scheme>();
		int count = 0;
		try {
			List<Scheme> schemeLIST = schemeRepository.findAll();
			for(SchemeUploadRequest schemeUpload : schemeUploadRequestList) {
				for (Scheme scheme : schemeLIST) {
					if (scheme.getSchemeCode().equals(schemeUpload.getSchemeCode())) {
						scheme.setSchemeName(schemeUpload.getSchemeName());
						scheme.setSchemeType(schemeUpload.getSchemeType());
						schemeList.add(scheme);
						break;
					}
				}
			}
			schemeRepository.saveAll(schemeList);
		} catch (Exception e) {
			e.printStackTrace();
			return false;
		}
		logger.info("Out updateSchemeName()");
		return true;
	}

	@Override
	public void updateInvalidOrderStatusWithIO() {
		String MemberId = "";
		String bseUserId = "";
		String password = "";
		List<SystemProperties> systemPropertiesList = systemPropertiesRepository.findAll();
		for (SystemProperties systemProperties : systemPropertiesList) {
			if (systemProperties.getId().getPropertyKey().equals("MemberId")) {
				MemberId = systemProperties.getId().getPropertyValue();
			}else if (systemProperties.getId().getPropertyKey().equals("UserId")) {
				bseUserId = systemProperties.getId().getPropertyValue();
			} else if (systemProperties.getId().getPropertyKey().equals("Password")) {
				password = systemProperties.getId().getPropertyValue();
			} 
		}
		List<Orders> orderList = orderRepository.getOrderWithStatusM();
		if(!orderList.isEmpty()){
			for (Orders orders : orderList) {
				if(orders.getStatus().equals("M")){
					OrderItem orderItem = orderItemRepository.getOrderItemByOrderId(orders.getOrdersId());
					String clientCode = orders.getUser().getOnboardingStatus().getClientCode();
					if(orders.getField2() != null && !orders.getField2().equals("N") && orders.getType().equals("SIP")){
						CheckOrderStatusRequest checkOrderStatusRequest = new CheckOrderStatusRequest(clientCode, "", "", "", "",MemberId,orders.getBseOrderId(), "", "", password, "","ALL", "", "", bseUserId);
						getAndUpdateInvalidOrderStatus(checkOrderStatusRequest, orders, orderItem);
					}/*else if(orders.getType().equals("LUMPSUM")){
						CheckOrderStatusRequest checkOrderStatusRequest = new CheckOrderStatusRequest(clientCode, "", "", "", "",MemberId,orders.getBseOrderId(), "", "", password, "","ALL", "", "", bseUserId);
						getAndUpdateInvalidOrderStatus(checkOrderStatusRequest, orders, orderItem);
					}*/
				}
			}
		}
	}

	public String getAndUpdateInvalidOrderStatus(CheckOrderStatusRequest checkOrderStatusRequest,Orders orders,OrderItem orderItem){
		String response ="Failed";
		Object orderStatusResponse = orderMfService.getOrderStatus(checkOrderStatusRequest);
		try {
			JSONObject jsonObject = new JSONObject(orderStatusResponse.toString());
			String checkStatus = jsonObject.getString("Status");
			if (checkStatus.equals("101")) {
			} else {
				JSONArray jSONArray = jsonObject.getJSONArray("OrderDetails");
				int size = jSONArray.length();
				for (int i = 0; i < size; i++) {
					JSONObject JSONObject5 = new JSONObject(jSONArray.get(i).toString());
					if (JSONObject5.getString("OrderStatus").equals("INVALID")) {
						String remark = JSONObject5.getString("OrderRemarks");
						orders.setStatus(GoForWealthPRSConstants.INVALID_ORDER_STATUS);
						orders.setLastupdate(new Date());
						orders.setField3(remark);
						orderItem.setStatus(GoForWealthPRSConstants.INVALID_ORDER_STATUS);
						orderItem.setLastupdate(new Date());
						orderItemRepository.save(orderItem);
						orderRepository.save(orders);
						response = "success";
					}
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return response;
	}

	@Override
	public List<FundSchemeDTO> getAllRecommendedScheme() {
		List<FundSchemeDTO> fundSchemeDTOList = new ArrayList<>();
		List<TopSchemes> topSchemesList = topSchemeRepository.getAllSchemes();
		List<Scheme> recommendedSchemeList = schemeRepository.getAllRecommendedSchemes();
		for (Scheme recommendedScheme : recommendedSchemeList) {
			if(recommendedScheme != null){
				FundSchemeDTO fundSchemeDTO = new FundSchemeDTO();
				int sequence = 0;
				for (TopSchemes topSchemes : topSchemesList) {
					if(recommendedScheme.getSchemeCode().equals(topSchemes.getSchemeCode())){
						sequence = topSchemes.getSequence();
						break;
					}
				}
				fundSchemeDTO.setSchemeId(recommendedScheme.getSchemeId());
				fundSchemeDTO.setSchemeCode(recommendedScheme.getSchemeCode());
				fundSchemeDTO.setPlan(recommendedScheme.getSchemePlan());
				fundSchemeDTO.setSchemeName(recommendedScheme.getSchemeName());
				fundSchemeDTO.setSchemeType(recommendedScheme.getSchemeType());
				fundSchemeDTO.setSchemeLaunchDate(recommendedScheme.getStartDate());
				fundSchemeDTO.setSchemeEndDate(recommendedScheme.getEndDate());
				fundSchemeDTO.setAmcSchemeCode(recommendedScheme.getAmcSchemeCode());
				fundSchemeDTO.setAmfiCode(recommendedScheme.getAmfiCode());
				fundSchemeDTO.setBenchmarkCode(recommendedScheme.getBenchmarkCode());
				fundSchemeDTO.setFaceValue(recommendedScheme.getFaceValue());
				if(recommendedScheme.getMinSipAmount() != null){
					Double minSipAmountDouble = Double.valueOf(recommendedScheme.getMinSipAmount());
					fundSchemeDTO.setMinSipAmount(String.valueOf(minSipAmountDouble.intValue()));
				}
				fundSchemeDTO.setReturn_(recommendedScheme.getReturnValue());
				fundSchemeDTO.setRisk(recommendedScheme.getRisk());
				fundSchemeDTO.setRtaCode(recommendedScheme.getRtaSchemeCode());
				fundSchemeDTO.setSipAllowed(recommendedScheme.getSipFlag());
				fundSchemeDTO.setMinimumPurchaseAmount(recommendedScheme.getMinimumPurchaseAmount());
				fundSchemeDTO.setMaximumPurchaseAmount(recommendedScheme.getMaximumPurchaseAmount());
				fundSchemeDTO.setStatus(recommendedScheme.getStatus());
				fundSchemeDTO.setSchemeKeyword(recommendedScheme.getKeyword());
				fundSchemeDTO.setAmcCode(recommendedScheme.getAmcCode());
				fundSchemeDTO.setPriority(recommendedScheme.getPriority());
				fundSchemeDTO.setSequence(sequence);
				fundSchemeDTOList.add(fundSchemeDTO);
			}
		}
		Collections.sort(fundSchemeDTOList,new Comparator<FundSchemeDTO>() {
			public int compare(FundSchemeDTO o1, FundSchemeDTO o2){
				return o1.getSequence() - o2.getSequence();
			}
		});
		return fundSchemeDTOList;
	}

	public MaintainCurrentNavCurrentValueForOrders calculateCurrentValueAndCurrentUnit(OrderStatusReportUserdata orderStatusReportUserdata,String type,String currentNav,Integer userId,AllotmentStatusReportUserdata allotmentStatusReportUserdata123){
		SimpleDateFormat sf = new SimpleDateFormat("dd-MM-yyyy HH:mm:ss");
		BigDecimal allotedUnit = new BigDecimal(0);
		BigDecimal investedAmount = new BigDecimal(0);
		BigDecimal currentAmount = new BigDecimal(0);
	    MaintainCurrentNavCurrentValueForOrders m = new MaintainCurrentNavCurrentValueForOrders();
		if(type.equals("SIP")){
			List<OrderStatusReportUserdata> orderStatusReportUserdataList = orderStatusReportUserdataRepository.findBySipRegnNo(orderStatusReportUserdata.getSipRegnNo());
	        for(OrderStatusReportUserdata orderStatusReportUserdata2 : orderStatusReportUserdataList) {
	        	if(orderStatusReportUserdata2.getStatus().equals("AD") || orderStatusReportUserdata2.getStatus().equals("AP") || orderStatusReportUserdata2.getStatus().equals("OD")){
	        		AllotmentStatusReportUserdata alloUserData =allotmentStatusReportUserdataRepository.findByOrderNo(Integer.parseInt(orderStatusReportUserdata2.getOrderNumber()));
					allotedUnit = alloUserData.getAllottedUnit().add(allotedUnit);
				    investedAmount = investedAmount.add(alloUserData.getAllottedAmt());	
				}
			}
	        currentAmount = allotedUnit.multiply(new BigDecimal(currentNav));
	        m.setOrderId(Integer.parseInt(orderStatusReportUserdata.getOrderNumber()));
	        m.setCurrentAmount(currentAmount);
	        m.setSchemeCode(orderStatusReportUserdata.getSchemeCode());
	        m.setUserId(userId);
			m.setClientCode(orderStatusReportUserdata.getClientCode());
			m.setLastNavUpdatedDate(sf.format(new Date()));
			m.setFolioNo(orderStatusReportUserdata.getFolioNo());
			m.setInvestedAmount(investedAmount);
			m.setCurrentUnit(allotedUnit);
			m.setSipRegnNo(orderStatusReportUserdata.getSipRegnNo());
		}else if(type.equals("LUMPSUM")){
			allotedUnit = allotmentStatusReportUserdata123.getAllottedUnit();
		    investedAmount = allotmentStatusReportUserdata123.getAllottedAmt();
		    currentAmount = allotedUnit.multiply(new BigDecimal(currentNav));
	        m.setOrderId(Integer.parseInt(orderStatusReportUserdata.getOrderNumber()));
	        m.setCurrentAmount(currentAmount);
	        m.setSchemeCode(orderStatusReportUserdata.getSchemeCode());
	        m.setUserId(userId);
			m.setClientCode(orderStatusReportUserdata.getClientCode());
			m.setLastNavUpdatedDate(sf.format(new Date()));
			m.setFolioNo(orderStatusReportUserdata.getFolioNo());
			m.setInvestedAmount(investedAmount);
			m.setCurrentUnit(allotedUnit);
			m.setSipRegnNo(0L);
		}
		maintainCurrentNavCurrentValueForOrdersRepository.save(m);
		MaintainCurrentNavCurrentValueForOrders m1 = new MaintainCurrentNavCurrentValueForOrders();
		m1.setCurrentAmount(currentAmount);
		m1.setInvestedAmount(investedAmount);
		m1.setCurrentUnit(allotedUnit);
		return m1;	
	}

	@Override
	public List<RedumptionResponse> getRedumptionDetail(String folioNo , String schemeCode) {
		logger.info("In getRedumptionDetail");
		List<RedumptionResponse> redumptionResponsesList = new ArrayList<RedumptionResponse>();
		List<RedumptionManagement> redumptionManagementsList = redumptionManagementepository.findByFolioNoAndSchemeCode(folioNo , schemeCode);
		if(!redumptionManagementsList.isEmpty()){
			for (RedumptionManagement redumptionManagement : redumptionManagementsList) {
				RedumptionResponse redumptionResponse = new RedumptionResponse();
				redumptionResponse.setFollioNo(redumptionManagement.getFolioNumber());
				redumptionResponse.setRedumptionAmount(redumptionManagement.getRedumptionAmount());
				redumptionResponse.setRedumptionDate(redumptionManagement.getRedumptionDate());
				redumptionResponse.setRedumptionStatus(redumptionManagement.getStatus());
				redumptionResponse.setSchemeName(redumptionManagement.getSchemeName());
				redumptionResponsesList.add(redumptionResponse);
			}
		}
		/*Orders order = orderRepository.findByOrdersId(orderId);
		List<RedumptionResponse> redumptionResponsesList = new ArrayList<RedumptionResponse>(); 
		if(order.getType().equals("SIP")){
			List<RedumptionManagement> redumptionManagementsList = redumptionManagementepository.findBySipRegnId(Long.parseLong(order.getField2()));
			for (RedumptionManagement redumptionManagement : redumptionManagementsList) {
				RedumptionResponse redumptionResponse = new RedumptionResponse();
				redumptionResponse.setFollioNo(redumptionManagement.getFolioNumber());
				redumptionResponse.setRedumptionAmount(redumptionManagement.getRedumptionAmount());
				redumptionResponse.setRedumptionDate(redumptionManagement.getRedumptionDate());
				redumptionResponse.setRedumptionStatus(redumptionManagement.getStatus());
				redumptionResponse.setSchemeName(redumptionManagement.getSchemeName());
				redumptionResponse.setType("SIP");
				redumptionResponsesList.add(redumptionResponse);
			}
		}else{
			List<RedumptionManagement> redumptionManagementsList = redumptionManagementepository.findByBseOrderId(order.getBseOrderId());
			for (RedumptionManagement redumptionManagement : redumptionManagementsList) {
				RedumptionResponse redumptionResponse = new RedumptionResponse();
				redumptionResponse.setFollioNo(redumptionManagement.getFolioNumber());
				redumptionResponse.setRedumptionAmount(redumptionManagement.getRedumptionAmount());
				redumptionResponse.setRedumptionDate(redumptionManagement.getRedumptionDate());
				redumptionResponse.setRedumptionStatus(redumptionManagement.getStatus());
				redumptionResponse.setSchemeName(redumptionManagement.getSchemeName());
				redumptionResponse.setType("LUMPSUM");
				redumptionResponsesList.add(redumptionResponse);
			}*/
		logger.info("Out getRedumptionDetail");
		return redumptionResponsesList;
	}

	@Override
	public List<AddToCartDTO> getActiveSipsList(User user) {
		List<Orders> orderList  = orderRepository.getSipOrderWithUniqueRegnId(user.getUserId());
		List<AddToCartDTO> addToCartDTOsList = new ArrayList<AddToCartDTO>();
		if(!orderList.isEmpty()){
			for(Orders order : orderList){
				if(order.getType().equals("SIP")){
					AddToCartDTO addToCartDTO = new AddToCartDTO();
					addToCartDTO.setOrderId(order.getOrdersId());
					addToCartDTO.setSipDate(order.getOrderItems().iterator().next().getDescription());
					if (order.getField2() != null && !order.getField2().equals("N")) {
						addToCartDTO.setSipRegnId(order.getField2());
						addToCartDTO.setBseOrderId(order.getBseOrderId());
					}else{
						addToCartDTO.setSipRegnId(order.getBseOrderId());
					}
					addToCartDTO.setStatus(order.getStatus());
					addToCartDTO.setSchemeName(order.getOrderItems().iterator().next().getScheme().getSchemeName());
					addToCartDTO.setDayOfSip(order.getOrderItems().iterator().next().getField2());
					/*if(order.getStatus().equals("C") || order.getStatus().equals("AD")){
						MaintainCurrentNavCurrentValueForOrders mcnForOrder = maintainCurrentNavCurrentValueForOrdersRepository.findBySipRegnNo(Long.valueOf(order.getField2()));
						if(mcnForOrder != null){
							addToCartDTO.setAmount(mcnForOrder.getCurrentAmount().setScale(2, RoundingMode.HALF_UP).doubleValue());
						}else{
							addToCartDTO.setAmount(order.getOrderItems().iterator().next().getProductprice().setScale(2, RoundingMode.HALF_UP).doubleValue());
						}
					}else{*/
						addToCartDTO.setAmount(order.getOrderItems().iterator().next().getProductprice().setScale(2, RoundingMode.HALF_UP).doubleValue());
					//}
					addToCartDTO.setMinimumSipAmount(order.getOrderItems().iterator().next().getProductprice().setScale(2, RoundingMode.HALF_UP).intValue());
					addToCartDTOsList.add(addToCartDTO);
				}
			}
		}
		return addToCartDTOsList;
	}

	@Override
	public void updateOrderAndAllotementWithStatusAP() {
		logger.info("In updateOrderAndAllotementWithStatusAP");
		SimpleDateFormat sf = new SimpleDateFormat("dd-MM-yyyy HH:mm:ss");
		List<OrderStatusReportUserdata> osrUserData = orderStatusReportUserdataRepository.getDetailWithStatusAP();
		if(!osrUserData.isEmpty()){
			for (OrderStatusReportUserdata orderStatusReportUserdata : osrUserData) {
				int userId = Integer.valueOf(orderStatusReportUserdata.getClientCode().substring(3));
				User user = userRepository.findByUserId(userId);
				//int settType = Integer.valueOf(orderStatusReportUserdata.getSettType().substring(1));//ex. value = 3//for T3
				StoreConf allotTime = storeConfRepository.findByKeyword(GoForWealthPRSConstants.ALLOTEMENT_TIME);
				int allotementTime =Integer.valueOf(allotTime.getKeywordValue()); 
				String dueDate = orderStatusReportUserdata.getDate();//ex. value = 15/01/2019	
				int diffInDays = GoForWealthPRSUtil.getDiffFromTwoDatesExceptSatAndSun(dueDate);//15/01/2019-23/01/2019  value == 8
				if(diffInDays>=allotementTime){
					PrsDTO prsDto = checkOrderAndAllotementStatus(user, orderStatusReportUserdata.getOrderNumber());
					AllotmentStatusReportUserdata allotStatusReportUserData = allotmentStatusReportUserdataRepository.findByOrderNo(Integer.valueOf(orderStatusReportUserdata.getOrderNumber()));
					Orders order  =orderRepository.findByBseOrderId(orderStatusReportUserdata.getOrderNumber());
					OrderItem orderItem = orderItemRepository.getOrderItemByOrderId(order.getOrdersId());
					List<Ppcpayinst> ppcpayinst1 = ppcpayinstRepository.findByOrderIdWithStatusAP(order.getOrdersId());
					if(prsDto.getResponse().equals("success")){
						orderStatusReportUserdata.setStatus("AD");
						allotStatusReportUserData.setStatus("AD");
						order.setStatus("AD");
						order.setLastupdate(new Date());
						orderItem.setStatus("AD");
						orderStatusReportUserdataRepository.save(orderStatusReportUserdata);
						allotmentStatusReportUserdataRepository.save(allotStatusReportUserData);
						orderRepository.save(order);
						orderItemRepository.save(orderItem);
						if(!ppcpayinst1.isEmpty()){
							for (Ppcpayinst ppcpayinst2 : ppcpayinst1) {
								ppcpayinst2.setState("AD");
								Ppcpaytran ppcpaytran = ppcpayinst2.getPpcpaytran();
								ppcpaytran.setState(1);
								ppcpayinstRepository.save(ppcpayinst2);
								ppcpaytranRepository.save(ppcpaytran);
								break;
							}
						}
					}else if(prsDto.getResponse().equals("Failed")){
						BigDecimal allotedUnit = new BigDecimal(0);
						BigDecimal investedAmount = new BigDecimal(0);
						orderStatusReportUserdata.setStatus("AF");
						allotStatusReportUserData.setStatus("AF");
						order.setStatus("AF");
						order.setLastupdate(new Date());
						orderItem.setStatus("AF");
						orderStatusReportUserdata.setOrderStatus(prsDto.getOrderStatus());
						orderStatusReportUserdata.setOrderRemarks(prsDto.getRemarks());
						orderStatusReportUserdataRepository.save(orderStatusReportUserdata);
						allotmentStatusReportUserdataRepository.save(allotStatusReportUserData);
						orderRepository.save(order);
						orderItemRepository.save(orderItem);
						if(!ppcpayinst1.isEmpty()){
							for (Ppcpayinst ppcpayinst2 : ppcpayinst1) {
								ppcpayinst2.setState("AF");
								Ppcpaytran ppcpaytran = ppcpayinst2.getPpcpaytran();
								ppcpaytran.setState(0);
								ppcpayinstRepository.save(ppcpayinst2);
								ppcpaytranRepository.save(ppcpaytran);
								break;
							}
						}
						
                       /* MaintainCurrentNavCurrentValueForOrders mcnOrderStatus = maintainCurrentNavCurrentValueForOrdersRepository.findBySipRegnNo(orderStatusReportUserdata.getSipRegnNo());
						List<OrderStatusReportUserdata> orderStatusReportUserdataList = orderStatusReportUserdataRepository.findBySipRegnNo(orderStatusReportUserdata.getSipRegnNo());
				        for (OrderStatusReportUserdata orderStatusReportUserdata2 : orderStatusReportUserdataList) {
							if(orderStatusReportUserdata2.getStatus().equals("AD") || orderStatusReportUserdata2.getStatus().equals("AP")){
								AllotmentStatusReportUserdata alloUserData =allotmentStatusReportUserdataRepository.findByOrderNo(Integer.parseInt(orderStatusReportUserdata2.getOrderNumber()));
								allotedUnit = alloUserData.getAllottedUnit().add(allotedUnit);
							    investedAmount = investedAmount.add(alloUserData.getAllottedAmt());	
							}
						}
				        Scheme scheme = schemeRepository.findBySchemeCode(orderStatusReportUserdata.getSchemeCode());
				        currentAmount = allotedUnit.multiply(new BigDecimal(scheme.getCurrentNav()));
				        mcnOrderStatus.setCurrentAmount(currentAmount);
				        mcnOrderStatus.setCurrentUnit(allotedUnit);
				        mcnOrderStatus.setInvestedAmount(investedAmount);
				        maintainCurrentNavCurrentValueForOrdersRepository.save(mcnOrderStatus);*/
				
						ConsolidatedPortfollio conFollio = consolidatedFollioRepository.
								getDetailByFollioAndSchemeCode(orderStatusReportUserdata.getFolioNo(), orderStatusReportUserdata.getSchemeCode());
						Scheme scheme = schemeRepository.findBySchemeCode(orderStatusReportUserdata.getSchemeCode());
						String currentNav = "0.0";
						if(scheme != null && scheme.getCurrentNav() != null){
							currentNav = scheme.getCurrentNav();
						}
						List<AllotmentStatusReportUserdata> allotmentStatusReportUserdatas = allotmentStatusReportUserdataRepository.
								getAllotementByFollioNoAndSchemeCode(orderStatusReportUserdata.getFolioNo(), orderStatusReportUserdata.getSchemeCode());
						if(allotmentStatusReportUserdatas != null){
							for (AllotmentStatusReportUserdata allotmentStatusReportUserdata2 : allotmentStatusReportUserdatas) {
									if(allotmentStatusReportUserdata2.getStatus().equals("AP") || allotmentStatusReportUserdata2.getStatus().equals("AD")){
										allotedUnit = allotmentStatusReportUserdata2.getAllottedUnit().add(allotedUnit);
									    investedAmount = investedAmount.add(allotmentStatusReportUserdata2.getAllottedAmt());
									}
						
							}
							conFollio.setAllotedUnit(allotedUnit);
							conFollio.setInvestedAmount(investedAmount);
							conFollio.setCurrentAmount(allotedUnit.multiply(new BigDecimal(currentNav)));
							conFollio.setLastNavUpdatedDate(sf.format(new Date()));
							consolidatedFollioRepository.save(conFollio);
					}
				}
			}
			
		}
		}
		logger.info("Out updateOrderAndAllotementWithStatusAP");
	}

	@Override
	public void sendReportToUserAfterAllotement(String reportType) {
		String response = null;
		SimpleDateFormat sf = new SimpleDateFormat("dd-MM-yyyy HH:mm:ss");
		List<AllotmentStatusReportUserdata> allotmentStatusReportUserdatasList = null;
		if(reportType.equals("D")){
			allotmentStatusReportUserdatasList = allotmentStatusReportUserdataRepository.getAllotWithEmailStatusZeroAndStatusNotAP(); //group by based on client code
		}else if(reportType.equals("M")){
			allotmentStatusReportUserdatasList = allotmentStatusReportUserdataRepository.getAllAllotedSchemeWithStatusADGroupBy(); //group by based on client code
		}
		if(!allotmentStatusReportUserdatasList.isEmpty()){
			for (AllotmentStatusReportUserdata allotmentStatusReportUserdata : allotmentStatusReportUserdatasList) {
				List<EveryAllotementReportSchemeData> everyAllotementReportSchemeData = new ArrayList<EveryAllotementReportSchemeData>();
				EveryAllotementReportData evryAllotReportData = new EveryAllotementReportData();
				if(reportType.equals("M")){
					List<MaintainCurrentNavCurrentValueForOrders> mcnList = maintainCurrentNavCurrentValueForOrdersRepository.findByClientCode(allotmentStatusReportUserdata.getClientCode());
					if(!mcnList.isEmpty()){
						int userId = Integer.valueOf(allotmentStatusReportUserdata.getClientCode().substring(3));
						User user = userRepository.findByUserId(userId);
						evryAllotReportData.setCustName(user.getFirstName()+" "+user.getLastName());
						evryAllotReportData.setEmail(user.getEmail());
						evryAllotReportData.setMobile(user.getMobileNumber());
						evryAllotReportData.setNominee(user.getBankDetails().getNomineeName());
						try {
							evryAllotReportData.setPan(encryptUserDetail.decrypt(user.getPanDetails().getPanNo()).toUpperCase());
						} catch (Exception e) {
							e.printStackTrace();
						}
						evryAllotReportData.setReportType("M");
						for (MaintainCurrentNavCurrentValueForOrders mcnOrderData : mcnList) {
							EveryAllotementReportSchemeData earUserData = new EveryAllotementReportSchemeData();
							Scheme scheme = schemeRepository.findBySchemeCode(mcnOrderData.getSchemeCode());
							earUserData.setNav(new BigDecimal(scheme.getCurrentNav()).setScale(2, RoundingMode.HALF_UP));
							earUserData.setInvestedAmount(mcnOrderData.getInvestedAmount().setScale(2, RoundingMode.HALF_UP));
							earUserData.setMarketValue(mcnOrderData.getCurrentAmount().setScale(2, RoundingMode.HALF_UP));
							earUserData.setNavDate(scheme.getNavDate());
							earUserData.setSchemeName(scheme.getSchemeName());
							earUserData.setUnit(mcnOrderData.getCurrentUnit().setScale(2, RoundingMode.HALF_UP));
							if(mcnOrderData.getSipRegnNo()!=0){
								earUserData.setSchemeType("SIP");
								earUserData.setSipRegnNo(mcnOrderData.getSipRegnNo());
								List<Orders> order = orderRepository.getOrderListByRegnIdInDescOrder(String.valueOf(mcnOrderData.getSipRegnNo()));
								if(!order.isEmpty()){
									for (Orders orders : order) {
										OrderItem orderItem = orderItemRepository.getOrderItemByOrderId(orders.getOrdersId());
										earUserData.setSipAmount(orderItem.getProductprice().setScale(2, RoundingMode.HALF_UP));
										earUserData.setSipDate(orderItem.getField2());
										List<OrderStatusReportUserdata> osrUserData = orderStatusReportUserdataRepository.findBySipRegnNo(mcnOrderData.getSipRegnNo());
										if(!osrUserData.isEmpty()){
											for (OrderStatusReportUserdata orderStatusReportUserdata : osrUserData) {
												earUserData.setStartDate(orderStatusReportUserdata.getSipregnDate());
												break;
											}
										}
										break;
									}
								}
							}else{
								earUserData.setSchemeType("LUMPSUM");
								earUserData.setOrderNumber(mcnOrderData.getOrderId());
							}
							everyAllotementReportSchemeData.add(earUserData);
						}
						evryAllotReportData.setEveryAllotementReportSchemeData(everyAllotementReportSchemeData);
						response = mailReportToUser(evryAllotReportData);
						System.out.println("Response === "+response);
					}
				}else if(reportType.equals("D")){
					List<AllotmentStatusReportUserdata> asrUserData = allotmentStatusReportUserdataRepository.getAllotWithClientCode(allotmentStatusReportUserdata.getClientCode());
					if(!asrUserData.isEmpty()){
						int userId = Integer.valueOf(allotmentStatusReportUserdata.getClientCode().substring(3));
						User user = userRepository.findByUserId(userId);
						evryAllotReportData.setCustName(user.getFirstName()+" "+user.getLastName());
						evryAllotReportData.setEmail(user.getEmail());
						evryAllotReportData.setMobile(user.getMobileNumber());
						evryAllotReportData.setNominee(user.getBankDetails().getNomineeName());
						try {
							evryAllotReportData.setPan(encryptUserDetail.decrypt(user.getPanDetails().getPanNo()).toUpperCase());
						} catch (Exception e) {
							e.printStackTrace();
						}
						evryAllotReportData.setReportType("D");
						for (AllotmentStatusReportUserdata allotmentStatusReportUserdata2 : asrUserData) {
							EveryAllotementReportSchemeData earUserData = new EveryAllotementReportSchemeData();
							earUserData.setOrderNumber(allotmentStatusReportUserdata2.getOrderNo());
							earUserData.setUnit(allotmentStatusReportUserdata2.getAllottedUnit().setScale(2, RoundingMode.HALF_UP));
							earUserData.setNav(allotmentStatusReportUserdata2.getAllottedNav().setScale(2, RoundingMode.HALF_UP));
							if(allotmentStatusReportUserdata2.getSipRegnNo() !=0){
								ConsolidatedPortfollio conFolio = consolidatedFollioRepository.getDetailByFollioAndSchemeCode(allotmentStatusReportUserdata2.getFolioNo(), allotmentStatusReportUserdata2.getSchemeCode());
								if(conFolio != null){
									earUserData.setInvestedAmount(conFolio.getCurrentAmount().setScale(2, RoundingMode.HALF_UP));
								}else{
									BigDecimal allotedUnit = new BigDecimal(0);
									BigDecimal investedAmount = new BigDecimal(0);
									BigDecimal currentAmount = new BigDecimal(0);
									String currentNav = "0.0";
									Scheme scheme = schemeRepository.findBySchemeCode(allotmentStatusReportUserdata.getSchemeCode());
									if(scheme != null){
										currentNav = scheme.getCurrentNav() != null?scheme.getCurrentNav():"0.0";
									}
									List<AllotmentStatusReportUserdata> arusData = allotmentStatusReportUserdataRepository.getAllotementByFollioNoAndSchemeCode(allotmentStatusReportUserdata.getFolioNo(),allotmentStatusReportUserdata.getSchemeCode());
									if(arusData != null){
										ConsolidatedPortfollio coPortfollio = new ConsolidatedPortfollio();
										for (AllotmentStatusReportUserdata allotmentStatusReportUserdata1 : arusData) {
											allotedUnit = allotmentStatusReportUserdata1.getAllottedUnit().add(allotedUnit);
										    investedAmount = investedAmount.add(allotmentStatusReportUserdata1.getAllottedAmt());
										}
										coPortfollio.setCurrentAmount(allotedUnit.multiply(new BigDecimal(currentNav)));
										coPortfollio.setAllotedUnit(allotedUnit);
										coPortfollio.setInvestedAmount(investedAmount);
										coPortfollio.setClientCode(allotmentStatusReportUserdata.getClientCode());
										coPortfollio.setFolioNo(allotmentStatusReportUserdata.getFolioNo());
										coPortfollio.setLastNavUpdatedDate(sf.format(new Date()));
										//coPortfollio.setOrderId(orderId);
										coPortfollio.setSchemeCode(allotmentStatusReportUserdata.getSchemeCode());
										//coPortfollio.setSipRegNo(sipRegNo);
										//coPortfollio.setStatus(status);
										//coPortfollio.setStatusCode(statusCode);
										coPortfollio.setUserId(userId);
										consolidatedFollioRepository.save(coPortfollio);
									}
									earUserData.setInvestedAmount(allotedUnit.multiply(new BigDecimal(currentNav)).setScale(2, RoundingMode.HALF_UP));
							
								}
								earUserData.setSchemeType("SIP");
								earUserData.setSipRegnNo(allotmentStatusReportUserdata2.getSipRegnNo());
								List<Orders> order = orderRepository.getOrderListByRegnIdInDescOrder(String.valueOf(allotmentStatusReportUserdata2.getSipRegnNo()));
								if(!order.isEmpty()){
									for (Orders orders : order) {
										OrderItem orderItem = orderItemRepository.getOrderItemByOrderId(orders.getOrdersId());
										earUserData.setSipAmount(orderItem.getProductprice().setScale(2, RoundingMode.HALF_UP));
										earUserData.setSipDate(orderItem.getField2());
										break;
									}
								}
							}else{
								earUserData.setSchemeType("LUMPSUM");
								earUserData.setInvestedAmount(allotmentStatusReportUserdata2.getAllottedAmt().setScale(2, RoundingMode.HALF_UP));
							}
							Scheme scheme = schemeRepository.findBySchemeCode(allotmentStatusReportUserdata2.getSchemeCode());
							earUserData.setNavDate(scheme.getNavDate());
							earUserData.setSchemeName(scheme.getSchemeName());
							everyAllotementReportSchemeData.add(earUserData);
						}
						evryAllotReportData.setEveryAllotementReportSchemeData(everyAllotementReportSchemeData);
						response = mailReportToUser(evryAllotReportData);
				    	System.out.println("Response === "+response);
				    	if(response.equals("success")){
				    		for (EveryAllotementReportSchemeData earUserData : everyAllotementReportSchemeData) {
								OrderStatusReportUserdata osrUserData = orderStatusReportUserdataRepository.getOrderStatusByOrderId(String.valueOf(earUserData.getOrderNumber()));
								AllotmentStatusReportUserdata asrUserData1 = allotmentStatusReportUserdataRepository.findByOrderNo(earUserData.getOrderNumber());
								if(osrUserData != null && asrUserData1 != null){
									osrUserData.setAllotMailStatus(1);
									asrUserData1.setAllotMailStatus(1);
									orderStatusReportUserdataRepository.save(osrUserData);
									allotmentStatusReportUserdataRepository.save(asrUserData1);
								}
							}
				    	}
					}
				}
			}
		}
	}

	private String mailReportToUser(EveryAllotementReportData evryAllotReportData) {
		try {
			logger.info("Report Detail Request:" + GoForWealthAdminUtil.getJsonFromObject(evryAllotReportData,null));
		} catch (JsonProcessingException e) {
			e.printStackTrace();
		}
		String response=null;
		StoreConf nodeApiUrlForReport = storeConfRepository.findByKeyword(GoForWealthPRSConstants.NODE_API_FOR_MAIL_REPORT);
		String url = nodeApiUrlForReport.getKeywordValue();
		//String url = "http://192.168.0.121:4243/generateStatement/consolidatedStatement";
		HttpHeaders httpHeaders = new HttpHeaders();
		httpHeaders.setContentType(MediaType.APPLICATION_JSON);
		try {
			/*
			TrustStrategy acceptingTrustStrategy = (X509Certificate[] chain, String authType) -> true;
			SSLContext sslContext = org.apache.http.ssl.SSLContexts.custom().loadTrustMaterial(null, acceptingTrustStrategy).build();
			SSLConnectionSocketFactory csf = new SSLConnectionSocketFactory(sslContext);
			CloseableHttpClient httpClient = HttpClients.custom().setSSLSocketFactory(csf).build();
			HttpComponentsClientHttpRequestFactory requestFactory = new HttpComponentsClientHttpRequestFactory();
			requestFactory.setHttpClient(httpClient);
			RestTemplate restTemplate = new RestTemplate(requestFactory);
			//RestTemplate restTemplate = new RestTemplate();
			HttpEntity<?> requestEntity = new HttpEntity<Object>(GoForWealthAdminUtil.getJsonFromObject(evryAllotReportData,null),httpHeaders);
			//ResponseEntity<String> responseEntity = restTemplate.postForEntity(url, requestEntity, String.class);
			ResponseEntity<String> responseEntity = restTemplate.postForEntity(url,requestEntity,String.class);
			*/
			RestTemplate restTemplate = new RestTemplate();
			HttpEntity<?> requestEntity = new HttpEntity<Object>(GoForWealthAdminUtil.getJsonFromObject(evryAllotReportData,null),httpHeaders);
			ResponseEntity<String> responseEntity = restTemplate.postForEntity(url, requestEntity, String.class);
			if (responseEntity != null) {
				response = responseEntity.getBody();
			}else{
				response = "Failed";
			}
		}catch (Exception ex) {
			logger.error("Exception Occured : ", ex);
			ex.printStackTrace();
		} 
		logger.info("Out invokeURL()");
		return response;
	}

	
	@Override
	public boolean userInvestmentGoals(UserGoalResponse userGoalResponse, Integer userId) {
		boolean resp = false;
		try{
			
			if(userGoalResponse != null){
				List<UserGoalResponse> suUserGoalResponsesList = new ArrayList<UserGoalResponse>();
				
				ConsolidatedPortfollio consolidatedPortfollio = consolidatedFollioRepository.
						getDetailByFollioAndSchemeCode(userGoalResponse.getFollioNo(), userGoalResponse.getSchemeCode());
				if(consolidatedPortfollio != null){
					consolidatedPortfollio.setGoalId(userGoalResponse.getGoalId());
					consolidatedPortfollio.setGoalName(userGoalResponse.getGoalName());
					consolidatedFollioRepository.save(consolidatedPortfollio);
				}
				
				
				List<OrderStatusReportUserdata> orderStatusReportUserdatas = orderStatusReportUserdataRepository.
						getAllotementByFollioNoAndSchemeCode(userGoalResponse.getFollioNo(), userGoalResponse.getSchemeCode());
				
				
				
				List<AllotmentStatusReportUserdata> allotmentStatusReportUserdatas = allotmentStatusReportUserdataRepository.
						getAllotementByFollioNoAndSchemeCode(userGoalResponse.getFollioNo(), userGoalResponse.getSchemeCode());
				if(!allotmentStatusReportUserdatas.isEmpty()){
					
					for (AllotmentStatusReportUserdata allotmentStatusReportUserdata : allotmentStatusReportUserdatas) {
						allotmentStatusReportUserdata.setGoalId(userGoalResponse.getGoalId());
						allotmentStatusReportUserdata.setGoalName(userGoalResponse.getGoalName());
					}
					
					allotmentStatusReportUserdataRepository.saveAll(allotmentStatusReportUserdatas);
				}
				
				if(!orderStatusReportUserdatas.isEmpty()){
					for (OrderStatusReportUserdata orderStatusReportUserdata : orderStatusReportUserdatas) {
						orderStatusReportUserdata.setGoalId(userGoalResponse.getGoalId());
						orderStatusReportUserdata.setGoalName(userGoalResponse.getGoalName());
						Orders order = orderRepository.findByBseOrderId(orderStatusReportUserdata.getOrderNumber());
						UserGoalResponse userGoalResponse2 = null;
						if(order != null){
						    userGoalResponse2 = new UserGoalResponse();
							userGoalResponse2.setAction(userGoalResponse.getAction());
							userGoalResponse2.setFollioNo(userGoalResponse.getFollioNo());
							userGoalResponse2.setGoalId(userGoalResponse.getGoalId());
							userGoalResponse2.setGoalName(userGoalResponse.getGoalName());
							userGoalResponse2.setSchemeCode(userGoalResponse.getSchemeCode());
							userGoalResponse2.setOrderId(order.getOrdersId());
							userGoalResponse2.setOldGoalId(userGoalResponse.getOldGoalId());
						}
						suUserGoalResponsesList.add(userGoalResponse2);
					}
					orderStatusReportUserdataRepository.saveAll(orderStatusReportUserdatas);
				}
				resp = userInvestmentGoals1(suUserGoalResponsesList,userId);
			}
		}catch(Exception e){
			resp = false;
			e.printStackTrace();
		}
		return resp;
	}
	
	
	public boolean userInvestmentGoals1(List<UserGoalResponse> userGoalResponse2, Integer userId) {
		boolean result = false;
		for (UserGoalResponse userGoalResponse : userGoalResponse2) {
			try{
				Orders orders = orderRepository.findByOrdersId(userGoalResponse.getOrderId());
				/*if(userGoalResponse.getAction().equals("Add")){
					if(orders != null){
						if(orders.getType().equals("SIP")){
							if(orders.getField2() != null && !orders.getField2().equals("N")){
								List<Orders> ordersList = orderRepository.findByField2(orders.getField2());
								GoalOrderItems goalOrderItemss =  goalOrderItemsRepository.findByGoalOrderItemId(userGoalResponse.getGoalId());
								if(ordersList.size()>0){
									for (Orders orders2 : ordersList) {
										orders2.setGoalId(userGoalResponse.getGoalId());
										orders2.setGoalName(goalOrderItemss.getDescription());
										orderRepository.save(orders2);
									}
								}
							}
						}else{
							GoalOrderItems goalOrderItemss =  goalOrderItemsRepository.findByGoalOrderItemId(userGoalResponse.getGoalId());
							orders.setGoalId(userGoalResponse.getGoalId());
							orders.setGoalName(goalOrderItemss.getDescription());
							orderRepository.save(orders);
						}
						GoalOrderItems goalOrderItems =  goalOrderItemsRepository.findByGoalOrderItemId(userGoalResponse.getGoalId());
						goalOrderItems.setField1(1);
						goalOrderItemsRepository.save(goalOrderItems);
					}
				}*/
				if(userGoalResponse.getAction().equals("Modify")){
					if(orders != null){
						GoalOrderItems goalOrderItemss =  goalOrderItemsRepository.findByGoalOrderItemId(userGoalResponse.getGoalId());
						if(orders.getType().equals("SIP")){
							if(orders.getField2() != null && !orders.getField2().equals("N")){
								List<Orders> ordersList = orderRepository.findByField2(orders.getField2());
								if(ordersList.size()>0){
									for (Orders orders2 : ordersList) {
										orders2.setGoalId(userGoalResponse.getGoalId());
										orders2.setGoalName(goalOrderItemss.getDescription());
										orderRepository.save(orders2);
									}
								}
							}
						}else{
							orders.setGoalId(userGoalResponse.getGoalId());
							orders.setGoalName(goalOrderItemss.getDescription());
							orderRepository.save(orders);
						}
						if(userGoalResponse.getGoalId() != 0){
							if(userGoalResponse.getOldGoalId() != 0){
								List<Orders> order =orderRepository.isOrderAssociated(userGoalResponse.getOldGoalId());
								if(order.isEmpty()){
									GoalOrderItems goalOrderItems =  goalOrderItemsRepository.findByGoalOrderItemId(userGoalResponse.getOldGoalId());
									goalOrderItems.setField1(0);
									goalOrderItemsRepository.save(goalOrderItems);
								}
								
							}
							GoalOrderItems goalOrderItemsObj =  goalOrderItemsRepository.findByGoalOrderItemId(userGoalResponse.getGoalId());
							goalOrderItemsObj.setField1(1);
							goalOrderItemsRepository.save(goalOrderItemsObj);
						}
					}
				}
				if(userGoalResponse.getAction().equals("Remove")){
					if(orders != null){
						if(orders.getType().equals("SIP")){
							if(orders.getField2() != null || !orders.getField2().equals("N")){
								List<Orders> ordersList = orderRepository.findByField2(orders.getField2());
								if(ordersList.size()>0){
									for (Orders orders2 : ordersList) {
										orders2.setGoalId(null);
										orders2.setGoalName(null);
										orderRepository.save(orders2);
									}
								}
							}
						}else{
							orders.setGoalId(null);
							orders.setGoalName(null);
							orderRepository.save(orders);
						}
						if(orderRepository.findByGoalId(userGoalResponse.getGoalId()).isEmpty()){
							GoalOrderItems goalOrderItems =  goalOrderItemsRepository.findByGoalOrderItemId(userGoalResponse.getGoalId());
							goalOrderItems.setField1(0);
							goalOrderItemsRepository.save(goalOrderItems);
						}
					}
				}
				result = true;
			}catch(Exception ex){
				logger.error("Exception Occured : " + ex);
				result = false;
			}
		}
		return result;
	}

	@Override
	public void updateBillerStatus(List<PrsDTO> prsDTO) {
		if(!prsDTO.isEmpty()){
			for (PrsDTO prsDTO2 : prsDTO) {
				OnboardingStatus onboardingStatus = onboardingStatusRepository.findByClientCode(prsDTO2.getClientCode());
				if(onboardingStatus != null){
					onboardingStatus.setBillerStatus(prsDTO2.getMandateStatus());
					if (prsDTO2.getMandateStatus().equals("REJECTED") 
							|| prsDTO2.getMandateStatus().equals("INITIAL REJECTION")
							|| prsDTO2.getMandateStatus().equals("REJECTION AT NPCI PRIOR TO DESTINATION BANK")
							|| prsDTO2.getMandateStatus().equals("REJECTED BY SPONSOR BANK") 
							|| prsDTO2.getMandateStatus().equals("CANCELLED BY INVESTOR")
							|| prsDTO2.getMandateStatus().equals("RETURNED BY EXCHANGE")
						) {
						onboardingStatus.setIsipMandateStatus(101);
					}				
					onboardingStatusRepository.save(onboardingStatus);
				}
			}
		}
	}

	@Override
	public List<PrsDTO> getBillerStatus() {
		List<PrsDTO> prsDTOsList = new ArrayList<PrsDTO>();
		List<User> user = userRepository.getUserWithOnboardingCompleted();
		if(!user.isEmpty()){
			for (User user2 : user) {
				if(user2.getOnboardingStatus().getIsipMandateNumber()!= null && (user2.getOnboardingStatus().getBillerStatus()==null ||!user2.getOnboardingStatus().getBillerStatus().equals("APPROVED"))){
					PrsDTO prsDTO = new PrsDTO();
					prsDTO.setBillerId(user2.getOnboardingStatus().getIsipMandateNumber());
					prsDTOsList.add(prsDTO);
					logger.info("Client Code === " + user2.getOnboardingStatus().getClientCode());
				}
			}
		}
		return prsDTOsList;
	}

	@Override
	public void savePreviousDateToDBForSipInstallment() {
		logger.info("In savePreviousDateToDBForSipInstallment()");
		String sipRegnDate ="";
		SimpleDateFormat dateFormat = new SimpleDateFormat("dd MMM yyyy");
		SimpleDateFormat dateFormat1 = new SimpleDateFormat("yyyy-MM-dd");
		String prevDate = GoForWealthPRSUtil.getPreviousDate(new Date());
		Date date = null;
		try {
			date = dateFormat1.parse(prevDate);
		    sipRegnDate = dateFormat.format(date);
		} catch (ParseException e1) {
			e1.printStackTrace();
		}
		StoreConf storeConf = storeConfRepository.findByKeyword(GoForWealthPRSConstants.PREVIOUD_DATE_FOR_SIP_INSTALLMENT_UPDATE);
		storeConf.setKeywordValue(sipRegnDate);
		storeConfRepository.save(storeConf);
		logger.info("Out savePreviousDateToDBForSipInstallment()");
	}

	@Override
	public void updateOrderRedemptionStatusForFollio() {
		logger.info("In updateOrderRedemptionStatusForFollio");
		List<RedumptionManagement>redumptionManagementsList = redumptionManagementepository.getDataWithStatusR();
		if(!redumptionManagementsList.isEmpty()){
			for (RedumptionManagement redumptionManagement : redumptionManagementsList) {
				List<AllotmentStatusReportUserdata> allotmentStatusReportUserdatas = allotmentStatusReportUserdataRepository.getAllotementByFollioNoAndSchemeCode(redumptionManagement.getFolioNumber(), redumptionManagement.getSchemeCode());

				if(!allotmentStatusReportUserdatas.isEmpty()){
					for (AllotmentStatusReportUserdata allotmentStatusReportUserdata : allotmentStatusReportUserdatas) {
						allotmentStatusReportUserdata.setRedumptionRemark(redumptionManagement.getRedumptionRemark());
						if(redumptionManagement.getRedumptionType().equals("PR")){
							if(allotmentStatusReportUserdata.getStatus().equals("AP") 
									|| allotmentStatusReportUserdata.getStatus().equals("AD") 
									|| allotmentStatusReportUserdata.getStatus().equals("OD")
									|| allotmentStatusReportUserdata.getStatus().equals("PR")){

								allotmentStatusReportUserdata.setStatus(GoForWealthPRSConstants.PARTIAL_ORDER_REDEEM_STATUS);
								
							}
						}else if(redumptionManagement.getRedumptionType().equals("FR")){
							if(allotmentStatusReportUserdata.getStatus().equals("AP") 
									|| allotmentStatusReportUserdata.getStatus().equals("AD") 
									|| allotmentStatusReportUserdata.getStatus().equals("OD")
									|| allotmentStatusReportUserdata.getStatus().equals("PR")){

								allotmentStatusReportUserdata.setStatus(GoForWealthPRSConstants.ORDER_REDEEM_STATUS);
							}
						}
						allotmentStatusReportUserdataRepository.save(allotmentStatusReportUserdata);
					}

				}
				List<OrderStatusReportUserdata> orderStatusReportUserdata = orderStatusReportUserdataRepository.getAllotementByFollioNoAndSchemeCode(redumptionManagement.getFolioNumber(), redumptionManagement.getSchemeCode());

				if(!orderStatusReportUserdata.isEmpty()){
					for (OrderStatusReportUserdata orderStatusReportUserdata2 : orderStatusReportUserdata) {
						orderStatusReportUserdata2.setRedumptionRemark(redumptionManagement.getRedumptionRemark());
						if(redumptionManagement.getRedumptionType().equals("PR")){
							if(orderStatusReportUserdata2.getStatus().equals("AP")
									|| orderStatusReportUserdata2.getStatus().equals("AD") 
									|| orderStatusReportUserdata2.getStatus().equals("OD")
									|| orderStatusReportUserdata2.getStatus().equals("PR")){

								orderStatusReportUserdata2.setStatus(GoForWealthPRSConstants.PARTIAL_ORDER_REDEEM_STATUS);
							}
						}else if(redumptionManagement.getRedumptionType().equals("FR")){
							if(orderStatusReportUserdata2.getStatus().equals("AP") 
									|| orderStatusReportUserdata2.getStatus().equals("AD") 
									|| orderStatusReportUserdata2.getStatus().equals("OD")
									|| orderStatusReportUserdata2.getStatus().equals("PR")){

								orderStatusReportUserdata2.setStatus(GoForWealthPRSConstants.ORDER_REDEEM_STATUS);
							}
						}
						orderStatusReportUserdataRepository.save(orderStatusReportUserdata2);
					}
				}
				
				redumptionManagement.setStatus("C");
				redumptionManagementepository.save(redumptionManagement);
				ConsolidatedPortfollio consolidatedPortfollio = consolidatedFollioRepository.getDetailByFollioAndSchemeCode(redumptionManagement.getFolioNumber(), redumptionManagement.getSchemeCode());
				if(consolidatedPortfollio != null){
					consolidatedPortfollio.setCurrentAmount(consolidatedPortfollio.getCurrentAmount().subtract(redumptionManagement.getRedumptionAmount()));
					consolidatedPortfollio.setAllotedUnit(consolidatedPortfollio.getAllotedUnit().subtract(redumptionManagement.getRedumptionUnit()));
					consolidatedFollioRepository.save(consolidatedPortfollio);
				}
			}
		}
		logger.info("Out updateOrderRedemptionStatusForFollio");
		 
	}

	@Override
	public List<PortFolioDataDTO> getConsoliDatedFollio(Integer userId) {
		/** New Code Change Start **/
		List<PortFolioDataDTO> portFolioDataDTOListFinal = new ArrayList<>();
		List<PortFolioDataDTO> consolidatedFolioRecordList = new ArrayList<>();
		/** New Code Change End **/
		List<PortFolioDataDTO> portFolioDataDTOList = new ArrayList<PortFolioDataDTO>();
		String goalName = null;
		int goalId = 0;
		User user = userRepository.findByUserId(userId);
		String ClientCode = user.getOnboardingStatus().getClientCode();
		List<AllotmentStatusReportUserdata> allotmentStatusReportUserdatas = allotmentStatusReportUserdataRepository.getDetailByClientCodeGroupByFollioAndSchemeCode(ClientCode);
		if (!allotmentStatusReportUserdatas.isEmpty()) {
			SimpleDateFormat sf = new SimpleDateFormat("dd-MM-yyyy HH:mm:ss");
			for (AllotmentStatusReportUserdata allotmentStatusReportUserdata : allotmentStatusReportUserdatas) {
				BigDecimal allotedUnit = new BigDecimal(0);
				BigDecimal investedAmount = new BigDecimal(0);
				BigDecimal currentAmount = new BigDecimal(0);
				PortFolioDataDTO portFolioDataDTO = new PortFolioDataDTO();
				portFolioDataDTO.setFollioNo(allotmentStatusReportUserdata.getFolioNo());
				Scheme scheme = schemeRepository.findBySchemeCode(allotmentStatusReportUserdata.getSchemeCode());
				String currentNav = "0.0";
				if(scheme != null){
					currentNav = scheme.getCurrentNav();
					portFolioDataDTO.setSchemeType(scheme.getSchemeType());
					portFolioDataDTO.setSchemeName(scheme.getSchemeName());
				}else{
					portFolioDataDTO.setSchemeType("Not Available");
					portFolioDataDTO.setSchemeName("Not Available");
				}
				ConsolidatedPortfollio consolidatedPortfollio = consolidatedFollioRepository.getDetailByFollioAndSchemeCode(allotmentStatusReportUserdata.getFolioNo(),allotmentStatusReportUserdata.getSchemeCode());
				if(consolidatedPortfollio != null){
					investedAmount = consolidatedPortfollio.getInvestedAmount();
					allotedUnit = consolidatedPortfollio.getAllotedUnit();
					currentAmount = consolidatedPortfollio.getCurrentAmount();
					if(consolidatedPortfollio.getGoalId() != null && consolidatedPortfollio.getGoalId() != 0 && consolidatedPortfollio.getGoalName() != null){
						goalId = consolidatedPortfollio.getGoalId();
						goalName = consolidatedPortfollio.getGoalName();
					}
					
				}else{
					List<AllotmentStatusReportUserdata> arusData = allotmentStatusReportUserdataRepository.getAllotementByFollioNoAndSchemeCode(allotmentStatusReportUserdata.getFolioNo(),allotmentStatusReportUserdata.getSchemeCode());
					if(arusData != null){
						goalId = 0;
						goalName =  null;
						ConsolidatedPortfollio coPortfollio = new ConsolidatedPortfollio();
						for (AllotmentStatusReportUserdata allotmentStatusReportUserdata2 : arusData) {
							allotedUnit = allotmentStatusReportUserdata2.getAllottedUnit().add(allotedUnit);
						    investedAmount = investedAmount.add(allotmentStatusReportUserdata2.getAllottedAmt());
						    if(allotmentStatusReportUserdata2.getGoalId() != null && allotmentStatusReportUserdata2.getGoalId() != 0 && allotmentStatusReportUserdata2.getGoalName() != null){
								goalId = allotmentStatusReportUserdata2.getGoalId();
								goalName = allotmentStatusReportUserdata2.getGoalName();
							}
						}
						coPortfollio.setCurrentAmount(allotedUnit.multiply(new BigDecimal(currentNav)));
						coPortfollio.setAllotedUnit(allotedUnit);
						coPortfollio.setInvestedAmount(investedAmount);
						coPortfollio.setClientCode(allotmentStatusReportUserdata.getClientCode());
						coPortfollio.setFolioNo(allotmentStatusReportUserdata.getFolioNo());
						coPortfollio.setLastNavUpdatedDate(sf.format(new Date()));
						if(goalId != 0 && goalName != null){
							coPortfollio.setGoalId(goalId);
							coPortfollio.setGoalName(goalName);
						}
						
						//coPortfollio.setOrderId(orderId);
						coPortfollio.setSchemeCode(allotmentStatusReportUserdata.getSchemeCode());
						//coPortfollio.setSipRegNo(sipRegNo);
						//coPortfollio.setStatus(status);
						//coPortfollio.setStatusCode(statusCode);
						coPortfollio.setUserId(userId);
						consolidatedFollioRepository.save(coPortfollio);
					}
				}
				List<AllotmentStatusReportUserdata> arusData1 = allotmentStatusReportUserdataRepository.getAllotementByFollioNoAndSchemeCode(allotmentStatusReportUserdata.getFolioNo(),allotmentStatusReportUserdata.getSchemeCode());
				List<String> orderDate = new ArrayList<String>();
				if(arusData1 != null){
					for (AllotmentStatusReportUserdata allotmentStatusReportUserdata3 : arusData1) {
						orderDate.add(allotmentStatusReportUserdata3.getOrderDate());
					}
				}
				if(orderDate.size()>1){
					LocalDate startDate = GoForWealthPRSUtil.getMinDateFromListOfDate(orderDate);
					portFolioDataDTO.setStatedOn(startDate.toString());
				}else if(orderDate.size() != 0){
					portFolioDataDTO.setStatedOn(orderDate.get(0).toString());
				}
				portFolioDataDTO.setAmcCode(scheme.getAmcCode());
				if (scheme.getNavDate() != null) {
					portFolioDataDTO.setCurrentDate(scheme.getNavDate());
				}
				portFolioDataDTO.setStatus(allotmentStatusReportUserdata.getStatus());
				
				if (goalId !=0 && goalName != null) {
					portFolioDataDTO.setGoalId(goalId);
					portFolioDataDTO.setGoalName(goalName);
				}
				
				///************************************Start Code to check is redumption allowed**********************//
				RedumptionResponse redumptionResponse = isRedumptionAllowed(allotmentStatusReportUserdata.getFolioNo() ,allotmentStatusReportUserdata.getSchemeCode());
				if(redumptionResponse != null){
					portFolioDataDTO.setTotalAmount(redumptionResponse.getTotalAmount());
					portFolioDataDTO.setMinimumRedumptionAmount(new BigDecimal(redumptionResponse.getMinimumRedumptionAmount()));
					portFolioDataDTO.setIsRedumptionAllowed(redumptionResponse.getIsRedumptionAllowed());
					
				}
				///************************************End Code to check is redumption allowed**********************//
				
				
				portFolioDataDTO.setCurrentNav((new BigDecimal(currentNav)));
                portFolioDataDTO.setCurrentAmount(currentAmount.setScale(2,RoundingMode.HALF_UP));
				portFolioDataDTO.setAllotedUnit(allotedUnit);
				portFolioDataDTO.setInvestedAmount(investedAmount.setScale(2, RoundingMode.HALF_UP));
				portFolioDataDTO.setSchemeCode(scheme.getSchemeCode());
				
				if (user.getOnboardingStatus().getMandateNumber() != null && !user.getOnboardingStatus().getEnachStatus().equals("APPROVED")) {
					portFolioDataDTO.setEnachEnable(true);
				} else {
					portFolioDataDTO.setEnachEnable(false);
				}
				if (user.getOnboardingStatus().getIsipMandateNumber() != null && !user.getOnboardingStatus().getBillerStatus().equals("APPROVED")) {
					portFolioDataDTO.setBillerEnable(true);
				} else {
					portFolioDataDTO.setBillerEnable(false);
				}
				List<IsipAllowedBankList> isipAllowedbankList = isipAllowedBankListRepository.findAll();
				boolean isFound = false;
				if (!isipAllowedbankList.isEmpty()) {
					for (IsipAllowedBankList isipAllowedBankList2 : isipAllowedbankList) {
						if (user.getBankDetails().getBankName().contains(isipAllowedBankList2.getBankName().toUpperCase())) {
							isFound = true;
							break;
						}
					}
				}
				if(scheme.getSipDates() != null && !scheme.getSipDates().equals(""))
					portFolioDataDTO.setAvailableSipDate(scheme.getSipDates().split(","));
				portFolioDataDTO.setIsipAllowed(isFound ? true : false);
				portFolioDataDTO.setMinLumpsumAmount(new BigDecimal(scheme.getMinimumPurchaseAmount()));
				portFolioDataDTO.setMinSipAmount(new BigDecimal(scheme.getMinSipAmount()));
				portFolioDataDTO.setSipAllowed(scheme.getSipFlag().equals("Y") ? true : false);
				portFolioDataDTO.setLumpsumAllowed(scheme.getPurchaseAllowed().equals("Y") ? true : false);
				portFolioDataDTO.setMinAdditionalAmount(new BigDecimal(scheme.getAdditionalPurchaseAmount()));
				
				String schemeCode = getSchemeCodeWithL1(scheme.getRtaSchemeCode(), scheme.getIsin(), scheme.getSchemeCode());/*scheme.getSchemeCode() + "-L1";*/
				Scheme schemeWithL1 = schemeRepository.findBySchemeCodeWithL1(schemeCode);
				if (schemeWithL1 != null) {
					if (!schemeWithL1.getMaximumPurchaseAmount().equals("0.000")) {
						portFolioDataDTO.setMaximumPurchaseAmount(schemeWithL1.getMaximumPurchaseAmount());
					} else {
						portFolioDataDTO.setMaximumPurchaseAmount("999999999.000");
					}
				} else {
					portFolioDataDTO.setMaximumPurchaseAmount(scheme.getMaximumPurchaseAmount());
				}
				
				DecimalFormat df = new DecimalFormat("##");
				if (schemeWithL1 != null) {
					if (schemeWithL1.getSipMaximumInstallmentAmount() != null && !schemeWithL1.getSipMaximumInstallmentAmount().toString().equals("0.00000")) {
						portFolioDataDTO.setSipMaxInstallmentAmount(df.format((Math.round(Double.valueOf(schemeWithL1.getSipMaximumInstallmentAmount().toString()) * 100.0) / 100.0)));
					} else {
						if (scheme.getSipMaximumInstallmentAmount() != null) {
							portFolioDataDTO.setSipMaxInstallmentAmount(df.format((Math.round(Double.valueOf(scheme.getSipMaximumInstallmentAmount().toString()) * 100.0) / 100.0)));
						}
					}
				} else {
					if (scheme.getSipMaximumInstallmentAmount() != null) {
						portFolioDataDTO.setSipMaxInstallmentAmount(df.format((Math.round(Double.valueOf(scheme.getSipMaximumInstallmentAmount().toString()) * 100.0) / 100.0)));
					}
				}
				
				
				portFolioDataDTOList.add(portFolioDataDTO);
			}
			// Get data from consolidated_portfollio table based on clientCode.
			List<ConsolidatedPortfollio> consolidatedPortfollioList = consolidatedFollioRepository.findByClientCode(user.getOnboardingStatus().getClientCode());
			for (ConsolidatedPortfollio consolidatedPortfollio : consolidatedPortfollioList) {
				boolean isFound = false;
				for (PortFolioDataDTO portFolioDataDto : portFolioDataDTOList) {
					if((consolidatedPortfollio.getFolioNo().equals(portFolioDataDto.getFollioNo())) && (consolidatedPortfollio.getSchemeCode().equals(portFolioDataDto.getSchemeCode()))){
						isFound = true;
						break;
					}
				}
				if(!isFound){
					TransferIn transferIn = transferInRepository.findBytransferInId(consolidatedPortfollio.getTranferInId());
					if(transferIn != null){
						Scheme scheme = schemeRepository.getBySchemeCode(consolidatedPortfollio.getSchemeCode());
						PortFolioDataDTO portFolioDataDtoObj = new PortFolioDataDTO();
						portFolioDataDtoObj.setFollioNo(consolidatedPortfollio.getFolioNo());
						portFolioDataDtoObj.setAllotedUnit(consolidatedPortfollio.getAllotedUnit());
						portFolioDataDtoObj.setInvestedAmount(consolidatedPortfollio.getInvestedAmount());
						portFolioDataDtoObj.setCurrentAmount(consolidatedPortfollio.getCurrentAmount());
						portFolioDataDtoObj.setBillerEnable(portFolioDataDTOList.get(0).isBillerEnable());
						portFolioDataDtoObj.setEnachEnable(portFolioDataDTOList.get(0).isEnachEnable());
						portFolioDataDtoObj.setIsipAllowed(portFolioDataDTOList.get(0).isIsipAllowed());
						portFolioDataDtoObj.setLumpsumAllowed(scheme.getPurchaseAllowed().equals("Y") ? true : false);
						portFolioDataDtoObj.setSipAllowed(scheme.getSipFlag().equals("Y") ? true : false);
						portFolioDataDtoObj.setMinLumpsumAmount(new BigDecimal(scheme.getMinimumPurchaseAmount()));
						portFolioDataDtoObj.setMinSipAmount(new BigDecimal(scheme.getMinSipAmount()));
						portFolioDataDtoObj.setSchemeCode(scheme.getSchemeCode());
						portFolioDataDtoObj.setSchemeName(scheme.getSchemeName());
						portFolioDataDtoObj.setSchemeType(scheme.getSchemeType());
						portFolioDataDtoObj.setAmcCode(scheme.getAmcCode());
						
						///************************************Start Code to check is redumption allowed**********************//
						RedumptionResponse redumptionResponse = isRedumptionAllowed(consolidatedPortfollio.getFolioNo() ,consolidatedPortfollio.getSchemeCode());
						if(redumptionResponse != null){
							portFolioDataDtoObj.setTotalAmount(redumptionResponse.getTotalAmount());
							portFolioDataDtoObj.setMinimumRedumptionAmount(new BigDecimal(redumptionResponse.getMinimumRedumptionAmount()));
							portFolioDataDtoObj.setIsRedumptionAllowed(redumptionResponse.getIsRedumptionAllowed());
						}
						///************************************End Code to check is redumption allowed**********************//
						
						if (scheme.getCurrentNav() != null && scheme.getNavDate() != null && (scheme.getSipDates() != null && !scheme.getSipDates().equals(""))){
							portFolioDataDtoObj.setCurrentDate(scheme.getNavDate());
							portFolioDataDtoObj.setCurrentNav(new BigDecimal(scheme.getCurrentNav()));
							portFolioDataDtoObj.setAvailableSipDate(scheme.getSipDates().split(","));
						}
						//portFolioDataDtoObj.setGoalId(0);
						//portFolioDataDtoObj.setGoalName("");
						portFolioDataDtoObj.setStatus("TI");
						try{
							SimpleDateFormat sdf = new SimpleDateFormat("dd-MMM-yyyy");
							SimpleDateFormat sf1 = new SimpleDateFormat("dd/MM/yyyy");
							Date date = sf1.parse(transferIn.getStartedOn().replace('\\','/'));
							portFolioDataDtoObj.setStatedOn(sdf.format(date));
						}catch(Exception ex){
							ex.printStackTrace();
						}
						consolidatedFolioRecordList.add(portFolioDataDtoObj);
					}
				}
			}
		}
		portFolioDataDTOListFinal.addAll(portFolioDataDTOList);
		portFolioDataDTOListFinal.addAll(consolidatedFolioRecordList);
		return portFolioDataDTOListFinal;
	}

	@Override
	public List<ResponseDto> checkAmcForFolio(String amcCode, User user) {
		List<ResponseDto> folioNoList = new ArrayList<>();
		List<ConsolidatedPortfollio> consolidatedPortfollioList = consolidatedFollioRepository.findByUserId(user.getUserId());
		if(!consolidatedPortfollioList.isEmpty()){
			for (ConsolidatedPortfollio consolidatedPortfollio : consolidatedPortfollioList) {
				ResponseDto responseDto = new ResponseDto();
				Scheme scheme = schemeRepository.getBySchemeCode(consolidatedPortfollio.getSchemeCode());
				if(scheme!=null && scheme.getAmcCode().equals(amcCode)){
					
						responseDto.setFollioNo(consolidatedPortfollio.getFolioNo());
				}
				if(consolidatedPortfollio.getGoalId() != 0){
					responseDto.setGoalId(consolidatedPortfollio.getGoalId());
					responseDto.setGoalName(consolidatedPortfollio.getGoalName());
				}
				if(responseDto != null){
					folioNoList.add(responseDto);
				}
			}
		}
		return folioNoList;
	}

	@Override
	public String redeemOrderByFollio(Integer userId, AddToCartDTO addToCartDTO) {
		String response = "failure";
		User user = userRepository.getOne(userId);
		OrderUniqueRef orderUniqueRef = orderUniqueRefRepository.getOne(1);
		String uniqueRefNo = orderUniqueRef.getOrderUniqueRefNo();
		long refNo = Long.valueOf(uniqueRefNo);
		refNo = refNo + 1;
		orderUniqueRef.setOrderUniqueRefNo(refNo + "");
		orderUniqueRefRepository.save(orderUniqueRef);
		String schemeCode = addToCartDTO.getSchemeCode();
		Scheme scheme = schemeRepository.findBySchemeCode(schemeCode);
		String schemeName = scheme.getSchemeName();
		String amount = String.valueOf(addToCartDTO.getAmount());
		OrderEntryRequest orderEntryRequest = new OrderEntryRequest();
		GetPasswordRequest getPasswordRequest = new GetPasswordRequest();
		List<SystemProperties> systemPropertiesList = systemPropertiesRepository.findAll();
		for (SystemProperties systemProperties : systemPropertiesList) {
			if (systemProperties.getId().getPropertyKey().equals("UserId")) {
				getPasswordRequest.setUserId(systemProperties.getId().getPropertyValue());
			} else if (systemProperties.getId().getPropertyKey().equals("MemberId")) {
				getPasswordRequest.setMemberId(systemProperties.getId().getPropertyValue());
			} else if (systemProperties.getId().getPropertyKey().equals("Password")) {
				getPasswordRequest.setPassword(systemProperties.getId().getPropertyValue());
			}
		}
		OtpGenerator otpGenerator = new OtpGenerator(5, true);
		String passKey = otpGenerator.generateOTP();
		getPasswordRequest.setPassKey(passKey);
		String passwordResponse = orderMfService.getPassword(getPasswordRequest);
		String[] res = passwordResponse.split("\\|");
		if (res[0].equals("100")) {
			orderEntryRequest.setTransCode("NEW");
			orderEntryRequest.setUniqueRefNo(uniqueRefNo);
			orderEntryRequest.setOrderId("");
			orderEntryRequest.setUserID(getPasswordRequest.getUserId());
			orderEntryRequest.setMemberId(getPasswordRequest.getMemberId());
			orderEntryRequest.setClientCode(user.getOnboardingStatus().getClientCode());
			orderEntryRequest.setSchemeCd(schemeCode);
			orderEntryRequest.setBuySell("R");
			orderEntryRequest.setBuySellType("FRESH");
			orderEntryRequest.setDpTxn("P");
			orderEntryRequest.setQty("");
			if(addToCartDTO.getType().equals("PR")){
				orderEntryRequest.setOrderVal(String.valueOf(addToCartDTO.getAmount()));
				orderEntryRequest.setAllRedeem("N");	
			}else if(addToCartDTO.getType().equals("FR")){
				orderEntryRequest.setOrderVal("");
				orderEntryRequest.setAllRedeem("Y");
			}
			orderEntryRequest.setFolioNo(addToCartDTO.getFolioNo());
			orderEntryRequest.setRemarks("");
			orderEntryRequest.setKycStatus("Y");
			orderEntryRequest.setRefNo("");
			orderEntryRequest.setSubBrCode("");
			orderEntryRequest.setEuin("");
			orderEntryRequest.setEuinVal("N");
			orderEntryRequest.setMinRedeem("Y");
			orderEntryRequest.setDpc("N");
			orderEntryRequest.setIpAdd("");
			orderEntryRequest.setPassword(res[1]);
			orderEntryRequest.setPassKey(getPasswordRequest.getPassKey());
			orderEntryRequest.setParma1("");
			orderEntryRequest.setParma2("");
			orderEntryRequest.setParma3("");
			OrderEntryResponse orderEntryResponse = orderMfService.getOrderEntryForRedeem(orderEntryRequest);
			if (orderEntryResponse.getOrderId() != null && !orderEntryResponse.getBseRemarks().contains("FAILED")) {
				response ="success";
				RedumptionManagement redumptionManagement = new RedumptionManagement();
				redumptionManagement.setRedeemOrderId(orderEntryResponse.getOrderId());
				redumptionManagement.setFolioNumber(addToCartDTO.getFolioNo());
				redumptionManagement.setRedumptionAmount(new BigDecimal(addToCartDTO.getAmount()));
				SimpleDateFormat sdf = new SimpleDateFormat("dd MMM yyyy");
				String currentDate = sdf.format(new Date());
				redumptionManagement.setRedumptionDate(currentDate);
				redumptionManagement.setSchemeName(schemeName);
				redumptionManagement.setStatus("P");
				redumptionManagement.setSchemeCode(addToCartDTO.getSchemeCode());
				redumptionManagement.setIsOrderAvailable(addToCartDTO.getStatus());
				redumptionManagement.setUserId(userId);
				redumptionManagement.setRedumptionUnit(new BigDecimal(addToCartDTO.getAmount()/Double.valueOf(scheme.getCurrentNav())));
				redumptionManagement.setRedumptionType(addToCartDTO.getType());
				redumptionManagement.setRedumptionRemark(orderEntryResponse.getBseRemarks());
				if(addToCartDTO.getStatus().equals("N")){
					TransferIn transferIn = transferInRepository.findByFolioNumberAndSchemeCode(addToCartDTO.getFolioNo() , addToCartDTO.getSchemeCode());
					if(transferIn != null){
						transferIn.setAvailableUnit(transferIn.getAvailableUnit().subtract(new BigDecimal(addToCartDTO.getAmount()/Double.valueOf(scheme.getCurrentNav()))));
						transferIn.setRedumptionStatus("P");
						transferInRepository.save(transferIn);
					}
				}
				redumptionManagementepository.save(redumptionManagement);
				
				String emailSubject = "";
				String emailContent = "";
				DecimalFormat df = new DecimalFormat("##");
				String amount1 = df.format((Math.round(Double.valueOf(amount) * 100.0) / 100.0));
				try {
					String userName = user.getFirstName() == null ? user.getUsername(): user.getFirstName() + " " + user.getLastName();
						emailSubject = messageSource.getMessage("sip.redeem.mailSubject", null, Locale.ENGLISH);
						emailContent = messageSource.getMessage("sip.redeem.mailBody",new String[] { userName, orderEntryResponse.getBseRemarks(), schemeName, amount1, addToCartDTO.getFolioNo() }, Locale.ENGLISH);
					
					mailUtility.baselineExample(user.getEmail(), emailSubject, emailContent);
				} catch (NoSuchMessageException | IOException e) {
					e.printStackTrace();
				}
			}
			else {
				String emailSubject = "";
				String emailContent = "";
				DecimalFormat df = new DecimalFormat("##");
				String amount1 = df.format((Math.round(Double.valueOf(amount) * 100.0) / 100.0));
				try {
					String userName = user.getFirstName() == null ? user.getUsername(): user.getFirstName() + " " + user.getLastName();
						emailSubject = messageSource.getMessage("sip.redeem.fail.mailSubject", null, Locale.ENGLISH) + " "+ response;
						emailContent = messageSource.getMessage("sip.redeem.fail.mailBody",new String[] { userName, orderEntryResponse.getBseRemarks() , schemeName, amount1, addToCartDTO.getFolioNo() }, Locale.ENGLISH);
					
					mailUtility.baselineExample(user.getEmail(), emailSubject, emailContent);
				} catch (NoSuchMessageException | IOException e) {
					e.printStackTrace();
				}
			}
			
		}
		return response;
	}

	@Override
	public void updateNavFromNode() {
		logger.info("In updateNavFromNode()");
		String response=null;
		StoreConf nodeApiUrlForUpdateNav = storeConfRepository.findByKeyword(GoForWealthPRSConstants.NODE_API_FOR_UPDATE_NAV);
		String url = nodeApiUrlForUpdateNav.getKeywordValue();
		HttpHeaders httpHeaders = new HttpHeaders();
		try {
			/*
			TrustStrategy acceptingTrustStrategy = (X509Certificate[] chain, String authType) -> true;
			SSLContext sslContext = org.apache.http.ssl.SSLContexts.custom().loadTrustMaterial(null, acceptingTrustStrategy).build();
			SSLConnectionSocketFactory csf = new SSLConnectionSocketFactory(sslContext);
			CloseableHttpClient httpClient = HttpClients.custom().setSSLSocketFactory(csf).build();
			HttpComponentsClientHttpRequestFactory requestFactory = new HttpComponentsClientHttpRequestFactory();
			requestFactory.setHttpClient(httpClient);
			RestTemplate restTemplate = new RestTemplate(requestFactory);
			//RestTemplate restTemplate = new RestTemplate();
			HttpEntity<?> requestEntity = new HttpEntity<Object>(GoForWealthAdminUtil.getJsonFromObject(evryAllotReportData,null),httpHeaders);
			//ResponseEntity<String> responseEntity = restTemplate.postForEntity(url, requestEntity, String.class);
			ResponseEntity<String> responseEntity = restTemplate.postForEntity(url,requestEntity,String.class);
			*/
			RestTemplate restTemplate = new RestTemplate();
			HttpEntity<?> requestEntity = new HttpEntity<Object>(httpHeaders);
			ResponseEntity<String> responseEntity = restTemplate.getForEntity(url,null);
			if (responseEntity != null) {
				response = responseEntity.getBody();
			}else{
				response = "Failed";
			}
		}catch (Exception ex) {
			logger.error("Exception Occured : ", ex);
			ex.printStackTrace();
		}
		logger.info("Out updateNavFromNode() With Response : " + response);
	}

	public void updateNavToNode(NavUpdateRequest navUpdateRequest) {
		logger.info("In updateNavToNode()");
		String response=null;
		StoreConf nodeApiUrlForReport = storeConfRepository.findByKeyword(GoForWealthPRSConstants.NODE_API_TO_UPDATE_NAV_TO_NODE);
		String url = nodeApiUrlForReport.getKeywordValue();
		HttpHeaders httpHeaders = new HttpHeaders();
		httpHeaders.setContentType(MediaType.APPLICATION_JSON);
		try {
			/*
			logger.info("Nav Detail Request:" + GoForWealthAdminUtil.getJsonFromObject(navUpdateRequest,null));
			TrustStrategy acceptingTrustStrategy = (X509Certificate[] chain, String authType) -> true;
			SSLContext sslContext = org.apache.http.ssl.SSLContexts.custom().loadTrustMaterial(null, acceptingTrustStrategy).build();
			SSLConnectionSocketFactory csf = new SSLConnectionSocketFactory(sslContext);
			CloseableHttpClient httpClient = HttpClients.custom().setSSLSocketFactory(csf).build();
			HttpComponentsClientHttpRequestFactory requestFactory = new HttpComponentsClientHttpRequestFactory();
			requestFactory.setHttpClient(httpClient);
			RestTemplate restTemplate = new RestTemplate(requestFactory);
			//RestTemplate restTemplate = new RestTemplate();
			HttpEntity<?> requestEntity = new HttpEntity<Object>(GoForWealthAdminUtil.getJsonFromObject(evryAllotReportData,null),httpHeaders);
			//ResponseEntity<String> responseEntity = restTemplate.postForEntity(url, requestEntity, String.class);
			ResponseEntity<String> responseEntity = restTemplate.postForEntity(url,requestEntity,String.class);
			*/
			RestTemplate restTemplate = new RestTemplate();
			HttpEntity<?> requestEntity = new HttpEntity<Object>(GoForWealthAdminUtil.getJsonFromObject(navUpdateRequest,null),httpHeaders);
			ResponseEntity<String> responseEntity = restTemplate.postForEntity(url, requestEntity, String.class);
			if (responseEntity != null) {
				response = responseEntity.getBody();
			}else{
				response = "Failed";
			}
		}catch (Exception ex) {
			logger.error("Exception Occured : ", ex);
			ex.printStackTrace();
		}
		logger.info("Out updateNavToNode() With Response : " + response);
	}

	 public String getSchemeCodeWithL1(String rtaSchemeCode,String isin,String schemeCode){
			String schemeCodeWithL1 = "";
			List<Scheme> schemeList = schemeRepository.findByRtaSchemeCodeAndIsin(rtaSchemeCode,isin);
			if(!schemeList.isEmpty()){
				for (Scheme scheme : schemeList) {
					if(!scheme.getSchemeCode().equals(schemeCode)){
						if(scheme.getSchemeCode().contains(schemeCode)){
							if(scheme.getSchemeCode().endsWith("-L1")) {
								schemeCodeWithL1 = scheme.getSchemeCode();
								break;
							}
						}else if(scheme.getSchemeCode().contains((schemeCode.replace("-","")).trim())){
							if(scheme.getSchemeCode().endsWith("-L1")) {
								schemeCodeWithL1 = scheme.getSchemeCode();
								break;
							}
						}
					}
				}
			}
			return schemeCodeWithL1;
		}

	 @Override
	public AddToCartDTO getOrderDetailsById(Integer orderId) {
		Orders order = orderRepository.getOne(orderId);
		AddToCartDTO addToCartDTO = new AddToCartDTO();
		if(order != null){
			addToCartDTO.setBseOrderId(order.getBseOrderId());
			addToCartDTO.setType(order.getType());
			addToCartDTO.setAmount(order.getOrderItems().iterator().next().getProductprice().doubleValue());
			addToCartDTO.setSchemeName(order.getOrderItems().iterator().next().getScheme().getSchemeName());
		}
		return addToCartDTO;
	}
	 
	 @Override
	public String getNavFromAmfi(List<NavDto> navDtoList) {

		 List<Scheme> schemesList = schemeRepository.findAll();
		 List<Schemes_Map> schemes_MapsList = schemesMapRepository.findAll();
		 if(!schemesList.isEmpty() && !navDtoList.isEmpty() && !schemes_MapsList.isEmpty()){
			 
			 for (NavDto navDto : navDtoList) {

				 for (Schemes_Map schemeMap : schemes_MapsList) {

					 if(navDto.getIsin().equals(schemeMap.getAmfiIsin())){
						 
						 for (Scheme scheme : schemesList) {

							 if(scheme.getSchemeCode().equals(schemeMap.getBseSchemeCode())){
								 scheme.setCurrentNav(navDto.getCurrentNav());
								 scheme.setNavDate(navDto.getNavDate());
								 break;
							 }
						 }
						 break;
					 }
				 }
			 }
			 
			 schemeRepository.saveAll(schemesList);
		 }
		return "success";
	}
}