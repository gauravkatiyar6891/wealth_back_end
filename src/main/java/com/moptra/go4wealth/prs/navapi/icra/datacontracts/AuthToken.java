
package com.moptra.go4wealth.prs.navapi.icra.datacontracts;

import javax.xml.bind.JAXBElement;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlElementRef;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Java class for AuthToken complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="AuthToken">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element name="Information" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="IsSucess" type="{http://www.w3.org/2001/XMLSchema}boolean" minOccurs="0"/>
 *         &lt;element name="SecurityKey" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *       &lt;/sequence>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "AuthToken", propOrder = {
    "information",
    "isSucess",
    "securityKey"
})
public class AuthToken {

    @XmlElementRef(name = "Information", namespace = "http://ICRA.SOAP.DataProdiver/DataContracts/", type = JAXBElement.class)
    protected JAXBElement<String> information;
    @XmlElement(name = "IsSucess")
    protected Boolean isSucess;
    @XmlElementRef(name = "SecurityKey", namespace = "http://ICRA.SOAP.DataProdiver/DataContracts/", type = JAXBElement.class)
    protected JAXBElement<String> securityKey;

    /**
     * Gets the value of the information property.
     * 
     * @return
     *     possible object is
     *     {@link JAXBElement }{@code <}{@link String }{@code >}
     *     
     */
    public JAXBElement<String> getInformation() {
        return information;
    }

    /**
     * Sets the value of the information property.
     * 
     * @param value
     *     allowed object is
     *     {@link JAXBElement }{@code <}{@link String }{@code >}
     *     
     */
    public void setInformation(JAXBElement<String> value) {
        this.information = ((JAXBElement<String> ) value);
    }

    /**
     * Gets the value of the isSucess property.
     * 
     * @return
     *     possible object is
     *     {@link Boolean }
     *     
     */
    public Boolean isIsSucess() {
        return isSucess;
    }

    /**
     * Sets the value of the isSucess property.
     * 
     * @param value
     *     allowed object is
     *     {@link Boolean }
     *     
     */
    public void setIsSucess(Boolean value) {
        this.isSucess = value;
    }

    /**
     * Gets the value of the securityKey property.
     * 
     * @return
     *     possible object is
     *     {@link JAXBElement }{@code <}{@link String }{@code >}
     *     
     */
    public JAXBElement<String> getSecurityKey() {
        return securityKey;
    }

    /**
     * Sets the value of the securityKey property.
     * 
     * @param value
     *     allowed object is
     *     {@link JAXBElement }{@code <}{@link String }{@code >}
     *     
     */
    public void setSecurityKey(JAXBElement<String> value) {
        this.securityKey = ((JAXBElement<String> ) value);
    }

}
