package com.moptra.go4wealth.sip.service;

import java.util.List;

import com.moptra.go4wealth.bean.User;
import com.moptra.go4wealth.sip.common.exception.GoForWealthSIPException;
import com.moptra.go4wealth.sip.model.AgeSlabDTO;
import com.moptra.go4wealth.sip.model.AssetClassDTO;
import com.moptra.go4wealth.sip.model.CalculateSipRequestDto;
import com.moptra.go4wealth.sip.model.CalculateSipResponseDto;
import com.moptra.go4wealth.sip.model.CityDTO;
import com.moptra.go4wealth.sip.model.DownloadReportRequestDTO;
import com.moptra.go4wealth.sip.model.GoalDTO;
import com.moptra.go4wealth.sip.model.GoalOrderResponse;
import com.moptra.go4wealth.sip.model.IncomeDTO;
import com.moptra.go4wealth.sip.model.KidsSlabDTO;
import com.moptra.go4wealth.sip.model.MaritalDTO;
import com.moptra.go4wealth.sip.model.ReturnsTypeDTO;
import com.moptra.go4wealth.sip.model.StateDTO;
import com.moptra.go4wealth.sip.model.UserGoalDto;

public interface GoForWealthSIPService {

	List<CityDTO> getCityList();

	List<StateDTO> getStateList();

	List<MaritalDTO> getMaritalList();
	
	CalculateSipResponseDto calculateSipV2(CalculateSipRequestDto sipRequestDto) throws GoForWealthSIPException;

	List<GoalDTO> getGoalsList();

	List<IncomeDTO> getIncomeSlabList();

	List<AgeSlabDTO> getAgeSlabList();

	List<MaritalDTO> getMaritalSlabList();

	List<KidsSlabDTO> getKidsSlabList();

	List<AssetClassDTO> getAssetClassList();
	
	UserGoalDto getGoalOrderDetailV2(Integer userId, Integer orderId);
	
	GoalOrderResponse  getGoalsOrderItemsDetailsById(Integer goalsOrderItemsId);

	List<IncomeDTO> getAllIncomeSlabs();

	List<ReturnsTypeDTO> getRiskProfileList();
	
	List<GoalDTO> getPredefinedGoalList();
	
	Integer getUserIdByGoalsOrderId(Integer goalsOrderId);
	
	User getUserByUserId(Integer userId);
	
	String sendDownloadReportUrlToEmail(User user,DownloadReportRequestDTO downloadReportRequestDto,String email) throws GoForWealthSIPException;

	ReturnsTypeDTO getUserRiskProfileV2(int riskSumVal);
	
	boolean createUserGoal(UserGoalDto createGoalDto,int userId);
	
	String replaceUserGoal(UserGoalDto replaceGoalDto,int userId);

	boolean isUserGoalExist(int userId);

}