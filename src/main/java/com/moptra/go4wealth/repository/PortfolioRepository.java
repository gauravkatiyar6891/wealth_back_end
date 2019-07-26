package com.moptra.go4wealth.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.moptra.go4wealth.bean.Portfolio;

public interface PortfolioRepository extends JpaRepository<Portfolio, Integer>{
	
	@Query("from Portfolio as p where p.schemeCode=:schemeCode AND p.portfolioCategoryId=:portfolioCategoryId")
	Portfolio findBySchemeCodeAndPortfolioCategoryId(@Param("schemeCode")String schemeCode,@Param("portfolioCategoryId")Integer portfolioCategoryId);
	
	@Query("from Portfolio as p where p.portfolioCategoryId=:portfolioCategoryId")
	List<Portfolio> findByPortfolioCategoryId(@Param("portfolioCategoryId")Integer portfolioCategoryId);
}