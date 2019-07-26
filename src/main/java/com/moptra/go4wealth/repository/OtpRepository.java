package com.moptra.go4wealth.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.moptra.go4wealth.bean.Otp;

public interface OtpRepository extends JpaRepository<Otp, Integer> {
	
	Otp findByMobileNumber(String mobileNumber);

}