package com.moptra.go4wealth.sip.service;

import java.io.IOException;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Locale;
import java.util.Optional;
import java.util.stream.Collectors;

import javax.transaction.Transactional;

import org.apache.poi.ss.formula.functions.FinanceLib;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.context.NoSuchMessageException;
import org.springframework.stereotype.Service;

import com.moptra.go4wealth.bean.AddressState;
import com.moptra.go4wealth.bean.AssetClass;
import com.moptra.go4wealth.bean.AssetClassInternal;
import com.moptra.go4wealth.bean.Cities;
import com.moptra.go4wealth.bean.GoalBucket;
import com.moptra.go4wealth.bean.GoalOrderItems;
import com.moptra.go4wealth.bean.Goals;
import com.moptra.go4wealth.bean.GoalsOrder;
import com.moptra.go4wealth.bean.IncomeSlabs;
import com.moptra.go4wealth.bean.MaritalStatus;
import com.moptra.go4wealth.bean.Scheme;
import com.moptra.go4wealth.bean.TopSchemes;
import com.moptra.go4wealth.bean.User;
import com.moptra.go4wealth.bean.UserAssetItems;
import com.moptra.go4wealth.configuration.DownloadReportConfiguration;
import com.moptra.go4wealth.repository.AddressStateRepository;
import com.moptra.go4wealth.repository.AgeSlabsRepository;
import com.moptra.go4wealth.repository.AssetClassInternalRepository;
import com.moptra.go4wealth.repository.AssetClassRepository;
import com.moptra.go4wealth.repository.CitiesRepository;
import com.moptra.go4wealth.repository.GoalBucketRepository;
import com.moptra.go4wealth.repository.GoalOrderItemsRepository;
import com.moptra.go4wealth.repository.GoalsOrderRepository;
import com.moptra.go4wealth.repository.GoalsRepository;
import com.moptra.go4wealth.repository.IncomeSlabsRepository;
import com.moptra.go4wealth.repository.InflationRepository;
import com.moptra.go4wealth.repository.KidsSlabsRepository;
import com.moptra.go4wealth.repository.MaritalStatusRepository;
import com.moptra.go4wealth.repository.RiskBearingCapacityRepository;
import com.moptra.go4wealth.repository.SchemeRepository;
import com.moptra.go4wealth.repository.TopSchemeRepository;
import com.moptra.go4wealth.repository.UserAssetItemsRepository;
import com.moptra.go4wealth.repository.UserRepository;
import com.moptra.go4wealth.sip.common.constant.GoForWealthSIPConstants;
import com.moptra.go4wealth.sip.common.enums.GoalBucketCodeEnum;
import com.moptra.go4wealth.sip.common.enums.GoalOrderStateEnum;
import com.moptra.go4wealth.sip.common.enums.GoalTypeEnum;
import com.moptra.go4wealth.sip.common.enums.InflationTypeEnum;
import com.moptra.go4wealth.sip.common.enums.KidsSlabCodeEnum;
import com.moptra.go4wealth.sip.common.enums.MaritalStatusEnum;
import com.moptra.go4wealth.sip.common.exception.GoForWealthSIPException;
import com.moptra.go4wealth.sip.model.AgeSlabDTO;
import com.moptra.go4wealth.sip.model.AssetClassDTO;
import com.moptra.go4wealth.sip.model.CalculateSipRequestDto;
import com.moptra.go4wealth.sip.model.CalculateSipResponseDto;
import com.moptra.go4wealth.sip.model.CityDTO;
import com.moptra.go4wealth.sip.model.DownloadReportRequestDTO;
import com.moptra.go4wealth.sip.model.GoalBucketDto;
import com.moptra.go4wealth.sip.model.GoalDTO;
import com.moptra.go4wealth.sip.model.GoalOrderResponse;
import com.moptra.go4wealth.sip.model.IncomeDTO;
import com.moptra.go4wealth.sip.model.KidsSlabDTO;
import com.moptra.go4wealth.sip.model.MaritalDTO;
import com.moptra.go4wealth.sip.model.ReturnsTypeDTO;
import com.moptra.go4wealth.sip.model.StateDTO;
import com.moptra.go4wealth.sip.model.SuggestSchemeDTO;
import com.moptra.go4wealth.sip.model.UserGoalDto;
import com.moptra.go4wealth.uma.common.constant.GoForWealthUMAConstants;
import com.moptra.go4wealth.uma.common.enums.GoForWealthErrorMessageEnum;
import com.moptra.go4wealth.util.MailUtility;

/**
 * 
 * @author Gaurav Pandey
 *
 */
@Service
public class GoForWealthSIPServiceImpl implements GoForWealthSIPService {

	private static Logger logger = LoggerFactory.getLogger(GoForWealthSIPServiceImpl.class);

	@Autowired
	private CitiesRepository citiesRepository;

	@Autowired
	private IncomeSlabsRepository incomeSlabsRepository;

	@Autowired
	private MaritalStatusRepository maritalStatusRepository;

	@Autowired
	private AgeSlabsRepository ageSlabsRepository;

	@Autowired
	private KidsSlabsRepository kidsSlabsRepository;

	@Autowired
	private GoalsRepository goalsRepository;

	@Autowired
	private AssetClassRepository assetClassRepository;

	@Autowired
	private InflationRepository inflationRepository;

	@Autowired
	private RiskBearingCapacityRepository riskBearingCapacityRepository;

	@Autowired
	private AssetClassInternalRepository assetClassInternalRepository;

	@Autowired
	private MessageSource messageSource;

	@Autowired
	private AddressStateRepository addressStateRepository;

	@Autowired
	private GoalsOrderRepository goalsOrderRepository;

	@Autowired
	private GoalBucketRepository goalBucketRepository;

	@Autowired
	private UserRepository userRepository;

	@Autowired
	private GoalOrderItemsRepository goalOrderItemsRepository;

	@Autowired
	private UserAssetItemsRepository userAssetItemsRepository;
	
	@Autowired
	TopSchemeRepository topSchemeRepository;
	
	@Autowired
	SchemeRepository schemeRepository;
	
	@Autowired
	private DownloadReportConfiguration downloadReportConfiguration;
	
	@Autowired
	MailUtility mailUtility;


	@Override
	public List<CityDTO> getCityList() {
		return citiesRepository.findAll().stream().map(city -> new CityDTO(city.getCityId(), city.getCityName())).collect(Collectors.toList());
	}

	@Override
	public List<StateDTO> getStateList() {
		List<AddressState> addressStateList = addressStateRepository.findAll();
		List<StateDTO> stateDtoList = new ArrayList<>();
		for (AddressState addressState : addressStateList) {
			StateDTO stateDto = new StateDTO();
			stateDto.setStateId(addressState.getAddressStateId());
			stateDto.setStateName(addressState.getAddressStatename());
			stateDtoList.add(stateDto);
		}
		return stateDtoList;
	}

	@Override
	public List<MaritalDTO> getMaritalList() {
		return maritalStatusRepository.findAll().stream().map(m -> new MaritalDTO(m.getMaritalStatusId(), m.getMaritalStatusName(), m.getMaritalStatusValue())).collect(Collectors.toList());
	}

	private BigDecimal convertToBigDecimal(double lumpsumTotal) {
		return new BigDecimal(lumpsumTotal).setScale(2, RoundingMode.HALF_UP);
	}

	@Override
	public List<GoalDTO> getGoalsList() {
		return goalsRepository.getGoalList();
	}

	@Override
	public List<IncomeDTO> getIncomeSlabList() {
		return incomeSlabsRepository.getIncomeSlabsList();
	}

	@Override
	public List<AgeSlabDTO> getAgeSlabList() {
		return ageSlabsRepository.getAgeSlabList();
	}

	@Override
	public List<MaritalDTO> getMaritalSlabList() {
		return maritalStatusRepository.getMaritalSlabList();
	}

	@Override
	public List<KidsSlabDTO> getKidsSlabList() {
		return kidsSlabsRepository.getKidsSlabList();
	}

	@Override
	public List<AssetClassDTO> getAssetClassList() {
		List<AssetClassDTO> assetClassDtoList = new ArrayList<>();
		List<AssetClass> assetClassList = assetClassRepository.findAll();
		for (AssetClass assetClass : assetClassList) {
			AssetClassDTO assetClassDto = new AssetClassDTO();
			assetClassDto.setAssetClass(assetClass.getAssetClass());
			assetClassDto.setAssetClassId(assetClass.getAssetClassId());
			assetClassDto.setAssetClassRoi(assetClass.getRoiExptd().intValue());
			assetClassDtoList.add(assetClassDto);
		}
		return assetClassDtoList;
	}

	@Override
	public List<ReturnsTypeDTO> getRiskProfileList() {
		return riskBearingCapacityRepository.getRiskProfileList();
	}
	
	@Override
	public UserGoalDto getGoalOrderDetailV2(Integer userId, Integer orderId) {
		Integer orderIds = null;
		UserGoalDto response = new UserGoalDto();
		List<GoalOrderItems> goalOrderItemsObj = goalOrderItemsRepository.findByUserId(userId);
		if(goalOrderItemsObj.size()==0){
			return response;
		}else{
			if(orderId == 0) {
				orderIds = goalOrderItemsObj.get(0).getGoalsOrder().getGoalsOrderId();
			} else {
			orderIds = orderId;
			}
			GoalsOrder goalsOrder = goalsOrderRepository.findByUserIdAndOrderId(userId,orderIds);
			if(goalsOrder != null){
				response.setRoi(goalsOrder.getRoi().intValue());
				response.setInflation(goalsOrder.getInflation().intValue());
				response.setTotalFutureValue(goalsOrder.getTotalFutureValue().intValue());
				response.setTotalLumpsumSaving(goalsOrder.getTotalLumpsumSaving().intValue());
				response.setTotalMonthlySaving(goalsOrder.getTotalMonthlySaving().intValue());
				response.setRiskProfile(goalsOrder.getRiskProfile());
				response.setNetLumpsumSaving(goalsOrder.getTotalLumpsumAdjusment().intValue());
				response.setNetMonthlySaving(goalsOrder.getTotalMonthlyAdjustment().intValue());
				List<GoalDTO> goalDtoList = new ArrayList<>();
				List<GoalOrderItems> goalOrderItemsList = new ArrayList<>(goalsOrder.getGoalOrderItemses());
				for (int i = 0; i < goalOrderItemsList.size(); i++) {
					GoalDTO goalDto = new GoalDTO();
					goalDto.setGoalId(goalOrderItemsList.get(i).getGoalOrderItemId());
					goalDto.setGoalName(goalOrderItemsList.get(i).getDescription());
					goalDto.setCostOfGoal(goalOrderItemsList.get(i).getCurrentCost());
					goalDto.setDuration(goalOrderItemsList.get(i).getDuration().doubleValue());
					goalDto.setFutureValue(goalOrderItemsList.get(i).getFutureValue());
					goalDto.setLumPsumValue(goalOrderItemsList.get(i).getLumpsumSaving());
					goalDto.setPmtFutureValue(goalOrderItemsList.get(i).getMonthlySaving());
					int num = goalOrderItemsList.get(i).getField1();
					if(num == 1)
						goalDto.setInvestmentFlag(true);
					else
						goalDto.setInvestmentFlag(false);	
					List<AssetClassDTO> assetClassDtoList = new ArrayList<>();
					List<UserAssetItems> userAssetItemsList = userAssetItemsRepository.findByGoalsOrderItemId(goalOrderItemsList.get(i).getGoalOrderItemId());
					if(userAssetItemsList.size()>0){
						for (UserAssetItems userAssetItems : userAssetItemsList) {
							AssetClassDTO assetClassDto = new AssetClassDTO();
							assetClassDto.setAssetClassId(userAssetItems.getAssetClass().getAssetClassId());
							assetClassDto.setAssetValue(userAssetItems.getAssetValue().intValue());
							assetClassDto.setFutureValue(userAssetItems.getAssetValue().intValue());
							assetClassDto.setAssetClass(userAssetItems.getAssetClass().getAssetClass());
							assetClassDtoList.add(assetClassDto);
						}
					}
					goalDto.setAssetClassDto(assetClassDtoList);
					goalDtoList.add(goalDto);
				}
				response.setGoalDto(goalDtoList);
			}
			return response;
		}
	}

	public Optional<GoalBucket> getGoalBucketFromDuration(double duration) {
		if (duration >0 && duration <=3) {
			return goalBucketRepository.findByGoalBucketCode(GoalBucketCodeEnum.SHORT_TERM.getCode());
		}else if(duration >3 && duration <=7){
			return goalBucketRepository.findByGoalBucketCode(GoalBucketCodeEnum.MEDIUM_TERM.getCode());
		}else{
			return goalBucketRepository.findByGoalBucketCode(GoalBucketCodeEnum.LONG_TERM.getCode());
		}
	}
	
	private Double getFutureValue(Double pv, Double r, Double n) {
		return pv * Math.pow((1 + r / 100), n);
	}

	private Double getMonthlySip(Double fv, Double r, int nper) {
		nper = nper * 12;
		r = r / (100 * 12);
		return -r * fv / (Math.pow(1 + r, nper) - 1);
	}

	private Double getLumpSumSaving(Double fv, Double r, int nper, double pmt) {
		r = r / (100 * 12);
		nper = nper * 12;
		return FinanceLib.pv(r, nper, pmt, fv, false);
	}
	
	private Double getCorpusValue(Double r, int nper, Double fv, double pmt) {
		pmt = fv;
		return FinanceLib.pv(r,nper,pmt,0,false);
	}

	static public double pmt(double r, int nper, double pv, double fv, int type) {
		double pmt = -r * (pv * Math.pow(1 + r, nper) + fv) / ((1 + r * type) * (Math.pow(1 + r, nper) - 1));
		return pmt;
	}

	@Override
	public List<IncomeDTO> getAllIncomeSlabs() {
		List<IncomeDTO> incomeDtoList = new ArrayList<>();
		List<IncomeSlabs> incomeSlabsList = incomeSlabsRepository.findAll();
		for (IncomeSlabs incomeSlabs : incomeSlabsList) {
			IncomeDTO incomeDto = new IncomeDTO();
			incomeDto.setIncomeSlabId(incomeSlabs.getIncomeSlabId());
			incomeDto.setIncomeSlabCode(incomeSlabs.getIncomeSlabCode());
			incomeDto.setIncomeSlabName(incomeSlabs.getIncomeSlabName());
			incomeDto.setIncomeFrom(incomeSlabs.getIncomeFrom());
			incomeDto.setIncomeTo(incomeSlabs.getIncomeTo());
			incomeDtoList.add(incomeDto);
		}
		return incomeDtoList;
	}

	@Override
	public List<GoalDTO> getPredefinedGoalList() {
		List<Goals> goals = goalsRepository.findAllPreDefineGoals();
		List<GoalDTO> goalDtoList = new ArrayList<>();
		for (Goals goals2 : goals) {
			if(goals2.getGoalType().equals("PD")){
				GoalDTO goalDto = new GoalDTO();
				goalDto.setGoalId(goals2.getGoalId());
				goalDto.setGoalName(goals2.getGoalName());
				goalDto.setCostOfGoal(goals2.getCostOfGoal());
				GoalBucketDto goalBucketDto = new GoalBucketDto();
				goalBucketDto.setGoalBucketId(goals2.getGoalBucket().getGoalBucketId());
				goalBucketDto.setGoalBucketName(goals2.getGoalBucket().getGoalBucketName());
				goalDto.setDuration(goals2.getDuration().doubleValue());
				goalDto.setGoalBucket(goalBucketDto);
				goalDtoList.add(goalDto);
			}
		}
		return goalDtoList;
	}

	@Override
	public Integer getUserIdByGoalsOrderId(Integer goalsOrderId) {
		GoalsOrder goalsOrder = goalsOrderRepository.getOne(goalsOrderId);
		Integer userId = goalsOrder.getUser().getUserId();
		return userId;
	}

	public List<SuggestSchemeDTO> setSuggestScheme(String fundCategory){
		List<SuggestSchemeDTO> suggestSchemeDtoList = new ArrayList<>();
		List<String> suggestedFundTypeLists = assetClassInternalRepository.getFundsListByFundType(Arrays.asList(fundCategory.split("\\+")));
		for (String fundType : suggestedFundTypeLists) {
			List<TopSchemes> topSchemesList = topSchemeRepository.findBySchemeCategory(fundType);
			for (TopSchemes topSchemes : topSchemesList) {
				String schemeCode = topSchemes.getSchemeCode();
				Scheme scheme = schemeRepository.findBySchemeCode(schemeCode);
				if(scheme!=null){
					SuggestSchemeDTO suggestSchemeDto = new SuggestSchemeDTO();
					suggestSchemeDto.setSchemeId(scheme.getSchemeId());
					suggestSchemeDto.setSchemeCode(scheme.getSchemeCode());
					suggestSchemeDto.setSchemeName(scheme.getSchemeName());
					suggestSchemeDto.setSchemeLaunchDate(scheme.getStartDate());
					suggestSchemeDto.setMinimumPurchaseAmount(scheme.getMinimumPurchaseAmount());
					suggestSchemeDto.setSchemeKeyword(scheme.getKeyword());
					suggestSchemeDto.setSchemeType(scheme.getSchemeType());
					suggestSchemeDtoList.add(suggestSchemeDto);
				}
			}
		}
		return suggestSchemeDtoList;
	}

	@Override
	public User getUserByUserId(Integer userId) {
		User user = userRepository.findByUserId(userId);
		if(user!=null){
			return user;
		}
		return null;
	}

	@Override
	public String sendDownloadReportUrlToEmail(User user,DownloadReportRequestDTO downloadReportRequestDto,String email) throws GoForWealthSIPException{
		if(user == null) {
			throw new GoForWealthSIPException(GoForWealthErrorMessageEnum.DATA_NOT_FOUND_CODE.getValue(),GoForWealthErrorMessageEnum.USER_DETAIL_NOT_EXIST.getValue());
		}
		try {
			String path = "locId="+downloadReportRequestDto.getCurrResidenceLocationId()+"&age="+downloadReportRequestDto.getAgeval()+"&income="+downloadReportRequestDto.getIncomeval()+"&incomeSlabCode="+downloadReportRequestDto.getIncomeSlabCode()+"&marital="+downloadReportRequestDto.getMaritalStatusId()+"&kids="+downloadReportRequestDto.getKidsval()+"&goalsOrderId="+downloadReportRequestDto.getGoalsOrderId()+"";
			String downloadReportLink ="<a href="+ downloadReportConfiguration.downloadReportUrl + path +"> here </a>";
			mailUtility.baselineExample(email,messageSource.getMessage("downloadreport.mailSubject", null, Locale.ENGLISH), messageSource.getMessage("downloadreport.mailBody", new String[] {"User",downloadReportLink}, Locale.ENGLISH));
		} catch (NoSuchMessageException | IOException e) {
			throw new GoForWealthSIPException(GoForWealthErrorMessageEnum.COMMON_ERROR_CODE.getValue(),GoForWealthSIPConstants.MAIL_SEND_FAILURE_MESSAGE);
		}
		return GoForWealthSIPConstants.MAIL_SEND_SUCCESS_MESSAGE;
	}

	public boolean recalculateAfterRiskProfile(GoalsOrder goalsOrder,BigDecimal avgNewRoi){
		boolean flag = false;
		try{
			for(GoalOrderItems goalOrderItems : goalsOrder.getGoalOrderItemses()){
				double r = goalsOrder.getInflation().doubleValue();
				int nper = (int) goalOrderItems.getDuration();
				double pv = goalOrderItems.getCurrentCost().doubleValue();
				double fv = getFutureValue(pv, r, goalOrderItems.getDuration().doubleValue());
				Optional<GoalBucket> goalBucket = getGoalBucketFromDuration(goalOrderItems.getDuration());
				if(goalBucket.isPresent()) {
					if(goalBucket.get().getGoalBucketName().equals("SHORT TERM")){
						fv = pv;
					}
				}
				r = goalsOrder.getRoi().doubleValue();
				double pmtFv = getMonthlySip(-fv, r, nper);
				goalOrderItems.setFutureValue(convertToBigDecimal(fv));
				goalOrderItems.setMonthlySaving(convertToBigDecimal(pmtFv));
				goalOrderItems.setLumpsumSaving(convertToBigDecimal(-(getLumpSumSaving(fv, r, (int) goalOrderItems.getDuration(),0))));
				goalOrderItemsRepository.save(goalOrderItems);
				flag = true;
			}
		}catch(Exception ex){
			flag = false;
		}
		return flag;
	}

	@Override
	public GoalOrderResponse getGoalsOrderItemsDetailsById(Integer goalsOrderItemsId) {
		GoalOrderResponse goalOrderResponse = new GoalOrderResponse();
		try{
			GoalOrderItems goalOrderItems = goalOrderItemsRepository.getOne(goalsOrderItemsId);
			if(goalOrderItems !=null){
				goalOrderResponse.setGoalsOrderItemId(goalOrderItems.getGoalOrderItemId());
				goalOrderResponse.setFutureValue(goalOrderItems.getFutureValue().doubleValue());
				goalOrderResponse.setLumpsumSaving(goalOrderItems.getLumpsumSaving().doubleValue());
				goalOrderResponse.setMonthlySaving(goalOrderItems.getMonthlySaving().doubleValue());
			}
		}catch(Exception ex){
			ex.printStackTrace();
			return goalOrderResponse;
		}
		return goalOrderResponse;
	}

	@Override
	public CalculateSipResponseDto calculateSipV2(CalculateSipRequestDto sipRequestDto) throws GoForWealthSIPException {
		CalculateSipResponseDto calculateSipResponseDto = new CalculateSipResponseDto();
		List<Goals> returnedGoalList = getGoalListBasedOnConditionsV2(sipRequestDto);
		BigDecimal generalInflation = inflationRepository.findByInflationType(InflationTypeEnum.GENERAL.getValue(),"India");
		String[] riskProfile = GoForWealthSIPConstants.DEFAULT_RISK_PROFILE.split("_");
		String returns = riskProfile[0];
		String risk = riskProfile[1];
		String fundTypString = riskBearingCapacityRepository.getFundTypeByReturnsAndRisk(returns, risk);
		Double newRoi = 0.0;
		List<String> suggestedFundTypeList = assetClassInternalRepository.getFundsListByFundType(Arrays.asList(fundTypString.split("\\+")));
		for (String fundType : suggestedFundTypeList) {
			AssetClassInternal assetClassInternal = assetClassInternalRepository.findByFundType(fundType);
			newRoi += assetClassInternal.getRoiExptd().doubleValue();
		}
		BigDecimal avgNominalReturn = new BigDecimal(newRoi/suggestedFundTypeList.size());
		BigDecimal avgInflationAdjustedReturn = assetClassInternalRepository.findAvgBigDecimalRoiExptd(generalInflation);
		calculateSipResponseDto.setAvgInflationAdjustedReturn(avgInflationAdjustedReturn);
		calculateSipResponseDto.setAvgNominalReturn(avgNominalReturn);
		calculateSipResponseDto.setGeneralInflation(generalInflation);
		double currentCostTotal = 0;
		double futureValueTotal = 0;
		double sipTotal = 0;
		double lumpsumTotal = 0;
		List<GoalDTO> goalDTOs = new ArrayList<GoalDTO>();
		List<GoalDTO> goalDTOsUnique = new ArrayList<GoalDTO>();// unique
		for (Goals goal : returnedGoalList) {
			Double duration = null;
			GoalDTO goalDTO = new GoalDTO(goal);
			if(goal.getGoalKey().equals("RP")){
				Integer currentAge = sipRequestDto.getCurrentAge();
				duration = (double) (GoForWealthSIPConstants.AGE_TO_RETIRE-currentAge);
				if(duration == 0.0){
					duration = 5.0;
				}
			}else{
				//duration = getDurationFromGoalBucket(goal.getGoalBucket());
				duration = goal.getDuration().doubleValue();
			}
			if(goal.getGoalKey().equals("RP")){
				double r = generalInflation.doubleValue();
				int nper = duration.intValue();
				double pv = goal.getCostOfGoal().doubleValue();
				if(sipRequestDto.getIncomeVal() <= 3){
					pv = 25000;
				}else if(sipRequestDto.getIncomeVal()>=3 && sipRequestDto.getIncomeVal() <= 10){
					pv = 40000;
				}else{
					pv = 50000;
				}
				double fv = getFutureValue(pv, r, duration);
				int survivalAge = GoForWealthSIPConstants.AVERAGE_AGE_AFTER_RETIRE - GoForWealthSIPConstants.AGE_TO_RETIRE;
				if(survivalAge <= 10){
					survivalAge = 10;
				}
				double firstPart0 = 1+ GoForWealthSIPConstants.RETIREMENT_ROI/100;
				double firstPart1 = 1+ r/100;
				double divOfFirst = firstPart0/firstPart1;
				double afterDivPre = divOfFirst - 1;			
				double afterDivPost = afterDivPre/12;	
				double secondPart2 = (survivalAge*12);
				int npre = (int)secondPart2;
				double thirdPart3 = (-fv);
				double corpusFutureValues = getCorpusValue(afterDivPost,npre,thirdPart3,0);
				r = avgNominalReturn.doubleValue();
				double pmtFv = getMonthlySip(-corpusFutureValues, r, nper);
				double lumpSumFromFv = getLumpSumSaving(-corpusFutureValues, r, nper, 0);
				//fv = corpusFutureValues;
				goalDTO.setFutureValue(convertToBigDecimal(fv));
				goalDTO.setDuration(duration);
				goalDTO.setPmtFutureValue(convertToBigDecimal(pmtFv));
				goalDTO.setLumPsumValue(convertToBigDecimal(lumpSumFromFv));
				goalDTO.setInflation(generalInflation.doubleValue());
				goalDTO.setRate(r);
				goalDTO.setGoalIcon(goal.getGoalIcon());
				goalDTO.setSipValue(convertToBigDecimal(pmtFv));
				goalDTO.setShowToProfileType(goal.getShowToProfileType());
				goalDTO.setCostOfGoal(convertToBigDecimal(pv));
				goalDTOs.add(goalDTO);
				currentCostTotal += pv;
				futureValueTotal += fv;
				sipTotal += pmtFv;
				lumpsumTotal += lumpSumFromFv;
			}else{
				double pv = 0.0;
				if(goal.getGoalKey().equals("CHEA")){
					if(sipRequestDto.getIncomeVal() <= 2){
						pv = 500000;
					}else if(sipRequestDto.getIncomeVal() >= 2 && sipRequestDto.getIncomeVal() <= 3){
						pv = 600000;
					}else if(sipRequestDto.getIncomeVal() >= 3 && sipRequestDto.getIncomeVal() <= 5){
						pv = 1000000;
					}else{
						pv = 1500000;
					}
				}else if(goal.getGoalKey().equals("VPI")){
					pv = (sipRequestDto.getIncomeVal() * 100000) * 0.5;
					if(pv > 200000){
						pv = 200000;
					}
				}else if(goal.getGoalKey().equals("SM")){
					pv = (sipRequestDto.getIncomeVal() * 100000) * 2;
					if(pv > 800000){
						pv = 800000;
					}
				}else {
					pv = goal.getCostOfGoal().doubleValue();
				}
				double r = generalInflation.doubleValue();
				int nper = duration.intValue();
				//double pv = goal.getCostOfGoal().doubleValue();
				double fv = getFutureValue(pv, r, duration);
				if(goal.getGoalKey().equals("M1C")){
					fv = pv;
				}
				Optional<GoalBucket> goalBucket = getGoalBucketFromDuration(duration);
				if(goalBucket.isPresent()) {
					if(goalBucket.get().getGoalBucketName().equals("SHORT TERM")){
						fv = pv;
					}
				}
				// calculate monthly sip amount
				r = avgNominalReturn.doubleValue();
				double pmtFv = getMonthlySip(-fv, r, nper);
				double lumpSumFromFv = getLumpSumSaving(-fv, r, nper, 0);
				goalDTO.setFutureValue(convertToBigDecimal(fv));
				goalDTO.setDuration(duration);
				goalDTO.setPmtFutureValue(convertToBigDecimal(pmtFv));
				goalDTO.setLumPsumValue(convertToBigDecimal(lumpSumFromFv));
				goalDTO.setInflation(generalInflation.doubleValue());
				goalDTO.setRate(r);
				goalDTO.setGoalIcon(goal.getGoalIcon());
				goalDTO.setSipValue(convertToBigDecimal(pmtFv));
				goalDTO.setCostOfGoal(convertToBigDecimal(pv));
				goalDTO.setShowToProfileType(goal.getShowToProfileType());
				goalDTOs.add(goalDTO);
				currentCostTotal += pv;
				futureValueTotal += fv;
				sipTotal += pmtFv;
				lumpsumTotal += lumpSumFromFv;
			}
		}
		for(GoalDTO goalDTOObj : goalDTOs) {
			int i=0;
			if(goalDTOsUnique.size()>0)
			for(GoalDTO goalDT : goalDTOsUnique) {
				if(goalDT.getGoalName()==goalDTOObj.getGoalName()){
					i=1;
				}
			}
			if(i==0){
				goalDTOsUnique.add(goalDTOObj);
			}	
		}
		Collections.sort(goalDTOsUnique);
		List<GoalDTO> modifiedGoalDtosList = new ArrayList<GoalDTO>();
		if(sipRequestDto.getIncomeVal() <= 10){
			int salaryPercentage = ((sipRequestDto.getIncomeVal() * 100000)*40)/100;
			if((int)sipTotal > salaryPercentage){
				int subTotal = 0;
				for (GoalDTO goalDTO2 : goalDTOsUnique) {
					if(subTotal < salaryPercentage){
						subTotal = subTotal + goalDTO2.getSipValue().intValue();
						modifiedGoalDtosList.add(goalDTO2);
					}
				}
			}else{
				modifiedGoalDtosList = goalDTOsUnique;
			}
		}else{
			int salaryPercentage = ((sipRequestDto.getIncomeVal() * 100000)*50)/100;
			if((int)sipTotal > salaryPercentage){
				int subTotal = 0;
				for (GoalDTO goalDTO2 : goalDTOsUnique) {
					if(subTotal < salaryPercentage){
						subTotal = subTotal + goalDTO2.getSipValue().intValue();
						modifiedGoalDtosList.add(goalDTO2);
					}
				}
			}else{
				modifiedGoalDtosList = goalDTOsUnique;
			}
		}
		List<Goals> predefinedGoalList = goalsRepository.findAllPreDefineGoals();
		List<GoalDTO> finalModifiedGoalDtosList = new ArrayList<GoalDTO>();
		finalModifiedGoalDtosList.addAll(modifiedGoalDtosList);
		List<String> sequenceList = new ArrayList<>();
		int count = 0;
		boolean isMarried = true;
		for (GoalDTO goalDTO : finalModifiedGoalDtosList) {
			sequenceList.add(goalDTO.getGoalName());
			if(goalDTO.getGoalName().equals("Self Marriage")){
				isMarried = false;
			}
			count++;
		}
		for (Goals goals : predefinedGoalList) {
			boolean flag = false;
			for (String sequence : sequenceList) {
				if(sequence.equals(goals.getGoalName())){
					flag = true;
					break;
				}
			}
			if(!flag && count < 9){
				if(goals.getGoalName().equals("Self Marriage") && isMarried){
				}else{
					if(!goals.getGoalName().equals("Child Higher Education") && !goals.getGoalName().equals("Child Dream Marriage")){
						GoalDTO goalDTO = new GoalDTO();
						goalDTO.setGoalName(goals.getGoalName());
						goalDTO.setFutureValue(convertToBigDecimal(0));
						goalDTO.setDuration(0.0);
						goalDTO.setPmtFutureValue(convertToBigDecimal(0));
						goalDTO.setLumPsumValue(convertToBigDecimal(0));
						goalDTO.setInflation(0.0);
						goalDTO.setRate(0.0);
						goalDTO.setSipValue(convertToBigDecimal(0));
						goalDTO.setShowToProfileType(0);
						goalDTO.setCostOfGoal(convertToBigDecimal(0));
						goalDTO.setGoalId(goals.getGoalId());
						finalModifiedGoalDtosList.add(goalDTO);
						count++;
					}
				}
			}
		}
		currentCostTotal = 0.0;
		futureValueTotal = 0.0;
		sipTotal = 0.0;
		lumpsumTotal = 0.0;
		for (GoalDTO goalDTO :finalModifiedGoalDtosList) {
			currentCostTotal += goalDTO.getCostOfGoal().intValue();
			futureValueTotal += goalDTO.getFutureValue().intValue();
			sipTotal += goalDTO.getSipValue().intValue();
			lumpsumTotal += goalDTO.getLumPsumValue().intValue();
		}
		calculateSipResponseDto.setGoalList(finalModifiedGoalDtosList);
		calculateSipResponseDto.setLumpsumValueTotal(convertToBigDecimal(lumpsumTotal));
		calculateSipResponseDto.setSipValueTotal(convertToBigDecimal(sipTotal));
		calculateSipResponseDto.setFutureValueTotal(convertToBigDecimal(futureValueTotal));
		calculateSipResponseDto.setCurrentCostTotal(convertToBigDecimal(currentCostTotal));
		calculateSipResponseDto.setGeneralSipMsg(messageSource.getMessage("sip.basic.general.message",new String[] { generalInflation.setScale(2, RoundingMode.HALF_UP) + "", avgNominalReturn + "" },Locale.ENGLISH));
		return calculateSipResponseDto;
	}

	public List<Goals> getGoalListBasedOnConditionsV2(CalculateSipRequestDto sipRequestDto) throws GoForWealthSIPException {
		logger.info("In getGoalListBasedOnConditionsV2()");
		logger.info("User Calculator Details : " + sipRequestDto);
		List<Goals> recommendedGoalsList = new ArrayList<>();
		CalculateSipResponseDto calculateSipResponseDto = new CalculateSipResponseDto();
		Optional<Cities> city = citiesRepository.findById(sipRequestDto.getCurrResidenceLocationId());
		String cityCode = "";
		if (city.isPresent()) {
			cityCode = city.get().getCityCode();
			calculateSipResponseDto.setCityName(city.get().getCityName());
		}
		Optional<MaritalStatus> maritalStatus = maritalStatusRepository.findById(sipRequestDto.getMaritalStatusId());
		String kidSlabCode = "";
		if (maritalStatus.isPresent() && maritalStatus.get().getMaritalStatusValue().equals(MaritalStatusEnum.Married.getValue())) {
			kidSlabCode = findKidSlabCodeByKids(sipRequestDto.getKids());
			calculateSipResponseDto.setMaritalStatusName(maritalStatus.get().getMaritalStatusName());
		}
		String city_code=cityCode;
		List<Goals> goalList = goalsRepository.findAllPreDefineGoals();
		goalList.forEach(goal -> {
			Goals goalsNew = new Goals();
			if(goal.getGoalName().equals("Pension/Retirement Plan")){
				if(sipRequestDto.getCurrentAge()<60){
					goalsNew = goal;
					recommendedGoalsList.add(goalsNew);
				}
			}else if(goal.getGoalName().equals("Self Marriage")){
				if((maritalStatus.get().getMaritalStatusValue().equals(MaritalStatusEnum.Not_Married.getValue()) || maritalStatus.get().getMaritalStatusValue().equals(MaritalStatusEnum.Other.getValue())) && (sipRequestDto.getCurrentAge()<=45) && (sipRequestDto.getKids()==0)){
					goalsNew = goal;
					recommendedGoalsList.add(goalsNew);
				}
			}else if(goal.getGoalName().equals("Vacation Planning")){
				if((!city_code.equals("Z"))){
					goalsNew = goal;
					recommendedGoalsList.add(goalsNew);
				}
			}else if(goal.getGoalName().equals("Child Higher Education")){
				if((sipRequestDto.getKids()>0) && (sipRequestDto.getCurrentAge()<=45)){
					goalsNew = goal;
					recommendedGoalsList.add(goalsNew);
				}
			}else if(goal.getGoalName().equals("Child Dream Marriage")){
				if((sipRequestDto.getKids()>0) && (sipRequestDto.getCurrentAge()<=55)){
					goalsNew = goal;
					recommendedGoalsList.add(goalsNew);
				}
			}else if(goal.getGoalName().equals("Car Purchase")){
				if((sipRequestDto.getCurrentAge()<=45) && (sipRequestDto.getIncomeVal()>=5 && sipRequestDto.getIncomeVal()<=20)){
					goalsNew = goal;
					recommendedGoalsList.add(goalsNew);
				}
			}else if(goal.getGoalName().equals("Dream Home")){
				if((sipRequestDto.getCurrentAge()>=27 && sipRequestDto.getCurrentAge()<=55) && (sipRequestDto.getIncomeVal()>=5)){
					goalsNew = goal;
					recommendedGoalsList.add(goalsNew);
				}
			}else if(goal.getGoalName().equals("My 1st Crore")){
				if((sipRequestDto.getCurrentAge()>=27 && sipRequestDto.getCurrentAge()<=45) && (sipRequestDto.getIncomeVal()>=5 && sipRequestDto.getIncomeVal()<=50)){
					goalsNew = goal;
					recommendedGoalsList.add(goalsNew);
				}
			}else if(goal.getGoalName().equals("Starting own Business")){
				if((city_code.equals("A") || city_code.equals("B") || city_code.equals("C")) && (sipRequestDto.getCurrentAge()>=20 && sipRequestDto.getCurrentAge()<=55) && (sipRequestDto.getIncomeVal()>=10)){
					goalsNew = goal;
					recommendedGoalsList.add(goalsNew);
				}
			}else if(goal.getGoalName().equals("Lifestyle Needs - Gadgets,etc")){
				if((sipRequestDto.getCurrentAge()>=20 && sipRequestDto.getCurrentAge()<=35) && (sipRequestDto.getIncomeVal()>=5)){
					goalsNew = goal;
					recommendedGoalsList.add(goalsNew);
				}
			}else if(goal.getGoalName().equals("My Second Home")){
				if( (city_code.equals("A") || city_code.equals("B") || city_code.equals("C") || city_code.equals("D")) && (sipRequestDto.getCurrentAge()>=55) && (sipRequestDto.getIncomeVal()>=10)){
					goalsNew = goal;
					recommendedGoalsList.add(goalsNew);
				}
			}else if(goal.getGoalName().equals("Philanthrophy & Charity")){
				if((city_code.equals("A") || city_code.equals("B") || city_code.equals("C") || city_code.equals("D")) && (sipRequestDto.getCurrentAge()>=45) && (sipRequestDto.getIncomeVal()>=20)){
					goalsNew = goal;
					recommendedGoalsList.add(goalsNew);
				}
			}else{
			}
		});
		logger.info("Out getGoalListBasedOnConditionsV2()");
		return recommendedGoalsList;
	}

	private String findKidSlabCodeByKids(Integer kids) {
		if (kids.equals(1)) {
			return KidsSlabCodeEnum.ONE.getValue();
		} else if (kids.equals(2)) {
			return KidsSlabCodeEnum.TWO.getValue();
		} else if (kids > 2) {
			return KidsSlabCodeEnum.GREATER_THAN_TWO.getValue();
		} else if (kids < 1) {
			return KidsSlabCodeEnum.NO_CHILD.getValue();
		}
		return "";
	}

	@Override
	public ReturnsTypeDTO getUserRiskProfileV2(int riskSumVal) {
		ReturnsTypeDTO response = new ReturnsTypeDTO();
		String HIGH = "HIGH";
		String LOW = "LOW";
		String MODERATE = "MODERATE";
		String seperator = "_";
		if (riskSumVal == 6 || riskSumVal == 7) {
			response.setRiskProfile(LOW + seperator + LOW);
			response.setRiskProfileMsg(messageSource.getMessage("sip.risprofile.very.cautious", new String[] {}, Locale.ENGLISH));
		} else if (riskSumVal >= 11 && riskSumVal <= 15) {
			response.setRiskProfile(MODERATE + seperator + MODERATE);
			response.setRiskProfileMsg(messageSource.getMessage("sip.risprofile.cautious", new String[] {}, Locale.ENGLISH));
		} else if (riskSumVal >= 8 && riskSumVal <= 10) {
			response.setRiskProfile(LOW + seperator + LOW);
			response.setRiskProfileMsg(messageSource.getMessage("sip.risprofile.very.cautious", new String[] {}, Locale.ENGLISH));
		} else if (riskSumVal >= 16) {
			response.setRiskProfile(HIGH + seperator + HIGH);
			response.setRiskProfileMsg(messageSource.getMessage("sip.risprofile.aggressive", new String[] {}, Locale.ENGLISH));
		}
     	String[] riskProfile = response.getRiskProfile().split("_");
	    String returns = riskProfile[0];
	    String risk = riskProfile[1];
		String fundTypString = riskBearingCapacityRepository.getFundTypeByReturnsAndRisk(returns, risk);
		List<SuggestSchemeDTO> suggestSchemeDtoList = setSuggestSchemeV2(fundTypString);
		Double newRoi = 0.0;
		BigDecimal generalInflation = new BigDecimal(5.00000);
		List<String> suggestedFundTypeList = assetClassInternalRepository.getFundsListByFundType(Arrays.asList(fundTypString.split("\\+")));
		for (String fundType : suggestedFundTypeList) {
			AssetClassInternal assetClassInternal = assetClassInternalRepository.findByFundType(fundType);
			newRoi += assetClassInternal.getRoiExptd().doubleValue();
		}
		BigDecimal avgNewRoi = new BigDecimal(newRoi/suggestedFundTypeList.size());
		response.setGeneralSipMessageAfterRisk(messageSource.getMessage("sip.general.message",new String[] { generalInflation.setScale(2, RoundingMode.HALF_UP) + "", avgNewRoi.setScale(2, RoundingMode.HALF_UP) + "" },Locale.ENGLISH));
		response.setSuggestSchemeList(suggestSchemeDtoList);
		return response;
	}

	public List<SuggestSchemeDTO> setSuggestSchemeV2(String fundCategory){
		List<SuggestSchemeDTO> suggestSchemeDtoList = new ArrayList<>();
		List<String> suggestedFundTypeLists = assetClassInternalRepository.getFundsListByFundType(Arrays.asList(fundCategory.split("\\+")));
		for (String fundType : suggestedFundTypeLists) {
			List<TopSchemes> topSchemesList = topSchemeRepository.findBySchemeCategory(fundType);
			for (TopSchemes topSchemes : topSchemesList) {
				String schemeCode = topSchemes.getSchemeCode();
				Scheme scheme = schemeRepository.findBySchemeCode(schemeCode);
				if(scheme!=null){
					SuggestSchemeDTO suggestSchemeDto = new SuggestSchemeDTO();
					suggestSchemeDto.setSchemeId(scheme.getSchemeId());
					suggestSchemeDto.setSchemeCode(scheme.getSchemeCode());
					suggestSchemeDto.setSchemeName(scheme.getSchemeName());
					suggestSchemeDto.setSchemeLaunchDate(scheme.getStartDate());
					suggestSchemeDto.setMinimumPurchaseAmount(scheme.getMinimumPurchaseAmount());
					suggestSchemeDto.setSchemeKeyword(scheme.getKeyword());
					suggestSchemeDto.setSchemeType(scheme.getSchemeType());
					suggestSchemeDto.setSequence(topSchemes.getSequence());
					suggestSchemeDtoList.add(suggestSchemeDto);
				}
			}
		}
		Collections.sort(suggestSchemeDtoList, new Comparator<SuggestSchemeDTO>() {
			@Override
			public int compare(SuggestSchemeDTO o1 , SuggestSchemeDTO o2) {
			   if(o1.getSequence() > o2.getSequence()) {
			      return 1;
			   } else if(o1.getSequence() < o2.getSequence()) {
			      return -1;
			   } else {
				   return 0;
			   }
			}
		});
		return suggestSchemeDtoList;
	}

	@Override
	public boolean createUserGoal(UserGoalDto createGoalDto,int userId) {
		boolean result = false;
		GoalsOrder goalsOrderObj = goalsOrderRepository.findByUserId(userId);
		if(goalsOrderObj != null){
			result = false;
		}else{
			GoalsOrder goalsOrderRef = createNewGoalsOrderV2(createGoalDto,userId);
			if(goalsOrderRef != null)
				result = true;
			else
				result = false;
		}
		return result;
	}

	@Override
	public boolean isUserGoalExist(int userId) {
		boolean result = false;
		GoalsOrder goalsOrderObj = goalsOrderRepository.findByUserId(userId);
		if(goalsOrderObj != null){
			return true;
		}
		return result;
	}

	@Transactional
	private GoalsOrder createNewGoalsOrderV2(UserGoalDto createGoalDto,int userId) {
		Optional<User> user = userRepository.findById(userId);
		GoalsOrder goalsOrder = createNewGoalsOrderV2(user.get());
		for (int i=0; i < createGoalDto.getGoalDto().size(); i++) {
			if(createGoalDto.getGoalDto().get(i).isInvestmentFlag()){
				GoalOrderItems goalOrderItem = goalOrderItemsRepository.findByGoalOrderItemId(createGoalDto.getGoalDto().get(i).getGoalId());
				Goals goalsObj = null;
				if(goalOrderItem.getGoals().getGoalType().equals("UD")){
					goalsObj = goalOrderItem.getGoals();
					goalsObj.setGoalName(createGoalDto.getGoalDto().get(i).getGoalName());
					goalsObj.setCostOfGoal(createGoalDto.getGoalDto().get(i).getCostOfGoal());
					goalsObj.setDuration(createGoalDto.getGoalDto().get(i).getDuration().intValue());
					Optional<GoalBucket> goalBucket = getGoalBucketFromDurationV2(createGoalDto.getGoalDto().get(i).getDuration().intValue());
					goalsObj.setGoalBucket(goalBucket.get());
				}else{
					goalsObj = goalOrderItem.getGoals();
				}
				goalOrderItem.setCreatedTimestamp(new Date());
				goalOrderItem.setFutureValue(createGoalDto.getGoalDto().get(i).getFutureValue());
				goalOrderItem.setMonthlySaving(createGoalDto.getGoalDto().get(i).getPmtFutureValue());
				goalOrderItem.setLumpsumSaving(createGoalDto.getGoalDto().get(i).getLumPsumValue());
				goalOrderItem.setGoalsOrder(goalsOrder);
				goalOrderItem.setCurrentCost(createGoalDto.getGoalDto().get(i).getCostOfGoal());
				goalOrderItem.setDuration(createGoalDto.getGoalDto().get(i).getDuration().intValue());
				goalOrderItem.setDescription(createGoalDto.getGoalDto().get(i).getGoalName());
				goalOrderItem.setField1(1);
				goalOrderItem.setUser(user.get());
				goalOrderItem.setGoals(goalsObj);
				goalOrderItemsRepository.save(goalOrderItem);
				if(createGoalDto.getGoalDto().get(i).getAssetClassDto().size()>0){
					for(int j=0; j<createGoalDto.getGoalDto().get(i).getAssetClassDto().size();j++) {
						AssetClass assetClass = assetClassRepository.getOne(createGoalDto.getGoalDto().get(i).getAssetClassDto().get(j).getAssetClassId());
						UserAssetItems userAssetItems  = new UserAssetItems();
						userAssetItems.setAssetClass(assetClass);
						userAssetItems.setAssetValue(new BigDecimal(createGoalDto.getGoalDto().get(i).getAssetClassDto().get(j).getAssetValue()));
						userAssetItems.setFutureValue(new BigDecimal(createGoalDto.getGoalDto().get(i).getAssetClassDto().get(j).getFutureValue()));
						userAssetItems.setGoalsOrder(goalsOrder);
						//List<GoalOrderItems> goalOrderItem = goalOrderItemsRepository.findByGoalsOrderIdAndDescription(goalsOrder.getGoalsOrderId(),createGoalDto.getGoalDto().get(i).getAssetClassDto().get(j).getAssociatedGoalName());
						userAssetItems.setGoalsOrderItemId(goalOrderItem.getGoalOrderItemId());
						userAssetItemsRepository.save(userAssetItems);
					}
				}
			}else{
				Goals goalsObj = null;
				Goals goals = goalsRepository.findByGoalName(createGoalDto.getGoalDto().get(i).getGoalName());
				if(goals != null){
					goalsObj = goals;
				}else{
					Goals goalsRef = new Goals();
					goalsRef.setGoalType(GoalTypeEnum.USER_DEFINED.getValue());
					goalsRef.setGoalName(createGoalDto.getGoalDto().get(i).getGoalName());
					goalsRef.setGoalIcon("user_goal_icon");
					goalsRef.setCostOfGoal(createGoalDto.getGoalDto().get(i).getCostOfGoal());
					goalsRef.setDuration(createGoalDto.getGoalDto().get(i).getDuration().intValue());
					Optional<GoalBucket> goalBucket = getGoalBucketFromDurationV2(createGoalDto.getGoalDto().get(i).getDuration());
					if (goalBucket.isPresent()) {
						goalsRef.setGoalBucket(goalBucket.get());
					}
					goalsObj = goalsRepository.save(goalsRef);
				}
				GoalOrderItems items = new GoalOrderItems();
				items.setCreatedTimestamp(new Date());
				//items.setGoalDate(new Date());
				//items.setUpdatedDateTime(new Date());
				items.setFutureValue(createGoalDto.getGoalDto().get(i).getFutureValue());
				items.setMonthlySaving(createGoalDto.getGoalDto().get(i).getPmtFutureValue());
				items.setLumpsumSaving(createGoalDto.getGoalDto().get(i).getLumPsumValue());
				items.setGoalsOrder(goalsOrder);
				items.setCurrentCost(createGoalDto.getGoalDto().get(i).getCostOfGoal());
				items.setDuration(createGoalDto.getGoalDto().get(i).getDuration().intValue());
				items.setDescription(createGoalDto.getGoalDto().get(i).getGoalName());
				items.setField1(0);
				items.setUser(user.get());
				items.setGoals(goalsObj);
				GoalOrderItems goalsOrderItem = goalOrderItemsRepository.save(items);
				if(createGoalDto.getGoalDto().get(i).getAssetClassDto().size()>0){
					for(int j=0; j<createGoalDto.getGoalDto().get(i).getAssetClassDto().size();j++) {
						AssetClass assetClass = assetClassRepository.getOne(createGoalDto.getGoalDto().get(i).getAssetClassDto().get(j).getAssetClassId());
						UserAssetItems userAssetItems  = new UserAssetItems();
						userAssetItems.setAssetClass(assetClass);
						userAssetItems.setAssetValue(new BigDecimal(createGoalDto.getGoalDto().get(i).getAssetClassDto().get(j).getAssetValue()));
						userAssetItems.setFutureValue(new BigDecimal(createGoalDto.getGoalDto().get(i).getAssetClassDto().get(j).getFutureValue()));
						userAssetItems.setGoalsOrder(goalsOrder);
						//List<GoalOrderItems> goalOrderItem = goalOrderItemsRepository.findByGoalsOrderIdAndDescription(goalsOrder.getGoalsOrderId(),createGoalDto.getGoalDto().get(i).getAssetClassDto().get(j).getAssociatedGoalName());
						userAssetItems.setGoalsOrderItemId(goalsOrderItem.getGoalOrderItemId());
						userAssetItemsRepository.save(userAssetItems);
					}
				}
			}
		}
		goalsOrder.setInflation(new BigDecimal(createGoalDto.getInflation()));
		goalsOrder.setRoi(new BigDecimal(createGoalDto.getRoi()));
		goalsOrder.setTotalFutureValue(new BigDecimal(createGoalDto.getTotalFutureValue()));
		goalsOrder.setTotalLumpsumSaving(new BigDecimal(createGoalDto.getTotalLumpsumSaving()));
		goalsOrder.setTotalMonthlySaving(new BigDecimal(createGoalDto.getTotalMonthlySaving()));
		goalsOrder.setTotalMonthlyAdjustment(new BigDecimal(createGoalDto.getNetMonthlySaving()));
		goalsOrder.setTotalLumpsumAdjusment(new BigDecimal(createGoalDto.getNetLumpsumSaving()));
		goalsOrder.setRiskProfile(createGoalDto.getRiskProfile());
		String[] riskProfile = createGoalDto.getRiskProfile().split("_");
	    String returns = riskProfile[0];
	    String risk = riskProfile[1];
		String fundTypeString = riskBearingCapacityRepository.getFundTypeByReturnsAndRisk(returns, risk);
		goalsOrder.setFundType(fundTypeString);
		GoalsOrder goalsOrderRef = goalsOrderRepository.save(goalsOrder);
		return goalsOrderRef;
	}
	
	private GoalsOrder createNewGoalsOrderV2(User user) {
		GoalsOrder goalsOrder = null;
		goalsOrder = goalsOrderRepository.findByUserId(user.getUserId());
		if(goalsOrder == null) {
			goalsOrder = new GoalsOrder();
			goalsOrder.setUser(user);
			goalsOrder.setTotalAssetValue(BigDecimal.ZERO);
			goalsOrder.setTotalFutureValue(BigDecimal.ZERO);
			goalsOrder.setTotalLumpsumAdjusment(BigDecimal.ZERO);
			goalsOrder.setTotalLumpsumSaving(BigDecimal.ZERO);
			goalsOrder.setTotalMonthlyAdjustment(BigDecimal.ZERO);
			goalsOrder.setTotalMonthlySaving(BigDecimal.ZERO);
			goalsOrder.setStatus(GoalOrderStateEnum.NEW.getValue());
			goalsOrder.setCreatedTimestamp(new Date());
			goalsOrder.setInflation(BigDecimal.ZERO);
			goalsOrder.setRoi(BigDecimal.ZERO);
			goalsOrder = goalsOrderRepository.save(goalsOrder);
		}
		return goalsOrder;
	}

	public Optional<GoalBucket> getGoalBucketFromDurationV2(double duration) {
		if (duration >0 && duration <=4) {
			return goalBucketRepository.findByGoalBucketCode(GoalBucketCodeEnum.SHORT_TERM.getCode());
		}else if(duration >=5 && duration <=9){
			return goalBucketRepository.findByGoalBucketCode(GoalBucketCodeEnum.MEDIUM_TERM.getCode());
		}else{
			return goalBucketRepository.findByGoalBucketCode(GoalBucketCodeEnum.LONG_TERM.getCode());
		}
	}

	@Override
	public String replaceUserGoal(UserGoalDto replaceGoalDto,int userId){
		logger.info("In replaceUserGoal() of GoForWealthServiceImpl");
		String message = "";
		User user = userRepository.getOne(userId);
		if(user!=null){
			if(user.getGoalsOrders().size()>0){
					List<GoalOrderItems> goalOrderItemsList = goalOrderItemsRepository.findByUserId(userId);
					for (int i = 0; i < goalOrderItemsList.size(); i++) {
						if(goalOrderItemsList.get(i).getField1()==0){
							Goals goalsObj = goalsRepository.getOne(goalOrderItemsList.get(i).getGoals().getGoalId());
							goalOrderItemsRepository.delete(goalOrderItemsList.get(i));
							if(goalsObj.getGoalType().equals("UD")){
								goalsRepository.delete(goalsObj);
							}
						}
				}
				if(user.getGoalsOrders().size()>0){
					List<UserAssetItems> userAssetItems =  userAssetItemsRepository.findByGoalsOrderId(user.getGoalsOrders().iterator().next().getGoalsOrderId());
					if(userAssetItems.size()>0){
						for (UserAssetItems userAssetItemsObj : userAssetItems) {
							userAssetItemsRepository.delete(userAssetItemsObj);
						}
					}
				}
				GoalsOrder userGolasOrder = goalsOrderRepository.findByUserId(userId);
				List<GoalOrderItems> goalOrderItemsObj = goalOrderItemsRepository.findByUserId(userId);
				List<UserAssetItems> userAssetItemsObj = userAssetItemsRepository.findByGoalsOrderId(userGolasOrder.getGoalsOrderId());
				userGolasOrder.setGoalOrderItemses(new HashSet<>(goalOrderItemsObj));
				userGolasOrder.setUserAssetItemses(new HashSet<>(userAssetItemsObj));
				if(userGolasOrder != null){
					GoalsOrder goalsOrderObj = createNewGoalsOrderV2(replaceGoalDto,userId);
					if(goalsOrderObj!=null)
						message=GoForWealthUMAConstants.SUCCESS;
					else
						message=GoForWealthUMAConstants.FAILURE;
				}
			}else{
				message="GoalOrderId not found";
			}
		}else{
			message = GoForWealthUMAConstants.USER_NOT_EXIST;
		}
		logger.info("Out replaceUserGoal() of GoForWealthServiceImpl");
		return message;
	}

	private Double getDurationFromGoalBucket(GoalBucket goalBucket) {
		if (goalBucket.getGoalBucketCode().equals(GoalBucketCodeEnum.SHORT_TERM.getCode())) {
			return GoalBucketCodeEnum.SHORT_TERM.getDuration();
		}
		if (goalBucket.getGoalBucketCode().equals(GoalBucketCodeEnum.MEDIUM_TERM.getCode())) {
			return GoalBucketCodeEnum.MEDIUM_TERM.getDuration();
		}
		if (goalBucket.getGoalBucketCode().equals(GoalBucketCodeEnum.LONG_TERM.getCode())) {
			return GoalBucketCodeEnum.LONG_TERM.getDuration();
		}
		return null;
	}


}
