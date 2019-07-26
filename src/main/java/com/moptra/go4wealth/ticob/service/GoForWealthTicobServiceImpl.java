package com.moptra.go4wealth.ticob.service;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.Writer;
import java.math.BigDecimal;
import java.text.DecimalFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Locale;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.context.NoSuchMessageException;
import org.springframework.stereotype.Service;

import com.moptra.go4wealth.bean.AllotmentStatusReportUserdata;
import com.moptra.go4wealth.bean.ConsolidatedPortfollio;
import com.moptra.go4wealth.bean.IsipAllowedBankList;
import com.moptra.go4wealth.bean.OnboardingStatus;
import com.moptra.go4wealth.bean.OrderItem;
import com.moptra.go4wealth.bean.OrderUniqueRef;
import com.moptra.go4wealth.bean.Orders;
import com.moptra.go4wealth.bean.Ppcpayinst;
import com.moptra.go4wealth.bean.Ppcpaytran;
import com.moptra.go4wealth.bean.Scheme;
import com.moptra.go4wealth.bean.StoreConf;
import com.moptra.go4wealth.bean.SystemProperties;
import com.moptra.go4wealth.bean.TransferIn;
import com.moptra.go4wealth.bean.User;
import com.moptra.go4wealth.prs.common.constant.GoForWealthPRSConstants;
import com.moptra.go4wealth.prs.common.exception.GoForWealthPRSException;
import com.moptra.go4wealth.prs.common.util.EncryptUserDetail;
import com.moptra.go4wealth.prs.model.AddToCartDTO;
import com.moptra.go4wealth.prs.orderapi.OrderMfService;
import com.moptra.go4wealth.prs.orderapi.request.GetPasswordRequest;
import com.moptra.go4wealth.prs.orderapi.request.OrderEntryRequest;
import com.moptra.go4wealth.prs.orderapi.request.XsipOrderEntryRequest;
import com.moptra.go4wealth.prs.orderapi.response.OrderEntryResponse;
import com.moptra.go4wealth.prs.orderapi.response.XsipOrderEntryResponse;
import com.moptra.go4wealth.prs.payment.PaymentService;
import com.moptra.go4wealth.prs.service.GoForWealthFundSchemeService;
import com.moptra.go4wealth.repository.ConsolidatedFollioRepository;
import com.moptra.go4wealth.repository.IsipAllowedBankListRepository;
import com.moptra.go4wealth.repository.OrderRepository;
import com.moptra.go4wealth.repository.OrderUniqueRefRepository;
import com.moptra.go4wealth.repository.SchemeRepository;
import com.moptra.go4wealth.repository.StoreConfRepository;
import com.moptra.go4wealth.repository.SystemPropertiesRepository;
import com.moptra.go4wealth.repository.TransferInRepository;
import com.moptra.go4wealth.repository.UserRepository;
import com.moptra.go4wealth.repository.ppcpaytranRepository;
import com.moptra.go4wealth.ticob.common.constant.GoForWealthTICOBConstants;
import com.moptra.go4wealth.ticob.common.enums.GoForWealthTICOBErrorMessageEnum;
import com.moptra.go4wealth.ticob.model.TransferInRecordDTO;
import com.moptra.go4wealth.ticob.model.TransferInRequestDTO;
import com.moptra.go4wealth.uma.common.util.OtpGenerator;
import com.moptra.go4wealth.util.MailUtility;

@Service
public class GoForWealthTicobServiceImpl implements GoForWealthTicobService{

	@Autowired
	UserRepository userRepository;

	@Autowired
	TransferInRepository transferInRepository;

	@Autowired
	EncryptUserDetail encryptUserDetail;

	@Autowired
	SchemeRepository schemeRepository;
	
	@Autowired
	GoForWealthFundSchemeService goForWealthFundSchemeService;

	@Autowired
	OrderRepository orderRepository;

	@Autowired
	OrderUniqueRefRepository orderUniqueRefRepository;
	
	@Autowired
	SystemPropertiesRepository systemPropertiesRepository;
	
	@Autowired
	StoreConfRepository storeConfRepository;
	
	@Autowired
	MailUtility mailUtility;
	
	@Autowired
	private MessageSource messageSource;

	@Autowired
	OrderMfService orderMfService;

	@Autowired
	ppcpaytranRepository ppcpaytranRepository;

	@Autowired
	IsipAllowedBankListRepository isipAllowedBankListRepository;

	@Autowired
	PaymentService paymentService;
	
	@Autowired
	ConsolidatedFollioRepository consolidatedFollioRepository;

	@Override
	public String uploadTransferInRecord(List<TransferInRequestDTO> transferInRequestDtoList) {
		String response = "";
		try{
			List<TransferInRequestDTO> uniqueTransferInRequestDTOList = new ArrayList<>();
			List<TransferIn> transferInRecordLists = null;
			List<TransferIn> transferInRecordList = transferInRepository.findAll();
			if(!transferInRecordList.isEmpty()){
				for(TransferInRequestDTO transferInRequestDtos : transferInRequestDtoList){
					boolean isFound = false;
					for (TransferIn transferInRecordObj : transferInRecordList) {
						if(transferInRequestDtos.getUniqueId()==transferInRecordObj.getUniqueId()){
							isFound = true;
							break;
						}
					}
					if(!isFound)
						uniqueTransferInRequestDTOList.add(transferInRequestDtos);
				}
				transferInRecordLists = new ArrayList<>();
				for (TransferInRequestDTO transferInRequestDTO : uniqueTransferInRequestDTOList) {
					TransferIn transferInRecord = new TransferIn();
					transferInRecord.setAmount(transferInRequestDTO.getAmount());
					transferInRecord.setFolioNumber(transferInRequestDTO.getFolioNo());
					transferInRecord.setInvesterName(transferInRequestDTO.getInvesterName());
					transferInRecord.setInvestmentType(transferInRequestDTO.getInvestmentType());
					transferInRecord.setLastUpdated(new Date());
					transferInRecord.setPan(transferInRequestDTO.getPan());
					transferInRecord.setSchemeCode(transferInRequestDTO.getSchemeCode());
					transferInRecord.setStartedOn(transferInRequestDTO.getStartedOn());
					transferInRecord.setStatus("P");
					transferInRecord.setStatusCode(0);
					transferInRecord.setTimeCreated(new Date());
					transferInRecord.setTransactionType(transferInRequestDTO.getTransactionType());
					transferInRecord.setUniqueId(transferInRequestDTO.getUniqueId());
					transferInRecord.setUnit(transferInRequestDTO.getUnit());
					transferInRecord.setAvailableUnit(transferInRequestDTO.getUnit());
					transferInRecord.setRtaAgent(transferInRequestDTO.getRtaAgent());
					transferInRecord.setField1(new BigDecimal(0.0));
					transferInRecordLists.add(transferInRecord);
				}
			}else{
				transferInRecordLists = new ArrayList<>();
				for (TransferInRequestDTO transferInRequestDto : transferInRequestDtoList) {
					TransferIn transferInRecord = new TransferIn();
					transferInRecord.setAmount(transferInRequestDto.getAmount());
					transferInRecord.setFolioNumber(transferInRequestDto.getFolioNo());
					transferInRecord.setInvesterName(transferInRequestDto.getInvesterName());
					transferInRecord.setInvestmentType(transferInRequestDto.getInvestmentType());
					transferInRecord.setLastUpdated(new Date());
					transferInRecord.setPan(transferInRequestDto.getPan());
					transferInRecord.setSchemeCode(transferInRequestDto.getSchemeCode());
					transferInRecord.setStartedOn(transferInRequestDto.getStartedOn());
					transferInRecord.setStatus("P");
					transferInRecord.setStatusCode(0);
					transferInRecord.setTimeCreated(new Date());
					transferInRecord.setTransactionType(transferInRequestDto.getTransactionType());
					transferInRecord.setUniqueId(transferInRequestDto.getUniqueId());
					transferInRecord.setUnit(transferInRequestDto.getUnit());
					transferInRecord.setAvailableUnit(transferInRequestDto.getUnit());
					transferInRecord.setField1(new BigDecimal(0.0));
					transferInRecord.setRtaAgent(transferInRequestDto.getRtaAgent());
					transferInRecordLists.add(transferInRecord);
				}
			}
			transferInRepository.saveAll(transferInRecordLists);
			response = GoForWealthTICOBConstants.SUCCESS_MESSAGE;
		}catch(Exception ex){
			response = GoForWealthTICOBErrorMessageEnum.FAILURE_MESSAGE.getValue();
		}
		return response;
	}
	
	@Override
	public List<TransferInRecordDTO> getTransferInRecord(int userId) {
		List<TransferInRecordDTO> transferInRecordDtoList = new ArrayList<>();
		List<TransferIn> workingTransferInRecordList = new ArrayList<>();
		try{
			User user = userRepository.findByUserId(userId);
			boolean enachStatus = false;
			boolean billerStatus = false;
			boolean isIsipAllowed = false;
			if(user != null && !user.getPanDetails().getPanNo().equals("")){
				if (user.getOnboardingStatus() != null) {
					if (user.getOnboardingStatus().getMandateNumber() != null && !user.getOnboardingStatus().getEnachStatus().equals("APPROVED")) {
						enachStatus = true;
					} else {
						enachStatus = false;
					}
					if (user.getOnboardingStatus().getIsipMandateNumber() != null && !user.getOnboardingStatus().getBillerStatus().equals("APPROVED")) {
						billerStatus = true;
					} else {
						billerStatus = false;
					}
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
				if (isFound) {
					isIsipAllowed = true;
				} else {
					isIsipAllowed = false;
				}
				String pan = encryptUserDetail.decrypt(user.getPanDetails().getPanNo());
				workingTransferInRecordList = transferInRepository.findByPan(pan.toUpperCase());
				for (TransferIn transferInRecord : workingTransferInRecordList) {
						TransferInRecordDTO transferInRecordDto = new TransferInRecordDTO();
						transferInRecordDto.setFolioNo(transferInRecord.getFolioNumber());
						transferInRecordDto.setIsipAllowed(isIsipAllowed);
						Scheme scheme = schemeRepository.getBySchemeCode(transferInRecord.getSchemeCode());
						boolean isSipAllowed = false;
						boolean isLumpsumAllowed = false;
						if(scheme!=null){
							transferInRecordDto.setSchemeType(scheme.getSchemeType());
							transferInRecordDto.setCurrentNav(new BigDecimal(scheme.getCurrentNav()));
							transferInRecordDto.setIsRedemptionAllowed(scheme.getRedemptionAllowed());
							transferInRecordDto.setMinimumRedemptionAmount(new BigDecimal(scheme.getRedemptionAmountMinimum()));
							transferInRecordDto.setMinSipAmount(Double.valueOf(scheme.getMinSipAmount()).intValue());
							transferInRecordDto.setMinLumpsumAmount(scheme.getMinimumPurchaseAmount());
							transferInRecordDto.setSchemeName(scheme.getSchemeName());
							transferInRecordDto.setSchemeCode(scheme.getSchemeCode());
							transferInRecordDto.setAmcCode(scheme.getAmcCode());
							if (scheme.getSipDates() != null && !scheme.getSipDates().equals("")){
								transferInRecordDto.setSipAllowedDate(scheme.getSipDates().split(","));
								transferInRecordDto.setSipDate(getSipDate(scheme.getSipDates().split(",")));
							}
							if(scheme.getPurchaseAllowed().equals("Y")){
								isLumpsumAllowed = true;
							}else{
								isLumpsumAllowed = false;
							}
							if(scheme.getSipFlag().equals("Y")){
								isSipAllowed = true;
							}else{
								isSipAllowed = false;
							}
						}
						transferInRecordDto.setInvestedAmount(transferInRecord.getAmount().setScale(2));
						transferInRecordDto.setAllotedUnit(transferInRecord.getUnit());
						transferInRecordDto.setAvailableUnit(transferInRecord.getAvailableUnit());
						transferInRecordDto.setBillerEnable(billerStatus);
						transferInRecordDto.setEnachEnable(enachStatus);
						transferInRecordDto.setTransferInId(transferInRecord.getTransferInId());
						transferInRecordDto.setSipAllowed(isSipAllowed);
						transferInRecordDto.setLumpsumAllowed(isLumpsumAllowed);
						transferInRecordDto.setInvestmentType(transferInRecord.getInvestmentType());
						SimpleDateFormat sf = new SimpleDateFormat("dd-MMM-yyyy");
						SimpleDateFormat sf1 = new SimpleDateFormat("dd/MM/yyyy");
						Date date = sf1.parse(transferInRecord.getStartedOn().replace('\\','/'));
						String startedDate = sf.format(date);
						transferInRecordDto.setStartedOn(startedDate);
						DecimalFormat df = new DecimalFormat("##");
						String schemeCode = getSchemeCodeWithL1(scheme.getRtaSchemeCode(), scheme.getIsin(), scheme.getSchemeCode());/*scheme.getSchemeCode() + "-L1";*/
						Scheme schemeWithL1 = schemeRepository.findBySchemeCodeWithL1(schemeCode);
						if (schemeWithL1 != null) {
							if (!schemeWithL1.getMaximumPurchaseAmount().equals("0.000")) {
								transferInRecordDto.setMaxLumpsumAmount(schemeWithL1.getMaximumPurchaseAmount());
							} else {
								transferInRecordDto.setMaxLumpsumAmount("999999999.000");
							}
						} else {
							transferInRecordDto.setMaxLumpsumAmount(scheme.getMaximumPurchaseAmount());
						}
						
						if (schemeWithL1 != null) {
							if (schemeWithL1.getSipMaximumInstallmentAmount() != null && !schemeWithL1.getSipMaximumInstallmentAmount().toString().equals("0.00000")) {
								transferInRecordDto.setMaxSipAmount(df.format((Math.round(Double.valueOf(schemeWithL1.getSipMaximumInstallmentAmount().toString()) * 100.0) / 100.0)));
							} else {
								if (scheme.getSipMaximumInstallmentAmount() != null) {
									transferInRecordDto.setMaxSipAmount(df.format((Math.round(Double.valueOf(scheme.getSipMaximumInstallmentAmount().toString()) * 100.0) / 100.0)));
								}
							}
						} else {
							if (scheme.getSipMaximumInstallmentAmount() != null) {
								transferInRecordDto.setMaxSipAmount(df.format((Math.round(Double.valueOf(scheme.getSipMaximumInstallmentAmount().toString()) * 100.0) / 100.0)));
							}
						}
						transferInRecordDto.setMinAdditionalAmount(new BigDecimal(scheme.getAdditionalPurchaseAmount()));
						
						
						
						
						/*
						transferInRecordDto.setGoalId(0);
						transferInRecordDto.setGoalName("");
						 */
						transferInRecordDtoList.add(transferInRecordDto);
				}
			}
		}catch(Exception ex){
			ex.printStackTrace();
		}
		return transferInRecordDtoList;
	}
	
	public String getSipDate(String[] sipDates) {
		LocalDate currentDate = LocalDate.now();
		int dom = currentDate.getDayOfMonth();
		int nextDate = dom+1;
		boolean found = false;
		String sipDate = "";
		for(int i=0;i<sipDates.length;i++) {
			if(nextDate==Integer.parseInt(sipDates[i])){
				sipDate = String.valueOf(nextDate);
				found = true;
				break;
			}else if(nextDate>Integer.parseInt(sipDates[i])){
				found = false;
			}else{
				found = true;
				sipDate = String.valueOf(sipDates[i]);
				break;
			}
		}
		if(!found){
			sipDate = sipDates[0];
		}
		return sipDate;
	}

	@Override
	public String placeTicobSipOrder(AddToCartDTO addToCartDTO, User user) {
		String response = "Failed";
		if(addToCartDTO != null){
			addToCartDTO.setUserId(user.getUserId());
			Scheme scheme = schemeRepository.findBySchemeCode(addToCartDTO.getSchemeCode());
			addToCartDTO.setSchemeId(scheme.getSchemeId());
			AddToCartDTO addToCartDTO2 = goForWealthFundSchemeService.addToCart(addToCartDTO);
			Orders order = orderRepository.findByOrdersId(addToCartDTO2.getOrderId());
			if(order != null){
				order.setFollioNo(addToCartDTO.getFolioNo());
				order.setTranferInId(addToCartDTO.getTransferInId());
				orderRepository.save(order);
				response = confirmSipTicobOrder(user, addToCartDTO2.getOrderId(),addToCartDTO.getFolioNo(),addToCartDTO.getTransferInId(),addToCartDTO);
			}
		}
		return response;
	}

	@Override
	public String placeTicobLumpsumOrder(AddToCartDTO addToCartDTO, User user) {
		String response = "Failed";
		if(addToCartDTO != null){
			addToCartDTO.setUserId(user.getUserId());
			Scheme scheme = schemeRepository.findBySchemeCode(addToCartDTO.getSchemeCode());
			addToCartDTO.setSchemeId(scheme.getSchemeId());
			AddToCartDTO addToCartDTO2 = goForWealthFundSchemeService.addToCart(addToCartDTO);
			Orders order = orderRepository.findByOrdersId(addToCartDTO2.getOrderId());
			if(order != null){
				if(addToCartDTO.getFolioNo() != null && !addToCartDTO.getFolioNo().equals("")){
					order.setFollioNo(addToCartDTO.getFolioNo());
					order.setTranferInId(addToCartDTO.getTransferInId());
				}else{
					order.setFollioNo("");
				}
				order.setTranferInId(addToCartDTO.getTransferInId());
				Orders orderObj = orderRepository.save(order);
				response = confirmLumpsumTicobOrder(user,orderObj.getOrdersId(),addToCartDTO);
				if(response.contains("DUPLICAT UNIQUE REF NO") || response.contains("FAILED")){
					response = "Failed";
				}else{
					response = makePayment(orderObj.getOrdersId(),user,String.valueOf(addToCartDTO.getAmount()));
				}
			}
		}
		return response;
	}

	public String confirmLumpsumTicobOrder(User user, Integer orderId,AddToCartDTO addToCartDTO) {
		String response = "success";
		Orders orders = orderRepository.findByOrdersId(orderId);
		String folioNo = "";
		String buySellType = "FRESH";
		if(orders.getFollioNo() != null && !orders.getFollioNo().equals("")){
			folioNo = orders.getFollioNo();
			buySellType = "ADDITIONAL";
		}
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
			orderEntryRequest.setBuySellType(buySellType);// Buy/Sell type i.e. FRESH/ADDITIONAL
			orderEntryRequest.setDpTxn("P");// CDSL/NSDL/PHYSICAL,,P = PHYSICAL
			orderEntryRequest.setOrderVal(orderValue);// Purchase/Redemption  amount(redemption amount only incase of physical redemption)
			orderEntryRequest.setQty("");
			orderEntryRequest.setAllRedeem("Y");// All units flag, If this Flag is"Y" then units and amount column should be blank
			orderEntryRequest.setFolioNo(folioNo);// Incase demat transacti this field will be blan and mandatory in case of physical redemption and physical purchase+additional
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
				SimpleDateFormat sf = new SimpleDateFormat("dd-MM-yyyy HH:mm:ss");
				TransferIn transferIn = transferInRepository.getOne(addToCartDTO.getTransferInId());
				BigDecimal allotedUnit = new BigDecimal(0);
				BigDecimal investedAmount = new BigDecimal(0);
				BigDecimal currentAmount = new BigDecimal(0);
				if(transferIn != null){
					transferIn.setSipRegNo(orderId.toString());
					transferIn.setStatus("C");
					transferIn.setUserId(user.getUserId());
					transferInRepository.save(transferIn);
					//after order placing successfully tranferIn recored inserted into ConsolidatedFollio Table 
					ConsolidatedPortfollio consolidatedPortfollio = consolidatedFollioRepository.getDetailByFollioAndSchemeCode(addToCartDTO.getFolioNo(), addToCartDTO.getSchemeCode());
					Scheme scheme = schemeRepository.findBySchemeCode(addToCartDTO.getSchemeCode());
					String currentNav = "0.0";
					if(scheme.getCurrentNav() != null){
						currentNav = scheme.getCurrentNav();
					}
					if(consolidatedPortfollio != null){
						allotedUnit = consolidatedPortfollio.getAllotedUnit().add(transferIn.getAvailableUnit());
					    investedAmount =consolidatedPortfollio.getInvestedAmount().add(transferIn.getAmount());
					    currentAmount = allotedUnit.multiply(new BigDecimal(currentNav));
					    consolidatedPortfollio.setAllotedUnit(allotedUnit);
					    consolidatedPortfollio.setInvestedAmount(investedAmount);
					    consolidatedPortfollio.setCurrentAmount(currentAmount);
					    consolidatedPortfollio.setTranferInId(addToCartDTO.getTransferInId());
					    consolidatedFollioRepository.save(consolidatedPortfollio);
					    transferIn.setStatusCode(1);
					    transferInRepository.save(transferIn);
					    
					}else{
						ConsolidatedPortfollio coPortfollio = new ConsolidatedPortfollio();
						coPortfollio.setAllotedUnit(transferIn.getAvailableUnit());
						coPortfollio.setInvestedAmount(transferIn.getAmount());
						coPortfollio.setCurrentAmount(transferIn.getAvailableUnit().multiply(new BigDecimal(currentNav)));
						coPortfollio.setClientCode(user.getOnboardingStatus().getClientCode());
						coPortfollio.setFolioNo(addToCartDTO.getFolioNo());
						coPortfollio.setLastNavUpdatedDate(sf.format(new Date()));
						//coPortfollio.setOrderId(orderId);
						coPortfollio.setSchemeCode(addToCartDTO.getSchemeCode());
						//coPortfollio.setSipRegNo(sipRegNo);
						//coPortfollio.setStatus(status);
						//coPortfollio.setStatusCode(statusCode);
						coPortfollio.setUserId(user.getUserId());
						coPortfollio.setTranferInId(addToCartDTO.getTransferInId());
						consolidatedFollioRepository.save(coPortfollio);
						 transferIn.setStatusCode(1);
						 transferInRepository.save(transferIn);
					}
				}
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
		return response;
	}

	public String makePayment(Integer orderId,User user, String amount) {
		String paymentResponse = "";
		Orders order = orderRepository.getOne(orderId);
		if (order != null) {
			String[] Order = new String[1];
			Order[0] = order.getBseOrderId();
			// calling payment api
			try{
				paymentResponse = paymentService.doPayment(user.getUserId(),Order,amount,orderId.toString());
			}catch(Exception ex){
				ex.printStackTrace();
			}
			if (!paymentResponse.contains("Please check your mail") && !paymentResponse.contains("Payment initiated for given OrderNo. Please wait some time.")) {
				StoreConf imageUrl = storeConfRepository.findByKeyword(GoForWealthPRSConstants.IMAGE_LOCATION);
				StoreConf path1 = storeConfRepository.findByKeyword(GoForWealthPRSConstants.PAYMENT_RESPONSE_HTML_LOCATION);
				StoreConf forwardSlash = storeConfRepository.findByKeyword(GoForWealthPRSConstants.FORWARD_SLASH);
				String path = imageUrl.getKeywordValue() + path1.getKeywordValue();
				String destPath = path + forwardSlash.getKeywordValue() + user.getUserId().toString() + "_" + orderId + ".html";
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
				ppcpaytranRepository.save(ppcpaytran);
			}/*else if(paymentResponse.contains("Payment initiated for given OrderNo. Please wait some time.")){
				StoreConf imageUrl = storeConfRepository.findByKeyword(GoForWealthPRSConstants.IMAGE_LOCATION);
				StoreConf path1 = storeConfRepository.findByKeyword(GoForWealthPRSConstants.PAYMENT_RESPONSE_HTML_LOCATION);
				StoreConf forwardSlash = storeConfRepository.findByKeyword(GoForWealthPRSConstants.FORWARD_SLASH);
				String path = imageUrl.getKeywordValue() + path1.getKeywordValue();
				String destPath = path + forwardSlash.getKeywordValue() + user.getUserId().toString() + "_" + orderId + ".html";
				String file = destPath;
				StringBuilder contentBuilder = new StringBuilder();
				try {
					BufferedReader br = new BufferedReader(new InputStreamReader(new FileInputStream(file)));
					String line;
					while ((line = br.readLine()) != null) {
						contentBuilder.append(line);
					}
					br.close();
					paymentResponse = contentBuilder.toString();
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
					Ppcpaytran ppcpaytran = new Ppcpaytran();
					ppcpaytran.setPpcpayinst(ppcpayinst);
					ppcpaytran.setProcessedamount(new BigDecimal(0.0));
					ppcpaytran.setState(0);
					ppcpaytranRepository.save(ppcpaytran);
				} catch (IOException e) {
					e.printStackTrace();
					paymentResponse = "101|Payment initiated for given OrderNo. Please wait some time.";
				}
			}*/
		}
		return paymentResponse;
	}

	private String confirmSipTicobOrder(User user, Integer orderId,String follioNo,int tranferInId,AddToCartDTO addToCartDTO) {
		String MemberId = "";
		String paymentOptions = "";
		Orders orders = orderRepository.findByOrdersId(orderId);
		boolean isFirstOrderOfTheDay = false;
		SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
		List<String> startDateList = new ArrayList<>();
		String response = "success";
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
				if (!onboardingStatus.getEnachStatus().equals("APPROVED")
						&& !onboardingStatus.getBillerStatus().equals("APPROVED")) {
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
			xsipOrderEntryRequest.setFolioNo(follioNo);
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
						
                        /*//------ store sip regn no into tranfer_in table---------------
						TransferIn transferIn = transferInRepository.getOne(tranferInId);
						if(transferIn != null){
							transferIn.setSipRegNo(xsipOrderEntryResonse.getXsipRegId());
							transferIn.setStatus("C");
							//transferIn.setStatusCode(1);
							transferIn.setUserId(user.getUserId());
							transferInRepository.save(transferIn);
						}*/
						
						SimpleDateFormat sf = new SimpleDateFormat("dd-MM-yyyy HH:mm:ss");
						TransferIn transferIn = transferInRepository.getOne(addToCartDTO.getTransferInId());
						BigDecimal allotedUnit = new BigDecimal(0);
						BigDecimal investedAmount = new BigDecimal(0);
						BigDecimal currentAmount = new BigDecimal(0);
						if(transferIn != null){
							transferIn.setSipRegNo(orderId.toString());
							transferIn.setStatus("C");
							transferIn.setUserId(user.getUserId());
							transferInRepository.save(transferIn);
							//after order placing successfully tranferIn recored inserted into ConsolidatedFollio Table 
							ConsolidatedPortfollio consolidatedPortfollio = consolidatedFollioRepository.getDetailByFollioAndSchemeCode(addToCartDTO.getFolioNo(), addToCartDTO.getSchemeCode());
							Scheme scheme = schemeRepository.findBySchemeCode(addToCartDTO.getSchemeCode());
							String currentNav = "0.0";
							if(scheme.getCurrentNav() != null){
								currentNav = scheme.getCurrentNav();
							}
							if(consolidatedPortfollio != null){
								allotedUnit = consolidatedPortfollio.getAllotedUnit().add(transferIn.getAvailableUnit());
							    investedAmount =consolidatedPortfollio.getInvestedAmount().add(transferIn.getAmount());
							    currentAmount = allotedUnit.multiply(new BigDecimal(currentNav));
							    consolidatedPortfollio.setAllotedUnit(allotedUnit);
							    consolidatedPortfollio.setInvestedAmount(investedAmount);
							    consolidatedPortfollio.setCurrentAmount(currentAmount);
							    consolidatedPortfollio.setTranferInId(addToCartDTO.getTransferInId());
							    consolidatedFollioRepository.save(consolidatedPortfollio);
							    transferIn.setStatusCode(1);
							    transferInRepository.save(transferIn);
							    
							}else{
								ConsolidatedPortfollio coPortfollio = new ConsolidatedPortfollio();
								coPortfollio.setAllotedUnit(transferIn.getAvailableUnit());
								coPortfollio.setInvestedAmount(transferIn.getAmount());
								coPortfollio.setCurrentAmount(transferIn.getAvailableUnit().multiply(new BigDecimal(currentNav)));
								coPortfollio.setClientCode(user.getOnboardingStatus().getClientCode());
								coPortfollio.setFolioNo(addToCartDTO.getFolioNo());
								coPortfollio.setLastNavUpdatedDate(sf.format(new Date()));
								//coPortfollio.setOrderId(orderId);
								coPortfollio.setSchemeCode(addToCartDTO.getSchemeCode());
								//coPortfollio.setSipRegNo(sipRegNo);
								//coPortfollio.setStatus(status);
								//coPortfollio.setStatusCode(statusCode);
								coPortfollio.setUserId(user.getUserId());
								coPortfollio.setTranferInId(addToCartDTO.getTransferInId());
								consolidatedFollioRepository.save(coPortfollio);
								 transferIn.setStatusCode(1);
								 transferInRepository.save(transferIn);
							}
							
						}
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
						emailContent = messageSource.getMessage("sip.start.mailBody", new String[] { userName, orderType, schemeName, amount, paymentDate, sipId },Locale.ENGLISH);
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



	/*
	@Override
	public String uploadCamsReport(List<TransferInRequestDTO> camsDataRequestDTOList) {
		String response = "";
		List<TransferInRequestDTO> camsDataRequestDTOList1 = new ArrayList<>();
		if(!camsDataRequestDTOList.isEmpty()){
			List<TransferIn> transferInsList = null;
			String rtaAgent = "Cams";
			List<TransferIn> transferInsList1 = transferInRepository.findByRtaAgent(rtaAgent);
			if(!transferInsList1.isEmpty()){
				transferInsList = transferInRepository.findAll();
				if(!transferInsList.isEmpty()){
					for (TransferInRequestDTO camsDataRequestDTO : camsDataRequestDTOList) {
						if(camsDataRequestDTO.getTrxnType().equals("TICOB")){
							boolean flag = false;
							for (TransferIn transferIn : transferInsList) {
								if(transferIn.getTransactionNumber().equals(camsDataRequestDTO.getTransactionNumber())){
									flag = true;
									break;
								}
							}
							if(!flag){
								camsDataRequestDTOList1.add(camsDataRequestDTO);
							}
						}
					}
					if(!camsDataRequestDTOList1.isEmpty()){
						List<TransferIn> transferInUpdateList = saveCamsTranferInData(camsDataRequestDTOList1, transferInsList);
						if(!transferInUpdateList.isEmpty()){
							response = GoForWealthTICOBConstants.SUCCESS_MESSAGE;
						}else{
							response = GoForWealthTICOBConstants.DATA_NOT_SAVED;
						}
					}
				}else{
					List<TransferIn> transferInUpdateList = saveCamsTranferInData(camsDataRequestDTOList, transferInsList);
					if(!transferInUpdateList.isEmpty()){
						response = GoForWealthTICOBConstants.SUCCESS_MESSAGE;
					}else{
						response = GoForWealthTICOBConstants.DATA_NOT_SAVED;
					}
				}
			}else{
				List<TransferIn> transferInUpdateList = saveCamsTranferInData(camsDataRequestDTOList, transferInsList);
				if(!transferInUpdateList.isEmpty()){
					response = GoForWealthTICOBConstants.SUCCESS_MESSAGE;
				}else{
					response = GoForWealthTICOBConstants.DATA_NOT_SAVED;
				}
			}	
		}else{
			return GoForWealthTICOBErrorMessageEnum.DATA_NOT_FOUND_MESSAGE.getValue();
		}
		return response;
	}

	public List<TransferIn> saveCamsTranferInData(List<TransferInRequestDTO> camsDataRequestDTOList,List<TransferIn> transferInsList){
		List<TransferIn> transferInsList1 = new ArrayList<TransferIn>();
		for (TransferInRequestDTO camsDataRequestDTO : camsDataRequestDTOList) {
			if(camsDataRequestDTO.getTrxnType().equals("TICOB")){
				TransferIn transferIn = new TransferIn();
				transferIn.setFollioNumber(camsDataRequestDTO.getFollioNo());
				transferIn.setSchemeName(camsDataRequestDTO.getScheme());
				transferIn.setTransactionNumber(camsDataRequestDTO.getTransactionNumber());
				transferIn.setInvesterName(camsDataRequestDTO.getInvesterName());
				transferIn.setTransactionDate(camsDataRequestDTO.getTradDate());
				transferIn.setProcessDate(camsDataRequestDTO.getPostDate());
				transferIn.setPrice(camsDataRequestDTO.getPurPrice());
				transferIn.setUnit(camsDataRequestDTO.getUnit());
				transferIn.setAmount(camsDataRequestDTO.getAmount());
				transferIn.setInvestmentType(camsDataRequestDTO.getInvestmentType());
				transferIn.setReportDate(camsDataRequestDTO.getReportDate());
				try {
					transferIn.setPan(encryptUserDetail.encrypt(camsDataRequestDTO.getPan()));
				} catch (Exception e) {
					e.printStackTrace();
				}
				transferIn.setTransactionType(camsDataRequestDTO.getTrxnType());
				transferIn.setChannelPartnerCode(camsDataRequestDTO.getProdCode());
				transferIn.setField1(new BigDecimal(0.00000));
				transferIn.setRtaAgent("Cams");
				transferIn.setStatus("P");
				transferIn.setStatusCode(0);

				//Setting TransferInDetails Data
				Set<TransferInDetails> tranferDetailsSet = new HashSet<TransferInDetails>();
				TransferInDetails transferInDetails = new TransferInDetails();
				transferInDetails.setAmount(camsDataRequestDTO.getAmount());
				transferInDetails.setFollioNumber(camsDataRequestDTO.getFollioNo());
				transferInDetails.setLastUpdated(new Date());
				transferInDetails.setPrice(camsDataRequestDTO.getPurPrice());
				transferInDetails.setTimeCreated(new Date());
				transferInDetails.setTransferIn(transferIn);
				transferInDetails.setUnit(camsDataRequestDTO.getUnit());
				transferInDetails.setField1(new BigDecimal(0.00000));
				transferInDetails.setInvestmentType(camsDataRequestDTO.getInvestmentType());
				tranferDetailsSet.add(transferInDetails);
				transferIn.setTransferInDetailses(tranferDetailsSet);
				transferInsList1.add(transferIn);
			}
		}
		List<TransferIn> transferInUpdateList = transferInRepository.saveAll(transferInsList1);
		return transferInUpdateList;
	}
	*/

	/*
	@Override
	public String uploadTransferInMasterUserData(List<TransferInMasterDTO> transferInUserDetailsDTOList) {
		String result = "";
		List<TransferInMasterDTO> uniqueTransferInMasterDTOList = new ArrayList<>();
		try{
			for (TransferInMasterDTO transferInMasterDTO : transferInUserDetailsDTOList) {
				int i=0;
				for (TransferInMasterDTO transferInMasterDTOObj : uniqueTransferInMasterDTOList) {
					if(transferInMasterDTOObj.getPan().equalsIgnoreCase(transferInMasterDTO.getPan())){
						i=1;
					}
				}
				if(i==0){
					uniqueTransferInMasterDTOList.add(transferInMasterDTO);
				}
			}
			List<TransferIn> transferInList = transferInRepository.findAll();
			if(transferInList.size()>0){
				List<TransferInUserDetails> transferInUserDetailsList = transferInUserDetailsRepository.findAll();
				List<TransferInMasterDTO> uniqueTransferInUserDetailsDtoList = new ArrayList<>();
				for (TransferInMasterDTO transferInUserDetailsDTO : uniqueTransferInMasterDTOList) {
					boolean flag = false;
					for (TransferInUserDetails transferInUserDetails : transferInUserDetailsList) {
						if(transferInUserDetailsDTO.getPan().equalsIgnoreCase(encryptUserDetail.decrypt(transferInUserDetails.getPan()))){
							flag = true;
							break;
						}
					}
					if(!flag){
						TransferInMasterDTO transferInMasterDto = new TransferInMasterDTO();
						transferInMasterDto.setInvesterEmail(transferInUserDetailsDTO.getInvesterEmail());
						transferInMasterDto.setInvesterMobile(transferInUserDetailsDTO.getInvesterMobile());
						transferInMasterDto.setInvesterName(transferInUserDetailsDTO.getInvesterName());
						transferInMasterDto.setPan(transferInUserDetailsDTO.getPan());
						uniqueTransferInUserDetailsDtoList.add(transferInMasterDto);
					}
				}
				boolean isValid = false;
				for (TransferInMasterDTO transferInUserDetailsDTOObj : uniqueTransferInUserDetailsDtoList) {
					boolean isExist = false;
					for (TransferIn transferIn : transferInList) {
						if(transferIn.getPan() != null){
							if(transferInUserDetailsDTOObj.getPan().equalsIgnoreCase(encryptUserDetail.decrypt(transferIn.getPan()))){
								isExist = true;
								break;
							}
						}
					}
					User user = userRepository.findByMobileNumber(transferInUserDetailsDTOObj.getInvesterMobile());
					if(isExist){
						if(user != null){
							isValid = true;
						}else{
							TransferInUserDetails transferInUserDetails = new TransferInUserDetails();
							transferInUserDetails.setInvesterMobile(transferInUserDetailsDTOObj.getInvesterMobile());
							transferInUserDetails.setInvesterEmail(transferInUserDetailsDTOObj.getInvesterEmail());
							transferInUserDetails.setInvesterName(transferInUserDetailsDTOObj.getInvesterName());
							transferInUserDetails.setPan(encryptUserDetail.encrypt(transferInUserDetailsDTOObj.getPan().toUpperCase()));
							transferInUserDetails.setLastUpdate(new Date());
							transferInUserDetailsRepository.save(transferInUserDetails);
							isValid = true;
						}
					}else{
						if(user != null){
							isValid = true;
						}
					}
				}
				if(isValid)
					result = GoForWealthTICOBErrorMessageEnum.SUCCESS_MESSAGE.getValue();
				else
					result = GoForWealthTICOBErrorMessageEnum.DATA_NOT_FOUND_MESSAGE.getValue();
			}else{
				result = GoForWealthTICOBErrorMessageEnum.DATA_NOT_FOUND_MESSAGE.getValue();
			}
		}catch(Exception ex){ 
			ex.printStackTrace();
			return GoForWealthTICOBErrorMessageEnum.FAILURE_MESSAGE.getValue();
		}
		return result;
	}
	*/

	
	/*
	@Override
	public List<TransferInRecordDTO> getTransferInRecords(int userId) {
		List<TransferInRecordDTO> transferInRecordDtoList = new ArrayList<>();
		List<TransferIn> workingTransferInList = new ArrayList<>();
		try{
			User user = userRepository.findByUserId(userId);
			boolean enachStatus = false;
			boolean billerStatus = false;
			boolean isIsipAllowed = false;
			if(user != null && !user.getPanDetails().getPanNo().equals("")){
				if (user.getOnboardingStatus() != null) {
					if (user.getOnboardingStatus().getMandateNumber() != null && !user.getOnboardingStatus().getEnachStatus().equals("APPROVED")) {
						enachStatus = true;
					} else {
						enachStatus = false;
					}
					if (user.getOnboardingStatus().getIsipMandateNumber() != null && !user.getOnboardingStatus().getBillerStatus().equals("APPROVED")) {
						billerStatus = true;
					} else {
						billerStatus = false;
					}
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
				if (isFound) {
					isIsipAllowed = true;
				} else {
					isIsipAllowed = false;
				}
				String pan = encryptUserDetail.decrypt(user.getPanDetails().getPanNo());
				List<TransferIn> transferInList = transferInRepository.findAll();
				for (TransferIn transferIn : transferInList) {
					if(pan.equalsIgnoreCase(encryptUserDetail.decrypt(transferIn.getPan()))){
						workingTransferInList.add(transferIn);
					}
				}
				for (TransferIn transferIn : workingTransferInList) {
						TransferInRecordDTO transferInRecordDto = new TransferInRecordDTO();
						transferInRecordDto.setFolioNo(transferIn.getFollioNumber());
						transferInRecordDto.setIsipAllowed(isIsipAllowed);
						List<Scheme> schemeList = null;
						if(transferIn.getRtaAgent().equals("Cams")){
							schemeList = schemeRepository.findByChannelPartnerCode(transferIn.getChannelPartnerCode());
							if(schemeList.size()==0){
								schemeList = schemeRepository.findByRtaSchemeCode(transferIn.getChannelPartnerCode().substring(1));
							}
						}else{
							schemeList = schemeRepository.findByRtaSchemeCodeAndIsin(transferIn.getRtaSchemeCode(),transferIn.getIsin());
						}
						String schemeName = "";
						String schemeCode = "";
						String sipDates = "";
						String amcCode = "";
						boolean isSipAllowed = false;
						boolean isLumpsumAllowed = false;
						for (Scheme scheme : schemeList) {
							if (!scheme.getSchemeCode().contains("-L1") && (!scheme.getSchemeCode().contains("-L0")) && (!scheme.getSchemeCode().contains("-L1-I")) && (!scheme.getSchemeCode().endsWith("-I"))) {
								schemeCode = scheme.getSchemeCode();
								schemeName = scheme.getSchemeName();
								amcCode = scheme.getAmcCode();
								transferInRecordDto.setSchemeType(scheme.getSchemeType());
								transferInRecordDto.setCurrentNav(new BigDecimal(scheme.getCurrentNav()));
								transferInRecordDto.setIsRedemptionAllowed(scheme.getRedemptionAllowed());
								transferInRecordDto.setMinimumRedemptionAmount(new BigDecimal(scheme.getRedemptionAmountMinimum()));
								if (scheme.getSipDates() != null)
									sipDates = scheme.getSipDates();
								transferInRecordDto.setMinSipAmount(Double.valueOf(scheme.getMinSipAmount()).intValue());
								transferInRecordDto.setMinLumpsumAmount(scheme.getMinimumPurchaseAmount());
								if(scheme.getPurchaseAllowed().equals("Y")){
									isLumpsumAllowed = true;
								}else{
									isLumpsumAllowed = false;
								}
								if(scheme.getSipFlag().equals("Y")){
									isSipAllowed = true;
								}else{
									isSipAllowed = false;
								}
								break;
							}
						}
						transferInRecordDto.setSchemeName(schemeName.equals("") ? "N/A" : schemeName);
						transferInRecordDto.setSchemeCode(schemeCode.equals("") ? "N/A" : schemeCode);
						transferInRecordDto.setInvestedAmount(transferIn.getAmount().setScale(2));
						transferInRecordDto.setAllotedUnit(transferIn.getUnit());
						transferInRecordDto.setBillerEnable(billerStatus);
						transferInRecordDto.setEnachEnable(enachStatus);
						transferInRecordDto.setTransferInId(transferIn.getTransferInId());
						transferInRecordDto.setSipAllowed(isSipAllowed);
						transferInRecordDto.setLumpsumAllowed(isLumpsumAllowed);
						transferInRecordDto.setInvestmentType(transferIn.getInvestmentType());
						transferInRecordDto.setSchemeType("");
						transferInRecordDto.setStartedOn("02 Mar 2019");
						transferInRecordDto.setGoalId(0);
						transferInRecordDto.setGoalName("");
						transferInRecordDto.setAmcCode(amcCode);
						if(!sipDates.equals("")){
							transferInRecordDto.setSipAllowedDate(sipDates.split(","));
							transferInRecordDto.setSipDate(getSipDate(sipDates.split(",")));
						}
						transferInRecordDtoList.add(transferInRecordDto);
					}
			}
		}catch(Exception ex){
			ex.printStackTrace();
		}
		return transferInRecordDtoList;
	}
	*/

	
	/*
	@Override
	public String uploadKarvyReports(List<TransferInRequestDTO> transferInRequestDTOList) {
		boolean flag = false;
		List<TransferInRequestDTO> transferInRequestKarvyDTOList = new ArrayList<>();
		List<TransferInRequestDTO> uniqueTransferInRequestKarvyDTOList = new ArrayList<>();
		List<TransferInKarvyDTO> transferInKarvyDTOList = new ArrayList<>();
		Set<String> uniqueFolioList = new HashSet<String>();
		List<TransferIn> transferInsList = transferInRepository.findByRtaAgent("Karvy");
		if(!transferInsList.isEmpty()){
			List<TransferInDetails> transferInDetailsList = transferInDetailsRepository.findByRtaAgent("Karvy");
			for(TransferInRequestDTO transferInRequestDtos : transferInRequestDTOList){
				if(transferInRequestDtos.getTrxnType().equals("TI")){
					boolean isFound = false;
					for (TransferInDetails transferInDetails : transferInDetailsList) {
						if(transferInRequestDtos.getTransactionNumber().equals(transferInDetails.getTransactioNumber())){
							isFound = true;
							break;
						}
					}
					if(!isFound)
						uniqueTransferInRequestKarvyDTOList.add(transferInRequestDtos);
				}
			}
		}else{
			uniqueTransferInRequestKarvyDTOList = transferInRequestDTOList;
		}
		if(!transferInRequestDTOList.isEmpty()){
			if(!uniqueTransferInRequestKarvyDTOList.isEmpty()){
				for (TransferInRequestDTO transferInRequestDTOs : uniqueTransferInRequestKarvyDTOList) {
					if(transferInRequestDTOs.getTrxnType().equals("TI")){
						transferInRequestKarvyDTOList.add(transferInRequestDTOs);
						uniqueFolioList.add(transferInRequestDTOs.getFollioNo());
					}
				}
			}else{
				return GoForWealthTICOBConstants.SUCCESS_MESSAGE;
			}
		}else{
			return GoForWealthTICOBErrorMessageEnum.FAILURE_MESSAGE.getValue();
		}
		for (String folioNo : uniqueFolioList) {
			List<TransferInRequestDTO> recordWithFolio = new ArrayList<>();
			TransferInKarvyDTO transferInKarvyDto = new TransferInKarvyDTO();
			transferInKarvyDto.setFolioNo(folioNo);
			for (TransferInRequestDTO transferInRequestDto : transferInRequestKarvyDTOList) {
				if(folioNo.equals(transferInRequestDto.getFollioNo())){
					recordWithFolio.add(transferInRequestDto);
				}
			}
			transferInKarvyDto.setTransferInRequestDto(recordWithFolio);
			transferInKarvyDTOList.add(transferInKarvyDto);
		}
		for(TransferInKarvyDTO transferInKarvyDtos : transferInKarvyDTOList){
			List<TransferInRequestDTO> differentSchemeList = new ArrayList<>();
			for (TransferInRequestDTO transferInRequestDtos : transferInKarvyDtos.getTransferInRequestDto()) {
				int i = 0;
				for(TransferInRequestDTO differentScheme : differentSchemeList){
					if( (differentScheme.getRtaSchemeCode().equals(transferInRequestDtos.getRtaSchemeCode())) && (differentScheme.getIsin().equals(transferInRequestDtos.getIsin())) ){
						i=1;
					}
				}
				if(i==0)
					differentSchemeList.add(transferInRequestDtos);
			}
			for (TransferInRequestDTO differentScheme : differentSchemeList) {
				List<TransferInRequestDTO> schemeWithSameIsinAndRtaCode = new ArrayList<>();
				int j=0;
				BigDecimal amount = new BigDecimal(0);
				BigDecimal price = new BigDecimal(0);
				BigDecimal unit = new BigDecimal(0);
				for (TransferInRequestDTO transObj : transferInKarvyDtos.getTransferInRequestDto()) {
					if((transObj.getRtaSchemeCode().equals(differentScheme.getRtaSchemeCode())) && (transObj.getIsin().equals(differentScheme.getIsin()))){
						if(transObj.getAmount().signum() == 1 && transObj.getPurPrice().signum()==1 && transObj.getUnit().signum()==1){
							amount = amount.add(transObj.getAmount());
							price = price.add(transObj.getPurPrice());
							unit = unit.add(transObj.getUnit());
						}
						schemeWithSameIsinAndRtaCode.add(transObj);
						j=1;
					}
				}
				if(j==1){
					flag = false;
					flag = saveKarvyReports(schemeWithSameIsinAndRtaCode,transferInKarvyDtos.getFolioNo(),differentScheme,amount,price,unit);
				}
			}
		}
		if(flag)
			return GoForWealthTICOBConstants.SUCCESS_MESSAGE;
		else
			return GoForWealthTICOBErrorMessageEnum.FAILURE_MESSAGE.getValue();
	}

	private boolean saveKarvyReports(List<TransferInRequestDTO> tranferInRequestDTOList,String folioNo,TransferInRequestDTO differentScheme,BigDecimal amount,BigDecimal price,BigDecimal unit) {
		Set<TransferInDetails> tranferDetailsSet = new HashSet<TransferInDetails>();
		List<TransferIn> transferInList = new ArrayList<>();
		String investmentType = "";
		TransferIn transferIn = new TransferIn();
		for (TransferInRequestDTO tranferInRequestDTO : tranferInRequestDTOList) {
			if(tranferInRequestDTO.getInvestmentType().equals("SIN") || tranferInRequestDTO.getInvestmentType().equals("ADD") || tranferInRequestDTO.getInvestmentType().equals("SINR")){
				investmentType = "SIP";
			}
			if(tranferInRequestDTO.getInvestmentType().equals("NEW")){
				investmentType = "LUMPSUM";
			}
			TransferInDetails transferInDetails = new TransferInDetails();
			transferInDetails.setAmount(tranferInRequestDTO.getAmount());
			transferInDetails.setFollioNumber(tranferInRequestDTO.getFollioNo());
			transferInDetails.setLastUpdated(new Date());
			transferInDetails.setPrice(tranferInRequestDTO.getPurPrice());
			transferInDetails.setTimeCreated(new Date());
			transferInDetails.setUnit(tranferInRequestDTO.getUnit());
			transferInDetails.setField1(new BigDecimal(0.00000));
			transferInDetails.setIsin(tranferInRequestDTO.getIsin());
			transferInDetails.setRtaSchemeCode(tranferInRequestDTO.getRtaSchemeCode());
			transferInDetails.setTransactioNumber(tranferInRequestDTO.getTransactionNumber());
			transferInDetails.setRtaAgent("Karvy");
			transferInDetails.setInvestmentType(tranferInRequestDTO.getInvestmentType());
			transferInDetails.setTransferIn(transferIn);
			tranferDetailsSet.add(transferInDetails);
		}
		transferIn.setInvesterName(differentScheme.getInvesterName());
		transferIn.setIsin(differentScheme.getIsin());
		transferIn.setRtaSchemeCode(differentScheme.getRtaSchemeCode());
		try {
			transferIn.setPan(encryptUserDetail.encrypt(differentScheme.getPan()));
		} catch (Exception e) {
			e.printStackTrace();
		}
		transferIn.setProcessDate(differentScheme.getPostDate());
		transferIn.setReportDate(differentScheme.getReportDate());
		transferIn.setRtaSchemeCode(differentScheme.getRtaSchemeCode());
		transferIn.setSchemeName(differentScheme.getScheme());
		transferIn.setTransactionDate(differentScheme.getTradDate());
		transferIn.setTransactionNumber(differentScheme.getTransactionNumber());
		transferIn.setTransactionType(differentScheme.getTrxnType());
		transferIn.setFollioNumber(folioNo);
		transferIn.setPrice(price);
		transferIn.setUnit(unit);
		transferIn.setAmount(amount);
		transferIn.setField1(new BigDecimal(0.00000));
		transferIn.setRtaAgent("Karvy");
		transferIn.setStatus("P");
		transferIn.setInvestmentType(investmentType);
		transferIn.setStatusCode(0);
		transferIn.setTransferInDetailses(tranferDetailsSet);
		transferInList.add(transferIn);
		List<TransferIn> transferInUpdateList = transferInRepository.saveAll(transferInList);
		if(transferInUpdateList.size()>0)
			return true;
		else
			return false;
	}
	*/

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
}