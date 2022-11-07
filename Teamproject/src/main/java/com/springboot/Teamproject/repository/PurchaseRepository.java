package com.springboot.Teamproject.repository;

import com.springboot.Teamproject.entity.Purchase;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PurchaseRepository extends JpaRepository<Purchase, Long> {
}
