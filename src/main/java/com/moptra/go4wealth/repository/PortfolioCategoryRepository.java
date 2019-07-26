package com.moptra.go4wealth.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.moptra.go4wealth.bean.PortfolioCategory;

public interface PortfolioCategoryRepository extends JpaRepository<PortfolioCategory, Integer> {
	
	PortfolioCategory findByCategoryName(String categoryName);
	
	PortfolioCategory findByCategoryKeyword(String categoryKeyword);
}
