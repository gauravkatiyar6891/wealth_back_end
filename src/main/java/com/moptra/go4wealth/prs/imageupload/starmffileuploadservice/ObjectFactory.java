
package com.moptra.go4wealth.prs.imageupload.starmffileuploadservice;

import javax.xml.bind.JAXBElement;
import javax.xml.bind.annotation.XmlElementDecl;
import javax.xml.bind.annotation.XmlRegistry;
import javax.xml.namespace.QName;


/**
 * This object contains factory methods for each 
 * Java content interface and Java element interface 
 * generated in the org.datacontract.schemas._2004._07.starmffileuploadservice package. 
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

    private final static QName _Response_QNAME = new QName("http://schemas.datacontract.org/2004/07/StarMFFileUploadService", "Response");
    private final static QName _PasswordRequest_QNAME = new QName("http://schemas.datacontract.org/2004/07/StarMFFileUploadService", "PasswordRequest");
    private final static QName _MandateScanFileData_QNAME = new QName("http://schemas.datacontract.org/2004/07/StarMFFileUploadService", "MandateScanFileData");
    private final static QName _FileData_QNAME = new QName("http://schemas.datacontract.org/2004/07/StarMFFileUploadService", "FileData");
    private final static QName _FileDataUserId_QNAME = new QName("http://schemas.datacontract.org/2004/07/StarMFFileUploadService", "UserId");
    private final static QName _FileDataFileName_QNAME = new QName("http://schemas.datacontract.org/2004/07/StarMFFileUploadService", "FileName");
    private final static QName _FileDataFiller2_QNAME = new QName("http://schemas.datacontract.org/2004/07/StarMFFileUploadService", "Filler2");
    private final static QName _FileDataPFileBytes_QNAME = new QName("http://schemas.datacontract.org/2004/07/StarMFFileUploadService", "pFileBytes");
    private final static QName _FileDataFiller1_QNAME = new QName("http://schemas.datacontract.org/2004/07/StarMFFileUploadService", "Filler1");
    private final static QName _FileDataEncryptedPassword_QNAME = new QName("http://schemas.datacontract.org/2004/07/StarMFFileUploadService", "EncryptedPassword");
    private final static QName _FileDataClientCode_QNAME = new QName("http://schemas.datacontract.org/2004/07/StarMFFileUploadService", "ClientCode");
    private final static QName _FileDataDocumentType_QNAME = new QName("http://schemas.datacontract.org/2004/07/StarMFFileUploadService", "DocumentType");
    private final static QName _FileDataMemberCode_QNAME = new QName("http://schemas.datacontract.org/2004/07/StarMFFileUploadService", "MemberCode");
    private final static QName _FileDataFlag_QNAME = new QName("http://schemas.datacontract.org/2004/07/StarMFFileUploadService", "Flag");
    private final static QName _PasswordRequestMemberId_QNAME = new QName("http://schemas.datacontract.org/2004/07/StarMFFileUploadService", "MemberId");
    private final static QName _PasswordRequestPassword_QNAME = new QName("http://schemas.datacontract.org/2004/07/StarMFFileUploadService", "Password");
    private final static QName _ResponseStatus_QNAME = new QName("http://schemas.datacontract.org/2004/07/StarMFFileUploadService", "Status");
    private final static QName _ResponseResponseString_QNAME = new QName("http://schemas.datacontract.org/2004/07/StarMFFileUploadService", "ResponseString");
    private final static QName _ResponseFiller_QNAME = new QName("http://schemas.datacontract.org/2004/07/StarMFFileUploadService", "Filler");
    private final static QName _MandateScanFileDataMandateType_QNAME = new QName("http://schemas.datacontract.org/2004/07/StarMFFileUploadService", "MandateType");
    private final static QName _MandateScanFileDataImageName_QNAME = new QName("http://schemas.datacontract.org/2004/07/StarMFFileUploadService", "ImageName");
    private final static QName _MandateScanFileDataMandateId_QNAME = new QName("http://schemas.datacontract.org/2004/07/StarMFFileUploadService", "MandateId");
    private final static QName _MandateScanFileDataImageType_QNAME = new QName("http://schemas.datacontract.org/2004/07/StarMFFileUploadService", "ImageType");

    /**
     * Create a new ObjectFactory that can be used to create new instances of schema derived classes for package: org.datacontract.schemas._2004._07.starmffileuploadservice
     * 
     */
    public ObjectFactory() {
    }

    /**
     * Create an instance of {@link FileData }
     * 
     */
    public FileData createFileData() {
        return new FileData();
    }

    /**
     * Create an instance of {@link PasswordRequest }
     * 
     */
    public PasswordRequest createPasswordRequest() {
        return new PasswordRequest();
    }

    /**
     * Create an instance of {@link Response }
     * 
     */
    public Response createResponse() {
        return new Response();
    }

    /**
     * Create an instance of {@link MandateScanFileData }
     * 
     */
    public MandateScanFileData createMandateScanFileData() {
        return new MandateScanFileData();
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link Response }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://schemas.datacontract.org/2004/07/StarMFFileUploadService", name = "Response")
    public JAXBElement<Response> createResponse(Response value) {
        return new JAXBElement<Response>(_Response_QNAME, Response.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link PasswordRequest }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://schemas.datacontract.org/2004/07/StarMFFileUploadService", name = "PasswordRequest")
    public JAXBElement<PasswordRequest> createPasswordRequest(PasswordRequest value) {
        return new JAXBElement<PasswordRequest>(_PasswordRequest_QNAME, PasswordRequest.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link MandateScanFileData }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://schemas.datacontract.org/2004/07/StarMFFileUploadService", name = "MandateScanFileData")
    public JAXBElement<MandateScanFileData> createMandateScanFileData(MandateScanFileData value) {
        return new JAXBElement<MandateScanFileData>(_MandateScanFileData_QNAME, MandateScanFileData.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link FileData }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://schemas.datacontract.org/2004/07/StarMFFileUploadService", name = "FileData")
    public JAXBElement<FileData> createFileData(FileData value) {
        return new JAXBElement<FileData>(_FileData_QNAME, FileData.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link String }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://schemas.datacontract.org/2004/07/StarMFFileUploadService", name = "UserId", scope = FileData.class)
    public JAXBElement<String> createFileDataUserId(String value) {
        return new JAXBElement<String>(_FileDataUserId_QNAME, String.class, FileData.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link String }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://schemas.datacontract.org/2004/07/StarMFFileUploadService", name = "FileName", scope = FileData.class)
    public JAXBElement<String> createFileDataFileName(String value) {
        return new JAXBElement<String>(_FileDataFileName_QNAME, String.class, FileData.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link String }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://schemas.datacontract.org/2004/07/StarMFFileUploadService", name = "Filler2", scope = FileData.class)
    public JAXBElement<String> createFileDataFiller2(String value) {
        return new JAXBElement<String>(_FileDataFiller2_QNAME, String.class, FileData.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link byte[]}{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://schemas.datacontract.org/2004/07/StarMFFileUploadService", name = "pFileBytes", scope = FileData.class)
    public JAXBElement<byte[]> createFileDataPFileBytes(byte[] value) {
        return new JAXBElement<byte[]>(_FileDataPFileBytes_QNAME, byte[].class, FileData.class, ((byte[]) value));
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link String }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://schemas.datacontract.org/2004/07/StarMFFileUploadService", name = "Filler1", scope = FileData.class)
    public JAXBElement<String> createFileDataFiller1(String value) {
        return new JAXBElement<String>(_FileDataFiller1_QNAME, String.class, FileData.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link String }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://schemas.datacontract.org/2004/07/StarMFFileUploadService", name = "EncryptedPassword", scope = FileData.class)
    public JAXBElement<String> createFileDataEncryptedPassword(String value) {
        return new JAXBElement<String>(_FileDataEncryptedPassword_QNAME, String.class, FileData.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link String }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://schemas.datacontract.org/2004/07/StarMFFileUploadService", name = "ClientCode", scope = FileData.class)
    public JAXBElement<String> createFileDataClientCode(String value) {
        return new JAXBElement<String>(_FileDataClientCode_QNAME, String.class, FileData.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link String }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://schemas.datacontract.org/2004/07/StarMFFileUploadService", name = "DocumentType", scope = FileData.class)
    public JAXBElement<String> createFileDataDocumentType(String value) {
        return new JAXBElement<String>(_FileDataDocumentType_QNAME, String.class, FileData.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link String }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://schemas.datacontract.org/2004/07/StarMFFileUploadService", name = "MemberCode", scope = FileData.class)
    public JAXBElement<String> createFileDataMemberCode(String value) {
        return new JAXBElement<String>(_FileDataMemberCode_QNAME, String.class, FileData.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link String }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://schemas.datacontract.org/2004/07/StarMFFileUploadService", name = "Flag", scope = FileData.class)
    public JAXBElement<String> createFileDataFlag(String value) {
        return new JAXBElement<String>(_FileDataFlag_QNAME, String.class, FileData.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link String }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://schemas.datacontract.org/2004/07/StarMFFileUploadService", name = "MemberId", scope = PasswordRequest.class)
    public JAXBElement<String> createPasswordRequestMemberId(String value) {
        return new JAXBElement<String>(_PasswordRequestMemberId_QNAME, String.class, PasswordRequest.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link String }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://schemas.datacontract.org/2004/07/StarMFFileUploadService", name = "UserId", scope = PasswordRequest.class)
    public JAXBElement<String> createPasswordRequestUserId(String value) {
        return new JAXBElement<String>(_FileDataUserId_QNAME, String.class, PasswordRequest.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link String }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://schemas.datacontract.org/2004/07/StarMFFileUploadService", name = "Password", scope = PasswordRequest.class)
    public JAXBElement<String> createPasswordRequestPassword(String value) {
        return new JAXBElement<String>(_PasswordRequestPassword_QNAME, String.class, PasswordRequest.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link String }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://schemas.datacontract.org/2004/07/StarMFFileUploadService", name = "Status", scope = Response.class)
    public JAXBElement<String> createResponseStatus(String value) {
        return new JAXBElement<String>(_ResponseStatus_QNAME, String.class, Response.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link String }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://schemas.datacontract.org/2004/07/StarMFFileUploadService", name = "ResponseString", scope = Response.class)
    public JAXBElement<String> createResponseResponseString(String value) {
        return new JAXBElement<String>(_ResponseResponseString_QNAME, String.class, Response.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link String }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://schemas.datacontract.org/2004/07/StarMFFileUploadService", name = "Filler", scope = Response.class)
    public JAXBElement<String> createResponseFiller(String value) {
        return new JAXBElement<String>(_ResponseFiller_QNAME, String.class, Response.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link String }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://schemas.datacontract.org/2004/07/StarMFFileUploadService", name = "MandateType", scope = MandateScanFileData.class)
    public JAXBElement<String> createMandateScanFileDataMandateType(String value) {
        return new JAXBElement<String>(_MandateScanFileDataMandateType_QNAME, String.class, MandateScanFileData.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link String }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://schemas.datacontract.org/2004/07/StarMFFileUploadService", name = "ImageName", scope = MandateScanFileData.class)
    public JAXBElement<String> createMandateScanFileDataImageName(String value) {
        return new JAXBElement<String>(_MandateScanFileDataImageName_QNAME, String.class, MandateScanFileData.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link String }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://schemas.datacontract.org/2004/07/StarMFFileUploadService", name = "Filler2", scope = MandateScanFileData.class)
    public JAXBElement<String> createMandateScanFileDataFiller2(String value) {
        return new JAXBElement<String>(_FileDataFiller2_QNAME, String.class, MandateScanFileData.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link byte[]}{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://schemas.datacontract.org/2004/07/StarMFFileUploadService", name = "pFileBytes", scope = MandateScanFileData.class)
    public JAXBElement<byte[]> createMandateScanFileDataPFileBytes(byte[] value) {
        return new JAXBElement<byte[]>(_FileDataPFileBytes_QNAME, byte[].class, MandateScanFileData.class, ((byte[]) value));
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link String }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://schemas.datacontract.org/2004/07/StarMFFileUploadService", name = "Filler1", scope = MandateScanFileData.class)
    public JAXBElement<String> createMandateScanFileDataFiller1(String value) {
        return new JAXBElement<String>(_FileDataFiller1_QNAME, String.class, MandateScanFileData.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link String }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://schemas.datacontract.org/2004/07/StarMFFileUploadService", name = "MandateId", scope = MandateScanFileData.class)
    public JAXBElement<String> createMandateScanFileDataMandateId(String value) {
        return new JAXBElement<String>(_MandateScanFileDataMandateId_QNAME, String.class, MandateScanFileData.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link String }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://schemas.datacontract.org/2004/07/StarMFFileUploadService", name = "EncryptedPassword", scope = MandateScanFileData.class)
    public JAXBElement<String> createMandateScanFileDataEncryptedPassword(String value) {
        return new JAXBElement<String>(_FileDataEncryptedPassword_QNAME, String.class, MandateScanFileData.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link String }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://schemas.datacontract.org/2004/07/StarMFFileUploadService", name = "ClientCode", scope = MandateScanFileData.class)
    public JAXBElement<String> createMandateScanFileDataClientCode(String value) {
        return new JAXBElement<String>(_FileDataClientCode_QNAME, String.class, MandateScanFileData.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link String }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://schemas.datacontract.org/2004/07/StarMFFileUploadService", name = "MemberCode", scope = MandateScanFileData.class)
    public JAXBElement<String> createMandateScanFileDataMemberCode(String value) {
        return new JAXBElement<String>(_FileDataMemberCode_QNAME, String.class, MandateScanFileData.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link String }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://schemas.datacontract.org/2004/07/StarMFFileUploadService", name = "ImageType", scope = MandateScanFileData.class)
    public JAXBElement<String> createMandateScanFileDataImageType(String value) {
        return new JAXBElement<String>(_MandateScanFileDataImageType_QNAME, String.class, MandateScanFileData.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link String }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://schemas.datacontract.org/2004/07/StarMFFileUploadService", name = "Flag", scope = MandateScanFileData.class)
    public JAXBElement<String> createMandateScanFileDataFlag(String value) {
        return new JAXBElement<String>(_FileDataFlag_QNAME, String.class, MandateScanFileData.class, value);
    }

}
