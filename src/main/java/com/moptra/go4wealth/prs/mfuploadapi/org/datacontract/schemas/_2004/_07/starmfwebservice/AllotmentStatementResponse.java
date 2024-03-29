
package com.moptra.go4wealth.prs.mfuploadapi.org.datacontract.schemas._2004._07.starmfwebservice;

import javax.xml.bind.JAXBElement;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElementRef;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Java class for AllotmentStatementResponse complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="AllotmentStatementResponse">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element name="AllotmentDetails" type="{http://schemas.datacontract.org/2004/07/StarMFWebService}ArrayOfAllotmentDetails" minOccurs="0"/>
 *         &lt;element name="Message" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="Status" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *       &lt;/sequence>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "AllotmentStatementResponse", propOrder = {
    "allotmentDetails",
    "message",
    "status"
})
public class AllotmentStatementResponse {

    @XmlElementRef(name = "AllotmentDetails", namespace = "http://schemas.datacontract.org/2004/07/StarMFWebService", type = JAXBElement.class)
    protected JAXBElement<ArrayOfAllotmentDetails> allotmentDetails;
    @XmlElementRef(name = "Message", namespace = "http://schemas.datacontract.org/2004/07/StarMFWebService", type = JAXBElement.class)
    protected JAXBElement<String> message;
    @XmlElementRef(name = "Status", namespace = "http://schemas.datacontract.org/2004/07/StarMFWebService", type = JAXBElement.class)
    protected JAXBElement<String> status;

    /**
     * Gets the value of the allotmentDetails property.
     * 
     * @return
     *     possible object is
     *     {@link JAXBElement }{@code <}{@link ArrayOfAllotmentDetails }{@code >}
     *     
     */
    public JAXBElement<ArrayOfAllotmentDetails> getAllotmentDetails() {
        return allotmentDetails;
    }

    /**
     * Sets the value of the allotmentDetails property.
     * 
     * @param value
     *     allowed object is
     *     {@link JAXBElement }{@code <}{@link ArrayOfAllotmentDetails }{@code >}
     *     
     */
    public void setAllotmentDetails(JAXBElement<ArrayOfAllotmentDetails> value) {
        this.allotmentDetails = ((JAXBElement<ArrayOfAllotmentDetails> ) value);
    }

    /**
     * Gets the value of the message property.
     * 
     * @return
     *     possible object is
     *     {@link JAXBElement }{@code <}{@link String }{@code >}
     *     
     */
    public JAXBElement<String> getMessage() {
        return message;
    }

    /**
     * Sets the value of the message property.
     * 
     * @param value
     *     allowed object is
     *     {@link JAXBElement }{@code <}{@link String }{@code >}
     *     
     */
    public void setMessage(JAXBElement<String> value) {
        this.message = ((JAXBElement<String> ) value);
    }

    /**
     * Gets the value of the status property.
     * 
     * @return
     *     possible object is
     *     {@link JAXBElement }{@code <}{@link String }{@code >}
     *     
     */
    public JAXBElement<String> getStatus() {
        return status;
    }

    /**
     * Sets the value of the status property.
     * 
     * @param value
     *     allowed object is
     *     {@link JAXBElement }{@code <}{@link String }{@code >}
     *     
     */
    public void setStatus(JAXBElement<String> value) {
        this.status = ((JAXBElement<String> ) value);
    }

}
