package com.moptra.go4wealth.prs.payment.tempuri;

import javax.jws.WebMethod;
import javax.jws.WebParam;
import javax.jws.WebResult;
import javax.jws.WebService;
import javax.xml.bind.annotation.XmlSeeAlso;
import javax.xml.ws.Action;
import javax.xml.ws.RequestWrapper;
import javax.xml.ws.ResponseWrapper;

/**
 * This class was generated by Apache CXF 2.7.5
 * 2018-05-31T13:58:30.818+05:30
 * Generated source version: 2.7.5
 * 
 */
@WebService(targetNamespace = "http://tempuri.org/", name = "IStarMFPaymentGatewayService")
@XmlSeeAlso({com.moptra.go4wealth.prs.payment.starmfpaymentgatewayservice.ObjectFactory.class, com.moptra.go4wealth.prs.payment.ObjectFactory.class, ObjectFactory.class, com.moptra.go4wealth.prs.payment.arrays.ObjectFactory.class})
public interface IStarMFPaymentGatewayService {

    @WebMethod(operationName = "PaymentGatewayAPI", action = "http://tempuri.org/IStarMFPaymentGatewayService/PaymentGatewayAPI")
    @Action(input = "http://tempuri.org/IStarMFPaymentGatewayService/PaymentGatewayAPI", output = "http://tempuri.org/IStarMFPaymentGatewayService/PaymentGatewayAPIResponse")
    @RequestWrapper(localName = "PaymentGatewayAPI", targetNamespace = "http://tempuri.org/", className = "org.tempuri.PaymentGatewayAPI")
    @ResponseWrapper(localName = "PaymentGatewayAPIResponse", targetNamespace = "http://tempuri.org/", className = "org.tempuri.PaymentGatewayAPIResponse")
    @WebResult(name = "PaymentGatewayAPIResult", targetNamespace = "http://tempuri.org/")
    public com.moptra.go4wealth.prs.payment.starmfpaymentgatewayservice.Response paymentGatewayAPI(
        @WebParam(name = "Param", targetNamespace = "http://tempuri.org/")
        com.moptra.go4wealth.prs.payment.starmfpaymentgatewayservice.RequestParam param
    );

    @WebMethod(operationName = "GetPassword", action = "http://tempuri.org/IStarMFPaymentGatewayService/GetPassword")
    @Action(input = "http://tempuri.org/IStarMFPaymentGatewayService/GetPassword", output = "http://tempuri.org/IStarMFPaymentGatewayService/GetPasswordResponse")
    @RequestWrapper(localName = "GetPassword", targetNamespace = "http://tempuri.org/", className = "org.tempuri.GetPassword")
    @ResponseWrapper(localName = "GetPasswordResponse", targetNamespace = "http://tempuri.org/", className = "org.tempuri.GetPasswordResponse")
    @WebResult(name = "GetPasswordResult", targetNamespace = "http://tempuri.org/")
    public com.moptra.go4wealth.prs.payment.starmfpaymentgatewayservice.Response getPassword(
        @WebParam(name = "Param", targetNamespace = "http://tempuri.org/")
        com.moptra.go4wealth.prs.payment.starmfpaymentgatewayservice.PasswordRequest param
    );
}
