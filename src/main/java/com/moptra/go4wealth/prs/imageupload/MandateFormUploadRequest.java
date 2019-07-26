package com.moptra.go4wealth.prs.imageupload;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement(name="MandateScanFileData")
public class MandateFormUploadRequest {
	private String clientCode;
	private String encrypedPassword;
	private String filler1;
	private String filler2;
	private String flag;
	private String imageName;
	private String imageType;
	private String mandateId;
	private String mandateType;
	private String memberCode;
	private String pFileBytes;
	
	
	@XmlElement(name="ClientCode")
	public String getClientCode() {
		return clientCode;
	}
	public void setClientCode(String clientCode) {
		this.clientCode = clientCode;
	}
	@XmlElement(name="EncryptedPassword")
	public String getEncrypedPassword() {
		return encrypedPassword;
	}
	public void setEncrypedPassword(String encrypedPassword) {
		this.encrypedPassword = encrypedPassword;
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
	
	@XmlElement(name="ImageName")
	public String getImageName() {
		return imageName;
	}
	public void setImageName(String imageName) {
		this.imageName = imageName;
	}
	
	@XmlElement(name="ImageType")
	public String getImageType() {
		return imageType;
	}
	public void setImageType(String imageType) {
		this.imageType = imageType;
	}
	
	@XmlElement(name="MandateId")
	public String getMandateId() {
		return mandateId;
	}
	public void setMandateId(String mandateId) {
		this.mandateId = mandateId;
	}
	
	@XmlElement(name="MandateType")
	public String getMandateType() {
		return mandateType;
	}
	public void setMandateType(String mandateType) {
		this.mandateType = mandateType;
	}
	
	@XmlElement(name="MemberCode")
	public String getMemberCode() {
		return memberCode;
	}
	public void setMemberCode(String memberCode) {
		this.memberCode = memberCode;
	}
	
	@XmlElement(name="pFileBytes")
	public String getpFileBytes() {
		return pFileBytes;
	}
	public void setpFileBytes(String pFileBytes) {
		this.pFileBytes = pFileBytes;
	}
	
	
	
}
