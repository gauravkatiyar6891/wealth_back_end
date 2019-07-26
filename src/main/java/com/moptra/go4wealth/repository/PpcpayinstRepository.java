package com.moptra.go4wealth.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.moptra.go4wealth.bean.Ppcpayinst;

public interface PpcpayinstRepository extends JpaRepository<Ppcpayinst, Integer> {
	
	@Query("FROM Ppcpayinst as ppcpayinst WHERE ppcpayinst.orders.ordersId=:ordersId and ppcpayinst.state ='P' order by ppcpayinst.timeupdated DESC")
	List<Ppcpayinst> findByOrderId(@Param("ordersId")  Integer ordersId);
	
	@Query("FROM Ppcpayinst as ppcpayinst WHERE ppcpayinst.orders.ordersId=:ordersId and ppcpayinst.state ='PA' order by ppcpayinst.timeupdated DESC")
	List<Ppcpayinst> findByOrderIdWithStatusPA(@Param("ordersId")  Integer ordersId);
	
	@Query("FROM Ppcpayinst as ppcpayinst WHERE ppcpayinst.orders.ordersId=:ordersId and ppcpayinst.state ='AP' order by ppcpayinst.timeupdated DESC")
	List<Ppcpayinst> findByOrderIdWithStatusAP(@Param("ordersId")  Integer ordersId);
	
	@Query("FROM Ppcpayinst as ppcpayinst WHERE ppcpayinst.orders.ordersId=:ordersId and (ppcpayinst.state ='AP' or ppcpayinst.state ='AD' or ppcpayinst.state ='C') order by ppcpayinst.timeupdated DESC")
	List<Ppcpayinst> findByOrderIdWithStatusAPOrAD(@Param("ordersId")  Integer ordersId);

	@Query("FROM Ppcpayinst as ppcpayinst WHERE ppcpayinst.orders.ordersId=:ordersId order by ppcpayinst.timeupdated DESC")
	List<Ppcpayinst> getAllUserTransactionByOrders(@Param("ordersId") Integer ordersId);

}
