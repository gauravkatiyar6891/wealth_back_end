package com.moptra.go4wealth.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.moptra.go4wealth.bean.AddressState;

public interface AddressStateRepository extends JpaRepository<AddressState,Integer>{

	 @Query("FROM AddressState AS addressState where addressState.addressStatename=:stateName")
	 AddressState getStateIdByStateName(@Param("stateName") String stateName );

	 @Query("FROM AddressState AS ads where ads.addressStatename=:state")
	AddressState getStateCodeByStateName(@Param("state") String state);

	}