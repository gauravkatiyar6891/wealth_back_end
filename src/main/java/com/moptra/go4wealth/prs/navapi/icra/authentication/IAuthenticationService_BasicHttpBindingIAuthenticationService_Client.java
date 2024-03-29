
package com.moptra.go4wealth.prs.navapi.icra.authentication;

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

/**
 * This class was generated by Apache CXF 2.7.5
 * 2018-09-26T14:13:35.395+05:30
 * Generated source version: 2.7.5
 * 
 */
public final class IAuthenticationService_BasicHttpBindingIAuthenticationService_Client {

    private static final QName SERVICE_NAME = new QName("http://ICRA.SOAP.DataProdiver/Authentication", "AuthService");

    private IAuthenticationService_BasicHttpBindingIAuthenticationService_Client() {
    }

    public static void main(String args[]) throws java.lang.Exception {
        URL wsdlURL = AuthService.WSDL_LOCATION;
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
      
        AuthService ss = new AuthService(wsdlURL, SERVICE_NAME);
        IAuthenticationService port = ss.getBasicHttpBindingIAuthenticationService();  
        
        {
        System.out.println("Invoking changePassword...");
        com.moptra.go4wealth.prs.navapi.icra.datacontracts.AuthToken _changePassword_authToken = null;
        byte[] _changePassword_oldPassword = new byte[0];
        byte[] _changePassword_newPassword = new byte[0];
        com.moptra.go4wealth.prs.navapi.icra.datacontracts.AuthToken _changePassword__return = port.changePassword(_changePassword_authToken, _changePassword_oldPassword, _changePassword_newPassword);
        System.out.println("changePassword.result=" + _changePassword__return);


        }
        {
        System.out.println("Invoking logIn...");
        java.lang.String _logIn_userName = "";
        byte[] _logIn_password = new byte[0];
        com.moptra.go4wealth.prs.navapi.icra.datacontracts.AuthToken _logIn__return = port.logIn(_logIn_userName, _logIn_password);
        System.out.println("logIn.result=" + _logIn__return);


        }
        {
        System.out.println("Invoking logOutForcefully...");
        java.lang.String _logOutForcefully_userName = "";
        byte[] _logOutForcefully_password = new byte[0];
        java.lang.Boolean _logOutForcefully__return = port.logOutForcefully(_logOutForcefully_userName, _logOutForcefully_password);
        System.out.println("logOutForcefully.result=" + _logOutForcefully__return);


        }
        {
        System.out.println("Invoking logOut...");
        com.moptra.go4wealth.prs.navapi.icra.datacontracts.AuthToken _logOut_authToken = null;
        com.moptra.go4wealth.prs.navapi.icra.datacontracts.AuthToken _logOut__return = port.logOut(_logOut_authToken);
        System.out.println("logOut.result=" + _logOut__return);


        }

        System.exit(0);
    }

}
