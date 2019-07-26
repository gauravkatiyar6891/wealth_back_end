package com.moptra.go4wealth.util;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;

import com.moptra.go4wealth.admin.service.GoForWealthAdminService;
import com.moptra.go4wealth.bean.Orders;
import com.moptra.go4wealth.prs.service.GoForWealthFundSchemeService;
import com.moptra.go4wealth.prs.service.GoForWealthPRSEmandateService;
import com.moptra.go4wealth.prs.service.GoForWealthPRSService;
import com.moptra.go4wealth.repository.OrderRepository;
import com.moptra.go4wealth.repository.StoreConfRepository;
import com.moptra.go4wealth.uma.service.GoForWealthUMAService;

@Configuration
@EnableScheduling
public class Go4wealthSchedular {
	
	@Autowired
	GoForWealthPRSService goForWealthPRSService;
	
	@Autowired
	GoForWealthAdminService goForWealthAdminService;
	
	@Autowired
	GoForWealthUMAService goForWealthUMAService;
	
	@Autowired
	OrderRepository orderRepository;
	
	@Autowired
	GoForWealthFundSchemeService goForWealthFundSchemeService;
	
	@Autowired
	GoForWealthPRSEmandateService goForWealthPRSEmandateService;
	
	@Autowired
	StoreConfRepository storeConfRepository;
	
	
	//Need To uncomment While Making War File

	@Scheduled(cron = "0 02 01 * * ?")
	public void savePreviousDateToDBForSipInstallment(){
		goForWealthFundSchemeService.savePreviousDateToDBForSipInstallment();
	}
	
		@Scheduled(cron = "0 15 01,15 * * ?")
		public void updateUserDebitedSipInstallments(){
			goForWealthFundSchemeService.updateUserDebitedSipInstallmentsThroughDates();
		}
	

	@Scheduled(cron = "0 05 04,18 * * ?")
	public void callOrderStatusApi(){
		List<Orders> orderList = orderRepository.getOrderWithStatusPA();
		if(!orderList.isEmpty()){
			for (Orders orders : orderList) {
				goForWealthFundSchemeService.updateOrderStatus(orders.getOrdersId(),orders.getUser().getUserId(),orders.getBseOrderId(),"No");
			}
		}
	}

	@Scheduled(cron = "0 05 05 5,19 * ?")
	public void changePassword(){
		goForWealthPRSEmandateService.changePassword();
	}

	@Scheduled(cron = "0 02 06 * * ?")
	public void updateNatchAndBillerStatusOfUserWithStatusNotApproved(){
		goForWealthFundSchemeService.updateNatchAndBillerStatusOfUserWithStatusNotApproved();
	}

	

	@Scheduled(cron = "0 05 07,20 * * ?")
	public void updateCurrentNavAndCurrentValueForOrder(){
		goForWealthFundSchemeService.updateCurrentNavAndCurrentValueForOrder();
	}

	@Scheduled(cron = "0 15 08,21 * * ?")
	public void  callPaymentServiceApi(){
		List<Orders> orderList = orderRepository.getOrderWithStatusM();
		if(!orderList.isEmpty()){	
			for (Orders orders : orderList) {
				goForWealthFundSchemeService.updatePaymentStatus(orders,orders.getUser());
			}
		}
	}
	
	@Scheduled(cron = "0 05 09,22 * * ?")
    public void updateOrderAndAllotementWithStatusAP(){
    	goForWealthFundSchemeService.updateOrderAndAllotementWithStatusAP();
    }

	@Scheduled(cron = "0 05 10,23 * * ?")
	public void sendReportToUserAfterAllotement(){
		goForWealthFundSchemeService.sendReportToUserAfterAllotement("D");
	}
	/*
	@Scheduled(cron="0 0 0 1 1/1 *")
	public void sendMonthlyReport(){
		goForWealthFundSchemeService.sendReportToUserAfterAllotement("M");
	}*/
	
	

	@Scheduled(cron = "0 30 11 * * ?")
	public void updateInvalidOrderStatusWith(){
	goForWealthFundSchemeService.updateInvalidOrderStatusWithIO();
	}
	
	@Scheduled(cron = "0 05 13 * * ?")
	public void updateOrderRedemptionStatus(){
		goForWealthFundSchemeService.updateOrderRedemptionStatus();
		goForWealthFundSchemeService.updateOrderRedemptionStatusForFollio();
	}
	
}