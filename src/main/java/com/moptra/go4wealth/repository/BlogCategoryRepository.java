package com.moptra.go4wealth.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.moptra.go4wealth.bean.BlogCategory;

public interface BlogCategoryRepository extends JpaRepository<BlogCategory, Integer>{

	@Query("FROM BlogCategory as bc where bc.blogCategoryName=:categoryName")
	BlogCategory findByCategoryName(@Param("categoryName") String categoryName);

}
