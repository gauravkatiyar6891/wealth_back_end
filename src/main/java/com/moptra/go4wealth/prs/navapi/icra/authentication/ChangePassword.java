
package com.moptra.go4wealth.prs.navapi.icra.authentication;

import javax.xml.bind.JAXBElement;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElementRef;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlType;

import com.moptra.go4wealth.prs.navapi.icra.datacontracts.AuthToken;


/**
 * <p>Java class for anonymous complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType>
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element name="AuthToken" type="{http://ICRA.SOAP.DataProdiver/DataContracts/}AuthToken" minOccurs="0"/>
 *         &lt;element name="OldPassword" type="{http://www.w3.org/2001/XMLSchema}base64Binary" minOccurs="0"/>
 *         &lt;element name="NewPassword" type="{http://www.w3.org/2001/XMLSchema}base64Binary" minOccurs="0"/>
 *       &lt;/sequence>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "", propOrder = {
    "authToken",
    "oldPassword",
    "newPassword"
})
@XmlRootElement(name = "ChangePassword")
public class ChangePassword {

    @XmlElementRef(name = "AuthToken", namespace = "http://ICRA.SOAP.DataProdiver/Authentication", type = JAXBElement.class)
    protected JAXBElement<AuthToken> authToken;
    @XmlElementRef(name = "OldPassword", namespace = "http://ICRA.SOAP.DataProdiver/Authentication", type = JAXBElement.class)
    protected JAXBElement<byte[]> oldPassword;
    @XmlElementRef(name = "NewPassword", namespace = "http://ICRA.SOAP.DataProdiver/Authentication", type = JAXBElement.class)
    protected JAXBElement<byte[]> newPassword;

    /**
     * Gets the value of the authToken property.
     * 
     * @return
     *     possible object is
     *     {@link JAXBElement }{@code <}{@link AuthToken }{@code >}
     *     
     */
    public JAXBElement<AuthToken> getAuthToken() {
        return authToken;
    }

    /**
     * Sets the value of the authToken property.
     * 
     * @param value
     *     allowed object is
     *     {@link JAXBElement }{@code <}{@link AuthToken }{@code >}
     *     
     */
    public void setAuthToken(JAXBElement<AuthToken> value) {
        this.authToken = ((JAXBElement<AuthToken> ) value);
    }

    /**
     * Gets the value of the oldPassword property.
     * 
     * @return
     *     possible object is
     *     {@link JAXBElement }{@code <}{@link byte[]}{@code >}
     *     
     */
    public JAXBElement<byte[]> getOldPassword() {
        return oldPassword;
    }

    /**
     * Sets the value of the oldPassword property.
     * 
     * @param value
     *     allowed object is
     *     {@link JAXBElement }{@code <}{@link byte[]}{@code >}
     *     
     */
    public void setOldPassword(JAXBElement<byte[]> value) {
        this.oldPassword = ((JAXBElement<byte[]> ) value);
    }

    /**
     * Gets the value of the newPassword property.
     * 
     * @return
     *     possible object is
     *     {@link JAXBElement }{@code <}{@link byte[]}{@code >}
     *     
     */
    public JAXBElement<byte[]> getNewPassword() {
        return newPassword;
    }

    /**
     * Sets the value of the newPassword property.
     * 
     * @param value
     *     allowed object is
     *     {@link JAXBElement }{@code <}{@link byte[]}{@code >}
     *     
     */
    public void setNewPassword(JAXBElement<byte[]> value) {
        this.newPassword = ((JAXBElement<byte[]> ) value);
    }

}
