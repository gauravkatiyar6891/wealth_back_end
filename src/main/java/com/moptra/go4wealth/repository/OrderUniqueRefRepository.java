package com.moptra.go4wealth.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.moptra.go4wealth.bean.OrderUniqueRef;

public interface OrderUniqueRefRepository extends JpaRepository<OrderUniqueRef,Integer> {

	OrderUniqueRef findByOrderUniqueRefId(int i);

}
