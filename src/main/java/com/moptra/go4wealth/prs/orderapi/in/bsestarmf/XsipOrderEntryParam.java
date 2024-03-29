
package com.moptra.go4wealth.prs.orderapi.in.bsestarmf;

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
 *         &lt;element name="TransactionCode" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="UniqueRefNo" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="SchemeCode" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="MemberCode" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="ClientCode" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="UserId" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="InternalRefNo" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="TransMode" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="DpTxnMode" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="StartDate" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="FrequencyType" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="FrequencyAllowed" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="InstallmentAmount" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="NoOfInstallment" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="Remarks" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="FolioNo" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="FirstOrderFlag" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="Brokerage" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="MandateID" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="SubberCode" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="Euin" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="EuinVal" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="DPC" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="XsipRegID" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="IPAdd" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="Password" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="PassKey" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="Param1" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="Param2" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="Param3" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
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
    "transactionCode",
    "uniqueRefNo",
    "schemeCode",
    "memberCode",
    "clientCode",
    "userId",
    "internalRefNo",
    "transMode",
    "dpTxnMode",
    "startDate",
    "frequencyType",
    "frequencyAllowed",
    "installmentAmount",
    "noOfInstallment",
    "remarks",
    "folioNo",
    "firstOrderFlag",
    "brokerage",
    "mandateID",
    "subberCode",
    "euin",
    "euinVal",
    "dpc",
    "xsipRegID",
    "ipAdd",
    "password",
    "passKey",
    "param1",
    "param2",
    "param3"
})
@XmlRootElement(name = "xsipOrderEntryParam")
public class XsipOrderEntryParam {

    @XmlElementRef(name = "TransactionCode", namespace = "http://bsestarmf.in/", type = JAXBElement.class)
    protected JAXBElement<String> transactionCode;
    @XmlElementRef(name = "UniqueRefNo", namespace = "http://bsestarmf.in/", type = JAXBElement.class)
    protected JAXBElement<String> uniqueRefNo;
    @XmlElementRef(name = "SchemeCode", namespace = "http://bsestarmf.in/", type = JAXBElement.class)
    protected JAXBElement<String> schemeCode;
    @XmlElementRef(name = "MemberCode", namespace = "http://bsestarmf.in/", type = JAXBElement.class)
    protected JAXBElement<String> memberCode;
    @XmlElementRef(name = "ClientCode", namespace = "http://bsestarmf.in/", type = JAXBElement.class)
    protected JAXBElement<String> clientCode;
    @XmlElementRef(name = "UserId", namespace = "http://bsestarmf.in/", type = JAXBElement.class)
    protected JAXBElement<String> userId;
    @XmlElementRef(name = "InternalRefNo", namespace = "http://bsestarmf.in/", type = JAXBElement.class)
    protected JAXBElement<String> internalRefNo;
    @XmlElementRef(name = "TransMode", namespace = "http://bsestarmf.in/", type = JAXBElement.class)
    protected JAXBElement<String> transMode;
    @XmlElementRef(name = "DpTxnMode", namespace = "http://bsestarmf.in/", type = JAXBElement.class)
    protected JAXBElement<String> dpTxnMode;
    @XmlElementRef(name = "StartDate", namespace = "http://bsestarmf.in/", type = JAXBElement.class)
    protected JAXBElement<String> startDate;
    @XmlElementRef(name = "FrequencyType", namespace = "http://bsestarmf.in/", type = JAXBElement.class)
    protected JAXBElement<String> frequencyType;
    @XmlElementRef(name = "FrequencyAllowed", namespace = "http://bsestarmf.in/", type = JAXBElement.class)
    protected JAXBElement<String> frequencyAllowed;
    @XmlElementRef(name = "InstallmentAmount", namespace = "http://bsestarmf.in/", type = JAXBElement.class)
    protected JAXBElement<String> installmentAmount;
    @XmlElementRef(name = "NoOfInstallment", namespace = "http://bsestarmf.in/", type = JAXBElement.class)
    protected JAXBElement<String> noOfInstallment;
    @XmlElementRef(name = "Remarks", namespace = "http://bsestarmf.in/", type = JAXBElement.class)
    protected JAXBElement<String> remarks;
    @XmlElementRef(name = "FolioNo", namespace = "http://bsestarmf.in/", type = JAXBElement.class)
    protected JAXBElement<String> folioNo;
    @XmlElementRef(name = "FirstOrderFlag", namespace = "http://bsestarmf.in/", type = JAXBElement.class)
    protected JAXBElement<String> firstOrderFlag;
    @XmlElementRef(name = "Brokerage", namespace = "http://bsestarmf.in/", type = JAXBElement.class)
    protected JAXBElement<String> brokerage;
    @XmlElementRef(name = "MandateID", namespace = "http://bsestarmf.in/", type = JAXBElement.class)
    protected JAXBElement<String> mandateID;
    @XmlElementRef(name = "SubberCode", namespace = "http://bsestarmf.in/", type = JAXBElement.class)
    protected JAXBElement<String> subberCode;
    @XmlElementRef(name = "Euin", namespace = "http://bsestarmf.in/", type = JAXBElement.class)
    protected JAXBElement<String> euin;
    @XmlElementRef(name = "EuinVal", namespace = "http://bsestarmf.in/", type = JAXBElement.class)
    protected JAXBElement<String> euinVal;
    @XmlElementRef(name = "DPC", namespace = "http://bsestarmf.in/", type = JAXBElement.class)
    protected JAXBElement<String> dpc;
    @XmlElementRef(name = "XsipRegID", namespace = "http://bsestarmf.in/", type = JAXBElement.class)
    protected JAXBElement<String> xsipRegID;
    @XmlElementRef(name = "IPAdd", namespace = "http://bsestarmf.in/", type = JAXBElement.class)
    protected JAXBElement<String> ipAdd;
    @XmlElementRef(name = "Password", namespace = "http://bsestarmf.in/", type = JAXBElement.class)
    protected JAXBElement<String> password;
    @XmlElementRef(name = "PassKey", namespace = "http://bsestarmf.in/", type = JAXBElement.class)
    protected JAXBElement<String> passKey;
    @XmlElementRef(name = "Param1", namespace = "http://bsestarmf.in/", type = JAXBElement.class)
    protected JAXBElement<String> param1;
    @XmlElementRef(name = "Param2", namespace = "http://bsestarmf.in/", type = JAXBElement.class)
    protected JAXBElement<String> param2;
    @XmlElementRef(name = "Param3", namespace = "http://bsestarmf.in/", type = JAXBElement.class)
    protected JAXBElement<String> param3;

    /**
     * Gets the value of the transactionCode property.
     * 
     * @return
     *     possible object is
     *     {@link JAXBElement }{@code <}{@link String }{@code >}
     *     
     */
    public JAXBElement<String> getTransactionCode() {
        return transactionCode;
    }

    /**
     * Sets the value of the transactionCode property.
     * 
     * @param value
     *     allowed object is
     *     {@link JAXBElement }{@code <}{@link String }{@code >}
     *     
     */
    public void setTransactionCode(JAXBElement<String> value) {
        this.transactionCode = ((JAXBElement<String> ) value);
    }

    /**
     * Gets the value of the uniqueRefNo property.
     * 
     * @return
     *     possible object is
     *     {@link JAXBElement }{@code <}{@link String }{@code >}
     *     
     */
    public JAXBElement<String> getUniqueRefNo() {
        return uniqueRefNo;
    }

    /**
     * Sets the value of the uniqueRefNo property.
     * 
     * @param value
     *     allowed object is
     *     {@link JAXBElement }{@code <}{@link String }{@code >}
     *     
     */
    public void setUniqueRefNo(JAXBElement<String> value) {
        this.uniqueRefNo = ((JAXBElement<String> ) value);
    }

    /**
     * Gets the value of the schemeCode property.
     * 
     * @return
     *     possible object is
     *     {@link JAXBElement }{@code <}{@link String }{@code >}
     *     
     */
    public JAXBElement<String> getSchemeCode() {
        return schemeCode;
    }

    /**
     * Sets the value of the schemeCode property.
     * 
     * @param value
     *     allowed object is
     *     {@link JAXBElement }{@code <}{@link String }{@code >}
     *     
     */
    public void setSchemeCode(JAXBElement<String> value) {
        this.schemeCode = ((JAXBElement<String> ) value);
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
     * Gets the value of the userId property.
     * 
     * @return
     *     possible object is
     *     {@link JAXBElement }{@code <}{@link String }{@code >}
     *     
     */
    public JAXBElement<String> getUserId() {
        return userId;
    }

    /**
     * Sets the value of the userId property.
     * 
     * @param value
     *     allowed object is
     *     {@link JAXBElement }{@code <}{@link String }{@code >}
     *     
     */
    public void setUserId(JAXBElement<String> value) {
        this.userId = ((JAXBElement<String> ) value);
    }

    /**
     * Gets the value of the internalRefNo property.
     * 
     * @return
     *     possible object is
     *     {@link JAXBElement }{@code <}{@link String }{@code >}
     *     
     */
    public JAXBElement<String> getInternalRefNo() {
        return internalRefNo;
    }

    /**
     * Sets the value of the internalRefNo property.
     * 
     * @param value
     *     allowed object is
     *     {@link JAXBElement }{@code <}{@link String }{@code >}
     *     
     */
    public void setInternalRefNo(JAXBElement<String> value) {
        this.internalRefNo = ((JAXBElement<String> ) value);
    }

    /**
     * Gets the value of the transMode property.
     * 
     * @return
     *     possible object is
     *     {@link JAXBElement }{@code <}{@link String }{@code >}
     *     
     */
    public JAXBElement<String> getTransMode() {
        return transMode;
    }

    /**
     * Sets the value of the transMode property.
     * 
     * @param value
     *     allowed object is
     *     {@link JAXBElement }{@code <}{@link String }{@code >}
     *     
     */
    public void setTransMode(JAXBElement<String> value) {
        this.transMode = ((JAXBElement<String> ) value);
    }

    /**
     * Gets the value of the dpTxnMode property.
     * 
     * @return
     *     possible object is
     *     {@link JAXBElement }{@code <}{@link String }{@code >}
     *     
     */
    public JAXBElement<String> getDpTxnMode() {
        return dpTxnMode;
    }

    /**
     * Sets the value of the dpTxnMode property.
     * 
     * @param value
     *     allowed object is
     *     {@link JAXBElement }{@code <}{@link String }{@code >}
     *     
     */
    public void setDpTxnMode(JAXBElement<String> value) {
        this.dpTxnMode = ((JAXBElement<String> ) value);
    }

    /**
     * Gets the value of the startDate property.
     * 
     * @return
     *     possible object is
     *     {@link JAXBElement }{@code <}{@link String }{@code >}
     *     
     */
    public JAXBElement<String> getStartDate() {
        return startDate;
    }

    /**
     * Sets the value of the startDate property.
     * 
     * @param value
     *     allowed object is
     *     {@link JAXBElement }{@code <}{@link String }{@code >}
     *     
     */
    public void setStartDate(JAXBElement<String> value) {
        this.startDate = ((JAXBElement<String> ) value);
    }

    /**
     * Gets the value of the frequencyType property.
     * 
     * @return
     *     possible object is
     *     {@link JAXBElement }{@code <}{@link String }{@code >}
     *     
     */
    public JAXBElement<String> getFrequencyType() {
        return frequencyType;
    }

    /**
     * Sets the value of the frequencyType property.
     * 
     * @param value
     *     allowed object is
     *     {@link JAXBElement }{@code <}{@link String }{@code >}
     *     
     */
    public void setFrequencyType(JAXBElement<String> value) {
        this.frequencyType = ((JAXBElement<String> ) value);
    }

    /**
     * Gets the value of the frequencyAllowed property.
     * 
     * @return
     *     possible object is
     *     {@link JAXBElement }{@code <}{@link String }{@code >}
     *     
     */
    public JAXBElement<String> getFrequencyAllowed() {
        return frequencyAllowed;
    }

    /**
     * Sets the value of the frequencyAllowed property.
     * 
     * @param value
     *     allowed object is
     *     {@link JAXBElement }{@code <}{@link String }{@code >}
     *     
     */
    public void setFrequencyAllowed(JAXBElement<String> value) {
        this.frequencyAllowed = ((JAXBElement<String> ) value);
    }

    /**
     * Gets the value of the installmentAmount property.
     * 
     * @return
     *     possible object is
     *     {@link JAXBElement }{@code <}{@link String }{@code >}
     *     
     */
    public JAXBElement<String> getInstallmentAmount() {
        return installmentAmount;
    }

    /**
     * Sets the value of the installmentAmount property.
     * 
     * @param value
     *     allowed object is
     *     {@link JAXBElement }{@code <}{@link String }{@code >}
     *     
     */
    public void setInstallmentAmount(JAXBElement<String> value) {
        this.installmentAmount = ((JAXBElement<String> ) value);
    }

    /**
     * Gets the value of the noOfInstallment property.
     * 
     * @return
     *     possible object is
     *     {@link JAXBElement }{@code <}{@link String }{@code >}
     *     
     */
    public JAXBElement<String> getNoOfInstallment() {
        return noOfInstallment;
    }

    /**
     * Sets the value of the noOfInstallment property.
     * 
     * @param value
     *     allowed object is
     *     {@link JAXBElement }{@code <}{@link String }{@code >}
     *     
     */
    public void setNoOfInstallment(JAXBElement<String> value) {
        this.noOfInstallment = ((JAXBElement<String> ) value);
    }

    /**
     * Gets the value of the remarks property.
     * 
     * @return
     *     possible object is
     *     {@link JAXBElement }{@code <}{@link String }{@code >}
     *     
     */
    public JAXBElement<String> getRemarks() {
        return remarks;
    }

    /**
     * Sets the value of the remarks property.
     * 
     * @param value
     *     allowed object is
     *     {@link JAXBElement }{@code <}{@link String }{@code >}
     *     
     */
    public void setRemarks(JAXBElement<String> value) {
        this.remarks = ((JAXBElement<String> ) value);
    }

    /**
     * Gets the value of the folioNo property.
     * 
     * @return
     *     possible object is
     *     {@link JAXBElement }{@code <}{@link String }{@code >}
     *     
     */
    public JAXBElement<String> getFolioNo() {
        return folioNo;
    }

    /**
     * Sets the value of the folioNo property.
     * 
     * @param value
     *     allowed object is
     *     {@link JAXBElement }{@code <}{@link String }{@code >}
     *     
     */
    public void setFolioNo(JAXBElement<String> value) {
        this.folioNo = ((JAXBElement<String> ) value);
    }

    /**
     * Gets the value of the firstOrderFlag property.
     * 
     * @return
     *     possible object is
     *     {@link JAXBElement }{@code <}{@link String }{@code >}
     *     
     */
    public JAXBElement<String> getFirstOrderFlag() {
        return firstOrderFlag;
    }

    /**
     * Sets the value of the firstOrderFlag property.
     * 
     * @param value
     *     allowed object is
     *     {@link JAXBElement }{@code <}{@link String }{@code >}
     *     
     */
    public void setFirstOrderFlag(JAXBElement<String> value) {
        this.firstOrderFlag = ((JAXBElement<String> ) value);
    }

    /**
     * Gets the value of the brokerage property.
     * 
     * @return
     *     possible object is
     *     {@link JAXBElement }{@code <}{@link String }{@code >}
     *     
     */
    public JAXBElement<String> getBrokerage() {
        return brokerage;
    }

    /**
     * Sets the value of the brokerage property.
     * 
     * @param value
     *     allowed object is
     *     {@link JAXBElement }{@code <}{@link String }{@code >}
     *     
     */
    public void setBrokerage(JAXBElement<String> value) {
        this.brokerage = ((JAXBElement<String> ) value);
    }

    /**
     * Gets the value of the mandateID property.
     * 
     * @return
     *     possible object is
     *     {@link JAXBElement }{@code <}{@link String }{@code >}
     *     
     */
    public JAXBElement<String> getMandateID() {
        return mandateID;
    }

    /**
     * Sets the value of the mandateID property.
     * 
     * @param value
     *     allowed object is
     *     {@link JAXBElement }{@code <}{@link String }{@code >}
     *     
     */
    public void setMandateID(JAXBElement<String> value) {
        this.mandateID = ((JAXBElement<String> ) value);
    }

    /**
     * Gets the value of the subberCode property.
     * 
     * @return
     *     possible object is
     *     {@link JAXBElement }{@code <}{@link String }{@code >}
     *     
     */
    public JAXBElement<String> getSubberCode() {
        return subberCode;
    }

    /**
     * Sets the value of the subberCode property.
     * 
     * @param value
     *     allowed object is
     *     {@link JAXBElement }{@code <}{@link String }{@code >}
     *     
     */
    public void setSubberCode(JAXBElement<String> value) {
        this.subberCode = ((JAXBElement<String> ) value);
    }

    /**
     * Gets the value of the euin property.
     * 
     * @return
     *     possible object is
     *     {@link JAXBElement }{@code <}{@link String }{@code >}
     *     
     */
    public JAXBElement<String> getEuin() {
        return euin;
    }

    /**
     * Sets the value of the euin property.
     * 
     * @param value
     *     allowed object is
     *     {@link JAXBElement }{@code <}{@link String }{@code >}
     *     
     */
    public void setEuin(JAXBElement<String> value) {
        this.euin = ((JAXBElement<String> ) value);
    }

    /**
     * Gets the value of the euinVal property.
     * 
     * @return
     *     possible object is
     *     {@link JAXBElement }{@code <}{@link String }{@code >}
     *     
     */
    public JAXBElement<String> getEuinVal() {
        return euinVal;
    }

    /**
     * Sets the value of the euinVal property.
     * 
     * @param value
     *     allowed object is
     *     {@link JAXBElement }{@code <}{@link String }{@code >}
     *     
     */
    public void setEuinVal(JAXBElement<String> value) {
        this.euinVal = ((JAXBElement<String> ) value);
    }

    /**
     * Gets the value of the dpc property.
     * 
     * @return
     *     possible object is
     *     {@link JAXBElement }{@code <}{@link String }{@code >}
     *     
     */
    public JAXBElement<String> getDPC() {
        return dpc;
    }

    /**
     * Sets the value of the dpc property.
     * 
     * @param value
     *     allowed object is
     *     {@link JAXBElement }{@code <}{@link String }{@code >}
     *     
     */
    public void setDPC(JAXBElement<String> value) {
        this.dpc = ((JAXBElement<String> ) value);
    }

    /**
     * Gets the value of the xsipRegID property.
     * 
     * @return
     *     possible object is
     *     {@link JAXBElement }{@code <}{@link String }{@code >}
     *     
     */
    public JAXBElement<String> getXsipRegID() {
        return xsipRegID;
    }

    /**
     * Sets the value of the xsipRegID property.
     * 
     * @param value
     *     allowed object is
     *     {@link JAXBElement }{@code <}{@link String }{@code >}
     *     
     */
    public void setXsipRegID(JAXBElement<String> value) {
        this.xsipRegID = ((JAXBElement<String> ) value);
    }

    /**
     * Gets the value of the ipAdd property.
     * 
     * @return
     *     possible object is
     *     {@link JAXBElement }{@code <}{@link String }{@code >}
     *     
     */
    public JAXBElement<String> getIPAdd() {
        return ipAdd;
    }

    /**
     * Sets the value of the ipAdd property.
     * 
     * @param value
     *     allowed object is
     *     {@link JAXBElement }{@code <}{@link String }{@code >}
     *     
     */
    public void setIPAdd(JAXBElement<String> value) {
        this.ipAdd = ((JAXBElement<String> ) value);
    }

    /**
     * Gets the value of the password property.
     * 
     * @return
     *     possible object is
     *     {@link JAXBElement }{@code <}{@link String }{@code >}
     *     
     */
    public JAXBElement<String> getPassword() {
        return password;
    }

    /**
     * Sets the value of the password property.
     * 
     * @param value
     *     allowed object is
     *     {@link JAXBElement }{@code <}{@link String }{@code >}
     *     
     */
    public void setPassword(JAXBElement<String> value) {
        this.password = ((JAXBElement<String> ) value);
    }

    /**
     * Gets the value of the passKey property.
     * 
     * @return
     *     possible object is
     *     {@link JAXBElement }{@code <}{@link String }{@code >}
     *     
     */
    public JAXBElement<String> getPassKey() {
        return passKey;
    }

    /**
     * Sets the value of the passKey property.
     * 
     * @param value
     *     allowed object is
     *     {@link JAXBElement }{@code <}{@link String }{@code >}
     *     
     */
    public void setPassKey(JAXBElement<String> value) {
        this.passKey = ((JAXBElement<String> ) value);
    }

    /**
     * Gets the value of the param1 property.
     * 
     * @return
     *     possible object is
     *     {@link JAXBElement }{@code <}{@link String }{@code >}
     *     
     */
    public JAXBElement<String> getParam1() {
        return param1;
    }

    /**
     * Sets the value of the param1 property.
     * 
     * @param value
     *     allowed object is
     *     {@link JAXBElement }{@code <}{@link String }{@code >}
     *     
     */
    public void setParam1(JAXBElement<String> value) {
        this.param1 = ((JAXBElement<String> ) value);
    }

    /**
     * Gets the value of the param2 property.
     * 
     * @return
     *     possible object is
     *     {@link JAXBElement }{@code <}{@link String }{@code >}
     *     
     */
    public JAXBElement<String> getParam2() {
        return param2;
    }

    /**
     * Sets the value of the param2 property.
     * 
     * @param value
     *     allowed object is
     *     {@link JAXBElement }{@code <}{@link String }{@code >}
     *     
     */
    public void setParam2(JAXBElement<String> value) {
        this.param2 = ((JAXBElement<String> ) value);
    }

    /**
     * Gets the value of the param3 property.
     * 
     * @return
     *     possible object is
     *     {@link JAXBElement }{@code <}{@link String }{@code >}
     *     
     */
    public JAXBElement<String> getParam3() {
        return param3;
    }

    /**
     * Sets the value of the param3 property.
     * 
     * @param value
     *     allowed object is
     *     {@link JAXBElement }{@code <}{@link String }{@code >}
     *     
     */
    public void setParam3(JAXBElement<String> value) {
        this.param3 = ((JAXBElement<String> ) value);
    }

}
