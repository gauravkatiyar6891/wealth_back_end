package com.moptra.go4wealth.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.moptra.go4wealth.bean.UserRole;
import com.moptra.go4wealth.bean.UserRoleId;

public interface UserRoleRepository extends JpaRepository<UserRole, UserRoleId>{
	
	

}
