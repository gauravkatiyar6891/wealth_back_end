package com.moptra.go4wealth.repository;

import java.math.BigDecimal;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.moptra.go4wealth.bean.AssetClassInternal;

public interface AssetClassInternalRepository extends JpaRepository<AssetClassInternal, Integer>{

	@Query("SELECT AVG(aci.roiExptd - :inflation) FROM AssetClassInternal as aci WHERE aci.fundTypeCode IN (:fundTypeCodeList)")
	BigDecimal findAvgRoiByFundType(@Param("fundTypeCodeList") List<String> fundTypeCodeList,@Param("inflation") BigDecimal inflation);
	
	@Query("SELECT aci.roiExptd FROM AssetClassInternal as aci WHERE aci.fundTypeCode IN (:fundTypeCodeList)")
	List<BigDecimal> getRoiListByFundType(@Param("fundTypeCodeList") List<String> fundTypeCodeList);

	@Query("SELECT AVG(aci.roiExptd) FROM AssetClassInternal as aci WHERE aci.fundTypeCode IN (:fundTypeCodeList)")
	BigDecimal findAvgRoiByFundType(@Param("fundTypeCodeList") List<String> fundTypeCodeList);

	@Query("SELECT aci.fundType FROM AssetClassInternal as aci WHERE aci.fundTypeCode IN (:fundTypeCodeList)")
	List<String> getFundsListByFundType(@Param("fundTypeCodeList") List<String> fundTypeCodeList);

	@Query("SELECT AVG(aci.roiExptd) FROM AssetClassInternal as aci")
	BigDecimal findAvgBigDecimalRoiExptd();

	@Query("SELECT AVG(aci.roiExptd - :inflation) FROM AssetClassInternal as aci")
	BigDecimal findAvgBigDecimalRoiExptd(@Param("inflation") BigDecimal generalInflation);
	
	@Query("From AssetClassInternal as aci where aci.fundType =:fundType")
	AssetClassInternal findByFundType(@Param("fundType")String fundType);
}
