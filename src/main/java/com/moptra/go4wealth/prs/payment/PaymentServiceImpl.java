package com.moptra.go4wealth.prs.payment;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

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
import org.springframework.stereotype.Component;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.ResourceAccessException;
import org.springframework.web.client.RestTemplate;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.moptra.go4wealth.admin.common.util.GoForWealthAdminUtil;
import com.moptra.go4wealth.bean.BankCodes;
import com.moptra.go4wealth.bean.BankDetails;
import com.moptra.go4wealth.bean.OrderItem;
import com.moptra.go4wealth.bean.Orders;
import com.moptra.go4wealth.bean.StoreConf;
import com.moptra.go4wealth.bean.SystemProperties;
import com.moptra.go4wealth.bean.User;
import com.moptra.go4wealth.prs.common.constant.GoForWealthPRSConstants;
import com.moptra.go4wealth.prs.common.enums.GoForWealthPRSErrorMessageEnum;
import com.moptra.go4wealth.prs.common.exception.GoForWealthPRSException;
import com.moptra.go4wealth.prs.common.util.EncryptUserDetail;
import com.moptra.go4wealth.prs.orderapi.request.ChildOrderDetailRequest;
import com.moptra.go4wealth.prs.orderapi.request.ChildPassword;
import com.moptra.go4wealth.repository.BankCodeRepository;
import com.moptra.go4wealth.repository.OrderItemRepository;
import com.moptra.go4wealth.repository.OrderRepository;
import com.moptra.go4wealth.repository.StoreConfRepository;
import com.moptra.go4wealth.repository.SystemPropertiesRepository;
import com.moptra.go4wealth.repository.UserRepository;
import com.moptra.go4wealth.uma.common.util.OtpGenerator;

@Component
public class PaymentServiceImpl {

	@Autowired 
	SystemPropertiesRepository systemPropertiesRepository;

	@Autowired
	UserRepository userRepository;

	@Autowired
	BankCodeRepository bankCodeRepository;

	@Autowired
	EncryptUserDetail encryptUserDetail;

	@Autowired
	OrderRepository orderRepository;
	
	@Autowired
	StoreConfRepository storeConfRepository;
	
	@Autowired
	OrderItemRepository orderItemRepository;

	private static Logger logger = LoggerFactory.getLogger(PaymentServiceImpl.class);
	String UserId;
	String Password;
	String MemberId;
	String PassKey;

	public void getCredencials(){
		
		List<SystemProperties> systemPropertiesList=systemPropertiesRepository.findAll();
		for (SystemProperties systemProperties : systemPropertiesList) {

			if(systemProperties.getId().getPropertyKey().equals("UserId")){
				this.UserId=systemProperties.getId().getPropertyValue();
			}
			if(systemProperties.getId().getPropertyKey().equals("MemberId")){
				this.MemberId=systemProperties.getId().getPropertyValue();
			}
			if(systemProperties.getId().getPropertyKey().equals("Password")){
				this.Password=systemProperties.getId().getPropertyValue();
			}
		}
		OtpGenerator otpGenerator = new OtpGenerator(5,true);
		this.PassKey = otpGenerator.generateOTP();
		
	}
	
	String getPassword() throws GoForWealthPRSException {
		
		GetPasswordRequest getPasswordRequest= new GetPasswordRequest(UserId,Password,MemberId,PassKey);
		String password=null;
		HttpHeaders httpHeaders = new HttpHeaders();
		httpHeaders.setContentType(MediaType.APPLICATION_JSON);
		StoreConf url = storeConfRepository.findByKeyword(GoForWealthPRSConstants.PAYMENT_PASSWORD_API_URL);
		//String url="http://www.bsestarmf.in/StarMFPaymentGatewayService/StarMFPaymentGatewayService.svc/GetPassword";
		password = getBseResponseForPassword(url.getKeywordValue(), getPasswordRequest, httpHeaders, HttpMethod.POST);
		String res[] = password.split("\\|");
		if(res[0].equals("100")){
			password = res[1];
		}
		return password;
	}

	public String doPayment(int userId, String[] order, String amount,String idList) throws GoForWealthPRSException {
		getCredencials();
		String paymentResponse=null;
		
		
		//String RegnNo1=order[0];
		User user= userRepository.getOne(userId);
		//Orders order1=orderRepository.getorderByRegId(RegnNo1);
		//Integer orderIds = order1.getOrdersId();
		/*if(order1.getType().equals("SIP"))
		{
			if(order1.getField2().equals("N")){

				OrderItem orderItem = orderItemRepository.getOrderItemByOrderId(order1.getOrdersId());
				SimpleDateFormat dateFormat =new SimpleDateFormat("dd MMM yyyy");
				String date = dateFormat.format(orderItem.getLastcreate());
				String ClientCode1=user.getOnboardingStatus().getClientCode();
				String Date =date;
				String EncryptedPassword1 =""; 
				String MemberCode1 = MemberId;
				String RegnNo = RegnNo1;
				String SystematicPlanType ="XSIP"; 
				String id = getChildOrderDetail(ClientCode1,Date,EncryptedPassword1,MemberCode1,RegnNo,SystematicPlanType);
				if(id.equals("101")){
					return "Please check your mail and authenticating the transactions as online as mentioned in email, kindly ignore if already authenticated via SMS.Your Reg. Id is :"+RegnNo1;
				}else{
					order1.setBseOrderId(id);
					order1.setField2(RegnNo1);
					orderRepository.save(order1);
					order1.setLastupdate(new Date());
					order[0] = id;
					String bankCode=null;
					String paymentMode= null;
					List<BankCodeRequest> bankCodeList= new ArrayList<BankCodeRequest>();
					BankDetails bankDetails = user.getBankDetails();
					String bankName =	bankDetails.getBankName();
					List<BankCodes> bankCodes= bankCodeRepository.findAll();
					for (BankCodes bankCodes2 : bankCodes) {
						if(bankCodes2.getBankName().contains(bankName)){
							BankCodeRequest bankCodeRequest= new BankCodeRequest();
							bankCodeRequest.setBankCode(bankCodes2.getBankCode());
							bankCodeRequest.setBankName(bankCodes2.getBankName());
							bankCodeRequest.setMode(bankCodes2.getBankMode());
							bankCodeList.add(bankCodeRequest);
						}
					}
					if(bankCodeList.size()==1){
						for (BankCodeRequest bankCodes2 : bankCodeList) {
							bankCode=bankCodes2.getBankCode();
							paymentMode=bankCodes2.getMode();
						}
					}
					if(bankCodeList.size()>1){
						for (BankCodeRequest bankCodes2 : bankCodeList) {
							if(bankCodes2.getBankName().contains("Retail")){
								bankCode=bankCodes2.getBankCode();
								paymentMode=bankCodes2.getMode();
							}
						}
					}

					String decryptBankAccountNo = "";
					try {
						decryptBankAccountNo = encryptUserDetail.decrypt(user.getBankDetails().getAccountNo());
					} catch(Exception ex) { 
						ex.printStackTrace();
					}
					String AccNo=decryptBankAccountNo; 
					String ClientCode=user.getOnboardingStatus().getClientCode();
					String EncryptedPassword=getPassword();
					String IFSC=user.getBankDetails().getIfsc();
					StoreConf LogOutURL = storeConfRepository.findByKeyword(GoForWealthPRSConstants.PAYMENT_REDIRECT_URL);
					String MemberCode=MemberId; 
					String Orders[]=order;
					String TotalAmount=amount;
					
					System.out.println("Logout Url : " + LogOutURL.getKeywordValue());
					System.out.println("Logout Url : " + LogOutURL.getKeywordValue() + "/simpleSchemePaymentStatus/" + idList);
					PaymentRequest paymentRequest= new PaymentRequest(AccNo,bankCode,ClientCode,EncryptedPassword,IFSC,LogOutURL.getKeywordValue()+"/simpleSchemePaymentStatus/"+orderIds,MemberCode,paymentMode,Orders,TotalAmount);
                     try {
						logger.info("Payment Request:" + GoForWealthAdminUtil.getJsonFromObject(paymentRequest,null));
					} catch (JsonProcessingException e) {
						e.printStackTrace();
					}

					HttpHeaders httpHeaders = new HttpHeaders();
					httpHeaders.setContentType(MediaType.APPLICATION_JSON);
					StoreConf url = storeConfRepository.findByKeyword(GoForWealthPRSConstants.PAYMENT_API_URL);
					paymentResponse = getBseResponse(url.getKeywordValue(), paymentRequest, httpHeaders, HttpMethod.POST);
					
					return paymentResponse;

				}
			}else{

				String bankCode=null;
				String paymentMode= null;
				List<BankCodeRequest> bankCodeList= new ArrayList<BankCodeRequest>();
				BankDetails bankDetails = user.getBankDetails();
				String bankName =	bankDetails.getBankName();
				List<BankCodes> bankCodes= bankCodeRepository.findAll();
				for (BankCodes bankCodes2 : bankCodes) {

					if(bankCodes2.getBankName().contains(bankName)){
						BankCodeRequest bankCodeRequest= new BankCodeRequest();
						bankCodeRequest.setBankCode(bankCodes2.getBankCode());
						bankCodeRequest.setBankName(bankCodes2.getBankName());
						bankCodeRequest.setMode(bankCodes2.getBankMode());
						bankCodeList.add(bankCodeRequest);
					}
				}
				if(bankCodeList.size()==1){
					for (BankCodeRequest bankCodes2 : bankCodeList) {
						bankCode=bankCodes2.getBankCode();
						paymentMode=bankCodes2.getMode();
					}
				}
				if(bankCodeList.size()>1){
					for (BankCodeRequest bankCodes2 : bankCodeList) {
						if(bankCodes2.getBankName().contains("Retail")){
							bankCode=bankCodes2.getBankCode();
							paymentMode=bankCodes2.getMode();
						}
					}
				}

				String decryptBankAccountNo = "";
				try {
					decryptBankAccountNo = encryptUserDetail.decrypt(user.getBankDetails().getAccountNo());
				} catch(Exception ex) { 
					ex.printStackTrace();
				}
				String AccNo=decryptBankAccountNo; 
				String ClientCode=user.getOnboardingStatus().getClientCode();
				String EncryptedPassword=getPassword();
				String IFSC=user.getBankDetails().getIfsc();
				StoreConf LogOutURL = storeConfRepository.findByKeyword(GoForWealthPRSConstants.PAYMENT_REDIRECT_URL);
				String MemberCode=MemberId; 
				String Orders[]=order;
				String TotalAmount=amount;
				
				System.out.println("Logout Url : " + LogOutURL.getKeywordValue());
				System.out.println("Logout Url : " + LogOutURL.getKeywordValue() + "/simpleSchemePaymentStatus/" + orderIds);
				PaymentRequest paymentRequest= new PaymentRequest(AccNo,bankCode,ClientCode,EncryptedPassword,IFSC,LogOutURL.getKeywordValue()+"/simpleSchemePaymentStatus/"+orderIds,MemberCode,paymentMode,Orders,TotalAmount);

				try {
					logger.info("Payment Request:" + GoForWealthAdminUtil.getJsonFromObject(paymentRequest,null));
				} catch (JsonProcessingException e) {
					e.printStackTrace();
				}

				HttpHeaders httpHeaders = new HttpHeaders();
				httpHeaders.setContentType(MediaType.APPLICATION_JSON);
				StoreConf url = storeConfRepository.findByKeyword(GoForWealthPRSConstants.PAYMENT_API_URL);
				paymentResponse = getBseResponse(url.getKeywordValue(), paymentRequest, httpHeaders, HttpMethod.POST);
				System.out.println(paymentResponse);
				return paymentResponse;
			}
		}*/
		//Lumpsum
		//else{
			String bankCode=null;
			String paymentMode= null;
			List<BankCodeRequest> bankCodeList= new ArrayList<BankCodeRequest>();
			BankDetails bankDetails = user.getBankDetails();
			String bankName =	bankDetails.getBankName();
			List<BankCodes> bankCodes= bankCodeRepository.findAll();
			for (BankCodes bankCodes2 : bankCodes) {

				if(bankCodes2.getBankName().contains(bankName)){
					BankCodeRequest bankCodeRequest= new BankCodeRequest();
					bankCodeRequest.setBankCode(bankCodes2.getBankCode());
					bankCodeRequest.setBankName(bankCodes2.getBankName());
					bankCodeRequest.setMode(bankCodes2.getBankMode());
					bankCodeList.add(bankCodeRequest);
				}
			}
			if(bankCodeList.size()==1){
				for (BankCodeRequest bankCodes2 : bankCodeList) {
					bankCode=bankCodes2.getBankCode();
					paymentMode=bankCodes2.getMode();
				}
			}
			if(bankCodeList.size()>1){
				for (BankCodeRequest bankCodes2 : bankCodeList) {
					if(bankCodes2.getBankName().contains("Retail")){
						bankCode=bankCodes2.getBankCode();
						paymentMode=bankCodes2.getMode();
					}
				}
			}

			String decryptBankAccountNo = "";
			try {
				decryptBankAccountNo = encryptUserDetail.decrypt(user.getBankDetails().getAccountNo());
			} catch(Exception ex) { 
				ex.printStackTrace();
			}
			String AccNo=decryptBankAccountNo; 
			String ClientCode=user.getOnboardingStatus().getClientCode();
			String EncryptedPassword=getPassword();
			String IFSC=user.getBankDetails().getIfsc();
			StoreConf LogOutURL = storeConfRepository.findByKeyword(GoForWealthPRSConstants.PAYMENT_REDIRECT_URL);
			String MemberCode=MemberId; 
			String Orders[]=order;
			String TotalAmount=amount;
			
			System.out.println("Logout Url : " + LogOutURL.getKeywordValue());
			System.out.println("Logout Url : " + LogOutURL.getKeywordValue() + "/simpleSchemePaymentStatus/" + idList);
			PaymentRequest paymentRequest= new PaymentRequest(AccNo,bankCode,ClientCode,EncryptedPassword,IFSC,LogOutURL.getKeywordValue()+"/simpleSchemePaymentStatus/"+idList,MemberCode,paymentMode,Orders,TotalAmount);
			
			try {
				logger.info("Payment Request:" + GoForWealthAdminUtil.getJsonFromObject(paymentRequest,null));
			} catch (JsonProcessingException e) {
				e.printStackTrace();
			}

			HttpHeaders httpHeaders = new HttpHeaders();
			httpHeaders.setContentType(MediaType.APPLICATION_JSON);
			StoreConf url = storeConfRepository.findByKeyword(GoForWealthPRSConstants.PAYMENT_API_URL);
			paymentResponse = getBseResponse(url.getKeywordValue(), paymentRequest, httpHeaders, HttpMethod.POST);
			System.out.println(paymentResponse);
			return paymentResponse;
		//}



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
				logger.info(jsonObject.getString("ResponseString"));
				String checkStatus = jsonObject.getString("Status");
				response=jsonObject.getString("Status")+"|"+jsonObject.getString("ResponseString");
				if(checkStatus.equals("101")){
					return response;
				}

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
				logger.info("sdfsdf"+jsonObject.getString("ResponseString"));
				response=jsonObject.getString("Status")+"|"+jsonObject.getString("ResponseString");

			}	
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

	public String getPasswordForChildOrder() throws GoForWealthPRSException {
	
    	String RequestType="XSIP";

		ChildPassword getPasswordRequest = new ChildPassword(MemberId,PassKey, Password,RequestType,UserId);

		String password=null;
		HttpHeaders httpHeaders = new HttpHeaders();
		httpHeaders.setContentType(MediaType.APPLICATION_JSON);
		StoreConf url = storeConfRepository.findByKeyword(GoForWealthPRSConstants.CHILD_ORDER_PASSWORD_URL);
		password = getBseResponseForPassword1(url.getKeywordValue(), getPasswordRequest, httpHeaders, HttpMethod.POST);
		String res[] = password.split("\\|");
		if(res[0].equals("100")){
			password = res[1];
		}
		return password;
	}



	public static String getBseResponseForPassword1(String url, Object jsonResponse, HttpHeaders headersToSend,
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
				response=jsonObject.getString("Status")+"|"+jsonObject.getString("ResponseString");

			}	
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

	public String getChildOrderDetail(String ClientCode, String Date, String EncryptedPassword, String MemberCode,
			String RegnNo, String SystematicPlanType) throws GoForWealthPRSException {
		String encPassword=getPasswordForChildOrder();
		ChildOrderDetailRequest childOrderDetailRequest= new ChildOrderDetailRequest(ClientCode, Date, encPassword,
				MemberCode, RegnNo, SystematicPlanType);
		try {
			logger.info("ChildOrderDetail Request:" + GoForWealthAdminUtil.getJsonFromObject(childOrderDetailRequest,null));
		} catch (JsonProcessingException e) {
			e.printStackTrace();
		}
		String ChildOrderId=null;
		HttpHeaders httpHeaders = new HttpHeaders();
		httpHeaders.setContentType(MediaType.APPLICATION_JSON);
		StoreConf url = storeConfRepository.findByKeyword(GoForWealthPRSConstants.CHILD_ORDER_API_URL);
		ChildOrderId = getBseResponse1(url.getKeywordValue(), childOrderDetailRequest, httpHeaders, HttpMethod.POST);
		System.out.println(ChildOrderId);
		return ChildOrderId;

	}

	public static String getBseResponse1(String url, Object jsonResponse, HttpHeaders headersToSend,
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
	
	
	
	public String doBulkOrderPayment(int userId, String[] order, String amount) throws GoForWealthPRSException {
		getCredencials();
		String paymentResponse=null;
		String RegnNo1=order[0];
		User user= userRepository.getOne(userId);

		Orders order1=orderRepository.getorderByRegId(RegnNo1);
		Integer mpBundleId = order1.getmPBundleId();

		if(order1.getType().equals("SIP"))
		{
				String bankCode=null;
				String paymentMode= null;
				List<BankCodeRequest> bankCodeList= new ArrayList<BankCodeRequest>();
				BankDetails bankDetails = user.getBankDetails();
				String bankName =	bankDetails.getBankName();
				List<BankCodes> bankCodes= bankCodeRepository.findAll();
				for (BankCodes bankCodes2 : bankCodes) {

					if(bankCodes2.getBankName().contains(bankName)){
						BankCodeRequest bankCodeRequest= new BankCodeRequest();
						bankCodeRequest.setBankCode(bankCodes2.getBankCode());
						bankCodeRequest.setBankName(bankCodes2.getBankName());
						bankCodeRequest.setMode(bankCodes2.getBankMode());
						bankCodeList.add(bankCodeRequest);
					}
				}
				if(bankCodeList.size()==1){
					for (BankCodeRequest bankCodes2 : bankCodeList) {
						bankCode=bankCodes2.getBankCode();
						paymentMode=bankCodes2.getMode();
					}
				}
				if(bankCodeList.size()>1){
					for (BankCodeRequest bankCodes2 : bankCodeList) {
						if(bankCodes2.getBankName().contains("Retail")){
							bankCode=bankCodes2.getBankCode();
							paymentMode=bankCodes2.getMode();
						}
					}
				}

				String decryptBankAccountNo = "";
				try {
					decryptBankAccountNo = encryptUserDetail.decrypt(user.getBankDetails().getAccountNo());
				} catch(Exception ex) { 
					ex.printStackTrace();
				}
				String AccNo=decryptBankAccountNo; 
				String ClientCode=user.getOnboardingStatus().getClientCode();
				String EncryptedPassword=getPassword();
				String IFSC=user.getBankDetails().getIfsc();
				StoreConf LogOutURL = storeConfRepository.findByKeyword(GoForWealthPRSConstants.PAYMENT_REDIRECT_URL);
				String MemberCode=MemberId; 
				String Orders[]=order;
				String TotalAmount=amount;
				
				/** Modified Code **/
				System.out.println("Logout Url : " + LogOutURL.getKeywordValue());
				System.out.println("Logout Url : " + LogOutURL.getKeywordValue() + "/modelportfolioPaymentStatus/" + mpBundleId);
				PaymentRequest paymentRequest= new PaymentRequest(AccNo,bankCode,ClientCode,EncryptedPassword,IFSC,LogOutURL.getKeywordValue() + "/modelportfolioPaymentStatus/" + mpBundleId,MemberCode,paymentMode,Orders,TotalAmount);

				//PaymentRequest paymentRequest= new PaymentRequest(AccNo,bankCode,ClientCode,EncryptedPassword,IFSC,LogOutURL.getKeywordValue(),MemberCode,paymentMode,Orders,TotalAmount);
				try {
					logger.info("Payment Request:" + GoForWealthAdminUtil.getJsonFromObject(paymentRequest,null));
				} catch (JsonProcessingException e) {
					e.printStackTrace();
				}

				HttpHeaders httpHeaders = new HttpHeaders();
				httpHeaders.setContentType(MediaType.APPLICATION_JSON);
				StoreConf url = storeConfRepository.findByKeyword(GoForWealthPRSConstants.PAYMENT_API_URL);
				paymentResponse = getBseResponse(url.getKeywordValue(), paymentRequest, httpHeaders, HttpMethod.POST);
				System.out.println(paymentResponse);
				return paymentResponse;
		}
		//Lumpsum Payment
		else{
			String bankCode=null;
			String paymentMode= null;
			List<BankCodeRequest> bankCodeList= new ArrayList<BankCodeRequest>();
			BankDetails bankDetails = user.getBankDetails();
			String bankName =	bankDetails.getBankName();
			List<BankCodes> bankCodes= bankCodeRepository.findAll();
			for (BankCodes bankCodes2 : bankCodes) {

				if(bankCodes2.getBankName().contains(bankName)){
					BankCodeRequest bankCodeRequest= new BankCodeRequest();
					bankCodeRequest.setBankCode(bankCodes2.getBankCode());
					bankCodeRequest.setBankName(bankCodes2.getBankName());
					bankCodeRequest.setMode(bankCodes2.getBankMode());
					bankCodeList.add(bankCodeRequest);
				}
			}
			if(bankCodeList.size()==1){
				for (BankCodeRequest bankCodes2 : bankCodeList) {
					bankCode=bankCodes2.getBankCode();
					paymentMode=bankCodes2.getMode();
				}
			}
			if(bankCodeList.size()>1){
				for (BankCodeRequest bankCodes2 : bankCodeList) {
					if(bankCodes2.getBankName().contains("Retail")){
						bankCode=bankCodes2.getBankCode();
						paymentMode=bankCodes2.getMode();
					}
				}
			}

			String decryptBankAccountNo = "";
			try {
				decryptBankAccountNo = encryptUserDetail.decrypt(user.getBankDetails().getAccountNo());
			} catch(Exception ex) { 
				ex.printStackTrace();
			}
			String AccNo=decryptBankAccountNo; 
			String ClientCode=user.getOnboardingStatus().getClientCode();
			String EncryptedPassword=getPassword();
			String IFSC=user.getBankDetails().getIfsc();
			StoreConf LogOutURL = storeConfRepository.findByKeyword(GoForWealthPRSConstants.PAYMENT_REDIRECT_URL);
			String MemberCode=MemberId; 
			String Orders[]=order;
			String TotalAmount=amount;
			
			/** Modified Code **/
			System.out.println("Logout Url : " + LogOutURL.getKeywordValue());
			System.out.println("Logout Url : " + LogOutURL.getKeywordValue() + "/modelportfolioPaymentStatus/" + mpBundleId);
			PaymentRequest paymentRequest= new PaymentRequest(AccNo,bankCode,ClientCode,EncryptedPassword,IFSC,LogOutURL.getKeywordValue() + "/modelportfolioPaymentStatus/" + mpBundleId,MemberCode,paymentMode,Orders,TotalAmount);


			//PaymentRequest paymentRequest= new PaymentRequest(AccNo,bankCode,ClientCode,EncryptedPassword,IFSC,LogOutURL.getKeywordValue(),MemberCode,paymentMode,Orders,TotalAmount);
			try {
				logger.info("Payment Request:" + GoForWealthAdminUtil.getJsonFromObject(paymentRequest,null));
			} catch (JsonProcessingException e) {
				e.printStackTrace();
			}

			HttpHeaders httpHeaders = new HttpHeaders();
			httpHeaders.setContentType(MediaType.APPLICATION_JSON);
			StoreConf url = storeConfRepository.findByKeyword(GoForWealthPRSConstants.PAYMENT_API_URL);
			paymentResponse = getBseResponse(url.getKeywordValue(), paymentRequest, httpHeaders, HttpMethod.POST);
			System.out.println(paymentResponse);
			return paymentResponse;
		}



	}

	public String isOrderAuthenticated(Orders order1, Integer userId) throws GoForWealthPRSException {
		getCredencials();
		User user= userRepository.getOne(userId);
		String reponse = null;
		String RegnNo1 = order1.getBseOrderId();
		  if(order1.getField2().equals("N")){
			  OrderItem orderItem = orderItemRepository.getOrderItemByOrderId(order1.getOrdersId());
				SimpleDateFormat dateFormat =new SimpleDateFormat("dd MMM yyyy");
				String date = dateFormat.format(orderItem.getLastcreate());
				String ClientCode1=user.getOnboardingStatus().getClientCode();
				String Date =date;
				String EncryptedPassword1 =""; 
				String MemberCode1 = MemberId;
				String RegnNo = RegnNo1;
				String SystematicPlanType ="XSIP"; 
				String id = getChildOrderDetail(ClientCode1,Date,EncryptedPassword1,MemberCode1,RegnNo,SystematicPlanType);
				if(id.equals("101")){
					reponse = "Please check your mail and authenticating the transactions as online as mentioned in email, kindly ignore if already authenticated via SMS.Your Reg. Id is :"+RegnNo1;
				}else{

					order1.setBseOrderId(id);
					order1.setField2(RegnNo1);
					order1.setLastupdate(new Date());
					orderRepository.save(order1);
					reponse =  "success";

				}
			}else{
				reponse =  "success";
			}
		return reponse;
	}
}
