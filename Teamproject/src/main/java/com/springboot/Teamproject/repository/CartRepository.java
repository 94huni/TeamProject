package com.springboot.Teamproject.repository;

import com.springboot.Teamproject.entity.Cart;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CartRepository extends JpaRepository<Cart, Integer> {
}
