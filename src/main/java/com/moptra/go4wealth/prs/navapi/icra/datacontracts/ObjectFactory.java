
package com.moptra.go4wealth.prs.navapi.icra.datacontracts;

import javax.xml.bind.JAXBElement;
import javax.xml.bind.annotation.XmlElementDecl;
import javax.xml.bind.annotation.XmlRegistry;
import javax.xml.namespace.QName;


/**
 * This object contains factory methods for each 
 * Java content interface and Java element interface 
 * generated in the dataprodiver.soap.icra.datacontracts package. 
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

    private final static QName _AuthToken_QNAME = new QName("http://ICRA.SOAP.DataProdiver/DataContracts/", "AuthToken");
    private final static QName _AuthTokenInformation_QNAME = new QName("http://ICRA.SOAP.DataProdiver/DataContracts/", "Information");
    private final static QName _AuthTokenSecurityKey_QNAME = new QName("http://ICRA.SOAP.DataProdiver/DataContracts/", "SecurityKey");

    /**
     * Create a new ObjectFactory that can be used to create new instances of schema derived classes for package: dataprodiver.soap.icra.datacontracts
     * 
     */
    public ObjectFactory() {
    }

    /**
     * Create an instance of {@link AuthToken }
     * 
     */
    public AuthToken createAuthToken() {
        return new AuthToken();
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link AuthToken }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://ICRA.SOAP.DataProdiver/DataContracts/", name = "AuthToken")
    public JAXBElement<AuthToken> createAuthToken(AuthToken value) {
        return new JAXBElement<AuthToken>(_AuthToken_QNAME, AuthToken.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link String }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://ICRA.SOAP.DataProdiver/DataContracts/", name = "Information", scope = AuthToken.class)
    public JAXBElement<String> createAuthTokenInformation(String value) {
        return new JAXBElement<String>(_AuthTokenInformation_QNAME, String.class, AuthToken.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link String }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://ICRA.SOAP.DataProdiver/DataContracts/", name = "SecurityKey", scope = AuthToken.class)
    public JAXBElement<String> createAuthTokenSecurityKey(String value) {
        return new JAXBElement<String>(_AuthTokenSecurityKey_QNAME, String.class, AuthToken.class, value);
    }

}
