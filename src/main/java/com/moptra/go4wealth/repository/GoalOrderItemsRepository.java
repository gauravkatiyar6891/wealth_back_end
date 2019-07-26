package com.moptra.go4wealth.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.moptra.go4wealth.bean.GoalOrderItems;

public interface GoalOrderItemsRepository extends JpaRepository<GoalOrderItems,Integer>{
	
	@Query("from GoalOrderItems as go where go.user.userId=:userId AND go.goals.goalId=:goalId")
	GoalOrderItems findByUserIdAndGoalId(@Param("userId")Integer userId,@Param("goalId")Integer goalId);
	
	@Query("from GoalOrderItems as go where go.user.userId=:userId")
	List<GoalOrderItems> findByUserId(@Param("userId")Integer userId);
	
	@Query("from GoalOrderItems as go where go.goalsOrder.goalsOrderId=:goalsOrderId AND go.description=:description")
	List<GoalOrderItems> findByGoalsOrderIdAndDescription(@Param("goalsOrderId")Integer goalsOrderId,@Param("description")String description);

	GoalOrderItems findByGoalOrderItemId(Integer goalId);

}