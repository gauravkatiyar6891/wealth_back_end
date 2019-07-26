package com.moptra.go4wealth.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.moptra.go4wealth.bean.ReturnsType;
import com.moptra.go4wealth.sip.model.ReturnsTypeDTO;

public interface ReturnsTypeRepository extends JpaRepository<ReturnsType, Integer> {

	@Query("SELECT new com.moptra.go4wealth.sip.model.ReturnsTypeDTO(rt.returnsTypeId,rt.returnsType) FROM ReturnsType as rt")
	List<ReturnsTypeDTO> getReturnsTypeList();

	@Query("SELECT rt.returnsType FROM ReturnsType as rt WHERE rt.returnsTypeId = :returnsTypeId")
	String findReturnsById(@Param("returnsTypeId") Integer returnsTypeId);
}
