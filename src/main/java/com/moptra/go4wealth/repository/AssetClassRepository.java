package com.moptra.go4wealth.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.moptra.go4wealth.bean.AssetClass;
import com.moptra.go4wealth.sip.model.AssetClassDTO;

public interface AssetClassRepository extends JpaRepository<AssetClass,Integer> {

	@Query("SELECT new com.moptra.go4wealth.sip.model.AssetClassDTO(ac.assetClassId,ac.assetClass) FROM AssetClass as ac")
	List<AssetClassDTO> getAssetClassList();
	
	AssetClass findByAssetClass(String assetClass);

}
