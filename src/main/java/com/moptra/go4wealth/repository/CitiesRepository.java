package com.moptra.go4wealth.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.moptra.go4wealth.bean.Cities;

public interface CitiesRepository extends JpaRepository<Cities, Integer> {


	@Query("SELECT c.cityCode FROM Cities as c GROUP BY c.cityCode")
	List<String> getCityCodeList();

	@Query("SELECT c.cityCode FROM Cities as c WHERE c.cityId=:cityId")
	String findCityCodeByCityId(@Param("cityId") Integer cityId);
	

}
