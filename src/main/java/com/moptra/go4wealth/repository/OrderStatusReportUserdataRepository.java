package com.moptra.go4wealth.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.moptra.go4wealth.bean.AllotmentStatusReportUserdata;
import com.moptra.go4wealth.bean.OrderStatusReportUserdata;

public interface OrderStatusReportUserdataRepository extends JpaRepository<OrderStatusReportUserdata, Integer>{

	@Query("FROM OrderStatusReportUserdata as c WHERE c.orderNumber= :orderId")
	OrderStatusReportUserdata getOrderStatusByOrderId(@Param("orderId")String orderId);

	@Query("FROM OrderStatusReportUserdata as o where o.clientCode =:clientCode GROUP BY o.sipRegnNo")
	List<OrderStatusReportUserdata> getDetailByClientCode(@Param("clientCode") String clientCode);

	@Query("FROM OrderStatusReportUserdata as o WHERE o.sipRegnNo = :sipRegnNo")
	List<OrderStatusReportUserdata> getDebitedInstallmentLastUpdated(@Param("sipRegnNo") Long sipRegnNo);

	List<OrderStatusReportUserdata> findBySipRegnNo(Long sipRegnNo);

	List<OrderStatusReportUserdata> findByClientCode(String clientCode);

	@Query("FROM OrderStatusReportUserdata as o where o.status ='AP'")
	List<OrderStatusReportUserdata> getDetailWithStatusAP();

	List<OrderStatusReportUserdata> findByFolioNo(String folioNumber);

	@Query("From OrderStatusReportUserdata as a where a.folioNo =:folioNo and a.schemeCode =:schemeCode and (a.status = 'AP' or a.status = 'AD' or a.status = 'PR' or a.status = 'OD')")
	List<OrderStatusReportUserdata> getAllotementByFollioNoAndSchemeCode(@Param("folioNo") String folioNo,@Param("schemeCode") String schemeCode);



}
