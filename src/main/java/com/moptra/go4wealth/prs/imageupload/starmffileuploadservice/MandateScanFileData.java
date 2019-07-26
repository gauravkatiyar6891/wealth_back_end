
package com.moptra.go4wealth.prs.imageupload.starmffileuploadservice;

import javax.xml.bind.JAXBElement;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElementRef;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Java class for MandateScanFileData complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="MandateScanFileData">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element name="ClientCode" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="EncryptedPassword" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="Filler1" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="Filler2" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="Flag" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="ImageName" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="ImageType" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="MandateId" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="MandateType" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="MemberCode" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="pFileBytes" type="{http://www.w3.org/2001/XMLSchema}base64Binary" minOccurs="0"/>
 *       &lt;/sequence>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "MandateScanFileData", propOrder = {
    "clientCode",
    "encryptedPassword",
    "filler1",
    "filler2",
    "flag",
    "imageName",
    "imageType",
    "mandateId",
    "mandateType",
    "memberCode",
    "pFileBytes"
})
public class MandateScanFileData {

    @XmlElementRef(name = "ClientCode", namespace = "http://schemas.datacontract.org/2004/07/StarMFFileUploadService", type = JAXBElement.class)
    protected JAXBElement<String> clientCode;
    @XmlElementRef(name = "EncryptedPassword", namespace = "http://schemas.datacontract.org/2004/07/StarMFFileUploadService", type = JAXBElement.class)
    protected JAXBElement<String> encryptedPassword;
    @XmlElementRef(name = "Filler1", namespace = "http://schemas.datacontract.org/2004/07/StarMFFileUploadService", type = JAXBElement.class)
    protected JAXBElement<String> filler1;
    @XmlElementRef(name = "Filler2", namespace = "http://schemas.datacontract.org/2004/07/StarMFFileUploadService", type = JAXBElement.class)
    protected JAXBElement<String> filler2;
    @XmlElementRef(name = "Flag", namespace = "http://schemas.datacontract.org/2004/07/StarMFFileUploadService", type = JAXBElement.class)
    protected JAXBElement<String> flag;
    @XmlElementRef(name = "ImageName", namespace = "http://schemas.datacontract.org/2004/07/StarMFFileUploadService", type = JAXBElement.class)
    protected JAXBElement<String> imageName;
    @XmlElementRef(name = "ImageType", namespace = "http://schemas.datacontract.org/2004/07/StarMFFileUploadService", type = JAXBElement.class)
    protected JAXBElement<String> imageType;
    @XmlElementRef(name = "MandateId", namespace = "http://schemas.datacontract.org/2004/07/StarMFFileUploadService", type = JAXBElement.class)
    protected JAXBElement<String> mandateId;
    @XmlElementRef(name = "MandateType", namespace = "http://schemas.datacontract.org/2004/07/StarMFFileUploadService", type = JAXBElement.class)
    protected JAXBElement<String> mandateType;
    @XmlElementRef(name = "MemberCode", namespace = "http://schemas.datacontract.org/2004/07/StarMFFileUploadService", type = JAXBElement.class)
    protected JAXBElement<String> memberCode;
    @XmlElementRef(name = "pFileBytes", namespace = "http://schemas.datacontract.org/2004/07/StarMFFileUploadService", type = JAXBElement.class)
    protected JAXBElement<byte[]> pFileBytes;

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
     * Gets the value of the filler1 property.
     * 
     * @return
     *     possible object is
     *     {@link JAXBElement }{@code <}{@link String }{@code >}
     *     
     */
    public JAXBElement<String> getFiller1() {
        return filler1;
    }

    /**
     * Sets the value of the filler1 property.
     * 
     * @param value
     *     allowed object is
     *     {@link JAXBElement }{@code <}{@link String }{@code >}
     *     
     */
    public void setFiller1(JAXBElement<String> value) {
        this.filler1 = ((JAXBElement<String> ) value);
    }

    /**
     * Gets the value of the filler2 property.
     * 
     * @return
     *     possible object is
     *     {@link JAXBElement }{@code <}{@link String }{@code >}
     *     
     */
    public JAXBElement<String> getFiller2() {
        return filler2;
    }

    /**
     * Sets the value of the filler2 property.
     * 
     * @param value
     *     allowed object is
     *     {@link JAXBElement }{@code <}{@link String }{@code >}
     *     
     */
    public void setFiller2(JAXBElement<String> value) {
        this.filler2 = ((JAXBElement<String> ) value);
    }

    /**
     * Gets the value of the flag property.
     * 
     * @return
     *     possible object is
     *     {@link JAXBElement }{@code <}{@link String }{@code >}
     *     
     */
    public JAXBElement<String> getFlag() {
        return flag;
    }

    /**
     * Sets the value of the flag property.
     * 
     * @param value
     *     allowed object is
     *     {@link JAXBElement }{@code <}{@link String }{@code >}
     *     
     */
    public void setFlag(JAXBElement<String> value) {
        this.flag = ((JAXBElement<String> ) value);
    }

    /**
     * Gets the value of the imageName property.
     * 
     * @return
     *     possible object is
     *     {@link JAXBElement }{@code <}{@link String }{@code >}
     *     
     */
    public JAXBElement<String> getImageName() {
        return imageName;
    }

    /**
     * Sets the value of the imageName property.
     * 
     * @param value
     *     allowed object is
     *     {@link JAXBElement }{@code <}{@link String }{@code >}
     *     
     */
    public void setImageName(JAXBElement<String> value) {
        this.imageName = ((JAXBElement<String> ) value);
    }

    /**
     * Gets the value of the imageType property.
     * 
     * @return
     *     possible object is
     *     {@link JAXBElement }{@code <}{@link String }{@code >}
     *     
     */
    public JAXBElement<String> getImageType() {
        return imageType;
    }

    /**
     * Sets the value of the imageType property.
     * 
     * @param value
     *     allowed object is
     *     {@link JAXBElement }{@code <}{@link String }{@code >}
     *     
     */
    public void setImageType(JAXBElement<String> value) {
        this.imageType = ((JAXBElement<String> ) value);
    }

    /**
     * Gets the value of the mandateId property.
     * 
     * @return
     *     possible object is
     *     {@link JAXBElement }{@code <}{@link String }{@code >}
     *     
     */
    public JAXBElement<String> getMandateId() {
        return mandateId;
    }

    /**
     * Sets the value of the mandateId property.
     * 
     * @param value
     *     allowed object is
     *     {@link JAXBElement }{@code <}{@link String }{@code >}
     *     
     */
    public void setMandateId(JAXBElement<String> value) {
        this.mandateId = ((JAXBElement<String> ) value);
    }

    /**
     * Gets the value of the mandateType property.
     * 
     * @return
     *     possible object is
     *     {@link JAXBElement }{@code <}{@link String }{@code >}
     *     
     */
    public JAXBElement<String> getMandateType() {
        return mandateType;
    }

    /**
     * Sets the value of the mandateType property.
     * 
     * @param value
     *     allowed object is
     *     {@link JAXBElement }{@code <}{@link String }{@code >}
     *     
     */
    public void setMandateType(JAXBElement<String> value) {
        this.mandateType = ((JAXBElement<String> ) value);
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
     * Gets the value of the pFileBytes property.
     * 
     * @return
     *     possible object is
     *     {@link JAXBElement }{@code <}{@link byte[]}{@code >}
     *     
     */
    public JAXBElement<byte[]> getPFileBytes() {
        return pFileBytes;
    }

    /**
     * Sets the value of the pFileBytes property.
     * 
     * @param value
     *     allowed object is
     *     {@link JAXBElement }{@code <}{@link byte[]}{@code >}
     *     
     */
    public void setPFileBytes(JAXBElement<byte[]> value) {
        this.pFileBytes = ((JAXBElement<byte[]> ) value);
    }

}
