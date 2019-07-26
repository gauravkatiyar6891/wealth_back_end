package com.moptra.go4wealth.repository;

import java.util.List;

import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.moptra.go4wealth.bean.Seo;
import com.moptra.go4wealth.bean.User;
import com.moptra.go4wealth.metamodel.User_;

public interface UserRepository extends JpaRepository<User,Integer>, JpaSpecificationExecutor<User_> {

	User findByUsername(String username);
	
	User findByEmail(String email);

	User findByUserId(Integer profileId);

	void delete(User deleted);

	void flush();

	User findByMobileNumber(String mobileNumber);

	@Query("FROM Seo as s Where s.pageName=:pageName")
	Seo fetchScoInfo(@Param("pageName") String pageName);
	
	@Query("FROM User as u WHERE u.userId IN (SELECT ur.id.userId FROM UserRole as ur WHERE ur.id.roleId != 1) ORDER BY u.createdTimestamp DESC")
	List<User> findUserExcludeAdmin(Pageable page);
	
	@Query("FROM User as u WHERE u.userId =:userId")
	User getUserByUserId(@Param("userId") Integer userId);
	
	@Query("FROM User as u WHERE u.userId IN (SELECT ur.id.userId FROM UserRole as ur WHERE ur.id.roleId != 1)")
	List<User> findUserExceptAdmin();

	@Query("From User as u where u.userId in (select ur.id.userId FROM UserRole as ur where ur.id.userId = :userId and ur.id.roleId in (select r.roleId from Role as r where r.roleName = :roleName))")
	User findUserByRole(@Param("userId") int userId,@Param("roleName") String roleName);

	@Query("From User as u where u.username LIKE %:userName% or u.mobileNumber LIKE %:userName% or u.email LIKE %:userName% or u.firstName LIKE %:userName% ")
	List<User> searchUserExcludeAdmin(@Param("userName") String userName);

	@Query("FROM User as u where DATE(u.createdTimestamp) = CURDATE()")
	List<User> findUserByTodayDate();

	@Query("FROM User as u WHERE u.userId IN (SELECT ur.id.userId FROM UserRole as ur WHERE ur.id.roleId != 1) and u.onboardingStatus.overallStatus =1")
	List<User> getUserWithOnboardingCompleted();
	
	List<User> findByRegisterType(String registerType);
}
