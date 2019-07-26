package com.moptra.go4wealth.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.moptra.go4wealth.bean.UserEnquiry;

public interface UserEnquryRepository extends JpaRepository<UserEnquiry,Integer>{

}