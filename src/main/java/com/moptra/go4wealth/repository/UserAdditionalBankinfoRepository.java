package com.moptra.go4wealth.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.moptra.go4wealth.bean.UserAdditionalBankinfo;

public interface UserAdditionalBankinfoRepository extends JpaRepository<UserAdditionalBankinfo,Integer>{
	
	UserAdditionalBankinfo findByUserId(Integer userId);

}