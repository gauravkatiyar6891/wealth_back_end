package com.moptra.go4wealth.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.moptra.go4wealth.bean.TransferIn;

public interface TransferInRepository extends JpaRepository<TransferIn, Integer>{

	TransferIn findByFolioNumber(String follioNo);

	@Query("From TransferIn as tran where tran.userId=:userId AND (tran.status='C' AND tran.statusCode=1)")
	List<TransferIn> findByUserId(@Param("userId")Integer userId);

	TransferIn findBytransferInId(Integer tranferInId);
	
	@Query("From TransferIn as tran where tran.folioNumber=:folioNo and tran.schemeCode =:schemeCode")
	TransferIn findByFolioNumberAndSchemeCode(@Param("folioNo") String folioNo, @Param("schemeCode") String schemeCode);

	@Query("From TransferIn as tran where tran.pan=:pan AND tran.status='P'")
	List<TransferIn> findByPan(@Param("pan")String pan);
	
}