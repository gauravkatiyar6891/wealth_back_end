package com.moptra.go4wealth.repository;

import java.util.List;

import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.moptra.go4wealth.bean.Orders;
import com.moptra.go4wealth.bean.Scheme;

public interface OrderRepository extends JpaRepository<Orders, Integer>{

	@Query("FROM Orders as c WHERE c.user.userId=:userId and c.status=:status")
	Orders getUserOrders(@Param("userId") Integer userId,@Param("status") String status);
	
	@Query("FROM Orders as c WHERE c.user.userId=:userId and c.type=:type and (c.status='P' or c.status='M'or c.status='C'or c.status='R') order by c.lastupdate DESC")
	List<Orders> findOrdersByUser(@Param("userId") Integer userId, @Param("type") String type);
	
	@Query("FROM Orders as c WHERE c.user.userId=:userId and c.type=:type and c.status='M' order by c.lastupdate DESC")
	List<Orders> findConfirmOrdersByUser(@Param("userId") Integer userId, @Param("type") String type);

	@Query("FROM Orders as c WHERE c.user.userId=:userId and (c.status='C') order by c.lastupdate DESC")
	List<Orders> findCompletedOrdersByUser(@Param("userId") Integer userId);

	@Query(" From Scheme sch   WHERE  sch.schemeCode=:toSchemeCode")
	Scheme getSchemeDetailBySchemeCode(@Param("toSchemeCode") String toSchemeCode);
	
	
	@Query("FROM Orders as c WHERE c.user.userId=:userId and (c.status='C') order by c.lastupdate DESC")
	List<Orders> findCompletedOrdersByUserForTransaction(@Param("userId") Integer userId);

	@Query("FROM Orders as c WHERE c.user.userId=:userId and c.type=:type and (c.status='P' or c.status='M') order by c.lastupdate DESC")
	List<Orders> findOrdersByUserSip(@Param("userId") Integer userId, @Param("type") String type);
	
	@Query("FROM Orders as c WHERE c.user.userId=:userId and c.type=:type and (c.status='P' or c.status='M') order by c.lastupdate DESC")
	List<Orders> findOrdersByUserLumpsum(@Param("userId") Integer userId, @Param("type") String type);
	
	@Query("FROM Orders as c WHERE c.user.userId=:userId  and (c.status='C') order by c.lastupdate DESC")
	List<Orders> findOrdersByUserInvestment(@Param("userId") Integer userId);
	
	@Query("From Orders as order ORDER BY order.ordersId DESC ")
	List<Orders> findAllOrders(Pageable page);

	@Query("From Orders as order WHERE order.user.userId=:userId")
	List<Orders> getOrderByUserId(@Param("userId")Integer userId);

	@Query("From Orders as order WHERE order.bseOrderId=:regnNo1")
	Orders getorderByRegId(@Param("regnNo1") String regnNo1);

	@Query("FROM Orders as c WHERE c.status='M' order by c.lastupdate DESC")
	List<Orders> getOrderWithStatusM();

	@Query("FROM Orders as c WHERE c.status='PA' order by c.lastupdate DESC")
	List<Orders> getOrderWithStatusPA();

	@Query("FROM Orders as c WHERE c.bseOrderId=:orderNo")
	Orders getOrderDataByBseOrderId(@Param("orderNo")String orderNo);

	@Query("FROM Orders as c WHERE  c.status=:status")
	List<Orders> getOrderDetailWithStatusR(@Param("status") String status);

	@Query("FROM Orders as c  order by c.mPBundleId DESC")
	List<Orders> findModelPortfolioOrder();

	Orders findByOrdersId(Integer orderId);
	
	@Query("FROM Orders as c WHERE c.user.userId=:userId and c.orderCategory='MPO' and c.type=:type and (c.status='P' or c.status='M')")
	List<Orders> getSipOrderCategoryMPO(@Param("userId") Integer userId, @Param("type") String type);
	
	@Query("FROM Orders as c WHERE c.user.userId=:userId and c.orderCategory='MPO' and c.type=:type and (c.status='P' or c.status='M')")
	List<Orders> getLumpsumOrderCategoryMPO(@Param("userId") Integer userId, @Param("type") String type);
	
	@Query("FROM Orders as c WHERE c.user.userId=:userId and c.orderCategory='MPO' and (c.status='C')")
	List<Orders> getInvestedOrderCategoryMPO(@Param("userId") Integer userId);
	
	@Query("FROM Orders as c WHERE c.mPBundleId=:bundleId and c.orderCategory='MPO' and (c.status='P' or c.status='M')")
	public List<Orders> getUserOrderListByBundleId(@Param("bundleId") Integer bundleId);

	@Query("FROM Orders as c WHERE c.user.userId=:userId and (c.status='PA' or c.status='C')")
	List<Orders> getOrderDetailWithStatusCAndPA(@Param("userId") Integer userId);

	@Query("FROM Orders as c WHERE c.user.userId=:userId and c.type=:type and (c.status='P' or c.status='M' or c.status='C') order by c.lastupdate DESC")
	List<Orders> getSipUserOrderForAdmin(@Param("userId") Integer userId,@Param("type") String type);

	@Query("FROM Orders as c WHERE c.user.userId=:userId and c.type=:type and (c.status='P' or c.status='M' or c.status='C') order by c.lastupdate DESC")
	List<Orders> getLumpsumUserOrderForAdmin(@Param("userId") Integer userId,@Param("type") String type);

	@Query("FROM Orders as c WHERE c.user.userId=:userId and (c.status='M' or c.status='C') order by c.lastupdate DESC")
	List<Orders> findUserOrderWithStatusMAndC(@Param("userId") Integer userId);
	
	@Query("FROM Orders as c WHERE c.mPBundleId=:bundelId")
	List<Orders> findByBundelId(@Param("bundelId") int bundelId);

	Orders findByBseOrderId(String resp);

	@Query("FROM Orders as c GROUP BY c.field2")
	List<Orders> getUniqueOrderRegnIdList();

	@Query("FROM Orders as c WHERE c.status='AP'")
	List<Orders> getOrderListWithStatusAP();

	@Query("FROM Orders as c WHERE c.field2=:regnId")
	List<Orders> getOrderListByRegnId(@Param("regnId")String regnId);

	List<Orders> findByField2(String field2);

	@Query("FROM Orders as c WHERE c.user.userId=:userId and (c.status='IO' or c.status='AD' or c.status='C' or c.status='M' or c.status='AP') GROUP BY c.field2")
	List<Orders> getSipOrderWithUniqueRegnId(@Param("userId") Integer userId);

	@Query("FROM Orders as c WHERE c.status='AP'")
	List<Orders> getOrderWithStatusAP();

	@Query("FROM Orders as c WHERE c.field2=:regnId order by c.lastupdate DESC")
	List<Orders> getOrderListByRegnIdInDescOrder(@Param("regnId") String regnId);
	
	List<Orders> findByGoalId(int goalId);
	
	@Query("From Orders as o where o.status='C' OR o.status='AD'")
	List<Orders> findByStatus();

	List<Orders> findByStatus(String status);

	@Query("From Orders as o WHERE o.bseOrderId IN (:bseOrderIdList)")
	List<Orders> findByBseOrderId(@Param("bseOrderIdList")List<String> bseOrderIdList);

	@Query(value = "SELECT o.bseOrderId From Orders as o WHERE o.ordersId IN (:orderList)")
	List<String> findByOrdersId(@Param("orderList")List<Integer> orderList);

	@Query("From Orders as o where o.goalId =:oldGoalId and o.status IN ('C','P','M','AP','OD','AD')")
	List<Orders> isOrderAssociated(@Param("oldGoalId") int oldGoalId);

}