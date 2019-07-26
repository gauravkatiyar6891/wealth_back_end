package com.moptra.go4wealth.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.moptra.go4wealth.bean.Role;

public interface RoleRepository extends JpaRepository<Role, Integer> {
	
	Role findByRoleName(String role);

}
