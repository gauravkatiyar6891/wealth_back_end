package com.moptra.go4wealth.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.moptra.go4wealth.bean.BankCodes;

public interface BankCodeRepository extends JpaRepository<BankCodes, Integer>{

}
