package com.moptra.go4wealth.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.moptra.go4wealth.bean.Goals;
import com.moptra.go4wealth.sip.model.GoalDTO;

public interface GoalsRepository extends JpaRepository<Goals,Integer> {
	
	@Query("SELECT new com.moptra.go4wealth.sip.model.GoalDTO(g) FROM Goals as g")
	List<GoalDTO> getGoalList();
	
	@Query("From Goals as goal Where goal.goalType='PD' AND goal.status='Active' GROUP BY goal.showToProfileType")
	List<Goals> findAllPreDefineGoals();

	@Query("From Goals as goal Where goal.goalType='UD' and goal.goalId=:goalId")
	Goals getUserGoals(@Param("goalId") Integer goalId);
	
	@Query("From Goals as goal Where goal.goalType='PD' and goal.goalName=:goalName")
	Goals getGoal(@Param("goalName")String goalName);
	
	Goals findByGoalId(Integer goalId);
	
	@Query("From Goals as goal where goal.goalType='PD' AND goal.goalName=:goalName")
	Goals findByGoalName(@Param("goalName")String goalName);
}
