package com.moptra.go4wealth.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.moptra.go4wealth.bean.TopSchemes;

public interface TopSchemeRepository extends JpaRepository<TopSchemes, Integer>{
	
	@Query("FROM TopSchemes as tsch WHERE tsch.schemeCode=:schemeCode")
	TopSchemes getSchemeBySchemeCode(@Param("schemeCode") String schemeCode);
	
	@Query("FROM TopSchemes as tsch WHERE tsch.schemeCategory=:schemeCategory AND tsch.status='Active' GROUP BY tsch.sequence")
	List<TopSchemes> findBySchemeCategory(@Param("schemeCategory") String schemeCategory);
	
	@Query("FROM TopSchemes as tsch WHERE tsch.status='Active' ORDER BY SEQUENCE")
	List<TopSchemes> getAllSchemes();
}
