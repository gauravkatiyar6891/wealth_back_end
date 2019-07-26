package com.moptra.go4wealth.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.moptra.go4wealth.bean.IncomeSlabs;
import com.moptra.go4wealth.sip.model.IncomeDTO;

public interface IncomeSlabsRepository extends JpaRepository<IncomeSlabs, Integer> {

	@Query("SELECT i.incomeSlabCode FROM IncomeSlabs as i GROUP BY i.incomeSlabCode")
	List<String> getIncomeSlabCodeList();

	@Query("SELECT i.incomeSlabCode FROM IncomeSlabs as i WHERE i.incomeSlabId=:incomeSlabId")
	String findCodeByIncomeId(@Param("incomeSlabId") Integer incomeSlabId);

	@Query("SELECT new com.moptra.go4wealth.sip.model.IncomeDTO(i) FROM IncomeSlabs as i")
	List<IncomeDTO> getIncomeSlabsList();

	@Query("SELECT i.incomeSlabCode FROM IncomeSlabs as i WHERE i.incomeFrom <= :incomeVal AND i.incomeTo > :incomeVal ")
	String findCodeByIncomeVal(@Param("incomeVal") Integer incomeVal);
	
	@Query("SELECT i FROM IncomeSlabs as i WHERE i.incomeFrom <= :incomeVal AND i.incomeTo > :incomeVal ")
	IncomeSlabs findByIncomeVal(@Param("incomeVal") Integer incomeVal);
	
	IncomeSlabs findByIncomeSlabCode(String incomeSlabCode);

}
