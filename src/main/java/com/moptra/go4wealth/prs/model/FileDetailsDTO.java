package com.moptra.go4wealth.prs.model;

public class FileDetailsDTO {
	
	String signatureString;
	String fileName;
	String frontAdharCardString;
	String backAdharCardString;
	String addressProof;
	String docNumber;
	String imageType;
	boolean isFatca;
	
	public String getImageType() {
		return imageType;
	}
	public void setImageType(String imageType) {
		this.imageType = imageType;
	}
	
	
	
	public String getSignatureString() {
		return signatureString;
	}
	public void setSignatureString(String signatureString) {
		this.signatureString = signatureString;
	}
	public String getFileName() {
		return fileName;
	}
	public void setFileName(String fileName) {
		this.fileName = fileName;
	}
	public String getFrontAdharCardString() {
		return frontAdharCardString;
	}
	public void setFrontAdharCardString(String frontAdharCardString) {
		this.frontAdharCardString = frontAdharCardString;
	}
	public String getBackAdharCardString() {
		return backAdharCardString;
	}
	public void setBackAdharCardString(String backAdharCardString) {
		this.backAdharCardString = backAdharCardString;
	}
	public String getAddressProof() {
		return addressProof;
	}
	public void setAddressProof(String addressProof) {
		this.addressProof = addressProof;
	}
	public String getDocNumber() {
		return docNumber;
	}
	public void setDocNumber(String docNumber) {
		this.docNumber = docNumber;
	}
	
	public boolean isFatca() {
		return isFatca;
	}
	 
	public void setFatca(boolean isFatca) {
		this.isFatca = isFatca;
	}
	
	
}
