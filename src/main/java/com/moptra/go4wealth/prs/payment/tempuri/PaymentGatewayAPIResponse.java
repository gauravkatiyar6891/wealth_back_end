
package com.moptra.go4wealth.prs.payment.tempuri;

import javax.xml.bind.JAXBElement;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElementRef;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlType;

import com.moptra.go4wealth.prs.payment.starmfpaymentgatewayservice.Response;


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
 *         &lt;element name="PaymentGatewayAPIResult" type="{http://schemas.datacontract.org/2004/07/StarMFPaymentGatewayService}Response" minOccurs="0"/>
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
    "paymentGatewayAPIResult"
})
@XmlRootElement(name = "PaymentGatewayAPIResponse")
public class PaymentGatewayAPIResponse {

    @XmlElementRef(name = "PaymentGatewayAPIResult", namespace = "http://tempuri.org/", type = JAXBElement.class)
    protected JAXBElement<Response> paymentGatewayAPIResult;

    /**
     * Gets the value of the paymentGatewayAPIResult property.
     * 
     * @return
     *     possible object is
     *     {@link JAXBElement }{@code <}{@link Response }{@code >}
     *     
     */
    public JAXBElement<Response> getPaymentGatewayAPIResult() {
        return paymentGatewayAPIResult;
    }

    /**
     * Sets the value of the paymentGatewayAPIResult property.
     * 
     * @param value
     *     allowed object is
     *     {@link JAXBElement }{@code <}{@link Response }{@code >}
     *     
     */
    public void setPaymentGatewayAPIResult(JAXBElement<Response> value) {
        this.paymentGatewayAPIResult = ((JAXBElement<Response> ) value);
    }

}
