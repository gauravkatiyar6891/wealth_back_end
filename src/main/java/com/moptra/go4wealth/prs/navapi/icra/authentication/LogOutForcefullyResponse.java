
package com.moptra.go4wealth.prs.navapi.icra.authentication;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
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
 *         &lt;element name="LogOutForcefullyResult" type="{http://www.w3.org/2001/XMLSchema}boolean" minOccurs="0"/>
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
    "logOutForcefullyResult"
})
@XmlRootElement(name = "LogOutForcefullyResponse")
public class LogOutForcefullyResponse {

    @XmlElement(name = "LogOutForcefullyResult")
    protected Boolean logOutForcefullyResult;

    /**
     * Gets the value of the logOutForcefullyResult property.
     * 
     * @return
     *     possible object is
     *     {@link Boolean }
     *     
     */
    public Boolean isLogOutForcefullyResult() {
        return logOutForcefullyResult;
    }

    /**
     * Sets the value of the logOutForcefullyResult property.
     * 
     * @param value
     *     allowed object is
     *     {@link Boolean }
     *     
     */
    public void setLogOutForcefullyResult(Boolean value) {
        this.logOutForcefullyResult = value;
    }

}
