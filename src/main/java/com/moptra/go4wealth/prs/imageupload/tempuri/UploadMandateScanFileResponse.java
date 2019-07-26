
package com.moptra.go4wealth.prs.imageupload.tempuri;

import javax.xml.bind.JAXBElement;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElementRef;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlType;

import com.moptra.go4wealth.prs.imageupload.starmffileuploadservice.Response;



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
 *         &lt;element name="UploadMandateScanFileResult" type="{http://schemas.datacontract.org/2004/07/StarMFFileUploadService}Response" minOccurs="0"/>
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
    "uploadMandateScanFileResult"
})
@XmlRootElement(name = "UploadMandateScanFileResponse")
public class UploadMandateScanFileResponse {

    @XmlElementRef(name = "UploadMandateScanFileResult", namespace = "http://tempuri.org/", type = JAXBElement.class)
    protected JAXBElement<Response> uploadMandateScanFileResult;

    /**
     * Gets the value of the uploadMandateScanFileResult property.
     * 
     * @return
     *     possible object is
     *     {@link JAXBElement }{@code <}{@link Response }{@code >}
     *     
     */
    public JAXBElement<Response> getUploadMandateScanFileResult() {
        return uploadMandateScanFileResult;
    }

    /**
     * Sets the value of the uploadMandateScanFileResult property.
     * 
     * @param value
     *     allowed object is
     *     {@link JAXBElement }{@code <}{@link Response }{@code >}
     *     
     */
    public void setUploadMandateScanFileResult(JAXBElement<Response> value) {
        this.uploadMandateScanFileResult = ((JAXBElement<Response> ) value);
    }

}
