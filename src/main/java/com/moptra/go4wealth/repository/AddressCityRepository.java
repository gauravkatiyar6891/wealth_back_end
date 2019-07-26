package com.moptra.go4wealth.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.moptra.go4wealth.bean.AddressCity;

public interface AddressCityRepository extends JpaRepository<AddressCity, Integer> {

	@Query("FROM AddressCity as addressCity where addressCity.addressState.addressStateId=:addressStateId")
	List<AddressCity> getCityListByStateId(@Param("addressStateId") Integer addressStateId );

}
