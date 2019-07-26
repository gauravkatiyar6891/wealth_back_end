package com.moptra.go4wealth.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.moptra.go4wealth.bean.StoreConf;

public interface StoreConfRepository extends JpaRepository<StoreConf, Integer>{

	StoreConf findByKeyword(String keyword);
}
