
package com.moptra.go4wealth.prs.mfuploadapi.com.bseindia.bsestarmfdemo._2016._01;

import javax.xml.bind.JAXBElement;
import javax.xml.bind.annotation.XmlElementDecl;
import javax.xml.bind.annotation.XmlRegistry;
import javax.xml.namespace.QName;
import com.moptra.go4wealth.prs.mfuploadapi.org.datacontract.schemas._2004._07.starmfwebservice.AllotmentStatementRequest;
import com.moptra.go4wealth.prs.mfuploadapi.org.datacontract.schemas._2004._07.starmfwebservice.ChildOrderRequest;
import com.moptra.go4wealth.prs.mfuploadapi.org.datacontract.schemas._2004._07.starmfwebservice.ChildOrderResponse;
import com.moptra.go4wealth.prs.mfuploadapi.org.datacontract.schemas._2004._07.starmfwebservice.EMandateAuthURLRequest;
import com.moptra.go4wealth.prs.mfuploadapi.org.datacontract.schemas._2004._07.starmfwebservice.MFAPIRequest;
import com.moptra.go4wealth.prs.mfuploadapi.org.datacontract.schemas._2004._07.starmfwebservice.MandateDetailsRequest;
import com.moptra.go4wealth.prs.mfuploadapi.org.datacontract.schemas._2004._07.starmfwebservice.OrderRequest;
import com.moptra.go4wealth.prs.mfuploadapi.org.datacontract.schemas._2004._07.starmfwebservice.OrderResponse;
import com.moptra.go4wealth.prs.mfuploadapi.org.datacontract.schemas._2004._07.starmfwebservice.PasswordRequest;
import com.moptra.go4wealth.prs.mfuploadapi.org.datacontract.schemas._2004._07.starmfwebservice.ProvOrderResponse;
import com.moptra.go4wealth.prs.mfuploadapi.org.datacontract.schemas._2004._07.starmfwebservice.RedemptionStatementRequest;
import com.moptra.go4wealth.prs.mfuploadapi.org.datacontract.schemas._2004._07.starmfwebservice.Response;


/**
 * This object contains factory methods for each 
 * Java content interface and Java element interface 
 * generated in the in.bsestarmf._2016._01 package. 
 * <p>An ObjectFactory allows you to programatically 
 * construct new instances of the Java representation 
 * for XML content. The Java representation of XML 
 * content can consist of schema derived interfaces 
 * and classes representing the binding of schema 
 * type definitions, element declarations and model 
 * groups.  Factory methods for each of these are 
 * provided in this class.
 * 
 */
@XmlRegistry
public class ObjectFactory {

    private final static QName _MFAPIMobileParam_QNAME = new QName("http://www.bsestarmf.in/2016/01/", "Param");
    private final static QName _GetPasswordMobileResponseGetPasswordMobileResult_QNAME = new QName("http://www.bsestarmf.in/2016/01/", "GetPasswordMobileResult");
    private final static QName _RedemptionStatementResponseRedemptionStatementResult_QNAME = new QName("http://www.bsestarmf.in/2016/01/", "RedemptionStatementResult");
    private final static QName _OrderStatusResponseOrderStatusResult_QNAME = new QName("http://www.bsestarmf.in/2016/01/", "OrderStatusResult");
    private final static QName _GetPasswordForChildOrderResponseGetPasswordForChildOrderResult_QNAME = new QName("http://www.bsestarmf.in/2016/01/", "GetPasswordForChildOrderResult");
    private final static QName _MandateDetailsResponseMandateDetailsResult_QNAME = new QName("http://www.bsestarmf.in/2016/01/", "MandateDetailsResult");
    private final static QName _MFAPIMobileResponseMFAPIMobileResult_QNAME = new QName("http://www.bsestarmf.in/2016/01/", "MFAPIMobileResult");
    private final static QName _EMandateAuthURLResponseEMandateAuthURLResult_QNAME = new QName("http://www.bsestarmf.in/2016/01/", "EMandateAuthURLResult");
    private final static QName _GetPasswordUserId_QNAME = new QName("http://www.bsestarmf.in/2016/01/", "UserId");
    private final static QName _GetPasswordMemberId_QNAME = new QName("http://www.bsestarmf.in/2016/01/", "MemberId");
    private final static QName _GetPasswordPassKey_QNAME = new QName("http://www.bsestarmf.in/2016/01/", "PassKey");
    private final static QName _GetPasswordPassword_QNAME = new QName("http://www.bsestarmf.in/2016/01/", "Password");
    private final static QName _GetPasswordResponseGetPasswordResult_QNAME = new QName("http://www.bsestarmf.in/2016/01/", "getPasswordResult");
    private final static QName _ChildOrderDetailsResponseChildOrderDetailsResult_QNAME = new QName("http://www.bsestarmf.in/2016/01/", "ChildOrderDetailsResult");
    private final static QName _GetAccessTokenResponseGetAccessTokenResult_QNAME = new QName("http://www.bsestarmf.in/2016/01/", "GetAccessTokenResult");
    private final static QName _AllotmentStatementResponseAllotmentStatementResult_QNAME = new QName("http://www.bsestarmf.in/2016/01/", "AllotmentStatementResult");
    private final static QName _MFAPIParam_QNAME = new QName("http://www.bsestarmf.in/2016/01/", "param");
    private final static QName _MFAPIFlag_QNAME = new QName("http://www.bsestarmf.in/2016/01/", "Flag");
    private final static QName _MFAPIEncryptedPassword_QNAME = new QName("http://www.bsestarmf.in/2016/01/", "EncryptedPassword");
    private final static QName _MFAPIResponseMFAPIResult_QNAME = new QName("http://www.bsestarmf.in/2016/01/", "MFAPIResult");
    private final static QName _ProvOrderStatusResponseProvOrderStatusResult_QNAME = new QName("http://www.bsestarmf.in/2016/01/", "ProvOrderStatusResult");

    /**
     * Create a new ObjectFactory that can be used to create new instances of schema derived classes for package: in.bsestarmf._2016._01
     * 
     */
    public ObjectFactory() {
    }

    /**
     * Create an instance of {@link MFAPIMobile }
     * 
     */
    public MFAPIMobile createMFAPIMobile() {
        return new MFAPIMobile();
    }

    /**
     * Create an instance of {@link GetPasswordMobileResponse }
     * 
     */
    public GetPasswordMobileResponse createGetPasswordMobileResponse() {
        return new GetPasswordMobileResponse();
    }

    /**
     * Create an instance of {@link in.bsestarmf._2016._01.RedemptionStatementResponse }
     * 
     */
    public com.moptra.go4wealth.prs.mfuploadapi.com.bseindia.bsestarmfdemo._2016._01.RedemptionStatementResponse createRedemptionStatementResponse() {
        return new com.moptra.go4wealth.prs.mfuploadapi.com.bseindia.bsestarmfdemo._2016._01.RedemptionStatementResponse();
    }

    /**
     * Create an instance of {@link OrderStatusResponse }
     * 
     */
    public OrderStatusResponse createOrderStatusResponse() {
        return new OrderStatusResponse();
    }

    /**
     * Create an instance of {@link GetPasswordForChildOrderResponse }
     * 
     */
    public GetPasswordForChildOrderResponse createGetPasswordForChildOrderResponse() {
        return new GetPasswordForChildOrderResponse();
    }

    /**
     * Create an instance of {@link in.bsestarmf._2016._01.MandateDetailsResponse }
     * 
     */
    public com.moptra.go4wealth.prs.mfuploadapi.com.bseindia.bsestarmfdemo._2016._01.MandateDetailsResponse createMandateDetailsResponse() {
        return new com.moptra.go4wealth.prs.mfuploadapi.com.bseindia.bsestarmfdemo._2016._01.MandateDetailsResponse();
    }

    /**
     * Create an instance of {@link ProvOrderStatus }
     * 
     */
    public ProvOrderStatus createProvOrderStatus() {
        return new ProvOrderStatus();
    }

    /**
     * Create an instance of {@link MFAPIMobileResponse }
     * 
     */
    public MFAPIMobileResponse createMFAPIMobileResponse() {
        return new MFAPIMobileResponse();
    }

    /**
     * Create an instance of {@link RedemptionStatement }
     * 
     */
    public RedemptionStatement createRedemptionStatement() {
        return new RedemptionStatement();
    }

    /**
     * Create an instance of {@link ChildOrderDetails }
     * 
     */
    public ChildOrderDetails createChildOrderDetails() {
        return new ChildOrderDetails();
    }

    /**
     * Create an instance of {@link in.bsestarmf._2016._01.EMandateAuthURLResponse }
     * 
     */
    public com.moptra.go4wealth.prs.mfuploadapi.com.bseindia.bsestarmfdemo._2016._01.EMandateAuthURLResponse createEMandateAuthURLResponse() {
        return new com.moptra.go4wealth.prs.mfuploadapi.com.bseindia.bsestarmfdemo._2016._01.EMandateAuthURLResponse();
    }

    /**
     * Create an instance of {@link GetPassword }
     * 
     */
    public GetPassword createGetPassword() {
        return new GetPassword();
    }

    /**
     * Create an instance of {@link GetPasswordResponse }
     * 
     */
    public GetPasswordResponse createGetPasswordResponse() {
        return new GetPasswordResponse();
    }

    /**
     * Create an instance of {@link ChildOrderDetailsResponse }
     * 
     */
    public ChildOrderDetailsResponse createChildOrderDetailsResponse() {
        return new ChildOrderDetailsResponse();
    }

    /**
     * Create an instance of {@link EMandateAuthURL }
     * 
     */
    public EMandateAuthURL createEMandateAuthURL() {
        return new EMandateAuthURL();
    }

    /**
     * Create an instance of {@link GetPasswordMobile }
     * 
     */
    public GetPasswordMobile createGetPasswordMobile() {
        return new GetPasswordMobile();
    }

    /**
     * Create an instance of {@link GetAccessTokenResponse }
     * 
     */
    public GetAccessTokenResponse createGetAccessTokenResponse() {
        return new GetAccessTokenResponse();
    }

    /**
     * Create an instance of {@link OrderStatus }
     * 
     */
    public OrderStatus createOrderStatus() {
        return new OrderStatus();
    }

    /**
     * Create an instance of {@link in.bsestarmf._2016._01.AllotmentStatementResponse }
     * 
     */
    public com.moptra.go4wealth.prs.mfuploadapi.com.bseindia.bsestarmfdemo._2016._01.AllotmentStatementResponse createAllotmentStatementResponse() {
        return new com.moptra.go4wealth.prs.mfuploadapi.com.bseindia.bsestarmfdemo._2016._01.AllotmentStatementResponse();
    }

    /**
     * Create an instance of {@link GetAccessToken }
     * 
     */
    public GetAccessToken createGetAccessToken() {
        return new GetAccessToken();
    }

    /**
     * Create an instance of {@link AllotmentStatement }
     * 
     */
    public AllotmentStatement createAllotmentStatement() {
        return new AllotmentStatement();
    }

    /**
     * Create an instance of {@link MandateDetails }
     * 
     */
    public MandateDetails createMandateDetails() {
        return new MandateDetails();
    }

    /**
     * Create an instance of {@link MFAPI }
     * 
     */
    public MFAPI createMFAPI() {
        return new MFAPI();
    }

    /**
     * Create an instance of {@link MFAPIResponse }
     * 
     */
    public MFAPIResponse createMFAPIResponse() {
        return new MFAPIResponse();
    }

    /**
     * Create an instance of {@link ProvOrderStatusResponse }
     * 
     */
    public ProvOrderStatusResponse createProvOrderStatusResponse() {
        return new ProvOrderStatusResponse();
    }

    /**
     * Create an instance of {@link GetPasswordForChildOrder }
     * 
     */
    public GetPasswordForChildOrder createGetPasswordForChildOrder() {
        return new GetPasswordForChildOrder();
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link MFAPIRequest }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://www.bsestarmf.in/2016/01/", name = "Param", scope = MFAPIMobile.class)
    public JAXBElement<MFAPIRequest> createMFAPIMobileParam(MFAPIRequest value) {
        return new JAXBElement<MFAPIRequest>(_MFAPIMobileParam_QNAME, MFAPIRequest.class, MFAPIMobile.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link Response }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://www.bsestarmf.in/2016/01/", name = "GetPasswordMobileResult", scope = GetPasswordMobileResponse.class)
    public JAXBElement<Response> createGetPasswordMobileResponseGetPasswordMobileResult(Response value) {
        return new JAXBElement<Response>(_GetPasswordMobileResponseGetPasswordMobileResult_QNAME, Response.class, GetPasswordMobileResponse.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link org.datacontract.schemas._2004._07.starmfwebservice.RedemptionStatementResponse }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://www.bsestarmf.in/2016/01/", name = "RedemptionStatementResult", scope = com.moptra.go4wealth.prs.mfuploadapi.com.bseindia.bsestarmfdemo._2016._01.RedemptionStatementResponse.class)
    public JAXBElement<com.moptra.go4wealth.prs.mfuploadapi.org.datacontract.schemas._2004._07.starmfwebservice.RedemptionStatementResponse> createRedemptionStatementResponseRedemptionStatementResult(com.moptra.go4wealth.prs.mfuploadapi.org.datacontract.schemas._2004._07.starmfwebservice.RedemptionStatementResponse value) {
        return new JAXBElement<com.moptra.go4wealth.prs.mfuploadapi.org.datacontract.schemas._2004._07.starmfwebservice.RedemptionStatementResponse>(_RedemptionStatementResponseRedemptionStatementResult_QNAME, com.moptra.go4wealth.prs.mfuploadapi.org.datacontract.schemas._2004._07.starmfwebservice.RedemptionStatementResponse.class, com.moptra.go4wealth.prs.mfuploadapi.com.bseindia.bsestarmfdemo._2016._01.RedemptionStatementResponse.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link OrderResponse }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://www.bsestarmf.in/2016/01/", name = "OrderStatusResult", scope = OrderStatusResponse.class)
    public JAXBElement<OrderResponse> createOrderStatusResponseOrderStatusResult(OrderResponse value) {
        return new JAXBElement<OrderResponse>(_OrderStatusResponseOrderStatusResult_QNAME, OrderResponse.class, OrderStatusResponse.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link Response }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://www.bsestarmf.in/2016/01/", name = "GetPasswordForChildOrderResult", scope = GetPasswordForChildOrderResponse.class)
    public JAXBElement<Response> createGetPasswordForChildOrderResponseGetPasswordForChildOrderResult(Response value) {
        return new JAXBElement<Response>(_GetPasswordForChildOrderResponseGetPasswordForChildOrderResult_QNAME, Response.class, GetPasswordForChildOrderResponse.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link org.datacontract.schemas._2004._07.starmfwebservice.MandateDetailsResponse }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://www.bsestarmf.in/2016/01/", name = "MandateDetailsResult", scope = com.moptra.go4wealth.prs.mfuploadapi.com.bseindia.bsestarmfdemo._2016._01.MandateDetailsResponse.class)
    public JAXBElement<com.moptra.go4wealth.prs.mfuploadapi.org.datacontract.schemas._2004._07.starmfwebservice.MandateDetailsResponse> createMandateDetailsResponseMandateDetailsResult(com.moptra.go4wealth.prs.mfuploadapi.org.datacontract.schemas._2004._07.starmfwebservice.MandateDetailsResponse value) {
        return new JAXBElement<com.moptra.go4wealth.prs.mfuploadapi.org.datacontract.schemas._2004._07.starmfwebservice.MandateDetailsResponse>(_MandateDetailsResponseMandateDetailsResult_QNAME, com.moptra.go4wealth.prs.mfuploadapi.org.datacontract.schemas._2004._07.starmfwebservice.MandateDetailsResponse.class, com.moptra.go4wealth.prs.mfuploadapi.com.bseindia.bsestarmfdemo._2016._01.MandateDetailsResponse.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link OrderRequest }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://www.bsestarmf.in/2016/01/", name = "Param", scope = ProvOrderStatus.class)
    public JAXBElement<OrderRequest> createProvOrderStatusParam(OrderRequest value) {
        return new JAXBElement<OrderRequest>(_MFAPIMobileParam_QNAME, OrderRequest.class, ProvOrderStatus.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link Response }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://www.bsestarmf.in/2016/01/", name = "MFAPIMobileResult", scope = MFAPIMobileResponse.class)
    public JAXBElement<Response> createMFAPIMobileResponseMFAPIMobileResult(Response value) {
        return new JAXBElement<Response>(_MFAPIMobileResponseMFAPIMobileResult_QNAME, Response.class, MFAPIMobileResponse.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link RedemptionStatementRequest }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://www.bsestarmf.in/2016/01/", name = "Param", scope = RedemptionStatement.class)
    public JAXBElement<RedemptionStatementRequest> createRedemptionStatementParam(RedemptionStatementRequest value) {
        return new JAXBElement<RedemptionStatementRequest>(_MFAPIMobileParam_QNAME, RedemptionStatementRequest.class, RedemptionStatement.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link ChildOrderRequest }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://www.bsestarmf.in/2016/01/", name = "Param", scope = ChildOrderDetails.class)
    public JAXBElement<ChildOrderRequest> createChildOrderDetailsParam(ChildOrderRequest value) {
        return new JAXBElement<ChildOrderRequest>(_MFAPIMobileParam_QNAME, ChildOrderRequest.class, ChildOrderDetails.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link org.datacontract.schemas._2004._07.starmfwebservice.EMandateAuthURLResponse }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://www.bsestarmf.in/2016/01/", name = "EMandateAuthURLResult", scope = com.moptra.go4wealth.prs.mfuploadapi.com.bseindia.bsestarmfdemo._2016._01.EMandateAuthURLResponse.class)
    public JAXBElement<com.moptra.go4wealth.prs.mfuploadapi.org.datacontract.schemas._2004._07.starmfwebservice.EMandateAuthURLResponse> createEMandateAuthURLResponseEMandateAuthURLResult(com.moptra.go4wealth.prs.mfuploadapi.org.datacontract.schemas._2004._07.starmfwebservice.EMandateAuthURLResponse value) {
        return new JAXBElement<com.moptra.go4wealth.prs.mfuploadapi.org.datacontract.schemas._2004._07.starmfwebservice.EMandateAuthURLResponse>(_EMandateAuthURLResponseEMandateAuthURLResult_QNAME, com.moptra.go4wealth.prs.mfuploadapi.org.datacontract.schemas._2004._07.starmfwebservice.EMandateAuthURLResponse.class, com.moptra.go4wealth.prs.mfuploadapi.com.bseindia.bsestarmfdemo._2016._01.EMandateAuthURLResponse.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link String }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://www.bsestarmf.in/2016/01/", name = "UserId", scope = GetPassword.class)
    public JAXBElement<String> createGetPasswordUserId(String value) {
        return new JAXBElement<String>(_GetPasswordUserId_QNAME, String.class, GetPassword.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link String }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://www.bsestarmf.in/2016/01/", name = "MemberId", scope = GetPassword.class)
    public JAXBElement<String> createGetPasswordMemberId(String value) {
        return new JAXBElement<String>(_GetPasswordMemberId_QNAME, String.class, GetPassword.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link String }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://www.bsestarmf.in/2016/01/", name = "PassKey", scope = GetPassword.class)
    public JAXBElement<String> createGetPasswordPassKey(String value) {
        return new JAXBElement<String>(_GetPasswordPassKey_QNAME, String.class, GetPassword.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link String }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://www.bsestarmf.in/2016/01/", name = "Password", scope = GetPassword.class)
    public JAXBElement<String> createGetPasswordPassword(String value) {
        return new JAXBElement<String>(_GetPasswordPassword_QNAME, String.class, GetPassword.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link String }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://www.bsestarmf.in/2016/01/", name = "getPasswordResult", scope = GetPasswordResponse.class)
    public JAXBElement<String> createGetPasswordResponseGetPasswordResult(String value) {
        return new JAXBElement<String>(_GetPasswordResponseGetPasswordResult_QNAME, String.class, GetPasswordResponse.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link ChildOrderResponse }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://www.bsestarmf.in/2016/01/", name = "ChildOrderDetailsResult", scope = ChildOrderDetailsResponse.class)
    public JAXBElement<ChildOrderResponse> createChildOrderDetailsResponseChildOrderDetailsResult(ChildOrderResponse value) {
        return new JAXBElement<ChildOrderResponse>(_ChildOrderDetailsResponseChildOrderDetailsResult_QNAME, ChildOrderResponse.class, ChildOrderDetailsResponse.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link EMandateAuthURLRequest }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://www.bsestarmf.in/2016/01/", name = "Param", scope = EMandateAuthURL.class)
    public JAXBElement<EMandateAuthURLRequest> createEMandateAuthURLParam(EMandateAuthURLRequest value) {
        return new JAXBElement<EMandateAuthURLRequest>(_MFAPIMobileParam_QNAME, EMandateAuthURLRequest.class, EMandateAuthURL.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link PasswordRequest }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://www.bsestarmf.in/2016/01/", name = "Param", scope = GetPasswordMobile.class)
    public JAXBElement<PasswordRequest> createGetPasswordMobileParam(PasswordRequest value) {
        return new JAXBElement<PasswordRequest>(_MFAPIMobileParam_QNAME, PasswordRequest.class, GetPasswordMobile.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link Response }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://www.bsestarmf.in/2016/01/", name = "GetAccessTokenResult", scope = GetAccessTokenResponse.class)
    public JAXBElement<Response> createGetAccessTokenResponseGetAccessTokenResult(Response value) {
        return new JAXBElement<Response>(_GetAccessTokenResponseGetAccessTokenResult_QNAME, Response.class, GetAccessTokenResponse.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link OrderRequest }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://www.bsestarmf.in/2016/01/", name = "Param", scope = OrderStatus.class)
    public JAXBElement<OrderRequest> createOrderStatusParam(OrderRequest value) {
        return new JAXBElement<OrderRequest>(_MFAPIMobileParam_QNAME, OrderRequest.class, OrderStatus.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link org.datacontract.schemas._2004._07.starmfwebservice.AllotmentStatementResponse }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://www.bsestarmf.in/2016/01/", name = "AllotmentStatementResult", scope = com.moptra.go4wealth.prs.mfuploadapi.com.bseindia.bsestarmfdemo._2016._01.AllotmentStatementResponse.class)
    public JAXBElement<com.moptra.go4wealth.prs.mfuploadapi.org.datacontract.schemas._2004._07.starmfwebservice.AllotmentStatementResponse> createAllotmentStatementResponseAllotmentStatementResult(com.moptra.go4wealth.prs.mfuploadapi.org.datacontract.schemas._2004._07.starmfwebservice.AllotmentStatementResponse value) {
        return new JAXBElement<com.moptra.go4wealth.prs.mfuploadapi.org.datacontract.schemas._2004._07.starmfwebservice.AllotmentStatementResponse>(_AllotmentStatementResponseAllotmentStatementResult_QNAME, com.moptra.go4wealth.prs.mfuploadapi.org.datacontract.schemas._2004._07.starmfwebservice.AllotmentStatementResponse.class, com.moptra.go4wealth.prs.mfuploadapi.com.bseindia.bsestarmfdemo._2016._01.AllotmentStatementResponse.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link PasswordRequest }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://www.bsestarmf.in/2016/01/", name = "Param", scope = GetAccessToken.class)
    public JAXBElement<PasswordRequest> createGetAccessTokenParam(PasswordRequest value) {
        return new JAXBElement<PasswordRequest>(_MFAPIMobileParam_QNAME, PasswordRequest.class, GetAccessToken.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link AllotmentStatementRequest }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://www.bsestarmf.in/2016/01/", name = "Param", scope = AllotmentStatement.class)
    public JAXBElement<AllotmentStatementRequest> createAllotmentStatementParam(AllotmentStatementRequest value) {
        return new JAXBElement<AllotmentStatementRequest>(_MFAPIMobileParam_QNAME, AllotmentStatementRequest.class, AllotmentStatement.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link MandateDetailsRequest }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://www.bsestarmf.in/2016/01/", name = "Param", scope = MandateDetails.class)
    public JAXBElement<MandateDetailsRequest> createMandateDetailsParam(MandateDetailsRequest value) {
        return new JAXBElement<MandateDetailsRequest>(_MFAPIMobileParam_QNAME, MandateDetailsRequest.class, MandateDetails.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link String }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://www.bsestarmf.in/2016/01/", name = "param", scope = MFAPI.class)
    public JAXBElement<String> createMFAPIParam(String value) {
        return new JAXBElement<String>(_MFAPIParam_QNAME, String.class, MFAPI.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link String }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://www.bsestarmf.in/2016/01/", name = "UserId", scope = MFAPI.class)
    public JAXBElement<String> createMFAPIUserId(String value) {
        return new JAXBElement<String>(_GetPasswordUserId_QNAME, String.class, MFAPI.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link String }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://www.bsestarmf.in/2016/01/", name = "Flag", scope = MFAPI.class)
    public JAXBElement<String> createMFAPIFlag(String value) {
        return new JAXBElement<String>(_MFAPIFlag_QNAME, String.class, MFAPI.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link String }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://www.bsestarmf.in/2016/01/", name = "EncryptedPassword", scope = MFAPI.class)
    public JAXBElement<String> createMFAPIEncryptedPassword(String value) {
        return new JAXBElement<String>(_MFAPIEncryptedPassword_QNAME, String.class, MFAPI.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link String }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://www.bsestarmf.in/2016/01/", name = "MFAPIResult", scope = MFAPIResponse.class)
    public JAXBElement<String> createMFAPIResponseMFAPIResult(String value) {
        return new JAXBElement<String>(_MFAPIResponseMFAPIResult_QNAME, String.class, MFAPIResponse.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link ProvOrderResponse }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://www.bsestarmf.in/2016/01/", name = "ProvOrderStatusResult", scope = ProvOrderStatusResponse.class)
    public JAXBElement<ProvOrderResponse> createProvOrderStatusResponseProvOrderStatusResult(ProvOrderResponse value) {
        return new JAXBElement<ProvOrderResponse>(_ProvOrderStatusResponseProvOrderStatusResult_QNAME, ProvOrderResponse.class, ProvOrderStatusResponse.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link PasswordRequest }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://www.bsestarmf.in/2016/01/", name = "Param", scope = GetPasswordForChildOrder.class)
    public JAXBElement<PasswordRequest> createGetPasswordForChildOrderParam(PasswordRequest value) {
        return new JAXBElement<PasswordRequest>(_MFAPIMobileParam_QNAME, PasswordRequest.class, GetPasswordForChildOrder.class, value);
    }

}
