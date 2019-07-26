package com.moptra.go4wealth.prs.mfuploadapi.BseStarMf;

import java.net.URL;

import javax.xml.namespace.QName;

import org.springframework.stereotype.Component;

import com.moptra.go4wealth.prs.mfuploadapi.com.bseindia.bsestarmfdemo._2016._01.IStarMFWebService;
import com.moptra.go4wealth.prs.mfuploadapi.model.FatcaRequest;
import com.moptra.go4wealth.prs.mfuploadapi.model.GetPasswordRequest;
import com.moptra.go4wealth.prs.mfuploadapi.model.MandateRequest;
import com.moptra.go4wealth.prs.mfuploadapi.model.PaymentStatusRequest;
import com.moptra.go4wealth.prs.mfuploadapi.model.UccRequest;
import com.moptra.go4wealth.prs.mfuploadapi.org.tempuri.StarMFWebService;
import com.moptra.go4wealth.prs.model.ChangePassword;

@Component
public class ImpUploadServiceClient {

	private static final QName SERVICE_NAME = new QName("http://tempuri.org/", "StarMFWebService");
	
	public  static IStarMFWebService getPort(){
		
		URL wsdlURL = StarMFWebService.WSDL_LOCATION;
		StarMFWebService ss = new StarMFWebService(wsdlURL, SERVICE_NAME);
        IStarMFWebService port = ss.getWSHttpBindingIStarMFWebService1(); 
			return port;  
	}
	
	public String getPassword(){
		IStarMFWebService port=	getPort();
		return port.getPassword("1887102", "18871", "123456", "18871");
	}
	public String getPassword(GetPasswordRequest getPasswordRequest){
		IStarMFWebService port=	getPort();
		System.out.println("password request: getPassword("+getPasswordRequest.getUserId()+", "+getPasswordRequest.getMemberId()+", "+getPasswordRequest.getPassword()+", "+getPasswordRequest.getPasskey());
		return port.getPassword(getPasswordRequest.getUserId(), getPasswordRequest.getMemberId(), getPasswordRequest.getPassword(), getPasswordRequest.getPasskey());
	}
	
	
	public String registerMandate(MandateRequest mandateRequest){		
		IStarMFWebService port=	getPort();
		String param = createMandateParam(mandateRequest);
		System.out.println("Mandate Register Request: mfapi("+mandateRequest.getFlag()+", "+mandateRequest.getUserId()+", "+mandateRequest.getEncryptedPassword()+", "+param);
		String response = port.mfapi(mandateRequest.getFlag(), mandateRequest.getUserId(), mandateRequest.getEncryptedPassword(), param);
		System.out.println("Mandate Register Response: "+response);
		return response;
	}

	private String createMandateParam(MandateRequest mandateRequest) {
		String param = mandateRequest.getClientCode()+"|"+mandateRequest.getAmount()+"|"+mandateRequest.getMandateType()+"|"+mandateRequest.getAccountNumber()+"|"+mandateRequest.getAccountType()+"|"+mandateRequest.getIfscCode()+"|"+mandateRequest.getMicrCode()+"|"+mandateRequest.getStartDate()+"|"+mandateRequest.getEndDate();
		return param;
		
	}

	public static void main(String ...sdf){
		ImpUploadServiceClient impUploadServiceClient= new ImpUploadServiceClient();
		//getPassword();
	}

	public String uccRegistration(UccRequest uccRequest) {
		IStarMFWebService port=	getPort();
		String param = createUccParam(uccRequest);
		System.out.println("UCC Registration Request: mfapi("+uccRequest.getFlag()+", "+uccRequest.getUserId()+", "+uccRequest.getEncryptedPassword()+", "+param+")");
		String response = port.mfapi(uccRequest.getFlag(), uccRequest.getUserId(), uccRequest.getEncryptedPassword(), param);
		System.out.println("UCC Registration Response: "+response);
		return response;
	}

	private String createUccParam(UccRequest uccRequest) {
		String param = uccRequest.getClientCode()+"|"+uccRequest.getClientHolding()+"|"+uccRequest.getClientTaxStatus()+"|"+uccRequest.getClientOccupationCode()+
				"|"+uccRequest.getClientAppName1()+"|"+uccRequest.getClientAppName2()+"|"+uccRequest.getClientAppName3()+"|"+uccRequest.getClientDob()+
				"|"+uccRequest.getClientGender()+"|"+uccRequest.getClientFather()+"|"+uccRequest.getClientPan()+"|"+uccRequest.getClientNominee()+
				"|"+uccRequest.getClientNomineeRelation()+"|"+uccRequest.getClientGuardianPan()+"|"+uccRequest.getClientType()+"|"+uccRequest.getClientDefaultDp()+
				"|"+uccRequest.getClientCDSLDPId()+"|"+uccRequest.getClientCDSLCLTId()+"|"+uccRequest.getClientNSDLDPId()+"|"+uccRequest.getClientNSDLCLTId()+
				"|"+uccRequest.getClientAccType1()+"|"+ uccRequest.getClientAccNo1()+"|"+uccRequest.getClientMICRNo1()+"|"+uccRequest.getClientIFSCCode1()+
				"|"+uccRequest.getDefaultBankFlag1()+"|"+uccRequest.getClientAccType2()+"|"+ uccRequest.getClientAccNo2()+"|"+uccRequest.getClientMICRNo2()+
				"|"+uccRequest.getClientIFSCCode2()+"|"+uccRequest.getDefaultBankFlag2()+"|"+uccRequest.getClientAccType3()+"|"+ uccRequest.getClientAccNo3()+
				"|"+uccRequest.getClientMICRNo3()+"|"+uccRequest.getClientIFSCCode3()+"|"+uccRequest.getDefaultBankFlag3()+"|"+uccRequest.getClientAccType4()+
				"|"+ uccRequest.getClientAccNo4()+"|"+uccRequest.getClientMICRNo4()+"|"+uccRequest.getClientIFSCCode4()+"|"+uccRequest.getDefaultBankFlag4()+
				"|"+uccRequest.getClientAccType5()+"|"+ uccRequest.getClientAccNo5()+"|"+uccRequest.getClientMICRNo5()+"|"+uccRequest.getClientIFSCCode5()+
				"|"+uccRequest.getDefaultBankFlag5()+"|"+uccRequest.getClientChequeName5()+"|"+uccRequest.getClientAdd1()+"|"+uccRequest.getClientAdd2()+
				"|"+uccRequest.getClientAdd3()+"|"+uccRequest.getClientCity()+"|"+uccRequest.getClientState()+"|"+uccRequest.getClientPincode()+
				"|"+uccRequest.getClientCountry()+"|"+uccRequest.getClientResiPhone()+"|"+uccRequest.getClientResiFax()+"|"+uccRequest.getClientOfficePhone()+
				"|"+uccRequest.getClientOfficeFax()+"|"+uccRequest.getClientEmail()+"|"+uccRequest.getClientCommMode()+"|"+uccRequest.getClientDivPayMode()+
				"|"+uccRequest.getClientPan2()+"|"+uccRequest.getClientPan3()+"|"+uccRequest.getMapinNo()+"|"+uccRequest.getCM_FORADD1()+"|"+uccRequest.getCM_FORADD2()+
				"|"+uccRequest.getCM_FORADD3()+"|"+uccRequest.getCM_FORCITY()+"|"+uccRequest.getCM_FORPINCODE()+"|"+uccRequest.getCM_FORSTATE()+
				"|"+uccRequest.getCM_FORCOUNTRY()+"|"+uccRequest.getCM_FORRESIPHONE()+"|"+uccRequest.getCM_FORRESIFAX()+"|"+uccRequest.getCM_FOROFFPHONE()+
				"|"+uccRequest.getCM_FOROFFFAX()+"|"+uccRequest.getCM_MOBILE();
		return param;
	}

	public String doFatca(FatcaRequest fatcaRequest) {
		
		IStarMFWebService port=getPort();
		String fatcaParam=createFetcaParam(fatcaRequest);
		System.out.println("FATCA Request: mfapi("+fatcaRequest.getFlag()+", "+fatcaRequest.getUserId()+", "+fatcaRequest.getEncryptedPassword()+", "+fatcaParam+")");
		String fatcaResponse=port.mfapi(fatcaRequest.getFlag(), fatcaRequest.getUserId(), fatcaRequest.getEncryptedPassword(), fatcaParam);
		System.out.println("FATCA Response: "+fatcaResponse);
		return fatcaResponse;
	}
	
	public String createFetcaParam(FatcaRequest fatcaRequest){
		String fatcaParam=fatcaRequest.getPanRp()+"|"+fatcaRequest.getPekrn()+"|"+fatcaRequest.getInvName()+"|"+fatcaRequest.getDob()+"|"+fatcaRequest.getFrName()+
				"|"+fatcaRequest.getSpName()+"|"+fatcaRequest.getTaxStatus()+"|"+fatcaRequest.getDataSrc()+"|"+fatcaRequest.getAddrType()+"|"+fatcaRequest.getPoBirInc()+
				"|"+fatcaRequest.getCoBirInc()+"|"+fatcaRequest.getTaxRes1()+"|"+fatcaRequest.getTpin1()+"|"+fatcaRequest.getId1Type()+"|"+fatcaRequest.getTaxRes2()+
				"|"+fatcaRequest.getTpin2()+"|"+fatcaRequest.getId2Type()+"|"+fatcaRequest.getTaxRes3()+"|"+fatcaRequest.getTpin3()+"|"+fatcaRequest.getId3Type()+
				"|"+fatcaRequest.getTaxRes4()+"|"+fatcaRequest.getTpin4()+"|"+fatcaRequest.getId4Type()+"|"+fatcaRequest.getSrceWealt()+"|"+fatcaRequest.getCorpServs()+
				"|"+fatcaRequest.getIncSlab()+"|"+fatcaRequest.getNetWorth()+"|"+fatcaRequest.getNwDate()+"|"+fatcaRequest.getPepFlag()+"|"+fatcaRequest.getOccCode()+
				"|"+fatcaRequest.getOccType()+"|"+fatcaRequest.getExempCode()+"|"+fatcaRequest.getFfiDrnfe()+"|"+fatcaRequest.getGiinNo()+"|"+fatcaRequest.getSprEntity()+
				"|"+fatcaRequest.getGiinNa()+"|"+fatcaRequest.getGiinExemc()+"|"+fatcaRequest.getNffeCatg()+"|"+fatcaRequest.getActNfeSc()+"|"+fatcaRequest.getNatureBus()+
				"|"+fatcaRequest.getRelListed()+"|"+fatcaRequest.getExchName()+"|"+fatcaRequest.getUboAppl()+"|"+fatcaRequest.getUboCount()+"|"+fatcaRequest.getUboName()+
				"|"+fatcaRequest.getUboPan()+"|"+fatcaRequest.getUboNation()+"|"+fatcaRequest.getUboAdd1()+"|"+fatcaRequest.getUboAdd2()+"|"+fatcaRequest.getUboAdd3()+
				"|"+fatcaRequest.getUboCity()+"|"+fatcaRequest.getUboPin()+"|"+fatcaRequest.getUboState()+"|"+fatcaRequest.getUboCntry()+"|"+fatcaRequest.getUboAddTy()+
				"|"+fatcaRequest.getUboCtr()+"|"+fatcaRequest.getUboTin()+"|"+fatcaRequest.getUboIdTy()+"|"+fatcaRequest.getUboCob()+"|"+fatcaRequest.getUboDob()+
				"|"+fatcaRequest.getUboGender()+"|"+fatcaRequest.getUboFrNam()+"|"+fatcaRequest.getUboOcc()+"|"+fatcaRequest.getUboOccTy()+"|"+fatcaRequest.getUboTel()+"|"+fatcaRequest.getUboMobile()+
				"|"+fatcaRequest.getUboCode()+"|"+fatcaRequest.getUboHolPc()+"|"+fatcaRequest.getSdfFlag()+"|"+fatcaRequest.getUboDf()+"|"+fatcaRequest.getAadhaarRp()+
				"|"+fatcaRequest.getNewChange()+"|"+fatcaRequest.getLogName()+"|"+fatcaRequest.getFiller1()+"|"+fatcaRequest.getFiller2();
		return fatcaParam;
	}
	
	public String getPaymentStatus(PaymentStatusRequest paymentStatusRequest){
		IStarMFWebService port=getPort();
		String param = paymentStatusParam(paymentStatusRequest);
		String paymentResponse = port.mfapi(paymentStatusRequest.getFlag(), paymentStatusRequest.getUserId(), paymentStatusRequest.getPassword(), param);
		return paymentResponse;
	}
	
	public String paymentStatusParam(PaymentStatusRequest paymentStatusRequest){
		String paymentParam=paymentStatusRequest.getClientCode()+"|"+paymentStatusRequest.getOrderNo()+"|"+paymentStatusRequest.getSagment();
		return paymentParam;
	}

	public String changePassword(ChangePassword changePassword) {
		IStarMFWebService port=getPort();
		String changePasswordParam = createChangePaswordParam(changePassword);
		String changePasswordResponse = port.mfapi(changePassword.getFlag(), changePassword.getUserId(), changePassword.getEncryptedPassword(), changePasswordParam);
		return changePasswordResponse;
	}
	
	public String createChangePaswordParam(ChangePassword changePassword){
		String param = changePassword.getOldPassword()+"|"+changePassword.getNewPassword()+"|"+changePassword.getConfPassword();
	    return param;
	}

		
}
