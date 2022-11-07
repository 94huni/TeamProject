package com.springboot.Teamproject.repository;

import com.springboot.Teamproject.entity.Comment;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CommentRepository extends JpaRepository<Comment,Integer> {
}
