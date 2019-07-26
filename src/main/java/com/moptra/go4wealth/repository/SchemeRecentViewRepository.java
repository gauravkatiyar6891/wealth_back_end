package com.moptra.go4wealth.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.moptra.go4wealth.bean.SchemeRecentView;

public interface SchemeRecentViewRepository extends JpaRepository<SchemeRecentView,Integer> {

	@Query("FROM SchemeRecentView as srv order by srv.lastUpdated ASC")
	List<SchemeRecentView> findSchemeByDate();
	
	@Query("FROM SchemeRecentView as srv order by srv.lastUpdated DESC")
	List<SchemeRecentView> findSchemeByDesc();
}