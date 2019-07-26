package com.moptra.go4wealth;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.List;

import org.apache.poi.util.IOUtils;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.web.multipart.MultipartFile;

import com.moptra.go4wealth.admin.common.exception.GoForWealthAdminException;
import com.moptra.go4wealth.admin.service.GoForWealthAdminService;
import com.moptra.go4wealth.bean.Seo;
import com.moptra.go4wealth.prs.common.exception.GoForWealthPRSException;
import com.moptra.go4wealth.prs.imageupload.ImageUploadService;
import com.moptra.go4wealth.prs.mfuploadapi.model.FatcaRequest;
import com.moptra.go4wealth.prs.mfuploadapi.model.MandateRequest;
import com.moptra.go4wealth.prs.mfuploadapi.model.PaymentStatusRequest;
import com.moptra.go4wealth.prs.mfuploadapi.model.UccRequest;
import com.moptra.go4wealth.prs.mfuploadapi.service.MandateService;
import com.moptra.go4wealth.prs.navapi.NavService;
import com.moptra.go4wealth.prs.orderapi.OrderMfService;
import com.moptra.go4wealth.prs.orderapi.request.GetPasswordRequest;
import com.moptra.go4wealth.prs.orderapi.request.OrderEntryRequest;
import com.moptra.go4wealth.prs.orderapi.request.SipOrderEntryRequest;
import com.moptra.go4wealth.prs.orderapi.request.SpreadOrderEntryRequest;
import com.moptra.go4wealth.prs.orderapi.request.SwitchOrderEntryRequest;
import com.moptra.go4wealth.prs.orderapi.request.XsipOrderEntryRequest;
import com.moptra.go4wealth.prs.orderapi.response.OrderEntryResponse;
import com.moptra.go4wealth.prs.orderapi.response.SwitchOrderEntryResponse;
import com.moptra.go4wealth.prs.payment.PaymentService;
import com.moptra.go4wealth.prs.service.GoForWealthFundSchemeService;
import com.moptra.go4wealth.prs.service.GoForWealthModelportfolioService;
import com.moptra.go4wealth.prs.service.GoForWealthPRSService;
import com.moptra.go4wealth.ticob.common.util.GoForWealthTICOBUtil;
import com.moptra.go4wealth.ticob.model.CamsMailbackUserPortfolioData;
import com.moptra.go4wealth.ticob.model.KarvyMailBackUserPortfolioData;
import com.moptra.go4wealth.uma.service.GoForWealthUMAService;


@RunWith(SpringRunner.class)
@SpringBootTest
public class AppTest {

	@Autowired 
	OrderMfService orderMfService;
	
	@Autowired 
	GoForWealthPRSService goForWealthPRSService;

	
	@Autowired
	GoForWealthUMAService goForWealthUmaService;
	
	@Autowired
	GoForWealthAdminService goForWealthAdminService;
	
	@Autowired
	MandateService mandateService;
	
	@Autowired
	PaymentService paymentService;
	
	@Autowired
	NavService navService;
	
	@Autowired
	ImageUploadService imageUploadCallApi;
	
	@Autowired
	GoForWealthFundSchemeService goForWealthFundSchemeService;
	
	@Autowired
	GoForWealthModelportfolioService goForWealthModelportfolioService;
	
	@Test
	public void testGetPassword() throws GoForWealthPRSException{
		
		//GoForWealthTICOBUtil go =new GoForWealthTICOBUtil();
		
		 //File file = new File("E://Documents/Report_Data/CAMS - WBR 2.xlsx");
		/* File file = new File("E://Documents/Report_Data/KARVY - MFSD201.xlsx");
		 //File file = new File("E://21092018123948_87142393R2.xls");
		    FileInputStream input=null;
			try {
				input = new FileInputStream(file);
			} catch (FileNotFoundException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		    MultipartFile multipartFile=null;
			try {
				
				multipartFile = new MockMultipartFile("file",file.getName(), "text/plain", IOUtils.toByteArray(input));
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			
			List<KarvyMailBackUserPortfolioData> mailbackUserPortfolioDatas=go.readKarvyDataFromExcelAndStoreToDatabase(multipartFile);
			 for (KarvyMailBackUserPortfolioData mailbackUserPortfolioData : mailbackUserPortfolioDatas) {
				System.out.println(mailbackUserPortfolioData.getTransactionFlag());
			}*/
		
			 /*List<CamsMailbackUserPortfolioData> mailbackUserPortfolioDatas=go.readCamsDataFromExcelAndStoreToDatabase(multipartFile);
			 for (CamsMailbackUserPortfolioData mailbackUserPortfolioData : mailbackUserPortfolioDatas) {
				System.out.println(mailbackUserPortfolioData.getTrxnType());
			}*/
		
		
		
		
		//String amfiCode="113544";
	    //String date="30 Nov 2017";
		//NavApiResponseDTO response=navService.getNavData(amfiCode);
		//System.out.println(response.getInformation());
		//System.out.println(response.getNav());
		//goForWealthModelportfolioService.getUserOrderByUserId(91, "SIP");
		/*List<AddToCartDTO> addToCartDTOs = new ArrayList<AddToCartDTO>();
		AddToCartDTO add1= new AddToCartDTO();
		add1.setOrderId(191);
		AddToCartDTO add2= new AddToCartDTO();
		add2.setOrderId(192);
		AddToCartDTO add3= new AddToCartDTO();
		add3.setOrderId(193);
		addToCartDTOs.add(add1);
		addToCartDTOs.add(add2);
		addToCartDTOs.add(add3);*/
		//for (AddToCartDTO addToCartDTO : addToCartDTOs) {
			//String res = goForWealthModelportfolioService.confirmOrder(91, addToCartDTO.getOrderId());
			//System.out.println("Response =====  "+res);
		//}
		
		/*String res=goForWealthFundSchemeService.getAllotementStatement("G4W184", "", "", "", "01/07/2018", "18871",
		"44992029", "ALL", "ALL", "123456!", "ALL","ALL","11/09/2018","1887102");
		System.out.println(res);*/
		
		
		/*String res=goForWealthFundSchemeService.getOrderStatus("G4W184", "", "", "", "01/07/2018", "18871",
				"44992029", "ALL", "ALL", "123456!", "ALL","ALL","11/09/2018","ALL","1887102");
				System.out.println(res);*/
		/*MandateDetailsRequestParam mandateDetailsRequestParam = new MandateDetailsRequestParam();
		mandateDetailsRequestParam.setClientCode("G4W184");
		mandateDetailsRequestParam.setFromDate("01/07/2018");
		mandateDetailsRequestParam.setMandateId("");
		mandateDetailsRequestParam.setMemberCode("18871");
		mandateDetailsRequestParam.setMemberId("18871");
		mandateDetailsRequestParam.setPassKey("123456");
		mandateDetailsRequestParam.setPassword("123456!");
		mandateDetailsRequestParam.setToDate("28/09/2018");
		mandateDetailsRequestParam.setUserId("1887102");
		mandateDetailsRequestParam.setRequestType("MANDATE");*/
		
		
		//try{
		//Object res = orderMfService.getMandateDetails(mandateDetailsRequestParam);
			/*Object res=goForWealthFundSchemeService.getOrderStatus("G4W184", "", "", "", "01/07/2018", "18871",
			"", "ALL", "ALL", "123456!", "ALL","ALL","11/09/2018","ALL","1887102");*/
			
			// goForWealthFundSchemeService.updateOrderStatus(123456, 94, "44996939");
		/*	
		JSONObject jsonObject = new JSONObject(res.toString());
		
		
		String checkStatus = jsonObject.getString("Status");
		if(checkStatus.equals("101")){
			//return checkStatus;
		}
		else{
			JSONArray jSONArray=jsonObject.getJSONArray("OrderDetails");
			int size = jSONArray.length();
			System.out.println(size);
			for (int i =0;i<=size;i++) {
				JSONObject JSONObject5 =new JSONObject(jSONArray.get(i).toString());
				String status = JSONObject5.getString("OrderRemarks");
				System.out.println(status);
			}
			
		}*/
		//}catch(Exception e){
			
		//}
		
/*     String res=goForWealthFundSchemeService.getOrderStatus("18871", "1887102", "123456!", "07/09/2018", "12/09/2018", "G4W94",
		"P", "All", "All", "All", "ALL","","","","");*/
		/*GetPasswordRequest getPasswordRequest= new GetPasswordRequest();
		getPasswordRequest.setMemberId("18871");
		getPasswordRequest.setPassKey("18871");
		getPasswordRequest.setPassword("123456");
		getPasswordRequest.setUserId("1887102");
		orderMfService.getPassword(getPasswordRequest);*/
	//String returnOut=orderMfService.getPassword(getPasswordRequest);
	//String returnOut = 	mandateService.RegisterMandate(new MandateRequest());
	//System.out.println("returnOut ===  "+returnOut);
		/*ObjectFactory objectFactory = new ObjectFactory();
		PasswordRequest passwordRequest= new PasswordRequest();
		
		JAXBElement<String> memberId = objectFactory.createString("10996");
		passwordRequest.setMemberId(memberId);
		
		JAXBElement<String> passKey = objectFactory.createString("1099");
		passwordRequest.setPassKey(passKey);
		
		JAXBElement<String> password = objectFactory.createString("123456");
		passwordRequest.setPassword(password);
		
		JAXBElement<String> userId = objectFactory.createString("1000901");
		passwordRequest.setUserId(userId);*/
		
		/*String pass=paymentService.getPassword();
		System.out.println(pass);*/
		
		
		
		//Gson g = new Gson(); 
		//Player p = g.fromJson(jsonString, Player.class)

	    //navService.getNavPassword();
		//System.out.println(response);
		//imageUploadCallApi.getPassword();
		
		//goForWealthAdminService.getUserAuthority(94);
		
		
		//String res=goForWealthFundSchemeService.getChildOrderId();
	}
	
	@Test
	public void doPayment() throws GoForWealthPRSException{
		com.moptra.go4wealth.prs.mfuploadapi.model.GetPasswordRequest getPasswordRequest = new com.moptra.go4wealth.prs.mfuploadapi.model.GetPasswordRequest();
		getPasswordRequest.setMemberId("18871");
		getPasswordRequest.setPasskey("18871");
		getPasswordRequest.setPassword("123456!");
		getPasswordRequest.setUserId("1887102");
		
		PaymentStatusRequest paymentStatusRequest= new PaymentStatusRequest();
		paymentStatusRequest.setClientCode("G4W184");
		paymentStatusRequest.setFlag("11");
		paymentStatusRequest.setOrderNo("3355926");
		paymentStatusRequest.setPassword(mandateService.getPassword());
		paymentStatusRequest.setSagment("BSEMF");
		paymentStatusRequest.setUserId("1887102");
		paymentStatusRequest.setGetPasswordRequest(getPasswordRequest);
		String response=mandateService.getPaymentStatus(paymentStatusRequest);
		System.out.println("Response====   "+response);
	}
	
	
	@Test
	public void testOrderEntry() throws GoForWealthPRSException{
		OrderEntryRequest orderEntryRequest= new OrderEntryRequest();
		GetPasswordRequest getPasswordRequest= new GetPasswordRequest();
		orderEntryRequest.setTransCode("NEW");
		orderEntryRequest.setUniqueRefNo("7770000");
		orderEntryRequest.setOrderId("");
		orderEntryRequest.setUserID("1000901");
		orderEntryRequest.setMemberId("10996");
		orderEntryRequest.setClientCode("G4W01");
		orderEntryRequest.setSchemeCd("1180B-GR");
		orderEntryRequest.setBuySell("P");
		orderEntryRequest.setBuySellType("FRESH");
		orderEntryRequest.setDpTxn("P");
		orderEntryRequest.setOrderVal("50000");
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
		orderEntryRequest.setPassword(orderMfService.getPassword(getPasswordRequest));
		orderEntryRequest.setPassKey("10996");
		orderEntryRequest.setParma1("");
		orderEntryRequest.setParma2("");
		orderEntryRequest.setParma3("");
	OrderEntryResponse orderEntryResponse=	orderMfService.getOrderEntry(orderEntryRequest);
	
	System.out.println("orderEntryResponse ===  "+orderEntryResponse);
	
	}
	
	@Test
	public void testOrderReedem() throws GoForWealthPRSException{
		GetPasswordRequest getPasswordRequest= new GetPasswordRequest();
		OrderEntryRequest orderEntryRequest= new OrderEntryRequest();
		orderEntryRequest.setTransCode("NEW");
		orderEntryRequest.setUniqueRefNo("727220");
		orderEntryRequest.setOrderId("1181510");
		orderEntryRequest.setUserID("1000901");
		orderEntryRequest.setMemberId("10996");
		orderEntryRequest.setClientCode("G4W01");
		orderEntryRequest.setSchemeCd("1180B-GR");
		orderEntryRequest.setBuySell("R");
		orderEntryRequest.setBuySellType("FRESH");
		orderEntryRequest.setDpTxn("P");
		orderEntryRequest.setOrderVal("");
		orderEntryRequest.setQty("");
		orderEntryRequest.setAllRedeem("Y");
		orderEntryRequest.setFolioNo("12345685");
		orderEntryRequest.setRemarks("");
		orderEntryRequest.setKycStatus("Y");
		orderEntryRequest.setRefNo("");
		orderEntryRequest.setSubBrCode("");
		orderEntryRequest.setEuin("");
		orderEntryRequest.setEuinVal("N");
		orderEntryRequest.setMinRedeem("Y");
		orderEntryRequest.setDpc("N");
		orderEntryRequest.setIpAdd("");
		orderEntryRequest.setPassword(orderMfService.getPassword(getPasswordRequest));
		orderEntryRequest.setPassKey("10996");
		orderEntryRequest.setParma1("");
		orderEntryRequest.setParma2("");
		orderEntryRequest.setParma3("");
	OrderEntryResponse orderEntryResponse=	orderMfService.getOrderEntry(orderEntryRequest);
	
	System.out.println("orderEntryResponse ===  "+orderEntryResponse);
	
		
	}
	
	
	@Test
	public void testSipOrderEntry(){
		SipOrderEntryRequest sipOrderEntryRequest= new SipOrderEntryRequest();
		sipOrderEntryRequest.setTransactionCode("NEW");
		sipOrderEntryRequest.setUniqueRefNo("71");
		//sipOrderEntryRequest.setOrderId("0011");
		sipOrderEntryRequest.setUserID("10996");
		sipOrderEntryRequest.setMemberCode("10996");
		sipOrderEntryRequest.setClientCode("a001");
		//sipOrderEntryRequest.schemeCode("02-DP");
		//sipOrderEntryRequest.setBuySell("P");
		//sipOrderEntryRequest.setBuySellType("FRESH");
		//sipOrderEntryRequest.setDpTxn("P");
		//sipOrderEntryRequest.setOrderVal("50000");
		//sipOrderEntryRequest.setQty("1");
		//sipOrderEntryRequest.setAllRedeem("N");
		//sipOrderEntryRequest.setFolioNo("2358641");
		//sipOrderEntryRequest.setRemarks("1111");
		//sipOrderEntryRequest.setKycStatus("Y");
		//sipOrderEntryRequest.setRefNo("");
		//sipOrderEntryRequest.setSubBrCode("");
		sipOrderEntryRequest.setEuin("0");
		sipOrderEntryRequest.setEuinVal("Y");
		//sipOrderEntryRequest.setMinRedeem("N");
		sipOrderEntryRequest.setDpc("Y");
		//orderEntryRequest.setIpAdd("");
		//sipOrderEntryRequest.setPassword(orderMfService.getPassword());
		sipOrderEntryRequest.setPassKey("212566");
		//orderEntryRequest.setParma1("");
		//orderEntryRequest.setParma2("");
		//orderEntryRequest.setParma3("");

		orderMfService.getSipOrderEntry(sipOrderEntryRequest);
	}
	
	@Test
	public void testSpreadOrderEntry(){
		SpreadOrderEntryRequest spreadOrderEntryRequest= new SpreadOrderEntryRequest();
		orderMfService.getSpreadOrderEntry(spreadOrderEntryRequest);
	}
	
	@Test
	public void testSwitchOrderEntryParam(){
		
		SwitchOrderEntryRequest switchOrderEntryRequest= new SwitchOrderEntryRequest();
		GetPasswordRequest getPasswordRequest= new GetPasswordRequest();
		switchOrderEntryRequest.setTransCode("NEW");
		switchOrderEntryRequest.setTransNo("32584548");
		switchOrderEntryRequest.setOrderId("1181447");//order id will be changed after switching
		switchOrderEntryRequest.setUserID("1000901");
		switchOrderEntryRequest.setMemberId("10996");
		switchOrderEntryRequest.setClientCode("G4W01");
		switchOrderEntryRequest.setFromSchemeCd("1180C-DR");
		switchOrderEntryRequest.setToSchemeCd("1180B-GR");
		switchOrderEntryRequest.setBuySell("SO");//Switchout/switchin SO/SI
		switchOrderEntryRequest.setBuySellType("FRESH");
		switchOrderEntryRequest.setDpTxn("P");
		switchOrderEntryRequest.setSwitchAmount("");
		switchOrderEntryRequest.setSwitchUnits("");
		switchOrderEntryRequest.setAllUnitsFlag("Y");
		switchOrderEntryRequest.setFolioNo("4574684");
		switchOrderEntryRequest.setRemarks("");
		switchOrderEntryRequest.setKycStatus("Y");
		switchOrderEntryRequest.setSubBrCode("");
		switchOrderEntryRequest.setEuin("");
		switchOrderEntryRequest.setEuinVal("N");
		switchOrderEntryRequest.setMinRedeem("Y");
		switchOrderEntryRequest.setIpAdd("");
		switchOrderEntryRequest.setPassword(orderMfService.getPassword(getPasswordRequest));
		switchOrderEntryRequest.setPassKey("10996");
		switchOrderEntryRequest.setParma1("");
		switchOrderEntryRequest.setParma2("");
		switchOrderEntryRequest.setParma3("");
		
		SwitchOrderEntryResponse response=orderMfService.getSwitchOrderEntry(switchOrderEntryRequest);
		System.out.println(response);
	}
	
	@Test
	public void testXsipOrderEntry(){
		
		XsipOrderEntryRequest xsipOrderEntryRequest= new XsipOrderEntryRequest();
		
		xsipOrderEntryRequest.setTransactionCode("NEW");
		xsipOrderEntryRequest.setUniqueRefNo("44444456");
		xsipOrderEntryRequest.setSchemeCode("1180B-GR");
		xsipOrderEntryRequest.setMemberCode("10996");
		xsipOrderEntryRequest.setClientCode("G4W01");
		xsipOrderEntryRequest.setUserID("1000901");
		xsipOrderEntryRequest.setInternalRefNo("");//non mandatory
		xsipOrderEntryRequest.setTransMode("D");//demat or physical
		xsipOrderEntryRequest.setDpTxnMode("P");//CDSL/NSDL/PHYSICAL
		xsipOrderEntryRequest.setStartDate("");//start date of the SIP
		xsipOrderEntryRequest.setFrequencyType("MONTHLY");//MONTHLY/QUARTELY/WEEKLY
		xsipOrderEntryRequest.setFrequencyAllowed("1");//1
		//xsipOrderEntryRequest.setInstallmentAmount(1000d);
		xsipOrderEntryRequest.setNoOfInstallment("");
		xsipOrderEntryRequest.setRemarks("");
		xsipOrderEntryRequest.setFolioNo("");//mandatory incaseof physical SIP
		xsipOrderEntryRequest.setFirstOrderFlag("Y");//Y/N
		xsipOrderEntryRequest.setBrokerage("");
		xsipOrderEntryRequest.setMandateID("");
		xsipOrderEntryRequest.setSubberCode("");
		xsipOrderEntryRequest.setEuin("");
		xsipOrderEntryRequest.setEuinVal("");
		xsipOrderEntryRequest.setDpc("");
		xsipOrderEntryRequest.setXsipRegID("");
		xsipOrderEntryRequest.setIpAdd("");
		//xsipOrderEntryRequest.setPassword(orderMfService.getPassword());
		xsipOrderEntryRequest.setPassKey("10996");
		xsipOrderEntryRequest.setParma1("");
		xsipOrderEntryRequest.setParma2("");
		xsipOrderEntryRequest.setParma3("");
		
		orderMfService.getXsipOrderEntry(xsipOrderEntryRequest);
	}
	
	/*@Test 
	public void addUserOrders(){
		AddToCartRequestDTO addToCartDTO= new AddToCartRequestDTO();
		addToCartDTO.setAmount(new BigDecimal("23000"));
		addToCartDTO.setSchemeId(2);
		int userId=23;
		goForWealthPRSService.addUserOrders(addToCartDTO, userId);
	}
	
	@Test
	public void getUserOrderByUserId(){
		
		goForWealthPRSService.getOrderByUserId(23, "NEW");
	}
	*/
	@Test
	public void getUserOrderItemsByOrderId(){
		
		goForWealthPRSService.getOrderItemListByOrderId(7, "NEW");
	}
	
	@Test
	public void getScoInfo(){
		
	Seo seo=goForWealthAdminService.getSeoInfo("home");
	System.out.println("Sco Page Name====="+seo.getMetaDescription());
	}
	
	@Test
	public void getAllBlogs(){
		
	//List<Blog> sco=goForWealthAdminService.getAllBlogs();
	
	}
	@Test
	public void getAllSchemes() throws GoForWealthAdminException{
		
	//List<SchemeDTO> list=goForWealthAdminService.getAllSchemes();
	
	}
	
	
	@Test
	public void testUccRegistration() {
		UccRequest uccRequest = new UccRequest();
		uccRequest.setUserId("1000901");
		uccRequest.setFlag("02");
		
		uccRequest.setClientCode("G4W01");
		uccRequest.setClientHolding("SI");
		uccRequest.setClientTaxStatus("01");
		uccRequest.setClientOccupationCode("03");
		uccRequest.setClientAppName1("Saurabh Kaiyar");
		uccRequest.setClientAppName2("");
		uccRequest.setClientAppName3("");
		uccRequest.setClientDob("20/05/1986");
		uccRequest.setClientGender("M");
		uccRequest.setClientFather("");
		uccRequest.setClientPan("CBSPK8180N");
		uccRequest.setClientNominee("");
		uccRequest.setClientNomineeRelation("");
		uccRequest.setClientGuardianPan("");
		uccRequest.setClientType("P");
		uccRequest.setClientDefaultDp("");
		uccRequest.setClientCDSLDPId("");
		uccRequest.setClientCDSLCLTId("");
		uccRequest.setClientNSDLDPId("");
		uccRequest.setClientNSDLCLTId("");
		
		uccRequest.setClientAccType1("SB");
		uccRequest.setClientAccNo1("09211050004644");
		uccRequest.setClientMICRNo1("");
		uccRequest.setClientIFSCCode1("HDFC0000921");
		uccRequest.setDefaultBankFlag1("Y");
		
		uccRequest.setClientAccType2("");
		uccRequest.setClientAccNo2("");
		uccRequest.setClientMICRNo2("");
		uccRequest.setClientIFSCCode2("");
		uccRequest.setDefaultBankFlag2("");
		
		uccRequest.setClientAccType3("");
		uccRequest.setClientAccNo3("");
		uccRequest.setClientMICRNo3("");
		uccRequest.setClientIFSCCode3("");
		uccRequest.setDefaultBankFlag3("");
		
		uccRequest.setClientAccType4("");
		uccRequest.setClientAccNo4("");
		uccRequest.setClientMICRNo4("");
		uccRequest.setClientIFSCCode4("");
		uccRequest.setDefaultBankFlag4("");
		
		uccRequest.setClientAccType5("");
		uccRequest.setClientAccNo5("");
		uccRequest.setClientMICRNo5("");
		uccRequest.setClientIFSCCode5("");
		uccRequest.setDefaultBankFlag5("");
		uccRequest.setClientChequeName5("");
	
		/*uccRequest.setClientAccType("SB");
		uccRequest.setClientAccNo("31041869121");
		uccRequest.setClientMICRNo("804042507");
		uccRequest.setClientIFSCCode("SBIN0012603");
		uccRequest.setClientChequeName("");*/
		uccRequest.setClientAdd1("Noida sector 63");
		uccRequest.setClientAdd2("G block");
		uccRequest.setClientAdd3("");
		uccRequest.setClientCity("Noida");
		uccRequest.setClientState("UP");
		uccRequest.setClientPincode("201301");
		uccRequest.setClientCountry("India");
		
		uccRequest.setClientEmail("saurabhkatiyar1986@gmail.com");
		uccRequest.setClientCommMode("P");
		uccRequest.setClientDivPayMode("02");
		
		uccRequest.setClientResiPhone("");
		uccRequest.setClientResiFax("");
		uccRequest.setClientOfficePhone("");
		uccRequest.setClientOfficeFax("");
		uccRequest.setClientPan2("");
		uccRequest.setClientPan3("");
		uccRequest.setMapinNo("");
		uccRequest.setCM_FORADD1("");
		uccRequest.setCM_FORADD2("");
		uccRequest.setCM_FORADD3("");
		uccRequest.setCM_FORCITY("");
		uccRequest.setCM_FORPINCODE("");
		uccRequest.setCM_FORSTATE("");
		uccRequest.setCM_FORCOUNTRY("");
		uccRequest.setCM_FORRESIPHONE("");
		uccRequest.setCM_FORRESIFAX("");
		uccRequest.setCM_FOROFFPHONE("");
		uccRequest.setCM_FOROFFFAX("");
		uccRequest.setCM_MOBILE("8376986970");
		
		mandateService.uccRegistration(uccRequest);
	}
	
	@Test
	public void createMandateParam() {
		MandateRequest mandateRequest= new MandateRequest();
		mandateRequest.setClientCode("G4W01");
		mandateRequest.setAmount("50000");
		mandateRequest.setMandateType("E");
		mandateRequest.setAccountNumber("09211050004644");
		mandateRequest.setAccountType("SB");
		mandateRequest.setIfscCode("HDFC0000921");
		mandateRequest.setMicrCode("");
		mandateRequest.setStartDate("15/09/2018");
		mandateRequest.setEndDate("15/09/2117");
		mandateRequest.setUserId("1000901");
		mandateRequest.setFlag("06");
		mandateService.registerMandate(mandateRequest);
	
	}
	
	@Test
	public void doFatca(){
		FatcaRequest fatcaRequest= new FatcaRequest();
		fatcaRequest.setPanRp("CBSPK8180N");//if pan no. is provided no need to fill blow 4 fields other wise mendatory
		fatcaRequest.setPekrn("");
		fatcaRequest.setInvName("Saurabh");
		fatcaRequest.setDob("05/20/1986");
		fatcaRequest.setFrName("");
		fatcaRequest.setSpName("");
		fatcaRequest.setTaxStatus("01");
		fatcaRequest.setDataSrc("P");
		fatcaRequest.setAddrType("1");
		fatcaRequest.setPoBirInc("IN");//Place Of Birth IN==INDIA
		fatcaRequest.setCoBirInc("IN");
		fatcaRequest.setTaxRes1("IN");
		fatcaRequest.setTpin1("CBSPK8180N");//
		fatcaRequest.setId1Type("C");//<option value="C">PAN Card</option>
		fatcaRequest.setTaxRes2("");
		fatcaRequest.setTpin2("");
		fatcaRequest.setId2Type("");
		fatcaRequest.setTaxRes3("");
		fatcaRequest.setTpin3("");
		fatcaRequest.setId3Type("");
		fatcaRequest.setTaxRes4("");
		fatcaRequest.setTpin4("");
		fatcaRequest.setId4Type("");
		fatcaRequest.setSrceWealt("01");//<option value="01">Salary</option>
		fatcaRequest.setCorpServs("");//M for Non-Individuals
		fatcaRequest.setIncSlab("32");//<option value="32">&gt; 1 &lt;=5 Lacs</option>
		fatcaRequest.setNetWorth("");//M for Non-Individuals
		fatcaRequest.setNwDate("");//M for Non-Individuals
		fatcaRequest.setPepFlag("N");
		fatcaRequest.setOccCode("41");//<option value="41">Private Sector Service</option>
		fatcaRequest.setOccType("O");//<option value="O">Others</option>
		fatcaRequest.setExempCode("");//M for Non-Individuals
		fatcaRequest.setFfiDrnfe("");//M for Non-Individuals
		fatcaRequest.setGiinNo("");//M for Non-Individuals
		fatcaRequest.setSprEntity("");
		fatcaRequest.setGiinNa("");
		fatcaRequest.setGiinExemc("");
		fatcaRequest.setNffeCatg("");
		fatcaRequest.setActNfeSc("");
		fatcaRequest.setNatureBus("");
		fatcaRequest.setRelListed("");
		fatcaRequest.setExchName("B");
		fatcaRequest.setUboAppl("N");
		fatcaRequest.setUboCount("");
		fatcaRequest.setUboName("");
		fatcaRequest.setUboPan("");
		fatcaRequest.setUboNation("");
		fatcaRequest.setUboAdd1("");
		fatcaRequest.setUboAdd2("");
		fatcaRequest.setUboAdd3("");
		fatcaRequest.setUboCity("");
		fatcaRequest.setUboPin("");
		fatcaRequest.setUboState("");
		fatcaRequest.setUboCntry("");
		fatcaRequest.setUboAddTy("");
		fatcaRequest.setUboCtr("");
		fatcaRequest.setUboTin("");
		fatcaRequest.setUboIdTy("");
		fatcaRequest.setUboCob("");
		fatcaRequest.setUboDob("");
		fatcaRequest.setUboGender("");
		fatcaRequest.setUboFrNam("");
		fatcaRequest.setUboOcc("");
		fatcaRequest.setUboOccTy("");
		fatcaRequest.setUboTel("");
		fatcaRequest.setUboMobile("");
		fatcaRequest.setUboCode("C14");
		fatcaRequest.setUboHolPc("");
		fatcaRequest.setSdfFlag("");
		fatcaRequest.setUboDf("N");
		fatcaRequest.setAadhaarRp("");
		fatcaRequest.setNewChange("N");
		fatcaRequest.setLogName("");
		fatcaRequest.setFiller1("");
		fatcaRequest.setFiller2("");
		mandateService.doFatca(fatcaRequest);
	}
	
}
