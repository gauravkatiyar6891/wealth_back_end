package com.moptra.go4wealth.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.moptra.go4wealth.bean.AllotmentStatusReportUserdata;

public interface AllotmentStatusReportUserdataRepository extends JpaRepository<AllotmentStatusReportUserdata, Integer>{

	@Query("FROM AllotmentStatusReportUserdata as c WHERE c.clientCode = :clientCode")
	List<AllotmentStatusReportUserdata> getUserDataByClientCode(@Param("clientCode") String clientCode);

	@Query("FROM AllotmentStatusReportUserdata as c WHERE c.orderNo = :transacId")
	AllotmentStatusReportUserdata getUserDataByOrderNumber(@Param("transacId") Integer transacId);

	AllotmentStatusReportUserdata findByOrderNo(int parseInt);

	@Query("FROM AllotmentStatusReportUserdata as c WHERE c.allotMailStatus = 0 and c.status ='AD' GROUP BY c.clientCode")
	List<AllotmentStatusReportUserdata> getAllotWithEmailStatusZeroAndStatusNotAP();

	@Query("FROM AllotmentStatusReportUserdata as c WHERE c.status = 'AD'  GROUP BY c.clientCode")
	List<AllotmentStatusReportUserdata> getAllAllotedSchemeWithStatusADGroupBy();

	@Query("FROM AllotmentStatusReportUserdata as c WHERE c.clientCode =:clientCode and c.allotMailStatus = 0 and c.status ='AD' ")
	List<AllotmentStatusReportUserdata> getAllotWithClientCode(@Param("clientCode") String clientCode);

	@Query("FROM AllotmentStatusReportUserdata as c WHERE c.orderNo =:orderId and (c.status = 'AP' or c.status = 'AD' or c.status = 'PR' or c.status = 'OD')")
	AllotmentStatusReportUserdata findByBseOrderId(@Param("orderId") int orderId);

	List<AllotmentStatusReportUserdata> findByFolioNo(String folioNumber);

	@Query("FROM AllotmentStatusReportUserdata as a where a.clientCode =:clientCode and (a.status = 'AP' or a.status = 'AD' or a.status = 'PR' or a.status = 'OD') GROUP BY a.folioNo,a.schemeCode")
	List<AllotmentStatusReportUserdata> getDetailByClientCodeGroupByFollioAndSchemeCode(@Param("clientCode") String clientCode);

	@Query("From AllotmentStatusReportUserdata as a where a.folioNo =:folioNo and a.schemeCode =:schemeCode and (a.status = 'AP' or a.status = 'AD' or a.status = 'PR' or a.status = 'OD')")
	List<AllotmentStatusReportUserdata> getAllotementByFollioNoAndSchemeCode(@Param("folioNo") String folioNo,@Param("schemeCode") String schemeCode);

	List<AllotmentStatusReportUserdata> findByFolioNoAndSchemeCode(String folioNo, String schemeCode);


}
