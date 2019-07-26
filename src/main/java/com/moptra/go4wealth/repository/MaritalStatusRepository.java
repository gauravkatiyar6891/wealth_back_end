package com.moptra.go4wealth.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.moptra.go4wealth.bean.MaritalStatus;
import com.moptra.go4wealth.sip.model.MaritalDTO;

public interface MaritalStatusRepository extends JpaRepository<MaritalStatus,Integer> {

	@Query("SELECT m.maritalStatusCode FROM MaritalStatus as m GROUP BY m.maritalStatusCode")
	List<String> getMaritalStatusCodeList();

	@Query("SELECT m.maritalStatusCode FROM MaritalStatus as m WHERE m.maritalStatusId=:maritalStatusId")
	String findMaritalCodeByMaritalId(@Param("maritalStatusId") Integer maritalStatusId);

	@Query("SELECT new com.moptra.go4wealth.sip.model.MaritalDTO(m) FROM MaritalStatus as m")
	List<MaritalDTO> getMaritalSlabList();

}
