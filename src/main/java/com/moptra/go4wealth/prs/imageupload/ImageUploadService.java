package com.moptra.go4wealth.prs.imageupload;

import java.io.IOException;

import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.stereotype.Component;
import com.moptra.go4wealth.prs.common.exception.GoForWealthPRSException;

@Component
public class ImageUploadService {
	
	
	/*@Autowired
	ImageUploadCallApi imageUploadCallApi;
	
	public String getPassword(){
		String passResponse=imageUploadCallApi.getPassword();
		return passResponse;
	}*/
	
	
	
	@Autowired
	ImageUploadCallApi imageUploadCallApi;

	public String getPassword(GetPasswordRequest getPasswordRequest) throws GoForWealthPRSException{
		String password=null;
		String encryptedPassword=imageUploadCallApi.getPassword(getPasswordRequest);
		String res[] = encryptedPassword.split("\\|");
		if(res[0].equals("100")){
			password = res[1];
		}
		return password;
	}

	public String uploadFile(FileUploadRequest fileUploadRequest) throws GoForWealthPRSException, IOException {
		
		String fileUploadResponse=imageUploadCallApi.uploadImage(fileUploadRequest);
		return fileUploadResponse;
	}

	public String uploadMandateFile(MandateFormUploadRequest mandateFormUploadRequest) {
		
		String fileUploadResponse=imageUploadCallApi.uploadMandateFile(mandateFormUploadRequest);
		return fileUploadResponse;
	}

	
}
