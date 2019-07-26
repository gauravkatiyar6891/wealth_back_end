package com.moptra.go4wealth.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.moptra.go4wealth.bean.UserMandateHistory;

public interface UserMandateHistoryRepository extends JpaRepository<UserMandateHistory, Integer>{

	//@Query("From UserMandateHistory as u where u.userId = :userId  order by u.createdDate DESC")
	List<UserMandateHistory> findByUserId(Integer userId);

}
