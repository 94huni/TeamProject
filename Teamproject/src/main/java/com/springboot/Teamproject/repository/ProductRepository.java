package com.springboot.Teamproject.repository;

import com.springboot.Teamproject.entity.Product;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ProductRepository extends JpaRepository<Product, Integer> {
}
