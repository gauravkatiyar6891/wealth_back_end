package com.moptra.go4wealth.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.moptra.go4wealth.bean.AddressProof;

public interface AddressProofRepository extends JpaRepository<AddressProof, Integer> {

	@Query("SELECT a FROM AddressProof as a WHERE a.user.userId=:userId")
	AddressProof findAddressProofByUserId(@Param("userId") Integer userId);
	
	AddressProof findAddressProofByAddressProofNo(String addressProofNo);

}
