
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
 *         &lt;element name="LogOutResult" type="{http://ICRA.SOAP.DataProdiver/DataContracts/}AuthToken" minOccurs="0"/>
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
    "logOutResult"
})
@XmlRootElement(name = "LogOutResponse")
public class LogOutResponse {

    @XmlElementRef(name = "LogOutResult", namespace = "http://ICRA.SOAP.DataProdiver/Authentication", type = JAXBElement.class)
    protected JAXBElement<AuthToken> logOutResult;

    /**
     * Gets the value of the logOutResult property.
     * 
     * @return
     *     possible object is
     *     {@link JAXBElement }{@code <}{@link AuthToken }{@code >}
     *     
     */
    public JAXBElement<AuthToken> getLogOutResult() {
        return logOutResult;
    }

    /**
     * Sets the value of the logOutResult property.
     * 
     * @param value
     *     allowed object is
     *     {@link JAXBElement }{@code <}{@link AuthToken }{@code >}
     *     
     */
    public void setLogOutResult(JAXBElement<AuthToken> value) {
        this.logOutResult = ((JAXBElement<AuthToken> ) value);
    }

}
