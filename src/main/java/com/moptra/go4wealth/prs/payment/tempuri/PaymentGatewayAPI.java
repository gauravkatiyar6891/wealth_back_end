
package com.moptra.go4wealth.prs.payment.tempuri;

import javax.xml.bind.JAXBElement;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElementRef;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlType;

import com.moptra.go4wealth.prs.payment.starmfpaymentgatewayservice.RequestParam;


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
 *         &lt;element name="Param" type="{http://schemas.datacontract.org/2004/07/StarMFPaymentGatewayService}RequestParam" minOccurs="0"/>
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
    "param"
})
@XmlRootElement(name = "PaymentGatewayAPI")
public class PaymentGatewayAPI {

    @XmlElementRef(name = "Param", namespace = "http://tempuri.org/", type = JAXBElement.class)
    protected JAXBElement<RequestParam> param;

    /**
     * Gets the value of the param property.
     * 
     * @return
     *     possible object is
     *     {@link JAXBElement }{@code <}{@link RequestParam }{@code >}
     *     
     */
    public JAXBElement<RequestParam> getParam() {
        return param;
    }

    /**
     * Sets the value of the param property.
     * 
     * @param value
     *     allowed object is
     *     {@link JAXBElement }{@code <}{@link RequestParam }{@code >}
     *     
     */
    public void setParam(JAXBElement<RequestParam> value) {
        this.param = ((JAXBElement<RequestParam> ) value);
    }

}
