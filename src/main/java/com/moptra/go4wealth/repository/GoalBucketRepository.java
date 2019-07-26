package com.moptra.go4wealth.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.moptra.go4wealth.bean.GoalBucket;

public interface GoalBucketRepository extends JpaRepository<GoalBucket, Integer> {

	Optional<GoalBucket> findByGoalBucketCode(String goalBucketCode);
}
