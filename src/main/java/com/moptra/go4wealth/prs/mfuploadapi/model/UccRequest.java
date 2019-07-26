package com.moptra.go4wealth.prs.mfuploadapi.model;

public class UccRequest {
	
	private GetPasswordRequest getPasswordRequest;
	
	private String flag;
	private String userId;
	private String encryptedPassword;
	
	private String clientCode; // mandatory for all cases.
	private String clientHolding;
	private String clientTaxStatus;
	private String clientOccupationCode;
	private String clientAppName1;
	private String clientAppName2;   // non mandatory. Mandatory if the holding is JO or AS
	private String clientAppName3;   // non mandatory.
	private String clientDob;
	private String clientGender;
	private String clientFather;     // if tax status is 02 i.e. on behalf of minor then mandatory or else non mandatory
	private String clientPan;        // non mandatory if the tax status is 02 i.e. on behalf of minor, else mandatory
	private String clientNominee;    // non mandatory
	private String clientNomineeRelation;  // mandatory if client no-minee is given else non mandatory
	private String clientGuardianPan;      // if tax status is 02 i.e. on behalf of minor then mandatory or else non mandatory
	private String clientType;             // mandatory for all the cases [P-Physical / D-Demat]
	private String clientDefaultDp;       // mandatory if value is "D" in client type
	private String clientCDSLDPId;       // mandatory if value is "C" in client default DP 
	private String clientCDSLCLTId;     // mandatory if value is "C" in client default DP
	private String clientNSDLDPId;     // mandatory if value is "N" in client default DP
	private String clientNSDLCLTId;   // mandatory if value is "N" in client default DP
	
	private String clientAccType1;
	private String clientAccNo1;
	private String clientMICRNo1;
	private String clientIFSCCode1;
	private String defaultBankFlag1;
	
	private String clientAccType2;         // non mandatory
	private String clientAccNo2;          // mandatory of client account type 2 is given
	private String clientMICRNo2;        // mandatory of client account type 2 is given
	private String clientIFSCCode2;     // mandatory of client account type 2 is given
	private String defaultBankFlag2;   // mandatory for all the cases - only one bank can be default bank
	
	private String clientAccType3;         // non mandatory
	private String clientAccNo3;          // mandatory of client account type 3 is given
	private String clientMICRNo3;        // mandatory of client account type 3 is given
	private String clientIFSCCode3;     // mandatory of client account type 3 is given
	private String defaultBankFlag3;   // mandatory for all the cases - only one bank can be default bank
	
	private String clientAccType4;         // non mandatory
	private String clientAccNo4;          // mandatory of client account type 4 is given
	private String clientMICRNo4;        // mandatory of client account type 4 is given
	private String clientIFSCCode4;     // mandatory of client account type 4 is given
	private String defaultBankFlag4;   // mandatory for all the cases - only one bank can be default bank
	 
	private String clientAccType5;         // non mandatory
	private String clientAccNo5;          // mandatory of client account type 5 is given
	private String clientMICRNo5;        // mandatory of client account type 5 is given
	private String clientIFSCCode5;     // mandatory of client account type 5 is given
	private String defaultBankFlag5;   // mandatory for all the cases - only one bank can be default bank
	private String clientChequeName5;
	
	private String clientAdd1;
	private String clientAdd2;          // non mandatory
	private String clientAdd3;          // non mandatory
	private String clientCity;
	private String clientState;
	private String clientPincode;
	private String clientCountry;
	private String clientResiPhone;       // non-mandatory
	private String clientResiFax;        // non-mandatory
	private String clientOfficePhone;   // non-mandatory
	private String clientOfficeFax;    // non-mandatory
	private String clientEmail;        //mandatory for MFD clients
	private String clientCommMode;
	private String clientDivPayMode;
	private String clientPan2;            // mandatory if client app name 2 is given
	private String clientPan3;           // mandatory if client app name 3 is given
	private String mapinNo;
	private String CM_FORADD1;                // mandatory if tax code is 21 or 24
	private String CM_FORADD2;               // non-mandatory
	private String CM_FORADD3;              // non-mandatory
	private String CM_FORCITY;             // mandatory if tax code is 21 or 24
	private String CM_FORPINCODE;         // mandatory if tax code is 21 or 24
	private String CM_FORSTATE;          // non-mandatory
	private String CM_FORCOUNTRY;       // mandatory if tax code is 21 or 24
	private String CM_FORRESIPHONE;    // non-mandatory
	private String CM_FORRESIFAX;     // non-mandatory
	private String CM_FOROFFPHONE;   // non-mandatory
	private String CM_FOROFFFAX;    // non-mandatory
	private String CM_MOBILE;      // mandatory for MFD clients
	
	
	public GetPasswordRequest getGetPasswordRequest() {
		return getPasswordRequest;
	}
	public void setGetPasswordRequest(GetPasswordRequest getPasswordRequest) {
		this.getPasswordRequest = getPasswordRequest;
	}
	
	
	public String getFlag() {
		return flag;
	}
	public void setFlag(String flag) {
		this.flag = flag;
	}
	public String getUserId() {
		return userId;
	}
	public void setUserId(String userId) {
		this.userId = userId;
	}
	public String getEncryptedPassword() {
		return encryptedPassword;
	}
	public void setEncryptedPassword(String encryptedPassword) {
		this.encryptedPassword = encryptedPassword;
	}
	public String getClientCode() {
		return clientCode;
	}
	public void setClientCode(String clientCode) {
		this.clientCode = clientCode;
	}
	public String getClientHolding() {
		return clientHolding;
	}
	public void setClientHolding(String clientHolding) {
		this.clientHolding = clientHolding;
	}
	public String getClientTaxStatus() {
		return clientTaxStatus;
	}
	public void setClientTaxStatus(String clientTaxStatus) {
		this.clientTaxStatus = clientTaxStatus;
	}
	public String getClientOccupationCode() {
		return clientOccupationCode;
	}
	public void setClientOccupationCode(String clientOccupationCode) {
		this.clientOccupationCode = clientOccupationCode;
	}
	public String getClientAppName1() {
		return clientAppName1;
	}
	public void setClientAppName1(String clientAppName1) {
		this.clientAppName1 = clientAppName1;
	}
	public String getClientAppName2() {
		return clientAppName2;
	}
	public void setClientAppName2(String clientAppName2) {
		this.clientAppName2 = clientAppName2;
	}
	public String getClientAppName3() {
		return clientAppName3;
	}
	public void setClientAppName3(String clientAppName3) {
		this.clientAppName3 = clientAppName3;
	}
	public String getClientDob() {
		return clientDob;
	}
	public void setClientDob(String clientDob) {
		this.clientDob = clientDob;
	}
	public String getClientGender() {
		return clientGender;
	}
	public void setClientGender(String clientGender) {
		this.clientGender = clientGender;
	}
	public String getClientFather() {
		return clientFather;
	}
	public void setClientFather(String clientFather) {
		this.clientFather = clientFather;
	}
	public String getClientPan() {
		return clientPan;
	}
	public void setClientPan(String clientPan) {
		this.clientPan = clientPan;
	}
	public String getClientNominee() {
		return clientNominee;
	}
	public void setClientNominee(String clientNominee) {
		this.clientNominee = clientNominee;
	}
	public String getClientNomineeRelation() {
		return clientNomineeRelation;
	}
	public void setClientNomineeRelation(String clientNomineeRelation) {
		this.clientNomineeRelation = clientNomineeRelation;
	}
	public String getClientGuardianPan() {
		return clientGuardianPan;
	}
	public void setClientGuardianPan(String clientGuardianPan) {
		this.clientGuardianPan = clientGuardianPan;
	}
	public String getClientType() {
		return clientType;
	}
	public void setClientType(String clientType) {
		this.clientType = clientType;
	}
	public String getClientDefaultDp() {
		return clientDefaultDp;
	}
	public void setClientDefaultDp(String clientDefaultDp) {
		this.clientDefaultDp = clientDefaultDp;
	}
	public String getClientCDSLDPId() {
		return clientCDSLDPId;
	}
	public void setClientCDSLDPId(String clientCDSLDPId) {
		this.clientCDSLDPId = clientCDSLDPId;
	}
	public String getClientCDSLCLTId() {
		return clientCDSLCLTId;
	}
	public void setClientCDSLCLTId(String clientCDSLCLTId) {
		this.clientCDSLCLTId = clientCDSLCLTId;
	}
	public String getClientNSDLDPId() {
		return clientNSDLDPId;
	}
	public void setClientNSDLDPId(String clientNSDLDPId) {
		this.clientNSDLDPId = clientNSDLDPId;
	}
	public String getClientNSDLCLTId() {
		return clientNSDLCLTId;
	}
	public void setClientNSDLCLTId(String clientNSDLCLTId) {
		this.clientNSDLCLTId = clientNSDLCLTId;
	}
	public String getClientAccType1() {
		return clientAccType1;
	}
	public void setClientAccType1(String clientAccType1) {
		this.clientAccType1 = clientAccType1;
	}
	public String getClientAccNo1() {
		return clientAccNo1;
	}
	public void setClientAccNo1(String clientAccNo1) {
		this.clientAccNo1 = clientAccNo1;
	}
	public String getClientMICRNo1() {
		return clientMICRNo1;
	}
	public void setClientMICRNo1(String clientMICRNo1) {
		this.clientMICRNo1 = clientMICRNo1;
	}
	public String getClientIFSCCode1() {
		return clientIFSCCode1;
	}
	public void setClientIFSCCode1(String clientIFSCCode1) {
		this.clientIFSCCode1 = clientIFSCCode1;
	}
	public String getDefaultBankFlag1() {
		return defaultBankFlag1;
	}
	public void setDefaultBankFlag1(String defaultBankFlag1) {
		this.defaultBankFlag1 = defaultBankFlag1;
	}
	public String getClientAccType2() {
		return clientAccType2;
	}
	public void setClientAccType2(String clientAccType2) {
		this.clientAccType2 = clientAccType2;
	}
	public String getClientAccNo2() {
		return clientAccNo2;
	}
	public void setClientAccNo2(String clientAccNo2) {
		this.clientAccNo2 = clientAccNo2;
	}
	public String getClientMICRNo2() {
		return clientMICRNo2;
	}
	public void setClientMICRNo2(String clientMICRNo2) {
		this.clientMICRNo2 = clientMICRNo2;
	}
	public String getClientIFSCCode2() {
		return clientIFSCCode2;
	}
	public void setClientIFSCCode2(String clientIFSCCode2) {
		this.clientIFSCCode2 = clientIFSCCode2;
	}
	public String getDefaultBankFlag2() {
		return defaultBankFlag2;
	}
	public void setDefaultBankFlag2(String defaultBankFlag2) {
		this.defaultBankFlag2 = defaultBankFlag2;
	}
	public String getClientAccType3() {
		return clientAccType3;
	}
	public void setClientAccType3(String clientAccType3) {
		this.clientAccType3 = clientAccType3;
	}
	public String getClientAccNo3() {
		return clientAccNo3;
	}
	public void setClientAccNo3(String clientAccNo3) {
		this.clientAccNo3 = clientAccNo3;
	}
	public String getClientMICRNo3() {
		return clientMICRNo3;
	}
	public void setClientMICRNo3(String clientMICRNo3) {
		this.clientMICRNo3 = clientMICRNo3;
	}
	public String getClientIFSCCode3() {
		return clientIFSCCode3;
	}
	public void setClientIFSCCode3(String clientIFSCCode3) {
		this.clientIFSCCode3 = clientIFSCCode3;
	}
	public String getDefaultBankFlag3() {
		return defaultBankFlag3;
	}
	public void setDefaultBankFlag3(String defaultBankFlag3) {
		this.defaultBankFlag3 = defaultBankFlag3;
	}
	public String getClientAccType4() {
		return clientAccType4;
	}
	public void setClientAccType4(String clientAccType4) {
		this.clientAccType4 = clientAccType4;
	}
	public String getClientAccNo4() {
		return clientAccNo4;
	}
	public void setClientAccNo4(String clientAccNo4) {
		this.clientAccNo4 = clientAccNo4;
	}
	public String getClientMICRNo4() {
		return clientMICRNo4;
	}
	public void setClientMICRNo4(String clientMICRNo4) {
		this.clientMICRNo4 = clientMICRNo4;
	}
	public String getClientIFSCCode4() {
		return clientIFSCCode4;
	}
	public void setClientIFSCCode4(String clientIFSCCode4) {
		this.clientIFSCCode4 = clientIFSCCode4;
	}
	public String getDefaultBankFlag4() {
		return defaultBankFlag4;
	}
	public void setDefaultBankFlag4(String defaultBankFlag4) {
		this.defaultBankFlag4 = defaultBankFlag4;
	}
	public String getClientAccType5() {
		return clientAccType5;
	}
	public void setClientAccType5(String clientAccType5) {
		this.clientAccType5 = clientAccType5;
	}
	public String getClientAccNo5() {
		return clientAccNo5;
	}
	public void setClientAccNo5(String clientAccNo5) {
		this.clientAccNo5 = clientAccNo5;
	}
	public String getClientMICRNo5() {
		return clientMICRNo5;
	}
	public void setClientMICRNo5(String clientMICRNo5) {
		this.clientMICRNo5 = clientMICRNo5;
	}
	public String getClientIFSCCode5() {
		return clientIFSCCode5;
	}
	public void setClientIFSCCode5(String clientIFSCCode5) {
		this.clientIFSCCode5 = clientIFSCCode5;
	}
	public String getDefaultBankFlag5() {
		return defaultBankFlag5;
	}
	public void setDefaultBankFlag5(String defaultBankFlag5) {
		this.defaultBankFlag5 = defaultBankFlag5;
	}
	public String getClientChequeName5() {
		return clientChequeName5;
	}
	public void setClientChequeName5(String clientChequeName5) {
		this.clientChequeName5 = clientChequeName5;
	}
	public String getClientAdd1() {
		return clientAdd1;
	}
	public void setClientAdd1(String clientAdd1) {
		this.clientAdd1 = clientAdd1;
	}
	public String getClientAdd2() {
		return clientAdd2;
	}
	public void setClientAdd2(String clientAdd2) {
		this.clientAdd2 = clientAdd2;
	}
	public String getClientAdd3() {
		return clientAdd3;
	}
	public void setClientAdd3(String clientAdd3) {
		this.clientAdd3 = clientAdd3;
	}
	public String getClientCity() {
		return clientCity;
	}
	public void setClientCity(String clientCity) {
		this.clientCity = clientCity;
	}
	public String getClientState() {
		return clientState;
	}
	public void setClientState(String clientState) {
		this.clientState = clientState;
	}
	public String getClientPincode() {
		return clientPincode;
	}
	public void setClientPincode(String clientPincode) {
		this.clientPincode = clientPincode;
	}
	public String getClientCountry() {
		return clientCountry;
	}
	public void setClientCountry(String clientCountry) {
		this.clientCountry = clientCountry;
	}
	public String getClientResiPhone() {
		return clientResiPhone;
	}
	public void setClientResiPhone(String clientResiPhone) {
		this.clientResiPhone = clientResiPhone;
	}
	public String getClientResiFax() {
		return clientResiFax;
	}
	public void setClientResiFax(String clientResiFax) {
		this.clientResiFax = clientResiFax;
	}
	public String getClientOfficePhone() {
		return clientOfficePhone;
	}
	public void setClientOfficePhone(String clientOfficePhone) {
		this.clientOfficePhone = clientOfficePhone;
	}
	public String getClientOfficeFax() {
		return clientOfficeFax;
	}
	public void setClientOfficeFax(String clientOfficeFax) {
		this.clientOfficeFax = clientOfficeFax;
	}
	public String getClientEmail() {
		return clientEmail;
	}
	public void setClientEmail(String clientEmail) {
		this.clientEmail = clientEmail;
	}
	public String getClientCommMode() {
		return clientCommMode;
	}
	public void setClientCommMode(String clientCommMode) {
		this.clientCommMode = clientCommMode;
	}
	public String getClientDivPayMode() {
		return clientDivPayMode;
	}
	public void setClientDivPayMode(String clientDivPayMode) {
		this.clientDivPayMode = clientDivPayMode;
	}
	public String getClientPan2() {
		return clientPan2;
	}
	public void setClientPan2(String clientPan2) {
		this.clientPan2 = clientPan2;
	}
	public String getClientPan3() {
		return clientPan3;
	}
	public void setClientPan3(String clientPan3) {
		this.clientPan3 = clientPan3;
	}
	public String getMapinNo() {
		return mapinNo;
	}
	public void setMapinNo(String mapinNo) {
		this.mapinNo = mapinNo;
	}
	public String getCM_FORADD1() {
		return CM_FORADD1;
	}
	public void setCM_FORADD1(String cM_FORADD1) {
		CM_FORADD1 = cM_FORADD1;
	}
	public String getCM_FORADD2() {
		return CM_FORADD2;
	}
	public void setCM_FORADD2(String cM_FORADD2) {
		CM_FORADD2 = cM_FORADD2;
	}
	public String getCM_FORADD3() {
		return CM_FORADD3;
	}
	public void setCM_FORADD3(String cM_FORADD3) {
		CM_FORADD3 = cM_FORADD3;
	}
	public String getCM_FORCITY() {
		return CM_FORCITY;
	}
	public void setCM_FORCITY(String cM_FORCITY) {
		CM_FORCITY = cM_FORCITY;
	}
	public String getCM_FORPINCODE() {
		return CM_FORPINCODE;
	}
	public void setCM_FORPINCODE(String cM_FORPINCODE) {
		CM_FORPINCODE = cM_FORPINCODE;
	}
	public String getCM_FORSTATE() {
		return CM_FORSTATE;
	}
	public void setCM_FORSTATE(String cM_FORSTATE) {
		CM_FORSTATE = cM_FORSTATE;
	}
	public String getCM_FORCOUNTRY() {
		return CM_FORCOUNTRY;
	}
	public void setCM_FORCOUNTRY(String cM_FORCOUNTRY) {
		CM_FORCOUNTRY = cM_FORCOUNTRY;
	}
	public String getCM_FORRESIPHONE() {
		return CM_FORRESIPHONE;
	}
	public void setCM_FORRESIPHONE(String cM_FORRESIPHONE) {
		CM_FORRESIPHONE = cM_FORRESIPHONE;
	}
	public String getCM_FORRESIFAX() {
		return CM_FORRESIFAX;
	}
	public void setCM_FORRESIFAX(String cM_FORRESIFAX) {
		CM_FORRESIFAX = cM_FORRESIFAX;
	}
	public String getCM_FOROFFPHONE() {
		return CM_FOROFFPHONE;
	}
	public void setCM_FOROFFPHONE(String cM_FOROFFPHONE) {
		CM_FOROFFPHONE = cM_FOROFFPHONE;
	}
	public String getCM_FOROFFFAX() {
		return CM_FOROFFFAX;
	}
	public void setCM_FOROFFFAX(String cM_FOROFFFAX) {
		CM_FOROFFFAX = cM_FOROFFFAX;
	}
	public String getCM_MOBILE() {
		return CM_MOBILE;
	}
	public void setCM_MOBILE(String cM_MOBILE) {
		CM_MOBILE = cM_MOBILE;
	}
	
	
	
}