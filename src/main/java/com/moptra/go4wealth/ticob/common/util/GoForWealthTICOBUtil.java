package com.moptra.go4wealth.ticob.common.util;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.apache.poi.ss.usermodel.DataFormatter;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.multipart.MultipartFile;

import com.moptra.go4wealth.ticob.common.constant.GoForWealthTICOBConstants;
import com.moptra.go4wealth.ticob.common.enums.GoForWealthTICOBErrorMessageEnum;
import com.moptra.go4wealth.ticob.common.exception.GoForWealthTICOBException;
import com.moptra.go4wealth.ticob.common.rest.GoForWealthTICOBResponseInfo;
import com.moptra.go4wealth.ticob.model.CamsMailbackUserPortfolioData;
import com.moptra.go4wealth.ticob.model.KarvyMailBackUserPortfolioData;

public class GoForWealthTICOBUtil {

	private static Logger logger = LoggerFactory.getLogger(GoForWealthTICOBUtil.class);

	public static GoForWealthTICOBResponseInfo getSuccessResponseInfo(String status,String message, Object data) {
		GoForWealthTICOBResponseInfo responseInfo = new GoForWealthTICOBResponseInfo();
		responseInfo.setStatus(status);
		responseInfo.setMessage(message);
		if (!GoForWealthTICOBStringUtil.isNull(data))
			responseInfo.setData(data);
		return responseInfo;
	}

	public static GoForWealthTICOBResponseInfo getErrorResponseInfo(String status, String message, Exception e) {
		logger.error("Exception occurred = ", e);
		GoForWealthTICOBResponseInfo responseInfo = new GoForWealthTICOBResponseInfo();
		responseInfo.setStatus(status);
		responseInfo.setMessage(message);
		responseInfo.setData(GoForWealthTICOBConstants.BLANK);
		responseInfo.setTotalRecords(GoForWealthTICOBConstants.ZERO);
		return responseInfo;
	}

	public static GoForWealthTICOBResponseInfo getExceptionResponseInfo(GoForWealthTICOBException e) {
		logger.error("Exception occurred = ", e);
		GoForWealthTICOBResponseInfo responseInfo = new GoForWealthTICOBResponseInfo();
		responseInfo.setStatus(e.getErrorCode());
		responseInfo.setMessage(e.getErrorMessage());
		responseInfo.setData(GoForWealthTICOBConstants.BLANK);
		responseInfo.setTotalRecords(GoForWealthTICOBConstants.ZERO);
		return responseInfo;
	}

	public static GoForWealthTICOBResponseInfo getExceptionResponseInfo(GoForWealthTICOBException e, Object data) {
		logger.error("Exception occurred = ", e);
		GoForWealthTICOBResponseInfo responseInfo = new GoForWealthTICOBResponseInfo();
		responseInfo.setStatus(e.getErrorCode());
		responseInfo.setMessage(e.getErrorMessage());
		responseInfo.setData(data);
		responseInfo.setTotalRecords(GoForWealthTICOBConstants.ZERO);
		return responseInfo;
	}

	/** Read the given binary file, and return its contents as a byte array. 
	 * @throws GoForWealthTICOBException 
	 * 
	*/
	public static byte[] read(String aInputFileName) throws GoForWealthTICOBException {
		logger.info("Reading in binary file named : " + aInputFileName);
		File file = new File(aInputFileName);
		logger.info("File size: " + file.length());
		byte[] result = new byte[(int) file.length()];
		try {
			InputStream input = null;
			try {
				int totalBytesRead = 0;
				input = new BufferedInputStream(new FileInputStream(file));
				while (totalBytesRead < result.length) {
					int bytesRemaining = result.length - totalBytesRead;
					int bytesRead = input.read(result, totalBytesRead,bytesRemaining);
					if (bytesRead > 0) {
						totalBytesRead = totalBytesRead + bytesRead;
					}
				}
				logger.info("Num bytes read: " + totalBytesRead);
			} finally {
				logger.info("Closing input stream.");
				input.close();
			}
		} catch (FileNotFoundException ex) {
			logger.error("File with name " + aInputFileName + " not found for encryption.");
			throw new GoForWealthTICOBException(GoForWealthTICOBErrorMessageEnum.ENCRYPTION_ERROR_CODE.getValue(), GoForWealthTICOBErrorMessageEnum.ENCRYPTION_ERROR_MESSAGE.getValue());
		} catch (IOException ex) {
			logger.error(ex.getMessage());
			throw new GoForWealthTICOBException(GoForWealthTICOBErrorMessageEnum.ENCRYPTION_ERROR_CODE.getValue(), GoForWealthTICOBErrorMessageEnum.ENCRYPTION_ERROR_MESSAGE.getValue());
		}
		return result;
	}

	/**
	 * Write a byte array to the given file. Writing binary data is
	 * significantly simpler than reading it.
	 * @throws GoForWealthTICOBException 
	*/
	public static void write(byte[] aInput, String aOutputFileName) throws GoForWealthTICOBException {
		logger.info("Writing binary file : " + aOutputFileName);
		try {
			OutputStream output = null;
			try {
				output = new BufferedOutputStream(new FileOutputStream(aOutputFileName));
				output.write(aInput);
			} finally {
				output.close();
			}
		} catch (FileNotFoundException ex) {
			logger.error("File with name " + aOutputFileName + " not found for encryption.");
			throw new GoForWealthTICOBException(GoForWealthTICOBErrorMessageEnum.ENCRYPTION_ERROR_CODE.getValue(), GoForWealthTICOBErrorMessageEnum.ENCRYPTION_ERROR_MESSAGE.getValue());
		} catch (IOException ex) {
			logger.error(ex.getMessage());
			throw new GoForWealthTICOBException(GoForWealthTICOBErrorMessageEnum.ENCRYPTION_ERROR_CODE.getValue(), GoForWealthTICOBErrorMessageEnum.ENCRYPTION_ERROR_MESSAGE.getValue());
		}
	}

	//******** Read Cams Report data from mail back excel and store into database **********************************//
	public static List<CamsMailbackUserPortfolioData> readCamsDataFromExcelAndStoreToDatabase(MultipartFile excelfile) {
		int update = 0;
		List<CamsMailbackUserPortfolioData> mailbackUserPortfolioDataList = new ArrayList<CamsMailbackUserPortfolioData>();
		try {
			if (excelfile.getOriginalFilename().contains(".xlsx")) {
				int i = 1;
				XSSFWorkbook workbook = new XSSFWorkbook(excelfile.getInputStream()); // for .xlsx
				// HSSFWorkbook workbook = new HSSFWorkbook(excelfile.getInputStream()); //for .xls
				// Creates a worksheet object representing the first sheet
				XSSFSheet worksheet = workbook.getSheetAt(0);
				// HSSFSheet worksheet = workbook.getSheetAt(0);
				while (i <= worksheet.getLastRowNum()) {
					CamsMailbackUserPortfolioData mailbackUserPortfolioData = new CamsMailbackUserPortfolioData();
					XSSFRow xrow = worksheet.getRow(i++);
					// HSSFRow xrow = worksheet.getRow(i++);
					
					if(xrow.getCell(5).getStringCellValue().equals("TICOB")){
						try {
							mailbackUserPortfolioData.setFolioNo(xrow.getCell(1).getStringCellValue());
						} catch (Exception e) {
							e.printStackTrace();
						}
						
						try {
							mailbackUserPortfolioData.setScheme(xrow.getCell(3).getStringCellValue());
						} catch (Exception e) {
							e.printStackTrace();
						}
						
						try {
							int value = new BigDecimal(xrow.getCell(6).getNumericCellValue()).setScale(0, RoundingMode.HALF_UP).intValue();
							mailbackUserPortfolioData.setTrxnNo(String.valueOf(value));
						} catch (Exception e) {
							e.printStackTrace();
						}
						
						try {
							mailbackUserPortfolioData.setInvName(xrow.getCell(4).getStringCellValue());
						} catch (Exception e) {
							e.printStackTrace();
						}
						
						
						
						try {
							DataFormatter dataFormatter = new DataFormatter();
							SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
							String cellStringValue = dataFormatter.formatCellValue(xrow.getCell(11));
							Date date = sdf.parse(cellStringValue);
							SimpleDateFormat sdf1 = new SimpleDateFormat("dd-MM-yyyy");
							mailbackUserPortfolioData.setTradDate(sdf1.format(date));
						} catch (Exception e) {
							e.printStackTrace();
						}
						try {
							DataFormatter dataFormatter = new DataFormatter();
							SimpleDateFormat sdf = new SimpleDateFormat("mm/DD/yyyy");
							String cellStringValue = dataFormatter.formatCellValue(xrow.getCell(12));
							Date date = sdf.parse(cellStringValue);
							SimpleDateFormat sdf1 = new SimpleDateFormat("mm-DD-yyyy");
							mailbackUserPortfolioData.setPostDate(sdf1.format(date));
						} catch (Exception e) {
							e.printStackTrace();
						}
						try {
							mailbackUserPortfolioData.setPurPrice(String.valueOf(xrow.getCell(13).getNumericCellValue()));
						} catch (Exception e) {
							e.printStackTrace();
						}
						try {
							mailbackUserPortfolioData.setUnits(String.valueOf(xrow.getCell(14).getNumericCellValue()));
						} catch (Exception e) {
							e.printStackTrace();
						}
						try {
							mailbackUserPortfolioData.setAmount(String.valueOf(xrow.getCell(15).getNumericCellValue()));
						} catch (Exception e) {
							e.printStackTrace();
						}
						
						try {
							DataFormatter dataFormatter = new DataFormatter();
							SimpleDateFormat sdf = new SimpleDateFormat("mm/DD/yyyy");
							String cellStringValue = dataFormatter.formatCellValue(xrow.getCell(21));
							Date date = sdf.parse(cellStringValue);
							SimpleDateFormat sdf1 = new SimpleDateFormat("mm-DD-yyyy");
							mailbackUserPortfolioData.setRepDate(sdf1.format(date));
						} catch (Exception e) {
							e.printStackTrace();
						}
						
						try {
							mailbackUserPortfolioData.setPan(xrow.getCell(42).getStringCellValue());
						} catch (Exception e) {
							e.printStackTrace();
						}
						
						try {
							mailbackUserPortfolioData.setTrxnType(xrow.getCell(5).getStringCellValue());
						} catch (Exception e) {
							e.printStackTrace();
						}
						
						try {
							mailbackUserPortfolioData.setProdCode(xrow.getCell(2).getStringCellValue());
						} catch (Exception e) {
							e.printStackTrace();
						}
						
						mailbackUserPortfolioDataList.add(mailbackUserPortfolioData);
					}
				}
				workbook.close();
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		System.out.println("Size =====  ===== " + mailbackUserPortfolioDataList.size());
		return mailbackUserPortfolioDataList;
	}

	//******************* Read Karvy Report data from mail back excel and store into database ***********************//
	public static List<KarvyMailBackUserPortfolioData> readKarvyDataFromExcelAndStoreToDatabase(MultipartFile excelfile) {
		int update = 0;
		List<KarvyMailBackUserPortfolioData> mailbackUserPortfolioDataList = new ArrayList<KarvyMailBackUserPortfolioData>();
		try {
				int i = 2;
				XSSFWorkbook workbook = new XSSFWorkbook(excelfile.getInputStream());
				XSSFSheet worksheet = workbook.getSheetAt(0);
				while (i <= worksheet.getLastRowNum()) {
					KarvyMailBackUserPortfolioData mailbackUserPortfolioData = new KarvyMailBackUserPortfolioData();
					XSSFRow xrow = worksheet.getRow(i++);
					
					if(xrow.getCell(37).toString().equals("TI")){
						try {
							mailbackUserPortfolioData.setFolioNumber(String.valueOf(xrow.getCell(2).getNumericCellValue()));
						} catch (Exception e) {
							e.printStackTrace();
						}
						
						
						try {
							mailbackUserPortfolioData.setFundDescription(xrow.getCell(5).getStringCellValue());
						} catch (Exception e) {
							e.printStackTrace();
						}
						
						try {
							int data = (int) xrow.getCell(7).getNumericCellValue();
							mailbackUserPortfolioData.setTransactionNumber(String.valueOf(data));
						} catch (Exception e) {
							e.printStackTrace();
						}
						
						try {
							mailbackUserPortfolioData.setInvestorName(xrow.getCell(10).getStringCellValue());
						} catch (Exception e) {
							e.printStackTrace();
						}
						
						try {
							Date date = xrow.getCell(15).getDateCellValue();
							SimpleDateFormat sdf1 = new SimpleDateFormat("dd-MM-yyyy");
							mailbackUserPortfolioData.setTransactionDate(sdf1.format(date));
						} catch (Exception e) {
							e.printStackTrace();
						}
						try {
							Date date = xrow.getCell(16).getDateCellValue();
							SimpleDateFormat sdf1 = new SimpleDateFormat("dd-MM-yyyy");
							mailbackUserPortfolioData.setProcessDate(sdf1.format(date));
						} catch (Exception e) {
							e.printStackTrace();
						}
						try {
							mailbackUserPortfolioData.setPrice(xrow.getCell(17).getStringCellValue());
						} catch (Exception e) {
							e.printStackTrace();
						}
						
						try {
							mailbackUserPortfolioData.setUnits(String.valueOf(xrow.getCell(19).getNumericCellValue()));
						} catch (Exception e) {
							e.printStackTrace();
						}
						try {
							mailbackUserPortfolioData.setAmount(String.valueOf(xrow.getCell(20).getNumericCellValue()));
						} catch (Exception e) {
							e.printStackTrace();
						}
					
						try {
							Date date = xrow.getCell(27).getDateCellValue();
							SimpleDateFormat sdf1 = new SimpleDateFormat("dd-MM-yyyy");
							mailbackUserPortfolioData.setReportDate(sdf1.format(date));
						} catch (Exception e) {
							e.printStackTrace();
						}
						
						try {
							mailbackUserPortfolioData.setSchemeCode(xrow.getCell(3).getStringCellValue());
						} catch (Exception e) {
							e.printStackTrace();
						}
						
						
						try {
							mailbackUserPortfolioData.setPAN1(xrow.getCell(48).getStringCellValue());
						} catch (Exception e) {
							e.printStackTrace();
						}
						
						
						try {
							mailbackUserPortfolioData.setISIN(xrow.getCell(71).getStringCellValue());
						} catch (Exception e) {
							e.printStackTrace();
						}
						try {
							mailbackUserPortfolioData.setTransactionFlag(xrow.getCell(37).getStringCellValue());
						} catch (Exception e) {
							e.printStackTrace();
						}
						
						mailbackUserPortfolioDataList.add(mailbackUserPortfolioData);
					}
				}
				workbook.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
		System.out.println("Out put ===== " + update);
		return mailbackUserPortfolioDataList;
	}

	
	//******** Read Cams Report data from mail back excel and store into database **********************************//
		/*public static List<CamsMailbackUserPortfolioData> readCamsDataFromExcelAndStoreToDatabase(MultipartFile excelfile) {
			int update = 0;
			List<CamsMailbackUserPortfolioData> mailbackUserPortfolioDataList = new ArrayList<CamsMailbackUserPortfolioData>();
			try {
				if (excelfile.getOriginalFilename().contains(".xlsx")) {
					int i = 1;
					XSSFWorkbook workbook = new XSSFWorkbook(excelfile.getInputStream()); // for .xlsx
					// HSSFWorkbook workbook = new HSSFWorkbook(excelfile.getInputStream()); //for .xls
					// Creates a worksheet object representing the first sheet
					XSSFSheet worksheet = workbook.getSheetAt(0);
					// HSSFSheet worksheet = workbook.getSheetAt(0);
					while (i <= worksheet.getLastRowNum()) {
						CamsMailbackUserPortfolioData mailbackUserPortfolioData = new CamsMailbackUserPortfolioData();
						XSSFRow xrow = worksheet.getRow(i++);
						// HSSFRow xrow = worksheet.getRow(i++);
						try {
							mailbackUserPortfolioData.setAmcCode(xrow.getCell(0).getStringCellValue());
						} catch (Exception e) {
							e.printStackTrace();
						}
						try {
							mailbackUserPortfolioData.setFolioNo(xrow.getCell(1).getStringCellValue());
						} catch (Exception e) {
							e.printStackTrace();
						}
						try {
							mailbackUserPortfolioData.setProdCode(xrow.getCell(2).getStringCellValue());
						} catch (Exception e) {
							e.printStackTrace();
						}
						try {
							mailbackUserPortfolioData.setScheme(xrow.getCell(3).getStringCellValue());
						} catch (Exception e) {
							e.printStackTrace();
						}
						try {
							mailbackUserPortfolioData.setInvName(xrow.getCell(4).getStringCellValue());
						} catch (Exception e) {
							e.printStackTrace();
						}
						try {
							mailbackUserPortfolioData.setTrxnType(xrow.getCell(5).getStringCellValue());
						} catch (Exception e) {
							e.printStackTrace();
						}
						try {
							int value = new BigDecimal(xrow.getCell(6).getNumericCellValue()).setScale(0, RoundingMode.HALF_UP).intValue();
							mailbackUserPortfolioData.setTrxnNo(String.valueOf(value));
						} catch (Exception e) {
							e.printStackTrace();
						}
						try {
							mailbackUserPortfolioData.setTrxnMode(xrow.getCell(7).getStringCellValue());
						} catch (Exception e) {
							e.printStackTrace();
						}
						try {
							mailbackUserPortfolioData.setTrxnStat(xrow.getCell(8).getStringCellValue());
						} catch (Exception e) {
							e.printStackTrace();
						}
						try {
							mailbackUserPortfolioData.setUserCode(xrow.getCell(9).getStringCellValue());
						} catch (Exception e) {
							e.printStackTrace();
						}
						try {
							int value = new BigDecimal(xrow.getCell(10).getNumericCellValue()).setScale(0, RoundingMode.HALF_UP).intValue();
							mailbackUserPortfolioData.setUsrTrxNo(String.valueOf(value));
						} catch (Exception e) {
							e.printStackTrace();
						}
						try {
							DataFormatter dataFormatter = new DataFormatter();
							SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
							String cellStringValue = dataFormatter.formatCellValue(xrow.getCell(11));
							Date date = sdf.parse(cellStringValue);
							SimpleDateFormat sdf1 = new SimpleDateFormat("dd-MM-yyyy");
							mailbackUserPortfolioData.setTradDate(sdf1.format(date));
						} catch (Exception e) {
							e.printStackTrace();
						}
						try {
							DataFormatter dataFormatter = new DataFormatter();
							SimpleDateFormat sdf = new SimpleDateFormat("mm/DD/yyyy");
							String cellStringValue = dataFormatter.formatCellValue(xrow.getCell(12));
							Date date = sdf.parse(cellStringValue);
							SimpleDateFormat sdf1 = new SimpleDateFormat("mm-DD-yyyy");
							mailbackUserPortfolioData.setPostDate(sdf1.format(date));
						} catch (Exception e) {
							e.printStackTrace();
						}
						try {
							mailbackUserPortfolioData.setPurPrice(String.valueOf(xrow.getCell(13).getNumericCellValue()));
						} catch (Exception e) {
							e.printStackTrace();
						}
						try {
							mailbackUserPortfolioData.setUnits(String.valueOf(xrow.getCell(14).getNumericCellValue()));
						} catch (Exception e) {
							e.printStackTrace();
						}
						try {
							mailbackUserPortfolioData.setAmount(String.valueOf(xrow.getCell(15).getNumericCellValue()));
						} catch (Exception e) {
							e.printStackTrace();
						}
						try {
							mailbackUserPortfolioData.setBrokCode(xrow.getCell(16).getStringCellValue());
						} catch (Exception e) {
							e.printStackTrace();
						}
						try {
							mailbackUserPortfolioData.setSubBrok(xrow.getCell(17).getStringCellValue());
						} catch (Exception e) {
							e.printStackTrace();
						}
						try {
							mailbackUserPortfolioData.setBrokPerc(String.valueOf(xrow.getCell(18).getNumericCellValue()));
						} catch (Exception e) {
							e.printStackTrace();
						}
						try {
							mailbackUserPortfolioData.setBrokComm(String.valueOf(xrow.getCell(19).getNumericCellValue()));
						} catch (Exception e) {
							e.printStackTrace();
						}
						try {
							mailbackUserPortfolioData.setAltFolio(xrow.getCell(20).getStringCellValue());
						} catch (Exception e) {
							e.printStackTrace();
						}
						try {
							DataFormatter dataFormatter = new DataFormatter();
							SimpleDateFormat sdf = new SimpleDateFormat("mm/DD/yyyy");
							String cellStringValue = dataFormatter.formatCellValue(xrow.getCell(21));
							Date date = sdf.parse(cellStringValue);
							SimpleDateFormat sdf1 = new SimpleDateFormat("mm-DD-yyyy");
							mailbackUserPortfolioData.setRepDate(sdf1.format(date));
						} catch (Exception e) {
							e.printStackTrace();
						}
						try {
							mailbackUserPortfolioData.setTime1(xrow.getCell(22).getStringCellValue());
						} catch (Exception e) {
							e.printStackTrace();
						}
						try {
							mailbackUserPortfolioData.setTrxnSubType(xrow.getCell(23).getStringCellValue());
						} catch (Exception e) {
							e.printStackTrace();
						}
						try {
							mailbackUserPortfolioData.setApplicationNo(xrow.getCell(24).getStringCellValue());
						} catch (Exception e) {
							e.printStackTrace();
						}
						try {
							mailbackUserPortfolioData.setTrxnNature(xrow.getCell(25).getStringCellValue());
						} catch (Exception e) {
							e.printStackTrace();
						}
						try {
							mailbackUserPortfolioData.setTax(String.valueOf(xrow.getCell(26).getNumericCellValue()));
						} catch (Exception e) {
							e.printStackTrace();
						}
						try {
							mailbackUserPortfolioData.setTotalTax(String.valueOf(xrow.getCell(27).getNumericCellValue()));
						} catch (Exception e) {
							e.printStackTrace();
						}
						try {
							mailbackUserPortfolioData.setTe15h(xrow.getCell(28).getStringCellValue());
						} catch (Exception e) {
							e.printStackTrace();
						}
						try {
							mailbackUserPortfolioData.setMicrNo(xrow.getCell(29).getStringCellValue());
						} catch (Exception e) {
							e.printStackTrace();
						}
						try {
							mailbackUserPortfolioData.setRemarks(xrow.getCell(30).getStringCellValue());
						} catch (Exception e) {
							e.printStackTrace();
						}
						try {
							mailbackUserPortfolioData.setSwFlag(xrow.getCell(31).getStringCellValue());
						} catch (Exception e) {
							e.printStackTrace();
						}
						try {
							mailbackUserPortfolioData.setOldFolio(xrow.getCell(32).getStringCellValue());
						} catch (Exception e) {
							e.printStackTrace();
						}
						try {
							mailbackUserPortfolioData.setSeqNo(String.valueOf(xrow.getCell(33).getNumericCellValue()));
						} catch (Exception e) {
							e.printStackTrace();
						}
						try {
							mailbackUserPortfolioData.setReinvestFlag(xrow.getCell(34).getStringCellValue());
						} catch (Exception e) {
							e.printStackTrace();
						}
						try {
							mailbackUserPortfolioData.setMultBrok(xrow.getCell(35).getStringCellValue());
						} catch (Exception e) {
							e.printStackTrace();
						}
						try {
							mailbackUserPortfolioData.setStt(String.valueOf(xrow.getCell(36).getNumericCellValue()));
						} catch (Exception e) {
							e.printStackTrace();
						}
						try {
							mailbackUserPortfolioData.setLocation(xrow.getCell(37).getStringCellValue());
						} catch (Exception e) {
							e.printStackTrace();
						}
						try {
							mailbackUserPortfolioData.setSchemeType(xrow.getCell(38).getStringCellValue());
						} catch (Exception e) {
							e.printStackTrace();
						}
						try {
							mailbackUserPortfolioData.setTaxStatus(xrow.getCell(39).getStringCellValue());
						} catch (Exception e) {
							e.printStackTrace();
						}
						try {
							mailbackUserPortfolioData.setLoads(String.valueOf(xrow.getCell(40).getNumericCellValue()));
						} catch (Exception e) {
							e.printStackTrace();
						}
						try {
							mailbackUserPortfolioData.setScanRefNo(xrow.getCell(41).getStringCellValue());
						} catch (Exception e) {
							e.printStackTrace();
						}
						try {
							mailbackUserPortfolioData.setPan(xrow.getCell(42).getStringCellValue());
						} catch (Exception e) {
							e.printStackTrace();
						}
						try {
							mailbackUserPortfolioData.setInvIin(String.valueOf(xrow.getCell(43).getNumericCellValue()));
						} catch (Exception e) {
							e.printStackTrace();
						}
						try {
							mailbackUserPortfolioData.setTargSrcScheme(xrow.getCell(44).getStringCellValue());
						} catch (Exception e) {
							e.printStackTrace();
						}
						try {
							mailbackUserPortfolioData.setTrxnTypeFlag(xrow.getCell(45).getStringCellValue());
						} catch (Exception e) {
							e.printStackTrace();
						}
						try {
							mailbackUserPortfolioData.setTicobTrtype(xrow.getCell(46).getStringCellValue());
						} catch (Exception e) {
							e.printStackTrace();
						}
						try {
							mailbackUserPortfolioData.setTicobTrno(xrow.getCell(47).getStringCellValue());
						} catch (Exception e) {
							e.printStackTrace();
						}
						try {
							mailbackUserPortfolioData.setTicobPostedDate(xrow.getCell(48).getStringCellValue());
						} catch (Exception e) {
							e.printStackTrace();
						}
						try {
							mailbackUserPortfolioData.setDpId(xrow.getCell(49).getStringCellValue());
						} catch (Exception e) {
							e.printStackTrace();
						}
						try {
							mailbackUserPortfolioData.setTrxnCharges(String.valueOf(xrow.getCell(50).getNumericCellValue()));
						} catch (Exception e) {
							e.printStackTrace();
						}
						try {
							mailbackUserPortfolioData.setEligibAmt(String.valueOf(xrow.getCell(51).getNumericCellValue()));
						} catch (Exception e) {
							e.printStackTrace();
						}
						try {
							mailbackUserPortfolioData.setSrcOfTxn(xrow.getCell(52).getStringCellValue());
						} catch (Exception e) {
							e.printStackTrace();
						}
						try {
							mailbackUserPortfolioData.setTrxnSuffix(xrow.getCell(53).getStringCellValue());
						} catch (Exception e) {
							e.printStackTrace();
						}
						try {
							mailbackUserPortfolioData.setSipTrxnNo(String.valueOf(xrow.getCell(54).getNumericCellValue()));
						} catch (Exception e) {
							e.printStackTrace();
						}
						try {
							mailbackUserPortfolioData.setTerLocation(xrow.getCell(55).getStringCellValue());
						} catch (Exception e) {
							e.printStackTrace();
						}
						try {
							mailbackUserPortfolioData.setEuin(xrow.getCell(56).getStringCellValue());
						} catch (Exception e) {
							e.printStackTrace();
						}
						try {
							mailbackUserPortfolioData.setEuinValid(xrow.getCell(57).getStringCellValue());
						} catch (Exception e) {
							e.printStackTrace();
						}
						try {
							mailbackUserPortfolioData.setEuinOpted(xrow.getCell(58).getStringCellValue());
						} catch (Exception e) {
							e.printStackTrace();
						}
						try {
							mailbackUserPortfolioData.setSubBrkArn(xrow.getCell(59).getStringCellValue());
						} catch (Exception e) {
							e.printStackTrace();
						}
						try {
							mailbackUserPortfolioData.setExchDcFlag(xrow.getCell(60).getStringCellValue());
						} catch (Exception e) {
							e.printStackTrace();
						}
						try {
							mailbackUserPortfolioData.setSrcBrkCode(xrow.getCell(61).getStringCellValue());
						} catch (Exception e) {
							e.printStackTrace();
						}
						try {
							DataFormatter dataFormatter = new DataFormatter();
							SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
							String cellStringValue = dataFormatter.formatCellValue(xrow.getCell(62));
							Date date = sdf.parse(cellStringValue);
							SimpleDateFormat sdf1 = new SimpleDateFormat("dd-MM-yyyy");
							mailbackUserPortfolioData.setSysRegnDate(sdf1.format(date));
						} catch (Exception e) {
							e.printStackTrace();
						}
						try {
							mailbackUserPortfolioData.setAcNo(xrow.getCell(63).getStringCellValue());
						} catch (Exception e) {
							e.printStackTrace();
						}
						try {
							mailbackUserPortfolioData.setBankName(xrow.getCell(64).getStringCellValue());
						} catch (Exception e) {
							e.printStackTrace();
						}
						try {
							mailbackUserPortfolioData.setReversalCode(String.valueOf(xrow.getCell(65).getNumericCellValue()));
						} catch (Exception e) {
							e.printStackTrace();
						}
						try {
							mailbackUserPortfolioData.setExchangeFlag(xrow.getCell(66).getStringCellValue());
						} catch (Exception e) {
							e.printStackTrace();
						}
						try {
							mailbackUserPortfolioData.setCaInitiatedDate(xrow.getCell(67).getStringCellValue());
						} catch (Exception e) {
							e.printStackTrace();
						}
						try {
							mailbackUserPortfolioData.setGstStateCode(xrow.getCell(68).getStringCellValue());
						} catch (Exception e) {
							e.printStackTrace();
						}
						try {
							mailbackUserPortfolioData.setIgstAmount(String.valueOf(xrow.getCell(69).getNumericCellValue()));
						} catch (Exception e) {
							e.printStackTrace();
						}
						try {
							mailbackUserPortfolioData.setCgstAmount(String.valueOf(xrow.getCell(70).getNumericCellValue()));
						} catch (Exception e) {
							e.printStackTrace();
						}
						try {
							mailbackUserPortfolioData.setSgstAmount(String.valueOf(xrow.getCell(70).getNumericCellValue()));
						} catch (Exception e) {
							e.printStackTrace();
						}
						
						 if (xrow.getCell(72).getStringCellValue() != null&& !xrow.getCell(72).getStringCellValue().isEmpty() && !xrow.getCell(72).getStringCellValue().equals(" ") && !xrow.getCell(72).getStringCellValue().equals("")) {
						 	mailbackUserPortfolioData.setRevRemark(xrow.getCell(72).
						 	getStringCellValue()); 
						 }
						 
						mailbackUserPortfolioDataList.add(mailbackUserPortfolioData);
					}
					workbook.close();
				}else if (excelfile.getOriginalFilename().contains(".xls")) {
					int i = 1;
					// XSSFWorkbook workbook = new XSSFWorkbook(excelfile.getInputStream()); //for .xlsx
					HSSFWorkbook workbook = new HSSFWorkbook(excelfile.getInputStream()); // for .xls
					// Creates a worksheet object representing the first sheet
					// XSSFSheet worksheet = workbook.getSheetAt(0);
					HSSFSheet worksheet = workbook.getSheetAt(0);
					while (i <= worksheet.getLastRowNum())	{
						CamsMailbackUserPortfolioData mailbackUserPortfolioData = new CamsMailbackUserPortfolioData();
						// XSSFRow xrow = worksheet.getRow(i++);
						HSSFRow xrow = worksheet.getRow(i++);
						try {
							mailbackUserPortfolioData.setAmcCode(xrow.getCell(0).getStringCellValue());
						} catch (Exception e) {
							e.printStackTrace();
						}
						try {
							mailbackUserPortfolioData.setFolioNo(xrow.getCell(1).getStringCellValue());
						} catch (Exception e) {
							e.printStackTrace();
						}
						try {
							mailbackUserPortfolioData.setProdCode(xrow.getCell(2).getStringCellValue());
						} catch (Exception e) {
							e.printStackTrace();
						}
						try {
							mailbackUserPortfolioData.setScheme(xrow.getCell(3).getStringCellValue());
						} catch (Exception e) {
							e.printStackTrace();
						}
						try {
							mailbackUserPortfolioData.setInvName(xrow.getCell(4).getStringCellValue());
						} catch (Exception e) {
							e.printStackTrace();
						}
						try {
							mailbackUserPortfolioData.setTrxnType(xrow.getCell(5).getStringCellValue());
						} catch (Exception e) {
							e.printStackTrace();
						}
						try {
							int value = new BigDecimal(xrow.getCell(6).getNumericCellValue()).setScale(0, RoundingMode.HALF_UP).intValue();
							mailbackUserPortfolioData.setTrxnNo(String.valueOf(value));
						} catch (Exception e) {
							e.printStackTrace();
						}
						try {
							mailbackUserPortfolioData.setTrxnMode(xrow.getCell(7).getStringCellValue());
						} catch (Exception e) {
							e.printStackTrace();
						}
						try {
							mailbackUserPortfolioData.setTrxnStat(xrow.getCell(8).getStringCellValue());
						} catch (Exception e) {
							e.printStackTrace();
						}
						try {
							mailbackUserPortfolioData.setUserCode(xrow.getCell(9).getStringCellValue());
						} catch (Exception e) {
							e.printStackTrace();
						}
						try {
							int value = new BigDecimal(xrow.getCell(6).getNumericCellValue()).setScale(0, RoundingMode.HALF_UP).intValue();
							mailbackUserPortfolioData.setUsrTrxNo(String.valueOf(value));
						} catch (Exception e) {
							e.printStackTrace();
						}
						try {
							DataFormatter dataFormatter = new DataFormatter();
							SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
							String cellStringValue = dataFormatter.formatCellValue(xrow.getCell(11));
							Date date = sdf.parse(cellStringValue);
							SimpleDateFormat sdf1 = new SimpleDateFormat("dd-MM-yyyy");
							mailbackUserPortfolioData.setTradDate(sdf1.format(date));
						} catch (Exception e) {
							e.printStackTrace();
						}
						try {
							DataFormatter dataFormatter = new DataFormatter();
							SimpleDateFormat sdf = new SimpleDateFormat("mm/DD/yyyy");
							String cellStringValue = dataFormatter.formatCellValue(xrow.getCell(11));
							Date date = sdf.parse(cellStringValue);
							SimpleDateFormat sdf1 = new SimpleDateFormat("mm-DD-yyyy");
							mailbackUserPortfolioData.setPostDate(sdf1.format(date));
						} catch (Exception e) {
							e.printStackTrace();
						}
						try {
							mailbackUserPortfolioData.setPurPrice(String.valueOf(xrow.getCell(13).getNumericCellValue()));
						} catch (Exception e) {
							e.printStackTrace();
						}
						try {
							mailbackUserPortfolioData.setUnits(String.valueOf(xrow.getCell(14).getNumericCellValue()));
						} catch (Exception e) {
							e.printStackTrace();
						}
						try {
							mailbackUserPortfolioData.setAmount(String.valueOf(xrow.getCell(15).getNumericCellValue()));
						} catch (Exception e) {
							e.printStackTrace();
						}
						try {
							mailbackUserPortfolioData.setBrokCode(xrow.getCell(16).getStringCellValue());
						} catch (Exception e) {
							e.printStackTrace();
						}
						try {
							mailbackUserPortfolioData.setSubBrok(xrow.getCell(17).getStringCellValue());
						} catch (Exception e) {
							e.printStackTrace();
						}
						try {
							mailbackUserPortfolioData.setBrokPerc(String.valueOf(xrow.getCell(18).getNumericCellValue()));
						} catch (Exception e) {
							e.printStackTrace();
						}
						try {
							mailbackUserPortfolioData.setBrokComm(String.valueOf(xrow.getCell(19).getNumericCellValue()));
						} catch (Exception e) {
							e.printStackTrace();
						}
						try {
							mailbackUserPortfolioData.setAltFolio(xrow.getCell(20).getStringCellValue());
						} catch (Exception e) {
							e.printStackTrace();
						}
						try {
							DataFormatter dataFormatter = new DataFormatter();
							SimpleDateFormat sdf = new SimpleDateFormat("mm/DD/yyyy");
							String cellStringValue = dataFormatter.formatCellValue(xrow.getCell(21));
							Date date = sdf.parse(cellStringValue);
							SimpleDateFormat sdf1 = new SimpleDateFormat("mm-DD-yyyy");
							mailbackUserPortfolioData.setRepDate(sdf1.format(date));
						} catch (Exception e) {
							e.printStackTrace();
						}
						try {
							mailbackUserPortfolioData.setTime1(xrow.getCell(22).getStringCellValue());
						} catch (Exception e) {
							e.printStackTrace();
						}
						try {
							mailbackUserPortfolioData.setTrxnSubType(xrow.getCell(23).getStringCellValue());
						} catch (Exception e) {
							e.printStackTrace();
						}
						try {
							mailbackUserPortfolioData.setApplicationNo(xrow.getCell(24).getStringCellValue());
						} catch (Exception e) {
							e.printStackTrace();
						}
						try {
							mailbackUserPortfolioData.setTrxnNature(xrow.getCell(25).getStringCellValue());
						} catch (Exception e) {
							e.printStackTrace();
						}
						try {
							mailbackUserPortfolioData.setTax(String.valueOf(xrow.getCell(26).getNumericCellValue()));
						} catch (Exception e) {
							e.printStackTrace();
						}
						try {
							mailbackUserPortfolioData.setTotalTax(String.valueOf(xrow.getCell(27).getNumericCellValue()));
						} catch (Exception e) {
							e.printStackTrace();
						}
						try {
							mailbackUserPortfolioData.setTe15h(xrow.getCell(28).getStringCellValue());
						} catch (Exception e) {
							e.printStackTrace();
						}
						try {
							mailbackUserPortfolioData.setMicrNo(xrow.getCell(29).getStringCellValue());
						} catch (Exception e) {
							e.printStackTrace();
						}
						try {
							mailbackUserPortfolioData.setRemarks(xrow.getCell(30).getStringCellValue());
						} catch (Exception e) {
							e.printStackTrace();
						}
						try {
							mailbackUserPortfolioData.setSwFlag(xrow.getCell(31).getStringCellValue());
						} catch (Exception e) {
							e.printStackTrace();
						}
						try {
							mailbackUserPortfolioData.setOldFolio(xrow.getCell(32).getStringCellValue());
						} catch (Exception e) {
							e.printStackTrace();
						}
						try {
							mailbackUserPortfolioData.setSeqNo(String.valueOf(xrow.getCell(33).getNumericCellValue()));
						} catch (Exception e) {
							e.printStackTrace();
						}
						try {
							mailbackUserPortfolioData.setReinvestFlag(xrow.getCell(34).getStringCellValue());
						} catch (Exception e) {
							e.printStackTrace();
						}
						try {
							mailbackUserPortfolioData.setMultBrok(xrow.getCell(35).getStringCellValue());
						} catch (Exception e) {
							e.printStackTrace();
						}
						try {
							mailbackUserPortfolioData.setStt(String.valueOf(xrow.getCell(36).getNumericCellValue()));
						} catch (Exception e) {
							e.printStackTrace();
						}
						try {
							mailbackUserPortfolioData.setLocation(xrow.getCell(37).getStringCellValue());
						} catch (Exception e) {
							e.printStackTrace();
						}
						try {
							mailbackUserPortfolioData.setSchemeType(xrow.getCell(38).getStringCellValue());
						} catch (Exception e) {
							e.printStackTrace();
						}
						try {
							mailbackUserPortfolioData.setTaxStatus(xrow.getCell(39).getStringCellValue());
						} catch (Exception e) {
							e.printStackTrace();
						}
						try {
							mailbackUserPortfolioData.setLoads(String.valueOf(xrow.getCell(40).getNumericCellValue()));
						} catch (Exception e) {
							e.printStackTrace();
						}
						try {
							mailbackUserPortfolioData.setScanRefNo(xrow.getCell(41).getStringCellValue());
						} catch (Exception e) {
							e.printStackTrace();
						}
						try {
							mailbackUserPortfolioData.setPan(xrow.getCell(42).getStringCellValue());
						} catch (Exception e) {
							e.printStackTrace();
						}
						try {
							mailbackUserPortfolioData.setInvIin(String.valueOf(xrow.getCell(43).getNumericCellValue()));
						} catch (Exception e) {
							e.printStackTrace();
						}
						try {
							mailbackUserPortfolioData.setTargSrcScheme(xrow.getCell(44).getStringCellValue());
						} catch (Exception e) {
							e.printStackTrace();
						}
						try {
							mailbackUserPortfolioData.setTrxnTypeFlag(xrow.getCell(45).getStringCellValue());
						} catch (Exception e) {
							e.printStackTrace();
						}
						try {
							mailbackUserPortfolioData.setTicobTrtype(xrow.getCell(46).getStringCellValue());
						} catch (Exception e) {
							e.printStackTrace();
						}
						try {
							mailbackUserPortfolioData.setTicobTrno(xrow.getCell(47).getStringCellValue());
						} catch (Exception e) {
							e.printStackTrace();
						}
						try {
							mailbackUserPortfolioData.setTicobPostedDate(xrow.getCell(48).getStringCellValue());
						} catch (Exception e) {
							e.printStackTrace();
						}
						try {
							mailbackUserPortfolioData.setDpId(xrow.getCell(49).getStringCellValue());
						} catch (Exception e) {
							e.printStackTrace();
						}
						try {
							mailbackUserPortfolioData.setTrxnCharges(String.valueOf(xrow.getCell(50).getNumericCellValue()));
						} catch (Exception e) {
							e.printStackTrace();
						}
						try {
							mailbackUserPortfolioData.setEligibAmt(String.valueOf(xrow.getCell(51).getNumericCellValue()));
						} catch (Exception e) {
							e.printStackTrace();
						}
						try {
							mailbackUserPortfolioData.setSrcOfTxn(xrow.getCell(52).getStringCellValue());
						} catch (Exception e) {
							e.printStackTrace();
						}
						try {
							mailbackUserPortfolioData.setTrxnSuffix(xrow.getCell(53).getStringCellValue());
						} catch (Exception e) {
							e.printStackTrace();
						}
						try {
							mailbackUserPortfolioData.setSipTrxnNo(String.valueOf(xrow.getCell(54).getNumericCellValue()));
						} catch (Exception e) {
							e.printStackTrace();
						}
						try {
							mailbackUserPortfolioData.setTerLocation(xrow.getCell(55).getStringCellValue());
						} catch (Exception e) {
							e.printStackTrace();
						}
						try {
							mailbackUserPortfolioData.setEuin(xrow.getCell(56).getStringCellValue());
						} catch (Exception e) {
							e.printStackTrace();
						}
						try {
							mailbackUserPortfolioData.setEuinValid(xrow.getCell(57).getStringCellValue());
						} catch (Exception e) {
							e.printStackTrace();
						}
						try {
							mailbackUserPortfolioData.setEuinOpted(xrow.getCell(58).getStringCellValue());
						} catch (Exception e) {
							e.printStackTrace();
						}
						try {
							mailbackUserPortfolioData.setSubBrkArn(xrow.getCell(59).getStringCellValue());
						} catch (Exception e) {
							e.printStackTrace();
						}
						try {
							mailbackUserPortfolioData.setExchDcFlag(xrow.getCell(60).getStringCellValue());
						} catch (Exception e) {
							e.printStackTrace();
						}
						try {
							mailbackUserPortfolioData.setSrcBrkCode(xrow.getCell(61).getStringCellValue());
						} catch (Exception e) {
							e.printStackTrace();
						}
						try {
							DataFormatter dataFormatter = new DataFormatter();
							SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
							String cellStringValue = dataFormatter.formatCellValue(xrow.getCell(62));
							Date date = sdf.parse(cellStringValue);
							SimpleDateFormat sdf1 = new SimpleDateFormat("dd-MM-yyyy");
							mailbackUserPortfolioData.setSysRegnDate(sdf1.format(date));
						} catch (Exception e) {
							e.printStackTrace();
						}
						try {
							mailbackUserPortfolioData.setAcNo(xrow.getCell(63).getStringCellValue());
						} catch (Exception e) {
							e.printStackTrace();
						}
						try {
							mailbackUserPortfolioData.setBankName(xrow.getCell(64).getStringCellValue());
						} catch (Exception e) {
							e.printStackTrace();
						}
						try {
							mailbackUserPortfolioData.setReversalCode(String.valueOf(xrow.getCell(65).getNumericCellValue()));
						} catch (Exception e) {
							e.printStackTrace();
						}
						try {
							mailbackUserPortfolioData.setExchangeFlag(xrow.getCell(66).getStringCellValue());
						} catch (Exception e) {
							e.printStackTrace();
						}
						try {
							mailbackUserPortfolioData.setCaInitiatedDate(xrow.getCell(67).getStringCellValue());
						} catch (Exception e) {
							e.printStackTrace();
						}
						try {
							mailbackUserPortfolioData.setGstStateCode(xrow.getCell(68).getStringCellValue());
						} catch (Exception e) {
							e.printStackTrace();
						}
						try {
							mailbackUserPortfolioData.setIgstAmount(String.valueOf(xrow.getCell(69).getNumericCellValue()));
						} catch (Exception e) {
							e.printStackTrace();
						}
						try {
							mailbackUserPortfolioData.setCgstAmount(String.valueOf(xrow.getCell(70).getNumericCellValue()));
						} catch (Exception e) {
							e.printStackTrace();
						}
						try {
							mailbackUserPortfolioData.setSgstAmount(String.valueOf(xrow.getCell(70).getNumericCellValue()));
						} catch (Exception e) {
							e.printStackTrace();
						}
						//if (xrow.getCell(72).getStringCellValue() != null&& !xrow.getCell(72).getStringCellValue().isEmpty() && !xrow.getCell(72).getStringCellValue().equals(" ") && !xrow.getCell(72).getStringCellValue().equals("")) {
						//	mailbackUserPortfolioData.setRevRemark(xrow.getCell(72).
						//	getStringCellValue()); 
						//}
						mailbackUserPortfolioDataList.add(mailbackUserPortfolioData);
					}
					workbook.close();
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
			System.out.println("Out put ===== " + update);
			return mailbackUserPortfolioDataList;
		}*/

		//******************* Read Karvy Report data from mail back excel and store into database ***********************//
		/*public static List<KarvyMailBackUserPortfolioData> readKarvyDataFromExcelAndStoreToDatabase(MultipartFile excelfile) {
			int update = 0;
			List<KarvyMailBackUserPortfolioData> mailbackUserPortfolioDataList = new ArrayList<KarvyMailBackUserPortfolioData>();
			try {
				if (excelfile.getOriginalFilename().contains(".xlsx")) {
					int i = 2;
					XSSFWorkbook workbook = new XSSFWorkbook(excelfile.getInputStream());
					XSSFSheet worksheet = workbook.getSheetAt(0);
					while (i <= worksheet.getLastRowNum()) {
						KarvyMailBackUserPortfolioData mailbackUserPortfolioData = new KarvyMailBackUserPortfolioData();
						XSSFRow xrow = worksheet.getRow(i++);
						try {
							mailbackUserPortfolioData.setProductCode(xrow.getCell(0).getStringCellValue());
						} catch (Exception e) {
							e.printStackTrace();
						}
						try {
							mailbackUserPortfolioData.setFund(xrow.getCell(1).getStringCellValue());
						} catch (Exception e) {
							e.printStackTrace();
						}
						try {
							mailbackUserPortfolioData.setFolioNumber(String.valueOf(xrow.getCell(2).getNumericCellValue()));
						} catch (Exception e) {
							e.printStackTrace();
						}
						try {
							mailbackUserPortfolioData.setSchemeCode(xrow.getCell(3).getStringCellValue());
						} catch (Exception e) {
							e.printStackTrace();
						}
						try {
							mailbackUserPortfolioData.setDividendOption(xrow.getCell(4).getStringCellValue());
						} catch (Exception e) {
							e.printStackTrace();
						}
						try {
							mailbackUserPortfolioData.setFundDescription(xrow.getCell(5).getStringCellValue());
						} catch (Exception e) {
							e.printStackTrace();
						}
						try {
							mailbackUserPortfolioData.setTransactionHead(xrow.getCell(6).getStringCellValue());
						} catch (Exception e) {
							e.printStackTrace();
						}
						try {
							int data = (int) xrow.getCell(7).getNumericCellValue();
							mailbackUserPortfolioData.setTransactionNumber(String.valueOf(data));
						} catch (Exception e) {
							e.printStackTrace();
						}
						try {
							mailbackUserPortfolioData.setSwitchRefNo(xrow.getCell(8).getStringCellValue());
						} catch (Exception e) {
							e.printStackTrace();
						}
						try {
							mailbackUserPortfolioData.setInstrumentNumber(xrow.getCell(9).getStringCellValue());
						} catch (Exception e) {
							e.printStackTrace();
						}
						try {
							mailbackUserPortfolioData.setInvestorName(xrow.getCell(10).getStringCellValue());
						} catch (Exception e) {
							e.printStackTrace();
						}
						try {
							mailbackUserPortfolioData.setTransactionMode(xrow.getCell(11).getStringCellValue());
						} catch (Exception e) {
							e.printStackTrace();
						}
						try {
							mailbackUserPortfolioData.setTransactionStatus(xrow.getCell(12).getStringCellValue());
						} catch (Exception e) {
							e.printStackTrace();
						}
						try {
							mailbackUserPortfolioData.setBranchName(xrow.getCell(13).getStringCellValue());
						} catch (Exception e) {
							e.printStackTrace();
						}
						try {
							int data = (int) xrow.getCell(14).getNumericCellValue();
							mailbackUserPortfolioData.setBranchTransactionNo(String.valueOf(data));
						} catch (Exception e) {
							e.printStackTrace();
						}
						try {
							Date date = xrow.getCell(15).getDateCellValue();
							SimpleDateFormat sdf1 = new SimpleDateFormat("dd-MM-yyyy");
							mailbackUserPortfolioData.setTransactionDate(sdf1.format(date));
						} catch (Exception e) {
							e.printStackTrace();
						}
						try {
							Date date = xrow.getCell(16).getDateCellValue();
							SimpleDateFormat sdf1 = new SimpleDateFormat("dd-MM-yyyy");
							mailbackUserPortfolioData.setProcessDate(sdf1.format(date));
						} catch (Exception e) {
							e.printStackTrace();
						}
						try {
							mailbackUserPortfolioData.setPrice(xrow.getCell(17).getStringCellValue());
						} catch (Exception e) {
							e.printStackTrace();
						}
						try {
							int data = (int) xrow.getCell(18).getNumericCellValue();
							mailbackUserPortfolioData.setLoadPercentage(String.valueOf(data));
						} catch (Exception e) {
							e.printStackTrace();
						}
						try {
							mailbackUserPortfolioData.setUnits(String.valueOf(xrow.getCell(19).getNumericCellValue()));
						} catch (Exception e) {
							e.printStackTrace();
						}
						try {
							mailbackUserPortfolioData.setAmount(String.valueOf(xrow.getCell(20).getNumericCellValue()));
						} catch (Exception e) {
							e.printStackTrace();
						}
						try {		
							mailbackUserPortfolioData.setLoadAmount(String.valueOf(xrow.getCell(21).getNumericCellValue()));
						} catch (Exception e) {
							e.printStackTrace();
						}
						try {
							mailbackUserPortfolioData.setAgentCode(xrow.getCell(22).getStringCellValue());
						} catch (Exception e) {
							e.printStackTrace();
						}
						try {
							mailbackUserPortfolioData.setSubBrokerCode(xrow.getCell(23).getStringCellValue());
						} catch (Exception e) {
							e.printStackTrace();
						}
						try {
							mailbackUserPortfolioData.setBrokeragePercentage(String.valueOf(xrow.getCell(24).getNumericCellValue()));
						} catch (Exception e) {
							e.printStackTrace();
						}
						try {
							mailbackUserPortfolioData.setCommission(String.valueOf(xrow.getCell(25).getNumericCellValue()));
						} catch (Exception e) {
							e.printStackTrace();
						}
						try {
							mailbackUserPortfolioData.setInvestorID(xrow.getCell(26).getStringCellValue());
						} catch (Exception e) {
							e.printStackTrace();
						}
						try {
							Date date = xrow.getCell(27).getDateCellValue();
							SimpleDateFormat sdf1 = new SimpleDateFormat("dd-MM-yyyy");
							mailbackUserPortfolioData.setReportDate(sdf1.format(date));
						} catch (Exception e) {
							e.printStackTrace();
						}
						try {
							mailbackUserPortfolioData.setReportTime(xrow.getCell(28).getStringCellValue());
						} catch (Exception e) {
							e.printStackTrace();
						}
						try {
							mailbackUserPortfolioData.setTransactionSub(xrow.getCell(29).getStringCellValue());
						} catch (Exception e) {
							e.printStackTrace();
						}
						try {
							mailbackUserPortfolioData.setApplicationNumber(xrow.getCell(30).getStringCellValue());
						} catch (Exception e) {
							e.printStackTrace();
						}
						try {
							mailbackUserPortfolioData.setTransactionID(xrow.getCell(31).getStringCellValue());
						} catch (Exception e) {
							e.printStackTrace();
						}
						try {
							mailbackUserPortfolioData.setTransactionDescription(xrow.getCell(32).getStringCellValue());
						} catch (Exception e) {
							e.printStackTrace();
						}
						try {
							mailbackUserPortfolioData.setTransactionType(xrow.getCell(33).getStringCellValue());
						} catch (Exception e) {
							e.printStackTrace();
						}
						try {
							Date date = xrow.getCell(34).getDateCellValue();
							SimpleDateFormat sdf1 = new SimpleDateFormat("dd-MM-yyyy");
							mailbackUserPortfolioData.setPurchaseDate(sdf1.format(date));
						} catch (Exception e) {
							e.printStackTrace();
						}
						try {
							mailbackUserPortfolioData.setPurchaseAmount(String.valueOf(xrow.getCell(35).getNumericCellValue()));
						} catch (Exception e) {
							e.printStackTrace();
						}
						try {
							mailbackUserPortfolioData.setPurchaseUnits(String.valueOf(xrow.getCell(36).getNumericCellValue()));
						} catch (Exception e) {
							e.printStackTrace();
						}
						try {
							mailbackUserPortfolioData.setTransactionFlag(xrow.getCell(37).getStringCellValue());
						} catch (Exception e) {
							e.printStackTrace();
						}
						try {
							DataFormatter dataFormatter = new DataFormatter();
							SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
							String cellStringValue = dataFormatter.formatCellValue(xrow.getCell(38));
							Date date = sdf.parse(cellStringValue);
							
							Date date = xrow.getCell(38).getDateCellValue();
							SimpleDateFormat sdf1 = new SimpleDateFormat("dd-MM-yyyy");
							mailbackUserPortfolioData.setSwitchFundDate(sdf1.format(date));
						} catch (Exception e) {
							e.printStackTrace();
						}
						try {
							Date date = xrow.getCell(39).getDateCellValue();
							SimpleDateFormat sdf1 = new SimpleDateFormat("dd-MM-yyyy");
							mailbackUserPortfolioData.setInstrumentDate(sdf1.format(date));
						} catch (Exception e) {
							e.printStackTrace();
						}
						try {
							mailbackUserPortfolioData.setInstrumentBank(xrow.getCell(40).getStringCellValue());
						} catch (Exception e) {
							e.printStackTrace();
						}
						try {
							mailbackUserPortfolioData.setNav(xrow.getCell(41).getStringCellValue());
						} catch (Exception e) {
							e.printStackTrace();
						}
						try {
							int data = (int) xrow.getCell(42).getNumericCellValue();
							mailbackUserPortfolioData.setPurchaseTransactionNo(String.valueOf(data));
						} catch (Exception e) {
							e.printStackTrace();
						}
						try {
							mailbackUserPortfolioData.setSTT(String.valueOf(xrow.getCell(43).getNumericCellValue()));
						} catch (Exception e) {
							e.printStackTrace();
						}
						try {
							int data = (int) xrow.getCell(44).getNumericCellValue();
							mailbackUserPortfolioData.setIhno(String.valueOf(data));
						} catch (Exception e) {
							e.printStackTrace();
						}
						try {
							mailbackUserPortfolioData.setBranchCode(xrow.getCell(45).getStringCellValue());
						} catch (Exception e) {
							e.printStackTrace();
						}
						try {
							mailbackUserPortfolioData.setInwardNumber(xrow.getCell(46).getStringCellValue());
						} catch (Exception e) {
							e.printStackTrace();
						}
						try {
							mailbackUserPortfolioData.setRemarks(xrow.getCell(47).getStringCellValue());
						} catch (Exception e) {
							e.printStackTrace();
						}
						try {
							mailbackUserPortfolioData.setPAN1(xrow.getCell(48).getStringCellValue());
						} catch (Exception e) {
							e.printStackTrace();
						}
						try {
							Date date = xrow.getCell(49).getDateCellValue();
							SimpleDateFormat sdf1 = new SimpleDateFormat("dd-MM-yyyy");
							mailbackUserPortfolioData.setNCTChangeDate(sdf1.format(date));
						} catch (Exception e) {
							e.printStackTrace();
						}
						try {
							mailbackUserPortfolioData.setStatus(xrow.getCell(50).getStringCellValue());
						} catch (Exception e) {
							e.printStackTrace();
						}
						try {
							mailbackUserPortfolioData.setRejTrnoOrgNo(xrow.getCell(51).getStringCellValue());
						} catch (Exception e) {
							e.printStackTrace();
						}
						try {
							mailbackUserPortfolioData.setClientId(xrow.getCell(52).getStringCellValue());
						} catch (Exception e) {
							e.printStackTrace();
						}
						try {
							mailbackUserPortfolioData.setDpId(xrow.getCell(53).getStringCellValue());
						} catch (Exception e) {
							e.printStackTrace();
						}
						try {
							mailbackUserPortfolioData.setTrCharges(String.valueOf(xrow.getCell(54).getNumericCellValue()));
						} catch (Exception e) {
							e.printStackTrace();
						}
						try {
							mailbackUserPortfolioData.setToProductCode(xrow.getCell(55).getStringCellValue());
						} catch (Exception e) {
							e.printStackTrace();
						}
						try {
							Date date = xrow.getCell(56).getDateCellValue();
							SimpleDateFormat sdf1 = new SimpleDateFormat("dd-MM-yyyy");
							mailbackUserPortfolioData.setNavDate(sdf1.format(date));
						} catch (Exception e) {
							e.printStackTrace();
						}
						try {	
							Date date = xrow.getCell(57).getDateCellValue();
							SimpleDateFormat sdf1 = new SimpleDateFormat("dd-MM-yyyy");
							mailbackUserPortfolioData.setPortDate(sdf1.format(date));
						} catch (Exception e) {
							e.printStackTrace();
						}
						try {
							mailbackUserPortfolioData.setAssetType(xrow.getCell(58).getStringCellValue());
						} catch (Exception e) {
							e.printStackTrace();
						}
						try {
							mailbackUserPortfolioData.setSubTranType(xrow.getCell(59).getStringCellValue());
						} catch (Exception e) {
							e.printStackTrace();
						}
						try {
							mailbackUserPortfolioData.setCityCategory(xrow.getCell(60).getStringCellValue());
						} catch (Exception e) {
							e.printStackTrace();
						}
						try {
							mailbackUserPortfolioData.setEUIN(xrow.getCell(61).getStringCellValue());
						} catch (Exception e) {
							e.printStackTrace();
						}
						try {		
							mailbackUserPortfolioData.setSubBrokerARNCode(xrow.getCell(62).getStringCellValue());
						} catch (Exception e) {
							e.printStackTrace();
						}
						try {
							mailbackUserPortfolioData.setPAN2(xrow.getCell(63).getStringCellValue());
						} catch (Exception e) {
							e.printStackTrace();
						}
						try {
							mailbackUserPortfolioData.setPAN3(xrow.getCell(64).getStringCellValue());
						} catch (Exception e) {
							e.printStackTrace();
						}
						try {
							mailbackUserPortfolioData.setTDSAmount(String.valueOf(xrow.getCell(65).getNumericCellValue()));
						} catch (Exception e) {
							e.printStackTrace();
						}
						try {
							mailbackUserPortfolioData.setScheme(xrow.getCell(66).getStringCellValue());
						} catch (Exception e) {
							e.printStackTrace();
						}
						try {
							mailbackUserPortfolioData.setPlan(xrow.getCell(67).getStringCellValue());
						} catch (Exception e) {
							e.printStackTrace();
						}
						try {
							mailbackUserPortfolioData.setTd_trxnmode(xrow.getCell(68).getStringCellValue());
						} catch (Exception e) {
							e.printStackTrace();
						}
						try {
							mailbackUserPortfolioData.setaTMCardStatus(xrow.getCell(69).getStringCellValue());
						} catch (Exception e) {
							e.printStackTrace();
						}
						try {
							mailbackUserPortfolioData.setaTMCardRemarks(xrow.getCell(70).getStringCellValue());
						} catch (Exception e) {
							e.printStackTrace();
						}
						try {
							mailbackUserPortfolioData.setISIN(xrow.getCell(71).getStringCellValue());
						} catch (Exception e) {
							e.printStackTrace();
						}
						try {
							mailbackUserPortfolioData.setNewUnqno(xrow.getCell(72).getStringCellValue());
						} catch (Exception e) {
							e.printStackTrace();
						}
						try {
							mailbackUserPortfolioData.setEUINValidIndicator(xrow.getCell(73).getStringCellValue());
						} catch (Exception e) {
							e.printStackTrace();
						}
						try {
							mailbackUserPortfolioData.setCommonAccountNumber(String.valueOf(xrow.getCell(74).getNumericCellValue()));
						} catch (Exception e) {
							e.printStackTrace();
						}
						try {
							mailbackUserPortfolioData.setEUINDeclarationIndicator(xrow.getCell(75).getStringCellValue());
						} catch (Exception e) {
							e.printStackTrace();
						}
						try {
							mailbackUserPortfolioData.setExchangeOrgTrType(xrow.getCell(76).getStringCellValue());
						} catch (Exception e) {
							e.printStackTrace();
						}
						try {
							mailbackUserPortfolioData.setElectronicTransactionFlag(String.valueOf(xrow.getCell(77).getNumericCellValue()));
						} catch (Exception e) {
							e.printStackTrace();
						}
						try {
							Date date = xrow.getCell(78).getDateCellValue();
							SimpleDateFormat sdf1 = new SimpleDateFormat("dd-MM-yyyy");
							mailbackUserPortfolioData.setSIPRegnDate(sdf1.format(date));
						} catch (Exception e) {
							e.printStackTrace();
						}
						try {
							mailbackUserPortfolioData.setDivPer(xrow.getCell(79).getStringCellValue());
						} catch (Exception e) {
							e.printStackTrace();
						}
						try {
							mailbackUserPortfolioData.setGuardPanNo(xrow.getCell(80).getStringCellValue());
						} catch (Exception e) {
							e.printStackTrace();
						}
						try {
							mailbackUserPortfolioData.setInvestorState(xrow.getCell(81).getStringCellValue());
						} catch (Exception e) {
							e.printStackTrace();
						}
						mailbackUserPortfolioDataList.add(mailbackUserPortfolioData);
					}
					workbook.close();
				} else if (excelfile.getOriginalFilename().contains(".xls")) {
					int i = 1;	
					HSSFWorkbook workbook = new HSSFWorkbook(excelfile.getInputStream());																	
					HSSFSheet worksheet = workbook.getSheetAt(0);
					while (i <= worksheet.getLastRowNum()) {
						KarvyMailBackUserPortfolioData mailbackUserPortfolioData = new KarvyMailBackUserPortfolioData();
						HSSFRow xrow = worksheet.getRow(i++);
						try {
							mailbackUserPortfolioData.setAmcCode(xrow.getCell(0).getStringCellValue());
						} catch (Exception e) {
							e.printStackTrace();
						}
						try {
							mailbackUserPortfolioData.setFolioNo(xrow.getCell(1).getStringCellValue());
						} catch (Exception e) {
							e.printStackTrace();
						}
						try {
							mailbackUserPortfolioData.setProdCode(xrow.getCell(2).getStringCellValue());
						} catch (Exception e) {
							e.printStackTrace();
						}
						try {
							mailbackUserPortfolioData.setScheme(xrow.getCell(3).getStringCellValue());
						} catch (Exception e) {
							e.printStackTrace();
						}
						try {
							mailbackUserPortfolioData.setInvName(xrow.getCell(4).getStringCellValue());
						} catch (Exception e) {
							e.printStackTrace();
						}
						try {
							mailbackUserPortfolioData.setTrxnType(xrow.getCell(5).getStringCellValue());
						} catch (Exception e) {
							e.printStackTrace();
						}
						try {
							int value = new BigDecimal(xrow.getCell(6).getNumericCellValue()).setScale(0, RoundingMode.HALF_UP).intValue();
							mailbackUserPortfolioData.setTrxnNo(String.valueOf(value));
						} catch (Exception e) {
							e.printStackTrace();
						}
						try {
							mailbackUserPortfolioData.setTrxnMode(xrow.getCell(7).getStringCellValue());
						} catch (Exception e) {
							e.printStackTrace();
						}
						try {
							mailbackUserPortfolioData.setTrxnStat(xrow.getCell(8).getStringCellValue());
						} catch (Exception e) {
							e.printStackTrace();
						}
						try {
							mailbackUserPortfolioData.setUserCode(xrow.getCell(9).getStringCellValue());
						} catch (Exception e) {
							e.printStackTrace();
						}
						try {
							int value = new BigDecimal(xrow.getCell(6).getNumericCellValue()).setScale(0, RoundingMode.HALF_UP).intValue();
							mailbackUserPortfolioData.setUsrTrxNo(String.valueOf(value));
						} catch (Exception e) {
							e.printStackTrace();
						}
						try {
							DataFormatter dataFormatter = new DataFormatter();
							SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
							String cellStringValue = dataFormatter.formatCellValue(xrow.getCell(11));
							Date date = sdf.parse(cellStringValue);
							SimpleDateFormat sdf1 = new SimpleDateFormat("dd-MM-yyyy");
							mailbackUserPortfolioData.setTradDate(sdf1.format(date));
						} catch (Exception e) {
							e.printStackTrace();
						}
						try {
							DataFormatter dataFormatter = new DataFormatter();
							SimpleDateFormat sdf = new SimpleDateFormat("mm/DD/yyyy");
							String cellStringValue = dataFormatter.formatCellValue(xrow.getCell(11));
							Date date = sdf.parse(cellStringValue);
							SimpleDateFormat sdf1 = new SimpleDateFormat("mm-DD-yyyy");
							mailbackUserPortfolioData.setPostDate(sdf1.format(date));
						} catch (Exception e) {
							e.printStackTrace();
						}
						try {
							mailbackUserPortfolioData.setPurPrice(String.valueOf(xrow.getCell(13).getNumericCellValue()));
						} catch (Exception e) {
							e.printStackTrace();
						}
						try {
							mailbackUserPortfolioData.setUnits(String.valueOf(xrow.getCell(14).getNumericCellValue()));
						} catch (Exception e) {
							e.printStackTrace();
						}
						try {
							mailbackUserPortfolioData.setAmount(String.valueOf(xrow.getCell(15).getNumericCellValue()));
						} catch (Exception e) {
							e.printStackTrace();
						}
						try {
							mailbackUserPortfolioData.setBrokCode(xrow.getCell(16).getStringCellValue());
						} catch (Exception e) {
							e.printStackTrace();
						}
						try {
							mailbackUserPortfolioData.setSubBrok(xrow.getCell(17).getStringCellValue());
						} catch (Exception e) {
							e.printStackTrace();
						}
						try {
							mailbackUserPortfolioData.setBrokPerc(String.valueOf(xrow.getCell(18).getNumericCellValue()));
						} catch (Exception e) {
							e.printStackTrace();
						}
						try {
							mailbackUserPortfolioData.setBrokComm(String.valueOf(xrow.getCell(19).getNumericCellValue()));
						} catch (Exception e) {
							e.printStackTrace();
						}
						try {
							mailbackUserPortfolioData.setAltFolio(xrow.getCell(20).getStringCellValue());
						} catch (Exception e) {
							e.printStackTrace();
						}
						try {
							DataFormatter dataFormatter = new DataFormatter();
							SimpleDateFormat sdf = new SimpleDateFormat("mm/DD/yyyy");
							String cellStringValue = dataFormatter.formatCellValue(xrow.getCell(21));
							Date date = sdf.parse(cellStringValue);
							SimpleDateFormat sdf1 = new SimpleDateFormat("mm-DD-yyyy");
							mailbackUserPortfolioData.setRepDate(sdf1.format(date));
						} catch (Exception e) {
							e.printStackTrace();
						}
						try {
							mailbackUserPortfolioData.setTime1(xrow.getCell(22).getStringCellValue());
						} catch (Exception e) {
							e.printStackTrace();
						}
						try {
							mailbackUserPortfolioData.setTrxnSubType(xrow.getCell(23).getStringCellValue());
						} catch (Exception e) {
							e.printStackTrace();
						}
						try {
							mailbackUserPortfolioData.setApplicationNo(xrow.getCell(24).getStringCellValue());
						} catch (Exception e) {
							e.printStackTrace();
						}
						try {
							mailbackUserPortfolioData.setTrxnNature(xrow.getCell(25).getStringCellValue());
						} catch (Exception e) {
							e.printStackTrace();
						}
						try {
							mailbackUserPortfolioData.setTax(String.valueOf(xrow.getCell(26).getNumericCellValue()));
						} catch (Exception e) {
							e.printStackTrace();
						}
						try {
							mailbackUserPortfolioData.setTotalTax(String.valueOf(xrow.getCell(27).getNumericCellValue()));
						} catch (Exception e) {
							e.printStackTrace();
						}
						try {
							mailbackUserPortfolioData.setTe15h(xrow.getCell(28).getStringCellValue());
						} catch (Exception e) {
							e.printStackTrace();
						}
						try {
							mailbackUserPortfolioData.setMicrNo(xrow.getCell(29).getStringCellValue());
						} catch (Exception e) {
							e.printStackTrace();
						}
						try {
							mailbackUserPortfolioData.setRemarks(xrow.getCell(30).getStringCellValue());
						} catch (Exception e) {
							e.printStackTrace();
						}
						try {
							mailbackUserPortfolioData.setSwFlag(xrow.getCell(31).getStringCellValue());
						} catch (Exception e) {
							e.printStackTrace();
						}
						try {
							mailbackUserPortfolioData.setOldFolio(xrow.getCell(32).getStringCellValue());
						} catch (Exception e) {
							e.printStackTrace();
						}
						try {
							mailbackUserPortfolioData.setSeqNo(String.valueOf(xrow.getCell(33).getNumericCellValue()));
						} catch (Exception e) {
							e.printStackTrace();
						}
						try {
							mailbackUserPortfolioData.setReinvestFlag(xrow.getCell(34).getStringCellValue());
						} catch (Exception e) {
							e.printStackTrace();
						}
						try {
							mailbackUserPortfolioData.setMultBrok(xrow.getCell(35).getStringCellValue());
						} catch (Exception e) {
							e.printStackTrace();
						}
						try {
							mailbackUserPortfolioData.setStt(String.valueOf(xrow.getCell(36).getNumericCellValue()));
						} catch (Exception e) {
							e.printStackTrace();
						}
						try {
							mailbackUserPortfolioData.setLocation(xrow.getCell(37).getStringCellValue());
						} catch (Exception e) {
							e.printStackTrace();
						}
						try {
							mailbackUserPortfolioData.setSchemeType(xrow.getCell(38).getStringCellValue());
						} catch (Exception e) {
							e.printStackTrace();
						}
						try {
							mailbackUserPortfolioData.setTaxStatus(xrow.getCell(39).getStringCellValue());
						} catch (Exception e) {
							e.printStackTrace();
						}
						try {
							mailbackUserPortfolioData.setLoads(String.valueOf(xrow.getCell(40).getNumericCellValue()));
						} catch (Exception e) {
							e.printStackTrace();
						}
						try {
							mailbackUserPortfolioData.setScanRefNo(xrow.getCell(41).getStringCellValue());
						} catch (Exception e) {
							e.printStackTrace();
						}
						try {
							mailbackUserPortfolioData.setPan(xrow.getCell(42).getStringCellValue());
						} catch (Exception e) {
							e.printStackTrace();
						}
						try {
							mailbackUserPortfolioData.setInvIin(String.valueOf(xrow.getCell(43).getNumericCellValue()));
						} catch (Exception e) {
							e.printStackTrace();
						}
						try {
							mailbackUserPortfolioData.setTargSrcScheme(xrow.getCell(44).getStringCellValue());
						} catch (Exception e) {
							e.printStackTrace();
						}
						try {
							mailbackUserPortfolioData.setTrxnTypeFlag(xrow.getCell(45).getStringCellValue());
						} catch (Exception e) {
							e.printStackTrace();
						}
						try {
							mailbackUserPortfolioData.setTicobTrtype(xrow.getCell(46).getStringCellValue());
						} catch (Exception e) {
							e.printStackTrace();
						}
						try {
							mailbackUserPortfolioData.setTicobTrno(xrow.getCell(47).getStringCellValue());
						} catch (Exception e) {
							e.printStackTrace();
						}
						try {
							mailbackUserPortfolioData.setTicobPostedDate(xrow.getCell(48).getStringCellValue());
						} catch (Exception e) {
							e.printStackTrace();
						}
						try {
							mailbackUserPortfolioData.setDpId(xrow.getCell(49).getStringCellValue());
						} catch (Exception e) {
							e.printStackTrace();
						}
						try {
							mailbackUserPortfolioData.setTrxnCharges(String.valueOf(xrow.getCell(50).getNumericCellValue()));
						} catch (Exception e) {
							e.printStackTrace();
						}
						try {
							mailbackUserPortfolioData.setEligibAmt(String.valueOf(xrow.getCell(51).getNumericCellValue()));
						} catch (Exception e) {
							e.printStackTrace();
						}
						try {
							mailbackUserPortfolioData.setSrcOfTxn(xrow.getCell(52).getStringCellValue());
						} catch (Exception e) {
							e.printStackTrace();
						}
						try {
							mailbackUserPortfolioData.setTrxnSuffix(xrow.getCell(53).getStringCellValue());
						} catch (Exception e) {
							e.printStackTrace();
						}
						try {
							mailbackUserPortfolioData.setSipTrxnNo(String.valueOf(xrow.getCell(54).getNumericCellValue()));
						} catch (Exception e) {
							e.printStackTrace();
						}
						try {
							mailbackUserPortfolioData.setTerLocation(xrow.getCell(55).getStringCellValue());
						} catch (Exception e) {
							e.printStackTrace();
						}
						try {
							mailbackUserPortfolioData.setEuin(xrow.getCell(56).getStringCellValue());
						} catch (Exception e) {
							e.printStackTrace();
						}
						try {
							mailbackUserPortfolioData.setEuinValid(xrow.getCell(57).getStringCellValue());
						} catch (Exception e) {
							e.printStackTrace();
						}
						try {
							mailbackUserPortfolioData.setEuinOpted(xrow.getCell(58).getStringCellValue());
						} catch (Exception e) {
							e.printStackTrace();
						}
						try {
							mailbackUserPortfolioData.setSubBrkArn(xrow.getCell(59).getStringCellValue());
						} catch (Exception e) {
							e.printStackTrace();
						}
						try {
							mailbackUserPortfolioData.setExchDcFlag(xrow.getCell(60).getStringCellValue());
						} catch (Exception e) {
							e.printStackTrace();
						}
						try {
							mailbackUserPortfolioData.setSrcBrkCode(xrow.getCell(61).getStringCellValue());
						} catch (Exception e) {
							e.printStackTrace();
						}
						try {
							DataFormatter dataFormatter = new DataFormatter();
							SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
							String cellStringValue = dataFormatter.formatCellValue(xrow.getCell(62));
							Date date = sdf.parse(cellStringValue);
							SimpleDateFormat sdf1 = new SimpleDateFormat("dd-MM-yyyy");
							mailbackUserPortfolioData.setSysRegnDate(sdf1.format(date));
						} catch (Exception e) {
							e.printStackTrace();
						}
						try {
							mailbackUserPortfolioData.setAcNo(xrow.getCell(63).getStringCellValue());
						} catch (Exception e) {
							e.printStackTrace();
						}
						try {
							mailbackUserPortfolioData.setBankName(xrow.getCell(64).getStringCellValue());
						} catch (Exception e) {
							e.printStackTrace();
						}
						try {
							mailbackUserPortfolioData.setReversalCode(String.valueOf(xrow.getCell(65).getNumericCellValue()));
						} catch (Exception e) {
							e.printStackTrace();
						}
						try {
							mailbackUserPortfolioData.setExchangeFlag(xrow.getCell(66).getStringCellValue());
						} catch (Exception e) {
							e.printStackTrace();
						}
						try {
							mailbackUserPortfolioData.setCaInitiatedDate(xrow.getCell(67).getStringCellValue());
						} catch (Exception e) {
							e.printStackTrace();
						}
						try {
							mailbackUserPortfolioData.setGstStateCode(xrow.getCell(68).getStringCellValue());
						} catch (Exception e) {
							e.printStackTrace();
						}
						try {
							mailbackUserPortfolioData.setIgstAmount(String.valueOf(xrow.getCell(69).getNumericCellValue()));
						} catch (Exception e) {
							e.printStackTrace();
						}
						try {
							mailbackUserPortfolioData.setCgstAmount(String.valueOf(xrow.getCell(70).getNumericCellValue()));
						} catch (Exception e) {
							e.printStackTrace();
						}
						try {
							mailbackUserPortfolioData.setSgstAmount(String.valueOf(xrow.getCell(70).getNumericCellValue()));
						} catch (Exception e) {
							e.printStackTrace();
						}
						//if (xrow.getCell(72).getStringCellValue() != null && !xrow.getCell(72).getStringCellValue().isEmpty() && !xrow.getCell(72).getStringCellValue().equals(" ") && !xrow.getCell(72).getStringCellValue().equals("")) {
						//	mailbackUserPortfolioData.setRevRemark(xrow.getCell(72).
						//	getStringCellValue()); 
						}
						mailbackUserPortfolioDataList.add(mailbackUserPortfolioData);
					}
					workbook.close();
					}
			} catch (Exception e) {
				e.printStackTrace();
			}
			System.out.println("Out put ===== " + update);
			return mailbackUserPortfolioDataList;
		}*/

}
