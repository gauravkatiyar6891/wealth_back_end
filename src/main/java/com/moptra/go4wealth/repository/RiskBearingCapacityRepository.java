package com.moptra.go4wealth.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.moptra.go4wealth.bean.RiskBearingCapacity;
import com.moptra.go4wealth.sip.model.ReturnsTypeDTO;

public interface RiskBearingCapacityRepository extends JpaRepository<RiskBearingCapacity, Integer> {

	@Query("SELECT rbc.fundType FROM RiskBearingCapacity as rbc WHERE rbc.return_ = :returns AND rbc.risk = :risk")
	String getFundTypeByReturnsAndRisk(@Param("returns") String returns,@Param("risk") String risk);
	
	@Query("SELECT new com.moptra.go4wealth.sip.model.ReturnsTypeDTO(rt.return_,rt.risk) FROM RiskBearingCapacity as rt")
	List<ReturnsTypeDTO> getRiskProfileList();

}
