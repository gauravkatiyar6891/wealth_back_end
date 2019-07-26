package com.moptra.go4wealth.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.moptra.go4wealth.bean.KidsSlabs;
import com.moptra.go4wealth.sip.model.KidsSlabDTO;

public interface KidsSlabsRepository extends JpaRepository<KidsSlabs,Integer> {

	@Query("SELECT k.kidsSlabCode FROM KidsSlabs as k GROUP BY k.kidsSlabCode")
	List<String> getKidsSlabsList();

	@Query("SELECT new com.moptra.go4wealth.sip.model.KidsSlabDTO(k.kidsSlabName,k.kidsSlabCode) FROM KidsSlabs as k")
	List<KidsSlabDTO> getKidsSlabList();

}
