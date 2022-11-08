package com.springboot.Teamproject.repository;

import com.springboot.Teamproject.entity.BlogBoard;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface BlogBoardRepository extends JpaRepository<BlogBoard, Integer> {

    Optional<BlogBoard> findById(int bno);
}
