package com.moptra.go4wealth.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.moptra.go4wealth.bean.SystemProperties;

public interface SystemPropertiesRepository extends JpaRepository<SystemProperties, String>{

	@Query("From SystemProperties as s where s.id.propertyValue =:password")
	SystemProperties getPassword(@Param("password") String password);

	
}
