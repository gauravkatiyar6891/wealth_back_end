
package com.moptra.go4wealth.prs.mfuploadapi.com.bseindia.bsestarmfdemo._2016._01;

import javax.xml.bind.JAXBElement;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElementRef;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlType;


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
 *         &lt;element name="MandateDetailsResult" type="{http://schemas.datacontract.org/2004/07/StarMFWebService}MandateDetailsResponse" minOccurs="0"/>
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
    "mandateDetailsResult"
})
@XmlRootElement(name = "MandateDetailsResponse")
public class MandateDetailsResponse {

    @XmlElementRef(name = "MandateDetailsResult", namespace = "http://www.bsestarmf.in/2016/01/", type = JAXBElement.class)
    protected JAXBElement<com.moptra.go4wealth.prs.mfuploadapi.org.datacontract.schemas._2004._07.starmfwebservice.MandateDetailsResponse> mandateDetailsResult;

    /**
     * Gets the value of the mandateDetailsResult property.
     * 
     * @return
     *     possible object is
     *     {@link JAXBElement }{@code <}{@link org.datacontract.schemas._2004._07.starmfwebservice.MandateDetailsResponse }{@code >}
     *     
     */
    public JAXBElement<com.moptra.go4wealth.prs.mfuploadapi.org.datacontract.schemas._2004._07.starmfwebservice.MandateDetailsResponse> getMandateDetailsResult() {
        return mandateDetailsResult;
    }

    /**
     * Sets the value of the mandateDetailsResult property.
     * 
     * @param value
     *     allowed object is
     *     {@link JAXBElement }{@code <}{@link org.datacontract.schemas._2004._07.starmfwebservice.MandateDetailsResponse }{@code >}
     *     
     */
    public void setMandateDetailsResult(JAXBElement<com.moptra.go4wealth.prs.mfuploadapi.org.datacontract.schemas._2004._07.starmfwebservice.MandateDetailsResponse> value) {
        this.mandateDetailsResult = ((JAXBElement<com.moptra.go4wealth.prs.mfuploadapi.org.datacontract.schemas._2004._07.starmfwebservice.MandateDetailsResponse> ) value);
    }

}
