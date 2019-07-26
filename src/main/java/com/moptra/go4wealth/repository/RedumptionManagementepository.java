package com.moptra.go4wealth.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.moptra.go4wealth.bean.RedumptionManagement;

public interface RedumptionManagementepository extends JpaRepository<RedumptionManagement, Integer>{

	@Query("From RedumptionManagement as r where r.bseOrderId = :bseOrderId and r.status='P'")
	List<RedumptionManagement> getAvailableAmountByOrderIdWithStatusP(@Param("bseOrderId") String bseOrderId);

	@Query("From RedumptionManagement as r where r.sipRegnId = :sipRegnId and r.status='P'")
	List<RedumptionManagement> getAvailableAmountBySipRegnIdWithStatusP(@Param("sipRegnId") Long sipRegnId);

	List<RedumptionManagement> findBySipRegnId(long parseLong);

	List<RedumptionManagement> findByBseOrderId(String bseOrderId);

	List<RedumptionManagement> findByStatus(String status);

	@Query("From RedumptionManagement as r where r.status='R'")
	List<RedumptionManagement> getDataWithStatusR();

	@Query("From RedumptionManagement as r where r.folioNumber =:folioNo and r.schemeCode =:schemeCode and r.status ='P'")
	List<RedumptionManagement> getRedumptionDataByFolioAndSchemeCodeWithStatusP(@Param("folioNo") String folioNo , @Param("schemeCode") String schemeCode);

	@Query("From RedumptionManagement as r where r.folioNumber =:folioNo and r.schemeCode =:schemeCode")
	List<RedumptionManagement> findByFolioNoAndSchemeCode(@Param("folioNo") String folioNo,@Param("schemeCode") String schemeCode);

	@Query("From RedumptionManagement as r where r.folioNumber =:folioNo and r.schemeCode =:schemeCode and r.status ='C'")
	List<RedumptionManagement> getRedumptionDataByFolioAndSchemeCodeWithStatusC(@Param("folioNo") String folioNo , @Param("schemeCode") String schemeCode);

}
