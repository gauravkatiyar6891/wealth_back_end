package com.moptra.go4wealth.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.moptra.go4wealth.bean.MaintainCurrentNavCurrentValueForOrders;

public interface MaintainCurrentNavCurrentValueForOrdersRepository extends JpaRepository<MaintainCurrentNavCurrentValueForOrders, Integer>{

	@Query("From MaintainCurrentNavCurrentValueForOrders as c where c.orderId = :orderNo")
	MaintainCurrentNavCurrentValueForOrders getDetailByOrderId(@Param("orderNo") Integer orderNo);

	MaintainCurrentNavCurrentValueForOrders findBySipRegnNo(Long sipRegnNo);

	List<MaintainCurrentNavCurrentValueForOrders> findByClientCode(String clientCode);

	MaintainCurrentNavCurrentValueForOrders findByFolioNo(String folioNumber);
}
