package com.moptra.go4wealth.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.moptra.go4wealth.bean.ConsolidatedPortfollio;
import com.moptra.go4wealth.bean.TransferIn;

public interface ConsolidatedFollioRepository extends JpaRepository<ConsolidatedPortfollio, Integer>{

	@Query("From ConsolidatedPortfollio as c where c.folioNo =:folioNo and c.schemeCode =:schemeCode")
	ConsolidatedPortfollio getDetailByFollioAndSchemeCode(@Param("folioNo") String folioNo,@Param("schemeCode") String schemeCode);

	List<ConsolidatedPortfollio> findByUserId(Integer userId);

	List<ConsolidatedPortfollio> findByClientCode(String clientCode);

}
