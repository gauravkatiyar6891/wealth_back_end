package com.moptra.go4wealth.prs.imageupload;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement(name="FileData")
public class FileUploadRequest {
	
	private String clientCode;
	private String docType;
	private String encrypedPassword;
	private String fileName;
	private String filler1;
	private String filler2;
	private String flag;
	private String memberCode;
	private String userId;
	private String pFileBytes;
	
	
	@XmlElement(name="ClientCode")
	public String getClientCode() {
		return clientCode;
	}
	public void setClientCode(String clientCode) {
		this.clientCode = clientCode;
	}
	@XmlElement(name="DocumentType")
	public String getDocType() {
		return docType;
	}
	public void setDocType(String docType) {
		this.docType = docType;
	}
	@XmlElement(name="EncryptedPassword")
	public String getEncrypedPassword() {
		return encrypedPassword;
	}
	public void setEncrypedPassword(String encrypedPassword) {
		this.encrypedPassword = encrypedPassword;
	}
	@XmlElement(name="FileName")
	public String getFileName() {
		return fileName;
	}
	public void setFileName(String fileName) {
		this.fileName = fileName;
	}
	@XmlElement(name="Filler1")
	public String getFiller1() {
		return filler1;
	}
	public void setFiller1(String filler1) {
		this.filler1 = filler1;
	}
	@XmlElement(name="Filler2")
	public String getFiller2() {
		return filler2;
	}
	public void setFiller2(String filler2) {
		this.filler2 = filler2;
	}
	@XmlElement(name="Flag")
	public String getFlag() {
		return flag;
	}
	public void setFlag(String flag) {
		this.flag = flag;
	}
	@XmlElement(name="MemberCode")
	public String getMemberCode() {
		return memberCode;
	}
	public void setMemberCode(String memberId) {
		this.memberCode = memberId;
	}
	
	@XmlElement(name="UserId")
	public String getUserId() {
		return userId;
	}
	public void setUserId(String userId) {
		this.userId = userId;
	}
	@XmlElement(name="pFileBytes")
	public String getpFileBytes() {
		return pFileBytes;
	}
	public void setpFileBytes(String file) {
		this.pFileBytes = file;
	}
	
	
}
