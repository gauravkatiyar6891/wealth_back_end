
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
 *         &lt;element name="UserId" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="Password" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="PassKey" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
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
    "userId",
    "password",
    "passKey"
})
@XmlRootElement(name = "getPassword")
public class GetPassword {

    @XmlElementRef(name = "UserId", namespace = "http://bsestarmf.in/", type = JAXBElement.class)
    protected JAXBElement<String> userId;
    @XmlElementRef(name = "Password", namespace = "http://bsestarmf.in/", type = JAXBElement.class)
    protected JAXBElement<String> password;
    @XmlElementRef(name = "PassKey", namespace = "http://bsestarmf.in/", type = JAXBElement.class)
    protected JAXBElement<String> passKey;

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

}
