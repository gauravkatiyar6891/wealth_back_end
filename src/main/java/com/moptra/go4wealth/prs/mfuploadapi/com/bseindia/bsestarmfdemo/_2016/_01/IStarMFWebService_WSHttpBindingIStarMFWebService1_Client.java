
package com.moptra.go4wealth.prs.mfuploadapi.com.bseindia.bsestarmfdemo._2016._01;

/**
 * Please modify this class to meet your needs
 * This class is not complete
 */

import java.io.File;
import java.net.MalformedURLException;
import java.net.URL;
import javax.xml.namespace.QName;
import javax.jws.WebMethod;
import javax.jws.WebParam;
import javax.jws.WebResult;
import javax.jws.WebService;
import javax.xml.bind.annotation.XmlSeeAlso;
import javax.xml.ws.Action;
import javax.xml.ws.RequestWrapper;
import javax.xml.ws.ResponseWrapper;
import com.moptra.go4wealth.prs.mfuploadapi.org.tempuri.StarMFWebService;

/**
 * This class was generated by Apache CXF 2.7.5
 * 2018-07-24T18:47:25.989+05:30
 * Generated source version: 2.7.5
 * 
 */
public final class IStarMFWebService_WSHttpBindingIStarMFWebService1_Client {

    private static final QName SERVICE_NAME = new QName("http://tempuri.org/", "StarMFWebService");

    private IStarMFWebService_WSHttpBindingIStarMFWebService1_Client() {
    }

    public static void main(String args[]) throws java.lang.Exception {
        URL wsdlURL = StarMFWebService.WSDL_LOCATION;
        if (args.length > 0 && args[0] != null && !"".equals(args[0])) { 
            File wsdlFile = new File(args[0]);
            try {
                if (wsdlFile.exists()) {
                    wsdlURL = wsdlFile.toURI().toURL();
                } else {
                    wsdlURL = new URL(args[0]);
                }
            } catch (MalformedURLException e) {
                e.printStackTrace();
            }
        }
      
        StarMFWebService ss = new StarMFWebService(wsdlURL, SERVICE_NAME);
        IStarMFWebService port = ss.getWSHttpBindingIStarMFWebService1();  
        
        {
        System.out.println("Invoking eMandateAuthURL...");
        com.moptra.go4wealth.prs.mfuploadapi.org.datacontract.schemas._2004._07.starmfwebservice.EMandateAuthURLRequest _eMandateAuthURL_param = null;
        com.moptra.go4wealth.prs.mfuploadapi.org.datacontract.schemas._2004._07.starmfwebservice.EMandateAuthURLResponse _eMandateAuthURL__return = port.eMandateAuthURL(_eMandateAuthURL_param);
        System.out.println("eMandateAuthURL.result=" + _eMandateAuthURL__return);


        }
        {
        System.out.println("Invoking provOrderStatus...");
        com.moptra.go4wealth.prs.mfuploadapi.org.datacontract.schemas._2004._07.starmfwebservice.OrderRequest _provOrderStatus_param = null;
        com.moptra.go4wealth.prs.mfuploadapi.org.datacontract.schemas._2004._07.starmfwebservice.ProvOrderResponse _provOrderStatus__return = port.provOrderStatus(_provOrderStatus_param);
        System.out.println("provOrderStatus.result=" + _provOrderStatus__return);


        }
        {
        System.out.println("Invoking getAccessToken...");
        com.moptra.go4wealth.prs.mfuploadapi.org.datacontract.schemas._2004._07.starmfwebservice.PasswordRequest _getAccessToken_param = null;
        com.moptra.go4wealth.prs.mfuploadapi.org.datacontract.schemas._2004._07.starmfwebservice.Response _getAccessToken__return = port.getAccessToken(_getAccessToken_param);
        System.out.println("getAccessToken.result=" + _getAccessToken__return);


        }
        {
        System.out.println("Invoking getPasswordMobile...");
        com.moptra.go4wealth.prs.mfuploadapi.org.datacontract.schemas._2004._07.starmfwebservice.PasswordRequest _getPasswordMobile_param = null;
        com.moptra.go4wealth.prs.mfuploadapi.org.datacontract.schemas._2004._07.starmfwebservice.Response _getPasswordMobile__return = port.getPasswordMobile(_getPasswordMobile_param);
        System.out.println("getPasswordMobile.result=" + _getPasswordMobile__return);


        }
        {
        System.out.println("Invoking getPasswordForChildOrder...");
        com.moptra.go4wealth.prs.mfuploadapi.org.datacontract.schemas._2004._07.starmfwebservice.PasswordRequest _getPasswordForChildOrder_param = null;
        com.moptra.go4wealth.prs.mfuploadapi.org.datacontract.schemas._2004._07.starmfwebservice.Response _getPasswordForChildOrder__return = port.getPasswordForChildOrder(_getPasswordForChildOrder_param);
        System.out.println("getPasswordForChildOrder.result=" + _getPasswordForChildOrder__return);


        }
        {
        System.out.println("Invoking childOrderDetails...");
        com.moptra.go4wealth.prs.mfuploadapi.org.datacontract.schemas._2004._07.starmfwebservice.ChildOrderRequest _childOrderDetails_param = null;
        com.moptra.go4wealth.prs.mfuploadapi.org.datacontract.schemas._2004._07.starmfwebservice.ChildOrderResponse _childOrderDetails__return = port.childOrderDetails(_childOrderDetails_param);
        System.out.println("childOrderDetails.result=" + _childOrderDetails__return);


        }
        {
        System.out.println("Invoking mfapiMobile...");
        com.moptra.go4wealth.prs.mfuploadapi.org.datacontract.schemas._2004._07.starmfwebservice.MFAPIRequest _mfapiMobile_param = null;
        com.moptra.go4wealth.prs.mfuploadapi.org.datacontract.schemas._2004._07.starmfwebservice.Response _mfapiMobile__return = port.mfapiMobile(_mfapiMobile_param);
        System.out.println("mfapiMobile.result=" + _mfapiMobile__return);


        }
        {
        System.out.println("Invoking getPassword...");
        java.lang.String _getPassword_userId = "";
        java.lang.String _getPassword_memberId = "";
        java.lang.String _getPassword_password = "";
        java.lang.String _getPassword_passKey = "";
        java.lang.String _getPassword__return = port.getPassword(_getPassword_userId, _getPassword_memberId, _getPassword_password, _getPassword_passKey);
        System.out.println("getPassword.result=" + _getPassword__return);


        }
        {
        System.out.println("Invoking orderStatus...");
        com.moptra.go4wealth.prs.mfuploadapi.org.datacontract.schemas._2004._07.starmfwebservice.OrderRequest _orderStatus_param = null;
        com.moptra.go4wealth.prs.mfuploadapi.org.datacontract.schemas._2004._07.starmfwebservice.OrderResponse _orderStatus__return = port.orderStatus(_orderStatus_param);
        System.out.println("orderStatus.result=" + _orderStatus__return);


        }
        {
        System.out.println("Invoking mfapi...");
        java.lang.String _mfapi_flag = "";
        java.lang.String _mfapi_userId = "";
        java.lang.String _mfapi_encryptedPassword = "";
        java.lang.String _mfapi_param = "";
        java.lang.String _mfapi__return = port.mfapi(_mfapi_flag, _mfapi_userId, _mfapi_encryptedPassword, _mfapi_param);
        System.out.println("mfapi.result=" + _mfapi__return);


        }
        {
        System.out.println("Invoking mandateDetails...");
        com.moptra.go4wealth.prs.mfuploadapi.org.datacontract.schemas._2004._07.starmfwebservice.MandateDetailsRequest _mandateDetails_param = null;
        com.moptra.go4wealth.prs.mfuploadapi.org.datacontract.schemas._2004._07.starmfwebservice.MandateDetailsResponse _mandateDetails__return = port.mandateDetails(_mandateDetails_param);
        System.out.println("mandateDetails.result=" + _mandateDetails__return);


        }
        {
        System.out.println("Invoking allotmentStatement...");
        com.moptra.go4wealth.prs.mfuploadapi.org.datacontract.schemas._2004._07.starmfwebservice.AllotmentStatementRequest _allotmentStatement_param = null;
        com.moptra.go4wealth.prs.mfuploadapi.org.datacontract.schemas._2004._07.starmfwebservice.AllotmentStatementResponse _allotmentStatement__return = port.allotmentStatement(_allotmentStatement_param);
        System.out.println("allotmentStatement.result=" + _allotmentStatement__return);


        }
        {
        System.out.println("Invoking redemptionStatement...");
        com.moptra.go4wealth.prs.mfuploadapi.org.datacontract.schemas._2004._07.starmfwebservice.RedemptionStatementRequest _redemptionStatement_param = null;
        com.moptra.go4wealth.prs.mfuploadapi.org.datacontract.schemas._2004._07.starmfwebservice.RedemptionStatementResponse _redemptionStatement__return = port.redemptionStatement(_redemptionStatement_param);
        System.out.println("redemptionStatement.result=" + _redemptionStatement__return);


        }

        System.exit(0);
    }

}
