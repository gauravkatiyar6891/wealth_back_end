package com.moptra.go4wealth.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.moptra.go4wealth.bean.PanDetails;

public interface PanDetailsRepository extends JpaRepository<PanDetails, Integer>{

	PanDetails findByPanNo(String panNo);
}
