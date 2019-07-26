package com.moptra.go4wealth.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.moptra.go4wealth.bean.IndianIfscCodes;

public interface IndianIfscCodesRepository extends JpaRepository<IndianIfscCodes, Integer>{

	IndianIfscCodes findByIfscCode(String ifscCode);
}
