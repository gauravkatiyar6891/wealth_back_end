package com.moptra.go4wealth.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.moptra.go4wealth.bean.BankDetails;

public interface BankDetailsRepository  extends JpaRepository<BankDetails, Integer>{

	BankDetails findBankDetailsByAccountNo(String accountNumber);

}
