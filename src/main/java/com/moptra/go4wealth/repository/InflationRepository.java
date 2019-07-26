package com.moptra.go4wealth.repository;

import java.math.BigDecimal;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.moptra.go4wealth.bean.Inflation;

public interface InflationRepository extends JpaRepository<Inflation,Integer> {

	@Query("SELECT i.inflation FROM Inflation as i WHERE i.inflationType = :type and i.locationOfSpend = :location")
	BigDecimal findByInflationType(@Param("type") String value,@Param("location") String location);
	
}
