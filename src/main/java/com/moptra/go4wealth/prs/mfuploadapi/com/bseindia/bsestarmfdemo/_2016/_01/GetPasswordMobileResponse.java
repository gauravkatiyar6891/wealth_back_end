
package com.moptra.go4wealth.prs.mfuploadapi.com.bseindia.bsestarmfdemo._2016._01;

import javax.xml.bind.JAXBElement;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElementRef;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlType;

import com.moptra.go4wealth.prs.mfuploadapi.org.datacontract.schemas._2004._07.starmfwebservice.Response;


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
 *         &lt;element name="GetPasswordMobileResult" type="{http://schemas.datacontract.org/2004/07/StarMFWebService}Response" minOccurs="0"/>
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
    "getPasswordMobileResult"
})
@XmlRootElement(name = "GetPasswordMobileResponse")
public class GetPasswordMobileResponse {

    @XmlElementRef(name = "GetPasswordMobileResult", namespace = "http://www.bsestarmf.in/2016/01/", type = JAXBElement.class)
    protected JAXBElement<Response> getPasswordMobileResult;

    /**
     * Gets the value of the getPasswordMobileResult property.
     * 
     * @return
     *     possible object is
     *     {@link JAXBElement }{@code <}{@link Response }{@code >}
     *     
     */
    public JAXBElement<Response> getGetPasswordMobileResult() {
        return getPasswordMobileResult;
    }

    /**
     * Sets the value of the getPasswordMobileResult property.
     * 
     * @param value
     *     allowed object is
     *     {@link JAXBElement }{@code <}{@link Response }{@code >}
     *     
     */
    public void setGetPasswordMobileResult(JAXBElement<Response> value) {
        this.getPasswordMobileResult = ((JAXBElement<Response> ) value);
    }

}
