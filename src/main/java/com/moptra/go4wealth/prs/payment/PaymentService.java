package com.moptra.go4wealth.prs.payment;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.moptra.go4wealth.bean.Orders;
import com.moptra.go4wealth.prs.common.exception.GoForWealthPRSException;

@Component
public class PaymentService {

	@Autowired
	PaymentServiceImpl paymentServiceImpl;

	public String getPassword() throws GoForWealthPRSException{
		String password=null;
		password=paymentServiceImpl.getPassword();
		return password;
	}

	public String doPayment(int userId,String[] orders,String amount,String idList) throws GoForWealthPRSException {
		String paymentResponse = paymentServiceImpl.doPayment(userId,orders,amount,idList);
		return paymentResponse;

	}
	
	public String doBulkOrderPayment(int userId,String[] orders,String amount) throws GoForWealthPRSException {
		String paymentResponse = paymentServiceImpl.doBulkOrderPayment(userId,orders,amount);
		return paymentResponse;

	}

	public String isOrderAuthenticated(Orders order, Integer userId) throws GoForWealthPRSException {
		String response = paymentServiceImpl.isOrderAuthenticated(order,userId);
		return response;
	}

	
}
