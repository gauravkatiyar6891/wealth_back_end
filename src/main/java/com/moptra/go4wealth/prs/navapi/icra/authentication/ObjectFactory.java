
package com.moptra.go4wealth.prs.navapi.icra.authentication;

import javax.xml.bind.JAXBElement;
import javax.xml.bind.annotation.XmlElementDecl;
import javax.xml.bind.annotation.XmlRegistry;
import javax.xml.namespace.QName;

import com.moptra.go4wealth.prs.navapi.icra.datacontracts.AuthToken;


/**
 * This object contains factory methods for each 
 * Java content interface and Java element interface 
 * generated in the dataprodiver.soap.icra.authentication package. 
 * <p>An ObjectFactory allows you to programatically 
 * construct new instances of the Java representation 
 * for XML content. The Java representation of XML 
 * content can consist of schema derived interfaces 
 * and classes representing the binding of schema 
 * type definitions, element declarations and model 
 * groups.  Factory methods for each of these are 
 * provided in this class.
 * 
 */
@XmlRegistry
public class ObjectFactory {

    private final static QName _LogOutForcefullyPassword_QNAME = new QName("http://ICRA.SOAP.DataProdiver/Authentication", "Password");
    private final static QName _LogOutForcefullyUserName_QNAME = new QName("http://ICRA.SOAP.DataProdiver/Authentication", "UserName");
    private final static QName _ChangePasswordAuthToken_QNAME = new QName("http://ICRA.SOAP.DataProdiver/Authentication", "AuthToken");
    private final static QName _ChangePasswordOldPassword_QNAME = new QName("http://ICRA.SOAP.DataProdiver/Authentication", "OldPassword");
    private final static QName _ChangePasswordNewPassword_QNAME = new QName("http://ICRA.SOAP.DataProdiver/Authentication", "NewPassword");
    private final static QName _ChangePasswordResponseChangePasswordResult_QNAME = new QName("http://ICRA.SOAP.DataProdiver/Authentication", "ChangePasswordResult");
    private final static QName _LogOutResponseLogOutResult_QNAME = new QName("http://ICRA.SOAP.DataProdiver/Authentication", "LogOutResult");
    private final static QName _LogInResponseLogInResult_QNAME = new QName("http://ICRA.SOAP.DataProdiver/Authentication", "LogInResult");

    /**
     * Create a new ObjectFactory that can be used to create new instances of schema derived classes for package: dataprodiver.soap.icra.authentication
     * 
     */
    public ObjectFactory() {
    }

    /**
     * Create an instance of {@link LogOutForcefully }
     * 
     */
    public LogOutForcefully createLogOutForcefully() {
        return new LogOutForcefully();
    }

    /**
     * Create an instance of {@link ChangePassword }
     * 
     */
    public ChangePassword createChangePassword() {
        return new ChangePassword();
    }

    /**
     * Create an instance of {@link ChangePasswordResponse }
     * 
     */
    public ChangePasswordResponse createChangePasswordResponse() {
        return new ChangePasswordResponse();
    }

    /**
     * Create an instance of {@link LogOutForcefullyResponse }
     * 
     */
    public LogOutForcefullyResponse createLogOutForcefullyResponse() {
        return new LogOutForcefullyResponse();
    }

    /**
     * Create an instance of {@link LogIn }
     * 
     */
    public LogIn createLogIn() {
        return new LogIn();
    }

    /**
     * Create an instance of {@link LogOutResponse }
     * 
     */
    public LogOutResponse createLogOutResponse() {
        return new LogOutResponse();
    }

    /**
     * Create an instance of {@link LogOut }
     * 
     */
    public LogOut createLogOut() {
        return new LogOut();
    }

    /**
     * Create an instance of {@link LogInResponse }
     * 
     */
    public LogInResponse createLogInResponse() {
        return new LogInResponse();
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link byte[]}{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://ICRA.SOAP.DataProdiver/Authentication", name = "Password", scope = LogOutForcefully.class)
    public JAXBElement<byte[]> createLogOutForcefullyPassword(byte[] value) {
        return new JAXBElement<byte[]>(_LogOutForcefullyPassword_QNAME, byte[].class, LogOutForcefully.class, ((byte[]) value));
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link String }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://ICRA.SOAP.DataProdiver/Authentication", name = "UserName", scope = LogOutForcefully.class)
    public JAXBElement<String> createLogOutForcefullyUserName(String value) {
        return new JAXBElement<String>(_LogOutForcefullyUserName_QNAME, String.class, LogOutForcefully.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link AuthToken }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://ICRA.SOAP.DataProdiver/Authentication", name = "AuthToken", scope = ChangePassword.class)
    public JAXBElement<AuthToken> createChangePasswordAuthToken(AuthToken value) {
        return new JAXBElement<AuthToken>(_ChangePasswordAuthToken_QNAME, AuthToken.class, ChangePassword.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link byte[]}{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://ICRA.SOAP.DataProdiver/Authentication", name = "OldPassword", scope = ChangePassword.class)
    public JAXBElement<byte[]> createChangePasswordOldPassword(byte[] value) {
        return new JAXBElement<byte[]>(_ChangePasswordOldPassword_QNAME, byte[].class, ChangePassword.class, ((byte[]) value));
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link byte[]}{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://ICRA.SOAP.DataProdiver/Authentication", name = "NewPassword", scope = ChangePassword.class)
    public JAXBElement<byte[]> createChangePasswordNewPassword(byte[] value) {
        return new JAXBElement<byte[]>(_ChangePasswordNewPassword_QNAME, byte[].class, ChangePassword.class, ((byte[]) value));
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link AuthToken }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://ICRA.SOAP.DataProdiver/Authentication", name = "ChangePasswordResult", scope = ChangePasswordResponse.class)
    public JAXBElement<AuthToken> createChangePasswordResponseChangePasswordResult(AuthToken value) {
        return new JAXBElement<AuthToken>(_ChangePasswordResponseChangePasswordResult_QNAME, AuthToken.class, ChangePasswordResponse.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link byte[]}{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://ICRA.SOAP.DataProdiver/Authentication", name = "Password", scope = LogIn.class)
    public JAXBElement<byte[]> createLogInPassword(byte[] value) {
        return new JAXBElement<byte[]>(_LogOutForcefullyPassword_QNAME, byte[].class, LogIn.class, ((byte[]) value));
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link String }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://ICRA.SOAP.DataProdiver/Authentication", name = "UserName", scope = LogIn.class)
    public JAXBElement<String> createLogInUserName(String value) {
        return new JAXBElement<String>(_LogOutForcefullyUserName_QNAME, String.class, LogIn.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link AuthToken }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://ICRA.SOAP.DataProdiver/Authentication", name = "LogOutResult", scope = LogOutResponse.class)
    public JAXBElement<AuthToken> createLogOutResponseLogOutResult(AuthToken value) {
        return new JAXBElement<AuthToken>(_LogOutResponseLogOutResult_QNAME, AuthToken.class, LogOutResponse.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link AuthToken }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://ICRA.SOAP.DataProdiver/Authentication", name = "AuthToken", scope = LogOut.class)
    public JAXBElement<AuthToken> createLogOutAuthToken(AuthToken value) {
        return new JAXBElement<AuthToken>(_ChangePasswordAuthToken_QNAME, AuthToken.class, LogOut.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link AuthToken }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://ICRA.SOAP.DataProdiver/Authentication", name = "LogInResult", scope = LogInResponse.class)
    public JAXBElement<AuthToken> createLogInResponseLogInResult(AuthToken value) {
        return new JAXBElement<AuthToken>(_LogInResponseLogInResult_QNAME, AuthToken.class, LogInResponse.class, value);
    }

}
