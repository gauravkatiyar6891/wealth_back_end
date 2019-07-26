package com.moptra.go4wealth.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.moptra.go4wealth.bean.Ppcpaytran;

public interface ppcpaytranRepository extends JpaRepository<Ppcpaytran, Integer>{

	@Query("FROM Ppcpaytran as c WHERE c.userId=:userId and (c.ppcpayinst.state='PA')")
	List<Ppcpaytran> getOrderWithAwiatingStatus(@Param("userId") Integer userId);

}
