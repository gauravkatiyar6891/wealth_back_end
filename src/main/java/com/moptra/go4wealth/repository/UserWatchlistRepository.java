package com.moptra.go4wealth.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.moptra.go4wealth.bean.UserWatchlist;

public interface UserWatchlistRepository extends JpaRepository<UserWatchlist,Integer>{
	
	@Query("From UserWatchlist as uw WHERE uw.user.userId=:userId AND uw.schemeCode=:schemeCode")
	UserWatchlist findByUserIdAndSchemeCode(@Param("userId")int userId,@Param("schemeCode")String schemeCode);

	@Query("From UserWatchlist as uw WHERE uw.user.userId=:userId AND uw.removedStatus=:removedStatus AND uw.purchasedStatus=:purchasedStatus")
	List<UserWatchlist> findByUserIdAndRemovedStatusAndPurchasedStatus(@Param("userId")int userId,@Param("removedStatus")int removedStatus,@Param("purchasedStatus")int purchasedStatus);

}