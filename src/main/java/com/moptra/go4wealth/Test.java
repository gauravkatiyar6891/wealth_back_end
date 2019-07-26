package com.moptra.go4wealth;
public class Test {
	
}/*
import java.awt.image.BufferedImage;
import java.awt.image.RenderedImage;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;

import javax.imageio.ImageIO;

import org.icepdf.core.exceptions.PDFException;
import org.icepdf.core.exceptions.PDFSecurityException;
import org.icepdf.core.pobjects.Document;
import org.icepdf.core.pobjects.Page;
import org.icepdf.core.util.GraphicsRenderingHints;

import com.moptra.go4wealth.prs.common.util.GoForWealthPRSUtil;
import com.moptra.go4wealth.prs.model.AOFPdfFormRequestDTO;

public class Test {

	public static void main(String[] args) {
		// TODO Auto-generated method stub
		
		try {
			
			AOFPdfFormRequestDTO aof = new AOFPdfFormRequestDTO();
			aof.setArnCode("arn");
			aof.setSubBrokerCode("sub");
			aof.setEuin("euin");
			
			aof.setFirstApplicant("firstapplicant");
			aof.setPanNumber1("panNumber1");
			aof.setKyc1("kyc1");
			aof.setDob1("dob1");
			aof.setNameOfGuardian("nameOfGuardian");
			
			aof.setAddress("address");
			aof.setAddressLine1("addressLine1");
			aof.setAddressLine2("addressLine2");
			aof.setCity1("city1");
			aof.setPincode1("pincode1");
			aof.setState1("state1");
			aof.setCountry1("country1");
			
			aof.setEmail("email");
			aof.setMobile("mobile");
			aof.setModeOfHolding("modeOfHolding");
			aof.setOccupation("occupation");
			
			aof.setSecondApplicant("secondApplicant");
			aof.setPanNumber2("panNumber2");
			aof.setKyc2("kyc2");
			aof.setDob2("dob2");
			
			aof.setThirdApplicant("thirdApplicant");
			aof.setPanNumber3("panNumber3");
			aof.setKyc3("kyc3");
			aof.setDob3("dob3");
			
			aof.setOtherDetails("otherDetails");
			aof.setOverseasAddress("overseasAddress");
			aof.setOtherCity("otherCity");
			aof.setOtherPincode("otherPincode");
			aof.setOtherCountry("otherCountry");
			
			aof.setNameOfBank("nameOfBank");
			aof.setBranch("branch");
			aof.setAccountNo("accountNo");
			aof.setAccountType("accountType");
			aof.setIfscCode("ifscCode");
			aof.setBankCity("bankCity");
			aof.setBankPincode("bankPincode");
			aof.setBankState("bankState");
			aof.setIfscCode("ifscCode");
			aof.setBankAddress("bankAddress");
			aof.setBankCountry("bankCountry");
			
			aof.setGurdianName("gurdianName");
			aof.setNomineeName("nomineeName");
			aof.setNomineeAddress("nomineeAddress");
			aof.setNomineePincode("nomineePincode");
			aof.setNomineeState("nomineeState");
			aof.setNomineecity("nomineecity");
			aof.setRelationship("relationship");
			
			
			aof.setDate("data");
			aof.setPlace("place");
			
			aof.setFirstApplicantSignature("D:\\go4wealth-image\\signature\\signature_33.png");
			
			String path = GoForWealthPRSUtil.generateAofFormPdf(aof);
			convertPdfToTiff(path);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		/*try {
			String ip_pub= "124.124.236.235";
			String app_ver = "ekycuat3";
			String url ="http://"+ip_pub+"/"+app_ver+"/eKYCVal_Aadhar.aspx";
			String kyc_data = "|";
			String aadhar = "";
			String sess_id = "";
			Request requestObj = new Request(sess_id, aadhar, "https://localhost:8443/go4wealth-web/secured/uma/test","I", kyc_data);
			
			GoForWealthSIPResponseInfo goForWealthSIPResponseInfo=(GoForWealthSIPResponseInfo) GoForWealthUMARestClient.invokeURL(url,requestObj,String.class,null, HttpMethod.POST);
		    System.out.println(goForWealthSIPResponseInfo);
		    
		    
		} catch (GoForWealthUMAException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}*/
/*
	}
	
	public static class Request{
		 
		public String sess_id;// - Session ID
		public String Aadhar;// - <null>
		public String url ;//* - Return url to handover control back to the website
		public String ekyctype ;// - ‘I’(or)’U’( Insert is for new eKYC record and ‘U’for updating existing eKYC processed record) (or) <null>
		public String kyc_data ;//* - Following items as pipe separated values
		
		public Request(String sess_id, String aadhar, String url, String ekyctype, String kyc_data) {
			super();
			this.sess_id = sess_id;
			Aadhar = aadhar;
			this.url = url;
			this.ekyctype = ekyctype;
			this.kyc_data = kyc_data;
		}
		
		
	}
	
	public static void convertPdfToTiff(String path){
		 Document document = new Document();
        try {
           document.setFile(path);
        } catch (PDFException ex) {
           System.out.println("Error parsing PDF document " + ex);
        } catch (PDFSecurityException ex) {
           System.out.println("Error encryption not supported " + ex);
        } catch (FileNotFoundException ex) {
           System.out.println("Error file not found " + ex);
        } catch (IOException ex) {
           System.out.println("Error IOException " + ex);
        }
        float scale = 2.0f;
        float rotation = 0f;
        for (int i = 0; i < document.getNumberOfPages(); i++) {
           BufferedImage image = (BufferedImage) document.getPageImage(
               i, GraphicsRenderingHints.PRINT, Page.BOUNDARY_CROPBOX, rotation, scale);
           RenderedImage rendImage = image;
           try {
              File file = new File("D:\\go4wealth-image\\aof-tiff\\10996G4W0120062018.tiff");
              ImageIO.write(rendImage, "tiff", file);
           } catch (IOException e) {
              e.printStackTrace();
           }
           image.flush();
        }
        document.dispose();
	}

}*/
