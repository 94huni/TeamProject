package com.springboot.Teamproject.repository;

import com.springboot.Teamproject.entity.BlogBoard;
import org.springframework.data.jpa.repository.JpaRepository;

public interface BlogBoardRepository extends JpaRepository<BlogBoard, Integer> {
}
