package com.moptra.go4wealth.prs.service;

import java.util.List;

import com.moptra.go4wealth.bean.OrderItem;
import com.moptra.go4wealth.prs.model.SchemeRecentViewDTO;
import com.moptra.go4wealth.prs.model.UserPortfolioDataDTO;
import com.moptra.go4wealth.ticob.model.CamsMailbackUserPortfolioData;

public interface GoForWealthPRSService {

	//public void addUserOrders(AddToCartRequestDTO addToCartDTO, int userId);

	//public Orders getOrderByUserId(int userId,String status);

	public List<OrderItem> getOrderItemListByOrderId(Integer ordersId, String status);
	
	String checkEkycStatus(int userId);
	
	boolean addToWatchlist(int userId,String schemeCode);

	boolean removeAndPurchaseFromWatchlist(int userId, String schemeCode, String operationType);
	
	List<SchemeRecentViewDTO> getWatchlist(int userId);
	
	//List<UserPortfolioDataDTO> getUserDailyPortfolioData(int userId);
	
	//boolean updateUserPortfolioData(List<CamsMailbackUserPortfolioData> mailbackUserPortfolioData);
}