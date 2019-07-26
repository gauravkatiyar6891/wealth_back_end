package com.moptra.go4wealth.ticob.service;

import java.util.List;

import com.moptra.go4wealth.bean.User;
import com.moptra.go4wealth.prs.model.AddToCartDTO;
import com.moptra.go4wealth.ticob.model.TransferInRecordDTO;
import com.moptra.go4wealth.ticob.model.TransferInRequestDTO;

public interface GoForWealthTicobService {

	List<TransferInRecordDTO> getTransferInRecord(int userId);
	
	String placeTicobSipOrder(AddToCartDTO addToCartDTO, User user);
	
	String placeTicobLumpsumOrder(AddToCartDTO addToCartDTO, User user);
	
	String uploadTransferInRecord(List<TransferInRequestDTO> transferInRequestDto);
	
	//String uploadTransferInMasterUserData(List<TransferInMasterDTO> transferInUserDetailsDTOList);
	
	//List<TransferInRecordDTO> getTransferInRecords(int userId);

	//String uploadCamsReport(List<TransferInRequestDTO> camsDataRequestDTO);

	//String uploadKarvyReports(List<TransferInRequestDTO> tranferInRequestDTO);
}