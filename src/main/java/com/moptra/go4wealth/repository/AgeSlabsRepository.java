package com.moptra.go4wealth.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.moptra.go4wealth.bean.AgeSlabs;
import com.moptra.go4wealth.sip.model.AgeSlabDTO;

public interface AgeSlabsRepository extends JpaRepository<AgeSlabs, Integer> {

	@Query("SELECT a.ageSlabCode FROM AgeSlabs as a GROUP BY a.ageSlabCode")
	List<String> getAgeSlabsCodeList();

	@Query("SELECT a.ageSlabCode FROM AgeSlabs as a WHERE a.ageFrom <= :currentAge AND a.ageTo > :currentAge ")
	String findAgeSlabCodeByAge(@Param("currentAge") Integer currentAge);

	@Query("SELECT new com.moptra.go4wealth.sip.model.AgeSlabDTO(a) FROM AgeSlabs as a")
	List<AgeSlabDTO> getAgeSlabList();

}
