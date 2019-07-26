package com.moptra.go4wealth.prs.service;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.Writer;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.text.DecimalFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.moptra.go4wealth.bean.IsipAllowedBankList;
import com.moptra.go4wealth.bean.OnboardingStatus;
import com.moptra.go4wealth.bean.OrderItem;
import com.moptra.go4wealth.bean.Orders;
import com.moptra.go4wealth.bean.Portfolio;
import com.moptra.go4wealth.bean.PortfolioCategory;
import com.moptra.go4wealth.bean.Ppcpayinst;
import com.moptra.go4wealth.bean.Ppcpaytran;
import com.moptra.go4wealth.bean.Scheme;
import com.moptra.go4wealth.bean.StoreConf;
import com.moptra.go4wealth.bean.User;
import com.moptra.go4wealth.prs.common.constant.GoForWealthPRSConstants;
import com.moptra.go4wealth.prs.common.exception.GoForWealthPRSException;
import com.moptra.go4wealth.prs.common.rest.GoForWealthPRSResponseInfo;
import com.moptra.go4wealth.prs.common.util.GoForWealthPRSUtil;
import com.moptra.go4wealth.prs.model.AddToCartDTO;
import com.moptra.go4wealth.prs.model.FundSchemeDTO;
import com.moptra.go4wealth.prs.payment.PaymentService;
import com.moptra.go4wealth.repository.IsipAllowedBankListRepository;
import com.moptra.go4wealth.repository.OrderItemRepository;
import com.moptra.go4wealth.repository.OrderRepository;
import com.moptra.go4wealth.repository.PortfolioCategoryRepository;
import com.moptra.go4wealth.repository.PortfolioRepository;
import com.moptra.go4wealth.repository.PpcpayinstRepository;
import com.moptra.go4wealth.repository.SchemeRepository;
import com.moptra.go4wealth.repository.StoreConfRepository;
import com.moptra.go4wealth.repository.UserRepository;
import com.moptra.go4wealth.repository.ppcpaytranRepository;

@Service
public class GoForWealthModelportfolioServiceImpl implements GoForWealthModelportfolioService {

	@Autowired
	PortfolioRepository portfolioRepository;

	@Autowired
	PortfolioCategoryRepository portfolioCategoryRepository;

	@Autowired
	SchemeRepository schemeRepository;

	@Autowired
	GoForWealthFundSchemeService goForWealthFundSchemeService;

	@Autowired
	OrderRepository orderRepository;

	@Autowired
	UserRepository userRepository;

	@Autowired
	PaymentService paymentService;

	@Autowired
	PpcpayinstRepository ppcpayinstRepository;

	@Autowired
	ppcpaytranRepository ppcpaytranRepository;

	@Autowired
	StoreConfRepository storeConfRepository;
	
	@Autowired
	OrderItemRepository orderItemRepository;
	
	@Autowired
	IsipAllowedBankListRepository isipAllowedBankListRepository;

	@Override
	public List<FundSchemeDTO> getModelportfolioList() {
		List<FundSchemeDTO> fundSchemeDtoList = new ArrayList<>();
		List<PortfolioCategory> portfolioCategoryList = portfolioCategoryRepository.findAll();
		for (PortfolioCategory portfolioCategory : portfolioCategoryList) {
			FundSchemeDTO fundSchemeDto = new FundSchemeDTO();
			fundSchemeDto.setDescription(portfolioCategory.getDescription());
			fundSchemeDto.setPortfolioCategoryName(portfolioCategory.getCategoryName());
			fundSchemeDto.setFeatures(portfolioCategory.getFeatures());
			fundSchemeDto.setInvestmentGoals(portfolioCategory.getInvestmentGoals());
			fundSchemeDto.setPortfolioCategoryKeyword(portfolioCategory.getCategoryKeyword());
			List<Portfolio> portfolioList = portfolioRepository.findByPortfolioCategoryId(portfolioCategory.getPortfolioCategoryId());
			Integer minSipAmount = 0;
			Integer minLumpsumAmount = 0;
			for (Portfolio portfolio : portfolioList) {
				Scheme schemeObj = schemeRepository.findBySchemeCode(portfolio.getSchemeCode());
				minSipAmount=(int) (minSipAmount + Double.parseDouble(schemeObj.getMinSipAmount()));
				minLumpsumAmount = minLumpsumAmount+schemeObj.getMinimumPurchaseAmount();
			}
			fundSchemeDto.setPortfolioCategoryMinSipAmount(String.valueOf(minSipAmount));
			fundSchemeDto.setPortfolioCategoryMinLumpsumAmount(String.valueOf(minLumpsumAmount));
			fundSchemeDtoList.add(fundSchemeDto);
		}
		return fundSchemeDtoList;
	}


	@Override
	public List<FundSchemeDTO> getModelportfolioDetailByCategory(String portfolioName) {
		List<FundSchemeDTO> fundSchemeDtoList = new ArrayList<>();
		PortfolioCategory portfolioCategory = portfolioCategoryRepository.findByCategoryKeyword(portfolioName);
		if(portfolioCategory!=null){
			List<Portfolio> portfolioList = portfolioRepository.findByPortfolioCategoryId(portfolioCategory.getPortfolioCategoryId());
			for (Portfolio portfolio : portfolioList) {
				FundSchemeDTO fundSchemeDto = new FundSchemeDTO();
				fundSchemeDto.setFeatures(portfolioCategory.getFeatures());
				fundSchemeDto.setPortfolioCategoryName(portfolioCategory.getCategoryName());
				fundSchemeDto.setInvestmentGoals(portfolioCategory.getInvestmentGoals());
				fundSchemeDto.setDescription(portfolioCategory.getDescription());
				Scheme schemeObj = schemeRepository.findBySchemeCode(portfolio.getSchemeCode());
				fundSchemeDto.setSchemeCode(schemeObj.getSchemeCode());
				fundSchemeDto.setSchemeId(schemeObj.getSchemeId());
				fundSchemeDto.setSchemeCode(schemeObj.getSchemeCode());
				fundSchemeDto.setPlan(schemeObj.getSchemePlan());
				fundSchemeDto.setSchemeName(schemeObj.getSchemeName());
				fundSchemeDto.setSchemeType(schemeObj.getSchemeType());
				fundSchemeDto.setSchemeLaunchDate(schemeObj.getStartDate());
				fundSchemeDto.setSchemeEndDate(schemeObj.getEndDate());
				fundSchemeDto.setAmcSchemeCode(schemeObj.getAmcSchemeCode());
				fundSchemeDto.setAmfiCode(schemeObj.getAmfiCode());
				fundSchemeDto.setBenchmarkCode(schemeObj.getBenchmarkCode());
				fundSchemeDto.setFaceValue(schemeObj.getFaceValue());
				fundSchemeDto.setReturn_(schemeObj.getReturnValue());
				fundSchemeDto.setRisk(schemeObj.getRisk());
				fundSchemeDto.setRtaCode(schemeObj.getRtaSchemeCode());
				fundSchemeDto.setSipAllowed(schemeObj.getSipFlag());
				if(schemeObj.getMinimumPurchaseAmount() != null){
					fundSchemeDto.setMinimumPurchaseAmount(schemeObj.getMinimumPurchaseAmount());
				}
				//fundSchemeDto.setMinimumPurchaseAmount(schemeObj.getMinimumPurchaseAmount());
				fundSchemeDto.setMaximumPurchaseAmount(schemeObj.getMaximumPurchaseAmount());
				fundSchemeDto.setStatus(schemeObj.getStatus());
				fundSchemeDto.setSchemeKeyword(schemeObj.getKeyword());
				fundSchemeDto.setAmcCode(schemeObj.getAmcCode());
				fundSchemeDto.setPriority(schemeObj.getPriority());
				fundSchemeDto.setPurchaseAllowed(schemeObj.getPurchaseAllowed());
				//fundSchemeDto.setMinSipAmount(schemeObj.getMinSipAmount());
				fundSchemeDto.setSipMaxInstallmentAmount(schemeObj.getSipMaximumInstallmentAmount().toString());
				if(schemeObj.getMinSipAmount() != null){
					fundSchemeDto.setMinSipAmount(String.valueOf(new BigDecimal(schemeObj.getMinSipAmount()).setScale(0, RoundingMode.HALF_UP)));
				}
				if(schemeObj.getSipDates() != null){
					fundSchemeDto.setSipAllowedDate(schemeObj.getSipDates());
				}
				DecimalFormat df = new DecimalFormat("##");
				if(schemeObj.getSipMaximumInstallmentAmount() != null){
					fundSchemeDto.setSipMaxInstallmentAmount(df.format((Math.round(Double.valueOf(schemeObj.getSipMaximumInstallmentAmount().toString()) * 100.0) / 100.0)));
				}
				if(schemeObj.getSipMinimumInstallmentNumber() != null){
					fundSchemeDto.setSipMinInstallmentNumber(schemeObj.getSipMinimumInstallmentNumber());
				}
				String[]sipDates =  schemeObj.getSipDates().split(",");
				//System.out.println(createDayOfSip(sipDates));
				//fundSchemeDto.setSipDateList(createDayOfSip(sipDates));
				fundSchemeDto.setSipDateList(sipDates);
				LocalDate currentDate = LocalDate.now();
				int dom = currentDate.getDayOfMonth();
				int nextDate = dom+1;
				boolean found = false;
				for(int i=0;i<sipDates.length;i++) {
					if(nextDate==Integer.parseInt(sipDates[i])){
						fundSchemeDto.setSipDate(String.valueOf(nextDate));
						found = true;
						break;
					}else if(nextDate>Integer.parseInt(sipDates[i])){
						found = false;
					}else{
						found = true;
						fundSchemeDto.setSipDate(String.valueOf(sipDates[i]));
						break;
					}
				}
				if(!found){
					fundSchemeDto.setSipDate(sipDates[0]);
				}
				//fundSchemeDto.setSipDate(createDayOfSip(fundSchemeDto.getSipDate()));
				fundSchemeDto.setSipDate(fundSchemeDto.getSipDate());
				fundSchemeDtoList.add(fundSchemeDto);
			}
		}
		return fundSchemeDtoList;
	}
	
	
	@Override
	public List<FundSchemeDTO> getModelportfolioDetailByCategorySecured(String portfolioName,Integer userId) {
		String kycStatus = "";
		int totalPurchageAmount = 0;
		int totalPurchageAmountRef = 0;
		boolean isFound = false;
		List<FundSchemeDTO> fundSchemeDtoList = new ArrayList<>();
		PortfolioCategory portfolioCategory = portfolioCategoryRepository.findByCategoryKeyword(portfolioName);
		if(portfolioCategory!=null){
			List<Portfolio> portfolioList = portfolioRepository.findByPortfolioCategoryId(portfolioCategory.getPortfolioCategoryId());
			for (Portfolio portfolio : portfolioList) {
				FundSchemeDTO fundSchemeDto = new FundSchemeDTO();
				fundSchemeDto.setFeatures(portfolioCategory.getFeatures());
				fundSchemeDto.setPortfolioCategoryName(portfolioCategory.getCategoryName());
				fundSchemeDto.setInvestmentGoals(portfolioCategory.getInvestmentGoals());
				fundSchemeDto.setDescription(portfolioCategory.getDescription());
				Scheme schemeObj = schemeRepository.findBySchemeCode(portfolio.getSchemeCode());
				fundSchemeDto.setSchemeCode(schemeObj.getSchemeCode());
				fundSchemeDto.setSchemeId(schemeObj.getSchemeId());
				fundSchemeDto.setSchemeCode(schemeObj.getSchemeCode());
				fundSchemeDto.setPlan(schemeObj.getSchemePlan());
				fundSchemeDto.setSchemeName(schemeObj.getSchemeName());
				fundSchemeDto.setSchemeType(schemeObj.getSchemeType());
				fundSchemeDto.setSchemeLaunchDate(schemeObj.getStartDate());
				fundSchemeDto.setSchemeEndDate(schemeObj.getEndDate());
				fundSchemeDto.setAmcSchemeCode(schemeObj.getAmcSchemeCode());
				fundSchemeDto.setAmfiCode(schemeObj.getAmfiCode());
				fundSchemeDto.setBenchmarkCode(schemeObj.getBenchmarkCode());
				fundSchemeDto.setFaceValue(schemeObj.getFaceValue());
				fundSchemeDto.setReturn_(schemeObj.getReturnValue());
				fundSchemeDto.setRisk(schemeObj.getRisk());
				fundSchemeDto.setRtaCode(schemeObj.getRtaSchemeCode());
				fundSchemeDto.setSipAllowed(schemeObj.getSipFlag());
				fundSchemeDto.setSipMaximumGap(schemeObj.getSipMaximumGap());
				if(schemeObj.getMinimumPurchaseAmount() != null){
					fundSchemeDto.setMinimumPurchaseAmount(schemeObj.getMinimumPurchaseAmount());
				}
				//fundSchemeDto.setMinimumPurchaseAmount(schemeObj.getMinimumPurchaseAmount());
				fundSchemeDto.setMaximumPurchaseAmount(schemeObj.getMaximumPurchaseAmount());
				fundSchemeDto.setStatus(schemeObj.getStatus());
				fundSchemeDto.setSchemeKeyword(schemeObj.getKeyword());
				fundSchemeDto.setAmcCode(schemeObj.getAmcCode());
				fundSchemeDto.setPriority(schemeObj.getPriority());
				fundSchemeDto.setPurchaseAllowed(schemeObj.getPurchaseAllowed());
				//fundSchemeDto.setMinSipAmount(schemeObj.getMinSipAmount());
				fundSchemeDto.setSipMaxInstallmentAmount(schemeObj.getSipMaximumInstallmentAmount().toString());
				if(schemeObj.getMinSipAmount() != null){
					fundSchemeDto.setMinSipAmount(String.valueOf(new BigDecimal(schemeObj.getMinSipAmount()).setScale(0, RoundingMode.HALF_UP)));
				}
				if(schemeObj.getSipDates() != null){
					fundSchemeDto.setSipAllowedDate(schemeObj.getSipDates());
				}
				DecimalFormat df = new DecimalFormat("##");
				if(schemeObj.getSipMaximumInstallmentAmount() != null){
					fundSchemeDto.setSipMaxInstallmentAmount(df.format((Math.round(Double.valueOf(schemeObj.getSipMaximumInstallmentAmount().toString()) * 100.0) / 100.0)));
				}
				if(schemeObj.getSipMinimumInstallmentNumber() != null){
					fundSchemeDto.setSipMinInstallmentNumber(schemeObj.getSipMinimumInstallmentNumber());
				}
				String[]sipDates =  schemeObj.getSipDates().split(",");
				//System.out.println(createDayOfSip(sipDates));
				//fundSchemeDto.setSipDateList(createDayOfSip(sipDates));
				fundSchemeDto.setSipDateList(sipDates);
				LocalDate currentDate = LocalDate.now();
				int dom = currentDate.getDayOfMonth();
				int nextDate = dom+1;
				boolean found = false;
				for(int i=0;i<sipDates.length;i++) {
					if(nextDate==Integer.parseInt(sipDates[i])){
						fundSchemeDto.setSipDate(String.valueOf(nextDate));
						found = true;
						break;
					}else if(nextDate>Integer.parseInt(sipDates[i])){
						found = false;
					}else{
						found = true;
						fundSchemeDto.setSipDate(String.valueOf(sipDates[i]));
						break;
					}
				}
				if(!found){
					fundSchemeDto.setSipDate(sipDates[0]);
				}
				//fundSchemeDto.setSipDate(createDayOfSip(fundSchemeDto.getSipDate()));
				fundSchemeDto.setSipDate(fundSchemeDto.getSipDate());
				User user = userRepository.getOne(userId);
				OnboardingStatus onboardingStatus = user.getOnboardingStatus();
			    if(onboardingStatus != null){

			    	if(onboardingStatus.getMandateNumber() != null && !onboardingStatus.getEnachStatus().equals("APPROVED")){
			    		
			    		fundSchemeDto.setIsEnachEnable("true");
			    	}else{
			    		fundSchemeDto.setIsEnachEnable("false");
			    	}
			    	if(onboardingStatus.getIsipMandateNumber() != null && !onboardingStatus.getBillerStatus().equals("APPROVED")){
			    		fundSchemeDto.setIsBillerEnable("true");
			    	}else{
			    		fundSchemeDto.setIsBillerEnable("false");
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
					fundSchemeDto.setIsIsipAllowed("true");
				} else {
					fundSchemeDto.setIsIsipAllowed("false");
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
				fundSchemeDto.setTotalPurchageAmount(totalPurchageAmount);
				int overAllStatus = user.getOnboardingStatus().getOverallStatus();
				if (user.getPanDetails().getVerified() != null) {
					kycStatus = user.getPanDetails().getVerified();
				}
				if (kycStatus.equals("verified") && overAllStatus == 1) {
					fundSchemeDto.setIsOrderAllowed("true");
				}
				if (kycStatus.equals("not-verified") && overAllStatus == 1) {
					fundSchemeDto.setIsOrderAllowed("false");
				}
				fundSchemeDtoList.add(fundSchemeDto);
			}
		}
		return fundSchemeDtoList;
	}

	public String[] createDayOfSip(String[] sipDates){
		for(int i=0; i<sipDates.length;i++){
			if(Integer.parseInt(sipDates[i]) % 100 == 11 || Integer.parseInt(sipDates[i]) % 100 == 12 || Integer.parseInt(sipDates[i]) % 100 == 13) {
				sipDates[i] = sipDates[i].concat("th");
			}else if(Integer.parseInt(sipDates[i])%10==1){
				sipDates[i] = sipDates[i].concat("st");
			}else if(Integer.parseInt(sipDates[i])%10==2){
				sipDates[i] = sipDates[i].concat("nd");
			}else if(Integer.parseInt(sipDates[i])%10==3){
				sipDates[i] = sipDates[i].concat("rd");
			}else{
				sipDates[i] = sipDates[i].concat("th");
			}
			System.out.println("Dates : " + sipDates[i]);
		}
		return sipDates;
	}

	public String createDayOfSip(String sipDate){
		if(Integer.parseInt(sipDate) % 100 == 11 || Integer.parseInt(sipDate) % 100 == 12 || Integer.parseInt(sipDate) % 100 == 13) {
			sipDate = sipDate.concat("th");
		}else if(Integer.parseInt(sipDate)%10==1){
			sipDate = sipDate.concat("st");
		}else if(Integer.parseInt(sipDate)%10==2){
			sipDate = sipDate.concat("nd");
		}else if(Integer.parseInt(sipDate)%10==3){
			sipDate = sipDate.concat("rd");
		}else{
			sipDate = sipDate.concat("th");
		}
		System.out.println("String : " + sipDate);
		return sipDate;
	}

	@Override
	public List<AddToCartDTO> addToCart(List<AddToCartDTO> addToCartDTO, User user) {
		int mPModelId = 1;
		List<AddToCartDTO> addToCartDTOsList = new ArrayList<AddToCartDTO>();
		if(!addToCartDTO.isEmpty()){
			List<Orders> ordersList = orderRepository.findModelPortfolioOrder();
			for (Orders orders : ordersList) {
				if(orders.getmPBundleId() != null && orders.getmPBundleId() !=0){
					mPModelId = orders.getmPBundleId();
					mPModelId = mPModelId+1;
					break;
				}
			}
			for (AddToCartDTO addToCartDTO2 : addToCartDTO) {
				addToCartDTO2.setUserId(user.getUserId());
				addToCartDTO2.setmPBundleId(mPModelId);
				AddToCartDTO addToCartDTO3 = addUserOrder(addToCartDTO2);
				addToCartDTOsList.add(addToCartDTO3);
			}
		}else{
			AddToCartDTO adCartDTO = new AddToCartDTO();
			adCartDTO.setStatus("Data Not Send By User");
			addToCartDTOsList.add(adCartDTO);
		}
		return addToCartDTOsList;
	}

	@Override
	public List<AddToCartDTO> confirmOrder(Integer userId , List<AddToCartDTO> addToCartDTOs) {
		String response ="";
		List<AddToCartDTO> orderList = new ArrayList<AddToCartDTO>();
		for (AddToCartDTO addToCartDTO : addToCartDTOs) {
			AddToCartDTO addToCartDTO2 = new AddToCartDTO();
			response = goForWealthFundSchemeService.confirmOrder(userId, addToCartDTO.getOrderId());
			addToCartDTO2.setStatus(response);
			addToCartDTO2.setOrderId(addToCartDTO.getOrderId());
			Orders order = orderRepository.getOne(addToCartDTO.getOrderId());
			if(order != null){
				addToCartDTO2.setBseOrderId(order.getBseOrderId());
			}
			orderList.add(addToCartDTO2);
		}
		return orderList;
	}

	@Override
	public List<AddToCartDTO> cancelOrder(List<AddToCartDTO> addToCartDTOs,Integer userId) {
		List<AddToCartDTO> orderList = new ArrayList<AddToCartDTO>();
		for (AddToCartDTO addToCartDTO : addToCartDTOs) {
			AddToCartDTO addToCartDTO2 = new AddToCartDTO();
			GoForWealthPRSResponseInfo goForWealthPRSResponseInfo = new GoForWealthPRSResponseInfo();
			goForWealthPRSResponseInfo = goForWealthFundSchemeService.cancelOrder( addToCartDTO.getOrderId(),userId);
			if(goForWealthPRSResponseInfo.getMessage().equals("Success")){
				addToCartDTO2.setFinalResponse(goForWealthPRSResponseInfo.getMessage());
			}else{
				addToCartDTO2.setFinalResponse(goForWealthPRSResponseInfo.getMessage());
			}
			addToCartDTO2.setStatus(goForWealthPRSResponseInfo.getData().toString());
			addToCartDTO2.setOrderId(addToCartDTO.getOrderId());
			orderList.add(addToCartDTO2);
		}
		//goForWealthPRSResponseInfo = goForWealthFundSchemeService.cancelOrder(orderId, userId);
		return orderList;
	}

	@Override
	public String deleteOrder(Integer userId, Integer orderId) {
		String response = "";
		response = goForWealthFundSchemeService.deleteOrder(userId, orderId);
		return response;
	}

	@Override
	public List<AddToCartDTO> getCartOrderByOrder(Integer userId, int bundelId) {
		List<AddToCartDTO> addToCartDTOsList = new ArrayList<AddToCartDTO>();
		List<Orders> orderList = orderRepository.findByBundelId(bundelId);	
		if(!orderList.isEmpty()){
			for (Orders orderIds : orderList) {
				if(orderIds.getOrdersId() != null && orderIds.getOrdersId() !=0){
					AddToCartDTO addToCartDTO = getCartOrderByOrder(userId, orderIds.getOrdersId());
					if(addToCartDTO != null){
						addToCartDTOsList.add(addToCartDTO);
					}
				}
			}
		}
		return addToCartDTOsList;
	}

	public AddToCartDTO addUserOrder(AddToCartDTO addToCartDTO) {
		try{
			User user = userRepository.getOne(addToCartDTO.getUserId());
			Scheme scheme = schemeRepository.getOne(addToCartDTO.getSchemeId());
			Orders orders = new Orders();
			orders.setUser(user);
			if(addToCartDTO.getModelPortfolioCategory() != null && addToCartDTO.getModelPortfolioCategory() != ""){
				orders.setModelPortfolioCategory(addToCartDTO.getModelPortfolioCategory());
				orders.setOrderCategory(GoForWealthPRSConstants.MODEL_PORTFOLIO_ORDER);
			}
			orders.setmPBundleId(addToCartDTO.getmPBundleId());
			orders.setLastupdate(new Date());
			orders.setStartdate(new Date());
			orders.setExpiredate(new Date());
			orders.setTimeplaced(new Date());
			if(addToCartDTO.getInvestmentType().equals("SIP")){
				if(addToCartDTO.getPaymentOptions() != null){
					orders.setPaymentOptions(addToCartDTO.getPaymentOptions());
				}else if(user.getOnboardingStatus().getMandateNumber() != null){
					orders.setPaymentOptions("Natch");
				}else if(user.getOnboardingStatus().getIsipMandateNumber() != null){
					orders.setPaymentOptions("Biller");
				}else{
					orders.setPaymentOptions("Pending");
				}
			}
			orders.setType(addToCartDTO.getInvestmentType());
			orders.setTotaladjustment(new BigDecimal(0));
			orders.setTotalproduct(new BigDecimal(1));
			orders.setTotaltax(new BigDecimal(0));
			if(addToCartDTO.getGoalId() != null){
				orders.setGoalId(addToCartDTO.getGoalId());
				orders.setGoalName(addToCartDTO.getGoalName());
			}
			orders.setStatus(GoForWealthPRSConstants.ORDER_PENDING_STATUS);
			Set<OrderItem> orderItems = new HashSet<>();
			OrderItem orderItem = new OrderItem();
			orderItem.setOrders(orders);
			if(addToCartDTO.getModelPortfolioCategory() != null && addToCartDTO.getModelPortfolioCategory() != ""){
				orderItem.setModelPortfolioCategory(addToCartDTO.getModelPortfolioCategory());
				orderItem.setOrderCategory(GoForWealthPRSConstants.MODEL_PORTFOLIO_ORDER);
			}
			orderItem.setmPBundleId(addToCartDTO.getmPBundleId());
			orderItem.setLastcreate(new Date());
			orderItem.setLastupdate(new Date());
			orderItem.setTimereleased(new Date());
			orderItem.setTimeshiped(new Date());
			orderItem.setProductprice(new BigDecimal(addToCartDTO.getAmount()));
			orderItem.setQuantity(1);
			orderItem.setStatus(GoForWealthPRSConstants.ORDER_PENDING_STATUS);
			orderItem.setTaxamount(new BigDecimal(0));
			orderItem.setTotaladjustment(new BigDecimal(0));
			orderItem.setField2(addToCartDTO.getDayOfSip());
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
			if(addToCartDTO.getInvestmentType().equals("LUMPSUM")){
				SimpleDateFormat sdf = new SimpleDateFormat("dd MMMM yyyy");
				orderItem.setDescription(sdf.format(new Date()));
				addToCartDTO.setCreationDate(new Date());
			}
			else{
				String sipStartDate=null;
				String getDayOfSip=null;
				String dayOfSip = addToCartDTO.getDayOfSip();
				if(dayOfSip.length()==3){
					String day = dayOfSip.substring(0,1);
					getDayOfSip="0"+day;
				}
				if(dayOfSip.length()==4){
					getDayOfSip = dayOfSip.substring(0,2);
				}
				sipStartDate = GoForWealthPRSUtil.getSipStartDate(getDayOfSip);
				orderItem.setDescription(sipStartDate);
			}
			orderItems.add(orderItem);
			orders.setOrderItems(orderItems);
			orderRepository.save(orders);
			addToCartDTO.setOrderId(orders.getOrdersId());
			String status = "success";
			addToCartDTO.setStatus(status);
		}catch(Exception e){
			e.printStackTrace();
			addToCartDTO.setStatus("Internal Server Error!");
		}
		return addToCartDTO;
	}

	public AddToCartDTO getCartOrderByOrder(Integer userId, Integer orderId) {
		Orders orders = orderRepository.findByOrdersId(orderId);
		AddToCartDTO addToCartDTO =null;	
		if(orders != null){
			for (OrderItem orderItem : orders.getOrderItems()) {
				addToCartDTO = new AddToCartDTO();
				addToCartDTO.setOrderId(orderItem.getOrders().getOrdersId());
				addToCartDTO.setAmount(orderItem.getProductprice().doubleValue());
				addToCartDTO.setDayOfSip(orderItem.getField2());
				addToCartDTO.setSchemeId(orderItem.getScheme().getSchemeId());
				addToCartDTO.setSchemeName(orderItem.getScheme().getSchemeName());
				addToCartDTO.setStatus(orderItem.getStatus());
				if(orderItem.getOrderCategory() != null && !orderItem.getOrderCategory().equals("")){
					addToCartDTO.setOrderCategory(orderItem.getOrderCategory());
				}
				if(orderItem.getModelPortfolioCategory() != null && !orderItem.getModelPortfolioCategory().equals("")){
					addToCartDTO.setModelPortfolioCategory(orderItem.getModelPortfolioCategory());
				}
			}
		}
		return addToCartDTO;
	}

	@Override
	public String makePayment(List<AddToCartDTO> addToCartDTO, Integer userId) throws GoForWealthPRSException {
		String response = null;
		int paymentAmount = 0;
		boolean isPaymentAllowed = false;
		String[] Order= new String[addToCartDTO.size()];
		String amount = "";
		int i = 0;
		for (AddToCartDTO addToCartDTO2 : addToCartDTO) {
			Orders order = orderRepository.findByOrdersId(addToCartDTO2.getOrderId());
			if(order != null){
				if(order.getType().equals("SIP")){
					response = paymentService.isOrderAuthenticated(order,userId);
					if(!response.equals("success")){
						isPaymentAllowed = false;
						return response;
					}else{
						Orders order12 = orderRepository.findByOrdersId(order.getOrdersId());
						paymentAmount =paymentAmount+Integer.parseInt(addToCartDTO2.getPaymentAmount());
						Order[i] = order12.getBseOrderId();
						i++;
						isPaymentAllowed = true;
					}

				}else{
					paymentAmount =paymentAmount+Integer.parseInt(addToCartDTO2.getPaymentAmount());
					Order[i] = order.getBseOrderId();
					i++;
					isPaymentAllowed = true;
				}
			}
		}
		if(isPaymentAllowed == true){
			amount = String.valueOf(paymentAmount);
			response = makePayment(Order, userId, amount, addToCartDTO);
		}
		return response;
	}

	public String makePayment(String[] Order, Integer userId,String amount,List<AddToCartDTO> addToCartDTO) throws GoForWealthPRSException {	

		String ordersId = "MPP";
		for(int i = 0;i<addToCartDTO.size();i++){
			ordersId = ordersId+"_"+addToCartDTO.get(i).getOrderId().toString();
		}
		String paymentResponse="";
		User user=userRepository.getOne(userId);
		paymentResponse = paymentService.doBulkOrderPayment(userId, Order, amount);//Calling Payment Api

		StoreConf imageUrl = storeConfRepository.findByKeyword(GoForWealthPRSConstants.IMAGE_LOCATION);
		StoreConf path1 = storeConfRepository.findByKeyword(GoForWealthPRSConstants.PAYMENT_RESPONSE_HTML_LOCATION);
		StoreConf forwardSlash = storeConfRepository.findByKeyword(GoForWealthPRSConstants.FORWARD_SLASH);
		String path=imageUrl.getKeywordValue()+path1.getKeywordValue();
		String destPath=path+forwardSlash.getKeywordValue()+userId.toString()+"_"+ordersId+".html";
		if (!paymentResponse.contains("Please check your mail") && !paymentResponse.contains("Payment initiated for given OrderNo. Please wait some time.")) {
			for (AddToCartDTO addToCartDTO2 : addToCartDTO) {
				Orders order = orderRepository.findByOrdersId(addToCartDTO2.getOrderId());
				Ppcpayinst ppcpayinst= new Ppcpayinst();
				ppcpayinst.setAmount(new BigDecimal(addToCartDTO2.getPaymentAmount()));
				ppcpayinst.setAccountNo(user.getBankDetails().getAccountNo());
				ppcpayinst.setOrders(order);
				ppcpayinst.setState("P");
				ppcpayinst.setApprovingamount(new BigDecimal(addToCartDTO2.getPaymentAmount()));
				ppcpayinst.setApprovedamount(new BigDecimal(0.0));
				ppcpayinst.setCreditedamount(new BigDecimal(0.0));
				ppcpayinst.setCreditingamount(new BigDecimal(addToCartDTO2.getPaymentAmount()));
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
		}/*else if(paymentResponse.contains("Payment initiated for given OrderNo. Please wait some time.")){
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

				for (AddToCartDTO addToCartDTO2 : addToCartDTO) {
					Orders order = orderRepository.findByOrdersId(addToCartDTO2.getOrderId());
					Ppcpayinst ppcpayinst= new Ppcpayinst();
					ppcpayinst.setAmount(new BigDecimal(addToCartDTO2.getPaymentAmount()));
					ppcpayinst.setAccountNo(user.getBankDetails().getAccountNo());
					ppcpayinst.setOrders(order);
					ppcpayinst.setState("P");
					ppcpayinst.setApprovingamount(new BigDecimal(addToCartDTO2.getPaymentAmount()));
					ppcpayinst.setApprovedamount(new BigDecimal(0.0));
					ppcpayinst.setCreditedamount(new BigDecimal(0.0));
					ppcpayinst.setCreditingamount(new BigDecimal(addToCartDTO2.getPaymentAmount()));
					ppcpayinst.setCurrency("INR");
					ppcpayinst.setMarkfordelete(0);
					Ppcpaytran ppcpaytran= new Ppcpaytran();
					ppcpaytran.setPpcpayinst(ppcpayinst);
					ppcpaytran.setProcessedamount(new BigDecimal(0.0));
					ppcpaytran.setState(0);
					ppcpaytranRepository.save(ppcpaytran);
				}
			} catch (IOException e) {
				e.printStackTrace(); 
				paymentResponse = "101|Payment initiated for given OrderNo. Please wait some time.";
			}

		}*/
		return paymentResponse;
	}

	
	@Override
	public List<AddToCartDTO> getPaymentStatus(Integer mpBundleId) {
		List<AddToCartDTO> adToCartDTOs = new ArrayList<AddToCartDTO>();
		List<Orders> orderList = orderRepository.findByBundelId(mpBundleId);
		List<AddToCartDTO> addToCartDTO = new ArrayList<>();
		for (Orders orders : orderList) {
			AddToCartDTO addToCartDTOObj = new AddToCartDTO();
			addToCartDTOObj.setOrderId(orders.getOrdersId());
			addToCartDTO.add(addToCartDTOObj);
		}
		String response = null;
		String isPaymentSuccess = "";
		String somethingWentWrong = "";
		for (AddToCartDTO addToCartDTO2 : addToCartDTO) {
			response =goForWealthFundSchemeService.getPaymentStatus(addToCartDTO2.getOrderId());
			Orders order = orderRepository.findByOrdersId(addToCartDTO2.getOrderId());
			OrderItem orderItem = orderItemRepository.getOrderItemByOrderId(addToCartDTO2.getOrderId());
			AddToCartDTO addToCartDTO3 = new AddToCartDTO();
			if(response.contains("AWAITING FOR FUNDS CONFIRMATION")){
				addToCartDTO3.setStatus("Success");
				isPaymentSuccess = "S";
			}else if(response.contains("APPROVED")){
				addToCartDTO3.setStatus("Success");
				isPaymentSuccess = "S";
			}
			else if(response.contains("AWAITING FOR RESPONSE FROM BILLDESK")){
				addToCartDTO3.setStatus("Failed");
			}else{
				somethingWentWrong = "T";
				addToCartDTO3.setStatus("Failed");
			}
			addToCartDTO3.setOrderId(Integer.parseInt(order.getBseOrderId()));
			addToCartDTO3.setInvestmentType(orderItem.getScheme().getSchemeName());
			DecimalFormat df = new DecimalFormat("##");
			String amount = df.format((Math.round(orderItem.getProductprice().doubleValue() * 100.0) / 100.0));
			addToCartDTO3.setPaymentAmount(amount);
			SimpleDateFormat sdf = new SimpleDateFormat("dd MMMM yyyy");
			String orderDate = sdf.format(order.getTimeplaced());
			addToCartDTO3.setOrderDate(orderDate);
			addToCartDTO3.setType(order.getType());
			adToCartDTOs.add(addToCartDTO3);
		}
		for (AddToCartDTO addToCartDTO2 : adToCartDTOs) {
			if(isPaymentSuccess.equals("S")){
				addToCartDTO2.setFinalResponse(response);
			}else if(somethingWentWrong.equals("T")){
				addToCartDTO2.setFinalResponse(response);
			}else{
				addToCartDTO2.setFinalResponse("AWAITING FOR RESPONSE FROM BILLDESK");
			}
		}
		return adToCartDTOs;
	}

	@Override
	public List<AddToCartDTO> getUserOrderByType(Integer userId,String type) {
		List<AddToCartDTO> addToCartDTOsList =  new ArrayList<AddToCartDTO>();
		List<Orders> ordersList=null;
		Set<OrderItem> orderItems;
		int bundleId = 0;
		if(type.equals("SIP")){
			ordersList = orderRepository.getSipOrderCategoryMPO(userId, type);
		}
		if(type.equals("LUMPSUM")){
			ordersList = orderRepository.getLumpsumOrderCategoryMPO(userId, type);
		}
		if(type.equals("Investment")){
			ordersList = orderRepository.getInvestedOrderCategoryMPO(userId);
		}
		if(!ordersList.isEmpty()){
			for (Orders orders : ordersList) {
				orderItems = orders.getOrderItems();
				for (OrderItem orderItem : orderItems) {
					if(bundleId !=orderItem.getmPBundleId()){
						bundleId = orderItem.getmPBundleId();
						AddToCartDTO addToCartDTO = new AddToCartDTO();
						addToCartDTO.setmPBundleId(orders.getmPBundleId());
						addToCartDTO.setModelPortfolioCategory(orders.getModelPortfolioCategory());
						addToCartDTO.setCreationDate(orderItem.getLastupdate());
						addToCartDTOsList.add(addToCartDTO);
					}
				}
			}
		}
		return addToCartDTOsList;
	}

	@Override
	public List<AddToCartDTO> getUserOrderListByBundleId(Integer userId, Integer bundleId) {
		List<AddToCartDTO> addToCartDTOsList =  new ArrayList<AddToCartDTO>();
		Set<OrderItem> orderItems;
		String type = "";
		List<Orders> ordersList = orderRepository.getUserOrderListByBundleId(bundleId);
		if(!ordersList.isEmpty()){
			for (Orders orders : ordersList) {
				String mandateId = null;
				String bseOrderId = null;
				String lumpsumOrderId = null;
				
				if(orders.getField2() != null && !orders.getField2().equals("N") && orders.getType().equals("SIP")){
					bseOrderId = orders.getBseOrderId();
				}
				if(orders.getType().equals("LUMPSUM")){
					lumpsumOrderId = orders.getBseOrderId();
				}
				if(orders.getMandateId() != null){
					mandateId = orders.getMandateId();
				}
				type = orders.getType();
				orderItems = orders.getOrderItems();						
				for (OrderItem orderItem : orderItems) {
					AddToCartDTO addToCartDTO = new AddToCartDTO();
					addToCartDTO.setOrderId(orderItem.getOrders().getOrdersId());
					addToCartDTO.setAmount(orderItem.getProductprice().doubleValue());
					addToCartDTO.setDayOfSip(orderItem.getField2());
					
					if(type.equals("SIP") && orderItem.getStatus().equals("P")){
						getNextSipDate(orderItem,addToCartDTO);
					}
					
					addToCartDTO.setSchemeId(orderItem.getScheme().getSchemeId());
					addToCartDTO.setSchemeName(orderItem.getScheme().getSchemeName());
					addToCartDTO.setStatus(orderItem.getStatus());
					addToCartDTO.setCreationDate(orderItem.getLastupdate());
					addToCartDTO.setInvestmentType(orders.getType());
					addToCartDTO.setmPBundleId(orderItem.getmPBundleId());
					addToCartDTO.setModelPortfolioCategory(orderItem.getModelPortfolioCategory());
					if(mandateId != null && !mandateId.equals("")){
						addToCartDTO.setMandateId(mandateId);
					}else{
						addToCartDTO.setMandateId("Not Created");
					}
					if(bseOrderId != null && !bseOrderId.equals("")){
						addToCartDTO.setBseOrderId(bseOrderId);
					}else{
						addToCartDTO.setBseOrderId("Not Created");
					}
					if(lumpsumOrderId != null && !lumpsumOrderId.equals("")){
						addToCartDTO.setLumpsumOrderId(lumpsumOrderId);
					}else{
						addToCartDTO.setLumpsumOrderId("Not Created");
					}
					addToCartDTOsList.add(addToCartDTO);
				}

			}
		}
		return addToCartDTOsList;
	}
	
	public boolean checkDatesWithSameMonth(String addToCartDate,String todayDate){
		boolean isTrue = false;
		SimpleDateFormat sdf1 = new SimpleDateFormat("dd MMM yyyy");
		SimpleDateFormat sdf12 = new SimpleDateFormat("MMM");
		try {
			Date addToCartDate1 = sdf1.parse(addToCartDate);
			Date todayDate1 = sdf1.parse(todayDate);
			String addToCartDate12 = sdf12.format(addToCartDate1);
			String todayDate12 = sdf12.format(todayDate1);
			if(addToCartDate12.equals(todayDate12)){
				isTrue = true;
			}
		} catch (ParseException e) {
			
			e.printStackTrace();
		}
		return isTrue;
	}
	
	public void getNextSipDate(OrderItem orderItem2, AddToCartDTO addToCartDTO){
		String sipDatesList = orderItem2.getScheme().getSipDates();
		String sipStartDate=orderItem2.getDescription();
		String dayOfSip = "";
		String[]sipDates =  sipDatesList.split(",");
		String addToCartDate = sipStartDate;
		SimpleDateFormat sdf1 = new SimpleDateFormat("dd MMM yyyy");
		String todayDate = sdf1.format(new Date());
		String isMinusValueContain = String.valueOf((addToCartDate.compareTo(todayDate)));
		boolean istrue = checkDatesWithSameMonth(addToCartDate,todayDate);
		if(sipStartDate != null){
			if(addToCartDate.compareTo(todayDate)<=0 || isMinusValueContain.contains("-")){
				if(istrue){
					String day ="";
					LocalDate currentDate = LocalDate.now();
					int dom = currentDate.getDayOfMonth();
					int nextDate = dom+1;
					boolean found = false;
					for(int i=0;i<sipDates.length;i++) {
						if(nextDate==Integer.parseInt(sipDates[i])){
							day = String.valueOf(nextDate);
							dayOfSip = String.valueOf(nextDate);
							found = true;
							break;
						}else if(nextDate>Integer.parseInt(sipDates[i])){
							found = false;
						}else{
							found = true;
							day = String.valueOf(sipDates[i]);
							dayOfSip = String.valueOf(sipDates[i]);
							break;
						}
					}
					if(!found){
						day = sipDates[0];
						dayOfSip = sipDates[0];
					}

					if(day.length() == 1){
						day = "0"+day;
					}

					sipStartDate = GoForWealthPRSUtil.getSipStartDate(day);
					orderItem2.setDescription(sipStartDate);
					orderItem2.setField2(GoForWealthPRSUtil.createDayOfSip(dayOfSip));
					orderItemRepository.save(orderItem2);
					addToCartDTO.setSipDate(GoForWealthPRSUtil.createDayOfSip(dayOfSip));
					String[] sipDateList = new String[sipDates.length];
					for(int i =0;i<sipDates.length;i++){
						String sipDate1 = new String();
						sipDate1 = GoForWealthPRSUtil.createDayOfSip(sipDates[i]);
						sipDateList[i] = sipDate1;
					}
					addToCartDTO.setSipDateList(sipDateList);
				}else{
					addToCartDTO.setSipDate(orderItem2.getField2());
					String[] sipDateList = new String[sipDates.length];
					for(int i =0;i<sipDates.length;i++){
						String sipDate1 = new String();
						sipDate1 = GoForWealthPRSUtil.createDayOfSip(sipDates[i]);
						sipDateList[i] = sipDate1;
					}
					addToCartDTO.setSipDateList(sipDateList);
				}
			}else{
				addToCartDTO.setSipDate(orderItem2.getField2());
				String[] sipDateList = new String[sipDates.length];
				for(int i =0;i<sipDates.length;i++){
					String sipDate1 = new String();
					sipDate1 = GoForWealthPRSUtil.createDayOfSip(sipDates[i]);
					sipDateList[i] = sipDate1;
				}
				addToCartDTO.setSipDateList(sipDateList);
			}
		}
	}
	
	@Override
	public FundSchemeDTO getUserOrderByType(User user) {
     List<Orders> ordersList =  orderRepository.getOrderDetailWithStatusCAndPA(user.getUserId());
     int totalPurchageAmount = 0;
		int totalPurchageAmountRef = 0;
		FundSchemeDTO fundSchemeDTO = new FundSchemeDTO();
		if(!ordersList.isEmpty()){
			for (Orders orders : ordersList) {
				OrderItem orderItem = orderItemRepository.getOrderItemByOrderId(orders.getOrdersId());
				if(orderItem != null){
					if(orders.getType().equals("SIP")){
						int totalPurchageAmountRef1 = orderItem.getProductprice().intValueExact()*12;
						totalPurchageAmountRef = totalPurchageAmountRef+totalPurchageAmountRef1;
					}if(orders.getType().equals("LUMPSUM")){
						totalPurchageAmountRef = totalPurchageAmountRef+orderItem.getProductprice().intValueExact();
					}
				}
			}
			totalPurchageAmount = totalPurchageAmountRef;
		}else{
			totalPurchageAmount = 0;
		}
		fundSchemeDTO.setTotalPurchageAmount(totalPurchageAmount);
		String kycStatus = "";
		int overAllStatus = user.getOnboardingStatus().getOverallStatus();
		if(user.getPanDetails().getVerified() != null){
		  kycStatus = user.getPanDetails().getVerified();
		}
	    if(kycStatus.equals("verified") && overAllStatus == 1){
	    	fundSchemeDTO.setIsOrderAllowed("true");
	    }if(kycStatus.equals("not-verified") && overAllStatus == 1){
	    	fundSchemeDTO.setIsOrderAllowed("false");
	    }
		return fundSchemeDTO;
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


}