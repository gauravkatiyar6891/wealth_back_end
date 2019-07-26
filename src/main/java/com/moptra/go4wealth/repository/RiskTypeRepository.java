package com.moptra.go4wealth.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.moptra.go4wealth.bean.RiskType;

public interface RiskTypeRepository extends JpaRepository<RiskType, Integer> {

	@Query("SELECT rt.riskType FROM RiskType as rt WHERE rt.riskTypeId = :riskTypeId")
	String findRiskById(@Param("riskTypeId") Integer riskTypeId);

}
 