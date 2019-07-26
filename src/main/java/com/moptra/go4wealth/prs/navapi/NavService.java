package com.moptra.go4wealth.prs.navapi;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.moptra.go4wealth.prs.model.NavApiResponseDTO;

@Component
public class NavService {

	@Autowired
	NavServiceApiImpl navServiceApiImpl;
	
	public String getNavPassword(){
		String password = navServiceApiImpl.getNavPassword();
		return password;
	}
	
	public String getCompressedKey(){
		navServiceApiImpl.getCompressedKey();
		return null;
	}
	
	public NavApiResponseDTO getNavData(String amfiCode){
		NavApiResponseDTO navResponse=navServiceApiImpl.getNavData(amfiCode);
		System.out.println("Nav : " + navResponse);
		return navResponse;
	}
	
	public Object getMFSchemeMaster(String amfiCode){
		Object mfSchemeMasterResponse = navServiceApiImpl.getMFSchemeMaster(amfiCode);
		return mfSchemeMasterResponse;
	}


}
