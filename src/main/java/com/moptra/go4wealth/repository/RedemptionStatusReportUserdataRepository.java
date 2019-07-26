package com.moptra.go4wealth.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.moptra.go4wealth.bean.RedemptionStatusReportUserdata;

public interface RedemptionStatusReportUserdataRepository extends JpaRepository<RedemptionStatusReportUserdata, Integer>{

	@Query("From RedemptionStatusReportUserdata as r where r.orderNo = :orderNo")
	RedemptionStatusReportUserdata getAvailableBalanceByOrderId(@Param("orderNo") int orderNo);

}
