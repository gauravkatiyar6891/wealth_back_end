
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
 *         &lt;element name="GetAccessTokenResult" type="{http://schemas.datacontract.org/2004/07/StarMFWebService}Response" minOccurs="0"/>
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
    "getAccessTokenResult"
})
@XmlRootElement(name = "GetAccessTokenResponse")
public class GetAccessTokenResponse {

    @XmlElementRef(name = "GetAccessTokenResult", namespace = "http://www.bsestarmf.in/2016/01/", type = JAXBElement.class)
    protected JAXBElement<Response> getAccessTokenResult;

    /**
     * Gets the value of the getAccessTokenResult property.
     * 
     * @return
     *     possible object is
     *     {@link JAXBElement }{@code <}{@link Response }{@code >}
     *     
     */
    public JAXBElement<Response> getGetAccessTokenResult() {
        return getAccessTokenResult;
    }

    /**
     * Sets the value of the getAccessTokenResult property.
     * 
     * @param value
     *     allowed object is
     *     {@link JAXBElement }{@code <}{@link Response }{@code >}
     *     
     */
    public void setGetAccessTokenResult(JAXBElement<Response> value) {
        this.getAccessTokenResult = ((JAXBElement<Response> ) value);
    }

}
