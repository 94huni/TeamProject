package com.springboot.Teamproject.repository;

import com.springboot.Teamproject.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends JpaRepository<User,String> {
}
