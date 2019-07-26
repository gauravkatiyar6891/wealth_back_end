/*package com.moptra.go4wealth;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.math.BigDecimal;
import java.util.Arrays;
import java.util.List;

import org.json.JSONException;
import org.json.JSONObject;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit4.SpringRunner;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.moptra.go4wealth.admin.common.exception.GoForWealthAdminException;
import com.moptra.go4wealth.admin.model.SchemeDTO;
import com.moptra.go4wealth.admin.service.GoForWealthAdminService;
import com.moptra.go4wealth.bean.Seo;
import com.moptra.go4wealth.prs.common.exception.GoForWealthPRSException;
import com.moptra.go4wealth.prs.common.util.GoForWealthPRSUtil;
import com.moptra.go4wealth.prs.imageupload.GetPasswordRequest;
import com.moptra.go4wealth.prs.imageupload.ImageUploadService;
import com.moptra.go4wealth.prs.mfuploadapi.model.FatcaRequest;
import com.moptra.go4wealth.prs.mfuploadapi.model.MandateRequest;
import com.moptra.go4wealth.prs.mfuploadapi.model.UccRequest;
import com.moptra.go4wealth.prs.mfuploadapi.service.MandateService;
import com.moptra.go4wealth.prs.orderapi.OrderMfService;
import com.moptra.go4wealth.prs.orderapi.request.AddToCartRequestDTO;
import com.moptra.go4wealth.prs.orderapi.request.OrderEntryRequest;
import com.moptra.go4wealth.prs.orderapi.request.SipOrderEntryRequest;
import com.moptra.go4wealth.prs.orderapi.request.SpreadOrderEntryRequest;
import com.moptra.go4wealth.prs.orderapi.request.SwitchOrderEntryRequest;
import com.moptra.go4wealth.prs.orderapi.request.XsipOrderEntryRequest;
import com.moptra.go4wealth.prs.orderapi.response.OrderEntryResponse;
import com.moptra.go4wealth.prs.service.GoForWealthPRSService;
import com.moptra.go4wealth.uma.service.GoForWealthUMAService;

@RunWith(SpringRunner.class)
@SpringBootTest
public class AppTest1 {

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
	ImageUploadService imageUploadService;
	
	
	
	@Test
	public void testGet1Password(){
	String returnOut=imageUploadService.getPassword();
	//String returnOut = 	mandateService.RegisterMandate(new MandateRequest());
	//System.out.println("returnOut ===  "+returnOut);
	}
	
	
	@Test
	public void testGetPassword(){
	//String returnOut=imageUploadService.getPassword();
	//String returnOut = 	mandateService.RegisterMandate(new MandateRequest());
	//System.out.println("returnOut ===  "+returnOut);
	}
	
	@Test
	public void testGetFilePassword() throws JSONException, JsonProcessingException{
	
		GetPasswordRequest getPasswordReques= new GetPasswordRequest("1000901","123456","10996");
		getPasswordReques.setMemberId("10996");
		getPasswordReques.setPassword("123456");
		getPasswordReques.setUserId("1000901");
		
		String jsonResponse=GoForWealthPRSUtil.getJsonFromObject(getPasswordReques, null);
		//System.out.println("response==="+jsonResponse);
		
		String password=null;
		JSONObject json = new JSONObject();
		json.put("UserId", "1000901");
		json.put("MemberId", "10996");
		json.put("Password", "123456");
		HttpHeaders httpHeaders = new HttpHeaders();
		httpHeaders.setContentType(MediaType.APPLICATION_JSON);
		String url="http://bsestarmfdemo.bseindia.com/StarMFFileUploadService/StarMFFileUploadService.svc/GetPassword";
		try {
			password=imageUploadService.getPassword(url, jsonResponse, httpHeaders, HttpMethod.POST);
			 System.out.println("[pass======"+password);
		} catch (GoForWealthPRSException e) {
		
			e.printStackTrace();
		}
		
	}
	
	@Test
	public void testUploadFile() throws JSONException, IOException, GoForWealthPRSException{
	
		File file = new File("D://Documents/GoFor_Wealth/AofPdf/10996G4W0114062018.PDF");
		  byte[] bytesArray = new byte[(int) file.length()]; 

		  FileInputStream fis = new FileInputStream(file);
		  fis.read(bytesArray); 
		  fis.close();
		
		byte[] byteArray=null;
		String encodedImage=null;
	    
	        InputStream inputStream = new FileInputStream("D://Documents/GoFor_Wealth/AofPdf/10996G4W0113062018.PDF");


	        String inputStreamToString = inputStream.toString();
	        byteArray = inputStreamToString.getBytes();

	       encodedImage = com.sun.org.apache.xerces.internal.impl.dv.util.Base64.encode(byteArray);
	        //inputStream.close();
	    } catch (FileNotFoundException e) {
	        System.out.println("File Not found"+e);
	    } catch (IOException e) {
	                System.out.println("IO Ex"+e);
	    }
	  
		JSONObject json = new JSONObject();
		json.put("UserId", "1000901");
		json.put("EncryptedPassword","ICu7O3TzVVyCC0+NXTmbYpWxOVxQ1F68V9n8a1evlCPl0GrialMZuA==");
		json.put("Flag", "UCC");
		json.put("MemberCode", "10996");
		json.put("ClientCode", "G4W01");
		json.put("FileName", "10996G4W0114062018.pdf");
		json.put("Document Type", "NRM");
		InputStream inputStream = new FileInputStream("D://Documents/GoFor_Wealth/AofPdf/10996G4W0114062018.PDF");
	    String inputStreamToString = inputStream.toString();
	    //byteArray = ((String) inputStream).getBytes();
	    File file = new File("D://Documents/GoFor_Wealth/AofPdf/10996G4W0114062018.PDF");
	    FileInputStream fis = new FileInputStream(file);
        //System.out.println(file.exists() + "!!");
        //InputStream in = resource.openStream();
        ByteArrayOutputStream bos = new ByteArrayOutputStream();
        byte[] buf = new byte[1024];
        try {
            for (int readNum; (readNum = fis.read(buf)) != -1;) {
                bos.write(buf, 0, readNum); //no doubt here is 0
                //Writes len bytes from the specified byte array starting at offset off to this byte array output stream.
                System.out.println("read " + readNum + " bytes,");
            }
        } catch (IOException ex) {
            //Logger.getLogger(genJpeg.class.getName()).log(Level.SEVERE, null, ex);
        }
        byte[] bytes = bos.toByteArray();
	    
	    
	    
	    encodedImage = com.sun.org.apache.xerces.internal.impl.dv.util.Base64.encode(byteArray);
	    System.out.println("resp========    "+encodedImage);
		System.out.println("Date++++++++     "+inputStreamToString.getBytes()); 
		json.put("PFileBytes",Arrays.toString(bytes));
		json.put("Filler1", "null");
		json.put("Filler2", "null");
		
		HttpHeaders httpHeaders = new HttpHeaders();
		httpHeaders.setContentType(MediaType.APPLICATION_JSON);
		String url="http://bsestarmfdemo.bseindia.com/StarMFFileUploadService/StarMFFileUploadService.svc/UploadFile";
		
		imageUploadService.uploadFile(url, json, httpHeaders, HttpMethod.POST);
		
		//File file= new File("D://Documents/GoFor_Wealth/AofPdf/10996.PDF");
		
		
	}
		
	    
		
	

	@Test
	public void testOrderEntry() throws GoForWealthPRSException{
		OrderEntryRequest orderEntryRequest= new OrderEntryRequest();
		
		orderEntryRequest.setTransCode("NEW");
		orderEntryRequest.setTransNo("777777");
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
		orderEntryRequest.setPassword(orderMfService.getPassword());
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
		sipOrderEntryRequest.setPassword(orderMfService.getPassword());
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
		orderMfService.getSwitchOrderEntry(switchOrderEntryRequest);
	}
	
	@Test
	public void testXsipOrderEntry(){
		
		XsipOrderEntryRequest xsipOrderEntryRequest= new XsipOrderEntryRequest();
		
		xsipOrderEntryRequest.setTransactionCode("NEW");
		xsipOrderEntryRequest.setUniqueRefNo("5252565");
		xsipOrderEntryRequest.setSchemeCode("1180B-GR");
		xsipOrderEntryRequest.setMemberCode("10996");
		xsipOrderEntryRequest.setClientCode("G4W01");
		xsipOrderEntryRequest.setUserID("1000901");
		xsipOrderEntryRequest.setInternalRefNo("");//non mandatory
		xsipOrderEntryRequest.setTransMode("P");//demat or physical
		xsipOrderEntryRequest.setDpTxnMode("P");//CDSL/NSDL/PHYSICAL
		xsipOrderEntryRequest.setStartDate("11/06/2018");//start date of the SIP
		xsipOrderEntryRequest.setFrequencyType("MONTHLY");//MONTHLY/QUARTELY/WEEKLY
		xsipOrderEntryRequest.setFrequencyAllowed("1");//1
		xsipOrderEntryRequest.setInstallmentAmount("50000");
		xsipOrderEntryRequest.setNoOfInstallment("10");
		xsipOrderEntryRequest.setRemarks("");
		xsipOrderEntryRequest.setFolioNo("");//mandatory incaseof physical SIP
		xsipOrderEntryRequest.setFirstOrderFlag("Y");//Y/N
		xsipOrderEntryRequest.setBrokerage("");
		xsipOrderEntryRequest.setMandateID("467393");
		xsipOrderEntryRequest.setSubberCode("");
		xsipOrderEntryRequest.setEuin("E233588");
		xsipOrderEntryRequest.setEuinVal("Y");
		xsipOrderEntryRequest.setDpc("N");
		xsipOrderEntryRequest.setXsipRegID("");
		xsipOrderEntryRequest.setIpAdd("");
		xsipOrderEntryRequest.setPassword(orderMfService.getPassword());
		xsipOrderEntryRequest.setPassKey("10996");
		xsipOrderEntryRequest.setParma1("");
		xsipOrderEntryRequest.setParma2("");
		xsipOrderEntryRequest.setParma3("");
		
		orderMfService.getXsipOrderEntry(xsipOrderEntryRequest);
	}
	
	@Test 
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
		
	List<SchemeDTO> list=goForWealthAdminService.getAllSchemes();
	
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
	
		uccRequest.setClientAccType("SB");
		uccRequest.setClientAccNo("31041869121");
		uccRequest.setClientMICRNo("804042507");
		uccRequest.setClientIFSCCode("SBIN0012603");
		uccRequest.setClientChequeName("");
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
	
	@Test
	public void getAllUsers() throws GoForWealthAdminException{
		goForWealthAdminService.getAllUsers();
	}
}
*/