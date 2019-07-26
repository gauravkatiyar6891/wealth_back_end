package com.moptra.go4wealth.prs.orderapi;

import java.io.IOException;

import javax.xml.parsers.ParserConfigurationException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.xml.sax.SAXException;

import com.moptra.go4wealth.prs.common.exception.GoForWealthPRSException;
import com.moptra.go4wealth.prs.orderapi.request.CheckOrderStatusRequest;
import com.moptra.go4wealth.prs.orderapi.request.GetPasswordRequest;
import com.moptra.go4wealth.prs.orderapi.request.MandateDetailsRequestParam;
import com.moptra.go4wealth.prs.orderapi.request.OrderAllotmentRequest;
import com.moptra.go4wealth.prs.orderapi.request.OrderEntryRequest;
import com.moptra.go4wealth.prs.orderapi.request.OrderRedemptionStatusRequest;
import com.moptra.go4wealth.prs.orderapi.request.SipOrderEntryRequest;
import com.moptra.go4wealth.prs.orderapi.request.SpreadOrderEntryRequest;
import com.moptra.go4wealth.prs.orderapi.request.SwitchOrderEntryRequest;
import com.moptra.go4wealth.prs.orderapi.request.XsipOrderEntryRequest;
import com.moptra.go4wealth.prs.orderapi.response.OrderEntryResponse;
import com.moptra.go4wealth.prs.orderapi.response.SipOrderEntryResponse;
import com.moptra.go4wealth.prs.orderapi.response.SpreadOrderEntryResponse;
import com.moptra.go4wealth.prs.orderapi.response.SwitchOrderEntryResponse;
import com.moptra.go4wealth.prs.orderapi.response.XsipOrderEntryResponse;
import com.moptra.go4wealth.repository.SystemPropertiesRepository;

@Component
public class OrderMfService {

	@Autowired
	OrderMfApi orderMfApi;
	
	/**
	 * 
	 * @param getPasswordRequest
	 * @return GetPasswordResponse
	 * @throws IOException 
	 * @throws SAXException 
	 * @throws ParserConfigurationException 
	 */
	public String getPassword(GetPasswordRequest getPasswordRequest) {
		String getPasswordResponse=orderMfApi.getPassword(getPasswordRequest);
		return getPasswordResponse;
	}

	/**
	 * 
	 * @param orderEntryRequest
	 * @return OrderEntryResponse
	 * @throws GoForWealthPRSException 
	 */
	public OrderEntryResponse getOrderEntry(OrderEntryRequest orderEntryRequest){
		OrderEntryResponse orderEntryResponse=orderMfApi.getOrderEntry(orderEntryRequest);
		return orderEntryResponse;
	}

	
	/**
	 * 
	 * @param sipOrderEntryRequest
	 * @return SipOrderEntryResponse
	 */
	public SipOrderEntryResponse getSipOrderEntry(SipOrderEntryRequest sipOrderEntryRequest){
		SipOrderEntryResponse sipOrderEntryResponse = orderMfApi.getSipOrderEntry(sipOrderEntryRequest);
		return sipOrderEntryResponse;
	}

	/**
	 * 
	 * @param spreadOrderEntryRequest
	 * @return SpreadOrderEntryResponse
	 */
	public SpreadOrderEntryResponse getSpreadOrderEntry(SpreadOrderEntryRequest spreadOrderEntryRequest){
		SpreadOrderEntryResponse spreadOrderEntryResponse=	orderMfApi.getSpreadOrderEntry(spreadOrderEntryRequest);
		return spreadOrderEntryResponse;
	}
	
	/**
	 * 
	 * @param switchOrderEntryRequest
	 * @return SwitchOrderEntryResponse
	 */
	public SwitchOrderEntryResponse getSwitchOrderEntry(SwitchOrderEntryRequest switchOrderEntryRequest){
		SwitchOrderEntryResponse switchOrderEntryResponse=orderMfApi.getSwitchOrderEntry(switchOrderEntryRequest);
		return switchOrderEntryResponse;
	}

	
	/**
	 * 
	 * @param xsipOrderEntryRequest
	 * @return XsipOrderEntryResponse
	 */
	public XsipOrderEntryResponse getXsipOrderEntry(XsipOrderEntryRequest xsipOrderEntryRequest){
		XsipOrderEntryResponse xsipOrderEntryResponse= orderMfApi.getXsipOrderEntry(xsipOrderEntryRequest);
		return xsipOrderEntryResponse;
	}

	public OrderEntryResponse getOrderEntryForRedeem(OrderEntryRequest orderEntryRequest) {
		OrderEntryResponse orderEntryResponse=orderMfApi.getOrderEntryForRedeem(orderEntryRequest);
		return orderEntryResponse;
	}
	
	/*
	public String getPasswordForChildOrder() throws GoForWealthPRSException{
   		String password = orderMfApi.getPasswordForChildOrder();
		return password;
	}
	*/

	public String getChildOrderDetail(String clientCode, String date, String encryptedPassword, String memberCode,String regnNo, String systematicPlanType, SystemPropertiesRepository systemPropertiesRepository2) {
		String res=null;
		try {
			res=orderMfApi.getChildOrderDetail(clientCode,date,encryptedPassword,memberCode,regnNo,systematicPlanType,systemPropertiesRepository2);
		} catch (GoForWealthPRSException e) {
			e.printStackTrace();
		}
		return res;
	}

	public Object getOrderStatus(CheckOrderStatusRequest checkOrderStatusRequest) {
		Object orderStatusResponse = orderMfApi.getOrderStatus(checkOrderStatusRequest);
		return orderStatusResponse;
	}

	public Object getAllotementStatement(OrderAllotmentRequest orderAllotmentRequest) {
		Object allotemetStatementResponse = orderMfApi.getAllotementStatement(orderAllotmentRequest);
		return allotemetStatementResponse;
	}
	
	public Object getMandateDetails(MandateDetailsRequestParam mandateDetailsRequestParam){
		Object mandateDetails = orderMfApi.getMandateDetails(mandateDetailsRequestParam);
		return mandateDetails;
	}
	
	
	/**
	 * 
	 * @param mandateDetailsRequestParam
	 * @return Object
	*/
	public Object getMandateAuthenticationUrl(MandateDetailsRequestParam mandateDetailsRequestParam){
		Object mandateAuthenticationCheckDetail = orderMfApi.getMandateAuthenticationUrlDetail(mandateDetailsRequestParam);
		return mandateAuthenticationCheckDetail;
	}

	
	public Object updateOrderRedemptionStatus(OrderRedemptionStatusRequest orderRedemptionStatusRequest) {
		Object redemptionStatus = orderMfApi.orderMfApi(orderRedemptionStatusRequest);
		return redemptionStatus;
	}

	/*
	public String getChildOrderDetail(ChildOrderDetailRequest childOrderDetailRequest) throws GoForWealthPRSException{
		orderMfApi.getChildOrderDetail(childOrderDetailRequest);
		return null; 
	}
	*/
	
	
}
