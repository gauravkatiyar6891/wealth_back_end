
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
 *         &lt;element name="UploadFileResult" type="{http://schemas.datacontract.org/2004/07/StarMFFileUploadService}Response" minOccurs="0"/>
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
    "uploadFileResult"
})
@XmlRootElement(name = "UploadFileResponse")
public class UploadFileResponse {

    @XmlElementRef(name = "UploadFileResult", namespace = "http://tempuri.org/", type = JAXBElement.class)
    protected JAXBElement<Response> uploadFileResult;

    /**
     * Gets the value of the uploadFileResult property.
     * 
     * @return
     *     possible object is
     *     {@link JAXBElement }{@code <}{@link Response }{@code >}
     *     
     */
    public JAXBElement<Response> getUploadFileResult() {
        return uploadFileResult;
    }

    /**
     * Sets the value of the uploadFileResult property.
     * 
     * @param value
     *     allowed object is
     *     {@link JAXBElement }{@code <}{@link Response }{@code >}
     *     
     */
    public void setUploadFileResult(JAXBElement<Response> value) {
        this.uploadFileResult = ((JAXBElement<Response> ) value);
    }

}
