package com.moptra.go4wealth.prs.service;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.EntityNotFoundException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.moptra.go4wealth.bean.OrderItem;
import com.moptra.go4wealth.bean.PanDetails;
import com.moptra.go4wealth.bean.Scheme;
import com.moptra.go4wealth.bean.User;
import com.moptra.go4wealth.bean.UserWatchlist;
import com.moptra.go4wealth.prs.model.SchemeRecentViewDTO;
import com.moptra.go4wealth.prs.web.GoForWealthPRSController;
import com.moptra.go4wealth.repository.OrderItemRepository;
import com.moptra.go4wealth.repository.OrderRepository;
import com.moptra.go4wealth.repository.PanDetailsRepository;
import com.moptra.go4wealth.repository.SchemeRepository;
import com.moptra.go4wealth.repository.UserRepository;
import com.moptra.go4wealth.repository.UserWatchlistRepository;

/**
 * 
 * @author ranjeet
 *
 */
@Service
public class GoForWealthPRSServiceImpl implements GoForWealthPRSService {

	@Autowired
	OrderRepository orderRepository;
	
	@Autowired
	OrderItemRepository orderItemRepository;
	
	@Autowired
	UserRepository userRepository;
	
	/*@Autowired
	AddressRepository addressRepository;*/
	
	@Autowired
	PanDetailsRepository panDetailsRepository;
	
	@Autowired
	UserWatchlistRepository userWatchlistRepository;
	
	@Autowired
	SchemeRepository schemeRepository;
	
	private static Logger logger = LoggerFactory.getLogger(GoForWealthPRSController.class);
	
	
	@Override
	public List<OrderItem> getOrderItemListByOrderId(Integer ordersId,String status) {
		List<OrderItem> orderItems=orderItemRepository.getOrderItemList(ordersId, status);
		
		return orderItems;
	}
	
	
	@Override
	public String checkEkycStatus(int userId) {
		logger.info("In checkEkycStatus()");
		String message = null;
		try {
			PanDetails panDetailsRef = panDetailsRepository.getOne(userId);
			if(panDetailsRef != null) {
				message = panDetailsRef.getVerified();
			} else {
				return message;
			}
		} catch(Exception ex) {
			ex.printStackTrace();
			if(ex instanceof EntityNotFoundException)
				return message;
		}
		logger.info("Out checkEkycStatus()");
		return message;
	}


	@Override
	@javax.transaction.Transactional
	public boolean addToWatchlist(int userId, String schemeCode) {
		boolean flag = false;
		User user = userRepository.getOne(userId);
		if(user != null) {
			user.getUserWatchlists();
			UserWatchlist userWatchlistRef = userWatchlistRepository.findByUserIdAndSchemeCode(userId,schemeCode);
			if(userWatchlistRef == null){
				try {
					UserWatchlist userWatchlistObj = new UserWatchlist();
					userWatchlistObj.setUser(user);
					userWatchlistObj.setSchemeCode(schemeCode);
					userWatchlistObj.setPurchasedStatus(0);
					userWatchlistObj.setRemovedStatus(0);
					userWatchlistRepository.save(userWatchlistObj);
					flag = true;
				}catch(Exception ex){
					ex.printStackTrace();
				}
			}else{
				userWatchlistRef.setPurchasedStatus(0);
				userWatchlistRef.setRemovedStatus(0);
				userWatchlistRepository.save(userWatchlistRef);
				flag = true;
			}
		}
		return flag;
	}
	
	
	@Override
	@javax.transaction.Transactional
	public boolean removeAndPurchaseFromWatchlist(int userId,String schemeCode,String operationType){
		boolean flag = false;
		User user = userRepository.getOne(userId);
		if(user != null){
			try{
				UserWatchlist userWatchlistRef = userWatchlistRepository.findByUserIdAndSchemeCode(userId,schemeCode);
				if(userWatchlistRef != null){
					if(operationType.equals("remove")){
						userWatchlistRef.setRemovedStatus(1);
						userWatchlistRepository.save(userWatchlistRef);
						flag = true;
					}if(operationType.equals("purchase")){
						userWatchlistRef.setPurchasedStatus(1);
						userWatchlistRepository.save(userWatchlistRef);
						flag = true;
					}
				}
			}catch(Exception ex){
				ex.printStackTrace();
			}
		}
		return flag;
	}


	@Override
	public List<SchemeRecentViewDTO> getWatchlist(int userId) {
		List<SchemeRecentViewDTO> watchlistDtoList = new ArrayList<>();
		int removedStatus = 0, purchasedStatus = 0;
		List<UserWatchlist> userWatchlists = userWatchlistRepository.findByUserIdAndRemovedStatusAndPurchasedStatus(userId,removedStatus,purchasedStatus);
		if(!userWatchlists.isEmpty()){
			for (UserWatchlist userWatchlist : userWatchlists) {
				SchemeRecentViewDTO watchlistDto = new SchemeRecentViewDTO();
				watchlistDto.setSchemeCode(userWatchlist.getSchemeCode());
				Scheme schemeObj = schemeRepository.findBySchemeCode(userWatchlist.getSchemeCode());
				watchlistDto.setSchemeId(schemeObj.getSchemeId());
				watchlistDto.setMinInvestment(schemeObj.getMinimumPurchaseAmount());
				watchlistDto.setSchemeName(schemeObj.getSchemeName());
				watchlistDto.setSchemePlan(schemeObj.getSchemePlan());
				watchlistDto.setSchemeType(schemeObj.getSchemeType());
				watchlistDto.setAmcCode(schemeObj.getAmcCode());
				watchlistDto.setSchemeKeyword(schemeObj.getKeyword());
				watchlistDtoList.add(watchlistDto);
			}
		}
		return watchlistDtoList;
	}


	/*
	@Override
	public List<UserPortfolioDataDTO> getUserDailyPortfolioData(int userId) {
		List<Orders> ordersList = orderRepository.findCompletedOrdersByUser(userId);
		List<UserPortfolioDataDTO> userPortfolioDataDtoList = new ArrayList<>();
		for (Orders orders : ordersList) {
			String bseOrderId = orders.getBseOrderId();
			DailyTransactionReportUserdata dailyTransactionReportUserdata=dailyTransactionReportUserdataRepository.findByUserTrxnNo(bseOrderId);
			UserPortfolioDataDTO userPortfolioDataDto = new UserPortfolioDataDTO();
			Iterator<DailyTransactionReportExtradata> dailyTransactionReportExtradatasIterator = dailyTransactionReportUserdata.getDailyTransactionReportExtradatas().iterator();
			while(dailyTransactionReportExtradatasIterator.hasNext()){
				DailyTransactionReportExtradata dailyTransactionReportExtradata=dailyTransactionReportExtradatasIterator.next();
				if(dailyTransactionReportExtradata.getStatus().equals("1")){
					userPortfolioDataDto.setAcNo(dailyTransactionReportExtradata.getAcNo() != null ? dailyTransactionReportExtradata.getAcNo() : "");
					userPortfolioDataDto.setBrokCode(dailyTransactionReportExtradata.getBrokCode() != null ? dailyTransactionReportExtradata.getBrokCode() : "");
					userPortfolioDataDto.setEuin(dailyTransactionReportExtradata.getEuin() != null ? dailyTransactionReportExtradata.getEuin() : "");
					userPortfolioDataDto.setSrcBrkCode(dailyTransactionReportExtradata.getSrcBrkCode() != null ? dailyTransactionReportExtradata.getSrcBrkCode() : "");
					userPortfolioDataDto.setAmount(dailyTransactionReportExtradata.getAmount() != null ? dailyTransactionReportExtradata.getAmount() : "");
					userPortfolioDataDto.setApplicationNo(dailyTransactionReportExtradata.getApplicationNo() != null ? dailyTransactionReportExtradata.getApplicationNo() : "");
					userPortfolioDataDto.setBankName(dailyTransactionReportExtradata.getBankName() != null ? dailyTransactionReportExtradata.getBankName() : "");
					userPortfolioDataDto.setBrokCode(dailyTransactionReportExtradata.getBrokCode() != null ? dailyTransactionReportExtradata.getBrokCode() : "");
					userPortfolioDataDto.setBrokComm(dailyTransactionReportExtradata.getBrokComm() != null ? dailyTransactionReportExtradata.getBrokComm() : "");
					userPortfolioDataDto.setBrokPerc(dailyTransactionReportExtradata.getBrokPerc() != null ? dailyTransactionReportExtradata.getBrokPerc() : "");
					userPortfolioDataDto.setCaInitiatedDate(dailyTransactionReportExtradata.getCaInitiatedDate() != null ? dailyTransactionReportExtradata.getCaInitiatedDate() : "");
					userPortfolioDataDto.setCgstAmount(dailyTransactionReportExtradata.getCgstAmount() != null ? dailyTransactionReportExtradata.getCgstAmount() : "");
					userPortfolioDataDto.setDpId(dailyTransactionReportExtradata.getDpId() != null ? dailyTransactionReportExtradata.getDpId() : "");
					userPortfolioDataDto.setEligibAmt(dailyTransactionReportExtradata.getEligibAmt() != null ? dailyTransactionReportExtradata.getEligibAmt() : "");
					userPortfolioDataDto.setEuin(dailyTransactionReportExtradata.getEuin() != null ? dailyTransactionReportExtradata.getEuin() : "");
					userPortfolioDataDto.setEuinOpted(dailyTransactionReportExtradata.getEuinOpted() != null ? dailyTransactionReportExtradata.getEuinOpted() : "");
					userPortfolioDataDto.setEuinValid(dailyTransactionReportExtradata.getEuinValid() != null ? dailyTransactionReportExtradata.getEuinValid() : "");
					userPortfolioDataDto.setExchangeFlag(dailyTransactionReportExtradata.getExchangeFlag() != null ? dailyTransactionReportExtradata.getExchangeFlag() : "");
					userPortfolioDataDto.setExchDcFlag(dailyTransactionReportExtradata.getExchDcFlag() != null ? dailyTransactionReportExtradata.getExchDcFlag() : "");
					userPortfolioDataDto.setGstStateCode(dailyTransactionReportExtradata.getGstStateCode() != null ? dailyTransactionReportExtradata.getGstStateCode() : "");
					userPortfolioDataDto.setIgstAmount(dailyTransactionReportExtradata.getIgstAmount() != null ? dailyTransactionReportExtradata.getIgstAmount() : "");
					userPortfolioDataDto.setLocation(dailyTransactionReportExtradata.getLocation() != null ? dailyTransactionReportExtradata.getLocation() : "");
					userPortfolioDataDto.setMultBrok(dailyTransactionReportExtradata.getMultBrok() != null ? dailyTransactionReportExtradata.getMultBrok() : "");
					userPortfolioDataDto.setPan(dailyTransactionReportExtradata.getPan() != null ? dailyTransactionReportExtradata.getPan() : "");
					userPortfolioDataDto.setPostDate(dailyTransactionReportExtradata.getPostDate() != null ? dailyTransactionReportExtradata.getPostDate() : "");
					userPortfolioDataDto.setPurPrice(dailyTransactionReportExtradata.getPurPrice() != null ? dailyTransactionReportExtradata.getPurPrice() : "");
					userPortfolioDataDto.setReinvestFlag(dailyTransactionReportExtradata.getReinvestFlag() != null ? dailyTransactionReportExtradata.getReinvestFlag() : "");
					userPortfolioDataDto.setRemarks(dailyTransactionReportExtradata.getRemarks() != null ? dailyTransactionReportExtradata.getRemarks() : "");
					userPortfolioDataDto.setRepDate(dailyTransactionReportExtradata.getRepDate() != null ? dailyTransactionReportExtradata.getRepDate() : "");
					userPortfolioDataDto.setReversalCode(dailyTransactionReportExtradata.getReversalCode() != null ? dailyTransactionReportExtradata.getReversalCode() : "");
					userPortfolioDataDto.setScanRefNo(dailyTransactionReportExtradata.getScanRefNo() != null ? dailyTransactionReportExtradata.getScanRefNo() : "");
					userPortfolioDataDto.setSeqNo(dailyTransactionReportExtradata.getSeqNo() != null ? dailyTransactionReportExtradata.getSeqNo() : "");
					userPortfolioDataDto.setSgstAmount(dailyTransactionReportExtradata.getSgstAmount() != null ? dailyTransactionReportExtradata.getSgstAmount() : "");
					userPortfolioDataDto.setSrcBrkCode(dailyTransactionReportExtradata.getSrcBrkCode() != null ? dailyTransactionReportExtradata.getSrcBrkCode() : "");
					userPortfolioDataDto.setStatus(dailyTransactionReportExtradata.getStatus() != null ? dailyTransactionReportExtradata.getStatus() : "");
					userPortfolioDataDto.setStt(dailyTransactionReportExtradata.getStt() != null ? dailyTransactionReportExtradata.getStt() : "");
					userPortfolioDataDto.setSubBrkArn(dailyTransactionReportExtradata.getSubBrkArn() != null ? dailyTransactionReportExtradata.getStt() : "");
					userPortfolioDataDto.setSwFlag(dailyTransactionReportExtradata.getSwFlag() != null ? dailyTransactionReportExtradata.getStt() : "");
					userPortfolioDataDto.setSysRegnDate(dailyTransactionReportExtradata.getSysRegnDate() != null ? dailyTransactionReportExtradata.getSysRegnDate() : "");
					userPortfolioDataDto.setTax(dailyTransactionReportExtradata.getTax() != null ? dailyTransactionReportExtradata.getTax() : "");
					userPortfolioDataDto.setTaxStatus(dailyTransactionReportExtradata.getTaxStatus() != null ? dailyTransactionReportExtradata.getTaxStatus() : "");
					userPortfolioDataDto.setTe15h(dailyTransactionReportExtradata.getTe15h() != null ? dailyTransactionReportExtradata.getTe15h() : "");
					userPortfolioDataDto.setLocation(dailyTransactionReportExtradata.getTerLocation() != null ? dailyTransactionReportExtradata.getTerLocation() : "");
					userPortfolioDataDto.setTicobPostedDate(dailyTransactionReportExtradata.getTicobPostedDate() != null ? dailyTransactionReportExtradata.getTicobPostedDate() : "");
					userPortfolioDataDto.setTicobTrno(dailyTransactionReportExtradata.getTicobTrno() != null ? dailyTransactionReportExtradata.getTicobTrno() : "");
					userPortfolioDataDto.setTicobPostedDate(dailyTransactionReportExtradata.getTicobPostedDate() != null ? dailyTransactionReportExtradata.getTicobPostedDate() : "");
					userPortfolioDataDto.setTicobTrtype(dailyTransactionReportExtradata.getTicobTrtype() != null ? dailyTransactionReportExtradata.getTicobTrtype() : "");
					userPortfolioDataDto.setTime1(dailyTransactionReportExtradata.getTime1() != null ? dailyTransactionReportExtradata.getTime1() : "");
					userPortfolioDataDto.setTotalTax(dailyTransactionReportExtradata.getTotalTax() != null ? dailyTransactionReportExtradata.getTotalTax() : "");
					userPortfolioDataDto.setTradDate(dailyTransactionReportExtradata.getTradDate() != null ? dailyTransactionReportExtradata.getTradDate() : "");
					userPortfolioDataDto.setUnits(dailyTransactionReportExtradata.getUnits() != null ? dailyTransactionReportExtradata.getUnits() : "");
				}
			}
			Iterator<DailyTransactionReportTrxndata> dailyTransactionReportTrxndataIterator = dailyTransactionReportUserdata.getDailyTransactionReportTrxndatas().iterator();
			while(dailyTransactionReportTrxndataIterator.hasNext()){
				DailyTransactionReportTrxndata dailyTransactionReportTrxndata = dailyTransactionReportTrxndataIterator.next();
				if(dailyTransactionReportTrxndata.getStatus().equals("1")){
					userPortfolioDataDto.setSipTrxnNo(dailyTransactionReportTrxndata.getSipTrxnNo() != null ? dailyTransactionReportTrxndata.getSipTrxnNo() : "");
					userPortfolioDataDto.setSrcOfTxn(dailyTransactionReportTrxndata.getSrcOfTxn() != null ? dailyTransactionReportTrxndata.getSrcOfTxn() : "");
					userPortfolioDataDto.setStatus(dailyTransactionReportTrxndata.getStatus() != null ? dailyTransactionReportTrxndata.getStatus() : "");
					userPortfolioDataDto.setTrxnCharges(dailyTransactionReportTrxndata.getTrxnCharges() != null ? dailyTransactionReportTrxndata.getTrxnCharges() : "");
					userPortfolioDataDto.setTrxnMode(dailyTransactionReportTrxndata.getTrxnMode() != null ? dailyTransactionReportTrxndata.getTrxnMode() : "");
					userPortfolioDataDto.setTrxnNature(dailyTransactionReportTrxndata.getTrxnNature() != null ? dailyTransactionReportTrxndata.getTrxnNature() : "");
					userPortfolioDataDto.setTrxnNo(dailyTransactionReportTrxndata.getTrxnNo() != null ? dailyTransactionReportTrxndata.getTrxnNo() : "");
					userPortfolioDataDto.setTrxnStat(dailyTransactionReportTrxndata.getTrxnStat() != null ? dailyTransactionReportTrxndata.getTrxnStat() : "");
					userPortfolioDataDto.setTrxnSubType(dailyTransactionReportTrxndata.getTrxnSubType() != null ? dailyTransactionReportTrxndata.getTrxnSubType() : "");
					userPortfolioDataDto.setTrxnSuffix(dailyTransactionReportTrxndata.getTrxnSuffix() != null ? dailyTransactionReportTrxndata.getTrxnSuffix() : "");
					userPortfolioDataDto.setTrxnType(dailyTransactionReportTrxndata.getTrxnType() != null ? dailyTransactionReportTrxndata.getTrxnType() : "");
					userPortfolioDataDto.setTrxnTypeFlag(dailyTransactionReportTrxndata.getTrxnTypeFlag() != null ? dailyTransactionReportTrxndata.getTrxnTypeFlag() : "");
					userPortfolioDataDto.setUsrTrxNo(dailyTransactionReportTrxndata.getUsrTrxNo() != null ? dailyTransactionReportTrxndata.getUsrTrxNo() : "");
				}
			}
			userPortfolioDataDto.setAltFolio(dailyTransactionReportUserdata.getAltFolio() != null ? dailyTransactionReportUserdata.getAltFolio() : "");
			userPortfolioDataDto.setAmcCode(dailyTransactionReportUserdata.getAmcCode() != null ? dailyTransactionReportUserdata.getAmcCode() : "");
			userPortfolioDataDto.setFolioNo(dailyTransactionReportUserdata.getFolioNo() != null ? dailyTransactionReportUserdata.getFolioNo() : "");
			userPortfolioDataDto.setInvIin(dailyTransactionReportUserdata.getInvIin() != null ? dailyTransactionReportUserdata.getInvIin() : "");
			userPortfolioDataDto.setInvName(dailyTransactionReportUserdata.getInvName() != null ? dailyTransactionReportUserdata.getInvName() : "");
			userPortfolioDataDto.setLoads(dailyTransactionReportUserdata.getLoads() != null ? dailyTransactionReportUserdata.getLoads() : "");
			userPortfolioDataDto.setMicrNo(dailyTransactionReportUserdata.getMicrNo() != null ? dailyTransactionReportUserdata.getMicrNo() : "");
			userPortfolioDataDto.setOldFolio(dailyTransactionReportUserdata.getOldFolio() != null ? dailyTransactionReportUserdata.getOldFolio() : "");
			userPortfolioDataDto.setProdCode(dailyTransactionReportUserdata.getProdCode() != null ? dailyTransactionReportUserdata.getProdCode() : "");
			userPortfolioDataDto.setScheme(dailyTransactionReportUserdata.getScheme() != null ? dailyTransactionReportUserdata.getScheme() : "");
			userPortfolioDataDto.setSchemeType(dailyTransactionReportUserdata.getSchemeType() != null ? dailyTransactionReportUserdata.getSchemeType() : "");
			userPortfolioDataDto.setTargSrcScheme(dailyTransactionReportUserdata.getTargSrcScheme() != null ? dailyTransactionReportUserdata.getTargSrcScheme() : "");
			userPortfolioDataDto.setUserCode(dailyTransactionReportUserdata.getUserCode() != null ? dailyTransactionReportUserdata.getUserCode() : "");
			userPortfolioDataDto.setUsrTrxNo(dailyTransactionReportUserdata.getUserTrxnNo() != null ? dailyTransactionReportUserdata.getUserTrxnNo() : "");
			userPortfolioDataDtoList.add(userPortfolioDataDto);
		}
		return userPortfolioDataDtoList;
	}
	
	
	@Override
	public boolean updateUserPortfolioData(List<CamsMailbackUserPortfolioData> mailbackUserPortfolioDataList) {
		boolean isSaved = false;
		List<DailyTransactionReportUserdata> dailyTransactionReportUserdataList = dailyTransactionReportUserdataRepository.findAll();
		if(dailyTransactionReportUserdataList.size()==0){
			for (CamsMailbackUserPortfolioData mailbackUserPortfolioData : mailbackUserPortfolioDataList) {
				isSaved=insertNewRecords(mailbackUserPortfolioData);
			}
		}else{
			for (CamsMailbackUserPortfolioData mailbackUserPortfolioData : mailbackUserPortfolioDataList) {
				DailyTransactionReportUserdata dailyTransactionReportUserdata = dailyTransactionReportUserdataRepository.findByFolioNo(mailbackUserPortfolioData.getFolioNo());
				if(dailyTransactionReportUserdata != null){
					isSaved=updateExistingRecords(mailbackUserPortfolioData,dailyTransactionReportUserdata);
				}else{
					isSaved=insertNewRecords(mailbackUserPortfolioData);
				}
			}
		}
		return isSaved;
	}
	
	
	public boolean insertNewRecords(CamsMailbackUserPortfolioData mailbackUserPortfolioData){
		boolean isSaved = false;
		try{
			DailyTransactionReportUserdata dailyTransactionReportUserdata = new DailyTransactionReportUserdata();
			dailyTransactionReportUserdata.setAltFolio(mailbackUserPortfolioData.getAltFolio());
			dailyTransactionReportUserdata.setAmcCode(mailbackUserPortfolioData.getAmcCode());
			dailyTransactionReportUserdata.setFolioNo(mailbackUserPortfolioData.getFolioNo());
			dailyTransactionReportUserdata.setInvIin(mailbackUserPortfolioData.getInvIin());
			dailyTransactionReportUserdata.setInvName(mailbackUserPortfolioData.getInvName());
			dailyTransactionReportUserdata.setLoads(mailbackUserPortfolioData.getLoads());
			dailyTransactionReportUserdata.setMicrNo(mailbackUserPortfolioData.getMicrNo());
			dailyTransactionReportUserdata.setOldFolio(mailbackUserPortfolioData.getOldFolio());
			dailyTransactionReportUserdata.setProdCode(mailbackUserPortfolioData.getProdCode());
			dailyTransactionReportUserdata.setScheme(mailbackUserPortfolioData.getScheme());
			dailyTransactionReportUserdata.setSchemeType(mailbackUserPortfolioData.getSchemeType());
			dailyTransactionReportUserdata.setTargSrcScheme(mailbackUserPortfolioData.getTargSrcScheme());
			dailyTransactionReportUserdata.setUserCode(mailbackUserPortfolioData.getUserCode());
			dailyTransactionReportUserdata.setUserTrxnNo(mailbackUserPortfolioData.getUsrTrxNo());
		
			DailyTransactionReportTrxndata dailyTransactionReportTrxndata = new DailyTransactionReportTrxndata();
			dailyTransactionReportTrxndata.setDailyTransactionReportUserdata(dailyTransactionReportUserdata);
			dailyTransactionReportTrxndata.setSipTrxnNo(mailbackUserPortfolioData.getSipTrxnNo());
			dailyTransactionReportTrxndata.setSrcOfTxn(mailbackUserPortfolioData.getSrcOfTxn());
			dailyTransactionReportTrxndata.setTrxnCharges(mailbackUserPortfolioData.getTrxnCharges());
			dailyTransactionReportTrxndata.setTrxnMode(mailbackUserPortfolioData.getTrxnMode());
			dailyTransactionReportTrxndata.setTrxnNature(mailbackUserPortfolioData.getTrxnNature());
			dailyTransactionReportTrxndata.setTrxnNo(mailbackUserPortfolioData.getTrxnNo());
			dailyTransactionReportTrxndata.setTrxnStat(mailbackUserPortfolioData.getTrxnStat());
			dailyTransactionReportTrxndata.setTrxnSubType(mailbackUserPortfolioData.getTrxnSubType());
			dailyTransactionReportTrxndata.setTrxnSuffix(mailbackUserPortfolioData.getTrxnSuffix());
			dailyTransactionReportTrxndata.setTrxnTypeFlag(mailbackUserPortfolioData.getTrxnTypeFlag());
			dailyTransactionReportTrxndata.setTrxnType(mailbackUserPortfolioData.getTrxnType());
			dailyTransactionReportTrxndata.setUsrTrxNo(mailbackUserPortfolioData.getUsrTrxNo());
			dailyTransactionReportTrxndata.setStatus("1");
		
			/*DailyTransactionReportRedumptionData dailyTransactionReportRedumptionData = new DailyTransactionReportRedumptionData();
			dailyTransactionReportRedumptionData.setFolioNumber(123456785);
			dailyTransactionReportRedumptionData.setBalanceDate("");
			dailyTransactionReportRedumptionData.setUnitBalance(new BigDecimal(15));
			dailyTransactionReportRedumptionData.setRupeeBalance(new BigDecimal(10));
			dailyTransactionReportRedumptionData.setRedumptionDate("");
			dailyTransactionReportRedumptionData.setRedumptionAmount(new BigDecimal(10));
			dailyTransactionReportRedumptionData.setRedumptionUnit(new BigDecimal(10));
			dailyTransactionReportRedumptionData.setStatus("");
			dailyTransactionReportRedumptionData.setField1("");
			dailyTransactionReportRedumptionData.setField2("");
			dailyTransactionReportRedumptionData.setField3("");
			dailyTransactionReportRedumptionData.setField4(0);
			dailyTransactionReportRedumptionData.setField5(new BigDecimal(0));
			dailyTransactionReportRedumptionData.setDailyTransactionReportUserdata(dailyTransactionReportUserdata);
		
			DailyTransactionReportExtradata dailyTransactionReportExtradata = new DailyTransactionReportExtradata();
			dailyTransactionReportExtradata.setAcNo(mailbackUserPortfolioData.getAcNo());
			dailyTransactionReportExtradata.setAmount(mailbackUserPortfolioData.getAmount());
			dailyTransactionReportExtradata.setApplicationNo(mailbackUserPortfolioData.getApplicationNo());
			dailyTransactionReportExtradata.setBankName(mailbackUserPortfolioData.getBankName());
			dailyTransactionReportExtradata.setBrokCode(mailbackUserPortfolioData.getBrokCode());
			dailyTransactionReportExtradata.setBrokComm(mailbackUserPortfolioData.getBrokComm());
			dailyTransactionReportExtradata.setBrokPerc(mailbackUserPortfolioData.getBrokPerc());
			dailyTransactionReportExtradata.setCaInitiatedDate(mailbackUserPortfolioData.getCaInitiatedDate());
			dailyTransactionReportExtradata.setCgstAmount(mailbackUserPortfolioData.getCgstAmount());
			dailyTransactionReportExtradata.setDailyTransactionReportUserdata(dailyTransactionReportUserdata);
			dailyTransactionReportExtradata.setDpId(mailbackUserPortfolioData.getDpId());
			dailyTransactionReportExtradata.setEligibAmt(mailbackUserPortfolioData.getEligibAmt());
			dailyTransactionReportExtradata.setEuin(mailbackUserPortfolioData.getEuin());
			dailyTransactionReportExtradata.setEuinOpted(mailbackUserPortfolioData.getEuinOpted());
			dailyTransactionReportExtradata.setEuinValid(mailbackUserPortfolioData.getEuinValid());
			dailyTransactionReportExtradata.setExchangeFlag(mailbackUserPortfolioData.getExchangeFlag());
			dailyTransactionReportExtradata.setExchDcFlag(mailbackUserPortfolioData.getExchDcFlag());
			dailyTransactionReportExtradata.setGstStateCode(mailbackUserPortfolioData.getGstStateCode());
			dailyTransactionReportExtradata.setIgstAmount(mailbackUserPortfolioData.getIgstAmount());
			dailyTransactionReportExtradata.setLocation(mailbackUserPortfolioData.getLocation());
			dailyTransactionReportExtradata.setMultBrok(mailbackUserPortfolioData.getMultBrok());
			dailyTransactionReportExtradata.setPan(mailbackUserPortfolioData.getPan());
			dailyTransactionReportExtradata.setPostDate(mailbackUserPortfolioData.getPostDate());
			dailyTransactionReportExtradata.setPurPrice(mailbackUserPortfolioData.getPurPrice());
			dailyTransactionReportExtradata.setReinvestFlag(mailbackUserPortfolioData.getReinvestFlag());
			dailyTransactionReportExtradata.setRemarks(mailbackUserPortfolioData.getRemarks());
			dailyTransactionReportExtradata.setRepDate(mailbackUserPortfolioData.getRepDate());
			dailyTransactionReportExtradata.setReversalCode(mailbackUserPortfolioData.getReversalCode());
			dailyTransactionReportExtradata.setScanRefNo(mailbackUserPortfolioData.getScanRefNo());
			dailyTransactionReportExtradata.setSeqNo(mailbackUserPortfolioData.getSeqNo());
			dailyTransactionReportExtradata.setSgstAmount(mailbackUserPortfolioData.getSgstAmount());
			dailyTransactionReportExtradata.setSrcBrkCode(mailbackUserPortfolioData.getSrcBrkCode());
			dailyTransactionReportExtradata.setStt(mailbackUserPortfolioData.getStt());
			dailyTransactionReportExtradata.setSubBrkArn(mailbackUserPortfolioData.getSubBrkArn());
			dailyTransactionReportExtradata.setSubBrok(mailbackUserPortfolioData.getSubBrok());
			dailyTransactionReportExtradata.setSwFlag(mailbackUserPortfolioData.getSwFlag());
			dailyTransactionReportExtradata.setSysRegnDate(mailbackUserPortfolioData.getSysRegnDate());
			dailyTransactionReportExtradata.setTax(mailbackUserPortfolioData.getTax());
			dailyTransactionReportExtradata.setTaxStatus(mailbackUserPortfolioData.getTaxStatus());
			dailyTransactionReportExtradata.setTe15h(mailbackUserPortfolioData.getTe15h());
			dailyTransactionReportExtradata.setTerLocation(mailbackUserPortfolioData.getTerLocation());
			dailyTransactionReportExtradata.setTicobPostedDate(mailbackUserPortfolioData.getTicobPostedDate());
			dailyTransactionReportExtradata.setTicobTrno(mailbackUserPortfolioData.getTicobTrno());
			dailyTransactionReportExtradata.setTicobTrtype(mailbackUserPortfolioData.getTicobTrtype());
			dailyTransactionReportExtradata.setTime1(mailbackUserPortfolioData.getTime1());
			dailyTransactionReportExtradata.setTotalTax(mailbackUserPortfolioData.getTotalTax());
			dailyTransactionReportExtradata.setTradDate(mailbackUserPortfolioData.getTradDate());
			dailyTransactionReportExtradata.setUnits(mailbackUserPortfolioData.getUnits());
			dailyTransactionReportExtradata.setStatus("1");
		
			dailyTransactionReportUserdata.getDailyTransactionReportTrxndatas().add(dailyTransactionReportTrxndata);
			dailyTransactionReportUserdata.getDailyTransactionReportExtradatas().add(dailyTransactionReportExtradata);
			//dailyTransactionReportUserdata.getDailyTransactionReportRedumptionDatas().add(dailyTransactionReportRedumptionData);
			dailyTransactionReportUserdataRepository.save(dailyTransactionReportUserdata);
			isSaved=true;
		}catch(Exception ex){
			ex.printStackTrace();
			isSaved=false;
		}
		return isSaved;
	}
	
	
	public boolean updateExistingRecords(CamsMailbackUserPortfolioData mailbackUserPortfolioData, DailyTransactionReportUserdata dailyTransactionReportUserdata){
		boolean isSaved = false;
		try{
			Set<DailyTransactionReportTrxndata> dailyTransactionReportTrxndataSet = dailyTransactionReportUserdata.getDailyTransactionReportTrxndatas();
			if(dailyTransactionReportTrxndataSet.size()>0){
				List<DailyTransactionReportTrxndata> dailyTransactionReportTrxndataList=new ArrayList<>(dailyTransactionReportTrxndataSet);
				for (DailyTransactionReportTrxndata dailyTransactionReportTrxndata : dailyTransactionReportTrxndataList) {
					dailyTransactionReportTrxndata.setStatus("0");
					dailyTransactionReportTrxndataRepository.save(dailyTransactionReportTrxndata);
				}
			}
			Set<DailyTransactionReportExtradata> dailyTransactionReportExtradatasSet = dailyTransactionReportUserdata.getDailyTransactionReportExtradatas();
			if(dailyTransactionReportExtradatasSet.size()>0){
				List<DailyTransactionReportExtradata> dailyTransactionReportExtradataList=new ArrayList<>(dailyTransactionReportExtradatasSet);
				for (DailyTransactionReportExtradata dailyTransactionReportExtradata : dailyTransactionReportExtradataList) {
					dailyTransactionReportExtradata.setStatus("0");
					dailyTransactionReportExtradataRepository.save(dailyTransactionReportExtradata);
				}
			}
			DailyTransactionReportTrxndata dailyTransactionReportTrxndata = new DailyTransactionReportTrxndata();
			dailyTransactionReportTrxndata.setSipTrxnNo(mailbackUserPortfolioData.getSipTrxnNo());
			dailyTransactionReportTrxndata.setSrcOfTxn(mailbackUserPortfolioData.getSrcOfTxn());
			dailyTransactionReportTrxndata.setTrxnCharges(mailbackUserPortfolioData.getTrxnCharges());
			dailyTransactionReportTrxndata.setTrxnMode(mailbackUserPortfolioData.getTrxnMode());
			dailyTransactionReportTrxndata.setTrxnNature(mailbackUserPortfolioData.getTrxnNature());
			dailyTransactionReportTrxndata.setTrxnNo(mailbackUserPortfolioData.getTrxnNo());
			dailyTransactionReportTrxndata.setTrxnStat(mailbackUserPortfolioData.getTrxnStat());
			dailyTransactionReportTrxndata.setTrxnSubType(mailbackUserPortfolioData.getTrxnSubType());
			dailyTransactionReportTrxndata.setTrxnSuffix(mailbackUserPortfolioData.getTrxnSuffix());
			dailyTransactionReportTrxndata.setTrxnTypeFlag(mailbackUserPortfolioData.getTrxnTypeFlag());
			dailyTransactionReportTrxndata.setTrxnType(mailbackUserPortfolioData.getTrxnType());
			dailyTransactionReportTrxndata.setUsrTrxNo(mailbackUserPortfolioData.getUsrTrxNo());
			dailyTransactionReportTrxndata.setStatus("1");
			dailyTransactionReportTrxndata.setDailyTransactionReportUserdata(dailyTransactionReportUserdata);
			dailyTransactionReportTrxndataRepository.save(dailyTransactionReportTrxndata);
			
			DailyTransactionReportExtradata dailyTransactionReportExtradata = new DailyTransactionReportExtradata();
			dailyTransactionReportExtradata.setAcNo(mailbackUserPortfolioData.getAcNo());
			dailyTransactionReportExtradata.setAmount(mailbackUserPortfolioData.getAmount());
			dailyTransactionReportExtradata.setApplicationNo(mailbackUserPortfolioData.getApplicationNo());
			dailyTransactionReportExtradata.setBankName(mailbackUserPortfolioData.getBankName());
			dailyTransactionReportExtradata.setBrokCode(mailbackUserPortfolioData.getBrokCode());
			dailyTransactionReportExtradata.setBrokComm(mailbackUserPortfolioData.getBrokComm());
			dailyTransactionReportExtradata.setBrokPerc(mailbackUserPortfolioData.getBrokPerc());
			dailyTransactionReportExtradata.setCaInitiatedDate(mailbackUserPortfolioData.getCaInitiatedDate());
			dailyTransactionReportExtradata.setCgstAmount(mailbackUserPortfolioData.getCgstAmount());
			dailyTransactionReportExtradata.setDpId(mailbackUserPortfolioData.getDpId());
			dailyTransactionReportExtradata.setEligibAmt(mailbackUserPortfolioData.getEligibAmt());
			dailyTransactionReportExtradata.setEuin(mailbackUserPortfolioData.getEuin());
			dailyTransactionReportExtradata.setEuinOpted(mailbackUserPortfolioData.getEuinOpted());
			dailyTransactionReportExtradata.setEuinValid(mailbackUserPortfolioData.getEuinValid());
			dailyTransactionReportExtradata.setExchangeFlag(mailbackUserPortfolioData.getExchangeFlag());
			dailyTransactionReportExtradata.setExchDcFlag(mailbackUserPortfolioData.getExchDcFlag());
			dailyTransactionReportExtradata.setGstStateCode(mailbackUserPortfolioData.getGstStateCode());
			dailyTransactionReportExtradata.setIgstAmount(mailbackUserPortfolioData.getIgstAmount());
			dailyTransactionReportExtradata.setLocation(mailbackUserPortfolioData.getLocation());
			dailyTransactionReportExtradata.setMultBrok(mailbackUserPortfolioData.getMultBrok());
			dailyTransactionReportExtradata.setPan(mailbackUserPortfolioData.getPan());
			dailyTransactionReportExtradata.setPostDate(mailbackUserPortfolioData.getPostDate());
			dailyTransactionReportExtradata.setPurPrice(mailbackUserPortfolioData.getPurPrice());
			dailyTransactionReportExtradata.setReinvestFlag(mailbackUserPortfolioData.getReinvestFlag());
			dailyTransactionReportExtradata.setRemarks(mailbackUserPortfolioData.getRemarks());
			dailyTransactionReportExtradata.setRepDate(mailbackUserPortfolioData.getRepDate());
			dailyTransactionReportExtradata.setReversalCode(mailbackUserPortfolioData.getReversalCode());
			dailyTransactionReportExtradata.setScanRefNo(mailbackUserPortfolioData.getScanRefNo());
			dailyTransactionReportExtradata.setSeqNo(mailbackUserPortfolioData.getSeqNo());
			dailyTransactionReportExtradata.setSgstAmount(mailbackUserPortfolioData.getSgstAmount());
			dailyTransactionReportExtradata.setSrcBrkCode(mailbackUserPortfolioData.getSrcBrkCode());
			dailyTransactionReportExtradata.setStt(mailbackUserPortfolioData.getStt());
			dailyTransactionReportExtradata.setSubBrkArn(mailbackUserPortfolioData.getSubBrkArn());
			dailyTransactionReportExtradata.setSubBrok(mailbackUserPortfolioData.getSubBrok());
			dailyTransactionReportExtradata.setSwFlag(mailbackUserPortfolioData.getSwFlag());
			dailyTransactionReportExtradata.setSysRegnDate(mailbackUserPortfolioData.getSysRegnDate());
			dailyTransactionReportExtradata.setTax(mailbackUserPortfolioData.getTax());
			dailyTransactionReportExtradata.setTaxStatus(mailbackUserPortfolioData.getTaxStatus());
			dailyTransactionReportExtradata.setTe15h(mailbackUserPortfolioData.getTe15h());
			dailyTransactionReportExtradata.setTerLocation(mailbackUserPortfolioData.getTerLocation());
			dailyTransactionReportExtradata.setTicobPostedDate(mailbackUserPortfolioData.getTicobPostedDate());
			dailyTransactionReportExtradata.setTicobTrno(mailbackUserPortfolioData.getTicobTrno());
			dailyTransactionReportExtradata.setTicobTrtype(mailbackUserPortfolioData.getTicobTrtype());
			dailyTransactionReportExtradata.setTime1(mailbackUserPortfolioData.getTime1());
			dailyTransactionReportExtradata.setTotalTax(mailbackUserPortfolioData.getTotalTax());
			dailyTransactionReportExtradata.setTradDate(mailbackUserPortfolioData.getTradDate());
			dailyTransactionReportExtradata.setUnits(mailbackUserPortfolioData.getUnits());
			dailyTransactionReportExtradata.setStatus("1");
			dailyTransactionReportExtradata.setDailyTransactionReportUserdata(dailyTransactionReportUserdata);
			dailyTransactionReportExtradataRepository.save(dailyTransactionReportExtradata);
			isSaved = true;
		}catch(Exception ex){
			ex.printStackTrace();
			isSaved = false;
		}
		return isSaved;
	}
	*/
	


}