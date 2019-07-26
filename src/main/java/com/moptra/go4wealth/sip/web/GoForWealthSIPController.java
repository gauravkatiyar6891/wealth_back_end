package com.moptra.go4wealth.sip.web;

import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.moptra.go4wealth.sip.common.enums.GoForWealthSIPErrorMessageEnum;
import com.moptra.go4wealth.sip.common.exception.GoForWealthSIPException;
import com.moptra.go4wealth.sip.common.rest.GoForWealthSIPResponseInfo;
import com.moptra.go4wealth.sip.common.util.GoForWealthSIPUtil;
import com.moptra.go4wealth.sip.model.CalculateSipRequestDto;
import com.moptra.go4wealth.sip.model.CalculateSipResponseDto;
import com.moptra.go4wealth.sip.model.CityDTO;
import com.moptra.go4wealth.sip.model.IncomeDTO;
import com.moptra.go4wealth.sip.model.MaritalDTO;
import com.moptra.go4wealth.sip.model.StateDTO;
import com.moptra.go4wealth.sip.service.GoForWealthSIPService;

@RestController
@RequestMapping("${server.contextPath}/sip")
public class GoForWealthSIPController {

	private static Logger logger = LoggerFactory.getLogger(GoForWealthSIPController.class);

	@Autowired
	private GoForWealthSIPService goForWealthSIPService;

	/**
	 * 
	 * @param exception
	 * @return GoForWealthUMAResponseInfo
	 */
	@ExceptionHandler({ GoForWealthSIPException.class })
	public GoForWealthSIPResponseInfo handleException(GoForWealthSIPException exception) {
		return GoForWealthSIPUtil.getExceptionResponseInfo(exception);
	}

	/**
	 * 
	 * @param exception
	 * @return
	 */
	@ExceptionHandler({ Exception.class })
	public GoForWealthSIPResponseInfo handleException(Exception exception) {
		return GoForWealthSIPUtil.getErrorResponseInfo(GoForWealthSIPErrorMessageEnum.COMMON_ERROR_CODE.getValue(),GoForWealthSIPErrorMessageEnum.FAILURE_MESSAGE.getValue(),exception);
	}

	@GetMapping("/api/getCityList")
	public GoForWealthSIPResponseInfo getCityList(HttpServletRequest request) throws GoForWealthSIPException {
		logger.info("In getCityList()");
		List<CityDTO> cityDTOs = goForWealthSIPService.getCityList();
		GoForWealthSIPResponseInfo responseInfo;
		if (cityDTOs != null && cityDTOs.size() > 0) {
			responseInfo = GoForWealthSIPUtil.getSuccessResponseInfo(GoForWealthSIPErrorMessageEnum.SUCCESS_CODE.getValue(),GoForWealthSIPErrorMessageEnum.SUCCESS_MESSAGE.getValue(),cityDTOs);
			responseInfo.setTotalRecords(cityDTOs.size());
		} else {
			responseInfo = new GoForWealthSIPResponseInfo();
			responseInfo.setMessage(GoForWealthSIPErrorMessageEnum.DATA_NOT_FOUND_MESSAGE.getValue());
			responseInfo.setStatus(GoForWealthSIPErrorMessageEnum.DATA_NOT_FOUND_CODE.getValue());
			responseInfo.setTotalRecords(0);
		}
		logger.info("Out getCityList()");
		return responseInfo;
	}

	@GetMapping("/api/getStateList")
	public GoForWealthSIPResponseInfo getStateList(HttpServletRequest request) throws GoForWealthSIPException {
		logger.info("In getStateList()");
		List<StateDTO> stateDTOs = goForWealthSIPService.getStateList();
		GoForWealthSIPResponseInfo responseInfo;
		if (stateDTOs != null && stateDTOs.size() > 0) {
			responseInfo = GoForWealthSIPUtil.getSuccessResponseInfo(GoForWealthSIPErrorMessageEnum.SUCCESS_CODE.getValue(),GoForWealthSIPErrorMessageEnum.SUCCESS_MESSAGE.getValue(),stateDTOs);
			responseInfo.setTotalRecords(stateDTOs.size());
		} else {
			responseInfo = new GoForWealthSIPResponseInfo();
			responseInfo.setMessage(GoForWealthSIPErrorMessageEnum.DATA_NOT_FOUND_MESSAGE.getValue());
			responseInfo.setStatus(GoForWealthSIPErrorMessageEnum.DATA_NOT_FOUND_CODE.getValue());
			responseInfo.setTotalRecords(0);
		}
		logger.info("Out getStateList()");
		return responseInfo;
	}

	@GetMapping("/api/getMaritalList")
	public GoForWealthSIPResponseInfo getMaritalList(HttpServletRequest request) throws GoForWealthSIPException {
		logger.info("In getMaritalList()");
		List<MaritalDTO> maritalDTOs = goForWealthSIPService.getMaritalList();
		GoForWealthSIPResponseInfo responseInfo;
		if (maritalDTOs != null && maritalDTOs.size() > 0) {
			responseInfo = GoForWealthSIPUtil.getSuccessResponseInfo(GoForWealthSIPErrorMessageEnum.SUCCESS_CODE.getValue(),GoForWealthSIPErrorMessageEnum.SUCCESS_MESSAGE.getValue(),maritalDTOs);
			responseInfo.setTotalRecords(maritalDTOs.size());
		} else {
			responseInfo = new GoForWealthSIPResponseInfo();
			responseInfo.setMessage(GoForWealthSIPErrorMessageEnum.DATA_NOT_FOUND_MESSAGE.getValue());
			responseInfo.setStatus(GoForWealthSIPErrorMessageEnum.DATA_NOT_FOUND_CODE.getValue());
			responseInfo.setTotalRecords(0);
		}
		logger.info("Out getMaritalList()");
		return responseInfo;
	}

	@GetMapping(value = "/api/getAllIncomeSlab")
	public GoForWealthSIPResponseInfo getAllIncomeSlab() {
		logger.info("In getAllIncomeSlab");
		GoForWealthSIPResponseInfo responseInfo;
		ArrayList<IncomeDTO> incomeDtoList = (ArrayList<IncomeDTO>) goForWealthSIPService.getAllIncomeSlabs();
		if (incomeDtoList != null && incomeDtoList.size() > 0) {
			responseInfo = GoForWealthSIPUtil.getSuccessResponseInfo(GoForWealthSIPErrorMessageEnum.SUCCESS_CODE.getValue(),GoForWealthSIPErrorMessageEnum.SUCCESS_MESSAGE.getValue(),incomeDtoList);
			responseInfo.setTotalRecords(incomeDtoList.size());
		} else {
			responseInfo = new GoForWealthSIPResponseInfo();
			responseInfo.setStatus(GoForWealthSIPErrorMessageEnum.DATA_NOT_FOUND_CODE.getValue());
			responseInfo.setMessage(GoForWealthSIPErrorMessageEnum.DATA_NOT_FOUND_MESSAGE.getValue());
		}
		logger.info("Out getAllIncomeSlab()");
		return responseInfo;
	}

	@PostMapping("/api/calculateSipV2")
	public GoForWealthSIPResponseInfo calculateSipV2(@Valid @RequestBody CalculateSipRequestDto sipRequestDto,BindingResult bindingResult, HttpServletRequest request, HttpServletResponse response) throws GoForWealthSIPException {
		logger.info("In calculateSipV2()");
	 	GoForWealthSIPResponseInfo responseInfo;
	 	if (bindingResult.hasErrors()) {
	 		logger.info("Out calculateSipV2()");
	 		throw new GoForWealthSIPException(GoForWealthSIPErrorMessageEnum.COMMON_ERROR_CODE.getValue(),GoForWealthSIPErrorMessageEnum.INVALID_REQ_PAYLOAD_MESSAGE.getValue());
	 	}
	 	CalculateSipResponseDto goalDTOs = goForWealthSIPService.calculateSipV2(sipRequestDto);
	 	if (goalDTOs != null) {
	 		responseInfo = GoForWealthSIPUtil.getSuccessResponseInfo(GoForWealthSIPErrorMessageEnum.SUCCESS_CODE.getValue(),GoForWealthSIPErrorMessageEnum.SUCCESS_MESSAGE.getValue(),goalDTOs);
	 	 	responseInfo.setTotalRecords(1);
	 	}else{
	 		responseInfo = new GoForWealthSIPResponseInfo();
	 	 	responseInfo.setMessage(GoForWealthSIPErrorMessageEnum.DATA_NOT_FOUND_MESSAGE.getValue());
	 	 	responseInfo.setStatus(GoForWealthSIPErrorMessageEnum.DATA_NOT_FOUND_CODE.getValue());
	 	 	responseInfo.setTotalRecords(0);
	 	}
	 	logger.info("Out calculateSipV2()");
	 	return responseInfo;
	}


}