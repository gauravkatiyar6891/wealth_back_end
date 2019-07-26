package com.moptra.go4wealth.ticob.model;

import java.util.List;

public class TransferInKarvyDTO {
	
	private String folioNo;
	private List<TransferInRequestDTO> transferInRequestDto;
	
	public String getFolioNo() {
		return folioNo;
	}
	public void setFolioNo(String folioNo) {
		this.folioNo = folioNo;
	}
	public List<TransferInRequestDTO> getTransferInRequestDto() {
		return transferInRequestDto;
	}
	public void setTransferInRequestDto(List<TransferInRequestDTO> transferInRequestDto) {
		this.transferInRequestDto = transferInRequestDto;
	}

}