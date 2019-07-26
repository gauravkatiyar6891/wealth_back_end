
package com.moptra.go4wealth.prs.payment.starmfpaymentgatewayservice;

import javax.xml.bind.JAXBElement;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElementRef;
import javax.xml.bind.annotation.XmlType;

import com.moptra.go4wealth.prs.payment.arrays.ArrayOfstring;


/**
 * <p>Java class for RequestParam complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="RequestParam">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element name="AccNo" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="BankID" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="ClientCode" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="EncryptedPassword" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="IFSC" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="LogOutURL" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="MemberCode" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="Mode" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="Orders" type="{http://schemas.microsoft.com/2003/10/Serialization/Arrays}ArrayOfstring" minOccurs="0"/>
 *         &lt;element name="TotalAmount" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *       &lt;/sequence>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "RequestParam", propOrder = {
    "accNo",
    "bankID",
    "clientCode",
    "encryptedPassword",
    "ifsc",
    "logOutURL",
    "memberCode",
    "mode",
    "orders",
    "totalAmount"
})
public class RequestParam {

    @XmlElementRef(name = "AccNo", namespace = "http://schemas.datacontract.org/2004/07/StarMFPaymentGatewayService", type = JAXBElement.class)
    protected JAXBElement<String> accNo;
    @XmlElementRef(name = "BankID", namespace = "http://schemas.datacontract.org/2004/07/StarMFPaymentGatewayService", type = JAXBElement.class)
    protected JAXBElement<String> bankID;
    @XmlElementRef(name = "ClientCode", namespace = "http://schemas.datacontract.org/2004/07/StarMFPaymentGatewayService", type = JAXBElement.class)
    protected JAXBElement<String> clientCode;
    @XmlElementRef(name = "EncryptedPassword", namespace = "http://schemas.datacontract.org/2004/07/StarMFPaymentGatewayService", type = JAXBElement.class)
    protected JAXBElement<String> encryptedPassword;
    @XmlElementRef(name = "IFSC", namespace = "http://schemas.datacontract.org/2004/07/StarMFPaymentGatewayService", type = JAXBElement.class)
    protected JAXBElement<String> ifsc;
    @XmlElementRef(name = "LogOutURL", namespace = "http://schemas.datacontract.org/2004/07/StarMFPaymentGatewayService", type = JAXBElement.class)
    protected JAXBElement<String> logOutURL;
    @XmlElementRef(name = "MemberCode", namespace = "http://schemas.datacontract.org/2004/07/StarMFPaymentGatewayService", type = JAXBElement.class)
    protected JAXBElement<String> memberCode;
    @XmlElementRef(name = "Mode", namespace = "http://schemas.datacontract.org/2004/07/StarMFPaymentGatewayService", type = JAXBElement.class)
    protected JAXBElement<String> mode;
    @XmlElementRef(name = "Orders", namespace = "http://schemas.datacontract.org/2004/07/StarMFPaymentGatewayService", type = JAXBElement.class)
    protected JAXBElement<ArrayOfstring> orders;
    @XmlElementRef(name = "TotalAmount", namespace = "http://schemas.datacontract.org/2004/07/StarMFPaymentGatewayService", type = JAXBElement.class)
    protected JAXBElement<String> totalAmount;

    /**
     * Gets the value of the accNo property.
     * 
     * @return
     *     possible object is
     *     {@link JAXBElement }{@code <}{@link String }{@code >}
     *     
     */
    public JAXBElement<String> getAccNo() {
        return accNo;
    }

    /**
     * Sets the value of the accNo property.
     * 
     * @param value
     *     allowed object is
     *     {@link JAXBElement }{@code <}{@link String }{@code >}
     *     
     */
    public void setAccNo(JAXBElement<String> value) {
        this.accNo = ((JAXBElement<String> ) value);
    }

    /**
     * Gets the value of the bankID property.
     * 
     * @return
     *     possible object is
     *     {@link JAXBElement }{@code <}{@link String }{@code >}
     *     
     */
    public JAXBElement<String> getBankID() {
        return bankID;
    }

    /**
     * Sets the value of the bankID property.
     * 
     * @param value
     *     allowed object is
     *     {@link JAXBElement }{@code <}{@link String }{@code >}
     *     
     */
    public void setBankID(JAXBElement<String> value) {
        this.bankID = ((JAXBElement<String> ) value);
    }

    /**
     * Gets the value of the clientCode property.
     * 
     * @return
     *     possible object is
     *     {@link JAXBElement }{@code <}{@link String }{@code >}
     *     
     */
    public JAXBElement<String> getClientCode() {
        return clientCode;
    }

    /**
     * Sets the value of the clientCode property.
     * 
     * @param value
     *     allowed object is
     *     {@link JAXBElement }{@code <}{@link String }{@code >}
     *     
     */
    public void setClientCode(JAXBElement<String> value) {
        this.clientCode = ((JAXBElement<String> ) value);
    }

    /**
     * Gets the value of the encryptedPassword property.
     * 
     * @return
     *     possible object is
     *     {@link JAXBElement }{@code <}{@link String }{@code >}
     *     
     */
    public JAXBElement<String> getEncryptedPassword() {
        return encryptedPassword;
    }

    /**
     * Sets the value of the encryptedPassword property.
     * 
     * @param value
     *     allowed object is
     *     {@link JAXBElement }{@code <}{@link String }{@code >}
     *     
     */
    public void setEncryptedPassword(JAXBElement<String> value) {
        this.encryptedPassword = ((JAXBElement<String> ) value);
    }

    /**
     * Gets the value of the ifsc property.
     * 
     * @return
     *     possible object is
     *     {@link JAXBElement }{@code <}{@link String }{@code >}
     *     
     */
    public JAXBElement<String> getIFSC() {
        return ifsc;
    }

    /**
     * Sets the value of the ifsc property.
     * 
     * @param value
     *     allowed object is
     *     {@link JAXBElement }{@code <}{@link String }{@code >}
     *     
     */
    public void setIFSC(JAXBElement<String> value) {
        this.ifsc = ((JAXBElement<String> ) value);
    }

    /**
     * Gets the value of the logOutURL property.
     * 
     * @return
     *     possible object is
     *     {@link JAXBElement }{@code <}{@link String }{@code >}
     *     
     */
    public JAXBElement<String> getLogOutURL() {
        return logOutURL;
    }

    /**
     * Sets the value of the logOutURL property.
     * 
     * @param value
     *     allowed object is
     *     {@link JAXBElement }{@code <}{@link String }{@code >}
     *     
     */
    public void setLogOutURL(JAXBElement<String> value) {
        this.logOutURL = ((JAXBElement<String> ) value);
    }

    /**
     * Gets the value of the memberCode property.
     * 
     * @return
     *     possible object is
     *     {@link JAXBElement }{@code <}{@link String }{@code >}
     *     
     */
    public JAXBElement<String> getMemberCode() {
        return memberCode;
    }

    /**
     * Sets the value of the memberCode property.
     * 
     * @param value
     *     allowed object is
     *     {@link JAXBElement }{@code <}{@link String }{@code >}
     *     
     */
    public void setMemberCode(JAXBElement<String> value) {
        this.memberCode = ((JAXBElement<String> ) value);
    }

    /**
     * Gets the value of the mode property.
     * 
     * @return
     *     possible object is
     *     {@link JAXBElement }{@code <}{@link String }{@code >}
     *     
     */
    public JAXBElement<String> getMode() {
        return mode;
    }

    /**
     * Sets the value of the mode property.
     * 
     * @param value
     *     allowed object is
     *     {@link JAXBElement }{@code <}{@link String }{@code >}
     *     
     */
    public void setMode(JAXBElement<String> value) {
        this.mode = ((JAXBElement<String> ) value);
    }

    /**
     * Gets the value of the orders property.
     * 
     * @return
     *     possible object is
     *     {@link JAXBElement }{@code <}{@link ArrayOfstring }{@code >}
     *     
     */
    public JAXBElement<ArrayOfstring> getOrders() {
        return orders;
    }

    /**
     * Sets the value of the orders property.
     * 
     * @param value
     *     allowed object is
     *     {@link JAXBElement }{@code <}{@link ArrayOfstring }{@code >}
     *     
     */
    public void setOrders(JAXBElement<ArrayOfstring> value) {
        this.orders = ((JAXBElement<ArrayOfstring> ) value);
    }

    /**
     * Gets the value of the totalAmount property.
     * 
     * @return
     *     possible object is
     *     {@link JAXBElement }{@code <}{@link String }{@code >}
     *     
     */
    public JAXBElement<String> getTotalAmount() {
        return totalAmount;
    }

    /**
     * Sets the value of the totalAmount property.
     * 
     * @param value
     *     allowed object is
     *     {@link JAXBElement }{@code <}{@link String }{@code >}
     *     
     */
    public void setTotalAmount(JAXBElement<String> value) {
        this.totalAmount = ((JAXBElement<String> ) value);
    }

}
