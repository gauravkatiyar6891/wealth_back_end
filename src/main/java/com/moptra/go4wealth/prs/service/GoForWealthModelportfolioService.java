package com.moptra.go4wealth.prs.service;

import java.util.List;

import com.moptra.go4wealth.bean.User;
import com.moptra.go4wealth.prs.common.exception.GoForWealthPRSException;
import com.moptra.go4wealth.prs.common.rest.GoForWealthPRSResponseInfo;
import com.moptra.go4wealth.prs.model.AddToCartDTO;
import com.moptra.go4wealth.prs.model.FundSchemeDTO;

public interface GoForWealthModelportfolioService {
	
	List<FundSchemeDTO> getModelportfolioList();
	
	List<FundSchemeDTO> getModelportfolioDetailByCategory(String portfolioName);
	
	List<FundSchemeDTO> getModelportfolioDetailByCategorySecured(String portfolioName,Integer userId);

	List<AddToCartDTO> addToCart(List<AddToCartDTO> addToCartDTO, User user);

	List<AddToCartDTO> confirmOrder(Integer userId, List<AddToCartDTO> addToCartDTOs);

	List<AddToCartDTO> cancelOrder(List<AddToCartDTO> addToCartDTOs, Integer userId);

	String deleteOrder(Integer userId, Integer orderId);
	
	List<AddToCartDTO> getCartOrderByOrder(Integer userId, int bundelId);

	String makePayment(List<AddToCartDTO> addToCartDTO, Integer userId) throws GoForWealthPRSException;

	List<AddToCartDTO> getPaymentStatus(Integer mpBundleId);

	List<AddToCartDTO> getUserOrderByType(Integer userId, String type);

	List<AddToCartDTO> getUserOrderListByBundleId(Integer userId, Integer bundleId);

	FundSchemeDTO getUserOrderByType(User user);

}