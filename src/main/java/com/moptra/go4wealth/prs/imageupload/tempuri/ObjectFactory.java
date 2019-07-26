
package com.moptra.go4wealth.prs.imageupload.tempuri;

import javax.xml.bind.JAXBElement;
import javax.xml.bind.annotation.XmlElementDecl;
import javax.xml.bind.annotation.XmlRegistry;
import javax.xml.namespace.QName;

import com.moptra.go4wealth.prs.imageupload.starmffileuploadservice.FileData;
import com.moptra.go4wealth.prs.imageupload.starmffileuploadservice.MandateScanFileData;
import com.moptra.go4wealth.prs.imageupload.starmffileuploadservice.PasswordRequest;
import com.moptra.go4wealth.prs.imageupload.starmffileuploadservice.Response;



/**
 * This object contains factory methods for each 
 * Java content interface and Java element interface 
 * generated in the org.tempuri package. 
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

    private final static QName _UploadFileData_QNAME = new QName("http://tempuri.org/", "data");
    private final static QName _UploadMandateScanFileResponseUploadMandateScanFileResult_QNAME = new QName("http://tempuri.org/", "UploadMandateScanFileResult");
    private final static QName _UploadFileResponseUploadFileResult_QNAME = new QName("http://tempuri.org/", "UploadFileResult");
    private final static QName _GetPasswordResponseGetPasswordResult_QNAME = new QName("http://tempuri.org/", "GetPasswordResult");
    private final static QName _UploadMandateScanFileData_QNAME = new QName("http://tempuri.org/", "Data");
    private final static QName _GetPasswordParam_QNAME = new QName("http://tempuri.org/", "Param");

    /**
     * Create a new ObjectFactory that can be used to create new instances of schema derived classes for package: org.tempuri
     * 
     */
    public ObjectFactory() {
    }

    /**
     * Create an instance of {@link UploadFile }
     * 
     */
    public UploadFile createUploadFile() {
        return new UploadFile();
    }

    /**
     * Create an instance of {@link UploadMandateScanFileResponse }
     * 
     */
    public UploadMandateScanFileResponse createUploadMandateScanFileResponse() {
        return new UploadMandateScanFileResponse();
    }

    /**
     * Create an instance of {@link UploadFileResponse }
     * 
     */
    public UploadFileResponse createUploadFileResponse() {
        return new UploadFileResponse();
    }

    /**
     * Create an instance of {@link GetPasswordResponse }
     * 
     */
    public GetPasswordResponse createGetPasswordResponse() {
        return new GetPasswordResponse();
    }

    /**
     * Create an instance of {@link JsonDataResponse }
     * 
     */
    public JsonDataResponse createJsonDataResponse() {
        return new JsonDataResponse();
    }

    /**
     * Create an instance of {@link UploadMandateScanFile }
     * 
     */
    public UploadMandateScanFile createUploadMandateScanFile() {
        return new UploadMandateScanFile();
    }

    /**
     * Create an instance of {@link JsonData }
     * 
     */
    public JsonData createJsonData() {
        return new JsonData();
    }

    /**
     * Create an instance of {@link GetPassword }
     * 
     */
    public GetPassword createGetPassword() {
        return new GetPassword();
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link FileData }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://tempuri.org/", name = "data", scope = UploadFile.class)
    public JAXBElement<FileData> createUploadFileData(FileData value) {
        return new JAXBElement<FileData>(_UploadFileData_QNAME, FileData.class, UploadFile.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link Response }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://tempuri.org/", name = "UploadMandateScanFileResult", scope = UploadMandateScanFileResponse.class)
    public JAXBElement<Response> createUploadMandateScanFileResponseUploadMandateScanFileResult(Response value) {
        return new JAXBElement<Response>(_UploadMandateScanFileResponseUploadMandateScanFileResult_QNAME, Response.class, UploadMandateScanFileResponse.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link Response }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://tempuri.org/", name = "UploadFileResult", scope = UploadFileResponse.class)
    public JAXBElement<Response> createUploadFileResponseUploadFileResult(Response value) {
        return new JAXBElement<Response>(_UploadFileResponseUploadFileResult_QNAME, Response.class, UploadFileResponse.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link Response }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://tempuri.org/", name = "GetPasswordResult", scope = GetPasswordResponse.class)
    public JAXBElement<Response> createGetPasswordResponseGetPasswordResult(Response value) {
        return new JAXBElement<Response>(_GetPasswordResponseGetPasswordResult_QNAME, Response.class, GetPasswordResponse.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link MandateScanFileData }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://tempuri.org/", name = "Data", scope = UploadMandateScanFile.class)
    public JAXBElement<MandateScanFileData> createUploadMandateScanFileData(MandateScanFileData value) {
        return new JAXBElement<MandateScanFileData>(_UploadMandateScanFileData_QNAME, MandateScanFileData.class, UploadMandateScanFile.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link String }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://tempuri.org/", name = "Data", scope = JsonData.class)
    public JAXBElement<String> createJsonDataData(String value) {
        return new JAXBElement<String>(_UploadMandateScanFileData_QNAME, String.class, JsonData.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link PasswordRequest }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://tempuri.org/", name = "Param", scope = GetPassword.class)
    public JAXBElement<PasswordRequest> createGetPasswordParam(PasswordRequest value) {
        return new JAXBElement<PasswordRequest>(_GetPasswordParam_QNAME, PasswordRequest.class, GetPassword.class, value);
    }

}
