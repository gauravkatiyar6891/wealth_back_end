package com.moptra.go4wealth.prs.orderapi;

import java.io.File;
import java.io.FileInputStream;
import java.net.URL;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.security.KeyStore;
import java.security.cert.X509Certificate;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;

import javax.net.ssl.SSLContext;
import javax.xml.namespace.QName;

import org.apache.http.client.HttpClient;
import org.apache.http.conn.ssl.SSLConnectionSocketFactory;
import org.apache.http.conn.ssl.SSLContextBuilder;
import org.apache.http.conn.ssl.TrustSelfSignedStrategy;
import org.apache.http.conn.ssl.TrustStrategy;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.json.JSONArray;
import org.json.JSONObject;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.http.client.ClientHttpRequestFactory;
import org.springframework.http.client.HttpComponentsClientHttpRequestFactory;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.http.converter.StringHttpMessageConverter;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.stereotype.Component;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.ResourceAccessException;
import org.springframework.web.client.RestTemplate;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.moptra.go4wealth.admin.common.util.GoForWealthAdminUtil;
import com.moptra.go4wealth.bean.Scheme;
import com.moptra.go4wealth.bean.StoreConf;
import com.moptra.go4wealth.bean.SystemProperties;
import com.moptra.go4wealth.prs.common.constant.GoForWealthPRSConstants;
import com.moptra.go4wealth.prs.common.enums.GoForWealthPRSErrorMessageEnum;
import com.moptra.go4wealth.prs.common.exception.GoForWealthPRSException;
import com.moptra.go4wealth.prs.orderapi.client.MfOrderClient;
import com.moptra.go4wealth.prs.orderapi.in.bsestarmf.MFOrderEntry;
import com.moptra.go4wealth.prs.orderapi.request.CheckOrderStatusRequest;
import com.moptra.go4wealth.prs.orderapi.request.ChildOrderDetailRequest;
import com.moptra.go4wealth.prs.orderapi.request.ChildPassword;
import com.moptra.go4wealth.prs.orderapi.request.GetAccessTokenRequest;
import com.moptra.go4wealth.prs.orderapi.request.GetMandateDetailRequest;
import com.moptra.go4wealth.prs.orderapi.request.GetPasswordRequest;
import com.moptra.go4wealth.prs.orderapi.request.MandateAuthenticationCheckRequest;
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
import com.moptra.go4wealth.prs.orderapi.tempuri.MFOrder;
import com.moptra.go4wealth.repository.SchemeRepository;
import com.moptra.go4wealth.repository.StoreConfRepository;
import com.moptra.go4wealth.repository.SystemPropertiesRepository;
import com.moptra.go4wealth.uma.common.util.OtpGenerator;

@Component
public class OrderMfApi {

	@Autowired
	StoreConfRepository storeConfRepository;
	
	@Autowired
	SchemeRepository schemeRepository;
	
	private static final QName SERVICE_NAME = new QName("http://tempuri.org/", "MFOrder");

	private static Logger logger = LoggerFactory.getLogger(OrderMfApi.class);
	public static MFOrderEntry getPort(){
		URL wsdlURL = MFOrder.WSDL_LOCATION;
		MFOrder ss = new MFOrder(wsdlURL, SERVICE_NAME);
        MFOrderEntry port = ss.getWSHttpBindingMFOrderEntry(); 
		return port;
	}
	
	/**
	 * 
	 * @return it will encrypted password
	 */
	
	public String getPassword( GetPasswordRequest getPasswordRequest) {
		try {
			logger.info("Get Password Request:" + GoForWealthAdminUtil.getJsonFromObject(getPasswordRequest,null));
		} catch (JsonProcessingException e) {
			e.printStackTrace();
		}
		MFOrderEntry port=MfOrderClient.getPort();
		String passwordResponse=port.getPassword(getPasswordRequest.getUserId(), getPasswordRequest.getPassword(), getPasswordRequest.getPassKey());
		System.out.println("Get Password Reponse==="+passwordResponse);
		logger.info("Get Password Reponse :"+passwordResponse);

		return passwordResponse;
	}
	
	
	/**
	 * 
	 * @param orderEntryRequest
	 * @return OrderEntryResponse
	 */
	public OrderEntryResponse getOrderEntry(OrderEntryRequest orderEntryRequest){
		MFOrderEntry port=getPort();
		String L1AddedSchemeCode="";
		String amount=orderEntryRequest.getOrderVal();
		double amount1=Double.parseDouble(amount);
		String schemeCode=orderEntryRequest.getSchemeCd();
		Scheme scheme = schemeRepository.findBySchemeCode(schemeCode);
		if(amount1>=200000){
			if(schemeCode.contains("-L1")){
				L1AddedSchemeCode=schemeCode;
			}
			else{
				if(scheme != null){
					String schemeCode1 = getSchemeCodeWithL1(scheme.getRtaSchemeCode(), scheme.getIsin(), schemeCode);
					if(!schemeCode1.equals("")){
						L1AddedSchemeCode = schemeCode1;
					}else{
						L1AddedSchemeCode = schemeCode+"-L1";
					}
				}
			}
		}
		else{
			L1AddedSchemeCode=schemeCode;
		}
		orderEntryRequest.setSchemeCd(L1AddedSchemeCode);
		orderEntryRequest.setOrderVal(String.valueOf(amount1));
		try {
			logger.info(" Order Enter Request:" + GoForWealthAdminUtil.getJsonFromObject(orderEntryRequest,null));
		} catch (JsonProcessingException e) {
			e.printStackTrace();
		}

		String orderEntry=port.orderEntryParam(orderEntryRequest.getTransCode(), orderEntryRequest.getUniqueRefNo(), orderEntryRequest.getOrderId(),
				orderEntryRequest.getUserID(), orderEntryRequest.getMemberId(), orderEntryRequest.getClientCode(), orderEntryRequest.getSchemeCd(), 
				orderEntryRequest.getBuySell(), orderEntryRequest.getBuySellType(), orderEntryRequest.getDpTxn(), orderEntryRequest.getOrderVal(), 
				orderEntryRequest.getQty(), orderEntryRequest.getAllRedeem(), orderEntryRequest.getFolioNo(), orderEntryRequest.getRemarks(), 
				orderEntryRequest.getKycStatus(), orderEntryRequest.getRefNo(), orderEntryRequest.getSubBrCode(), orderEntryRequest.getEuin(), 
				orderEntryRequest.getEuinVal(), orderEntryRequest.getMinRedeem(), orderEntryRequest.getDpc(), orderEntryRequest.getIpAdd(), 
				orderEntryRequest.getPassword(), orderEntryRequest.getPassKey(), orderEntryRequest.getParma1(), orderEntryRequest.getParma2(),
				orderEntryRequest.getParma3());
		
				OrderEntryResponse orderEntryResponse= new OrderEntryResponse();
				String[] orderResponseArray= orderEntry.split("\\|");
				orderEntryResponse.setTransCode(orderResponseArray[0]);
				orderEntryResponse.setUniqeRefNo(orderResponseArray[1]);
				orderEntryResponse.setOrderId(orderResponseArray[2]);
				orderEntryResponse.setUserID(orderResponseArray[3]);
				orderEntryResponse.setMemberId(orderResponseArray[4]);
				orderEntryResponse.setClientCode(orderResponseArray[5]);
				orderEntryResponse.setBseRemarks(orderResponseArray[6]);
				orderEntryResponse.setSuccessFlag(orderResponseArray[7]);
				
				try {
					logger.info(" Order Enter Response:" + GoForWealthAdminUtil.getJsonFromObject(orderEntryResponse,null));
				} catch (JsonProcessingException e) {
					e.printStackTrace();
				}
		return orderEntryResponse;
	}

	/**
	 * 
	 * @param orderEntryRequest
	 * @return OrderEntryResponse
	 */
	public OrderEntryResponse getOrderEntryForRedeem(OrderEntryRequest orderEntryRequest){
		MFOrderEntry port=getPort();
		
		try {
			logger.info(" Order Enter Request:" + GoForWealthAdminUtil.getJsonFromObject(orderEntryRequest,null));
		} catch (JsonProcessingException e) {
			e.printStackTrace();
		}

		String orderEntry=port.orderEntryParam(orderEntryRequest.getTransCode(), orderEntryRequest.getUniqueRefNo(), orderEntryRequest.getOrderId(),
				orderEntryRequest.getUserID(), orderEntryRequest.getMemberId(), orderEntryRequest.getClientCode(), orderEntryRequest.getSchemeCd(), 
				orderEntryRequest.getBuySell(), orderEntryRequest.getBuySellType(), orderEntryRequest.getDpTxn(), orderEntryRequest.getOrderVal(), 
				orderEntryRequest.getQty(), orderEntryRequest.getAllRedeem(), orderEntryRequest.getFolioNo(), orderEntryRequest.getRemarks(), 
				orderEntryRequest.getKycStatus(), orderEntryRequest.getRefNo(), orderEntryRequest.getSubBrCode(), orderEntryRequest.getEuin(), 
				orderEntryRequest.getEuinVal(), orderEntryRequest.getMinRedeem(), orderEntryRequest.getDpc(), orderEntryRequest.getIpAdd(), 
				orderEntryRequest.getPassword(), orderEntryRequest.getPassKey(), orderEntryRequest.getParma1(), orderEntryRequest.getParma2(),
				orderEntryRequest.getParma3());
		
				OrderEntryResponse orderEntryResponse= new OrderEntryResponse();
				String[] orderResponseArray= orderEntry.split("\\|");
				orderEntryResponse.setTransCode(orderResponseArray[0]);
				orderEntryResponse.setUniqeRefNo(orderResponseArray[1]);
				orderEntryResponse.setOrderId(orderResponseArray[2]);
				orderEntryResponse.setUserID(orderResponseArray[3]);
				orderEntryResponse.setMemberId(orderResponseArray[4]);
				orderEntryResponse.setClientCode(orderResponseArray[5]);
				orderEntryResponse.setBseRemarks(orderResponseArray[6]);
				orderEntryResponse.setSuccessFlag(orderResponseArray[7]);
				
				try {
					logger.info(" Order Enter Response:" + GoForWealthAdminUtil.getJsonFromObject(orderEntryResponse,null));
				} catch (JsonProcessingException e) {
					e.printStackTrace();
				}
		return orderEntryResponse;
	}
	
	
	/**
	 * 
	 * @param sipOrderEntryRequest
	 * @return SipOrderEntryResponse
	 */
	public SipOrderEntryResponse getSipOrderEntry(SipOrderEntryRequest sipOrderEntryRequest){

		
		
		/*String schemeCode=sipOrderEntryRequest.getSchemeCode();
		String L1OrL0RemovedSchemeCode="";
		
		if(schemeCode.contains("-L1")||schemeCode.contains("-L0")){
			if(schemeCode.contains("-L1")){
				L1OrL0RemovedSchemeCode=schemeCode.replace("-L1", "");
			}if(schemeCode.contains("-L0")){
				L1OrL0RemovedSchemeCode=schemeCode.replace("-L0", "");
			}
				
		}else{
			L1OrL0RemovedSchemeCode=sipOrderEntryRequest.getSchemeCode();
		}
		
		
		sipOrderEntryRequest.setSchemeCode(L1OrL0RemovedSchemeCode);*/
		
		try {
			logger.info(" Sip Order Entry Request:" + GoForWealthAdminUtil.getJsonFromObject(sipOrderEntryRequest,null));
		} catch (JsonProcessingException e) {
			e.printStackTrace();
		}
		
		MFOrderEntry port=getPort();
		String sipOrderEntry=port.sipOrderEntryParam(sipOrderEntryRequest.getTransactionCode(), sipOrderEntryRequest.getUniqueRefNo(), sipOrderEntryRequest.getSchemeCode(),
				sipOrderEntryRequest.getMemberCode(), sipOrderEntryRequest.getClientCode(), sipOrderEntryRequest.getUserID(), sipOrderEntryRequest.getInternalRefNo(),
				sipOrderEntryRequest.getTransMode(), sipOrderEntryRequest.getDpTxnMode(),sipOrderEntryRequest.getStartDate(), sipOrderEntryRequest.getFrequencyType(),
				sipOrderEntryRequest.getFrequencyAllowed(), sipOrderEntryRequest.getInstallmentAmount(), sipOrderEntryRequest.getNoOfInstallment(),
				sipOrderEntryRequest.getRemarks(), sipOrderEntryRequest.getFolioNo(), sipOrderEntryRequest.getFirstOrderFlag(), sipOrderEntryRequest.getSubberCode(),
				sipOrderEntryRequest.getEuin(), sipOrderEntryRequest.getEuinVal(), sipOrderEntryRequest.getDpc(), sipOrderEntryRequest.getRegId(), 
				sipOrderEntryRequest.getIpAdd(), sipOrderEntryRequest.getPassword(), sipOrderEntryRequest.getPassKey(), sipOrderEntryRequest.getParma1(),
				sipOrderEntryRequest.getParma2(), sipOrderEntryRequest.getParma3());

		
				SipOrderEntryResponse sipOrderEntryResponse= new SipOrderEntryResponse();
		
				String[] sipOrderEntryArray=sipOrderEntry.split("\\|");
				sipOrderEntryResponse.setTransCode(sipOrderEntryArray[0]);
				sipOrderEntryResponse.setUniqeRefNo(sipOrderEntryArray[1]);
				sipOrderEntryResponse.setMemberId(sipOrderEntryArray[2]);
				sipOrderEntryResponse.setClientCode(sipOrderEntryArray[3]);
				sipOrderEntryResponse.setUserID(sipOrderEntryArray[4]);
				sipOrderEntryResponse.setSipRegId(sipOrderEntryArray[5]);
				sipOrderEntryResponse.setBseRemarks(sipOrderEntryArray[6]);
				sipOrderEntryResponse.setSuccessFlag(sipOrderEntryArray[7]);
				
				try {
					logger.info(" Sip Order Entry Response:" + GoForWealthAdminUtil.getJsonFromObject(sipOrderEntryResponse,null));
				} catch (JsonProcessingException e) {
					e.printStackTrace();
				}

		return sipOrderEntryResponse;
	}
	
	
	/**
	 * 
	 * @param spreadOrderEntryRequest
	 * @return SpreadOrderEntryResponse
	 */
	public SpreadOrderEntryResponse getSpreadOrderEntry(SpreadOrderEntryRequest spreadOrderEntryRequest){
		
		
		/*String schemeCode=spreadOrderEntryRequest.getSchemeCode();
		String L1OrL0RemovedSchemeCode="";
		
		if(schemeCode.contains("-L1")||schemeCode.contains("-L0")){
			if(schemeCode.contains("-L1")){
				L1OrL0RemovedSchemeCode=schemeCode.replace("-L1", "");
			}if(schemeCode.contains("-L0")){
				L1OrL0RemovedSchemeCode=schemeCode.replace("-L0", "");
			}
				
		}else{
			L1OrL0RemovedSchemeCode=spreadOrderEntryRequest.getSchemeCode();
		}
		
		spreadOrderEntryRequest.setSchemeCode(L1OrL0RemovedSchemeCode);*/
		
		try {
			logger.info("Spread Order Entry Request:" + GoForWealthAdminUtil.getJsonFromObject(spreadOrderEntryRequest,null));
		} catch (JsonProcessingException e) {
			e.printStackTrace();
		}
		
		MFOrderEntry port=getPort();
		String spreadOrderEntry=port.spreadOrderEntryParam(spreadOrderEntryRequest.getTransactionCode(), spreadOrderEntryRequest.getUniqueRefNo(),
				spreadOrderEntryRequest.getOrderID(),spreadOrderEntryRequest.getUserID(), spreadOrderEntryRequest.getMemberId(), spreadOrderEntryRequest.getClientCode(),
				spreadOrderEntryRequest.getSchemeCode(), spreadOrderEntryRequest.getBuySell(), spreadOrderEntryRequest.getBuySellType(), spreadOrderEntryRequest.getDpTxn(),
				spreadOrderEntryRequest.getOrderValue(), spreadOrderEntryRequest.getRedemptionAmt(), spreadOrderEntryRequest.getAllUnitFlag(), 
				spreadOrderEntryRequest.getRedeemDate(), spreadOrderEntryRequest.getFolioNo(), spreadOrderEntryRequest.getRemarks(), spreadOrderEntryRequest.getKycStatus(),
				spreadOrderEntryRequest.getRefNo(), spreadOrderEntryRequest.getSubBroCode(), spreadOrderEntryRequest.getEuin(), spreadOrderEntryRequest.getEuinVal(),
				spreadOrderEntryRequest.getMinRedeem(), spreadOrderEntryRequest.getDpc(), spreadOrderEntryRequest.getIpAddress(), spreadOrderEntryRequest.getPassword(),
				spreadOrderEntryRequest.getPassKey(), spreadOrderEntryRequest.getParam1(), spreadOrderEntryRequest.getParam2(), spreadOrderEntryRequest.getParam3());
		
				
				SpreadOrderEntryResponse spreadOrderEntryResponse = new SpreadOrderEntryResponse();
				String[] spreadOrderEntryArray=spreadOrderEntry.split("\\|");
				spreadOrderEntryResponse.setTransCode(spreadOrderEntryArray[0]);
				spreadOrderEntryResponse.setUniqeRefNo(spreadOrderEntryArray[1]);
				spreadOrderEntryResponse.setOrderId(spreadOrderEntryArray[2]);
				spreadOrderEntryResponse.setUserID(spreadOrderEntryArray[3]);
				spreadOrderEntryResponse.setMemberId(spreadOrderEntryArray[4]);
				spreadOrderEntryResponse.setClientCode(spreadOrderEntryArray[5]);
				spreadOrderEntryResponse.setSuccessFlag(spreadOrderEntryArray[6]);
				spreadOrderEntryResponse.setBseRemarks(spreadOrderEntryArray[7]);
		
				try {
					logger.info("Spread Order Entry Response:" + GoForWealthAdminUtil.getJsonFromObject(spreadOrderEntryResponse,null));
				} catch (JsonProcessingException e) {
					e.printStackTrace();
				}
		return spreadOrderEntryResponse;
	}
	
	
	/**
	 * 
	 * @param switchOrderEntryRequest
	 * @return SwitchOrderEntryResponse
	 */
	public SwitchOrderEntryResponse getSwitchOrderEntry(SwitchOrderEntryRequest switchOrderEntryRequest){
		
		String fromSchemeCode=switchOrderEntryRequest.getFromSchemeCd();
		String toSchemeCode=switchOrderEntryRequest.getToSchemeCd();
		String L1OrL0RemovedFromSchemeCode="";
		String L1OrL0RemovedToSchemeCode="";
		
		if(fromSchemeCode.contains("-L1")||fromSchemeCode.contains("-L0")){
			if(fromSchemeCode.contains("-L1")){
				L1OrL0RemovedFromSchemeCode=fromSchemeCode.replace("-L1", "");
			}if(fromSchemeCode.contains("-L0")){
				L1OrL0RemovedFromSchemeCode=fromSchemeCode.replace("-L0", "");
			}
				
		}else{
			L1OrL0RemovedFromSchemeCode=switchOrderEntryRequest.getFromSchemeCd();
		}
		if(toSchemeCode.contains("-L1")||toSchemeCode.contains("-L0")){
			if(toSchemeCode.contains("-L1")){
				L1OrL0RemovedToSchemeCode=toSchemeCode.replace("-L1", "");
			}if(toSchemeCode.contains("-L0")){
				L1OrL0RemovedToSchemeCode=toSchemeCode.replace("-L0", "");
			}
				
		}else{
			L1OrL0RemovedToSchemeCode=switchOrderEntryRequest.getToSchemeCd();
		}
		
		switchOrderEntryRequest.setFromSchemeCd(L1OrL0RemovedFromSchemeCode);
		switchOrderEntryRequest.setToSchemeCd(L1OrL0RemovedToSchemeCode);
		
		try {
			logger.info("Switch Order Request:" + GoForWealthAdminUtil.getJsonFromObject(switchOrderEntryRequest,null));
		} catch (JsonProcessingException e) {
			e.printStackTrace();
		}
		
		
		MFOrderEntry port= getPort();
		String switchOrderEntry= port.switchOrderEntryParam(switchOrderEntryRequest.getTransCode(), switchOrderEntryRequest.getTransNo(),
				switchOrderEntryRequest.getOrderId(), switchOrderEntryRequest.getUserID(), switchOrderEntryRequest.getMemberId(), switchOrderEntryRequest.getClientCode(),
				switchOrderEntryRequest.getFromSchemeCd(), switchOrderEntryRequest.getToSchemeCd(), switchOrderEntryRequest.getBuySell(), switchOrderEntryRequest.getBuySellType(),
				switchOrderEntryRequest.getDpTxn(),switchOrderEntryRequest.getSwitchAmount(), switchOrderEntryRequest.getSwitchUnits(), switchOrderEntryRequest.getAllUnitsFlag(), 
				switchOrderEntryRequest.getFolioNo(), switchOrderEntryRequest.getRemarks(), switchOrderEntryRequest.getKycStatus(), switchOrderEntryRequest.getSubBrCode(), 
				switchOrderEntryRequest.getEuin(), switchOrderEntryRequest.getEuinVal(), switchOrderEntryRequest.getMinRedeem(), switchOrderEntryRequest.getIpAdd(), 
				switchOrderEntryRequest.getPassword(), switchOrderEntryRequest.getPassKey(), switchOrderEntryRequest.getParma1(), switchOrderEntryRequest.getParma2(),
				switchOrderEntryRequest.getParma3());
				
				SwitchOrderEntryResponse switchOrderEntryResponse= new SwitchOrderEntryResponse();
				String[] switchOrderEntryArray=switchOrderEntry.split("\\|");
				
				switchOrderEntryResponse.setTransCode(switchOrderEntryArray[0]);
				switchOrderEntryResponse.setUniqeRefNo(switchOrderEntryArray[1]);
				switchOrderEntryResponse.setOrderId(switchOrderEntryArray[2]);
				switchOrderEntryResponse.setUserID(switchOrderEntryArray[3]);
				switchOrderEntryResponse.setMemberId(switchOrderEntryArray[4]);
				switchOrderEntryResponse.setClientCode(switchOrderEntryArray[5]);
				switchOrderEntryResponse.setSuccessFlag(switchOrderEntryArray[6]);
				switchOrderEntryResponse.setBseRemarks(switchOrderEntryArray[7]);
				try {
					logger.info("Switch Order Response:" + GoForWealthAdminUtil.getJsonFromObject(switchOrderEntryResponse,null));
				} catch (JsonProcessingException e) {
					e.printStackTrace();
				}

		return switchOrderEntryResponse;
	}
	
	
	
	/**
	 * 
	 * @param xsipOrderEntryRequest
	 * @return XsipOrderEntryResponse
	 */
	public XsipOrderEntryResponse getXsipOrderEntry(XsipOrderEntryRequest xsipOrderEntryRequest){
		
		
		String schemeCode=xsipOrderEntryRequest.getSchemeCode();
		String L1OrL0RemovedSchemeCode="";
		
		if(schemeCode.contains("-L1")||schemeCode.contains("-L0")){
			if(schemeCode.contains("-L1")){
				L1OrL0RemovedSchemeCode=schemeCode.replace("-L1", "");
			}if(schemeCode.contains("-L0")){
				L1OrL0RemovedSchemeCode=schemeCode.replace("-L0", "");
			}
				
		}else{
			L1OrL0RemovedSchemeCode=xsipOrderEntryRequest.getSchemeCode();
		}
		
		xsipOrderEntryRequest.setSchemeCode(L1OrL0RemovedSchemeCode);
		try {
			logger.info("Xsip Order Entry Request:" + GoForWealthAdminUtil.getJsonFromObject(xsipOrderEntryRequest,null));
		} catch (JsonProcessingException e) {
			e.printStackTrace();
		}
		
		MFOrderEntry port= getPort();
		String xsipOrderEntry=port.xsipOrderEntryParam(xsipOrderEntryRequest.getTransactionCode(), xsipOrderEntryRequest.getUniqueRefNo(), 
				xsipOrderEntryRequest.getSchemeCode(), xsipOrderEntryRequest.getMemberCode(), xsipOrderEntryRequest.getClientCode(), xsipOrderEntryRequest.getUserID(),
				xsipOrderEntryRequest.getInternalRefNo(), xsipOrderEntryRequest.getTransMode(), xsipOrderEntryRequest.getDpTxnMode(), xsipOrderEntryRequest.getStartDate(),
				xsipOrderEntryRequest.getFrequencyType(), xsipOrderEntryRequest.getFrequencyAllowed(), xsipOrderEntryRequest.getInstallmentAmount(), 
				xsipOrderEntryRequest.getNoOfInstallment(), xsipOrderEntryRequest.getRemarks(), xsipOrderEntryRequest.getFolioNo(),
				xsipOrderEntryRequest.getFirstOrderFlag(), xsipOrderEntryRequest.getBrokerage(), xsipOrderEntryRequest.getMandateID(), xsipOrderEntryRequest.getSubberCode(),
				xsipOrderEntryRequest.getEuin(), xsipOrderEntryRequest.getEuinVal(), xsipOrderEntryRequest.getDpc(), xsipOrderEntryRequest.getXsipRegID(), 
				xsipOrderEntryRequest.getIpAdd(), xsipOrderEntryRequest.getPassword(), xsipOrderEntryRequest.getPassKey(), xsipOrderEntryRequest.getParma1(), 
				xsipOrderEntryRequest.getParma2(), xsipOrderEntryRequest.getParma3());
		
		        XsipOrderEntryResponse xsipOrderEntryResponse= new XsipOrderEntryResponse();
		        String[] xsipOrderEntryArray=xsipOrderEntry.split("\\|");
		        xsipOrderEntryResponse.setTransCode(xsipOrderEntryArray[0]);
		        xsipOrderEntryResponse.setUniqeRefNo(xsipOrderEntryArray[1]);
		        xsipOrderEntryResponse.setMemberId(xsipOrderEntryArray[2]);
		        xsipOrderEntryResponse.setClientCode(xsipOrderEntryArray[3]);
		        xsipOrderEntryResponse.setUserID(xsipOrderEntryArray[4]);
		        xsipOrderEntryResponse.setXsipRegId(xsipOrderEntryArray[5]);
		        xsipOrderEntryResponse.setBseRemarks(xsipOrderEntryArray[6]);
		        xsipOrderEntryResponse.setSuccessFlag(xsipOrderEntryArray[7]);
		
		        try {
					logger.info("Xsip Order Entry Response:" + GoForWealthAdminUtil.getJsonFromObject(xsipOrderEntryResponse,null));
				} catch (JsonProcessingException e) {
					e.printStackTrace();
				}
		
		return xsipOrderEntryResponse;
	}

	public String getPasswordForChildOrder(SystemPropertiesRepository systemPropertiesRepository2) throws GoForWealthPRSException {
		
		String MemberId="";
		String PassKey ="";
		String Password="";
		String RequestType="XSIP";
		String UserId="";
		
		List<SystemProperties> systemPropertiesList=  systemPropertiesRepository2.findAll();
		for (SystemProperties systemProperties : systemPropertiesList) {
			if(systemProperties.getId().getPropertyKey().equals("UserId")){
				
				UserId = systemProperties.getId().getPropertyValue();
			}else if(systemProperties.getId().getPropertyKey().equals("MemberId")){
				
				MemberId = systemProperties.getId().getPropertyValue();
			}else if(systemProperties.getId().getPropertyKey().equals("Password")){
				
				Password = systemProperties.getId().getPropertyValue();
			}
		}
		OtpGenerator otpGenerator = new OtpGenerator(5,true);
		String passKey = otpGenerator.generateOTP();
		PassKey = passKey;
		
		
		
		ChildPassword getPasswordRequest = new ChildPassword(MemberId,PassKey, Password,RequestType,UserId);
		
		String password=null;
		HttpHeaders httpHeaders = new HttpHeaders();
		httpHeaders.setContentType(MediaType.APPLICATION_JSON);
		StoreConf storeConf = storeConfRepository.findByKeyword(GoForWealthPRSConstants.CHILD_ORDER_PASSWORD_URL);
		String url = storeConf.getKeywordValue();
		//String url="http://www.bsestarmf.in/StarMFWebService/StarMFWebService.svc/GetPasswordForChildOrder";
		password = getBseResponseForPassword(url, getPasswordRequest, httpHeaders, HttpMethod.POST);
		String res[] = password.split("\\|");
		if(res[0].equals("100")){
			password = res[1];
		}
		return password;
	}
	
	
	
	public static String getBseResponseForPassword(String url, Object jsonResponse, HttpHeaders headersToSend,
			HttpMethod httpMethod) throws GoForWealthPRSException {
		String response=null;
		Object responseObj = null;
		try {
			RestTemplate restTemplate = new RestTemplate();

			HttpEntity<?> requestEntity = new HttpEntity<Object>(jsonResponse,headersToSend);
			ResponseEntity<String> responseEntity = restTemplate.postForEntity(url, requestEntity, String.class);
			

			if (responseEntity != null) {
				responseObj = responseEntity.getBody();
				JSONObject jsonObject = new JSONObject(responseObj.toString());
				//logger.info("sdfsdf"+jsonObject.getString("ResponseString"));
				response=jsonObject.getString("Status")+"|"+jsonObject.getString("ResponseString");

			}	
			if(response==null){
				throw new GoForWealthPRSException(GoForWealthPRSErrorMessageEnum.FAILURE_CODE.getValue(),GoForWealthPRSErrorMessageEnum.FAILURE_MESSAGE.getValue());
			}
		} catch (ResourceAccessException ex) {
			//logger.error("Exception Occured : ", ex);
			throw new GoForWealthPRSException(
					GoForWealthPRSErrorMessageEnum.FAILURE_CODE.getValue(),
					GoForWealthPRSErrorMessageEnum.FAILURE_MESSAGE.getValue());
		} catch (HttpClientErrorException ex) {
			//logger.error("Exception Occured : ", ex);
			throw new GoForWealthPRSException(
					GoForWealthPRSErrorMessageEnum.FAILURE_CODE.getValue(),
					GoForWealthPRSErrorMessageEnum.FAILURE_MESSAGE.getValue());
		} catch (Exception e) {
			//logger.error("Exception Occured : ", e);
			throw new GoForWealthPRSException(
					GoForWealthPRSErrorMessageEnum.FAILURE_CODE.getValue(),
					GoForWealthPRSErrorMessageEnum.FAILURE_MESSAGE.getValue());
		}
		//logger.info("Out invokeURL()");
		return response;

	
	}

	public String getChildOrderDetail(String ClientCode, String Date, String EncryptedPassword, String MemberCode,
			String RegnNo, String SystematicPlanType, SystemPropertiesRepository systemPropertiesRepository2) throws GoForWealthPRSException {
		String encPassword=getPasswordForChildOrder(systemPropertiesRepository2);
		ChildOrderDetailRequest childOrderDetailRequest= new ChildOrderDetailRequest(ClientCode, Date, encPassword,
				MemberCode, RegnNo, SystematicPlanType);
		try {
			logger.info("Child Order Request:" + GoForWealthAdminUtil.getJsonFromObject(childOrderDetailRequest,null));
		} catch (JsonProcessingException e) {
			e.printStackTrace();
		}
		String ChildOrderId=null;
		HttpHeaders httpHeaders = new HttpHeaders();
		httpHeaders.setContentType(MediaType.APPLICATION_JSON);
		StoreConf storeConf = storeConfRepository.findByKeyword(GoForWealthPRSConstants.CHILD_ORDER_API_URL);
		String url = storeConf.getKeywordValue();
		ChildOrderId = getBseResponse(url, childOrderDetailRequest, httpHeaders, HttpMethod.POST);
		System.out.println(ChildOrderId);
		logger.info("Child Order Response : " + ChildOrderId);
		return ChildOrderId;
	}
	
	public static String getBseResponse(String url, Object jsonResponse, HttpHeaders headersToSend,
			HttpMethod httpMethod) throws GoForWealthPRSException {
		String response=null;
		Object responseObj = null;
		try {
			RestTemplate restTemplate = new RestTemplate();

			HttpEntity<?> requestEntity = new HttpEntity<Object>(jsonResponse,headersToSend);
			ResponseEntity<String> responseEntity = restTemplate.postForEntity(url, requestEntity, String.class);
			
			if (responseEntity != null) {
				responseObj = responseEntity.getBody();
				JSONObject jsonObject = new JSONObject(responseObj.toString());
				
				String checkStatus = jsonObject.getString("Status");
				if(checkStatus.equals("101")){
					return checkStatus;
				}
				else{
					JSONArray jSONArray=jsonObject.getJSONArray("ChildOrderDetails");
					JSONObject JSONObject5 =new JSONObject(jSONArray.get(0).toString());
					String orderNumber = JSONObject5.getString("OrderNumber");
					return orderNumber;
				}
				/*String checkStatus = jsonObject.getString("Status");
				response=jsonObject.getString("Status")+"|"+jsonObject.getString("ChildOrderDetails");
				if(checkStatus.equals("101")){
					return response;
				}*/

			}	
			Document html =Jsoup.parse(response);
			/*String str=html.body().select("input[name=msg]").first().attr("value");
			String[] splitedStr = str.split("\\|");
			str = splitedStr[splitedStr.length-2];
			response=str;*/
			response=html.toString();
			if(response==null){
				throw new GoForWealthPRSException(GoForWealthPRSErrorMessageEnum.FAILURE_CODE.getValue(),GoForWealthPRSErrorMessageEnum.FAILURE_MESSAGE.getValue());
			}
		} catch (ResourceAccessException ex) {
			logger.error("Exception Occured : ", ex);
			throw new GoForWealthPRSException(
					GoForWealthPRSErrorMessageEnum.FAILURE_CODE.getValue(),
					GoForWealthPRSErrorMessageEnum.FAILURE_MESSAGE.getValue());
		} catch (HttpClientErrorException ex) {
			logger.error("Exception Occured : ", ex);
			throw new GoForWealthPRSException(
					GoForWealthPRSErrorMessageEnum.FAILURE_CODE.getValue(),
					GoForWealthPRSErrorMessageEnum.FAILURE_MESSAGE.getValue());
		} catch (Exception e) {
			logger.error("Exception Occured : ", e);
			throw new GoForWealthPRSException(
					GoForWealthPRSErrorMessageEnum.FAILURE_CODE.getValue(),
					GoForWealthPRSErrorMessageEnum.FAILURE_MESSAGE.getValue());
		}
		logger.info("Out invokeURL()");
		return response;

	}

	public Object getOrderStatus(CheckOrderStatusRequest checkOrderStatusRequest) {
		Object response = null;
		
		try {
			logger.info("Order Status Request:" + GoForWealthAdminUtil.getJsonFromObject(checkOrderStatusRequest,null));
		} catch (JsonProcessingException e) {
			e.printStackTrace();
		}
		HttpHeaders httpHeaders = new HttpHeaders();
		httpHeaders.setContentType(MediaType.APPLICATION_JSON);
		StoreConf storeConf = storeConfRepository.findByKeyword(GoForWealthPRSConstants.ORDER_STATUS_API_URL);
		String url = storeConf.getKeywordValue();
		try {
			response= getBseResponseForOrderStatus(url, checkOrderStatusRequest, httpHeaders, HttpMethod.POST);
			logger.info("Order Status Response:" + response);
		} catch (GoForWealthPRSException e) {
			e.printStackTrace();
		}
		
		return response;
		
	}

	public static Object getBseResponseForOrderStatus(String url, Object jsonResponse, HttpHeaders headersToSend,
			HttpMethod httpMethod) throws GoForWealthPRSException {
		String response=null;
		Object responseObj = null;
		try {
			RestTemplate restTemplate = new RestTemplate();

			HttpEntity<?> requestEntity = new HttpEntity<Object>(jsonResponse,headersToSend);
			ResponseEntity<String> responseEntity = restTemplate.postForEntity(url, requestEntity, String.class);
			

			if (responseEntity != null) {
				responseObj = responseEntity.getBody();
				/*JSONObject jsonObject = new JSONObject(responseObj.toString());
				response = jsonObject.getString("Status")+"|"+jsonObject.getString("OrderDetails");
				if(response.equals("101")){
					return response;
				}
			else{
				response=jsonObject.getString("Status")+"|"+jsonObject.getString("OrderDetails");
				return response;
			}

			}	
			if(response==null){*/
				return responseObj;
			}
		} catch (ResourceAccessException ex) {
			logger.error("Exception Occured : ", ex);
			throw new GoForWealthPRSException(
					GoForWealthPRSErrorMessageEnum.FAILURE_CODE.getValue(),
					GoForWealthPRSErrorMessageEnum.FAILURE_MESSAGE.getValue());
		} catch (HttpClientErrorException ex) {
			logger.error("Exception Occured : ", ex);
			throw new GoForWealthPRSException(
					GoForWealthPRSErrorMessageEnum.FAILURE_CODE.getValue(),
					GoForWealthPRSErrorMessageEnum.FAILURE_MESSAGE.getValue());
		} catch (Exception e) {
			logger.error("Exception Occured : ", e);
			throw new GoForWealthPRSException(
					GoForWealthPRSErrorMessageEnum.FAILURE_CODE.getValue(),
					GoForWealthPRSErrorMessageEnum.FAILURE_MESSAGE.getValue());
		}
		logger.info("Out invokeURL()");
		return response;

	}

	public Object getAllotementStatement(OrderAllotmentRequest orderAllotmentRequest) {

		Object response = null;

		try {
			logger.info("Order Allotement Request:" + GoForWealthAdminUtil.getJsonFromObject(orderAllotmentRequest,null));
		} catch (JsonProcessingException e) {
			e.printStackTrace();
		}
		HttpHeaders httpHeaders = new HttpHeaders();
		httpHeaders.setContentType(MediaType.APPLICATION_JSON);
		StoreConf storeConf = storeConfRepository.findByKeyword(GoForWealthPRSConstants.ALLOTMENT_STATEMENT_API_URL);
		String url = storeConf.getKeywordValue();
		try {
			response= getBseResponseForOrderAllotement(url, orderAllotmentRequest, httpHeaders, HttpMethod.POST);
			logger.info("Order Allotement Response:" + response);
		} catch (GoForWealthPRSException e) {
			e.printStackTrace();
		}

		return response;

	}
	
	public static Object getBseResponseForOrderAllotement(String url, Object jsonResponse, HttpHeaders headersToSend,
			HttpMethod httpMethod) throws GoForWealthPRSException {
		Object response=null;
		Object responseObj = null;
		try {
			RestTemplate restTemplate = new RestTemplate();

			HttpEntity<?> requestEntity = new HttpEntity<Object>(jsonResponse,headersToSend);
			ResponseEntity<String> responseEntity = restTemplate.postForEntity(url, requestEntity, String.class);
			

			if (responseEntity != null) {
				responseObj = responseEntity.getBody();
				/*JSONObject jsonObject = new JSONObject(responseObj.toString());
				response = jsonObject.getString("Status")+"|"+jsonObject.getString("AllotmentDetails");
				if(response.equals("101")){
					return response;
				}
			else{
				response=jsonObject.getString("Status")+"|"+jsonObject.getString("AllotmentDetails");
				return response;
			}

			}	
			if(response==null){*/
				response = responseObj;
			}
		} catch (ResourceAccessException ex) {
			logger.error("Exception Occured : ", ex);
			throw new GoForWealthPRSException(
					GoForWealthPRSErrorMessageEnum.FAILURE_CODE.getValue(),
					GoForWealthPRSErrorMessageEnum.FAILURE_MESSAGE.getValue());
		} catch (HttpClientErrorException ex) {
			logger.error("Exception Occured : ", ex);
			throw new GoForWealthPRSException(
					GoForWealthPRSErrorMessageEnum.FAILURE_CODE.getValue(),
					GoForWealthPRSErrorMessageEnum.FAILURE_MESSAGE.getValue());
		} catch (Exception e) {
			logger.error("Exception Occured : ", e);
			throw new GoForWealthPRSException(
					GoForWealthPRSErrorMessageEnum.FAILURE_CODE.getValue(),
					GoForWealthPRSErrorMessageEnum.FAILURE_MESSAGE.getValue());
		}
		logger.info("Out invokeURL()");
		return response;

	}

	
	public Object getMandateDetails(MandateDetailsRequestParam mandateDetailsRequestParam) {

		String accessToken = getAccessToken(mandateDetailsRequestParam);
		
		GetMandateDetailRequest getMandateDetailRequest = new GetMandateDetailRequest(mandateDetailsRequestParam.getFromDate(),
				mandateDetailsRequestParam.getToDate(), mandateDetailsRequestParam.getMemberCode(), mandateDetailsRequestParam.getClientCode(), 
				mandateDetailsRequestParam.getMandateId(), accessToken);
		Object response = null;

		try {
			logger.info("Mandate Detail Request:" + GoForWealthAdminUtil.getJsonFromObject(getMandateDetailRequest,null));
		} catch (JsonProcessingException e) {
			e.printStackTrace();
		}
		HttpHeaders httpHeaders = new HttpHeaders();
		httpHeaders.setContentType(MediaType.APPLICATION_JSON);
		StoreConf storeConf = storeConfRepository.findByKeyword(GoForWealthPRSConstants.MANDATE_DETAIL_API_URL);
		String url = storeConf.getKeywordValue();
		try {
			response= getBseResponseForMandateDetails(url, getMandateDetailRequest, httpHeaders, HttpMethod.POST);
			logger.info("Mandate Detail Response:" + response);
		} catch (GoForWealthPRSException e) {
			e.printStackTrace();
		}
		return response;
	}
	
	
	public String getAccessToken(MandateDetailsRequestParam mandateDetailsRequestParam) {
		String tokenResponse = null;
		GetAccessTokenRequest getAccessTokenRequest = new GetAccessTokenRequest(mandateDetailsRequestParam.getRequestType(), mandateDetailsRequestParam.getUserId(), 
				mandateDetailsRequestParam.getMemberId(), mandateDetailsRequestParam.getPassword(), mandateDetailsRequestParam.getPassKey());
		try {
			logger.info("Get Access Token  Request:" + GoForWealthAdminUtil.getJsonFromObject(getAccessTokenRequest,null));
		} catch (JsonProcessingException e) {
			e.printStackTrace();
		}
		HttpHeaders httpHeaders = new HttpHeaders();
		httpHeaders.setContentType(MediaType.APPLICATION_JSON);
		StoreConf storeConf = storeConfRepository.findByKeyword(GoForWealthPRSConstants.GET_TOKEN_ACCESS_API_URL);
		String url = storeConf.getKeywordValue();
		try {
			tokenResponse= getBseResponseForAccessToken(url, getAccessTokenRequest, httpHeaders, HttpMethod.POST);
			logger.info("Get Access Token  Response:" + tokenResponse);
		} catch (GoForWealthPRSException e) {
			e.printStackTrace();
		}
		return tokenResponse;
	}

	
	public static Object getBseResponseForMandateDetails(String url, Object jsonResponse, HttpHeaders headersToSend,HttpMethod httpMethod) throws GoForWealthPRSException {
		Object response=null;
		Object responseObj = null;
		try {
			RestTemplate restTemplate = new RestTemplate();
			HttpEntity<?> requestEntity = new HttpEntity<Object>(jsonResponse,headersToSend);
			ResponseEntity<String> responseEntity = restTemplate.postForEntity(url, requestEntity, String.class);
			if (responseEntity != null) {
				responseObj = responseEntity.getBody();
				/*JSONObject jsonObject = new JSONObject(responseObj.toString());
				response = jsonObject.getString("Status");
				if(response.equals("101")){
					return response;
				}
			else{
				response=jsonObject.getString("Status")+"|"+jsonObject.getString("MandateDetails");
				return response;
			}
			}	
			if(response==null){*/
				response = responseObj;
			}
		} catch (ResourceAccessException ex) {
			logger.error("Exception Occured : ", ex);
			throw new GoForWealthPRSException(
					GoForWealthPRSErrorMessageEnum.FAILURE_CODE.getValue(),
					GoForWealthPRSErrorMessageEnum.FAILURE_MESSAGE.getValue());
		} catch (HttpClientErrorException ex) {
			logger.error("Exception Occured : ", ex);
			throw new GoForWealthPRSException(
					GoForWealthPRSErrorMessageEnum.FAILURE_CODE.getValue(),
					GoForWealthPRSErrorMessageEnum.FAILURE_MESSAGE.getValue());
		} catch (Exception e) {
			logger.error("Exception Occured : ", e);
			throw new GoForWealthPRSException(
					GoForWealthPRSErrorMessageEnum.FAILURE_CODE.getValue(),
					GoForWealthPRSErrorMessageEnum.FAILURE_MESSAGE.getValue());
		}
		logger.info("Out invokeURL()");
		return response;
	}
	
	
	public static String getBseResponseForAccessToken(String url, Object jsonResponse, HttpHeaders headersToSend,HttpMethod httpMethod) throws GoForWealthPRSException {
		String response=null;
		Object responseObj = null;
		try {
			RestTemplate restTemplate = new RestTemplate();
			HttpEntity<?> requestEntity = new HttpEntity<Object>(jsonResponse,headersToSend);
			ResponseEntity<String> responseEntity = restTemplate.postForEntity(url, requestEntity, String.class);
			if (responseEntity != null) {
				responseObj = responseEntity.getBody();
				JSONObject jsonObject = new JSONObject(responseObj.toString());
				response = jsonObject.getString("Status");
				if(response.equals("101")){
					return response;
				}else{
					response=jsonObject.getString("ResponseString");
					return response;
				}
			}	
			if(response==null){
				response = "Data Not Found";
			}
		} catch (ResourceAccessException ex) {
			logger.error("Exception Occured : ", ex);
			throw new GoForWealthPRSException(
					GoForWealthPRSErrorMessageEnum.FAILURE_CODE.getValue(),
					GoForWealthPRSErrorMessageEnum.FAILURE_MESSAGE.getValue());
		} catch (HttpClientErrorException ex) {
			logger.error("Exception Occured : ", ex);
			throw new GoForWealthPRSException(
					GoForWealthPRSErrorMessageEnum.FAILURE_CODE.getValue(),
					GoForWealthPRSErrorMessageEnum.FAILURE_MESSAGE.getValue());
		} catch (Exception e) {
			logger.error("Exception Occured : ", e);
			throw new GoForWealthPRSException(
					GoForWealthPRSErrorMessageEnum.FAILURE_CODE.getValue(),
					GoForWealthPRSErrorMessageEnum.FAILURE_MESSAGE.getValue());
		}
		logger.info("Out invokeURL()");
		return response;
	}


	public Object getMandateAuthenticationUrlDetail(MandateDetailsRequestParam mandateDetailsRequestParam) {
		MandateAuthenticationCheckRequest mandateAuthenticationCheckRequest = new MandateAuthenticationCheckRequest(mandateDetailsRequestParam.getClientCode(),mandateDetailsRequestParam.getMandateId(),mandateDetailsRequestParam.getMemberCode(),mandateDetailsRequestParam.getPassword(),mandateDetailsRequestParam.getUserId());
		Object response = null;
		try {
			logger.info("Mandate Authentication Detail Request : " + GoForWealthAdminUtil.getJsonFromObject(mandateAuthenticationCheckRequest,null));
		}catch(JsonProcessingException e) {
			e.printStackTrace();
		}
		HttpHeaders httpHeaders = new HttpHeaders();
		httpHeaders.setContentType(MediaType.APPLICATION_JSON);
		StoreConf storeConf = storeConfRepository.findByKeyword(GoForWealthPRSConstants.MANDATE_AUTHENTICATION_API_URL);
		String url = storeConf.getKeywordValue();
		try{
			response= getBseResponseForMandateDetails(url,mandateAuthenticationCheckRequest, httpHeaders, HttpMethod.POST);
			logger.info("Mandate Authentication Detail Response:" + response);
		}catch(GoForWealthPRSException e) {
			e.printStackTrace();
		}
		return response;
	}

	
	 Object orderMfApi(OrderRedemptionStatusRequest orderRedemptionStatusRequest) {
		
		 Object response = null;

			try {
				logger.info("Order Redemption Status Request:" + GoForWealthAdminUtil.getJsonFromObject(orderRedemptionStatusRequest,null));
			} catch (JsonProcessingException e) {
				e.printStackTrace();
			}
			HttpHeaders httpHeaders = new HttpHeaders();
			httpHeaders.setContentType((MediaType.APPLICATION_JSON));
			StoreConf storeConf = storeConfRepository.findByKeyword(GoForWealthPRSConstants.REDEMPTION_STATUS_API_URL);
			String url = storeConf.getKeywordValue();
			try {
				response= getBseResponseForRedemptionDetails(url, orderRedemptionStatusRequest, httpHeaders, HttpMethod.POST);
				logger.info("Order Redemption Status Response:" + response);
			} catch (GoForWealthPRSException e) {
				e.printStackTrace();
			}
			return response;
	}
	 
	 public static Object getBseResponseForRedemptionDetails(String url, Object jsonResponse, HttpHeaders headersToSend,HttpMethod httpMethod) throws GoForWealthPRSException {
			Object response=null;
			Object responseObj = null;
			try {
				
				/*KeyStore keyStore = KeyStore.getInstance(KeyStore.getDefaultType());
				keyStore.load(new FileInputStream(new File("D://go4wealth-image/cert/keystore.jks")),"go4w_wealth".toCharArray());
				SSLConnectionSocketFactory socketFactory = new SSLConnectionSocketFactory( new SSLContextBuilder().loadTrustMaterial(null, new TrustSelfSignedStrategy())
				                .loadKeyMaterial(keyStore, "go4w_wealth".toCharArray()).build());
				HttpClient httpClient = HttpClients.custom().setSSLSocketFactory(socketFactory).build();
				ClientHttpRequestFactory requestFactory = new HttpComponentsClientHttpRequestFactory(httpClient);
				RestTemplate restTemplate = new RestTemplate(requestFactory);
				//ResponseEntity<String> response = restTemplate.getForEntity("https://localhost:8443", String.class);
				HttpEntity<?> requestEntity = new HttpEntity<Object>(jsonResponse,headersToSend);
				ResponseEntity<String> responseEntity = restTemplate.postForEntity(url, requestEntity, String.class);*/
				
				
				/*TrustStrategy acceptingTrustStrategy = (X509Certificate[] chain, String authType) -> true;
				SSLContext sslContext = org.apache.http.ssl.SSLContexts.custom().loadTrustMaterial(null, acceptingTrustStrategy).build();
				SSLConnectionSocketFactory csf = new SSLConnectionSocketFactory(sslContext);
				CloseableHttpClient httpClient = HttpClients.custom().setSSLSocketFactory(csf).build();
				HttpComponentsClientHttpRequestFactory requestFactory = new HttpComponentsClientHttpRequestFactory();
				requestFactory.setHttpClient(httpClient);
				RestTemplate restTemplate = new RestTemplate(requestFactory);
				HttpEntity<?> requestEntity = new HttpEntity<Object>(jsonResponse,headersToSend);
				ResponseEntity<String> responseEntity = restTemplate.postForEntity(url, requestEntity, String.class);*/
				
				
				RestTemplate restTemplate = new RestTemplate();
				HttpEntity<?> requestEntity = new HttpEntity<Object>(jsonResponse,headersToSend);
				ResponseEntity<String> responseEntity = restTemplate.postForEntity(url, requestEntity, String.class);
				if (responseEntity != null) {
					responseObj = responseEntity.getBody();
					
					response = responseObj;
				}else{
					response = responseObj;
				}
			} catch (ResourceAccessException ex) {
				logger.error("Exception Occured : ", ex);
				throw new GoForWealthPRSException(
						GoForWealthPRSErrorMessageEnum.FAILURE_CODE.getValue(),
						GoForWealthPRSErrorMessageEnum.FAILURE_MESSAGE.getValue());
			} catch (HttpClientErrorException ex) {
				logger.error("Exception Occured : ", ex);
				throw new GoForWealthPRSException(
						GoForWealthPRSErrorMessageEnum.FAILURE_CODE.getValue(),
						GoForWealthPRSErrorMessageEnum.FAILURE_MESSAGE.getValue());
			} catch (Exception e) {
				logger.error("Exception Occured : ", e);
				throw new GoForWealthPRSException(
						GoForWealthPRSErrorMessageEnum.FAILURE_CODE.getValue(),
						GoForWealthPRSErrorMessageEnum.FAILURE_MESSAGE.getValue());
			}
			logger.info("Out invokeURL()");
			return response;
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
