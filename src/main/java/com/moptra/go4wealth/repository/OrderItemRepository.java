package com.moptra.go4wealth.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.moptra.go4wealth.bean.OrderItem;

public interface OrderItemRepository extends JpaRepository<OrderItem, Integer>{
	
	@Query("FROM OrderItem as c WHERE c.orders.ordersId=:ordersId and c.status=:status")
	public List<OrderItem> getOrderItemList(@Param("ordersId") Integer userId,@Param("status") String status);

	@Query("FROM OrderItem as c WHERE c.orders.ordersId=:orderId")
	public OrderItem getOrderItemByOrderId(@Param("orderId") Integer orderId);
}
