package com.moptra.go4wealth.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.moptra.go4wealth.bean.OnboardingStatus;

public interface OnboardingStatusRepository  extends JpaRepository<OnboardingStatus, Integer> {

	OnboardingStatus findByClientCode(String clientCode);
}
