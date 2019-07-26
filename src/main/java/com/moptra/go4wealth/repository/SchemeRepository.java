package com.moptra.go4wealth.repository;

import java.util.List;

import javax.transaction.Transactional;

import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.moptra.go4wealth.bean.Scheme;

public interface SchemeRepository extends JpaRepository<Scheme, Integer>{

	@Query("From Scheme as sch WHERE sch.display='0' AND( NOT (sch.schemeName LIKE '%DIVIDEND%')) AND (sch.purchaseAllowed='Y' OR sch.sipFlag='Y') ORDER BY FIELD(sch.priority,'1','2','3')")
	List<Scheme> getAllSchemes(Pageable page);
	
	@Query("From Scheme as sch WHERE sch.display='0' AND (sch.purchaseAllowed='Y' OR sch.sipFlag='Y') ORDER BY FIELD(sch.priority,'1','2','3')")
	List<Scheme> getAllSchemesWithDividend(Pageable page);
	
	//@Query("From Scheme  WHERE schemeCode NOT IN (SELECT schemeCode FROM TopSchemes where status='Active')")
	@Query("From Scheme as sch WHERE sch.display='0' AND (sch.purchaseAllowed='Y' OR sch.sipFlag='Y') AND schemeCode NOT IN (SELECT schemeCode FROM TopSchemes where status='Active')")
	List<Scheme> findAllSchemes(Pageable page);
	
	@Query("From Scheme  WHERE schemeCode IN (SELECT schemeCode FROM TopSchemes where status='Active')")
	List<Scheme> findAllTopSchemes();
 
	@Query("From Scheme sch WHERE schemeCode IN (SELECT schemeCode FROM TopSchemes where status='Active') ORDER BY sch.schemeId DESC ")
	List<Scheme> findFiveTopSchemes();
	
	@Query("From Scheme sch WHERE sch.schemeCode=:schemeCode AND (sch.purchaseAllowed='Y' OR sch.sipFlag='Y')")
	Scheme findBySchemeCode(@Param("schemeCode")String schemeCode);
	
	@Query("SELECT schemeType From Scheme GROUP BY schemeType")
	List<String> getAllSchemeType();
	
	@Query("FROM Scheme as sch WHERE sch.schemeName LIKE %:schemeName% AND sch.display='0' AND (sch.purchaseAllowed='Y' OR sch.sipFlag='Y') ORDER BY FIELD(sch.priority,'1','2','3')")
	List<Scheme> findBySchemeName(@Param("schemeName")String schemeName,Pageable page);
	
	@Query("FROM Scheme as sch WHERE sch.schemeType=:schemeType AND sch.display='0' AND (sch.purchaseAllowed='Y' OR sch.sipFlag='Y') ORDER BY FIELD(sch.priority,'1','2','3')")
	List<Scheme> findBySchemeType(@Param("schemeType")String schemeType,Pageable page);
	
	@Query("FROM Scheme as sch WHERE sch.display='0' AND (sch.dividendReinvestmentFlag='Y' OR sch.dividendReinvestmentFlag='N') ORDER BY FIELD(sch.priority,'1','2','3')")
	List<Scheme> findByDividend(Pageable page);
	
	@Query("FROM Scheme as sch WHERE sch.display='0' AND sch.dividendReinvestmentFlag='Z' ORDER BY FIELD(sch.priority,'1','2','3')")
	List<Scheme> findByGrowth(Pageable page);
	
	@Query("FROM Scheme as sch WHERE sch.schemeType=:schemeType AND (sch.dividendReinvestmentFlag='Y' OR sch.dividendReinvestmentFlag='N') AND sch.display='0' AND (sch.purchaseAllowed='Y' OR sch.sipFlag='Y') ORDER BY FIELD(sch.priority,'1','2','3')")
	List<Scheme> findBySchemeTypeAndDividend(@Param("schemeType")String schemeType,Pageable page);
	
	@Query("FROM Scheme as sch WHERE sch.schemeType=:schemeType AND (sch.dividendReinvestmentFlag='Z') AND sch.display='0' AND (sch.purchaseAllowed='Y' OR sch.sipFlag='Y') ORDER BY FIELD(sch.priority,'1','2','3')")
	List<Scheme> findBySchemeTypeAndGrowth(@Param("schemeType")String schemeType,Pageable page);
		
	@Query("FROM Scheme as sch WHERE sch.schemeType=:schemeType AND sch.display='0' AND (sch.purchaseAllowed='Y' OR sch.sipFlag='Y') ORDER BY FIELD(sch.priority,'1','2','3')")
	List<Scheme> findBySchemeType(@Param("schemeType")String schemeType);
	
	@Query("FROM Scheme as sch WHERE sch.schemeName LIKE %:schemeName% AND sch.schemeType=:schemeType AND (sch.dividendReinvestmentFlag='Y' OR sch.dividendReinvestmentFlag='N') AND sch.display='0' AND (sch.purchaseAllowed='Y' OR sch.sipFlag='Y') ORDER BY FIELD(sch.priority,'1','2','3')")
	List<Scheme> findBySchemeNameAndSchemeTypeAndDividend(@Param("schemeType")String schemeType,@Param("schemeName")String schemeName, Pageable page);
	
	@Query("FROM Scheme as sch WHERE sch.schemeName LIKE %:schemeName% AND sch.schemeType=:schemeType AND sch.display='0' AND sch.dividendReinvestmentFlag='Z' AND (sch.purchaseAllowed='Y' OR sch.sipFlag='Y') ORDER BY FIELD(sch.priority,'1','2','3')")
	List<Scheme> findBySchemeNameAndSchemeTypeAndGrowth(@Param("schemeType")String schemeType,@Param("schemeName")String schemeName, Pageable page);
	
	@Query("FROM Scheme as sch WHERE sch.schemeName LIKE %:schemeName% AND sch.schemeType=:schemeType AND sch.display='0' AND (sch.purchaseAllowed='Y' OR sch.sipFlag='Y') ORDER BY FIELD(sch.priority,'1','2','3')")
	List<Scheme> findBySchemeNameAndSchemeType(@Param("schemeType")String schemeType,@Param("schemeName")String schemeName, Pageable page);
	
	@Query("FROM Scheme as sch WHERE sch.schemeName LIKE %:schemeName% AND (sch.dividendReinvestmentFlag='Y' OR sch.dividendReinvestmentFlag='N') AND sch.display='0' AND (sch.purchaseAllowed='Y' OR sch.sipFlag='Y') ORDER BY FIELD(sch.priority,'1','2','3')")
	List<Scheme> findBySchemeNameAndDividend(@Param("schemeName")String schemeName, Pageable page);
	
	@Query("FROM Scheme as sch WHERE sch.schemeName LIKE %:schemeName% AND (sch.dividendReinvestmentFlag='Z') AND sch.display='0' AND (sch.purchaseAllowed='Y' OR sch.sipFlag='Y') ORDER BY FIELD(sch.priority,'1','2','3')")
	List<Scheme> findBySchemeNameAndGrowth(@Param("schemeName")String schemeName, Pageable page);
	
	List<Scheme> findByChannelPartnerCode(String channelPartnerCode);

	@Query("FROM Scheme as sch WHERE sch.isin=:isin")
	List<Scheme> getSchemeListByIsin(@Param("isin") String isin);
	
	@Query("FROM Scheme as sch WHERE sch.rtaSchemeCode=:rtaCode")
	List<Scheme> getSchemeListByrtaCode(@Param("rtaCode") String rtaCode);

	@Transactional
	@Modifying(clearAutomatically = true)
	@Query("UPDATE Scheme as sch SET sch.navDate =:previousDate WHERE sch.navDate <>:nullString")
	void setNavDateToCurrentDate(@Param("previousDate") String previousDate,@Param("nullString") String nullString);

	@Query("From Scheme sch WHERE sch.schemeCode=:schemeCode")
	Scheme findBySchemeCodeWithL1(@Param("schemeCode")String schemeCode);

	@Query("From Scheme sch where sch.amfiCode <> 0 GROUP BY sch.amfiCode")
	List<Scheme> findSchemeDateWithAmfiCode();

	@Query("From Scheme sch WHERE sch.keyword=:keyword AND (sch.purchaseAllowed='Y' OR sch.sipFlag='Y') AND sch.display='0'")
	List<Scheme> findBykeyword(@Param("keyword")String keyword);
	
	@Query("From Scheme as sch WHERE sch.display='0' AND (sch.purchaseAllowed='Y' OR sch.sipFlag='Y') ORDER BY FIELD(sch.priority,'1','2','3')")
	List<Scheme> getAllSchemesKeyword();
	
	List<Scheme> findByIsin(String isin);
	
	@Query("From Scheme as sch WHERE sch.schemeCode IN(select t.schemeCode from TopSchemes as t  where t.status='Active')")
	List<Scheme> getAllRecommendedSchemes();

	List<Scheme> findByRtaSchemeCodeAndIsin(String rtaSchemeCode, String isin);
	
	List<Scheme> findByRtaSchemeCode(String rtaSchemeCode);
	
	Scheme getBySchemeCode(String schemeCode);
}