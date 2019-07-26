package com.moptra.go4wealth.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.moptra.go4wealth.bean.UserAssetItems;

public interface UserAssetItemsRepository extends JpaRepository<UserAssetItems,Integer>{
	
	@Query("from UserAssetItems as uai where uai.goalsOrder.goalsOrderId=:orderId and uai.assetClass.assetClassId=:assetId")
	UserAssetItems findByGoalsOrderIdAndAssetClassId(@Param("orderId")Integer orderId,@Param("assetId")Integer assetId);
	
	@Query("from UserAssetItems as uai where uai.goalsOrder.goalsOrderId=:goalsOrderId")
	List<UserAssetItems> findByGoalsOrderId(@Param("goalsOrderId")Integer goalsOrderId);
	
	List<UserAssetItems> findByGoalsOrderItemId(Integer goalsOrderItemId);

}
