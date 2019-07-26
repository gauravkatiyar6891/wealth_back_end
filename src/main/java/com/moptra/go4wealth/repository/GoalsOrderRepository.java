package com.moptra.go4wealth.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.moptra.go4wealth.bean.GoalsOrder;

public interface GoalsOrderRepository extends JpaRepository<GoalsOrder,Integer> {

	@Query("FROM GoalsOrder go where go.user.userId = :userId and go.status = :status")
	GoalsOrder findByUserIdAndState(@Param("userId") Integer userId,@Param("status") String state);

	@Query("FROM GoalsOrder go where go.user.userId = :userId and go.goalsOrderId = :orderId")
	GoalsOrder findByUserIdAndOrderId(@Param("userId") Integer userId,@Param("orderId") Integer orderId);
	
	@Query("From GoalsOrder go where go.user.userId =:userId")
	GoalsOrder findByUserId(@Param("userId")Integer userId);
	
	@Query("Delete From GoalsOrder go where go.goalsOrderId =:goalsOrderId")
	void deleteByGoalsOrderId(Integer goalsOrderId);
	
	GoalsOrder findByGoalsOrderId(Integer goalsOrderId);
}