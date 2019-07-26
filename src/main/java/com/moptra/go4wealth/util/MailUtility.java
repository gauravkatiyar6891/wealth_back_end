package com.moptra.go4wealth.util;

import java.io.File;
import java.io.IOException;

import org.apache.commons.codec.binary.Base64;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.moptra.go4wealth.bean.StoreConf;
import com.moptra.go4wealth.repository.StoreConfRepository;
import com.sendgrid.Attachments;
import com.sendgrid.Content;
import com.sendgrid.Email;
import com.sendgrid.Mail;
import com.sendgrid.Method;
import com.sendgrid.Request;
import com.sendgrid.Response;
import com.sendgrid.SendGrid;

@Component
public class MailUtility {
	
	private static Logger logger = LoggerFactory.getLogger(MailUtility.class);
	
	@Autowired
	StoreConfRepository storeConfRepository;
	
	/*
	public static String apiKey="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"; 
	static final String sendGridUser = "go4wealth";
	static final String sendGridPassword = "Go4wealth@1";
    static final String emailFrom="support@go4wealth.com";
    */
	
	/* 
	public static void main(String[] args) throws IOException {
		try{ 
			baselineExample(emailFrom,emailTo,emailSubject,emailText);
		}catch(Exception e){
			System.out.println("Exception encountered: " +  e.getMessage());   
		}
	}
	*/

	public void baselineExample(String emailTo,String emailSubject,String emailText) throws IOException {
		StoreConf authKeyObj = storeConfRepository.findByKeyword("SENDGRID_API_KEY");
		String apiKey=authKeyObj.getKeywordValue();
		SendGrid sg = new SendGrid(apiKey);
		logger.info("In MailUtility apiKey=== "+apiKey);
		sg.addRequestHeader("X-Mock", "true");
		Request request = new Request();
		String type = "noreply";
		Mail mailConfiguration = buildHelloEmail(emailTo,emailSubject,emailText,type);
		try {
			request.setMethod(Method.POST);
		    request.setEndpoint("mail/send");
		    request.setBody(mailConfiguration.build());
		    Response response = sg.api(request);
		}catch (IOException ex) {
			throw ex;
		}
	}
	
	public void baselineExamples(String emailTo,String emailSubject,String emailText,String type) throws IOException {
		StoreConf authKeyObj = storeConfRepository.findByKeyword("SENDGRID_API_KEY");  
		String apiKey=authKeyObj.getKeywordValue();
		SendGrid sg = new SendGrid(apiKey);
		logger.info("In MailUtility apiKey=== "+apiKey);
		sg.addRequestHeader("X-Mock", "true");
		Request request = new Request();
		Mail mailConfiguration = buildHelloEmail(emailTo,emailSubject,emailText,type);
		try {
			request.setMethod(Method.POST);
		    request.setEndpoint("mail/send");
		    request.setBody(mailConfiguration.build());
		    Response response = sg.api(request);
		} catch (IOException ex) {
			throw ex;
		}
	}
	
	//Minimum required to send an email
	public Mail buildHelloEmail(String emailTo,String emailSubject,String emailText,String type) throws IOException {
		StoreConf emailFromObj = storeConfRepository.findByKeyword("SENDGRID_SENDER_EMAIL");
		String emailFrom = "";
		if(type.equals("support"))
			emailFrom = "support@go4wealth.com";
		else
			emailFrom = emailFromObj.getKeywordValue();
		//String emailFrom=emailFromObj.getKeywordValue();
		logger.info("In MailUtility emailFrom=== "+emailFrom);
		SendGrid sg = new SendGrid(emailFrom);
		Email from = new Email(emailFrom);
	    Email to = new Email(emailTo);
	    Content content = new Content("text/html", emailText+ "<br/>");
	    Mail mail = new Mail(from, emailSubject, to, content);
	    return mail;
	}
  
	public void baselineExampleWithAttechment(String emailTo,String emailSubject,String emailText, byte[] fileData, File file,String encodeString) throws IOException {
		StoreConf authKeyObj = storeConfRepository.findByKeyword("SENDGRID_API_KEY");  
		String apiKey=authKeyObj.getKeywordValue();
		SendGrid sg = new SendGrid(apiKey);
		logger.info("In MailUtility apiKey=== "+apiKey);
		sg.addRequestHeader("X-Mock", "true");
		Request request = new Request();
		Mail mailConfiguration = buildHelloEmailWithAttechment(emailTo,emailSubject,emailText,fileData,file,encodeString);
		try {			      
		      request.setMethod(Method.POST);
		      request.setEndpoint("mail/send");
		      request.setBody(mailConfiguration.build());
		      Response response = sg.api(request);
		}catch (IOException ex) {
		   throw ex;
		}
    }
	
	  // Minimum required to send an email
	  public  Mail buildHelloEmailWithAttechment(String emailTo,String emailSubject,String emailText, byte[] fileData, File file,String encodeString) throws IOException {
		   StoreConf emailFromObj = storeConfRepository.findByKeyword("SENDGRID_SENDER_EMAIL");
			String emailFrom=emailFromObj.getKeywordValue();
			logger.info("In MailUtility emailFrom=== "+emailFrom);
			SendGrid sg = new SendGrid(emailFrom);
		  Email from = new Email(emailFrom);
	       Email to = new Email(emailTo);     
	      Content content = new Content("text/html", emailText+ "<br/>");
	      Mail mail = new Mail(from, emailSubject, to, content);
	      Attachments attachments3 = new Attachments();
	      Base64 x = new Base64();
	        String imageDataString = x.encodeAsString(fileData);
	        attachments3.setContent(imageDataString);
	        attachments3.setType("application/xls");//"application/pdf"
	        attachments3.setFilename("RegisteredUserReport.xls");
	        attachments3.setDisposition("attachment");
	        attachments3.setContentId("report");
	        mail.addAttachments(attachments3);
	     return mail;
	  }


}